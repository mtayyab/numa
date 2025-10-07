import { Metadata } from 'next';
import Link from 'next/link';
import { ArrowLeftIcon, QrCodeIcon, DevicePhoneMobileIcon, ChartBarIcon, CreditCardIcon, PlayIcon } from '@heroicons/react/24/outline';
import QRCodeGenerator from '@/components/ui/QRCodeGenerator';

export const metadata: Metadata = {
  title: 'Demo - Numa',
  description: 'See Numa in action with our interactive demo.',
};

const demoSteps = [
  {
    step: 1,
    title: 'Scan QR Code',
    description: 'Guests scan the QR code at their table to access the menu instantly.',
    icon: QrCodeIcon,
  },
  {
    step: 2,
    title: 'Browse Menu',
    description: 'View the restaurant menu with photos, descriptions, and prices.',
    icon: DevicePhoneMobileIcon,
  },
  {
    step: 3,
    title: 'Place Order',
    description: 'Add items to cart, customize orders, and submit with special instructions.',
    icon: CreditCardIcon,
  },
  {
    step: 4,
    title: 'Track Order',
    description: 'Monitor order status in real-time and receive updates.',
    icon: ChartBarIcon,
  },
];

export default function DemoPage() {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <Link href="/" className="flex items-center space-x-2">
              <div className="w-8 h-8 bg-primary-500 rounded-lg flex items-center justify-center">
                <span className="text-white font-bold text-lg">N</span>
              </div>
              <span className="text-xl font-bold text-gray-900">Numa</span>
            </Link>
            <div className="flex items-center space-x-4">
              <Link 
                href="/auth/login" 
                className="text-gray-700 hover:text-primary-600 font-medium transition-colors"
              >
                Sign In
              </Link>
              <Link 
                href="/restaurants/register" 
                className="btn-primary"
              >
                Get Started
              </Link>
            </div>
          </div>
        </div>
      </div>

      {/* Hero Section */}
      <div className="bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
          <div className="text-center">
            <h1 className="text-4xl font-bold tracking-tight text-gray-900 sm:text-5xl">
              See Numa in Action
            </h1>
            <p className="mt-4 text-xl text-gray-600 max-w-3xl mx-auto">
              Experience how our platform transforms the dining experience for both guests and restaurant staff.
            </p>
          </div>
        </div>
      </div>

      {/* Demo Steps */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="text-center mb-16">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">
            How It Works
          </h2>
          <p className="text-lg text-gray-600">
            Follow these simple steps to see the complete ordering process
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          {demoSteps.map((step, index) => (
            <div key={step.step} className="relative">
              <div className="bg-white rounded-lg shadow-lg p-6 text-center">
                <div className="w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <step.icon className="w-6 h-6 text-primary-600" />
                </div>
                <div className="text-2xl font-bold text-primary-600 mb-2">
                  {step.step}
                </div>
                <h3 className="text-lg font-semibold text-gray-900 mb-2">
                  {step.title}
                </h3>
                <p className="text-gray-600 text-sm">
                  {step.description}
                </p>
              </div>
              
              {/* Arrow between steps */}
              {index < demoSteps.length - 1 && (
                <div className="hidden lg:block absolute top-1/2 -right-4 transform -translate-y-1/2">
                  <div className="w-8 h-0.5 bg-gray-300"></div>
                  <div className="absolute right-0 top-1/2 transform -translate-y-1/2 w-0 h-0 border-l-4 border-l-gray-300 border-t-2 border-b-2 border-t-transparent border-b-transparent"></div>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* Interactive Demo Section */}
      <div className="bg-primary-600">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-white mb-4">
              Try the Interactive Demo
            </h2>
            <p className="text-xl text-primary-100 mb-8 max-w-2xl mx-auto">
              Experience the full ordering process with our interactive demo restaurant.
            </p>
            
            <div className="bg-white rounded-lg shadow-xl p-8 max-w-2xl mx-auto">
              <div className="text-center mb-6">
                <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <QrCodeIcon className="w-8 h-8 text-primary-600" />
                </div>
                <h3 className="text-2xl font-bold text-gray-900 mb-2">
                  Demo Restaurant
                </h3>
                <p className="text-gray-600">
                  Scan this QR code to experience the guest ordering flow
                </p>
              </div>
              
              {/* Actual QR Code */}
              <div className="bg-white rounded-lg p-8 mb-6 border border-gray-200">
                <div className="flex justify-center">
                  <QRCodeGenerator 
                    url="https://numa-demo.vercel.app/restaurant/demo-restaurant/table/TABLE001"
                    size={128}
                    className="rounded-lg"
                  />
                </div>
                <p className="text-sm text-gray-600 mt-4 text-center">
                  Scan this QR code to experience the guest ordering flow
                </p>
              </div>
              
              <div className="space-y-4">
                <button className="w-full bg-primary-600 text-white py-3 px-6 rounded-lg font-semibold hover:bg-primary-700 transition-colors flex items-center justify-center">
                  <PlayIcon className="w-5 h-5 mr-2" />
                  Start Interactive Demo
                </button>
                
                <Link 
                  href="/restaurants/register" 
                  className="block w-full bg-gray-100 text-gray-900 py-3 px-6 rounded-lg font-semibold hover:bg-gray-200 transition-colors text-center"
                >
                  Create Your Own Restaurant
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Features Showcase */}
      <div className="bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
          <div className="text-center mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-4">
              Key Features
            </h2>
            <p className="text-lg text-gray-600">
              Everything you need to modernize your restaurant
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <QrCodeIcon className="w-6 h-6 text-primary-600" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                QR Code Ordering
              </h3>
              <p className="text-gray-600">
                Guests scan QR codes to instantly access your menu and place orders without waiting for staff.
              </p>
            </div>

            <div className="text-center">
              <div className="w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <ChartBarIcon className="w-6 h-6 text-primary-600" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                Real-time Analytics
              </h3>
              <p className="text-gray-600">
                Track orders, revenue, and customer behavior with comprehensive analytics dashboards.
              </p>
            </div>

            <div className="text-center">
              <div className="w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <DevicePhoneMobileIcon className="w-6 h-6 text-primary-600" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                Mobile Optimized
              </h3>
              <p className="text-gray-600">
                Seamless experience across all devices with responsive design and fast loading times.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* CTA Section */}
      <div className="bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-gray-900 mb-4">
              Ready to Get Started?
            </h2>
            <p className="text-lg text-gray-600 mb-8">
              Join hundreds of restaurants already using Numa to improve their service.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link 
                href="/restaurants/register" 
                className="btn-primary text-lg px-8 py-3"
              >
                Start Free Trial
              </Link>
              <Link 
                href="/contact" 
                className="text-lg font-semibold text-gray-900 hover:text-primary-600 transition-colors"
              >
                Contact Sales
              </Link>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <div className="bg-white border-t">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="flex justify-between items-center">
            <Link 
              href="/" 
              className="inline-flex items-center text-sm text-gray-600 hover:text-gray-900"
            >
              <ArrowLeftIcon className="w-4 h-4 mr-2" />
              Back to home
            </Link>
            <p className="text-sm text-gray-500">
              © 2024 Numa. All rights reserved.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
