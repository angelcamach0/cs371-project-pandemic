import javafx.scene.Scene;

public interface Returnable {
	
	/**
	 * Return to the original (main pane) of this scene.
	 */
	public void returnToMain();
	
	/**
	 * Return the scene
	 * @return
	 */
	public Scene getScene();
}
