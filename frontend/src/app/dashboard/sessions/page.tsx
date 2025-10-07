'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { toast } from 'react-hot-toast';
import { authApi } from '@/services/api';
import DashboardLayout from '@/components/dashboard/DashboardLayout';
import ActiveSessions from '@/components/dashboard/ActiveSessions';

export default function SessionsPage() {
  const router = useRouter();
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const userData = await authApi.getCurrentUser();
        setUser(userData);
      } catch (error) {
        console.error('Error fetching user:', error);
        toast.error('Failed to load user data');
        router.push('/auth/login');
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
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
          <h1 className="text-2xl font-bold text-gray-900">Active Sessions</h1>
          <p className="mt-1 text-sm text-gray-600">
            Monitor and manage your restaurant's active dining sessions.
          </p>
        </div>

        {/* Active Sessions Component */}
        <ActiveSessions restaurantId={user?.restaurantId} />
      </div>
    </DashboardLayout>
  );
}
