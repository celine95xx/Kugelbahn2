package de.celineevelyn.kugelbahn;

import java.util.Arrays;

public class Vector2d 
{
	private double x;
	private double y;
	
	public Vector2d (double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public String getVector2d()
	{
		double x = this.getX();
		double y = this.getY();
		double[] vectorArray = new double[] {x,y};
		
		return Arrays.toString(vectorArray);
	}
	
	public Vector2d add(Vector2d v)
	{
		double x = this.getX() + v.getX();
		double y = this.getY() + v.getY();
		
		Vector2d v3 = new Vector2d(x,y);
		return v3;
	}
	
	public Vector2d subtract(Vector2d v) 
	{
		double x = this.getX() - v.getX();
		double y = this.getY() - v.getY();
		
		Vector2d v3 = new Vector2d(x,y);
		return v3;
	}

	public double crossProduct(Vector2d v) 
	{
		double crossProduct = this.getX() * v.getY() - this.getY() * v.getX();
		return crossProduct;
	}
	
	public double dotProduct(Vector2d v)
	{
		double dotProduct = this.getX() * v.getX() + this.getY() * v.getY();
		return dotProduct;
	}
	
	public Vector2d multiply(double s)
	{
		double x = this.getX() * s;
		double y = this.getY() * s;
		
		Vector2d v = new Vector2d(x,y);
		return v;
	}

	public double getNorm() //Euklidische Norm: sqrt(x^2 + y^2)
	{
		double norm = Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2));
		return norm;
	}
	
	public Vector2d normalize()
	{
		double invertedLength = 1/this.getNorm();
		Vector2d normalized = this.multiply(invertedLength);
		
		return normalized;
	}
	
	public Vector2d normal()
	{
		double x = this.getY() * (-1);
		double y = this.getX();
		Vector2d transformed = new Vector2d(x,y);
		
		return transformed;
	}
	
	public Vector2d absolute()
	{
		double x = Math.abs(this.getX());
		double y = Math.abs(this.getY());
		
		Vector2d abs = new Vector2d(x,y);
		return abs;
	}
	
	public double calculateAngle(Vector2d v2)
	{
		double dotProduct = this.dotProduct(v2);
		double v1Length = this.getNorm();
		double v2Length = v2.getNorm();
		
		double cosangle = dotProduct/(v1Length * v2Length);
		
		double angle = Math.acos(cosangle);
		
		return angle;
		
	}

	
}
