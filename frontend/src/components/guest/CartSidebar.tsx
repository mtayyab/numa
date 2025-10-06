'use client';

import { useState } from 'react';
import { XMarkIcon, PlusIcon, MinusIcon, TrashIcon } from '@heroicons/react/24/outline';
import { CartItem, Restaurant, GuestSessionResponse } from '@/types';

interface CartSidebarProps {
  items: CartItem[];
  restaurant: Restaurant;
  session: GuestSessionResponse | null;
  subtotal: number;
  taxAmount: number;
  serviceCharge: number;
  total: number;
  onClose: () => void;
  onUpdateQuantity: (index: number, quantity: number) => void;
  onRemoveItem: (index: number) => void;
}

export default function CartSidebar({
  items,
  restaurant,
  session,
  subtotal,
  taxAmount,
  serviceCharge,
  total,
  onClose,
  onUpdateQuantity,
  onRemoveItem,
}: CartSidebarProps) {
  const [isCheckingOut, setIsCheckingOut] = useState(false);

  const handleCheckout = async () => {
    setIsCheckingOut(true);
    // TODO: Implement checkout logic
    console.log('Checkout initiated', { items, session });
    // Simulate checkout process
    setTimeout(() => {
      setIsCheckingOut(false);
      onClose();
    }, 2000);
  };

  const getItemPrice = (item: CartItem) => {
    const basePrice = item.menuItem.price;
    const variationPrice = item.variation?.priceAdjustment || 0;
    return basePrice + variationPrice;
  };

  const getItemTotal = (item: CartItem) => {
    return getItemPrice(item) * item.quantity;
  };

  return (
    <div className="fixed inset-0 z-50 overflow-hidden">
      <div className="absolute inset-0 bg-black bg-opacity-50" onClick={onClose} />
      
      <div className="absolute right-0 top-0 h-full w-full max-w-md bg-white shadow-xl">
        <div className="flex h-full flex-col">
          {/* Header */}
          <div className="flex items-center justify-between border-b border-gray-200 px-6 py-4">
            <h2 className="text-lg font-semibold text-gray-900">Your Order</h2>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600 transition-colors"
            >
              <XMarkIcon className="w-6 h-6" />
            </button>
          </div>

          {/* Session Info */}
          {session && (
            <div className="bg-blue-50 border-b border-blue-200 px-6 py-3">
              <div className="text-sm text-blue-800">
                <strong>Table {session.table.tableNumber}</strong> • Session: {session.session.sessionCode}
              </div>
              <div className="text-xs text-blue-600 mt-1">
                {session.session.guestCount} guests in this session
              </div>
            </div>
          )}

          {/* Cart Items */}
          <div className="flex-1 overflow-y-auto px-6 py-4">
            {items.length === 0 ? (
              <div className="text-center py-8">
                <div className="text-gray-400 mb-2">Your cart is empty</div>
                <div className="text-sm text-gray-500">Add some items to get started</div>
              </div>
            ) : (
              <div className="space-y-4">
                {items.map((item, index) => (
                  <div key={`${item.menuItem.id}-${item.variation?.id || 'default'}-${index}`} 
                       className="border border-gray-200 rounded-lg p-4">
                    <div className="flex items-start justify-between mb-2">
                      <div className="flex-1">
                        <h3 className="font-medium text-gray-900">{item.menuItem.name}</h3>
                        {item.variation && (
                          <p className="text-sm text-gray-600">{item.variation.name}</p>
                        )}
                        {item.specialInstructions && (
                          <p className="text-xs text-gray-500 italic">
                            Note: {item.specialInstructions}
                          </p>
                        )}
                        {item.guestName && (
                          <p className="text-xs text-blue-600">
                            Ordered by: {item.guestName}
                          </p>
                        )}
                      </div>
                      <button
                        onClick={() => onRemoveItem(index)}
                        className="text-gray-400 hover:text-red-600 transition-colors ml-2"
                      >
                        <TrashIcon className="w-4 h-4" />
                      </button>
                    </div>
                    
                    <div className="flex items-center justify-between">
                      <div className="flex items-center space-x-2">
                        <button
                          onClick={() => onUpdateQuantity(index, item.quantity - 1)}
                          className="p-1 border border-gray-300 rounded hover:bg-gray-50 transition-colors"
                        >
                          <MinusIcon className="w-3 h-3" />
                        </button>
                        <span className="text-sm font-medium min-w-[2rem] text-center">
                          {item.quantity}
                        </span>
                        <button
                          onClick={() => onUpdateQuantity(index, item.quantity + 1)}
                          className="p-1 border border-gray-300 rounded hover:bg-gray-50 transition-colors"
                        >
                          <PlusIcon className="w-3 h-3" />
                        </button>
                      </div>
                      <div className="text-sm font-medium text-gray-900">
                        ${getItemTotal(item).toFixed(2)}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* Order Summary */}
          {items.length > 0 && (
            <div className="border-t border-gray-200 bg-gray-50 px-6 py-4">
              <div className="space-y-2 text-sm">
                <div className="flex justify-between">
                  <span className="text-gray-600">Subtotal</span>
                  <span className="text-gray-900">${subtotal.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Tax ({restaurant.taxRate * 100}%)</span>
                  <span className="text-gray-900">${taxAmount.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Service Charge ({restaurant.serviceChargeRate * 100}%)</span>
                  <span className="text-gray-900">${serviceCharge.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-base font-semibold border-t border-gray-300 pt-2">
                  <span>Total</span>
                  <span>${total.toFixed(2)}</span>
                </div>
              </div>
            </div>
          )}

          {/* Checkout Button */}
          {items.length > 0 && (
            <div className="border-t border-gray-200 px-6 py-4">
              <button
                onClick={handleCheckout}
                disabled={isCheckingOut}
                className="w-full btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isCheckingOut ? 'Processing...' : `Checkout - $${total.toFixed(2)}`}
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
