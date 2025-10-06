// Common types for the Numa platform

export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  phone?: string;
  role: UserRole;
  status: string;
  restaurantId: string;
  restaurantName: string;
  lastLoginAt?: string;
  emailVerified: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Restaurant {
  id: string;
  name: string;
  slug: string;
  description?: string;
  email: string;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  state?: string;
  postalCode?: string;
  country?: string;
  currencyCode: string;
  languageCode: string;
  timezone: string;
  logoUrl?: string;
  bannerUrl?: string;
  brandColor: string;
  status: RestaurantStatus;
  subscriptionPlan: string;
  subscriptionExpiresAt?: string;
  deliveryEnabled: boolean;
  takeawayEnabled: boolean;
  dineInEnabled: boolean;
  deliveryRadiusKm?: number;
  deliveryFee?: number;
  minimumOrderAmount?: number;
  taxRate: number;
  serviceChargeRate: number;
  createdAt: string;
  updatedAt: string;
}

export interface RestaurantTable {
  id: string;
  restaurantId: string;
  tableNumber: string;
  capacity: number;
  locationDescription?: string;
  qrCode: string;
  qrCodeUrl?: string;
  status: TableStatus;
  currentSessionId?: string;
  lastCleanedAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface MenuCategory {
  id: string;
  restaurantId: string;
  name: string;
  description?: string;
  imageUrl?: string;
  sortOrder: number;
  isActive: boolean;
  availableFrom?: string;
  availableUntil?: string;
  menuItems?: MenuItem[];
  createdAt: string;
  updatedAt: string;
}

export interface MenuItem {
  id: string;
  restaurantId: string;
  categoryId: string;
  name: string;
  description?: string;
  price: number;
  imageUrl?: string;
  preparationTimeMinutes: number;
  calories?: number;
  isVegetarian: boolean;
  isVegan: boolean;
  isGlutenFree: boolean;
  isSpicy: boolean;
  spiceLevel: number;
  allergens?: string;
  ingredients?: string;
  tags?: string;
  sortOrder: number;
  isActive: boolean;
  isAvailable: boolean;
  availableFrom?: string;
  availableUntil?: string;
  stockQuantity?: number;
  lowStockThreshold: number;
  variations?: MenuItemVariation[];
  createdAt: string;
  updatedAt: string;
}

export interface MenuItemVariation {
  id: string;
  menuItemId: string;
  name: string;
  description?: string;
  priceAdjustment: number;
  isDefault: boolean;
  sortOrder: number;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Order {
  id: string;
  restaurantId: string;
  tableId?: string;
  sessionId?: string;
  orderNumber: string;
  orderType: OrderType;
  status: OrderStatus;
  customerName?: string;
  customerPhone?: string;
  customerEmail?: string;
  specialInstructions?: string;
  subtotal: number;
  taxAmount: number;
  serviceCharge: number;
  deliveryFee: number;
  discountAmount: number;
  totalAmount: number;
  paymentStatus: string;
  paymentMethod?: string;
  estimatedReadyTime?: string;
  readyAt?: string;
  servedAt?: string;
  deliveryAddress?: any;
  orderItems: OrderItem[];
  createdAt: string;
  updatedAt: string;
}

export interface OrderItem {
  id: string;
  orderId: string;
  menuItemId: string;
  menuItem: MenuItem;
  variationId?: string;
  variation?: MenuItemVariation;
  guestName?: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  specialInstructions?: string;
  status: OrderStatus;
  preparedAt?: string;
  servedAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface DiningSession {
  id: string;
  restaurantId: string;
  tableId: string;
  table: RestaurantTable;
  sessionCode: string;
  status: SessionStatus;
  guestCount: number;
  hostName?: string;
  hostPhone?: string;
  specialRequests?: string;
  totalAmount: number;
  tipAmount: number;
  paymentStatus: string;
  waiterCalled: boolean;
  waiterCallTime?: string;
  waiterResponseTime?: string;
  startedAt: string;
  endedAt?: string;
  guests: SessionGuest[];
  orders: Order[];
  billSplits: BillSplit[];
  createdAt: string;
  updatedAt: string;
}

// Guest session response structure from backend
export interface GuestSessionResponse {
  sessionId: string;
  sessionToken: string;
  guestToken: string;
  guestName: string;
  session: {
    id: string;
    sessionCode: string;
    status: SessionStatus;
    guestCount: number;
    hostName?: string;
    hostPhone?: string;
    specialRequests?: string;
    totalAmount: number;
    tipAmount: number;
    paymentStatus: string;
    waiterCalled: boolean;
    waiterCallTime?: string;
    waiterResponseTime?: string;
    startedAt: string;
    endedAt?: string;
    createdAt: string;
    updatedAt: string;
  };
  table: RestaurantTable;
  guests: SessionGuest[];
  cartItems: Order[];
  orders: Order[];
}

export interface SessionGuest {
  id: string;
  sessionId: string;
  guestName: string;
  guestPhone?: string;
  isHost: boolean;
  joinToken: string;
  joinedAt: string;
  lastActivityAt: string;
  createdAt: string;
}

export interface BillSplit {
  id: string;
  sessionId: string;
  guestId: string;
  guest: SessionGuest;
  splitType: BillSplitType;
  amount: number;
  percentage?: number;
  paymentStatus: string;
  paymentMethod?: string;
  paidAt?: string;
  createdAt: string;
  updatedAt: string;
}

// Enums
export enum UserRole {
  OWNER = 'OWNER',
  MANAGER = 'MANAGER',
  STAFF = 'STAFF',
  KITCHEN_STAFF = 'KITCHEN_STAFF',
  WAITER = 'WAITER',
}

export enum RestaurantStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  SUSPENDED = 'SUSPENDED',
  PENDING_APPROVAL = 'PENDING_APPROVAL',
  DEACTIVATED = 'DEACTIVATED',
}

export enum TableStatus {
  AVAILABLE = 'AVAILABLE',
  OCCUPIED = 'OCCUPIED',
  RESERVED = 'RESERVED',
  NEEDS_CLEANING = 'NEEDS_CLEANING',
  OUT_OF_SERVICE = 'OUT_OF_SERVICE',
}

export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  PREPARING = 'PREPARING',
  READY = 'READY',
  SERVED = 'SERVED',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  REFUNDED = 'REFUNDED',
}

export enum OrderType {
  DINE_IN = 'DINE_IN',
  TAKEAWAY = 'TAKEAWAY',
  DELIVERY = 'DELIVERY',
  PRE_ORDER = 'PRE_ORDER',
}

export enum SessionStatus {
  ACTIVE = 'ACTIVE',
  PAUSED = 'PAUSED',
  AWAITING_PAYMENT = 'AWAITING_PAYMENT',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
}

export enum BillSplitType {
  EQUAL = 'EQUAL',
  PERCENTAGE = 'PERCENTAGE',
  CUSTOM = 'CUSTOM',
  ITEM_BASED = 'ITEM_BASED',
}

// API Request/Response types
export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  userId: string;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  restaurantId: string;
  restaurantName: string;
}

