package de.celineevelyn.kugelbahn;

import java.util.ArrayList;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import de.celineevelyn.kugelbahn.objects.BasicNode;
import de.celineevelyn.kugelbahn.objects.Marble;
import javafx.scene.shape.Circle;

/**
 * 
 * @author Evelyn Romanjuk
 * @author Celine Viehmann
 *
 */
public class Level 
{
	
	public static Level instance;
	
	private Marble marble;
	
	private double gravity = 9.81;
	private RealVector windDirection = new ArrayRealVector(new double []{-1,0});
	private double windAngle = 0;
	private double windVelocity = 0;
	
	/**
	 * Liste aller Objekte auf der Bahn
	 */
	private ArrayList<BasicNode> nodeList;
	private ArrayList<Circle> testList;
	
	

	public Level() 
	{
		this.testList = new ArrayList<>();
	}	
	
	public void addToNodeList(Circle circle)
	{
		testList.add(circle);
	}
	
	public ArrayList<Circle> getTestList()
	{
		return testList;
	}
	
	
	/**
	 * Temporäre Methode damit Marble vor dem Start der Simulation platziert werden kann
	 */
	public BasicNode placeMarble(double x, double y) 
	{
		marble = new Marble(x, y, 10, 10,"green");
		return marble;
	}
	
	public void setMarbleStartVelocity(double currentVelX, double currentVelY)
	{
		marble.setCurrentVelocityX(currentVelX);
		marble.setCurrentVelocityY(currentVelY);
	}
	
	public Marble getMarble()
	{
		return marble;
	}
	
	public void setGravity(double gravity)
	{
		this.gravity = gravity;
	}

	public double getGravity()
	{
		return gravity;
	}
	
	public double getVelX()
	{
		return marble.getCurrentVelocityX();
	}
	
	public double getVelY()
	{
		return marble.getCurrentVelocityY();
	}

	
	public void update(double deltaTime) 
	{
		marble.update(deltaTime);
	}
	
	
	//REMOVE LATER! Wind now calculated by angle!
//	public void setWindDirection(RealVector windDirection)
//	{
//		this.windDirection = windDirection;
//	}
	
//	public RealVector getWindDirection()
//	{
//		return windDirection;
//	}
	
	public void setWindAngle (double angle)
	{
		this.windAngle = angle;
	}
	
	public double getWindAngle()
	{
		return windAngle;
	}

	
	public void setWindVelocity(double windVelocity)
	{
		this.windVelocity = windVelocity;
	}
	
	public double getWindVelocity()
	{
		return windVelocity;
	}

	
}
