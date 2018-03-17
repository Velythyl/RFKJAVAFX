public class Kitten extends Case {
	private String nom; 
	
	/**
	 * Constructeur du Kitten
	 * Lui donne un nom et position donnes en parametres
	 * et un symbole aleatoire comme representation
	 * 
	 * @param nom Le nom du kitten par l'utilisateur
	 * @param pos La position du kitten en Point (cree par la generation du jeu)
	 */
	public Kitten(String nom, Point pos){
		this.nom = nom;
		this.pos = pos;
		this.representation = getRandomSymbole();
	}
	
	/**
	 * Indique si l'interaction robot-kitten est possible
	 * (Ça l'est toujours)
	 * 
	 * @param robot Le robot qui tente d'interagir avec le kitten
	 */
	public boolean interactionPossible(Robot robot) {
		return true;
	}
	
	/**
	 * Fait l'interaction entre le robot et le kitten
	 * On retourne le message de victoire (qui comprend le nom du kitten et du robot), 
	 * et on dit au robot qu'il a trouve le kitten avec sa methode .foundKitten();
	 * 
	 * @param robot Le robot qui interagit avec le kitten
	 */
	
	public String interagir(Robot robot) {
		robot.foundKitten();
		return this.nom+" <3 "+robot.getNom();
	}
}
