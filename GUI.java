import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform; 
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GUI extends Application {
	
	public static void main(String[] args) {	//Launch du stage
		GUI.launch(args);
	}
	
	public void start(Stage stage) throws Exception {	//Stage pour le jeu entier
		VBox root = new VBox();						//Tous le jeu est dans root
		root.setStyle("-fx-background: #000000;");	//background noir,
		root.setAlignment(Pos.TOP_CENTER);			//alignement centre
		
		Scene scene = new Scene(root, 1280, 720);	//scene pour le jeu entier
		stage.setTitle("RobotFindsKitten");
		stage.setScene(scene);
		
		/*
		 * Dans cette section, on cree tous les elements graphiques requis. Ils seront
		 * modifies par apres avec des setText ou clear et add, etc
		 * 
		 * Message: le message affiche au dessus de la grille de jeu. Initialement le
		 * message d'acceuil du jeu
		 * 
		 * qRobot, rRobot, qKitten, rKitten: q = question, r = reponse
		 * Champs de textes utilises pour avoir le nom du kitten et du robot
		 * 
		 * submit: le bouton permettant d'entrer les reponses robot et kitten
		 * 
		 * affichage: gridPane qui represente la grille de jeu
		 * 
		 * robotStatus: le status du robot, affiche sous la grille
		 * 
		 * vicImg: l'image de victoire finale
		 * 
		 * victoire1: texte de victoire qui est toujours le meme
		 * 
		 * victoire2: texte de victoire ajuste selon le nom du robot et du
		 * kitten.
		 * 
		 */
		Text message = new Text("Bienvenue dans RobotFindsKitten: Super Dungeon Master 3000 Ultra Turbo Edition!");
		message.setFont(Font.font ("Verdana", 25));
		message.setFill(Color.WHITE);
		message.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().add(message);
		
		Label qRobot = new Label("Quel est ton nom, robot?");
		qRobot.setPadding(new Insets(0, 10, 0, 0));
		qRobot.setFont(Font.font ("Verdana", 20));
		TextField rRobot = new TextField();
		
		Label qKitten = new Label("Quel est le nom de ton chat?");
		qKitten.setPadding(new Insets(0, 10, 0, 10));
		qKitten.setFont(Font.font ("Verdana", 20));
		TextField rKitten = new TextField();
		
		HBox hb = new HBox();
		hb.setAlignment(Pos.TOP_CENTER);
		hb.setTranslateY(20);
		hb.getChildren().addAll(qRobot, rRobot, qKitten, rKitten);
		
		Button submit = new Button("Envoyer");
		submit.setTranslateY(40);
		root.getChildren().addAll(hb, submit);
		
		GridPane affichage = new GridPane();
		affichage.setAlignment(Pos.TOP_CENTER);
		root.getChildren().add(affichage);
		
		Controller controller = new Controller();	//Controlleur du jeu
		
		Text robotStatus = new Text("");
		robotStatus.setFont(Font.font ("Verdana", 25));
		robotStatus.setFill(Color.WHITE);
		robotStatus.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().add(robotStatus);
		
		Image vicImg = new Image("File:nki/found-kitten.png");
		ImageView vicImgView = new ImageView(vicImg);
		vicImgView.setFitHeight(720);
		vicImgView.setFitWidth(1280);
		
		Text victoire1 = new Text("You found kitten! Way to go, robot.");
		victoire1.setFill(Color.RED);
		victoire1.setFont(Font.font ("Verdana", 30));
		victoire1.setTranslateY(210);
		
		Text victoire2 = new Text("");	//est modifie lors de la victoire
		victoire2.setFill(Color.RED);
		victoire2.setFont(Font.font ("Verdana", 30));
		victoire2.setTranslateY(240);
		
		StackPane pane = new StackPane();	//Contient les elements de victoire
		
		pane.getChildren().add(vicImgView);
	    pane.getChildren().add(victoire1);
	    pane.getChildren().add(victoire2);
	    
		stage.show();

		/*
		 * Lorsqu'on clique sur le bouton, on entre nos reponses pour le nom
		 * du robot et du kitten
		 */
		submit.setOnAction((event) -> {
			/* 
			 * Si les fieldText ne son pas vides et qu'on est en debut de jeu
			 * Note: utiliser message.getText() semble peu logique au lieu d'un
			 * boolean, mais les variables venant d'au dehors du event ne peuvent
			 * etre modifiees dans l'event.
			 * 
			 * Ce meme code peut aussi etre acitve avec ENTER, voir le event des
			 * touches de clavier. Il n'est seulement commente qu'ici.
			 */
			if (rKitten.getText() != null && !rKitten.getText().isEmpty() &&
					rRobot.getText() != null && !rKitten.getText().isEmpty()&&
					message.getText().equals("Bienvenue dans RobotFindsKitten: Super Dungeon Master 3000 Ultra Turbo Edition!")) {
	        	
				/*
				 * On cree le robot et le kitten avec les noms recus. On enleve
				 * le hb contenant les r et q robot/kitten et le bouton associe.
				 */
	        	controller.generateRobKit(rRobot.getText(), rKitten.getText());
	        	
	        	root.getChildren().remove(hb);
	        	root.getChildren().remove(submit);
	    		
	        	/*
	        	 * On fait un tour d'initiation qui retourne la grille entiere.
	        	 * On rempli le gridpane avec cela.
	        	 */
	    		Turn firstTurn = controller.turn("INIT");
	    		ArrayList<ArrayList<ImageView>> firstGrid = firstTurn.getGrid();
	    		
	    		for(int y=0; y<firstGrid.size(); y++) {
	    			for(int x=0; x<firstGrid.get(0).size(); x++) {
	    				affichage.add(firstGrid.get(y).get(x), x, y);
	    			}
	    		}
	    		
	    		/*
	    		 * On update les messages avec les infos recues
	    		 */
	    		String[] firstStatus = firstTurn.getStatus();
	    		message.setText(firstStatus[0]);
	    		robotStatus.setText(firstStatus[1]);
	    		
	    		controller.thisIsFirstTurn(); //Tour d'init fait
	        }
		});
		
		/*
		 * Events de clavier:
		 * 
		 * On a d'abord les keyCode, puis les touches
		 * 
		 * ENTER: idem que le bouton submit
		 * 
		 * F5: si stage est fullscreen -> pas full screen
		 * 	   si stage est !fullscreen -> full screen
		 * 
		 * ESCAPE: on ferme tout avec system.exit
		 * 
		 * SPACE: on cree une nouvelle partie
		 * 
		 * Sinon: traitement des entrees textes
		 */
		scene.setOnKeyPressed((event) -> {
			KeyCode eventCode = event.getCode();
			
			if(eventCode == KeyCode.ENTER && rKitten.getText() != null && !rKitten.getText().isEmpty() 
					&& rRobot.getText() != null && !rKitten.getText().isEmpty()&&
					message.getText().equals("Bienvenue dans RobotFindsKitten: Super Dungeon Master 3000 Ultra Turbo Edition!")) {
				controller.generateRobKit(rRobot.getText(), rKitten.getText());
	        	
	        	root.getChildren().remove(hb);
	        	root.getChildren().remove(submit);
	    		
	    		Turn firstTurn = controller.turn("INIT");
	    		ArrayList<ArrayList<ImageView>> firstGrid = firstTurn.getGrid();
	    		
	    		for(int y=0; y<firstGrid.size(); y++) {
	    			for(int x=0; x<firstGrid.get(0).size(); x++) {
	    				affichage.add(firstGrid.get(y).get(x), x, y);
	    			}
	    		}
	    		
	    		String[] firstStatus = firstTurn.getStatus();
	    		message.setText(firstStatus[0]);
	    		robotStatus.setText(firstStatus[1]);
	    		
	    		controller.thisIsFirstTurn();
	    		
			}else if(eventCode == KeyCode.F5) {
				if(stage.isFullScreen()) {
					stage.setFullScreen(false);
				} else stage.setFullScreen(true);
				
			}else if(eventCode == KeyCode.ESCAPE) {
				System.exit(0);
				
			}else if(eventCode == KeyCode.SPACE && controller.getIsGameDone()) {	
				root.getChildren().remove(pane);	//On enleve affichage victoire
				
				/*
				 * On replace les messages et le gridPane comme au debut
				 * 
				 * On reajoute ces elements et le hb de questions et le bouton
				 */
				message.setText("Bienvenue dans RobotFindsKitten: Super Dungeon Master 3000 Ultra Turbo Edition!");
				robotStatus.setText("");
				affichage.getChildren().clear();
				
				root.getChildren().addAll(message, hb, submit, affichage, robotStatus);
				
				controller.newGame(); //Genere une nouvelle grille sans robot ni kitten
				
			/*
			 * Si rien n'est valide et qu'on a fait le tour d'init et que la partie
			 * n'est pas terminee: on traite l'entree de touches pas keycode
			 */
			}else if(controller.didFirstTurn() && controller.getIsGameDone() == false) {
				
				/*
				 * On fait un tour avec la touche envoyee
				 * On prend les messages de ce tour dans otherStatus
				 */
				Turn otherTurns = controller.turn(event.getText());
				String[] otherStatus = otherTurns.getStatus();
	    		
				/*
				 * Si on a gagne la partie, on le dit au controlleur,
				 * on enleve les elements graphiques de la partie,
				 * on ajuste le message de victoire2,
				 * on affiche les elements graphiques de victoire
				 */
	    		if(otherTurns.getWinCondition()) {
	    			controller.wonGame();
	    			
	    			root.getChildren().remove(message);
	    			root.getChildren().remove(robotStatus);
	    			root.getChildren().remove(affichage);
	    			
	    			victoire2.setText(otherStatus[0]);
					
				    root.getChildren().add(pane);
				
				//Sinon, on traite un tour normal
	    		} else {
	    			
	    			/*
	    			 * On ajuste les messages
	    			 */
		    		message.setText(otherStatus[0]);
		    		robotStatus.setText(otherStatus[1]);
		    		
		    		/*
		    		 * On prend les prochaines representations
		    		 * nextRep est de forme
		    		 * [repRobot, rX, rY, autreRep, aX, aY]
		    		 */
	    			String[] nextRep = otherTurns.getNextRep();
	    			
	    			String rob = nextRep[0];	//representation du robot
	    			int xRob = Integer.parseInt(nextRep[1]);	//x robot
	    			int yRob = Integer.parseInt(nextRep[2]);	//y robot
	    			Image tempRobImg = new Image(rob);	//image du robot
					
	    			String former = nextRep[3];	// representation de l'autre
	    			int xFormer = Integer.parseInt(nextRep[4]);	//x autre
	    			int yFormer = Integer.parseInt(nextRep[5]);	//y autre
	    			Image tempFormerImg = new Image(former);	//image autre
	    			
	    			/*
	    			 * Logique du traitement d'affichage:
	    			 * 
	    			 * Il a eu un changement (dif == true) si le x OU y du robot
	    			 * 		est different de celui de l'autre
	    			 * 
	    			 * breakerRob est faux.
	    			 * breakerFormer est faux, mais vrai si il n'y a pas de difference
	    			 */
	    			boolean dif = (xRob != xFormer || yRob != yFormer);
	    			boolean breakerRob = false;
	    			boolean breakerFormer = false;
	    			if(!dif) breakerFormer = true;
					for (Node node : affichage.getChildren()) {
						
						//La node (imageView) a le meme x et y que rob, on y met rob
					    if (affichage.getColumnIndex(node) == xRob && affichage.getRowIndex(node) == yRob) {
					        ((ImageView) node).setImage(tempRobImg);	//cast safe car affichage n'a que des imageView
					        breakerRob = true;
					    }
					    
					    //Si different, idem mais pour former
					    if(dif) {
						    if (affichage.getColumnIndex(node) == xFormer && affichage.getRowIndex(node) == yFormer) {
						        ((ImageView) node).setImage(tempFormerImg);
						        breakerFormer = true;
						    }
					    }
					    
					    //Si on a fait ajustement necessaire pour rob et former, break
					    if(breakerRob && breakerFormer) break;
					}
	    		}
			}
		});
	}
}
