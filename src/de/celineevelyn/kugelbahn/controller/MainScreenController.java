package de.celineevelyn.kugelbahn.controller;

import java.util.ArrayList;

import de.celineevelyn.kugelbahn.Level;
import de.celineevelyn.kugelbahn.objects.BasicNode;
import javafx.animation.AnimationTimer;
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
	private TextField velocityText;

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
	public void start(ActionEvent event) {
		
		// TODO: Level Werte der TextFelder übergeben
		ArrayList<BasicNode> nodeList = level.start(10,10, 10.0, 0.981, 1.0, Integer.parseInt(velocityText.getText()));
		
		for(BasicNode node : nodeList) {
			//group.getChildren().add(node.getNode());
		}
		
		// Starte Physiksimulation
		timer.start();
		
	}
	
	
	/**
	 * Initialisiere Fenster-Objekte
	 * Hier kann noch nicht auf die Szene zugegriffen werden
	 */
	@FXML
	public void initialize() {
		
		// Fülle gravity ChoiceBox mit Inhalt
		gravity.getItems().addAll(FXCollections.observableArrayList("keine Gravitation", "Erde", "Mond"));
		gravity.setValue("Gravitation");
		
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
		gravity.getScene().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();
                
                // Erstelle eine Test-Marble zum Platzieren mit der Maus
                BasicNode marble = level.placeMarble(mouseX, mouseY);
                group.getChildren().add(marble.getNode());
                
            }

        });
	}

	
	/**
	 * Baue Timer
	 */
	private void initTimer() {
		timer = new AnimationTimer() {
			// Zeitstempel
			private long last = 0;

			@Override
			public void handle(long now) {
				double deltaT = (double) ((now - last) / 1000_000.0);
				level.update(deltaT);
				last = now;
			}
		};
	}
	
}
