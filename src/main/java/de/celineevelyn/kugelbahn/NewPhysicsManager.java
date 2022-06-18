package de.celineevelyn.kugelbahn;


import de.celineevelyn.kugelbahn.controller.MainScreenController;
import de.celineevelyn.kugelbahn.objects.Marble;
import de.celineevelyn.kugelbahn.objects.Scissors;
import java.util.ArrayList;

public class NewPhysicsManager 
{

	//private static Marble marble;
	private static ArrayList<Marble> marbles;
	private static Scissors s;
	private static boolean positionCorrected = false;
	private static boolean isRolling = false;
	private static boolean bladeIsMoving = false;
	private static double degreeAdded = 0;
	
//	public static void setMarble (Marble m)
//	{
//		marble = m;
//	}
	
	public static void setMarbles(ArrayList<Marble> marbleList)
	{
		marbles = marbleList;
	}
	
	public static void moveMarble(double deltaTime)
	{
		for(Marble marble : marbles)
		{
//		double sx;
//		double sy;
		double proportionFactor = 727; //1667 = 12mm Durchmesser, 727 = 8px Radius, 11mm Radius
		double gravity = Level.instance.getGravity();
		
		double windAngle =  Level.instance.getWindAngle();
		
		double windAcc = Level.instance.getWindAcc(); //User Input
		double windAccX = windAcc * Math.cos(windAngle);
		double windAccY = windAcc * Math.sin(windAngle);
		
		double accX = 0;
		double accY = 0;				
		
		accY += gravity;
		accY += windAccY;
		accX += windAccX;
		
		//Berechne Position der Murmel im naechsten Frame
		double sxNext = marble.getNode().getTranslateX() + ((marble.getCurrentVelocityX() * deltaTime) 
						+ (0.5*accX*deltaTime*deltaTime))*proportionFactor;
		double syNext = marble.getNode().getTranslateY() + ((marble.getCurrentVelocityY() * deltaTime) 
						+ (0.5*accY*deltaTime*deltaTime))*proportionFactor;
		
		marble.setLastPosition(marble.getCurrentPos());
		
		marble.setNextPosition(sxNext, syNext); //naechste Position speichern
		
		int collisionType = NewCollisionManager.checkCollisionsStart(marble); //Collision Types: Keine Beruehrung, Kollision, Kontakt
		System.out.println("COLLISION TYPE: " + collisionType);

		////Richtige Kollision, Aufeinandertreffen
		if(collisionType == 1)
		{
			Vector2d marblePosition = marble.getCurrentPos(); 
			
			//System.out.println("NPM: Marble ID: " + marble.getId() + " | Current Position: " + marblePosition.getVector2d());
			
			Vector2d collisionPosition = NewCollisionManager.calculateSetbackPosition(marblePosition); //Position der Murmel, nachdem sie zurueckgesetzt wurde, um nicht im Shape zu stecken
			
			marble.setNextPosition(collisionPosition.getX(), collisionPosition.getY());
			
			marble.setCollisionShape(collisionPosition.getX(), collisionPosition.getY());
			
			Vector2d newVel1 = NewCollisionManager.calculatePostCollisionVel1();
			
			// if collision node is a marble
			// let NCM calculate post collision velocities for collision marble!
			
			if(NewCollisionManager.collisionWithMarble())
			{
				Vector2d newVel2 = NewCollisionManager.calculatePostCollisionVel2();
				
				Marble cm = NewCollisionManager.getCollisionMarble();
				
				cm.setCurrVelX(newVel2.getX() + (accX * deltaTime));
				cm.setCurrVelY(newVel2.getY() + (accY * deltaTime));
			}
			
			if(NewCollisionManager.collisionWithBlade())
			{
				degreeAdded += NewCollisionManager.bladeRotation();
				double degreeNow = NewCollisionManager.getScissors().getRotation();
				NewCollisionManager.getScissors().setRotation(degreeNow-degreeAdded);
				//let Scissors rotate
			}
			
			marble.setCurrVelX(newVel1.getX() + (accX * deltaTime)); //Geschwindigkeit fuer naechsten Frame updaten
			marble.setCurrVelY(newVel1.getY() + (accY * deltaTime));
			
			
			
			isRolling = false;
			//MainScreenController.end();
		}
		////Kontakt
		else if(collisionType == 2)
		{
			Vector2d marblePosition = marble.getCurrentPos();
			
			Vector2d newAcc = NewCollisionManager.calculateAccelerations(gravity);
			
			accX = newAcc.getX(); //korrigieren, eigentlich fehlt Gravitation und Wind
			accY = newAcc.getY();
			
			//Falls die Murmel vorher nicht rollte, aber jetzt anfaengt
			if(!isRolling)
			{
				isRolling = true;
				Vector2d tangentVel = NewCollisionManager.calculateTangentialVelocity(); //Geschwindigkeit vor dem Rollen, parallel zur Ebene, die nicht verloren gehen soll
				
				marble.setCurrVelX(tangentVel.getX());
				marble.setCurrVelY(tangentVel.getY());
				
				Vector2d collisionPosition = NewCollisionManager.calculateSetbackPosition(marblePosition); //Position korrigieren
				marble.setNextPosition(collisionPosition.getX(), collisionPosition.getY());
				marble.applyNextPosition(); //muss spaeter weg ??
			}
			
			marble.setCurrVelX(marble.getCurrentVelocityX() + (accX * deltaTime)); //Geschwindigkeiten fuer naechsten Frame updaten
			marble.setCurrVelY(marble.getCurrentVelocityY() + (accY * deltaTime));
			
			marble.updateLine(accX, accY);
			
			//Position fuer naechsten Frame
			sxNext = marblePosition.getX() 
					+ ((marble.getCurrentVelocityX() * deltaTime) 
					+ (0.5*accX*deltaTime*deltaTime))*proportionFactor;
			syNext = marblePosition.getY()
					+ ((marble.getCurrentVelocityY() * deltaTime) 
					+ (0.5*accY*deltaTime*deltaTime))*proportionFactor;
			
			marble.setNextPosition(sxNext, syNext);
		}
		////Keine Beruehrung
		else 
		{
			marble.setCurrVelX(marble.getCurrentVelocityX() + (accX * deltaTime));
			marble.setCurrVelY(marble.getCurrentVelocityY() + (accY * deltaTime));
			isRolling = false;
		}

		marble.applyNextPosition(); //Murmel zeichnen
		
		if(Math.abs(marble.getCurrentVelocityX()) < 0.00001 & Math.abs(marble.getCurrentVelocityY()) < 0.00001)
		{
			MainScreenController.end();
			System.out.println("Beendet mit den Geschwindigkeiten: x: " + marble.getCurrentVelocityX() + " / y: " + marble.getCurrentVelocityY());
		}
		
			double degreeNow = NewCollisionManager.getScissors().getRotation();
			NewCollisionManager.getScissors().setRotation(degreeNow-degreeAdded);
			
			System.out.println("+++++++++++++++Degree Added: " + degreeAdded);
			//let Scissors rotate
	}
		
	}

}
