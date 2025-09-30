'use client';

import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';

export default function MenuManagementPage() {
  const router = useRouter();
  const [menuCategories, setMenuCategories] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [showAddCategory, setShowAddCategory] = useState(false);
  const [newCategory, setNewCategory] = useState({ name: '', description: '' });

  useEffect(() => {
    // Check if user is logged in
    const token = localStorage.getItem('numa_access_token');
    if (!token) {
      toast.error('Please login to access this page');
      router.push('/auth/login');
      return;
    }

    // TODO: Fetch menu data from API
    // For now, just set mock data
    setMenuCategories([
      {
        id: 1,
        name: 'Appetizers',
        description: 'Start your meal with our delicious appetizers',
        items: [
          { id: 1, name: 'Caesar Salad', price: 8.99, description: 'Fresh romaine lettuce with caesar dressing' },
          { id: 2, name: 'Buffalo Wings', price: 12.99, description: 'Spicy buffalo wings with ranch dip' }
        ]
      },
      {
        id: 2,
        name: 'Main Courses',
        description: 'Our signature main dishes',
        items: [
          { id: 3, name: 'Grilled Salmon', price: 24.99, description: 'Fresh Atlantic salmon with lemon butter' },
          { id: 4, name: 'Ribeye Steak', price: 32.99, description: '12oz ribeye steak cooked to perfection' }
        ]
      }
    ]);
    setLoading(false);
  }, [router]);

  const handleAddCategory = () => {
    if (!newCategory.name.trim()) {
      toast.error('Category name is required');
      return;
    }

    const category = {
      id: Date.now(),
      name: newCategory.name,
      description: newCategory.description,
      items: []
    };

    setMenuCategories([...menuCategories, category]);
    setNewCategory({ name: '', description: '' });
    setShowAddCategory(false);
    toast.success('Category added successfully!');
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

          {/* Menu Categories */}
          <div className="space-y-6">
            {menuCategories.map((category) => (
              <div key={category.id} className="bg-white shadow rounded-lg">
                <div className="px-4 py-5 sm:p-6">
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="text-lg font-medium text-gray-900">{category.name}</h3>
                      <p className="mt-1 text-sm text-gray-600">{category.description}</p>
                    </div>
                    <div className="flex space-x-2">
                      <button className="text-primary-600 hover:text-primary-700 text-sm font-medium">
                        Edit
                      </button>
                      <button className="text-red-600 hover:text-red-700 text-sm font-medium">
                        Delete
                      </button>
                    </div>
                  </div>
                  
                  {/* Menu Items */}
                  <div className="mt-6">
                    <div className="flex justify-between items-center mb-4">
                      <h4 className="text-md font-medium text-gray-900">Menu Items</h4>
                      <button className="bg-green-600 hover:bg-green-700 text-white px-3 py-1 rounded-md text-sm font-medium">
                        Add Item
                      </button>
                    </div>
                    
                    {category.items.length > 0 ? (
                      <div className="space-y-3">
                        {category.items.map((item) => (
                          <div key={item.id} className="flex justify-between items-center p-3 bg-gray-50 rounded-md">
                            <div>
                              <h5 className="font-medium text-gray-900">{item.name}</h5>
                              <p className="text-sm text-gray-600">{item.description}</p>
                            </div>
                            <div className="flex items-center space-x-4">
                              <span className="text-lg font-semibold text-gray-900">${item.price}</span>
                              <div className="flex space-x-2">
                                <button className="text-primary-600 hover:text-primary-700 text-sm">
                                  Edit
                                </button>
                                <button className="text-red-600 hover:text-red-700 text-sm">
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
                        <button className="mt-2 text-primary-600 hover:text-primary-700 text-sm font-medium">
                          Add your first item
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>

          {menuCategories.length === 0 && (
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
