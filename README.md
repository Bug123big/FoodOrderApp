Jixiao Express 🍜

A Java-based food ordering application that allows users to order meals, desserts, drinks, and combo meals with automatic discounts and payment processing.

📌 Features
Interactive food ordering system
Main food, desserts, drinks, and snack menu
Adult and children combo meals
Automatic combo discounts
Random special discounts
Order summary and receipt
Multiple payment methods
Tax calculation
Delete and modify orders
Order code generation
🥡 Menu Categories
Main Food
10 different main dishes
Desserts
10 dessert options
Drinks
10 drink options
Snacks
Additional snack items
👨‍👩‍👧 Combo System
Adult Combo

Includes:

Main Food
Dessert
Drink
Children Combo

Includes:

Children’s Main Food
Healthy Dessert
Children’s Drink
💸 Discount System
Combo Discount
1 person combo → 5% off
Each additional person → +3% off
Maximum discount → 20% off
Special Discounts
Food Type	Chance	Discount
Dessert	30%	20% – 50%
Staple Food	20%	15% – 35%
Drink	15%	10% – 25%
Main Food	5%	5% – 15%
💳 Payment Methods
Credit
Debit
Cash
E-transfer

The app also:

Calculates tax
Applies discounts
Generates an order code
🏗️ Project Structure
Main Class
TestFood

Controls application startup and flow.

Methods:

main()
startApp()
Display Class

Handles all user interface pages.

Methods:

showFrontPage()
showMenuPage()
showOrderPage()
showComboPage()
showPaymentPage()
showOrderSummary()
Order Class

Stores and manages customer orders.

Variables:

ArrayList<Entrees> entrees
ArrayList<Mains> mains
ArrayList<Desserts> desserts
ArrayList<Drinks> drinks
int people

Methods:

addFood()
deleteFood()
calculateOriginalCost()
calculateDiscount()
calculateFinalCost()
Food Classes
Entrees / Desserts / Drinks

Variables:

String name
double price
int amount

Methods:

order()
setName()
setPrice()
reset()
DiscountSystem Class

Methods:

getComboDiscount()
getSpecialDiscount()
calculateTax()
Payment Class

Variables:

String method
double tax
int orderCode

Methods:

pay()
generateCode()
Menu Class

Stores all food items, prices, and images using ArrayList.

The menu remains constant while selected items are added into the Order class.

💻 Concepts Used
Object-Oriented Programming (OOP)
Classes and Objects
ArrayLists
Encapsulation
User Input Handling
Randomized Discounts
Method Interaction
🎯 Project Goal：

Jixiao Express was created to practice Java programming and object-oriented design by building a realistic food ordering application with structured classes, interactive features, and organized data management.
