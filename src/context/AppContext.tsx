import React, { createContext, useContext, useState, useEffect } from 'react';
import {
  UserProfile,
  CartItem,
  MenuItem,
  PortionSelection,
  Reservation,
  RewardVoucher,
  Review,
  SpecialOffer,
  LoyaltyTierKey,
  LOYALTY_TIERS
} from '../types/models';
import { SPECIAL_OFFERS, INITIAL_REVIEWS } from '../data/menuData';

interface AppContextType {
  user: UserProfile | null;
  isLoadingAuth: boolean;
  signIn: (name?: string, email?: string) => Promise<void>;
  signOut: () => void;
  activeTab: 'home' | 'menu' | 'reserve' | 'rewards' | 'concierge' | 'reviews';
  setActiveTab: (tab: 'home' | 'menu' | 'reserve' | 'rewards' | 'concierge' | 'reviews') => void;
  
  // Cart
  cart: CartItem[];
  addToCart: (item: MenuItem, portion?: PortionSelection, quantity?: number) => void;
  updateCartQty: (itemId: string, portion: PortionSelection, delta: number) => void;
  clearCart: () => void;
  cartCount: number;
  cartTotal: number;

  // Reservations
  reservations: Reservation[];
  bookTable: (
    guestName: string,
    guestPhone: string,
    partySize: number,
    seatingArea: string,
    date: string,
    timeSlot: string,
    specialRequests?: string
  ) => Promise<{ success: boolean; message: string }>;
  cancelReservation: (id: string) => void;

  // Loyalty & Rewards
  vouchers: RewardVoucher[];
  redeemReward: (title: string, pointsCost: number) => { success: boolean; message: string };

  // Reviews
  reviews: Review[];
  addReview: (
    authorName: string,
    rating: number,
    foodRating: number,
    serviceRating: number,
    comment: string
  ) => void;

  // Offers
  offers: SpecialOffer[];

  // Dialogs / Sheets
  showCartSheet: boolean;
  setShowCartSheet: (open: boolean) => void;
  showQrDialog: boolean;
  setShowQrDialog: (open: boolean) => void;
  showOffersDialog: boolean;
  setShowOffersDialog: (open: boolean) => void;
  toastMessage: string | null;
  setToastMessage: (msg: string | null) => void;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

const STORAGE_KEY_USER = 'dvelora_user_profile';
const STORAGE_KEY_RESERVATIONS = 'dvelora_reservations';
const STORAGE_KEY_VOUCHERS = 'dvelora_vouchers';
const STORAGE_KEY_REVIEWS = 'dvelora_reviews';
const STORAGE_KEY_CART = 'dvelora_cart';

export const AppProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<UserProfile | null>(() => {
    try {
      const saved = localStorage.getItem(STORAGE_KEY_USER);
      if (saved) return JSON.parse(saved);
    } catch (e) {
      console.warn('Failed to parse saved user:', e);
    }
    // Default logged in patron for instant delightful access
    return {
      id: 'usr_patron_01',
      userId: 'usr_patron_01',
      displayName: 'Sreya Sharma',
      email: 'sreya.patron@gmail.com',
      loyaltyPoints: 180,
      tier: 'BRONZE',
      createdAt: new Date().toISOString()
    };
  });

  const [isLoadingAuth, setIsLoadingAuth] = useState(false);
  const [activeTab, setActiveTab] = useState<'home' | 'menu' | 'reserve' | 'rewards' | 'concierge' | 'reviews'>('home');

  // Cart State
  const [cart, setCart] = useState<CartItem[]>(() => {
    try {
      const saved = localStorage.getItem(STORAGE_KEY_CART);
      if (saved) return JSON.parse(saved);
    } catch (e) {
      console.warn('Failed to parse saved cart:', e);
    }
    return [];
  });

  // Reservations State
  const [reservations, setReservations] = useState<Reservation[]>(() => {
    try {
      const saved = localStorage.getItem(STORAGE_KEY_RESERVATIONS);
      if (saved) return JSON.parse(saved);
    } catch (e) {
      console.warn('Failed to parse saved reservations:', e);
    }
    return [
      {
        id: 'res_sample_01',
        userId: 'usr_patron_01',
        guestName: 'Sreya Sharma',
        guestPhone: '+91 98765 43210',
        partySize: 2,
        seatingArea: 'Patio Cafe Terrace',
        date: 'Today, 7:30 PM',
        timeSlot: '19:30',
        specialRequests: 'Near garden flowers, outdoor terrace',
        status: 'CONFIRMED',
        createdAt: new Date().toISOString()
      }
    ];
  });

