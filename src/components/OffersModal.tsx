import React from 'react';
import { useApp } from '../context/AppContext';
import { X, Sparkles, Tag, Clock } from 'lucide-react';

export const OffersModal: React.FC = () => {
  const { offers, showOffersDialog, setShowOffersDialog, setToastMessage } = useApp();

  if (!showOffersDialog) return null;

  const copyCode = (code: string) => {
    navigator.clipboard?.writeText(code);
    setToastMessage(`Copied promo code ${code}`);
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-xs flex items-center justify-center p-4">
      <div className="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-[#DECFC2] relative max-h-[85vh] flex flex-col">
        <button
          onClick={() => setShowOffersDialog(false)}
          className="absolute top-4 right-4 p-1.5 rounded-full hover:bg-[#FAF7F2] text-[#6F5F55]"
        >
          <X className="w-5 h-5" />
        </button>

        <div className="mb-4">
          <span className="text-[10px] font-bold uppercase tracking-widest text-[#C5A059] flex items-center gap-1.5">
            <Sparkles className="w-3.5 h-3.5" />
            Special Deals
          </span>
          <h3 className="font-serif font-bold text-xl text-[#2E1810]">
            Offers & Events
          </h3>
          <p className="text-xs text-[#6F5F55]">
            Handcrafted promo treats available at D Velora
          </p>
        </div>

        <div className="flex-1 overflow-y-auto space-y-3 pr-1 scrollbar-thin">
          {offers.map((offer) => (
            <div
              key={offer.id}
              className="bg-[#FAF7F2] rounded-2xl border border-[#DECFC2] p-4 space-y-2 hover:border-[#C5A059] transition-colors"
            >
              <div className="flex items-start justify-between gap-2">
                <h4 className="font-serif font-bold text-sm text-[#2E1810]">
                  {offer.title}
                </h4>
                <span className="bg-[#D97706]/15 text-[#D97706] text-[10px] font-bold px-2 py-0.5 rounded-md shrink-0">
                  {offer.discountPercent}% OFF
                </span>
              </div>

              <p className="text-xs text-[#6F5F55]">
                {offer.description}
              </p>

              <div className="flex items-center justify-between pt-2 border-t border-[#DECFC2]/50 text-xs">
                <div className="flex items-center gap-1 text-[11px] text-[#8D5B28]">
                  <Clock className="w-3.5 h-3.5" />
                  <span>{offer.validUntil}</span>
                </div>

                <button
                  onClick={() => copyCode(offer.promoCode)}
                  className="flex items-center gap-1 bg-white hover:bg-[#FAF7F2] border border-[#C5A059] text-[#2E1810] text-[11px] font-mono font-bold px-2.5 py-1 rounded-lg shadow-2xs cursor-pointer"
                >
                  <Tag className="w-3 h-3 text-[#C5A059]" />
                  <span>{offer.promoCode}</span>
                </button>
              </div>
            </div>
          ))}
        </div>

        <div className="pt-4 border-t border-[#DECFC2] mt-4">
          <button
            onClick={() => setShowOffersDialog(false)}
            className="w-full py-2.5 bg-[#2E1810] hover:bg-[#4A2810] text-white text-xs font-bold rounded-xl transition-colors"
          >
            Got It
          </button>
        </div>
      </div>
    </div>
  );
};
