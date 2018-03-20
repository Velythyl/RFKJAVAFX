import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

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
	
	public void toggleSound() {
		this.sound = !this.sound;
	}
	
	public boolean hasSound() {
		return this.sound;
	}
	
	public String[] getDLC() {
		String[] status = {"", "true"};
		
		boolean reachable = false;
		try{
            InetAddress address = InetAddress.getByName("game-icons.net");
            reachable = address.isReachable(10000);
        } catch (Exception e){
            e.printStackTrace();
            status[0] = "Ping vers google a echoue: mauvaise connexion";
            return status;
        }
		
		if(reachable) {
        	ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        	
        	try {
				engine.eval(new FileReader("nki/dlc.js"));
			} catch (FileNotFoundException | ScriptException e1) {
				status[0] = "Erreur: js script introuvable";
				e1.printStackTrace();
				return status;
			}
			
			Invocable invocable = (Invocable) engine;
			String[] urlArray;
			try {
				urlArray = (String[]) invocable.invokeFunction("func");
			} catch (NoSuchMethodException | ScriptException e1) {
				status[0] = "Erreur: js script incomplet";
				e1.printStackTrace();
				return status;
			}
			//ligne ci-haut est un faut cast: c'est deja un String[] de java (voir dlc.js)

			
        	for(int counter=0; counter<urlArray.length; counter++) {
        		URL curUrl;
				try {
					curUrl = new URL(urlArray[counter]);
				} catch (MalformedURLException e1) {
					status[0] = "Erreur: mauvais url";
					e1.printStackTrace();
					return status;
				}
        		Path curPath = Paths.get("nki/tempImg/"+counter+".svg");
        		
        		try (InputStream in = curUrl.openStream()) {
        		    Files.copy(in, curPath, StandardCopyOption.REPLACE_EXISTING);
        		} catch (Exception e){
        			e.printStackTrace();
        			status[0] = "Une erreur de telechargement de game-icons.net est survenue";
        			return status;
        		}            		
        	}
        	status[0] = "Telechargement avec succes. Traitement...";
        	status[1] = "false";
        	return status;
        	
        } else {
        	status[0] = "Ping vers google a echoue: mauvaise connexion";
        	return status;
        }
	}
	
	public String[] resizeAndColorDLC() {
		String[] status = {"","false"};
		
		for(int counter=0; counter<82; counter++) {
			Image oldImg = new Image("nki/tempImg/"+counter+".svg", 50, 50, false, false);
			
			BufferedImage newImg = SwingFXUtils.fromFXImage(oldImg, null);
			
			//https://stackoverflow.com/questions/27462758/how-to-replace-color-with-another-color-in-bufferedimage
			int target = Color.BLACK.getRGB();
			int preferred = new Color(00, 00, 00, 00).getRGB();
			int color;
			for (int i = 0; i <50; i++) {
		        for (int j = 0; j <50; j++) {
		            color = newImg.getRGB(i, j);
		            if (color == target) {
		                newImg.setRGB(i, j, preferred);
		            }
		        }
		    }
			
			File outputPath = new File("nki/tempImg/"+counter+".png");
		
			try {
				ImageIO.write(newImg, "png", outputPath);
			} catch (IOException e) {
				e.printStackTrace();
				status[0] = "Une erreur d'ecriture est survenue";
				status[1] = "true";
				return status;
			}
			
		}
		
		status[0] = "Image rapetisee et recoloriee avec succes";
		return status;
		
	}

	public String[] parseDLC() {
		String[] status = {"","false"};
		
		for(int counter=0; counter<82; counter++) {
			//https://stackoverflow.com/questions/2318020/merging-two-images
			
			Image dlcImg = new Image("nki/tempImg/"+counter+".png");
			BufferedImage bDlcImg = SwingFXUtils.fromFXImage(dlcImg, null);
			
			Image back = new Image("nki/back.png");
			BufferedImage bBack = SwingFXUtils.fromFXImage(back, null);
			
			BufferedImage combined = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = combined.getGraphics();
			g.drawImage(bDlcImg, 0, 0, null);
			g.drawImage(bBack, 0, 0, null);
			
			File outputPath = new File("nki/"+counter+".png");
			try {
				ImageIO.write(combined, "png", outputPath);
			} catch (IOException e) {
				e.printStackTrace();
				status[0] = "Une erreur d'ecriture de l'image mergee est survenue";
				status[1] = "true";
				return status;
			}
		}
		
		status[0] = "Succes! Vous pouvez lancer le jeu et les DLC seront utilises";
		return status;
	}
}
