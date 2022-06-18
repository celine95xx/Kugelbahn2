package de.celineevelyn.kugelbahn.objects;

import javafx.scene.shape.Rectangle;

public class Scissors extends BasicNode
{
	
	public Scissors() 
	{
		super(new Rectangle(0, 0, 350, 15));
	
		this.setRotation(70);
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

}
