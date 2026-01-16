import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

import javax.imageio.ImageIO;

import javafx.scene.image.ImageView;

public class Board {
	// locations
	private Location locations[][];
	private int nrows;
	private int ncols;
	
	// Virus
	public final Virus virus;
	


	//global totals
	private double totalInf = 0;
	private double totalDead = 0;
	private double totalRec = 0;
	private double totalHealthy;
	private double totalAlive;
	private double totalStartPop;
	private int current_iter = 0; 
	
	// lists to keep track of the infected
	LinkedList<Location> infectedLocations;
	LinkedList<Location> newlyInfectedLocations;
	// transient menas this data of this field will not be saved.
	transient LinkedList<Location> airports;
	
	private String configFileName;
	
// board initialization
	// constructor for the board
	Board(boolean continents_in, boolean useConfig, String filename) {
		
		// a list taking care of the infected location
		infectedLocations = new LinkedList<Location>();
		
		// Virus initialization

		String Name = "Corona";
		double InfectionRate = .05; //.3 fraction increase infected every iteration. (if .5, every iteration infected pop grows by 50%)
		double DeathRate = .01;//.003 //fraction of infected who die every iteration (0 - .5??)
		double RecoverRate = .001; //.0006//fraction of infected who recover every iteration (0 - .5??)
		double ShortTravelRate = .15; //.01
		double LongTravelRate = .01; // add by .05
		int KillTime = 15; //15 //can stay as integers how many iterations for people to start dying
		int RecoverTime = 30; //40 //can stay as integers how many iterations for people to start recovering
		

		virus = new Virus(Name, InfectionRate, DeathRate, KillTime,RecoverRate,RecoverTime,ShortTravelRate,LongTravelRate);

			

		
		// make function so the main can change these variables
		
		
		//set up board for continents
		if(continents_in)
			this.do_img_continents();
		else {
			nrows = 20;
			ncols = 20;
			locations = new Location[nrows][ncols];
			
			//population per location
			double startpop = 100000;
			
			
			totalHealthy =  startpop * nrows * ncols;
			totalAlive = startpop * nrows * ncols;
			totalStartPop = startpop*nrows*ncols;
			for (int i = 0; i < nrows; ++i)
				for (int j = 0; j < ncols; ++j) {
					//startpop = (int) (100*Math.random()) + 50;
					locations[i][j] = new Location(i, j);
					Location loc = locations[i][j];
					
					loc.pop = startpop;
					loc.alive = startpop;
					loc.healthy = startpop;
		        	loc.xxx = Math.random();
		        	loc.color = BoardUI.LAND;
				} // end for j
			
			putInfection(nrows/2,ncols/2,50);
			//print_infected();
		} // end else
		
		if (useConfig) {
			configFileName = filename;
			setUpTravel();
		} // end if
		
	}//end Board constructor
	
