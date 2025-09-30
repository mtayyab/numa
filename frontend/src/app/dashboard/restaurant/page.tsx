'use client';

import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { authApi, restaurantApi } from '@/services/api';

export default function RestaurantManagementPage() {
  const router = useRouter();
  const [restaurant, setRestaurant] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    const fetchRestaurantData = async () => {
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

        // Fetch restaurant data from API
        const restaurantData = await restaurantApi.getById(user.restaurantId);
        setRestaurant({
          id: restaurantData.id,
          name: restaurantData.name,
          email: restaurantData.email,
          phone: restaurantData.phone,
          address: `${restaurantData.addressLine1}, ${restaurantData.city}, ${restaurantData.state} ${restaurantData.postalCode}`,
          status: restaurantData.status,
          currency: restaurantData.currencyCode,
          timezone: restaurantData.timezone,
          description: restaurantData.description,
          brandColor: restaurantData.brandColor
        });
      } catch (error: any) {
        console.error('Error fetching restaurant data:', error);
        toast.error(error.message || 'Failed to load restaurant data');
        router.push('/dashboard');
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurantData();
  }, [router]);

  const handleSave = async () => {
    try {
      if (!restaurant?.id) {
        toast.error('No restaurant data to save');
        return;
      }

      // Parse address back into components
      const addressParts = restaurant.address.split(', ');
      const updateData = {
        name: restaurant.name,
        email: restaurant.email,
        phone: restaurant.phone,
        addressLine1: addressParts[0] || '',
        city: addressParts[1] || '',
        state: addressParts[2]?.split(' ')[0] || '',
        postalCode: addressParts[2]?.split(' ')[1] || '',
        status: restaurant.status,
        currencyCode: restaurant.currency,
        timezone: restaurant.timezone,
        description: restaurant.description,
        brandColor: restaurant.brandColor
      };

      await restaurantApi.update(restaurant.id, updateData);
      toast.success('Restaurant information updated successfully!');
      setIsEditing(false);
    } catch (error: any) {
      console.error('Error updating restaurant:', error);
      toast.error(error.message || 'Failed to update restaurant information');
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
              <h1 className="text-3xl font-bold text-gray-900">Restaurant Management</h1>
            </div>
            <div className="flex items-center space-x-4">
              {!isEditing ? (
                <button
                  onClick={() => setIsEditing(true)}
                  className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                >
                  Edit Restaurant
                </button>
              ) : (
                <div className="flex space-x-2">
                  <button
                    onClick={() => setIsEditing(false)}
                    className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md text-sm font-medium"
                  >
                    Cancel
                  </button>
                  <button
                    onClick={handleSave}
                    className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                  >
                    Save Changes
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="bg-white shadow rounded-lg">
            <div className="px-4 py-5 sm:p-6">
              <h3 className="text-lg leading-6 font-medium text-gray-900 mb-6">
                Restaurant Information
              </h3>
              
              <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Restaurant Name</label>
                  <input
                    type="text"
                    value={restaurant?.name || ''}
                    disabled={!isEditing}
                    onChange={(e) => setRestaurant({...restaurant, name: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm disabled:bg-gray-100"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Email</label>
                  <input
                    type="email"
                    value={restaurant?.email || ''}
                    disabled={!isEditing}
                    onChange={(e) => setRestaurant({...restaurant, email: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm disabled:bg-gray-100"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Phone</label>
                  <input
                    type="tel"
                    value={restaurant?.phone || ''}
                    disabled={!isEditing}
                    onChange={(e) => setRestaurant({...restaurant, phone: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm disabled:bg-gray-100"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Status</label>
                  <select
                    value={restaurant?.status || ''}
                    disabled={!isEditing}
                    onChange={(e) => setRestaurant({...restaurant, status: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm disabled:bg-gray-100"
                  >
                    <option value="ACTIVE">Active</option>
                    <option value="INACTIVE">Inactive</option>
                    <option value="PENDING_APPROVAL">Pending Approval</option>
                  </select>
                </div>

                <div className="sm:col-span-2">
                  <label className="block text-sm font-medium text-gray-700">
                    Address</label>
                  <textarea
                    value={restaurant?.address || ''}
                    disabled={!isEditing}
                    onChange={(e) => setRestaurant({...restaurant, address: e.target.value})}
                    rows={3}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm disabled:bg-gray-100"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Currency</label>
                  <select
                    value={restaurant?.currency || ''}
                    disabled={!isEditing}
                    onChange={(e) => setRestaurant({...restaurant, currency: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm disabled:bg-gray-100"
                  >
                    <option value="USD">USD</option>
                    <option value="EUR">EUR</option>
                    <option value="GBP">GBP</option>
                    <option value="CAD">CAD</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Timezone</label>
                  <select
                    value={restaurant?.timezone || ''}
                    disabled={!isEditing}
                    onChange={(e) => setRestaurant({...restaurant, timezone: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm disabled:bg-gray-100"
                  >
                    <option value="America/New_York">Eastern Time</option>
                    <option value="America/Chicago">Central Time</option>
                    <option value="America/Denver">Mountain Time</option>
                    <option value="America/Los_Angeles">Pacific Time</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
