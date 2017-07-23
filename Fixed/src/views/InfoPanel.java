package views;

import controllers.Game;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class InfoPanel {
	public VBox layout;
	public Station[] stations = new Station[8];
	public Station s;
	public TrainPreview p;
	public AddPanel a;
	public Game g;

	public InfoPanel(Game g){
		layout = new VBox(0);
		
		this.g = g;
		
		createAddPanel();
	}
	
	public void createAddPanel(){
		a = new AddPanel(g);
		layout.getChildren().add(a.layout);
	}
	
	public void initStations(){
		for(int i = 0; i < stations.length; i++){
			stations[i] = new Station(i, 0, 0);
		}
	}
	
	public void createStation(int index, int numleft, int numright){
		stations[index].make(index, numleft, numright);
		layout.getChildren().clear();
		addLayout(stations[index].layout);
		if(p != null)
			addLayout(p.layout);
		createAddPanel();
	}
	
	public void removeChildren(){
		layout.getChildren().clear();
	}
	
	public void addLayout(Pane n){
		layout.getChildren().add(n);
	}
	
	public void resetLayout(){
		removeChildren();
		addLayout(s.layout);
		addLayout(p.layout);
		createAddPanel();
	}
	
	public void train(int trainNum, int passNum, int nextStation){
		p = new TrainPreview(trainNum, passNum, nextStation + 1);
		layout.getChildren().clear();
		if(stations[nextStation] != null)
			addLayout(stations[nextStation].layout);
		addLayout(p.layout);
		createAddPanel();
	}
	
}
