package de.celineevelyn.kugelbahn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import de.celineevelyn.kugelbahn.objects.Marble;
import javafx.scene.shape.Rectangle;

public class NewCollisionManager 
{
	private static Marble marble;
	private static List<Rectangle> envShapes;
	private static RealVector closestEdgeCorner1;
	private static RealVector closestEdgeCorner2;
	
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
		
		RealVector marblePosition = new ArrayRealVector(2);
		
		marblePosition.setEntry(0, marblePos[0]);
		marblePosition.setEntry(1, marblePos[1]);
		
		
		boolean collisionDetected = false;
		closestEdgeCorner1 = new ArrayRealVector(2);
		closestEdgeCorner2 = new ArrayRealVector(2);
		String edge = "No Edgels";
		
		for(Rectangle rect : shape)
		{
			List<RealVector> cornerList = shapeToCorners(rect);
			
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
		return collisionDetected;
		
	}
	
	//Get corner coordinates of a rectangle
	public static List<RealVector> shapeToCorners(Rectangle rect)
	{
		double width = rect.getWidth();
		double height = rect.getHeight();
		double angle = rect.getRotate();
		double[] rectCenter = new double[2];
		rectCenter[0] = rect.getLayoutX() + 0.5 * width;
		rectCenter[1] = rect.getLayoutY() + 0.5 * height;
		
		
		List<RealVector> cornerList = new ArrayList<RealVector>();
		
		RealVector corner1 = new ArrayRealVector(2);
		corner1.setEntry(0, rect.getLayoutX());
		corner1.setEntry(1, rect.getLayoutY());
		
		RealVector corner2 = new ArrayRealVector(2);
		corner2.setEntry(0, rect.getLayoutX() + width);
		corner2.setEntry(1, rect.getLayoutY());
		
		RealVector corner3 = new ArrayRealVector(2);
		corner3.setEntry(0, rect.getLayoutX());
		corner3.setEntry(1, rect.getLayoutY() + height);
		
		RealVector corner4 = new ArrayRealVector(2);
		corner4.setEntry(0, rect.getLayoutX() + width);
		corner4.setEntry(1, rect.getLayoutY() + height);
		
//		double[] corner1 = {rect.getLayoutX(), rect.getLayoutY()}; //oben links
//		double[] corner2 = {rect.getLayoutX() + width, rect.getLayoutY()}; //oben rechts
//		double[] corner3 = {rect.getLayoutX(), rect.getLayoutY() + height}; //unten links
//		double[] corner4 = {rect.getLayoutX() + width, rect.getLayoutY() + height}; //unten rechts
		
		cornerList.add(corner1);
		cornerList.add(corner2);
		cornerList.add(corner3);
		cornerList.add(corner4);
		
		if(angle != 0)
		{
			for(int i = 0; i < cornerList.size(); i++)
			{
				RealVector newCoordinate = calcRotCoordinates(angle, cornerList.get(i), rectCenter);
				cornerList.set(i, newCoordinate);
				
//				System.out.println("New Coordinates: " + newCoordinate[0] + ", " + newCoordinate[1]);
			}
		}
		
		return cornerList;
	}
	
	@SuppressWarnings("exports")
	public static double calculateDistance(RealVector marblePosition, RealVector corner1, RealVector corner2)
	{
		//NEU
		
		RealVector c = marblePosition.subtract(corner1);
		RealMatrix outerProductMatrix = c.outerProduct(corner2);
		RealVector outerProduct = outerProductMatrix.getColumnVector(0);
		double norm1 = outerProduct.getNorm();
		double norm2 = corner2.getNorm();
		
		double distance =  norm1 / norm2;
				
		return distance;
	}
	
	//Falls Shape rotiert, rechne neue Eck-Koordinaten
	public static RealVector calcRotCoordinates(double angle, RealVector corner, double[] rectCenter)
	{
		RealVector newCoordinates = new ArrayRealVector(2);
		double newCornerX;
		double newCornerY;
		
		double[] cornerDouble = corner.toArray();
		
		//Formel: x' = (x - xc) * cos(phi) - (y - yc) * sin(phi) + xc 
		double angleRadian = Math.toRadians(angle);
		newCornerX = (cornerDouble[0] - rectCenter[0]) * Math.cos(angleRadian) - (cornerDouble[1] - rectCenter[1]) * Math.sin(angleRadian) + rectCenter[0];
		newCornerY = ((cornerDouble[0] - rectCenter[0]) * Math.sin(angleRadian) + (cornerDouble[1] - rectCenter[1]) * Math.cos(angleRadian) + rectCenter[1]);
		
		newCoordinates.setEntry(0, newCornerX);
		newCoordinates.setEntry(1, newCornerY);
		
		return newCoordinates;
	}
}
