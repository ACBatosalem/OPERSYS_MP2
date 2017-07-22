package controllers;

import java.util.ArrayList;

import javafx.scene.control.Button;
import models.CalTrain;
import models.CalTrainDriver;
import models.Passenger;
import models.Station;
import models.Train;
import views.Game;

public class Controller {
	public Game g;
	public CalTrain c;
	
	public ArrayList<Station> allStations = new ArrayList<Station>();
	public ArrayList<Train> allTrains = new ArrayList<Train>();
	
	/* Passenger-related variables */
	int totalPassengers = 10;
	int passengersLeft = totalPassengers;	// Passengers left to be picked up
	int passengersServed = totalPassengers;	// Passengers who haven't arrived to their destination
	boolean trainsReturned = true;			// If trains haven't returned to Station 0

	/* Program running-related variables */
	int totalPassengersBoarded = 0;
	int totalNumSeats = 0;
	int threadsCompleted = 0;
	int maxFreeSeats = 5;
	int trainCtr = 0;
	int passCtr = 10;
	int maxInsert = 0;
	boolean loadTrainReturned = false;

	/* Temporary Variables */
	int inStationNum, freeSeats;
	Passenger tempRobot;
	Train tempTrain;
	
	public Controller(){
		this.g = new Game();
		this.c = new CalTrain();
		
		createStations();
		createPassengers();
		
		//logic();
	}
	
	public void createStations(){
		g.passLeft = 0;
		System.out.println("Initializing stations");
		g.p.initStations();
		for(int i=0;i<8;i++) {
			allStations.add(c.station_init(i));
			if (i >= 1 && i < 8) {
				allStations.get(i-1).setRightStation(allStations.get(i));
				allStations.get(i).setLeftStation(allStations.get(i-1));
			}
			System.out.println(allStations.get(i).displayNextStations());
		}
	}
	
	public void createPassengers(){
		/* Initialize Passengers */
		for(int i=0;i<totalPassengers;i++) 
		{
			inStationNum = (int)Math.floor(Math.random()*8);
			Passenger tempRobot = new Passenger(allStations.get(inStationNum), c, i, allStations.get(CalTrainDriver.outStat(inStationNum)));
			threadsCompleted++;
			try {Thread.sleep(300);} catch(Exception e){}
		}
		
		for(int i = 0; i < allStations.size(); i++){
			int pass = allStations.get(i).getWaitPassCount(false);
			g.p.createStation(i, pass);
			System.out.println("Station: " + (i + 1) + " Passengers: " + pass);
		}
		
		a();
	}
	
	public void a(){
		for(int i = 0; i < g.t.stations.length; i++){
			int j = i;
			g.t.stations[i].setOnMouseClicked(e -> {
				g.p.createStation(j, allStations.get(j).getWaitPassCount(false));
				g.layout.setRight(g.p.layout);
			});
		}
	}
	
	public void logic(){
		System.out.println("\n---------------------\n");
		while(totalPassServed != totalPassengers || trainsReturned) 
		{
			/* Add Trains into Railway if deficient */
			if(totalNumSeats < totalPassengers) 
			{
				/* Establish Free Seats */
				freeSeats = 5;
				totalNumSeats += freeSeats;

				/* Train is entering first station */
				loadTrainReturned = false;
				tempTrain = new Train(allStations.get(0), c, freeSeats, trainCtr);
				loadTrainReturned = true;

				/* Indicates successful adding of Train */
				allTrains.add(tempTrain);
				trainCtr++;
			}

			/* Random generation of passengers */
			if (insertPass() && maxInsert < 5) {
				inStationNum = (int)Math.floor(Math.random()*8);
				tempRobot = new Passenger(allStations.get(inStationNum), c, passCtr, 
										  allStations.get(CalTrainDriver.outStat(inStationNum)));
				threadsCompleted++;
				passCtr++;
				maxInsert++;
				totalPassengers++;
				passengersLeft++;
				passengersServed++;
				try {Thread.sleep(300);} catch(Exception e){}				
			}

			/* How Train Works */
			for(int j=0;j<allTrains.size();j++) {
				int tempStatNum = allTrains.get(j).getBoardStation().getStationNum();
				boolean tempDirection = allTrains.get(j).getDirection();
				int threadsToReap = -1;
				int threadsReaped = 0;

				threadsToReap = Math.min(allTrains.get(j).getBoardStation().getWaitPassCount(tempDirection),
										 allTrains.get(j).getFreeSeats());

				/* Passengers board train */
				while(threadsReaped < threadsToReap) {
					boolean boarded = false;
					if(threadsCompleted > 0) {
						boarded = c.station_on_board(allTrains.get(j).getBoardStation(),
														  allTrains.get(j).getBoardStation().getWaitingPass(tempDirection).get(0),
														  threadsReaped + 1 == threadsToReap);
						if(boarded)
							threadsReaped++;
//						try{allTrains.get(j).getTrainThread().sleep(500);} catch(Exception e){}
					}
				}

				passengersLeft -= threadsReaped;
				//passengersServed -= served;
				totalPassengersBoarded += threadsReaped;

				if(threadsToReap != threadsReaped)
					System.out.println("Error: Too many passengers on this train!");
				try{allTrains.get(j).getTrainThread().sleep(500);} catch(Exception e){}

				/* Make sure all trains return to first station */
				if (totalPassServed == totalPassengers && allTrains.get(j).getBoardStation().getStationNum() == 0) {
					allTrains.get(j).stopRun();
					allTrains.remove(allTrains.get(j));
					j--;
					System.out.println("STOPP");
					if (allTrains.size() == 0) {
						System.out.println("All trains are gone!");
						trainsReturned = false;
					}
				}
			}
		
			/* Notes */
			System.out.println("Passengers left: " + passengersLeft);
			System.out.println("Passengers boarded: " + totalPassengersBoarded);
			System.out.println("Passengers served: " + totalPassServed);
			System.out.println("\n-------------------------------\n");
		}

		if(totalPassengersBoarded == totalPassengers) {
			System.out.println("All Passengers served!");
			System.exit(0);
		}
	}
	
	public static int outStat(int num) {
		int number = (int)Math.floor(Math.random()*8);
		return (num != number) ? number : outStat(num);
	}

	public static boolean insertPass() {
		int number = (int)Math.floor(Math.random()*10);
		return (number >= 5) ? true : false;
	}
	
	public static int totalPassServed = 0;
}
