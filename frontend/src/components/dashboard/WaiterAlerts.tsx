'use client';

import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { authApi } from '@/services/api';

interface WaiterAlert {
  id: string;
  tableNumber: string;
  tableId: string;
  message: string;
  status: 'PENDING' | 'ACKNOWLEDGED' | 'COMPLETED';
  createdAt: string;
  acknowledgedAt?: string;
  completedAt?: string;
}

export default function WaiterAlerts() {
  const [alerts, setAlerts] = useState<WaiterAlert[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAlerts = async () => {
      try {
        // Get current user to get restaurant ID
        const user = await authApi.getCurrentUser();
        if (!user.restaurantId) {
          console.log('No restaurant ID found for user');
          return;
        }

        // TODO: Implement real API call to fetch waiter alerts
        // For now, using mock data
        setAlerts([
          {
            id: '1',
            tableNumber: 'T-05',
            tableId: 'table-5',
            message: 'Customer needs assistance',
            status: 'PENDING',
            createdAt: new Date().toISOString()
          },
          {
            id: '2',
            tableNumber: 'T-12',
            tableId: 'table-12',
            message: 'Ready to order',
            status: 'ACKNOWLEDGED',
            createdAt: new Date(Date.now() - 5 * 60 * 1000).toISOString(),
            acknowledgedAt: new Date(Date.now() - 2 * 60 * 1000).toISOString()
          }
        ]);
      } catch (error: any) {
        console.error('Error fetching waiter alerts:', error);
        // If it's a 401 error, don't show error toast as it's handled by parent
        if (error.response?.status !== 401) {
          toast.error(error.message || 'Failed to load waiter alerts');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchAlerts();
  }, []);

  const acknowledgeAlert = async (alertId: string) => {
    try {
      // TODO: Implement real API call to acknowledge alert
      setAlerts(alerts.map(alert => 
        alert.id === alertId 
          ? { ...alert, status: 'ACKNOWLEDGED', acknowledgedAt: new Date().toISOString() }
          : alert
      ));
      toast.success('Alert acknowledged');
    } catch (error: any) {
      console.error('Error acknowledging alert:', error);
      toast.error(error.message || 'Failed to acknowledge alert');
    }
  };

  const completeAlert = async (alertId: string) => {
    try {
      // TODO: Implement real API call to complete alert
      setAlerts(alerts.map(alert => 
        alert.id === alertId 
          ? { ...alert, status: 'COMPLETED', completedAt: new Date().toISOString() }
          : alert
      ));
      toast.success('Alert completed');
    } catch (error: any) {
      console.error('Error completing alert:', error);
      toast.error(error.message || 'Failed to complete alert');
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING':
        return 'bg-red-100 text-red-800';
      case 'ACKNOWLEDGED':
        return 'bg-yellow-100 text-yellow-800';
      case 'COMPLETED':
        return 'bg-green-100 text-green-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const formatTime = (dateString: string) => {
    return new Date(dateString).toLocaleTimeString();
  };

  if (loading) {
    return (
      <div className="bg-white shadow rounded-lg p-6">
        <h3 className="text-lg font-medium text-gray-900 mb-4">Waiter Alerts</h3>
        <div className="animate-pulse">
          <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
          <div className="h-4 bg-gray-200 rounded w-1/2"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white shadow rounded-lg p-6">
      <div className="flex justify-between items-center mb-4">
        <h3 className="text-lg font-medium text-gray-900">Waiter Alerts</h3>
        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
          {alerts.filter(alert => alert.status === 'PENDING').length} Pending
        </span>
      </div>

      {alerts.length === 0 ? (
        <div className="text-center py-8 text-gray-500">
          <p>No waiter alerts at the moment</p>
        </div>
      ) : (
        <div className="space-y-4">
          {alerts.map((alert) => (
            <div key={alert.id} className="border rounded-lg p-4">
              <div className="flex justify-between items-start">
                <div className="flex-1">
                  <div className="flex items-center space-x-2 mb-2">
                    <h4 className="font-medium text-gray-900">Table {alert.tableNumber}</h4>
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(alert.status)}`}>
                      {alert.status}
                    </span>
                  </div>
                  <p className="text-sm text-gray-600 mb-2">{alert.message}</p>
                  <p className="text-xs text-gray-500">
                    Requested at {formatTime(alert.createdAt)}
                    {alert.acknowledgedAt && (
                      <span> • Acknowledged at {formatTime(alert.acknowledgedAt)}</span>
                    )}
                    {alert.completedAt && (
                      <span> • Completed at {formatTime(alert.completedAt)}</span>
                    )}
                  </p>
                </div>
                
                <div className="flex space-x-2 ml-4">
                  {alert.status === 'PENDING' && (
                    <button
                      onClick={() => acknowledgeAlert(alert.id)}
                      className="bg-yellow-600 hover:bg-yellow-700 text-white px-3 py-1 rounded-md text-sm font-medium"
                    >
                      Acknowledge
                    </button>
                  )}
                  
                  {alert.status === 'ACKNOWLEDGED' && (
                    <button
                      onClick={() => completeAlert(alert.id)}
                      className="bg-green-600 hover:bg-green-700 text-white px-3 py-1 rounded-md text-sm font-medium"
                    >
                      Complete
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
