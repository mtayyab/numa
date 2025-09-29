'use client';

import { useState } from 'react';
import Image from 'next/image';
import { MenuCategory, MenuItem } from '@/types';
import { ChevronDownIcon, ChevronUpIcon } from '@heroicons/react/24/outline';

interface MenuCategoryListProps {
  categories: MenuCategory[];
  selectedCategory: string | null;
  onCategorySelect: (categoryId: string | null) => void;
  onItemSelect: (item: MenuItem) => void;
  searchTerm: string;
}

export default function MenuCategoryList({
  categories,
  selectedCategory,
  onCategorySelect,
  onItemSelect,
  searchTerm,
}: MenuCategoryListProps) {
  const [expandedCategories, setExpandedCategories] = useState<Set<string>>(
    new Set(categories.map(cat => cat.id))
  );

  const toggleCategory = (categoryId: string) => {
    const newExpanded = new Set(expandedCategories);
    if (newExpanded.has(categoryId)) {
      newExpanded.delete(categoryId);
    } else {
      newExpanded.add(categoryId);
    }
    setExpandedCategories(newExpanded);
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(price);
  };

  const getDietaryBadges = (item: MenuItem) => {
    const badges = [];
    if (item.isVegetarian) badges.push('Vegetarian');
    if (item.isVegan) badges.push('Vegan');
    if (item.isGlutenFree) badges.push('Gluten-Free');
    if (item.isSpicy) badges.push(`Spicy ${item.spiceLevel}/5`);
    return badges;
  };

  return (
    <div className="space-y-6 p-4">
      {categories.map((category) => {
        const isExpanded = expandedCategories.has(category.id);
        const availableItems = category.menuItems?.filter(item => 
          item.isActive && item.isAvailable
        ) || [];

        return (
          <div key={category.id} className="bg-white rounded-lg shadow-sm border border-gray-200">
            {/* Category Header */}
            <button
              onClick={() => toggleCategory(category.id)}
              className="w-full px-4 py-4 flex items-center justify-between text-left hover:bg-gray-50 transition-colors"
            >
              <div className="flex items-center space-x-3">
                {category.imageUrl && (
                  <div className="w-12 h-12 relative rounded-lg overflow-hidden">
                    <Image
                      src={category.imageUrl}
                      alt={category.name}
                      fill
                      className="object-cover"
                    />
                  </div>
                )}
                <div>
                  <h3 className="text-lg font-semibold text-gray-900">
                    {category.name}
                  </h3>
                  {category.description && (
                    <p className="text-sm text-gray-600 mt-1">
                      {category.description}
                    </p>
                  )}
                  <p className="text-xs text-gray-500 mt-1">
                    {availableItems.length} items available
                  </p>
                </div>
              </div>
              {isExpanded ? (
                <ChevronUpIcon className="w-5 h-5 text-gray-400" />
              ) : (
                <ChevronDownIcon className="w-5 h-5 text-gray-400" />
              )}
            </button>

            {/* Menu Items */}
            {isExpanded && (
              <div className="border-t border-gray-200">
                {availableItems.length === 0 ? (
                  <div className="p-4 text-center text-gray-500">
                    No items available in this category
                  </div>
                ) : (
                  <div className="divide-y divide-gray-100">
                    {availableItems.map((item) => (
                      <button
                        key={item.id}
                        onClick={() => onItemSelect(item)}
                        className="w-full p-4 text-left hover:bg-gray-50 transition-colors"
                      >
                        <div className="flex items-start space-x-3">
                          {item.imageUrl && (
                            <div className="w-16 h-16 relative rounded-lg overflow-hidden flex-shrink-0">
                              <Image
                                src={item.imageUrl}
                                alt={item.name}
                                fill
                                className="object-cover"
                              />
                            </div>
                          )}
                          <div className="flex-1 min-w-0">
                            <div className="flex items-start justify-between">
                              <div className="flex-1">
                                <h4 className="text-base font-medium text-gray-900 mb-1">
                                  {item.name}
                                </h4>
                                {item.description && (
                                  <p className="text-sm text-gray-600 mb-2 line-clamp-2">
                                    {item.description}
                                  </p>
                                )}
                                
                                {/* Dietary badges */}
                                {getDietaryBadges(item).length > 0 && (
                                  <div className="flex flex-wrap gap-1 mb-2">
                                    {getDietaryBadges(item).map((badge, index) => (
                                      <span
                                        key={index}
                                        className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800"
                                      >
                                        {badge}
                                      </span>
                                    ))}
                                  </div>
                                )}

                                {/* Additional info */}
                                <div className="flex items-center text-xs text-gray-500 space-x-3">
                                  <span>{item.preparationTimeMinutes} min</span>
                                  {item.calories && <span>{item.calories} cal</span>}
                                  {item.stockQuantity !== undefined && item.stockQuantity <= item.lowStockThreshold && (
                                    <span className="text-warning-600 font-medium">Limited stock</span>
                                  )}
                                </div>
                              </div>
                              <div className="text-right ml-4">
                                <p className="text-lg font-semibold text-gray-900">
                                  {formatPrice(item.price)}
                                </p>
                                {item.variations && item.variations.length > 0 && (
                                  <p className="text-xs text-gray-500">
                                    + variations
                                  </p>
                                )}
                              </div>
                            </div>
                          </div>
                        </div>
                      </button>
                    ))}
                  </div>
                )}
              </div>
            )}
          </div>
        );
      })}

      {categories.length === 0 && (
        <div className="text-center py-12">
          <p className="text-gray-500 text-lg">
            {searchTerm ? 'No items found matching your search.' : 'No menu items available.'}
          </p>
        </div>
      )}
    </div>
  );
}