  // Vouchers State
  const [vouchers, setVouchers] = useState<RewardVoucher[]>(() => {
    try {
      const saved = localStorage.getItem(STORAGE_KEY_VOUCHERS);
      if (saved) return JSON.parse(saved);
    } catch (e) {
      console.warn('Failed to parse saved vouchers:', e);
    }
    return [
      {
        id: 'vouch_init_01',
        userId: 'usr_patron_01',
        title: 'Free Almond Blueberry Latte',
        voucherCode: 'DV-LATTE-8821',
        pointsSpent: 150,
        status: 'ACTIVE',
        createdAt: new Date().toISOString()
      }
    ];
  });

  // Reviews State
  const [reviews, setReviews] = useState<Review[]>(() => {
    try {
      const saved = localStorage.getItem(STORAGE_KEY_REVIEWS);
      if (saved) return JSON.parse(saved);
    } catch (e) {
      console.warn('Failed to parse saved reviews:', e);
    }
    return INITIAL_REVIEWS;
  });

  // Modals & Notifications
  const [showCartSheet, setShowCartSheet] = useState(false);
  const [showQrDialog, setShowQrDialog] = useState(false);
  const [showOffersDialog, setShowOffersDialog] = useState(false);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  // Sync to localStorage
  useEffect(() => {
    if (user) {
      localStorage.setItem(STORAGE_KEY_USER, JSON.stringify(user));
    } else {
      localStorage.removeItem(STORAGE_KEY_USER);
    }
  }, [user]);

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY_CART, JSON.stringify(cart));
  }, [cart]);

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY_RESERVATIONS, JSON.stringify(reservations));
  }, [reservations]);

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY_VOUCHERS, JSON.stringify(vouchers));
  }, [vouchers]);

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY_REVIEWS, JSON.stringify(reviews));
  }, [reviews]);

  // Toast auto-clear
  useEffect(() => {
    if (toastMessage) {
      const timer = setTimeout(() => setToastMessage(null), 4000);
      return () => clearTimeout(timer);
    }
  }, [toastMessage]);

  // Auth Functions
  const signIn = async (name?: string, email?: string) => {
    setIsLoadingAuth(true);
    // Simulate auth token exchange safely
    await new Promise((r) => setTimeout(r, 600));

    const finalName = name?.trim() || 'Sreya Sharma';
    const finalEmail = email?.trim() || 'sreya.patron@gmail.com';
    const uid = 'usr_' + Math.random().toString(36).substring(2, 9);

    const newUser: UserProfile = {
      id: uid,
      userId: uid,
      displayName: finalName,
      email: finalEmail,
      loyaltyPoints: 150, // Welcome points
      tier: 'BRONZE',
      createdAt: new Date().toISOString()
    };

    setUser(newUser);
    setIsLoadingAuth(false);
    setToastMessage(`Welcome to D Velora, ${finalName}! 150 welcome loyalty points credited.`);
  };

  const signOut = () => {
    setUser(null);
    setToastMessage('Signed out successfully.');
  };

  // Helper to recompute tier
  const computeTier = (points: number): LoyaltyTierKey => {
    if (points >= LOYALTY_TIERS.PLATINUM.requiredPoints) return 'PLATINUM';
    if (points >= LOYALTY_TIERS.GOLD.requiredPoints) return 'GOLD';
    if (points >= LOYALTY_TIERS.SILVER.requiredPoints) return 'SILVER';
    return 'BRONZE';
  };

  // Cart operations
  const addToCart = (item: MenuItem, portion: PortionSelection = 'SINGLE', quantity = 1) => {
    const unitPrice =
      portion === 'HALF'
        ? item.halfPrice ?? item.singlePrice ?? 0
        : portion === 'FULL'
        ? item.fullPrice ?? item.singlePrice ?? 0
        : item.singlePrice ?? item.fullPrice ?? item.halfPrice ?? 0;

    setCart((prev) => {
      const existingIndex = prev.findIndex((ci) => ci.item.id === item.id && ci.portion === portion);
      if (existingIndex > -1) {
        const copy = [...prev];
        const newQty = copy[existingIndex].quantity + quantity;
        copy[existingIndex] = {
          ...copy[existingIndex],
          quantity: newQty,
          totalPrice: newQty * unitPrice
        };
        return copy;
      } else {
        const newItem: CartItem = {
          item,
          portion,
          quantity,
          unitPrice,
          totalPrice: unitPrice * quantity
        };
        return [...prev, newItem];
      }
    });

    setToastMessage(`Added ${item.name} (${portion}) to order`);
  };

  const updateCartQty = (itemId: string, portion: PortionSelection, delta: number) => {
    setCart((prev) => {
      return prev
        .map((ci) => {
          if (ci.item.id === itemId && ci.portion === portion) {
            const nextQty = ci.quantity + delta;
            if (nextQty <= 0) return null;
            return {
              ...ci,
              quantity: nextQty,
              totalPrice: nextQty * ci.unitPrice
            };
          }
          return ci;
        })
        .filter(Boolean) as CartItem[];
    });
  };

  const clearCart = () => setCart([]);

  const cartCount = cart.reduce((sum, item) => sum + item.quantity, 0);
  const cartTotal = cart.reduce((sum, item) => sum + item.totalPrice, 0);

  // Reservation operations
  const bookTable = async (
    guestName: string,
    guestPhone: string,
    partySize: number,
    seatingArea: string,
    date: string,
    timeSlot: string,
    specialRequests?: string
  ) => {
    const newRes: Reservation = {
      id: 'res_' + Math.random().toString(36).substring(2, 9),
      userId: user?.userId || 'guest',
      guestName,
      guestPhone,
      partySize,
      seatingArea,
      date,
      timeSlot,
      specialRequests: specialRequests || null,
      status: 'CONFIRMED',
      createdAt: new Date().toISOString()
    };

    setReservations((prev) => [newRes, ...prev]);

    // Award 50 points
    if (user) {
      const updatedPoints = user.loyaltyPoints + 50;
      setUser({
        ...user,
        loyaltyPoints: updatedPoints,
        tier: computeTier(updatedPoints)
      });
    }

    return {
      success: true,
      message: `Table confirmed for ${partySize} guests on ${date} at ${timeSlot}! +50 loyalty points earned.`
    };
  };

  const cancelReservation = (id: string) => {
    setReservations((prev) =>
      prev.map((r) => (r.id === id ? { ...r, status: 'CANCELLED' } : r))
    );
    setToastMessage('Reservation cancelled.');
  };

  // Rewards operations
  const redeemReward = (title: string, pointsCost: number) => {
    if (!user) {
      return { success: false, message: 'Please sign in to redeem rewards' };
    }

    if (user.loyaltyPoints < pointsCost) {
      return { success: false, message: `Need ${pointsCost} points. You have ${user.loyaltyPoints}.` };
    }

    const newCode = `DV-${Math.random().toString(36).substring(2, 6).toUpperCase()}-${Math.floor(1000 + Math.random() * 9000)}`;
    const newVoucher: RewardVoucher = {
      id: 'vouch_' + Math.random().toString(36).substring(2, 9),
      userId: user.userId,
      title,
      voucherCode: newCode,
      pointsSpent: pointsCost,
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };

    const remainingPoints = user.loyaltyPoints - pointsCost;
    setUser({
      ...user,
      loyaltyPoints: remainingPoints,
      tier: computeTier(remainingPoints)
    });

    setVouchers((prev) => [newVoucher, ...prev]);
    return {
      success: true,
      message: `Redeemed "${title}"! Voucher code: ${newCode}`
    };
  };

  // Review operations
  const addReview = (
    authorName: string,
    rating: number,
    foodRating: number,
    serviceRating: number,
    comment: string
  ) => {
    const newRev: Review = {
      id: 'rev_' + Math.random().toString(36).substring(2, 9),
      userId: user?.userId || 'guest',
      authorName,
      rating,
      foodRating,
      serviceRating,
      comment,
      createdAt: 'Just now'
    };

    setReviews((prev) => [newRev, ...prev]);

    if (user) {
      const updatedPoints = user.loyaltyPoints + 25;
      setUser({
        ...user,
        loyaltyPoints: updatedPoints,
        tier: computeTier(updatedPoints)
      });
    }

    setToastMessage('Thank you for your review! +25 bonus loyalty points awarded.');
  };

  return (
    <AppContext.Provider
      value={{
        user,
        isLoadingAuth,
        signIn,
        signOut,
        activeTab,
        setActiveTab,
        cart,
        addToCart,
        updateCartQty,
        clearCart,
        cartCount,
        cartTotal,
        reservations,
        bookTable,
        cancelReservation,
        vouchers,
        redeemReward,
        reviews,
        addReview,
        offers: SPECIAL_OFFERS,
        showCartSheet,
        setShowCartSheet,
        showQrDialog,
        setShowQrDialog,
        showOffersDialog,
        setShowOffersDialog,
        toastMessage,
        setToastMessage
      }}
    >
      {children}
    </AppContext.Provider>
  );
};

export const useApp = () => {
  const context = useContext(AppContext);
  if (!context) throw new Error('useApp must be used within an AppProvider');
  return context;
};
