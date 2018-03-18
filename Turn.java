import javafx.scene.image.ImageView;
import java.util.ArrayList;

public class Turn {
	private ArrayList<ArrayList<ImageView>> grid;
	private String[] status;
	private boolean winCondition;
	private String[] nextRep;
	private String sound;
	
	/**
	 * Un Turn est un bundle d'informations utiles pour un tour.
	 * Il contient une grille d'ImageView, un tableau de messages,
	 * un boolean winCondition, et un tableau de string
	 * 
	 * @param grid un grille d'ImageView avec des ArrayList
	 * @param status un tableau de string de messages de status
	 * @param winCondition un boolean disant si on a gagne ou pas
	 * @param nextRep un tableau de string de representation future 
	 */
	public Turn(ArrayList<ArrayList<ImageView>> grid, String[] status, 
			boolean winCondition, String[] nextRep, String sound) {
		this.grid = grid;
		this.status = status;
		this.winCondition = winCondition;
		this.nextRep = nextRep;
		this.sound = sound;
	}
	
	/**
	 * Retourne l'attribut grid
	 * 
	 * @return grid
	 */
	public ArrayList<ArrayList<ImageView>> getGrid() {
		return grid;
	}
	
	/**
	 * Retourne l'attribut status
	 * 
	 * @return status
	 */
	public String[] getStatus() {
		return status;
	}
	
	/**
	 * Retourne l'attribut winCondition
	 * 
	 * @return winCondition
	 */
	public boolean getWinCondition() {
		return winCondition;
	}
	
	/**
	 * Retourne l'attribut nextRep
	 * 
	 * @return nextRep
	 */
	public String[] getNextRep() {
		return nextRep;
	}
	
	public String getSound() {
		return sound;
	}
}
