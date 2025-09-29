import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { AuthResponse, LoginRequest, RestaurantRegistrationRequest, ApiError } from '@/types';

// Create axios instance with default configuration
const createApiInstance = (): AxiosInstance => {
  const instance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1',
    timeout: 30000,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  // Request interceptor to add auth token
  instance.interceptors.request.use(
    (config) => {
      const token = getAccessToken();
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  // Response interceptor to handle token refresh and errors
  instance.interceptors.response.use(
    (response) => response,
    async (error) => {
      const originalRequest = error.config;

      // Handle 401 errors (unauthorized)
      if (error.response?.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true;

        try {
          const refreshToken = getRefreshToken();
          if (refreshToken) {
            const response = await refreshAccessToken(refreshToken);
            setTokens(response.accessToken, response.refreshToken);
            originalRequest.headers.Authorization = `Bearer ${response.accessToken}`;
            return instance(originalRequest);
          }
        } catch (refreshError) {
          // Refresh failed, redirect to login
          clearTokens();
          if (typeof window !== 'undefined') {
            window.location.href = '/auth/login';
          }
        }
      }

      // Transform error response
      const apiError: ApiError = {
        message: error.response?.data?.message || error.message || 'An unexpected error occurred',
        status: error.response?.status || 500,
        timestamp: new Date().toISOString(),
        path: error.config?.url || '',
        details: error.response?.data?.details,
      };

      return Promise.reject(apiError);
    }
  );

  return instance;
};

// Create API instance
export const api = createApiInstance();

// Token management functions
const TOKEN_KEY = 'numa_access_token';
const REFRESH_TOKEN_KEY = 'numa_refresh_token';

export const getAccessToken = (): string | null => {
  if (typeof window === 'undefined') return null;
  return localStorage.getItem(TOKEN_KEY);
};

export const getRefreshToken = (): string | null => {
  if (typeof window === 'undefined') return null;
  return localStorage.getItem(REFRESH_TOKEN_KEY);
};

export const setTokens = (accessToken: string, refreshToken: string): void => {
  if (typeof window === 'undefined') return;
  localStorage.setItem(TOKEN_KEY, accessToken);
  localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
};

export const clearTokens = (): void => {
  if (typeof window === 'undefined') return;
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
};

// Authentication API
export const authApi = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/login', credentials);
    return response.data;
  },

  refreshToken: async (refreshToken: string): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/refresh', {
      refreshToken,
    });
    return response.data;
  },

  getCurrentUser: async () => {
    const response = await api.get('/auth/me');
    return response.data;
  },

  logout: async (): Promise<void> => {
    await api.post('/auth/logout');
    clearTokens();
  },
};

// Restaurant API
export const restaurantApi = {
  register: async (data: RestaurantRegistrationRequest) => {
    const response = await api.post('/restaurants/register', data);
    return response.data;
  },

  getById: async (id: string) => {
    const response = await api.get(`/restaurants/${id}`);
    return response.data;
  },

  getBySlug: async (slug: string) => {
    const response = await api.get(`/restaurants/by-slug/${slug}`);
    return response.data;
  },

  update: async (id: string, data: any) => {
    const response = await api.put(`/restaurants/${id}`, data);
    return response.data;
  },

  getStats: async (id: string) => {
    const response = await api.get(`/restaurants/${id}/stats`);
    return response.data;
  },

  canAcceptOrders: async (id: string): Promise<boolean> => {
    const response = await api.get(`/restaurants/${id}/can-accept-orders`);
    return response.data;
  },
};

// Menu API
export const menuApi = {
  getCategories: async (restaurantId: string) => {
    const response = await api.get(`/restaurants/${restaurantId}/menu/categories`);
    return response.data;
  },

  getPublicMenu: async (restaurantSlug: string) => {
    const response = await api.get(`/public/restaurants/${restaurantSlug}/menu`);
    return response.data;
  },

  createCategory: async (restaurantId: string, data: any) => {
    const response = await api.post(`/restaurants/${restaurantId}/menu/categories`, data);
    return response.data;
  },

  updateCategory: async (restaurantId: string, categoryId: string, data: any) => {
    const response = await api.put(`/restaurants/${restaurantId}/menu/categories/${categoryId}`, data);
    return response.data;
  },

  deleteCategory: async (restaurantId: string, categoryId: string) => {
    await api.delete(`/restaurants/${restaurantId}/menu/categories/${categoryId}`);
  },

  getItems: async (restaurantId: string, categoryId?: string) => {
    const url = categoryId 
      ? `/restaurants/${restaurantId}/menu/categories/${categoryId}/items`
      : `/restaurants/${restaurantId}/menu/items`;
    const response = await api.get(url);
    return response.data;
  },

  createItem: async (restaurantId: string, categoryId: string, data: any) => {
    const response = await api.post(`/restaurants/${restaurantId}/menu/categories/${categoryId}/items`, data);
    return response.data;
  },

  updateItem: async (restaurantId: string, itemId: string, data: any) => {
    const response = await api.put(`/restaurants/${restaurantId}/menu/items/${itemId}`, data);
    return response.data;
  },

  deleteItem: async (restaurantId: string, itemId: string) => {
    await api.delete(`/restaurants/${restaurantId}/menu/items/${itemId}`);
  },
};

