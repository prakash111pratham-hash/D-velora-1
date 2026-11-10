import React from 'react';
import { useApp } from '../context/AppContext';
import { MENU_ITEMS } from '../data/menuData';
import { Plus, Sparkles } from 'lucide-react';

export const HeroBanner: React.FC = () => {
  const { addToCart } = useApp();
  const latteItem = MENU_ITEMS.find((item) => item.id === 'sig_almond_latte');

  return (
    <div className="relative rounded-3xl overflow-hidden shadow-lg border border-[#DECFC2] bg-[#2E1810] text-white">
      {/* Background Image with Gradient Overlay */}
      <div className="relative h-60 sm:h-72 w-full">
        <img
          src="/images/hero_almond_latte.jpg"
          alt="Almond Blueberry Latte"
          className="w-full h-full object-cover"
          onError={(e) => {
            // Elegant styled fallback background if file not loaded
            (e.target as HTMLElement).style.display = 'none';
          }}
        />
        <div className="absolute inset-0 bg-gradient-to-t from-black/90 via-black/40 to-transparent" />

        {/* Content Overlaid at Bottom */}
        <div className="absolute inset-x-0 bottom-0 p-5 sm:p-7 flex flex-col justify-end">
          <div className="flex items-center gap-2 mb-2">
            <span className="inline-flex items-center gap-1 bg-[#D97706] text-white text-[10px] font-bold uppercase tracking-wider px-2.5 py-0.5 rounded-md shadow-xs">
              <Sparkles className="w-3 h-3" />
              House Signature
            </span>
            <span className="text-white/80 text-[11px] font-medium hidden sm:inline">
              Roast: Medium-Dark • Wild Mountain Blueberry
            </span>
          </div>

          <h2 className="font-serif text-2xl sm:text-3xl font-bold text-white tracking-wide leading-tight">
            Almond Blueberry Latte
          </h2>

          <p className="text-xs sm:text-sm text-white/85 line-clamp-2 mt-1 max-w-xl">
            Velvety espresso infused with wild mountain blueberry compote, steamed almond milk & roasted almond crunch.
          </p>

          <div className="flex items-center justify-between mt-4">
            <div>
              <span className="text-2xl font-serif font-bold text-[#C5A059]">₹180</span>
              <span className="text-[11px] text-white/70 ml-2">Single Serving</span>
            </div>

            <button
              onClick={() => latteItem && addToCart(latteItem, 'SINGLE')}
              className="flex items-center gap-2 bg-[#C5A059] hover:bg-[#d6b26a] active:scale-95 text-white font-bold text-xs sm:text-sm px-4 py-2.5 rounded-xl shadow-md transition-all cursor-pointer"
            >
              <Plus className="w-4 h-4" />
              <span>Order Now</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
