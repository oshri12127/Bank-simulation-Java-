package hw6;

/**
 * Clock - a main clock regulating the bank's work day
 * 
 */
public class Clock extends Thread {

	private boolean isWorking;
	private int dayLength; //in minutes

	/**
	 * @param dayLength
	 */
	public Clock(int dayLength)
	{
		this.dayLength=Bank.TIME_SIMULATION_FACTOR * dayLength;
	}
	/**
	 * @return true/false if the clock is working
	 */
	public boolean isWorking()
	{
		return this.isWorking;
	}
	/**
	 * @param dayLength
	 */
	public void setDayLength(int dayLength) {
		this.dayLength = dayLength;
	}
	/**
	 * @return day Length 
	 */
	public long getDayLength() {
		return dayLength;
	}
	/**
	 * @param isWorking
	 */
	public void setIsWorking(boolean isWorking) {
		this.isWorking = isWorking;
	}
	
	/* 
	 * #run()
	 */
	public void run() 
	{
		setIsWorking(true);
		try {
			
			sleep(dayLength);
			setIsWorking(false);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
