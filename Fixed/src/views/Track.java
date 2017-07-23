package views;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Track {
	public Pane layout;
	public ArrayList<ImageView> trains;
	public ArrayList<AnimationTimer> anims;
	public Button[] stations;
	
	public Track(Game g){
		layout = new Pane();
		trains = new ArrayList<>();
		anims = new ArrayList<>();
		
		stations = new Button[8];
		
		setUpStations();
		
		setUpLayout();
	}
	
	public void createTrain(Game g){
		views.Train t = new views.Train(trains.size() + 1, g);
		
		anims.add(t.timer);
		
		layout.getChildren().add(t.sprite);
		
		trains.add(t.sprite);
		
		resetLayout();
	}
	
	public void setUpStations(){
		for(int i = 0; i < stations.length; i++){
			stations[i] = new Button("" + (i + 1));
			stations[i].setPrefSize(64, 64);
			stations[i].setId("station");
		}
		
		stations[0].setLayoutX(0);
		stations[0].setLayoutY(100);
		stations[1].setLayoutX(0);
		stations[1].setLayoutY(350);
		
		stations[2].setLayoutX(100);
		stations[2].setLayoutY(440);
		stations[3].setLayoutX(350);
		stations[3].setLayoutY(440);
		
		stations[4].setLayoutX(435);
		stations[4].setLayoutY(350);
		stations[5].setLayoutX(435);
		stations[5].setLayoutY(100);
		
		stations[6].setLayoutX(350);
		stations[6].setLayoutY(0);
		stations[7].setLayoutX(100);
		stations[7].setLayoutY(0);
	}
	
	public void setUpLayout(){
		layout.setPadding(new Insets(0));
		layout.setStyle("-fx-background-image: url('sprites/track.png')");
		
		layout.getChildren().addAll(trains);
		layout.getChildren().addAll(stations);
	}
	
	public void resetLayout(){
		layout.getChildren().clear();
		layout.getChildren().addAll(trains);
		layout.getChildren().addAll(stations);
	}
	
	public ImageView getTrain(int index){
		return trains.get(index);
	}
	
	public AnimationTimer getAnim(int index){
		return anims.get(index);
	}
	
}
