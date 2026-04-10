# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./gradlew assembleDebug        # Build debug APK
./gradlew assembleRelease      # Build release APK
./gradlew installDebug         # Build and install on connected device
./gradlew test                 # Run unit tests
./gradlew connectedAndroidTest # Run instrumented tests (requires device/emulator)
./gradlew lint                 # Run Android Lint
./gradlew clean                # Clean build outputs
```

Single test class: `./gradlew test --tests "com.kaushal.japacountercompose.ExampleUnitTest"`

## About the App

**Japa Counter** is a lightweight Android app that helps users track and manage their Japa counts efficiently. 
Users can create multiple Japas by specifying details such as the Japa name and target count. Users can easily 
increase or decrease the Japa count as they progress and update the count whenever needed.
The app enables users to mark a Japa as completed upon reaching the target, or remove it when no longer needed. 
The application supports tracking multiple Japas at the same time and stores all data locally on the device, 
providing a simple, private, and reliable way for users to maintain consistency in their spiritual practice.


## Architecture

Single-module Android app (`app`) using **MVVM + Repository + Hilt DI**, fully offline with Room as the only data store.

**Data flow:**
```
Composable → ViewModel → MainRepository (interface) → RoomDBDao → Room SQLite
                ↑                ↑
           Hilt injects    Hilt injects (ViewModelComponent scope)
```

**Key layers:**

- `data/` — Domain models, Room entity, DAO, `Outcome` sealed class, Hilt modules
- `ui/composables/` — Screen composables (Welcome, JapaList, AddNewJapa, JapaDetails)
- `ui/viewmodels/` — `JapaListViewModel` and `AddNewJapaViewModel`
- `ui/theme/` — Material3 theme

**Patterns in use:**

- `Outcome<T>` sealed class (`InProgress`, `Success`, `Failure`) is the standard type for async results across ViewModels and repository.
- Two model types: `JapaInfoDBEntity` (Room `@Entity`) and `JapaInfoEntities` (domain model), bridged by a `toJapaInfoEntities()` extension.
- ViewModels expose `StateFlow` (list state) or `SharedFlow` (one-shot events like save result) — collected in composables via `collectAsState()` / `collectAsStateWithLifecycle()`.
- Navigation uses a string-route enum `JapaAppScreens` with four destinations: `welcome`, `japaList`, `addJapa`, `japDetails`.
- Hilt: `@HiltAndroidApp` on `ApplicationClass`, `@AndroidEntryPoint` on `MainActivity`, `@HiltViewModel` on ViewModels. `RepositoryModule` uses `@Binds` to bind `MainRepositoryImpl` to `MainRepository`.
- Room + Moshi instances are both provided as Hilt `@Singleton`s in `AppModule`.
- Annotation processing uses **KSP** (not kapt) for Room, Hilt, and Moshi codegen.


## Guidelines

- Always avoid using deprecated methods and API's and use the most recent and officially documented ones.
- Do not create large methods which makes methods complex & confusing to other devs, instead break it down to 
  simple small functions which helps in better readability & understanding.
- Always use constants instead of hardcoded strings.
- Prioritize on reusable components or code.
- Follow SOLID principle pattern with clear separation of concern.
- Provide one-liner sentence for each & every function.
- Create unit tests for all business logics.


## Tech Stack Reference

| Area | Library | Version |
|---|---|---|
| Language | Kotlin | 2.0.21 |
| UI | Jetpack Compose BOM | 2025.05.00 |
| UI | Material3 | via BOM |
| Navigation | Navigation Compose | 2.7.4 |
| DI | Dagger Hilt | 2.56.2 |
| Database | Room | 2.7.1 |
| Serialization | Moshi | 1.15.2 |
| Min/Target SDK | — | 26 / 35 |
