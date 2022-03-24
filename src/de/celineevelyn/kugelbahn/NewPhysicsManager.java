package de.celineevelyn.kugelbahn;

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
		double proportionFactor = 100; //TODO: woher kam das??
		double gravity = Level.getGravity();
		double windAcc = 2 ; 
		
		double accX = 0;
		double accY = 0;
		
		// TODO: apply wind via GUI
		accX += windAcc;
									
		// apply gravity
		accY += gravity;
		
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
