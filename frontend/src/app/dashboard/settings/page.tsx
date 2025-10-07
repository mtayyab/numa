'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { toast } from 'react-hot-toast';
import { authApi } from '@/services/api';
import DashboardLayout from '@/components/dashboard/DashboardLayout';
import {
  CurrencyDollarIcon,
  GlobeAltIcon,
  ClockIcon,
  CheckIcon
} from '@heroicons/react/24/outline';

interface CurrencySettings {
  currencyCode: string;
  currencySymbol: string;
  decimalPlaces: number;
}

interface LanguageSettings {
  languageCode: string;
  timezone: string;
}

export default function SettingsPage() {
  const router = useRouter();
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  
  // Currency settings
  const [currencySettings, setCurrencySettings] = useState<CurrencySettings>({
    currencyCode: 'USD',
    currencySymbol: '$',
    decimalPlaces: 2
  });

  // Language settings
  const [languageSettings, setLanguageSettings] = useState<LanguageSettings>({
    languageCode: 'en',
    timezone: 'America/New_York'
  });

  const currencyOptions = [
    { code: 'USD', symbol: '$', name: 'US Dollar' },
    { code: 'EUR', symbol: '€', name: 'Euro' },
    { code: 'GBP', symbol: '£', name: 'British Pound' },
    { code: 'CAD', symbol: 'C$', name: 'Canadian Dollar' },
    { code: 'AUD', symbol: 'A$', name: 'Australian Dollar' },
    { code: 'JPY', symbol: '¥', name: 'Japanese Yen' },
    { code: 'INR', symbol: '₹', name: 'Indian Rupee' },
    { code: 'CNY', symbol: '¥', name: 'Chinese Yuan' }
  ];

  const languageOptions = [
    { code: 'en', name: 'English' },
    { code: 'es', name: 'Spanish' },
    { code: 'fr', name: 'French' },
    { code: 'de', name: 'German' },
    { code: 'it', name: 'Italian' },
    { code: 'pt', name: 'Portuguese' },
    { code: 'zh', name: 'Chinese' },
    { code: 'ja', name: 'Japanese' }
  ];

  const timezoneOptions = [
    'America/New_York',
    'America/Chicago',
    'America/Denver',
    'America/Los_Angeles',
    'Europe/London',
    'Europe/Paris',
    'Europe/Berlin',
    'Asia/Tokyo',
    'Asia/Shanghai',
    'Asia/Kolkata',
    'Australia/Sydney'
  ];

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const userData = await authApi.getCurrentUser();
        setUser(userData);
        
        // Load existing settings if available
        if (userData.restaurant) {
          setCurrencySettings({
            currencyCode: userData.restaurant.currencyCode || 'USD',
            currencySymbol: getCurrencySymbol(userData.restaurant.currencyCode || 'USD'),
            decimalPlaces: 2
          });
          
          setLanguageSettings({
            languageCode: userData.restaurant.languageCode || 'en',
            timezone: userData.restaurant.timezone || 'America/New_York'
          });
        }
      } catch (error) {
        console.error('Error fetching user:', error);
        toast.error('Failed to load user data');
        router.push('/auth/login');
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [router]);

  const getCurrencySymbol = (code: string) => {
    const currency = currencyOptions.find(c => c.code === code);
    return currency ? currency.symbol : '$';
  };

  const handleCurrencyChange = (code: string) => {
    const currency = currencyOptions.find(c => c.code === code);
    setCurrencySettings({
      currencyCode: code,
      currencySymbol: currency ? currency.symbol : '$',
      decimalPlaces: 2
    });
  };

  const handleSaveSettings = async () => {
    try {
      setSaving(true);
      
      // TODO: Implement API call to save settings
      // await settingsApi.updateRestaurantSettings(user.restaurantId, {
      //   currencyCode: currencySettings.currencyCode,
      //   languageCode: languageSettings.languageCode,
      //   timezone: languageSettings.timezone
      // });
      
      toast.success('Settings saved successfully!');
    } catch (error) {
      console.error('Error saving settings:', error);
      toast.error('Failed to save settings');
    } finally {
      setSaving(false);
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
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Settings</h1>
          <p className="mt-1 text-sm text-gray-600">
            Manage your restaurant settings and preferences.
          </p>
        </div>

        {/* Currency Settings */}
        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <div className="flex items-center">
              <CurrencyDollarIcon className="h-6 w-6 text-gray-400 mr-3" />
              <h3 className="text-lg font-medium text-gray-900">Currency Settings</h3>
            </div>
          </div>
          <div className="px-6 py-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Currency
                </label>
                <select
                  value={currencySettings.currencyCode}
                  onChange={(e) => handleCurrencyChange(e.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                >
                  {currencyOptions.map((currency) => (
                    <option key={currency.code} value={currency.code}>
                      {currency.symbol} {currency.name} ({currency.code})
                    </option>
                  ))}
                </select>
              </div>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Currency Symbol
                </label>
                <div className="flex items-center px-3 py-2 border border-gray-300 rounded-md bg-gray-50">
                  <span className="text-lg font-medium">{currencySettings.currencySymbol}</span>
                  <span className="ml-2 text-sm text-gray-500">({currencySettings.currencyCode})</span>
                </div>
              </div>
            </div>
            
            <div className="mt-4">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Decimal Places
              </label>
              <select
                value={currencySettings.decimalPlaces}
                onChange={(e) => setCurrencySettings({
                  ...currencySettings,
                  decimalPlaces: parseInt(e.target.value)
                })}
                className="w-full md:w-32 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
              >
                <option value={0}>0</option>
                <option value={1}>1</option>
                <option value={2}>2</option>
                <option value={3}>3</option>
              </select>
            </div>
          </div>
        </div>

        {/* Language & Regional Settings */}
        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <div className="flex items-center">
              <GlobeAltIcon className="h-6 w-6 text-gray-400 mr-3" />
              <h3 className="text-lg font-medium text-gray-900">Language & Regional Settings</h3>
            </div>
          </div>
          <div className="px-6 py-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Language
                </label>
                <select
                  value={languageSettings.languageCode}
                  onChange={(e) => setLanguageSettings({
                    ...languageSettings,
                    languageCode: e.target.value
                  })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                >
                  {languageOptions.map((language) => (
                    <option key={language.code} value={language.code}>
                      {language.name}
                    </option>
                  ))}
                </select>
              </div>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Timezone
                </label>
                <select
                  value={languageSettings.timezone}
                  onChange={(e) => setLanguageSettings({
                    ...languageSettings,
                    timezone: e.target.value
                  })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                >
                  {timezoneOptions.map((timezone) => (
                    <option key={timezone} value={timezone}>
                      {timezone}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </div>
        </div>

        {/* Save Button */}
        <div className="flex justify-end">
          <button
            onClick={handleSaveSettings}
            disabled={saving}
            className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {saving ? (
              <>
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                Saving...
              </>
            ) : (
              <>
                <CheckIcon className="h-4 w-4 mr-2" />
                Save Settings
              </>
            )}
          </button>
        </div>
      </div>
    </DashboardLayout>
  );
}
