package de.celineevelyn.kugelbahn.objects;

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
 
	
	public double weight;
	
	public double radius;

	public  String color;
	
	private double currentVelocityX;
	
	private double currentVelocityY;
	
	
	public Marble (double startX, double startY, double radius, double weight, String color) 
	{
		super(new Circle(startX,startY,radius));
		
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

	@Override
	public void update(double deltaTime) 
	{
		
		//double gravity2 = Level.getGravity();
		double gravity = 1.62;
		double windVelocity = 0.1;
		double accX;
		
		accX = windVelocity / deltaTime;
		
		this.node.setTranslateX(this.node.getTranslateX() + ((currentVelocityX * deltaTime)+(0.5*accX*deltaTime*deltaTime))*1667);
		//this.node.setTranslateY(this.node.getTranslateY() - (currentVelocityY * deltaTime)*1667);
		
		this.node.setTranslateY(this.node.getTranslateY() - ((currentVelocityY * deltaTime)+(-0.5*gravity*deltaTime*deltaTime))*1667);
		
		double currentPositionX = this.node.getTranslateX();
		double currentPositionY = this.node.getTranslateY();
		
		System.out.println("Gravity: " + gravity);
		System.out.println("Current VelocityX: " + currentVelocityX + " | Current VelocityY: " +  currentVelocityY + " | Current PositionX: " + currentPositionX + " | Current PositionY: " + currentPositionY);
		System.out.println("deltaTime: " + deltaTime + " | deltaTime zum Quadrat " + deltaTime*deltaTime);

		currentVelocityY = currentVelocityY + (-gravity*deltaTime);
		currentVelocityX = currentVelocityX + windVelocity;
		
		if(this.node.getTranslateY() >= 700)
		{
			MainScreenController.end();
		}

		
	}
}
