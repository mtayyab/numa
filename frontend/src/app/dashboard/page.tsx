'use client';

import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { authApi } from '@/services/api';
import DashboardLayout from '@/components/dashboard/DashboardLayout';
import WaiterAlerts from '@/components/dashboard/WaiterAlerts';
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

  if (loading) {
    return (
      <DashboardLayout>
        <div className="animate-pulse">
          <div className="h-8 bg-gray-200 rounded w-1/4 mb-6"></div>
          <div className="space-y-4">
            <div className="h-32 bg-gray-200 rounded"></div>
            <div className="h-32 bg-gray-200 rounded"></div>
          </div>
        </div>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
          <p className="mt-1 text-sm text-gray-600">
            Welcome back, {user?.firstName} {user?.lastName}! Here's an overview of your restaurant.
          </p>
        </div>

        {/* Waiter Alerts Section */}
        <div>
          <WaiterAlerts />
        </div>

        {/* Session Analytics Section */}
        <div>
          <SessionAnalytics restaurantId={user?.restaurantId} />
        </div>

        {/* Quick Actions */}
        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <h3 className="text-lg font-medium text-gray-900">Quick Actions</h3>
          </div>
          <div className="p-6">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="text-center">
                <div className="bg-primary-100 rounded-full w-12 h-12 flex items-center justify-center mx-auto mb-4">
                  <span className="text-primary-600 text-xl">📊</span>
                </div>
                <h4 className="text-lg font-semibold text-gray-900 mb-2">Active Sessions</h4>
                <p className="text-gray-600 mb-4">Monitor current dining sessions</p>
                <button
                  onClick={() => router.push('/dashboard/sessions')}
                  className="bg-primary-600 text-white px-4 py-2 rounded-md hover:bg-primary-700 text-sm"
                >
                  View Sessions
                </button>
              </div>
              
              <div className="text-center">
                <div className="bg-green-100 rounded-full w-12 h-12 flex items-center justify-center mx-auto mb-4">
                  <span className="text-green-600 text-xl">🪑</span>
                </div>
                <h4 className="text-lg font-semibold text-gray-900 mb-2">Tables</h4>
                <p className="text-gray-600 mb-4">Manage tables and QR codes</p>
                <button
                  onClick={() => router.push('/dashboard/tables')}
                  className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 text-sm"
                >
                  Manage Tables
                </button>
              </div>
              
              <div className="text-center">
                <div className="bg-blue-100 rounded-full w-12 h-12 flex items-center justify-center mx-auto mb-4">
                  <span className="text-blue-600 text-xl">⚙️</span>
                </div>
                <h4 className="text-lg font-semibold text-gray-900 mb-2">Settings</h4>
                <p className="text-gray-600 mb-4">Configure restaurant settings</p>
                <button
                  onClick={() => router.push('/dashboard/settings')}
                  className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 text-sm"
                >
                  Open Settings
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}