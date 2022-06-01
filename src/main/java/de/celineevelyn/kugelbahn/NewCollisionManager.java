package de.celineevelyn.kugelbahn;

import java.util.ArrayList;
import java.util.List;

import de.celineevelyn.kugelbahn.objects.Marble;
import javafx.scene.shape.Rectangle;

public class NewCollisionManager 
{
	private static Marble marble;
	private static List<Rectangle> envShapes;
	private static Vector2d closestEdgeCorner1;
	private static Vector2d closestEdgeCorner2;
	private static Rectangle closestRect;
	
	public static <envShapes> void initializeCollisionManager(List<Rectangle> env) //ArrayList<Circle> list
	{
		//testList = list;
		envShapes = env;
	}
	
	public static int checkCollisionsStart()
	{
		return checkCollisions(envShapes);
	}
	
	public static void setMarble(Marble marble2)
	{
		marble = marble2;
	}
	
	public static int checkCollisions(List<Rectangle> shape)
	{
		closestRect = null;
		double shortestDistance = 10000;
		
		Vector2d marblePosition =  marble.getCurrentPos();
		
		int collisionType = 0; //0 = kein Beruehrung, 1 = Kollision, 2 = Kontakt
		closestEdgeCorner1 = new Vector2d(0,0);
		closestEdgeCorner2 = new Vector2d(0,0);
		String edge = "No Edgels";
		
		for(Rectangle rect : shape)
		{
			List<Vector2d> cornerList = shapeToCorners(rect);
			
			double d1 = calculateDistance(marblePosition, cornerList.get(0), cornerList.get(1)); //oben
			double d2 = calculateDistance(marblePosition, cornerList.get(2), cornerList.get(0)); //links
			double d3 = calculateDistance(marblePosition, cornerList.get(1), cornerList.get(3)); //rechts
			double d4 = calculateDistance(marblePosition, cornerList.get(3), cornerList.get(2)); //unten
			
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
				closestEdgeCorner1 = cornerList.get(2);
				closestEdgeCorner2 = cornerList.get(0);
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
				closestEdgeCorner1 = cornerList.get(3);
				closestEdgeCorner2 = cornerList.get(2);
				edge = "edge 4";
			}
			
			//später entkommentieren???
			if(shortestDistance <= (marble.getRadius())) //Berührung detektiert!
			{
				//collisionDetected = true;
				collisionType = 1;
				break;
			}
		}
		
//		if(closestRect != null)
//		{
//			System.out.println("Marble Position: " + marblePos[0] + ", " + marblePos[1]);
//		}
		
		if (collisionType == 1) 
		{
			Vector2d marbleVelocity = new Vector2d( marble.getCurrentVelocityX(), marble.getCurrentVelocityY());
			Vector2d edgeNormal = (closestEdgeCorner2.subtract(closestEdgeCorner1)).normal();
			
			if(isParallel())
			{
				collisionType = 2; //Kontakt
			}
			else if(marbleVelocity.dotProduct(edgeNormal) > 0) //Bedingung aus dem Skript, relative Geschwindigkeit mal Berührnormale größer Null
			{
				collisionType = 1;
				System.out.println("(NewCollisionManager) COLLISION DETECTED, Shortest Distance: " + shortestDistance + ", Edge: " + edge ); //Kollision
			}
			else
			{
				collisionType = 0;
			}
//			else(marbleVelocity.dotProduct(edgeNormal) < 0)
//			{
//				collisionType = 0;
//			}
			
		}
		return collisionType; 
		
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
		else //LFP ist außerhalb
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
	
	public static Vector2d calculateRollPosition(Vector2d marblePosition)
	{
		return new Vector2d(0,0);
	}

