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
		double[] closestEdgeCorner1 = new double[2];
		double[] closestEdgeCorner2 = new double[2];
		String edge = "No Edgels";
		
		for(Rectangle rect : shape)
		{
			System.out.println("NOW CHECKING::::: " + rect.getId());
			List<double[]> cornerList = shapeToCorners(rect);
			
			double distance1 = calculateDistance(marblePos, cornerList.get(0), cornerList.get(1));
			double distance2 = calculateDistance(marblePos, cornerList.get(0), cornerList.get(2));
			double distance3 = calculateDistance(marblePos, cornerList.get(1), cornerList.get(3));
			double distance4 = calculateDistance(marblePos, cornerList.get(2), cornerList.get(3));
			
			if(distance1 < 100)
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
			
			if(shortestDistance < (marble.getRadius()))
			{
				collisionDetected = true;
				break;
			}
		}
		
		System.out.println("shortest distance: " + shortestDistance + " to: " + closestRect.getId());
		System.out.println("Corner1 : " + closestEdgeCorner1[0] + ", " + closestEdgeCorner1[1] );
		System.out.println("Corner2 : " + closestEdgeCorner2[0] + ", " + closestEdgeCorner2[1] );
		System.out.println("Edge: " + edge);
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
		
		if(distance < 2.8)
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
			}
		}
		
		return cornerList;
	}
	

	public static double calculateDistance(double[] marblePos, double[] corner1, double[] corner2)
	{
		boolean isSpecialEdge = checkEdgeType(corner1, corner2);
		double[] closestPointOnEdge;
		
		if(isSpecialEdge)
		{
			closestPointOnEdge = getClosestPointOnEdgeNoLPF(corner1, corner2, marblePos);
		}
		else
		{
			double[] lotfusspunkt = calculateLotfusspunkt(corner1, corner2, marblePos);
			closestPointOnEdge = getClosestPointOnEdge(lotfusspunkt, corner1, corner2);
		}
				
		return calculateDistanceToPoint(marblePos, closestPointOnEdge);
	}
	
	public static boolean checkEdgeType(double[] p1, double[] p2) //ob senkrecht oder waagerecht
	{
		boolean isSpecialEdge = false;
		
		if(p1[0] == p2[0] || p1[1] == p2[1])
		{
			isSpecialEdge = true;
		}
		
		return isSpecialEdge;
	}
	
	public static double[] getClosestPointOnEdgeNoLPF(double[] gP1, double[] gP2, double[] p)
	{
		double[] closestPoint = new double[2];
		double[] buffer;
		
		if(gP1[0] == gP2[0]) //Gerade ist senkrecht
		{
			if(gP1[1] > gP2[1])
			{
				buffer = gP1;
				gP1 = gP2;
				gP2 = buffer;
			}
			
			if(p[1] >= gP1[1] && p[1] <= gP2[1]) //Punkt liegt neben der Geraden
			{
				closestPoint[0] = gP1[0];
				closestPoint[1] = p[1];
			}
			else
			{
				if(p[1] < gP1[1]) //Punkt liegt unter der Geraden
				{
					closestPoint = gP1;
				}
				else //Punkt liegt über der Geraden
				{
					closestPoint = gP2;
				}
			}
		}
		
		if(gP1[1] == gP2[1])
		{
			if(p[0] >= gP1[0] && p[0] <= gP2[0]) //Punkt liegt ueber/unter der Geraden
			{
				closestPoint[0] = p[0];
				closestPoint[1] = gP1[1];
			}
			else
			{
				if(p[1] < gP1[1]) //Punkt links von der Geraden
				{
					closestPoint = gP1;
				}
				else //Punkt liegt rechts von der Geraden
				{
					closestPoint = gP2;
				}
			}
		}
		return closestPoint;
	}
	
	public static double[] calculateLotfusspunkt(double[] gP1, double[] gP2, double[] punkt)
	{
		double m1 = berechneSteigung(gP1, gP2);
		double b1 = berechneYAchsenabschnitt(gP1, m1);
		
		double m2 = - (1)/(m1);
		double b2 = berechneYAchsenabschnitt(punkt, m2);
		
		//X- und Y-Koordinate vom Lotfußpunkt
		double lotfusspunktX = berechneGeradenschnittpunktX(m1, m2, b1, b2);
		double lotfusspunktY = berechneGeradenschnittpunktY(lotfusspunktX, m1, b1);
		
		System.out.println("Schnittpunkt: " + lotfusspunktX + "/" + lotfusspunktY);
		
		double[] lotfusspunkt = new double[2];
		lotfusspunkt[0] = lotfusspunktX;
		lotfusspunkt[1] = lotfusspunktY;
		
		return lotfusspunkt;
	}
	
	public static double berechneSteigung(double[] p1, double[] p2)
	{
		double m, g;
		
		m = 1.0*(p2[1]-p1[1])/(p2[0]-p1[0]);
		
		return m;
	}
	
	public static double berechneYAchsenabschnitt(double[] p, double m)
	{
		double b;
		
		b = p[1] - m * p[0];
		
		return b;
	}
	
	public static double berechneGeradenschnittpunktX(double m1, double m2, double b1, double b2)
	{
		double schnittpunkt;
		
		schnittpunkt = (-b1+b2)/(m1-m2);
		
		return schnittpunkt;
	}
	
	public static double berechneGeradenschnittpunktY(double x, double m, double b)
	{
		double schnittpunktY;
		
		schnittpunktY = m * x + b;
		
		return schnittpunktY;
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
		
		System.out.println("Punkt: " + p1[0] + "|" + p1[1] + ", closest point: " + p2[0] + "|" + p2[1] );
		System.out.println("Distance: " + distance);
		
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
	
	public void calculateAngle(double[] p1, double[] p2, double[] marbleDirectionVect)
	{
		double[] edgeNormal = edgeToNormal(p1, p2);
		
		double dotProduct = edgeNormal[0]*marbleDirectionVect[0] + edgeNormal[1]*marbleDirectionVect[1];
		double edgeNormalLength = Math.sqrt(Math.pow(edgeNormal[0], 2) + Math.pow(edgeNormal[1], 2));
		double marbleVectLength = Math.sqrt(Math.pow(marbleDirectionVect[0], 2) + Math.pow(marbleDirectionVect[1], 2));
		
		double angle = dotProduct/(edgeNormalLength * marbleVectLength);
		
		System.out.println("Winkel: " + angle);
		
	}
	
	//Get normal vector to a edge made of two points
	public double[] edgeToNormal(double[] p1, double[] p2)
	{
		double[] normal = new double[2];
		normal[0] = p1[1] - p2[1];
		normal[1] = -(p1[0] - p2[0]);
		
		return normal;
	}
}
