'use client';

import { useState, useEffect } from 'react';
import { 
  ClockIcon, 
  UsersIcon, 
  TableCellsIcon, 
  CurrencyDollarIcon,
  ChartBarIcon,
  CalendarIcon,
  ArrowLeftIcon,
  ArrowRightIcon
} from '@heroicons/react/24/outline';
import { sessionApi } from '@/services/api';
import { toast } from 'react-hot-toast';

interface SessionHistoryItem {
  sessionId: string;
  sessionCode: string;
  status: string;
  tableNumber: string;
  tableLocation: string;
  guestCount: number;
  hostName: string;
  hostPhone?: string;
  totalAmount: number;
  tipAmount: number;
  paymentStatus: string;
  startedAt: string;
  endedAt?: string;
  durationMinutes?: number;
  totalOrders: number;
  averageOrderValue: number;
  createdAt: string;
  updatedAt: string;
}

interface SessionHistoryProps {
  restaurantId: string;
}

export default function SessionHistory({ restaurantId }: SessionHistoryProps) {
  const [sessions, setSessions] = useState<SessionHistoryItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [pageSize] = useState(10);

  useEffect(() => {
    fetchSessionHistory();
  }, [restaurantId, currentPage]);

  const fetchSessionHistory = async () => {
    try {
      setLoading(true);
      const response = await sessionApi.getSessionHistory(restaurantId, currentPage, pageSize);
      setSessions(response.content || []);
      setTotalPages(response.totalPages || 0);
      setTotalElements(response.totalElements || 0);
    } catch (error) {
      console.error('Error fetching session history:', error);
      toast.error('Failed to load session history');
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

  const formatDateTime = (dateString: string) => {
    return new Date(dateString).toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const formatDuration = (minutes?: number) => {
    if (!minutes) return 'N/A';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    if (hours > 0) {
      return `${hours}h ${mins}m`;
    }
    return `${mins}m`;
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'COMPLETED':
        return 'bg-green-100 text-green-800';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800';
      case 'AWAITING_PAYMENT':
        return 'bg-blue-100 text-blue-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const handlePageChange = (newPage: number) => {
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
    }
  };

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow p-6">
        <div className="animate-pulse">
          <div className="h-6 bg-gray-200 rounded w-1/4 mb-4"></div>
          <div className="space-y-3">
            {[1, 2, 3, 4, 5].map((i) => (
              <div key={i} className="h-16 bg-gray-200 rounded"></div>
            ))}
          </div>
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
            <h3 className="text-lg font-medium text-gray-900">Session History</h3>
          </div>
          <span className="text-sm text-gray-500">
            {totalElements} total sessions
          </span>
        </div>
      </div>

      <div className="divide-y divide-gray-200">
        {sessions.length === 0 ? (
          <div className="px-6 py-8 text-center">
            <CalendarIcon className="mx-auto h-12 w-12 text-gray-400" />
            <h3 className="mt-2 text-sm font-medium text-gray-900">No session history</h3>
            <p className="mt-1 text-sm text-gray-500">Session history will appear here once sessions are completed.</p>
          </div>
        ) : (
          sessions.map((session) => (
            <div key={session.sessionId} className="px-6 py-4">
              <div className="flex items-center justify-between">
                <div className="flex-1">
                  <div className="flex items-center space-x-3 mb-2">
                    <div className="flex items-center space-x-2">
                      <TableCellsIcon className="h-5 w-5 text-gray-400" />
                      <span className="text-sm font-medium text-gray-900">
                        Table {session.tableNumber}
                      </span>
                      {session.tableLocation && (
                        <span className="text-xs text-gray-500">({session.tableLocation})</span>
                      )}
                    </div>
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(session.status)}`}>
                      {session.status}
                    </span>
                    <span className="text-xs text-gray-500 font-mono">
                      {session.sessionCode}
                    </span>
                  </div>
                  
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm text-gray-500">
                    <div className="flex items-center space-x-1">
                      <UsersIcon className="h-4 w-4" />
                      <span>{session.guestCount} guests</span>
                    </div>
                    <div className="flex items-center space-x-1">
                      <ClockIcon className="h-4 w-4" />
                      <span>{formatDuration(session.durationMinutes)}</span>
                    </div>
                    <div className="flex items-center space-x-1">
                      <CurrencyDollarIcon className="h-4 w-4" />
                      <span>{formatCurrency(session.totalAmount)}</span>
                    </div>
                    <div className="flex items-center space-x-1">
                      <ChartBarIcon className="h-4 w-4" />
                      <span>{session.totalOrders} orders</span>
                    </div>
                  </div>
                  
                  <div className="mt-2 text-sm text-gray-600">
                    <div className="flex items-center justify-between">
                      <span>
                        {session.hostName && `Host: ${session.hostName}`}
                        {session.hostPhone && ` • ${session.hostPhone}`}
                      </span>
                      <span className="text-xs">
                        {formatDateTime(session.startedAt)}
                        {session.endedAt && ` - ${formatDateTime(session.endedAt)}`}
                      </span>
                    </div>
                    {session.averageOrderValue > 0 && (
                      <div className="text-xs text-gray-500 mt-1">
                        Avg order: {formatCurrency(session.averageOrderValue)}
                        {session.tipAmount > 0 && ` • Tip: ${formatCurrency(session.tipAmount)}`}
                      </div>
                    )}
                  </div>
                </div>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="px-6 py-4 border-t border-gray-200">
          <div className="flex items-center justify-between">
            <div className="text-sm text-gray-700">
              Showing {currentPage * pageSize + 1} to {Math.min((currentPage + 1) * pageSize, totalElements)} of {totalElements} sessions
            </div>
            <div className="flex items-center space-x-2">
              <button
                onClick={() => handlePageChange(currentPage - 1)}
                disabled={currentPage === 0}
                className="inline-flex items-center px-3 py-1.5 border border-gray-300 shadow-sm text-xs font-medium rounded text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <ArrowLeftIcon className="h-4 w-4 mr-1" />
                Previous
              </button>
              
              <div className="flex items-center space-x-1">
                {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                  const pageNum = Math.max(0, Math.min(totalPages - 5, currentPage - 2)) + i;
                  return (
                    <button
                      key={pageNum}
                      onClick={() => handlePageChange(pageNum)}
                      className={`inline-flex items-center px-3 py-1.5 border text-xs font-medium rounded ${
                        pageNum === currentPage
                          ? 'border-indigo-500 bg-indigo-50 text-indigo-600'
                          : 'border-gray-300 text-gray-700 bg-white hover:bg-gray-50'
                      } focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500`}
                    >
                      {pageNum + 1}
                    </button>
                  );
                })}
              </div>
              
              <button
                onClick={() => handlePageChange(currentPage + 1)}
                disabled={currentPage >= totalPages - 1}
                className="inline-flex items-center px-3 py-1.5 border border-gray-300 shadow-sm text-xs font-medium rounded text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Next
                <ArrowRightIcon className="h-4 w-4 ml-1" />
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
