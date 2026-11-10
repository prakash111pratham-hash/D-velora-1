# D Velora Cafe & Restaurant - Mobile Companion App

A modern, high-polish Android application for **D Velora Cafe & Restaurant** (Est. 2026), bringing together artisanal cafe culture and authentic restaurant dining with seamless table reservations, interactive digital ordering, a tiered loyalty rewards ecosystem, in-app special offers & notifications, guest reviews, and an AI culinary concierge with Google Maps grounding.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following key decisions were confirmed through interactive clarification and will guide the architecture and implementation:

- **Confirmed Backend & Auth**: Powered by **Firebase Firestore** and **Firebase Authentication with Google Sign-In** via Jetpack `CredentialManager`, ensuring secure, cloud-synced user profiles, real-time table reservations, order history, and loyalty points.
- **Confirmed Service Offerings**: Complete digital menu browsing with **table reservations**, **in-restaurant pre-ordering**, and **express pickup/takeout**.
- **Confirmed Loyalty Architecture**: Tiered membership system (**Bronze Member**, **Silver Connoisseur**, **Gold VIP**) with points per order (10 points per $1 / ₹10), tier milestone progress meters, and an interactive **Rewards Store** to redeem points for signature drinks (e.g., *Almond Blueberry Latte*), appetizers, and billing vouchers.
- **Brand Palette & Theme**: Directly derived from the uploaded logo and website hero:
  - *Light Mode*: Warm ivory cream (`#F9F6F0`), deep espresso roast brown (`#2E1810`), and warm golden harvest amber (`#C89B53`).
  - *Dark Mode*: Obsidian mocha charcoal (`#181412`), velvety roasted bean (`#2A1E17`), and gleaming antique gold (`#E5B869`).

---

## 1. Overview & Core Concept

### What It Does
D Velora Cafe & Restaurant is an all-in-one companion app for guests and patrons. It allows customers to:
1. **Browse an Artisanal Menu**: View the signature cafe creations (e.g. the viral *Almond Blueberry Latte*) and the full family restaurant menu (Indian Veg/Non-Veg, Biryanis, Tandoori Breads, Chinese Fast Food, Starters, Desserts, and Beverages) with half/full portion selectors, search, and dietary badges.
2. **Book Tables Instantly**: Select party size, dining area (Indoor Dining, Cafe Terrace / Patio, Window Booth, Private Family Dining), date, and time slot, with instant confirmation and booking management.
3. **Cart & Pre-Order**: Add items to an intuitive cart with custom kitchen instructions, portion sizes, secure simulated checkout (Card, UPI, Pay at Counter), and real-time status tracking.
4. **Loyalty Rewards & Dashboard**: Track points, view current tier benefits, redeem points for signature items, and display a digital member QR pass for in-restaurant scanning.
5. **Special Offers & Notifications**: Stay informed with seasonal tasting announcements, weekend specials, push-style reward alerts, and reservation reminders.
6. **AI Culinary Concierge & Sommelier**: Powered by Gemini with High Thinking (`gemini-4-argon`) for customized multi-course meal pairings and Gemini with Google Maps Grounding (`gemini-3.8-flash`) for real-time location details, parking advice, and nearby transit directions.
7. **Customer Reviews & Feedback**: Submit 5-star ratings with photo tags, dining compliments, and constructive feedback for restaurant management.

### Target Audience & Persona
- Daily coffee lovers and remote workers visiting for specialty brews and cafe ambiance.
- Families and dinner groups looking for comfortable table reservations and authentic North Indian / Continental meals.
- Frequent diners seeking perks, discounts, and tier upgrades through loyalty rewards.

---

## 2. User Experience & Visual Design

### Key User Flows

```
┌────────────────────────────────────────────────────────────────────────┐
│                        MAIN APP NAVIGATION                             │
├───────────────┬────────────────┬───────────────┬───────────────────────┤
│    ☕ HOME    │    📖 MENU     │  📅 RESERVE   │  ⭐ REWARDS / PROFILE │
├───────────────┴────────────────┴───────────────┴───────────────────────┤
│  • Brand Hero (Almond Blueberry Latte Banner)                          │
│  • Quick Reservation CTA & Active Booking Card                         │
│  • Member Tier Bar & Points Summary                                    │
│  • Chef's Special Carousels & Quick Re-order                           │
│  • Google Maps Restaurant Location & Live Status                       │
└────────────────────────────────────────────────────────────────────────┘
```

1. **Onboarding & Sign-In**:
   - Welcomes users with the D Velora signature coffee & wheat monogram (`DV`).
   - Seamless one-tap **Sign in with Google** via Jetpack `CredentialManager` creates the customer's cloud loyalty account in Firestore.