	// take the airport information from the config file
	private void configureMap(String filename) {
		try {
			AirportConfigFileManager configManager = new AirportConfigFileManager();
			//
			if (filename == null || filename.isEmpty() || !configManager.hasConfig(filename))
				filename = "config";
			// valid filename
			for (String location : configManager.loadConfig(filename)) {
					String[] values = location.split(" ");
					int row = Integer.parseInt(values[0]);
					int col = Integer.parseInt(values[1]);
					Location loc = locations[row][col];
					loc.hasAirport = true;
					loc.cityLevel = Integer.parseInt(values[2]); 
					loc.canFlyTo = new ArrayList<Location>();
					airports.add(locations[row][col]);
			} // end for
			System.out.println(airports.size());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // end configureMap
	
	//helper function to set locations airports can travel to
	public void setAirplaneTravel() {	
		
		System.out.println("SETTING AIRPLANE TRAVEL");
		
		int[] distance_thresholds = {0,20,25,40,60,50,70,90,125,150,175};
		
		for(Location origin : airports) {
			for(Location destination : airports) {
			
				//dont compare to self
				if(!origin.equals(destination)) {
			
						if (canTravelTo(origin,destination,distance_thresholds) == true) {
							origin.canFlyTo.add(destination);
						}
				
				} // end if
			} // end for destination
		} // end for origin
	} // end setAirplaneTravel
	
	public boolean canTravelTo(Location origin, Location destination, int[] distance_thresholds) {
		
		Location larger;
		Location smaller;
		double distance = distance(origin.row,origin.col,destination.row,destination.col);
		
		if(origin.cityLevel == 10 && destination.cityLevel == 10) {
			//System.out.println("Can fly to");
			return true;
		}
		
		if(origin.cityLevel == destination.cityLevel) {
			smaller = destination;
			larger = origin;
		}
		
		if(origin.cityLevel > destination.cityLevel) {
			larger = origin;
			smaller =  destination;
		}
		
		else {
			smaller = destination;
			larger = origin;
		}
		
		
		
		
		if(distance <= distance_thresholds[smaller.cityLevel]) {
			
			//System.out.println("Can fly to");
			
			return true;
		}
		
		else {
			//System.out.println("Can't fly to");
			return false;
		}
		
		
		
	}
	
	//reads black and white image, sets up board based on image	
	public void do_img_continents() {
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("bw_world.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // end catch
		
		int width = image.getWidth();
		int height = image.getHeight();	
		double total_land = 0.0;
		double total_pop = 7530000000.0;
		totalStartPop = total_pop;
		totalHealthy = total_pop;
		totalAlive = total_pop;
		
		// the row and column of the picture will now be the size of the map
		// you can change this figure to have different parts of the map
		nrows = height;
		ncols = width;
		
		System.out.println("nrows = " + nrows);
		System.out.println("ncols = " + ncols);
	
		// initialize the locations array
		locations = new Location[nrows][ncols];
		
		int rgb;
		int red;
		//System.out.println("Width =" + width + " Height =" + height);

		for(int i = 0; i < nrows; i++) {
		    for(int j = 0; j < ncols; j++) {
		    	rgb = image.getRGB(j, i);
		    	Color mycolor = new Color(rgb);
		        red = mycolor.getRed();

		        if(red > 100) {
		        } // end if
		        else {
		        	Location loc = new Location(i, j);
		        	loc.canFlyTo = new ArrayList<Location>();
			    	locations[i][j] = loc;
			    	
		        	loc.land = 1;
		        	loc.color = BoardUI.LAND;
		        	total_land++;
		        } // end else

		    } // end for j
		} // end for i
		
		//give each location same population
		int proportional_pop = (int) (total_pop/total_land);
		//System.out.println("proportional pop = " + proportional_pop);
		
		//give land locations a total population,healthy population, alive population and infectability (xxx)
		for (int row = 0; row < nrows;row++) {
			for (int col = 0; col < ncols; col++) {
				Location loc = locations[row][col];
				
				if(loc != null) {
		        	loc.xxx = Math.random();
					loc.pop = proportional_pop;
					loc.alive = proportional_pop;
					loc.healthy = proportional_pop;
				} // end if
			} // end for col
		} // end for row
	} // end do_img_continents
	
	
//	// prints the board as number infected
//	public void print_infected() {
//
//		for (int row = 0; row < nrows; row++) {
//
//			System.out.println("\n");
//			for (int col = 0; col < ncols; col++) {
//				if (isLand(row, col))
//					System.out.print(locations[row][col].inf + " ");
//			}//end for col
//		}//end for row
//
//	}//end print_infected

	// increases by the number of infected but does not exceed the population
	public void increase_infected(int rowpos, int colpos, double increment) {
		Location loc = locations[rowpos][colpos];

		
		if (loc != null && increment != 0) {
			
			if(loc.inf + loc.rip + loc.rec >= loc.alive)
				return;
		
			
			
			double amountIncremented = loc.inf + increment > loc.alive
										? loc.alive - loc.inf 
										: increment;
			//System.out.println(amountIncremented);
			// if there is a list of newly infected location and the land is currently not infected
			// add the location to the list
			if (newlyInfectedLocations != null && loc.inf == 0)
				newlyInfectedLocations.add(loc);
			
			// set the infected
			loc.inf = loc.inf + amountIncremented;
			
			// set the healthy
			loc.healthy = loc.healthy - amountIncremented;
			
			// increment the number of infected
			totalInf += amountIncremented;
			
			// increment the number of healthy people
			
			if(totalHealthy - amountIncremented >= 0) {
				totalHealthy = totalHealthy - amountIncremented;}
			
			else {
				totalHealthy = 0;
			}
			
			// set the color of that location. 
			//BoardUI.setInfectedColor(loc, true);
			
			
		} // end if
		
	}//end increase_invfected

	// method to infect a position's neighbors based on previous iteration
	public void infect_neighbors(int rowpos, int colpos) {
		Location loc = locations[rowpos][colpos];
		
	
		// percent infected in current position based on previous iteration
		// #
		double percent_inf = (loc.inf / loc.pop);
		double travel_factor = (percent_inf * 1.5) + virus.getShortTravelRate();
		//System.out.println("percent_inf = " + percent_inf);
		//System.out.println("travel factor = " + travel_factor + "\n");
		double distance;
		double num_infected = 0;
		
		// loops through neighbors
		for (int row_offset = -5; row_offset <= 5; row_offset++) {
			for (int col_offset = -5; col_offset <= 5; col_offset++) {

				// if offset is 0 (position isn't changed) do nothing
				if ((row_offset != 0 || col_offset != 0)) {
					int row_neighbor = rowpos + row_offset;
					int col_neighbor = colpos + col_offset;
					
					distance = distance(rowpos,colpos,row_neighbor,col_neighbor);
					
					// if the neighbor is in bounds, try to infect it
					if (this.in_bounds(row_neighbor, col_neighbor) && isLand(row_neighbor, col_neighbor)) {
						double randn = Math.random();
						
						// infect it by chance
						if (randn < (travel_factor/Math.pow(distance, 3.5))) {
							
							double amount_increased;
							num_infected++;
							
							if(virus.getShortTravelRate() <= 1) {
								amount_increased = loc.inf/1000;
							}
							else {
								amount_increased = Math.pow(500 * percent_inf, virus.getShortTravelRate()) + (500 * percent_inf * virus.getShortTravelRate());
							}
							
							this.increase_infected(row_neighbor, col_neighbor, amount_increased);
						} // end chance if
					} // end bounds if
					
				} // end if
				
			} // end col offset loop
		} // end row offset loop

			System.out.println("num infected = " + num_infected);
	}// end infect neighbors
	
	public void infect_neighbors_gauss(int rowpos, int colpos) {
		
		
		Location loc = locations[rowpos][colpos];
	
	
		double percent_inf = (loc.inf / loc.pop);
		double travel_factor = (percent_inf) + virus.getShortTravelRate();
		
		int num_infected = (int) (travel_factor * 10);
				
		for(int i = 1; i <= num_infected; i++ ) {
			
			
			Random r = new Random();
			Random c = new Random();
			
			int r_g = (int)(r.nextGaussian()*3);
			int c_g = (int)(c.nextGaussian()*3);
			
			int row_neighbor = rowpos + r_g;
			int col_neighbor = colpos + c_g;
			
			
			if(isLand(rowpos + r_g,colpos + c_g) == false)
				return;
			if(r_g == 0 && c_g == 0)
				return;
			
			
			double 	amount_increased = loc.inf/1000;
			
			double randn = Math.random();
			
			double distance = distance(rowpos,colpos,row_neighbor,col_neighbor);
			
			if (randn < ((travel_factor)/Math.pow(distance, 2))) {
			
			this.increase_infected(rowpos + r_g, colpos + c_g, amount_increased);
			}
		
		}
		
	}
	
	//kills an amount of the infected population at the position
	public void eliminate_population(int rowpos, int colpos) {
		// Location
		
		
		
		Location loc = locations[rowpos][colpos];
		
		//factor killed every iteration
		double kill_factor = virus.getDeathRate();
		
		//number of iterations location needs to be infected to begin killing
		double iters_to_kill = virus.getKillTime();
		
		//kill off if enough iterations have passed
		if (loc.iters_infected >= iters_to_kill) {
		
			//get the amount killed
			double amount_killed = kill_factor * (loc.inf);
			
			//adjust location populations
			loc.alive = loc.alive - amount_killed;
			loc.inf = loc.inf - amount_killed;
			loc.rip = loc.rip + amount_killed;
			
	
			
			//adjust global populations
			totalAlive = totalAlive - amount_killed;
			totalInf = totalInf - amount_killed;
			totalDead = totalDead + amount_killed;
		}//end iters_infected if statement
		
			
	} // end eliminate_population
	
	// increases the passed position's infection count based on the previous iterations infectivity and infected population
	public void infect_self(int rowpos, int colpos) {
		Location loc = locations[rowpos][colpos];
		
		//increase count based on previous iteration's infectivity and infected population
		int infection_increase = (int) (loc.inf * virus.getInfectRate());
		this.increase_infected(rowpos, colpos, infection_increase);

	}//end infect_self
		
	public void recover_self(int rowpos, int colpos) {
		
	
		
		Location loc = locations[rowpos][colpos];
		
		//factor killed every iteration
		double recover_factor = virus.getRecoverRate();
		
		//number of iterations location needs to be infected to begin killing
		double iters_to_recover = virus.getRecoverTime();
		
		//kill off if enough iterations have passed
		if (loc.iters_infected >= iters_to_recover) {
		
			//get the amount killed
			double amount_recovered = recover_factor * (loc.inf);
			
			//adjust location populations
			
			loc.inf = loc.inf - amount_recovered;
			loc.rec = loc.rec + amount_recovered;
			
			
			//adjust global populations
			
			totalInf = totalInf - amount_recovered;
			totalRec = totalRec + amount_recovered;
		}//end iters_infected if statement	
		
			
	}
	
	public void airport_travel(int rowpos, int colpos) {
		
		
		
		//get the location
		Location loc = locations[rowpos][colpos];
		
		//if its not an aiport dont worry about it
		if(loc.hasAirport == false) {
			return;
		}
		
		double infected_neighbors = 0;
		double alive_neighbors = 0;
		int rowoffset;
		int coloffset;
		Location neighbor;
		
		
		
		for(rowoffset = -5; rowoffset <= 5; rowoffset++) {
			for(coloffset = -5; coloffset <= 5; coloffset++) {
				
			
				if(isLand(rowoffset + rowpos,coloffset + colpos) == false)
					break;
				
				neighbor = locations[rowoffset + rowpos][coloffset + colpos];
				
		
				if(neighbor != null) {
				infected_neighbors = infected_neighbors + neighbor.inf;
				alive_neighbors = alive_neighbors + neighbor.alive;}
			
			}
		}
		
		
		
		//get percent infected
		double percent_inf = infected_neighbors / alive_neighbors;		
		//System.out.print("percent inf " + percent_inf);
		double chanceForFlight; 
		
		if(virus.getLongTravelRate() < .5)
			chanceForFlight = (1 - Math.pow(1 - percent_inf, 1.5))*(1 + virus.getLongTravelRate()*10) + virus.getLongTravelRate();
		else
			chanceForFlight = ((1. - Math.pow((1.-percent_inf),(10. + (virus.getLongTravelRate()*10)))) / (10. - virus.getLongTravelRate() ))*virus.getLongTravelRate();

		//System.out.println(" chance for flight " + chanceForFlight);
			
	
		
		//choose the destination to try to fly to
		double randn = Math.random();
		int number_destinations = loc.canFlyTo == null ? 0 : loc.canFlyTo.size();
		if (number_destinations == 0)
			return;
		
		int random_destination = ThreadLocalRandom.current().nextInt(0, number_destinations);
		Location destination = loc.canFlyTo.get(random_destination);
		
		//infect the destination by chance based off the percent infected
		if (randn < chanceForFlight) {

			this.increase_infected(destination.row, destination.col, 25);
		} 
		
			
		
	}
	//performs an iteration of the simulation, infecting neighbors and self
	public void do_iter() {
		long start = System.currentTimeMillis();
		//System.out.println("Length: "+ infectedLocations.size());
		
		// empty list of newly infected location
		newlyInfectedLocations = new LinkedList<Location>();
		
		System.out.println(infectedLocations.size());
		//loops through board, does infecting operations
		for (Location loc : infectedLocations) {
				this.eliminate_population(loc.row, loc.col);
				this.airport_travel(loc.row,loc.col);
				this.infect_neighbors_gauss(loc.row, loc.col);
				this.infect_self(loc.row, loc.col);
				this.recover_self(loc.row, loc.col);

				
				// no need to check for inf != 0 because the list only contains infected
				loc.iters_infected++;
		} // end col
		
		
		
		
		// add it in at the end of the iteration
		infectedLocations.addAll(newlyInfectedLocations);
		
//		for (Location loc : infectedLocations) {
//			
//			BoardUI.setInfectedColor(loc, true);
//		}
		
		for (int i = 0; i < nrows; ++i) {
			for (int j = 0; j < ncols; ++j) { 
				BoardUI.setInfectedColor(locations[i][j], true);
			} // end j
		} // end i 
		
		System.out.println("End of iter");
		
		//increment iteration
		current_iter++;

		long end = System.currentTimeMillis();
		System.out.println("Total time for iteration (s):" + ((end-start)/1000.0));	
	}//end do_iter
	
	//returns true if land, false otherwise
	public boolean isLand(int x, int y) {
		
		if(x < 0 || x >= nrows)
			return false;
		if(y < 0 || y >= ncols)
			return false;
		
		
		return locations[x][y] != null;
	}//end isLand
	
	// checks whether the passed position is in bounds
	public boolean in_bounds(int rowpos, int colpos) {
		return  !((rowpos < 0) || (rowpos >= this.nrows) || (colpos < 0) || (colpos >= this.ncols));
	}//end in_bounds
	
// get information of the board
	public double getTotalInfected() {
		return totalInf;
	} // end getTotalInfected
	
	public double getTotalDeath() {
		return totalDead;
	} // end getTotalDeath
	
	public double getTotalHealthy() {
		return totalHealthy;
	} // end getTotalHealthy
	
	public double getTotalAlive() {
		return totalAlive;
	} // end getTotalInfected
	
	public double getTotalRecovered() {
		return totalRec;
	}
	
	// return the number of iterations that the board has been through
	public int getIteration() {
		return current_iter;
	} // end getIteration
	
	// return the row number of the board
	public int getBoardRows() {
		return nrows;
	} /// end getNROWS
	
	// return the col number of the board
	public int getBoardColumns() {
		return ncols;
	} /// end getNROWS
  
	
	// return a location using the row and pos
	public Location getLocation(int row, int pos) {
		return locations[row][pos];
	} // end getLocation
	
	public double getTotalStartPop() {
		
		return totalStartPop;
	}
		
	// put the infection based on the row and col of the position
	public void putInfection(int row, int col, double amount) {
		if (isLand(row, col)) {
			newlyInfectedLocations = new LinkedList<Location>();
			increase_infected(row, col, amount - locations[row][col].inf);	
			infectedLocations.addAll(newlyInfectedLocations);
			
				
			BoardUI.setInfectedColor(locations[row][col], true);
			
		} // end if
	} // end putInfection
	
	public void setUpTravel() {
		// no specific travel data was required.
		if (configFileName == null)
			return;
		
		// configure the airports and set the airplane travel between airports.
		airports = new LinkedList<Location>();
		configureMap(configFileName);
		setAirplaneTravel();
	} // end setTravel
	
	// static methods
	public static double distance(int row1, int col1, int row2, int col2) {	
		return Math.sqrt(((row2 - row1)*(row2 - row1)) + ((col2-col1)*(col2-col1)));
	} // end calc_distance
	
}//end class board