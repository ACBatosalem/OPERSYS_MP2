import java.util.*;

public class Train implements Runnable {
	public Train(Station in, CalTrain system, int free, int trainNum) {
		boardStation = in;
		sync = system;
		freeSeats = free;
		numSeats = free;
		this.trainNum = trainNum;
		direction = true;
		continueRun = true;
		riders = new ArrayList<Passenger>();
		boardStation.addTrainQueue(this, direction);
		trainThread.start();
	}

	/* Getters and Setters */
	public int getTrainNum() {
		return trainNum;
	}

	public int getFreeSeats() {
		return freeSeats;
	}

	public int getNumSeats() {
		return numSeats;
	}

	public Station getBoardStation() {
		return boardStation;
	}

	public boolean getDirection() {
		return direction;
	}

	public ArrayList<Passenger> getRiders() {
		return riders;
	}

	public boolean getContinueRun() {
		return continueRun;
	}

	public Thread getTrainThread() {
		return trainThread;
	}

	public void setDirection(boolean value) {
		direction = value;
	}

	/* Other Functions */
	public void addRider(Passenger pass) {
		riders.add(pass);
		freeSeats = numSeats - riders.size();
	}

	public void removeRider(Passenger pass) {
		riders.remove(pass);
		freeSeats = numSeats - riders.size();
	}

	public void stopRun() {
		continueRun = false;
	}

	@Override
	public void run() {
		while(getContinueRun()) {
			if (boardStation.getTrain(direction) == null &&
				boardStation.checkNextQueue(this, direction)) 
			{
				/* Train arrives at station */
				boardStation.getLock().lock();
				boardStation.setTrain(direction, this);
			//	boardStation.getLock().unlock();

				/* Executes loading train at station */
				sync.station_load_train(boardStation, this);

				/* Train leaves station */
			//	boardStation.getLock().lock();
				boardStation.setTrain(direction, null);
				boardStation.getLock().unlock();

				/* Train plans next destination */
				boardStation.removeFromQueue(direction);
				boardStation = boardStation.getNextStation(direction);
				boardStation.addTrainQueue(this, direction);
				System.out.println("Train " + trainNum + " is going next to Station "
								   + boardStation.getStationNum());
			}
			try{Thread.sleep(800);} catch(Exception e) {}
		}
	}

	private CalTrain sync;
	private Station boardStation;
	private int freeSeats, trainNum;
	private final int numSeats;
	private boolean continueRun, direction; /* True = Right. False = Left */
	private ArrayList<Passenger> riders;
	private Thread trainThread = new Thread(this);
}