// Table API
export const tableApi = {
  getAll: async (restaurantId: string) => {
    const response = await api.get(`/restaurants/${restaurantId}/tables`);
    return response.data;
  },

  getByQrCode: async (qrCode: string) => {
    const response = await api.get(`/public/tables/qr/${qrCode}`);
    return response.data;
  },

  create: async (restaurantId: string, data: any) => {
    const response = await api.post(`/restaurants/${restaurantId}/tables`, data);
    return response.data;
  },

  update: async (restaurantId: string, tableId: string, data: any) => {
    const response = await api.put(`/restaurants/${restaurantId}/tables/${tableId}`, data);
    return response.data;
  },

  delete: async (restaurantId: string, tableId: string) => {
    await api.delete(`/restaurants/${restaurantId}/tables/${tableId}`);
  },

  generateQrCode: async (restaurantId: string, tableId: string) => {
    const response = await api.post(`/restaurants/${restaurantId}/tables/${tableId}/qr/generate`);
    return response.data;
  },
};

// Session API
export const sessionApi = {
  create: async (tableId: string, data: any) => {
    const response = await api.post(`/guest/tables/${tableId}/sessions`, data);
    return response.data;
  },

  getByCode: async (sessionCode: string) => {
    const response = await api.get(`/guest/sessions/${sessionCode}`);
    return response.data;
  },

  joinSession: async (sessionCode: string, guestData: any) => {
    const response = await api.post(`/guest/sessions/${sessionCode}/join`, guestData);
    return response.data;
  },

  callWaiter: async (sessionCode: string) => {
    const response = await api.post(`/guest/sessions/${sessionCode}/call-waiter`);
    return response.data;
  },

  requestBill: async (sessionCode: string) => {
    const response = await api.post(`/guest/sessions/${sessionCode}/request-bill`);
    return response.data;
  },

  // Restaurant session management
  getActiveSessions: async (restaurantId: string) => {
    const response = await api.get(`/restaurants/${restaurantId}/sessions/active`);
    return response.data;
  },

  closeSession: async (restaurantId: string, sessionId: string) => {
    const response = await api.post(`/restaurants/${restaurantId}/sessions/${sessionId}/close`);
    return response.data;
  },
};

// Order API
export const orderApi = {
  create: async (sessionCode: string, orderData: any) => {
    const response = await api.post(`/guest/sessions/${sessionCode}/orders`, orderData);
    return response.data;
  },

  getBySession: async (sessionCode: string) => {
    const response = await api.get(`/guest/sessions/${sessionCode}/orders`);
    return response.data;
  },

  // Restaurant order management
  getAll: async (restaurantId: string, params?: any) => {
    const response = await api.get(`/restaurants/${restaurantId}/orders`, { params });
    return response.data;
  },

  getById: async (restaurantId: string, orderId: string) => {
    const response = await api.get(`/restaurants/${restaurantId}/orders/${orderId}`);
    return response.data;
  },

  updateStatus: async (restaurantId: string, orderId: string, status: string) => {
    const response = await api.patch(`/restaurants/${restaurantId}/orders/${orderId}/status`, {
      status,
    });
    return response.data;
  },

  cancel: async (restaurantId: string, orderId: string) => {
    const response = await api.post(`/restaurants/${restaurantId}/orders/${orderId}/cancel`);
    return response.data;
  },
};

// Analytics API
export const analyticsApi = {
  getRestaurantStats: async (restaurantId: string, period?: string) => {
    const response = await api.get(`/restaurants/${restaurantId}/analytics/stats`, {
      params: { period },
    });
    return response.data;
  },

  getOrderStats: async (restaurantId: string, startDate?: string, endDate?: string) => {
    const response = await api.get(`/restaurants/${restaurantId}/analytics/orders`, {
      params: { startDate, endDate },
    });
    return response.data;
  },

  getPopularItems: async (restaurantId: string, limit?: number) => {
    const response = await api.get(`/restaurants/${restaurantId}/analytics/popular-items`, {
      params: { limit },
    });
    return response.data;
  },
};

// Helper function to refresh access token
const refreshAccessToken = async (refreshToken: string): Promise<AuthResponse> => {
  const response = await axios.post<AuthResponse>(
    `${process.env.NEXT_PUBLIC_API_URL}/auth/refresh`,
    { refreshToken },
    {
      headers: { 'Content-Type': 'application/json' },
    }
  );
  return response.data;
};

// Export default API instance for custom requests
export default api;
