# Japa Counter - Jetpack Compose

Japa Counter is a lightweight, modern Android application built with Jetpack Compose designed to help users track and manage their Japa (chanting) counts efficiently. It provides a simple, private, and reliable way to maintain consistency in spiritual practices by storing all data locally on the device.

## Features

- **Multiple Japa Tracking**: Create and manage multiple Japa counters simultaneously.
- **Customizable Targets**: Set specific target counts for each Japa.
- **Easy Counter Management**: Increment or decrement counts with a single tap as you progress.
- **Status Tracking**: Mark Japas as completed upon reaching the target or remove them when no longer needed.
- **Progress Visualization**: View progress bars and percentages for each active Japa.
- **Local Storage**: All data is stored locally using Room database, ensuring user privacy and offline availability.

## Architecture

The project follows the **MVVM (Model-View-ViewModel)** architectural pattern combined with a **Repository** layer to ensure a clean separation of concerns and maintainability.

### Data Flow
`Composable (UI) → ViewModel → Repository (Interface) → Room DAO → SQLite Database`

### Key Components
- **UI Layer**: Built entirely with **Jetpack Compose** using Material3 components.
- **ViewModel Layer**: Manages UI state and communicates with the repository using `StateFlow` and `SharedFlow`.
- **Repository Layer**: Acts as a single source of truth for data, abstracting the data source from the ViewModels.
- **Data Layer**: Utilizes **Room Persistence Library** for local data storage. It uses an `Outcome` sealed class to handle different states of data operations (InProgress, Success, Failure).

## Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/) (2.0.21)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (BOM 2025.05.00)
- **Dependency Injection**: [Dagger Hilt](https://dagger.dev/hilt/) (2.56.2)
- **Database**: [Room](https://developer.android.com/training/data-storage/room) (2.7.1)
- **Navigation**: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) (2.7.4)
- **Architecture Components**: ViewModel, StateFlow, SharedFlow
- **Image Loading**: Painter Resource / Image
- **Annotation Processing**: [KSP (Kotlin Symbol Processing)](https://kotlinlang.org/docs/ksp-overview.html)

## Project Structure

```text
com.kaushal.japacountercompose/
├── data/
│   ├── db/            # Room Database and DAO
│   ├── di/            # Hilt Dependency Injection modules
│   ├── repository/    # Repository interfaces and implementations
│   ├── Entities.kt    # Domain and DB models
│   └── Outcome.kt     # Sealed class for state handling
├── ui/
│   ├── composables/   # Jetpack Compose UI components and screens
│   ├── theme/         # Material3 theme definitions (Color, Type, Theme)
│   ├── viewmodels/    # ViewModels for UI state management
│   └── JapaApp.kt     # Navigation host and app entry point
└── ApplicationClass.kt # Hilt Application class
```

## Getting Started

### Prerequisites
- Android Studio Ladybug | 2024.2.1 or newer
- JDK 17+
- Android SDK 26+

### Build & Run
1. Clone the repository.
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an emulator or a physical device.

```bash
./gradlew assembleDebug
./gradlew installDebug
```

## License
This project is for educational purposes.
