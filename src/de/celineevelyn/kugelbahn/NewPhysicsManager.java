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
		double proportionFactor = 100; 
		double gravity = Level.instance.getGravity();
		
		RealVector windDirection = Level.instance.getWindDirection(); 
		RealVector windVelocityVector = new ArrayRealVector();
		double windVelocity = Level.instance.getWindVelocity();
		
		windVelocityVector = windDirection.mapMultiply(windVelocity);
		
		double windDirectionX = windVelocityVector.getEntry(0);
		double windDirectionY = windVelocityVector.getEntry(1);
		
		
		
		double accX = 0;
		double accY = 0;
		
		// TODO: apply wind via GUI
		
		
		
		System.out.println("wind: " + windVelocityVector + ", Wind Velocity: " + windVelocity);
		System.out.println("gravity : " + gravity);
		
									
		// apply accelerations
		accY += gravity;
		accY += windDirectionY;
		accX += windDirectionX;
		
		double sxNext = marble.getNode().getTranslateX() + ((marble.getCurrentVelocityX() * deltaTime) + (0.5*accX*deltaTime*deltaTime))*proportionFactor;
		double syNext = marble.getNode().getTranslateY() + ((marble.getCurrentVelocityY() * deltaTime) + (0.5*accY*deltaTime*deltaTime))*proportionFactor;
		
		marble.setNextPosition(sxNext, syNext);
		
		sx = sxNext;
		sy = syNext;
		
		marble.setPosition(sx, sy);
		
		marble.setCurrVelX(marble.getCurrentVelocityX() + (accX * deltaTime));
		marble.setCurrVelY(marble.getCurrentVelocityY() + (accY * deltaTime));
		
		System.out.println("CurrentPosition: " + marble.getCurrentPos()[0] + ", " + marble.getCurrentPos()[1]);
		
	}

}
