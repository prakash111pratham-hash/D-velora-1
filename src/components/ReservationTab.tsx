import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { 
  CalendarDays, 
  Users, 
  Clock, 
  Sparkles, 
  CheckCircle2, 
  Phone, 
  User, 
  Armchair, 
  AlertCircle 
} from 'lucide-react';

export const ReservationTab: React.FC = () => {
  const { user, bookTable, reservations, cancelReservation } = useApp();

  const [activeSubTab, setActiveSubTab] = useState<'book' | 'list'>('book');

  // Form State
  const [guestName, setGuestName] = useState(user?.displayName || '');
  const [guestPhone, setGuestPhone] = useState('+91 98765 43210');
  const [partySize, setPartySize] = useState<number>(2);
  const [seatingArea, setSeatingArea] = useState('Patio Cafe Terrace');
  const [date, setDate] = useState('Today, Nov 10');
  const [timeSlot, setTimeSlot] = useState('19:30');
  const [specialRequests, setSpecialRequests] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [formSuccessMessage, setFormSuccessMessage] = useState<string | null>(null);

  const seatingOptions = [
    { title: 'Patio Cafe Terrace', desc: 'Open-air garden vibes with acoustic cafe jazz' },
    { title: 'Window Booth', desc: 'Private booth with boulevard street view' },
    { title: 'Main Dining Hall', desc: 'Spacious family dining near the live tandoor' },
    { title: 'Private Suite', desc: 'Quiet premium section for special occasions' }
  ];

  const dateOptions = [
    'Today, Nov 10',
    'Tomorrow, Nov 11',
    'Friday, Nov 14',
    'Saturday, Nov 15',
    'Sunday, Nov 16'
  ];

  const timeOptions = [
    '12:30', '13:15', '14:00',
    '19:00', '19:30', '20:15', '21:00', '21:45'
  ];

  const handleBooking = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!guestName.trim() || !guestPhone.trim()) return;

    setIsSubmitting(true);
    const result = await bookTable(
      guestName,
      guestPhone,
      partySize,
      seatingArea,
      date,
      timeSlot,
      specialRequests
    );
    setIsSubmitting(false);

    if (result.success) {
      setFormSuccessMessage(result.message);
      setTimeout(() => {
        setFormSuccessMessage(null);
        setActiveSubTab('list');
      }, 1500);
    }
  };

  return (
    <div className="space-y-5 pb-28">
      {/* Header */}
      <div>
        <h2 className="font-serif text-2xl sm:text-3xl font-bold text-[#2E1810]">
          Table Reservations
        </h2>
        <p className="text-xs font-semibold text-[#8D5B28]">
          Dine with us • Experience warmth and authentic hospitality
        </p>
      </div>

      {/* Sub Tabs */}
      <div className="flex border-b border-[#DECFC2] bg-white rounded-xl p-1 shadow-2xs">
        <button
          onClick={() => setActiveSubTab('book')}
          className={`flex-1 py-2 text-xs font-bold rounded-lg transition-all ${
            activeSubTab === 'book'
              ? 'bg-[#2E1810] text-white shadow-xs'
              : 'text-[#6F5F55] hover:text-[#2E1810]'
          }`}
        >
          Book a Table
        </button>
        <button
          onClick={() => setActiveSubTab('list')}
          className={`flex-1 py-2 text-xs font-bold rounded-lg transition-all ${
            activeSubTab === 'list'
              ? 'bg-[#2E1810] text-white shadow-xs'
              : 'text-[#6F5F55] hover:text-[#2E1810]'
          }`}
        >
          My Bookings ({reservations.length})
        </button>
      </div>

      {activeSubTab === 'book' ? (
        <form onSubmit={handleBooking} className="space-y-6">
          {formSuccessMessage && (
            <div className="bg-[#16A34A]/10 border border-[#16A34A]/40 text-[#16A34A] p-4 rounded-2xl flex items-center gap-3">
              <CheckCircle2 className="w-5 h-5 shrink-0" />
              <span className="text-xs font-bold">{formSuccessMessage}</span>
            </div>
          )}

          {/* 1. Guest Contact */}
          <div className="bg-white rounded-2xl border border-[#DECFC2] p-5 shadow-2xs space-y-3.5">
            <h3 className="font-serif font-bold text-sm text-[#2E1810] flex items-center gap-2">
              <span className="w-5 h-5 rounded-full bg-[#C5A059] text-white flex items-center justify-center text-[10px] font-bold">1</span>
              Guest Contact Details
            </h3>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <div>
                <label className="block text-[11px] font-bold text-[#6F5F55] mb-1">Guest Name</label>
                <div className="relative">
                  <User className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-[#C5A059]" />
                  <input
                    type="text"
                    required
                    value={guestName}
                    onChange={(e) => setGuestName(e.target.value)}
                    placeholder="Full name"
                    className="w-full pl-9 pr-3 py-2.5 bg-[#FAF7F2] border border-[#DECFC2] rounded-xl text-xs font-medium focus:outline-none focus:border-[#C5A059]"
                  />
                </div>
              </div>

              <div>
                <label className="block text-[11px] font-bold text-[#6F5F55] mb-1">Phone Number</label>
                <div className="relative">
                  <Phone className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-[#C5A059]" />
                  <input
                    type="tel"
                    required
                    value={guestPhone}
                    onChange={(e) => setGuestPhone(e.target.value)}
                    placeholder="+91 Phone"
                    className="w-full pl-9 pr-3 py-2.5 bg-[#FAF7F2] border border-[#DECFC2] rounded-xl text-xs font-medium focus:outline-none focus:border-[#C5A059]"
                  />
                </div>
              </div>
            </div>
          </div>

          {/* 2. Party Size */}
          <div className="bg-white rounded-2xl border border-[#DECFC2] p-5 shadow-2xs space-y-3">
            <h3 className="font-serif font-bold text-sm text-[#2E1810] flex items-center gap-2">
              <span className="w-5 h-5 rounded-full bg-[#C5A059] text-white flex items-center justify-center text-[10px] font-bold">2</span>
              Party Size
            </h3>

            <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-none">
              {[1, 2, 4, 6, 8, 12].map((num) => (
                <button
                  key={num}
                  type="button"
                  onClick={() => setPartySize(num)}
                  className={`px-4 py-2 rounded-xl text-xs font-bold transition-all shrink-0 ${
                    partySize === num
                      ? 'bg-[#2E1810] text-white shadow-xs'
                      : 'bg-[#FAF7F2] text-[#6F5F55] border border-[#DECFC2] hover:bg-white'
                  }`}
                >
                  {num} {num === 1 ? 'Guest' : 'Guests'}
                </button>
              ))}
            </div>
          </div>

          {/* 3. Seating Atmosphere */}
          <div className="bg-white rounded-2xl border border-[#DECFC2] p-5 shadow-2xs space-y-3">
            <h3 className="font-serif font-bold text-sm text-[#2E1810] flex items-center gap-2">
              <span className="w-5 h-5 rounded-full bg-[#C5A059] text-white flex items-center justify-center text-[10px] font-bold">3</span>
              Seating Atmosphere
            </h3>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2.5">
              {seatingOptions.map((opt) => (
                <div
                  key={opt.title}
                  onClick={() => setSeatingArea(opt.title)}
                  className={`p-3.5 rounded-xl border cursor-pointer transition-all ${
                    seatingArea === opt.title
                      ? 'bg-[#FAF7F2] border-[#C5A059] shadow-2xs'
                      : 'bg-white border-[#DECFC2] hover:bg-[#FAF7F2]'
                  }`}
                >
                  <div className="flex items-center gap-2">
                    <Armchair className={`w-4 h-4 ${seatingArea === opt.title ? 'text-[#C5A059]' : 'text-[#6F5F55]'}`} />
                    <span className="text-xs font-bold text-[#2E1810]">{opt.title}</span>
                  </div>
                  <p className="text-[11px] text-[#6F5F55] mt-1 pl-6">
                    {opt.desc}
                  </p>
                </div>
              ))}
            </div>
          </div>

          {/* 4. Date & Time Slots */}
          <div className="bg-white rounded-2xl border border-[#DECFC2] p-5 shadow-2xs space-y-3.5">
            <h3 className="font-serif font-bold text-sm text-[#2E1810] flex items-center gap-2">
              <span className="w-5 h-5 rounded-full bg-[#C5A059] text-white flex items-center justify-center text-[10px] font-bold">4</span>
              Date & Dining Time
            </h3>

            {/* Date Pills */}
            <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-none">
              {dateOptions.map((d) => (
                <button
                  key={d}
                  type="button"
                  onClick={() => setDate(d)}
                  className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all shrink-0 ${
                    date === d
                      ? 'bg-[#2E1810] text-white shadow-xs'
                      : 'bg-[#FAF7F2] text-[#6F5F55] border border-[#DECFC2]'
                  }`}
                >
                  {d}
                </button>
              ))}
            </div>

            {/* Time Slot Chips */}
            <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-none">
              {timeOptions.map((t) => (
                <button
                  key={t}
                  type="button"
                  onClick={() => setTimeSlot(t)}
                  className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all shrink-0 ${
                    timeSlot === t
                      ? 'bg-[#C5A059] text-white shadow-xs'
                      : 'bg-[#FAF7F2] text-[#6F5F55] border border-[#DECFC2]'
                  }`}
                >
                  {t}
                </button>
              ))}
            </div>
          </div>

          {/* 5. Special Requests */}
          <div className="bg-white rounded-2xl border border-[#DECFC2] p-5 shadow-2xs">
            <label className="block text-xs font-bold text-[#2E1810] mb-2">
              Special Requests / Dietary Notes (Optional)
            </label>
            <input
              type="text"
              value={specialRequests}
              onChange={(e) => setSpecialRequests(e.target.value)}
              placeholder="e.g. Birthday candle, quiet corner, wheelchair accessible"
              className="w-full px-3 py-2.5 bg-[#FAF7F2] border border-[#DECFC2] rounded-xl text-xs font-medium focus:outline-none focus:border-[#C5A059]"
            />
          </div>

          {/* Submit Button */}
          <button
            type="submit"
            disabled={isSubmitting || !guestName.trim() || !guestPhone.trim()}
            className="w-full py-3.5 bg-[#2E1810] hover:bg-[#4A2810] active:scale-[0.99] text-white text-sm font-bold rounded-2xl shadow-md transition-all cursor-pointer flex items-center justify-center gap-2 disabled:opacity-60"
          >
            {isSubmitting ? (
              <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
            ) : (
              <>
                <Sparkles className="w-4 h-4 text-[#C5A059]" />
                <span>Confirm Table Reservation (+50 Pts)</span>
              </>
            )}
          </button>
        </form>
      ) : (
        /* My Bookings List */
        <div className="space-y-3.5">
          {reservations.length === 0 ? (
            <div className="bg-white rounded-2xl border border-[#DECFC2] p-8 text-center space-y-3">
              <div className="w-12 h-12 rounded-full bg-[#FAF7F2] border border-[#DECFC2] flex items-center justify-center mx-auto text-[#6F5F55]">
                <CalendarDays className="w-6 h-6" />
              </div>
              <h3 className="font-serif font-bold text-base text-[#2E1810]">No Reservations Found</h3>
              <p className="text-xs text-[#6F5F55] max-w-xs mx-auto">
                Reserve your table in advance and earn 50 bonus loyalty points upon confirmation.
              </p>
              <button
                onClick={() => setActiveSubTab('book')}
                className="bg-[#C5A059] text-white text-xs font-bold px-4 py-2 rounded-xl shadow-xs"
              >
                Book Table Now
              </button>
            </div>
          ) : (
            reservations.map((res) => (
              <div
                key={res.id}
                className="bg-white rounded-2xl border border-[#DECFC2] p-5 shadow-2xs space-y-3"
              >
                <div className="flex items-center justify-between">
                  <span
                    className={`text-[10px] font-bold px-2 py-0.5 rounded-md uppercase tracking-wider ${
                      res.status === 'CONFIRMED'
                        ? 'bg-[#16A34A]/15 text-[#16A34A] border border-[#16A34A]/30'
                        : 'bg-gray-100 text-gray-600 border border-gray-200'
                    }`}
                  >
                    {res.status}
                  </span>
                  <span className="text-xs font-bold text-[#2E1810]">
                    {res.partySize} Guests
                  </span>
                </div>

                <div>
                  <h4 className="font-serif font-bold text-base text-[#2E1810]">
                    {res.date} • {res.timeSlot}
                  </h4>
                  <p className="text-xs text-[#6F5F55]">
                    Area: <span className="font-semibold text-[#2E1810]">{res.seatingArea}</span>
                  </p>
                  <p className="text-xs text-[#6F5F55]">
                    Guest: {res.guestName} ({res.guestPhone})
                  </p>
                  {res.specialRequests && (
                    <p className="text-xs text-[#8D5B28] mt-1 font-medium bg-[#FAF7F2] p-2 rounded-lg">
                      Note: {res.specialRequests}
                    </p>
                  )}
                </div>

                {res.status === 'CONFIRMED' && (
                  <div className="pt-2 border-t border-[#DECFC2]/50 flex justify-end">
                    <button
                      onClick={() => cancelReservation(res.id)}
                      className="text-xs font-bold text-[#DC2626] hover:underline"
                    >
                      Cancel Booking
                    </button>
                  </div>
                )}
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
};
