# Tech Stack Document

## JotList - Shopping List App

**Document Version:** 1.0  
**Created:** February 2026  
**Status:** Final  
**Related Documents:** JotList PRD v1.0, JotList Design Document

---

## Table of Contents

1. [Overview](#1-overview)
2. [Development Environment](#2-development-environment)
3. [Core Technology Stack](#3-core-technology-stack)
4. [Architecture](#4-architecture)
5. [Project Structure](#5-project-structure)
6. [Dependencies](#6-dependencies)
7. [Database Schema](#7-database-schema)
8. [Coding Standards](#8-coding-standards)
9. [Testing Strategy](#9-testing-strategy)
10. [Build Configuration](#10-build-configuration)
11. [Implementation Guidelines](#11-implementation-guidelines)

---

## 1. Overview

### 1.1 Purpose

This document defines the technical architecture, tools, libraries, and development guidelines for the JotList Android application. It serves as the authoritative technical reference for development.

### 1.2 App Summary

JotList is a minimalistic, offline-first shopping list app built natively for Android. It allows users to create multiple lists, add/edit/delete items, check off items, reorder via drag-and-drop, and receive word suggestions based on previous entries.

### 1.3 Technical Goals

- Clean, maintainable codebase following modern Android best practices
- Responsive UI with smooth animations
- Reliable local data persistence
- Minimal app size and resource usage
- Support for Android 10 (API 29) through Android 15 (API 35)

---

## 2. Development Environment

### 2.1 Required Tools

| Tool | Version | Purpose |
|------|---------|---------|
| Android Studio | Latest stable (Ladybug or newer) | Primary IDE |
| JDK | 17 | Java Development Kit |
| Kotlin | 1.9.x or 2.0.x | Programming language |
| Gradle | 8.x | Build system |
| Android SDK | API 29-35 | Target platforms |

### 2.2 SDK Configuration

| Setting | Value |
|---------|-------|
| `minSdk` | 29 (Android 10) |
| `targetSdk` | 35 (Android 15) |
| `compileSdk` | 35 |

### 2.3 Device Testing

**Primary Test Device:** Poco X7 Pro (Android 15, build AP3A.240905.A2)

**Recommended Emulator Configurations:**
- Pixel 6 - API 35 (Android 15)
- Pixel 4 - API 29 (Android 10) - for minimum SDK testing
- Medium Phone - API 33 (Android 13) - for mid-range testing

---

## 3. Core Technology Stack

### 3.1 Stack Overview

| Layer | Technology |
|-------|------------|
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Design System | Material 3 (Material You) |
| Architecture | MVVM + Clean Architecture |
| Dependency Injection | Hilt |
| Async Operations | Kotlin Coroutines + Flow |
| Navigation | Jetpack Navigation Compose |
| Local Database | Room |
| Build System | Gradle with Kotlin DSL |

### 3.2 Technology Justifications

#### Kotlin
- Official recommended language for Android
- Null safety reduces crashes
- Coroutines for clean async code
- Concise syntax reduces boilerplate

#### Jetpack Compose
- Modern declarative UI toolkit
- Less code than XML layouts
- Built-in animation support
- Seamless state management
- Native drag-and-drop support

#### Material 3
- Latest Material Design system
- Modern, clean aesthetic
- Built-in theming support
- Consistent with Android 12+ design language

#### MVVM + Clean Architecture
- Clear separation of concerns
- Testable business logic
- Scalable structure
- Industry standard pattern

#### Hilt
- Simplifies dependency injection
- Integrates with Jetpack components
- Compile-time verification
- Reduces boilerplate

#### Room
- Abstraction over SQLite
- Type-safe queries
- Compile-time verification
- Flow support for reactive updates
- Built-in migration support

---

## 4. Architecture

### 4.1 Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        UI LAYER                                  │
│  ┌─────────────────┐    ┌─────────────────┐                     │
│  │    Screens      │    │   ViewModels    │                     │
│  │  (Composables)  │◄───│  (State Holders)│                     │
│  └─────────────────┘    └────────┬────────┘                     │
│                                  │                               │
├──────────────────────────────────┼───────────────────────────────┤
│                        DOMAIN LAYER                              │
│                      ┌───────────┴───────────┐                   │
│                      │      Use Cases        │                   │
│                      │    (Business Logic)   │                   │
│                      └───────────┬───────────┘                   │
│                                  │                               │
├──────────────────────────────────┼───────────────────────────────┤
│                        DATA LAYER                                │
│  ┌─────────────────┐    ┌────────┴────────┐    ┌──────────────┐ │
│  │   Repository    │◄───│   Repository    │───►│    Room      │ │
│  │   Interfaces    │    │ Implementations │    │   Database   │ │
│  └─────────────────┘    └─────────────────┘    └──────────────┘ │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.2 Layer Responsibilities

#### UI Layer
- **Screens (Composables):** Render UI based on state, handle user interactions
- **ViewModels:** Hold UI state, process user actions, communicate with domain layer

#### Domain Layer
- **Use Cases:** Encapsulate single business operations
- **Models:** Domain entities (can be same as data entities for this simple app)

#### Data Layer
- **Repositories:** Single source of truth for data, abstract data sources
- **Room Database:** Local persistence, DAOs for data access
- **Entities:** Database table representations

### 4.3 Data Flow

```
User Action → Composable → ViewModel → Use Case → Repository → Room DAO → Database
                                                                    ↓
UI Update ← Composable ← ViewModel ← Use Case ← Repository ← Flow/StateFlow
```

---

## 5. Project Structure

### 5.1 Package Structure

```
com.jotlist.app/
├── JotListApplication.kt          # Application class with Hilt
├── MainActivity.kt                 # Single activity entry point
│
├── data/
│   ├── local/
│   │   ├── database/
│   │   │   ├── JotListDatabase.kt      # Room database class
│   │   │   ├── dao/
│   │   │   │   ├── ShoppingListDao.kt
│   │   │   │   ├── ListItemDao.kt
│   │   │   │   └── SuggestionDao.kt
│   │   │   └── entity/
│   │   │       ├── ShoppingListEntity.kt
│   │   │       ├── ListItemEntity.kt
│   │   │       └── SuggestionEntity.kt
│   │   └── converter/
│   │       └── Converters.kt           # Type converters if needed
│   └── repository/
│       ├── ShoppingListRepositoryImpl.kt
│       ├── ListItemRepositoryImpl.kt
│       └── SuggestionRepositoryImpl.kt
│
├── domain/
│   ├── model/
│   │   ├── ShoppingList.kt
│   │   ├── ListItem.kt
│   │   └── Suggestion.kt
│   ├── repository/
│   │   ├── ShoppingListRepository.kt   # Interface
│   │   ├── ListItemRepository.kt       # Interface
│   │   └── SuggestionRepository.kt     # Interface
│   └── usecase/
│       ├── list/
│       │   ├── CreateListUseCase.kt
│       │   ├── DeleteListUseCase.kt
│       │   ├── GetAllListsUseCase.kt
│       │   ├── GetListByIdUseCase.kt
│       │   └── UpdateListUseCase.kt
│       ├── item/
│       │   ├── AddItemUseCase.kt
│       │   ├── DeleteItemUseCase.kt
│       │   ├── UpdateItemUseCase.kt
│       │   ├── CheckItemUseCase.kt
│       │   ├── UncheckItemUseCase.kt
│       │   └── ReorderItemsUseCase.kt
│       └── suggestion/
│           ├── GetSuggestionsUseCase.kt
│           └── AddSuggestionUseCase.kt
│
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Type.kt
│   │   ├── Shape.kt
│   │   └── Theme.kt
│   ├── components/
│   │   ├── ListCard.kt
│   │   ├── ListItemRow.kt
│   │   ├── CheckboxItem.kt
│   │   ├── DragHandle.kt
│   │   ├── InputFieldWithSuggestions.kt
│   │   ├── SuggestionDropdown.kt
│   │   ├── EmptyState.kt
│   │   ├── ConfirmationDialog.kt
│   │   └── EditDialog.kt
│   ├── screens/
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   └── HomeViewModel.kt
│   │   └── listdetail/
│   │       ├── ListDetailScreen.kt
│   │       └── ListDetailViewModel.kt
│   └── navigation/
│       ├── NavGraph.kt
│       └── Screen.kt                   # Sealed class for routes
│
├── di/
│   ├── AppModule.kt                    # Application-wide dependencies
│   ├── DatabaseModule.kt               # Room database providers
│   └── RepositoryModule.kt             # Repository bindings
│
└── util/
    ├── TitleCaseConverter.kt           # Title case formatting utility
    ├── DateFormatter.kt                # Date formatting for list names
    └── Extensions.kt                   # Kotlin extension functions
```

### 5.2 Resource Structure

```
res/
├── values/
│   ├── strings.xml
│   ├── colors.xml          # Base colors (Compose theme is primary)
│   └── themes.xml          # App theme for splash/system UI
├── drawable/
│   └── ic_launcher_foreground.xml
├── mipmap-*/
│   └── ic_launcher.webp
└── xml/
    └── backup_rules.xml
```

---

## 6. Dependencies

### 6.1 Version Catalog (libs.versions.toml)

```toml
[versions]
# Core
kotlin = "1.9.22"
agp = "8.2.2"
ksp = "1.9.22-1.0.17"

# AndroidX Core
core-ktx = "1.12.0"
lifecycle = "2.7.0"
activity-compose = "1.8.2"

# Compose
compose-bom = "2024.02.00"
compose-compiler = "1.5.10"

# Navigation
navigation-compose = "2.7.7"

# Room
room = "2.6.1"

# Hilt
hilt = "2.50"
hilt-navigation-compose = "1.2.0"

# Coroutines
coroutines = "1.8.0"

# Testing
junit = "4.13.2"
junit-ext = "1.1.5"
espresso = "3.5.1"
mockk = "1.13.9"
turbine = "1.0.0"

# Code Quality
ktlint = "12.1.0"
detekt = "1.23.5"

[libraries]
# Core
core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "core-ktx" }

# Lifecycle
lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycle" }
lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycle" }

# Compose
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }
compose-foundation = { group = "androidx.compose.foundation", name = "foundation" }
compose-animation = { group = "androidx.compose.animation", name = "animation" }
activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activity-compose" }

# Navigation
navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation-compose" }

# Room
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hilt-navigation-compose" }

# Coroutines
coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "coroutines" }
coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }

# Testing
junit = { group = "junit", name = "junit", version.ref = "junit" }
junit-ext = { group = "androidx.test.ext", name = "junit", version.ref = "junit-ext" }
espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espresso" }
compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
mockk = { group = "io.mockk", name = "mockk", version.ref = "mockk" }
coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutines" }
turbine = { group = "app.cash.turbine", name = "turbine", version.ref = "turbine" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ktlint = { id = "org.jlleitschuh.gradle.ktlint", version.ref = "ktlint" }
detekt = { id = "io.gitlab.arturbosch.detekt", version.ref = "detekt" }

[bundles]
compose = [
    "compose-ui",
    "compose-ui-graphics",
    "compose-ui-tooling-preview",
    "compose-material3",
    "compose-foundation",
    "compose-animation"
]
lifecycle = [
    "lifecycle-runtime-ktx",
    "lifecycle-viewmodel-compose",
    "lifecycle-runtime-compose"
]
room = [
    "room-runtime",
    "room-ktx"
]
coroutines = [
    "coroutines-core",
    "coroutines-android"
]
testing = [
    "junit",
    "mockk",
    "coroutines-test",
    "turbine"
]
android-testing = [
    "junit-ext",
    "espresso-core",
    "compose-ui-test-junit4"
]
```

### 6.2 App-level build.gradle.kts

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.jotlist.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jotlist.app"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core
    implementation(libs.core.ktx)
    
    // Lifecycle
    implementation(libs.bundles.lifecycle)
    
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.activity.compose)
    debugImplementation(libs.compose.ui.tooling)
    
    // Navigation
    implementation(libs.navigation.compose)
    
    // Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    
    // Coroutines
    implementation(libs.bundles.coroutines)
    
    // Unit Testing
    testImplementation(libs.bundles.testing)
    
    // Android/UI Testing
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.android.testing)
    debugImplementation(libs.compose.ui.test.manifest)
}

ktlint {
    android = true
    ignoreFailures = false
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}

detekt {
    config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}
```

---

## 7. Database Schema

### 7.1 Entity Definitions

#### ShoppingListEntity

```kotlin
@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
```

#### ListItemEntity

```kotlin
@Entity(
    tableName = "list_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["id"],
            childColumns = ["list_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["list_id"])]
)
data class ListItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "list_id")
    val listId: Long,
    
    @ColumnInfo(name = "text")
    val text: String,
    
    @ColumnInfo(name = "is_checked")
    val isChecked: Boolean = false,
    
    @ColumnInfo(name = "position")
    val position: Int,
    
    @ColumnInfo(name = "original_position")
    val originalPosition: Int? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
```

#### SuggestionEntity

```kotlin
@Entity(tableName = "suggestions")
data class SuggestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "text")
    val text: String,
    
    @ColumnInfo(name = "usage_count")
    val usageCount: Int = 1,
    
    @ColumnInfo(name = "last_used_at")
    val lastUsedAt: Long,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
```

### 7.2 DAO Interfaces

#### ShoppingListDao

```kotlin
@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_lists ORDER BY updated_at DESC")
    fun getAllLists(): Flow<List<ShoppingListEntity>>
    
    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    fun getListById(id: Long): Flow<ShoppingListEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ShoppingListEntity): Long
    
    @Update
    suspend fun updateList(list: ShoppingListEntity)
    
    @Delete
    suspend fun deleteList(list: ShoppingListEntity)
    
    @Query("DELETE FROM shopping_lists WHERE id = :id")
    suspend fun deleteListById(id: Long)
}
```

#### ListItemDao

```kotlin
@Dao
interface ListItemDao {
    @Query("SELECT * FROM list_items WHERE list_id = :listId ORDER BY is_checked ASC, position ASC")
    fun getItemsByListId(listId: Long): Flow<List<ListItemEntity>>
    
    @Query("SELECT * FROM list_items WHERE id = :id")
    suspend fun getItemById(id: Long): ListItemEntity?
    
    @Query("SELECT COUNT(*) FROM list_items WHERE list_id = :listId")
    fun getItemCount(listId: Long): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM list_items WHERE list_id = :listId AND is_checked = 0")
    fun getUncheckedItemCount(listId: Long): Flow<Int>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ListItemEntity): Long
    
    @Update
    suspend fun updateItem(item: ListItemEntity)
    
    @Update
    suspend fun updateItems(items: List<ListItemEntity>)
    
    @Delete
    suspend fun deleteItem(item: ListItemEntity)
    
    @Query("DELETE FROM list_items WHERE id = :id")
    suspend fun deleteItemById(id: Long)
    
    @Query("SELECT COALESCE(MAX(position), -1) + 1 FROM list_items WHERE list_id = :listId")
    suspend fun getNextPosition(listId: Long): Int
}
```

#### SuggestionDao

```kotlin
@Dao
interface SuggestionDao {
    @Query("SELECT * FROM suggestions WHERE LOWER(text) LIKE '%' || LOWER(:query) || '%' ORDER BY usage_count DESC LIMIT :limit")
    suspend fun getSuggestions(query: String, limit: Int = 3): List<SuggestionEntity>
    
    @Query("SELECT * FROM suggestions WHERE LOWER(text) = LOWER(:text) LIMIT 1")
    suspend fun getSuggestionByText(text: String): SuggestionEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestion: SuggestionEntity): Long
    
    @Update
    suspend fun updateSuggestion(suggestion: SuggestionEntity)
    
    @Query("UPDATE suggestions SET usage_count = usage_count + 1, last_used_at = :timestamp WHERE id = :id")
    suspend fun incrementUsageCount(id: Long, timestamp: Long)
}
```

### 7.3 Database Class

```kotlin
@Database(
    entities = [
        ShoppingListEntity::class,
        ListItemEntity::class,
        SuggestionEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class JotListDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun listItemDao(): ListItemDao
    abstract fun suggestionDao(): SuggestionDao
}
```

---

## 8. Coding Standards

### 8.1 Naming Conventions

| Element | Convention | Example |
|---------|------------|---------|
| Package | lowercase, dot-separated | `com.jotlist.app.ui.screens` |
| Class/Object | PascalCase | `ShoppingListRepository` |
| Function | camelCase | `getItemsByListId()` |
| Variable | camelCase | `itemCount` |
| Constant | SCREAMING_SNAKE_CASE | `MAX_SUGGESTIONS` |
| Composable | PascalCase | `ListItemRow()` |
| ViewModel | PascalCase with suffix | `HomeViewModel` |
| UseCase | PascalCase with suffix | `CreateListUseCase` |
| Entity | PascalCase with suffix | `ShoppingListEntity` |
| DAO | PascalCase with suffix | `ShoppingListDao` |

### 8.2 File Naming

| Type | Pattern | Example |
|------|---------|---------|
| Screen | `[Name]Screen.kt` | `HomeScreen.kt` |
| ViewModel | `[Name]ViewModel.kt` | `HomeViewModel.kt` |
| Composable Component | `[Name].kt` | `ListItemRow.kt` |
| Entity | `[Name]Entity.kt` | `ShoppingListEntity.kt` |
| DAO | `[Name]Dao.kt` | `ShoppingListDao.kt` |
| Repository Interface | `[Name]Repository.kt` | `ShoppingListRepository.kt` |
| Repository Impl | `[Name]RepositoryImpl.kt` | `ShoppingListRepositoryImpl.kt` |
| UseCase | `[Action][Subject]UseCase.kt` | `CreateListUseCase.kt` |
| DI Module | `[Name]Module.kt` | `DatabaseModule.kt` |

### 8.3 Code Style Rules

#### General
- Maximum line length: 120 characters
- Use trailing commas in multi-line declarations
- Prefer expression bodies for simple functions
- Use `val` over `var` whenever possible

#### Compose Specific
- Composables should be stateless when possible (hoisted state)
- Use `remember` and `derivedStateOf` appropriately
- Avoid side effects in Composables; use `LaunchedEffect`, `SideEffect`
- Extract reusable components to separate files

#### Coroutines/Flow
- Use `StateFlow` for UI state in ViewModels
- Use `Flow` for data streams from Room
- Handle exceptions with `catch` operator or try-catch
- Use `viewModelScope` for ViewModel coroutines

### 8.4 Documentation

- Add KDoc comments for public classes and functions
- Document complex business logic
- Include `@param`, `@return`, `@throws` where applicable

```kotlin
/**
 * Creates a new shopping list with the given name.
 * If name is blank, generates a default name using current date.
 *
 * @param name Optional name for the list
 * @return ID of the created list
 */
suspend fun createList(name: String? = null): Long
```

---

## 9. Testing Strategy

### 9.1 Testing Scope

| Test Type | Scope | Tools |
|-----------|-------|-------|
| Unit Tests | ViewModels, Use Cases, Repositories | JUnit, MockK, Turbine |
| Integration Tests | Database operations | Room testing, Coroutines Test |
| UI Tests | Critical user flows (optional) | Compose Testing |

### 9.2 Unit Testing Guidelines

#### What to Test
- ViewModel state changes and actions
- Use case business logic
- Repository data transformations
- Utility functions (e.g., title case converter)

#### Test Structure
```kotlin
class HomeViewModelTest {
    
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    
    private lateinit var viewModel: HomeViewModel
    private lateinit var getAllListsUseCase: GetAllListsUseCase
    
    @Before
    fun setup() {
        getAllListsUseCase = mockk()
        viewModel = HomeViewModel(getAllListsUseCase)
    }
    
    @Test
    fun `when initialized, lists are loaded`() = runTest {
        // Given
        val lists = listOf(/* test data */)
        coEvery { getAllListsUseCase() } returns flowOf(lists)
        
        // When
        viewModel.uiState.test {
            // Then
            val state = awaitItem()
            assertEquals(lists, state.lists)
        }
    }
}
```

### 9.3 Test File Location

```
src/
├── main/kotlin/com/jotlist/app/
│   └── ... (production code)
├── test/kotlin/com/jotlist/app/
│   ├── ui/screens/home/HomeViewModelTest.kt
│   ├── domain/usecase/CreateListUseCaseTest.kt
│   └── util/TitleCaseConverterTest.kt
└── androidTest/kotlin/com/jotlist/app/
    └── data/local/database/dao/ShoppingListDaoTest.kt
```

---

## 10. Build Configuration

### 10.1 Build Variants

| Variant | Application ID | Minify | Use Case |
|---------|----------------|--------|----------|
| debug | `com.jotlist.app.debug` | No | Development |
| release | `com.jotlist.app` | Yes | Production |

### 10.2 ProGuard Rules

```proguard
# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
```

### 10.3 Gradle Tasks

| Task | Purpose |
|------|---------|
| `./gradlew assembleDebug` | Build debug APK |
| `./gradlew assembleRelease` | Build release APK |
| `./gradlew test` | Run unit tests |
| `./gradlew connectedAndroidTest` | Run instrumented tests |
| `./gradlew ktlintCheck` | Check code formatting |
| `./gradlew ktlintFormat` | Auto-fix formatting |
| `./gradlew detekt` | Run static analysis |

---

## 11. Implementation Guidelines

### 11.1 Implementation Order

Follow this order to build the app incrementally:

#### Phase 1: Project Setup
1. Create new Android project with Compose template
2. Configure `build.gradle.kts` with all dependencies
3. Set up version catalog (`libs.versions.toml`)
4. Create package structure
5. Configure Hilt in Application class

#### Phase 2: Data Layer
1. Define Room entities
2. Create DAO interfaces
3. Create database class
4. Set up database module for Hilt
5. Define repository interfaces
6. Implement repositories
7. Set up repository module for Hilt

#### Phase 3: Domain Layer
1. Create domain models (if different from entities)
2. Implement use cases for lists
3. Implement use cases for items
4. Implement use cases for suggestions

#### Phase 4: UI Foundation
1. Set up theme (colors, typography, shapes)
2. Create navigation graph
3. Create Screen sealed class for routes
4. Set up MainActivity with NavHost

#### Phase 5: Home Screen
1. Create HomeViewModel with UI state
2. Create HomeScreen composable
3. Create ListCard component
4. Create EmptyState component
5. Implement FAB for new list creation

#### Phase 6: List Detail Screen
1. Create ListDetailViewModel with UI state
2. Create ListDetailScreen composable
3. Create ListItemRow component
4. Create CheckboxItem component
5. Implement item input field

#### Phase 7: Core Features
1. Implement add item functionality
2. Implement title case formatting
3. Implement check/uncheck with position tracking
4. Implement item reordering (drag and drop)
5. Implement long-press to delete item

#### Phase 8: Suggestions
1. Implement suggestion storage on item add
2. Create SuggestionDropdown component
3. Implement suggestion matching logic
4. Integrate suggestions with input field

#### Phase 9: Dialogs
1. Create ConfirmationDialog component
2. Implement delete list with confirmation
3. Create EditDialog component
4. Implement rename list functionality
5. Implement edit item functionality

#### Phase 10: Polish
1. Add animations (check, move, delete)
2. Refine empty states
3. Test on target device
4. Fix edge cases and bugs

### 11.2 Key Implementation Patterns

#### UI State Pattern

```kotlin
data class HomeUiState(
    val lists: List<ShoppingList> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel @Inject constructor(
    private val getAllListsUseCase: GetAllListsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadLists()
    }
    
    private fun loadLists() {
        viewModelScope.launch {
            getAllListsUseCase()
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { lists ->
                    _uiState.update { it.copy(lists = lists, isLoading = false) }
                }
        }
    }
}
```

#### Use Case Pattern

```kotlin
class CreateListUseCase @Inject constructor(
    private val repository: ShoppingListRepository,
    private val dateFormatter: DateFormatter
) {
    suspend operator fun invoke(name: String?): Long {
        val listName = if (name.isNullOrBlank()) {
            "List (${dateFormatter.formatDate(System.currentTimeMillis())})"
        } else {
            name
        }
        
        val list = ShoppingList(
            name = listName,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        return repository.createList(list)
    }
}
```

#### Repository Pattern

```kotlin
// Interface in domain layer
interface ShoppingListRepository {
    fun getAllLists(): Flow<List<ShoppingList>>
    fun getListById(id: Long): Flow<ShoppingList?>
    suspend fun createList(list: ShoppingList): Long
    suspend fun updateList(list: ShoppingList)
    suspend fun deleteList(id: Long)
}

// Implementation in data layer
class ShoppingListRepositoryImpl @Inject constructor(
    private val dao: ShoppingListDao
) : ShoppingListRepository {
    
    override fun getAllLists(): Flow<List<ShoppingList>> {
        return dao.getAllLists().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    // ... other implementations
}
```

#### Title Case Utility

```kotlin
object TitleCaseConverter {
    fun convert(input: String): String {
        return input.lowercase()
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { 
                    if (it.isLetter()) it.titlecase() else it.toString() 
                }
            }
    }
}
```

### 11.3 Common Pitfalls to Avoid

| Pitfall | Solution |
|---------|----------|
| Collecting Flow in Composables incorrectly | Use `collectAsStateWithLifecycle()` |
| Blocking main thread with Room | Always use `suspend` functions or `Flow` |
| Memory leaks in ViewModels | Use `viewModelScope` for coroutines |
| Hardcoded strings in UI | Use `strings.xml` resources |
| Missing Hilt annotations | Ensure all classes are properly annotated |
| Not handling loading/error states | Always include these in UI state |
| Forgetting to update `updatedAt` | Update timestamp on every list modification |

---

## Document History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | February 2026 | - | Initial document creation |

---

*This document serves as the technical reference for JotList development. For product requirements, refer to the PRD. For visual design specifications, refer to the Design Document.*
