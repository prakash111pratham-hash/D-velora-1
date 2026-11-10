import React from 'react';
import { useApp } from '../context/AppContext';
import { HeroBanner } from './HeroBanner';
import { LoyaltyMemberCard } from './LoyaltyMemberCard';
import { MENU_ITEMS } from '../data/menuData';
import { 
  CalendarDays, 
  UtensilsCrossed, 
  Award, 
  Sparkles, 
  Star, 
  MapPin, 
  ArrowRight, 
  Plus, 
  Clock, 
  Car 
} from 'lucide-react';

export const HomeTab: React.FC = () => {
  const { setActiveTab, reservations, addToCart } = useApp();

  const upcomingReservation = reservations.find((r) => r.status === 'CONFIRMED');
  const featuredItems = MENU_ITEMS.filter((item) => item.badge != null).slice(0, 6);

  return (
    <div className="space-y-6 pb-24">
      {/* Hero Banner */}
      <HeroBanner />

      {/* Member Card */}
      <LoyaltyMemberCard />

      {/* Upcoming Reservation Banner (if any) */}
      {upcomingReservation && (
        <div className="bg-[#FAF7F2] border border-[#C5A059]/40 rounded-2xl p-4 flex items-center justify-between shadow-2xs">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-[#2E1810] text-white flex items-center justify-center shrink-0">
              <CalendarDays className="w-5 h-5 text-[#C5A059]" />
            </div>
            <div>
              <p className="text-xs font-bold text-[#2E1810]">
                Table Confirmed: {upcomingReservation.date} ({upcomingReservation.timeSlot})
              </p>
              <p className="text-[11px] text-[#6F5F55]">
                {upcomingReservation.partySize} Guests • {upcomingReservation.seatingArea}
              </p>
            </div>
          </div>
          <button
            onClick={() => setActiveTab('reserve')}
            className="bg-[#2E1810] hover:bg-[#4A2810] text-white text-xs font-semibold px-3 py-1.5 rounded-lg transition-colors"
          >
            Details
          </button>
        </div>
      )}

      {/* Quick Navigation Action Grid */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
        <button
          onClick={() => setActiveTab('reserve')}
          className="bg-white hover:bg-[#FAF7F2] border border-[#DECFC2] rounded-2xl p-3.5 text-center flex flex-col items-center justify-center gap-2 shadow-2xs hover:border-[#C5A059] transition-all group"
        >
          <div className="w-10 h-10 rounded-full bg-[#C5A059]/15 flex items-center justify-center group-hover:scale-110 transition-transform">
            <CalendarDays className="w-5 h-5 text-[#C5A059]" />
          </div>
          <div>
            <span className="text-xs font-bold text-[#2E1810] block">Book Table</span>
            <span className="text-[10px] text-[#6F5F55]">Patio & booths</span>
          </div>
        </button>

        <button
          onClick={() => setActiveTab('menu')}
          className="bg-white hover:bg-[#FAF7F2] border border-[#DECFC2] rounded-2xl p-3.5 text-center flex flex-col items-center justify-center gap-2 shadow-2xs hover:border-[#C5A059] transition-all group"
        >
          <div className="w-10 h-10 rounded-full bg-[#C5A059]/15 flex items-center justify-center group-hover:scale-110 transition-transform">
            <UtensilsCrossed className="w-5 h-5 text-[#C5A059]" />
          </div>
          <div>
            <span className="text-xs font-bold text-[#2E1810] block">Digital Menu</span>
            <span className="text-[10px] text-[#6F5F55]">Browse 50+ dishes</span>
          </div>
        </button>

        <button
          onClick={() => setActiveTab('rewards')}
          className="bg-white hover:bg-[#FAF7F2] border border-[#DECFC2] rounded-2xl p-3.5 text-center flex flex-col items-center justify-center gap-2 shadow-2xs hover:border-[#C5A059] transition-all group"
        >
          <div className="w-10 h-10 rounded-full bg-[#C5A059]/15 flex items-center justify-center group-hover:scale-110 transition-transform">
            <Award className="w-5 h-5 text-[#C5A059]" />
          </div>
          <div>
            <span className="text-xs font-bold text-[#2E1810] block">Rewards Club</span>
            <span className="text-[10px] text-[#6F5F55]">Redeem vouchers</span>
          </div>
        </button>

        <button
          onClick={() => setActiveTab('concierge')}
          className="bg-white hover:bg-[#FAF7F2] border border-[#DECFC2] rounded-2xl p-3.5 text-center flex flex-col items-center justify-center gap-2 shadow-2xs hover:border-[#C5A059] transition-all group"
        >
          <div className="w-10 h-10 rounded-full bg-[#C5A059]/15 flex items-center justify-center group-hover:scale-110 transition-transform">
            <Sparkles className="w-5 h-5 text-[#C5A059]" />
          </div>
          <div>
            <span className="text-xs font-bold text-[#2E1810] block">Chef AI</span>
            <span className="text-[10px] text-[#6F5F55]">Culinary pairings</span>
          </div>
        </button>
      </div>

      {/* Chef's Featured Picks Carousel */}
      <div>
        <div className="flex items-center justify-between mb-3 px-1">
          <div>
            <h3 className="font-serif text-xl font-bold text-[#2E1810]">Chef's Featured Picks</h3>
            <p className="text-xs text-[#8D5B28]">Signatures from our charcoal tandoor and espresso bar</p>
          </div>
          <button
            onClick={() => setActiveTab('menu')}
            className="text-xs font-bold text-[#C5A059] hover:underline flex items-center gap-1"
          >
            See Full Menu <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </div>

        <div className="flex gap-4 overflow-x-auto pb-2 scrollbar-none snap-x">
          {featuredItems.map((dish) => (
            <div
              key={dish.id}
              className="min-w-[240px] sm:min-w-[270px] bg-white rounded-2xl border border-[#DECFC2] p-4 flex flex-col justify-between shadow-2xs snap-start hover:shadow-md transition-shadow"
            >
              <div>
                <div className="flex items-center justify-between mb-2">
                  <span className="bg-[#FAF7F2] text-[#8D5B28] border border-[#DECFC2] text-[10px] font-bold px-2 py-0.5 rounded-md">
                    {dish.badge || dish.category}
                  </span>
                  <div className="flex items-center gap-1 text-xs font-bold text-[#D97706]">
                    <Star className="w-3.5 h-3.5 fill-current" />
                    <span>{dish.rating}</span>
                  </div>
                </div>

                <h4 className="font-serif font-bold text-sm text-[#2E1810] line-clamp-1">
                  {dish.name}
                </h4>
                <p className="text-xs text-[#6F5F55] line-clamp-2 mt-1">
                  {dish.description}
                </p>
              </div>

              <div className="flex items-center justify-between mt-4 pt-3 border-t border-[#DECFC2]/50">
                <span className="font-bold text-sm text-[#2E1810]">
                  {dish.halfPrice && dish.fullPrice
                    ? `₹${dish.halfPrice} / ₹${dish.fullPrice}`
                    : `₹${dish.singlePrice || 160}`}
                </span>

                <button
                  onClick={() => addToCart(dish, dish.halfPrice ? 'HALF' : 'SINGLE')}
                  className="w-8 h-8 rounded-full bg-[#2E1810] hover:bg-[#4A2810] active:scale-90 text-white flex items-center justify-center transition-all cursor-pointer shadow-xs"
                  title="Add to Order"
                >
                  <Plus className="w-4 h-4" />
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Location, Atmosphere & Google Maps Grounding Card */}
      <div className="bg-white rounded-2xl border border-[#DECFC2] p-5 shadow-xs">
        <div className="flex items-start gap-3 mb-4">
          <div className="w-10 h-10 rounded-xl bg-[#C5A059]/20 text-[#8D5B28] flex items-center justify-center shrink-0">
            <MapPin className="w-5 h-5 text-[#C5A059]" />
          </div>
          <div>
            <h4 className="font-serif font-bold text-base text-[#2E1810]">
              D Velora Cafe & Restaurant
            </h4>
            <p className="text-xs text-[#6F5F55]">
              42 Promenade Boulevard, Heritage Cafe District
            </p>
          </div>
        </div>

        <div className="grid grid-cols-2 gap-2 text-xs py-3 border-y border-[#DECFC2]/60 mb-4">
          <div className="flex items-center gap-2 text-[#2E1810]">
            <Clock className="w-4 h-4 text-[#C5A059]" />
            <span>Open Daily: 8 AM – 11:30 PM</span>
          </div>
          <div className="flex items-center gap-2 text-[#2E1810]">
            <Car className="w-4 h-4 text-[#C5A059]" />
            <span>Complimentary Valet & EV</span>
          </div>
        </div>

        <button
          onClick={() => {
            setActiveTab('concierge');
          }}
          className="w-full py-2.5 px-4 bg-[#FAF7F2] hover:bg-[#F1EAE0] text-[#2E1810] font-semibold text-xs rounded-xl border border-[#DECFC2] flex items-center justify-center gap-2 transition-colors cursor-pointer"
        >
          <span>Ask Chef Velora for directions & parking tips</span>
          <ArrowRight className="w-3.5 h-3.5 text-[#C5A059]" />
        </button>
      </div>
    </div>
  );
};
