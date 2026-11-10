import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { 
  X, 
  Plus, 
  Minus, 
  Trash2, 
  CreditCard, 
  Banknote, 
  Sparkles, 
  ShoppingBag,
  Check
} from 'lucide-react';

export const CartDrawer: React.FC = () => {
  const { 
    cart, 
    cartCount, 
    cartTotal, 
    updateCartQty, 
    clearCart, 
    showCartSheet, 
    setShowCartSheet, 
    setToastMessage 
  } = useApp();

  const [orderType, setOrderType] = useState<'DINE_IN' | 'PICKUP'>('DINE_IN');
  const [paymentMethod, setPaymentMethod] = useState<'UPI' | 'CARD' | 'CASH'>('UPI');
  const [promoCode, setPromoCode] = useState('');
  const [discountPercent, setDiscountPercent] = useState(0);
  const [promoFeedback, setPromoFeedback] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [orderConfirmed, setOrderConfirmed] = useState(false);

  if (!showCartSheet) return null;

  const discountAmount = Math.round((cartTotal * discountPercent) / 100);
  const finalTotal = Math.max(0, cartTotal - discountAmount);
  const pointsToEarn = Math.max(10, Math.round(finalTotal / 10));

  const applyPromo = () => {
    const code = promoCode.trim().toUpperCase();
    if (code === 'BLUEBERRY20') {
      setDiscountPercent(20);
      setPromoFeedback('20% Almond Blueberry celebration discount applied!');
    } else if (code === 'FAMILYFEAST') {
      setDiscountPercent(15);
      setPromoFeedback('15% Family Dining feast discount applied!');
    } else if (code === 'TANDOOR50') {
      setDiscountPercent(25);
      setPromoFeedback('Tandoori special discount applied!');
    } else {
      setDiscountPercent(0);
      setPromoFeedback('Invalid promo code. Try BLUEBERRY20');
    }
  };

  const handleCheckout = () => {
    setIsSubmitting(true);
    setTimeout(() => {
      setIsSubmitting(false);
      setOrderConfirmed(true);
      clearCart();
      setToastMessage(`Order placed successfully! Earned ${pointsToEarn} loyalty points.`);
    }, 1000);
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-xs flex justify-end">
      <div className="bg-white w-full max-w-md h-full flex flex-col shadow-2xl animate-in slide-in-from-right duration-200">
        {/* Drawer Header */}
        <div className="p-4 sm:p-5 border-b border-[#DECFC2] flex items-center justify-between">
          <div className="flex items-center gap-2.5">
            <div className="w-9 h-9 rounded-full bg-[#FAF7F2] border border-[#DECFC2] flex items-center justify-center text-[#2E1810]">
              <ShoppingBag className="w-5 h-5 text-[#C5A059]" />
            </div>
            <div>
              <h3 className="font-serif font-bold text-lg text-[#2E1810]">
                Your Dining Order
              </h3>
              <p className="text-xs text-[#6F5F55]">
                {cartCount} {cartCount === 1 ? 'dish' : 'dishes'} selected
              </p>
            </div>
          </div>

          <button
            onClick={() => {
              setShowCartSheet(false);
              setOrderConfirmed(false);
            }}
            className="p-2 rounded-full hover:bg-[#FAF7F2] text-[#6F5F55]"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Drawer Body */}
        {orderConfirmed ? (
          <div className="flex-1 flex flex-col items-center justify-center p-8 text-center space-y-4">
            <div className="w-16 h-16 rounded-full bg-[#16A34A]/15 text-[#16A34A] flex items-center justify-center">
              <Check className="w-8 h-8" />
            </div>
            <h4 className="font-serif font-bold text-2xl text-[#2E1810]">
              Order Confirmed!
            </h4>
            <p className="text-xs text-[#6F5F55] max-w-xs">
              Our kitchen team has received your order. We are preparing it fresh with utmost hygiene and care.
            </p>
            <div className="bg-[#FAF7F2] p-4 rounded-2xl border border-[#DECFC2] w-full text-xs">
              <span className="font-bold text-[#8D5B28] block">★ +{pointsToEarn} Points Credited</span>
              <span className="text-[#6F5F55]">Check Rewards tab to view your updated club balance</span>
            </div>
            <button
              onClick={() => {
                setShowCartSheet(false);
                setOrderConfirmed(false);
              }}
              className="w-full py-3 bg-[#2E1810] text-white text-xs font-bold rounded-xl"
            >
              Continue Dining
            </button>
          </div>
        ) : cart.length === 0 ? (
          <div className="flex-1 flex flex-col items-center justify-center p-8 text-center space-y-3">
            <div className="w-14 h-14 rounded-full bg-[#FAF7F2] border border-[#DECFC2] flex items-center justify-center text-[#6F5F55]">
              <ShoppingBag className="w-7 h-7" />
            </div>
            <h4 className="font-serif font-bold text-base text-[#2E1810]">Your Order is Empty</h4>
            <p className="text-xs text-[#6F5F55]">
              Add some of our signature lattes, tandoori starters, or dum biryanis to get started!
            </p>
          </div>
        ) : (
          <div className="flex-1 overflow-y-auto p-4 sm:p-5 space-y-4 scrollbar-thin">
            {/* Items List */}
            <div className="space-y-3">
              {cart.map((ci) => (
                <div
                  key={`${ci.item.id}_${ci.portion}`}
                  className="bg-[#FAF7F2] border border-[#DECFC2] rounded-xl p-3 flex items-center justify-between"
                >
                  <div className="flex-1 pr-2">
                    <h5 className="font-bold text-xs text-[#2E1810] line-clamp-1">
                      {ci.item.name}
                    </h5>
                    <p className="text-[11px] text-[#6F5F55]">
                      {ci.portion} • ₹{ci.unitPrice} each
                    </p>
                    <span className="font-bold text-xs text-[#2E1810]">
                      ₹{ci.totalPrice}
                    </span>
                  </div>

                  <div className="flex items-center gap-2 bg-white rounded-lg border border-[#DECFC2] p-1">
                    <button
                      onClick={() => updateCartQty(ci.item.id, ci.portion, -1)}
                      className="p-1 hover:bg-[#FAF7F2] rounded text-[#6F5F55]"
                    >
                      {ci.quantity === 1 ? (
                        <Trash2 className="w-3.5 h-3.5 text-red-500" />
                      ) : (
                        <Minus className="w-3.5 h-3.5" />
                      )}
                    </button>
                    <span className="text-xs font-bold px-1.5">{ci.quantity}</span>
                    <button
                      onClick={() => updateCartQty(ci.item.id, ci.portion, 1)}
                      className="p-1 hover:bg-[#FAF7F2] rounded text-[#2E1810]"
                    >
                      <Plus className="w-3.5 h-3.5" />
                    </button>
                  </div>
                </div>
              ))}
            </div>

            {/* Order Option (Dine-In vs Takeout) */}
            <div>
              <label className="block text-xs font-bold text-[#2E1810] mb-1.5">
                Order Type
              </label>
              <div className="grid grid-cols-2 gap-2">
                <button
                  type="button"
                  onClick={() => setOrderType('DINE_IN')}
                  className={`py-2 rounded-xl text-xs font-bold transition-all ${
                    orderType === 'DINE_IN'
                      ? 'bg-[#2E1810] text-white shadow-xs'
                      : 'bg-[#FAF7F2] text-[#6F5F55] border border-[#DECFC2]'
                  }`}
                >
                  Dine-In Table Order
                </button>
                <button
                  type="button"
                  onClick={() => setOrderType('PICKUP')}
                  className={`py-2 rounded-xl text-xs font-bold transition-all ${
                    orderType === 'PICKUP'
                      ? 'bg-[#2E1810] text-white shadow-xs'
                      : 'bg-[#FAF7F2] text-[#6F5F55] border border-[#DECFC2]'
                  }`}
                >
                  Takeout / Pickup
                </button>
              </div>
            </div>

            {/* Payment Method */}
            <div>
              <label className="block text-xs font-bold text-[#2E1810] mb-1.5">
                Payment Method
              </label>
              <div className="grid grid-cols-3 gap-2">
                <button
                  type="button"
                  onClick={() => setPaymentMethod('UPI')}
                  className={`py-2 px-1 rounded-xl text-xs font-bold flex flex-col items-center gap-1 transition-all ${
                    paymentMethod === 'UPI'
                      ? 'bg-[#2E1810] text-white shadow-xs'
                      : 'bg-[#FAF7F2] text-[#6F5F55] border border-[#DECFC2]'
                  }`}
                >
                  <Sparkles className="w-3.5 h-3.5 text-[#C5A059]" />
                  <span>UPI / GPay</span>
                </button>
                <button
                  type="button"
                  onClick={() => setPaymentMethod('CARD')}
                  className={`py-2 px-1 rounded-xl text-xs font-bold flex flex-col items-center gap-1 transition-all ${
                    paymentMethod === 'CARD'
                      ? 'bg-[#2E1810] text-white shadow-xs'
                      : 'bg-[#FAF7F2] text-[#6F5F55] border border-[#DECFC2]'
                  }`}
                >
                  <CreditCard className="w-3.5 h-3.5 text-[#C5A059]" />
                  <span>Card</span>
                </button>
                <button
                  type="button"
                  onClick={() => setPaymentMethod('CASH')}
                  className={`py-2 px-1 rounded-xl text-xs font-bold flex flex-col items-center gap-1 transition-all ${
                    paymentMethod === 'CASH'
                      ? 'bg-[#2E1810] text-white shadow-xs'
                      : 'bg-[#FAF7F2] text-[#6F5F55] border border-[#DECFC2]'
                  }`}
                >
                  <Banknote className="w-3.5 h-3.5 text-[#C5A059]" />
                  <span>Pay at Cashier</span>
                </button>
              </div>
            </div>

            {/* Promo Code Input */}
            <div>
              <label className="block text-xs font-bold text-[#2E1810] mb-1.5">
                Promo Code
              </label>
              <div className="flex gap-2">
                <input
                  type="text"
                  value={promoCode}
                  onChange={(e) => setPromoCode(e.target.value.toUpperCase())}
                  placeholder="e.g. BLUEBERRY20"
                  className="flex-1 px-3 py-2 bg-[#FAF7F2] border border-[#DECFC2] rounded-xl text-xs uppercase font-medium focus:outline-none focus:border-[#C5A059]"
                />
                <button
                  type="button"
                  onClick={applyPromo}
                  className="bg-[#C5A059] text-white text-xs font-bold px-3 py-2 rounded-xl"
                >
                  Apply
                </button>
              </div>
              {promoFeedback && (
                <p className="text-[11px] font-semibold text-[#8D5B28] mt-1">
                  {promoFeedback}
                </p>
              )}
            </div>

            {/* Pricing Breakdown */}
            <div className="border-t border-[#DECFC2] pt-3 space-y-1.5 text-xs text-[#6F5F55]">
              <div className="flex justify-between">
                <span>Subtotal</span>
                <span>₹{cartTotal}</span>
              </div>
              {discountAmount > 0 && (
                <div className="flex justify-between text-[#16A34A] font-bold">
                  <span>Promo Discount ({discountPercent}%)</span>
                  <span>-₹{discountAmount}</span>
                </div>
              )}
              <div className="flex justify-between">
                <span>Taxes & Service (GST)</span>
                <span>Included</span>
              </div>
              <div className="flex justify-between text-sm font-bold text-[#2E1810] pt-2 border-t border-[#DECFC2]/50">
                <span className="font-serif">Total Payable</span>
                <span className="font-serif text-base text-[#C5A059]">₹{finalTotal}</span>
              </div>
              <p className="text-[10px] text-[#8D5B28] font-bold text-center pt-1">
                ★ Earn {pointsToEarn} loyalty points on this order
              </p>
            </div>
          </div>
        )}

        {/* Drawer Footer */}
        {!orderConfirmed && cart.length > 0 && (
          <div className="p-4 border-t border-[#DECFC2] bg-[#FAF7F2]">
            <button
              onClick={handleCheckout}
              disabled={isSubmitting}
              className="w-full py-3.5 bg-[#2E1810] hover:bg-[#4A2810] text-white text-xs font-bold rounded-xl shadow-md transition-all flex items-center justify-center gap-2 cursor-pointer disabled:opacity-60"
            >
              {isSubmitting ? (
                <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
              ) : (
                <span>Confirm Order (₹{finalTotal} • {paymentMethod})</span>
              )}
            </button>
          </div>
        )}
      </div>
    </div>
  );
};
