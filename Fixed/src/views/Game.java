package views;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.CalTrain;
import models.CalTrainDriver;
import models.Passenger;
import models.Station;
import models.Train;

public class Game {
	public BorderPane layout;
	public Scene scene;
	public Track t;
	public InfoPanel p;
	public static int ctr = 0;
	public int passLeft = 0;
	public int nextStation = 0;
	
	public Game(Stage window){
		layout = new BorderPane();
		t = new Track(this);
		p = new InfoPanel(this);
		
		this.c = new CalTrain();
		setUpLayout();
		
		scene = new Scene(layout, 800, 500);
		
		scene.getStylesheets().add("style.css");
		
		createStations();
		createPassengers();
		createTrain();
		
		window.setScene(scene);
		window.show();
		window.setTitle("CalTrain - Group 2: Aguila, Batosalem, Mirafuentes");
		window.setResizable(false);
		
		Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	/*
            	 * 1. Check if a train has reached a station
            	 * 		a. stop trains behind it
            	 * 		b. board passengers
            	 * 		
            	 */
            	
            	
            	
            	update();
            }
        }, 0, 500);
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
		for(int i=0;i<totalPassengers;i++) 
		{
			inStationNum = (int)Math.floor(Math.random()*8);
			Passenger tempRobot = new Passenger(allStations.get(inStationNum), c, i, allStations.get(CalTrainDriver.outStat(inStationNum)));
			threadsCompleted++;
			try {Thread.sleep(300);} catch(Exception e){}
		}
		
		for(int i = 0; i < allStations.size(); i++){
			int pass = allStations.get(i).getWaitPassCount(false);
			p.createStation(i, pass);
			System.out.println("Station: " + (i + 1) + " Passengers: " + pass);
		}
		
		a();
	}
	
	public void addPassenger(int in, boolean direction){
		inStationNum = in;
		Passenger temp = new Passenger(allStations.get(in), c, totalPassengers++, allStations.get(outStat(in)));
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
			p.createStation(i, pass);
			System.out.println("Station: " + (i + 1) + " Passengers: " + pass);
		}
	}
	
	public void a(){
		for(int i = 0; i < t.stations.length; i++){
			int j = i;
			t.stations[i].setOnMouseClicked(e -> {
				p.createStation(j, allStations.get(j).getWaitPassCount(false));
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
		
//		p.a.options[0].setOnMouseClicked(e -> {
//			boolean no = false;
//			if(ctr % 2 == 0 && t.trains.size() < 15){
//				for(int i = 0; i < t.trains.size(); i++){
//					System.out.println("y " + t.trains.get(i).getTranslateY() + " x " + t.trains.get(i).getTranslateX());
//					if(t.trains.get(i).getTranslateY() < 150 && t.trains.get(i).getTranslateX() == 0)
//						no = true;
//				}
//				if(!no){
//					System.out.println("createTrain");
//					createTrain();
//				}
//				System.out.println("Inside");	
//			}
//			else{
//				System.out.println("Cannot create a train while paused!");
//			}
//		});
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
	
	public int outStat(int num) {
		int number = (int)Math.floor(Math.random()*8);
		return (num != number) ? number : outStat(num);
	}
	
	public void update(){
		
	}
	
	public void logic(){
		
	}
	
	public void pause(){
		
	}
	
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
	
	public static int totalPassServed = 0;
}
