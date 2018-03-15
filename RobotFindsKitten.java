import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
 
public class RobotFindsKitten {	//Ceci est la classe regroupant les modeles
	private String message;
	private Grille grille;
	private Robot robot;
	
	public RobotFindsKitten () {
		this.grille = new Grille(3,2,7,5,25);
	}
	
	public void generateRobKit (String robotName, String kittenName) {
		this.robot = new Robot(robotName, this.grille.randomEmptyCell());
		this.grille.generateKitten(kittenName);
	}
	public boolean getTele() {
		return this.robot.getTele();
	}
	
	public Turn turn(String move) {
		String robotStatus;
		String message;
		boolean winCondition = false;
		boolean firstTurn = false;
		
		Point tempPoint;
		int formerX = robot.getX();
		int formerY = robot.getY();
		int futureX = formerX;
		int futureY = formerY;
		
		
		switch (move) {
		case "w":	futureY--;
		        break;
		case "a": 	futureX--;
		        break;
		case "s": 	futureY++;
		        break;
		case "d": 	futureX++;
		        break;
		case "T": 	tempPoint = grille.randomEmptyCell();
					futureX = tempPoint.getX();
					futureY = tempPoint.getY();
		        break;
		case "t": 	tempPoint = grille.randomEmptyCell();
					futureX = tempPoint.getX();
					futureY = tempPoint.getY();
				break;
		case "INIT":	firstTurn = true;
				break;
		case "NA":
				break;
		}
		
		if(grille.deplacementPossible(robot, futureX, futureY) == true) {
			robot.setPos(futureX, futureY);
		}
		
		
		if(move.equals("INIT")) {
			message = "Retrouvons ton chaton!";
			ArrayList<ArrayList<ImageView>> grid = this.grille.init(robot);
			robotStatus = robot.getNom() + "[" + robot.getKey() + "]";
			
			String[] initTempArray = {message, robotStatus};
			return new Turn(grid, initTempArray, false, null);
		} else {
			message = grille.interagir(robot);
			String temp = "";
			if(robot.getTele()) temp = "T";
			robotStatus = robot.getNom() + "[" + robot.getKey() + "]" + temp;
			String[] tempArray = {message, robotStatus};
			
			winCondition = this.robot.getWinCondition();
			
			String[] nextRep = this.grille.getGrid(robot, formerX, formerY);
			return new Turn(null, tempArray, winCondition, nextRep);
		}
	}
	
	/*public static void main(String[] args) {
		
		System.out.println("Bienvenue dans RobotFindsKitten: Super Dungeon Master 3000 Ultra Turbo Edition !");
		
		System.out.println("Quel est ton nom, robot?:");
		
		System.out.println("Oh non! Tu as perdu ton chaton!?!\n"
				+ "Quel est son nom???:");
		
		System.out.println("Retrouvons "+kittenName+"!");
		
		/*
		 * Ce qui se trouve ci-haut cree la grille de jeu avec des noms
		 * fournis par l'utilisateur pour le robot et le kitten.
		 */
		
		/*
		 * Tant qu'on a pas trouve le kitten, on demande le move a l'utilisateur.
		 * Le move peut être wasd ou T (si le robot a le teleporteur)
		 * 
		 * De façon appropriee avec le move, on cree futureX et futureY, les futures
		 * coordonnees du robot apres le move. Pour T, on fait ceci avec la methode
		 * .randomEmptyCell(); de la grille.
		 * 
		 * Si le deplacement vers (futureX,futureY) est valide, on le fait.
		 * 
		 * Dans tout les cas, même si on ne bouge pas, le robot tente d'interagir
		 * avec la case qu'il ecrase.
		 * 
		 * Les messages sont affiches en haut de la grille de façon a garder la grille
		 * au même endroit. Ceci rend le jeu plus jouable. L'exception est rentrer un move
		 * invalide: ceci est une "erreur" selon main (on ne peut rien faire avec z, par exemple)
		 * 
		 * Puis, on lance un nouveau tour tour.
		 */
		//String move = "";
		
    
}
