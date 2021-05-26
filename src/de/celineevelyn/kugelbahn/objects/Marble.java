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
	
	private double currentVelocityX;
	
	private double currentVelocityY;
	
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
		
		//saveLastPos(this.node.getTranslateX(), this.node.getTranslateY());
		
		//curPosX = ... this.node.getTranslateX() + ((currentVelocityX * deltaTime)+(0.5*accX*deltaTime*deltaTime))*100
		
		this.node.setTranslateX(this.node.getTranslateX() + ((currentVelocityX * deltaTime)+(0.5*accX*deltaTime*deltaTime))*100);
		this.node.setTranslateY(this.node.getTranslateY() - ((currentVelocityY * deltaTime)+(-0.5*accY*deltaTime*deltaTime))*100);
		
		double currentPositionX = this.node.getTranslateX();
		double currentPositionY = this.node.getTranslateY();
		
		//saveCurrentPos(currentPositionX, currentPositionY);
		
		//System.out.println("Gravity: " + gravity);
		//System.out.println("Current VelocityX: " + currentVelocityX + " | Current VelocityY: " +  currentVelocityY + " | Current PositionX: " + currentPositionX + " | Current PositionY: " + currentPositionY);

		currentVelocityX = currentVelocityX + (accX*deltaTime);
		currentVelocityY = currentVelocityY + (-accY*deltaTime);
		
		if(this.node.getTranslateY() >= 700)
		{
			MainScreenController.end();
		}	
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
