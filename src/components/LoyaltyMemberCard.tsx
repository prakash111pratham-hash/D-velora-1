import React from 'react';
import { useApp } from '../context/AppContext';
import { LOYALTY_TIERS, LoyaltyTierKey } from '../types/models';
import { QrCode, Award, ArrowUpRight } from 'lucide-react';

export const LoyaltyMemberCard: React.FC = () => {
  const { user, setShowQrDialog, setActiveTab } = useApp();

  if (!user) return null;

  const currentTier = LOYALTY_TIERS[user.tier];
  const nextTierKey: LoyaltyTierKey | null =
    user.tier === 'BRONZE' ? 'SILVER' :
    user.tier === 'SILVER' ? 'GOLD' :
    user.tier === 'GOLD' ? 'PLATINUM' : null;

  const nextTier = nextTierKey ? LOYALTY_TIERS[nextTierKey] : null;
  const progress = nextTier
    ? Math.min(100, Math.max(0, (user.loyaltyPoints / nextTier.requiredPoints) * 100))
    : 100;

  return (
    <div className="bg-white rounded-2xl border border-[#DECFC2] p-5 shadow-xs relative overflow-hidden">
      {/* Decorative background glow */}
      <div className="absolute -top-10 -right-10 w-36 h-36 bg-[#C5A059]/10 rounded-full blur-2xl pointer-events-none" />

      <div className="flex items-center justify-between mb-4">
        <div>
          <span className="text-[11px] font-bold uppercase tracking-wider text-[#C5A059] flex items-center gap-1.5">
            <Award className="w-3.5 h-3.5" />
            {currentTier.displayName}
          </span>
          <h3 className="text-base font-bold text-[#2E1810]">
            Welcome back, {user.displayName}
          </h3>
        </div>

        <button
          onClick={() => setShowQrDialog(true)}
          className="flex items-center gap-1.5 bg-[#FAF7F2] hover:bg-[#F1EAE0] border border-[#DECFC2] text-[#2E1810] px-3 py-1.5 rounded-full text-xs font-bold transition-all shadow-2xs active:scale-95"
        >
          <QrCode className="w-3.5 h-3.5 text-[#C5A059]" />
          <span>QR Pass</span>
        </button>
      </div>

      <div className="flex items-baseline justify-between mb-2">
        <div>
          <span className="font-serif text-3xl font-bold text-[#2E1810]">
            {user.loyaltyPoints}
          </span>
          <span className="text-xs text-[#6F5F55] ml-2 font-medium">D Velora Reward Points</span>
        </div>

        {nextTier ? (
          <span className="text-[11px] font-semibold text-[#8D5B28]">
            {nextTier.requiredPoints - user.loyaltyPoints} pts to {nextTier.displayName}
          </span>
        ) : (
          <span className="text-[11px] font-bold text-[#C5A059]">
            ★ Highest VIP Tier Active
          </span>
        )}
      </div>

      {/* Progress Bar */}
      <div className="w-full bg-[#FAF7F2] border border-[#DECFC2]/50 h-2.5 rounded-full overflow-hidden mb-3">
        <div
          className="bg-gradient-to-r from-[#C5A059] to-[#D97706] h-full rounded-full transition-all duration-500"
          style={{ width: `${progress}%` }}
        />
      </div>

      <div className="flex items-center justify-between pt-2 border-t border-[#DECFC2]/50 text-xs text-[#6F5F55]">
        <span className="truncate pr-2">
          Perk: {currentTier.perkDescription}
        </span>
        <button
          onClick={() => setActiveTab('rewards')}
          className="text-[#C5A059] font-bold hover:underline shrink-0 flex items-center gap-0.5"
        >
          Catalog <ArrowUpRight className="w-3 h-3" />
        </button>
      </div>
    </div>
  );
};
