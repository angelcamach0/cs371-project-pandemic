# cs371-project-pandemic
The files which make up our simulation and provide supporting files, including the java files are found in main_workspace/working_sim/. In this folder we have the folders:

  src - Contains java files themselves, make up the program
  <ul>
    <li>Main.java - Where system runtime is executed using the main function. </li>
    <li>Board.java - Represents and facilitates the simulation itself, stores Locations, global information</li>
    <li>Location.java - Represents a single location, the smallest unit of space in the program</li>
    <li>BoardUI.java - Facilitates determining which colors should be displayed in GUI</li>
    <li>Virus.java - Holds information about the Virus itself, it's attributes such as infection rate, death time etc.</li>
    <li>ScenarioFileManager.java - manages the different scenarios </li>
    <li>lineChart.java - Driving functions for creating charts, stores arrays of values for the chart. </li>
    <li>Returnable.java -  java interface that connects airport settings</li>
    <li>AirportSetting.java -  Initialize important components for AirportSetting</li>
    <li>AirportConfigFileManager.java - sets up the config file with airport data</li>
  </ul>
  config_files - holds files necessary for reading in/modifying airport locations
  <ul>
    <li>worldAirports.txt - master airport config file, airports scattered across world with different sizes</li>
  </ul>
  external_libs - holds jar file(s) nesessary for managing scenarios
   - gson-2.8.6.jar
  
  scenario_files - set up different scenarios for the simulation using the gson external_lib
   - highfatalities.json


## Build and run the program on Linux or MacOs:
### Dependency
* Gson - Already in external_libs folder. It was downloaded from https://github.com/google/gson
* JavaFx
* Java

### Constants to use in the isntruction
<b>PATH-TO-THE-LIB-FOLDER-IN-JAVAFX-SDK-FOLDER</b>: The path to the lib/ folder of javafx sdk folder on your machine. For example, /Users/longtran/Library/javafx-sdk-11.0.2/lib.

<b>PATH-TO-GSON-JAR</b>: The path to the jar file of gson library. A copy of the gson jar file can be found in the main_workspace/working_sim/external_libs folder.

### Using Eclipse
You can just import the project in Eclipse, add in necessary librabries (javafx, gson), then add this vm argument in the run configuration: --module-path "PATH-TO-THE-LIB-FOLDER-IN-JAVAFX-SDK-FOLDER" --add-modules javafx.controls,javafx.fxml,javafx.graphics.  

### Using command line
##### 1. Go to main_workspace/working_sim folder in the terminal
   
##### 2. Compile .java files

    javac --module-path PATH-TO-THE-LIB-FOLDER-IN-JAVAFX-SDK-FOLDER --add-modules javafx.controls,javafx.graphics,javafx.base -cp external_libs/gson-2.8.6.jar src/*.java -d ./out/


##### 3. Make jar file

    jar cvfm PandemicSim.jar MANIFEST.MF -C ./out/ .

##### 4. Run the program using jar file

    java --module-path PATH-TO-THE-LIB-FOLDER-IN-JAVAFX-SDK-FOLDER --add-modules javafx.controls,javafx.graphics,javafx.base -jar PandemicSim.jar

### Using Ant
Before doing the steps below, you will have to change two lines in build.xml: 

    From: <property name="fx.dir" value="/Users/longtran/Library/javafx-sdk-11.0.2/lib"/>
  
    To:   <property name="fx.dir" value="PATH-TO-THE-LIB-FOLDER-IN-JAVAFX-SDK-FOLDER"/>
    
    From: <property name="gson.dir" value="/Users/longtran/Workspace/cs371-project-pandemic/main_workspace/working_sim/external_libs/gson-2.8.6.jar"/>
    
    To: <property name="gson.dir" value="PATH-TO-GSON-JAR"/>
    
##### 1. Go to main_workspace/working_sim folder in the terminal

##### 2. Build jar file
    ant

##### 3. Run jar file (program)
    ant run

## To operate the program:

- Scenario Window - Choose which type of scenario you want to run, either a preset one from "Scenarios", or make a custom one, either a basic square, simulation with no airports, or simulation with airport scenario. Also in this window you can select which airport config file you want to use with the Airport Settings button. config.txt is chosen by default and supports a comprehensive global airport set up. 

- Simulation Window -
  Set up - Click Set up, enter custom virus attributes, or use the default ones. Rates need to be between 0-1, and Times can be any positive integer. Click update attributes. Then to 
  Start/Pause/Continue Button - click the start button. The simulation should begin. The same button will switch to pause. Pause will stop the simulation where you are and display graphs. The same button will change to continue. This button will restart the simulation and hide the graph windows. 


  
  
  
