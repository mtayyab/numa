'use client';

import { useState, useEffect } from 'react';
import QRCode from 'qrcode';

interface QRCodeGeneratorProps {
  url: string;
  size?: number;
  className?: string;
  showLoading?: boolean;
}

export default function QRCodeGenerator({ 
  url, 
  size = 256, 
  className = '',
  showLoading = true 
}: QRCodeGeneratorProps) {
  const [qrCodeDataUrl, setQrCodeDataUrl] = useState<string>('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    const generateQRCode = async () => {
      try {
        setLoading(true);
        setError('');
        const qrCodeDataUrl = await QRCode.toDataURL(url, {
          width: size,
          margin: 2,
          color: {
            dark: '#000000',
            light: '#FFFFFF'
          },
          errorCorrectionLevel: 'M'
        });
        setQrCodeDataUrl(qrCodeDataUrl);
      } catch (err) {
        console.error('Error generating QR code:', err);
        setError('Failed to generate QR code');
      } finally {
        setLoading(false);
      }
    };

    if (url) {
      generateQRCode();
    }
  }, [url, size]);

  if (loading && showLoading) {
    return (
      <div className={`flex items-center justify-center ${className}`}>
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
        <span className="ml-2 text-gray-600">Generating QR Code...</span>
      </div>
    );
  }

  if (error) {
    return (
      <div className={`flex items-center justify-center bg-gray-100 border-2 border-dashed border-gray-300 rounded-lg ${className}`}>
        <div className="text-center">
          <div className="text-gray-500 text-sm">{error}</div>
        </div>
      </div>
    );
  }

  if (!qrCodeDataUrl) {
    return (
      <div className={`flex items-center justify-center bg-gray-100 border-2 border-dashed border-gray-300 rounded-lg ${className}`}>
        <div className="text-center">
          <div className="text-gray-500 text-sm">No QR code available</div>
        </div>
      </div>
    );
  }

  return (
    <img 
      src={qrCodeDataUrl} 
      alt="QR Code"
      className={`border border-gray-200 rounded-lg ${className}`}
      style={{ width: size, height: size }}
    />
  );
}
