import { GoogleGenAI } from '@google/genai';

const RESTAURANT_SYSTEM_PROMPT = `
You are "Chef Velora", the esteemed Executive Chef & Culinary Sommelier of "D Velora Cafe & Restaurant" (Est. 2026).
D Velora combines artisanal coffee culture with authentic Indian & tandoori family dining.

Our Star Items:
- House Signature: Almond Blueberry Latte (Medium-Dark roast espresso, wild mountain blueberry compote, almond milk, roasted almond crunch - ₹180).
- Starters: Paneer Tikka (Half ₹100 / Full ₹180), Chicken Chilli Boneless (Half ₹120 / Full ₹220), Chicken Lollipop (Half ₹130 / Full ₹240), Paneer Chilli.
- Chef Tandoor & Curries: Tandoori Butter Chicken (Half ₹220 / Full ₹400), Paneer Handi (Half ₹100 / Full ₹180), Chicken Tikka Masala (₹230), Dehati Chicken (Rustic whole garlic village curry - Half ₹230 / Full ₹400), Mutton Kosha (₹270).
- Biryani & Breads: Hyderabadi Chicken Biryani (₹170), Paneer Dum Biryani (₹160), Garlic Naan (₹45), Butter Naan (₹30), Dal Makhani (₹100).
- Cafe & Desserts: Matka Kulfi Ice Cream (₹90), Warm Gulab Jamun (₹60), Velora Cream Cold Coffee (₹180).

Location & Ambiance:
- Address: 42 Promenade Boulevard, Heritage Cafe District.
- Seating: Cozy Indoor Cafe Lounge, Open-Air Patio Terrace, Window Booths, and Family Dining Hall.
- Ambiance: Warm cafe aroma, acoustic jazz, roasted coffee notes, family friendly.
- Hours: Daily 8:00 AM - 11:30 PM.
- Amenities: Complimentary guest Wi-Fi, valet parking, electric vehicle charging, wheel-chair accessible.

Guide guests on customized food pairings, portion recommendations for party sizes, dietary requirements (Veg, Non-Veg, Vegan, Gluten-free substitutions), and travel/parking directions with warmth, sophistication, and enthusiasm. Keep responses concise, appetizing, and inviting.
`;

export async function askChefVelora(query: string, highThinking: boolean = false): Promise<string> {
  const apiKey = (typeof process !== 'undefined' && process.env?.GEMINI_API_KEY) || (import.meta as any).env?.VITE_GEMINI_API_KEY || (import.meta as any).env?.GEMINI_API_KEY;

  if (apiKey) {
    try {
      const ai = new GoogleGenAI({ apiKey });
      const modelName = highThinking ? 'gemini-2.5-pro' : 'gemini-2.5-flash';
      const response = await ai.models.generateContent({
        model: modelName,
        contents: `${RESTAURANT_SYSTEM_PROMPT}\n\nGuest Query: ${query}`
      });

      if (response.text) {
        return response.text;
      }
    } catch (err) {
      console.warn('Chef Velora API call failed, falling back to culinary knowledge base:', err);
    }
  }

  // Intelligent culinary sommelier fallback engine
  const q = query.toLowerCase();

  if (q.includes('almond') || q.includes('latte') || q.includes('blueberry') || q.includes('coffee')) {
    return "Our crowning jewel is the **Almond Blueberry Latte** (₹180). We take freshly pulled medium-dark roast espresso, blend it with artisanal wild mountain blueberry compote, steam silky almond milk to microfoam perfection, and crown it with roasted almond crunch. For a pairing, try it with our warm Gulab Jamun or a light Kathi roll!";
  }

  if (q.includes('pair') || q.includes('dessert') || q.includes('match')) {
    return "For a sublime pairing, Chef Velora recommends balancing the nutty sweetness of our **Almond Blueberry Latte** with the warm floral notes of our **Gulab Jamun (2 pcs)** (₹60). If you are dining for dinner, pairing our charcoal-smoked **Chicken Tikka** with fresh **Garlic Naan** and **Dal Makhani** creates an unforgettable balance of crunch and velvety depth.";
  }

  if (q.includes('dehati') || q.includes('chicken') || q.includes('non-veg') || q.includes('non veg')) {
    return "Our **Dehati Chicken** (Half ₹230 / Full ₹400) is slow-cooked village style in mustard oil with whole roasted pods of garlic and hand-ground rustic spices. We also highly recommend our iconic **Tandoori Butter Chicken** and **Hyderabadi Chicken Biryani** (₹170) made on authentic dum!";
  }

  if (q.includes('parking') || q.includes('direction') || q.includes('address') || q.includes('location') || q.includes('time') || q.includes('hours')) {
    return "D Velora is located at **42 Promenade Boulevard in the Heritage Cafe District**. We are open daily from **8:00 AM to 11:30 PM**. We provide complimentary valet parking at our entrance, dedicated electric vehicle charging stations, and full step-free wheelchair accessibility.";
  }

  if (q.includes('dinner') || q.includes('budget') || q.includes('plan') || q.includes('family') || q.includes('4 person') || q.includes('people')) {
    return "For a 4-person dinner feast under ₹1,000, Chef Velora curates:\n\n1. **Paneer Tikka (Half)** - ₹100\n2. **Chicken Chilli Boneless (Half)** - ₹120\n3. **Dehati Chicken (Half)** or **Paneer Handi (Full)** - ₹230 / ₹180\n4. **Dal Makhani** - ₹100\n5. **4x Butter Naan** - ₹120\n6. **Hyderabadi Chicken Biryani** - ₹170\n\nTotal is right around ~₹840–₹890, leaving room for our Matka Kulfi!";
  }

  return `Welcome to D Velora! As your culinary concierge, I recommend starting with our signature Almond Blueberry Latte or Paneer Tikka. For main dining, our Dehati Chicken slow-simmered with whole roasted garlic or our creamy Dal Makhani with tandoor Butter Naan are guests' top favorites. What flavors are you craving today?`;
}
