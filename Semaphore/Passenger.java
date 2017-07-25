public class Passenger implements Runnable {
	public Passenger(Station in, CalTrain system, int num, Station out, int trainNum) {
		boardStation = in;
		leaveStation = out;
		boardStation.addPassenger(this, determineDirection());
		direction = determineDirection();
		sync = system;
		passNum = num;
		prefTrain = trainNum;
		passThread.start();
	}

	public Station getBoardStation() {
		return boardStation;
	}

	public Station getLeaveStation() {
		return leaveStation;
	}

	public int getPassNum() {
		return passNum;
	}

	public boolean getDirection() {
		return direction;
	}

	public int getPreferredTrain() {
		return prefTrain;
	}

	public boolean determineDirection() {
		if (boardStation.getStationNum() <= leaveStation.getStationNum()) // to the right
			return true;
		return false;
	}

	@Override
	public void run() {
		while(true) {
			sync.station_wait_for_train(boardStation, this);
			try {Thread.sleep(500);} catch(Exception e){}
		}
	}

	private CalTrain sync;
	private Station boardStation;
	private Station leaveStation;
	private int passNum;
	private int prefTrain;
	private boolean direction;
	private Thread passThread = new Thread(this);
}