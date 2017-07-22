package views;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Game {
	public BorderPane layout;
	public Scene scene;
	public Track t;
	public InfoPanel p;
	public static int ctr = 0;
	public int passLeft = 0;
	
	public Game(){
		layout = new BorderPane();
		t = new Track(this);
		p = new InfoPanel();
		
		setUpLayout();
		
		scene = new Scene(layout, 800, 500);
		
		scene.getStylesheets().add("style.css");
		
		handleEvents();
	}
	
	public void setUpLayout(){
		layout.setCenter(t.layout);
		layout.setRight(p.layout);
	}
	
	public void handleEvents(){
		scene.setOnKeyPressed(e -> {
			switch(e.getCode()){
			case K: 
				
				if(ctr % 2 == 1){
					t.createTrain(this); 
					t.getAnim(t.anims.size() - 1).stop();
				}
				else{
					System.out.println("Cannot create a train while paused!");
				}
				break;
			default: 
				ctr++;
				for(int i = 0; i < t.anims.size(); i++){
					if(ctr % 2 == 1)
						t.getAnim(i).stop();
					else
						t.getAnim(i).start();
				}
				System.out.println(ctr);
				
				break;
			}
		});
	}
}
