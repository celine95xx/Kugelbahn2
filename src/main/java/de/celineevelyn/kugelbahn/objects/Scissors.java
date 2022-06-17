package de.celineevelyn.kugelbahn.objects;

import javafx.scene.shape.Rectangle;

public class Scissors extends BasicNode
{
	public Scissors() 
	{
		super(new Rectangle(737, 364, 350, 15));
		
		this.setRotation(70);
		
		this.setStyle("-fx-fill: red;");
	}
	
	public void setRotation(double degree)
	{
		this.node.setRotate(degree);
	}
	
	public double getRotation()
	{
		return this.node.getRotate();
	}
}
