# German ↔ Albanian Vocabulary Trainer — Java CLI with Spaced Repetition

A self-developed command-line vocabulary trainer for learning **German ↔ Albanian** vocabulary.  
The application features flexible quiz modes, performance tracking, error analysis, and persistent local storage.

> 🎨 A graphical user interface (GUI), designed by a **UX/UI designer**, is planned as the next major development step.

---

## ✨ Features


- Quiz modes with four options:
    - German → Albanian
    - Albanian → German
    - Random direction
    - Error Quiz (retry incorrect answers from past sessions)
- Add, search, and view your own vocabulary
- Persistent storage using JSON:
  - `vocab_data.json` (vocabulary)
  - `fehlerliste.json` (mistakes)
  - `stats.json` (statistics)
  - `users.json` (user accounts)
- Tracks performance statistics including:
    - Total correct and incorrect answers
    - Accuracy percentage
    - Most frequent errors per vocabulary item
    - Correct answers tracked per day for progress monitoring
    - View detailed statistics anytime from the menu
- Smart spaced repetition algorithm for personalized review timing
- Role-based user system (see below for details)

---

## 🔐 User Management & Login System

The application includes a secure login and user role system:

- Register/login with username and password
- Passwords are securely hashed with SHA-256
- Admin users can:
    - View all registered users
    - Delete users (except themselves)
    - Register new users manually
    - Reset all statistics
- Role-based access enforcement for admin features
- Persistent user data stored in `users.json`

---

## 🔧 Technologies & Tools

- Java 17
- IntelliJ IDEA
- Gson with a custom LocalDateAdapter for JSON serialization/deserialization of LocalDate
- Secure password handling using MessageDigest and SHA-256
- JUnit 5 for unit testing
- Clean, modular object-oriented design
- Command-line interface using Scanner
- Persistent file handling with JSON
- Real-world error handling
- Planned GUI using JavaFX or Web technologies

---

## 🎯 Motivation & Context

This project was created to fill a gap:  
Popular language learning apps rarely support **German ↔ Albanian**.

It was developed to:

- Support my own language learning process
- Provide a practical tool for an underserved language combination
- Showcase core programming skills and clean software architecture
- Apply proven learning methods like spaced repetition for improved retention

---

## 💼 Career Relevance

This project reflects key backend development skills:

- CLI-based application architecture
- File I/O handling via JSON
- Integration of external libraries (Gson) with custom type adapters
- Implementation of a spaced repetition algorithm
- Realistic error handling and user interaction
- Role-based access control with admin and user roles

It also lays the foundation for:

- Collaboration with UX/UI designers
- Future cloud-readiness (e.g. user accounts, APIs, frontend)
- Security-relevant features (e.g. audit logging, authentication layers)
---

## 🗂 Project Structure

```
project-root/ 
├── README.md
├── src/
│ └── trainer/
│   ├── AnswerCheckResult.java
│   ├── LocalDateAdapter.java
│   ├── Main.java
│   ├── QuizManager.java
│   ├── StatsManager.java
│   ├── UIHelper.java
│   ├── User.java
│   ├── UserService.java
│   ├── Vocab.java
│   ├── VocabRepository.java
│   └── VocabService.java
├── tests/    
│ └── trainer/   
│   ├── FehlerQuizTest.java
│   ├── LocalDateAdapterTest.java
│   ├── MainTest.java 
│   ├── QuizLogicTest.java 
│   ├── QuizManagerTest.java
│   ├── StatsManagerTest.java
│   ├── UIHelperTest.java
│   ├── UserServiceTest.java
│   ├── UserTest.java
│   ├── VocabRepositoryTest.java
│   ├── VocabServiceTest.java
│   └── VocabTest.java
├── fehlerliste.json
├── stats.json
├── users.json
└── vocab_data.json
 
```

---

## ▶️ How to Run

1. Clone the repository

2. Open the project in IntelliJ IDEA or any Java IDE

3. If you use Maven or Gradle, dependencies will be managed automatically.  
   Otherwise, download and add [gson-2.10.1.jar](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar) to your classpath

4. Run `Main.java`

5. Follow the menu instructions (register / login required)

---

## 👥 Predefined User Accounts

The application comes with two predefined users for quick testing and demonstration:  

👑 Admin account  
Username: admin  
Password: admin123  
Role: admin

👤 User account  
Username: chris  
Password: chris123  
Role: user

You can use these accounts for testing or create your own via the registration option.

---

## 💻 Sample Console Output

Willkommen zum Vokabeltrainer (Deutsch ↔ Albanisch)

🔐 Bitte wählen:
1. Einloggen
2. Registrieren  
   Auswahl: 1  
Benutzername: admin  
Passwort: admin123  
✅ Willkommen, admin (admin)

Menü:  
1. Vokabel hinzufügen
2. Vokabeln anzeigen
3. Vokabeln abfragen
4. Fehler-Quiz starten
5. Vokabel suchen
6. Statistiken anzeigen
7. Beenden   
8. Admin-Menü  
   Auswahl: 3

Es sind 35 Vokabeln fällig.  
Es werden zufällig 20 davon abgefragt.  

In welche Richtung möchtest du das Quiz machen?

1. Deutsch → Albanisch
2. Albanisch → Deutsch
3. Zufällig  
   Auswahl: 1

💡 Gib "exit" ein, um die Session vorzeitig zu beenden.

Was heißt "arbeiten" auf Albanisch? > punoj  
✅ Richtig!

Was heißt "Haus" auf Albanisch? > shtepi  
❌ Falsch! Richtig: shtëpi

Was heißt "stehlen" auf Albanisch? > vjedh  
✅ Richtig!

Was heißt "Baum" auf Albanisch? > exit  
⏹️ Quiz abgebrochen. Fortschritt wird gespeichert.

📊 Ergebnis:  
Richtig: 2  
Falsch: 1  
Trefferquote: 66,67 %

❗ Falsch beantwortete Vokabeln:  
Haus → shtëpi (Nächste Wiederholung: 2025-06-05)

---

## 🚀 Upcoming Features

- Development of a graphical user interface (JavaFX or Web)
- Responsive UI design in collaboration with a UX/UI designer
- Import/export support for custom vocabulary sets

---

## ✅ Testing

This project includes comprehensive unit tests covering:

- Vocabulary data management (load/save JSON)
- Quiz logic correctness and edge cases
- Search functionality (exact, partial, case-insensitive)
- Performance statistics tracking
- User registration, login and role management

Tests are located in the `tests/` directory and can be run using JUnit 5 in IntelliJ or via command line.

---

## 🤝 Team

💻 Backend & CLI development:  
Christopher Schäfer (Computer Science student at LMU Munich)

🎨 UX/UI design (planned):  
Megi Beka (Media Design student & Albanian native speaker)

---

## 📜 License

MIT License – free to use, share and modify for educational or personal purposes.