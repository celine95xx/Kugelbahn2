package de.celineevelyn.kugelbahn;

import java.util.ArrayList;
import java.util.List;
import de.celineevelyn.kugelbahn.objects.Marble;
import de.celineevelyn.kugelbahn.objects.Scissors;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;


/**
 * 
 * @author Celine Viehmann
 * @author Evelyn Romanjuk
 *
 */

public class NewCollisionManager 
{
	private static Marble m;
	private static List<Node> envShapes;
	private static Vector2d closestEdgeCorner1;
	private static Vector2d closestEdgeCorner2;
//	private static Rectangle closestRect;
	private static Node closestNode;
	private static Marble collisionMarble;
	private static Scissors scissors;
	private static boolean isBlade = false;
	private static boolean collisionWithBlade = false;
	
	public static <envShapes> void initializeCollisionManager(List<Node> env)
	{
		envShapes = env;
	}
	
	public static int checkCollisionsStart(Marble marble)
	{
		m = marble;
		return checkCollisions(envShapes);
	}
	
	public static int checkCollisions(List<Node> nodes)
	{
		closestNode = null;
		double shortestDistance = 10000;
		int collisionType = 0; //0 = kein Beruehrung, 1 = Kollision, 2 = Kontakt
		
		Vector2d marblePosition =  m.getCurrentPos();
		
		closestEdgeCorner1 = new Vector2d(0,0);
		closestEdgeCorner2 = new Vector2d(0,0);
		String edge = "No Edgels";
		
		for(Node n : nodes)
		{
			if(n instanceof Rectangle || n instanceof Scissors) //Fall Rectangles
			{
				if(n instanceof Scissors) // konkreter Fall Schere
				{
					scissors = (Scissors) n;
					n = ((Scissors) n).getNode();
					isBlade = true;
				}
				else
					isBlade = false;
					
				Rectangle rect = (Rectangle) n;
				
				List<Vector2d> cornerList = shapeToCorners(rect);
				
				double d1 = calculateDistanceToRectangle(marblePosition, cornerList.get(0), cornerList.get(1)); //oben
				double d2 = calculateDistanceToRectangle(marblePosition, cornerList.get(2), cornerList.get(0)); //links
				double d3 = calculateDistanceToRectangle(marblePosition, cornerList.get(1), cornerList.get(3)); //rechts
				double d4 = calculateDistanceToRectangle(marblePosition, cornerList.get(3), cornerList.get(2)); //unten
				
				
				if(d1 < shortestDistance)
				{
					shortestDistance = d1;
					closestNode = rect;
					closestEdgeCorner1 = cornerList.get(0);
					closestEdgeCorner2 = cornerList.get(1);
					edge = "edge 1";
				}
				if(d2 < shortestDistance)
				{
					shortestDistance = d2;
					closestNode = rect;
					closestEdgeCorner1 = cornerList.get(2);
					closestEdgeCorner2 = cornerList.get(0);
					edge = "edge 2";
				}
				if(d3 < shortestDistance)
				{
					shortestDistance = d3;
					closestNode = rect;
					closestEdgeCorner1 = cornerList.get(1);
					closestEdgeCorner2 = cornerList.get(3);
					edge = "edge 3";
				}
				if(d4 < shortestDistance)
				{
					shortestDistance = d4;
					closestNode = rect;
					closestEdgeCorner1 = cornerList.get(3);
					closestEdgeCorner2 = cornerList.get(2);
					edge = "edge 4";
				}
			}
			else //Fall Murmel
			{
				if(!n.equals(m))
				{
					Marble cm = (Marble) n;
					
					double d = calculateDistanceToCircle(marblePosition, cm);
					
					if(d < shortestDistance)
					{
						shortestDistance = d;
						closestNode = n;
						collisionMarble = (Marble) n;
					}
				}
			}
			
			if(shortestDistance <= (m.getRadius() - 0.1))
			{
				collisionType = 1;
				
				break;
			}
		}
		
		
		if ((closestNode instanceof Rectangle || closestNode instanceof Scissors) & collisionType == 1) //Falls Kollision vorliegt, noch einmal differenzieren, fuer Rectangles
		{
			Vector2d marbleVelocity = new Vector2d( m.getCurrentVelocityX(), m.getCurrentVelocityY());
			Vector2d edgeNormal = (closestEdgeCorner2.subtract(closestEdgeCorner1)).normal();
			
			if(isBlade)
				collisionWithBlade = true;
			else
				collisionWithBlade = false;
			
			if(isParallel())
			{
				collisionType = 2; //Kontakt
			}
			else if(marbleVelocity.dotProduct(edgeNormal) > 0) //Bedingung aus dem Skript, relative Geschwindigkeit mal Beruehrnormale groesser Null
			{
				collisionType = 1;
			}
			else
			{
				collisionType = 0;
			}		
		}
		
		
		if(closestNode instanceof Marble & collisionType == 1) //Falls Kollision vorliegt, noch einmal differenzieren, fuer Marble
		{
			Vector2d marbleVelocity = new Vector2d( m.getCurrentVelocityX(), m.getCurrentVelocityY());
			Vector2d normal = collisionMarble.getCurrentPos().subtract(m.getCurrentPos());
			
			if(marbleVelocity.dotProduct(normal) > 0)
				collisionType = 1;
			else
				collisionType = 0;
		}
		
		return collisionType; 
		
}
	
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
		
