import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Station {
	/* Constructors */
	public Station(int num) {
		train_arrived = new ReentrantLock().newCondition();
		all_pass_seated = new ReentrantLock().newCondition();
		on_board = new ReentrantLock();
		lock = new ReentrantLock();
		stationNum = num;
		leftEmptySeats = rightEmptySeats = 0;
		leftTrainPass = rightTrainPass = 0;
		leftTotalSeats = rightTotalSeats = 0;
		leftTrain = rightTrain = null;
		leftStation = null;
		rightStation = null;
		leftWaitingPass = new ArrayList<Passenger>();
		rightWaitingPass = new ArrayList<Passenger>();
		leftQueue = new ArrayList<Train>();
		rightQueue = new ArrayList<Train>();
	}

	/* Getters */
	public Condition getArrivedTrain() {
		return train_arrived;
	}

	public Condition getAllPassSeated() {
		return all_pass_seated;
	}

	public Lock getLock() {
		return lock;
	}

	public Lock getVarLock() {
		return on_board;
	}

	public int getStationNum() {
		return stationNum;
	}

	public Train getTrain(boolean direction) {
		if (direction)
			return rightTrain;
		return leftTrain;
	}

	public int getEmptySeats(boolean direction) {
		if (direction)
			return rightEmptySeats;
		return leftEmptySeats;
	}

	public int getTotalSeats(boolean direction) {
		if (direction)
			return rightTotalSeats;
		return leftTotalSeats;
	}

	public int getTrainPass(boolean direction) {
		if (direction)
			return rightTrainPass;
		return leftTrainPass;
	}

	public Station getNextStation(boolean direction) {
		if (direction)
			return rightStation;
		return leftStation;
	}

	public ArrayList<Passenger> getWaitingPass(boolean direction) {
		if (direction)
			return rightWaitingPass;
		return leftWaitingPass;
	}

	public int getWaitPassCount(boolean direction) {
		if (direction)
			return rightWaitingPass.size();
		return leftWaitingPass.size();
	}

	public ArrayList<Train> getQueue(boolean direction) {
		if (direction)
			return rightQueue;
		return leftQueue;
	}

	/* Setters */
	public void setEmptySeats(boolean direction, int seats) {
		if(direction)
			rightEmptySeats = seats;
		else
			leftEmptySeats = seats;
	}

	public void setTotalSeats(boolean direction, int seats) {
		if(direction)
			rightTotalSeats = seats;
		else
			leftTotalSeats = seats;
	}

	public void setLeftStation(Station next) {
		leftStation = next;
	}

	public void setRightStation(Station next) {
		rightStation = next;
	}

	public void setLeftTrain(Train train) {
		leftTrain = train;
	}

	public void setRightTrain(Train train) {
		rightTrain = train;
	}

	public void setTrain(boolean direction, Train train) {
		if (direction)
			rightTrain = train;
		else
			leftTrain = train;
	} 

	/* Other Functions */
	public void addPassenger(Passenger newPass, boolean direction) {
		if (direction)
			rightWaitingPass.add(newPass);
		else
			leftWaitingPass.add(newPass);
	}

	public void decWaitPass(Passenger pass, boolean direction) {
		if (direction) {
			for(Passenger p : rightWaitingPass) {
				if (p.getPassNum() == pass.getPassNum()) {
					rightWaitingPass.remove(p);
					break;
				}
			}
		}
		else {
			for(Passenger p : leftWaitingPass) {
				if (p.getPassNum() == pass.getPassNum()) {
					leftWaitingPass.remove(p);
					break;
				}
			}
		}
	}

	public void incStandPass(boolean direction) {
		if (direction)
			rightTrainPass++;
		else
			leftTrainPass++;
	}

	public void decStandPass(boolean direction) {
		if (direction)
			rightTrainPass--;
		else
			leftTrainPass--;
	}

	public void incEmptySeats(boolean direction) {
		if (direction)
			rightEmptySeats++;
		else
			leftEmptySeats++;
	}

	public void decEmptySeats(boolean direction) {
		if (direction)
			rightEmptySeats--;
		else
			leftEmptySeats--;
	}

	public String displayNextStations() {
		String left = (leftStation == null) ? "null" : "Station " + leftStation.getStationNum(); 
		String right = (rightStation == null) ? "null" : "Station " + rightStation.getStationNum(); 
		return "Station " + stationNum + ": Left Station = " + left + " Right Station = " + right;
	}

	public boolean checkTrainInQueue(Train train, boolean direction) {
		if (getQueue(direction).contains(train))
			return true;
		return false;
	}

	public boolean checkNextQueue(Train train, boolean direction) {
		if(getQueue(direction).get(0).getTrainNum() == train.getTrainNum())
			return true;
		return false;
	}

	public void addTrainQueue(Train train, boolean direction) {
		getQueue(direction).add(train);
	}

	public void removeFromQueue(boolean direction) {
		getQueue(direction).remove(0);
	}

	/* Synchronization Functions */
	public void waitPassSeated() {
		try {
			all_pass_seated.await();
			Thread.sleep(1500);
		} catch(Exception e){}
	}

	public void signalPassSeated() {
		try {
			all_pass_seated.signal();
		} catch(Exception e){}
	}

	public void waitTrain() {
		try {
			train_arrived.await();
		} catch(Exception e){}
	}

	public void signalTrain() {
		try {
			train_arrived.signal();
		} catch(Exception e){}
	}

	/* Variables */
	private Condition train_arrived;
	private Condition all_pass_seated;
	private Lock on_board;
	private Lock lock;
	private int stationNum;
	private ArrayList<Passenger> leftWaitingPass, rightWaitingPass;
	private int leftEmptySeats, rightEmptySeats;
	private int leftTotalSeats, rightTotalSeats;
	private int leftTrainPass, rightTrainPass;
	private Station leftStation, rightStation;
	private Train leftTrain, rightTrain;
	private ArrayList<Train> leftQueue, rightQueue;
}