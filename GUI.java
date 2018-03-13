import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {
	
	public static void main(String[] args) {
		GUI.launch(args);
	}
	
	
	public void start(Stage stage) throws Exception {
		VBox root = new VBox();
		Scene scene = new Scene(root, 1280, 720);
		stage.setTitle("RobotFindsKitten");
		stage.setScene(scene);
		
		
		
		Text message = new Text("Bienvenue dans RobotFindsKitten: Super Dungeon Master 3000 Ultra Turbo Edition !");
		root.getChildren().add(message);
		
		Label qRobot = new Label("Quel est ton con, robot?");
		TextField rRobot = new TextField();
		Label qKitten = new Label("Quel est le nom de ton chat?");
		TextField rKitten = new TextField();
		HBox hb = new HBox();
		hb.getChildren().addAll(qRobot, rRobot, qKitten, rKitten);
		Button submit = new Button("Envoyer");
		root.getChildren().addAll(hb, submit);
		stage.show();
		
		final String[] nomKitten = {""};
		final String[] nomRobot = {""};
		
		submit.setOnAction((event) -> {		//Contient le code entier: on a besoin des nom
			if (rKitten.getText() != null && !rKitten.getText().isEmpty() &&
	        		rRobot.getText() != null && !rKitten.getText().isEmpty()) {
	        	nomKitten[0] = rKitten.getText();
	        	nomRobot[0] = rRobot.getText();
	        	
	        	root.getChildren().remove(hb);
	        	root.getChildren().remove(submit);
	        	
	        	Controller controller = new Controller(nomRobot[0], nomKitten[0]);
				ArrayList<String[]> grille = controller.turn("bidon");
	        	
				message.setText(grille.get(0)[0]);
	        	
	        	
	        }
		});
				
			
			
			
			/*GridPane affichage = new GridPane();
			int y = 0;
			for(int x=0; x<grille.get(2).length; x++) {
				if(grille.get(2)[x].equals("NEXT")) {
					x--;
					y++;
					
				} else {
					Image tempImg = new Image("File:nki/" + grille.get(2)[x]);
					ImageView tempImgView = new ImageView(tempImg);
					tempImgView.setFitHeight(30);
					tempImgView.setFitWidth(30);
					affichage.setConstraints(tempImgView, x, y );
					
				}
			}
			root.getChildren().add(affichage);
			Text robotStatus = new Text(grille.get(1)[0]);
			root.getChildren().add(robotStatus);
			
			
			/*boolean winCondition = false;
			do {
				scene.setOnKeyPresser((event) -> {
					ArrayList<String[]> grille = controller.turn(event.getText());
				}
				
				message.setText(grille.get(0)[0]);
				
				y=0;
				for(int x=0; x<grille.get(2).length; x++) {
					if(grille.get(2)[x].equals("NEXT")) {
						x--;
						y++;
						
					} else {
						Image tempImg = new Image("File:nki/" + grille.get(2)[x]);
						ImageView tempImgView = new ImageView(tempImg);
						tempImgView.setFitHeight(30);
						tempImgView.setFitWidth(30);
						affichage.setConstraints(tempImgView, x, y );
						
					}
				}
				robotStatus.setText(grille.get(1)[0]);
				winCondition = grille.get(3)[0];
			} while(winCondition);*/
		
	}
}
