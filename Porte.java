 
public class Porte extends Case {
	private boolean unlocked;
	
	/**
	 * Le constructeur des portes
	 * Est construit a une position donnee en parametre
	 * A toujours la representation door.png
	 * 
	 * @param pos La position donnee a la porte
	 */
	public Porte(Point pos) {
		this.pos =  pos;
		this.representation = "door.png";
		this.sound = "unlockDoor.wav";	
		this.unlocked = false;
	}
	
	/**
	 * Indique si l'interaction porte-robot est possible
	 * Vrai si le robot a plus d'une cle et si la porte n'est pas
	 * deja debarree (barree: representation = port.png, debarree:
	 * representation = "back.png")
	 * 
	 * @param robot Le robot qui tente d'interagir avec la porte
	 */
	public boolean interactionPossible(Robot robot) {
		if (robot.getKey()>=1 && this.unlocked == false) {
			return true;
		}
		return false;
	}
	
	/**
	 * Lorsque le robot interagit avec la porte, on la debarre en
	 * changeant sa representation de porte.png a door.png
	 * On decremente le nombre du cles du robot avec sa methode
	 * .usedKey();
	 * On imprime aussi un message apprenant a l'utilisateur qu'il
	 * a utilise une cle et qu'il a maintenant "le nouveau nombre de cles"
	 * 
	 * @param robot Le robot qui interagit avec le NonKitten
	 */
	public String[] interagir(Robot robot) {
		robot.usedKey();
		this.representation = "back.png";
		
		String soundSorter = "";
		if(unlocked == false) {
			unlocked = true;
			soundSorter = sound;
			this.sound = "footstep.wav";
		} else soundSorter = this.sound;
		
		String[] temp = {"Vous avez utilise la cle pour debarrer la porte. Vous avez "+robot.getKey()+" cles",
				soundSorter};
		
		return temp;
	}
}
