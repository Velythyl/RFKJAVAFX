import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Controller {
	private RobotFindsKitten rfk;
	private boolean firstTurnDone;
	private boolean isGameDone;
	private boolean sound;
	
	/**
	 * Au debut controller ne contient qu'un rfk sans robot ni kitten
	 * On a pas fait le tour d'init
	 * On a pas gagne la partie
	 */
	public Controller () {
		this.rfk = new RobotFindsKitten();
		this.firstTurnDone = false;
		this.isGameDone = false;
		this.sound = true;
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
	
	/**
	 * Met le son on/off
	 */
	public void toggleSound() {
		this.sound = !this.sound;
	}
	
	/**
	 * Indique la valeur de verite d'activation du son
	 * 
	 * @return boolean sound
	 */
	public boolean hasSound() {
		return this.sound;
	}
	
	/**
	 * Downloads 82 nouvelle images a utiliser dans le jeu
	 * 
	 * On utilise un script JS puisque le bouton "random icon"
	 * du site game-icons.net utilise un event: il n'y a pas de
	 * lien vers un "game-icons.net/random". On fait donc rouler
	 * leur script a l'interieur de l'engine de scriptage nashorn.
	 * 
	 * http://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/
	 * 
	 * Ceci vient de base depuis JDK8.
	 * 
	 * Ceci fait aussi en sorte qu'on a pas besoin de traiter le json
	 * nous meme en java
	 */
	//public String getDLC() {
		
		
		
		/*
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		
		int infLoopBreaker1 = 0;
		while(true) {
			try {
				engine.eval(new FileReader("dlc.js"));
				
				Invocable invocable = (Invocable) engine;
				ArrayList<String> result = new ArrayList<>();
				
				for(int counter=0; counter<82; counter++) {
					
					int infLoopBreaker2 = 0;
					while(true) {
						try {
							Object objResult = invocable.invokeFunction("func");
							result.add(objResult.toString());
							break;
						
						//Si 3 essais sans succes, abandonne
						} catch (NoSuchMethodException e) {
							infLoopBreaker2++;
							e.printStackTrace();
							if(infLoopBreaker2 == 2) break;
						}
					};
						
				}
			} catch (FileNotFoundException | ScriptException e) {
				infLoopBreaker1++;
				e.printStackTrace();
				if(infLoopBreaker1 == 2) {
					return "Erreur de connecton a internet";
				}
			}
		}*/
	//}
}
