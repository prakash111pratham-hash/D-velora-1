import { MenuItem, CatalogReward, SpecialOffer } from '../types/models';

export const MENU_ITEMS: MenuItem[] = [
  // House & Cafe Signatures
  {
    id: 'sig_almond_latte',
    name: 'Almond Blueberry Latte',
    description: 'House signature specialty: velvety espresso infused with wild mountain blueberry compote, steamed almond milk & roasted almond crunch. Roast: Medium-Dark.',
    category: 'SIGNATURE',
    dietaryType: 'CHEF_SPECIAL',
    singlePrice: 180,
    rating: 4.9,
    badge: '★ House Signature'
  },
  {
    id: 'sig_cold_coffee',
    name: 'Velora Cream Cold Coffee',
    description: 'Rich artisanal dark espresso whipped with chilled creamy milk, chocolate dust & vanilla scoop.',
    category: 'SIGNATURE',
    dietaryType: 'CHEF_SPECIAL',
    singlePrice: 180,
    rating: 4.8,
    badge: 'Bestseller'
  },
  {
    id: 'sig_hot_choc_coffee',
    name: 'Hot Chocolate Coffee',
    description: 'Single-origin Belgian melted chocolate folded into dark roast espresso & microfoam.',
    category: 'SIGNATURE',
    dietaryType: 'CHEF_SPECIAL',
    singlePrice: 80,
    rating: 4.7
  },

  // Starters
  {
    id: 'st_paneer_chilli',
    name: 'Paneer Chilli',
    description: 'Crisp cottage cheese cubes tossed with bell peppers, green chilies, and tangy house wok sauce.',
    category: 'STARTER',
    dietaryType: 'VEG',
    halfPrice: 90,
    fullPrice: 160,
    rating: 4.8
  },
  {
    id: 'st_paneer_manchurian',
    name: 'Paneer Manchurian',
    description: 'Golden paneer dumplings glazed in spicy Indo-Chinese garlic Manchurian gravy.',
    category: 'STARTER',
    dietaryType: 'VEG',
    halfPrice: 80,
    fullPrice: 150,
    rating: 4.6
  },
  {
    id: 'st_paneer_tikka',
    name: 'Paneer Tikka',
    description: 'Charcoal grilled cottage cheese skewers marinated in hung yogurt, mustard oil, and kashmiri spices.',
    category: 'STARTER',
    dietaryType: 'VEG',
    halfPrice: 100,
    fullPrice: 180,
    rating: 4.9,
    badge: 'Tandoor Special'
  },
  {
    id: 'st_baby_corn_chilli',
    name: 'Baby Corn Chilli',
    description: 'Tender young corn crisped in spiced batter and wok tossed with scallions and soy.',
    category: 'STARTER',
    dietaryType: 'VEG',
    halfPrice: 100,
    fullPrice: 180,
    rating: 4.5
  },
  {
    id: 'st_mushroom_chilli',
    name: 'Mushroom Chilli',
    description: 'Fresh button mushrooms tossed in spicy oriental wok sauce with crunchy bell peppers.',
    category: 'STARTER',
    dietaryType: 'VEG',
    halfPrice: 90,
    fullPrice: 160,
    rating: 4.7
  },
  {
    id: 'st_veg_manchurian',
    name: 'Veg Manchurian',
    description: 'Finely shredded vegetables formed into savory dumplings in rich savory soy sauce.',
    category: 'STARTER',
    dietaryType: 'VEG',
    halfPrice: 70,
    fullPrice: 120,
    rating: 4.6
  },
  {
    id: 'st_chicken_chilli_born',
    name: 'Chicken Chilli (With Bone)',
    description: 'Succulent bone-in chicken tossed in spicy garlic soy, onions, and slit green chilies.',
    category: 'STARTER',
    dietaryType: 'NON_VEG',
    halfPrice: 110,
    fullPrice: 180,
    rating: 4.7
  },
  {
    id: 'st_chicken_chilli_boneless',
    name: 'Chicken Chilli (Boneless)',
    description: 'Juicy tender boneless chicken bites wok fried with fresh capsicum and spicy chilli sauce.',
    category: 'STARTER',
    dietaryType: 'NON_VEG',
    halfPrice: 120,
    fullPrice: 220,
    rating: 4.9,
    badge: 'Top Pick'
  },
  {
    id: 'st_chicken_65',
    name: 'Chicken 65',
    description: 'Famous fiery spiced South-style deep fried chicken tempered with curry leaves and mustard seeds.',
    category: 'STARTER',
    dietaryType: 'NON_VEG',
    halfPrice: 140,
    fullPrice: 250,
    rating: 4.8
  },
  {
    id: 'st_chicken_lollipop',
    name: 'Chicken Lollipop',
    description: 'Frenched chicken drumettes coated in spicy red batter and fried to crunchy perfection.',
    category: 'STARTER',
    dietaryType: 'NON_VEG',
    halfPrice: 130,
    fullPrice: 240,
    rating: 4.8
  },
  {
    id: 'st_chicken_tikka',
    name: 'Chicken Tikka',
    description: 'Boneless chicken marinated in spiced tandoori yogurt and smoked over clay oven charcoal.',
    category: 'STARTER',
    dietaryType: 'NON_VEG',
    halfPrice: 150,
    fullPrice: 280,
    rating: 4.9,
    badge: 'Chef Recommendation'
  },

  // Fast Food & Rolls
  {
    id: 'ff_veg_chowmein',
    name: 'Veg Chowmein',
    description: 'Stir-fried noodles loaded with garden vegetables, spring onions, and oriental seasoning.',
    category: 'FAST_FOOD',
    dietaryType: 'VEG',
    singlePrice: 80,
    rating: 4.5
  },
  {
    id: 'ff_paneer_chowmein',
    name: 'Paneer Chowmein',
    description: 'Savory street-style noodles tossed with seasoned paneer cubes and wok veggies.',
    category: 'FAST_FOOD',
    dietaryType: 'VEG',
    singlePrice: 120,
    rating: 4.6
  },
  {
    id: 'ff_egg_chowmein',
    name: 'Egg Chowmein',
    description: 'Noodles wok-tossed with farm-fresh scrambled eggs and fragrant seasonings.',
    category: 'FAST_FOOD',
    dietaryType: 'NON_VEG',
    singlePrice: 100,
    rating: 4.6
  },
  {
    id: 'ff_chicken_chowmein',
    name: 'Chicken Chowmein',
    description: 'Shredded chicken and fresh crunchy vegetables stir-fried with hot noodles.',
    category: 'FAST_FOOD',
    dietaryType: 'NON_VEG',
    singlePrice: 140,
    rating: 4.8,
    badge: 'Popular'
  },
  {
    id: 'ff_veg_roll',
    name: 'Veg Kathi Roll',
    description: 'Crisp flaky paratha wrapped with seasoned mixed vegetables and house chutneys.',
    category: 'FAST_FOOD',
    dietaryType: 'VEG',
    singlePrice: 50,
    rating: 4.5
  },
  {
    id: 'ff_egg_roll',
    name: 'Egg Roll',
    description: 'Layered flatbread lined with egg, stuffed with sliced onions, chillies, and savory sauce.',
    category: 'FAST_FOOD',
    dietaryType: 'NON_VEG',
    singlePrice: 60,
    rating: 4.6
  },
  {
    id: 'ff_chicken_roll',
    name: 'Chicken Roll',
    description: 'Char-grilled spiced chicken chunks rolled inside warm buttery layered bread.',
    category: 'FAST_FOOD',
    dietaryType: 'NON_VEG',
    singlePrice: 90,
    rating: 4.8
  },
  {
    id: 'ff_paneer_roll',
    name: 'Paneer Roll',
    description: 'Tender spiced cottage cheese strips with salad and mint mayo wrapped in warm roll.',
    category: 'FAST_FOOD',
    dietaryType: 'VEG',
    singlePrice: 80,
    rating: 4.7
  },

  // Indian Veg
  {
    id: 'veg_paneer_handi',
    name: 'Paneer Handi',
    description: 'Cottage cheese simmered in earthenware handi with rich aromatic tomato onion gravy.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    halfPrice: 100,
    fullPrice: 180,
    rating: 4.9,
    badge: 'House Special'
  },
  {
    id: 'veg_paneer_masala',
    name: 'Paneer Masala',
    description: 'Classic homestyle paneer cooked in spiced masala gravy with fresh coriander.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    halfPrice: 80,
    fullPrice: 150,
    rating: 4.6
  },
  {
    id: 'veg_paneer_kadai',
    name: 'Paneer Kadai',
    description: 'Diced cottage cheese tossed with freshly pounded coriander seeds, dried chilies, and bell peppers.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    halfPrice: 100,
    fullPrice: 180,
    rating: 4.7
  },
  {
    id: 'veg_paneer_do_pyaja',
    name: 'Paneer Do Pyaja',
    description: 'Creamy paneer cubes prepared with caramelized shallots and two varieties of onions.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    halfPrice: 110,
    fullPrice: 190,
    rating: 4.7
  },
  {
    id: 'veg_paneer_tikka_masala',
    name: 'Paneer Tikka Masala',
    description: 'Smoky tandoori paneer tikka pieces drenched in luxurious buttery tomato-cream sauce.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    halfPrice: 120,
    fullPrice: 220,
    rating: 4.8
  },
  {
    id: 'veg_matar_paneer',
    name: 'Matar Paneer',
    description: 'Fresh green garden peas and soft paneer cooked in savory spiced onion gravy.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    singlePrice: 160,
    rating: 4.5
  },
  {
    id: 'veg_palak_paneer',
    name: 'Palak Paneer',
    description: 'Pureed fresh spinach tempered with garlic, cumin, cream, and paneer cubes.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    halfPrice: 110,
    fullPrice: 190,
    rating: 4.7
  },
  {
    id: 'veg_shahi_paneer',
    name: 'Shahi Paneer',
    description: 'Royal Mughlai preparation with cashew nut paste, fragrant cardamom, and silky cream.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    halfPrice: 120,
    fullPrice: 200,
    rating: 4.8
  },
  {
    id: 'veg_paneer_butter_masala',
    name: 'Paneer Butter Masala',
    description: 'Velvety smooth butter and cashew tomato gravy enveloping soft cottage cheese.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    singlePrice: 160,
    rating: 4.9,
    badge: 'Most Loved'
  },
  {
    id: 'veg_paneer_malai_kofta',
    name: 'Paneer Malai Kofta',
    description: 'Melt-in-mouth cottage cheese and potato dumplings bathed in rich golden gravy.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    singlePrice: 180,
    rating: 4.8
  },
  {
    id: 'veg_mushroom_masala',
    name: 'Mushroom Masala',
    description: 'Fresh button mushrooms cooked with ginger, garlic, and robust country spices.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    singlePrice: 165,
    rating: 4.6
  },
  {
    id: 'veg_mix_veg',
    name: 'Mix Veg',
    description: 'Melange of seasonal vegetables simmered with cumin and hand-ground spices.',
    category: 'INDIAN_VEG',
    dietaryType: 'VEG',
    singlePrice: 140,
    rating: 4.4
  },

  // Indian Non-Veg
  {
    id: 'nv_dehati_chicken',
    name: 'Dehati Chicken',
    description: 'Authentic rustic village-style chicken curry slow-cooked with whole roasted garlic and mustard oil.',
    category: 'INDIAN_NON_VEG',
    dietaryType: 'NON_VEG',
    halfPrice: 230,
    fullPrice: 400,
    rating: 5.0,
    badge: 'D Velora Signature'
  },
  {
    id: 'nv_chicken_masala',
    name: 'Chicken Masala',
    description: 'Tender chicken pieces simmered in thick spicy onion and tomato masala gravy.',
    category: 'INDIAN_NON_VEG',
    dietaryType: 'NON_VEG',
    halfPrice: 120,
    fullPrice: 200,
    rating: 4.7
  },
  {
    id: 'nv_chicken_curry',
    name: 'Chicken Curry',
    description: 'Comforting traditional home-style chicken curry with mild aromatic gravy.',
    category: 'INDIAN_NON_VEG',
    dietaryType: 'NON_VEG',
    halfPrice: 110,
    fullPrice: 190,
    rating: 4.6
  },
  {
    id: 'nv_chicken_kadai',
    name: 'Chicken Kadai',
    description: 'Chicken braised in cast iron wok with coarsely crushed spices, capsicum, and tomato.',
    category: 'INDIAN_NON_VEG',
    dietaryType: 'NON_VEG',
    halfPrice: 130,
    fullPrice: 220,
    rating: 4.8
  },
  {
    id: 'nv_chicken_tikka_masala',
    name: 'Chicken Tikka Masala',
    description: 'Charcoal roasted chicken tikka chunks folded into rich, creamy tomato butter gravy.',
    category: 'INDIAN_NON_VEG',
    dietaryType: 'NON_VEG',
    singlePrice: 230,
    rating: 4.9
  },
  {
    id: 'nv_tandoori_butter_chicken',
    name: 'Tandoori Butter Chicken',
    description: 'Iconic clay-oven tandoori chicken simmered in silky makhani gravy enriched with fresh butter.',
    category: 'INDIAN_NON_VEG',
    dietaryType: 'NON_VEG',
    halfPrice: 220,
    fullPrice: 400,
    rating: 5.0,
    badge: 'Chef Special'
  },
  {
    id: 'nv_mutton_curry',
    name: 'Mutton Curry',
    description: 'Tender bone-in mutton slow-braised with caramelized onions and whole spices.',
    category: 'INDIAN_NON_VEG',
    dietaryType: 'NON_VEG',
    halfPrice: 140,
    fullPrice: 250,
    rating: 4.7
  },
  {
    id: 'nv_mutton_kosha',
    name: 'Mutton Kosha',
    description: 'Celebrated slow-cooked mutton in thick, glossy, intensely spiced dark gravy.',
    category: 'INDIAN_NON_VEG',
    dietaryType: 'NON_VEG',
    singlePrice: 270,
    rating: 4.9,
    badge: 'Must Try'
  },
  {
    id: 'nv_egg_curry',
    name: 'Egg Curry',
    description: 'Fried boiled eggs bathed in seasoned North Indian onion and tomato gravy.',
    category: 'INDIAN_NON_VEG',
    dietaryType: 'NON_VEG',
    halfPrice: 80,
    fullPrice: 100,
    rating: 4.5
  },

  // Biryani & Rice
  {
    id: 'bir_paneer',
    name: 'Paneer Dum Biryani',
    description: 'Aromatic long-grain basmati rice layered with spiced paneer, mint, saffron, and ghee.',
    category: 'BIRYANI_RICE',
    dietaryType: 'VEG',
    singlePrice: 160,
    rating: 4.8
  },
  {
    id: 'bir_chicken',
    name: 'Hyderabadi Chicken Biryani',
    description: 'Fragrant basmati rice slow-cooked on dum with marinated chicken, fried onions, and kewra.',
    category: 'BIRYANI_RICE',
    dietaryType: 'NON_VEG',
    singlePrice: 170,
    rating: 4.9,
    badge: 'Bestseller'
  },
  {
    id: 'bir_chicken_egg',
    name: 'Chicken Egg Biryani',
    description: 'Royal dum biryani featuring juicy chicken pieces along with seasoned boiled eggs.',
    category: 'BIRYANI_RICE',
    dietaryType: 'NON_VEG',
    singlePrice: 180,
    rating: 4.8
  },
  {
    id: 'rice_jeera',
    name: 'Jeera Rice',
    description: 'Basmati rice tempered with roasted cumin seeds and farm butter.',
    category: 'BIRYANI_RICE',
    dietaryType: 'VEG',
    halfPrice: 45,
    fullPrice: 70,
    rating: 4.6
  },
  {
    id: 'rice_kashmiri_pulao',
    name: 'Kashmiri Pulao',
    description: 'Sweet and savory saffron basmati rice loaded with dried fruits, nuts, and fresh fruits.',
    category: 'BIRYANI_RICE',
    dietaryType: 'VEG',
    singlePrice: 180,
    rating: 4.7
  },
  {
    id: 'rice_veg_fried',
    name: 'Veg Fried Rice',
    description: 'Wok-tossed basmati rice with finely diced carrots, beans, and spring onion greens.',
    category: 'BIRYANI_RICE',
    dietaryType: 'VEG',
    halfPrice: 55,
    fullPrice: 90,
    rating: 4.5
  },

  // Breads & Dal
  {
    id: 'bread_butter_naan',
    name: 'Butter Naan',
    description: 'Soft leavened tandoor flatbread brushed lavishly with melted butter.',
    category: 'ROTI_DAL',
    dietaryType: 'VEG',
    singlePrice: 30,
    rating: 4.8
  },
  {
    id: 'bread_garlic_naan',
    name: 'Garlic Naan',
    description: 'Clay-oven baked naan infused with minced roasted garlic and fresh coriander.',
    category: 'ROTI_DAL',
    dietaryType: 'VEG',
    singlePrice: 45,
    rating: 4.9
  },
  {
    id: 'bread_lachha_paratha',
    name: 'Lachha Paratha',
    description: 'Multi-layered flaky whole wheat paratha crisped on the tandoor walls.',
    category: 'ROTI_DAL',
    dietaryType: 'VEG',
    singlePrice: 40,
    rating: 4.7
  },
  {
    id: 'bread_paneer_paratha',
    name: 'Paneer Paratha',
    description: 'Whole wheat flatbread stuffed generously with seasoned grated paneer.',
    category: 'ROTI_DAL',
    dietaryType: 'VEG',
    singlePrice: 60,
    rating: 4.8
  },
  {
    id: 'bread_tandoori_butter_roti',
    name: 'Tandoori Butter Roti',
    description: 'Whole wheat clay oven roti finished with pure butter.',
    category: 'ROTI_DAL',
    dietaryType: 'VEG',
    singlePrice: 18,
    rating: 4.6
  },
  {
    id: 'dal_makhani',
    name: 'Dal Makhani',
    description: 'Black lentils and red kidney beans slow-simmered overnight with butter and fresh cream.',
    category: 'ROTI_DAL',
    dietaryType: 'VEG',
    singlePrice: 100,
    rating: 4.9,
    badge: 'Classic'
  },
  {
    id: 'dal_tarka',
    name: 'Tarka Dal',
    description: 'Yellow lentils tempered with ghee, cumin seeds, garlic, and dried red chilies.',
    category: 'ROTI_DAL',
    dietaryType: 'VEG',
    singlePrice: 80,
    rating: 4.6
  },

  // Desserts & Cafe
  {
    id: 'des_gulab_jamun',
    name: 'Gulab Jamun (2 pcs)',
    description: 'Warm fried milk dumplings soaked in aromatic cardamom and rose sugar syrup.',
    category: 'DESSERT_CAFE',
    dietaryType: 'VEG',
    singlePrice: 60,
    rating: 4.8
  },
  {
    id: 'des_matka_ice_cream',
    name: 'Matka Kulfi Ice Cream',
    description: 'Traditional rich saffron-pistachio kulfi set and served in terracotta pot.',
    category: 'DESSERT_CAFE',
    dietaryType: 'VEG',
    singlePrice: 90,
    rating: 4.9
  },
  {
    id: 'bev_hot_coffee',
    name: 'Hot Artisan Coffee',
    description: 'Freshly brewed roasted coffee with steamed frothy milk.',
    category: 'DESSERT_CAFE',
    dietaryType: 'VEG',
    singlePrice: 40,
    rating: 4.7
  },
  {
    id: 'bev_masala_tea',
    name: 'Special Masala Tea',
    description: 'Simmered Assam tea leaves infused with crushed ginger, cloves, and cardamom.',
    category: 'DESSERT_CAFE',
    dietaryType: 'VEG',
    singlePrice: 30,
    rating: 4.7
  },
  {
    id: 'bev_french_fries',
    name: 'French Fries',
    description: 'Crispy golden potato fingers tossed with sea salt and peri peri spices.',
    category: 'DESSERT_CAFE',
    dietaryType: 'VEG',
    singlePrice: 80,
    rating: 4.5
  }
];

