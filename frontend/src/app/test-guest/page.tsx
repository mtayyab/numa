'use client';

import { useState, useEffect } from 'react';
import { toast } from 'react-hot-toast';

// Mock data for testing
const mockRestaurant = {
  id: '802fa392-f2f7-44e2-a292-560eef97f1e0',
  name: 'Test Restaurant',
  slug: 'test-restaurant',
  description: 'A wonderful test restaurant',
  email: 'test@restaurant.com',
  phone: '+1234567890',
  addressLine1: '123 Test St',
  city: 'Test City',
  state: 'TS',
  country: 'US',
  currencyCode: 'USD',
  brandColor: '#FF6B35',
  status: 'ACTIVE'
};

const mockTable = {
  id: 'table-123',
  restaurantId: '802fa392-f2f7-44e2-a292-560eef97f1e0',
  tableNumber: 'T1',
  qrCode: 'TEST-QR-CODE-123',
  capacity: 4,
  status: 'AVAILABLE'
};

const mockMenuCategories = [
  {
    id: 'cat-1',
    name: 'Appetizers',
    description: 'Start your meal with our delicious appetizers',
    menuItems: [
      {
        id: 'item-1',
        name: 'Caesar Salad',
        description: 'Fresh romaine lettuce with parmesan cheese and croutons',
        price: 8.99,
        isActive: true,
        isAvailable: true
      },
      {
        id: 'item-2',
        name: 'Buffalo Wings',
        description: 'Spicy chicken wings with blue cheese dip',
        price: 12.99,
        isActive: true,
        isAvailable: true
      }
    ]
  },
  {
    id: 'cat-2',
    name: 'Main Courses',
    description: 'Our signature main dishes',
    menuItems: [
      {
        id: 'item-3',
        name: 'Grilled Salmon',
        description: 'Fresh Atlantic salmon with lemon butter sauce',
        price: 24.99,
        isActive: true,
        isAvailable: true
      },
      {
        id: 'item-4',
        name: 'Beef Burger',
        description: 'Juicy beef patty with lettuce, tomato, and onion',
        price: 16.99,
        isActive: true,
        isAvailable: true
      }
    ]
  },
  {
    id: 'cat-3',
    name: 'Desserts',
    description: 'Sweet endings to your meal',
    menuItems: [
      {
        id: 'item-5',
        name: 'Chocolate Cake',
        description: 'Rich chocolate cake with vanilla ice cream',
        price: 7.99,
        isActive: true,
        isAvailable: true
      },
      {
        id: 'item-6',
        name: 'Tiramisu',
        description: 'Classic Italian dessert with coffee and mascarpone',
        price: 6.99,
        isActive: true,
        isAvailable: true
      }
    ]
  }
];

