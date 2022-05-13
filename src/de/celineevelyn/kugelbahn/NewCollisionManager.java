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
		double[] marblePos = marble.getCurrentPos();
		
		Vector2d marblePosition = new Vector2d(marblePos[0], marblePos[1]);	
		
		boolean collisionDetected = false;
		boolean isIntersected = false;
		closestEdgeCorner1 = new Vector2d(0,0);
		closestEdgeCorner2 = new Vector2d(0,0);
		String edge = "No Edgels";
		
		for(Rectangle rect : shape)
		{
			List<Vector2d> cornerList = shapeToCorners(rect);
			
			isIntersected = checkIntersection(marblePos, cornerList);
			
			double distance1 = calculateDistance(marblePosition, cornerList.get(0), cornerList.get(1)); //Edge oben
			double distance2 = calculateDistance(marblePosition, cornerList.get(0), cornerList.get(2)); //Edge links
			double distance3 = calculateDistance(marblePosition, cornerList.get(1), cornerList.get(3)); //Edge rechts
			double distance4 = calculateDistance(marblePosition, cornerList.get(2), cornerList.get(3)); //Edge unten
			
			System.out.println("DISTANCES: 1 = " + distance1 + ", 2 = " + distance2 + ", 3 = " + distance3 + ", 4 = " + distance4);
			
			if(distance1 < shortestDistance)
			{
				shortestDistance = distance1;
				closestRect = rect;
				closestEdgeCorner1 = cornerList.get(0);
				closestEdgeCorner2 = cornerList.get(1);
				edge = "edge 1";
			}
			if(distance2 < shortestDistance)
			{
				shortestDistance = distance2;
				closestRect = rect;
				closestEdgeCorner1 = cornerList.get(0);
				closestEdgeCorner2 = cornerList.get(2);
				edge = "edge 2";
			}
			if(distance3 < shortestDistance)
			{
				shortestDistance = distance3;
				closestRect = rect;
				closestEdgeCorner1 = cornerList.get(1);
				closestEdgeCorner2 = cornerList.get(3);
				edge = "edge 3";
			}
			if(distance4 < shortestDistance)
			{
				shortestDistance = distance4;
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
			System.out.println("Marble Position: " + marblePos[0] + ", " + marblePos[1]);
		}
		
		if (collisionDetected) 
		{
			System.out.println("(NewCollisionManager) COLLISION DETECTED, Shortest Distance: " + shortestDistance + ", Edge: " + edge );
		}
		return isIntersected; //collisionDetected
		
	}
	
	public static boolean checkIntersection(double[] marblePos, List<Vector2d> cornerList)
	{
		boolean intersected = false;
		
		if((marblePos[0] + marble.getRadius() >= cornerList.get(0).getX()) && marblePos[0] <= cornerList.get(1).getX() && 
				(marblePos[1] + marble.getRadius() >= cornerList.get(0).getY()) && marblePos[1] <= cornerList.get(2).getY())
		{
			intersected = true;
		}
		
		return intersected;
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
			System.out.println("Corner " + j + ": " + cornerList.get(j).getVector2d());
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
	
	@SuppressWarnings("exports")
	public static double calculateDistance(Vector2d marblePosition, Vector2d corner1, Vector2d corner2)
	{

		// d = |(p-a) x b| / |b| 
		// p: Mittelpunkt der Murmel
		// a: Ecke 1
		// b : Ecke 2 - Ecke 1
		
		Vector2d c = marblePosition.subtract(corner1); // p-a
		Vector2d b = corner2.subtract(corner1); //Richtungsvektor b = Ecke 2 - Ecke 1
		double detAbsolute = Math.abs(c.crossProduct(b)); //Betrag der Determinante
		double bLengthAbsolute = Math.abs(b.getNorm());
		
		System.out.println("Marble Position - Corner 1 = " + marblePosition.getVector2d() + " - " + corner1.getVector2d() + " = " + c.getVector2d() );
		System.out.println("b = " + b.getVector2d());
		System.out.println("norm1 = " + detAbsolute);
		System.out.println("norm2 = " + bLengthAbsolute);

		double distance =  detAbsolute / bLengthAbsolute;
		
		System.out.println("CALCULATED DISTANCE: " + distance);
				
		return distance;
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
}
