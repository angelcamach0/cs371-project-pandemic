# cs371-project-pandemic
JavaFX-based pandemic simulation with configurable scenarios, travel routes, and charts.
Made during the covid 19 pandemic. for software development class NMSU.
## Purpose and technical scope
This project demonstrates:
- A discrete-time simulation engine (infection spread, recovery, death).
- Data-driven scenarios and configurations (Gson JSON + config files).
- Interactive JavaFX UI with charts and live state updates.
- Separation of model, view, and control logic across focused classes.

## Project layout
The simulation lives in `main_workspace/working_sim`:

- `src/` - Java source
  - `Main.java` - application entry point and UI wiring
  - `Board.java` - simulation state and iteration logic
  - `Location.java` - grid cell model
  - `BoardUI.java` - color mapping and rendering
  - `Virus.java` - virus parameters
  - `ScenarioFileManager.java` - save/load scenarios
  - `GraphsMaker.java` - chart data and chart windows
  - `Returnable.java` - interface to return between panes
  - `AirportSetting.java` - airport settings UI
  - `AirportConfigFileManager.java` - airport config CRUD
- `config_files/` - airport configuration data
  - `world_airports.txt` - global airports data
- `external_libs/` - third-party jars
  - `gson-2.8.6.jar`
- `scenario_files/` - saved scenarios
  - `high_fatalities.json`

## Requirements
- Java JDK 11+ (JDK 17 works)
- JavaFX SDK (OpenJFX)
- Gson (included in `main_workspace/working_sim/external_libs/`)

## Download and run (all platforms)
1) Clone the repo:
```bash
git clone https://github.com/angelcamach0/cs371-project-pandemic.git
cd cs371-project-pandemic
```

2) Go to the simulation folder:
```bash
cd main_workspace/working_sim
```

3) Build and run using the platform-specific guide below.

## Run on Linux
Install Java and JavaFX:
```bash
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk openjfx
```

Then build and run:
```bash
JFX_LIB=/usr/share/openjfx/lib

javac --module-path "$JFX_LIB" \
  --add-modules javafx.controls,javafx.graphics,javafx.base \
  -cp external_libs/gson-2.8.6.jar src/*.java -d ./out/

jar cvfm PandemicSim.jar MANIFEST.MF -C ./out/ .

java --module-path "$JFX_LIB" \
  --add-modules javafx.controls,javafx.graphics,javafx.base \
  -jar PandemicSim.jar
```

## Run on Windows
Install:
- JDK 11+ (Temurin/Adoptium recommended)
- JavaFX SDK from https://openjfx.io/

Set `JFX_LIB` to your JavaFX `lib` folder (example: `C:\javafx-sdk-17.0.10\lib`), then in PowerShell:
```powershell
$env:JFX_LIB="C:\javafx-sdk-17.0.10\lib"

javac --module-path "$env:JFX_LIB" `
  --add-modules javafx.controls,javafx.graphics,javafx.base \
  -cp external_libs/gson-2.8.6.jar src/*.java -d ./out/

jar cvfm PandemicSim.jar MANIFEST.MF -C ./out/ .

java --module-path "$env:JFX_LIB" `
  --add-modules javafx.controls,javafx.graphics,javafx.base \
  -jar PandemicSim.jar
```

## Run on macOS
Install:
- JDK 11+ (Temurin/Adoptium recommended)
- JavaFX SDK from https://openjfx.io/ or via Homebrew:
```bash
brew install openjfx
```

If installed via SDK, set `JFX_LIB` to the SDK `lib` folder. If installed via Homebrew, use:
```bash
JFX_LIB=$(brew --prefix openjfx)/lib
```

Then build and run:
```bash
javac --module-path "$JFX_LIB" \
  --add-modules javafx.controls,javafx.graphics,javafx.base \
  -cp external_libs/gson-2.8.6.jar src/*.java -d ./out/

jar cvfm PandemicSim.jar MANIFEST.MF -C ./out/ .

java --module-path "$JFX_LIB" \
  --add-modules javafx.controls,javafx.graphics,javafx.base \
  -jar PandemicSim.jar
```

## IDE setup (optional)
### Eclipse
Import the project, add JavaFX and Gson to the build path, then set VM args:

```
--module-path "PATH_TO_FX_LIB" --add-modules javafx.controls,javafx.fxml,javafx.graphics
```

## How to use
- Scenario window:
  - Pick a preset scenario, or create a new simulation (basic, no airports, or airports).
  - Use "Airport Settings" to choose or create an airport config (defaults to `config.txt` if no selection).
- Simulation window:
  - Click "Set up" to edit virus parameters and initial infections.
  - Click "Start" to run, "Pause" to open charts, and "Continue" to resume.


  
  
  
