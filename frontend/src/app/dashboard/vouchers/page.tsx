'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { authApi, voucherApi } from '@/services/api';
import DashboardLayout from '@/components/dashboard/DashboardLayout';
import VoucherModal from '@/components/dashboard/VoucherModal';
import { 
  PlusIcon, 
  PencilIcon, 
  TrashIcon, 
  EyeIcon,
  EyeSlashIcon,
  ClockIcon,
  UsersIcon,
  CurrencyDollarIcon,
  TagIcon
} from '@heroicons/react/24/outline';
import toast from 'react-hot-toast';

interface Voucher {
  id: string;
  code: string;
  description: string;
  type: 'PERCENTAGE' | 'FIXED_AMOUNT';
  discountValue: number;
  minimumOrderAmount?: number;
  maximumDiscountAmount?: number;
  expiresAt?: string;
  usageLimit?: number;
  usedCount: number;
  status: 'ACTIVE' | 'INACTIVE' | 'EXPIRED' | 'USED_UP';
  isPublic: boolean;
  validFrom?: string;
  createdAt: string;
  updatedAt: string;
  isExpired: boolean;
  isUsageLimitReached: boolean;
  isActive: boolean;
  remainingUses?: number;
}

export default function VoucherManagementPage() {
  const router = useRouter();
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [vouchers, setVouchers] = useState<Voucher[]>([]);
  const [showVoucherModal, setShowVoucherModal] = useState(false);
  const [editingVoucher, setEditingVoucher] = useState<Voucher | null>(null);
  const [filterStatus, setFilterStatus] = useState<string>('ALL');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userData = await authApi.getCurrentUser();
        setUser(userData);
        
        const vouchersData = await voucherApi.getVouchers(userData.restaurantId);
        setVouchers(vouchersData || []);
      } catch (error) {
        console.error('Error fetching data:', error);
        toast.error('Failed to load voucher data');
        router.push('/auth/login');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [router]);

  const handleCreateVoucher = () => {
    setEditingVoucher(null);
    setShowVoucherModal(true);
  };

  const handleEditVoucher = (voucher: Voucher) => {
    setEditingVoucher(voucher);
    setShowVoucherModal(true);
  };

  const handleVoucherSaved = async () => {
    try {
      const vouchersData = await voucherApi.getVouchers(user.restaurantId);
      setVouchers(vouchersData || []);
      setShowVoucherModal(false);
      setEditingVoucher(null);
    } catch (error) {
      console.error('Error refreshing vouchers:', error);
      toast.error('Failed to refresh voucher data');
    }
  };

  const handleDeleteVoucher = async (voucherId: string) => {
    if (!confirm('Are you sure you want to delete this voucher?')) {
      return;
    }

    try {
      await voucherApi.deleteVoucher(voucherId);
      toast.success('Voucher deleted successfully');
      
      const vouchersData = await voucherApi.getVouchers(user.restaurantId);
      setVouchers(vouchersData || []);
    } catch (error: any) {
      console.error('Error deleting voucher:', error);
      toast.error(error.message || 'Failed to delete voucher');
    }
  };

  const handleToggleStatus = async (voucherId: string) => {
    try {
      await voucherApi.toggleVoucherStatus(voucherId);
      toast.success('Voucher status updated successfully');
      
      const vouchersData = await voucherApi.getVouchers(user.restaurantId);
      setVouchers(vouchersData || []);
    } catch (error: any) {
      console.error('Error toggling voucher status:', error);
      toast.error(error.message || 'Failed to update voucher status');
    }
  };

  const filteredVouchers = vouchers.filter(voucher => {
    if (filterStatus === 'ALL') return true;
    if (filterStatus === 'ACTIVE') return voucher.isActive;
    if (filterStatus === 'INACTIVE') return voucher.status === 'INACTIVE';
    if (filterStatus === 'EXPIRED') return voucher.isExpired;
    if (filterStatus === 'USED_UP') return voucher.isUsageLimitReached;
    return true;
  });

  const getStatusColor = (voucher: Voucher) => {
    if (voucher.isExpired) return 'text-red-600 bg-red-50';
    if (voucher.isUsageLimitReached) return 'text-orange-600 bg-orange-50';
    if (voucher.status === 'INACTIVE') return 'text-gray-600 bg-gray-50';
    return 'text-green-600 bg-green-50';
  };

  const getStatusText = (voucher: Voucher) => {
    if (voucher.isExpired) return 'Expired';
    if (voucher.isUsageLimitReached) return 'Used Up';
    return voucher.status;
  };

  const formatDiscount = (voucher: Voucher) => {
    if (voucher.type === 'PERCENTAGE') {
      return `${voucher.discountValue}% off`;
    } else {
      return `$${voucher.discountValue} off`;
    }
  };

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
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Voucher Management</h1>
            <p className="text-gray-600">Create and manage discount vouchers for your customers</p>
          </div>
          <button
            onClick={handleCreateVoucher}
            className="btn-primary flex items-center space-x-2"
          >
            <PlusIcon className="w-5 h-5" />
            <span>Create Voucher</span>
          </button>
        </div>

        {/* Filters */}
        <div className="flex space-x-4">
          <select
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value)}
            className="px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
          >
            <option value="ALL">All Vouchers</option>
            <option value="ACTIVE">Active</option>
            <option value="INACTIVE">Inactive</option>
            <option value="EXPIRED">Expired</option>
            <option value="USED_UP">Used Up</option>
          </select>
        </div>

        {/* Vouchers List */}
        <div className="grid gap-6">
          {filteredVouchers.length === 0 ? (
            <div className="text-center py-12">
              <TagIcon className="w-12 h-12 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">No vouchers found</h3>
              <p className="text-gray-600 mb-4">
                {filterStatus === 'ALL' 
                  ? 'Create your first voucher to get started'
                  : `No ${filterStatus.toLowerCase()} vouchers found`
                }
              </p>
              {filterStatus === 'ALL' && (
                <button
                  onClick={handleCreateVoucher}
                  className="btn-primary"
                >
                  Create Voucher
                </button>
              )}
            </div>
          ) : (
            filteredVouchers.map((voucher) => (
              <div key={voucher.id} className="bg-white rounded-lg border border-gray-200 p-6">
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <div className="flex items-center space-x-3 mb-2">
                      <h3 className="text-lg font-semibold text-gray-900">{voucher.code}</h3>
                      <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(voucher)}`}>
                        {getStatusText(voucher)}
                      </span>
                      {!voucher.isPublic && (
                        <span className="px-2 py-1 rounded-full text-xs font-medium text-blue-600 bg-blue-50">
                          Private
                        </span>
                      )}
                    </div>
                    
                    {voucher.description && (
                      <p className="text-gray-600 mb-3">{voucher.description}</p>
                    )}

                    <div className="flex items-center space-x-6 text-sm text-gray-500">
                      <div className="flex items-center space-x-1">
                        <CurrencyDollarIcon className="w-4 h-4" />
                        <span>{formatDiscount(voucher)}</span>
                      </div>
                      
                      {voucher.minimumOrderAmount && (
                        <div className="flex items-center space-x-1">
                          <span>Min: ${voucher.minimumOrderAmount}</span>
                        </div>
                      )}

                      {voucher.usageLimit && (
                        <div className="flex items-center space-x-1">
                          <UsersIcon className="w-4 h-4" />
                          <span>{voucher.usedCount}/{voucher.usageLimit} used</span>
                        </div>
                      )}

                      {voucher.expiresAt && (
                        <div className="flex items-center space-x-1">
                          <ClockIcon className="w-4 h-4" />
                          <span>Expires: {new Date(voucher.expiresAt).toLocaleDateString()}</span>
                        </div>
                      )}
                    </div>
                  </div>

                  <div className="flex items-center space-x-2">
                    <button
                      onClick={() => handleToggleStatus(voucher.id)}
                      className="p-2 text-gray-400 hover:text-gray-600 transition-colors"
                      title={voucher.status === 'ACTIVE' ? 'Deactivate' : 'Activate'}
                    >
                      {voucher.status === 'ACTIVE' ? (
                        <EyeSlashIcon className="w-5 h-5" />
                      ) : (
                        <EyeIcon className="w-5 h-5" />
                      )}
                    </button>
                    
                    <button
                      onClick={() => handleEditVoucher(voucher)}
                      className="p-2 text-gray-400 hover:text-blue-600 transition-colors"
                      title="Edit voucher"
                    >
                      <PencilIcon className="w-5 h-5" />
                    </button>
                    
                    <button
                      onClick={() => handleDeleteVoucher(voucher.id)}
                      className="p-2 text-gray-400 hover:text-red-600 transition-colors"
                      title="Delete voucher"
                    >
                      <TrashIcon className="w-5 h-5" />
                    </button>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* Voucher Modal */}
      {showVoucherModal && (
        <VoucherModal
          voucher={editingVoucher}
          restaurantId={user?.restaurantId}
          onSave={handleVoucherSaved}
          onClose={() => {
            setShowVoucherModal(false);
            setEditingVoucher(null);
          }}
        />
      )}
    </DashboardLayout>
  );
}
