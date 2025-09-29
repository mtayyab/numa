'use client';

import { ExclamationTriangleIcon } from '@heroicons/react/24/outline';

interface ErrorMessageProps {
  message: string;
  onRetry?: () => void;
  className?: string;
}

export default function ErrorMessage({ message, onRetry, className = '' }: ErrorMessageProps) {
  return (
    <div className={`text-center p-6 ${className}`}>
      <div className="flex flex-col items-center space-y-4">
        <div className="w-16 h-16 bg-error-100 rounded-full flex items-center justify-center">
          <ExclamationTriangleIcon className="w-8 h-8 text-error-600" />
        </div>
        <div className="space-y-2">
          <h3 className="text-lg font-medium text-gray-900">Something went wrong</h3>
          <p className="text-gray-600 max-w-md">{message}</p>
        </div>
        {onRetry && (
          <button
            onClick={onRetry}
            className="btn-primary"
          >
            Try Again
          </button>
        )}
      </div>
    </div>
  );
}
