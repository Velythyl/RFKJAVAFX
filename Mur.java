
public class Mur extends Case {
	
	/**
	 * Le construceur des murs
	 * Est construit a une position donnee en parametres
	 * A toujours la representation wall.png
	 * 
	 * @param pos La position donnee au mur
	 */
	public Mur (Point pos) {
		this.pos = pos;
		this. representation = "wall.png";
	}
	
	/**
	 * Indique si l'interaction mur-robot est possible
	 * Toujours faux
	 * 
	 * @param robot Le robot qui tente d'interagir avec le mur
	 */
	public boolean interactionPossible(Robot robot) {
		return false;
	}
	
	/**
	 * Methode bidon: on ne peut interagir avec un mur
	 * 
	 * @param robot Le robot qui interagirait avec le mur
	 */
	public String interagir(Robot robot) {
		return "";
	}
}
