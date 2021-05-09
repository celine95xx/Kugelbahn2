package de.celineevelyn.kugelbahn.controller;

import java.util.ArrayList;

import de.celineevelyn.kugelbahn.Level;
import de.celineevelyn.kugelbahn.objects.BasicNode;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainScreenController {
	@FXML
	private Button startBtn;

	@FXML
	private TextField startVelX, startVelY, currentVelX, currentVelY;
	

	@FXML
	private ChoiceBox<String> gravity;
	
	private AnimationTimer timer;
	
	private Level level;
	
	@FXML
	private Group group;
	
	

	/**
	 * Wird aufgerufen, wenn der Nutzer auf Start klickt
	 * @param event
	 */
	@FXML
	public void start(ActionEvent event) 
	{
		// TODO: Level Werte der TextFelder übergeben
//		ArrayList<BasicNode> nodeList = level.start(10,10, 10.0, 9.81, 1.0, Integer.parseInt(velocityText.getText()));
//		for(BasicNode node : nodeList) {
//			//group.getChildren().add(node.getNode());
//		}
		level.setMarbleStartVelocity(Double.parseDouble(startVelX.getText()), Double.parseDouble(startVelY.getText()));
		//level.setGravity(9.81);
		
		// Starte Physiksimulation
		timer.start();
		
	}
	
	
	/**
	 * Initialisiere Fenster-Objekte
	 * Hier kann noch nicht auf die Szene zugegriffen werden
	 */
	@FXML
	public void initialize() 
	{
		
		// Fülle gravity ChoiceBox mit Inhalt
		String gravityValues[] = {"keine Gravitation", "Erde", "Mond"};
		gravity.getItems().addAll(FXCollections.observableArrayList(gravityValues));
//		gravity.setValue("Gravitation");
		gravity.getSelectionModel().select(1);
		gravity.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
		{

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) 
			{
				System.out.println("old Selected Option: " + gravityValues[arg1.intValue()] + " | New Selected Option: " + gravityValues[arg2.intValue()]);
				
				String selectedItem = gravityValues[arg2.intValue()];
				
				switch(selectedItem)
				{
					case "keine Gravitation":
						level.setGravity(0);
						break;
					case "Erde":
						level.setGravity(9.81);
						break;
					case "Mond":
						level.setGravity(1.62);
						break;
					default:
						level.setGravity(9.81);
						break;
						
				}
				
			}
			
		});
		
		// Timer initialisieren
		initTimer();
		
		level = new Level();
		
	}
	
	
	
	/**
	 * Post Initialize Methode
	 * Hier kann auf die Szene zugegriffen werden
	 */
	public void postInit() {
		
		// Registriere Handler für Mausklicks
		gravity.getScene().setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
            @Override
            public void handle(MouseEvent event) 
            {
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();
                
                // Erstelle eine Test-Marble zum Platzieren mit der Maus
                if(group.getChildren().isEmpty())
                {
                	BasicNode marble = level.placeMarble(mouseX, mouseY);
                	group.getChildren().add(marble.getNode());
                }
                else
                {
                	System.out.println("There is a marble already.");
                }
                
            }

        });
	}

	
	/**
	 * Erstelle Timer
	 */
	private void initTimer() 
	{
		timer = new AnimationTimer() 
		{
			// Zeitstempel
			private long last = 0;

			@Override
			public void handle(long now) 
			{
				
				double deltaT = (double) ((now - last) / 1000_000_000.0);
				if(last != 0)
				{
					level.update(deltaT);
					showVelocities(level.getVelX(), level.getVelY());
				}
				last = now;
			}
		};
	}
	
	public void showVelocities(double velX, double velY)
	{
		currentVelX.setText(Double.toString(velX));
		currentVelY.setText(Double.toString(velY));
	}
	
}
