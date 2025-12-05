# TeamMate

TeamMate is a small Java console app that helps organizers build **balanced teams** for games and tournaments using each participant’s:

- preferred game  
- preferred role  
- skill level  
- simple 5-question personality survey  

It then applies a set of constraints (game mix, role mix, personality mix, skill balance) to form the best possible teams and list anyone who couldn’t be placed.

---

## Features

- **Participant management**
  - Add new participants via guided console prompts
  - Automatically validate input (ranges, email format, duplicates)
  - Persist participants to a CSV “system file”
  - Import participants from an external CSV file

- **Personality classification**
  - 5 survey questions scored 1–5
  - Score scaled to 0–100
  - Classified into `LEADER`, `BALANCED`, `THINKER`, or `OTHER`
  - Input validation with clear error messages

- **Team formation**
  - Builds teams of a chosen size (2–10)
  - Ensures each team has:
    - At least one **Leader**
    - At least one **Thinker**
  - Tries to:
    - Keep **average skill levels** close across teams
    - Limit the number of participants with the same **game**
    - Limit the number of participants with the same **role**
  - Produces:
    - A list of formed teams (with members)
    - A list of leftover participants

- **Threaded operations**
  - Background thread for team formation
  - Background thread for saving participants to file
  - Logs errors using `java.util.logging`

- **Basic test coverage**
  - `PersonalityClassifierTest` – verifies scoring & type mapping
  - `TeamBuilderTest` – verifies team formation rules & edge cases

---

## Tech Stack

- **Language:** Java (JDK 8+ recommended)  
- **Build/Run:** Plain `javac` / `java` or any Java IDE (IntelliJ IDEA, Eclipse, VS Code, …)  
- **Testing:** JUnit 5 (via IDE or manual classpath setup)  
- **Data format:** CSV

---

## Project Structure

```text
TeamMate/
├─ src/
│  ├─ TeamMateApp.java          # Main entry point
│  ├─ Controllers/
│  │  ├─ AppController.java     # Top-level app flow / menus
│  │  ├─ OrganizerController.java
│  │  └─ ParticipantController.java
│  ├─ Models/
│  │  ├─ Participant.java
│  │  ├─ ParticipantRepository.java
│  │  ├─ PersonalityClassifier.java
│  │  ├─ PersonalityType.java
│  │  ├─ GameType.java
│  │  ├─ RoleType.java
│  │  ├─ Team.java
│  │  ├─ TeamBuilder.java
│  │  ├─ TeamFormationResult.java
│  │  ├─ TeamFormationThread.java
│  │  └─ SaveParticipantThread.java
│  └─ ValidatorHelp/
│     ├─ ConsoleInput.java
│     └─ FileProcessingException.java
├─ participants_sample.csv      # Example participant data
├─ copy.csv                     # Another sample CSV
└─ README.md
```
