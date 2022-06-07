package de.celineevelyn.kugelbahn.objects;

import de.celineevelyn.kugelbahn.Vector2d;
import de.celineevelyn.kugelbahn.controller.MainScreenController;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * 
 * @author Evelyn Romanjuk
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
	
	public Marble (double startX, double startY, double radius, double weight, String color) 
	{
		super(new Circle(0,0,radius));
		
		this.startX = 0;
		this.startY = 0;
		
		this.setPosition(startX, startY);
		
		this.weight = weight; 
		this.color = color; 
		this.radius = radius;
		
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
	
	public double getId() 
	{
		return id;
	}

	public void setId(double id)
	{
		this.id = id;
	}
	
	public  String getColor() 
	{
		return color;
	}

	public void setColor(String color) 
	{
		this.color = color;
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
		//updateLine();
	}
	
	public void setCurrVelY(double velY)
	{
		this.currentVelocityY = velY;
		//updateLine();
	}
	
	public void updateLine(double velX, double velY)
	{
		//velX = currentVelocityX;
		//velY = currentVelocityY;
		Vector2d currentPosition = getCurrentPos();
		line.setStartX(currentPosition.getX());
		line.setStartY(currentPosition.getY());
		System.out.println("set line: "+velX+" / "+velY);
		line.setEndX(currentPosition.getX() + velX*1000);
		line.setEndY(currentPosition.getY() + velY*1000);
	}
	
//	public void saveLastPos(double lastPositionX, double lastPositionY)
//	{
//		lastPosX = lastPositionX;
//		lastPosY = lastPositionY;
//	}
//	
//	public void saveCurrentPos(double currPositionX, double currPositionY)
//	{
//		currPosX = currPositionX;
//		currPosY = currPositionY;
//	}
	
	public void setNextPosition(double nextPosX, double nextPosY) //to check if marble would collide in the next frame
	{
		this.currPosX = nextPosX;
		this.currPosY = nextPosY;
		
		this.nextPosX = nextPosX;
		this.nextPosY = nextPosY;
	}
	
//	public double[] getCurrentPosition2()
//	{
//		double[] currentPosition = new double[2];
//		currentPosition[0] = currPosX;
//		currentPosition[1] = currPosY;
//		
//		return currentPosition;
//	}
	
	public Vector2d getDirectionVector()
	{	
		double x = this.currPosX - this.lastPosX;
		double y = this.currPosY - this.lastPosY;
		
		//System.out.println(" Current Position : " + currPosX + ", " + currPosY + " / Last Position: " + lastPosX + ", " + lastPosY);
		
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
