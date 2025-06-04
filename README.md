# German ↔ Albanian Vocabulary Trainer — Java CLI Application

A self-developed command-line vocabulary trainer for learning **German ↔ Albanian** vocabulary.  
The application features flexible quiz modes, performance tracking, error analysis, and persistent local storage.

> 🎨 A graphical user interface (GUI), designed by a **UX/UI designer**, is planned as the next major development step.

---

## ✨ Features

- Quiz mode with three options:
    - German → Albanian
    - Albanian → German
    - Random direction
- Fehler-Quiz mode: retry incorrect answers from past sessions
- Add and view your own vocabulary
- Automatically saves data to `vocab_data.json`
- Loads default vocabulary from `vokabeln.csv` on first run
- Incorrect answers are saved to `fehlerliste.txt`
- Tracks performance statistics (correct/incorrect answers, accuracy)

---

## 🔧 Technologies & Tools

- **Java 17**
- **IntelliJ IDEA**
- **Gson** (for JSON serialization)
- Clean, modular **object-oriented design**
- GUI planned using **JavaFX** or **Web technologies**

---

## 🎯 Motivation & Context

This project was built to solve a real problem:  
Popular language learning apps rarely support **German ↔ Albanian**.

It was developed to:
- Support my own language learning process
- Offer a practical tool for underserved language pairs
- Showcase core programming skills and clean software architecture

---

## 💼 Career Relevance

This project reflects key backend development skills:

- CLI-based app architecture
- File handling: CSV, JSON, plain text
- External library integration (Gson)
- Structured code and data separation
- Real-world error handling and user interaction
- Writing and maintaining unit tests (JUnit) to ensure code quality and reliability

It also lays the foundation for:
- Collaboration with UX/UI designers
- Future cloud-readiness (e.g. database, API, frontend)

---

## 🗂 Project Structure

```
project-root/ 
├── README.md
├── src/
│ └── trainer/
│   ├── Main.java
│   ├── Vocab.java
│   ├── StatsManager.java
│   └── VocabRepository.java
├── tests/    
│ └── trainer/   
│   ├── MainTest.java 
│   ├── QuizLogicTest.java 
│   ├── SearchTest.java
│   ├── StatsManagerTest.java
│   ├── VocabRepositoryTest.java
│   └── VocabTest.java
├── vokabeln.csv
├── vocab_data.json
└── fehlerliste.txt
 
```
---

## 📄 vokabeln.csv

This file contains almost **300 German–Albanian vocabulary pairs** and is automatically loaded on the first run if no saved vocabulary (`vocab_data.json`) exists.

**Format:**  
- Each line consists of one word pair, separated by a comma:

```
AlbanianWord,GermanWord
```
- If a word has multiple German meanings, they are enclosed in double quotes:

```
verë,"sommer, wein"
```
The vocabulary list is sorted alphabetically by the Albanian terms to improve readability and usability.  
Special characters (e.g. ë, ç, …) are UTF-8 encoded and fully supported.

---

## ▶️ How to Run

1. Clone the repository

2. Open the project in IntelliJ (or any Java IDE)

3. Download and add [gson-2.10.1.jar](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar) to your classpath

4. Run Main.java

5. Follow the menu instructions

---

## 💻 Sample Console Output

Willkommen zum Vokabeltrainer (Deutsch ↔ Albanisch)

Menü:
1. Vokabel hinzufügen
2. Vokabeln anzeigen
3. Vokabeln abfragen
4. Fehler-Quiz starten
5. Vokabel suchen
6. Beenden  
Auswahl: 3

Welche Richtung möchtest du üben?
1. Deutsch → Albanisch
2. Albanisch → Deutsch
3. Zufällig  
Auswahl: 1  
Was heißt "arbeiten" auf Albanisch? > punoj  
✅ Richtig!

---

## 🚀 Upcoming Features
- Graphical user interface (JavaFX or Web)

- Responsive UI planned in collaboration with a media design student with a focus on UX/UI

- Smart learning logic (e.g. spaced repetition)

- Import/export support for custom vocab sets

---

## ✅ Testing
This project includes comprehensive unit tests covering:

- Vocabulary data management (load/save CSV and JSON)

- Quiz logic correctness and edge cases

- Search functionality (exact, partial, case-insensitive)

- Performance statistics tracking

Tests are located in the tests/ directory and can be run using JUnit 5 in IntelliJ or via command line.

---

## 🤝 Team
💻 Backend & CLI development:  
Christopher Schäfer (Computer Science student at LMU Munich)

🎨 UX/UI design (planned):  
Megi Beka (Media Design student & Albanian native speaker)

---

## 📜 License
MIT License – free to use, share and modify for educational or personal purposes.