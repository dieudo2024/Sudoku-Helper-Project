# Sudoku Helper

Professional JavaFX Sudoku assistant that helps analyze and solve 9x9 Sudoku puzzles.

This project provides:

- A JavaFX user interface for entering and inspecting Sudoku puzzles.
- A backtracking solver that can solve valid puzzles.
- Candidate analysis that shows possible digits for empty cells.
- Utilities for importing puzzles from text/CSV files and validating solutions.

## Requirements

- Java 17+ (project was developed against Java 17 runtime and builds with newer JDKs)
- Maven
- JavaFX 17 (libraries are declared in the project's Maven configuration)

Note: When running with newer JDKs you may see warnings about native access. For a clean runtime add the following JVM option when necessary:

```
--enable-native-access=javafx.graphics
```

## Build

From the project root, build with Maven:

```bash
mvn clean package
```

## Run

Two common ways to run the application:

- Using the packaged module (recommended when modules are configured):

```bash
mvn -q package
java -m com.example.sudokuhelper/com.example.sudokuhelper.SudokuApplication
```

- Or run via your IDE (run `com.example.sudokuhelper.SudokuApplication`).

If you encounter JavaFX native access warnings on newer JDKs, add `--enable-native-access=javafx.graphics` to the Java command as shown in the Requirements section.

## Tests

Run unit tests with:

```bash
mvn test
```

## Project Structure

- `src/main/java` — application sources; key packages:
	- `com.example.sudokuhelper.Controller` — JavaFX controller
	- `com.example.sudokuhelper.Model` — solver and model utilities
- `src/main/resources` — FXML and view resources

## Contributing

Fork, create a feature branch, add tests for new behavior, and open a pull request. Keep UI responsibilities in the `Controller` package and game logic in `Model`.

## License

Coming soon!

---

If you want, I can also add a `USAGE.md` with screenshots and a short tutorial, or adjust the run instructions for your specific development environment.

## Run in your IDE (IntelliJ IDEA and VS Code)

These quick steps show the usual, button-driven workflow in popular IDEs.

- IntelliJ IDEA

	1. Open the project as a Maven project (File → Open, select project root).
	2. Ensure the project's SDK is set to a compatible JDK (17+).
	3. In the Project view open `src/main/java/com/example/sudokuhelper/SudokuApplication.java`.
	4. Use the green Run gutter icon or right-click the `main` method and choose Run to launch the application.
	5. If you see JavaFX native access warnings, edit the Run Configuration and add the VM option `--enable-native-access=javafx.graphics`.

- Visual Studio Code

	1. Install the Java Extension Pack (includes Run/Debug support).
	2. Open the project folder (it will be recognized as a Maven/Java project).
	3. Open `SudokuApplication.java` and click the Run CodeLens link or the green Run button in the editor gutter above the `main` method.
	4. Alternatively use the Run view (left sidebar) and click the play button for the `SudokuApplication` entry.
	5. Add the same VM option (`--enable-native-access=javafx.graphics`) in your launch configuration if needed.

These steps let you launch using the IDE run/play buttons without manual command lines. If you want, I can add example `launch.json` and IntelliJ Run Configuration notes for exact VM/module settings.
