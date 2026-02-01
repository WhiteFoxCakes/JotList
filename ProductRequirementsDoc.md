# Product Requirements Document (PRD)

## JotList - Shopping List App

**Document Version:** 1.0  
**Created:** February 2026  
**Status:** Draft

---

## Table of Contents

1. [Product Overview](#1-product-overview)
2. [Core Features Summary](#2-core-features-summary)
3. [Detailed Functional Requirements](#3-detailed-functional-requirements)
4. [Data Model](#4-data-model)
5. [Technical Requirements](#5-technical-requirements)
6. [User Stories](#6-user-stories)
7. [Out of Scope (v1)](#7-out-of-scope-v1)
8. [Future Considerations](#8-future-considerations)
9. [Glossary](#9-glossary)

---

## 1. Product Overview

### 1.1 Product Name

**JotList**

### 1.2 Description

JotList is a minimalistic Android shopping list application that allows users to create, manage, and save multiple shopping lists. The app features item completion tracking with intuitive check/uncheck functionality and intelligent word suggestions based on previously entered items. Designed for simplicity and ease of use, JotList works entirely offline with local data storage.

### 1.3 Target Platform

- **Operating System:** Android
- **Minimum SDK:** API 29 (Android 10)
- **Target SDK:** API 35 (Android 15)
- **Reference Device:** Poco X7 Pro (Android 15, build AP3A.240905.A2)

### 1.4 Target Users

Individual users managing personal shopping lists who prefer a simple, distraction-free experience without the need for accounts, cloud sync, or complex features.

### 1.5 Storage Approach

All data is stored locally on the device. No internet connection or user account is required. The app uses Room Database (Android's recommended persistence library built on SQLite) for reliable, type-safe local storage with support for migrations.

---

## 2. Core Features Summary

| Feature | Description |
|---------|-------------|
| Multiple Lists | Create and manage multiple named shopping lists |
| Auto-Naming | Unnamed lists receive auto-generated name: "List (DD/MM/YYYY)" |
| Add Items | Manually type items to add to a list |
| Title Case Formatting | Items are automatically formatted in title case (e.g., "Milk", "Orange Juice") |
| Edit Items | Tap to edit any item's text after creation |
| Delete Items | Long press (hold) to delete an item from a list |
| Reorder Items | Drag and drop to manually reorder unchecked items |
| Check Off Items | Tap to mark complete; item gets strikethrough and moves to bottom |
| Uncheck Items | Tap checked item to restore; returns to exact original position |
| Auto-Save | Lists persist automatically using local storage |
| Delete Lists | Option to delete lists with confirmation dialog |
| Word Suggestions | Dropdown suggestions appear when typed characters match saved items |
| Offline-Only | No internet connection required; fully functional offline |

---

## 3. Detailed Functional Requirements

### 3.1 List Management

| ID | Requirement | Priority |
|----|-------------|----------|
| LM-01 | User can create a new shopping list | Must Have |
| LM-02 | User can optionally name a list during or after creation | Must Have |
| LM-03 | Unnamed lists are automatically named "List (DD/MM/YYYY)" using creation date | Must Have |
| LM-04 | User can rename any list at any time | Must Have |
| LM-05 | User can delete a list | Must Have |
| LM-06 | A confirmation dialog is displayed before deleting a list | Must Have |
| LM-07 | User can view all saved lists on a home/main screen | Must Have |
| LM-08 | Lists are automatically saved after any change (add, edit, check, reorder, delete item) | Must Have |
| LM-09 | Lists display the number of items and/or completion status | Should Have |

#### List Deletion Confirmation

When the user initiates list deletion, a confirmation dialog shall appear with:
- Title: "Delete List"
- Message: "Are you sure you want to delete '[List Name]'? This action cannot be undone."
- Actions: "Cancel" (dismiss dialog), "Delete" (proceed with deletion)

### 3.2 Item Management

| ID | Requirement | Priority |
|----|-------------|----------|
| IM-01 | User can add a new item by typing text and confirming (e.g., pressing enter or tapping add button) | Must Have |
| IM-02 | Item text is automatically converted to title case upon saving | Must Have |
| IM-03 | User can edit an existing item's text by tapping on it | Must Have |
| IM-04 | Edited item text is automatically converted to title case upon saving | Must Have |
| IM-05 | User can delete an item from a list by long pressing (holding) the item | Must Have |
| IM-06 | User can reorder unchecked items via drag and drop | Must Have |
| IM-07 | User can check off an item by tapping the checkbox or item | Must Have |
| IM-08 | Checked items display with strikethrough styling | Must Have |
| IM-09 | Checked items automatically move to the bottom of the list | Must Have |
| IM-10 | Checked items maintain their relative order among other checked items | Should Have |
| IM-11 | User can uncheck a checked item by tapping it again | Must Have |
| IM-12 | Unchecked items return to their exact original position in the list | Must Have |

#### Title Case Formatting Rules

Items are automatically formatted in title case according to these rules:
- First letter of each word is capitalized
- Remaining letters are lowercase
- Common articles and prepositions may remain lowercase when not the first word (e.g., "Bag of Chips")
- Numbers and special characters remain unchanged

Examples:
- "milk" → "Milk"
- "ORANGE JUICE" → "Orange Juice"
- "bread and butter" → "Bread And Butter"
- "2% milk" → "2% Milk"

#### Item Position Tracking

To support returning unchecked items to their exact original position:
- Each item stores its current position index
- When an item is checked, its original position is preserved
- When an item is unchecked, it is restored to the saved original position
- Surrounding items shift accordingly to accommodate the restored item

### 3.3 Word Suggestions

| ID | Requirement | Priority |
|----|-------------|----------|
| WS-01 | Every unique item text entered by the user is saved to a suggestion history | Must Have |
| WS-02 | Duplicate items (case-insensitive) are not added to suggestion history; instead, usage count is incremented | Must Have |
| WS-03 | Suggestions are stored in title case format | Must Have |
| WS-04 | When user types 3 or more characters in the item input field, a dropdown appears showing matching suggestions | Must Have |
| WS-05 | Matching is case-insensitive (typing "mil" matches "Milk") | Must Have |
| WS-06 | Matching uses substring search (typed characters can appear anywhere in the saved item) | Must Have |
| WS-07 | Maximum of 3 suggestions are displayed in the dropdown at one time | Must Have |
| WS-08 | Suggestions are ordered by frequency of use (most frequently used first) | Must Have |
| WS-09 | User can tap a suggestion to auto-fill the input field with that item | Must Have |
| WS-10 | Suggestions persist across app sessions using local storage | Must Have |
| WS-11 | User can dismiss the suggestion dropdown by tapping outside or clearing the input | Should Have |

#### Suggestion Matching Logic

1. User types in the item input field
2. After 3 characters are entered, the system queries the suggestion history
3. Query performs case-insensitive substring matching
4. Results are sorted by usage count (descending)
5. Top 3 results are displayed in a dropdown below the input field
6. Dropdown updates in real-time as user continues typing
7. If no matches found, no dropdown is displayed

### 3.4 Auto-Save Behavior

| ID | Requirement | Priority |
|----|-------------|----------|
| AS-01 | All list changes are automatically saved to local storage | Must Have |
| AS-02 | Save occurs immediately after each action (add, edit, delete, check, uncheck, reorder) | Must Have |
| AS-03 | No explicit "save" button is required or shown to users | Must Have |
| AS-04 | Data persists across app restarts and device reboots | Must Have |

---

## 4. Data Model

### 4.1 Entity Relationship Diagram

```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│  ShoppingList   │       │    ListItem     │       │ SuggestionItem  │
├─────────────────┤       ├─────────────────┤       ├─────────────────┤
│ id (PK)         │───┐   │ id (PK)         │       │ id (PK)         │
│ name            │   │   │ listId (FK)     │───────│ text            │
│ createdAt       │   └──►│ text            │       │ usageCount      │
│ updatedAt       │       │ isChecked       │       │ lastUsedAt      │
└─────────────────┘       │ position        │       │ createdAt       │
                          │ originalPosition│       └─────────────────┘
                          │ createdAt       │
                          └─────────────────┘
```

### 4.2 Entity Definitions

#### ShoppingList

| Field | Type | Description | Constraints |
|-------|------|-------------|-------------|
| id | Long | Unique identifier (auto-generated) | Primary Key, Auto-increment |
| name | String | List name (user-provided or auto-generated) | Not null, Max 100 characters |
| createdAt | Long | Timestamp of creation (epoch milliseconds) | Not null |
| updatedAt | Long | Timestamp of last modification (epoch milliseconds) | Not null |

#### ListItem

| Field | Type | Description | Constraints |
|-------|------|-------------|-------------|
| id | Long | Unique identifier (auto-generated) | Primary Key, Auto-increment |
| listId | Long | Reference to parent ShoppingList | Foreign Key, Not null |
| text | String | Item description (stored in title case) | Not null, Max 200 characters |
| isChecked | Boolean | Whether item is checked off | Not null, Default: false |
| position | Int | Current order position in list | Not null |
| originalPosition | Int | Position before being checked (for restore) | Nullable |
| createdAt | Long | Timestamp of creation (epoch milliseconds) | Not null |

#### SuggestionItem

| Field | Type | Description | Constraints |
|-------|------|-------------|-------------|
| id | Long | Unique identifier (auto-generated) | Primary Key, Auto-increment |
| text | String | Item text (stored in title case) | Not null, Unique, Max 200 characters |
| usageCount | Int | Number of times this item has been added | Not null, Default: 1 |
| lastUsedAt | Long | Timestamp of last use (epoch milliseconds) | Not null |
| createdAt | Long | Timestamp of first creation (epoch milliseconds) | Not null |

### 4.3 Storage Technology

**Room Database** (recommended Android persistence library)

Justification:
- Built on SQLite, providing reliable local storage
- Type-safe database queries with compile-time verification
- Seamless integration with Kotlin coroutines and Flow for reactive updates
- Built-in support for database migrations
- Officially recommended by Android/Google
- No external dependencies or network requirements

---

## 5. Technical Requirements

### 5.1 Platform Requirements

| ID | Requirement | Details |
|----|-------------|---------|
| TR-01 | Minimum SDK | API 29 (Android 10) |
| TR-02 | Target SDK | API 35 (Android 15) |
| TR-03 | Reference Device | Poco X7 Pro (Android 15, build AP3A.240905.A2) |

### 5.2 Storage Requirements

| ID | Requirement | Details |
|----|-------------|---------|
| TR-04 | Database | Room Database (SQLite-based) |
| TR-05 | Data Location | Local device storage only |
| TR-06 | Data Persistence | Data must survive app restarts and device reboots |
| TR-07 | No Cloud Sync | App must not require or use cloud storage |

### 5.3 Permissions

| ID | Requirement | Details |
|----|-------------|---------|
| TR-08 | No Network Permission | App must not request INTERNET permission |
| TR-09 | Minimal Permissions | App should request only essential permissions for core functionality |

### 5.4 Performance Requirements

| ID | Requirement | Details |
|----|-------------|---------|
| TR-10 | App Launch | App should launch and display main screen within 2 seconds |
| TR-11 | List Loading | Lists should load and display within 500ms |
| TR-12 | Auto-Save | Save operations should complete within 100ms (non-blocking) |
| TR-13 | Suggestion Dropdown | Suggestions should appear within 200ms of typing |

### 5.5 Compatibility

| ID | Requirement | Details |
|----|-------------|---------|
| TR-14 | Screen Sizes | Support various screen sizes (phones) |
| TR-15 | Orientation | Support portrait orientation (landscape optional) |
| TR-16 | Android Versions | Support Android 10 through Android 15+ |

---

## 6. User Stories

### 6.1 List Management Stories

| ID | User Story | Acceptance Criteria |
|----|------------|---------------------|
| US-01 | As a user, I want to create a new shopping list so I can organize items for a specific shopping trip | - New list button visible on home screen<br>- Tapping creates new list and opens it<br>- List appears in home screen list view |
| US-02 | As a user, I want to name my list so I can identify it later | - Can enter name when creating list<br>- Can rename existing list<br>- Name displays on home screen and list view |
| US-03 | As a user, I want unnamed lists to have an auto-generated name so I don't have to name every list | - Unnamed lists receive name "List (DD/MM/YYYY)"<br>- Date reflects creation date |
| US-04 | As a user, I want to view all my lists on the home screen so I can quickly access any list | - Home screen shows all saved lists<br>- Lists show name and item count or status<br>- Tapping a list opens it |
| US-05 | As a user, I want to delete a list when I'm done with it so my home screen stays organized | - Delete option available for each list<br>- Confirmation dialog appears before deletion<br>- Deleted list is removed from home screen |

### 6.2 Item Management Stories

| ID | User Story | Acceptance Criteria |
|----|------------|---------------------|
| US-06 | As a user, I want to add items to my list by typing them so I can record what I need to buy | - Text input field visible in list view<br>- Can type item name and confirm<br>- Item appears in list immediately |
| US-07 | As a user, I want my items automatically formatted in title case so my list looks neat and consistent | - "milk" becomes "Milk"<br>- "BREAD" becomes "Bread"<br>- Formatting applied on save |
| US-08 | As a user, I want to edit an item if I made a typo or need to change it | - Can tap item to edit<br>- Edited text is saved<br>- Title case formatting applied |
| US-09 | As a user, I want to delete items by holding them so I can remove items I don't need | - Long press on item triggers delete<br>- Item is removed from list immediately |
| US-10 | As a user, I want to reorder my list by dragging items so I can group items the way I prefer | - Can drag and drop unchecked items<br>- New order is saved automatically |

### 6.3 Check/Uncheck Stories

| ID | User Story | Acceptance Criteria |
|----|------------|---------------------|
| US-11 | As a user, I want to check off items as I shop so I can track my progress | - Can tap item to check it<br>- Checked items show strikethrough<br>- Checked items move to bottom |
| US-12 | As a user, I want checked items to move to the bottom so I can focus on what's left to buy | - Checked items automatically reposition<br>- Unchecked items remain at top |
| US-13 | As a user, I want to uncheck an item if I made a mistake so it returns to my active list | - Can tap checked item to uncheck<br>- Item returns to exact original position<br>- Strikethrough is removed |

### 6.4 Suggestion Stories

| ID | User Story | Acceptance Criteria |
|----|------------|---------------------|
| US-14 | As a user, I want to see word suggestions as I type so I can quickly add items I've used before | - Dropdown appears after 3 characters<br>- Shows matching items from history<br>- Maximum 3 suggestions shown |
| US-15 | As a user, I want suggestions ordered by how often I use them so the most relevant items appear first | - Most frequently used items appear first<br>- Order updates as usage changes |
| US-16 | As a user, I want to tap a suggestion to add it to my list so I don't have to type the whole word | - Tapping suggestion fills input field<br>- User can then confirm to add item |

### 6.5 Data Persistence Stories

| ID | User Story | Acceptance Criteria |
|----|------------|---------------------|
| US-17 | As a user, I want my lists to save automatically so I don't lose my work | - All changes saved immediately<br>- No save button needed<br>- Data persists after closing app |
| US-18 | As a user, I want to use the app without internet so it works anywhere | - App functions fully offline<br>- No network errors or prompts<br>- All features available offline |

---

## 7. Out of Scope (v1)

The following features are explicitly excluded from version 1 to maintain simplicity and focus:

| Feature | Reason |
|---------|--------|
| Cloud sync | Personal use, offline-only requirement |
| User accounts / authentication | Not needed for single-user local app |
| List sharing | Personal use only; no multi-user support |
| Categories / aisles / sections | Keeping the interface minimalistic |
| Price tracking | Outside core scope; keeping it simple |
| Quantity fields | User can include quantity in item text if needed (e.g., "Milk 2L") |
| Photos / images | Keeping the interface minimalistic |
| Notifications / reminders | Outside core scope |
| Home screen widgets | Can be considered for future versions |
| Dark mode / themes | Deferred to future versions |
| Export / import functionality | Deferred to future versions |
| Barcode scanning | Outside core scope |
| Voice input | Outside core scope |
| Multiple languages / localization | English only for v1 |
| Suggestion history management | Users cannot manually view/delete suggestions in v1 |

---

## 8. Future Considerations

The following features may be considered for future versions (v2+):

### 8.1 Visual & UX Enhancements
- Dark mode / theme options
- Customizable accent colors
- Swipe gestures (swipe to delete, swipe to check)
- Animations and transitions

### 8.2 Functionality Enhancements
- Home screen widget showing current list
- Export lists (share as text, PDF, or file)
- Import lists from text or file
- Backup and restore functionality
- Categories or sections within a list
- Sorting options (alphabetical, by category, custom)
- List templates (save and reuse frequent list setups)
- Duplicate list functionality

### 8.3 Suggestion Enhancements
- Suggestion management screen (view/delete saved suggestions)
- Suggestion categories
- Smart suggestions based on time/day patterns

### 8.4 Platform Enhancements
- Tablet layout optimization
- Landscape orientation support
- Wear OS companion app
- iOS version

---

## 9. Glossary

| Term | Definition |
|------|------------|
| Auto-save | Automatic saving of data without user action |
| Checked item | An item marked as complete/purchased |
| Dropdown | A UI element that appears below the input field showing suggestions |
| Home screen | The main screen of the app showing all lists |
| List | A shopping list containing multiple items |
| List item | An individual entry in a shopping list |
| Long press | Touch and hold gesture (typically 500ms+) |
| Original position | The position an item held before being checked |
| Room Database | Android's recommended SQLite abstraction library |
| Strikethrough | Text styling with a line through it indicating completion |
| Substring match | Finding text that contains the search term anywhere within it |
| Suggestion | A previously entered item offered as auto-complete |
| Suggestion history | Collection of all unique items ever entered by the user |
| Title case | Capitalization style where the first letter of each word is uppercase |
| Unchecked item | An item not yet marked as complete |

---

## Document History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | February 2026 | - | Initial document creation |

---

## Approval

| Role | Name | Date | Signature |
|------|------|------|-----------|
| Product Owner | | | |
| Technical Lead | | | |
| Designer | | | |

---

*This document serves as the single source of truth for JotList v1 product requirements. All development and design decisions should align with the specifications outlined herein. For technical implementation details, refer to the Tech Stack Document. For visual and interaction design, refer to the Design Document.*
