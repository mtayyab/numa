'use client';

import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { authApi } from '@/services/api';
import WaiterAlerts from '@/components/dashboard/WaiterAlerts';
import ActiveSessions from '@/components/dashboard/ActiveSessions';
import SessionHistory from '@/components/dashboard/SessionHistory';
import SessionAnalytics from '@/components/dashboard/SessionAnalytics';

export default function DashboardPage() {
  const router = useRouter();
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        // Small delay to ensure tokens are stored after login redirect
        await new Promise(resolve => setTimeout(resolve, 100));
        
        // Check if user is logged in
        const token = localStorage.getItem('numa_access_token');
        if (!token) {
          console.log('No access token found, redirecting to login');
          toast.error('Please login to access the dashboard');
          router.push('/auth/login');
          return;
        }

        console.log('Access token found, fetching user data...');
        // Fetch user data from API
        const userData = await authApi.getCurrentUser();
        console.log('User data fetched successfully:', userData);
        setUser(userData);
      } catch (error: any) {
        console.error('Error fetching user data:', error);
        // If it's a 401 error, clear tokens and redirect to login
        if (error.response?.status === 401) {
          console.log('401 error - clearing tokens and redirecting to login');
          localStorage.removeItem('numa_access_token');
          localStorage.removeItem('numa_refresh_token');
          toast.error('Session expired. Please login again.');
          router.push('/auth/login');
        } else {
          toast.error(error.message || 'Failed to load user data');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
  }, [router]);

  const handleLogout = () => {
    localStorage.removeItem('numa_access_token');
    localStorage.removeItem('numa_refresh_token');
    toast.success('Logged out successfully');
    router.push('/auth/login');
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
              <h1 className="text-3xl font-bold text-gray-900">Numa Dashboard</h1>
            </div>
            <div className="flex items-center space-x-4">
              <span className="text-sm text-gray-700">
                Welcome, {user?.name}
              </span>
              <button
                onClick={handleLogout}
                className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-md text-sm font-medium"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          {/* Waiter Alerts Section */}
          <div className="mb-8">
            <WaiterAlerts />
          </div>

          {/* Active Sessions Section */}
          <div className="mb-8">
            <ActiveSessions restaurantId={user?.restaurantId} />
          </div>

          {/* Session History Section */}
          <div className="mb-8">
            <SessionHistory restaurantId={user?.restaurantId} />
          </div>

          {/* Session Analytics Section */}
          <div className="mb-8">
            <SessionAnalytics restaurantId={user?.restaurantId} />
          </div>

          <div className="border-4 border-dashed border-gray-200 rounded-lg p-8">
            <div className="text-center">
              <h2 className="text-2xl font-bold text-gray-900 mb-4">
                Welcome to your Restaurant Dashboard
              </h2>
              <p className="text-gray-600 mb-8">
                Manage your restaurant, orders, and customer experience from here.
              </p>
              
              {/* Quick Actions */}
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 max-w-6xl mx-auto">
                <div className="bg-white p-6 rounded-lg shadow-md">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">Restaurant Info</h3>
                  <p className="text-gray-600 mb-4">Update your restaurant details and settings</p>
                  <button 
                    onClick={() => router.push('/dashboard/restaurant')}
                    className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                  >
                    Manage Restaurant
                  </button>
                </div>
                
                <div className="bg-white p-6 rounded-lg shadow-md">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">Menu Management</h3>
                  <p className="text-gray-600 mb-4">Add and manage your menu items</p>
                  <button 
                    onClick={() => router.push('/dashboard/menu')}
                    className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                  >
                    Manage Menu
                  </button>
                </div>
                
                <div className="bg-white p-6 rounded-lg shadow-md">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">Orders</h3>
                  <p className="text-gray-600 mb-4">View and manage customer orders</p>
                  <button 
                    onClick={() => router.push('/dashboard/orders')}
                    className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                  >
                    View Orders
                  </button>
                </div>
                
                <div className="bg-white p-6 rounded-lg shadow-md">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">Tables & QR Codes</h3>
                  <p className="text-gray-600 mb-4">Manage restaurant tables and generate QR codes</p>
                  <button 
                    onClick={() => router.push('/dashboard/tables')}
                    className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                  >
                    Manage Tables
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
