'use client';

import { useState, useEffect } from 'react';
import { voucherApi } from '@/services/api';
import { XMarkIcon, SparklesIcon } from '@heroicons/react/24/outline';
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
  isPublic: boolean;
  validFrom?: string;
}

interface VoucherModalProps {
  voucher?: Voucher | null;
  restaurantId: string;
  onSave: () => void;
  onClose: () => void;
}

export default function VoucherModal({ voucher, restaurantId, onSave, onClose }: VoucherModalProps) {
  const [formData, setFormData] = useState({
    code: '',
    description: '',
    type: 'PERCENTAGE' as 'PERCENTAGE' | 'FIXED_AMOUNT',
    discountValue: '',
    minimumOrderAmount: '',
    maximumDiscountAmount: '',
    expiresAt: '',
    usageLimit: '',
    isPublic: true,
    validFrom: ''
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isGeneratingCode, setIsGeneratingCode] = useState(false);

  useEffect(() => {
    if (voucher) {
      setFormData({
        code: voucher.code,
        description: voucher.description || '',
        type: voucher.type,
        discountValue: voucher.discountValue.toString(),
        minimumOrderAmount: voucher.minimumOrderAmount?.toString() || '',
        maximumDiscountAmount: voucher.maximumDiscountAmount?.toString() || '',
        expiresAt: voucher.expiresAt ? new Date(voucher.expiresAt).toISOString().slice(0, 16) : '',
        usageLimit: voucher.usageLimit?.toString() || '',
        isPublic: voucher.isPublic,
        validFrom: voucher.validFrom ? new Date(voucher.validFrom).toISOString().slice(0, 16) : ''
      });
    } else {
      // Set default valid from to now
      setFormData(prev => ({
        ...prev,
        validFrom: new Date().toISOString().slice(0, 16)
      }));
    }
  }, [voucher]);

  const handleGenerateCode = async () => {
    setIsGeneratingCode(true);
    try {
      const response = await voucherApi.generateVoucherCode();
      setFormData(prev => ({ ...prev, code: response.code }));
      toast.success('Voucher code generated');
    } catch (error: any) {
      console.error('Error generating code:', error);
      toast.error('Failed to generate voucher code');
    } finally {
      setIsGeneratingCode(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      const voucherData = {
        code: formData.code.toUpperCase(),
        description: formData.description,
        type: formData.type,
        discountValue: parseFloat(formData.discountValue),
        minimumOrderAmount: formData.minimumOrderAmount ? parseFloat(formData.minimumOrderAmount) : null,
        maximumDiscountAmount: formData.maximumDiscountAmount ? parseFloat(formData.maximumDiscountAmount) : null,
        expiresAt: formData.expiresAt ? new Date(formData.expiresAt).toISOString() : null,
        usageLimit: formData.usageLimit ? parseInt(formData.usageLimit) : null,
        restaurantId: restaurantId,
        isPublic: formData.isPublic,
        validFrom: formData.validFrom ? new Date(formData.validFrom).toISOString() : null
      };

      if (voucher) {
        await voucherApi.updateVoucher(voucher.id, voucherData);
        toast.success('Voucher updated successfully');
      } else {
        await voucherApi.createVoucher(voucherData);
        toast.success('Voucher created successfully');
      }

      onSave();
    } catch (error: any) {
      console.error('Error saving voucher:', error);
      toast.error(error.message || 'Failed to save voucher');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    
    if (type === 'checkbox') {
      const checked = (e.target as HTMLInputElement).checked;
      setFormData(prev => ({ ...prev, [name]: checked }));
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between p-6 border-b border-gray-200">
          <h2 className="text-xl font-semibold text-gray-900">
            {voucher ? 'Edit Voucher' : 'Create Voucher'}
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <XMarkIcon className="w-6 h-6" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          {/* Voucher Code */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Voucher Code *
            </label>
            <div className="flex space-x-2">
              <input
                type="text"
                name="code"
                value={formData.code}
                onChange={handleInputChange}
                placeholder="Enter voucher code"
                className="flex-1 px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                required
              />
              <button
                type="button"
                onClick={handleGenerateCode}
                disabled={isGeneratingCode}
                className="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors disabled:opacity-50 flex items-center space-x-2"
              >
                <SparklesIcon className="w-4 h-4" />
                <span>{isGeneratingCode ? 'Generating...' : 'Generate'}</span>
              </button>
            </div>
          </div>

          {/* Description */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Description
            </label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleInputChange}
              placeholder="Enter voucher description"
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
            />
          </div>

          {/* Discount Type and Value */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Discount Type *
              </label>
              <select
                name="type"
                value={formData.type}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                required
              >
                <option value="PERCENTAGE">Percentage Discount</option>
                <option value="FIXED_AMOUNT">Fixed Amount Discount</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Discount Value *
              </label>
              <div className="relative">
                <input
                  type="number"
                  name="discountValue"
                  value={formData.discountValue}
                  onChange={handleInputChange}
                  placeholder={formData.type === 'PERCENTAGE' ? '10' : '5.00'}
                  min="0"
                  max={formData.type === 'PERCENTAGE' ? '100' : undefined}
                  step={formData.type === 'PERCENTAGE' ? '1' : '0.01'}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                  required
                />
                <span className="absolute right-3 top-2 text-gray-500">
                  {formData.type === 'PERCENTAGE' ? '%' : '$'}
                </span>
              </div>
            </div>
          </div>

          {/* Minimum Order Amount and Maximum Discount */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Minimum Order Amount
              </label>
              <input
                type="number"
                name="minimumOrderAmount"
                value={formData.minimumOrderAmount}
                onChange={handleInputChange}
                placeholder="0.00"
                min="0"
                step="0.01"
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Maximum Discount Amount
              </label>
              <input
                type="number"
                name="maximumDiscountAmount"
                value={formData.maximumDiscountAmount}
                onChange={handleInputChange}
                placeholder="0.00"
                min="0"
                step="0.01"
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
              />
            </div>
          </div>

          {/* Usage Limit */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Usage Limit
            </label>
            <input
              type="number"
              name="usageLimit"
              value={formData.usageLimit}
              onChange={handleInputChange}
              placeholder="Leave empty for unlimited"
              min="1"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
            />
          </div>

          {/* Valid From and Expires At */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Valid From
              </label>
              <input
                type="datetime-local"
                name="validFrom"
                value={formData.validFrom}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Expires At
              </label>
              <input
                type="datetime-local"
                name="expiresAt"
                value={formData.expiresAt}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
              />
            </div>
          </div>

          {/* Public Voucher */}
          <div className="flex items-center">
            <input
              type="checkbox"
              name="isPublic"
              checked={formData.isPublic}
              onChange={handleInputChange}
              className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
            />
            <label className="ml-2 block text-sm text-gray-700">
              Make this voucher public (guests can use it)
            </label>
          </div>

          {/* Action Buttons */}
          <div className="flex justify-end space-x-3 pt-6 border-t border-gray-200">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isSubmitting}
              className="px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors disabled:opacity-50"
            >
              {isSubmitting ? 'Saving...' : (voucher ? 'Update Voucher' : 'Create Voucher')}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
