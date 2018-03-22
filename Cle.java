public class Cle extends Case {
	private boolean pickedUp;
	
	/**
	 * Le constructeur des Cles
	 * Prend comme position une pos passee en parametres
	 * A toujours key.png comme representation
	 * 
	 * @param pos La position a laquelle la Cle est construite
	 */
	public Cle (Point pos) {
		this.pos = pos;
		this.representation = "key.png";
		this.sound = "key.wav"; //https://freesound.org/people/Davidsraba/sounds/347172/
		this.pickedUp = false;
	}
	
	/**
	 * Indique si l'interaction cle-robot est possible
	 * Vrai si la representation de la cle n'est pas "back.png", faux sinon
	 * 
	 * @param robot Le robot qui tente d'interagir avec la cle
	 */
	public boolean interactionPossible(Robot robot) {
		if(this.pickedUp == false) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Lors de l'interaction avec une cle, on indique au robot
	 * qu'il a trouve une cle.
	 * On dit a l'utilisateur qu'il a trouve une cle et combien
	 * il en a
	 * On change la representation de la cle a "back.png"
	 * 
	 * @param robot Le robot qui interagit avec la cle
	 */
	public String[] interagir(Robot robot) {
		robot.foundKey();
		this.representation = "back.png";
		
		String soundSorter = "";
		if(this.pickedUp == false) {
			soundSorter = sound;
			pickedUp = true;
			this.sound = "footstep.wav";
		} else soundSorter = sound;
	
		String[] temp = {"Vous avez trouve une cle! Vous avez "+robot.getKey()+" cles", soundSorter};
		return temp;
	}
	
}