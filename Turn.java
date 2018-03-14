import javafx.scene.image.ImageView;
import java.util.ArrayList;

public class Turn {
	private ArrayList<ArrayList<ImageView>> grid;
	private String[] status;
	private boolean winCondition;
	
	public Turn(ArrayList<ArrayList<ImageView>> grid, String[] status, boolean winCondition) {
		this.grid = grid;
		this.status = status;
		this.winCondition = winCondition;
	}
	
	public ArrayList<ArrayList<ImageView>> getGrid() {
		return grid;
	}
	
	public String[] getStatus() {
		return status;
	}
	
	public boolean getWinCondition() {
		return winCondition;
	}
}
