import React from 'react';
import { useApp } from './context/AppContext';
import { Navbar } from './components/Navbar';
import { BottomNav } from './components/BottomNav';
import { SignInView } from './components/SignInView';
import { HomeTab } from './components/HomeTab';
import { MenuTab } from './components/MenuTab';
import { ReservationTab } from './components/ReservationTab';
import { RewardsTab } from './components/RewardsTab';
import { ConciergeTab } from './components/ConciergeTab';
import { ReviewsTab } from './components/ReviewsTab';
import { CartDrawer } from './components/CartDrawer';
import { MemberQrModal } from './components/MemberQrModal';
import { OffersModal } from './components/OffersModal';
import { Check } from 'lucide-react';

export const MainApp: React.FC = () => {
  const { user, activeTab, toastMessage, setToastMessage } = useApp();

  // If user is not logged in, present the sign-in experience
  if (!user) {
    return <SignInView />;
  }

  return (
    <div className="min-h-screen bg-[#FAF7F2] text-[#23160F] flex flex-col selection:bg-[#C5A059] selection:text-white">
      {/* Top App Header */}
      <Navbar />

      {/* Main Content Area */}
      <main className="flex-1 max-w-4xl w-full mx-auto p-4 sm:p-6">
        {activeTab === 'home' && <HomeTab />}
        {activeTab === 'menu' && <MenuTab />}
        {activeTab === 'reserve' && <ReservationTab />}
        {activeTab === 'rewards' && <RewardsTab />}
        {activeTab === 'concierge' && <ConciergeTab />}
        {activeTab === 'reviews' && <ReviewsTab />}
      </main>

      {/* Bottom Tab Navigation */}
      <BottomNav />

      {/* Cart & Checkout Sheet */}
      <CartDrawer />

      {/* Member QR Pass Modal */}
      <MemberQrModal />

      {/* Special Deals & Offers Modal */}
      <OffersModal />

      {/* Toast Alert Notification */}
      {toastMessage && (
        <div className="fixed top-18 left-1/2 -translate-x-1/2 z-50 bg-[#2E1810] text-white text-xs font-semibold px-4 py-2.5 rounded-full shadow-lg border border-[#C5A059] flex items-center gap-2 animate-in fade-in slide-in-from-top-3">
          <div className="w-4 h-4 rounded-full bg-[#16A34A] flex items-center justify-center shrink-0">
            <Check className="w-3 h-3 text-white" />
          </div>
          <span>{toastMessage}</span>
          <button
            onClick={() => setToastMessage(null)}
            className="ml-2 text-white/60 hover:text-white"
          >
            ×
          </button>
        </div>
      )}
    </div>
  );
};
