'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { toast } from 'react-hot-toast';
import { authApi, menuApi } from '@/services/api';
import DashboardLayout from '@/components/dashboard/DashboardLayout';
import CategoryModal from '@/components/dashboard/CategoryModal';
import MenuItemModal from '@/components/dashboard/MenuItemModal';
import {
  PlusIcon,
  PencilIcon,
  TrashIcon,
  EyeIcon,
  EyeSlashIcon,
  DocumentTextIcon
} from '@heroicons/react/24/outline';

interface MenuCategory {
  id: string;
  name: string;
  description: string;
  imageUrl?: string;
  sortOrder: number;
  isActive: boolean;
  availableFrom?: string;
  availableUntil?: string;
  menuItems: MenuItem[];
}

interface MenuItem {
  id: string;
  name: string;
  description: string;
  price: number;
  imageUrl?: string;
  sortOrder: number;
  isActive: boolean;
  isAvailable: boolean;
  isVegetarian: boolean;
  isVegan: boolean;
  isGlutenFree: boolean;
  isSpicy: boolean;
  spiceLevel?: number;
  allergens: string[];
}

export default function MenuManagementPage() {
  const router = useRouter();
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [categories, setCategories] = useState<MenuCategory[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<MenuCategory | null>(null);
  const [showCategoryModal, setShowCategoryModal] = useState(false);
  const [showItemModal, setShowItemModal] = useState(false);
  const [editingCategory, setEditingCategory] = useState<MenuCategory | null>(null);
  const [editingItem, setEditingItem] = useState<MenuItem | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userData = await authApi.getCurrentUser();
        setUser(userData);
        
        // Fetch menu categories with items
        const categoriesData = await menuApi.getCategories(userData.restaurantId);
        setCategories(categoriesData || []);
      } catch (error) {
        console.error('Error fetching data:', error);
        toast.error('Failed to load menu data');
        router.push('/auth/login');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [router]);

  const handleCreateCategory = () => {
    setEditingCategory(null);
    setShowCategoryModal(true);
  };

  const handleEditCategory = (category: MenuCategory) => {
    setEditingCategory(category);
    setShowCategoryModal(true);
  };

  const handleCreateItem = (category: MenuCategory) => {
    setSelectedCategory(category);
    setEditingItem(null);
    setShowItemModal(true);
  };

  const handleEditItem = (item: MenuItem, category: MenuCategory) => {
    setSelectedCategory(category);
    setEditingItem(item);
    setShowItemModal(true);
  };

  const handleDeleteCategory = async (categoryId: string) => {
    if (!confirm('Are you sure you want to delete this category? This will also delete all items in this category.')) {
      return;
    }

    try {
      await menuApi.deleteCategory(user.restaurantId, categoryId);
      toast.success('Category deleted successfully');
      
      // Refresh categories
      const categoriesData = await menuApi.getCategories(user.restaurantId);
      setCategories(categoriesData || []);
    } catch (error) {
      console.error('Error deleting category:', error);
      toast.error('Failed to delete category');
    }
  };

  const handleDeleteItem = async (itemId: string) => {
    if (!confirm('Are you sure you want to delete this item?')) {
      return;
    }

    try {
      await menuApi.deleteItem(user.restaurantId, itemId);
      toast.success('Item deleted successfully');
      
      // Refresh categories
      const categoriesData = await menuApi.getCategories(user.restaurantId);
      setCategories(categoriesData || []);
    } catch (error) {
      console.error('Error deleting item:', error);
      toast.error('Failed to delete item');
    }
  };

  const handleSaveCategory = async (categoryData: any) => {
    try {
      if (editingCategory) {
        await menuApi.updateCategory(user.restaurantId, editingCategory.id, categoryData);
        toast.success('Category updated successfully');
      } else {
        await menuApi.createCategory(user.restaurantId, categoryData);
        toast.success('Category created successfully');
      }
      setShowCategoryModal(false);
      
      // Refresh categories
      const categoriesData = await menuApi.getCategories(user.restaurantId);
      setCategories(categoriesData || []);
    } catch (error) {
      console.error('Error saving category:', error);
      toast.error('Failed to save category');
    }
  };

  const handleSaveItem = async (itemData: any) => {
    try {
      if (editingItem) {
        await menuApi.updateItem(user.restaurantId, editingItem.id, itemData);
        toast.success('Item updated successfully');
      } else {
        await menuApi.createItem(user.restaurantId, selectedCategory.id, itemData);
        toast.success('Item created successfully');
      }
      setShowItemModal(false);
      
      // Refresh categories
      const categoriesData = await menuApi.getCategories(user.restaurantId);
      setCategories(categoriesData || []);
    } catch (error) {
      console.error('Error saving item:', error);
      toast.error('Failed to save item');
    }
  };

  const toggleCategoryStatus = async (category: MenuCategory) => {
    try {
      const updatedData = { ...category, isActive: !category.isActive };
      await menuApi.updateCategory(user.restaurantId, category.id, updatedData);
      toast.success(`Category ${category.isActive ? 'deactivated' : 'activated'} successfully`);
      
      // Refresh categories
      const categoriesData = await menuApi.getCategories(user.restaurantId);
      setCategories(categoriesData || []);
    } catch (error) {
      console.error('Error toggling category status:', error);
      toast.error('Failed to update category status');
    }
  };

  const toggleItemStatus = async (item: MenuItem) => {
    try {
      const updatedData = { ...item, isActive: !item.isActive };
      await menuApi.updateItem(user.restaurantId, item.id, updatedData);
      toast.success(`Item ${item.isActive ? 'deactivated' : 'activated'} successfully`);
      
      // Refresh categories
      const categoriesData = await menuApi.getCategories(user.restaurantId);
      setCategories(categoriesData || []);
    } catch (error) {
      console.error('Error toggling item status:', error);
      toast.error('Failed to update item status');
    }
  };

  if (loading) {
    return (
      <DashboardLayout>
        <div className="animate-pulse">
          <div className="h-8 bg-gray-200 rounded w-1/4 mb-6"></div>
          <div className="space-y-4">
            <div className="h-32 bg-gray-200 rounded"></div>
            <div className="h-32 bg-gray-200 rounded"></div>
          </div>
        </div>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Menu Management</h1>
            <p className="mt-1 text-sm text-gray-600">
              Manage your restaurant's menu categories and items.
            </p>
          </div>
          <button
            onClick={handleCreateCategory}
            className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
          >
            <PlusIcon className="h-4 w-4 mr-2" />
            Add Category
          </button>
        </div>

        {/* Categories List */}
        <div className="space-y-6">
          {categories.length === 0 ? (
            <div className="text-center py-12">
              <DocumentTextIcon className="mx-auto h-12 w-12 text-gray-400" />
              <h3 className="mt-2 text-sm font-medium text-gray-900">No categories</h3>
              <p className="mt-1 text-sm text-gray-500">Get started by creating your first menu category.</p>
              <div className="mt-6">
                <button
                  onClick={handleCreateCategory}
                  className="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700"
                >
                  <PlusIcon className="h-4 w-4 mr-2" />
                  Add Category
                </button>
              </div>
            </div>
          ) : (
            categories.map((category) => (
              <div key={category.id} className="bg-white shadow rounded-lg">
                <div className="px-6 py-4 border-b border-gray-200">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-3">
                      <h3 className="text-lg font-medium text-gray-900">{category.name}</h3>
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                        category.isActive 
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-red-100 text-red-800'
                      }`}>
                        {category.isActive ? 'Active' : 'Inactive'}
                      </span>
                    </div>
                    <div className="flex items-center space-x-2">
                      <button
                        onClick={() => toggleCategoryStatus(category)}
                        className={`p-2 rounded-md ${
                          category.isActive 
                            ? 'text-red-600 hover:bg-red-50' 
                            : 'text-green-600 hover:bg-green-50'
                        }`}
                        title={category.isActive ? 'Deactivate' : 'Activate'}
                      >
                        {category.isActive ? (
                          <EyeSlashIcon className="h-4 w-4" />
                        ) : (
                          <EyeIcon className="h-4 w-4" />
                        )}
                      </button>
                      <button
                        onClick={() => handleEditCategory(category)}
                        className="p-2 text-gray-400 hover:text-gray-600 rounded-md hover:bg-gray-50"
                        title="Edit Category"
                      >
                        <PencilIcon className="h-4 w-4" />
                      </button>
                      <button
                        onClick={() => handleDeleteCategory(category.id)}
                        className="p-2 text-red-400 hover:text-red-600 rounded-md hover:bg-red-50"
                        title="Delete Category"
                      >
                        <TrashIcon className="h-4 w-4" />
                      </button>
                    </div>
                  </div>
                  {category.description && (
                    <p className="mt-2 text-sm text-gray-600">{category.description}</p>
                  )}
                </div>

                {/* Menu Items */}
                <div className="px-6 py-4">
                  <div className="flex items-center justify-between mb-4">
                    <h4 className="text-sm font-medium text-gray-900">Menu Items</h4>
                    <button
                      onClick={() => handleCreateItem(category)}
                      className="inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded text-primary-700 bg-primary-100 hover:bg-primary-200"
                    >
                      <PlusIcon className="h-3 w-3 mr-1" />
                      Add Item
                    </button>
                  </div>

                  {category.menuItems && category.menuItems.length > 0 ? (
                    <div className="space-y-3">
                      {category.menuItems.map((item) => (
                        <div key={item.id} className="flex items-center justify-between p-3 border border-gray-200 rounded-lg">
                          <div className="flex-1">
                            <div className="flex items-center space-x-3">
                              <h5 className="text-sm font-medium text-gray-900">{item.name}</h5>
                              <span className={`inline-flex items-center px-2 py-0.5 rounded text-xs font-medium ${
                                item.isActive 
                                  ? 'bg-green-100 text-green-800' 
                                  : 'bg-red-100 text-red-800'
                              }`}>
                                {item.isActive ? 'Active' : 'Inactive'}
                              </span>
                              {!item.isAvailable && (
                                <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-yellow-100 text-yellow-800">
                                  Out of Stock
                                </span>
                              )}
                            </div>
                            <p className="text-sm text-gray-600 mt-1">{item.description}</p>
                            <div className="flex items-center space-x-4 mt-2">
                              <span className="text-sm font-medium text-gray-900">₨{item.price.toFixed(2)}</span>
                              {item.isVegetarian && (
                                <span className="text-xs text-green-600">Vegetarian</span>
                              )}
                              {item.isVegan && (
                                <span className="text-xs text-green-600">Vegan</span>
                              )}
                              {item.isGlutenFree && (
                                <span className="text-xs text-blue-600">Gluten Free</span>
                              )}
                              {item.isSpicy && (
                                <span className="text-xs text-red-600">Spicy {item.spiceLevel}/5</span>
                              )}
                            </div>
                          </div>
                          <div className="flex items-center space-x-2">
                            <button
                              onClick={() => toggleItemStatus(item)}
                              className={`p-1.5 rounded ${
                                item.isActive 
                                  ? 'text-red-600 hover:bg-red-50' 
                                  : 'text-green-600 hover:bg-green-50'
                              }`}
                              title={item.isActive ? 'Deactivate' : 'Activate'}
                            >
                              {item.isActive ? (
                                <EyeSlashIcon className="h-4 w-4" />
                              ) : (
                                <EyeIcon className="h-4 w-4" />
                              )}
                            </button>
                            <button
                              onClick={() => handleEditItem(item, category)}
                              className="p-1.5 text-gray-400 hover:text-gray-600 rounded hover:bg-gray-50"
                              title="Edit Item"
                            >
                              <PencilIcon className="h-4 w-4" />
                            </button>
                            <button
                              onClick={() => handleDeleteItem(item.id)}
                              className="p-1.5 text-red-400 hover:text-red-600 rounded hover:bg-red-50"
                              title="Delete Item"
                            >
                              <TrashIcon className="h-4 w-4" />
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>
                  ) : (
                    <div className="text-center py-6">
                      <p className="text-sm text-gray-500">No items in this category yet.</p>
                      <button
                        onClick={() => handleCreateItem(category)}
                        className="mt-2 inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded text-primary-700 bg-primary-100 hover:bg-primary-200"
                      >
                        <PlusIcon className="h-3 w-3 mr-1" />
                        Add First Item
                      </button>
                    </div>
                  )}
                </div>
              </div>
            ))
          )}
        </div>

        {/* Category Modal */}
        <CategoryModal
          isOpen={showCategoryModal}
          onClose={() => setShowCategoryModal(false)}
          onSave={handleSaveCategory}
          category={editingCategory}
          isEditing={!!editingCategory}
        />

        {/* Menu Item Modal */}
        <MenuItemModal
          isOpen={showItemModal}
          onClose={() => setShowItemModal(false)}
          onSave={handleSaveItem}
          item={editingItem}
          category={selectedCategory}
          isEditing={!!editingItem}
        />
      </div>
    </DashboardLayout>
  );
}