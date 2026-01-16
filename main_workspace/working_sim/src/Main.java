import java.util.Scanner;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Main extends Application {
	// Pane
	private static GridPane startScreen;
	
	// Scene
	private static Scene scene;
	
	//
	private static ListView<String> scenariosList;
	private static ChoiceBox<String> configChoices;
	
	// Stage
	private static Stage pStage;

	
	// ScenarioFileManager
	private static ScenarioFileManager sManager; 
	private static AirportConfigFileManager configManager;
	
	// Scenario
	public void init() {
		sManager = new ScenarioFileManager();
		
		//box for title
		startScreen = new GridPane();
		
		//startScreen.getChildren().g
		
		// create an observable list of all the options and scenarios for list view.
		ObservableList<String> list = FXCollections.observableList(sManager.getAllScenarios());
		
		// set the on click for each item.
		scenariosList = new ListView<String>(list);
		scenariosList.setOnMouseClicked((MouseEvent e) -> {
			// not double click, get out
			if (e.getClickCount() < 2) 
				return;
			
			// initialize boards
			Board testBoard = null;
			
			// get the name of the clicked sceranio
			String selectedName = scenariosList.getSelectionModel().getSelectedItem();
			// choose between options
			
			// existing sceranios
			testBoard = sManager.loadScenario(selectedName);
			// set up travel again (if there is) because the airports data is not saved
			testBoard.setUpTravel();
			
			GridPane simulationScreen = startNewSimulation(testBoard);
			scene.setRoot(simulationScreen);
		});
	
		// choices for configurations
		configManager = new AirportConfigFileManager();
		configChoices = new ChoiceBox<String>();
		configChoices.getItems().addAll(configManager.getAllConfigFiles());
		
		Button airportSetting = new Button("Airport Settings");
		airportSetting.setOnAction((ActionEvent e) -> {
			Stage settings = new Stage();
			settings.initModality(Modality.APPLICATION_MODAL);
			settings.initOwner(pStage);
			settings.setOnCloseRequest((WindowEvent event) -> {
				returnToStartScreen();
			}); // end settings.setOnCloseRequest
			
			// the frame of the scene
			AirportSetting as = new AirportSetting();
			
			settings.setScene(as.getScene());
			settings.show();
		}); // end setOnAction
		
		// Buttons for initialization
		
		Button sqButton = new Button("Add new square");
		Button sButton = new Button("Add new simulation");
		Button saButton = new Button("Add new simulation with airports");
		
		sqButton.setOnAction((ActionEvent e) ->  {
			Board testBoard = new Board(false, false, getConfigChoice());
			GridPane simulationScreen = startNewSimulation(testBoard);
			scene.setRoot(simulationScreen);
		});
		
		sButton.setOnAction((ActionEvent e) ->  {
			Board testBoard = new Board(true, false, getConfigChoice());
			GridPane simulationScreen = startNewSimulation(testBoard);
			scene.setRoot(simulationScreen);
		});
		
		saButton.setOnAction((ActionEvent e) ->  {
			Board testBoard = new Board(true, true, getConfigChoice());
			GridPane simulationScreen = startNewSimulation(testBoard);
			scene.setRoot(simulationScreen);
		});
		
		// delete scenario button
		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			// check if an option or scenario has been selected
			String selectedName = scenariosList.getSelectionModel().getSelectedItem();
			if (selectedName == null)
				return;
			
			// delete, and check if it has not been deleted
			System.out.println(selectedName);
			if (sManager.deleteScenario(selectedName)) 
				System.out.println("Successfully delete " + selectedName);
			else
				System.out.println("Failed to delete " + selectedName);
			
			// remove the item from the ListView list.
			ObservableList<String> l = FXCollections.observableList(sManager.getAllScenarios());
			scenariosList.setItems(l);
			
		});
		
		// add the components into the pane
		startScreen.addColumn(1, deleteButton);
		startScreen.addColumn(1, configChoices);
		startScreen.addColumn(0, new Text("Scenarios"));
		startScreen.addColumn(0, scenariosList);	
		startScreen.addColumn(0, sqButton);
		startScreen.addColumn(0, sButton);
		startScreen.addColumn(0, saButton);
		startScreen.addColumn(0, airportSetting);	
	} // end init
	
	@Override
	public void start(Stage primaryStage) {
		// scene
		pStage = primaryStage;
		
		//Scene scene = new Scene(gridPane);
		scene = new Scene(startScreen);
		primaryStage.setTitle("Pandemic Simulation");
		primaryStage.setScene(scene);
		primaryStage.show();
	} // end start
	
	// start a simulation screen based on the board object
	private GridPane startNewSimulation(Board testBoard) {
		
		
		// check if the parameter is a test board object
		if (testBoard == null) {
			System.err.println("No test board initialization");
			return null;
		} // end if
		DecimalFormat percentFormat = new DecimalFormat("#.##");
		DecimalFormat numFormat = new DecimalFormat("#");	
		numFormat.setGroupingUsed(true);
		numFormat.setGroupingSize(3);
	
		// store the size of the Board
		int nrows = testBoard.getBoardRows();
		int ncols = testBoard.getBoardColumns();
        
        // GridPane
  		GridPane gridPane = new GridPane();
  		// set up the grid pane
		gridPane.setHgap(0.25); //The size of the horizontal gap between squares
  		gridPane.setVgap(0.25); //The size of the horizontal gap between squares
  		gridPane.setStyle("-fx-background-color: WHITE;"); //The color of the background
  		
  		// initialize the canvas in InfectedColor
  		BoardUI.initialize(nrows, ncols);

// Info Box
		// Data representation

  		VBox infoBox = new VBox();
		Text show_alive = new Text("Number of people alive: "+ numFormat.format(testBoard.getTotalAlive())  );
		show_alive.setFill(Color.WHITE);
		show_alive.setFont(Font.font("Verdana",25));
		
		Text show_healthy = new Text("Number of healthy people:  "+ numFormat.format(testBoard.getTotalHealthy()) + "(" + percentFormat.format((testBoard.getTotalHealthy()/testBoard.getTotalStartPop())*100) +"%)");
		show_healthy.setFill(Color.AQUA);
		show_healthy.setFont(Font.font("Verdana",25));
		
		Text show_inf = new Text("Number of people infected: "+ numFormat.format(testBoard.getTotalInfected()) + "(" + percentFormat.format((testBoard.getTotalInfected()/testBoard.getTotalStartPop())*100) +"%)");
		show_inf.setFill(Color.RED);
		show_inf.setFont(Font.font("Verdana",25));
		
		Text show_dead = new Text("Number of dead people:  "+ numFormat.format(testBoard.getTotalDeath()) + "(" + percentFormat.format((testBoard.getTotalDeath()/testBoard.getTotalStartPop())*100) +"%)");
		show_dead.setFill(Color.BLACK);
		show_dead.setFont(Font.font("Verdana",25));
		
		Text show_rec = new Text("Number of recovered people:  " + numFormat.format(testBoard.getTotalRecovered()) + "(" + percentFormat.format((testBoard.getTotalRecovered()/testBoard.getTotalStartPop())*100) +"%)");
		show_rec.setFill(Color.PURPLE);
		show_rec.setFont(Font.font("Verdana",25));
		
		Text show_iter = new Text("Iteration: "+ testBoard.getIteration());
		show_iter.setFont(Font.font("Verdana",15));
		infoBox.getChildren().addAll(show_alive, show_healthy, show_inf, show_dead, show_rec, show_iter);
		
// Mode buttons
		// button to determine if it is in the set up or not 
		HBox buttonBox = new HBox();

		ToggleButton setButton = new ToggleButton("Set up");
		setButton.setMinSize(75, 75);
		
		// The button to start the simulation button
		ToggleButton startButton = new ToggleButton("Start");
		startButton.setMinSize(75, 75);
		
		// save buttons
		Button saveButton = new Button("Save");
		saveButton.setMinSize(75, 75);
		
		buttonBox.getChildren().addAll(startButton, setButton, saveButton);
		
		// TextField for the amount to set a point in the program
		HBox setAmountFieldBox = new HBox();
		setAmountFieldBox.setSpacing(5);
		Text setAmountFieldTitle = new Text("Infection Amount Set");
		TextField setAmountField = new TextField("50");
		setAmountField.setMaxWidth(150);
		setAmountFieldBox.getChildren().addAll(setAmountFieldTitle, setAmountField);
		
// Virus attributes
		// the parent pane holding virus attributes
		GridPane virusAttributesBox = new GridPane();
		
		Button updateVirusButton = new Button("Update Attributes");
		
		// TestFiled for the amount to set a infectionRate in the program
		Text setInfectionRateTitle = new Text("Infection Rate");
  		TextField setInfectionRate = new TextField(Double.toString(testBoard.virus.getInfectRate()));
  		setInfectionRate.setMaxWidth(150);
  	
  		// Text field for the ammount that will set the death rate
		Text setDeathRateTitle = new Text("Death Rate");
  		TextField setDeathRate = new TextField(Double.toString(testBoard.virus.getDeathRate()));
  		setDeathRate.setMaxWidth(150);
  		
  		// Text field that will set the recover rate
		Text setRecoverRateTitle = new Text("Recover Rate");
  		TextField setRecoverRate = new TextField(Double.toString(testBoard.virus.getRecoverRate()));
  		setRecoverRate.setMaxWidth(150);
  		
  		// Text field that will set the short travel rate
  		Text setShortTravelRateTitle = new Text("Short Travel Rate");
  	  	TextField setShortTravelRate = new TextField(Double.toString(testBoard.virus.getShortTravelRate()));
  	  	setShortTravelRate.setMaxWidth(150);
  	  	
  	  	// Text field that will set the long travel rate
  	  	Text setLongTravelRateTitle = new Text("Long Travel Rate");
	  	TextField setLongTravelRate = new TextField(Double.toString(testBoard.virus.getLongTravelRate()));
	  	setLongTravelRate.setMaxWidth(150);

  		// Text field that will set the kill time.
  		Text setKillingTimeTitle = new Text("Kill Time");
  		TextField setKillingTime = new TextField(Integer.toString(testBoard.virus.getKillTime()));
  		setKillingTime.setMaxWidth(150);
  		
  		// text field that twill set the recover time.
  		Text setRecoveringTimeTitle = new Text("Recover Time");
  		TextField setRecoveringTime = new TextField(Integer.toString(testBoard.virus.getRecoverTime()));
  		setRecoveringTime.setMaxWidth(150);
  		
  		// add the title and the virus attributes components in the box.
		virusAttributesBox.addColumn(0, setInfectionRateTitle, setDeathRateTitle, setKillingTimeTitle,  
				setRecoverRateTitle, setRecoveringTimeTitle, setShortTravelRateTitle, setLongTravelRateTitle);
		virusAttributesBox.addColumn(1, setInfectionRate, setDeathRate, setKillingTime, 
				setRecoverRate, setRecoveringTime, setShortTravelRate, setLongTravelRate);
  		
// General Setting Box		
		VBox settingBox = new VBox();
		settingBox.getChildren().addAll(buttonBox, setAmountFieldBox, updateVirusButton, virusAttributesBox);
		
		VBox simulationBox = new VBox();
		simulationBox.getChildren().addAll(infoBox, BoardUI.getInstance().canvas);
		
// Set listener for set infection amoutn and virus attributes
		 /* Inspired by Evan Knowlesand Nicolas Filotto 
		 * Links: https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
		 */
		setAmountField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
				// if the new value is not a number (denoted by the regular expression \d+
				// set it to 0
				if (newValue.matches("\\d+")) 
					return;
				setAmountField.setText("0");
		}); // end changed
			
		// set Infection Rate
		setInfectionRate.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {			
			// if the value matches, exit
			if (newValue.matches("^$|1|0|(0\\.)|0\\.\\d+")) 
				return;
			
			// if the number entered is not a float from 0 to 1, set back to old value
			setInfectionRate.setText(oldValue);
		}); // end changed
		
		// set Death rate
		setDeathRate.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			// if the value matches, exit
			if (newValue.matches("^$|1|0|(0\\.)|0\\.\\d+")) 
				return;
			
			// if the number entered is not a float from 0 to 1, set back to old value
			setDeathRate.setText(oldValue);
		}); // end changed
		
		//set Recover Rate
		setRecoverRate.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			// if the value matches, exit
			if (newValue.matches("^$|1|0|(0\\.)|0\\.\\d+")) 
				return;
			
			// if the number entered is not a float from 0 to 1, set back to old value
			setRecoverRate.setText(oldValue);
		}); // end changed
		
		//set Short Travel Rate
		setShortTravelRate.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			// if the value matches, exit
			if (newValue.matches("^$|1|0|(0\\.)|0\\.\\d+")) 
				return;
			
			// if the number entered is not a float from 0 to 1, set back to old value
			setShortTravelRate.setText(oldValue);
		}); // end changed
		
		//set Long Travel Rate
		setLongTravelRate.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			// if the value matches, exit
			if (newValue.matches("^$|1|0|(0\\.)|0\\.\\d+")) 
				return;
			
			// if the number entered is not a float from 0 to 1, set back to old value
			setLongTravelRate.setText(oldValue);
		}); // end changed
		
		
		//set Kill time
		setKillingTime.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			// if the new value is not a positive number (denoted by the regular expression \d+)
			// set it to 0
			if (newValue.matches("\\d+")) 
				return;
			
			setKillingTime.setText("0");
		}); // end changed
		
		//set Recover Time
		setRecoveringTime.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			// if the new value is not a positive number (denoted by the regular expression \d+)
			// set it to 0
			if (newValue.matches("\\d+")) 
				return;
			
			setRecoveringTime.setText("0");
		}); // end changed
		
	
