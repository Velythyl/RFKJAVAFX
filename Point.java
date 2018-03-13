 
public class Point {
	private final int x, y;
	
	/**
	 * Le constructeur des Points
	 * 
	 * @param x La valeur x donnee au point
	 * @param y La valeur y donnee au point
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Indique si deux coordonnees sont egales
	 * Compare un x,y a un Point
	 * 
	 * @param x La composante x de la coordonnee evaluee
	 * @param y La composante y de la coordonnee evaluee
	 * @return True si on a le meme endroit, false sinon
	 */
	boolean egal(int x, int y) {
		return x == this.x && y == this.y;
	}
	
	/**
	 * Retourne la composante x du point
	 * 
	 * @return l'attribut x du point
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Retourne la composante y du point
	 * 
	 * @return l'attribut y du point
	 */
	public int getY() {
		return y;
	}
}
