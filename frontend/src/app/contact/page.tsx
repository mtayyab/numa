import { Metadata } from 'next';
import Link from 'next/link';
import { ArrowLeftIcon, PhoneIcon, EnvelopeIcon, MapPinIcon } from '@heroicons/react/24/outline';

export const metadata: Metadata = {
  title: 'Contact Us - Numa',
  description: 'Get in touch with the Numa team for support, sales, or general inquiries.',
};

export default function ContactPage() {
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
              Get in Touch
            </h1>
            <p className="mt-4 text-xl text-gray-600 max-w-3xl mx-auto">
              Have questions about Numa? We're here to help. Reach out to our team for support, sales, or general inquiries.
            </p>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="lg:grid lg:grid-cols-2 lg:gap-8">
          {/* Contact Form */}
          <div className="mt-8 lg:mt-0">
            <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
              <div>
                <h2 className="text-3xl font-bold tracking-tight text-gray-900">
                  Send us a message
                </h2>
                <p className="mt-2 text-sm text-gray-600">
                  We'll get back to you within 24 hours.
                </p>
              </div>

              <form className="mt-8 space-y-6" action="#" method="POST">
                <div className="space-y-4">
                  <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                    <div>
                      <label htmlFor="first-name" className="block text-sm font-medium leading-6 text-gray-900">
                        First name
                      </label>
                      <div className="mt-2">
                        <input
                          id="first-name"
                          name="first-name"
                          type="text"
                          required
                          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                        />
                      </div>
                    </div>
                    <div>
                      <label htmlFor="last-name" className="block text-sm font-medium leading-6 text-gray-900">
                        Last name
                      </label>
                      <div className="mt-2">
                        <input
                          id="last-name"
                          name="last-name"
                          type="text"
                          required
                          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                        />
                      </div>
                    </div>
                  </div>

                  <div>
                    <label htmlFor="email" className="block text-sm font-medium leading-6 text-gray-900">
                      Email address
                    </label>
                    <div className="mt-2">
                      <input
                        id="email"
                        name="email"
                        type="email"
                        autoComplete="email"
                        required
                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                      />
                    </div>
                  </div>

                  <div>
                    <label htmlFor="company" className="block text-sm font-medium leading-6 text-gray-900">
                      Company (optional)
                    </label>
                    <div className="mt-2">
                      <input
                        id="company"
                        name="company"
                        type="text"
                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                      />
                    </div>
                  </div>

                  <div>
                    <label htmlFor="subject" className="block text-sm font-medium leading-6 text-gray-900">
                      Subject
                    </label>
                    <div className="mt-2">
                      <select
                        id="subject"
                        name="subject"
                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                      >
                        <option>General Inquiry</option>
                        <option>Sales Question</option>
                        <option>Technical Support</option>
                        <option>Partnership</option>
                        <option>Other</option>
                      </select>
                    </div>
                  </div>

                  <div>
                    <label htmlFor="message" className="block text-sm font-medium leading-6 text-gray-900">
                      Message
                    </label>
                    <div className="mt-2">
                      <textarea
                        id="message"
                        name="message"
                        rows={4}
                        required
                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                        placeholder="Tell us how we can help you..."
                      />
                    </div>
                  </div>
                </div>

                <div>
                  <button
                    type="submit"
                    className="flex w-full justify-center rounded-md bg-primary-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-primary-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primary-600"
                  >
                    Send Message
                  </button>
                </div>
              </form>
            </div>
          </div>

          {/* Contact Information */}
          <div className="mt-12 lg:mt-0">
            <div className="bg-primary-50 rounded-lg p-8">
              <h3 className="text-2xl font-bold text-gray-900 mb-6">
                Contact Information
              </h3>
              
              <div className="space-y-6">
                <div className="flex items-start">
                  <PhoneIcon className="h-6 w-6 text-primary-600 mt-1 mr-4 flex-shrink-0" />
                  <div>
                    <h4 className="text-lg font-semibold text-gray-900">Phone</h4>
                    <p className="text-gray-600">+1 (555) 123-4567</p>
                    <p className="text-sm text-gray-500">Mon-Fri 9AM-6PM EST</p>
                  </div>
                </div>

                <div className="flex items-start">
                  <EnvelopeIcon className="h-6 w-6 text-primary-600 mt-1 mr-4 flex-shrink-0" />
                  <div>
                    <h4 className="text-lg font-semibold text-gray-900">Email</h4>
                    <p className="text-gray-600">hello@numa.app</p>
                    <p className="text-sm text-gray-500">We respond within 24 hours</p>
                  </div>
                </div>

                <div className="flex items-start">
                  <MapPinIcon className="h-6 w-6 text-primary-600 mt-1 mr-4 flex-shrink-0" />
                  <div>
                    <h4 className="text-lg font-semibold text-gray-900">Office</h4>
                    <p className="text-gray-600">
                      123 Restaurant Street<br />
                      San Francisco, CA 94105
                    </p>
                  </div>
                </div>
              </div>

              <div className="mt-8 bg-white rounded-lg p-6">
                <h4 className="text-lg font-semibold text-gray-900 mb-4">
                  Quick Links
                </h4>
                <div className="space-y-2">
                  <Link href="/demo" className="block text-primary-600 hover:text-primary-500">
                    → View Demo
                  </Link>
                  <Link href="/restaurants/register" className="block text-primary-600 hover:text-primary-500">
                    → Start Free Trial
                  </Link>
                  <Link href="/auth/login" className="block text-primary-600 hover:text-primary-500">
                    → Sign In
                  </Link>
                </div>
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

      {/* FAQ Section */}
      <div className="bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
          <div className="text-center mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-4">
              Frequently Asked Questions
            </h2>
            <p className="text-lg text-gray-600">
              Quick answers to common questions
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                How quickly can I get started?
              </h3>
              <p className="text-gray-600">
                You can set up your restaurant and start taking orders within minutes. Our onboarding process is designed to be quick and intuitive.
              </p>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                Is there a free trial?
              </h3>
              <p className="text-gray-600">
                Yes! We offer a free trial with up to 100 orders per month. No credit card required to get started.
              </p>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                Do you support multiple locations?
              </h3>
              <p className="text-gray-600">
                Absolutely! Our multi-tenant architecture is perfect for restaurant chains with multiple locations.
              </p>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                What payment methods do you support?
              </h3>
              <p className="text-gray-600">
                We integrate with all major payment processors including Stripe, PayPal, and Square for flexible payment options.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