	public static Vector2d calculateSetbackPosition(Vector2d marblePosition) 
	{
		Vector2d marbleDV = marble.getDirectionVector(); //Richtungsvektor Murmel
		Vector2d marbleDVreversed = marbleDV.multiply(-1); //umgekehrter Richtungsvektor
		Vector2d marbleDVreversednormalized = marbleDVreversed.normalize(); //als Einheitsvektor
		
		
		Vector2d edge = closestEdgeCorner2.subtract(closestEdgeCorner1);
		double angle = Math.round(marbleDVreversed.calculateAngle(edge)*10000)/10000.0; //Ebenenwinkel gerundet auf 4 Nachkommastellen
		double boxAngle = closestRect.getRotate();
		System.out.println("marble DV: " + marbleDVreversednormalized.getVector2d() + ", Angle in Degrees: " + Math.toDegrees(angle) + ", Angle in Radian " + angle);
		
		double setback1;
		double setback2;
		double setback3;
//		if(angle == Math.round((0.5*Math.PI)*10000)/10000.0) //Wenn Winkel = 90 Grad
//		{
//			setback = 0.011; //Radius in m
//			System.out.println("ANGLE = 90 GRAD");
//		}
//		else
//		{
//			setback = (0.011 / Math.sin(angle)); //spaeter neu schreiben, damit Murmel nicht so weit zurueckbewegt wird
//			System.out.println("ANGLE IS NOT 90 GRAD");
//		}
		
		if(boxAngle == 0 & marble.getDirectionVector().getX() != 0) //Wenn Winkel = 90 Grad
		{
			setback3 = marble.getRadius();
		}
		else
		{
			setback3 = marble.getRadius() / Math.sin(angle);
		}
		
		Vector2d collisionPoint = calculateCollisionPoint(marblePosition, closestEdgeCorner1, closestEdgeCorner2);
		
		if(collisionPoint == null)
		{
			collisionPoint = marblePosition;
		}

		Vector2d marbleNew = collisionPoint.add(marbleDVreversednormalized.multiply(setback3)); //Vom Kollisionspunkt eine bestimmte Strecke in Bewegungsrichtung zurueck
		
		return marbleNew;

	}
	
	public static Vector2d calculateCollisionPoint(Vector2d marblePosition, Vector2d q, Vector2d r)
	{
		//Gleichungssystem loesen mit Cramersche Regel: http://www.feuerbachers-matheseite.de/Cramer.pdf
		//Schnittpunkt zweier Geraden
		//Vector2d marbleDirection = new Vector2d(marble.getCurrentVelocityX(), marble.getCurrentVelocityY());// marble.getDirectionVector();
		Vector2d marbleDirection = marble.getDirectionVector();
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
		//marble.SetCollisionShape(collisionPoint.getX(), collisionPoint.getY());
		return collisionPoint;
		
		//################# Quelle: http://walter.bislins.ch/blog/index.asp?page=Schnittpunkt+zweier+Geraden+berechnen+%28JavaScript%29
//		Vector2d rv = q.subtract(r); //Richtungsvektor Gerade
//		Vector2d marbleRV = new Vector2d(marble.getCurrentVelocityX(), marble.getCurrentVelocityY());// marble.getDirectionVector();
//		Vector2d marbleRVnorm = marbleRV.normalize();
//		
//		Vector2d mq = q.subtract(marblePosition); //Corner 1 - marblePosition
//		Vector2d marbleRVnormT = marbleRVnorm.normal();
//		
//		double qx = mq.getX() * marbleRVnorm.getX() + mq.getY() * marbleRVnorm.getY();
//		double qy = mq.getX() * marbleRVnormT.getX() + mq.getY() * marbleRVnormT.getY();
//		
//		double sx = rv.getX() * marbleRVnorm.getX() + rv.getY() * marbleRVnorm.getY();
//		double sy = rv.getX() * marbleRVnormT.getX() + rv.getY() * marbleRVnormT.getY();
//		
//		if(sy == 0)
//			return null;
//		
//		double a = qx - qy * (sx / sy);
//		
//		Vector2d collisionPoint = marblePosition.add(marbleRVnorm.multiply(a));
//		
//		marble.setCollisionShape(collisionPoint.getX(), collisionPoint.getY());
//		
//		return collisionPoint;
	}

	public static Vector2d calculatePostCollisionVel() 
	{
		Vector2d marbleVelocity = new Vector2d( marble.getCurrentVelocityX(), marble.getCurrentVelocityY());
		Vector2d edgeNormal = (closestEdgeCorner2.subtract(closestEdgeCorner1)).normal();
		
		Vector2d par = edgeNormal.normalize().multiply(marbleVelocity.dotProduct(edgeNormal.normalize())); //Geschwindigkeit parallel, berechnet über orthogonale Projektion: https://de.wikipedia.org/wiki/Skalarprodukt#Orthogonalit%C3%A4t_und_orthogonale_Projektion
		Vector2d orth = marbleVelocity.subtract(par); //Geschwindigkeit orthogonal
		
		double marbleMass = marble.getWeight();
		double boxMass = 1000; //SPÄTER NEU!!
		Vector2d boxVel = new Vector2d(0,0);
		Vector2d velDifference = par.subtract(boxVel);
		double coeff = 0.9;
		
		//Vector2d par2 = ((par.multiply(marbleMass).add((boxVel.multiply(boxMass))).divide(marbleMass + boxMass)).multiply(2)).subtract(par); //neu berechnet mit Formel aus Skript
		Vector2d par3 = ((par.multiply(marbleMass).add((boxVel.multiply(boxMass)))).subtract((velDifference.multiply(boxMass).multiply(coeff)))).divide(marbleMass + boxMass);
		//To-do: boxVel2 !!
		
		Vector2d newVel = orth.add(par3);
		
		return newVel;
	}
	
