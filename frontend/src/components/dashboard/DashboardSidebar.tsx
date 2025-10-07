'use client';

import { useState } from 'react';
import { useRouter, usePathname } from 'next/navigation';
import {
  HomeIcon,
  ChartBarIcon,
  ClockIcon,
  CogIcon,
  TableCellsIcon,
  MenuIcon,
  XMarkIcon
} from '@heroicons/react/24/outline';

interface DashboardSidebarProps {
  isOpen: boolean;
  onToggle: () => void;
}

const navigationItems = [
  {
    name: 'Dashboard',
    href: '/dashboard',
    icon: HomeIcon,
    current: true
  },
  {
    name: 'Active Sessions',
    href: '/dashboard/sessions',
    icon: ChartBarIcon,
    current: false
  },
  {
    name: 'Session History',
    href: '/dashboard/history',
    icon: ClockIcon,
    current: false
  },
  {
    name: 'Tables',
    href: '/dashboard/tables',
    icon: TableCellsIcon,
    current: false
  },
  {
    name: 'Settings',
    href: '/dashboard/settings',
    icon: CogIcon,
    current: false
  }
];

export default function DashboardSidebar({ isOpen, onToggle }: DashboardSidebarProps) {
  const router = useRouter();
  const pathname = usePathname();

  const handleNavigation = (href: string) => {
    router.push(href);
    onToggle(); // Close sidebar on mobile after navigation
  };

  return (
    <>
      {/* Mobile sidebar overlay */}
      {isOpen && (
        <div 
          className="fixed inset-0 bg-gray-600 bg-opacity-75 z-20 lg:hidden"
          onClick={onToggle}
        />
      )}

      {/* Sidebar */}
      <div className={`
        fixed inset-y-0 left-0 z-30 w-64 bg-white shadow-lg transform transition-transform duration-300 ease-in-out
        lg:translate-x-0 lg:static lg:inset-0
        ${isOpen ? 'translate-x-0' : '-translate-x-full'}
      `}>
        <div className="flex items-center justify-between h-16 px-4 border-b border-gray-200">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-primary-600 rounded-lg flex items-center justify-center">
                <span className="text-white font-bold text-sm">N</span>
              </div>
            </div>
            <div className="ml-3">
              <h1 className="text-lg font-semibold text-gray-900">Numa Dashboard</h1>
            </div>
          </div>
          <button
            onClick={onToggle}
            className="lg:hidden p-2 rounded-md text-gray-400 hover:text-gray-600 hover:bg-gray-100"
          >
            <XMarkIcon className="h-6 w-6" />
          </button>
        </div>

        <nav className="mt-5 px-2">
          <div className="space-y-1">
            {navigationItems.map((item) => {
              const isActive = pathname === item.href;
              return (
                <button
                  key={item.name}
                  onClick={() => handleNavigation(item.href)}
                  className={`
                    group flex items-center px-2 py-2 text-sm font-medium rounded-md w-full text-left
                    ${isActive
                      ? 'bg-primary-100 text-primary-900 border-r-2 border-primary-500'
                      : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'
                    }
                  `}
                >
                  <item.icon
                    className={`
                      mr-3 h-5 w-5 flex-shrink-0
                      ${isActive ? 'text-primary-500' : 'text-gray-400 group-hover:text-gray-500'}
                    `}
                  />
                  {item.name}
                </button>
              );
            })}
          </div>
        </nav>

        {/* User info section */}
        <div className="absolute bottom-0 w-full p-4 border-t border-gray-200">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-gray-300 rounded-full flex items-center justify-center">
                <span className="text-gray-600 text-sm font-medium">U</span>
              </div>
            </div>
            <div className="ml-3">
              <p className="text-sm font-medium text-gray-700">Restaurant Owner</p>
              <p className="text-xs text-gray-500">Admin</p>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
