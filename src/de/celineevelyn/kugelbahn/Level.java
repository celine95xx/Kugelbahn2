package de.celineevelyn.kugelbahn;

import java.util.ArrayList;

import de.celineevelyn.kugelbahn.objects.BasicNode;
import de.celineevelyn.kugelbahn.objects.Marble;

/**
 * 
 * @author Evelyn Romenjuk
 * @author Celine Viehmann
 *
 */
public class Level 
{
	
	private Marble marble;
	
	private static double gravity = 9.81;
	
	/**
	 * Liste aller Objekte auf der Bahn
	 */
	private ArrayList<BasicNode> nodeList;
	
	
	public Level() 
	{
		this.nodeList = new ArrayList<>();
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
	
	
	
}
