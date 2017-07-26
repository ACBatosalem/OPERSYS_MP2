package views;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TrainPreview {
	
	public Pane layout;
	public Label[] labels;
	
	public TrainPreview(int trainNum, int passNum, int nextStation){
		layout = new Pane();
		
		labels = new Label[] {new Label("Train " + trainNum), new Label("Passengers: " + passNum + "/5"), new Label("Next Station: " + nextStation)};
		
		for(int i = 0; i < labels.length; i++){
			labels[i].setFont(new Font("Consolas", 14));
			labels[i].setTextFill(Color.WHITE);
		}
		
		labels[0].setLayoutX(40);
		labels[0].setLayoutY(37);
		labels[1].setLayoutX(40);
		labels[1].setLayoutY(50);
		labels[2].setLayoutX(40);
		labels[2].setLayoutY(64);
		
		layout.setStyle("-fx-background-image: url('sprites/trainpreview.gif');");
		
		layout.setMaxSize(300, 200);
		layout.setMinSize(300, 200);
		
		layout.getChildren().addAll(labels);
	}

}
