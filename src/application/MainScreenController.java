package application;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

public class MainScreenController 
{
	@FXML
	private Button startBtn;
	
	@FXML
	private Circle circle;
	
	@FXML
	private TextField velocityText;
	
	
	public void start (ActionEvent event) throws Exception
	{
	    AnimationTimer timer = new AnimationTimer() 
	    {
            // Zeitstempel
            private long last = 0;

            @Override
            public void handle(long now) {

                // Aufrufen der Updateroutine des Spiels mit Angabe der Delta-Zeit
                dosomething((float) ((now - last) / 1000_000.0));
                last = now;
            }
        };
        timer.start();
	}
	
	public void dosomething(float deltaT)
	{
		int velocity = Integer.parseInt(velocityText.getText());
		System.out.println(velocity);
		circle.setTranslateX(velocity+circle.getTranslateX()); //deltaT??
		circle.setTranslateY(5+circle.getTranslateY());
		//circle.getBoundsInLocal().getHeight();
	}
	
	
}
