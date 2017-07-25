package views;

import controllers.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class AddPanel {
	
	public Pane layout;
	public Button[] options;
	
	public Game g;
	
	public boolean addPass = false;
	
	public AddPanel(Game g){
		this.g = g;
		setUpButtons();
		setUpLayout();
	}
	
	public void setUpButtons(){
		options = new Button[3];
		
		options[0] = new Button("Add Train");
		options[1] = new Button("Add Passenger");
		options[2] = new Button("Pause");
		
		for(int i = 0; i < options.length; i++){
			options[i].setPrefSize(100, 20);
			options[i].setPadding(new Insets(0));
			options[i].setTextAlignment(TextAlignment.CENTER);
			options[i].setBorder(new Border(new BorderStroke(Color.BLACK, 
		            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			
			options[i].setLayoutX(100);
			options[i].setLayoutY(10 + i * 30);
			
		}
		
		options[0].setOnMouseClicked(e -> {
			boolean no = false;
			if(Game.ctr % 2 == 0 && g.t.trains.size() < 15){
				for(int i = 0; i < g.t.trains.size(); i++){
					System.out.println("y " + g.t.trains.get(i).getTranslateY() + " x " + g.t.trains.get(i).getTranslateX());
					if(g.t.trains.get(i).getTranslateY() < 150 && g.t.trains.get(i).getTranslateX() == 0)
						no = true;
				}
				if(!no){
					System.out.println("createTrain");
					g.createTrain();
				}
				System.out.println("Inside");	
			}
			else{
				System.out.println("Cannot create a train while paused!");
			}
		});
		
		options[1].setOnMouseClicked(e -> {
			addPass = true;
			AddPassenger t = new AddPassenger();
			
			System.out.println("clicked addPass");
			
			t.accept.setOnAction(j -> {
				if(t.input2.getText() != "" || t.input2.getText() != null){
					String d = t.direction.getValue();
					boolean direction = false;
					
					if(d.equals("Right"))
						direction = true;
					for(int i = 0; i < Integer.parseInt(t.input2.getText()); i++)
						g.addPassenger(t.station.getValue() - 1, direction);
					
					t.stage.close();
				}
			});
		});
		
		options[2].setOnMouseClicked(e -> {
			Game.ctr++;
			for(int i = 0; i < g.t.anims.size(); i++){
				System.out.println("Paused!");
			}
		});
	}
	
	public void setUpLayout(){
		layout = new Pane();
		layout.getChildren().addAll(options);
	}

}
