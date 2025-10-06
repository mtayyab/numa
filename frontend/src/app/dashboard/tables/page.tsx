'use client';

import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { authApi, tableApi } from '@/services/api';
import QRCodeDisplay from '@/components/dashboard/QRCodeDisplay';

export default function TableManagementPage() {
  const router = useRouter();
  const [tables, setTables] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [showAddTable, setShowAddTable] = useState(false);
  const [showEditTable, setShowEditTable] = useState(false);
  const [editingTable, setEditingTable] = useState<any>(null);
  const [showQRCode, setShowQRCode] = useState(false);
  const [selectedTable, setSelectedTable] = useState<any>(null);
  const [restaurantSlug, setRestaurantSlug] = useState('');
  const [newTable, setNewTable] = useState({ 
    tableNumber: '', 
    capacity: 4, 
    location: '',
    description: ''
  });
  const [editTable, setEditTable] = useState({ 
    tableNumber: '', 
    capacity: 4, 
    location: '',
    description: ''
  });

  useEffect(() => {
    const fetchTablesData = async () => {
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

      // Fetch tables from API
      const tablesData = await tableApi.getAll(user.restaurantId);
      setTables(tablesData);
      
      // Get restaurant slug for QR code URLs
      if (user.restaurantSlug) {
        setRestaurantSlug(user.restaurantSlug);
      } else {
        console.error('Restaurant slug not found in user data:', user);
        toast.error('Restaurant slug not found. Please contact support.');
      }
      } catch (error: any) {
        console.error('Error fetching tables data:', error);
        toast.error(error.message || 'Failed to load tables data');
        router.push('/dashboard');
      } finally {
        setLoading(false);
      }
    };

    fetchTablesData();
  }, [router]);

  const handleAddTable = async () => {
    try {
      if (!newTable.tableNumber.trim()) {
        toast.error('Table number is required');
        return;
      }

      // Get current user to get restaurant ID
      const user = await authApi.getCurrentUser();
      if (!user.restaurantId) {
        toast.error('No restaurant associated with this account');
        return;
      }

      const tableData = {
        tableNumber: newTable.tableNumber,
        capacity: newTable.capacity,
        location: newTable.location,
        description: newTable.description,
        status: 'AVAILABLE'
      };

      const newTableData = await tableApi.create(user.restaurantId, tableData);
      setTables([...tables, newTableData]);
      setNewTable({ tableNumber: '', capacity: 4, location: '', description: '' });
      setShowAddTable(false);
      toast.success('Table added successfully!');
    } catch (error: any) {
      console.error('Error adding table:', error);
      toast.error(error.message || 'Failed to add table');
    }
  };

  const generateQRCode = async (tableId: string) => {
    try {
      // Get current user to get restaurant ID
      const user = await authApi.getCurrentUser();
      if (!user.restaurantId) {
        toast.error('No restaurant associated with this account');
        return;
      }

      const qrData = await tableApi.generateQrCode(user.restaurantId, tableId);
      setTables(tables.map(table => 
        table.id === tableId ? { ...table, qrCode: qrData.qrCode } : table
      ));
      toast.success(`QR Code generated for table ${tableId}`);
    } catch (error: any) {
      console.error('Error generating QR code:', error);
      toast.error(error.message || 'Failed to generate QR code');
    }
  };

  const showQRCodeModal = (table: any) => {
    setSelectedTable(table);
    setShowQRCode(true);
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'AVAILABLE':
        return 'bg-green-100 text-green-800';
      case 'OCCUPIED':
        return 'bg-red-100 text-red-800';
      case 'RESERVED':
        return 'bg-yellow-100 text-yellow-800';
      case 'MAINTENANCE':
        return 'bg-gray-100 text-gray-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const handleEditTable = (table: any) => {
    setEditingTable(table);
    setEditTable({
      tableNumber: table.tableNumber,
      capacity: table.capacity,
      location: table.location || '',
      description: table.description || ''
    });
    setShowEditTable(true);
  };

  const handleUpdateTable = async () => {
    try {
      if (!editingTable) return;
      
      const user = await authApi.getCurrentUser();
      if (!user.restaurantId) {
        toast.error('No restaurant associated with this account');
        return;
      }

      const tableData = {
        tableNumber: editTable.tableNumber,
        capacity: editTable.capacity,
        location: editTable.location,
        description: editTable.description
      };

      await tableApi.update(user.restaurantId, editingTable.id, tableData);
      
      toast.success('Table updated successfully');
      setShowEditTable(false);
      setEditingTable(null);
      
      // Refresh tables list
      const tablesData = await tableApi.getAll(user.restaurantId);
      setTables(tablesData);
    } catch (error: any) {
      console.error('Error updating table:', error);
      toast.error(error.message || 'Failed to update table');
    }
  };

  const handleDeleteTable = async (table: any) => {
    if (!confirm(`Are you sure you want to delete table "${table.tableNumber}"? This action cannot be undone.`)) {
      return;
    }

    try {
      const user = await authApi.getCurrentUser();
      if (!user.restaurantId) {
        toast.error('No restaurant associated with this account');
        return;
      }

      await tableApi.delete(user.restaurantId, table.id);
      
      toast.success('Table deleted successfully');
      
      // Refresh tables list
      const tablesData = await tableApi.getAll(user.restaurantId);
      setTables(tablesData);
    } catch (error: any) {
      console.error('Error deleting table:', error);
      toast.error(error.message || 'Failed to delete table');
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
              <h1 className="text-3xl font-bold text-gray-900">Table Management</h1>
            </div>
            <div className="flex items-center space-x-4">
              <button
                onClick={() => setShowAddTable(true)}
                className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
              >
                Add Table
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          {/* Add Table Modal */}
          {showAddTable && (
            <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
              <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
                <div className="mt-3">
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Add New Table</h3>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Table Number</label>
                      <input
                        type="text"
                        value={newTable.tableNumber}
                        onChange={(e) => setNewTable({...newTable, tableNumber: e.target.value})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="e.g., T-01"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Capacity</label>
                      <input
                        type="number"
                        min="1"
                        max="20"
                        value={newTable.capacity}
                        onChange={(e) => setNewTable({...newTable, capacity: parseInt(e.target.value)})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Location</label>
                      <input
                        type="text"
                        value={newTable.location}
                        onChange={(e) => setNewTable({...newTable, location: e.target.value})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="e.g., Main Dining, Patio"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Description</label>
                      <textarea
                        value={newTable.description}
                        onChange={(e) => setNewTable({...newTable, description: e.target.value})}
                        rows={3}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="Optional description"
                      />
                    </div>
                  </div>
                  <div className="flex justify-end space-x-3 mt-6">
                    <button
                      onClick={() => setShowAddTable(false)}
                      className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Cancel
                    </button>
                    <button
                      onClick={handleAddTable}
                      className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Add Table
                    </button>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Edit Table Modal */}
          {showEditTable && (
            <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
              <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
                <div className="mt-3">
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Edit Table</h3>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Table Number</label>
                      <input
                        type="text"
                        value={editTable.tableNumber}
                        onChange={(e) => setEditTable({...editTable, tableNumber: e.target.value})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="e.g., T-01"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Capacity</label>
                      <input
                        type="number"
                        min="1"
                        max="20"
                        value={editTable.capacity}
                        onChange={(e) => setEditTable({...editTable, capacity: parseInt(e.target.value)})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Location</label>
                      <input
                        type="text"
                        value={editTable.location}
                        onChange={(e) => setEditTable({...editTable, location: e.target.value})}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="e.g., Main Dining, Patio"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Description</label>
                      <textarea
                        value={editTable.description}
                        onChange={(e) => setEditTable({...editTable, description: e.target.value})}
                        rows={3}
                        className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                        placeholder="Optional description"
                      />
                    </div>
                  </div>
                  <div className="flex justify-end space-x-3 mt-6">
                    <button
                      onClick={() => setShowEditTable(false)}
                      className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Cancel
                    </button>
                    <button
                      onClick={handleUpdateTable}
                      className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                    >
                      Update Table
                    </button>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Tables Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {tables.map((table) => (
              <div key={table.id} className="bg-white shadow rounded-lg">
                <div className="px-4 py-5 sm:p-6">
                  <div className="flex justify-between items-start">
                    <div className="flex-1">
                      <h3 className="text-lg font-medium text-gray-900">{table.tableNumber}</h3>
                      <p className="text-sm text-gray-600 mt-1">{table.location}</p>
                      <p className="text-sm text-gray-500 mt-1">Capacity: {table.capacity} people</p>
                      {table.description && (
                        <p className="text-sm text-gray-500 mt-1">{table.description}</p>
                      )}
                    </div>
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(table.status)}`}>
                      {table.status}
                    </span>
                  </div>
                  
                  <div className="mt-4">
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm text-gray-600">QR Code</p>
                        <p className="text-sm font-mono text-gray-900">{table.qrCode || 'Not generated'}</p>
                      </div>
                      <div className="flex space-x-2">
                        {table.qrCode ? (
                          <button
                            onClick={() => showQRCodeModal(table)}
                            className="bg-green-600 hover:bg-green-700 text-white px-3 py-1 rounded-md text-sm font-medium"
                          >
                            View QR
                          </button>
                        ) : (
                          <button
                            onClick={() => generateQRCode(table.id)}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded-md text-sm font-medium"
                          >
                            Generate QR
                          </button>
                        )}
                      </div>
                    </div>
                  </div>

                  <div className="mt-4 flex space-x-2">
                    <button 
                      onClick={() => handleEditTable(table)}
                      className="flex-1 bg-primary-600 hover:bg-primary-700 text-white px-3 py-2 rounded-md text-sm font-medium"
                    >
                      Edit
                    </button>
                    <button 
                      onClick={() => handleDeleteTable(table)}
                      className="flex-1 bg-red-600 hover:bg-red-700 text-white px-3 py-2 rounded-md text-sm font-medium"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {tables.length === 0 && (
            <div className="text-center py-12">
              <h3 className="text-lg font-medium text-gray-900 mb-2">No tables yet</h3>
              <p className="text-gray-600 mb-4">Start by adding your first table.</p>
              <button
                onClick={() => setShowAddTable(true)}
                className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm font-medium"
              >
                Add Your First Table
              </button>
            </div>
          )}

          {/* QR Code Display Modal */}
          {showQRCode && selectedTable && (
            <QRCodeDisplay
              tableNumber={selectedTable.tableNumber}
              qrCode={selectedTable.qrCode}
              restaurantSlug={restaurantSlug}
              onClose={() => setShowQRCode(false)}
            />
          )}
        </div>
      </main>
    </div>
  );
}
