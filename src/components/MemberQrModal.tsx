import React from 'react';
import { useApp } from '../context/AppContext';
import { LOYALTY_TIERS } from '../types/models';
import { X, Sparkles } from 'lucide-react';

export const MemberQrModal: React.FC = () => {
  const { user, showQrDialog, setShowQrDialog } = useApp();

  if (!showQrDialog || !user) return null;

  const tier = LOYALTY_TIERS[user.tier];
  const memberId = 'DV-' + (user.id?.substring(0, 8).toUpperCase() || 'PATRON01');

  return (
    <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-xs flex items-center justify-center p-4">
      <div className="bg-white rounded-3xl max-w-sm w-full p-6 shadow-2xl border border-[#DECFC2] relative text-center">
        <button
          onClick={() => setShowQrDialog(false)}
          className="absolute top-4 right-4 p-1.5 rounded-full hover:bg-[#FAF7F2] text-[#6F5F55]"
        >
          <X className="w-5 h-5" />
        </button>

        <span className="text-[10px] font-bold uppercase tracking-widest text-[#C5A059] block mb-1">
          D VELORA MEMBER PASS
        </span>
        <h3 className="font-serif font-bold text-xl text-[#2E1810]">
          {user.displayName}
        </h3>
        <p className="text-xs text-[#8D5B28] font-semibold mt-0.5">
          {tier.displayName} • {user.loyaltyPoints} Reward Points
        </p>

        {/* Visual Simulated QR Pass Canvas */}
        <div className="my-5 p-4 bg-white rounded-2xl border-2 border-[#2E1810] inline-block shadow-xs">
          <svg
            className="w-44 h-44 mx-auto"
            viewBox="0 0 110 110"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            {/* Background */}
            <rect width="110" height="110" fill="white" />

            {/* Corner 1: Top-Left */}
            <rect x="10" y="10" width="28" height="28" fill="#2E1810" rx="3" />
            <rect x="16" y="16" width="16" height="16" fill="white" />
            <rect x="20" y="20" width="8" height="8" fill="#2E1810" />

            {/* Corner 2: Top-Right */}
            <rect x="72" y="10" width="28" height="28" fill="#2E1810" rx="3" />
            <rect x="78" y="16" width="16" height="16" fill="white" />
            <rect x="82" y="20" width="8" height="8" fill="#2E1810" />

            {/* Corner 3: Bottom-Left */}
            <rect x="10" y="72" width="28" height="28" fill="#2E1810" rx="3" />
            <rect x="16" y="78" width="16" height="16" fill="white" />
            <rect x="20" y="82" width="8" height="8" fill="#2E1810" />

            {/* Simulated Data Matrix Bits */}
            <rect x="46" y="12" width="6" height="6" fill="#2E1810" />
            <rect x="56" y="18" width="8" height="6" fill="#2E1810" />
            <rect x="44" y="26" width="6" height="8" fill="#2E1810" />
            <rect x="54" y="36" width="8" height="8" fill="#C5A059" rx="1" />
            <rect x="24" y="44" width="6" height="6" fill="#2E1810" />
            <rect x="36" y="44" width="8" height="6" fill="#2E1810" />
            <rect x="68" y="46" width="6" height="6" fill="#2E1810" />
            <rect x="80" y="44" width="8" height="6" fill="#2E1810" />
            <rect x="46" y="56" width="6" height="8" fill="#2E1810" />
            <rect x="60" y="60" width="8" height="6" fill="#2E1810" />
            <rect x="74" y="58" width="6" height="6" fill="#2E1810" />
            <rect x="44" y="74" width="8" height="6" fill="#2E1810" />
            <rect x="58" y="76" width="6" height="8" fill="#2E1810" />
            <rect x="72" y="74" width="6" height="6" fill="#2E1810" />
            <rect x="84" y="76" width="8" height="6" fill="#2E1810" />
            <rect x="50" y="88" width="8" height="8" fill="#2E1810" />
            <rect x="64" y="90" width="6" height="6" fill="#2E1810" />
            <rect x="76" y="88" width="8" height="6" fill="#2E1810" />
          </svg>
        </div>

        <div className="bg-[#FAF7F2] p-3 rounded-xl border border-[#DECFC2] mb-4">
          <p className="font-mono font-bold text-xs text-[#2E1810] tracking-widest">
            {memberId}
          </p>
          <p className="text-[10px] text-[#6F5F55] mt-0.5">
            Scan at cashier or present to waiter for instant points & voucher claims
          </p>
        </div>

        <button
          onClick={() => setShowQrDialog(false)}
          className="w-full py-2.5 bg-[#2E1810] hover:bg-[#4A2810] text-white text-xs font-bold rounded-xl transition-colors"
        >
          Done
        </button>
      </div>
    </div>
  );
};
