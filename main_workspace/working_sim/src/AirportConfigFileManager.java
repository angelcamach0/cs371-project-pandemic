import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AirportConfigFileManager {
	private static final String PATH_TO_CONFIG_FOLDER = "config_files/";
	private static final int LOWEST_LEVEL = 1;
	
	private LinkedList<String> configFiles;
	
	/**
	 * Create an object with the latest list of config
	 */
	public AirportConfigFileManager() {
		updateConfigList();
	} // end SetUp
	
	/**
	 * Return if a config with this name exist
	 * @param configName the name of the config
	 * @return true if the config exists, else false
	 */
	public boolean hasConfig(String configName) {
		if (configName == null) throw new IllegalArgumentException("Config name cannot be null");
		if (configName.isEmpty()) throw new IllegalArgumentException("Config name cannot be empty");
		
		updateConfigList();
		return configFiles.contains(configName);
	} // end hasConfig
	
	/**
	 * Return a list of airports with its information of a config
	 * @param configName the name of a config
	 * @return a list of airports with its information of a config.
	 */
	public LinkedList<String> loadConfig(String configName) {
		// error cases
		if (configName == null) throw new IllegalArgumentException("Config name cannot be null");
		if (configName.isEmpty()) throw new IllegalArgumentException("Config name cannot be empty");
		
		LinkedList<String> result = new LinkedList<String>();
		
		// check if the file exists
		if (!hasConfig(configName)) return result;
		
		// a config with that name exists
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(AirportConfigFileManager.createPathToConfigFile(configName));
			br = new BufferedReader(fr);
			
			for (String location = null; (location = br.readLine()) != null && !location.isEmpty();) {
				result.add(location);
			} // end for
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
					br.close();
				} catch (IOException e) {e.printStackTrace();}
			} // end if
		} // end finally
		
		// return the result
		return result;
	} // end loadConfig
	
	/**
	 * Delete a config file from the folder config_files
	 * @param configFileName the name of the config to delete
	 * @return the result of the deletion
	 */
	public boolean deleteConfig(String configName) {
		// error cases
		if (configName == null) throw new IllegalArgumentException("Config name cannot be null");
		if (configName.isEmpty()) throw new IllegalArgumentException("Config name cannot be empty");
		
		File file = new File(createPathToConfigFile(configName));
		
		// check if the file is a directory, as well as checking if it exists
		if (file.isDirectory())
			return false;
		
		// delete the config from the configFiles list and also check if there is such a file
		if (!configFiles.remove(configName))
			return false;
		
		// delete the actual file
		return file.delete();
	} // end deleteConfigFiles.
	
	/**
	 * Save the airport configuration as a file in the config_files folder.
	 * @param configName the name of the configuration
	 * @param cities the list of airports
	 * @return the result of the save.
	 */
	public boolean saveConfig(String configName, LinkedList<Location> cities) {
		// error cases
		if (configName == null) throw new IllegalArgumentException("Config name cannot be null");
		if (configName.isEmpty()) throw new IllegalArgumentException("Config name cannot be empty");
		if (cities == null) throw new IllegalArgumentException("the list of cities cannot be nulls");
		
		// overwrite any configs
		FileWriter fw = null;
		try {
			String content = "";
			// save the content of board as a .txt file
			for (Location city : cities) 
				content += city.row + " " + city.col + " " + (int) city.cityLevel + "\n";
			
			// write it in
			fw = new FileWriter(createPathToConfigFile(configName));
			fw.write(content);
			
			// add it into the scenarios list
			configFiles.remove(configName);
			configFiles.addFirst(configName);
			
			// successful write
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // end catch
		} // end finally
		
		return false;
	} // end saveConfig
	
	
	/**
	 * Return a list of the names of all files ended with .txt in config_files folder. 
	 * Because the config files has .txt extension, we can assume that those files will be config_files.
	 * @return a list of the names of all files ended with .txt
	 */
	private LinkedList<String> searchConfigs() {
		LinkedList<String> cf = new LinkedList<String>();
		File folder = new File(PATH_TO_CONFIG_FOLDER);
		
		// check if the path points to a folder.
		if (!folder.isDirectory())
			return cf;
		
		// searching for configFiles in that folder
		for (File config : folder.listFiles()) {
			// a folder, skip
			if (!config.isFile())
				continue;
			
			// check to see if it has .txt extension
			int extensionIdx = config.getName().lastIndexOf(".txt");
			if (extensionIdx == -1)
				continue;
			
			// add it into the table
			cf.add(config.getName().substring(0, extensionIdx));
		} // end scenario
		
		return cf;
	} // end searchConfigs
	
	
	/**
	 * Update the list of the names of config files in the config_files folder.
	 */
	public void updateConfigList() {
		configFiles = searchConfigs();
	}// end updateConfigList 
	
	/**
	 * Return the list of the names of config files in config_files folder.
	 * @return the list of the names of config files in config_files folder
	 */
	public LinkedList<String> getAllConfigFiles() {
		return configFiles;
	} // end getAllConfigFiles.
	
	/**
	 * Create a GUI GridPane object, that has the components, and the mechanism to choose airports 
	 * @return a GUI GridPane object
	 */
	public static GridPane createSetUpAirportPane(Returnable pane) {
		GridPane gridPane = new GridPane();
		// set up the grid pane
		gridPane.setHgap(0.25); //The size of the horizontal gap between squares
  		gridPane.setVgap(0.25); //The size of the horizontal gap between squares
  		gridPane.setStyle("-fx-background-color: white;"); //The color of the background
  		
		LinkedList<Location> cities = new LinkedList<Location>();
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("bw_world.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		} // end catch
		
		// set up a temporary Board, which we only use land attribute and cityLevel
        int nrows = image.getHeight();
        int ncols = image.getWidth();
        Location[][] locations = new Location[nrows][ncols];
        // initialize the canvas in InfectedColor
  		BoardUI.initialize(nrows, ncols); 
        for(int i = 0; i < nrows; i++) {
		    for(int j = 0; j < ncols; j++) {
		    	locations[i][j] = new Location(i, j);
		        int red = new Color(image.getRGB(j, i)).getRed();
		        if(red <= 100) {
			    	locations[i][j].land = 1;
			    	
			    	// cityLevel = -1 meaning this has not been added
			    	locations[i][j].cityLevel = -1;
			    	
			    	BoardUI.drawPos(i, j, BoardUI.LAND);
		        } // end if
		        else 
		        	BoardUI.drawPos(i, j, BoardUI.WATER);
		    } // end for j
		} // end for i
        
		// buttons
		// button to determine if it is in the set up or not 
		ToggleButton setButton = new ToggleButton("Set up");
		
		// TextField for the city level
		HBox cityLevelBox = new HBox();
		Text cityLevelFieldTitle = new Text("City Level");
		TextField cityLevelField = new TextField(Integer.toString(LOWEST_LEVEL));
		cityLevelField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
				// if the new value is not a number (denoted by the regular expression \d+
				// set it to 0
				if (!newValue.matches("[1-9]|^$|(10)")) {
					cityLevelField.setText(oldValue);
				} // end if
		}); // end setAmountField.textProperty().addListener()
		cityLevelBox.getChildren().addAll(cityLevelFieldTitle, cityLevelField);
		
		// TextField for fileName
		HBox fileNameBox = new HBox();
		Text fileNameTitle = new Text("Filename");
		TextField fileName = new TextField();
		fileName.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			// for file name only allow alphabet and numbers, and filename must start with alphabet.
			if (!newValue.matches("[a-zA-Z][a-zA-Z0-9]*|^$")) {
				fileName.setText(oldValue);
			} // end if
		}); // end addListener
		fileNameBox.getChildren().addAll(fileNameTitle, fileName);
		
		// how we can set the infected location
		BoardUI.getInstance().canvas.setOnMouseClicked((MouseEvent event) -> {
				// only set when the setButton is chosen
				if (!setButton.isSelected()) 
					return;
					
				// because the event.getY give us the location on the screen
				// we have to get the location using by dividing it by the scale 
				// because its location on the screen is calculated by the row and pos
				// times the scale
				int row = (int) (event.getY() / BoardUI.scale);
				int col  = (int) (event.getX() / BoardUI.scale); 
				
				if (locations[row][col].land != 1 || event.getClickCount() <= 1) 
					return;
				
				if (event.getButton() == MouseButton.PRIMARY) {
					BoardUI.drawPos(row, col, BoardUI.PINK);
					
					String level = cityLevelField.getText();
					if (level.isEmpty()) level = "1";
					
					if (locations[row][col].cityLevel == -1) {
						System.out.println("added a new airport");
						cities.add(locations[row][col]);
					} // end if
					
					locations[row][col].cityLevel = (level.isEmpty() ? LOWEST_LEVEL : Integer.parseInt(level));
					
				} // end if
				else if (locations[row][col].cityLevel != -1) {
					System.out.println("remove an airport");
					BoardUI.drawPos(row, col, BoardUI.LAND);
					cities.remove(locations[row][col]);
					locations[row][col].cityLevel = -1;
				} // end else
		}); // end setOnMouseClicked
		
		// The button to save airports
		Button saveButton = new Button("Save");
		saveButton.setOnMouseClicked((MouseEvent event) -> {
			AirportConfigFileManager su = new AirportConfigFileManager();
				su.saveConfig(fileName.getText().isEmpty() ? "config" : fileName.getText(), cities);
		}); // end saveButton Lambda
		
		// return button
		Button returnButton = new Button("Return");
		returnButton.setOnAction((ActionEvent e) -> {
			if (pane != null)
				pane.returnToMain();
		});
		
		//Add show_inf node to gridplane at last colum on first row 
		//Add show_inf node and show_iter node to gridplane at last colum on first and second row
		//Add the displaying number before the map is made
		gridPane.addColumn(0, returnButton);
		gridPane.addColumn(0, saveButton);
		gridPane.addColumn(0, setButton);
		gridPane.addColumn(0, cityLevelBox);
		gridPane.addColumn(0, fileNameBox);
		gridPane.addColumn(0, BoardUI.getInstance().canvas);	
		
		return gridPane;
	} // end createSetUpPane
	
	
	/**
	 * Create a path to a config file in the config folder.
	 * @param configFileName the name of a config file
	 * @return the path to a config file in the config folder.
	 */
	public static String createPathToConfigFile(String configFileName) {
		return PATH_TO_CONFIG_FOLDER  + configFileName + ".txt";
	} // end createPathToConfigFile
	
