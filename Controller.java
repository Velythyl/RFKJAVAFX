import java.util.ArrayList;

public class Controller {
	private RobotFindsKitten rfk;
	
	public Controller (String robotName, String kittenName) {
		this.rfk = new RobotFindsKitten(robotName, kittenName);
	}
	
	public ArrayList<String[]> turn(String move) {
		return rfk.turn(move);
	}
}
