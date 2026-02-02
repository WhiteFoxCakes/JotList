# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**App Name:** JotList
**Description:** A minimalistic Android shopping list app with multiple lists, check/uncheck items, drag-and-drop reordering, and word suggestions based on previous entries.
**Target Device:** Poco X7 Pro (Android 15)
**Storage:** Offline-only, local Room database (no cloud/accounts)

## Project Status

JotList is an Android shopping list app. **Phase 6 (List Detail Screen) is complete.**

**Package name:** `com.jotlist.app`

### Completed Phases
- [x] Phase 1: Project Setup - build config, Hilt, theme, navigation scaffold
- [x] Phase 2: Data Layer - Room entities, DAOs, database, repositories, Hilt modules
- [x] Phase 3: Domain Layer - Domain models, use cases, utilities (TitleCaseConverter, DateFormatter)
- [x] Phase 4: UI Foundation - Theme (colors, typography, shapes), navigation graph, Screen sealed class, MainActivity
- [x] Phase 5: Home Screen - HomeViewModel, HomeScreen, ListCard, EmptyState, ConfirmationDialog, item count queries
- [x] Phase 6: List Detail Screen - ListDetailViewModel, ListDetailScreen, item management (add/edit/delete/check), suggestions

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

### Domain Layer Components

**Utilities** (`util/`):
- `TitleCaseConverter` - Object with `convert()` method for title case formatting
- `DateFormatter` - Injectable class with `formatDate()` method for list name generation

**List Use Cases** (`domain/usecase/list/`):
- `GetAllListsUseCase` - Returns `Flow<List<ShoppingList>>`
- `GetAllListsWithCountUseCase` - Returns `Flow<List<ShoppingListWithCount>>` with item counts
- `GetListByIdUseCase` - Returns `Flow<ShoppingList?>` by ID
- `CreateListUseCase` - Creates list, auto-generates name "List (Feb 1, 2026)" if blank
- `UpdateListUseCase` - Updates list, auto-updates `updatedAt` timestamp
- `DeleteListUseCase` - Deletes list by ID (items cascade deleted)

**Item Use Cases** (`domain/usecase/item/`):
- `GetItemsByListIdUseCase` - Returns `Flow<List<ListItem>>` for a specific list
- `AddItemUseCase` - Adds item with title case + adds to suggestions
- `UpdateItemUseCase` - Updates item text with title case (2 overloads)
- `DeleteItemUseCase` - Deletes item by ID
- `CheckItemUseCase` - Checks item, stores `originalPosition`
- `UncheckItemUseCase` - Unchecks item, restores to `originalPosition`
- `ReorderItemsUseCase` - Updates positions after drag-and-drop

**Suggestion Use Cases** (`domain/usecase/suggestion/`):
- `GetSuggestionsUseCase` - Returns suggestions (min 3 chars, max 3 results)
- `AddSuggestionUseCase` - Adds/increments suggestion with title case

### UI Layer Components

**Theme** (`ui/theme/`):
- `Color.kt` - Design system colors (DarkBackground, SurfaceCard, ElectricBlue, NeonGreen, etc.)
- `Type.kt` - Typography scale (H1-H3, Body1-2, Caption, Button styles)
- `Shape.kt` - Corner radii (8dp/16dp/24dp) + `Spacing` object (xs/sm/md/lg/xl) + `ScreenMargin`
- `Theme.kt` - `JotListTheme` composable with Material 3 dark color scheme

**Components** (`ui/components/`):
- `ConfirmationDialog.kt` - Reusable dialog for destructive actions (delete confirmation)
- `EmptyState.kt` - Centered empty state with icon and message
- `ListCard.kt` - Card component for displaying list summary with item count
- `EditItemDialog.kt` - Dialog for editing item text with TextField
- `ListItemRow.kt` - Item display with checkbox (custom circle border for unchecked, CheckCircle for checked)
- `InputFieldWithSuggestions.kt` - Input field with animated dropdown for suggestions
- `ListDetailTopBar.kt` - Top bar with back button and overflow menu

**Screens** (`ui/screens/`):
- `home/HomeViewModel.kt` - State management for home screen, navigation events via Channel
- `home/HomeScreen.kt` - 2-column staggered grid of lists, FAB for new list creation
- `listdetail/ListDetailViewModel.kt` - State management for list detail, 11 use case dependencies, debounced suggestions
- `listdetail/ListDetailScreen.kt` - Item management UI with input field, suggestions, and dialogs

**Navigation** (`ui/navigation/`):
- `Screen.kt` - Sealed class with `Home` and `ListDetail` routes
- `NavGraph.kt` - Navigation graph with both HomeScreen and ListDetailScreen fully wired

**Activity**:
- `MainActivity.kt` - Single activity with `@AndroidEntryPoint`, edge-to-edge enabled, hosts `NavGraph`

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
- **Channel + Flow** for single-shot navigation events (prevents re-navigation on config changes)
- **Title Case formatting** via `TitleCaseConverter.convert()` utility
- Use `collectAsStateWithLifecycle()` in Composables (NEVER `collectAsState()`)
- Use `viewModelScope` for ViewModel coroutines
- **Entity/Domain mapping:** Use `entity.toDomain()` and `model.toEntity()` extension functions
- **Repository pattern:** Interfaces in `domain/repository/`, implementations in `data/repository/`
- **Use Case pattern:** All use cases use `@Inject constructor` and `operator fun invoke()` for single-responsibility operations
- **DAO queries with JOIN:** Use `@Embedded` for combined data (e.g., `ShoppingListWithItemCount`)

## Common Pitfalls & Lessons Learned

### Material Icons
- **ALWAYS verify icon existence** before using - not all Material Icons exist in `Icons.Rounded.*`
- Many intuitive icon names do NOT exist in the Material Icons library
- **Icons that DO NOT exist:**
  - ❌ `Icons.Rounded.ShoppingBag`
  - ❌ `Icons.Rounded.RadioButtonUnchecked`
  - ❌ `Icons.Rounded.Circle`
  - ❌ `Icons.Outlined.Circle`
  - ❌ `Icons.Rounded.PanoramaFishEye`
- **Working alternatives:**
  - ✅ `Icons.Rounded.ShoppingCart` - Use for shopping/list icons
  - ✅ `Icons.Rounded.CheckCircle` - Use for checked state
  - ✅ Custom circle border (Box + BorderStroke + CircleShape) - Use for unchecked radio/checkbox
- **Solution:** When a specific icon doesn't exist, use Compose drawing primitives (Box, Canvas, etc.) to create custom icons
- Check Material Icons documentation or use autocomplete to verify icon availability before committing to a design

### Navigation Events
- Use `Channel<NavigationEvent>` + `receiveAsFlow()` for one-shot events
- Prevents navigation being triggered multiple times on configuration changes
- Collect in `LaunchedEffect(Unit)` in the Composable

### Room Queries with Aggregates
- Use `LEFT JOIN` when counting related items (returns 0 if no items exist)
- Use `@Embedded` to combine entity with aggregate data in result class
- Place data class outside interface (before `@Dao`) in same file

### Compose Animations
- `slideVertically()` animation may not always be available in imports
- When animation imports fail, simplify to `fadeIn()`/`fadeOut()` only
- Complex enter/exit animations are optional - focus on functionality first

### PaddingValues Parameter Syntax
- Prefer explicit parameters (`start`, `end`, `top`, `bottom`) over convenience parameters
- Avoid `horizontal` parameter in PaddingValues - use `start` and `end` instead
- Example: `PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp)`

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
| Utility | `[Name].kt` | `TitleCaseConverter.kt` |
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
