package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AddPassenger {
	public Stage stage;
	public Scene scene;
	public VBox layout;
	public TextField input2;
	public Button accept;
	public Label text1;
	public Label text2;
	
	public ComboBox<Integer> station;
	public ComboBox<String> direction;
	
	public AddPassenger(){
		initialize();
		setUpChildren();
		setUpLayout();
		
		stage.setScene(scene);
		stage.setTitle("CalTrain - Add Passenger");
		stage.show();
		stage.sizeToScene();
		stage.setResizable(false);
	}
	
	public void initialize(){
		stage = new Stage();
		
		layout = new VBox(10);
		
		text1 = new Label("Station:");
		text2 = new Label("Add:");
		
		input2 = new TextField();
		
		station = new ComboBox<Integer>();
		direction = new ComboBox<String>();
		
		accept = new Button("Accept");
		
		scene = new Scene(layout, 200, 200);
	}
	
	public void setUpLayout(){
		layout.getChildren().addAll(text1, station, direction, text2, input2, accept);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(10));
	}
	
	public void setUpChoiceBox(){
		station.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);
		direction.getItems().addAll("Left", "Right");

		station.setPrefSize(200, 20);
		direction.setPrefSize(200, 20);
		
		station.setValue(1);
		direction.setValue("Left");
	}
	
	public void setUpChildren(){
		setUpChoiceBox();
		
		text1.setAlignment(Pos.CENTER_LEFT);
		text2.setAlignment(Pos.CENTER_LEFT);
		input2.setAlignment(Pos.CENTER_LEFT);
		accept.setAlignment(Pos.CENTER);
		
		text1.setFont(new Font("Arial", 12));
		text2.setFont(new Font("Arial", 12));
		input2.setFont(new Font("Arial", 12));
		accept.setFont(new Font("Arial", 12));
		
		accept.setTextAlignment(TextAlignment.CENTER);
	}
}
