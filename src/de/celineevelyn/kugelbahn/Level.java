package de.celineevelyn.kugelbahn;

import java.util.ArrayList;

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
	
	private Marble marble;
	
	private static double gravity = 9.81;
	private static RealVector winddirection;
	/**
	 * Liste aller Objekte auf der Bahn
	 */
	private ArrayList<BasicNode> nodeList;
	private ArrayList<Circle> testList;
	
	public void addToNodeList(Circle circle)
	{
		testList.add(circle);
	}
	
	public ArrayList<Circle> getTestList()
	{
		return testList;
	}
	
	public Level() 
	{
		this.testList = new ArrayList<>();
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

	public static double getGravity()
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
	
	public void setWinddirection(RealVector nord)
	{
		Level.winddirection = nord;
	}

	public static RealVector getWinddirection()
	{
		return winddirection;
	}

	
}
