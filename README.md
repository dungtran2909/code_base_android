# 🚀 CodeBase Android - Modern Android Jetpack Compose Codebase createdBy Dũng Trần

**Source** is a robust Android starter kit and real-time messaging application. Built with **Jetpack Compose**, **Firebase**, and **Clean Architecture**, this codebase serves as a scalable foundation for modern Android development. It features a sophisticated chat system, secure authentication, and a type-safe navigation structure.

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.04-green.svg)
![Firebase](https://img.shields.io/badge/Firebase-Auth%20%26%20Firestore-orange.svg)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-red.svg)

---

## 🛠 Tech Stack & Tools

- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3) - 100% declarative UI.
- **Architecture:** Clean Architecture + MVVM (Model-View-ViewModel).
- **Dependency Injection:** [Hilt](https://dagger.dev/hilt/) - For decoupled and testable code.
- **Navigation:** [Type-safe Navigation Compose](https://developer.android.com/guide/navigation/design/type-safety) (v2.8.0+) using Kotlin Serialization.
- **Backend/Database:**
    - **Firebase Authentication:** Email/Password & Google Sign-In (via Credential Manager).
    - **Cloud Firestore:** For real-time chat rooms and message synchronization.
- **Local Storage:** [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Handles Auth Tokens, User Credentials, and App Preferences.
- **Asynchronous:** Kotlin Coroutines & Flow (Reactive Data Streams).
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/) - Optimized image loading for avatars and media.

---

## ✨ Key Features

### 📨 Advanced Real-time Messaging
- **Reactive Streams:** Uses Firestore `SnapshotListener` to receive messages instantly.
- **Smart Message Grouping:** Messages are automatically grouped by sender and timestamp (same minute) to optimize UI layout.
- **Dynamic Bubble Shapes:** Custom `RoundedCornerShape` logic (FIRST, MIDDLE, LAST, SINGLE positions) that adapts based on message grouping—similar to Messenger/Telegram.
- **Interactive UI:**
    - **Auto-scroll:** Automatically scrolls to the latest message.
    - **Smart "Scroll to Bottom" Button:** Appears only when the user scrolls up, allowing a quick jump back to the latest chat.
    - **Time Headers:** Contextual date/time headers (e.g., `18:00 12 OCT`) appear when there is a significant gap (30+ mins) between messages.

### 🔐 Authentication & Profile
- Secure login and registration flow.
- Seamless Google Sign-In integration.
- Profile management with customizable avatars and display names.

### 🗺️ Navigation & UX
- **Nested Navigation:** Dual `NavHost` setup. A `rootNavController` for full-screen transitions (Auth, Private Chat) and a `childNavController` for internal tab switching (Home, Chat, Profile).
- **Custom Transitions:** Smooth Slide-in/Slide-out animations for a native feel.
- **Material 3 Theming:** Consistent design system with custom typography and color schemes.

---

## 📂 Project Structure
```
app/src/main/java/com/dungtran/codebase/
├── data/                       # Data Layer: Implementation of repositories & data sources
│   ├── local/                  # Local persistence (DataStore, Database)
│   │   └── datastore/          # Auth tokens, preferences & session management
│   ├── remote/                 # Remote data sources (Firebase, API services)
│   │   └── firebase/           # Firestore & Firebase Auth implementation
│   ├── repository/             # Repository implementations (Logic for Data fetching)
│   └── model/                  # Data Transfer Objects (DTOs) & Entity Mappers
│
├── domain/                     # Domain Layer: Pure business logic (Platform independent)
│   ├── model/                  # Domain Entities (User, Message, ChatRoom)
│   ├── repository/             # Repository interfaces (Contracts for Data layer)
│   └── usecase/                # Application specific business rules
│
├── ui/                         # Presentation Layer: Jetpack Compose UI
│   ├── features/               # Screen-based modules (Feature-by-package)
│   │   ├── auth/               # Login, Register, Forgot Password
│   │   ├── main/               # Main Container & Bottom Navigation tabs
│   │   └── chat/               # Chat List & Private Chat (Real-time messaging)
│   ├── components/             # Reusable UI components (Buttons, Avatars, Inputs)
│   ├── navigation/             # Type-safe Navigation (Routes & NavHosts)
│   └── theme/                  # Design System (Color, Typography, Shape, Theme)
│
├── di/                         # Dependency Injection: Hilt Modules
│   ├── AppModule.kt            # Core application dependencies
│   ├── RepositoryModule.kt     # Repository bindings
│   └── FirebaseModule.kt       # Firebase service providers
│
└── utils/                      # Helper classes, Extensions & Constants
```

---

## 🚀 Getting Started

1. **Clone the repository:**

2. **Setup Firebase: (This is a demo)**
    - Create a project on the [Firebase Console](https://console.firebase.google.com/).
    - Add an Android App with package name `com.dungtran.codebase`.
    - Download `google-services.json` and place it in the `app/` directory.
    - Enable **Authentication** (Email & Google) and **Cloud Firestore**.
3. **Firestore Security Rules:**
   Ensure your Firestore rules allow authenticated users to read/write to the `chat_rooms` and `messages` collections.
4. **Build:** Open in Android Studio (Ladybug or newer) and sync Gradle.

---

## 🤝 Contributing
Contributions are welcome! Feel free to open issues or submit pull requests to improve the codebase.

## 👤 Author
**Dung Tran**
- GitHub: [@dungtran2909](https://github.com/dungtran2909)

---
*If you find this codebase helpful, please give it a ⭐!*