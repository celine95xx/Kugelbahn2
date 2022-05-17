package de.celineevelyn.kugelbahn;

public class MathUtil 
{
	/////ANALYSIS
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
	
	public static boolean checkEdgeType(double[] p1, double[] p2) //ob senkrecht oder waagerecht
	{
		boolean isSpecialEdge = false;
		
		if(p1[0] == p2[0] || p1[1] == p2[1])
		{
			isSpecialEdge = true;
		}
		
		return isSpecialEdge;
	}
	
	public static double calculateAngle(double[] directionVector1, double[] directionVector2)
	{
		double dotProduct = directionVector1[0]*directionVector2[0] + directionVector1[1]*directionVector2[1];
		double edgeNormalLength = Math.sqrt(Math.pow(directionVector1[0], 2) + Math.pow(directionVector1[1], 2));
		double marbleVectLength = Math.sqrt(Math.pow(directionVector2[0], 2) + Math.pow(directionVector2[1], 2));
		
		double cosangle = dotProduct/(edgeNormalLength * marbleVectLength);
		
		double angle = Math.acos(cosangle);
		double angledegree = Math.toDegrees(angle);
		
		System.out.println("Winkel: " + angle + " = " + angledegree + "°");
		
		return angle;
		
	}
	
	public static double[] calculateIntersection(double[] directionVector, double[] marblePosition, double[] corner1, double[] corner2)
	{
		// directionVector = marble.getDirectionVector();
		double[] intersection = new double[2];
		
		
		if(directionVector[0] == 0.0 | directionVector[1] == 0)
		{
			if(checkEdgeType(corner1, corner2))
			{
				intersection = getClosestPointOnEdgeNoLFP(corner1, corner2, marblePosition);
			}
			else
			{
				//intersection = CollisionManager.calculateLotfusspunkt(corner1, corner2, marblePosition);
				intersection = calculateIntersectionSpecialDirVect(corner1, corner2, marblePosition, directionVector);
			}
		}
		else
		{
			double m1 = (directionVector[1]/directionVector[0]);
			double b1 = MathUtil.berechneYAchsenabschnitt(marblePosition, m1);
			
			//Edge
			double m2 = MathUtil.berechneSteigung(corner1, corner2);
			double b2 = MathUtil.berechneYAchsenabschnitt(corner1, m2);
			
			//Schnittpunkt
			double intersectionX = MathUtil.berechneGeradenschnittpunktX(m1, m2, b1, b2);
			double intersectionY = MathUtil.berechneGeradenschnittpunktY(intersectionX, m1, b1);
			
			intersection[0] = intersectionX;
			intersection[1] = intersectionY;
		}
	
		System.out.println("***********Intersection X " + intersection[0]+ ", Intersection Y " +  intersection[1]);
		
		return intersection;
	}
	
	public static double[] calculateIntersectionSpecialDirVect(double[] corner1, double[] corner2, double[] p, double[] directionVector)
	{
		double[] intersection = new double[2];
		//Marble
		double m1 = (directionVector[1]/directionVector[0]);
		double b1 = MathUtil.berechneYAchsenabschnitt(p, m1);
		
		//Edge
		double m2 = berechneSteigung(corner1, corner2);
		double b2 = berechneYAchsenabschnitt(corner1, m2);
		
		if(directionVector[0] == 0) //marble moves vertically
		{
			intersection[0] = p[0];
			intersection[1] = berechneGeradenschnittpunktY(intersection[0], m2 , b2);
		}
		
		if(directionVector[1] == 0) //marble moves horizontally
		{
			intersection[0] = berechneGeradenschnittpunktX(m1, m2, b1, b2);
			intersection[1] = p[1];
		}
		
		return intersection;
	}
	
	public static double[] getClosestPointOnEdgeNoLFP(double[] gP1, double[] gP2, double[] p)
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
	
	
	
	/////LINEAR ALGEBRA
	public static double[] getDirVector(double[] p1, double[] p2)
	{
		double[] dirVector = new double[2];
		
		dirVector[0] = p2[0] - p1[0];
		dirVector[1] = p2[1] - p1[1];
		
		return dirVector;
	}
	
	public static double[] reverseDirVector2(double[] dirVector) // orthogonal zum ursprünglichen
	{
		double[] revDirVector = new double[2];
		
		revDirVector[0] = dirVector[1];
		revDirVector[1] = - dirVector[0];
		
		return revDirVector;
	}
	
	public static double[] reverseDirVector(double[] dirVector) 
	{
		double[] revDirVector = new double[2];
		
		revDirVector[0] = -dirVector[0];
		revDirVector[1] = - dirVector[1];
		
		return revDirVector;
	}
	
	public static double calcVectorLength(double[] vector)
	{
		 return Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));		
	}
	
	public static double[] getNormalizedDirVector(double[] vector)
	{
		double[] normalizedVector = new double[2];
		double vectorLength = calcVectorLength(vector);
		
		normalizedVector[0] = (1 / vectorLength) * vector[0];
		normalizedVector[1] = (1 / vectorLength) * vector[1];
		
		return normalizedVector;
	}
}
