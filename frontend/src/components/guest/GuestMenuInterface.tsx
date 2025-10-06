'use client';

import { useState, useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { 
  ShoppingCartIcon, 
  UserGroupIcon, 
  HandRaisedIcon,
  MagnifyingGlassIcon,
  AdjustmentsHorizontalIcon 
} from '@heroicons/react/24/outline';
import { Restaurant, RestaurantTable, MenuCategory, MenuItem, CartItem } from '@/types';
import { menuApi, guestApi } from '@/services/api';
import MenuCategoryList from './MenuCategoryList';
import MenuItemModal from './MenuItemModal';
import CartSidebar from './CartSidebar';
import SessionJoinModal from './SessionJoinModal';
import LoadingSpinner from '@/components/ui/LoadingSpinner';
import ErrorMessage from '@/components/ui/ErrorMessage';
import toast from 'react-hot-toast';

interface GuestMenuInterfaceProps {
  restaurantSlug: string;
  qrCode: string;
}

export default function GuestMenuInterface({ 
  restaurantSlug,
  qrCode 
}: GuestMenuInterfaceProps) {
  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);
  const [table, setTable] = useState<RestaurantTable | null>(null);
  const [selectedItem, setSelectedItem] = useState<MenuItem | null>(null);
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [showCart, setShowCart] = useState(false);
  const [showSessionJoin, setShowSessionJoin] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [dietaryFilters, setDietaryFilters] = useState({
    vegetarian: false,
    vegan: false,
    glutenFree: false,
  });
  const [currentSession, setCurrentSession] = useState<any>(null);
  const [guestName, setGuestName] = useState('');

  // Fetch restaurant and table data
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [restaurantData, tableData] = await Promise.all([
          guestApi.getRestaurantBySlug(restaurantSlug),
          guestApi.getTableByQrCode(qrCode),
        ]);
        
        // Verify the table belongs to the restaurant
        if (tableData.restaurantId !== restaurantData.id) {
          toast.error('Table does not belong to this restaurant');
          return;
        }
        
        setRestaurant(restaurantData);
        setTable(tableData);
      } catch (error) {
        console.error('Error fetching restaurant/table data:', error);
        toast.error('Failed to load restaurant data');
      }
    };

    fetchData();
  }, [restaurantSlug, qrCode]);

  // Fetch menu data
  const { data: menuCategories, isLoading, error } = useQuery({
    queryKey: ['menu', restaurantSlug],
    queryFn: () => menuApi.getPublicMenu(restaurantSlug),
    staleTime: 5 * 60 * 1000, // 5 minutes
    enabled: !!restaurant, // Only fetch when restaurant is loaded
  });

  // Filter menu items based on search and dietary preferences
  const filteredCategories = menuCategories?.filter((category: MenuCategory) => {
    if (selectedCategory && category.id !== selectedCategory) return false;
    
    const hasMatchingItems = category.menuItems?.some((item: MenuItem) => {
      const matchesSearch = !searchTerm || 
        item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        item.description?.toLowerCase().includes(searchTerm.toLowerCase());
      
      const matchesDietary = 
        (!dietaryFilters.vegetarian || item.isVegetarian) &&
        (!dietaryFilters.vegan || item.isVegan) &&
        (!dietaryFilters.glutenFree || item.isGlutenFree);
      
      return matchesSearch && matchesDietary && item.isActive && item.isAvailable;
    });

    return hasMatchingItems;
  });

  // Calculate cart totals (moved after loading check)

  // Handle adding item to cart
  const addToCart = (item: MenuItem, variation: any, quantity: number, specialInstructions?: string) => {
    const cartItem: CartItem = {
      menuItem: item,
      variation,
      quantity,
      specialInstructions,
      guestName: guestName || 'Guest',
    };

    setCartItems(prev => {
      const existingIndex = prev.findIndex(cartItem => 
        cartItem.menuItem.id === item.id && 
        cartItem.variation?.id === variation?.id &&
        cartItem.specialInstructions === specialInstructions
      );

      if (existingIndex >= 0) {
        const updated = [...prev];
        updated[existingIndex].quantity += quantity;
        return updated;
      } else {
        return [...prev, cartItem];
      }
    });

    setSelectedItem(null);
    toast.success(`Added ${item.name} to cart`);
  };

  // Handle removing item from cart
  const removeFromCart = (index: number) => {
    setCartItems(prev => prev.filter((_, i) => i !== index));
  };

  // Handle updating cart item quantity
  const updateCartItemQuantity = (index: number, quantity: number) => {
    if (quantity <= 0) {
      removeFromCart(index);
      return;
    }

    setCartItems(prev => {
      const updated = [...prev];
      updated[index].quantity = quantity;
      return updated;
    });
  };

  // Handle joining or creating session
  const handleSessionJoin = async (sessionCode?: string, newGuestName?: string) => {
    try {
      if (newGuestName) {
        setGuestName(newGuestName);
      }

      if (sessionCode) {
        // Join existing session
        const session = await guestApi.getSession(sessionCode);
        const guestData = await guestApi.joinSession({
          tableQrCode: qrCode,
          guestName: newGuestName || guestName,
          restaurantId: restaurant.id
        });
        setCurrentSession({ ...session, currentGuest: guestData });
        toast.success(`Joined session for ${session.table.tableNumber}`);
      } else {
        // Create new session
        const session = await guestApi.joinSession({
          tableQrCode: qrCode,
          guestName: newGuestName || guestName,
          restaurantId: restaurant.id
        });
        setCurrentSession(session);
        toast.success(`Created new session for ${table.tableNumber}`);
      }
      
      setShowSessionJoin(false);
    } catch (error: any) {
      toast.error(error.message || 'Failed to join session');
    }
  };

  // Handle calling waiter
  const handleCallWaiter = async () => {
    if (!currentSession) return;
    
    try {
      // TODO: Implement waiter call functionality
      toast.success('Waiter has been notified');
    } catch (error: any) {
      toast.error(error.message || 'Failed to call waiter');
    }
  };

  // Show session join modal on mount
  useEffect(() => {
    setShowSessionJoin(true);
  }, []);

  // Show loading state while fetching restaurant/table data
  if (!restaurant || !table) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <LoadingSpinner />
      </div>
    );
  }

  // Calculate cart totals (after loading check)
  const cartSubtotal = cartItems.reduce((total, item) => {
    const itemPrice = item.variation 
      ? item.menuItem.price + item.variation.priceAdjustment 
      : item.menuItem.price;
    return total + (itemPrice * item.quantity);
  }, 0);

  const taxAmount = cartSubtotal * restaurant.taxRate;
  const serviceCharge = cartSubtotal * restaurant.serviceChargeRate;
  const cartTotal = cartSubtotal + taxAmount + serviceCharge;

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center p-4">
        <ErrorMessage 
          message="Failed to load menu. Please try again." 
          onRetry={() => window.location.reload()}
        />
      </div>
    );
  }

  return (
    <>
      {/* Header */}
      <div className="sticky top-0 z-40 bg-white shadow-sm border-b border-gray-200">
        <div className="px-4 py-4">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h1 className="text-xl font-bold text-gray-900">{restaurant.name}</h1>
              <p className="text-sm text-gray-600">Table {table.tableNumber}</p>
            </div>
            <div className="flex items-center space-x-2">
              <button
                onClick={handleCallWaiter}
                disabled={!currentSession}
                className="p-2 text-gray-600 hover:text-primary-600 hover:bg-primary-50 rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                title="Call Waiter"
              >
                <HandRaisedIcon className="w-6 h-6" />
              </button>
              <button
                onClick={() => setShowCart(true)}
                className="relative p-2 text-gray-600 hover:text-primary-600 hover:bg-primary-50 rounded-lg transition-colors"
              >
                <ShoppingCartIcon className="w-6 h-6" />
                {cartItems.length > 0 && (
                  <span className="absolute -top-1 -right-1 bg-primary-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                    {cartItems.reduce((total, item) => total + item.quantity, 0)}
                  </span>
                )}
              </button>
            </div>
          </div>

          {/* Search and Filters */}
          <div className="space-y-3">
            <div className="relative">
              <MagnifyingGlassIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="text"
                placeholder="Search menu items..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
              />
            </div>

            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-2 text-sm">
                <AdjustmentsHorizontalIcon className="w-4 h-4 text-gray-400" />
                <label className="flex items-center">
                  <input
                    type="checkbox"
                    checked={dietaryFilters.vegetarian}
                    onChange={(e) => setDietaryFilters(prev => ({ ...prev, vegetarian: e.target.checked }))}
                    className="rounded border-gray-300 text-primary-600 focus:ring-primary-500"
                  />
                  <span className="ml-1 text-gray-600">Vegetarian</span>
                </label>
                <label className="flex items-center">
                  <input
                    type="checkbox"
                    checked={dietaryFilters.vegan}
                    onChange={(e) => setDietaryFilters(prev => ({ ...prev, vegan: e.target.checked }))}
                    className="rounded border-gray-300 text-primary-600 focus:ring-primary-500"
                  />
                  <span className="ml-1 text-gray-600">Vegan</span>
                </label>
                <label className="flex items-center">
                  <input
                    type="checkbox"
                    checked={dietaryFilters.glutenFree}
                    onChange={(e) => setDietaryFilters(prev => ({ ...prev, glutenFree: e.target.checked }))}
                    className="rounded border-gray-300 text-primary-600 focus:ring-primary-500"
                  />
                  <span className="ml-1 text-gray-600">Gluten-Free</span>
                </label>
              </div>

              {currentSession && (
                <div className="flex items-center text-sm text-gray-600">
                  <UserGroupIcon className="w-4 h-4 mr-1" />
                  <span>{currentSession.guestCount} guests</span>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Menu Content */}
      <div className="pb-20">
        <MenuCategoryList
          categories={filteredCategories || []}
          selectedCategory={selectedCategory}
          onCategorySelect={setSelectedCategory}
          onItemSelect={setSelectedItem}
          searchTerm={searchTerm}
        />
      </div>

      {/* Fixed Bottom Bar */}
      {cartItems.length > 0 && (
        <div className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 p-4 z-30">
          <button
            onClick={() => setShowCart(true)}
            className="w-full btn-primary flex items-center justify-between"
          >
            <span>View Cart ({cartItems.length} items)</span>
            <span>${cartTotal.toFixed(2)}</span>
          </button>
        </div>
      )}

      {/* Modals */}
      {selectedItem && (
        <MenuItemModal
          item={selectedItem}
          restaurant={restaurant}
          onClose={() => setSelectedItem(null)}
          onAddToCart={addToCart}
        />
      )}

      {showCart && (
        <CartSidebar
          items={cartItems}
          restaurant={restaurant}
          session={currentSession}
          subtotal={cartSubtotal}
          taxAmount={taxAmount}
          serviceCharge={serviceCharge}
          total={cartTotal}
          onClose={() => setShowCart(false)}
          onUpdateQuantity={updateCartItemQuantity}
          onRemoveItem={removeFromCart}
        />
      )}

      {showSessionJoin && (
        <SessionJoinModal
          table={table}
          onJoinSession={handleSessionJoin}
          onClose={() => setShowSessionJoin(false)}
        />
      )}
    </>
  );
}
