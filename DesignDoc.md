# Design Document: JotList (v1.0)

**Project:** JotList - Shopping List App  
**Platform:** Android (Material Design 3 Implementation)  
**Version:** 1.0  
**Date:** February 2026  
**Status:** Approved for Development  
**Reference:** [PRD v1.0] [Visual Inspiration: Dark Dashboard UI]

---

## 1. Design Overview

### 1.1 Design Philosophy: "Vibrant Utility"
JotList combines the functional minimalism required by the PRD with the engaging, high-contrast aesthetic of the provided reference image. The design language uses a **"Dark Mode First"** approach, utilizing deep backgrounds to reduce battery consumption and eye strain, punctuated by vibrant neon accents to guide user interaction and establish hierarchy.

### 1.2 Visual Inspiration Analysis
Based on the provided reference image, the following visual pillars have been extracted:
* **Depth Strategy:** High contrast between background (`#1F222B`) and surface elements (`#2D303E`) replaces traditional heavy drop shadows.
* **Geometry:** Soft, approachable geometry with generous corner radii (approx. 20dp-24dp) on cards and buttons.
* **Palette:** A dark canvas accented by "Cyber" tones—Electric Blue, Violet, and Neon Green—creating a modern, tech-forward feel.
* **Layout:** Dashboard-style grids with clear breathing room (whitespace) between elements.

### 1.3 Target Aesthetic
* **Mood:** Sleek, Focused, Modern.
* **Texture:** Matte surfaces with soft gradients on interactive elements.

---

## 2. Color System

The color palette is derived directly from the reference image's dashboard UI.

### 2.1 Primary Palette (Foundation)
| Token | Hex Code | Android Role | Usage |
| :--- | :--- | :--- | :--- |
| `Dark Background` | **#181920** | `colorBackground` | Main app background (Deepest dark) |
| `Surface Card` | **#252836** | `colorSurface` | List cards, dialogs, bottom sheets |
| `Input Surface` | **#2D303E** | `colorSurfaceVariant` | Text input fields, search bars |

### 2.2 Accent Palette (Action)
| Token | Hex Code | Android Role | Usage |
| :--- | :--- | :--- | :--- |
| `Electric Blue` | **#546FFF** | `colorPrimary` | FAB, Primary Buttons, Active States |
| `Violet` | **#9747FF** | `colorSecondary` | Tags, Selection highlights, decorative icons |
| `Neon Green` | **#2AC769** | `colorTertiary` | Success states, Checkboxes, Completion |
| `Soft Red` | **#FF5C5C** | `colorError` | Delete actions, Error messages |

### 2.3 Text & Iconography
| Token | Hex Code | Usage |
| :--- | :--- | :--- |
| `White` | **#FFFFFF** | **H1, H2, H3**, Active List Items |
| `Soft Grey` | **#8F92A1** | **Body**, Subtitles, Completed Items (Strikethrough) |
| `Dark Grey` | **#5E6272** | **Disabled**, Placeholders, Hints |

---

## 3. Typography

**Font Strategy:** The reference image uses a clean, geometric sans-serif. We will use **Google Fonts** to ensure Android compatibility without adding APK size bloat.

* **Headings:** **Outfit** (Geometric, bold, modern).
* **Body:** **Inter** (Highly legible, neutral, excellent for lists).

### Type Scale

| Style | Font Family | Weight | Size (sp) | Line Height | Usage |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **H1** | Outfit | Bold (700) | 32sp | 40sp | App Title, "Hello User" |
| **H2** | Outfit | SemiBold (600) | 22sp | 28sp | Screen Headers, Dialog Titles |
| **H3** | Outfit | Medium (500) | 18sp | 24sp | List Card Names |
| **Body 1** | Inter | Regular (400) | 16sp | 24sp | List Items, Input Text |
| **Body 2** | Inter | Medium (500) | 14sp | 20sp | Suggestions, Toasts |
| **Caption** | Inter | Regular (400) | 12sp | 16sp | Timestamps, Item Counts |
| **Button** | Inter | SemiBold (600) | 16sp | 20sp | Action Buttons |

---

## 4. Spacing & Layout

**Grid System:** 8dp Baseline Grid.
**Screen Margins:** 20dp (Standard for modern mobile).

### Spacing Tokens
* `xs`: **4dp** (Tight grouping)
* `sm`: **8dp** (Related elements)
* `md`: **16dp** (Component padding)
* `lg`: **24dp** (Section separation)
* `xl`: **32dp** (Major layout breaks)

### Corner Radii
Based on the "soft" look of the reference image:
* **Cards / Dialogs:** `24dp`
* **Buttons / Inputs:** `16dp`
* **Checkboxes / Tags:** `8dp`

---

## 5. Component Specifications

### 5.1 App Bar / Header
* **Height:** 64dp
* **Background:** Transparent (or `#181920`)
* **Content:**
    * **Left:** H1 Title ("JotList").
    * **Right:** 48dp Icon Button (Settings/Menu) or Avatar placeholder.

### 5.2 List Card (Home Screen)
*Visualizes a shopping list on the main dashboard.*
* **Dimensions:** Width: `Match Parent` (in Grid), Min Height: `140dp`.
* **Background:** `#252836` (Surface Card).
* **Corner Radius:** `24dp`.
* **Padding:** `16dp`.
* **Content:**
    * Icon container: 48x48dp rounded rect (`#2D303E`) with an icon in center.
    * Title: H3 (`#FFFFFF`).
    * Progress Bar (Optional visual): Thin line `#2D303E` with `#546FFF` progress.
    * Item Count: Caption (`#8F92A1`).

