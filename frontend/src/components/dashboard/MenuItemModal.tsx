'use client';

import { useState, useEffect } from 'react';
import { XMarkIcon } from '@heroicons/react/24/outline';

interface MenuItemModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSave: (itemData: any) => void;
  item?: any;
  category: any;
  isEditing: boolean;
}

export default function MenuItemModal({ isOpen, onClose, onSave, item, category, isEditing }: MenuItemModalProps) {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: 0,
    imageUrl: '',
    sortOrder: 0,
    isActive: true,
    isAvailable: true,
    isVegetarian: false,
    isVegan: false,
    isGlutenFree: false,
    isSpicy: false,
    spiceLevel: 1,
    allergens: [] as string[]
  });

  const [saving, setSaving] = useState(false);
  const [newAllergen, setNewAllergen] = useState('');

  const commonAllergens = [
    'Nuts', 'Dairy', 'Eggs', 'Soy', 'Wheat', 'Fish', 'Shellfish', 'Sesame'
  ];

  useEffect(() => {
    if (isOpen) {
      if (isEditing && item) {
        setFormData({
          name: item.name || '',
          description: item.description || '',
          price: item.price || 0,
          imageUrl: item.imageUrl || '',
          sortOrder: item.sortOrder || 0,
          isActive: item.isActive !== undefined ? item.isActive : true,
          isAvailable: item.isAvailable !== undefined ? item.isAvailable : true,
          isVegetarian: item.isVegetarian || false,
          isVegan: item.isVegan || false,
          isGlutenFree: item.isGlutenFree || false,
          isSpicy: item.isSpicy || false,
          spiceLevel: item.spiceLevel || 1,
          allergens: (item.allergens && typeof item.allergens === 'string') 
            ? item.allergens.split(', ').filter(a => a.trim()) 
            : []
        });
      } else {
        setFormData({
          name: '',
          description: '',
          price: 0,
          imageUrl: '',
          sortOrder: 0,
          isActive: true,
          isAvailable: true,
          isVegetarian: false,
          isVegan: false,
          isGlutenFree: false,
          isSpicy: false,
          spiceLevel: 1,
          allergens: []
        });
      }
    }
  }, [isOpen, isEditing, item]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);

    try {
      // Convert allergens array to string for backend
      const dataToSave = {
        ...formData,
        allergens: formData.allergens.join(', ')
      };
      await onSave(dataToSave);
      onClose();
    } catch (error) {
      console.error('Error saving item:', error);
    } finally {
      setSaving(false);
    }
  };

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        setFormData({ ...formData, imageUrl: event.target?.result as string });
      };
      reader.readAsDataURL(file);
    }
  };

  const addAllergen = (allergen: string) => {
    if (allergen && !formData.allergens.includes(allergen)) {
      setFormData({ ...formData, allergens: [...formData.allergens, allergen] });
    }
  };

  const removeAllergen = (allergen: string) => {
    setFormData({ 
      ...formData, 
      allergens: formData.allergens.filter(a => a !== allergen) 
    });
  };

  const handleAddCustomAllergen = () => {
    if (newAllergen.trim() && !formData.allergens.includes(newAllergen.trim())) {
      addAllergen(newAllergen.trim());
      setNewAllergen('');
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      <div className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" onClick={onClose}></div>

        <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-2xl sm:w-full">
          <form onSubmit={handleSubmit}>
            <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <div className="flex items-center justify-between mb-4">
                <h3 className="text-lg font-medium text-gray-900">
                  {isEditing ? 'Edit Menu Item' : 'Create New Menu Item'}
                </h3>
                <button
                  type="button"
                  onClick={onClose}
                  className="text-gray-400 hover:text-gray-600"
                >
                  <XMarkIcon className="h-6 w-6" />
                </button>
              </div>

              <div className="text-sm text-gray-600 mb-4">
                Category: <span className="font-medium">{category.name}</span>
              </div>

              <div className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Item Name *
                    </label>
                    <input
                      type="text"
                      required
                      value={formData.name}
                      onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                      placeholder="e.g., Grilled Chicken"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Price (PKR) *
                    </label>
                    <input
                      type="number"
                      required
                      min="0"
                      step="0.01"
                      value={formData.price}
                      onChange={(e) => setFormData({ ...formData, price: parseFloat(e.target.value) || 0 })}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                      placeholder="0.00"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Description
                  </label>
                  <textarea
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    rows={3}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                    placeholder="Describe the item, ingredients, and preparation"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Item Image
                  </label>
                  <div className="flex items-center space-x-4">
                    {formData.imageUrl ? (
                      <img
                        src={formData.imageUrl}
                        alt="Item preview"
                        className="w-20 h-20 rounded-lg object-cover border border-gray-300"
                      />
                    ) : (
                      <div className="w-20 h-20 rounded-lg border-2 border-dashed border-gray-300 flex items-center justify-center">
                        <span className="text-gray-400 text-xs">No Image</span>
                      </div>
                    )}
                    <div>
                      <input
                        type="file"
                        accept="image/*"
                        onChange={handleImageUpload}
                        className="hidden"
                        id="item-image-upload"
                      />
                      <label
                        htmlFor="item-image-upload"
                        className="cursor-pointer bg-primary-600 text-white px-3 py-1.5 rounded-md hover:bg-primary-700 text-sm"
                      >
                        Upload Image
                      </label>
                      <p className="text-xs text-gray-500 mt-1">
                        Recommended: 400x400px
                      </p>
                    </div>
                  </div>
                </div>

                <div className="grid grid-cols-3 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Sort Order
                    </label>
                    <input
                      type="number"
                      value={formData.sortOrder}
                      onChange={(e) => setFormData({ ...formData, sortOrder: parseInt(e.target.value) || 0 })}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                      placeholder="0"
                    />
                  </div>

                  <div className="flex items-center">
                    <input
                      type="checkbox"
                      id="isActive"
                      checked={formData.isActive}
                      onChange={(e) => setFormData({ ...formData, isActive: e.target.checked })}
                      className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
                    />
                    <label htmlFor="isActive" className="ml-2 block text-sm text-gray-900">
                      Active
                    </label>
                  </div>

                  <div className="flex items-center">
                    <input
                      type="checkbox"
                      id="isAvailable"
                      checked={formData.isAvailable}
                      onChange={(e) => setFormData({ ...formData, isAvailable: e.target.checked })}
                      className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
                    />
                    <label htmlFor="isAvailable" className="ml-2 block text-sm text-gray-900">
                      Available
                    </label>
                  </div>
                </div>

                {/* Dietary Preferences */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Dietary Preferences
                  </label>
                  <div className="grid grid-cols-2 gap-4">
                    <div className="flex items-center">
                      <input
                        type="checkbox"
                        id="isVegetarian"
                        checked={formData.isVegetarian}
                        onChange={(e) => setFormData({ ...formData, isVegetarian: e.target.checked })}
                        className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
                      />
                      <label htmlFor="isVegetarian" className="ml-2 block text-sm text-gray-900">
                        Vegetarian
                      </label>
                    </div>

                    <div className="flex items-center">
                      <input
                        type="checkbox"
                        id="isVegan"
                        checked={formData.isVegan}
                        onChange={(e) => setFormData({ ...formData, isVegan: e.target.checked })}
                        className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
                      />
                      <label htmlFor="isVegan" className="ml-2 block text-sm text-gray-900">
                        Vegan
                      </label>
                    </div>

                    <div className="flex items-center">
                      <input
                        type="checkbox"
                        id="isGlutenFree"
                        checked={formData.isGlutenFree}
                        onChange={(e) => setFormData({ ...formData, isGlutenFree: e.target.checked })}
                        className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
                    />
                      <label htmlFor="isGlutenFree" className="ml-2 block text-sm text-gray-900">
                        Gluten Free
                      </label>
                    </div>

                    <div className="flex items-center">
                      <input
                        type="checkbox"
                        id="isSpicy"
                        checked={formData.isSpicy}
                        onChange={(e) => setFormData({ ...formData, isSpicy: e.target.checked })}
                        className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
                      />
                      <label htmlFor="isSpicy" className="ml-2 block text-sm text-gray-900">
                        Spicy
                      </label>
                    </div>
                  </div>

                  {formData.isSpicy && (
                    <div className="mt-3">
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Spice Level (1-5)
                      </label>
                      <input
                        type="range"
                        min="1"
                        max="5"
                        value={formData.spiceLevel}
                        onChange={(e) => setFormData({ ...formData, spiceLevel: parseInt(e.target.value) })}
                        className="w-full"
                      />
                      <div className="flex justify-between text-xs text-gray-500 mt-1">
                        <span>Mild</span>
                        <span>Very Hot</span>
                      </div>
                    </div>
                  )}
                </div>

                {/* Allergens */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Allergens
                  </label>
                  
                  <div className="flex flex-wrap gap-2 mb-3">
                    {commonAllergens.map((allergen) => (
                      <button
                        key={allergen}
                        type="button"
                        onClick={() => 
                          formData.allergens.includes(allergen) 
                            ? removeAllergen(allergen) 
                            : addAllergen(allergen)
                        }
                        className={`px-3 py-1 rounded-full text-xs font-medium ${
                          formData.allergens.includes(allergen)
                            ? 'bg-red-100 text-red-800 border border-red-200'
                            : 'bg-gray-100 text-gray-800 border border-gray-200 hover:bg-gray-200'
                        }`}
                      >
                        {allergen}
                      </button>
                    ))}
                  </div>

                  <div className="flex space-x-2">
                    <input
                      type="text"
                      value={newAllergen}
                      onChange={(e) => setNewAllergen(e.target.value)}
                      placeholder="Add custom allergen"
                      className="flex-1 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                    />
                    <button
                      type="button"
                      onClick={handleAddCustomAllergen}
                      className="px-3 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 text-sm"
                    >
                      Add
                    </button>
                  </div>

                  {formData.allergens.length > 0 && (
                    <div className="mt-2">
                      <p className="text-sm text-gray-600 mb-1">Selected allergens:</p>
                      <div className="flex flex-wrap gap-1">
                        {formData.allergens.map((allergen) => (
                          <span
                            key={allergen}
                            className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-red-100 text-red-800"
                          >
                            {allergen}
                            <button
                              type="button"
                              onClick={() => removeAllergen(allergen)}
                              className="ml-1 text-red-600 hover:text-red-800"
                            >
                              ×
                            </button>
                          </span>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </div>

            <div className="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button
                type="submit"
                disabled={saving}
                className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-primary-600 text-base font-medium text-white hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50"
              >
                {saving ? 'Saving...' : (isEditing ? 'Update Item' : 'Create Item')}
              </button>
              <button
                type="button"
                onClick={onClose}
                className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
