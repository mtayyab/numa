'use client';

import { useState, useEffect } from 'react';
import { 
  ChartBarIcon,
  ClockIcon,
  UsersIcon,
  CurrencyDollarIcon,
  TrendingUpIcon,
  CalendarIcon
} from '@heroicons/react/24/outline';
import { sessionApi } from '@/services/api';
import { toast } from 'react-hot-toast';

interface AnalyticsData {
  totalSessions: number;
  activeSessions: number;
  completedSessions: number;
  totalRevenue: number;
  averageSessionDuration: number;
  averageGuestsPerSession: number;
  averageOrderValue: number;
  totalGuests: number;
  peakHours: Array<{
    hour: number;
    sessionCount: number;
  }>;
  dailyStats: Array<{
    date: string;
    sessions: number;
    revenue: number;
    guests: number;
  }>;
}

interface SessionAnalyticsProps {
  restaurantId: string;
}

export default function SessionAnalytics({ restaurantId }: SessionAnalyticsProps) {
  const [analytics, setAnalytics] = useState<AnalyticsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [timeRange, setTimeRange] = useState<'7d' | '30d' | '90d'>('7d');

  useEffect(() => {
    fetchAnalytics();
  }, [restaurantId, timeRange]);

  const fetchAnalytics = async () => {
    try {
      setLoading(true);
      // TODO: Implement analytics API endpoint
      // const response = await sessionApi.getSessionAnalytics(restaurantId, timeRange);
      // setAnalytics(response);
      
      // Mock data for now
      setAnalytics({
        totalSessions: 156,
        activeSessions: 8,
        completedSessions: 148,
        totalRevenue: 12450.75,
        averageSessionDuration: 85,
        averageGuestsPerSession: 3.2,
        averageOrderValue: 45.30,
        totalGuests: 499,
        peakHours: [
          { hour: 12, sessionCount: 15 },
          { hour: 13, sessionCount: 18 },
          { hour: 19, sessionCount: 22 },
          { hour: 20, sessionCount: 19 },
        ],
        dailyStats: [
          { date: '2024-01-01', sessions: 12, revenue: 540.00, guests: 38 },
          { date: '2024-01-02', sessions: 15, revenue: 680.50, guests: 48 },
          { date: '2024-01-03', sessions: 18, revenue: 820.25, guests: 58 },
          { date: '2024-01-04', sessions: 14, revenue: 650.75, guests: 45 },
          { date: '2024-01-05', sessions: 22, revenue: 980.00, guests: 72 },
        ]
      });
    } catch (error) {
      console.error('Error fetching analytics:', error);
      toast.error('Failed to load analytics');
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  const formatDuration = (minutes: number) => {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    if (hours > 0) {
      return `${hours}h ${mins}m`;
    }
    return `${mins}m`;
  };

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow p-6">
        <div className="animate-pulse">
          <div className="h-6 bg-gray-200 rounded w-1/4 mb-4"></div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            {[1, 2, 3, 4].map((i) => (
              <div key={i} className="h-24 bg-gray-200 rounded"></div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (!analytics) {
    return (
      <div className="bg-white rounded-lg shadow p-6">
        <div className="text-center">
          <ChartBarIcon className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">No analytics data</h3>
          <p className="mt-1 text-sm text-gray-500">Analytics will appear here once you have session data.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow">
      <div className="px-6 py-4 border-b border-gray-200">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <ChartBarIcon className="h-6 w-6 text-gray-400" />
            <h3 className="text-lg font-medium text-gray-900">Session Analytics</h3>
          </div>
          <div className="flex items-center space-x-2">
            <select
              value={timeRange}
              onChange={(e) => setTimeRange(e.target.value as '7d' | '30d' | '90d')}
              className="text-sm border border-gray-300 rounded-md px-3 py-1 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              <option value="7d">Last 7 days</option>
              <option value="30d">Last 30 days</option>
              <option value="90d">Last 90 days</option>
            </select>
          </div>
        </div>
      </div>

      <div className="p-6">
        {/* Key Metrics */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <div className="bg-blue-50 rounded-lg p-4">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <CalendarIcon className="h-8 w-8 text-blue-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-blue-600">Total Sessions</p>
                <p className="text-2xl font-bold text-blue-900">{analytics.totalSessions}</p>
                <p className="text-xs text-blue-500">
                  {analytics.activeSessions} active, {analytics.completedSessions} completed
                </p>
              </div>
            </div>
          </div>

          <div className="bg-green-50 rounded-lg p-4">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <CurrencyDollarIcon className="h-8 w-8 text-green-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-green-600">Total Revenue</p>
                <p className="text-2xl font-bold text-green-900">{formatCurrency(analytics.totalRevenue)}</p>
                <p className="text-xs text-green-500">
                  Avg: {formatCurrency(analytics.averageOrderValue)} per order
                </p>
              </div>
            </div>
          </div>

          <div className="bg-purple-50 rounded-lg p-4">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <UsersIcon className="h-8 w-8 text-purple-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-purple-600">Total Guests</p>
                <p className="text-2xl font-bold text-purple-900">{analytics.totalGuests}</p>
                <p className="text-xs text-purple-500">
                  Avg: {analytics.averageGuestsPerSession} per session
                </p>
              </div>
            </div>
          </div>

          <div className="bg-orange-50 rounded-lg p-4">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <ClockIcon className="h-8 w-8 text-orange-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-orange-600">Avg Duration</p>
                <p className="text-2xl font-bold text-orange-900">{formatDuration(analytics.averageSessionDuration)}</p>
                <p className="text-xs text-orange-500">per session</p>
              </div>
            </div>
          </div>
        </div>

        {/* Peak Hours Chart */}
        <div className="mb-8">
          <h4 className="text-lg font-medium text-gray-900 mb-4">Peak Hours</h4>
          <div className="bg-gray-50 rounded-lg p-4">
            <div className="grid grid-cols-12 gap-2">
              {analytics.peakHours.map((hourData) => (
                <div key={hourData.hour} className="text-center">
                  <div className="text-xs text-gray-500 mb-1">
                    {hourData.hour}:00
                  </div>
                  <div className="bg-blue-200 rounded-t" style={{ height: `${(hourData.sessionCount / Math.max(...analytics.peakHours.map(h => h.sessionCount))) * 100}px` }}>
                    <div className="text-xs text-blue-800 font-medium pt-1">
                      {hourData.sessionCount}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Daily Stats Table */}
        <div>
          <h4 className="text-lg font-medium text-gray-900 mb-4">Daily Performance</h4>
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Date
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Sessions
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Revenue
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Guests
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Avg/Order
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {analytics.dailyStats.map((day, index) => (
                  <tr key={index} className={index % 2 === 0 ? 'bg-white' : 'bg-gray-50'}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {new Date(day.date).toLocaleDateString('en-US', {
                        month: 'short',
                        day: 'numeric'
                      })}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {day.sessions}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {formatCurrency(day.revenue)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {day.guests}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {formatCurrency(day.revenue / day.sessions)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
