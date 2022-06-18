package de.celineevelyn.kugelbahn.objects;

import javafx.scene.shape.Rectangle;

public class Scissors extends BasicNode
{
	private double weight;
	
	public Scissors() 
	{
		super(new Rectangle(0, 0, 350, 15));
	
		this.setRotation(0); //70
		this.weight = 0.03;
	}
	
	public void setRotation(double degree)
	{
		this.node.setRotate(degree);
	}
	
	public double getRotation()
	{
		return this.node.getRotate();
	}

	
	public void setPosition()
	{
		this.node.setLayoutX(737);
		this.node.setLayoutY(364);
	}
	
	public double getWeight()
	{
		return this.weight;
	}

}
