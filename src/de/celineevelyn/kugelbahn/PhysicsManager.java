package de.celineevelyn.kugelbahn;

import java.util.List;

import de.celineevelyn.kugelbahn.controller.MainScreenController;
import de.celineevelyn.kugelbahn.objects.Marble;
import javafx.scene.shape.Rectangle;

public class PhysicsManager 
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
		double gravity = Level.getGravity();
		double windAcc = 0 ; //ueber GUI!!
		
		double accX = 0;
		double accY = 0;
		
	
		if(CollisionManager.checkCollisionsStart() && positionCorrected)
		{
			//Edge
			double[] corner1 = CollisionManager.getClosestEdgeCorner1();
			double[] corner2 = CollisionManager.getClosestEdgeCorner2();
			
			double m = MathUtil.berechneSteigung(corner1, corner2);
			double angle = Math.atan(m);
			double gradeResistance = Math.sin(angle) * gravity; //Hangabtriebskraft
			
//			accY += Math.sin(angle) * gradeResistance;
//			accX += Math.cos(angle) * gradeResistance * 20;
		
			
			System.out.println("ACC:::" + accX + ", " + accY + " Grade Resistance " + gradeResistance + " Gravity: " + gravity);
		}
		else
		{
			// apply wind
			accX += windAcc;
							
			// apply gravity
			accY += gravity;
			
		}
		
		System.out.println("Marble Direction Vectore before: " + marble.getDirectionVector()[0] + ", " + marble.getDirectionVector()[1]);
		
		double sxNext = marble.getNode().getTranslateX() + ((marble.getCurrentVelocityX() * deltaTime) + (0.5*accX*deltaTime*deltaTime))*proportionFactor;
		double syNext = marble.getNode().getTranslateY() + ((marble.getCurrentVelocityY() * deltaTime) + (0.5*accY*deltaTime*deltaTime))*proportionFactor;
		
		marble.setNextPosition(sxNext, syNext);
		
		boolean collisionInNextFrame = CollisionManager.checkCollisionsStart();
		if(collisionInNextFrame ) 
		{	
			//move back along reversed direction vector of marble
			double[] marbleDirectionVector = marble.getDirectionVector();
			System.out.println("Marble Dir Vector BEFORe: " + marbleDirectionVector[0] + ", " + marbleDirectionVector[1] );
			double[] marbleReversedDirVector = MathUtil.reverseDirVector(marbleDirectionVector);	
			double[] marbleReversedNormDirVector = MathUtil.getNormalizedDirVector(marbleReversedDirVector);
			
			//Marble
			double[] marblePosition = marble.getCurrentPos();
			
			//Edge
			double[] corner1 = CollisionManager.getClosestEdgeCorner1();
			double[] corner2 = CollisionManager.getClosestEdgeCorner2();
			double[] edgeVector = MathUtil.getDirVector(corner1, corner2);
			double m = MathUtil.berechneSteigung(corner1, corner2);
			double edgeAngle = Math.atan(m);
			
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
				setback = (marble.getRadius() / Math.sin(angle));
			}
						
			
			double[] gewuenschtePosition = new double[2];
			gewuenschtePosition[0] = intersection[0] + marbleReversedNormDirVector[0]*(setback);
			gewuenschtePosition[1] = intersection[1] + marbleReversedNormDirVector[1]*(setback);
			
			double translationX = Math.abs(marble.getCurrentPos()[0] - gewuenschtePosition[0]);
			double translationY = Math.abs(marble.getCurrentPos()[1] - gewuenschtePosition[1]);
			
			sx = marble.getNode().getTranslateX() + translationX;
			sy = marble.getNode().getTranslateY() +  marbleReversedNormDirVector[1] * translationY;
			
			marble.setPosition(sx, sy);
			
			System.out.println("BEFORE CURRENT VELOCITY Y::" + marble.getCurrentVelocityY());
			
			double angle2 = Math.toRadians(90) - (angle - Math.toRadians(90)) - edgeAngle;
			//mirrorDirectionVector(corner1, corner2, m, );
			
			
			marble.setCurrVelX((marble.getCurrentVelocityY() + (accY * deltaTime))*Math.cos(angle2)); //sin cos vertauscht???
			marble.setCurrentVelocityY(-(marble.getCurrentVelocityY() + (accY * deltaTime)) * Math.sin(angle2));
			
			System.out.println("Marble Direction Vector after: " + marble.getDirectionVector()[0] + ", " + marble.getDirectionVector()[1]);
			
			System.out.println("AFTER CLEANUP: CURRENT VELOCITY::::: " + marble.getCurrentVelocityX() + " , " + marble.getCurrentVelocityY());
			
		}
		else
		{
			sx = sxNext;
			sy = syNext;
			
			marble.setPosition(sx, sy);
			
			marble.setCurrVelX(marble.getCurrentVelocityX() + (accX * deltaTime));
			marble.setCurrVelY(marble.getCurrentVelocityY() + (accY * deltaTime));
			
			System.out.println("CurrentPosition: " + marble.getCurrentPos()[0] + ", " + marble.getCurrentPos()[1]);
		}
		

	}
	
	public static double[] mirrorDirectionVector(double[] corner1, double[] corner2, double mEdge, double[] marblePosition, double[] intersection, double[] marbleDirVector)
	{
		double mNormal;
		double[] mirroredDirectionVector = new double[2];

//		boolean isSpecialEdge = MathUtil.checkEdgeType(corner2, corner2);
//		if(!isSpecialEdge)
//		{
//			mNormal = - (1)/(mEdge);
//			double bNormal = MathUtil.berechneYAchsenabschnitt(intersection, mNormal);
//			double secondPointX = intersection[0] + marblePosition[0] + 10;
//			double secondPointY = MathUtil.berechneGeradenschnittpunktY(secondPointX, mNormal, bNormal);
//			double[] secondPoint = new double[2];
//			secondPoint[0] = secondPointX;
//			secondPoint[1] = secondPointY;
//			
//			double[] mirrorPoint = CollisionManager.calculateLotfusspunkt(intersection, secondPoint, marblePosition);
//			
//			double vectorMarbleMirrorPointX = mirrorPoint[0] - marblePosition[0];
//			double vectorMarbleMirrorPointY = mirrorPoint[1] - marblePosition[1];
//			
//			double mirroredPointX = mirrorPoint[0] + vectorMarbleMirrorPointX;
//			double mirroredPointY = mirrorPoint[1] + vectorMarbleMirrorPointY;
//			
//			mirroredDirectionVector[0] = mirroredPointX - intersection[0];
//			mirroredDirectionVector[1] = mirroredPointY - intersection[1];
//		}

		if(marbleDirVector[1] > 0 & marbleDirVector[0] > 0 )
		{
		
		}
		
		return mirroredDirectionVector;

	}

	

	
}
