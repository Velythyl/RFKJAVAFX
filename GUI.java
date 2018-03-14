import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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

		GridPane affichage = new GridPane();
		root.getChildren().add(affichage);
		
		Controller controller = new Controller();
		
		Text robotStatus = new Text("");
		root.getChildren().add(robotStatus);
		stage.show();

		submit.setOnAction((event) -> {		//Contient le code entier: on a besoin des nom
			if (rKitten.getText() != null && !rKitten.getText().isEmpty() &&
					rRobot.getText() != null && !rKitten.getText().isEmpty()) {
	        	
	        	controller.generateRobKit(rRobot.getText(), rKitten.getText());
	        	
	        	root.getChildren().remove(hb);
	        	root.getChildren().remove(submit);
	    		
	    		Turn firstTurn = controller.turn("m");
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
	    		affichage.getChildren().clear();
	    		
	    		if(otherTurns.getWinCondition()) {
	    			root.getChildren().remove(message);
	    			root.getChildren().remove(robotStatus);
	    			root.getChildren().remove(affichage);
	    			
	    			Image tempImg = new Image("File:nki/found-kitten.png");
					ImageView tempImgView = new ImageView(tempImg);
					tempImgView.setFitHeight(720);
					tempImgView.setFitWidth(1280);
					
					Text victoire = new Text(firstStatus[0]);
					StackPane pane = new StackPane();
					
					pane.getChildren().add(tempImgView);
				    pane.getChildren().add(victoire);
					
				    root.getChildren().add(pane);
	    			//gagne
	    		} else {
	    			
		    		message.setText(firstStatus[0]);
		    		robotStatus.setText(firstStatus[1]);
		    		
	    			ArrayList<ArrayList<ImageView>> otherGrids = otherTurns.getGrid();
	    			
	    			for(int y=0; y<otherGrids.size(); y++) {
		    			for(int x=0; x<otherGrids.get(0).size(); x++) {
		    				affichage.add(otherGrids.get(y).get(x), x, y);
		    			}
		    		}
	    		}
			}
		});
	}
}
