package de.celineevelyn.kugelbahn;

import java.util.ArrayList;

import de.celineevelyn.kugelbahn.objects.BasicNode;
import de.celineevelyn.kugelbahn.objects.Marble;

public class Level {
	
	private Marble marble;
	
	/**
	 * Liste aller Objekte auf der Bahn
	 */
	private ArrayList<BasicNode> nodeList;
	
	
	public Level() {
		this.nodeList = new ArrayList<>();
	}
	
	
	
	/**
	 * Startet das Level mit Physik und angegebenen Marble-Eigenschaften
	 * @param marbleStartX
	 * @param marbleStartY
	 * @param radius
	 * @param weight
	 * @param gravity
	 * @param startSpeed
	 * @return
	 */
	public ArrayList<BasicNode> start(double marbleStartX, double marbleStartY, double radius, double weight, double gravity, double startSpeed) {
		
		// TODO
		marble.startSpeed = startSpeed;
		
		nodeList.add(marble);
		
		return nodeList;
		
	}
	
	
	/**
	 * Temporäre Methode damit Marble platziert werden kann vor dem Startknopf
	 * @param x
	 * @param y
	 * @return
	 */
	public BasicNode placeMarble(double x, double y) {
		marble = new Marble(x, y,10 ,0.3,10,0,"green");
		return marble;
	}
	
	public void update(double deltaT) {
		marble.update(deltaT);
		
		// TODO: Kollisionen berechnen
	}
	
	
	
}
