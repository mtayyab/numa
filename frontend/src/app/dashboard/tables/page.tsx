'use client';

import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';

export default function TableManagementPage() {
  const router = useRouter();
  const [tables, setTables] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [showAddTable, setShowAddTable] = useState(false);
  const [newTable, setNewTable] = useState({ 
    tableNumber: '', 
    capacity: 4, 
    location: '',
    description: ''
  });

  useEffect(() => {
    // Check if user is logged in
    const token = localStorage.getItem('numa_access_token');
    if (!token) {
      toast.error('Please login to access this page');
      router.push('/auth/login');
      return;
    }

    // TODO: Fetch tables data from API
    // For now, just set mock data
    setTables([
      {
        id: '1',
        tableNumber: 'T-01',
        capacity: 4,
        location: 'Main Dining',
        status: 'AVAILABLE',
        qrCode: 'QR-TABLE-001',
        description: 'Window table with city view'
      },
      {
        id: '2',
        tableNumber: 'T-02',
        capacity: 2,
        location: 'Main Dining',
        status: 'OCCUPIED',
        qrCode: 'QR-TABLE-002',
        description: 'Intimate corner table'
      },
      {
        id: '3',
        tableNumber: 'T-03',
        capacity: 6,
        location: 'Private Room',
        status: 'AVAILABLE',
        qrCode: 'QR-TABLE-003',
        description: 'Large family table'
      }
    ]);
    setLoading(false);
  }, [router]);

  const handleAddTable = () => {
    if (!newTable.tableNumber.trim()) {
      toast.error('Table number is required');
      return;
    }

    const table = {
      id: Date.now().toString(),
      tableNumber: newTable.tableNumber,
      capacity: newTable.capacity,
      location: newTable.location,
      status: 'AVAILABLE',
      qrCode: `QR-TABLE-${Date.now()}`,
      description: newTable.description
    };

    setTables([...tables, table]);
    setNewTable({ tableNumber: '', capacity: 4, location: '', description: '' });
    setShowAddTable(false);
    toast.success('Table added successfully!');
  };

  const generateQRCode = (tableId: string) => {
    // TODO: Implement actual QR code generation
    toast.success(`QR Code generated for table ${tableId}`);
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
                        <p className="text-sm font-mono text-gray-900">{table.qrCode}</p>
                      </div>
                      <button
                        onClick={() => generateQRCode(table.id)}
                        className="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded-md text-sm font-medium"
                      >
                        Generate QR
                      </button>
                    </div>
                  </div>

                  <div className="mt-4 flex space-x-2">
                    <button className="flex-1 bg-primary-600 hover:bg-primary-700 text-white px-3 py-2 rounded-md text-sm font-medium">
                      Edit
                    </button>
                    <button className="flex-1 bg-red-600 hover:bg-red-700 text-white px-3 py-2 rounded-md text-sm font-medium">
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
        </div>
      </main>
    </div>
  );
}
