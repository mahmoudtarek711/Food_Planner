# 🍽️ ForkFlow: A Food Planner Android App

A feature-rich Android application designed for food enthusiasts to discover meals, plan their weekly nutrition, and manage their favorite recipes. This project follows the **MVP (Model-View-Presenter)** architecture and utilizes **ReactiveX (RxJava)** for asynchronous data handling.

## 🚀 Features

### 1. Discovery & Search

* **Meal of the Day:** Get daily inspiration with a random meal that refreshes periodically using `SharedPreferences`.
* **Browse by Category:** View a diverse list of meal categories (Seafood, Vegan, Dessert, etc.).
* **Browse by Area:** Explore popular dishes from different countries.
* **Advanced Search:** Search for specific meals based on ingredients, categories, or country of origin.

### 2. Detailed Meal View

* **Comprehensive Info:** View meal name, images, and country of origin.
* **Ingredients & Steps:** Detailed list of ingredients with images and step-by-step instructions.
* **Embedded Video:** Watch tutorials via the integrated YouTube player.

### 3. Personalization & Planning

* **Favorites:** Save meals to a local **Room Database** for offline access.
* **Weekly Planner:** Add meals to specific days. The date picker is restricted to "today or future" dates to ensure valid planning.
* **Calendar Integration:** A custom internal calendar to track your planned meals.

### 4. Authentication & Sync

* **Multi-method Auth:** Sign up/Login via Email or **Google Sign-In** via Firebase.
* **Guest Mode:** Browse and search meals without an account.
* **Firebase Backup:** Synchronize local Room data to the cloud automatically.

---

## 📸 Screenshots

| Home | Meal Details | Search & Filter |
| --- | --- | --- |
| <img width="250" height="2992" alt="image" src="https://github.com/user-attachments/assets/25b9bfdc-2051-4c4a-9758-1013b6d6bebb" />| <img width="250" height="2992" alt="image" src="https://github.com/user-attachments/assets/08622b25-6fd4-41b1-beba-d05b5b9ccfeb" />| <img width="250" height="2992" alt="image" src="https://github.com/user-attachments/assets/a2adfb65-8f90-4036-aac1-db02b0966b51" />
| --- | <img width="250" height="2992" alt="image" src="https://github.com/user-attachments/assets/78166a90-fb66-45a6-b149-6e012ea378d5" />
 |

| Planner | Favorites | Login |
| --- | --- | --- |
| <img width="250" height="2992" alt="image" src="https://github.com/user-attachments/assets/25aad484-9685-4869-bee0-55f4806277b2" />| <img width="250" height="2992" alt="image" src="https://github.com/user-attachments/assets/b49ae9a0-8d3d-4281-a04b-6a5dfe3f182e" />| <img width="250" height="2992" alt="image" src="https://github.com/user-attachments/assets/c17b0816-0cc4-4249-a2ef-be6142e3a11c" />

 |

---

## 🛠️ Tech Stack & Libraries

* **Architecture:** MVP (Model View Presenter)
* **Asynchronous Logic:** RxJava3 & RxAndroid
* **Local Database:** Room Persistence
* **Remote Data:** Retrofit (TheMealDB API)
* **Image Loading:** Glide
* **Authentication:** Firebase Auth & Google Sign-In
* **UI Components:** - Lottie Files (Splash Animation)
* YouTube Player Core
* Navigation Component
* Material Design 3



---

## ⚙️ How to Run the Project

1. **Clone the repository:**
```bash
git clone https://github.com/yourusername/food-planner-app.git

```


2. **Setup Firebase:**
* Create a project in [Firebase Console](https://console.firebase.google.com/).
* Add an Android app with package name `com.example.foodplaner`.
* Place `google-services.json` in the `app/` folder.


3. **Open in Android Studio:**
* Wait for Gradle sync to complete.


4. **Build & Run:**
* Deploy to a physical device or emulator (API 26+ recommended).



---

## 📖 Usage Guide

* **Testing the Timer:** On the Home Screen, **long-press** the "Meal of the Day" card. This will clear the `SharedPreferences` and force a new random meal fetch immediately.
* **Planning:** Select a meal -> Click "Add to Calendar" -> Choose a date. Note that past dates are disabled.
* **Offline Mode:** Turn off the internet to see your "Favorites" and "Weekly Plan" load instantly from the local Room database.

---

## 📄 License

This project is developed by Mahmoud Tarek All rights reserved.
