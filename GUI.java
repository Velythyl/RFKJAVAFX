import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	
	public static void main(String[] args) {
		GUI.launch(args);
	}
	
	
	public void start(Stage stage) throws Exception {
		VBox root = new VBox();
		root.setStyle("-fx-background: #000000;");
		root.setAlignment(Pos.TOP_CENTER);
		Scene scene = new Scene(root, 1280, 720);
		stage.setTitle("RobotFindsKitten");
		stage.setScene(scene);
		
		Text message = new Text("Bienvenue dans RobotFindsKitten: Super Dungeon Master 3000 Ultra Turbo Edition !");
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
		
		Controller controller = new Controller();
		
		Text robotStatus = new Text("");
		robotStatus.setFont(Font.font ("Verdana", 25));
		robotStatus.setFill(Color.WHITE);
		robotStatus.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().add(robotStatus);
		
		stage.show();

		submit.setOnAction((event) -> {		//Contient le code entier: on a besoin des nom
			if (rKitten.getText() != null && !rKitten.getText().isEmpty() &&
					rRobot.getText() != null && !rKitten.getText().isEmpty()) {
	        	
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
	        }
		});
		
		scene.setOnKeyPressed((event) -> {
			if(controller.didFirstTurn()) {
				Turn otherTurns = controller.turn(event.getText());
				String[] firstStatus = otherTurns.getStatus();
	    		
	    		if(otherTurns.getWinCondition()) {
	    			root.getChildren().remove(message);
	    			root.getChildren().remove(robotStatus);
	    			root.getChildren().remove(affichage);
	    			
	    			Image tempImg = new Image("File:nki/found-kitten.png");
					ImageView tempImgView = new ImageView(tempImg);
					tempImgView.setFitHeight(720);
					tempImgView.setFitWidth(1280);
					
					Text victoire1 = new Text("You found kitten! Way to go, robot.\n");
					victoire1.setFill(Color.RED);
					victoire1.setFont(Font.font ("Verdana", 30));
					victoire1.setTranslateY(210);
					
					Text victoire2 = new Text(firstStatus[0]);
					victoire2.setFill(Color.RED);
					victoire2.setFont(Font.font ("Verdana", 30));
					victoire2.setTranslateY(230);
					
					StackPane pane = new StackPane();
					
					pane.getChildren().add(tempImgView);
				    pane.getChildren().add(victoire1);
				    pane.getChildren().add(victoire2);
					
				    root.getChildren().add(pane);
				    
	    		} else {
	    			
		    		message.setText(firstStatus[0]);
		    		robotStatus.setText(firstStatus[1]);
		    		
	    			String[] nextRep = otherTurns.getNextRep();
	    			
	    			String rob = nextRep[0];
	    			int xRob = Integer.parseInt(nextRep[1]);
	    			int yRob = Integer.parseInt(nextRep[2]);
	    			Image tempRobImg = new Image(rob);
					
	    			String former = nextRep[3];
	    			int xFormer = Integer.parseInt(nextRep[4]);
	    			int yFormer = Integer.parseInt(nextRep[5]);
	    			Image tempFormerImg = new Image(former);
	    			
	    			boolean dif = (xRob != xFormer || yRob != yFormer);
	    			boolean breakerRob = false;
	    			boolean breakerFormer = false;
	    			if(!dif) breakerFormer = true;
					for (Node node : affichage.getChildren()) {
					    if (affichage.getColumnIndex(node) == xRob && affichage.getRowIndex(node) == yRob) {
					        ((ImageView) node).setImage(tempRobImg);
					        breakerRob = true;
					    }
					    
					    if(dif) {
						    if (affichage.getColumnIndex(node) == xFormer && affichage.getRowIndex(node) == yFormer) {
						        ((ImageView) node).setImage(tempFormerImg);
						        breakerFormer = true;
						    }
					    }
					    
					    if(breakerRob && breakerFormer) break;
					}
	    		}
			}
		});
	}
}
