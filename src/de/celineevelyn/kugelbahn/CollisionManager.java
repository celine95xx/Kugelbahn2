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
		
		getShapeInfos(envShapes);
	}
	
	public static void getShapeInfos(List<Rectangle> shape)
	{
		for(Rectangle rect : shape)
		{
			shapeToCorners(rect);
		}
	}
	
	public static void setMarble(Marble marble2)
	{
		marble = marble2;
	}
	
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
	public static void shapeToCorners(Rectangle rect)
	{
		double[][] corners = new double[4][2];
		double width = rect.getWidth();
		double height = rect.getHeight();
		
		corners[0][0] = rect.getLayoutX(); //oben links
		corners[0][1] = rect.getLayoutY();
		
		corners[1][0] = rect.getLayoutX() + width; //oben rechts
		corners[1][1] = rect.getLayoutY();
		
		corners[2][0] = rect.getLayoutX(); //unten links
		corners[2][1] = rect.getLayoutY() + height;
		
		corners[3][0] = rect.getLayoutX() + width; //unten rechts
		corners[3][1] = rect.getLayoutY() + height;
		
		System.out.println("Width: " + width + "| Heigth: " + height);
		System.out.println("Ecke 1: " + corners[0][0] + "|" + corners[0][1]);
		System.out.println("Ecke 2: " + corners[1][0] + "|" + corners[1][1]);
		System.out.println("Ecke 3: " + corners[2][0] + "|" + corners[2][1]);
		System.out.println("Ecke 4: " + corners[3][0] + "|" + corners[3][1]);
	}
	
	/**
	 * Calculate angle between the normal vector of an edge of a shape and the direction vector of the marble.
	 * The marble can only potentially collide with an edge, if the angle is 45 degree max.
	 * @param p1
	 * @param p2
	 * @param marbleDirectionVect
	 */
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
