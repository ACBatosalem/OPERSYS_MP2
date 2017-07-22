package controllers;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{
	
	@Override
	public void start(Stage window) throws Exception {
		
		Controller c = new Controller();
		
		window.setScene(c.g.scene);
		window.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
