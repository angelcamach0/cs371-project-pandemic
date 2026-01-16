import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class BoardUI  {
	
	
	public final static Color WATER = Color.BLUE;
	public final static Color LAND = Color.GREEN;
	
	public final static Color MAGENTA = Color.rgb(207,31,154);
	public final static Color PINK = Color.rgb(255,0,179);
	
	public final static Color B = Color.BLACK;
	public final static Color GRAY= Color.rgb(46,46,46);

	
	public final static Color first = Color.rgb(255,255,0);
	public final static Color second = Color.rgb(255,180,0);
	public final static Color third = Color.rgb(255,130,0);
	public final static Color fourth = Color.rgb(255,65,0);
	public final static Color fifth = Color.rgb(255,0,0);
	public final static Color sixth = Color.rgb(210,0,0);
	public final static Color seventh = Color.rgb(160,0,0);
	public final static Color eighth = Color.rgb(115,0,0);
	public final static Color ninth = Color.rgb(80,0,0);
	public final static Color tenth = Color.rgb(60,0,0);
	
	
	
	// the scale of the picture increase this to make it bigger
	public static double scale = 1.5;
	
	// the canvas to display the simulation process
	// this will be initialized in Main
	public Canvas canvas = null;
	
	private static BoardUI instance = null;
	
	private BoardUI() {
		
	} // end BoardUI
	
	private BoardUI(int row, int col) {
		canvas = new Canvas(col * instance.scale, 
				row * instance.scale);
	} // end BoardUI
		
	
	// set the infected color of a location
	public static void setInfectedColor(Location loc, boolean opt) {	
			//System.out.println("Here");
			if (loc != null) {
				double infected = loc.inf;
				double population = loc.pop;
				double dead = loc.rip;
				double alive = loc.alive;
				double recovered = loc.rec;
				
				// this is here because there is a color whose condition will be true at the same time as black.
				if(loc.color != null && loc.color.equals(B)) {
					return;
				} // end if
					
				// if there is not more infected
				if (infected == 0) {
					if (loc.color != null && loc.color.equals(LAND) && opt)
						return;

					drawPos(loc.row, loc.col, LAND); 
					loc.color = LAND;
				} // end if
				
				
				else if (dead/alive >= 1 && dead/alive < 2) {
					if (loc.color != null && loc.color.equals(GRAY) && opt) { 
						
						return;}
		
					drawPos(loc.row, loc.col, BoardUI.GRAY); 
					
					loc.color = GRAY;
					return;
				} // end if
				
				else if (dead/alive >= 2) {
					if (loc.color != null && loc.color.equals(B) && opt) { 
						
						return;}
		
					drawPos(loc.row, loc.col, BoardUI.B); 
					
					loc.color = B;
					return;
				} // end if
				
				else if (recovered/alive >= .5) {
					if (loc.color != null && loc.color.equals(MAGENTA) && opt) { 
						
						return;}
		
					drawPos(loc.row, loc.col, BoardUI.MAGENTA); 
					
					loc.color = MAGENTA;
					return;
				} // end if
				
				else if (recovered/alive >= .7) {
					if (loc.color != null && loc.color.equals(PINK) && opt) { 
						
						return;}
		
					drawPos(loc.row, loc.col, BoardUI.PINK); 
					
					loc.color = PINK;
					return;
				} // end if
			
					
				else if ( (infected + dead)/population > 0 && (infected + dead)/population < .1) {
					if (loc.color != null && loc.color.equals(first) && opt)
						return;
					
					drawPos(loc.row, loc.col, BoardUI.first); 
					loc.color = first;
				} // if infected low
				else if ( (infected + dead)/population >= .1 && (infected + dead)/population < 0.2) {
					if (loc.color != null && loc.color.equals(second) && opt) 
						return;
					
					drawPos(loc.row, loc.col, BoardUI.second); 
					loc.color = second;
				} // if infected midS
				
				else if ( (infected + dead)/population >= .2 && (infected + dead)/population < 0.3) {
					if (loc.color != null && loc.color.equals(third) && opt) 
						return;
					
					drawPos(loc.row, loc.col, BoardUI.third); 
					loc.color = third;
				} // if infected midS
				
				else if ( (infected + dead)/population >= .3 && (infected + dead)/population < 0.4) {
					if (loc.color != null && loc.color.equals(fourth) && opt) 
						return;
					
					drawPos(loc.row, loc.col, BoardUI.fourth); 
					loc.color = fourth;
				} // if infected midS
				
				else if ( (infected + dead)/population >= .4 && (infected + dead)/population < 0.5) {
					if (loc.color != null && loc.color.equals(fifth) && opt) 
						return;
					
					drawPos(loc.row, loc.col, BoardUI.fifth); 
					loc.color = fifth;
				} // if infected midS
				
				else if ( (infected + dead)/population >= .5 && (infected + dead)/population < 0.6) {
					if (loc.color != null && loc.color.equals(sixth) && opt) 
						return;
					
					drawPos(loc.row, loc.col, BoardUI.sixth); 
					loc.color = sixth;
				} // if infected midS
				
				else if ( (infected + dead)/population >= .6 && (infected + dead)/population < 0.7) {
					if (loc.color != null && loc.color.equals(seventh) && opt) 
						return;
					
					drawPos(loc.row, loc.col, BoardUI.seventh); 
					loc.color = seventh;
				} // if infected midS
				
				else if ( (infected + dead)/population >= .7 && (infected + dead)/population < 0.8) {
					if (loc.color != null && loc.color.equals(eighth) && opt) 
						return;
					
					drawPos(loc.row, loc.col, BoardUI.eighth); 
					loc.color = eighth;
				} // if infected midS
				
				
				else if ( (infected + dead)/population >= .8 && (infected + dead)/population < 0.9) {
					if (loc.color != null && loc.color.equals(ninth) && opt) 
						return;
					
					drawPos(loc.row, loc.col, BoardUI.ninth); 
					loc.color = ninth;
				} // if infected midS
			
				
				else if ( (infected + dead)/population >= .9) {
					if (loc.color != null && loc.color.equals(tenth) && opt) 
						return;
					
					drawPos(loc.row, loc.col, BoardUI.tenth); 
					loc.color = tenth;
		        } // if infected high
			} // end if

	} // end setPositionColor
	
	
	// set a location with the specified color
	public static void drawPos(double row, double col, Color color) {
		
		Runnable draw = new Runnable() {

			@Override
			public void run() {
				if (instance != null && instance.canvas != null) {
					// TODO Auto-generated method stub
					GraphicsContext gc = instance.canvas.getGraphicsContext2D();
					
					// set the color that will be used to draw
					gc.setFill(color);;
					
					// the order is switched because fill rectangle take x and y
					// x ~ x axis and y ~ the y axis
					// make a rectangle with the location on the screen, and the size of the rectangle
					// * scale because we scaled the picture
					gc.fillRect(col * instance.scale, row * instance.scale, instance.scale, instance.scale);
				} // end if
			} // end run
			
		}; // end draw
		
		Platform.runLater(draw);
		//System.out.println("Infect Color");
		//System.out.println(row + " " + col);
	
	} // end drawPos

	public static void initialize(int row, int col) {
		instance = new BoardUI(row, col);
	} // end initialize
	
	public static BoardUI getInstance() {
		return instance;
	} // end getInstance
	
} // end InfectedColo