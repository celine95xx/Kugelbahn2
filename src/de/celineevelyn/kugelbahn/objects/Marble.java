package de.celineevelyn.kugelbahn.objects;

import javafx.scene.shape.Circle;

public class Marble extends BasicNode  {
 
	public double startSpeed;
	
	public double weight;
	
	public double radius;
	
	public  String color;
	
	private double currentVelocityY;
	
	private double gravity;
	
	
	
	public Marble (double startX, double startY, double radius, double gravity, double weight, double startSpeed, String color) {
		super(new Circle(startX,startY,radius));
		
		currentVelocityY = 0;
		
		this.startSpeed = startSpeed;
		this.weight = weight; 
		this.color = color; 
		this.radius = radius;
		this.gravity = gravity;
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



	@Override
	public void update(double deltaT) {
		
		this.node.setTranslateY(startSpeed + currentVelocityY + this.node.getTranslateY()); // deltaT??
		// circle.setTranslateX(5+circle.getTranslateX());
		// circle.getBoundsInLocal().getHeight();

		//if (currentVelocityY < weight)
			currentVelocityY += gravity;
		
	}

}
