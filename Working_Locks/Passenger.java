public class Passenger implements Runnable {
	public Passenger(Station in, CalTrain system, int num, Station out) {
		boardStation = in;
		leaveStation = out;
		boardStation.addPassenger(this, determineDirection());
		direction = determineDirection();
		sync = system;
		passNum = num;
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
	private boolean direction;
	private Thread passThread = new Thread(this);
}