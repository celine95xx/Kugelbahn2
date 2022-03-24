package de.celineevelyn.kugelbahn.controller;

import java.util.ArrayList;
import java.util.List;

import de.celineevelyn.kugelbahn.CollisionManager;
import de.celineevelyn.kugelbahn.Level;
import de.celineevelyn.kugelbahn.NewPhysicsManager;
import de.celineevelyn.kugelbahn.PhysicsManager;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * 
 * @author Evelyn Romanjuk
 * @author Celine Viehmann
 *
 */
public class MainScreenController 
{
	@FXML
	private Button startBtn, resetBtn;

	@FXML
	private TextField startVelX, startVelY, currentVelX, currentVelY;
	
	@FXML
	private Circle collisionCircle;
	
	@FXML
	private Rectangle colRect, colRect2;
	
	@FXML
	private ChoiceBox<String> gravity;
	
	@FXML
	private ChoiceBox<String> winddirection;
	
	private static AnimationTimer timer;
	
	private Level level;
	
	private static long timeStart;
	
	private static List<Rectangle> envShapes = new ArrayList<Rectangle>();
	
	@FXML
	private Group group;
	
	

	/**
	 * Wird aufgerufen, wenn der Nutzer auf Start klickt
	 */
	@FXML
	public void start(ActionEvent event) 
	{
		level.setMarbleStartVelocity(Double.parseDouble(startVelX.getText()), Double.parseDouble(startVelY.getText()));
		
		// Starte Physiksimulation
		timeStart = System.currentTimeMillis();
		timer.start(); //handle-Methode wird in jedem Frame ausgeführt
		
	}
	
	public static void end()
	{
		timer.stop();
		long timeEnd = System.currentTimeMillis();
		calculateSimulationTime(timeEnd);
	}
	
	public void endSimulation(ActionEvent event) throws Exception
	{
		timer.stop();
		
	}
	
	public static void calculateSimulationTime(long timeEnd)
	{
		long simulationTime = timeEnd - timeStart;
		System.out.println("Die Gesamtzeit betrug: " + simulationTime);
	}
	
	/**
	 * Initialisiere Fenster-Objekte
	 */
	@FXML
	public void initialize() 
	{
		level = new Level();
		
		// Fülle gravity ChoiceBox mit Inhalt
		String gravityValues[] = {"keine Gravitation", "Erde", "Mond"};
		gravity.getItems().addAll(FXCollections.observableArrayList(gravityValues));
		gravity.getSelectionModel().select(1);
		gravity.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
		{

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) 
			{
				String selectedItem = gravityValues[newValue.intValue()];
				
				switch(selectedItem)
				{
					case "keine Gravitation":
						level.setGravity(0.5);
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
		
		
		winddirection.getItems().addAll(FXCollections.observableArrayList("Nord","Ost", "Süd", "West"));
		winddirection.getSelectionModel().select(3);
		winddirection.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
		{

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) 
			{
				String selectedItem = gravityValues[newValue.intValue()];
				
				switch(selectedItem)
				{
					case "Nord":
						level.setGravity(0.5);
						break;
					case "Ost":
						level.setGravity(9.81);
						break;
					case "Süd":
						level.setGravity(1.62);
						break;
					case "West":
						level.setGravity(1.62);
						break;
					default:
						level.setGravity(9.81);
						break;
				}				
			}			
		});

		
		addEnvShapesToList();
		level.addToNodeList(collisionCircle);
		//CollisionManager.initializeCollisionManager(level.getTestList()); //fuer Test Circle
		CollisionManager.initializeCollisionManager(envShapes);
		// Timer initialisieren
		initTimer();
		//level = new Level();
	}
	
	
	
	/**
	 * Post Initialize Methode, Handler zum Setzen der Murmel
	 */
	public void postInit() 
	{	
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
                	BasicNode marbleNode = level.placeMarble(mouseX, mouseY);
                	group.getChildren().add(marbleNode.getNode());
                	
                	CollisionManager.setMarble(level.getMarble());
                	//PhysicsManager.setMarble(level.getMarble());
                	NewPhysicsManager.setMarble(level.getMarble());
                	
                	CollisionManager.checkCollisionsStart();
                }
                else
                {
                	System.out.println("There is a marble already.");
                }
            }
        });
	}

	
	/**
	 * Erstellen des AnimationTimers, Berechnung von deltaTime, sowie Update des Levels und Anzeigen der momentanen Geschwindigkeiten
	 */
	private void initTimer() 
	{
		timer = new AnimationTimer() 
		{
			double last = 0;

			@Override
			public void handle(long now) 
			{
				
				//deltaTime = Differenz zwischen diesem und letztem Frame; Timestamp in Nanosekunden, deshalb durch 1.000.000.000 geteilt
				double deltaTime = (double) ((now - last) / 1000_000_000.0);
				if(last != 0)
				{
					if(CollisionManager.checkCollisionsStart())
					{
						System.out.println("COLLISION DETECTED");
					}
					//level.update(deltaTime);  //alte update methode  
					//PhysicsManager.moveMarble(deltaTime);
					NewPhysicsManager.moveMarble(deltaTime);
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
	
	public void addEnvShapesToList()
	{	
		envShapes.add(colRect);
		envShapes.add(colRect2);
	}
	
	
	public List<Rectangle> getEnvShapes()
	{
		return envShapes;
	}
}
