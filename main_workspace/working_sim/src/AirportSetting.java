import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AirportSetting implements Returnable {
	// the list of configs
	private ListView<String> configList;
	
	// delete button
	private Button dButton;
	
	// add button
	private Button aButton;
	
	// Pane
	private VBox frame;
	
	// my scene
	private Scene myScene;
	
	/**
	 * Initialize important components for AirportSetting
	 */
	public AirportSetting() {
		frame = new VBox();
		myScene = new Scene(frame);
		
		AirportConfigFileManager configManager = new AirportConfigFileManager();
		// the list of configs
		configList = new ListView<String>();
		configList.getItems().setAll(configManager.getAllConfigFiles());
		
		// delete button
		dButton = new Button("Delete");
		dButton.setOnAction((ActionEvent action) -> {
			int idx = configList.getSelectionModel().getSelectedIndex();
			if (idx != -1 && configManager.deleteConfig(configList.getItems().get(idx)))
				configList.getItems().remove(idx);
		}); // end setOnAction
		
		// add button
		aButton = new Button("Add new config");
		aButton.setOnAction((ActionEvent action) -> {
			myScene.setRoot(AirportConfigFileManager.createSetUpAirportPane(this));
		});
		
		// add the components in
		frame.getChildren().addAll(configList, dButton, aButton);
	} // end AirportSetting
	
	//
	public void returnToMain() {
		AirportConfigFileManager configManager = new AirportConfigFileManager();
		configList.getItems().setAll(configManager.getAllConfigFiles());
		myScene.setRoot(frame);
	} // end returnToSetting
	
	public Scene getScene() {
		return myScene;
	} // end getScene
}
