import java.util.ArrayList;

public class Grille {
	private Case[][] grille;
	private int largeur;
	private int hauteur;
	
	/**
	 * Le constructeur de la Grille de jeu, selon le nombre de piaces en x et y,
	 * la largeur de ces piaces, et le nombre de NonKittenItems
	 * 
	 * @param nbrPiecesX Le nombre de piaces en x
	 * @param nbrPiecesY Le nombre de piaces en y
	 * @param largeurPiece La largeur intarieur des piaces
	 * @param hauteurPiece La hauteur intarieure des piaces
	 * @param nbrNonKitten Le nombre de NonKittenItems
	 */
	public Grille(int nbrPiecesX, int nbrPiecesY,
			int largeurPiece, int hauteurPiece, int nbrNonKitten) {
		
		
		/* Largeur: le nombre de cases en largeur au total
		 * Hauteur: le nombre de cases en hauteur au total
		 * 
		 * La grille[][] est crae avec ces paramatres
		 */
		this.largeur = nbrPiecesX*largeurPiece+(1+nbrPiecesX);
		this.hauteur = nbrPiecesY*hauteurPiece+(1+nbrPiecesY);
		
		grille = new Case[largeur][hauteur];

		/*
		 * Ganaration des murs et des portes.
		 * Peu efficace, mais beaucoup plus simple d'implamentation et de lisibilita:
		 * On parcours chaque case. Si elle correspond a un mur extarieur ou intarieur valide,
		 * on y crae un mur (même si une porte devrait être la)
		 */
		for(int posY = 0; posY<hauteur; posY++) {
			for(int posX = 0; posX<largeur; posX++){
				
				if((posY%(hauteurPiece+1)==0)//Ligne de murs
						||(posY%(hauteurPiece+1)!=0 && posX%(largeurPiece+1)==0)) {//Murs+espaces
					Point posMur = new Point(posX,posY);
					grille[posMur.getX()][posMur.getY()] = new Mur(posMur);
				}
				
				if( posX!=0 && posX!=(largeur-1) && posY!=0 && posY!=hauteur-1) {
					if (posY%(hauteurPiece+1)!=0 && posX%(largeurPiece+1)==0 && (posY%(Math.ceil((hauteurPiece+1)/2))==0)) {
						Point posPorte = new Point(posX,posY);
						grille[posPorte.getX()][posPorte.getY()] = new Porte(posPorte);
					}
					
					if (posX%(largeurPiece+1)!=0 && posX%(Math.ceil((largeurPiece+1)/2))==0 && posY%(hauteurPiece+1)==0) {
						Point posPorte = new Point(posX,posY);
						grille[posPorte.getX()][posPorte.getY()] = new Porte(posPorte);
					}
				}
			}
		}
		
		/*
		 * Ganaration des cles
		 * On passe dans chaque piece et on y genere aleatoirement un new Point,
		 * puis une new Cle avec ce point
		 */
		for(int counter = 0; counter<nbrPiecesX*nbrPiecesY; counter++) {
			int randLargeur = (int)Math.floor(Math.random()*largeurPiece+1);
				randLargeur += (largeurPiece+1)*(counter%(nbrPiecesX));
				
			int randHauteur = (int)Math.floor(Math.random()*hauteurPiece+1);
				randHauteur += (hauteurPiece+1)*Math.floor(counter/nbrPiecesX);
				
			Point posCle = new Point(randLargeur,randHauteur);
			
			grille[randLargeur][randHauteur] = new Cle(posCle);
		}
		
		/*
		 * Generation des NonKitten
		 */
		for(int counter=nbrNonKitten; counter>0; counter--) {
			Point posRand = randomEmptyCell();
			grille[posRand.getX()][posRand.getY()] = new NonKitten(posRand);
		}
		
		/*
		 * Generation du teleporteur
		 */
		Point posTel = randomEmptyCell();
		grille[posTel.getX()][posTel.getY()] = new Teleporteur(posTel);
		
		//Robot genere autrement: robot nest pas une case
		
		
	}
	