export const CATALOG_REWARDS: CatalogReward[] = [
  {
    id: 'rew_latte',
    title: 'Free Almond Blueberry Latte',
    description: 'Redeem 150 points for our signature house latte, hot or iced.',
    pointsCost: 150,
    category: 'Beverage',
    badge: 'Signature Perk'
  },
  {
    id: 'rew_appetizer',
    title: 'Complimentary Paneer Chilli / Chicken Chilli',
    description: 'Enjoy a free half starter plate of your choice on your next dine-in.',
    pointsCost: 250,
    category: 'Starter',
    badge: 'Popular'
  },
  {
    id: 'rew_discount_100',
    title: '₹100 Off Dining Voucher',
    description: 'Direct ₹100 deduction on any order over ₹500.',
    pointsCost: 200,
    category: 'Voucher',
    badge: 'Instant Cash'
  },
  {
    id: 'rew_dessert',
    title: 'Free Gulab Jamun & Ice Cream',
    description: 'Sweet ending: warm gulab jamun with matka ice cream on the house.',
    pointsCost: 120,
    category: 'Dessert',
    badge: 'Sweet Treat'
  },
  {
    id: 'rew_chef_platter',
    title: "Chef's Special Tasting Platter",
    description: 'VIP exclusive appetizer and mocktail pairing curated by the executive chef.',
    pointsCost: 500,
    category: 'VIP Special',
    badge: 'Gold VIP Exclusive'
  }
];

