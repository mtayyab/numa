'use client';

import { useState, useEffect } from 'react';
import { 
  ClockIcon, 
  UsersIcon, 
  TableCellsIcon, 
  EyeIcon, 
  StopIcon,
  CurrencyDollarIcon,
  PhoneIcon
} from '@heroicons/react/24/outline';
import { sessionApi } from '@/services/api';
import { toast } from 'react-hot-toast';

interface ActiveSession {
  sessionId: string;
  sessionCode: string;
  status: string;
  tableNumber: string;
  tableLocation: string;
  guestCount: number;
  hostName: string;
  hostPhone?: string;
  specialRequests?: string;
  totalAmount: number;
  tipAmount: number;
  paymentStatus: string;
  waiterCalled: boolean;
  waiterCallTime?: string;
  waiterResponseTime?: string;
  startedAt: string;
  lastActivityAt: string;
  guests: Array<{
    guestId: string;
    guestName: string;
    guestPhone?: string;
    isHost: boolean;
    joinedAt: string;
    lastActivityAt: string;
  }>;
  orders: Array<{
    orderId: string;
    customerName: string;
    status: string;
    totalAmount: number;
    createdAt: string;
    items: Array<{
      itemId: string;
      itemName: string;
      quantity: number;
      unitPrice: number;
      totalPrice: number;
      specialInstructions?: string;
    }>;
  }>;
  cartItems: Array<{
    orderId: string;
    customerName: string;
    status: string;
    totalAmount: number;
    createdAt: string;
    items: Array<{
      itemId: string;
      itemName: string;
      quantity: number;
      unitPrice: number;
      totalPrice: number;
      specialInstructions?: string;
    }>;
  }>;
  createdAt: string;
  updatedAt: string;
}

interface ActiveSessionsProps {
  restaurantId: string;
}

