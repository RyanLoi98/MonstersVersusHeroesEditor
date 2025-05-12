# 🗺️ Monsters vs Heroes – Java OOP Map Making Program

**CPSC 233 – Introduction to Computer Science II**
**Assignment 3 – Winter 2022**
Author: Ryan Loi
Version: 1.0
Date: 27/03/22

---

## 🧙‍♂️ Project Overview

This project is a Java-based **Map Making** utility designed to support the **Monsters vs Heroes** simulation game (https://github.com/RyanLoi98/MonstersVersusHeroes). The program allows users to build a custom game world interactively and then save it to a file in the correct format for simulation.

It reinforces object-oriented design and GUI programming through:

* Object creation and grid management
* JavaFX GUI interface
* User interaction through input fields and buttons
* File I/O operations for saving world files

---

## 🕹️ How to Run the Program



## 📸 Demo Screenshot

Here’s a screenshot of the game in action:

![Screenshot](https://imgur.com/M4F0hng.png)



### 🧪 Option 1: Run with `mvh.app.Main` via Terminal

1. Navigate to the `target/classes` directory inside your project.
2. Open Terminal (Linux/macOS) or CMD (Windows).
3. Enter the following command (replace the `[path]` with the actual path to your JavaFX `lib` folder):

```bash
java --module-path "[path to javafx lib]" --add-modules javafx.controls,javafx.fxml mvh.app.Main
```

✅ **Example**:

```bash
java --module-path "/lib/jvm/openjfx-18_linux-x64_bin-sdk/javafx-sdk-18/lib" --add-modules javafx.controls,javafx.fxml mvh.app.Main
```
---

### 🧪 Option 2: Run with `.jar` File

You can run this from:

#### A) The Same Directory as the `.jar`

```bash
java --module-path "[path to javafx lib]" --add-modules javafx.controls,javafx.fxml -jar CPSC233W22A3.jar
```

#### B) Anywhere (With Full Path to `.jar`)

```bash
java --module-path "[path to javafx lib]" --add-modules javafx.controls,javafx.fxml -jar "/your/path/to/CPSC233W22A3.jar"
```

✅ **Example**:

```bash
java --module-path "/lib/jvm/openjfx-18_linux-x64_bin-sdk/javafx-sdk-18/lib" --add-modules javafx.controls,javafx.fxml -jar "/home/ryan/Documents/University/Year 1/Winter 2022/Computer Science 233/Assignments/Assignments/Assignment 3/CPSC233W22A3/out/artifacts/CPSC233W22A3/CPSC233W22A3.jar"
```

---

### 🧪 Option 3: Run via an IDE

You can also run the program via any IDE (such as IntelliJ or Eclipse):

* Locate `Main.java` inside `mvh.app`
* Right-click the file and choose **Run** (ensure JavaFX is configured in your project settings)

---

## 🧭 General GUI Use Instructions

### 📁 Menu Bar

1. **Load File**

   * Error-handled loading of existing files.
   * Verifies each entity’s type, symbol, health, weapon, and armor.

2. **Save / Save As**

   * `Save`: Only works when a file is already loaded.
   * `Save As`: Opens dialog to create or overwrite a file.

3. **Exit**

   * Closes the program safely.

4. **About**

   * Shows program and author information.

---

### 🧱 Map Creation

5. **World Creation**

   * Input fields expect integer values ≥ 1 for rows/columns.

6. **Hero & Monster Creation**

   * Must select either Hero or Monster radio button.
   * Health must be integer ≥ 0.
   * Symbol must be a **unique single character** (A-Z).
   * Monster weapon: Select from dropdown.
   * Hero: Attack and armor must be ≥ 0.
   * Row/Column inputs must be valid within world bounds.

7. **Entity Deletion**

   * Works only with a loaded world.
   * Deletes based on row/column location.
   * Errors shown if no entity exists at the target cell.

8. **View Map**

   * Automatically updates on changes.
   * Scrollable and supports large world sizes.

9. **Entity Details**

   * Input row/column to display entity info.
   * Alerts shown for invalid cells or empty tiles.

10. **Status Panel**

* Left: User actions.
* Right: Errors (red) and warnings (orange).

---

## 📜 Program Features

* 📦 Object-oriented design with separate model, view, and control layers
* 🎨 Visual entity placement on a scrollable map
* 🛠️ Input validation to prevent invalid configurations
* 💾 Save/export to `.txt` files for game simulation
* 🧍 Hero/Monster creation with full attribute customization
* 🗑️ Entity deletion and map updates in real-time

---

## 🧪 Manual Testing

Manual tests were used to verify the following:

* Validation of all user inputs
* Functional save/load operations
* GUI responsiveness for entity placement, deletion, and updates
* Correct file export format for integration with the simulation game

---

## 📁 Example Output (`world.txt`)

```
5
5
0,0,MONSTER,G,15,S
1,1,HERO,H,20,5,2
2,2,WALL
```

---

## 🧠 Learning Outcomes

* Built a functional JavaFX GUI application
* Practiced encapsulation and object composition
* Reinforced MVC architecture patterns
* Learned file handling and user input validation
* Created a tool that integrates with a larger simulation system
