import React from 'react';
import { useApp } from '../context/AppContext';
import { Bell, QrCode, LogOut, Coffee } from 'lucide-react';
import { LOYALTY_TIERS } from '../types/models';

export const Navbar: React.FC = () => {
  const { user, signOut, setShowQrDialog, setShowOffersDialog, offers, activeTab, setActiveTab } = useApp();
  const tierMeta = user ? LOYALTY_TIERS[user.tier] : null;

  return (
    <header className="sticky top-0 z-40 bg-white/95 backdrop-blur-md border-b border-[#DECFC2]/60 px-4 py-3 sm:px-6 shadow-xs">
      <div className="max-w-6xl mx-auto flex items-center justify-between">
        {/* Brand Logo & Name */}
        <div 
          className="flex items-center gap-3 cursor-pointer select-none"
          onClick={() => setActiveTab('home')}
        >
          <div className="w-11 h-11 rounded-xl bg-white border border-[#C5A059]/40 p-1 flex items-center justify-center shadow-xs overflow-hidden">
            <img 
              src="/images/dvelora_logo.png" 
              alt="D Velora Logo" 
              className="w-full h-full object-contain"
              onError={(e) => {
                // Fallback icon if image path issues
                (e.target as HTMLElement).style.display = 'none';
              }}
            />
            <Coffee className="w-6 h-6 text-[#2E1810] hidden" />
          </div>
          <div>
            <div className="flex items-center gap-2">
              <span className="font-serif font-bold text-lg sm:text-xl tracking-wider text-[#2E1810]">
                D VELORA
              </span>
              <span className="text-[10px] font-semibold uppercase px-1.5 py-0.5 rounded bg-[#FAF7F2] text-[#C5A059] border border-[#DECFC2]">
                Est. 2026
              </span>
            </div>
            <p className="text-[11px] font-medium tracking-wide text-[#8D5B28]">
              Artisanal Cafe & Restaurant
            </p>
          </div>
        </div>

        {/* Right Action Icons */}
        <div className="flex items-center gap-2 sm:gap-3">
          {/* Member Digital Pass Button */}
          {user && (
            <button
              onClick={() => setShowQrDialog(true)}
              className="flex items-center gap-1.5 bg-[#FAF7F2] hover:bg-[#F1EAE0] text-[#2E1810] border border-[#DECFC2] rounded-full px-3 py-1.5 text-xs font-semibold transition-colors shadow-2xs"
              title="Open QR Member Pass"
            >
              <QrCode className="w-4 h-4 text-[#C5A059]" />
              <span className="hidden sm:inline">Pass</span>
              <span className="bg-[#C5A059]/20 text-[#2E1810] px-1.5 py-0.2 rounded-full font-bold text-[11px]">
                {user.loyaltyPoints} pts
              </span>
            </button>
          )}

          {/* Offers & Notifications Bell */}
          <button
            onClick={() => setShowOffersDialog(true)}
            className="relative p-2 rounded-full hover:bg-[#F1EAE0] text-[#2E1810] transition-colors"
            title="Special Offers & Events"
          >
            <Bell className="w-5 h-5" />
            {offers.length > 0 && (
              <span className="absolute top-1 right-1 bg-[#D97706] text-white text-[10px] font-bold w-4 h-4 rounded-full flex items-center justify-center animate-pulse">
                {offers.length}
              </span>
            )}
          </button>

          {/* User Profile or Sign Out */}
          {user ? (
            <div className="flex items-center gap-2 border-l border-[#DECFC2] pl-2 sm:pl-3">
              <div className="hidden md:block text-right">
                <p className="text-xs font-bold text-[#2E1810] leading-tight truncate max-w-[120px]">
                  {user.displayName}
                </p>
                <p className="text-[10px] font-semibold text-[#C5A059] leading-tight">
                  {tierMeta?.displayName || 'Member'}
                </p>
              </div>
              <button
                onClick={signOut}
                className="p-2 rounded-full text-[#6F5F55] hover:text-[#DC2626] hover:bg-red-50 transition-colors"
                title="Sign Out"
              >
                <LogOut className="w-4 h-4" />
              </button>
            </div>
          ) : (
            <button
              onClick={() => setActiveTab('home')}
              className="bg-[#2E1810] hover:bg-[#4A2810] text-white text-xs font-semibold px-4 py-2 rounded-full transition-colors shadow-xs"
            >
              Sign In
            </button>
          )}
        </div>
      </div>
    </header>
  );
};
