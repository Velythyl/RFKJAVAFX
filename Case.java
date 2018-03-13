
public abstract class Case {
	protected String representation;
	protected Point pos;
	
	/**
	* Retourne la representation de la case (un seul caractere)
	*
	* @return la representation de la case (representation)
	*/
	public String getRepresentation() {
		return representation;
	}
	
	/**
	* Indique si une interaction entre la case et le robot est
	* possible (ex.: le robot peut interagir avec un NonKittenItem
	* en tout temps, mais ne peut pas interagir avec un mur, le robot
	* peut interagir avec une porte seulement s'il possede une cle,
	* etc.)
	*
	* @param robot Le robot qui interagirait avec la case
	* @return true si une interaction entre le robot et la case est possible
	*/
	public abstract boolean interactionPossible(Robot robot);
	
	/**
	* Interaction entre la case et le robot
	* Depend de la case en question
	*
	* @param robot
	*/
	public abstract String interagir(Robot robot);
	
	/**
	* Genere un symbole aleatoire
	*
	* @return Un symbole ASCII valide
	*/
	public static String getRandomSymbole() {
		return Math.floor((Math.random() * 82 + 1)) + ".png";
	}
}
