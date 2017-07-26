package views;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class PassengerFeed {
	public Stage window = new Stage();
	public Scene scene;
	public ScrollPane layout;
	public ArrayList<Label> text = new ArrayList<Label>();
	public double y = 10;
	public VBox p = new VBox(10);
	
	public PassengerFeed(){
		setUpLayout();
		scene = new Scene(layout, 325, 800);
		window.setScene(scene);
		window.show();
		window.setTitle("Passenger Feed");
		window.setX(0);
		window.setY(0);
	}
	
	public void addEntry(String info){
		Label t = new Label(info);
		t.setFont(new Font("Arial", 14));
		text.add(t);
		p.getChildren().add(t);
	}
	
	public void setUpLayout(){
		layout = new ScrollPane();
		layout.setFitToHeight(true);
		layout.setFitToWidth(true);
		layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		p.autosize();
		
		for(int i = 0; i < text.size(); i++){
			p.getChildren().add(text.get(i));
		}
		
		layout.setContent(p);
		
	}
	
	public boolean exists(String t){
		for(Label temp: text){
			if(temp.getText().equals(t))
				return true;
		}
		return false;
	}
	
	public String getEntry(int i){
		return text.get(i).getText();
	}
	
}
