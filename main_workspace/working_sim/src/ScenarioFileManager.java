import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import com.google.gson.Gson;

public class ScenarioFileManager {
	private LinkedList<String> scenarios;
	private final String PATH_TO_SCENARIOS = "scenario_files";
	
	public ScenarioFileManager() {
		updateScenarioList();
	}
	
	// load a scenario with a file name without .json extension
	public Board loadScenario(String scenarioName) {
		// check if that scenario exists
		if (!scenarios.contains(scenarioName))
			return null;
		
		FileReader fr = null;
		BufferedReader br = null;
		try {
			// open the file
			fr = new FileReader(PATH_TO_SCENARIOS + "/" + scenarioName + ".json");
			br = new BufferedReader(fr);
			
			// get the data
			String content = br.readLine();
			
			// interpret it
			Board board = new Gson().fromJson(content, Board.class);
			
			return board;
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	} 
	
	// save a scenario with a file name + .json extension
	public boolean saveScenario(String scenarioName, Board board) {
		// overwrite any scenarios
		// save the content
		FileWriter fw = null;
		try {
			String content = "";
			Gson gs = new Gson();
			
			// serialize the content of board to a json string
			content = gs.toJson(board);
			
			// write it in
			fw = new FileWriter(PATH_TO_SCENARIOS + "/" + scenarioName + ".json");
			fw.write(content);
			
			// add it into the scenarios list
			scenarios.remove(scenarioName);
			scenarios.addFirst(scenarioName);
			
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
	}
	
	// delete a sceranio
	public boolean deleteScenario(String scenarioName) {
		File file = new File(PATH_TO_SCENARIOS + "/" + scenarioName + ".json");
		
		// check if the file is a directory, as well as checking if it exists
		if (file.isDirectory())
			return false;
		
//		// delete the scenario from the scenarios list and also check there is such a file
		if (!scenarios.remove(scenarioName))
			return false;
		
		// delete the actual file
		return file.delete();
	}
	
	// search for all scenarios in the scenarios folder
	private LinkedList<String> searchScenario() {
		LinkedList<String> sc = new LinkedList<String>();
		File folder = new File(PATH_TO_SCENARIOS);
		
		// check if the path points to a folder.
		if (!folder.isDirectory())
			return sc;
		
		// searching for scenarios in that folder
		for (File scenario : folder.listFiles()) {
			// a folder, skip
			if (!scenario.isFile())
				continue;
			
			// check to see if it has .json extension
			int extensionIdx = scenario.getName().lastIndexOf(".json");
			if (extensionIdx == -1)
				continue;
			
			// add it into the table
			sc.add(scenario.getName().substring(0, extensionIdx));
		} // end scenario
		
		return sc;
	} // end searchScenario
	
	public void updateScenarioList() {
		scenarios = searchScenario();
	}
	
	public LinkedList<String> getAllScenarios() {
		return scenarios;
	}
	
} // end ScenarioFileManager
