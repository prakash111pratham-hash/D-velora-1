import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { CATALOG_REWARDS } from '../data/menuData';
import { LOYALTY_TIERS, LoyaltyTierKey } from '../types/models';
import { 
  Award, 
  QrCode, 
  Gift, 
  Copy, 
  Check, 
  Star, 
  Sparkles,
  Lock,
  ArrowRight
} from 'lucide-react';

export const RewardsTab: React.FC = () => {
  const { user, vouchers, redeemReward, setShowQrDialog, setToastMessage } = useApp();
  const [copiedCode, setCopiedCode] = useState<string | null>(null);

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

  const handleCopy = (code: string) => {
    navigator.clipboard?.writeText(code);
    setCopiedCode(code);
    setToastMessage(`Copied voucher code ${code}`);
    setTimeout(() => setCopiedCode(null), 2500);
  };

  const handleRedeem = (title: string, pointsCost: number) => {
    const res = redeemReward(title, pointsCost);
    setToastMessage(res.message);
  };

  return (
    <div className="space-y-6 pb-28">
      {/* Header */}
      <div>
        <h2 className="font-serif text-2xl sm:text-3xl font-bold text-[#2E1810]">
          Loyalty Club
        </h2>
        <p className="text-xs font-semibold text-[#8D5B28]">
          Dine, earn points & unlock handcrafted treats
        </p>
      </div>

      {/* Digital VIP Membership Card */}
      <div className="rounded-3xl bg-gradient-to-br from-[#2E1810] to-[#4E2B18] text-white p-6 shadow-xl border border-[#C5A059] relative overflow-hidden">
        {/* Decorative corner pattern */}
        <div className="absolute top-0 right-0 w-32 h-32 bg-[#C5A059]/15 rounded-full blur-2xl" />

        <div className="flex items-center justify-between mb-6">
          <div>
            <span className="text-[10px] font-bold tracking-widest uppercase text-[#C5A059] block">
              D VELORA CLUB PASS
            </span>
            <h3 className="font-serif text-xl sm:text-2xl font-bold text-white tracking-wide">
              {user.displayName}
            </h3>
          </div>

          <button
            onClick={() => setShowQrDialog(true)}
            className="w-11 h-11 rounded-2xl bg-[#C5A059] hover:bg-[#d6b26a] text-[#2E1810] flex items-center justify-center transition-all shadow-md active:scale-95"
            title="Scan QR Pass"
          >
            <QrCode className="w-6 h-6 text-[#2E1810]" />
          </button>
        </div>

        <div className="flex items-end justify-between mb-4">
          <div>
            <span className="font-serif text-4xl sm:text-5xl font-bold text-[#C5A059]">
              {user.loyaltyPoints}
            </span>
            <span className="text-xs text-white/70 block mt-0.5">
              Available Reward Points
            </span>
          </div>

          <span className="bg-[#C5A059]/30 border border-[#C5A059] text-[#C5A059] text-xs font-bold px-3 py-1 rounded-full uppercase tracking-wider">
            {currentTier.displayName}
          </span>
        </div>

        {/* Tier Progress Bar */}
        <div className="w-full bg-white/20 h-2.5 rounded-full overflow-hidden mb-2">
          <div
            className="bg-gradient-to-r from-[#C5A059] to-[#D97706] h-full rounded-full transition-all duration-500"
            style={{ width: `${progress}%` }}
          />
        </div>

        <p className="text-[11px] text-white/75">
          {nextTier
            ? `${nextTier.requiredPoints - user.loyaltyPoints} points needed to unlock ${nextTier.displayName}`
            : 'Congratulations! You have reached our highest Platinum Patron VIP status.'}
        </p>
      </div>

      {/* Active Vouchers Section */}
      {vouchers.length > 0 && (
        <div className="space-y-3">
          <h3 className="font-serif font-bold text-base text-[#2E1810] flex items-center gap-2">
            <Gift className="w-4 h-4 text-[#C5A059]" />
            <span>My Active Vouchers ({vouchers.length})</span>
          </h3>

          <div className="space-y-2.5">
            {vouchers.map((voucher) => (
              <div
                key={voucher.id}
                className="bg-white rounded-2xl border border-[#C5A059]/60 p-4 shadow-2xs flex items-center justify-between"
              >
                <div>
                  <h4 className="font-serif font-bold text-sm text-[#2E1810]">
                    {voucher.title}
                  </h4>
                  <p className="text-xs font-mono font-bold tracking-wider text-[#C5A059] mt-0.5">
                    {voucher.voucherCode}
                  </p>
                  <p className="text-[10px] text-[#6F5F55]">
                    Show this code to your waiter or at checkout
                  </p>
                </div>

                <button
                  onClick={() => handleCopy(voucher.voucherCode)}
                  className="flex items-center gap-1.5 bg-[#FAF7F2] hover:bg-[#F1EAE0] border border-[#DECFC2] text-[#2E1810] text-xs font-bold px-3 py-2 rounded-xl transition-all shadow-2xs shrink-0"
                >
                  {copiedCode === voucher.voucherCode ? (
                    <>
                      <Check className="w-3.5 h-3.5 text-[#16A34A]" />
                      <span className="text-[#16A34A]">Copied</span>
                    </>
                  ) : (
                    <>
                      <Copy className="w-3.5 h-3.5 text-[#C5A059]" />
                      <span>Copy</span>
                    </>
                  )}
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Membership Tiers & Benefits */}
      <div className="space-y-3">
        <h3 className="font-serif font-bold text-base text-[#2E1810]">
          Membership Tiers & Perks
        </h3>

        <div className="space-y-2.5">
          {Object.values(LOYALTY_TIERS).map((t) => {
            const isCurrent = user.tier === t.key;
            return (
              <div
                key={t.key}
                className={`p-4 rounded-2xl border transition-all ${
                  isCurrent
                    ? 'bg-[#FAF7F2] border-[#C5A059] shadow-xs'
                    : 'bg-white border-[#DECFC2]'
                }`}
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <Star className={`w-4 h-4 ${isCurrent ? 'text-[#C5A059] fill-[#C5A059]' : 'text-[#6F5F55]'}`} />
                    <span className="font-bold text-xs text-[#2E1810]">{t.displayName}</span>
                  </div>

                  {isCurrent && (
                    <span className="bg-[#C5A059] text-white text-[9px] font-bold uppercase tracking-wider px-2 py-0.5 rounded-full">
                      Current Tier
                    </span>
                  )}
                </div>
                <p className="text-xs text-[#6F5F55] mt-1 pl-6">
                  {t.requiredPoints} points required • {t.perkDescription}
                </p>
              </div>
            );
          })}
        </div>
      </div>

      {/* Rewards Catalog */}
      <div className="space-y-3">
        <h3 className="font-serif font-bold text-base text-[#2E1810]">
          Redeem Rewards Catalog
        </h3>

        <div className="space-y-3">
          {CATALOG_REWARDS.map((reward) => {
            const canAfford = user.loyaltyPoints >= reward.pointsCost;
            return (
              <div
                key={reward.id}
                className="bg-white rounded-2xl border border-[#DECFC2] p-4 shadow-2xs flex items-center justify-between gap-3"
              >
                <div className="flex items-start gap-3">
                  <div className="w-10 h-10 rounded-xl bg-[#FAF7F2] border border-[#DECFC2] flex items-center justify-center shrink-0">
                    <Gift className="w-5 h-5 text-[#C5A059]" />
                  </div>
                  <div>
                    <span className="text-[10px] font-bold text-[#8D5B28] bg-[#FAF7F2] px-2 py-0.5 rounded-md border border-[#DECFC2]">
                      {reward.badge}
                    </span>
                    <h4 className="font-serif font-bold text-sm text-[#2E1810] mt-1">
                      {reward.title}
                    </h4>
                    <p className="text-xs text-[#6F5F55] line-clamp-1">
                      {reward.description}
                    </p>
                  </div>
                </div>

                <button
                  disabled={!canAfford}
                  onClick={() => handleRedeem(reward.title, reward.pointsCost)}
                  className={`px-3.5 py-2 rounded-xl text-xs font-bold shrink-0 transition-all cursor-pointer ${
                    canAfford
                      ? 'bg-[#C5A059] hover:bg-[#d6b26a] active:scale-95 text-white shadow-xs'
                      : 'bg-gray-100 text-gray-400 cursor-not-allowed border border-gray-200'
                  }`}
                >
                  <div className="text-center">
                    <div>{reward.pointsCost} Pts</div>
                    <div className="text-[10px] font-medium">
                      {canAfford ? 'Claim' : 'Locked'}
                    </div>
                  </div>
                </button>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
