# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**App Name:** JotList
**Description:** A minimalistic Android shopping list app with multiple lists, check/uncheck items, drag-and-drop reordering, and word suggestions based on previous entries.
**Target Device:** Poco X7 Pro (Android 15)
**Storage:** Offline-only, local Room database (no cloud/accounts)

## Project Status

JotList is an Android shopping list app. **Phase 2 (Data Layer) is complete.**

**Package name:** `com.jotlist.app`

### Completed Phases
- [x] Phase 1: Project Setup - build config, Hilt, theme, navigation scaffold
- [x] Phase 2: Data Layer - Room entities, DAOs, database, repositories, Hilt modules

## Build Commands

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK
./gradlew test                   # Unit tests
./gradlew connectedAndroidTest   # Instrumented tests
```

**Note:** ktlint and detekt are mentioned in TechStack.md but are NOT configured in this project.

## Architecture

MVVM + Clean Architecture with three layers:
- **UI Layer** - Jetpack Compose screens + ViewModels (state holders)
- **Domain Layer** - Use cases (business logic) + repository interfaces
- **Data Layer** - Repository implementations + Room database

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, Hilt, Room, Coroutines + Flow

**Database Entities:** ShoppingListEntity, ListItemEntity, SuggestionEntity

## Project Structure Notes

- **Source directory:** `app/src/main/kotlin/` (NOT `java/`)
- **Room schema exports:** Configured to `$projectDir/schemas` via KSP arg in build.gradle.kts
- Domain models include mapper extension functions (`toDomain()`, `toEntity()`)

## Critical Rules

- **DO NOT** use XML layouts - use Jetpack Compose exclusively
- **DO NOT** add cloud sync, accounts, or network features - app is offline-only
- **ALWAYS** apply title case formatting when saving items
- **ALWAYS** show confirmation dialog before deleting a list
- **ALWAYS** use Hilt for dependency injection - no manual instantiation
- **ALWAYS** read the design doc for colors, spacing, and component specs before building UI
- Items are deleted by **long press**, not swipe
- Checked items move to **bottom** of list with strikethrough
- Unchecked items return to their **exact original position**
- Suggestions appear after **3 characters**, showing **max 3 results** ordered by **frequency**

## Key Patterns

- **StateFlow** for UI state in ViewModels (`MutableStateFlow` + `asStateFlow()`)
- **Flow** for reactive data streams from Room DAOs
- **Title Case formatting** via `TitleCaseConverter.convert()` utility
- Use `collectAsStateWithLifecycle()` in Composables
- Use `viewModelScope` for ViewModel coroutines
- **Entity/Domain mapping:** Use `entity.toDomain()` and `model.toEntity()` extension functions
- **Repository pattern:** Interfaces in `domain/repository/`, implementations in `data/repository/`

## File Naming Conventions

| Type | Pattern | Example |
|------|---------|---------|
| Screen | `[Name]Screen.kt` | `HomeScreen.kt` |
| ViewModel | `[Name]ViewModel.kt` | `HomeViewModel.kt` |
| Composable | `[Name].kt` | `ListItemRow.kt` |
| Entity | `[Name]Entity.kt` | `ShoppingListEntity.kt` |
| DAO | `[Name]Dao.kt` | `ShoppingListDao.kt` |
| Repository Interface | `[Name]Repository.kt` | `ShoppingListRepository.kt` |
| Repository Impl | `[Name]RepositoryImpl.kt` | `ShoppingListRepositoryImpl.kt` |
| UseCase | `[Action][Subject]UseCase.kt` | `CreateListUseCase.kt` |
| DI Module | `[Name]Module.kt` | `DatabaseModule.kt` |

## Implementation Order

1. **Project Setup** - Create project, configure build.gradle.kts, version catalog, package structure, Hilt
2. **Data Layer** - Room entities, DAOs, database class, Hilt modules, repository interfaces & implementations
3. **Domain Layer** - Domain models, use cases for lists/items/suggestions
4. **UI Foundation** - Theme (colors, typography, shapes), navigation graph, Screen sealed class, MainActivity
5. **Home Screen** - HomeViewModel, HomeScreen, ListCard, EmptyState, FAB
6. **List Detail Screen** - ListDetailViewModel, ListDetailScreen, ListItemRow, CheckboxItem, input field
7. **Core Features** - Add item, title case formatting, check/uncheck with position tracking, drag & drop reorder, long-press delete
8. **Suggestions** - Suggestion storage, SuggestionDropdown, matching logic, input field integration
9. **Dialogs** - ConfirmationDialog, delete list confirmation, EditDialog, rename list, edit item
10. **Polish** - Animations, empty states, device testing, edge case fixes

## Technical Requirements

- Min SDK: API 29 (Android 10)
- Target SDK: API 35 (Android 15)
- JDK 17+ required
- Gradle 8.9 with Kotlin DSL

## Build Environment

**IMPORTANT:** This project requires a full JDK (not JRE) with `jlink` available. The system Java may be JRE-only.

The project is configured to use Android Studio's bundled JBR via `gradle.properties`:
```properties
org.gradle.java.home=/opt/android-studio/jbr
```

If builds fail with jlink errors or strange version numbers as error messages, ensure:
1. Android Studio is installed at `/opt/android-studio/`
2. The JBR path is correct in `gradle.properties`
3. Alternatively, set `JAVA_HOME` to a full JDK installation before running Gradle

## Available MCP Tools

- **Context7 MCP** - Look up latest library documentation for Compose, Room, Hilt
- **GitHub MCP** - DO NOT use GitHub MCP for commits/pushes - it consumes too many tokens.
Instead, provide git commands for the user to run manually:
```bash
git add .
git commit -m "feat: description"
git push origin main
```
Use conventional commits: `feat:`, `fix:`, `refactor:`, `docs:`, `test:`
- **TestSprite MCP** - Generate unit tests for ViewModels and Use Cases

## Documentation

- [ProductRequirementsDoc.md](ProductRequirementsDoc.md) - Requirements and user stories
- [DesignDoc.md](DesignDoc.md) - UI/UX specifications and color system
- [TechStack.md](TechStack.md) - Full architecture, dependencies, and implementation guidelines
