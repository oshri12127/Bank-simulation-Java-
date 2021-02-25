package hw6;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * SamplerQueues - periodically wake up and sample the Bank's queues for the
 * purpose of gathering statistics.
 * 
 */
public class SamplerQueues extends Thread {
	private int rate; //in minutes
	private Bank bankClock;
	private Set<Teller>AllTellers;
	private List<Integer>AllRates;

	/**
	 * @param rate
	 * @param AllTellers
	 * @param bankClock
	 */
	public SamplerQueues(int rate,Set<Teller>AllTellers, Bank bankClock)
	{
		this.rate=rate;
		this.AllTellers=AllTellers;
		AllRates=new ArrayList<Integer>();
		this.bankClock=bankClock;
	}
	
	/**
	 * @return average of the lines of tellers
	 */
	public int statistics() {
		int sum = 0;
		for(Teller t : AllTellers)
			sum=sum+t.getLine();
		return (sum=sum/AllTellers.size());
	}
	/* 
	 * run()
	 */
	public void run() {
		while(bankClock.getClock().isWorking())
		{
			AllRates.add(statistics());//add to ArrayList.
			try {
				sleep(rate*Bank.TIME_SIMULATION_FACTOR);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

} /* class SampleQueues */
