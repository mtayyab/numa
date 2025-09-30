'use client';

import { useState } from 'react';
import { XMarkIcon, PlusIcon, MinusIcon } from '@heroicons/react/24/outline';
import { MenuItem, MenuItemVariation, Restaurant } from '@/types';

interface MenuItemModalProps {
  item: MenuItem;
  restaurant: Restaurant;
  onClose: () => void;
  onAddToCart: (item: MenuItem, variation: MenuItemVariation | null, quantity: number, specialInstructions?: string) => void;
}

export default function MenuItemModal({ 
  item, 
  restaurant, 
  onClose, 
  onAddToCart 
}: MenuItemModalProps) {
  const [selectedVariation, setSelectedVariation] = useState<MenuItemVariation | null>(
    item.variations?.find(v => v.isDefault) || null
  );
  const [quantity, setQuantity] = useState(1);
  const [specialInstructions, setSpecialInstructions] = useState('');

  const handleAddToCart = () => {
    onAddToCart(item, selectedVariation, quantity, specialInstructions.trim() || undefined);
  };

  const getItemPrice = () => {
    const basePrice = item.price;
    const variationPrice = selectedVariation?.priceAdjustment || 0;
    return basePrice + variationPrice;
  };

  const getTotalPrice = () => {
    return getItemPrice() * quantity;
  };

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      <div className="flex min-h-screen items-center justify-center p-4">
        <div className="fixed inset-0 bg-black bg-opacity-50" onClick={onClose} />
        
        <div className="relative bg-white rounded-lg shadow-xl max-w-md w-full max-h-[90vh] overflow-y-auto">
          {/* Header */}
          <div className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4">
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-semibold text-gray-900">{item.name}</h3>
              <button
                onClick={onClose}
                className="text-gray-400 hover:text-gray-600 transition-colors"
              >
                <XMarkIcon className="w-6 h-6" />
              </button>
            </div>
          </div>

          {/* Content */}
          <div className="p-6 space-y-6">
            {/* Image */}
            {item.imageUrl && (
              <div className="aspect-video bg-gray-100 rounded-lg overflow-hidden">
                <img
                  src={item.imageUrl}
                  alt={item.name}
                  className="w-full h-full object-cover"
                />
              </div>
            )}

            {/* Description */}
            {item.description && (
              <p className="text-gray-600 text-sm leading-relaxed">{item.description}</p>
            )}

            {/* Dietary Info */}
            <div className="flex flex-wrap gap-2">
              {item.isVegetarian && (
                <span className="px-2 py-1 bg-green-100 text-green-800 text-xs rounded-full">
                  Vegetarian
                </span>
              )}
              {item.isVegan && (
                <span className="px-2 py-1 bg-green-100 text-green-800 text-xs rounded-full">
                  Vegan
                </span>
              )}
              {item.isGlutenFree && (
                <span className="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full">
                  Gluten-Free
                </span>
              )}
              {item.isSpicy && (
                <span className="px-2 py-1 bg-red-100 text-red-800 text-xs rounded-full">
                  Spicy ({item.spiceLevel}/5)
                </span>
              )}
            </div>

            {/* Allergens */}
            {item.allergens && (
              <div>
                <h4 className="text-sm font-medium text-gray-900 mb-1">Allergens</h4>
                <p className="text-sm text-gray-600">{item.allergens}</p>
              </div>
            )}

            {/* Ingredients */}
            {item.ingredients && (
              <div>
                <h4 className="text-sm font-medium text-gray-900 mb-1">Ingredients</h4>
                <p className="text-sm text-gray-600">{item.ingredients}</p>
              </div>
            )}

            {/* Variations */}
            {item.variations && item.variations.length > 0 && (
              <div>
                <h4 className="text-sm font-medium text-gray-900 mb-3">Options</h4>
                <div className="space-y-2">
                  {item.variations.map((variation) => (
                    <label
                      key={variation.id}
                      className="flex items-center justify-between p-3 border border-gray-200 rounded-lg cursor-pointer hover:bg-gray-50"
                    >
                      <div className="flex items-center">
                        <input
                          type="radio"
                          name="variation"
                          value={variation.id}
                          checked={selectedVariation?.id === variation.id}
                          onChange={() => setSelectedVariation(variation)}
                          className="text-primary-600 focus:ring-primary-500"
                        />
                        <div className="ml-3">
                          <div className="text-sm font-medium text-gray-900">
                            {variation.name}
                          </div>
                          {variation.description && (
                            <div className="text-sm text-gray-500">
                              {variation.description}
                            </div>
                          )}
                        </div>
                      </div>
                      <div className="text-sm font-medium text-gray-900">
                        {variation.priceAdjustment > 0 ? `+$${variation.priceAdjustment.toFixed(2)}` : 
                         variation.priceAdjustment < 0 ? `-$${Math.abs(variation.priceAdjustment).toFixed(2)}` : 
                         'Included'}
                      </div>
                    </label>
                  ))}
                </div>
              </div>
            )}

            {/* Special Instructions */}
            <div>
              <label className="block text-sm font-medium text-gray-900 mb-2">
                Special Instructions (Optional)
              </label>
              <textarea
                value={specialInstructions}
                onChange={(e) => setSpecialInstructions(e.target.value)}
                placeholder="e.g., No onions, extra spicy, well done..."
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 resize-none"
                rows={3}
              />
            </div>

            {/* Quantity */}
            <div>
              <label className="block text-sm font-medium text-gray-900 mb-2">
                Quantity
              </label>
              <div className="flex items-center space-x-3">
                <button
                  onClick={() => setQuantity(Math.max(1, quantity - 1))}
                  className="p-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
                >
                  <MinusIcon className="w-4 h-4" />
                </button>
                <span className="text-lg font-medium text-gray-900 min-w-[2rem] text-center">
                  {quantity}
                </span>
                <button
                  onClick={() => setQuantity(quantity + 1)}
                  className="p-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
                >
                  <PlusIcon className="w-4 h-4" />
                </button>
              </div>
            </div>
          </div>

          {/* Footer */}
          <div className="sticky bottom-0 bg-white border-t border-gray-200 p-6">
            <div className="flex items-center justify-between mb-4">
              <div>
                <div className="text-sm text-gray-500">Total</div>
                <div className="text-lg font-semibold text-gray-900">
                  ${getTotalPrice().toFixed(2)}
                </div>
              </div>
              <div className="text-sm text-gray-500">
                ${getItemPrice().toFixed(2)} × {quantity}
              </div>
            </div>
            <button
              onClick={handleAddToCart}
              className="w-full btn-primary"
            >
              Add to Cart - ${getTotalPrice().toFixed(2)}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
