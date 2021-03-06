import java.util.*;
import java.util.concurrent.Semaphore;

public class Station {
	/* Constructors */
	public Station(int num) {
		train_arrived = new Semaphore(0,true);
		all_pass_seated = new Semaphore(0, true);
		on_board = new Semaphore(1, true);
		lock = new Semaphore(1, true);
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
	public Semaphore getArrivedTrain() {
		return train_arrived;
	}

	public Semaphore getAllPassSeated() {
		return all_pass_seated;
	}

	public Semaphore getLock() {
		return lock;
	}

	public Semaphore getVarLock() {
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

	public ArrayList<Passenger> getWaitingPass(boolean direction, int trainNum) {
		ArrayList<Passenger> tempPass = new ArrayList<Passenger>();
		for(Passenger p : getWaitingPass(direction))
			if(p.getPreferredTrain() == trainNum)
				tempPass.add(p);
		return tempPass;
	}

	public int getWaitPassCount(boolean direction) {
		if (direction)
			return rightWaitingPass.size();
		return leftWaitingPass.size();
	}

	public int getWaitPassCount(boolean direction, int trainNum) {
		int counter = 0;
		for(Passenger p : getWaitingPass(direction))
			if (p.getPreferredTrain() == trainNum)
				counter++;
		return counter; 
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
			all_pass_seated.acquire();
			Thread.sleep(1500);
		} catch(Exception e){}
	}

	public void signalPassSeated() {
		try {
			all_pass_seated.release();
		} catch(Exception e){}
	}

	public void waitTrain() {
		try {
			train_arrived.acquire();
		} catch(Exception e){}
	}

	public void signalTrain() {
		try {
			train_arrived.release();
		} catch(Exception e){}
	}

	public void waitOnBoard() {
		try {
			on_board.acquire();
		} catch(Exception e){}
	}

	public void signalOnBoard() {
		try {
			on_board.release();
		} catch(Exception e){}
	}

	public void waitStationLock() {
		try {
			lock.acquire();
		} catch(Exception e){}
	}

	public void signalStationLock() {
		try {
			lock.release();
		} catch(Exception e){}
	}

	/* Variables */
	private Semaphore train_arrived;
	private Semaphore all_pass_seated;
	private Semaphore on_board;
	private Semaphore lock;
	private int stationNum;
	private ArrayList<Passenger> leftWaitingPass, rightWaitingPass;
	private int leftEmptySeats, rightEmptySeats;
	private int leftTotalSeats, rightTotalSeats;
	private int leftTrainPass, rightTrainPass;
	private Station leftStation, rightStation;
	private Train leftTrain, rightTrain;
	private ArrayList<Train> leftQueue, rightQueue;
}