export interface RestaurantRegistrationRequest {
  restaurantName: string;
  restaurantEmail: string;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  state?: string;
  postalCode?: string;
  country?: string;
  currencyCode: string;
  timezone: string;
  ownerFirstName: string;
  ownerLastName: string;
  ownerEmail: string;
  ownerPhone?: string;
  password: string;
  confirmPassword: string;
  acceptTerms: boolean;
}

export interface ApiError {
  message: string;
  status: number;
  timestamp: string;
  path: string;
  details?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}

// Cart and ordering types
export interface CartItem {
  menuItem: MenuItem;
  variation?: MenuItemVariation;
  quantity: number;
  specialInstructions?: string;
  guestName?: string;
}

export interface Cart {
  items: CartItem[];
  subtotal: number;
  taxAmount: number;
  serviceCharge: number;
  totalAmount: number;
}

// Analytics types
export interface RestaurantStats {
  totalTables: number;
  totalMenuItems: number;
  totalOrders: number;
  activeOrders: number;
  todayRevenue: number;
  activeSessions: number;
}

export interface OrderStats {
  totalOrders: number;
  pendingOrders: number;
  preparingOrders: number;
  readyOrders: number;
  completedOrders: number;
  cancelledOrders: number;
  averageOrderValue: number;
  totalRevenue: number;
}

// UI State types
export interface LoadingState {
  [key: string]: boolean;
}

export interface ErrorState {
  [key: string]: string | null;
}
