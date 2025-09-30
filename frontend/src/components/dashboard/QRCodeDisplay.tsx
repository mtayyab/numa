'use client';

import { useState } from 'react';

interface QRCodeDisplayProps {
  tableNumber: string;
  qrCode: string;
  restaurantSlug: string;
  onClose: () => void;
}

export default function QRCodeDisplay({ tableNumber, qrCode, restaurantSlug, onClose }: QRCodeDisplayProps) {
  const [copied, setCopied] = useState(false);
  
  const guestUrl = `${window.location.origin}/restaurant/${restaurantSlug}/table/${qrCode}`;
  
  const copyToClipboard = async () => {
    try {
      await navigator.clipboard.writeText(guestUrl);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    } catch (err) {
      console.error('Failed to copy: ', err);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 max-w-md w-full mx-4">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-semibold text-gray-900">QR Code for Table {tableNumber}</h3>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600"
          >
            ✕
          </button>
        </div>
        
        <div className="text-center">
          {/* QR Code Placeholder - In a real implementation, you would use a QR code library */}
          <div className="bg-gray-100 border-2 border-dashed border-gray-300 rounded-lg p-8 mb-4">
            <div className="text-gray-500 text-sm">
              QR Code would be displayed here
            </div>
            <div className="text-xs text-gray-400 mt-2">
              (Use a QR code library like qrcode.js)
            </div>
          </div>
          
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Guest URL:
            </label>
            <div className="flex items-center space-x-2">
              <input
                type="text"
                value={guestUrl}
                readOnly
                className="flex-1 px-3 py-2 border border-gray-300 rounded-md text-sm"
              />
              <button
                onClick={copyToClipboard}
                className="bg-blue-600 hover:bg-blue-700 text-white px-3 py-2 rounded-md text-sm"
              >
                {copied ? 'Copied!' : 'Copy'}
              </button>
            </div>
          </div>
          
          <div className="text-sm text-gray-600 mb-4">
            <p>Customers can scan this QR code or visit the URL above to:</p>
            <ul className="text-left mt-2 space-y-1">
              <li>• View your restaurant menu</li>
              <li>• Place orders directly</li>
              <li>• Call for waiter assistance</li>
              <li>• Join group dining sessions</li>
            </ul>
          </div>
          
          <div className="flex space-x-3">
            <button
              onClick={onClose}
              className="flex-1 bg-gray-500 hover:bg-gray-600 text-white py-2 px-4 rounded-md text-sm font-medium"
            >
              Close
            </button>
            <button
              onClick={() => window.open(guestUrl, '_blank')}
              className="flex-1 bg-primary-600 hover:bg-primary-700 text-white py-2 px-4 rounded-md text-sm font-medium"
            >
              Test Guest Interface
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