// set up the canvas
		for (int x = 0; x < nrows; x++) {
			for (int y = 0; y < ncols; y++) {
				Location loc = testBoard.getLocation(x, y);
				if(!testBoard.isLand(x, y)) {
					BoardUI.drawPos(x, y, BoardUI.WATER);
					continue;
				} // end if
				
				BoardUI.setInfectedColor(testBoard.getLocation(x, y), false);
			} // end for x
		} // end for y, 
		
		// how we can set the infected location and other virus attributes

		BoardUI.getInstance().canvas.setOnMouseClicked((MouseEvent event) -> {
				
				if (startButton.isSelected() 
					|| !setButton.isSelected()
					|| event.getClickCount() < 2 
					|| event.getButton() != MouseButton.PRIMARY)
					return;
				
				// because the event.getY give us the location on the screen
				// we have to get the location using by dividing it by the scale 
				// bceause its location on the screen is calculated by the row and pos
				// times the scale
				testBoard.putInfection((int) (event.getY() / BoardUI.scale), 

									   (int) (event.getX() / BoardUI.scale), 
									   Double.parseDouble(setAmountField.getText()));			

		}); // end setOnMouseClicked

// Button onMouseClicked
		// the Status of the simulation
		class Status {
			boolean simulating = false;
			// for graphing
			int i = 0;
		} // end Status
		Status status = new Status();
		
		Stage chartStage = new Stage();
		Stage deadStage = new Stage();
		Stage rStage = new Stage();
		Stage pieStage = new Stage();
		
		// set the mouse click event for start button
		startButton.setOnMouseClicked((MouseEvent event) -> {
				// simulation thread
				Thread simThread;
				
				// when start
				if (startButton.isSelected()) {
					// initialize simulation thread


					simThread = new Thread(() -> {
						while (status.simulating) {
							// do iter
							testBoard.do_iter();;
							// update info
							show_alive.setText("Number of people alive: " + numFormat.format(testBoard.getTotalAlive()));
							show_healthy.setText("Number of healthy people: "+ numFormat.format(testBoard.getTotalHealthy()) + "(" + percentFormat.format((testBoard.getTotalHealthy()/testBoard.getTotalStartPop())*100) +"%)");
							show_inf.setText("Number of people infected: "+ numFormat.format(testBoard.getTotalInfected()) + "(" + percentFormat.format((testBoard.getTotalInfected()/testBoard.getTotalStartPop())*100) +"%)");
							show_dead.setText("Number of people dead: " + numFormat.format(testBoard.getTotalDeath()) + "(" + percentFormat.format((testBoard.getTotalDeath()/testBoard.getTotalStartPop())*100) +"%)");
							show_rec.setText("Number of people recovered: " + numFormat.format(testBoard.getTotalRecovered()) + "(" + percentFormat.format((testBoard.getTotalRecovered()/testBoard.getTotalStartPop())*100) +"%)");
							show_iter.setText("Iteration: "+ testBoard.getIteration());
							//Store data for charts
							GraphsMaker.healthy = testBoard.getTotalHealthy() + testBoard.getTotalRecovered();;
							GraphsMaker.dead = testBoard.getTotalDeath();
							GraphsMaker.infected = testBoard.getTotalInfected();
							if(testBoard.getIteration() % 20 == 0) {
								GraphsMaker.itermult[status.i] = testBoard.getIteration();
								GraphsMaker.infectedOniteration[status.i] = testBoard.getTotalInfected();
								GraphsMaker.deadOniteration[status.i] = testBoard.getTotalDeath();
								GraphsMaker.recoveredOniteration[status.i] = testBoard.getTotalRecovered();
								status.i++;
							}//storing data for charts
          
							// wait
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								return;
							} // end catch
						} // end while
					}); // end setOnMouseClicked

					
					// start simulating
					status.simulating = true;
					
					// set this so that this thread will also be closed when javafx thread is closed
					simThread.setDaemon(true); 
					
					// start running the simulation
					simThread.start();
					
					// change the text
					startButton.setText("Pause");
					
					// hide the chart
					chartStage.hide();
					deadStage.hide();
					rStage.hide();
					pieStage.hide();
				} // end if
				else {
					// set this to end the simulation
					status.simulating = false;
					
					// change the text
					startButton.setText("Continue");
					
					//creates line chart infected
					GraphsMaker.init(chartStage);
					GraphsMaker.CreateDeadChart(deadStage);
					GraphsMaker.CreateRecoveredChart(rStage);
					GraphsMaker.CreatePieChart(pieStage);					
				} // end else
		}); // end button.setonActio
		
		updateVirusButton.setOnAction((ActionEvent e) -> {
			// if the simulation is running or the set button is not set, do not update
			if (startButton.isSelected() || !setButton.isSelected())
				return;
			
			// knowing that the values in the virus attributes field will be of the right form, we can expect them to get right value.
			// this class will return the right value from the input
			class InputGetter {
				public double getDouble(String s) {
					// if s is empty or s is 0. -> the value is 0
					if (s.matches("^$|0\\."))
							return 0;
					
					return Double.parseDouble(s);
				} // end getInput
				
				public int getInt(String s) {
					return Integer.parseInt(s);
				} // end getInt
			} // end InputChecker
			
			InputGetter ig = new InputGetter();
			System.out.println("Set value");
			// set rates
			testBoard.virus.setInfectRate(ig.getDouble(setInfectionRate.getText()));
			testBoard.virus.setDeathRate(ig.getDouble(setDeathRate.getText()));
			testBoard.virus.setRecoverRate(ig.getDouble(setRecoverRate.getText()));
			testBoard.virus.setShortTravelRate(ig.getDouble(setShortTravelRate.getText()));
			testBoard.virus.setLongTravelRate(ig.getDouble(setLongTravelRate.getText()));
			
			// set time
			testBoard.virus.setKillTime(ig.getInt(setKillingTime.getText()));
			testBoard.virus.setRecoverTime(ig.getInt(setRecoveringTime.getText()));
		}); // end setOnAction
		
		// gui components for saving the scenarios box.
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL); // other stages will not be interactive.
        dialog.initOwner(pStage);
        
        // field for entering the name of the scenario saved.
        final String DEFAULT_FILE_NAME = "scenario";
        TextField fileName = new TextField(DEFAULT_FILE_NAME);
        fileName.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
				// if the value is an invalid file name
				// Taken from https://superuser.com/questions/358855/what-characters-are-safe-in-cross-platform-file-names-for-linux-windows-and-os
				// stack exchange
				// Windows: Not \/:*?"<>|
				// Linux/Mac: Not empty or /
				if (!newValue.matches("[^\\/:*?\"<>|]*"))
					Platform.runLater(() -> fileName.setText(""));
        });
        
