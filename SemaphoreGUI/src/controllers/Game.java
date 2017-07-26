package controllers;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.CalTrain;
import models.Passenger;
import models.Station;
import models.Train;
import views.InfoPanel;
import views.PassengerFeed;
import views.Track;

public class Game {
	public BorderPane layout;
	public Scene scene;
	public Track t;
	public InfoPanel p;
	public static boolean pause = false;
	public int passLeft = 0;
	public int nextStation = 0;
	
	public PassengerFeed passFeed;
	
	public Timer timer = new Timer();
	
	public Game(Stage window){
		layout = new BorderPane();
		t = new Track(this);
		p = new InfoPanel(this);
		
		this.c = new CalTrain(this);
		setUpLayout();
		
		scene = new Scene(layout, 800, 500);
		
		scene.getStylesheets().add("style.css");
		
		createStations();
		createPassengers();
		if(totalPassengers > 0)
			createTrain();
		
		passFeed = new PassengerFeed();
		
		for(int i = 0; i < c.getText().size(); i++)
			passFeed.addEntry(c.getText().get(i));
		
		window.setScene(scene);
		window.show();
		window.setTitle("CalTrain with Semaphores - Group 2: Aguila, Batosalem, Mirafuentes");
		window.setResizable(false);
		window.setX(350);
		update();
		
	}
	
	public void updateFeed(){
		for(int i = 0; i < c.getText().size(); i++){
			if(!passFeed.exists(c.getText().get(i)))
				passFeed.addEntry(c.getText().get(i));
		}
			
	}
	
	public void setUpLayout(){
		layout.setCenter(t.layout);
		layout.setRight(p.layout);
	}
	
	public void createStations(){
		passLeft = 0;
		System.out.println("Initializing stations");
		p.initStations();
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
		int[] inStation = {1,1,1,1,1,1,
						   2,2,2,2,2,2,2,2,
						   3,3,3,3,
						   4,4,4,4,4,
						   5,5,5,
						   6,6,
						   7,7,7,
						   8,8,8};
		int[] outStation = {2,3,4,8,8,8,
							5,5,6,6,6,7,7,7,
							4,6,6,6,
							5,5,6,8,1,
							2,8,1,
							7,4,
							8,8,1,
							3,6,6};
		for(int i=0;i<totalPassengers;i++) 
		{
			//inStationNum = (int)Math.floor(Math.random()*8);
			
			new Passenger(allStations.get(inStation[i]-1), c, i, allStations.get(outStation[i]-1));
			threadsCompleted++;
			try {Thread.sleep(300);} catch(Exception e){}
		}
		
		for(int i = 0; i < allStations.size(); i++){
			int pass = allStations.get(i).getWaitPassCount(false);
			p.createStation(i, pass, allStations.get(i).getWaitPassCount(true));
			System.out.println("Station: " + (i + 1) + " Passengers: " + pass);
		}
		
		a();
	}
	
	public void addPassenger(int in, int out, boolean direction){
		inStationNum = in;
		Passenger temp = new Passenger(allStations.get(in), c, totalPassengers++, allStations.get(out));
		if(temp.getDirection() != direction){
			allStations.get(in).decWaitPass(temp, temp.getDirection());
			temp.setDirection(direction);
			allStations.get(in).addPassenger(temp, direction);
		}
		
		System.out.println("Added Pass " + totalPassengers + " at Station " + in + " " + direction);
		threadsCompleted++;
		try {Thread.sleep(300);} catch(Exception e){}
		
		for(int i = 0; i < allStations.size(); i++){
			int pass = allStations.get(i).getWaitPassCount(false);
			p.createStation(i, pass, allStations.get(i).getWaitPassCount(true));
			System.out.println("Station: " + (i + 1) + " Passengers: " + pass);
		}
	}
	
	public void a(){
		for(int i = 0; i < t.stations.length; i++){
			int j = i;
			t.stations[i].setOnMouseClicked(e -> {
				currentStation = j;
				p.createStation(j, allStations.get(j).getWaitPassCount(false), allStations.get(j).getWaitPassCount(true));
				layout.setRight(p.layout);
			});
		}
		
		for(int i = 0; i < t.trains.size(); i++){
			int j = i;
			t.getTrain(i).setOnMouseClicked(e -> {
				passLeft = allTrains.get(j).getRiders().size();
				nextStation = allTrains.get(j).getBoardStation().getStationNum();
			});
		}
	}
	
