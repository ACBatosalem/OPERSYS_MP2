package views;

import java.util.ArrayList;

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
		
		window.setScene(scene);
		window.show();
		window.setTitle("CalTrain - Group 2: Aguila, Batosalem, Mirafuentes");
		window.setResizable(false);
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
	}
	
	public int outStat(int num) {
		int number = (int)Math.floor(Math.random()*8);
		return (num != number) ? number : outStat(num);
	}
	
	public void logic(){
		
	}
	
	public void pause(){
//		t.trains.get(0)
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