// Pop up window for saveButton
        // save file button to confirm the file name entered
        Button saveFileButton = new Button("Save File");
        saveFileButton.setOnAction((ActionEvent e) -> {
        	if (sManager.saveScenario(fileName.getText(), testBoard))
        		System.out.println("Saved file sucessfully");
        	else
        		System.out.println("Unsuccessful Save");
        });
        
        // One column layout is preferred
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().addAll(new Text("Enter your file name"), fileName, saveFileButton);
        
        // set the new scene for the stage
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        
        // the save button to turn on the save option.
		saveButton.setOnAction((ActionEvent event) -> {
			// make sure that the simulation is not running before saving
			if (startButton.isSelected())
        		return;
			
	        dialog.show();
		}); // end setOnAction

// return button
		//Add show_inf node to gridplane at last colum on first row 
		//Add show_inf node and show_iter node to gridplane at last colum on first and second row
		//Add the displaying number before the map is made
		Button returnButton = new Button("Return");
		returnButton.setOnAction((ActionEvent e) -> {
			// stop the simulation thread
			status.simulating = false;
			
			chartStage.hide();
			deadStage.hide();
			rStage.hide();
			pieStage.hide();
			
			ObservableList<String> list = FXCollections.observableList(sManager.getAllScenarios());
			// set the new list of scenarios. 
			scenariosList.setItems(list);
			
			// set the scene to the startScreen Pane.
			scene.setRoot(startScreen);
			
			GraphsMaker.resetGraphs();
			
		}); // end setOnAction
		
		gridPane.addRow(0, returnButton);
		gridPane.addRow(1, simulationBox);
		gridPane.addRow(1, settingBox);

		
		return gridPane;
	} // start new simulation
	
	public static void returnToStartScreen() {
		ObservableList<String> list = FXCollections.observableList(sManager.getAllScenarios());
		// set the new list of scenarios. 
		scenariosList.setItems(list);
		
		// set the new list of configs
		configManager.updateConfigList();
		configChoices.getItems().setAll(configManager.getAllConfigFiles());
		
		// set the scene to the startScreen Pane.
		scene.setRoot(startScreen);
	} // end returnToStartScreen
	
	// 
	private static String getConfigChoice() {
		if (configChoices == null || configChoices.getSelectionModel().getSelectedIndex() == -1)
			return "config";
		
		return configChoices.getSelectionModel().getSelectedItem();
	} // end getConfigChoice
	
	public static void main(String[] args) throws Exception {
        Main.launch(args);
	} // end main
} // end Main