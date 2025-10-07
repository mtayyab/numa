'use client';

import { useState, useEffect, useRef } from 'react';
import QRCode from 'qrcode';

interface QRCodeDisplayProps {
  tableNumber: string;
  qrCode: string;
  restaurantSlug: string;
  onClose: () => void;
}

export default function QRCodeDisplay({ tableNumber, qrCode, restaurantSlug, onClose }: QRCodeDisplayProps) {
  const [copied, setCopied] = useState(false);
  const [qrCodeDataUrl, setQrCodeDataUrl] = useState<string>('');
  const [loading, setLoading] = useState(true);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  
  const guestUrl = `${window.location.origin}/restaurant/${restaurantSlug}/table/${qrCode}`;
  
  // Generate QR code when component mounts
  useEffect(() => {
    const generateQRCode = async () => {
      try {
        setLoading(true);
        const qrCodeDataUrl = await QRCode.toDataURL(guestUrl, {
          width: 256,
          margin: 2,
          color: {
            dark: '#000000',
            light: '#FFFFFF'
          },
          errorCorrectionLevel: 'M'
        });
        setQrCodeDataUrl(qrCodeDataUrl);
      } catch (error) {
        console.error('Error generating QR code:', error);
      } finally {
        setLoading(false);
      }
    };

    generateQRCode();
  }, [guestUrl]);

  const copyToClipboard = async () => {
    try {
      await navigator.clipboard.writeText(guestUrl);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    } catch (err) {
      console.error('Failed to copy: ', err);
    }
  };

  const downloadQRCode = () => {
    if (qrCodeDataUrl) {
      const link = document.createElement('a');
      link.download = `table-${tableNumber}-qr-code.png`;
      link.href = qrCodeDataUrl;
      link.click();
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
          {/* Actual QR Code Display */}
          <div className="bg-white border-2 border-gray-200 rounded-lg p-6 mb-4">
            {loading ? (
              <div className="flex items-center justify-center h-64">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
                <span className="ml-2 text-gray-600">Generating QR Code...</span>
              </div>
            ) : qrCodeDataUrl ? (
              <div className="flex flex-col items-center">
                <img 
                  src={qrCodeDataUrl} 
                  alt={`QR Code for Table ${tableNumber}`}
                  className="w-64 h-64 border border-gray-200 rounded-lg"
                />
                <p className="text-sm text-gray-600 mt-2">
                  Scan this QR code to access the menu
                </p>
              </div>
            ) : (
              <div className="bg-gray-100 border-2 border-dashed border-gray-300 rounded-lg p-8">
                <div className="text-gray-500 text-sm">
                  Failed to generate QR code
                </div>
              </div>
            )}
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
          
          <div className="flex flex-col space-y-3">
            <div className="flex space-x-3">
              <button
                onClick={downloadQRCode}
                disabled={!qrCodeDataUrl}
                className="flex-1 bg-green-600 hover:bg-green-700 disabled:bg-gray-400 disabled:cursor-not-allowed text-white py-2 px-4 rounded-md text-sm font-medium"
              >
                Download QR Code
              </button>
              <button
                onClick={() => window.open(guestUrl, '_blank')}
                className="flex-1 bg-primary-600 hover:bg-primary-700 text-white py-2 px-4 rounded-md text-sm font-medium"
              >
                Test Guest Interface
              </button>
            </div>
            <button
              onClick={onClose}
              className="w-full bg-gray-500 hover:bg-gray-600 text-white py-2 px-4 rounded-md text-sm font-medium"
            >
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
