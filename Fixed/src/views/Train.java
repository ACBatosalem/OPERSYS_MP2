package views;

import controllers.Game;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Train {
	public final static String spriteLoc = "sprites/pellettrain.png";
	private final static int hBounds = 470, wBounds = 460;
	public ImageView sprite;
	public double xValue;
	public double yValue;
	public int trainNum;
	public AnimationTimer timer;
	
	public Game g;
	
	private int num = 10;
	private int num2 = 10;
	private boolean right = false;
	private boolean up = false;
	private boolean left = false;
	private boolean reverse = false;
	private boolean down = false;
	private boolean last = false;
	
	public int atStation = 0;
	
	public Train(int num, Game g){
		trainNum = num;
		
		sprite = new ImageView();
		
		sprite.setImage(new Image(spriteLoc));
		
		xValue = sprite.getLayoutBounds().getMinX();
		yValue = sprite.getLayoutBounds().getMinY();
		
		sprite.setRotate(90);
		
		sprite.setLayoutX(xValue);
		sprite.setLayoutY(yValue);
		
		this.g = g;
		
		handleEvents();
		animate();
	}
	
	public void updateCoordinates(double x, double y){
		xValue = x;
		yValue = y;
	}
	
	public void handleEvents(){
		sprite.setOnMouseClicked(e -> {
			g.passLeft = g.allTrains.get(trainNum - 1).getRiders().size();
			g.p.train(trainNum, g.passLeft, g.nextStation);
		});
	}
	
	public void animate(){
		timer = new AnimationTimer(){
			@Override
            public void handle(long now) {
				int multiplier = 5;
				
				g.resetStations();
				g.resetTrainPrev();
				
            	if(sprite.getLayoutY() + num < hBounds && !right && !up && !reverse){
            		num += multiplier;
                	sprite.setTranslateY(sprite.getLayoutY() + num);
            	}
            	else if(sprite.getLayoutY() + num >= hBounds && !right && !up && !reverse){
            		right = true;
            		sprite.setRotate(0);
            	}
            	
            	if(sprite.getLayoutX() + num2 < wBounds && right && !up && !left && !reverse && !last){
            		num2 += multiplier;
            		sprite.setTranslateX(sprite.getLayoutX() + num2);
            	}
            	else if(sprite.getLayoutX() + num2 >= wBounds && !up && !reverse && !last){
            		if(right){
            			right = false;
            			up = true;
                		sprite.setRotate(-90);
            		}
            	}
            	
            	if(sprite.getLayoutY() + num > 10 && up && !right && !reverse){
            		num = num - multiplier;
            		sprite.setTranslateY(sprite.getLayoutY() + num);
            	}
            	else if(sprite.getLayoutY() + num <= 10 && up && !right && !left && !reverse){
            		right = true;
            		left = true;
            		sprite.setRotate(180);
            	}
            	
            	if(sprite.getLayoutX() + num2 > 75 && left && !reverse && !last){
            		num2 -= multiplier;
            		sprite.setTranslateX(sprite.getLayoutX() + num2);
            	}
            	else if(sprite.getLayoutX() + num2 <= 75 && left && !reverse && !last){
            		sprite.setTranslateY(30);
            		right = false;
            		left = false;
            		reverse = true;
            		sprite.setRotate(0);
            	}
            	
            	if(sprite.getLayoutX() + num2 < wBounds - 25 && reverse && !left && !last){
            		num2 += multiplier;
            		sprite.setTranslateX(sprite.getLayoutX() + num2);
            	}
            	else if(sprite.getLayoutX() + num2 >= wBounds - 25 && reverse && !left && !last){
            		down = true;
            		sprite.setRotate(90);
            	}
            	
            	if(sprite.getLayoutY() + num < hBounds - 25 && reverse && down && !left && !last){
            		num += multiplier;
            		sprite.setTranslateY(sprite.getLayoutY() + num);
            	}
            	else if(sprite.getLayoutY() + num >= wBounds - 25 && reverse && down && !left && !last){
            		sprite.setRotate(180);
            		left = true;
            		down = false;
            	}
            	
            	if(sprite.getLayoutX() + num2 > 20 && reverse && left && !last){
            		num2 -= multiplier;
            		sprite.setTranslateX(sprite.getLayoutX() + num2);
            	}
            	else if(sprite.getLayoutX() + num2 <= 20 && reverse && left && !last){
            		left = false;
            		sprite.setRotate(-90);
            		last = true;
            	}
            	
            	if(sprite.getLayoutY() + num > 80 && reverse && last){
            		num -= multiplier;
            		sprite.setTranslateY(sprite.getLayoutY() + num);
            	}
            	else if(sprite.getLayoutY() + num >= 80 && reverse && last){
            		sprite.setRotate(90);
            		last = false;
            		sprite.setTranslateX(0);
            		right = false;
            		up = false;
            		left = false;
            		reverse = false;
            		down = false;
            	}
            	
            	if(!reverse){
            		// stop at station 1
                	if(sprite.getTranslateX() == 0 && sprite.getTranslateY() == 115){
                		if(g.allStations.get(0).getWaitingPass(true).size() > 0)
                			stop();
                		atStation = 0;
                	}
                	
                	// stop at station 2
                	if(sprite.getTranslateX() == 0 && sprite.getTranslateY() == 365){
                		stop();
                		atStation = 1;
                	}
                	
                	// stop at station 3
                	if(sprite.getTranslateY() == hBounds && sprite.getTranslateX() == 100){
                		stop();
                		atStation = 2;
                	}
                	
                	// stop at station 4
                	if(sprite.getTranslateY() == hBounds && sprite.getTranslateX() == 360){
                		stop();
                		atStation = 3;
                	}
                	
                	// stop at station 5
                	if(sprite.getTranslateX() == wBounds && sprite.getTranslateY() == 365){
                		stop();
                		atStation = 4;
                	}
                		
                	// stop at station 6
                	if(sprite.getTranslateX() == wBounds && sprite.getTranslateY() == 115){
                		stop();
                		atStation = 5;
                	}
                	
                	// stop at station 7
                	if(sprite.getTranslateY() == 10 && sprite.getTranslateX() == 365){
                		stop();
                		atStation = 6;
                	}
                	
                	// stop at station 8
                	if(sprite.getTranslateY() == 10 && sprite.getTranslateX() == 115){
                		if(g.allStations.get(7).getWaitingPass(true).size() > 0)
                			stop();
                		atStation = 7;
                	}
            	}
            	else{
            		// stop at station 1
                	if(sprite.getTranslateX() == 20 && sprite.getTranslateY() == 110){
                		if(g.allStations.get(0).getWaitingPass(false).size() > 0)
                			stop();
                		atStation = 0;
                	}
//                	
                	// stop at station 2
                	if(sprite.getTranslateX() == 20 && sprite.getTranslateY() == 365){
                		stop();
                		atStation = 1;
                	}
                	
                	// stop at station 3
                	if(sprite.getTranslateY() == 445 && sprite.getTranslateX() == 100){
                		stop();
                		atStation = 2;
                	}
                	
                	// stop at station 4
                	if(sprite.getTranslateY() == 445 && sprite.getTranslateX() == 350){
                		stop();
                		atStation = 3;
                	}
                	
                	// stop at station 5
                	if(sprite.getTranslateX() > wBounds - 50 && sprite.getTranslateX() < wBounds && sprite.getTranslateY() == 370){	
            			stop();
                		atStation = 4;
                	}
                		
                	// stop at station 6
            		if(sprite.getTranslateX() > wBounds - 50 && sprite.getTranslateX() < wBounds && sprite.getTranslateY() == 120){	
                		stop();
                		atStation = 5;
                	}
                	
                	// stop at station 7
                	if(sprite.getTranslateY() < 80 && sprite.getTranslateY() > 10  && sprite.getTranslateX() == 350){
                		stop();
                		atStation = 6;
                	}
                	
                	// stop at station 8
                	if(sprite.getTranslateY() < 80 && sprite.getTranslateY() > 10  && sprite.getTranslateX() == 105){
                		if(g.allStations.get(7).getWaitingPass(false).size() > 0)
                			stop();
                		atStation = 7;
                	}
            	}
			}
		};
		
		timer.start();
	}
}
