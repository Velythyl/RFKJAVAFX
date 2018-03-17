public class Controller {
	private RobotFindsKitten rfk;
	private boolean firstTurnDone;
	private boolean isGameDone;
	
	/**
	 * Au debut controller ne contient qu'un rfk sans robot ni kitten
	 * On a pas fait le tour d'init
	 * On a pas gagne la partie
	 */
	public Controller () {
		this.rfk = new RobotFindsKitten();
		this.firstTurnDone = false;
		this.isGameDone = false;
	}
	
	/**
	 * controller.turn(move) traite move et retourne rfk.turn
	 * 
	 * Si move==INIT, on fait le tour d'initialisation
	 * 
	 * Sinon, si le tour est invalide, on le remplace par NA
	 * 
	 * Puis, on fait un tour avec le move ajuste.
	 * 
	 * @param move Le move donne par l'utilisateur
	 * @return Un Turn contenant de l'info pour le GUI
	 */
	public Turn turn(String move) {
		if(move.equals("INIT")) {
			return rfk.turn(move);
		} else {
			String nextMove = move;
			String okMoves = "wasd";
			if (rfk.getTele()) okMoves = "wasdTt";
			if (okMoves.indexOf(move)==-1) {
				nextMove = "NA";
			} 
			return rfk.turn(nextMove);
		}
	}
	
	/**
	 * Dit au controller que le tour d'init est fait.
	 * 
	 * firstTurnDone = true
	 */
	public void thisIsFirstTurn() {
		this.firstTurnDone = true;
	}
	
	/**
	 * On demande au controller si le firstTurn est fait
	 * 
	 * @return boolean firstTurnDone
	 */
	public boolean didFirstTurn() {
		return this.firstTurnDone;
	}
	
	/**
	 * Genere le robot et le kitten
	 * 
	 * @param robotName le nom du robot a creer
	 * @param kittenName le nom du kitten a creer
	 */
	public void generateRobKit(String robotName, String kittenName) {
		this.rfk.generateRobKit(robotName, kittenName);
	}
	
	/**
	 * Dit au controller qu'on a gagne la partie
	 * 
	 * isGameDone = true
	 */
	public void wonGame() {
		this.isGameDone = true;
	}
	
	/**
	 * Reconstruits le controlleur.
	 * 
	 * Reinitialisation de rfk, firstTurnDone, et isGameDone
	 */
	public void newGame() {
		this.rfk = new RobotFindsKitten();
		this.firstTurnDone = false;
		this.isGameDone = false;
	}
	
	/**
	 * Demande au controller si on a gagne la partie
	 * 
	 * @return boolean isGameDone
	 */
	public boolean getIsGameDone() {
		return this.isGameDone;
	}
}
