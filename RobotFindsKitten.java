import java.util.ArrayList;

import javafx.scene.image.ImageView;
 
public class RobotFindsKitten {	//Ceci est la classe regroupant les modeles
	private Grille grille;
	private Robot robot;
	
	/**
	 * Cree la grille de jeu
	 */
	public RobotFindsKitten () {
		this.grille = new Grille(3,2,7,7,45);
	}
	
	/**
	 * Genere le robot et le kitten avec leurs noms
	 * 
	 * Kitten est dans la grille, le robot non
	 * 
	 * @param robotName le nom du robot
	 * @param kittenName le nom du kitten
	 */
	public void generateRobKit (String robotName, String kittenName) {
		this.robot = new Robot(robotName, this.grille.randomEmptyCell());
		this.grille.generateKitten(kittenName);
	}
	
	/**
	 * Indique si le robot a le teleporteur
	 * 
	 * @return robot.getTele()
	 */
	public boolean getTele() {
		return this.robot.getTele();
	}
	
	/**
	 * Fait un tour de partie
	 * 
	 * Si INIT, on on fait:
	 * 		message = "Retrouvons ton chaton!"
	 * 		robotStatus = nomRobot[0]
	 * 		et on demande init a la grille
	 * 
	 * Sinon,
	 * 		message = interaction robot-item
	 * 		robotStatus (traitement normal) = 
	 * 			"nomRobot[nbCles]getTele()"
	 * 		et on demande getChanges a la grille
	 * 
	 * @param move traite par le controller
	 * 
	 * @return Turn cree par le tour
	 */
	public Turn turn(String move) {
		String robotStatus;
		String message;
		boolean winCondition = false;
		
		/*
		 * formerX/Y est la position du robot.
		 * idem pour futureX/Y
		 * tempPoint est utilise si on se teleporte
		 */
		Point tempPoint;
		int formerX = robot.getX();
		int formerY = robot.getY();
		int futureX = formerX;
		int futureY = formerY;
		
		/*
		 * logique du switch: on change le futureX ou Y de facon appropriee
		 * selon wasd.
		 * 
		 * On change futureX et Y avec un randomEmptyCell si on se 
		 * teleporte avec T ou t
		 * 
		 * Si on a INIT ou NA, on ne change pas le futureX ou Y
		 */
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
		case "INIT":
				break;
		case "NA":
				break;
		}
		
		//Si futureX et Y sont valides, on change la position du robot
		if(grille.deplacementPossible(robot, futureX, futureY) == true) {
			robot.setPos(futureX, futureY);
		}
		
		/*
		 * Note sur Turn: il m'a ete utile de pouvoir a tout moment demander INIT
		 * et de recevoir la grille complete. J'ai donc laisse le format 4 attributs
		 * au lieu de 3 pour aider au debuggage.
		 */
		
		/*
		 * Si le movement est INIT:
		 * traitement special de l'initialisation
		 */
		if(move.equals("INIT")) {
			message = "Retrouvons ton chaton!";	//Message toujours le meme
			ArrayList<ArrayList<ImageView>> grid = this.grille.init(robot);	//On a besoin de tout
																			//la grille
			robotStatus = robot.getNom() + "[0]";	//impossible d'avoir une cle ou le tele
			
			/*
			 * On retourne un Turn de type x, x, false, null
			 * winCondition est toujours false en INIT
			 */
			String[] initTempArray = {message, robotStatus};
			return new Turn(grid, initTempArray, false, null, null);
	
		//Pour tout autre mouvement
		} else {
			String[] repSound = grille.interagir(robot);
			message = repSound[0]; //message est l'interaction appropriee
			String sound = repSound[1];
			/*
			 * Si pas le tele, robotStatus est nomRobot[nbClefs]
			 * Sinon, robotStatus est nomRobot[nbClefs]T
			 */
			String temp = "";
			if(robot.getTele()) temp = "T";
			robotStatus = robot.getNom() + "[" + robot.getKey() + "]" + temp;
			
			String[] tempArray = {message, robotStatus};
			
			winCondition = this.robot.getWinCondition();
			
			/*
			 * On retourne un Turn de type null, x, x, x
			 */
			String[] nextRep = this.grille.getChanges(robot, formerX, formerY);
			return new Turn(null, tempArray, winCondition, nextRep, sound);
		}
	}    
}
