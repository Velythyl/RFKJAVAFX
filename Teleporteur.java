public class Teleporteur extends Case {
 	
	/**
	 * Constructeur du teleporteur
	 * Est construit a une position pos donnee en parametre
	 * Sa representation est un symbole valide au hasard
	 * 
	 * @param pos Position a laquelle le robot est construit
	 */
	public Teleporteur (Point pos) {
		this.pos = pos;
		this.representation = getRandomSymbole();
		this.sound = "teleporteur.wav";	//https://freesound.org/people/GameAudio/sounds/220173/
	}
	
	/**
	 * Indique si l'interaction teleporteur-robot est possible
	 * Vrai si le teleporteur n'a pas deja ete trouve (donc faux si
	 * sa representation est back.png
	 * 
	 * @param robot Le robot qui tente d'interagir avec le teleporteur
	 */
	public boolean interactionPossible(Robot robot) {
		if (representation != "back.png") return true;
		return false;
	}
	
	/**
	 * Lorsque le robot interagit avec le teleporteur, on imprime
	 * a l'utilisateur qu'il a trouve le teleporteur.
	 * On indique au robot qu'il a trouve le teleporteur avec sa methode
	 * .foundTele()
	 * La representation du teleporteur devient back.png pour indiquer qu'il a
	 * ete trouve
	 */
	public String[] interagir(Robot robot) {
		robot.foundTele();
		String soundSorter = sound;
		this.sound = "footstep.wav";
		this.representation = "back.png";
		
		String[] temp = {"Vous avez trouve le teleporteur!", soundSorter};
		return temp;
	}
}