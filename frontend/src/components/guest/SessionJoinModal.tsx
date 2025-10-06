'use client';

import { useState } from 'react';
import { XMarkIcon, UserGroupIcon, QrCodeIcon } from '@heroicons/react/24/outline';
import { RestaurantTable } from '@/types';

interface SessionJoinModalProps {
  table: RestaurantTable;
  activeSessionInfo?: any;
  onJoinSession: (sessionCode?: string, guestName?: string) => void;
  onClose: () => void;
}

export default function SessionJoinModal({ 
  table, 
  activeSessionInfo,
  onJoinSession, 
  onClose 
}: SessionJoinModalProps) {
  const [guestName, setGuestName] = useState('');
  const [sessionCode, setSessionCode] = useState('');
  const [isJoining, setIsJoining] = useState(false);
  const [isCreating, setIsCreating] = useState(false);

  const handleJoinSession = async () => {
    if (!guestName.trim()) return;
    
    setIsJoining(true);
    try {
      // If there's an active session, use its session code
      const sessionCodeToUse = activeSessionInfo?.hasActiveSession ? activeSessionInfo.sessionCode : sessionCode;
      await onJoinSession(sessionCodeToUse, guestName.trim());
    } catch (error) {
      console.error('Failed to join session:', error);
    } finally {
      setIsJoining(false);
    }
  };

  const handleCreateSession = async () => {
    if (!guestName.trim()) return;
    
    setIsCreating(true);
    try {
      await onJoinSession(undefined, guestName.trim());
    } catch (error) {
      console.error('Failed to create session:', error);
    } finally {
      setIsCreating(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      <div className="flex min-h-screen items-center justify-center p-4">
        <div className="fixed inset-0 bg-black bg-opacity-50" onClick={onClose} />
        
        <div className="relative bg-white rounded-lg shadow-xl max-w-md w-full">
          {/* Header */}
          <div className="flex items-center justify-between border-b border-gray-200 px-6 py-4">
            <div className="flex items-center space-x-3">
              <div className="p-2 bg-blue-100 rounded-lg">
                <UserGroupIcon className="w-6 h-6 text-blue-600" />
              </div>
              <div>
                <h3 className="text-lg font-semibold text-gray-900">Join Table Session</h3>
                <p className="text-sm text-gray-600">Table {table.tableNumber}</p>
              </div>
            </div>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600 transition-colors"
            >
              <XMarkIcon className="w-6 h-6" />
            </button>
          </div>

          {/* Content */}
          <div className="p-6 space-y-6">
            {/* Guest Name */}
            <div>
              <label className="block text-sm font-medium text-gray-900 mb-2">
                Your Name *
              </label>
              <input
                type="text"
                value={guestName}
                onChange={(e) => setGuestName(e.target.value)}
                placeholder="Enter your name"
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                required
              />
            </div>

            {/* Session Options */}
            <div className="space-y-4">
              {activeSessionInfo?.hasActiveSession ? (
                /* Active Session Found */
                <div className="border border-green-200 bg-green-50 rounded-lg p-4">
                  <div className="flex items-center space-x-3 mb-3">
                    <UserGroupIcon className="w-5 h-5 text-green-600" />
                    <h4 className="font-medium text-green-900">Active Session Found</h4>
                  </div>
                  <div className="text-sm text-green-800 mb-3">
                    <p>There's already an active session at this table:</p>
                    <ul className="mt-2 space-y-1">
                      <li>• Session Code: <span className="font-mono font-semibold">{activeSessionInfo.sessionCode}</span></li>
                      <li>• Host: {activeSessionInfo.hostName}</li>
                      <li>• Guests: {activeSessionInfo.guestCount}</li>
                      <li>• Started: {new Date(activeSessionInfo.startedAt).toLocaleTimeString()}</li>
                    </ul>
                  </div>
                  <button
                    onClick={handleJoinSession}
                    disabled={!guestName.trim() || isJoining}
                    className="w-full btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    {isJoining ? 'Joining...' : 'Join Active Session'}
                  </button>
                </div>
              ) : (
                /* No Active Session - Show Options */
                <div>
                  <div className="text-sm font-medium text-gray-900 mb-4">No active session found. Choose an option:</div>
                  
                  {/* Join Existing Session */}
                  <div className="border border-gray-200 rounded-lg p-4 mb-4">
                    <div className="flex items-center space-x-3 mb-3">
                      <QrCodeIcon className="w-5 h-5 text-gray-400" />
                      <h4 className="font-medium text-gray-900">Join Existing Session</h4>
                    </div>
                    <p className="text-sm text-gray-600 mb-3">
                      If someone at your table has already started a session, enter the session code below.
                    </p>
                    <div className="space-y-3">
                      <input
                        type="text"
                        value={sessionCode}
                        onChange={(e) => setSessionCode(e.target.value)}
                        placeholder="Enter session code (e.g., ABC123)"
                        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                      />
                      <button
                        onClick={handleJoinSession}
                        disabled={!sessionCode.trim() || !guestName.trim() || isJoining}
                        className="w-full btn-secondary disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        {isJoining ? 'Joining...' : 'Join Session'}
                      </button>
                    </div>
                  </div>

                  {/* Create New Session */}
                  <div className="border border-gray-200 rounded-lg p-4">
                    <div className="flex items-center space-x-3 mb-3">
                      <UserGroupIcon className="w-5 h-5 text-gray-400" />
                      <h4 className="font-medium text-gray-900">Start New Session</h4>
                    </div>
                    <p className="text-sm text-gray-600 mb-3">
                      Create a new session for your table. Others can join using the session code.
                    </p>
                    <button
                      onClick={handleCreateSession}
                      disabled={!guestName.trim() || isCreating}
                      className="w-full btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                      {isCreating ? 'Creating...' : 'Start New Session'}
                    </button>
                  </div>
                </div>
              )}
            </div>

            {/* Info */}
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
              <div className="text-sm text-blue-800">
                <strong>How it works:</strong>
                <ul className="mt-2 space-y-1 text-blue-700">
                  <li>• One person starts a session and shares the code</li>
                  <li>• Everyone else joins using the same code</li>
                  <li>• All orders are combined into one bill</li>
                  <li>• You can split the bill at the end</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
