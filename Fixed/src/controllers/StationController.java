package controllers;

public class StationController {
	public models.Station model;
	public views.Station view;
	
	public StationController(models.Station model, views.Station view){
		this.model = model;
		this.view = view;
		
		update();
	}
	
	public void update(){
		this.view.passLeft = this.model.getWaitingPass(true).size();
		this.view.resetLayout();
	}
}
