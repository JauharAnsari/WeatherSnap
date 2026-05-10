# WeatherSnap 🌤️📸
**WeatherSnap** is a modern, fully-featured Android application built with Jetpack Compose that allows users to check real-time weather conditions for any city globally and capture "weather reports" containing camera evidence and field notes. All reports are saved locally for offline viewing.

## 📥 Download App
[Download APK](https://drive.google.com/file/d/1Iz1KyoydFV74xih7kgUdE5NuDT7Bjltx/view?usp=drivesdk)

## 🚀 Features
- **Live City Search:** Search for any city worldwide with instant, debounced suggestions powered by the Open-Meteo Geocoding API.
- **Real-time Weather Data:** View accurate current temperature, weather conditions, humidity, wind speed, and pressure.
- **Camera Evidence Capture:** Built-in custom CameraX integration allows users to snap photos of current weather conditions.
- **Smart Image Compression:** Automatically scales and compresses captured photos to save device storage while tracking original vs. compressed file sizes.
- **Local Offline Storage:** Save comprehensive weather reports (including photos and field notes) to a local SQLite database for later viewing.
- **Professional UI:** A clean, modern Light Theme built with Jetpack Compose and Material 3, optimized for readability and user experience.

## 🛠️ Technology Stack
This project follows modern Android development best practices:
- **Language:** Kotlin
- **UI Toolkit:** Jetpack Compose & Material 3
- **Architecture:** MVVM (Model-View-ViewModel) + StateFlow
- **Dependency Injection:** Dagger Hilt
- **Navigation:** Navigation Compose (Single-Activity Architecture)
- **Networking:** Retrofit 2 + OkHttp (with Logging Interceptor) + Gson
- **Local Persistence:** Room Database
- **Hardware Integration:** CameraX
- **Image Loading:** Coil
- **Asynchrony:** Kotlin Coroutines

## 🌍 APIs Used
WeatherSnap relies on the open-source and free **Open-Meteo** APIs (no API keys required):
1. **Geocoding API:** `https://geocoding-api.open-meteo.com/v1/search`
2. **Forecast API:** `https://api.open-meteo.com/v1/forecast`

## 📸 Screenshots & UI

The application features three primary screens:

### 1. Weather Screen
Search for cities and view current weather metrics.

| |
|:---:|
| <img src="https://github.com/user-attachments/assets/ec25e88c-7378-4394-b8b4-9c5f93917424" width="200"/> |

### 2. Create Report & Camera Screen
Capture a photo using the custom CameraX view and add personal field notes to the weather data.

| |
|:---:|
| <img src="https://github.com/user-attachments/assets/04b8a4fb-6d66-4ce5-90cc-845f9fc3e81e" width="200"/> |

### 3. Saved Reports Screen
Scroll through a persistent list of all historically saved weather reports, complete with image previews and compression metadata.

| |
|:---:|
| <img src="https://github.com/user-attachments/assets/68a84ac1-0227-4591-ab0a-14e97f6c4a5e" width="200"/> |

## ⚙️ How to Build and Run
1. Clone this repository: `git clone https://github.com/JauharAnsari/WeatherSnap.git`
2. Open the project in **Android Studio**.
3. Sync Gradle files (requires Internet connection to download dependencies).
4. Run the app on an Android Emulator or a physical device (API level 24+).

*Note: Make sure to grant Camera permissions when prompted to enable the Capture Photo functionality.*
