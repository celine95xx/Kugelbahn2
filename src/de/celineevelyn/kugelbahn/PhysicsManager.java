package de.celineevelyn.kugelbahn;

import de.celineevelyn.kugelbahn.controller.MainScreenController;
import de.celineevelyn.kugelbahn.objects.Marble;

public class PhysicsManager 
{
	private static Marble marble;
	
	public static void setMarble (Marble m)
	{
		marble = m;
	}
	
	public static void moveMarble(double deltaTime)
	{
		double sx;
		double sy;
		double proportionFactor = 100;
		double gravity = Level.getGravity();
		double windAcc = 0 ; //ueber GUI!!
		
		double accX = 0;
		double accY = 0;
		
		// apply wind
		accX+=windAcc;
						
		// apply gravity
		accY+=gravity;
		
		double sxNext = marble.getNode().getTranslateX() + ((marble.getCurrentVelocityX() * deltaTime) + (0.5*accX*deltaTime*deltaTime))*proportionFactor;
		double syNext = marble.getNode().getTranslateY() + ((marble.getCurrentVelocityY() * deltaTime) + (0.5*accY*deltaTime*deltaTime))*proportionFactor;
		
		marble.setNextPosition(sxNext, syNext);
		boolean collisionInNextFrame = CollisionManager.checkCollisionsStart();
		
		System.out.println("Before collision: " + sxNext + ", " + syNext);
		
		if(collisionInNextFrame) 
		{
			System.out.println("--- Marble Collided, calculate new position!");
			//move back along reversed direction vector of marble
			double[] marbleDirectionVector = marble.getDirectionVector();
			double[] marbleReversedDirVector = MathUtil.reverseDirVector(marbleDirectionVector);	
			double[] marbleReversedNormDirVector = MathUtil.getNormalizedDirVector(marbleReversedDirVector);
			
			//Marble
			double[] marblePosition = marble.getCurrentPos();
			
			//Edge
			double[] corner1 = CollisionManager.getClosestEdgeCorner1();
			double[] corner2 = CollisionManager.getClosestEdgeCorner2();
			double[] edgeVector = MathUtil.getDirVector(corner1, corner2);
			
			//Intersection between marble line and edge
			double[] intersection = MathUtil.calculateIntersection(marbleDirectionVector, marblePosition, corner1, corner2);
			marble.setNextPosition(intersection[0], intersection[1]); //new position, marble center set to intersection
			
			//distance that marble needs to move back
			double angle = MathUtil.calculateAngle(marbleReversedDirVector, edgeVector); //hier aufpassen ob der richtige Winkel rauskommt
			double setback;
			if(angle == 0)
			{
				setback = marble.getRadius();
			}
			else
			{
				//double angleRadian = Math.toRadians(angle);
				setback = (marble.getRadius() / Math.sin(angle));
			}
						
			System.out.println("Marble getCurrentPosition() BEFORE: " + marble.getCurrentPos()[0] + ", " + marble.getCurrentPos()[1]);
			
			double[] gewuenschtePosition = new double[2];
			gewuenschtePosition[0] = intersection[0] + marbleReversedNormDirVector[0]*(setback);
			gewuenschtePosition[1] = intersection[1] + marbleReversedNormDirVector[1]*(setback);
			
			double translationX = Math.abs(marble.getCurrentPos()[0] - gewuenschtePosition[0]);
			double translationY = Math.abs(marble.getCurrentPos()[1] - gewuenschtePosition[1]);
			
			sx = marble.getNode().getTranslateX() + translationX;
			sy = marble.getNode().getTranslateY() +  marbleReversedNormDirVector[1] * translationY;
			
			marble.setPosition(sx, sy);
			
			System.out.println("New Position: " + sx + ", " + sy);
			System.out.println("Intersection: " + intersection[0] + ", " + intersection[1]);
			System.out.println("Edgevector: " + edgeVector[0] + ", " + edgeVector[1]);
			System.out.println("Marble  reversed normalized dir Vector:" + marbleReversedNormDirVector[0] + ", " + marbleReversedNormDirVector[1]);
			System.out.println("Marble getCurrentPosition(): " + marble.getCurrentPos()[0] + ", " + marble.getCurrentPos()[1]);
			System.out.println("Setback: " + setback);
			
			MainScreenController.end();
		}
		else
		{
			sx = sxNext;
			sy = syNext;
			
			marble.setPosition(sx, sy);
		}
		
//		marble.setPosition(sx, sy);
//		MainScreenController.end();
		
		marble.setCurrVelX(marble.getCurrentVelocityX() + (accX * deltaTime));
		marble.setCurrVelY(marble.getCurrentVelocityY() + (accY * deltaTime));

	}
	

	

	
}
