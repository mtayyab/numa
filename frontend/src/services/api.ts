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
      console.log('API Request:', config.url, 'Token:', token ? 'Present' : 'Missing');
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
        console.log('401 Unauthorized error:', error.response?.data);
        originalRequest._retry = true;

        try {
          const refreshToken = getRefreshToken();
          console.log('Refresh token available:', refreshToken ? 'Yes' : 'No');
          if (refreshToken) {
            const response = await refreshAccessToken(refreshToken);
            setTokens(response.accessToken, response.refreshToken);
            originalRequest.headers.Authorization = `Bearer ${response.accessToken}`;
            return instance(originalRequest);
          }
        } catch (refreshError) {
          console.log('Token refresh failed:', refreshError);
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
    const response = await api.get(`/guest/restaurants/${restaurantSlug}/menu`);
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
    const response = await api.get(`/guest/tables/${qrCode}`);
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
    const response = await api.post(`/restaurants/${restaurantId}/tables/${tableId}/qr-code`);
    return response.data;
  },
};

// Guest API
export const guestApi = {
  getRestaurantBySlug: async (slug: string) => {
    const response = await api.get(`/guest/restaurants/${slug}`);
    return response.data;
  },

  getTableByQrCode: async (qrCode: string) => {
    const response = await api.get(`/guest/tables/${qrCode}`);
    return response.data;
  },

  getActiveSessionForTable: async (qrCode: string) => {
    const response = await api.get(`/guest/tables/${qrCode}/active-session`);
    return response.data;
  },

  getPublicMenu: async (slug: string) => {
    const response = await api.get(`/guest/restaurants/${slug}/menu`);
    return response.data;
  },

  joinSession: async (data: any) => {
    const response = await api.post(`/guest/sessions/join`, data);
    return response.data;
  },

  getSession: async (sessionId: string) => {
    const response = await api.get(`/guest/sessions/${sessionId}`);
    return response.data;
  },

  addToCart: async (sessionId: string, data: any) => {
    const response = await api.post(`/guest/sessions/${sessionId}/cart`, data);
    return response.data;
  },

  removeFromCart: async (sessionId: string, orderId: string) => {
    const response = await api.delete(`/guest/sessions/${sessionId}/cart/${orderId}`);
    return response.data;
  },

  updateCartItem: async (sessionId: string, orderId: string, data: any) => {
    const response = await api.put(`/guest/sessions/${sessionId}/cart/${orderId}`, data);
    return response.data;
  },

  submitOrder: async (sessionId: string) => {
    const response = await api.post(`/guest/sessions/${sessionId}/orders`);
    return response.data;
  },

  getSessionOrders: async (sessionId: string) => {
    const response = await api.get(`/guest/sessions/${sessionId}/orders`);
    return response.data;
  },

  leaveSession: async (sessionId: string, guestToken: string) => {
    const response = await api.post(`/guest/sessions/${sessionId}/leave`, null, {
      params: { guestToken }
    });
    return response.data;
  },
};

// Session API (for backward compatibility)
export const sessionApi = {
  create: async (tableId: string, data: any) => {
    // This endpoint doesn't exist in our backend, so we'll use joinSession instead
    const response = await guestApi.joinSession({
      tableQrCode: tableId, // Assuming tableId is actually QR code
      guestName: data.hostName,
      restaurantId: data.restaurantId
    });
    return response;
  },

  getByCode: async (sessionCode: string) => {
    const response = await guestApi.getSession(sessionCode);
    return response;
  },

  joinSession: async (sessionCode: string, guestData: any) => {
    const response = await guestApi.joinSession({
      tableQrCode: sessionCode, // Assuming sessionCode is actually QR code
      guestName: guestData.guestName,
      restaurantId: guestData.restaurantId
    });
    return response;
  },

  callWaiter: async (sessionCode: string) => {
    // This endpoint doesn't exist in our backend yet
    throw new Error('Call waiter functionality not implemented yet');
  },

  requestBill: async (sessionCode: string) => {
    // This endpoint doesn't exist in our backend yet
    throw new Error('Request bill functionality not implemented yet');
  },

  // Restaurant session management
  getActiveSessions: async (restaurantId: string) => {
    const response = await api.get(`/sessions/restaurant/${restaurantId}/active`);
    return response.data;
  },

  getSessionHistory: async (restaurantId: string, page: number = 0, size: number = 20) => {
    const response = await api.get(`/sessions/restaurant/${restaurantId}/history`, {
      params: { page, size }
    });
    return response.data;
  },

  getSessionDetails: async (sessionId: string) => {
    const response = await api.get(`/sessions/${sessionId}/details`);
    return response.data;
  },

  closeSession: async (restaurantId: string, sessionId: string) => {
    const response = await api.post(`/sessions/${sessionId}/end`);
    return response.data;
  },

  getSessionAnalytics: async (restaurantId: string, timeRange: string = '30d') => {
    const response = await api.get(`/sessions/restaurant/${restaurantId}/analytics`, {
      params: { timeRange }
    });
    return response.data;
  },
};

// Settings API
export const settingsApi = {
  getRestaurantSettings: async () => {
    const response = await api.get('/settings/restaurant');
    return response.data;
  },

  updateRestaurantSettings: async (settings: any) => {
    const response = await api.put('/settings/restaurant', settings);
    return response.data;
  },
};

// Order API
export const orderApi = {
  create: async (sessionId: string, orderData: any) => {
    const response = await guestApi.addToCart(sessionId, orderData);
    return response;
  },

  getBySession: async (sessionId: string) => {
    const response = await guestApi.getSessionOrders(sessionId);
    return response;
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