	public void createTrain(){
		freeSeats = 5;
		totalNumSeats += freeSeats;
		
		loadTrainReturned = false;
		tempTrain = new Train(allStations.get(0), c, freeSeats, trainCtr);
		loadTrainReturned = true;
		allTrains.add(tempTrain);
		trainCtr++;
		
		p.train(allTrains.size(), tempTrain.getRiders().size(), tempTrain.getBoardStation().getStationNum());
		t.createTrain(this);
	}
	
	public void update(){
		timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	/*
            	 * 1. Check if a train has reached a station
            	 * 		a. stop trains behind it
            	 * 		b. board passengers
            	 * 		
            	 */
            	logic();
            }
        }, 0, 500);
	}
	
	public void logic(){
		for(int i = 0; i < allTrains.size(); i++){
			currentTrain = i;
			try{Thread.sleep(2500);} catch(Exception e) {e.printStackTrace();}
			
			if(pause){
				try{
					System.out.println("Threads are asleep");
					Thread.sleep(p.a.getTime());
				} 
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					pause = false;
					t.getAnim(i).start();
				}
			}
			
			boolean tempDirection = allTrains.get(i).getDirection();
			
			int threadsToReap = -1;
			int threadsReaped = 0;
			
			threadsToReap = Math.min(allTrains.get(i).getBoardStation().getWaitPassCount(tempDirection),
									 allTrains.get(i).getFreeSeats());
			
			while(threadsReaped < threadsToReap) {
				boolean boarded = false;
				if(threadsCompleted > 0) {
					if(allTrains.get(i).getBoardStation().getWaitingPass(tempDirection).size() > 0)
						boarded = c.station_on_board(allTrains.get(i).getBoardStation(),
													  allTrains.get(i).getBoardStation().getWaitingPass(tempDirection).get(0),
													  threadsReaped + 1 == threadsToReap);
					if(boarded)
						threadsReaped++;
				}
			}
			
			passengersLeft -= threadsReaped;
			totalPassengersBoarded += threadsReaped;

			if(threadsToReap != threadsReaped)
				System.out.println("Error: Too many passengers on this train!");
			try{Thread.sleep(800);} catch(Exception e){e.printStackTrace();}

			/* Make sure all trains return to first station */
			if (totalPassServed == totalPassengers) {
				allTrains.get(i).stopRun();
				System.out.println("Train " + allTrains.get(i).getTrainNum() + " is decommissioned.");
				allTrains.remove(allTrains.get(i));
				t.anims.remove(i);
				t.trains.remove(i);
				i--;
				if (allTrains.size() == 0) {
					System.out.println("All trains are gone!");
				}
			}
			
		}
	}
	
	public void checkStop(){
//		for(int i = 1; i < allTrains.size() - 2; i++){
//			if(allTrains.get(i - 1).getBoardStation().getStationNum() == allTrains.get(i).getBoardStation().getStationNum()){
//				t.getAnim(i).stop();
//			}
//			
//			if(allTrains.get(i - 1).getBoardStation().getStationNum() > allTrains.get(i).getBoardStation().getStationNum())
//				t.getAnim(i).start();
//		}
		
		if(currentTrain > 0){
			try{
				if(allTrains.get(currentTrain).getBoardStation().checkNextQueue(allTrains.get(currentTrain - 1), allTrains.get(currentTrain).getDirection()))
					t.getAnim(currentTrain).stop();
			}catch(Exception e){}
		}
			
		
	}
	
	public void resetTrains(){
		t.resetLayout();
	}
	
	public void resetStations(){
		for(int i = 0; i < t.stations.length; i++){
			p.createStation(i, allStations.get(i).getWaitPassCount(false), allStations.get(i).getWaitPassCount(true));
			layout.setRight(p.layout);
		}
		p.createStation(currentStation, allStations.get(currentStation).getWaitPassCount(false), allStations.get(currentStation).getWaitPassCount(true));
		layout.setRight(p.layout);
	}
	
	public void resetTrainPrev(){
		if(allTrains.size() > 0)
			p.train(allTrains.size(), allTrains.get(currentTrain).getRiders().size(), allTrains.get(currentTrain).getBoardStation().getStationNum());
	}
	
	public void pause(){
		for(int i = 0; i < allTrains.size(); i++){
			
			if(pause){
				try{
					System.out.println("Threads are asleep");
					t.getAnim(i).stop();
					Thread.sleep(p.a.getTime());
				} 
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					pause = false;
					t.getAnim(i).start();
				}
			}
		}
		
	}
	
	public int currentStation = 0;
	
	public CalTrain c;
	
	public ArrayList<Station> allStations = new ArrayList<Station>();
	public ArrayList<Train> allTrains = new ArrayList<Train>();
	
	/* Passenger-related variables */
	int totalPassengers = 34;
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
	
	public static int currentTrain = 0;
	
	public static int totalPassServed = 0;
}
