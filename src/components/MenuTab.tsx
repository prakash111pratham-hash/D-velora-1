import React, { useState, useMemo } from 'react';
import { useApp } from '../context/AppContext';
import { MENU_ITEMS } from '../data/menuData';
import { 
  MENU_CATEGORIES, 
  MenuCategoryKey, 
  DietaryType, 
  PortionSelection,
  MenuItem
} from '../types/models';
import { 
  Search, 
  X, 
  Star, 
  Plus, 
  ShoppingBag, 
  Sparkles,
  Flame
} from 'lucide-react';

export const MenuTab: React.FC = () => {
  const { addToCart, cartCount, cartTotal, setShowCartSheet } = useApp();

  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<MenuCategoryKey | 'ALL'>('ALL');
  const [selectedDietary, setSelectedDietary] = useState<DietaryType | 'ALL'>('ALL');
  const [portionChoices, setPortionChoices] = useState<Record<string, PortionSelection>>({});

  // Filter logic
  const filteredDishes = useMemo(() => {
    return MENU_ITEMS.filter((item) => {
      // Category match
      if (selectedCategory !== 'ALL' && item.category !== selectedCategory) {
        return false;
      }
      // Dietary match
      if (selectedDietary !== 'ALL' && item.dietaryType !== selectedDietary) {
        return false;
      }
      // Search match
      if (searchQuery.trim()) {
        const query = searchQuery.toLowerCase();
        const matchesName = item.name.toLowerCase().includes(query);
        const matchesDesc = item.description.toLowerCase().includes(query);
        const matchesCategory = item.category.toLowerCase().includes(query);
        if (!matchesName && !matchesDesc && !matchesCategory) return false;
      }
      return true;
    });
  }, [searchQuery, selectedCategory, selectedDietary]);

  const handlePortionSelect = (dishId: string, portion: PortionSelection) => {
    setPortionChoices((prev) => ({ ...prev, [dishId]: portion }));
  };

  return (
    <div className="space-y-5 pb-28">
      {/* Header & Subtitle */}
      <div>
        <h2 className="font-serif text-2xl sm:text-3xl font-bold text-[#2E1810]">
          Digital Menu
        </h2>
        <p className="text-xs font-semibold text-[#8D5B28]">
          Taste • Quality • Hygiene • Fresh Food Fresh Life
        </p>
      </div>

      {/* Search Bar */}
      <div className="relative">
        <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-[#C5A059]" />
        <input
          type="text"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          placeholder="Search dishes, rolls, curries, lattes, biryanis..."
          className="w-full pl-10 pr-10 py-3 bg-white border border-[#DECFC2] rounded-2xl text-xs sm:text-sm placeholder-[#6F5F55]/60 focus:outline-none focus:border-[#C5A059] shadow-2xs"
        />
        {searchQuery && (
          <button
            onClick={() => setSearchQuery('')}
            className="absolute right-3.5 top-1/2 -translate-y-1/2 text-[#6F5F55] hover:text-[#2E1810]"
          >
            <X className="w-4 h-4" />
          </button>
        )}
      </div>

      {/* Dietary Filter Pills */}
      <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-none">
        <button
          onClick={() => setSelectedDietary('ALL')}
          className={`px-3 py-1.5 rounded-full text-xs font-bold transition-all shrink-0 ${
            selectedDietary === 'ALL'
              ? 'bg-[#2E1810] text-white shadow-xs'
              : 'bg-white text-[#6F5F55] border border-[#DECFC2]'
          }`}
        >
          All
        </button>

        <button
          onClick={() => setSelectedDietary('VEG')}
          className={`flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-bold transition-all shrink-0 ${
            selectedDietary === 'VEG'
              ? 'bg-[#16A34A] text-white shadow-xs'
              : 'bg-white text-[#16A34A] border border-[#16A34A]/40'
          }`}
        >
          <span className="w-2 h-2 rounded-full bg-current" />
          Pure Veg
        </button>

        <button
          onClick={() => setSelectedDietary('NON_VEG')}
          className={`flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-bold transition-all shrink-0 ${
            selectedDietary === 'NON_VEG'
              ? 'bg-[#DC2626] text-white shadow-xs'
              : 'bg-white text-[#DC2626] border border-[#DC2626]/40'
          }`}
        >
          <span className="w-2 h-2 rounded-full bg-current" />
          Non-Veg
        </button>

        <button
          onClick={() => setSelectedDietary('CHEF_SPECIAL')}
          className={`flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-bold transition-all shrink-0 ${
            selectedDietary === 'CHEF_SPECIAL'
              ? 'bg-[#C5A059] text-white shadow-xs'
              : 'bg-white text-[#8D5B28] border border-[#C5A059]/40'
          }`}
        >
          <Sparkles className="w-3 h-3 text-current" />
          Signatures
        </button>
      </div>

      {/* Category Scroll Bar */}
      <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-none">
        <button
          onClick={() => setSelectedCategory('ALL')}
          className={`px-3.5 py-2 rounded-xl text-xs font-bold transition-all shrink-0 ${
            selectedCategory === 'ALL'
              ? 'bg-[#2E1810] text-white shadow-xs'
              : 'bg-white text-[#6F5F55] border border-[#DECFC2] hover:bg-[#FAF7F2]'
          }`}
        >
          All Categories
        </button>

        {Object.values(MENU_CATEGORIES).map((cat) => (
          <button
            key={cat.key}
            onClick={() => setSelectedCategory(cat.key)}
            className={`px-3.5 py-2 rounded-xl text-xs font-bold transition-all shrink-0 ${
              selectedCategory === cat.key
                ? 'bg-[#2E1810] text-white shadow-xs'
                : 'bg-white text-[#6F5F55] border border-[#DECFC2] hover:bg-[#FAF7F2]'
            }`}
          >
            {cat.displayName}
          </button>
        ))}
      </div>

      {/* Dish Count Header */}
      <div className="flex items-center justify-between text-xs text-[#6F5F55] px-1">
        <span>Showing {filteredDishes.length} artisanal dishes</span>
        {(selectedCategory !== 'ALL' || selectedDietary !== 'ALL' || searchQuery) && (
          <button
            onClick={() => {
              setSelectedCategory('ALL');
              setSelectedDietary('ALL');
              setSearchQuery('');
            }}
            className="text-[#C5A059] font-bold hover:underline"
          >
            Reset Filters
          </button>
        )}
      </div>

      {/* Dish Items List */}
      <div className="space-y-3.5">
        {filteredDishes.map((dish) => {
          const selectedPortion =
            portionChoices[dish.id] ||
            (dish.halfPrice != null && dish.fullPrice != null ? 'HALF' : 'SINGLE');

          return (
            <div
              key={dish.id}
              className="bg-white rounded-2xl border border-[#DECFC2] p-4 sm:p-5 shadow-2xs hover:shadow-xs transition-shadow"
            >
              <div className="flex items-start justify-between gap-3">
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-1.5">
                    {/* Dietary indicator dot box */}
                    <div
                      className={`w-3.5 h-3.5 rounded-sm border flex items-center justify-center ${
                        dish.dietaryType === 'VEG'
                          ? 'border-[#16A34A]'
                          : dish.dietaryType === 'NON_VEG'
                          ? 'border-[#DC2626]'
                          : 'border-[#C5A059]'
                      }`}
                    >
                      <div
                        className={`w-1.5 h-1.5 rounded-full ${
                          dish.dietaryType === 'VEG'
                            ? 'bg-[#16A34A]'
                            : dish.dietaryType === 'NON_VEG'
                            ? 'bg-[#DC2626]'
                            : 'bg-[#C5A059]'
                        }`}
                      />
                    </div>

                    {dish.badge && (
                      <span className="bg-[#FAF7F2] text-[#8D5B28] border border-[#DECFC2] text-[10px] font-bold px-2 py-0.5 rounded-md">
                        {dish.badge}
                      </span>
                    )}
                  </div>

                  <h3 className="font-serif font-bold text-base text-[#2E1810]">
                    {dish.name}
                  </h3>
                  <p className="text-xs text-[#6F5F55] mt-1 line-clamp-2">
                    {dish.description}
                  </p>
                </div>

                <div className="flex items-center gap-1 text-xs font-bold text-[#D97706] bg-[#FAF7F2] px-2 py-1 rounded-lg shrink-0">
                  <Star className="w-3.5 h-3.5 fill-current" />
                  <span>{dish.rating}</span>
                </div>
              </div>

              {/* Portion selector & Add to order button */}
              <div className="flex items-center justify-between mt-4 pt-3 border-t border-[#DECFC2]/50">
                {dish.halfPrice != null && dish.fullPrice != null ? (
                  <div className="flex gap-2">
                    <button
                      onClick={() => handlePortionSelect(dish.id, 'HALF')}
                      className={`px-2.5 py-1 rounded-lg text-xs font-bold transition-all ${
                        selectedPortion === 'HALF'
                          ? 'bg-[#2E1810] text-white shadow-2xs'
                          : 'bg-[#FAF7F2] text-[#6F5F55] border border-[#DECFC2]'
                      }`}
                    >
                      Half (₹{dish.halfPrice})
                    </button>
                    <button
                      onClick={() => handlePortionSelect(dish.id, 'FULL')}
                      className={`px-2.5 py-1 rounded-lg text-xs font-bold transition-all ${
                        selectedPortion === 'FULL'
                          ? 'bg-[#2E1810] text-white shadow-2xs'
                          : 'bg-[#FAF7F2] text-[#6F5F55] border border-[#DECFC2]'
                      }`}
                    >
                      Full (₹{dish.fullPrice})
                    </button>
                  </div>
                ) : (
                  <span className="font-serif font-bold text-base text-[#2E1810]">
                    ₹{dish.singlePrice || 160}
                  </span>
                )}

                <button
                  onClick={() => addToCart(dish, selectedPortion)}
                  className="flex items-center gap-1.5 bg-[#C5A059] hover:bg-[#d6b26a] active:scale-95 text-white text-xs font-bold px-4 py-2 rounded-xl shadow-xs transition-all cursor-pointer"
                >
                  <Plus className="w-3.5 h-3.5" />
                  <span>Add</span>
                </button>
              </div>
            </div>
          );
        })}
      </div>

      {/* Floating Order Cart Bar */}
      {cartCount > 0 && (
        <div className="fixed bottom-20 left-4 right-4 z-40 max-w-md mx-auto">
          <div
            onClick={() => setShowCartSheet(true)}
            className="bg-[#2E1810] text-white rounded-2xl p-3.5 sm:p-4 flex items-center justify-between shadow-xl border border-[#C5A059] cursor-pointer hover:bg-[#3d2015] transition-all"
          >
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-full bg-[#C5A059] text-[#2E1810] flex items-center justify-center font-bold">
                <ShoppingBag className="w-5 h-5 text-[#2E1810]" />
              </div>
              <div>
                <p className="text-xs font-bold leading-tight">
                  {cartCount} {cartCount === 1 ? 'item' : 'items'} in Order
                </p>
                <p className="text-sm font-serif font-bold text-[#C5A059]">
                  Total: ₹{cartTotal}
                </p>
              </div>
            </div>

            <button
              onClick={(e) => {
                e.stopPropagation();
                setShowCartSheet(true);
              }}
              className="bg-[#C5A059] hover:bg-[#d6b26a] text-white text-xs font-bold px-4 py-2 rounded-xl transition-colors shadow-xs"
            >
              View Order
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