2. **Home Hub**:
   - Hero banner matching the website attachment with the signature *Almond Blueberry Latte*, roast level (*Medium-Dark*), and acidity notes (*Smooth & Fruity*).
   - Live member card with points balance, progress bar to the next tier, and quick QR code.
   - Quick table booking widget and upcoming reservation pill.
3. **Digital Menu & Ordering**:
   - Tabbed category navigation: *Cafe Signatures*, *Starters*, *Fast Food*, *Indian Veg*, *Indian Non-Veg*, *Biryani & Rice*, *Breads & Dal*, *Desserts & Beverages*.
   - Dish cards with Half/Full portion toggles, spice level indicators, veg/non-veg dots, and one-tap "Add to Order".
   - Sticky floating Cart bar showing count and total price, leading to order review.
4. **Table Reservation Flow**:
   - 3-step interactive booking: Select Date & Time $\rightarrow$ Select Party Size & Seating Zone $\rightarrow$ Dietary / Special Notes & Confirmation.
   - Real-time booking record saved to Firestore under `/users/{uid}/reservations` with status (*Confirmed*, *Completed*, *Cancelled*).
5. **Loyalty Rewards Store**:
   - Interactive points ledger showing lifetime earned, redeemed, and expiring points.
   - Tier benefits chart (Bronze: 5% off, Silver: Free monthly beverage + 10% off, Gold: Priority booking + free appetizer + 15% off).
   - "Claim Reward" buttons that instantly deduct points and generate a unique redemption voucher code.
6. **AI Culinary Concierge**:
   - Accessible via floating action pill: "Ask Chef Velora".
   - Provides intelligent multi-course pairing suggestions for parties or dietary constraints using High Thinking (`gemini-4-argon`).
   - Answers questions about location, parking, table availability, and distance using Google Maps Grounding (`gemini-3.8-flash`).

### Visual Identity & Theme Tokens

| Token | Light Theme Value | Dark Theme Value | Usage |
| :--- | :--- | :--- | :--- |
| **`Primary`** | `#4A2810` (Espresso Roast) | `#E8BD70` (Warm Amber Gold) | Key CTAs, active indicators, titles |
| **`OnPrimary`** | `#FFFFFF` (Pure White) | `#2A1808` (Dark Roast Bean) | Text on primary buttons |
| **`Secondary`** | `#C29B4D` (Harvest Gold) | `#C29B4D` (Harvest Gold) | Badges, rating stars, loyalty accents |
| **`Background`** | `#FAF7F2` (Warm Cafe Cream) | `#141210` (Deep Espresso Night) | Screen background |
| **`Surface`** | `#FFFFFF` (Card White) | `#1F1A16` (Roast Charcoal Surface) | Menu item cards, modal sheets |
| **`SurfaceVariant`** | `#F0EAE1` (Light Froth) | `#2C241E` (Dark Froth) | Pill chips, search input container |
| **`Outline`** | `#DECDBD` (Soft Latte Stroke) | `#443930` (Muted Cocoa Border) | Card borders, dividers |

---

## 3. Key Product Decisions & Trade-Offs

- **Firebase Firestore with Custom DB ID vs Local Room Storage**:
  - *Chosen Approach*: Firebase Firestore initialized with `context.getString(R.string.firestore_database_id)` following strict security rules.
  - *Why*: User points, reservations, and feedback are collaborative, durable assets that must persist across app re-installs and allow restaurant staff to verify bookings.
- **Google Sign-In via Jetpack Credential Manager**:
  - *Chosen Approach*: `GetSignInWithGoogleOption` with `default_web_client_id` and zero anonymous fallback.
  - *Why*: Eliminates friction, delivers authentic email and profile pictures, and adheres to the mandatory zero-trust auth gate.
- **Two-Tier AI Architecture (Thinking Mode + Maps Grounding)**:
  - *Chosen Approach*:
    - Complex recommendation queries (e.g., "Plan a 4-person vegetarian dinner under 800 with dal, paneer, and bread pairings") routed to thinking mode (`gemini-4-argon`) with `ThinkingLevel.HIGH`.
    - Location, route, transit, and landmark queries routed to `gemini-3.8-flash` with Google Maps tool grounding.
  - *Why*: Gives patrons both deep gastronomic reasoning and factual location guidance.

---

## 4. Technical Architecture & Data Strategy

### System Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                        ANDROID COMPOSE UI LAYER                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌────────────┐  │
│  │  HomeScreen  │  │  MenuScreen  │  │ReserveScreen │  │RewardsView │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  └─────┬──────┘  │
└─────────┼─────────────────┼─────────────────┼────────────────┼─────────┘
          │                 │                 │                │
