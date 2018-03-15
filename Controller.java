public class Controller {
	private RobotFindsKitten rfk;
	private boolean firstTurnDone;
	
	public Controller () {
		this.rfk = new RobotFindsKitten();
		this.firstTurnDone = false;
	}
	
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
	
	public void thisIsFirstTurn() {
		this.firstTurnDone = true;
	}
	
	public boolean didFirstTurn() {
		return this.firstTurnDone;
	}
	
	public void generateRobKit(String robotName, String kittenName) {
		this.rfk.generateRobKit(robotName, kittenName);
	}
}