	public static Vector2d calcRelativeVelocityToEdge()
	{
		Vector2d marbleVelocity = new Vector2d( marble.getCurrentVelocityX(), marble.getCurrentVelocityY());
		Vector2d edgeNormal = (closestEdgeCorner2.subtract(closestEdgeCorner1)).normal();
		
		Vector2d par = edgeNormal.normalize().multiply(marbleVelocity.dotProduct(edgeNormal.normalize())); //"gruener Pfeil" berechnet ueber orthogonale Projection --> Murmel fliegt sehr tief wenn der Wert klein ist
		
		return par;
	}
	
	private static boolean isParallel() 
	{
		boolean isParallel = false;
		double angle = Math.toRadians(closestRect.getRotate());
		System.out.println("GET ROTATE: " + Math.toDegrees(angle));
		double limit = 0.01; //fuer schiefe Ebenen geeignet		
		
		Vector2d par = calcRelativeVelocityToEdge();
		boolean smallAngle = Math.abs(par.getX()) < 0.005 & Math.abs(par.getY()) < limit;
		boolean lowVel = Math.abs(marble.getCurrentVelocityX()) < 0.05 & Math.abs(marble.getCurrentVelocityY()) < 0.05;
		
//		if(smallAngle)
//			System.out.println("----------- Der Winkel ist klein genug");
//		if(lowVel)
//			System.out.println("----------- Die Geschwindigkeit ist klein genug: " + marble.getCurrentVelocityX() + " / " + marble.getCurrentVelocityY());
//		System.out.println("------------- Parallele Geschwindigkeit: " + par.getVector2d());
//		if(smallAngle || lowVel) //wenn das erfuellt ist, fliegt die Murmel fast parallel
//			isParallel = true;
		
		if(smallAngle)
			isParallel = true;

		return isParallel;
	}

	public static Vector2d calculateAccelerations(double gravity) 
	{
		Vector2d marbleVelocity = new Vector2d( marble.getCurrentVelocityX(), marble.getCurrentVelocityY());
		Vector2d edge = closestEdgeCorner2.subtract(closestEdgeCorner1);
		
		double angle = Math.toRadians(closestRect.getRotate());
		double friction = 0.004; //nochmal nachschauen, wird bei 0.4 für Holz auf Holz zu schnell??
		
		if(angle < 0)
			edge = edge.multiply(-1);
		
		double accHValue = gravity * Math.sin(angle);
		double accRValue = gravity * Math.cos(angle) * friction;
		
//		Vector2d accH = new Vector2d(Math.cos(angle), Math.sin(angle)).multiply(accHValue); //aus den Werten Vektoren machen
//		Vector2d accR = new Vector2d(Math.cos(angle), Math.sin(angle)).multiply(accRValue);
		
		Vector2d accH =  edge.normalize().multiply(Math.abs(accHValue)); //Hangabtriebsbeschleunigung in Richtung der Ebene nach unten
		Vector2d accR =  marbleVelocity.normalize().multiply(Math.abs(accRValue)).multiply(-1);
		
		System.out.println("ACCELERATIONS::: accH: " + accH.getVector2d() + " / accR: " + accR.getVector2d() );
		
		Vector2d newAcc = accH.add(accR);
		
		return newAcc;
	}
	
	public static Vector2d calculateTangentialVelocity() 
	{
		Vector2d marbleVelocity = new Vector2d( marble.getCurrentVelocityX(), marble.getCurrentVelocityY());
		Vector2d edgeNormal = (closestEdgeCorner2.subtract(closestEdgeCorner1)).normal();
		
		Vector2d par = edgeNormal.normalize().multiply(marbleVelocity.dotProduct(edgeNormal.normalize())); 
		Vector2d orth = marbleVelocity.subtract(par); 
		
		return orth;
	}
}