┌─────────▼─────────────────▼─────────────────▼────────────────▼─────────┐
│                           VIEWMODEL LAYER                              │
│   • AuthViewModel (CredentialManager state & session)                  │
│   • MenuViewModel (Portions, Cart, Filter State)                       │
│   • ReservationViewModel (Date/Time, Seating, Firestore Writes)        │
│   • RewardsViewModel (Points, Tier Level, Redemptions)                 │
│   • ConciergeViewModel (Gemini AI Logic + Thinking + Maps Grounding)   │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                          REPOSITORY LAYER                              │
│   • AuthRepository (Google Sign-In, Firebase Auth State)               │
│   • MenuRepository (Local Master Menu with Portion Prices)             │
│   • ReservationRepository (Firestore /users/{uid}/reservations)       │
│   • LoyaltyRepository (Firestore /users/{uid}/loyalty)                 │
│   • FeedbackRepository (Firestore /reviews/{reviewId})                 │
│   • AiConciergeService (Firebase.ai / Thinking / Maps Grounding)       │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                         FIREBASE CLOUD BACKEND                         │
│   • Firebase Authentication (Google Identity Provider)                 │
│   • Cloud Firestore (Named DB: users, reservations, loyalty, reviews)  │
│   • Firebase AI Logic (App Check secured Gemini generative models)     │
└────────────────────────────────────────────────────────────────────────┘
```

### Firestore Schema Specification

1. **User Loyalty & Profile** (`/users/{uid}`):
   ```json
   {
     "userId": "string",
     "displayName": "string",
     "email": "string",
     "photoUrl": "string",
     "loyaltyPoints": 420,
     "tier": "SILVER",
     "createdAt": "serverTimestamp",
     "updatedAt": "serverTimestamp"
   }
   ```
2. **Table Reservations** (`/users/{uid}/reservations/{reservationId}`):
   ```json
   {
     "id": "string",
     "userId": "string",
     "guestName": "string",
     "guestPhone": "string",
     "partySize": 4,
     "seatingArea": "Patio / Window / Main",
     "date": "2026-11-15",
     "timeSlot": "19:30",
     "specialRequests": "string",
     "status": "CONFIRMED",
     "createdAt": "serverTimestamp"
   }
   ```
3. **Loyalty Transactions & Redemptions** (`/users/{uid}/transactions/{txId}`):
   ```json
   {
     "id": "string",
     "userId": "string",
     "title": "Almond Blueberry Latte Free Voucher",
     "pointsChange": -150,
     "type": "REDEEM",
     "voucherCode": "VELORA-LATTE-8472",
     "createdAt": "serverTimestamp"
   }
   ```
4. **Guest Reviews & Feedback** (`/reviews/{reviewId}`):
   ```json
   {
     "id": "string",
     "userId": "string",
     "authorName": "string",
     "rating": 5,
     "foodRating": 5,
     "serviceRating": 5,
     "comment": "Exceptional coffee and paneer tikka!",
     "createdAt": "serverTimestamp"
   }
   ```

---

## 5. Phased Execution Roadmap

1. **Phase 1: Backend Provisioning & Foundation**:
   - Provision Firebase Auth & Firestore using `set_up_firebase` with `"firestore"` and `"ai_logic"`.
   - Setup `firebase-blueprint.json`, write and locally test `firestore.rules` using the Node.js test harness, and deploy via `deploy_firebase`.
2. **Phase 2: App Icon & Brand Assets**:
   - Generate custom adaptive launcher icon incorporating the D Velora monogram, coffee bean, and golden wheat sprig.
   - Configure local strings, app name ("D Velora"), package ID, and colors.
3. **Phase 3: Core Customer Experience (Menu, Cart, Reservations)**:
   - Implement the complete digitized menu from the user's photos with real prices, half/full sizing, and search.
   - Build the interactive Table Reservation system with real-time status and validation.
   - Build the Cart & Simulated Checkout flow with points earning.
4. **Phase 4: Loyalty Program, Notification Hub & Reviews**:
   - Build the Tiered Loyalty Dashboard with QR pass, points progress, and reward redemption.
   - Build the In-App Notification Center with promotional announcements and reservation alerts.
   - Build the 5-Star Feedback and Review submission screen.
5. **Phase 5: AI Concierge with High Thinking & Maps Grounding**:
   - Implement "Chef Velora" with Thinking Mode (`gemini-4-argon`) for culinary curation and Maps Grounding (`gemini-3.8-flash`) for restaurant location and directions.
6. **Phase 6: Verification & Polish**:
   - Full edge-to-edge insets, dark mode testing, and `compile_applet` verification.
