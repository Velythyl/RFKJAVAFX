 
public class Robot {
	private String nom;
	private Point pos;
	private boolean winCondition;
	private int nbCle;
	private boolean tele;
	
	/**
	 * Le constructeur du robot
	 * Son nom et pos sont donnes en parametres
	 * Il commence avec aucune cles (nbCle=0), il n'a pas le
	 * teleporteur (tele=false), et il n'a pas trouve le chat
	 * (winCondition=false)
	 * 
	 * @param nom Le nom donne au robot par l'utilisateur
	 * @param pos La position donnee au robot par la generation du jeu
	 */
	public Robot (String nom, Point pos) {
		this.nom = nom;
		this.pos = pos;
		this.nbCle = 0;
		this.tele = false;
		this.winCondition = false;
	}
	
	/**
	 * Retourne le nombre de cles du robot
	 * 
	 * @return Le nombre de cles du robot
	 */
	public int getKey() {
		return nbCle;
	}
	
	/**
	 * Indique si le robot a trouve le teleporteur
	 * 
	 * @return La valeur true/false du fait que le robot a le teleporteur.
	 */
	public boolean getTele() {
		return tele;
	}
	
	/**
	 * Indique au robot qu'il a trouve le teleporteur
	 * On met tele=true
	 */
	public void foundTele() {
		this.tele = true;
	}
	
	/**
	 * Indique au robot qu'il a utilise une cle
	 * Decremente son nombre de cles
	 */
	public void usedKey() {
		nbCle--;
	}
	
	/**
	 * Indique au robot qu'il a trouve une cle
	 * Incremente son nombre de cles
	 */
	public void foundKey() {
		nbCle++;
	}
	
	/**
	 * Retourne la position du robot
	 * 
	 * @return L'attribut pos du robot
	 */
	public Point getPos() {
		return pos;
	}
	
	/**
	 * Retourne la composante x du point pos du robot
	 * 
	 * @return La methode getX() du point du robot (coordonnee x du robot)
	 */
	public int getX() {
		return pos.getX();
	}
	
	/**
	 * Retourne la composante y du point pos du robot
	 * 
	 * @return La methode getY() du point du robot (coordonnee y du robot)
	 */
	public int getY() {
		return pos.getY();
	}
	
	/**
	 * Retourne le nom du robot
	 * 
	 * @return Le nom du robot
	 */
	public String getNom() {
		return nom;
	}
	
	/**
	 * Change la position du robot en creant un
	 * nouveau point et en pointant pos vers ce point
	 * 
	 * @param x La nouvelle position en x du robot
	 * @param y	La nouvelle position en y du robot
	 */
	public void setPos(int x, int y) {
		this.pos = new Point (x, y);
	}
	
	/**
	 * Indique si le robot a trouve le kitten
	 * 
	 * @return winCondtion: true/false: kitten/pas de kitten
	 */
	public boolean getWinCondition() {
		return winCondition;
	}
	
	/**
	 * Indique au robot qu'il a trouve le kitten
	 * On met winCondition = true
	 */
	public void foundKitten() {
		this.winCondition = true;
	}
	
}
