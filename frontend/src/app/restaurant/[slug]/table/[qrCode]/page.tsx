import { Metadata } from 'next';
import GuestMenuInterface from '@/components/guest/GuestMenuInterface';

interface PageProps {
  params: {
    slug: string;
    qrCode: string;
  };
}

export async function generateMetadata({ params }: PageProps): Promise<Metadata> {
  return {
    title: 'Restaurant Menu',
    description: 'Browse the menu and place your order',
  };
}

export default function TableMenuPage({ params }: PageProps) {
  return (
    <div className="min-h-screen bg-gray-50">
      <GuestMenuInterface 
        restaurantSlug={params.slug}
        qrCode={params.qrCode}
      />
    </div>
  );
}