//	public static void main(String[] args) {
//		SetUp su = new SetUp();
//		Scanner input = new Scanner(System.in);
//		LinkedList<Location> cities = new LinkedList<Location>();
//		
//		System.out.println("Test setup");
//		for (int i = 1; true; i++) {
//			System.out.println("Do you want to set up, or set up more: 0 = no, other = yes ");
//			if (input.nextInt() == 0) break;
//			
//			Location loc = new Location(0, 0);
//			System.out.println("City number " + i);
//			
//			System.out.println("Enter row: ");
//			loc.row = input.nextInt();
//			
//			System.out.println("Enter column: ");
//			loc.col = input.nextInt();
//			
//			System.out.println("Enter city level: ");
//			loc.cityLevel = input.nextInt();
//			
//			cities.add(loc);
//		} // end for i
//		
//		
//		System.out.println("Test start");
//		while (true) {
//			System.out.println("Enter a number: ");
//			System.out.println("Options: ");
//			System.out.println("Option 0: exit");
//			System.out.println("Option 1: Save a predefined config");
//			System.out.println("Option 2: Delete a config");
//			System.out.println("Option 3: Show the list of configs");
//			System.out.println("Option 4: Load a config");
//			
//			
//			int option = input.nextInt();
//			if (option == 0) break;
//			else if (option == 1) {
//				System.out.println("Enter the name of the config: ");
//				su.saveConfig(input.next(), cities);
//			} // end else if
//			else if (option == 2) {
//				System.out.println("Enter the name of the config: ");
//				su.deleteConfig(input.next());
//			} // end else if
//			else if (option == 3) {
//				System.out.println("The name of config files: ");
//				for (String s : su.getAllConfigFiles())
//					System.out.println(s);
//				System.out.println();
//			} // end else if
//			else if (option == 4) {
//				System.out.println("Enter the name of the config: ");
//				for (String airport : su.loadConfig(input.next())) 
//					System.out.println(airport);
//				
//				System.out.println();
//			} // end else if
//		} // end while
//		
//		System.out.println("Test end");
//	} // end main
//
} // end SetUp
