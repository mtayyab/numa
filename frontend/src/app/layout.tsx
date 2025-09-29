import type { Metadata, Viewport } from 'next';
import { Inter } from 'next/font/google';
import { Providers } from './providers';
import '@/styles/globals.css';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: {
    default: 'Numa - Modern Restaurant Ordering Platform',
    template: '%s | Numa',
  },
  description: 'Multi-tenant restaurant ordering and management platform with QR-based ordering, group sessions, and real-time management.',
  keywords: [
    'restaurant',
    'ordering',
    'QR code',
    'menu',
    'food service',
    'restaurant management',
    'digital ordering',
    'table service'
  ],
  authors: [{ name: 'Numa Team' }],
  creator: 'Numa',
  publisher: 'Numa',
  formatDetection: {
    email: false,
    address: false,
    telephone: false,
  },
  metadataBase: new URL(process.env.NEXT_PUBLIC_APP_URL || 'http://localhost:3000'),
  alternates: {
    canonical: '/',
  },
  openGraph: {
    type: 'website',
    locale: 'en_US',
    url: process.env.NEXT_PUBLIC_APP_URL,
    siteName: 'Numa',
    title: 'Numa - Modern Restaurant Ordering Platform',
    description: 'Multi-tenant restaurant ordering and management platform with QR-based ordering, group sessions, and real-time management.',
    images: [
      {
        url: '/og-image.png',
        width: 1200,
        height: 630,
        alt: 'Numa - Restaurant Ordering Platform',
      },
    ],
  },
  twitter: {
    card: 'summary_large_image',
    title: 'Numa - Modern Restaurant Ordering Platform',
    description: 'Multi-tenant restaurant ordering and management platform with QR-based ordering, group sessions, and real-time management.',
    images: ['/og-image.png'],
  },
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      'max-video-preview': -1,
      'max-image-preview': 'large',
      'max-snippet': -1,
    },
  },
  verification: {
    google: process.env.GOOGLE_SITE_VERIFICATION,
  },
  icons: {
    icon: [
      { url: '/favicon-16x16.png', sizes: '16x16', type: 'image/png' },
      { url: '/favicon-32x32.png', sizes: '32x32', type: 'image/png' },
    ],
    apple: [
      { url: '/apple-touch-icon.png', sizes: '180x180', type: 'image/png' },
    ],
    other: [
      { rel: 'mask-icon', url: '/safari-pinned-tab.svg', color: '#f97316' },
    ],
  },
  manifest: '/manifest.json',
  appleWebApp: {
    capable: true,
    statusBarStyle: 'default',
    title: 'Numa',
  },
};

export const viewport: Viewport = {
  width: 'device-width',
  initialScale: 1,
  maximumScale: 1,
  userScalable: false,
  themeColor: [
    { media: '(prefers-color-scheme: light)', color: '#f97316' },
    { media: '(prefers-color-scheme: dark)', color: '#ea580c' },
  ],
  colorScheme: 'light dark',
};

interface RootLayoutProps {
  children: React.ReactNode;
}

export default function RootLayout({ children }: RootLayoutProps) {
  return (
    <html lang="en" suppressHydrationWarning>
      <head>
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossOrigin="anonymous" />
      </head>
      <body className={`${inter.className} antialiased`} suppressHydrationWarning>
        <Providers>
          <div id="root" className="min-h-screen bg-gray-50">
            {children}
          </div>
          <div id="modal-root" />
          <div id="toast-root" />
        </Providers>
      </body>
    </html>
  );
}
