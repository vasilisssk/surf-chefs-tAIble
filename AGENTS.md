# Repository Guidelines

## Project Structure & Module Organization
This repo currently contains a single Android app module plus analysis and development notes.

- `app/src/main/` - Android source, manifests, resources, layouts, and navigation XML.
- `app/src/test/` - local JVM tests.
- `app/src/androidTest/` - instrumented Android tests.
- `01-analysis/` - requirements, ER model, API contract, and design notes.
- `02-development/` - implementation checklists and delivery notes.

Keep Android code organized by feature and layer. For example, prefer `ui/home`, `ui/bookings`, `data/...`, and `domain/...` over one large shared package.

## Build, Test, and Development Commands
Use the Gradle wrapper from the repository root:

- `./gradlew assembleDebug` - builds the Android debug APK.
- `./gradlew test` - runs local unit tests.
- `./gradlew connectedAndroidTest` - runs instrumented tests on a device or emulator.
- `./gradlew lint` - runs Android lint checks.

## Coding Style & Naming Conventions
Use Kotlin, 4-space indentation, and small classes. Do not place all logic in one file.

- Classes: `PascalCase` (`HomeViewModel`, `BookingRepository`).
- Functions and properties: `camelCase`.
- XML files: `snake_case` (`fragment_home.xml`, `activity_main.xml`).
- Keep one `entity`, `dto`, `ViewModel`, or `service` per file.
- Prefer XML layouts for UI in this project; extend existing layouts instead of replacing them wholesale.
- Use Koin for dependency injection and keep modules separated by feature.

## Testing Guidelines
Use JUnit for unit tests and Espresso for UI tests.

- Name unit tests after the class under test, e.g. `BookingViewModelTest`.
- Put pure logic tests in `app/src/test/`.
- Put UI and navigation tests in `app/src/androidTest/`.
- Cover booking rules, cancellation limits, review constraints, and empty/error states.

## Commit & Pull Request Guidelines
The Git history is minimal, so there is no strict legacy convention yet. Use short, imperative commit messages such as `Add booking validation`.

For pull requests:

- Describe the change and the affected screens or API areas.
- Link the relevant task or requirement when available.
- Include screenshots or screen recordings for UI changes.
- Mention test commands you ran and any known limitations.

## Security & Configuration Tips
Do not commit secrets, API keys, or local environment files. Keep backend URLs and build-time settings configurable, and avoid logging sensitive data such as passwords or tokens.
