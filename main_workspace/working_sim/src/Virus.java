
public class Virus {
	private double infectRate;
	private double deathRate;
	private double recoverRate;
	private double shortTravelRate;
	private double longTravelRate;
	private int killTime;
	private int recoverTime;
	private String name;
	
	public Virus(String n, double ir, double dr, int killTime, double rr, int recoverTime, double st,double lt) {
		name = n;
		setInfectRate(ir);
		setDeathRate(dr);
		setKillTime(killTime);
		setRecoverRate(rr);
		setRecoverTime(recoverTime);
		setShortTravelRate(st);
		setLongTravelRate(lt);
	} // end constructor Virus
	
	public String getName() {
		return name;
	} // end getDeathRate
	
	public double getInfectRate() {
		return infectRate;
	} // end getInfectRate
	
	public double getDeathRate() {
		return deathRate;
	} // end getDeathRate
	
	public int getKillTime() {
		return killTime;
	} // end getKillTime
	
	public int getRecoverTime() {
		return recoverTime;
	}// end getRecoverTime
	
	public double getRecoverRate() {
		return recoverRate;
	}
	
	public double getShortTravelRate() {
		
		return shortTravelRate;
	}
	
	public double getLongTravelRate() {
		
		return longTravelRate;
	}

	public void setInfectRate(double value) {
		if (value < 0)
			throw new IllegalArgumentException("Infect rate must be 0 or positive.");
		infectRate = value;
	} // end setInfectedRate
	
	public void setDeathRate(double value) {
		if (value < 0)
			throw new IllegalArgumentException("Death rate must be 0 or positive.");
		deathRate = value;
	} // end setInfectedRate
	
	public void setKillTime(int value) {
		if (value < 0)
			throw new IllegalArgumentException("killTime must be at least 0.");
		killTime = value;
	} // end setInfectedRate
	
	public void setRecoverTime(int value) {
		if (value < 0)
			throw new IllegalArgumentException("recoverTime must be at least 0.");
		recoverTime = value;
	} // end setRecoverTime
	
	
	public void setRecoverRate(double value) {
		if (value < 0)
			throw new IllegalArgumentException("recoverRate must be at least 0.");
		recoverRate = value;
	} // end setInfectedRate
	
	public void setShortTravelRate(double value) {
		if (value < 0)
			throw new IllegalArgumentException("shortTravelRate must be at least 0.");
		shortTravelRate = value;
	} // end setShortTravelRate
	
	public void setLongTravelRate(double value) {
		if (value < 0)
			throw new IllegalArgumentException("longTravelRate must be at least 0.");
		longTravelRate = value;
	} // end setShortTravelRate
	
	
	
} // end Virus

