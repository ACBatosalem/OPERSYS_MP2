package models;

public class CalTrain {
	public Station station_init(int num) 
	{
		System.out.println("Initializing Station " + num);
		return new Station(num);
	}

	public void station_load_train(Station station, Train curr) 
	{
		int trainExiters;
		if (station.getTrain(curr.getDirection()) != null &&
			station.getTrain(curr.getDirection()).getTrainNum() == curr.getTrainNum())
		{
			/* Train arrives at specific station */
		//	station.getLock().lock();
			trainExiters = station_off_board(station, curr);
			CalTrainDriver.totalPassServed += trainExiters;
			if (station.getStationNum() == 7 && station.getTrain(true) != null) 				// When reaching end stations,
			{												// Train drops off from one side
				station.setRightTrain(null);				// and receives in the other
				station.removeFromQueue(curr.getDirection());
				station.setLeftTrain(curr);
				station.addTrainQueue(curr, !curr.getDirection());
				curr.setDirection(!curr.getDirection());
			}
			else if (station.getStationNum() == 0 && station.getTrain(false) != null)
			{
				station.setLeftTrain(null);
				station.removeFromQueue(curr.getDirection());
				station.setRightTrain(curr);
				station.addTrainQueue(curr, !curr.getDirection());
				curr.setDirection(!curr.getDirection());
			}
			station.setEmptySeats(curr.getDirection(), curr.getFreeSeats());
			station.setTotalSeats(curr.getDirection(), curr.getNumSeats());
			//station.getLock().unlock();

			System.out.println("Train " + curr.getTrainNum() + 
							   " arrives in Station " + (station.getStationNum() + 1) 
							   + ". Train's number of available seats = " 
							   + curr.getFreeSeats());

			/* Boarding: Train side */
			while(station.getWaitPassCount(curr.getDirection()) > 0 && station.getEmptySeats(curr.getDirection()) > 0)
			{
				try {
					station.signalTrain();
					station.waitPassSeated();
				} catch(Exception e) {}
			} 

			System.out.println("Station " + (station.getStationNum() + 1) +
							   " Waiting Passengers - " + station.getWaitPassCount(curr.getDirection()) +
							   " Empty Seats - " + station.getEmptySeats(curr.getDirection()));
			
			try{Thread.sleep(800);}catch(Exception e){}
			
			/* Reset Station */
			//station.getLock().lock();
			station.setEmptySeats(curr.getDirection(), 0);
			station.setTotalSeats(curr.getDirection(), 0);
			System.out.println("Train " + curr.getTrainNum() 
								+ " leaves Station " + (station.getStationNum() + 1));
			//station.getLock().unlock();
		}
	}

	public void station_wait_for_train(Station station, Passenger pass) 
	{
		/* Passenger arrives at station */
		station.getLock().lock();
		System.out.println("Passenger " + pass.getPassNum() + " arrives at Station " 
						   + (station.getStationNum() + 1) + ". Destination is Station " + 
						   (pass.getLeaveStation().getStationNum() + 1));
		station.getLock().unlock();

		/* Passenger waits for a train */
		while (station.getTrainPass(pass.getDirection()) <= station.getEmptySeats(pass.getDirection()))
		{
			try { station.waitTrain(); } catch(Exception e) {}
		}

		station.getLock().lock();
		if(station.getTrainPass(pass.getDirection()) + 1 < station.getTotalSeats(pass.getDirection())) {
			station.incStandPass(pass.getDirection());
			System.out.println("Passenger " + pass.getPassNum() + 
							   " boards Train " + station.getTrain(pass.getDirection()).getTrainNum());
		}
		station.getLock().unlock();
	}

	public boolean station_on_board(Station station, Passenger pass, boolean allRode) {
		boolean boarded = false;

		if (station.getTrain(pass.getDirection()) != null)
		{
			/* Passenger rides Train and updates Train stuff */
			System.out.println("Passenger " + pass.getPassNum() + " is on board at Train " + 
				       station.getTrain(pass.getDirection()).getTrainNum());
			station.getTrain(pass.getDirection()).addRider(pass);
			station.getVarLock().lock();
			station.decWaitPass(pass, pass.getDirection());
			station.decStandPass(pass.getDirection());
			station.decEmptySeats(pass.getDirection());
			station.getVarLock().unlock();
			
			/* Passenger signals station that he/she is seated on the train */
			if (station.getEmptySeats(pass.getDirection()) == 0 || 
				station.getTrainPass(pass.getDirection()) == 0 || 
				allRode)
				station.signalPassSeated();

			boarded = true;
		}

		try { Thread.sleep(1000); } catch(Exception e) {}
		return boarded;
	}

	public int station_off_board(Station station, Train t) {
		int exiters = 0;
		if (!t.getRiders().isEmpty()) {
			//station.getLock().lock();
			for(int k=0;k<t.getRiders().size();k++) {
				if (t.getRiders().get(k).getLeaveStation().getStationNum() == station.getStationNum())
				{
					station.incEmptySeats(t.getRiders().get(k).getDirection());
					System.out.println("Passenger " + t.getRiders().get(k).getPassNum() + 
									   " leaves Train " + t.getTrainNum() + 
									   " at Station " + (station.getStationNum() + 1));
					t.removeRider(t.getRiders().get(k));
					k--;
					exiters++;
				}
			}
			//station.getLock().unlock();
//			try { Thread.sleep(500); } catch(Exception e) {}
		}
		return exiters;
	}

	public void countPassengersBoarded(int num) {
		passengersBoarded += num;
	}

	public void countPassengersLeft(int num) {
		passengersLeft -= num;
	}

	public int getPassBoarded() {
		return passengersBoarded;
	}

	public int getPassLeft() {
		return passengersLeft;
	}

	private int passengersBoarded = 0;
	private int passengersLeft = 10;
	private int threadsCompleted = 10;
}