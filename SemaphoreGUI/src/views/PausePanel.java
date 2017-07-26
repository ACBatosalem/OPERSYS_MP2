package views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PausePanel {
	public Stage window;
	public Scene scene;
	public VBox layout;
	public Label[] labels;
	public ComboBox<Integer> input;
	public Button accept;
	
	public PausePanel(){
		setUpItems();
		setUpLayout();
		scene = new Scene(layout, 200, 200);
		window = new Stage();
		window.setScene(scene);
		window.show();
	}
	
	public void setUpItems(){
		labels = new Label[]{new Label("Pause for how many seconds?")};
		
		
		input = new ComboBox<Integer>();
		input.getItems().addAll(0, 5, 10, 15, 20, 25, 30);
		input.setPrefSize(150, 20);
		input.setValue(0);
		
		accept = new Button("Accept");
		accept.setAlignment(Pos.CENTER);
		accept.setPrefSize(150, 20);
	}
	
	public void setUpLayout(){
		layout = new VBox(20);
		layout.getChildren().addAll(labels[0], input, accept);
	}
}
