import { Metadata } from 'next';
import Link from 'next/link';
import { ArrowRightIcon, QrCodeIcon, DevicePhoneMobileIcon, ChartBarIcon, CreditCardIcon } from '@heroicons/react/24/outline';

export const metadata: Metadata = {
  title: 'Numa - Modern Restaurant Ordering Platform',
  description: 'Transform your restaurant with QR-based ordering, group sessions, and real-time management.',
};

const features = [
  {
    name: 'QR Code Ordering',
    description: 'Guests scan QR codes at their table to instantly access your menu and place orders.',
    icon: QrCodeIcon,
  },
  {
    name: 'Mobile-First Design',
    description: 'Optimized for mobile devices with a seamless, app-like experience.',
    icon: DevicePhoneMobileIcon,
  },
  {
    name: 'Real-Time Analytics',
    description: 'Track orders, revenue, and performance with comprehensive analytics dashboards.',
    icon: ChartBarIcon,
  },
  {
    name: 'Flexible Payments',
    description: 'Support multiple payment methods and easy bill splitting for groups.',
    icon: CreditCardIcon,
  },
];

const benefits = [
  'Reduce wait times and improve customer satisfaction',
  'Minimize staff interaction while maintaining quality service',
  'Increase average order value with smart recommendations',
  'Support dine-in, takeaway, and delivery from one platform',
  'Multi-tenant architecture for restaurant chains',
  'Real-time order management and kitchen integration',
];

export default function HomePage() {
  return (
    <div className="min-h-screen bg-white">
      {/* Hero Section */}
      <div className="relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-primary-50 to-orange-100" />
        <div className="relative">
          {/* Navigation */}
          <nav className="flex items-center justify-between p-6 lg:px-8">
            <div className="flex items-center">
              <div className="flex items-center space-x-2">
                <div className="w-8 h-8 bg-primary-500 rounded-lg flex items-center justify-center">
                  <span className="text-white font-bold text-lg">N</span>
                </div>
                <span className="text-xl font-bold text-gray-900">Numa</span>
              </div>
            </div>
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
          </nav>

          {/* Hero Content */}
          <div className="px-6 py-24 sm:px-6 sm:py-32 lg:px-8">
            <div className="mx-auto max-w-2xl text-center">
              <h1 className="text-4xl font-bold tracking-tight text-gray-900 sm:text-6xl">
                Modern Restaurant
                <span className="text-gradient block">Ordering Platform</span>
              </h1>
              <p className="mt-6 text-lg leading-8 text-gray-600">
                Transform your restaurant with QR-based ordering, group sessions, and real-time management. 
                Reduce wait times, increase efficiency, and boost customer satisfaction.
              </p>
              <div className="mt-10 flex items-center justify-center gap-x-6">
                <Link href="/restaurants/register" className="btn-primary text-lg px-8 py-3">
                  Start Free Trial
                  <ArrowRightIcon className="ml-2 h-5 w-5" />
                </Link>
                <Link href="/demo" className="text-lg font-semibold leading-6 text-gray-900 hover:text-primary-600 transition-colors">
                  View Demo <span aria-hidden="true">→</span>
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="py-24 sm:py-32">
        <div className="mx-auto max-w-7xl px-6 lg:px-8">
          <div className="mx-auto max-w-2xl lg:text-center">
            <h2 className="text-base font-semibold leading-7 text-primary-600">Everything you need</h2>
            <p className="mt-2 text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
              Built for modern restaurants
            </p>
            <p className="mt-6 text-lg leading-8 text-gray-600">
              Our platform combines the best of technology and hospitality to create an exceptional dining experience 
              for both your guests and your team.
            </p>
          </div>
          <div className="mx-auto mt-16 max-w-2xl sm:mt-20 lg:mt-24 lg:max-w-none">
            <dl className="grid max-w-xl grid-cols-1 gap-x-8 gap-y-16 lg:max-w-none lg:grid-cols-2 xl:grid-cols-4">
              {features.map((feature) => (
                <div key={feature.name} className="flex flex-col">
                  <dt className="flex items-center gap-x-3 text-base font-semibold leading-7 text-gray-900">
                    <feature.icon className="h-5 w-5 flex-none text-primary-600" aria-hidden="true" />
                    {feature.name}
                  </dt>
                  <dd className="mt-4 flex flex-auto flex-col text-base leading-7 text-gray-600">
                    <p className="flex-auto">{feature.description}</p>
                  </dd>
                </div>
              ))}
            </dl>
          </div>
        </div>
      </div>

      {/* Benefits Section */}
      <div className="bg-gray-50 py-24 sm:py-32">
        <div className="mx-auto max-w-7xl px-6 lg:px-8">
          <div className="mx-auto max-w-2xl lg:mx-0">
            <h2 className="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
              Why restaurants choose Numa
            </h2>
            <p className="mt-6 text-lg leading-8 text-gray-600">
              Join hundreds of restaurants that have transformed their operations with our platform.
            </p>
          </div>
          <div className="mx-auto mt-16 max-w-2xl lg:mx-0 lg:max-w-none">
            <div className="grid grid-cols-1 gap-y-6 lg:grid-cols-2 lg:gap-x-8 lg:gap-y-8">
              {benefits.map((benefit, index) => (
                <div key={index} className="flex items-start">
                  <div className="flex-shrink-0">
                    <div className="flex h-6 w-6 items-center justify-center rounded-full bg-primary-500">
                      <svg className="h-3 w-3 text-white" fill="currentColor" viewBox="0 0 20 20">
                        <path
                          fillRule="evenodd"
                          d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                          clipRule="evenodd"
                        />
                      </svg>
                    </div>
                  </div>
                  <p className="ml-4 text-lg leading-7 text-gray-600">{benefit}</p>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* CTA Section */}
      <div className="bg-primary-600">
        <div className="px-6 py-24 sm:px-6 sm:py-32 lg:px-8">
          <div className="mx-auto max-w-2xl text-center">
            <h2 className="text-3xl font-bold tracking-tight text-white sm:text-4xl">
              Ready to transform your restaurant?
            </h2>
            <p className="mx-auto mt-6 max-w-xl text-lg leading-8 text-primary-100">
              Start your free trial today and see how Numa can help you serve more customers, 
              increase efficiency, and boost revenue.
            </p>
            <div className="mt-10 flex items-center justify-center gap-x-6">
              <Link
                href="/restaurants/register"
                className="rounded-md bg-white px-3.5 py-2.5 text-sm font-semibold text-primary-600 shadow-sm hover:bg-primary-50 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-white transition-colors"
              >
                Get started for free
              </Link>
              <Link
                href="/contact"
                className="text-sm font-semibold leading-6 text-white hover:text-primary-100 transition-colors"
              >
                Contact sales <span aria-hidden="true">→</span>
              </Link>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="bg-white">
        <div className="mx-auto max-w-7xl px-6 py-12 md:flex md:items-center md:justify-between lg:px-8">
          <div className="flex justify-center space-x-6 md:order-2">
            <p className="text-xs leading-5 text-gray-500">
              &copy; 2024 Numa. All rights reserved.
            </p>
          </div>
          <div className="mt-8 md:order-1 md:mt-0">
            <div className="flex items-center justify-center md:justify-start">
              <div className="flex items-center space-x-2">
                <div className="w-6 h-6 bg-primary-500 rounded-lg flex items-center justify-center">
                  <span className="text-white font-bold text-sm">N</span>
                </div>
                <span className="text-lg font-bold text-gray-900">Numa</span>
              </div>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}