export default function ActiveSessions({ restaurantId }: ActiveSessionsProps) {
  const [sessions, setSessions] = useState<ActiveSession[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedSession, setSelectedSession] = useState<ActiveSession | null>(null);
  const [showDetails, setShowDetails] = useState(false);
  const [endingSession, setEndingSession] = useState<string | null>(null);

  useEffect(() => {
    fetchActiveSessions();
    // Refresh every 30 seconds
    const interval = setInterval(fetchActiveSessions, 30000);
    return () => clearInterval(interval);
  }, [restaurantId]);

  const fetchActiveSessions = async () => {
    try {
      const response = await sessionApi.getActiveSessions(restaurantId);
      setSessions(response);
    } catch (error) {
      console.error('Error fetching active sessions:', error);
      toast.error('Failed to load active sessions');
    } finally {
      setLoading(false);
    }
  };

  const handleEndSession = async (sessionId: string) => {
    if (!confirm('Are you sure you want to end this session? This action cannot be undone.')) {
      return;
    }

    setEndingSession(sessionId);
    try {
      await sessionApi.closeSession(restaurantId, sessionId);
      toast.success('Session ended successfully');
      fetchActiveSessions(); // Refresh the list
    } catch (error) {
      console.error('Error ending session:', error);
      toast.error('Failed to end session');
    } finally {
      setEndingSession(null);
    }
  };

  const handleViewDetails = async (sessionId: string) => {
    try {
      const response = await sessionApi.getSessionDetails(sessionId);
      setSelectedSession(response);
      setShowDetails(true);
    } catch (error) {
      console.error('Error fetching session details:', error);
      toast.error('Failed to load session details');
    }
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  const formatTime = (dateString: string) => {
    return new Date(dateString).toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'ACTIVE':
        return 'bg-green-100 text-green-800';
      case 'PAUSED':
        return 'bg-yellow-100 text-yellow-800';
      case 'AWAITING_PAYMENT':
        return 'bg-blue-100 text-blue-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow p-6">
        <div className="animate-pulse">
          <div className="h-6 bg-gray-200 rounded w-1/4 mb-4"></div>
          <div className="space-y-3">
            {[1, 2, 3].map((i) => (
              <div key={i} className="h-20 bg-gray-200 rounded"></div>
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
          <h3 className="text-lg font-medium text-gray-900">Active Sessions</h3>
          <span className="text-sm text-gray-500">{sessions.length} active</span>
        </div>
      </div>

      <div className="divide-y divide-gray-200">
        {sessions.length === 0 ? (
          <div className="px-6 py-8 text-center">
            <TableCellsIcon className="mx-auto h-12 w-12 text-gray-400" />
            <h3 className="mt-2 text-sm font-medium text-gray-900">No active sessions</h3>
            <p className="mt-1 text-sm text-gray-500">All tables are currently available.</p>
          </div>
        ) : (
          sessions.map((session) => (
            <div key={session.sessionId} className="px-6 py-4">
              <div className="flex items-center justify-between">
                <div className="flex-1">
                  <div className="flex items-center space-x-3">
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
                  </div>
                  
                  <div className="mt-2 flex items-center space-x-4 text-sm text-gray-500">
                    <div className="flex items-center space-x-1">
                      <UsersIcon className="h-4 w-4" />
                      <span>{session.guestCount} guests</span>
                    </div>
                    <div className="flex items-center space-x-1">
                      <ClockIcon className="h-4 w-4" />
                      <span>Started {formatTime(session.startedAt)}</span>
                    </div>
                    <div className="flex items-center space-x-1">
                      <CurrencyDollarIcon className="h-4 w-4" />
                      <span>{formatCurrency(session.totalAmount)}</span>
                    </div>
                    {session.waiterCalled && (
                      <div className="flex items-center space-x-1 text-orange-600">
                        <PhoneIcon className="h-4 w-4" />
                        <span>Waiter called</span>
                      </div>
                    )}
                  </div>
                  
                  {session.hostName && (
                    <div className="mt-1 text-sm text-gray-600">
                      Host: {session.hostName}
                      {session.hostPhone && ` • ${session.hostPhone}`}
                    </div>
                  )}
                  
                  {session.specialRequests && (
                    <div className="mt-1 text-sm text-gray-600">
                      <span className="font-medium">Special requests:</span> {session.specialRequests}
                    </div>
                  )}
                </div>
                
                <div className="flex items-center space-x-2">
                  <button
                    onClick={() => handleViewDetails(session.sessionId)}
                    className="inline-flex items-center px-3 py-1.5 border border-gray-300 shadow-sm text-xs font-medium rounded text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                  >
                    <EyeIcon className="h-4 w-4 mr-1" />
                    Details
                  </button>
                  
                  <button
                    onClick={() => handleEndSession(session.sessionId)}
                    disabled={endingSession === session.sessionId}
                    className="inline-flex items-center px-3 py-1.5 border border-transparent shadow-sm text-xs font-medium rounded text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    {endingSession === session.sessionId ? (
                      <>
                        <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-1"></div>
                        Ending...
                      </>
                    ) : (
                      <>
                        <StopIcon className="h-4 w-4 mr-1" />
                        End Session
                      </>
                    )}
                  </button>
                </div>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Session Details Modal */}
      {showDetails && selectedSession && (
        <SessionDetailsModal
          session={selectedSession}
          onClose={() => {
            setShowDetails(false);
            setSelectedSession(null);
          }}
        />
      )}
    </div>
  );
}

// Session Details Modal Component
interface SessionDetailsModalProps {
  session: ActiveSession;
  onClose: () => void;
}

function SessionDetailsModal({ session, onClose }: SessionDetailsModalProps) {
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

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      <div className="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" onClick={onClose}></div>
        
        <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-4xl sm:w-full">
          <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-medium text-gray-900">
                Session Details - Table {session.tableNumber}
              </h3>
              <button
                onClick={onClose}
                className="text-gray-400 hover:text-gray-600"
              >
                <span className="sr-only">Close</span>
                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* Session Info */}
              <div>
                <h4 className="text-sm font-medium text-gray-900 mb-3">Session Information</h4>
                <dl className="space-y-2">
                  <div>
                    <dt className="text-xs text-gray-500">Session Code</dt>
                    <dd className="text-sm text-gray-900 font-mono">{session.sessionCode}</dd>
                  </div>
                  <div>
                    <dt className="text-xs text-gray-500">Status</dt>
                    <dd className="text-sm text-gray-900">{session.status}</dd>
                  </div>
                  <div>
                    <dt className="text-xs text-gray-500">Started At</dt>
                    <dd className="text-sm text-gray-900">{formatDateTime(session.startedAt)}</dd>
                  </div>
                  <div>
                    <dt className="text-xs text-gray-500">Total Amount</dt>
                    <dd className="text-sm text-gray-900 font-medium">{formatCurrency(session.totalAmount)}</dd>
                  </div>
                </dl>
              </div>
              
              {/* Guests */}
              <div>
                <h4 className="text-sm font-medium text-gray-900 mb-3">Guests ({session.guests.length})</h4>
                <div className="space-y-2">
                  {session.guests.map((guest) => (
                    <div key={guest.guestId} className="flex items-center justify-between p-2 bg-gray-50 rounded">
                      <div>
                        <div className="text-sm font-medium text-gray-900">
                          {guest.guestName}
                          {guest.isHost && <span className="ml-1 text-xs text-blue-600">(Host)</span>}
                        </div>
                        {guest.guestPhone && (
                          <div className="text-xs text-gray-500">{guest.guestPhone}</div>
                        )}
                      </div>
                      <div className="text-xs text-gray-500">
                        Joined {formatDateTime(guest.joinedAt)}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
            
            {/* Orders */}
            {session.orders.length > 0 && (
              <div className="mt-6">
                <h4 className="text-sm font-medium text-gray-900 mb-3">Orders ({session.orders.length})</h4>
                <div className="space-y-3">
                  {session.orders.map((order) => (
                    <div key={order.orderId} className="border border-gray-200 rounded-lg p-4">
                      <div className="flex items-center justify-between mb-2">
                        <div className="text-sm font-medium text-gray-900">
                          Order by {order.customerName}
                        </div>
                        <div className="text-sm font-medium text-gray-900">
                          {formatCurrency(order.totalAmount)}
                        </div>
                      </div>
                      <div className="text-xs text-gray-500 mb-2">
                        {formatDateTime(order.createdAt)} • Status: {order.status}
                      </div>
                      <div className="space-y-1">
                        {order.items.map((item) => (
                          <div key={item.itemId} className="flex items-center justify-between text-sm">
                            <span>{item.quantity}x {item.itemName}</span>
                            <span>{formatCurrency(item.totalPrice)}</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
            
            {/* Cart Items */}
            {session.cartItems.length > 0 && (
              <div className="mt-6">
                <h4 className="text-sm font-medium text-gray-900 mb-3">Cart Items ({session.cartItems.length})</h4>
                <div className="space-y-3">
                  {session.cartItems.map((item) => (
                    <div key={item.orderId} className="border border-gray-200 rounded-lg p-4">
                      <div className="flex items-center justify-between mb-2">
                        <div className="text-sm font-medium text-gray-900">
                          Cart by {item.customerName}
                        </div>
                        <div className="text-sm font-medium text-gray-900">
                          {formatCurrency(item.totalAmount)}
                        </div>
                      </div>
                      <div className="text-xs text-gray-500 mb-2">
                        {formatDateTime(item.createdAt)} • Status: {item.status}
                      </div>
                      <div className="space-y-1">
                        {item.items.map((cartItem) => (
                          <div key={cartItem.itemId} className="flex items-center justify-between text-sm">
                            <span>{cartItem.quantity}x {cartItem.itemName}</span>
                            <span>{formatCurrency(cartItem.totalPrice)}</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
