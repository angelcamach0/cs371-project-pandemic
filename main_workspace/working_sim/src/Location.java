import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.util.*;

public class Location {
	public int land = 0;
	public double pop = 0;
	public double alive = 0;
	public double healthy = 0;
	public double inf = 0;
	public double iters_infected = 0;
	public double rip = 0;
	public double xxx = 0;
	public double rec = 0;
	public int row = 0;
	public int col = 0;
	public boolean hasAirport;
	// transient means the data of this field will not be included in the file save.
	public transient ArrayList<Location> canFlyTo;
	public int cityLevel;
	public transient Color color;
	
	public Location(int r, int c) {
		row = r;
		col = c;
	} // end Location
	

} // end Location
