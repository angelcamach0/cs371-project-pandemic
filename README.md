# cs371-project-pandemic
JavaFX-based pandemic simulation with configurable scenarios, travel routes, and charts.

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
- Java (JDK)
- JavaFX SDK
- Gson (included in `external_libs/`)

## Build and run (Linux/macOS)
Define these paths first:

- `PATH_TO_FX_LIB` - path to the `lib/` folder of your JavaFX SDK (example: `/Users/you/Library/javafx-sdk-11.0.2/lib`)
- `PATH_TO_GSON_JAR` - path to `gson-2.8.6.jar` (found in `main_workspace/working_sim/external_libs`)

### Command line
From `main_workspace/working_sim`:

```bash
javac --module-path PATH_TO_FX_LIB \
  --add-modules javafx.controls,javafx.graphics,javafx.base \
  -cp external_libs/gson-2.8.6.jar src/*.java -d ./out/

jar cvfm PandemicSim.jar MANIFEST.MF -C ./out/ .

java --module-path PATH_TO_FX_LIB \
  --add-modules javafx.controls,javafx.graphics,javafx.base \
  -jar PandemicSim.jar
```

### Ant
Edit `main_workspace/working_sim/build.xml` and update:

```xml
<property name="fx.dir" value="PATH_TO_FX_LIB"/>
<property name="gson.dir" value="PATH_TO_GSON_JAR"/>
```

Then from `main_workspace/working_sim`:

```bash
ant
ant run
```

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


  
  
  
