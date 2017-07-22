package views;

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
	
	private int num = 10;
	private int num2 = 10;
	private boolean right = false;
	private boolean up = false;
	private boolean left = false;
	private boolean reverse = false;
	private boolean down = false;
	private boolean last = false;
	
	public Train(int num, Game g){
		trainNum = num;
		
		sprite = new ImageView();
		
		sprite.setImage(new Image(spriteLoc));
		
		xValue = sprite.getLayoutBounds().getMinX();
		yValue = sprite.getLayoutBounds().getMinY();
		
		sprite.setRotate(90);
		
		sprite.setLayoutX(xValue);
		sprite.setLayoutY(yValue);
		
		handleEvents(g);
		animate();
	}
	
	public void updateCoordinates(double x, double y){
		xValue = x;
		yValue = y;
	}
	
	public void handleEvents(Game g){
		sprite.setOnMouseClicked(e -> {
			g.p.train(trainNum, g.passLeft, 2);
		});
	}
	
	public void animate(){
		timer = new AnimationTimer(){
			@Override
            public void handle(long now) {
            	if(sprite.getLayoutY() + num < hBounds && !right && !up && !reverse){
            		num += 5;
                	sprite.setTranslateY(sprite.getLayoutY() + num);
            	}
            	else if(sprite.getLayoutY() + num >= hBounds && !right && !up && !reverse){
            		right = true;
            		sprite.setRotate(0);
            	}
            	
            	if(sprite.getLayoutX() + num2 < wBounds && right && !up && !left && !reverse && !last){
            		num2 += 5;
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
            		num = num - 5;
            		sprite.setTranslateY(sprite.getLayoutY() + num);
            	}
            	else if(sprite.getLayoutY() + num <= 10 && up && !right && !left && !reverse){
            		right = true;
            		left = true;
            		sprite.setRotate(180);
            	}
            	
            	if(sprite.getLayoutX() + num2 > 75 && left && !reverse && !last){
            		num2 -= 5;
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
            		num2 += 5;
            		sprite.setTranslateX(sprite.getLayoutX() + num2);
            	}
            	else if(sprite.getLayoutX() + num2 >= wBounds - 25 && reverse && !left && !last){
            		down = true;
            		sprite.setRotate(90);
            	}
            	
            	if(sprite.getLayoutY() + num < hBounds - 25 && reverse && down && !left && !last){
            		num += 5;
            		sprite.setTranslateY(sprite.getLayoutY() + num);
            	}
            	else if(sprite.getLayoutY() + num >= wBounds - 25 && reverse && down && !left && !last){
            		sprite.setRotate(180);
            		left = true;
            		down = false;
            	}
            	
            	if(sprite.getLayoutX() + num2 > 20 && reverse && left && !last){
            		num2 -= 5;
            		sprite.setTranslateX(sprite.getLayoutX() + num2);
            	}
            	else if(sprite.getLayoutX() + num2 <= 20 && reverse && left && !last){
            		left = false;
            		sprite.setRotate(-90);
            		last = true;
            	}
            	
            	if(sprite.getLayoutY() + num > 80 && reverse && last){
            		num -= 5;
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
			}
		};
		
		timer.start();
	}
}
