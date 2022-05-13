package de.celineevelyn.kugelbahn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import de.celineevelyn.kugelbahn.objects.Marble;
import javafx.scene.shape.Rectangle;

public class NewCollisionManager 
{
	private static Marble marble;
	private static List<Rectangle> envShapes;
	private static Vector2d closestEdgeCorner1;
	private static Vector2d closestEdgeCorner2;
	
	public static void initializeCollisionManager(List<Rectangle> env) //ArrayList<Circle> list
	{
		//testList = list;
		envShapes = env;
	}
	
	public static boolean checkCollisionsStart()
	{
		return checkCollisions(envShapes);
	}
	
	public static void setMarble(Marble marble2)
	{
		marble = marble2;
	}
	
	public static boolean checkCollisions(List<Rectangle> shape)
	{
		Rectangle closestRect = null;
		double shortestDistance = 10000;
		
		Vector2d marblePosition =  marble.getCurrentPos(); //getCurrPos
		
		boolean collisionDetected = false;
		closestEdgeCorner1 = new Vector2d(0,0);
		closestEdgeCorner2 = new Vector2d(0,0);
		String edge = "No Edgels";
		
		for(Rectangle rect : shape)
		{
			List<Vector2d> cornerList = shapeToCorners(rect);
			
			//Liste von Distanzen
			//für jede Kante s berechnen
			//Wenn s zwischen 0 und 1 --> berechne Distanz
			//Falls nicht: berechne Distanz nicht.
			//Dann aus der Liste alle Distanzen vergleichen
			
			
			double d1 = calculateDistance(marblePosition, cornerList.get(0), cornerList.get(1));
			double d2 = calculateDistance(marblePosition, cornerList.get(0), cornerList.get(2));
			double d3 = calculateDistance(marblePosition, cornerList.get(1), cornerList.get(3));
			double d4 = calculateDistance(marblePosition, cornerList.get(2), cornerList.get(3));
			
			if(d1 < shortestDistance)
			{
				shortestDistance = d1;
				closestRect = rect;
				closestEdgeCorner1 = cornerList.get(0);
				closestEdgeCorner2 = cornerList.get(1);
				edge = "edge 1";
			}
			if(d2 < shortestDistance)
			{
				shortestDistance = d2;
				closestRect = rect;
				closestEdgeCorner1 = cornerList.get(0);
				closestEdgeCorner2 = cornerList.get(2);
				edge = "edge 2";
			}
			if(d3 < shortestDistance)
			{
				shortestDistance = d3;
				closestRect = rect;
				closestEdgeCorner1 = cornerList.get(1);
				closestEdgeCorner2 = cornerList.get(3);
				edge = "edge 3";
			}
			if(d4 < shortestDistance)
			{
				shortestDistance = d4;
				closestRect = rect;
				closestEdgeCorner1 = cornerList.get(2);
				closestEdgeCorner2 = cornerList.get(3);
				edge = "edge 4";
			}
			
			//später entkommentieren???
			if(shortestDistance <= (marble.getRadius()))
			{
				collisionDetected = true;
				break;
			}
		}
		

		if(closestRect != null)
		{
			//System.out.println("Marble Position: " + marblePos[0] + ", " + marblePos[1]);
		}
		
		if (collisionDetected) 
		{
			System.out.println("(NewCollisionManager) COLLISION DETECTED, Shortest Distance: " + shortestDistance + ", Edge: " + edge );
		}
		return collisionDetected; 
		
	}
	

	
	//Get corner coordinates of a rectangle
	public static List<Vector2d> shapeToCorners(Rectangle rect)
	{
		double width = rect.getWidth();
		double height = rect.getHeight();
		double angle = rect.getRotate();
		double[] rectCenter = new double[2];
		rectCenter[0] = rect.getLayoutX() + 0.5 * width;
		rectCenter[1] = rect.getLayoutY() + 0.5 * height;
		
		
		List<Vector2d> cornerList = new ArrayList<Vector2d>();
		
		Vector2d corner1 = new Vector2d(rect.getLayoutX(), rect.getLayoutY());
		
		Vector2d corner2 = new Vector2d(rect.getLayoutX() + width, rect.getLayoutY());

		Vector2d corner3 = new Vector2d(rect.getLayoutX(), rect.getLayoutY() + height);
		
		Vector2d corner4 = new Vector2d(rect.getLayoutX() + width, rect.getLayoutY() + height);
		
		cornerList.add(corner1);
		cornerList.add(corner2);
		cornerList.add(corner3);
		cornerList.add(corner4);
		
		for(int j = 0 ; j < cornerList.size(); j++)
		{
			//System.out.println("Corner " + j + ": " + cornerList.get(j).getVector2d());
		}
		
		if(angle != 0) //calculate new coordinates, if shape is rotated
		{
			for(int i = 0; i < cornerList.size(); i++)
			{
				Vector2d newCoordinate = calcRotCoordinates(angle, cornerList.get(i), rectCenter);
				cornerList.set(i, newCoordinate);
				
				//System.out.println("New Coordinates: " + newCoordinate.getVector2d());
			}
		}
		
		
		return cornerList;
	}
	
