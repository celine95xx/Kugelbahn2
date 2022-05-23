package de.celineevelyn.kugelbahn;


import de.celineevelyn.kugelbahn.controller.MainScreenController;
import de.celineevelyn.kugelbahn.objects.Marble;

public class NewPhysicsManager 
{

	private static Marble marble;
	private static boolean positionCorrected = false;
	private static boolean isRolling = false;
	
	public static void setMarble (Marble m)
	{
		marble = m;
	}
	
	public static void moveMarble(double deltaTime)
	{
		double sx;
		double sy;
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
		
		//Berechne Position der Murmel im nächsten Frame
		double sxNext = marble.getNode().getTranslateX() + ((marble.getCurrentVelocityX() * deltaTime) 
						+ (0.5*accX*deltaTime*deltaTime))*proportionFactor;
		double syNext = marble.getNode().getTranslateY() + ((marble.getCurrentVelocityY() * deltaTime) 
						+ (0.5*accY*deltaTime*deltaTime))*proportionFactor;
		
		marble.setLastPosition(marble.getCurrentPos());
		
		marble.setNextPosition(sxNext, syNext); //naechste Position speichern
		
		int collisionType = NewCollisionManager.checkCollisionsStart(); //Collision Types: Keine Berührung, Kollision, Kontakt
		System.out.println("COLLISION TYPE: " + collisionType);

		////Richtige Kollision, Aufeinandertreffen
		if(collisionType == 1)
		{
			Vector2d marblePosition = marble.getCurrentPos(); 
			Vector2d collisionPosition = NewCollisionManager.calculateSetbackPosition(marblePosition); //Position der Murmel, nachdem sie zurückgesetzt wurde, um nicht im Shape zu stecken
			
			marble.setNextPosition(collisionPosition.getX(), collisionPosition.getY());
			
			marble.setCollisionShape(collisionPosition.getX(), collisionPosition.getY());
			
			Vector2d newVel = NewCollisionManager.calculatePostCollisionVel();
			
			marble.setCurrVelX(newVel.getX() + (accX * deltaTime)); //Geschwindigkeit fuer naechsten Frame updaten
			marble.setCurrVelY(newVel.getY() + (accY * deltaTime));
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
	}

}
