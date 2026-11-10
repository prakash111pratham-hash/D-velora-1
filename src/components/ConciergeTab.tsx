import React, { useState, useRef, useEffect } from 'react';
import { useApp } from '../context/AppContext';
import { askChefVelora } from '../services/aiConcierge';
import { ChatMessage } from '../types/models';
import { 
  Sparkles, 
  Send, 
  Bot, 
  User, 
  Lightbulb, 
  Clock, 
  MapPin, 
  Coffee 
} from 'lucide-react';

export const ConciergeTab: React.FC = () => {
  const { user } = useApp();

  const [messages, setMessages] = useState<ChatMessage[]>([
    {
      id: 'msg_0',
      sender: 'ai',
      message: `Greetings! I am Chef Velora, Executive Chef & Sommelier of D Velora Cafe & Restaurant. How may I assist your culinary experience today? Feel free to ask for pairings, recommendations, or questions about our signature Almond Blueberry Latte!`,
      timestamp: 'Just now'
    }
  ]);

  const [inputQuery, setInputQuery] = useState('');
  const [isThinking, setIsThinking] = useState(false);
  const [highThinkingMode, setHighThinkingMode] = useState(true);
  const chatBottomRef = useRef<HTMLDivElement>(null);

  const promptSuggestions = [
    'Pair a coffee with dessert',
    'What makes the Almond Blueberry Latte special?',
    'Plan a 4-person dinner under ₹1000',
    'Tell me about parking & transit directions',
    'What is Dehati Chicken cooked with?'
  ];

  useEffect(() => {
    chatBottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isThinking]);

  const handleSendMessage = async (textToSend?: string) => {
    const text = (textToSend || inputQuery).trim();
    if (!text || isThinking) return;

    const userMsg: ChatMessage = {
      id: 'usr_' + Date.now(),
      sender: 'user',
      message: text,
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    };

    setMessages((prev) => [...prev, userMsg]);
    if (!textToSend) setInputQuery('');
    setIsThinking(true);

    try {
      const response = await askChefVelora(text, highThinkingMode);
      const aiMsg: ChatMessage = {
        id: 'ai_' + Date.now(),
        sender: 'ai',
        message: response,
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
      };
      setMessages((prev) => [...prev, aiMsg]);
    } catch (err) {
      console.error('Error querying Chef Velora:', err);
      const errMsg: ChatMessage = {
        id: 'err_' + Date.now(),
        sender: 'ai',
        message: 'Chef Velora is currently overseeing the tandoor kitchen. For today, our signature Almond Blueberry Latte paired with Paneer Tikka or Dehati Chicken is highly recommended!',
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
      };
      setMessages((prev) => [...prev, errMsg]);
    } finally {
      setIsThinking(false);
    }
  };

  return (
    <div className="flex flex-col h-[calc(100vh-140px)] max-w-2xl mx-auto pb-20">
      {/* Header */}
      <div className="mb-3">
        <h2 className="font-serif text-2xl font-bold text-[#2E1810] flex items-center gap-2">
          <Sparkles className="w-5 h-5 text-[#C5A059]" />
          Chef Velora AI Concierge
        </h2>
        <p className="text-xs text-[#8D5B28]">
          Culinary sommelier, food pairings & restaurant guidance
        </p>
      </div>

      {/* Mode Toggle Banner */}
      <div className="bg-white rounded-xl border border-[#DECFC2] p-3 shadow-2xs mb-3 flex items-center justify-between">
        <div className="flex items-center gap-2.5">
          <div className="w-8 h-8 rounded-full bg-[#FAF7F2] border border-[#DECFC2] flex items-center justify-center text-[#C5A059]">
            <Sparkles className="w-4 h-4" />
          </div>
          <div>
            <span className="text-xs font-bold text-[#2E1810] block">
              {highThinkingMode ? 'High Thinking Culinary Sommelier' : 'Express Flash Mode'}
            </span>
            <span className="text-[10px] text-[#6F5F55]">
              {highThinkingMode ? 'Deep culinary reasoning with flavor balance' : 'Fast casual answers'}
            </span>
          </div>
        </div>

        <button
          onClick={() => setHighThinkingMode(!highThinkingMode)}
          className={`w-11 h-6 rounded-full transition-colors p-0.5 ${
            highThinkingMode ? 'bg-[#2E1810]' : 'bg-gray-300'
          }`}
        >
          <div
            className={`w-5 h-5 rounded-full bg-white transition-transform ${
              highThinkingMode ? 'translate-x-5' : 'translate-x-0'
            }`}
          />
        </button>
      </div>

      {/* Suggestion Chips */}
      <div className="flex gap-2 overflow-x-auto pb-2 scrollbar-none mb-2">
        {promptSuggestions.map((prompt) => (
          <button
            key={prompt}
            onClick={() => handleSendMessage(prompt)}
            className="px-3 py-1.5 rounded-full bg-white hover:bg-[#FAF7F2] border border-[#DECFC2] text-xs font-medium text-[#2E1810] shrink-0 transition-colors shadow-2xs"
          >
            {prompt}
          </button>
        ))}
      </div>

      {/* Chat Messages Stream */}
      <div className="flex-1 overflow-y-auto space-y-3.5 pr-1 scrollbar-thin">
        {messages.map((msg) => {
          const isUser = msg.sender === 'user';
          return (
            <div
              key={msg.id}
              className={`flex items-start gap-2.5 ${isUser ? 'flex-row-reverse' : 'flex-row'}`}
            >
              <div
                className={`w-8 h-8 rounded-full flex items-center justify-center shrink-0 text-xs font-bold ${
                  isUser
                    ? 'bg-[#2E1810] text-white'
                    : 'bg-[#C5A059] text-[#2E1810]'
                }`}
              >
                {isUser ? (
                  <User className="w-4 h-4 text-white" />
                ) : (
                  <Sparkles className="w-4 h-4 text-[#2E1810]" />
                )}
              </div>

              <div
                className={`max-w-[85%] rounded-2xl p-3.5 text-xs sm:text-sm leading-relaxed whitespace-pre-line shadow-2xs ${
                  isUser
                    ? 'bg-[#2E1810] text-white rounded-tr-none'
                    : 'bg-white text-[#2E1810] border border-[#DECFC2] rounded-tl-none'
                }`}
              >
                {msg.message}
                <div
                  className={`text-[9px] mt-1 text-right ${
                    isUser ? 'text-white/60' : 'text-[#6F5F55]'
                  }`}
                >
                  {msg.timestamp}
                </div>
              </div>
            </div>
          );
        })}

        {isThinking && (
          <div className="flex items-center gap-2 text-xs text-[#8D5B28] italic bg-white/70 p-3 rounded-2xl border border-[#DECFC2]/50 w-fit">
            <div className="w-4 h-4 border-2 border-[#C5A059]/40 border-t-[#C5A059] rounded-full animate-spin" />
            <span>Chef Velora is deliberating pairings and flavors...</span>
          </div>
        )}

        <div ref={chatBottomRef} />
      </div>

      {/* Input Field */}
      <div className="pt-2">
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSendMessage();
          }}
          className="flex items-center gap-2 bg-white rounded-2xl border border-[#DECFC2] p-1.5 shadow-sm focus-within:border-[#C5A059]"
        >
          <input
            type="text"
            value={inputQuery}
            onChange={(e) => setInputQuery(e.target.value)}
            placeholder="Ask Chef Velora anything..."
            className="flex-1 px-3 py-2 text-xs sm:text-sm bg-transparent focus:outline-none"
          />
          <button
            type="submit"
            disabled={!inputQuery.trim() || isThinking}
            className="w-10 h-10 rounded-xl bg-[#2E1810] hover:bg-[#4A2810] disabled:opacity-50 text-white flex items-center justify-center transition-all cursor-pointer shadow-xs"
          >
            <Send className="w-4 h-4 text-[#C5A059]" />
          </button>
        </form>
      </div>
    </div>
  );
};
