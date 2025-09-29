import { Metadata } from 'next';
import { notFound } from 'next/navigation';
import GuestMenuInterface from '@/components/guest/GuestMenuInterface';
import { tableApi, restaurantApi } from '@/services/api';

interface PageProps {
  params: {
    slug: string;
    qrCode: string;
  };
}

export async function generateMetadata({ params }: PageProps): Promise<Metadata> {
  try {
    const restaurant = await restaurantApi.getBySlug(params.slug);
    return {
      title: `${restaurant.name} - Menu`,
      description: `Browse the menu and place your order at ${restaurant.name}`,
    };
  } catch {
    return {
      title: 'Restaurant Menu',
      description: 'Browse the menu and place your order',
    };
  }
}

export default async function TableMenuPage({ params }: PageProps) {
  try {
    // Fetch restaurant and table data
    const [restaurant, table] = await Promise.all([
      restaurantApi.getBySlug(params.slug),
      tableApi.getByQrCode(params.qrCode),
    ]);

    // Verify the table belongs to the restaurant
    if (table.restaurantId !== restaurant.id) {
      notFound();
    }

    return (
      <div className="min-h-screen bg-gray-50">
        <GuestMenuInterface 
          restaurant={restaurant} 
          table={table} 
          qrCode={params.qrCode}
        />
      </div>
    );
  } catch (error) {
    notFound();
  }
}