	public static double calculateDistance(Vector2d p, Vector2d q, Vector2d r)
	{
		//marblePosition = P
		//corner1 = Q
		//corner2 = R
		Vector2d rq = r.subtract(q); //Richtungvektor: Ecke 2 - Ecke 1
		Vector2d qp = q.subtract(p); //Ecke1 - marblePosition
		double dot1 = qp.dotProduct(rq) * (-1); //Skalarprodukt: qp * rq
		double dot2 = rq.dotProduct(rq);
		
		double s = dot1/dot2;
		double distance;
		
		if(s>=0 && s<= 1)
		{
			Vector2d lfp = calculateLFP(q, r, s);
			
			Vector2d lp = lfp.subtract(p);
			
			distance = lp.getNorm();
		}
		else
		{
			distance = 1000000;
		}
		
		return distance;
		
	}
	
	public static Vector2d calculateLFP(Vector2d q, Vector2d r, double s)
	{
		Vector2d rq = r.subtract(q); //Richtungvektor: Ecke 2 - Ecke 1
		Vector2d lfp = q.add(rq.multiply(s));
		
		return lfp;
	}
	
	
	//Falls Shape rotiert, rechne neue Eck-Koordinaten
	public static Vector2d calcRotCoordinates(double angle, Vector2d corner, double[] rectCenter)
	{
		double newCornerX;
		double newCornerY;
		
		//Formel: x' = (x - xc) * cos(phi) - (y - yc) * sin(phi) + xc 
		double angleRadian = Math.toRadians(angle);
		newCornerX = (corner.getX() - rectCenter[0]) * Math.cos(angleRadian) - (corner.getY() - rectCenter[1]) * Math.sin(angleRadian) + rectCenter[0];
		newCornerY = ((corner.getX() - rectCenter[0]) * Math.sin(angleRadian) + (corner.getY() - rectCenter[1]) * Math.cos(angleRadian) + rectCenter[1]);
		
		Vector2d newCoordinates = new Vector2d(newCornerX, newCornerY);
		
		return newCoordinates;
	}

	public static Vector2d calculateTranslation(Vector2d marblePosition) 
	{
		Vector2d marbleDV = marble.getDirectionVector(); //Richtungsvektor Murmel
		Vector2d marbleDVreversed = marbleDV.multiply(-1); //umgekehrter Richtungsvektor
		Vector2d marbleDVreversednormalized = marbleDVreversed.normalize();
		
		Vector2d edge = closestEdgeCorner2.subtract(closestEdgeCorner1);
		double angle = marbleDVreversed.calculateAngle(edge);
		double setback;
		if(angle == 0)
		{
			setback = marble.getRadius();
		}
		else
		{
			setback = (marble.getRadius() / Math.sin(angle));
		}
		
		Vector2d collisionPoint = calculateCollisionPoint(marblePosition, closestEdgeCorner1, closestEdgeCorner2);
		
		Vector2d marbleNew = collisionPoint.add(marbleDVreversednormalized.multiply(setback));
		
		double tx = Math.abs(marblePosition.getX() - marbleNew.getX());
		double ty = Math.abs(marblePosition.getY() - marbleNew.getY());
		
		double tx2 = marbleDVreversednormalized.getX() * tx;
		double ty2 = marbleDVreversednormalized.getY() * ty;
		
		Vector2d translation = new Vector2d(tx2, ty2);
		
		System.out.println("Translation: " + translation.getVector2d());
		
		
		return translation;
	}
	
	public static Vector2d calculateCollisionPoint(Vector2d marblePosition, Vector2d q, Vector2d r)
	{
		Vector2d rv = r.subtract(q); //Richtungsvektor Gerade
		Vector2d marbleRV = marble.getDirectionVector();
		Vector2d marbleRVnorm = marbleRV.normalize();
		
		Vector2d mq = q.subtract(marblePosition); //Corner 1 - marblePosition
		Vector2d marbleRVnormT = marbleRVnorm.transform();
		
		double qx = mq.getX() * marbleRVnorm.getX() + mq.getY() * marbleRVnorm.getY();
		double qy = mq.getX() * marbleRVnormT.getX() + mq.getY() * marbleRVnormT.getY();
		
		double sx = rv.getX() * marbleRVnorm.getX() + rv.getY() * marbleRVnorm.getY();
		double sy = rv.getX() * marbleRVnormT.getX() + rv.getY() * marbleRVnormT.getY();
		
		if(sy == 0)
			return null;
		
		double a = qx - qy * sx / sy;
		
		Vector2d collisionPoint = marblePosition.add(marbleRVnorm.multiply(a));
		
		return collisionPoint;
	}
}
