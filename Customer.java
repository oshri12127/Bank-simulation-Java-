package hw6;

import java.util.Set;

/**
 * Customer - represents a client of the bank.
 * 
 */
public class Customer extends Thread {
	public enum Status {
		WAITING, INSERVICE, DONE
	};

	private int serviceTime; // in minutes
	private Status status;
	private Set<Teller> tellers;
	private Bank CustCount;//count the customer in bank

	/**
	 * @param serviceTime
	 * @param status
	 * @param tellers
	 * @param CustCount
	 */
	public Customer(int serviceTime, Status status, Set<Teller> tellers, Bank CustCount) {
		this.serviceTime = serviceTime;
		this.status = status;
		this.tellers = tellers;
		this.CustCount = CustCount;
	}

	/**
	 * @return status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the teller with the min line. 
	 */
	private Teller getShortLine() {

		Teller temp = tellers.iterator().next();
		int min = temp.getLine();
		for (Teller t : tellers) {
			int numTemp = t.getLine();
			if (numTemp < min) {
				min = numTemp;
				temp = t;
			}
		}
		return temp;
	}

	/**
	 * run - main thread action
	 */
	public void run() {
		
		CustCount.setCustCount(1);
		Teller t = getShortLine();
		t.addCustomer(this);//add to Queue of teller

		if (t.getLine() == 1) //if the customer first in line
		{
			synchronized (t) {
				t.notify();
			}
		}
		synchronized (this) {
			System.out.println("cust in bank " + CustCount.getCustCount());
			setStatus(Status.WAITING);
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CustCount.setCustCount(-1);
		}
	}

	/**
	 * @return serviceTime of customer 
	 * @throws InterruptedException
	 */
	public int serve() throws InterruptedException {
		setStatus(Status.INSERVICE);
		return serviceTime * Bank.TIME_SIMULATION_FACTOR;

	}

} /* class Customer */
