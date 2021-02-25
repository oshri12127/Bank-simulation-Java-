package hw6;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import hw6.Customer.Status;

/**
 * Teller - represents a bank clerk
 * 
 */
public class Teller extends Thread {
	private Queue<Customer> queue;
	private Bank bankClock;
	private boolean active;
	private int numOfCustomerService;
	private boolean flagWait;
	private int tellerIdle;
	private int tellerActive;
	private long startActiveTime;
	private long elapsedTimeToIdle;

	/**
	 * @param active
	 * @param bankClock
	 * @param tellerIdle
	 * @param tellerActive
	 */
	public Teller(boolean active, Bank bankClock, int tellerIdle, int tellerActive) {
		this.active = active;
		queue = new LinkedList<Customer>();
		this.bankClock = bankClock;
		flagWait = false;
		this.tellerIdle = tellerIdle;
		this.tellerActive = tellerActive;
	}

	/**
	 * @return teller Idle
	 */
	public int getTellerIdle() {
		return tellerIdle;
	}

	/**
	 * @return teller Active
	 */
	public int getTellerActive() {
		return tellerActive;
	}

	/**
	 * @return true/false if the teller is wait
	 */
	public boolean isFlagWait() {
		return flagWait;
	}

	/**
	 * @param flagWait
	 */
	public void setFlagWait(boolean flagWait) {
		this.flagWait = flagWait;
	}

	/**add Customer to queue 
	 * @param client
	 */
	public void addCustomer(Customer client) {
		this.queue.add(client);
	}

	/**
	 * @return true/false if teller active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return num Of Customer Service
	 */
	public int getNumOfCustomerService() {
		return numOfCustomerService;
	}

	/**
	 * @return size of queue(line) 
	 */
	public int getLine() {
		return queue.size();
	}

	/**
	 * @return the first Customer in the line
	 */
	public Customer getCustomer() {
		return queue.poll();
	}

	/**
	 * run - main thread action
	 */
	public void run() {
		numOfCustomerService = 0;
		startActiveTime = System.currentTimeMillis();
		while (!queue.isEmpty() || bankClock.getClock().isWorking()) {
			synchronized (this) {
				if (bankClock.getClock().isWorking()){
					elapsedTimeToIdle =((new Date()).getTime() - startActiveTime);//elapsed Time To Idle
					if (elapsedTimeToIdle >= this.getTellerActive()* Bank.TIME_SIMULATION_FACTOR)//check if the time to Idle
					{
						this.setActive(false);

					}
				}
				if (isActive() == false) {
					try {
						sleep(this.getTellerIdle() * Bank.TIME_SIMULATION_FACTOR);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.setActive(true);
					startActiveTime = System.currentTimeMillis();
				}
				if (queue.isEmpty() && isActive()) {

					try {
						this.setFlagWait(true);
						this.wait();
						this.setFlagWait(false);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (isActive()) {

					Customer client = getCustomer();
					synchronized (client) {

						try {
							sleep(client.serve());
							client.setStatus(Status.DONE);
							client.notify();
							numOfCustomerService++;

							System.out.println(this.getName() + "num Of Customer Service" + numOfCustomerService);
						} catch (InterruptedException e) {
							setActive(true);
						}
					}

				}

			}
		}

	}
}
