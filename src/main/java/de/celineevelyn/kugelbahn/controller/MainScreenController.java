package de.celineevelyn.kugelbahn.controller;

import java.util.ArrayList;

import java.util.List;

import de.celineevelyn.kugelbahn.Level;
import de.celineevelyn.kugelbahn.NewCollisionManager;
import de.celineevelyn.kugelbahn.NewPhysicsManager;
import de.celineevelyn.kugelbahn.objects.BasicNode;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.*;


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
	private TextField startVelX, startVelY, currentVelX, currentVelY, windAcceleration, boxAngle;
	
	@FXML
	private Circle collisionCircle;
	
	@FXML
	private Rectangle colRect, colRect1, colRect2, colRect3;
	
	@FXML
	private ChoiceBox<String> gravity;
	
	@FXML
	private ChoiceBox<String> windDirection;
	
	private static AnimationTimer timer;
	
	private Level level;
	
	private static long timeStart;
	
	private static List<Node> envShapes = new ArrayList<Node>();
	
	@FXML
	private Group group;
	
	

	/**
	 * Wird aufgerufen, wenn der Nutzer auf Start klickt
	 */
	@FXML
	public void start(ActionEvent event) 
	{
		double startVelocityX = 0;
		double startVelocityY = 0;
		double windAcc = 0;
		if(!startVelX.getText().isEmpty())
			startVelocityX = Double.parseDouble(startVelX.getText());
		if(!startVelY.getText().isEmpty())
			startVelocityY = Double.parseDouble(startVelY.getText());
		
		if(!windAcceleration.getText().isEmpty())
			windAcc = Double.parseDouble(windAcceleration.getText());
		
		level.setMarbleStartVelocity(startVelocityX, startVelocityY);
		level.setWindAcc(windAcc);
		
		// Starte Physiksimulation
		timeStart = System.currentTimeMillis();
		timer.start(); //handle-Methode wird in jedem Frame ausgefuehrt
		
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
		long simulationTime = (timeEnd - timeStart);
		System.out.println("Die Gesamtzeit betrug: " + (simulationTime/1000.0) + "Sekunden, bzw " + simulationTime + " Millisekunden");
	}
	
	/**
	 * Initialisiere Fenster-Objekte
	 */
	@FXML
	public void initialize() 
	{
        
		level = new Level();
		Level.instance = level;
		
		// Gravity Choicebox
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
		
		//Wind Choicebox, Winkel in Radiant
		double north = 0.5 * Math.PI;
		double northwest = 0.25 * Math.PI;
		double west =  0;
		double southwest = 1.75 * Math.PI;
		double south = 1.5 * Math.PI;
		double southeast = 1.25 * Math.PI;
		double east = Math.PI;
		double northeast = 0.75 * Math.PI;
		
		String winddirectionValues[] = {"North","Northwest", "West", "Southwest", "South", "Southeast", "East" , "Northeast"};
		windDirection.getItems().addAll(FXCollections.observableArrayList(winddirectionValues));
		windDirection.getSelectionModel().select(3);
		windDirection.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
		{

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) 
			{
				String selectedItem = winddirectionValues[newValue.intValue()];
				
				switch(selectedItem)
				{
					case "North":
						level.setWindAngle(north);
						break;
					case "Northwest":
						level.setWindAngle(northwest);
						break;
					case "West":
						level.setWindAngle(west);
						break;
					case "Southwest":
						level.setWindAngle(southwest);
						break;
					case "South":
						level.setWindAngle(south);
						break;
					case "Southeast":
						level.setWindAngle(southeast);
						break;
					case "East":
						level.setWindAngle(east);
						break;
					case "Northeast":
						level.setWindAngle(northeast);
						break;
					default:
						level.setWindAngle(southwest);
						break;
				}				
			}			
		});

		
		addEnvShapesToList();
		//level.addToNodeList(collisionCircle);
		
		NewCollisionManager.initializeCollisionManager(envShapes);
		
		
		// Timer initialisieren
		initTimer();
	}
	
	/**
	 * Post Initialize Methode, Handler zum Setzen der Murmel
	 */
	public void postInit()
	{	
		// Registriere Handler fuer Mausklicks
		gravity.getScene().setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
            @Override
            public void handle(MouseEvent event) 
            {
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();
                
                // Erstelle eine Marble zum Platzieren mit der Maus
                if(group.getChildren().size() <= 6) //isEmpty() //In Group: fuer jede Marble 3 Elemente: Circle, Line, CollisionPoint
                {
                	BasicNode marbleNode = level.placeMarble(mouseX, mouseY);
                	group.getChildren().add(marbleNode.getNode());
                	group.getChildren().add(level.getMarble().line);
                	group.getChildren().add(level.getMarble().CollisionPoint);
                	
                	envShapes.add(level.getMarble());
                	
                	//level.addToMarbleList(marbleNode);
                	
                	NewPhysicsManager.setMarbles(level.getMarbleList());
                	//NewCollisionManager.setMarbles(level.getMarbleList());
                	
//                	NewPhysicsManager.setMarble(level.getMarble());
//                	NewCollisionManager.setMarble(level.getMarble());
                	
                	//NewCollisionManager.checkCollisionsStart();  //braucht man das ???? 
                }
                else
                {
                	System.out.println("You cannot add more marbles.");
                }
            }
        });
		
		boxAngle.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				level.setBoxAngle(Double.parseDouble(newValue));
				colRect1.setRotate(Double.parseDouble(newValue));
				
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
					NewPhysicsManager.moveMarble(deltaTime);
					showVelocities(level.getVelX(), level.getVelY());
				}
				last = now;
			}
		};
	}

	public void showVelocities(double velX, double velY)
	{
		currentVelX.setText(Double.toString(Math.round(velX*1000)/1000.0)); //Rundung auf 3 Nachkommastellen
		currentVelY.setText(Double.toString(velY * (-1)));
	}
	
	public void addEnvShapesToList()
	{	
		envShapes.add(colRect);
		envShapes.add(colRect1);
		envShapes.add(colRect2);
		envShapes.add(colRect3);
	}
	
	
	public List<Node> getEnvShapes()
	{
		return envShapes;
	}
}
