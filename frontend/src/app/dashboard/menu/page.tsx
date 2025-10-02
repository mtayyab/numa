'use client';

import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { authApi, menuApi } from '@/services/api';

export default function MenuManagementPage() {
  const router = useRouter();
  const [menuCategories, setMenuCategories] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [showAddCategory, setShowAddCategory] = useState(false);
  const [showEditCategory, setShowEditCategory] = useState(false);
  const [showAddItem, setShowAddItem] = useState(false);
  const [showEditItem, setShowEditItem] = useState(false);
  const [newCategory, setNewCategory] = useState({ name: '', description: '' });
  const [editCategory, setEditCategory] = useState({ id: '', name: '', description: '' });
  const [newItem, setNewItem] = useState({ name: '', description: '', price: '', categoryId: '' });
  const [editItem, setEditItem] = useState({ id: '', name: '', description: '', price: '', categoryId: '' });
  const [selectedCategoryId, setSelectedCategoryId] = useState('');
  const [menuItems, setMenuItems] = useState<any[]>([]);

  useEffect(() => {
    const fetchMenuData = async () => {
      try {
        // Check if user is logged in
        const token = localStorage.getItem('numa_access_token');
        if (!token) {
          toast.error('Please login to access this page');
          router.push('/auth/login');
          return;
        }

        // Get current user to get restaurant ID
        const user = await authApi.getCurrentUser();
        if (!user.restaurantId) {
          toast.error('No restaurant associated with this account');
          router.push('/dashboard');
          return;
        }

        // Fetch menu categories from API
        const categories = await menuApi.getCategories(user.restaurantId);
        setMenuCategories(categories);

        // Fetch all menu items
        const items = await menuApi.getItems(user.restaurantId);
        setMenuItems(items);
      } catch (error: any) {
        console.error('Error fetching menu data:', error);
        toast.error(error.message || 'Failed to load menu data');
        router.push('/dashboard');
      } finally {
        setLoading(false);
      }
    };

    fetchMenuData();
  }, [router]);

  const handleAddCategory = async () => {
    try {
      if (!newCategory.name.trim()) {
        toast.error('Category name is required');
        return;
      }

      // Get current user to get restaurant ID
      const user = await authApi.getCurrentUser();
      if (!user.restaurantId) {
        toast.error('No restaurant associated with this account');
        return;
      }

      const categoryData = {
        name: newCategory.name,
        description: newCategory.description,
        sortOrder: (menuCategories?.length || 0) + 1,
        isActive: true
      };

      const newCategoryData = await menuApi.createCategory(user.restaurantId, categoryData);
      setMenuCategories([...(menuCategories || []), newCategoryData]);
      setNewCategory({ name: '', description: '' });
      setShowAddCategory(false);
      toast.success('Category added successfully!');
    } catch (error: any) {
      console.error('Error adding category:', error);
      toast.error(error.message || 'Failed to add category');
    }
  };

  const handleEditCategory = async () => {
    try {
      if (!editCategory.name.trim()) {
        toast.error('Category name is required');
        return;
      }

      // Get current user to get restaurant ID
      const user = await authApi.getCurrentUser();
      if (!user.restaurantId) {
        toast.error('No restaurant associated with this account');
        return;
      }

      const categoryData = {
        name: editCategory.name,
        description: editCategory.description,
        isActive: true
      };

      const updatedCategory = await menuApi.updateCategory(user.restaurantId, editCategory.id, categoryData);
      setMenuCategories(menuCategories.map(cat => 
        cat.id === editCategory.id ? updatedCategory : cat
      ));
      setEditCategory({ id: '', name: '', description: '' });
      setShowEditCategory(false);
      toast.success('Category updated successfully!');
    } catch (error: any) {
      console.error('Error updating category:', error);
      toast.error(error.message || 'Failed to update category');
    }
  };

  const handleDeleteCategory = async (categoryId: string) => {
    if (!confirm('Are you sure you want to delete this category? This will also delete all items in this category.')) {
      return;
    }

    try {
      // Get current user to get restaurant ID
      const user = await authApi.getCurrentUser();
      if (!user.restaurantId) {
        toast.error('No restaurant associated with this account');
        return;
      }

      await menuApi.deleteCategory(user.restaurantId, categoryId);
      setMenuCategories(menuCategories.filter(cat => cat.id !== categoryId));
      toast.success('Category deleted successfully!');
    } catch (error: any) {
      console.error('Error deleting category:', error);
      toast.error(error.message || 'Failed to delete category');
    }
  };

  const handleAddItem = async () => {
    try {
      if (!newItem.name.trim()) {
        toast.error('Item name is required');
        return;
      }
      if (!newItem.price || parseFloat(newItem.price) <= 0) {
        toast.error('Valid price is required');
        return;
      }

      // Get current user to get restaurant ID
      const user = await authApi.getCurrentUser();
      if (!user.restaurantId) {
        toast.error('No restaurant associated with this account');
        return;
      }

      const itemData = {
        name: newItem.name,
        description: newItem.description,
        price: parseFloat(newItem.price),
        isActive: true,
        isAvailable: true
      };

      const newItemData = await menuApi.createItem(user.restaurantId, newItem.categoryId, itemData);
      setMenuItems([...menuItems, newItemData]);
      setNewItem({ name: '', description: '', price: '', categoryId: '' });
      setShowAddItem(false);
      toast.success('Menu item added successfully!');
    } catch (error: any) {
      console.error('Error adding item:', error);
      toast.error(error.message || 'Failed to add menu item');
    }
  };

  const handleEditItem = async () => {
    try {
      if (!editItem.name.trim()) {
        toast.error('Item name is required');
        return;
      }
      if (!editItem.price || parseFloat(editItem.price) <= 0) {
        toast.error('Valid price is required');
        return;
      }

      // Get current user to get restaurant ID
      const user = await authApi.getCurrentUser();
      if (!user.restaurantId) {
        toast.error('No restaurant associated with this account');
        return;
      }

      const itemData = {
        name: editItem.name,
        description: editItem.description,
        price: parseFloat(editItem.price),
        isActive: true,
        isAvailable: true
      };

      const updatedItem = await menuApi.updateItem(user.restaurantId, editItem.id, itemData);
      setMenuItems(menuItems.map(item => 
        item.id === editItem.id ? updatedItem : item
      ));
      setEditItem({ id: '', name: '', description: '', price: '', categoryId: '' });
      setShowEditItem(false);
      toast.success('Menu item updated successfully!');
    } catch (error: any) {
      console.error('Error updating item:', error);
      toast.error(error.message || 'Failed to update menu item');
    }
  };

  const handleDeleteItem = async (itemId: string) => {
    if (!confirm('Are you sure you want to delete this menu item?')) {
      return;
    }

    try {
      // Get current user to get restaurant ID
      const user = await authApi.getCurrentUser();
      if (!user.restaurantId) {
        toast.error('No restaurant associated with this account');
        return;
      }

      await menuApi.deleteItem(user.restaurantId, itemId);
      setMenuItems(menuItems.filter(item => item.id !== itemId));
      toast.success('Menu item deleted successfully!');
    } catch (error: any) {
      console.error('Error deleting item:', error);
      toast.error(error.message || 'Failed to delete menu item');
    }
  };

  const fetchMenuItems = async (categoryId: string) => {
    try {
      const user = await authApi.getCurrentUser();
      if (!user.restaurantId) {
        return;
      }

      const items = await menuApi.getItems(user.restaurantId, categoryId);
      setMenuItems(items);
    } catch (error: any) {
      console.error('Error fetching menu items:', error);
      toast.error(error.message || 'Failed to load menu items');
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div className="flex items-center">
              <button
                onClick={() => router.back()}
                className="mr-4 text-gray-500 hover:text-gray-700"
              >
                ← Back
              </button>
              <h1 className="text-3xl font-bold text-gray-900">Menu Management</h1>
            </div>
            <div className="flex items-center space-x-4">
              <button
                onClick={() => setShowAddCategory(true)}
                className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
              >
                Add Category
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          {/* Add Category Modal */}
          {showAddCategory && (
            <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
              <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
                <div className="mt-3">
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Add New Category</h3>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Category Name</label>
                      <input
                        type="text"
                        value={newCategory.name}
                        onChange={(e) => setNewCategory({...newCategory, name: e.target.value})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="e.g., Appetizers"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Description</label>
                      <textarea
                        value={newCategory.description}
                        onChange={(e) => setNewCategory({...newCategory, description: e.target.value})}
                        rows={3}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="Brief description of the category"
                      />
                    </div>
                  </div>
                  <div className="flex justify-end space-x-3 mt-6">
                    <button
                      onClick={() => setShowAddCategory(false)}
                      className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Cancel
                    </button>
                    <button
                      onClick={handleAddCategory}
                      className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Add Category
                    </button>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Edit Category Modal */}
          {showEditCategory && (
            <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
              <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
                <div className="mt-3">
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Edit Category</h3>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Category Name</label>
                      <input
                        type="text"
                        value={editCategory.name}
                        onChange={(e) => setEditCategory({...editCategory, name: e.target.value})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="e.g., Appetizers"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Description</label>
                      <textarea
                        value={editCategory.description}
                        onChange={(e) => setEditCategory({...editCategory, description: e.target.value})}
                        rows={3}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="Brief description of the category"
                      />
                    </div>
                  </div>
                  <div className="flex justify-end space-x-3 mt-6">
                    <button
                      onClick={() => setShowEditCategory(false)}
                      className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Cancel
                    </button>
                    <button
                      onClick={handleEditCategory}
                      className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Update Category
                    </button>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Add Item Modal */}
          {showAddItem && (
            <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
              <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
                <div className="mt-3">
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Add Menu Item</h3>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Item Name</label>
                      <input
                        type="text"
                        value={newItem.name}
                        onChange={(e) => setNewItem({...newItem, name: e.target.value})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="e.g., Caesar Salad"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Description</label>
                      <textarea
                        value={newItem.description}
                        onChange={(e) => setNewItem({...newItem, description: e.target.value})}
                        rows={3}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="Brief description of the item"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Price</label>
                      <input
                        type="number"
                        step="0.01"
                        min="0"
                        value={newItem.price}
                        onChange={(e) => setNewItem({...newItem, price: e.target.value})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="0.00"
                      />
                    </div>
                  </div>
                  <div className="flex justify-end space-x-3 mt-6">
                    <button
                      onClick={() => setShowAddItem(false)}
                      className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Cancel
                    </button>
                    <button
                      onClick={handleAddItem}
                      className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Add Item
                    </button>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Edit Item Modal */}
          {showEditItem && (
            <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
              <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
                <div className="mt-3">
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Edit Menu Item</h3>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Item Name</label>
                      <input
                        type="text"
                        value={editItem.name}
                        onChange={(e) => setEditItem({...editItem, name: e.target.value})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="e.g., Caesar Salad"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Description</label>
                      <textarea
                        value={editItem.description}
                        onChange={(e) => setEditItem({...editItem, description: e.target.value})}
                        rows={3}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="Brief description of the item"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Price</label>
                      <input
                        type="number"
                        step="0.01"
                        min="0"
                        value={editItem.price}
                        onChange={(e) => setEditItem({...editItem, price: e.target.value})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="0.00"
                      />
                    </div>
                  </div>
                  <div className="flex justify-end space-x-3 mt-6">
                    <button
                      onClick={() => setShowEditItem(false)}
                      className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Cancel
                    </button>
                    <button
                      onClick={handleEditItem}
                      className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Update Item
                    </button>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Menu Categories */}
          <div className="space-y-6">
            {menuCategories && menuCategories.map((category) => (
              <div key={category.id} className="bg-white shadow rounded-lg">
                <div className="px-4 py-5 sm:p-6">
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="text-lg font-medium text-gray-900">{category.name}</h3>
                      <p className="mt-1 text-sm text-gray-600">{category.description}</p>
                    </div>
                    <div className="flex space-x-2">
                      <button 
                        onClick={() => {
                          setEditCategory({ id: category.id, name: category.name, description: category.description || '' });
                          setShowEditCategory(true);
                        }}
                        className="text-primary-600 hover:text-primary-700 text-sm font-medium"
                      >
                        Edit
                      </button>
                      <button 
                        onClick={() => handleDeleteCategory(category.id)}
                        className="text-red-600 hover:text-red-700 text-sm font-medium"
                      >
                        Delete
                      </button>
                    </div>
                  </div>
                  
                  {/* Menu Items */}
                  <div className="mt-6">
                    <div className="flex justify-between items-center mb-4">
                      <h4 className="text-md font-medium text-gray-900">Menu Items</h4>
                      <button 
                        onClick={() => {
                          setNewItem({ name: '', description: '', price: '', categoryId: category.id });
                          setShowAddItem(true);
                        }}
                        className="bg-green-600 hover:bg-green-700 text-white px-3 py-1 rounded-md text-sm font-medium"
                      >
                        Add Item
                      </button>
                    </div>
                    
                    {menuItems.filter(item => item.categoryId === category.id).length > 0 ? (
                      <div className="space-y-3">
                        {menuItems.filter(item => item.categoryId === category.id).map((item) => (
                          <div key={item.id} className="flex justify-between items-center p-3 bg-gray-50 rounded-md">
                            <div>
                              <h5 className="font-medium text-gray-900">{item.name}</h5>
                              <p className="text-sm text-gray-600">{item.description}</p>
                            </div>
                            <div className="flex items-center space-x-4">
                              <span className="text-lg font-semibold text-gray-900">${item.price}</span>
                              <div className="flex space-x-2">
                                <button 
                                  onClick={() => {
                                    setEditItem({ 
                                      id: item.id, 
                                      name: item.name, 
                                      description: item.description || '', 
                                      price: item.price.toString(), 
                                      categoryId: category.id 
                                    });
                                    setShowEditItem(true);
                                  }}
                                  className="text-primary-600 hover:text-primary-700 text-sm"
                                >
                                  Edit
                                </button>
                                <button 
                                  onClick={() => handleDeleteItem(item.id)}
                                  className="text-red-600 hover:text-red-700 text-sm"
                                >
                                  Delete
                                </button>
                              </div>
                            </div>
                          </div>
                        ))}
                      </div>
                    ) : (
                      <div className="text-center py-8 text-gray-500">
                        <p>No menu items in this category yet.</p>
                        <button 
                          onClick={() => {
                            setNewItem({ name: '', description: '', price: '', categoryId: category.id });
                            setShowAddItem(true);
                          }}
                          className="mt-2 text-primary-600 hover:text-primary-700 text-sm font-medium"
                        >
                          Add your first item
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>

          {(!menuCategories || menuCategories.length === 0) && (
            <div className="text-center py-12">
              <h3 className="text-lg font-medium text-gray-900 mb-2">No menu categories yet</h3>
              <p className="text-gray-600 mb-4">Start by adding your first menu category.</p>
              <button
                onClick={() => setShowAddCategory(true)}
                className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
              >
                Add Your First Category
              </button>
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
