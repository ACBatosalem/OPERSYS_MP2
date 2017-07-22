package views;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class InfoPanel {
	public VBox layout;
	public Station[] stations = new Station[8];
	public Station s;
	public TrainPreview p;

	public InfoPanel(){
		layout = new VBox(0);
		
		train(1, 0, 0);
	}
	
	public void initStations(){
		for(int i = 0; i < stations.length; i++){
			stations[i] = new Station(i, 0);
		}
	}
	
	public void createStation(int index, int num){
		stations[index].make(index, num);
		layout.getChildren().clear();
		addLayout(stations[index].layout);
		if(p != null)
			addLayout(p.layout);
	}
	
	public void removeChildren(){
		layout.getChildren().remove(s.layout);
		layout.getChildren().remove(p.layout);
	}
	
	public void addLayout(Pane n){
		layout.getChildren().add(n);
	}
	
	public void resetLayout(){
		removeChildren();
		addLayout(s.layout);
		addLayout(p.layout);
	}
	
	public void train(int trainNum, int passNum, int nextStation){
		p = new TrainPreview(trainNum, passNum, nextStation);
		layout.getChildren().clear();
		if(s != null)
			addLayout(s.layout);
		addLayout(p.layout);
	}
	
}