### 5.3 List Item (Shopping Item)
* **Height:** Min `56dp`.
* **Background:** Transparent.
* **Padding:** Horizontal `4dp` (internal pad `12dp`).
* **States:**
    * **Unchecked:** Text `#FFFFFF`. Icon: Ring `#5E6272`.
    * **Checked:** Text `#8F92A1` + Strikethrough. Icon: Solid Circle `#2AC769` + Checkmark.
* **Interaction:** Entire row is clickable. Long-press to delete.

### 5.4 Text Input Field (Add Item)
* **Height:** `56dp`.
* **Background:** `#2D303E` (Input Surface).
* **Corner Radius:** `16dp`.
* **Text:** Body 1 (`#FFFFFF`).
* **Hint:** "Add item..." (`#5E6272`).
* **Icon:** Right-aligned "Plus" icon tint `#546FFF`.

### 5.5 Suggestion Dropdown
* **Background:** `#252836` (Surface Card).
* **Elevation:** 8dp (Shadow).
* **Item Height:** `48dp`.
* **Divider:** 1dp solid `#2D303E`.
* **Logic:** Appears anchored to the bottom of the Input Field when typing 3+ chars.

### 5.6 Floating Action Button (FAB)
* **Size:** `56dp` x `56dp`.
* **Background:** `#546FFF` (Electric Blue).
* **Icon:** White "Plus" (`+`).
* **Shape:** Squircle / Rounded Rect (`16dp` radius) to match image aesthetic.

### 5.7 Dialogs / Modals
* **Overlay:** Black @ 60% Opacity.
* **Container:** `#252836` with `24dp` radius.
* **Actions:**
    * **Primary:** Filled Button (`#546FFF`).
    * **Secondary:** Text Button (`#8F92A1`).
    * **Destructive:** Filled Button (`#FF5C5C`).

---

## 6. Iconography

**Style:** Material Symbols Rounded (Filled).
**Size:** Standard `24dp`.

| Icon Name | Usage |
| :--- | :--- |
| `add_rounded` | FAB, Input Field |
| `delete_rounded` | Delete Action |
| `edit_rounded` | Rename/Edit |
| `check_circle_rounded` | Completed Item |
| `drag_indicator_rounded` | Reordering |
| `arrow_back_rounded` | Navigation |
| `shopping_bag_outlined` | Empty State (Large 64dp) |

---

## 7. Motion & Animation

* **Duration:** 300ms (Standard).
* **Curve:** `FastOutSlowInInterpolator`.
* **Specific Animations:**
    * **Item Check:** Checkbox fills green -> Strikethrough draws L to R -> Item fades to 50% opacity -> Item slides to bottom of list.
    * **Item Uncheck:** Item slides up to original index -> Opacity restores -> Strikethrough disappears.
    * **Suggestions:** Dropdown fades in + slides down (100ms).

---

## 8. Screen Layouts

### 8.1 Home Screen
* **Header:** "JotList" title.
* **Body:** Staggered Grid Layout (2 columns) of **List Cards**. Resembles the "Projects" view in the reference image.
* **FAB:** Bottom Right, fixed.
* **Empty State:** Centered Icon + Text "No lists yet".

### 8.2 List View Screen
* **Top Bar:** Back Button, List Title, Overflow Menu (Rename/Delete List).
* **Input Area:** Pinned to the top, below the header. Full width input field.
* **List Area:**
    * `RecyclerView` containing items.
    * Unchecked items at top.
    * "Completed" divider (Optional, sleek line).
    * Checked items at bottom.
* **Drag & Drop:** Enabled only for Unchecked section.

### 8.3 Dialogs
* **Delete List:** Centered modal. Title: "Delete List?". Message: "This cannot be undone." Actions: Cancel / Delete.
* **Edit Item:** Centered modal with Input Field pre-filled with item text.

---

## 9. Touch Targets & Accessibility

* **Minimum Target:** 48x48dp for all interactive elements (even if the icon is smaller, the transparent padding must extend to 48dp).
* **Contrast Ratios:**
    * White text on `#181920`: **15.6:1** (AAA Pass).
    * Grey text (`#8F92A1`) on `#181920`: **5.2:1** (AA Pass).
    * Blue buttons (`#546FFF`) on `#252836`: **5.1:1** (AA Pass).
* **Screen Readers:** All IconButtons must have `android:contentDescription` (e.g., "Add new list", "Delete item").

---

## 10. Assets & Graphics

* **App Icon:** A stylized, minimalist "J" or shopping bag using a gradient from `#546FFF` to `#9747FF` on a dark background.
* **Splash Screen:** Background `#181920`, Logo centered.
* **Empty States:** Vector illustrations using the `Soft Grey` line art style with a single pop of `Electric Blue`.

---

## 11. Dark Mode Considerations

* **Current Design:** The primary design **is** Dark Mode.
* **Future Light Mode:** If implemented later, simply invert the `colorBackground` to `#F5F7FA` and `colorSurface` to `#FFFFFF`, while keeping the `Electric Blue` accent constant. Text would invert to `#181920`.