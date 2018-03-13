import java.util.ArrayList;
 
public class RobotFindsKitten {	//Ceci est la classe regroupant les modeles
	private String message;
	private Grille grille;
	private Robot robot;
	
	public RobotFindsKitten (String robotName, String kittenName) {
		this.grille = new Grille(5,2,11,5,25);
		this.robot = new Robot(robotName, this.grille.randomEmptyCell());
		this.grille.generateKitten(kittenName);
	}
	
	public ArrayList<String[]> turn(char tempMove) {
		String okMoves = "wasd";
		String[] affichage;
		String[] message = {""};
		String[] robotStatus = {""};
		
		if (robot.getTele()) okMoves = "wasdTt";
		if (okMoves.indexOf(tempMove)==-1) {
			affichage = this.grille.afficher(robot);
			
		} else {
			String move = ""+tempMove;	
			Point tempPoint;
			int futureX = robot.getX();
			int futureY = robot.getY();
			
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
			}
			
			if(grille.deplacementPossible(robot, futureX, futureY) == true) {
				robot.setPos(futureX, futureY);
			}
			
			message[0] = grille.interagir(robot);
			String temp = "";
			if(robot.getTele()) temp = "T";
			robotStatus[0] = robot.getNom() + "[" + robot.getKey() + "]" + temp;
			affichage = this.grille.afficher(robot);
		}
		
		ArrayList<String[]> messStatusAffi = new ArrayList<>();
		messStatusAffi.add(message);
		messStatusAffi.add(robotStatus);
		messStatusAffi.add(affichage);
		
		return messStatusAffi;
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
