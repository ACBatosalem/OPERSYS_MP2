package controllers;

import javafx.application.Application;
import javafx.stage.Stage;
import views.PassengerFeed;

public class Main extends Application{
	
	@Override
	public void start(Stage window) throws Exception {
		
		Game g = new Game(window);
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
