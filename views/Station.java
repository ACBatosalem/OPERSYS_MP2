package views;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.control.Button;

public class Station {
	public final static String waitingL = "Waiting Left: ";
	public final static String waitingR = "Waiting Right: ";
	public Pane layout;
	public int stationNum;
	public int passLeft;
	public int passRight;
	public Label text;
	public Label text2;
	public Label text3;
	
	public int x = 31, y = 80;
	
	public ArrayList<Button> passengers = new ArrayList<>();
	
	public Station(int num, int passleft, int passright){
		layout = new Pane();
		passLeft = passleft;
		passRight = passright;
		make(num, passleft, passright);
		setUpLayout();
	}
	
	public void make(int num, int passleft, int passright){
		passLeft = passleft;
		passRight = passright;
		text = new Label("" + (num + 1));
		text2 = new Label(waitingL + passLeft);
		text3 = new Label(waitingR + passRight);
		
		stationNum = num;
		
		text.setLayoutX(220);
		text.setLayoutY(33);
		
		text2.setLayoutX(5);
		text2.setLayoutY(20);
		
		text3.setLayoutX(5);
		text3.setLayoutY(40);
		
		text.setFont(new Font("Arial", 16));
		text.setTextFill(Color.WHITE);
		
		text2.setFont(new Font("Arial", 14));
		text2.setTextFill(Color.WHITE);
		
		text3.setFont(new Font("Arial", 14));
		text3.setTextFill(Color.WHITE);
		
		pass(passright);
	}
	
	public void pass(int pass){
		int total = passengers.size();
		
		if(pass < total){
			passengers.clear();
		}
		
		total = passengers.size();
		
		if(pass > total){
			for(int i = 0; i < pass - total; i++){
				addPassenger();
			}
		}
		
		for(int i = 0; i < passengers.size(); i++){
			passengers.get(i).relocate(x * i + 10, y);
		}
		
		resetLayout();
	}
	
	public void setUpLayout(){
		layout.setStyle("-fx-background-image: url('sprites/stationprev.png')");
		layout.setMinSize(300, 200);
		layout.setMaxSize(300, 200);
		layout.getChildren().clear();
		layout.getChildren().addAll(text, text2, text3);
	}
	
	public void resetLayout(){
		layout.getChildren().clear();
		layout.getChildren().addAll(text, text2, text3);
		layout.getChildren().addAll(passengers);
	}
	
	public void addPassenger(){
		Random rand = new Random();
		
		String t = "-fx-background-image: url('sprites/pass" + rand.nextInt(8) + ".png');";
		
		Button temp = new Button();
		
		temp.setStyle(t);
		
		temp.setMaxSize(31, 72);
		temp.setMinSize(31, 72);
		
		passengers.add(temp);
		
		layout.getChildren().clear();
		layout.getChildren().addAll(text, text2, text3);
		
		layout.getChildren().addAll(passengers);
	}
	
}
