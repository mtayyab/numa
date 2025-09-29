import { Metadata } from 'next';
import Link from 'next/link';
import { ArrowLeftIcon, CheckIcon } from '@heroicons/react/24/outline';

export const metadata: Metadata = {
  title: 'Register Restaurant - Numa',
  description: 'Create your restaurant account and start using Numa for digital ordering.',
};

const features = [
  'QR code ordering system',
  'Real-time order management',
  'Analytics dashboard',
  'Multi-location support',
  'Staff management tools',
  'Customer insights',
];

export default function RegisterRestaurantPage() {
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
            <Link 
              href="/auth/login" 
              className="text-gray-700 hover:text-primary-600 font-medium transition-colors"
            >
              Sign In
            </Link>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8">
        <div className="lg:grid lg:grid-cols-2 lg:gap-8">
          {/* Left side - Registration form */}
          <div className="mt-8 lg:mt-0">
            <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
              <div>
                <h2 className="text-3xl font-bold tracking-tight text-gray-900">
                  Create your restaurant account
                </h2>
                <p className="mt-2 text-sm text-gray-600">
                  Start your free trial today. No credit card required.
                </p>
              </div>

              <form className="mt-8 space-y-6" action="#" method="POST">
                <div className="space-y-4">
                  <div>
                    <label htmlFor="restaurant-name" className="block text-sm font-medium leading-6 text-gray-900">
                      Restaurant Name *
                    </label>
                    <div className="mt-2">
                      <input
                        id="restaurant-name"
                        name="restaurant-name"
                        type="text"
                        required
                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                        placeholder="Your Restaurant Name"
                      />
                    </div>
                  </div>

                  <div>
                    <label htmlFor="email" className="block text-sm font-medium leading-6 text-gray-900">
                      Email Address *
                    </label>
                    <div className="mt-2">
                      <input
                        id="email"
                        name="email"
                        type="email"
                        autoComplete="email"
                        required
                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                        placeholder="your@email.com"
                      />
                    </div>
                  </div>

                  <div>
                    <label htmlFor="phone" className="block text-sm font-medium leading-6 text-gray-900">
                      Phone Number
                    </label>
                    <div className="mt-2">
                      <input
                        id="phone"
                        name="phone"
                        type="tel"
                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                        placeholder="+1 (555) 123-4567"
                      />
                    </div>
                  </div>

                  <div>
                    <label htmlFor="password" className="block text-sm font-medium leading-6 text-gray-900">
                      Password *
                    </label>
                    <div className="mt-2">
                      <input
                        id="password"
                        name="password"
                        type="password"
                        autoComplete="new-password"
                        required
                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                        placeholder="Create a strong password"
                      />
                    </div>
                  </div>

                  <div>
                    <label htmlFor="restaurant-type" className="block text-sm font-medium leading-6 text-gray-900">
                      Restaurant Type
                    </label>
                    <div className="mt-2">
                      <select
                        id="restaurant-type"
                        name="restaurant-type"
                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                      >
                        <option>Fast Food</option>
                        <option>Casual Dining</option>
                        <option>Fine Dining</option>
                        <option>Cafe</option>
                        <option>Bar & Grill</option>
                        <option>Food Truck</option>
                        <option>Other</option>
                      </select>
                    </div>
                  </div>

                  <div>
                    <label htmlFor="location" className="block text-sm font-medium leading-6 text-gray-900">
                      Location
                    </label>
                    <div className="mt-2">
                      <input
                        id="location"
                        name="location"
                        type="text"
                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                        placeholder="City, State"
                      />
                    </div>
                  </div>
                </div>

                <div className="flex items-center">
                  <input
                    id="terms"
                    name="terms"
                    type="checkbox"
                    required
                    className="h-4 w-4 rounded border-gray-300 text-primary-600 focus:ring-primary-600"
                  />
                  <label htmlFor="terms" className="ml-2 block text-sm text-gray-900">
                    I agree to the{' '}
                    <a href="#" className="text-primary-600 hover:text-primary-500">
                      Terms of Service
                    </a>{' '}
                    and{' '}
                    <a href="#" className="text-primary-600 hover:text-primary-500">
                      Privacy Policy
                    </a>
                  </label>
                </div>

                <div>
                  <button
                    type="submit"
                    className="flex w-full justify-center rounded-md bg-primary-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-primary-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primary-600"
                  >
                    Create Restaurant Account
                  </button>
                </div>
              </form>
            </div>
          </div>

          {/* Right side - Features */}
          <div className="mt-12 lg:mt-0">
            <div className="bg-primary-50 rounded-lg p-8">
              <h3 className="text-2xl font-bold text-gray-900 mb-6">
                Everything you need to get started
              </h3>
              <ul className="space-y-4">
                {features.map((feature, index) => (
                  <li key={index} className="flex items-start">
                    <CheckIcon className="h-5 w-5 text-primary-600 mt-0.5 mr-3 flex-shrink-0" />
                    <span className="text-gray-700">{feature}</span>
                  </li>
                ))}
              </ul>

              <div className="mt-8 bg-white rounded-lg p-6">
                <h4 className="text-lg font-semibold text-gray-900 mb-4">
                  Free Trial Includes:
                </h4>
                <ul className="space-y-2 text-sm text-gray-600">
                  <li>• Up to 100 orders per month</li>
                  <li>• Basic analytics dashboard</li>
                  <li>• QR code generation</li>
                  <li>• Email support</li>
                  <li>• No setup fees</li>
                </ul>
              </div>

              <div className="mt-6 text-center">
                <Link 
                  href="/" 
                  className="inline-flex items-center text-sm text-gray-600 hover:text-gray-900"
                >
                  <ArrowLeftIcon className="w-4 h-4 mr-2" />
                  Back to home
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
