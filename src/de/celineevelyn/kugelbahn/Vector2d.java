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

	public double getNorm() //Euklidische Norm: sqrt(x^2 + y^2)
	{
		double norm = Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2));
		return norm;
	}

	
}