	/**
	 * Ganarateur du Kitten
	 * N'est pas dans le constructeur de la grille car on a besoin
	 * d'un paramatre entra par l'utilisateur (le nom du kitten)
	 * 
	 * Crae un Kitten a une position valide
	 * 
	 * @param name Le nom du kitten
	 */
	public void generateKitten(String name) {
		Point posKitten = randomEmptyCell();
		grille[posKitten.getX()][posKitten.getY()] =  new Kitten(name, posKitten);
		
	}
	
	/**
	 * Retourne une cellule de la grille qui n'est pas occupae
	 * Peu efficace, mais tras simple (lisibilita+implamentation)
	 * Essaie des cases et les rejette tant qu'elles ne sont pas null
	 * 
	 * @return un Point correspondant a une cellule vide avec un x et y au hasard
	 */
	public Point randomEmptyCell() {
		
		int randLargeur = 0;
		int randHauteur = 0;
		
		do {
			randLargeur = (int)Math.round(Math.floor(Math.random()*largeur));
			randHauteur = (int)Math.round(Math.floor(Math.random()*hauteur));
			
		} while (grille[randLargeur][randHauteur] != null);
		
		return new Point(randLargeur, randHauteur);
		
	}
	
	/**
	 * Indique si le deplacement vers une case est possible
	 * True si on ne tente pas d'aller vers un mur ou une porte
	 * (et qu'on a pas de clas)
	 * 
	 * @param robot Le robot qui tente d'aller vers la case
	 * @param x La coordonnae x de la case
	 * @param y La coordonnae y de la case
	 * @return true si possible, false sinon
	 */
	public boolean deplacementPossible(Robot robot, int x, int y) {
		if (grille[x][y]==null || 
				(grille[x][y].getRepresentation()=="door.png" && robot.getKey()>=1)) {
			return true;
			
		}else if (grille[x][y].getRepresentation()=="wall.png" 
				|| grille[x][y].getRepresentation()=="door.png") {
			return false;
			
		}else return true;
	}
	
	/**
	 * Affiche le jeu (grille+robot) a l'aide d'un string reprasentant
	 * la grille. Affiche le nom du robot, son nombre de clas, et s'il
	 * a le teleporteur apras cette grille.
	 * 
	 * Ordre de l'algorithme:
	 * 		Si le robot est au-dessus de l'item dans la case
	 * 			on place # au bout du string
	 * 		Si la case est vide (null)
	 * 			on place " " au bout du string
	 * 		Sinon,
	 * 			on place case.getRepresentation(); au bout du string
	 * 
	 * @param robot Le robot a placer dans le string
	 */
	public String[] afficher(Robot robot) {
	    ArrayList<String> grid = new ArrayList<>();
	    int rX = robot.getX();
	    int rY = robot.getY();
	
	    for(int y=0;y<hauteur;y++){
			for(int x=0;x<largeur;x++){
				
				if (x == rX && y == rY) {
					grid.add("rob.png");
					
				}else if (grille[x][y]==null) {
					grid.add("back.png");
					
				}else {
					grid.add(grille[x][y].getRepresentation());
				}
			}
			
			grid.add("NEXT");
	    }
	    grid.remove(grid.size()-1);
	    return grid.toArray(new String[grid.size()]);
	}
	
	/**
	 * Fait l'interaction entre le robot et la case qu'il acrase
	 * Si la case sous le robot n'est pas vide et qu'il peut interagir:
	 * 		Interagit
	 * Sinon
	 * 		On saute une ligne
	 * @param robot
	 */
	public String interagir(Robot robot) {
		int rX = robot.getX();
		int rY = robot.getY();
		
		if (grille[rX][rY] != null && grille[rX][rY].interactionPossible(robot)) {
			return grille[rX][rY].interagir(robot);
		}else return "";
	}
}
