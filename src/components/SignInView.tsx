import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { Coffee, Utensils, CalendarDays, Award, ArrowRight, Sparkles } from 'lucide-react';

export const SignInView: React.FC = () => {
  const { signIn, isLoadingAuth } = useApp();
  const [customName, setCustomName] = useState('Sreya Sharma');
  const [customEmail, setCustomEmail] = useState('sreya0913@gmail.com');
  const [showCustomFields, setShowCustomFields] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleGoogleSignIn = async () => {
    try {
      setErrorMessage(null);
      await signIn(customName, customEmail);
    } catch (err: any) {
      console.error('Sign in error:', err);
      setErrorMessage(err?.message || 'Sign in encountered an issue. Please try again.');
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-[#FAF7F2] to-[#F1EAE0] flex flex-col justify-between p-6 sm:p-10">
      <div className="max-w-md w-full mx-auto flex-1 flex flex-col justify-center">
        {/* Logo and Monogram */}
        <div className="text-center mb-8">
          <div className="inline-block relative">
            <div className="w-24 h-24 sm:w-28 sm:h-28 mx-auto rounded-3xl bg-white border-2 border-[#C5A059]/50 shadow-md p-3 flex items-center justify-center overflow-hidden">
              <img
                src="/images/d_velora_logo.png"
                alt="D Velora Monogram"
                className="w-full h-full object-contain"
                onError={(e) => {
                  (e.target as HTMLImageElement).src = '/images/dvelora_logo.png';
                }}
              />
            </div>
            <div className="absolute -bottom-2 -right-2 bg-[#D97706] text-white p-1.5 rounded-full shadow-md">
              <Sparkles className="w-4 h-4" />
            </div>
          </div>

          <h1 className="mt-4 font-serif text-3xl sm:text-4xl font-bold tracking-wider text-[#2E1810]">
            D VELORA
          </h1>
          <p className="text-xs font-bold tracking-widest text-[#C5A059] uppercase mt-1">
            Cafe • Restaurant • Est. 2026
          </p>
          <p className="text-xs text-[#6F5F55] mt-2 max-w-xs mx-auto">
            Artisanal coffee, authentic dining, and rewarding loyalty experiences tailored for our guests.
          </p>
        </div>

        {/* Brand Value Props Card */}
        <div className="bg-white/90 backdrop-blur-xs rounded-2xl border border-[#DECFC2] p-5 shadow-sm space-y-3.5 mb-6">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-full bg-[#C5A059]/15 flex items-center justify-center shrink-0">
              <Coffee className="w-4 h-4 text-[#C5A059]" />
            </div>
            <div>
              <h3 className="text-xs font-bold text-[#2E1810]">Artisanal Coffee & Tea</h3>
              <p className="text-[11px] text-[#6F5F55]">Home of the viral Almond Blueberry Latte & nitro cold brews</p>
            </div>
          </div>

          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-full bg-[#C5A059]/15 flex items-center justify-center shrink-0">
              <Utensils className="w-4 h-4 text-[#C5A059]" />
            </div>
            <div>
              <h3 className="text-xs font-bold text-[#2E1810]">Family Restaurant Menu</h3>
              <p className="text-[11px] text-[#6F5F55]">Authentic tandoori, handi curries, dum biryanis & fast food rolls</p>
            </div>
          </div>

          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-full bg-[#C5A059]/15 flex items-center justify-center shrink-0">
              <CalendarDays className="w-4 h-4 text-[#C5A059]" />
            </div>
            <div>
              <h3 className="text-xs font-bold text-[#2E1810]">Instant Table Booking</h3>
              <p className="text-[11px] text-[#6F5F55]">Reserve patio, booth, or main dining tables with instant confirmation</p>
            </div>
          </div>

          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-full bg-[#C5A059]/15 flex items-center justify-center shrink-0">
              <Award className="w-4 h-4 text-[#C5A059]" />
            </div>
            <div>
              <h3 className="text-xs font-bold text-[#2E1810]">Loyalty Club & Rewards</h3>
              <p className="text-[11px] text-[#6F5F55]">Earn points on every meal to unlock free drinks and dining vouchers</p>
            </div>
          </div>
        </div>

        {/* Error Feedback */}
        {errorMessage && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-xs rounded-xl text-center">
            {errorMessage}
          </div>
        )}

        {/* Custom fields toggle */}
        {showCustomFields && (
          <div className="mb-4 bg-white p-4 rounded-xl border border-[#DECFC2] space-y-3">
            <div>
              <label className="block text-[11px] font-semibold text-[#6F5F55] mb-1">Your Name</label>
              <input
                type="text"
                value={customName}
                onChange={(e) => setCustomName(e.target.value)}
                className="w-full text-xs px-3 py-2 border border-[#DECFC2] rounded-lg focus:outline-none focus:border-[#C5A059]"
                placeholder="Guest Name"
              />
            </div>
            <div>
              <label className="block text-[11px] font-semibold text-[#6F5F55] mb-1">Email Address</label>
              <input
                type="email"
                value={customEmail}
                onChange={(e) => setCustomEmail(e.target.value)}
                className="w-full text-xs px-3 py-2 border border-[#DECFC2] rounded-lg focus:outline-none focus:border-[#C5A059]"
                placeholder="Email address"
              />
            </div>
          </div>
        )}

        {/* Sign In CTA Buttons */}
        <div className="space-y-2.5">
          <button
            onClick={handleGoogleSignIn}
            disabled={isLoadingAuth}
            className="w-full h-13 bg-[#2E1810] hover:bg-[#4A2810] active:scale-[0.99] text-white rounded-xl font-semibold flex items-center justify-center gap-3 shadow-md transition-all cursor-pointer disabled:opacity-75"
          >
            {isLoadingAuth ? (
              <div className="flex items-center gap-2">
                <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                <span className="text-sm">Entering D Velora...</span>
              </div>
            ) : (
              <>
                <div className="w-6 h-6 rounded-full bg-white flex items-center justify-center text-[#2E1810] font-bold text-xs shadow-xs">
                  G
                </div>
                <span className="text-sm tracking-wide">Sign In with Google</span>
                <ArrowRight className="w-4 h-4 text-[#C5A059]" />
              </>
            )}
          </button>

          <button
            type="button"
            onClick={() => setShowCustomFields(!showCustomFields)}
            className="w-full text-center text-xs font-semibold text-[#8D5B28] hover:underline py-1"
          >
            {showCustomFields ? 'Hide Patron Details' : 'Custom guest name & email'}
          </button>
        </div>

        <p className="text-center text-[11px] text-[#6F5F55] mt-4 font-medium">
          ★ <span className="font-bold text-[#D97706]">150 Welcome Loyalty Points</span> automatically credited upon entry
        </p>
      </div>

      <div className="text-center text-[10px] text-[#6F5F55]/70 py-4">
        Protected by D Velora Patron Security • 42 Promenade Boulevard
      </div>
    </div>
  );
};
