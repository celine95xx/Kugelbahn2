package de.celineevelyn.kugelbahn.objects;

import java.util.Vector;

import de.celineevelyn.kugelbahn.Level;
import de.celineevelyn.kugelbahn.controller.MainScreenController;
import javafx.scene.shape.Circle;

/**
 * 
 * @author Evelyn Romanjuk
 * @author Celine Viehmann
 *
 */
public class Marble extends BasicNode  
{
 	
	private double startX;
	
	private double startY;
	
	public double weight;
	
	public double radius;

	public  String color;
	
	private static double currentVelocityX;
	
	private static double currentVelocityY;
	
	private double lastPosX, lastPosY, currPosX, currPosY;
	
	
	public Marble (double startX, double startY, double radius, double weight, String color) 
	{
		super(new Circle(startX,startY,radius));
		
		this.startX = startX;
		this.startY = startY;
		this.weight = weight; 
		this.color = color; 
		this.radius = radius;
	
	}
	

	public double getWeight() 
	{
		return weight;
	}

	public void setWeight(double weight) 
	{
		this.weight = weight;
	}
	
	public  double getRadius() 
	{
		return radius;
	}

	public void setRadius(double radius)
	{
		this.radius = radius;
	}
	
	public  String getColor() 
	{
		return color;
	}

	public void setColor(String color) 
	{
		this.color = color;
	}
	
	public void setCurrentVelocityX(double currentVelX)
	{
		currentVelocityX = currentVelX;
	}
	
	public void setCurrentVelocityY(double currentVelY)
	{
		currentVelocityY = currentVelY;
	}
	
	public double getCurrentVelocityX()
	{
		return currentVelocityX;
	}

	public double getCurrentVelocityY()
	{
		return currentVelocityY;
	}
	
	public double getStartX()
	{
		return startX;
	}
	
	public double getStartY()
	{
		return startY;
	}
	


	@Override
	public void update(double deltaTime) 
	{
		double gravity = Level.getGravity();
		double windAcc = 0;
		
		double accX = 0;
		double accY = 0;
		
		// apply wind
		accX+=windAcc;
				
		// apply gravity
		accY+=gravity;
		
		
		//curPosX = ... this.node.getTranslateX() + ((currentVelocityX * deltaTime)+(0.5*accX*deltaTime*deltaTime))*100
		
		this.node.setTranslateX(this.node.getTranslateX() + ((currentVelocityX * deltaTime)+(0.5*accX*deltaTime*deltaTime))*100);
		this.node.setTranslateY(this.node.getTranslateY() - ((currentVelocityY * deltaTime)+(-0.5*accY*deltaTime*deltaTime))*100);
		
		double currentPositionX = this.node.getTranslateX();
		double currentPositionY = this.node.getTranslateY();
		

		currentVelocityX = currentVelocityX + (accX*deltaTime);
		currentVelocityY = currentVelocityY + (-accY*deltaTime);
		
		if(this.node.getTranslateY() >= 700)
		{
			MainScreenController.end();
		}	
	}
	
	public void setPosition(double sx, double sy)
	{
		this.node.setTranslateX(sx);
		this.node.setTranslateY(sy);
		
		if(this.node.getTranslateY() >= 700)
		{
			MainScreenController.end();
		}	
	}
	
	public void setCurrVelX(double velX)
	{
		currentVelocityX = velX;
	}
	
	public void setCurrVelY(double velY)
	{
		currentVelocityY = velY;
	}
	
	public void saveLastPos(double lastPositionX, double lastPositionY)
	{
		lastPosX = lastPositionX;
		lastPosY = lastPositionY;
	}
	
	public void saveCurrentPos(double currPositionX, double currPositionY)
	{
		currPosX = currPositionX;
		currPosY = currPositionY;
	}
	
	public void setNextPosition(double nextPosX, double nextPosY) //to check if marble would collide in the next frame
	{
		currPosX = nextPosX;
		currPosY = nextPosY;
	}
	
	public double[] getCurrentPosition2()
	{
		double[] currentPosition = new double[2];
		currentPosition[0] = currPosX;
		currentPosition[1] = currPosY;
		
		return currentPosition;
	}
	
	public double[] getDirectionVector()
	{
		double[] directionVector = new double[2];
		
		directionVector[0] = currPosX - lastPosX;
		directionVector[1] = currPosY - lastPosY;
		
		return directionVector;
		
	}
	
	public double[] getCurrentPos()
	{
		double[] currentPosition = new double[2];
		currentPosition[0] = getStartX() + this.node.getTranslateX();
		currentPosition[1] = getStartY()  + this.node.getTranslateY();
		
		return currentPosition;
	}
}