export const SPECIAL_OFFERS: SpecialOffer[] = [
  {
    id: 'off_almond',
    title: 'Almond Blueberry Celebration',
    description: '20% off our signature Almond Blueberry Latte every morning from 8 AM to 11 AM.',
    promoCode: 'BLUEBERRY20',
    discountPercent: 20,
    validUntil: 'Daily 8 AM - 11 AM'
  },
  {
    id: 'off_family',
    title: 'Family Dining Weekend Feast',
    description: 'Get 15% off total bill on table reservations of 4 or more guests this weekend.',
    promoCode: 'FAMILYFEAST',
    discountPercent: 15,
    validUntil: 'Weekends'
  },
  {
    id: 'off_tandoor',
    title: 'Tandoori Night Special',
    description: 'Order any Biryani and get a Butter Naan or Roti at 50% off.',
    promoCode: 'TANDOOR50',
    discountPercent: 50,
    validUntil: 'Evenings after 7 PM'
  }
];

export const INITIAL_REVIEWS = [
  {
    id: 'rev_1',
    userId: 'user_1',
    authorName: 'Aarav Sharma',
    rating: 5,
    foodRating: 5,
    serviceRating: 5,
    comment: 'The Almond Blueberry Latte lived up to every expectation! The medium-dark roast with real berry compote is sensational. We also loved the Dehati Chicken and Butter Naan.',
    createdAt: '2 days ago'
  },
  {
    id: 'rev_2',
    userId: 'user_2',
    authorName: 'Sneha Patel',
    rating: 5,
    foodRating: 5,
    serviceRating: 4,
    comment: 'Booked a window booth for a family dinner. Tandoori Butter Chicken and Dal Makhani were creamy perfection. Love the digital loyalty QR pass too!',
    createdAt: '4 days ago'
  },
  {
    id: 'rev_3',
    userId: 'user_3',
    authorName: 'Rohan Verma',
    rating: 5,
    foodRating: 5,
    serviceRating: 5,
    comment: 'Best cafe ambiance in the district. The acoustic jazz and open-air patio make it wonderful for both remote coffee work and evening feasts.',
    createdAt: '1 week ago'
  }
];
