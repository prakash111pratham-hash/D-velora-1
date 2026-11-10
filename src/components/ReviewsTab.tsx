import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { Star, MessageSquarePlus, Check, X } from 'lucide-react';

export const ReviewsTab: React.FC = () => {
  const { user, reviews, addReview } = useApp();
  const [showModal, setShowModal] = useState(false);

  // Form State
  const [authorName, setAuthorName] = useState(user?.displayName || 'Guest Patron');
  const [overallRating, setOverallRating] = useState(5);
  const [foodRating, setFoodRating] = useState(5);
  const [serviceRating, setServiceRating] = useState(5);
  const [comment, setComment] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!comment.trim() || !authorName.trim()) return;

    addReview(authorName, overallRating, foodRating, serviceRating, comment);
    setComment('');
    setShowModal(false);
  };

  return (
    <div className="space-y-6 pb-28">
      {/* Header */}
      <div>
        <h2 className="font-serif text-2xl sm:text-3xl font-bold text-[#2E1810]">
          Guest Reviews
        </h2>
        <p className="text-xs font-semibold text-[#8D5B28]">
          Community dining experiences & authentic patron feedback
        </p>
      </div>

      {/* Summary Rating Card */}
      <div className="bg-white rounded-2xl border border-[#DECFC2] p-5 shadow-xs">
        <div className="flex items-center justify-between">
          <div>
            <div className="flex items-center gap-2">
              <span className="font-serif text-3xl sm:text-4xl font-bold text-[#2E1810]">
                4.9
              </span>
              <div className="flex text-[#D97706]">
                {[1, 2, 3, 4, 5].map((i) => (
                  <Star key={i} className="w-4 h-4 fill-current" />
                ))}
              </div>
            </div>
            <p className="text-xs text-[#6F5F55] mt-0.5">
              Based on verified patron dining visits
            </p>
          </div>

          <button
            onClick={() => setShowModal(true)}
            className="flex items-center gap-1.5 bg-[#C5A059] hover:bg-[#d6b26a] text-white text-xs font-bold px-3.5 py-2 rounded-xl shadow-xs transition-colors"
          >
            <MessageSquarePlus className="w-4 h-4" />
            <span>Rate Us</span>
          </button>
        </div>

        <div className="grid grid-cols-3 gap-2 mt-4 pt-4 border-t border-[#DECFC2]/50 text-center">
          <div className="bg-[#FAF7F2] p-2 rounded-xl">
            <span className="text-xs font-bold text-[#2E1810] block">5.0 ★</span>
            <span className="text-[10px] text-[#6F5F55]">Food Taste</span>
          </div>
          <div className="bg-[#FAF7F2] p-2 rounded-xl">
            <span className="text-xs font-bold text-[#2E1810] block">4.8 ★</span>
            <span className="text-[10px] text-[#6F5F55]">Hospitality</span>
          </div>
          <div className="bg-[#FAF7F2] p-2 rounded-xl">
            <span className="text-xs font-bold text-[#2E1810] block">4.9 ★</span>
            <span className="text-[10px] text-[#6F5F55]">Ambiance</span>
          </div>
        </div>
      </div>

      {/* Reviews List */}
      <div className="space-y-3.5">
        {reviews.map((rev) => (
          <div
            key={rev.id}
            className="bg-white rounded-2xl border border-[#DECFC2] p-4 sm:p-5 shadow-2xs space-y-2.5"
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2.5">
                <div className="w-8 h-8 rounded-full bg-[#FAF7F2] border border-[#DECFC2] text-[#2E1810] flex items-center justify-center font-bold text-xs">
                  {rev.authorName.charAt(0).toUpperCase()}
                </div>
                <div>
                  <h4 className="text-xs font-bold text-[#2E1810]">
                    {rev.authorName}
                  </h4>
                  <span className="text-[10px] text-[#6F5F55]">{rev.createdAt}</span>
                </div>
              </div>

              <div className="flex text-[#D97706]">
                {Array.from({ length: rev.rating }).map((_, i) => (
                  <Star key={i} className="w-3.5 h-3.5 fill-current" />
                ))}
              </div>
            </div>

            <p className="text-xs sm:text-sm text-[#2E1810] leading-relaxed">
              {rev.comment}
            </p>

            <div className="flex gap-3 text-[10px] text-[#8D5B28] font-semibold pt-2 border-t border-[#DECFC2]/40">
              <span>Food: {rev.foodRating}★</span>
              <span>•</span>
              <span>Service: {rev.serviceRating}★</span>
            </div>
          </div>
        ))}
      </div>

      {/* Write Review Modal */}
      {showModal && (
        <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-xs flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-[#DECFC2] relative">
            <button
              onClick={() => setShowModal(false)}
              className="absolute top-4 right-4 p-1.5 rounded-full hover:bg-[#FAF7F2] text-[#6F5F55]"
            >
              <X className="w-5 h-5" />
            </button>

            <h3 className="font-serif font-bold text-lg text-[#2E1810] mb-1">
              Share Your Dining Experience
            </h3>
            <p className="text-xs text-[#8D5B28] mb-4">
              Your feedback inspires our kitchen and service team (+25 Pts awarded)
            </p>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-xs font-bold text-[#6F5F55] mb-1">Your Name</label>
                <input
                  type="text"
                  required
                  value={authorName}
                  onChange={(e) => setAuthorName(e.target.value)}
                  className="w-full px-3 py-2 bg-[#FAF7F2] border border-[#DECFC2] rounded-xl text-xs focus:outline-none focus:border-[#C5A059]"
                />
              </div>

              {/* Star Selectors */}
              <div>
                <label className="block text-xs font-bold text-[#6F5F55] mb-1">Overall Rating</label>
                <div className="flex gap-2">
                  {[1, 2, 3, 4, 5].map((star) => (
                    <button
                      key={star}
                      type="button"
                      onClick={() => setOverallRating(star)}
                      className="p-1 hover:scale-110 transition-transform"
                    >
                      <Star
                        className={`w-6 h-6 ${
                          star <= overallRating ? 'text-[#D97706] fill-current' : 'text-gray-300'
                        }`}
                      />
                    </button>
                  ))}
                </div>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-[11px] font-bold text-[#6F5F55] mb-1">Food Taste</label>
                  <div className="flex gap-1">
                    {[1, 2, 3, 4, 5].map((star) => (
                      <button
                        key={star}
                        type="button"
                        onClick={() => setFoodRating(star)}
                      >
                        <Star
                          className={`w-4 h-4 ${
                            star <= foodRating ? 'text-[#D97706] fill-current' : 'text-gray-300'
                          }`}
                        />
                      </button>
                    ))}
                  </div>
                </div>

                <div>
                  <label className="block text-[11px] font-bold text-[#6F5F55] mb-1">Hospitality</label>
                  <div className="flex gap-1">
                    {[1, 2, 3, 4, 5].map((star) => (
                      <button
                        key={star}
                        type="button"
                        onClick={() => setServiceRating(star)}
                      >
                        <Star
                          className={`w-4 h-4 ${
                            star <= serviceRating ? 'text-[#D97706] fill-current' : 'text-gray-300'
                          }`}
                        />
                      </button>
                    ))}
                  </div>
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold text-[#6F5F55] mb-1">
                  Comments / Favorite Dish
                </label>
                <textarea
                  required
                  rows={3}
                  value={comment}
                  onChange={(e) => setComment(e.target.value)}
                  placeholder="The Almond Blueberry Latte was extraordinary..."
                  className="w-full px-3 py-2 bg-[#FAF7F2] border border-[#DECFC2] rounded-xl text-xs focus:outline-none focus:border-[#C5A059]"
                />
              </div>

              <button
                type="submit"
                className="w-full py-3 bg-[#2E1810] hover:bg-[#4A2810] text-white text-xs font-bold rounded-xl shadow-md transition-colors"
              >
                Submit Review (+25 Pts)
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
