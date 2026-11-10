export type MenuCategoryKey = 
  | 'SIGNATURE'
  | 'STARTER'
  | 'FAST_FOOD'
  | 'INDIAN_VEG'
  | 'INDIAN_NON_VEG'
  | 'BIRYANI_RICE'
  | 'ROTI_DAL'
  | 'DESSERT_CAFE';

export interface MenuCategoryMeta {
  key: MenuCategoryKey;
  displayName: string;
  iconName: string;
}

export const MENU_CATEGORIES: Record<MenuCategoryKey, MenuCategoryMeta> = {
  SIGNATURE: { key: 'SIGNATURE', displayName: 'House Signatures', iconName: 'Star' },
  STARTER: { key: 'STARTER', displayName: 'Starters', iconName: 'Utensils' },
  FAST_FOOD: { key: 'FAST_FOOD', displayName: 'Fast Food & Rolls', iconName: 'Sandwich' },
  INDIAN_VEG: { key: 'INDIAN_VEG', displayName: 'Indian Veg', iconName: 'Leaf' },
  INDIAN_NON_VEG: { key: 'INDIAN_NON_VEG', displayName: 'Indian Non-Veg', iconName: 'Drumstick' },
  BIRYANI_RICE: { key: 'BIRYANI_RICE', displayName: 'Biryani & Rice', iconName: 'Soup' },
  ROTI_DAL: { key: 'ROTI_DAL', displayName: 'Breads & Dal', iconName: 'Wheat' },
  DESSERT_CAFE: { key: 'DESSERT_CAFE', displayName: 'Cafe & Desserts', iconName: 'Coffee' }
};

export type DietaryType = 'VEG' | 'NON_VEG' | 'CHEF_SPECIAL';

export interface MenuItem {
  id: string;
  name: string;
  description: string;
  category: MenuCategoryKey;
  dietaryType: DietaryType;
  singlePrice?: number | null;
  halfPrice?: number | null;
  fullPrice?: number | null;
  rating: number;
  isAvailable?: boolean;
  badge?: string | null;
}

export type PortionSelection = 'SINGLE' | 'HALF' | 'FULL';

export interface CartItem {
  item: MenuItem;
  portion: PortionSelection;
  quantity: number;
  specialNote?: string;
  unitPrice: number;
  totalPrice: number;
}

export type LoyaltyTierKey = 'BRONZE' | 'SILVER' | 'GOLD' | 'PLATINUM';

export interface LoyaltyTierMeta {
  key: LoyaltyTierKey;
  displayName: string;
  requiredPoints: number;
  perkDescription: string;
}

export const LOYALTY_TIERS: Record<LoyaltyTierKey, LoyaltyTierMeta> = {
  BRONZE: {
    key: 'BRONZE',
    displayName: 'Bronze Guest',
    requiredPoints: 0,
    perkDescription: '5% order discount & birthday treat'
  },
  SILVER: {
    key: 'SILVER',
    displayName: 'Silver Connoisseur',
    requiredPoints: 300,
    perkDescription: '10% order discount + free monthly specialty coffee'
  },
  GOLD: {
    key: 'GOLD',
    displayName: 'Gold VIP',
    requiredPoints: 800,
    perkDescription: '15% off + priority seating + complimentary chef dessert'
  },
  PLATINUM: {
    key: 'PLATINUM',
    displayName: 'Platinum Patron',
    requiredPoints: 1500,
    perkDescription: '20% off all dining + chef table private access'
  }
};

export interface UserProfile {
  id: string;
  userId: string;
  displayName: string;
  email: string;
  photoUrl?: string | null;
  loyaltyPoints: number;
  tier: LoyaltyTierKey;
  createdAt?: string;
}

export interface Reservation {
  id: string;
  userId: string;
  guestName: string;
  guestPhone: string;
  partySize: number;
  seatingArea: string;
  date: string;
  timeSlot: string;
  specialRequests?: string | null;
  status: 'CONFIRMED' | 'CANCELLED';
  createdAt: string;
}

export interface Order {
  id: string;
  userId: string;
  itemSummary: string;
  totalAmount: number;
  orderType: 'DINE_IN_PREORDER' | 'PICKUP';
  paymentMethod: 'UPI' | 'CARD' | 'PAY_AT_COUNTER';
  status: 'PLACED' | 'PREPARING' | 'READY';
  createdAt: string;
}

export interface RewardVoucher {
  id: string;
  userId: string;
  title: string;
  voucherCode: string;
  pointsSpent: number;
  status: 'ACTIVE' | 'REDEEMED';
  createdAt: string;
}

export interface CatalogReward {
  id: string;
  title: string;
  description: string;
  pointsCost: number;
  category: string;
  badge: string;
}

export interface Review {
  id: string;
  userId: string;
  authorName: string;
  rating: number;
  foodRating: number;
  serviceRating: number;
  comment: string;
  createdAt: string;
}

export interface SpecialOffer {
  id: string;
  title: string;
  description: string;
  promoCode: string;
  discountPercent: number;
  validUntil: string;
}

export interface ChatMessage {
  id: string;
  sender: 'user' | 'ai';
  message: string;
  timestamp: string;
}