		if(angle != 0) //neue Koordinaten berechnen, falls Shape rotiert ist
		{
			for(int i = 0; i < cornerList.size(); i++)
			{
				Vector2d newCoordinate = calcRotCoordinates(angle, cornerList.get(i), rectCenter);
				cornerList.set(i, newCoordinate);
			}
		}
	
		return cornerList;
	}
	
	public static double calculateDistanceToRectangle(Vector2d p, Vector2d q, Vector2d r)
	{
		Vector2d rq = r.subtract(q); //Richtungvektor: Ecke 2 - Ecke 1
		Vector2d qp = q.subtract(p); //Ecke1 - marblePosition
		double dot1 = qp.dotProduct(rq) * (-1); //Skalarprodukt: qp * rq
		double dot2 = rq.dotProduct(rq);
		
		double s = dot1/dot2;
		double distance;
		
		if(s>=0 && s<= 1) //LFP ist auf Kante
		{
			Vector2d lfp = calculateLFP(q, r, s);
			
			Vector2d lp = lfp.subtract(p);
			
			distance = lp.getNorm();
		}
		else //LFP ist ausserhalb
		{
			distance = 1000000;
		}
		
		return distance;
	}
	
	
	public static Vector2d calculateLFP(Vector2d q, Vector2d r, double s)
	{
		Vector2d rq = r.subtract(q); //nochmal Richtungsvektor aufstellen
		Vector2d lfp = q.add(rq.multiply(s)); //Geradengleichung aufstellen und Parameter s einsetzen
		
		return lfp;
	}
	
	
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
	
	public static double calculateDistanceToCircle(Vector2d m1, Marble cm)
	{
		Vector2d cmPos = cm.getCurrentPos(); //Mittelpunkt der collisionMarble
		Vector2d m2m1 = cmPos.subtract(m1); //Richtungvektor: Ecke 2 - Ecke 1
		
		double distance = m2m1.getNorm() - m.getRadius() - cm.getRadius();
		
		return distance;	
	}
	
	public static Vector2d calculateRollPosition(Vector2d marblePosition)
	{
		return new Vector2d(0,0);
	}

	public static Vector2d calculateSetbackPosition(Vector2d marblePosition) 
	{
		Vector2d marbleDV = m.getDirectionVector(); //Richtungsvektor Murmel
		Vector2d marbleDVreversed = marbleDV.multiply(-1); //umgekehrter Richtungsvektor
		Vector2d marbleDVreversednormalized = marbleDVreversed.normalize(); //als Einheitsvektor

		double setback3;
		Vector2d collisionPoint = marblePosition;
		Vector2d marbleNew;
		
		if(closestNode instanceof Marble)
		{
			Vector2d cmPosition = collisionMarble.getCurrentPos();
			setback3 = m.getRadius();
			collisionPoint = cmPosition.add(marbleDVreversednormalized.multiply(collisionMarble.getRadius()));
		}
		
		else
		{
			Vector2d edge = closestEdgeCorner2.subtract(closestEdgeCorner1);
			double angle = Math.round(marbleDVreversed.calculateAngle(edge)*10000)/10000.0; //Ebenenwinkel gerundet auf 4 Nachkommastellen
			double boxAngle = closestNode.getRotate();
			System.out.println("marble DV: " + marbleDVreversednormalized.getVector2d() + ", Angle in Degrees: " + Math.toDegrees(angle) + ", Angle in Radian " + angle);
			
			if(closestNode instanceof Marble || boxAngle == 0 & m.getDirectionVector().getX() != 0) //Wenn Kollision mit einer Murmel oder Winkel = 90 Grad
			{
				setback3 = m.getRadius();
			}
			else
			{
				setback3 = m.getRadius() / Math.sin(angle);
			}
			
			collisionPoint = calculateCollisionPoint(marblePosition, closestEdgeCorner1, closestEdgeCorner2);
			
			if(collisionPoint == null)
			{
				collisionPoint = marblePosition;
			}
		}
		
		marbleNew = collisionPoint.add(marbleDVreversednormalized.multiply(setback3)); //Vom Kollisionspunkt eine bestimmte Strecke in Bewegungsrichtung zurueck
		
		return marbleNew;
	}
	
	public static Vector2d calculateCollisionPoint(Vector2d marblePosition, Vector2d q, Vector2d r)
	{
		//Gleichungssystem loesen mit Cramersche Regel: http://www.feuerbachers-matheseite.de/Cramer.pdf
		//Schnittpunkt zweier Geraden
		Vector2d marbleDirection = m.getDirectionVector();
		Vector2d edgeDirection = r.subtract(q);
		Vector2d relativeDirection = q.subtract(marblePosition);
		
		double a1 = marbleDirection.getX();
		double a2 = marbleDirection.getY();
		double b1 = edgeDirection.getX();
		double b2 = edgeDirection.getY();
		double c1 = relativeDirection.getX();
		double c2 = relativeDirection.getY();
		
		double param =  (c1*b2 - b1*c2) / (a1*b2 - b1*a2);
		if(Math.abs(param) == Double.POSITIVE_INFINITY)
			return null;
		
		Vector2d collisionPoint = marblePosition.add(marbleDirection.multiply(param));
		return collisionPoint;
	}

	public static Vector2d calculatePostCollisionVel1() 
	{
		Vector2d marbleVelocity = new Vector2d( m.getCurrentVelocityX(), m.getCurrentVelocityY());
		double marbleMass = m.getWeight();
		
		Vector2d normal = new Vector2d(0,0);
		Vector2d csVelocity = new Vector2d(0,0); //collisionShape Velocity
		double csMass = 0;
		double coeff = 0.8;
		
		if(closestNode instanceof Marble) //Fall Murmel
		{
			csMass = collisionMarble.getWeight();
			csVelocity = new Vector2d( collisionMarble.getCurrentVelocityX(), collisionMarble.getCurrentVelocityY());
			normal = collisionMarble.getCurrentPos().subtract(m.getCurrentPos());
		}
		else //Fall Rectangle 
		{
			if(isBlade)
				csMass = scissors.getWeight();
			else
				csMass = 1000;
			
			csVelocity = new Vector2d(0,0);
			normal = (closestEdgeCorner2.subtract(closestEdgeCorner1)).normal();
		}
		
		//aktive Murmel
		Vector2d par = normal.normalize().multiply(marbleVelocity.dotProduct(normal.normalize())); //Geschwindigkeit parallel, berechnet ueber orthogonale Projektion: https://de.wikipedia.org/wiki/Skalarprodukt#Orthogonalit%C3%A4t_und_orthogonale_Projektion
		Vector2d orth = marbleVelocity.subtract(par); //Geschwindigkeit orthogonal
		
		//Kollisionsshape
		Vector2d csPar = normal.normalize().multiply(csVelocity.dotProduct(normal.normalize())); 
		Vector2d velDifference = par.subtract(csPar);
		Vector2d parNew = ((par.multiply(marbleMass).add((csPar.multiply(csMass))))
				.subtract((velDifference.multiply(csMass).multiply(coeff)))).divide(marbleMass + csMass);

		Vector2d newVel = orth.add(parNew);
		
		return newVel;
	}
	
	public static Vector2d calculatePostCollisionVel2() 
	{
		Vector2d marbleVelocity = new Vector2d( m.getCurrentVelocityX(), m.getCurrentVelocityY());
		double marbleMass = m.getWeight();
		
		Vector2d normal = new Vector2d(0,0);
		Vector2d csVelocity = new Vector2d(0,0); //collisionShape Velocity
		double csMass = 0;
		double coeff = 0.9;
		
		if(closestNode instanceof Marble) //Fall Murmel
		{
			csMass = collisionMarble.getWeight();
			csVelocity = new Vector2d( collisionMarble.getCurrentVelocityX(), collisionMarble.getCurrentVelocityY());
			
			normal = collisionMarble.getCurrentPos().subtract(m.getCurrentPos());
		}
		else //Fall Rectangle
		{ 	
			if(isBlade)
				csMass = scissors.getWeight();
			else
				csMass = 1000;
			
			csVelocity = new Vector2d(0,0);
			normal = (closestEdgeCorner2.subtract(closestEdgeCorner1)).normal();
		}
		
		//aktive Murmel
		Vector2d par = normal.normalize().multiply(marbleVelocity.dotProduct(normal.normalize())); //Geschwindigkeit parallel, berechnet ueber orthogonale Projektion: https://de.wikipedia.org/wiki/Skalarprodukt#Orthogonalit%C3%A4t_und_orthogonale_Projektion
		
		//Kollisionsshape
		Vector2d csPar = normal.normalize().multiply(csVelocity.dotProduct(normal.normalize())); //Geschwindigkeit parallel, berechnet ueber orthogonale Projektion: https://de.wikipedia.org/wiki/Skalarprodukt#Orthogonalit%C3%A4t_und_orthogonale_Projektion
		Vector2d csOrth = csVelocity.subtract(csPar); //Geschwindigkeit orthogonal

		
		Vector2d velDifference = csPar.subtract(par);
		
		//Neue Formel fuer kollisionsmurmel
		Vector2d parNew = ((par.multiply(marbleMass).add((csPar.multiply(csMass)))).subtract((velDifference.multiply(marbleMass).multiply(coeff)))).divide(marbleMass + csMass);
		
		Vector2d newVel = csOrth.add(parNew);
		
		return newVel;
	}
	
	public static Vector2d calcRelativeVelocityToEdge()
	{
		Vector2d marbleVelocity = new Vector2d( m.getCurrentVelocityX(), m.getCurrentVelocityY());
		Vector2d edgeNormal = (closestEdgeCorner2.subtract(closestEdgeCorner1)).normal();
		
		Vector2d par = edgeNormal.normalize().multiply(marbleVelocity.dotProduct(edgeNormal.normalize())); //"gruener Pfeil" berechnet ueber orthogonale Projektion --> Murmel fliegt sehr tief wenn der Wert klein ist
		
		return par;
	}
	
	private static boolean isParallel() 
	{
		boolean isParallel = false;
		double angle = Math.toRadians(closestNode.getRotate());
		System.out.println("GET ROTATE: " + Math.toDegrees(angle));	
		
		Vector2d par = calcRelativeVelocityToEdge();
		boolean smallAngle = par.getNorm() < 0.05 ; //Praktikum: Betrag von par reicht fuer Vergleich mit Limit
		
		if(smallAngle)
			isParallel = true;

		return isParallel;
	}

	public static Vector2d calculateAccelerations(double gravity) 
	{
		Vector2d marbleVelocity = new Vector2d( m.getCurrentVelocityX(), m.getCurrentVelocityY());
		Vector2d edge = closestEdgeCorner2.subtract(closestEdgeCorner1);
		
		double angle = Math.toRadians(closestNode.getRotate());
		double friction = 0.004;
		
		if(angle < 0)
			edge = edge.multiply(-1);
		
		double accHValue = gravity * Math.sin(angle);
		double accRValue = gravity * Math.cos(angle) * friction;
		
		Vector2d accH =  edge.normalize().multiply(Math.abs(accHValue)); //Hangabtriebsbeschleunigung in Richtung der Ebene nach unten
		Vector2d accR =  marbleVelocity.normalize().multiply(Math.abs(accRValue)).multiply(-1); //entgegen Murmelbewegung
		
		Vector2d newAcc = accH.add(accR);
		
		return newAcc;
	}
	
	public static Vector2d calculateTangentialVelocity() 
	{
		Vector2d marbleVelocity = new Vector2d( m.getCurrentVelocityX(), m.getCurrentVelocityY());
		Vector2d edgeNormal = (closestEdgeCorner2.subtract(closestEdgeCorner1)).normal();
		
		Vector2d par = edgeNormal.normalize().multiply(marbleVelocity.dotProduct(edgeNormal.normalize())); 
		Vector2d orth = marbleVelocity.subtract(par); 
		
		return orth;
	}
	
	public static double bladeRotation()
	{
		Vector2d marbleVelocityBefore = new Vector2d(m.getCurrentVelocityX(), m.getCurrentVelocityY());
		Vector2d marbleVelocityAfter = calculatePostCollisionVel1();
		double marbleMass = m.getWeight();
		double bladeMass = scissors.getWeight();
		
		Vector2d velocityDifference = marbleVelocityBefore.subtract(marbleVelocityAfter);
		
		double velocityDifferenceValue = velocityDifference.getNorm();
		
		double leverArm = calculateLeverArm();
		
		double omega = Math.sqrt((marbleMass * Math.pow(velocityDifferenceValue, 2)) / (bladeMass * Math.pow(leverArm, 2)));
		
		return omega;
	}
	
	public static double calculateLeverArm()
	{
		Vector2d collisionPoint = calculateCollisionPoint(m.getCurrentPos(), closestEdgeCorner1, closestEdgeCorner2);
		Vector2d edgeCenter = calculateEdgeCenter(closestEdgeCorner1, closestEdgeCorner2);
		
		Vector2d centerToCollisionPoint = collisionPoint.subtract(edgeCenter);
		double leverArm = centerToCollisionPoint.getNorm();
		
		return leverArm;
		
	}
	
	public static Vector2d calculateEdgeCenter(Vector2d q, Vector2d r)
	{
		Vector2d rq = r.subtract(q);
		Vector2d edgeCenter = q.add((rq.multiply(0.5)));
		
		return edgeCenter;
	}
	
	public static Marble getCollisionMarble()
	{
		return collisionMarble;
	}
	
	public static Scissors getScissors()
	{
		return scissors;
	}

	public static boolean collisionWithMarble() 
	{
		boolean marbleCollision = false;
		if(closestNode instanceof Marble)
			marbleCollision = true;
		
		return marbleCollision;
	}
	
	public static boolean collisionWithBlade()
	{
		return collisionWithBlade;
	}
}
