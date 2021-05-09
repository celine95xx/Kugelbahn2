package de.celineevelyn.kugelbahn.objects;

import javafx.scene.Node;


/**
 * Grundklasse für alle Objekte, die später auf dem Bildschirm gezeichnet werden sollen und Teil der Murmelbahn sind
 * @author Celine Viehmann
 *
 */
public abstract class BasicNode {
	
	
	/**
	 * JavaFX Node Klasse
	 */
	protected Node node;
	
	
	public BasicNode(Node node) {
		this.node = node;
	}

	
	/**
	 * Alle erbenden Klassen müssen eine update-Methode bereitstellen
	 */
	public abstract void update(double deltaT);
	
	public Node getNode() {
		return node;
	}
	
}
