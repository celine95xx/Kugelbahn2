package de.celineevelyn.kugelbahn.objects;

import de.celineevelyn.kugelbahn.Vector2d;
import de.celineevelyn.kugelbahn.controller.MainScreenController;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * 
 * @author Celine Viehmann
 *
 */
public class Marble extends BasicNode
{
 	
	private double startX, startY, weight, radius, id;

	public  String color;
	
	private double currentVelocityX, currentVelocityY;
	private double lastPosX, lastPosY, currPosX, currPosY, nextPosX, nextPosY;
	
	public Line line;
	public Circle CollisionPoint;
	
	public Marble (double startX, double startY, double radius, double weight, String color, double id) 
	{
		super(new Circle(0,0,radius));
		
		this.startX = 0;
		this.startY = 0;
		
		this.setPosition(startX, startY);
		
		this.weight = weight; 
		this.color = color; 
		this.radius = radius;
		this.id = id;
		
		line = new Line(startX, startY, startX, startY);
		CollisionPoint = new Circle(0,0,radius/5);
		this.setCollisionShape(startX, startY);
	}

	public double getWeight() 
	{
		return weight;
	}

	public void setWeight(double weight) 
	{
		this.weight = weight;
	}
	
	public double getRadius() 
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
	
	public double getMarbleId() 
	{
		return id;
	}
	
	@Override
	public boolean equals (Object o)
	{	
		if ( this == o )
			return true;
		
		if( o ==  null || getClass() != o.getClass())
			return false;
		
		Marble m = (Marble) o;
		return startX == m.startX && startY == m.startY && radius == m.radius && weight == m.weight && color.equals(m.color);
	}
	
	public double getCurrentVelocityX()
	{
		return this.currentVelocityX;
	}

	public double getCurrentVelocityY()
	{
		return this.currentVelocityY;
	}
	
	public double getStartX()
	{
		return this.startX;
	}
	
	public double getStartY()
	{
		return this.startY;
	}
	
	public void setCollisionShape(double x, double y)
	{
		this.CollisionPoint.setCenterX(x);;
		this.CollisionPoint.setCenterY(y);
	}
	
	public void applyNextPosition()
	{
		setPosition(this.nextPosX, this.nextPosY);
	}
	
	public void setPosition(double sx, double sy)
	{
		this.node.setTranslateX(sx);
		this.node.setTranslateY(sy);
		
		if(this.node.getTranslateY() >= 880)
		{
			MainScreenController.end();
		}	
	}
	
	public void setCurrVelX(double velX)
	{
		this.currentVelocityX = velX;
	}
	
	public void setCurrVelY(double velY)
	{
		this.currentVelocityY = velY;
	}
	
	public void updateLine(double velX, double velY)
	{
		Vector2d currentPosition = getCurrentPos();
		line.setStartX(currentPosition.getX());
		line.setStartY(currentPosition.getY());
		System.out.println("set line: "+velX+" / "+velY);
		line.setEndX(currentPosition.getX() + velX*1000);
		line.setEndY(currentPosition.getY() + velY*1000);
	}
	
	public void setNextPosition(double nextPosX, double nextPosY) //um zu pruefen, ob in dem naechsten Frame eine Kollision stattfindet
	{
		this.currPosX = nextPosX;
		this.currPosY = nextPosY;
		
		this.nextPosX = nextPosX;
		this.nextPosY = nextPosY;
	}
	
	public Vector2d getDirectionVector()
	{	
		double x = this.currPosX - this.lastPosX;
		double y = this.currPosY - this.lastPosY;
		
		Vector2d directionVector = new Vector2d(x,y);
		
		return directionVector;
		
	}
	
	public Vector2d getCurrentPos()
	{
		double currentPositionX = getStartX() + this.node.getTranslateX();
		double currentPositionY = getStartY()  + this.node.getTranslateY();
		
		Vector2d currentPosition = new Vector2d(currentPositionX, currentPositionY);
		
		return currentPosition;
	}
	
	public Vector2d getNextPosition()
	{
		Vector2d nextPosition = new Vector2d(this.nextPosX, this.nextPosY);
		return nextPosition;
	}
	
	public void setLastPosition(Vector2d position)
	{
		this.lastPosX = position.getX();
		this.lastPosY = position.getY();
	}

}
