package de.celineevelyn.kugelbahn;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import de.celineevelyn.kugelbahn.objects.Marble;

public class NewPhysicsManager 
{

	private static Marble marble;
	private static boolean positionCorrected = false;
	
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
		
		marble.setNextPosition(sxNext, syNext);
		
		sx = sxNext;
		sy = syNext;
		
		marble.setPosition(sx, sy);
		
		marble.setCurrVelX(marble.getCurrentVelocityX() + (accX * deltaTime));
		marble.setCurrVelY(marble.getCurrentVelocityY() + (accY * deltaTime));
		
		//System.out.println("CurrentPosition: " + marble.getCurrentPos()[0] + ", " + marble.getCurrentPos()[1]);

	}

}
