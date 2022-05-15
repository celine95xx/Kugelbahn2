package de.celineevelyn.kugelbahn;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

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
		
		double windAcc = Level.instance.getWindAcc();
		double windAccX = windAcc * Math.cos(windAngle);
		double windAccY = windAcc * Math.sin(windAngle);
		
		double accX = 0;
		double accY = 0;
									
		
		accY += gravity;
		accY += windAccY;
		accX += windAccX;
		
		double sxNext = marble.getNode().getTranslateX() + ((marble.getCurrentVelocityX() * deltaTime) 
						+ (0.5*accX*deltaTime*deltaTime))*proportionFactor;
		double syNext = marble.getNode().getTranslateY() + ((marble.getCurrentVelocityY() * deltaTime) 
						+ (0.5*accY*deltaTime*deltaTime))*proportionFactor;
		
		
//		System.out.println("CURRENT POSITION: X: " + marble.getCurrentPos().getVector2d());
		marble.setNextPosition(sxNext, syNext);
//		System.out.println("NEXT POSITION: X: " + marble.getCurrentPos().getX()+sxNext + " , Y: " +  marble.getCurrentPos().getY()+syNext );
//		System.out.println("NEXT POSITION: X: " + sxNext + " , Y: " + syNext );
		
		int collisionType = NewCollisionManager.checkCollisionsStart();
		System.out.println("COLLISION TYPE: " + collisionType);
		//boolean collided = NewCollisionManager.checkCollisionsStart();
		if(collisionType == 1)
		{
			Vector2d marblePosition = marble.getCurrentPos();
			Vector2d collisionPosition = NewCollisionManager.calculateSetbackPosition(marblePosition);
			
			marble.setNextPosition(collisionPosition.getX(), collisionPosition.getY());
			
			marble.setCollisionShape(collisionPosition.getX(), collisionPosition.getY());
			//Velocity nach Kollision neu berechnen und unten einfügen für nächsten Frame
			
			Vector2d newVel = NewCollisionManager.calculatePostCollisionVel();
			
			marble.setCurrVelX(newVel.getX());
			marble.setCurrVelY(newVel.getY());
			
			marble.setCurrVelX(marble.getCurrentVelocityX() + (accX * deltaTime));
			marble.setCurrVelY(marble.getCurrentVelocityY() + (accY * deltaTime));
			isRolling = false;
		}
		else if(collisionType == 2)
		{
			Vector2d marblePosition = marble.getCurrentPos();		
			//marble.setPosition(sx, sy);
			
			Vector2d newAcc = NewCollisionManager.calculateAccelerations(gravity);
			
			accX = newAcc.getX();
			accY = newAcc.getY();
			
			if(!isRolling)
			{
				isRolling = true;
				Vector2d tangentVel = NewCollisionManager.calculateTangentialVelocity();
				accX = tangentVel.getX();
				accY = tangentVel.getY();
				
				marble.setCurrVelX(accX);
				marble.setCurrVelY(accY);
				
				Vector2d collisionPosition = NewCollisionManager.calculateSetbackPosition(marblePosition);
				marble.setNextPosition(collisionPosition.getX(), collisionPosition.getY());
				marble.applyNextPosition();
			}
			
			marble.setCurrVelX(marble.getCurrentVelocityX() + (accX * deltaTime));
			marble.setCurrVelY(marble.getCurrentVelocityY() + (accY * deltaTime));
			
			marble.updateLine(accX, accY);
			
			sxNext = marblePosition.getX() 
					+ ((marble.getCurrentVelocityX() * deltaTime) 
					+ (0.5*accX*deltaTime*deltaTime))*proportionFactor;
			syNext = marblePosition.getY()
					+ ((marble.getCurrentVelocityY() * deltaTime) 
					+ (0.5*accY*deltaTime*deltaTime))*proportionFactor;
			
			marble.setNextPosition(sxNext, syNext);
		}
		else
		{
			marble.setCurrVelX(marble.getCurrentVelocityX() + (accX * deltaTime));
			marble.setCurrVelY(marble.getCurrentVelocityY() + (accY * deltaTime));
			isRolling = false;
		}
		
//		if(collisionType != 1)
		marble.applyNextPosition();
	}

}
