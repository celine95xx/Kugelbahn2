package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
	
	
	
	@FXML
	private ChoiceBox<String> gravity;
	
	
	
	
	private double acceleration = 0.3; // * 0.165; // Der Mond hat 16,5% der Erdanziehungskraft
	
	private double currentVelocity = 0;
	
	private double weight = 3;
	
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
		int startSpeed = Integer.parseInt(velocityText.getText());
		circle.setTranslateY(startSpeed + currentVelocity + circle.getTranslateY()); //deltaT??
		//circle.setTranslateX(5+circle.getTranslateX());
		//circle.getBoundsInLocal().getHeight();
		
		if (currentVelocity < weight)
		currentVelocity += acceleration;
	}
	
	
	@FXML
	public void initialize() 
	{ 
		gravity.getItems().addAll(FXCollections.observableArrayList("keine Gravitation", "Erde", "Mond"));
		gravity.setValue("Gravitation");
			
	}
	
	//public void getGravity (ActionEvent event) {
		
		//if (gravity.getSelectionModel().getSelectedItem()) {
			
	//	}
		
//	}

}
