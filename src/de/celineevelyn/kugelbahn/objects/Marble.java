package de.celineevelyn.kugelbahn.objects;

import javafx.scene.shape.Circle;

public class Marble extends BasicNode  
{
 
	public double startSpeed;
	
	public double weight;
	
	public double radius;

	public  String color;
	
	private double currentVelocityX;
	private double currentVelocityY;
	
	private double gravity;
	
//	private double forceX;
//	private double forceY;
	
	
	
	public Marble (double startX, double startY, double radius, double weight, String color) 
	{
		super(new Circle(startX,startY,radius));
		
//		currentVelocityY = 2;
//		currentVelocityX = 1;
		
		this.startSpeed = currentVelocityY;
		this.weight = weight; 
		this.color = color; 
		this.radius = radius;
		
//		this.forceY = gravity;
	}
	
	

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public  double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public  String getColor() {
		return color;
	}

	public void setColor(String color) {
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

	public void setGravity(double gravity)
	{
		this.gravity = gravity;
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
	public void update(double deltaT) {
		
		this.node.setTranslateX(this.node.getTranslateX() + (currentVelocityX * deltaT)*833);
		this.node.setTranslateY(this.node.getTranslateY() - (currentVelocityY * deltaT)*833);
		
		double currentPosX = this.node.getTranslateX();
		double currentPosY = this.node.getTranslateY();
		
		System.out.println("CurrentVelX: " + currentVelocityX + "CurrentVelY: " + currentVelocityY + " X Position: " + currentPosX + " | Y Position: " + currentPosY);
		System.out.println("Gravity: " + gravity);
		//if(this.node.getTranslateY()>100)
		//	currentVelocityY*=-0.75;
		
		//this.node.setTranslateX(5+this.node.getTranslateX());
		// circle.setTranslateX(5+circle.getTranslateX());
		// circle.getBoundsInLocal().getHeight();

		//if (currentVelocityY < weight)
		currentVelocityY = currentVelocityY + (-gravity*deltaT);
			
			//currentVelocityX -= 0.05/deltaT;
		
	}
}
