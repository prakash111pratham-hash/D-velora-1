import React from 'react';
import { useApp } from '../context/AppContext';
import { 
  Home, 
  UtensilsCrossed, 
  CalendarDays, 
  Award, 
  Sparkles, 
  Star 
} from 'lucide-react';

export const BottomNav: React.FC = () => {
  const { activeTab, setActiveTab } = useApp();

  const tabs = [
    { key: 'home', label: 'Home', icon: Home },
    { key: 'menu', label: 'Menu', icon: UtensilsCrossed },
    { key: 'reserve', label: 'Book', icon: CalendarDays },
    { key: 'rewards', label: 'Rewards', icon: Award },
    { key: 'concierge', label: 'Chef AI', icon: Sparkles },
    { key: 'reviews', label: 'Reviews', icon: Star }
  ] as const;

  return (
    <nav className="fixed bottom-0 left-0 right-0 z-40 bg-white/95 backdrop-blur-md border-t border-[#DECFC2] py-2 px-3 shadow-lg">
      <div className="max-w-md mx-auto flex items-center justify-around">
        {tabs.map((tab) => {
          const Icon = tab.icon;
          const isActive = activeTab === tab.key;
          return (
            <button
              key={tab.key}
              onClick={() => setActiveTab(tab.key)}
              className={`flex flex-col items-center justify-center py-1 px-2.5 rounded-xl transition-all duration-200 ${
                isActive
                  ? 'text-[#2E1810] font-bold scale-105'
                  : 'text-[#6F5F55] hover:text-[#2E1810]'
              }`}
            >
              <div
                className={`p-1.5 rounded-full transition-colors ${
                  isActive ? 'bg-[#2E1810] text-white shadow-xs' : 'text-inherit'
                }`}
              >
                <Icon className="w-4 h-4" />
              </div>
              <span className="text-[10px] mt-1 tracking-tight">
                {tab.label}
              </span>
            </button>
          );
        })}
      </div>
    </nav>
  );
};
