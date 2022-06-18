package de.celineevelyn.kugelbahn.objects;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

public class Schere extends Group
{
	private Rectangle schere;

	public Schere()
	{
		schere = new Rectangle ();
		schere.setLayoutX(737);
		schere.setLayoutY(364);
		schere.setWidth(350);
		schere.setHeight(15);
		schere.setRotate(70);
		schere.setFill(javafx.scene.paint.Color.HOTPINK);
		
		getChildren().addAll(schere);
	}
	
	public void setRotation(double degree)
	{
		this.setRotate(degree);
	}
	
	public double getRotation()
	{
		return this.getRotate();
	}
}
