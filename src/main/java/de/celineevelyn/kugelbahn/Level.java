package de.celineevelyn.kugelbahn;

import java.util.ArrayList;

import de.celineevelyn.kugelbahn.objects.BasicNode;
import de.celineevelyn.kugelbahn.objects.Marble;
import de.celineevelyn.kugelbahn.objects.Scissors;
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
	private Scissors s;
	
	private double gravity = 9.81;
	private double windAngle = 0;
	private double windAcc = 0;
	private double angle = 0;
	
	/**
	 * Liste aller Objekte auf der Bahn
	 */
	private ArrayList<Marble> marbleList;
	private ArrayList<Circle> testList;
	
	

	public Level() 
	{
		this.marbleList = new ArrayList<>();
	}	
	
	public void addToNodeList(Circle circle)
	{
		testList.add(circle);
	}
	
	public ArrayList<Circle> getTestList()
	{
		return testList;
	}
	
	public BasicNode placeMarble(double x, double y) 
	{
		String color = "green";
		if(marbleList.size() == 1)
		{
			color = "red";
		}
		marble = new Marble(x, y, 8, 0.02, color); //0.002kg
		
		addToMarbleList(marble);
		
		
		return marble;
	}
	
	public BasicNode placeScissors()
	{
		s = new Scissors();
		
		return s;
	}
	
	public void addToMarbleList(Marble marble)
	{
		marbleList.add(marble);
	}
	
	public void setMarbleStartVelocity(double currentVelX, double currentVelY)
	{
		marble.setCurrVelX(currentVelX);
		marble.setCurrVelY(currentVelY);
	}
	
	public Marble getMarble()
	{
		return marble;
	}
	
	public ArrayList<Marble> getMarbleList()
	{
		return marbleList;
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
	
	public void setWindAngle (double angle)
	{
		this.windAngle = angle;
	}
	
	public double getWindAngle()
	{
		return windAngle;
	}

	
	public void setWindAcc(double windAcc)
	{
		this.windAcc = windAcc;
	}
	
	public double getWindAcc()
	{
		return windAcc;
	}
	
	public void setBoxAngle(double angle)
	{
		this.angle = angle;
	}
	
	public double getBoxAngle()
	{
		return angle;
	}


	
}
