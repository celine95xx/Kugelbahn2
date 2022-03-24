package de.celineevelyn.kugelbahn;

import java.util.ArrayList;
import java.util.List;

import de.celineevelyn.kugelbahn.objects.BasicNode;
import de.celineevelyn.kugelbahn.objects.Marble;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class CollisionManager 
{
	private static ArrayList<Circle> testList;
	private static Marble marble;
	private static List<Rectangle> envShapes;
	private static double[] closestEdgeCorner1;
	private static double[] closestEdgeCorner2;
	private static double[] perpendicularFoot;
	
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
		boolean collisionDetected = false;
//		double[] closestEdgeCorner1 = new double[2];
//		double[] closestEdgeCorner2 = new double[2];
		closestEdgeCorner1 = new double[2];
		closestEdgeCorner2 = new double[2];
		String edge = "No Edgels";
		
		for(Rectangle rect : shape)
		{
//			System.out.println("NOW CHECKING::::: " + rect.getId());
			List<double[]> cornerList = shapeToCorners(rect);
			
			double distance1 = calculateDistance(marblePos, cornerList.get(0), cornerList.get(1)); //Edge oben
			double distance2 = calculateDistance(marblePos, cornerList.get(0), cornerList.get(2)); //Edge links
			double distance3 = calculateDistance(marblePos, cornerList.get(1), cornerList.get(3)); //Edge rechts
			double distance4 = calculateDistance(marblePos, cornerList.get(2), cornerList.get(3)); //Edge unten
			
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
		
//		System.out.println("shortest distance: " + shortestDistance + " to: " + closestRect.getId());
//		System.out.println("Corner1 : " + closestEdgeCorner1[0] + ", " + closestEdgeCorner1[1] );
//		System.out.println("Corner2 : " + closestEdgeCorner2[0] + ", " + closestEdgeCorner2[1] );
//		System.out.println("Edge: " + edge);
		if(closestRect != null)
		{
			System.out.println("Marble Position: " + marblePos[0] + ", " + marblePos[1]);
		}
		
		return collisionDetected;
		
	}
	
	//SECOND MARBLE
	public static boolean checkCollisionTest()
	{
		boolean collided = false;
		
		Circle collisionCircle = testList.get(0);
		
		
		double marbleXPosition = marble.getStartX() + marble.getNode().getTranslateX();
		double marbleYPosition = marble.getStartY()  + marble.getNode().getTranslateY();
		double marbleRadius = marble.getRadius();
		
		double ccXPosition = collisionCircle.getLayoutX();
		double ccYPosition = collisionCircle.getLayoutY();
		double ccRadius = collisionCircle.getRadius();
		
		double distance = Math.abs(Math.sqrt(Math.pow((ccXPosition-marbleXPosition), 2) + Math.pow((ccYPosition-marbleYPosition), 2)) - (marbleRadius + ccRadius));
		System.out.println("Distance: " + distance);
		System.out.println("Marble X : " + marbleXPosition + " , Marble Y : " + marbleYPosition + "| CC X : " + ccXPosition + "cc Y : " + ccYPosition);
		System.out.println("RADIUS -- Marble : " + marbleRadius + " | CC: " + ccRadius);
		
		if(distance < marbleRadius)
		{
			collided = true;
		}

		return collided;
	}

	//Get corner coordinates of a rectangle
	public static List<double[]> shapeToCorners(Rectangle rect)
	{
		double width = rect.getWidth();
		double height = rect.getHeight();
		double angle = rect.getRotate();
		double[] rectCenter = new double[2];
		rectCenter[0] = rect.getLayoutX() + 0.5 * width;
		rectCenter[1] = rect.getLayoutY() + 0.5 * height;
		
		
		List<double[]> cornerList = new ArrayList<double[]>();
		
		double[] corner1 = {rect.getLayoutX(), rect.getLayoutY()}; //oben links
		double[] corner2 = {rect.getLayoutX() + width, rect.getLayoutY()}; //oben rechts
		double[] corner3 = {rect.getLayoutX(), rect.getLayoutY() + height}; //unten links
		double[] corner4 = {rect.getLayoutX() + width, rect.getLayoutY() + height}; //unten rechts
		
		cornerList.add(corner1);
		cornerList.add(corner2);
		cornerList.add(corner3);
		cornerList.add(corner4);
		
		if(angle != 0)
		{
			for(int i = 0; i < cornerList.size(); i++)
			{
				double[] newCoordinate = calcRotCoordinates(angle, cornerList.get(i), rectCenter);
				cornerList.set(i, newCoordinate);
				
//				System.out.println("New Coordinates: " + newCoordinate[0] + ", " + newCoordinate[1]);
			}
		}
		
		return cornerList;
	}
	

	public static double calculateDistance(double[] marblePos, double[] corner1, double[] corner2)
	{
		if(corner1[0] > corner2[0])
		{
			double[] buffer = corner1;
			corner1 = corner2;
			corner2 = buffer;
			//System.out.println("Points were reversed");
		}
		
		boolean isSpecialEdge = MathUtil.checkEdgeType(corner1, corner2);
		double[] closestPointOnEdge;
		
		if(isSpecialEdge)
		{
			closestPointOnEdge = MathUtil.getClosestPointOnEdgeNoLFP(corner1, corner2, marblePos);
		}
		else
		{
			double[] lotfusspunkt = calculateLotfusspunkt(corner1, corner2, marblePos);
			closestPointOnEdge = getClosestPointOnEdge(lotfusspunkt, corner1, corner2);
		}
				
		return calculateDistanceToPoint(marblePos, closestPointOnEdge);
	}
	
	
	public static double[] calculateLotfusspunkt(double[] gP1, double[] gP2, double[] punkt)
	{
		double m1 = MathUtil.berechneSteigung(gP1, gP2);
		double b1 = MathUtil.berechneYAchsenabschnitt(gP1, m1);
		
		double m2 = - (1)/(m1);
		double b2 = MathUtil.berechneYAchsenabschnitt(punkt, m2);
		
		//X- und Y-Koordinate vom Lotfußpunkt
		double lotfusspunktX = MathUtil.berechneGeradenschnittpunktX(m1, m2, b1, b2);
		double lotfusspunktY = MathUtil.berechneGeradenschnittpunktY(lotfusspunktX, m1, b1);
		
		
//		System.out.println("Schnittpunkt: " + lotfusspunktX + "/" + lotfusspunktY);
		
		perpendicularFoot = new double[2];
		perpendicularFoot[0] = lotfusspunktX;
		perpendicularFoot[1] = lotfusspunktY;
		
//		System.out.println("Lotfußpunkt: " + lotfusspunktX + "|" + lotfusspunktY);
		
		return perpendicularFoot;
	}
	
	public static double[] getClosestPointOnEdge(double[] lotfusspunkt, double[] p1, double[] p2)
	{
		double[] closestPoint = new double[2];
		
			if(lotfusspunkt[0] >= p1[0] && lotfusspunkt[0] <= p2[0] )
			{
				closestPoint = lotfusspunkt;
			}
			else
			{
				if(lotfusspunkt[0] < p1[0])
				{
					closestPoint = p1;
				}
				else
				{
					closestPoint = p2;
				}
			}
		
		return closestPoint;
	}
	
	public static double calculateDistanceToPoint(double[] p1, double[] p2)
	{
		double distance = Math.sqrt(Math.pow((p2[0]-p1[0]),2)+Math.pow((p2[1]-p1[1]),2));;
		
		return distance;
	}
	
	public static double[] calcRotCoordinates(double angle, double[] corner, double[] rectCenter)
	{
		double[] newCoordinates = new double[2];
		//Formel: x' = (x - xc) * cos(phi) - (y - yc) * sin(phi) + xc 
		double angleRadian = Math.toRadians(angle);
		newCoordinates[0] = (corner[0] - rectCenter[0]) * Math.cos(angleRadian) - (corner[1] - rectCenter[1]) * Math.sin(angleRadian) + rectCenter[0];
		newCoordinates[1] = ((corner[0] - rectCenter[0]) * Math.sin(angleRadian) + (corner[1] - rectCenter[1]) * Math.cos(angleRadian) + rectCenter[1]);
		
		return newCoordinates;
	}
	
	//Get normal vector of an edge made of two points
	public double[] edgeToNormal(double[] p1, double[] p2)
	{
		double[] normal = new double[2];
		normal[0] = p1[1] - p2[1];
		normal[1] = -(p1[0] - p2[0]);
		
		return normal;
	}
	
	public static double[] getClosestEdgeCorner1()
	{
		return closestEdgeCorner1;
	}
	
	public static double[] getClosestEdgeCorner2()
	{
		return closestEdgeCorner2;
	}
	
	public static double[] getPerpendicularFoot()
	{
		return perpendicularFoot;
	}
}