export default function TestGuestPage() {
  const [restaurant, setRestaurant] = useState(mockRestaurant);
  const [table, setTable] = useState(mockTable);
  const [menuCategories, setMenuCategories] = useState(mockMenuCategories);
  const [cart, setCart] = useState<any[]>([]);
  const [guestName, setGuestName] = useState('');
  const [showSessionJoin, setShowSessionJoin] = useState(true);
  const [currentSession, setCurrentSession] = useState<any>(null);

  const addToCart = (item: any) => {
    const existingItem = cart.find(cartItem => cartItem.menuItemId === item.id);
    if (existingItem) {
      setCart(cart.map(cartItem => 
        cartItem.menuItemId === item.id 
          ? { ...cartItem, quantity: cartItem.quantity + 1 }
          : cartItem
      ));
    } else {
      setCart([...cart, {
        id: `cart-${Date.now()}`,
        menuItemId: item.id,
        name: item.name,
        price: item.price,
        quantity: 1
      }]);
    }
    toast.success(`${item.name} added to cart`);
  };

  const removeFromCart = (cartItemId: string) => {
    setCart(cart.filter(item => item.id !== cartItemId));
    toast.success('Item removed from cart');
  };

  const updateQuantity = (cartItemId: string, quantity: number) => {
    if (quantity <= 0) {
      removeFromCart(cartItemId);
      return;
    }
    setCart(cart.map(item => 
      item.id === cartItemId ? { ...item, quantity } : item
    ));
  };

  const getTotalPrice = () => {
    return cart.reduce((total, item) => total + (item.price * item.quantity), 0);
  };

  const handleSessionJoin = async (sessionCode?: string, newGuestName?: string) => {
    try {
      if (newGuestName) {
        setGuestName(newGuestName);
      }

      if (sessionCode) {
        // Join existing session
        setCurrentSession({
          id: 'session-123',
          table: table,
          currentGuest: { name: newGuestName || guestName }
        });
        toast.success(`Joined session for ${table.tableNumber}`);
      } else {
        // Create new session
        setCurrentSession({
          id: 'session-123',
          table: table,
          currentGuest: { name: newGuestName || guestName }
        });
        toast.success(`Created new session for ${table.tableNumber}`);
      }
      
      setShowSessionJoin(false);
    } catch (error: any) {
      toast.error(error.message || 'Failed to join session');
    }
  };

  const handleCallWaiter = async () => {
    if (!currentSession) return;
    
    try {
      // TODO: Implement real API call to notify waiter
      // For now, just show success message
      toast.success('Waiter has been notified and will be with you shortly!');
      
      // In a real implementation, this would:
      // 1. Send a request to the backend
      // 2. Create a waiter alert in the database
      // 3. The dashboard would show the alert
      // 4. Restaurant staff could acknowledge and complete the request
    } catch (error: any) {
      toast.error(error.message || 'Failed to call waiter');
    }
  };

  const handleSubmitOrder = async () => {
    if (cart.length === 0) {
      toast.error('Your cart is empty');
      return;
    }

    try {
      // Mock order submission
      toast.success('Order submitted successfully!');
      setCart([]);
    } catch (error: any) {
      toast.error(error.message || 'Failed to submit order');
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">{restaurant.name}</h1>
              <p className="text-sm text-gray-600">Table {table.tableNumber}</p>
            </div>
            <div className="flex items-center space-x-4">
              <button
                onClick={handleCallWaiter}
                className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
              >
                Call Waiter
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Menu */}
          <div className="lg:col-span-2">
            <h2 className="text-2xl font-bold text-gray-900 mb-6">Menu</h2>
            
            {menuCategories.map((category) => (
              <div key={category.id} className="mb-8">
                <h3 className="text-xl font-semibold text-gray-900 mb-4">{category.name}</h3>
                <p className="text-gray-600 mb-4">{category.description}</p>
                
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  {category.menuItems.map((item) => (
                    <div key={item.id} className="bg-white rounded-lg shadow-sm border p-4">
                      <div className="flex justify-between items-start mb-2">
                        <h4 className="font-semibold text-gray-900">{item.name}</h4>
                        <span className="text-lg font-bold text-primary-600">${item.price}</span>
                      </div>
                      <p className="text-gray-600 text-sm mb-3">{item.description}</p>
                      <button
                        onClick={() => addToCart(item)}
                        className="w-full bg-primary-600 text-white py-2 px-4 rounded-lg hover:bg-primary-700 transition-colors"
                      >
                        Add to Cart
                      </button>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>

          {/* Cart Sidebar */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow-sm border p-6 sticky top-4">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Your Order</h3>
              
              {cart.length === 0 ? (
                <p className="text-gray-500 text-center py-8">Your cart is empty</p>
              ) : (
                <div className="space-y-3">
                  {cart.map((item) => (
                    <div key={item.id} className="flex justify-between items-center py-2 border-b">
                      <div className="flex-1">
                        <p className="font-medium text-gray-900">{item.name}</p>
                        <p className="text-sm text-gray-600">${item.price}</p>
                      </div>
                      <div className="flex items-center space-x-2">
                        <button
                          onClick={() => updateQuantity(item.id, item.quantity - 1)}
                          className="w-6 h-6 rounded-full bg-gray-200 flex items-center justify-center hover:bg-gray-300"
                        >
                          -
                        </button>
                        <span className="w-8 text-center">{item.quantity}</span>
                        <button
                          onClick={() => updateQuantity(item.id, item.quantity + 1)}
                          className="w-6 h-6 rounded-full bg-gray-200 flex items-center justify-center hover:bg-gray-300"
                        >
                          +
                        </button>
                        <button
                          onClick={() => removeFromCart(item.id)}
                          className="text-red-600 hover:text-red-800 ml-2"
                        >
                          ×
                        </button>
                      </div>
                    </div>
                  ))}
                  
                  <div className="border-t pt-4">
                    <div className="flex justify-between items-center text-lg font-semibold">
                      <span>Total:</span>
                      <span>${getTotalPrice().toFixed(2)}</span>
                    </div>
                    <button
                      onClick={handleSubmitOrder}
                      className="w-full bg-primary-600 text-white py-3 px-4 rounded-lg hover:bg-primary-700 transition-colors mt-4"
                    >
                      Submit Order
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Session Join Modal */}
      {showSessionJoin && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 max-w-md w-full mx-4">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Join Session</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Your Name
                </label>
                <input
                  type="text"
                  value={guestName}
                  onChange={(e) => setGuestName(e.target.value)}
                  className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
                  placeholder="Enter your name"
                />
              </div>
              <div className="flex space-x-3">
                <button
                  onClick={() => handleSessionJoin(undefined, guestName)}
                  className="flex-1 bg-primary-600 text-white py-2 px-4 rounded-lg hover:bg-primary-700 transition-colors"
                >
                  Start New Session
                </button>
                <button
                  onClick={() => setShowSessionJoin(false)}
                  className="flex-1 bg-gray-300 text-gray-700 py-2 px-4 rounded-lg hover:bg-gray-400 transition-colors"
                >
                  Cancel
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
