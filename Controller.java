import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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
        	
			URL url;
			URLConnection con;
			try {
				url = new URL("http://game-icons.net/recent.html");
				con = url.openConnection();
			} catch (IOException e) {
				status[0] = "Erreur de creation d'url";
				e.printStackTrace();
				return status;
			}
			
	        BufferedReader in;
			try {
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} catch (IOException e) {
				status[0] = "Erreur de creation de bufferedReader";
				e.printStackTrace();
				return status;
			}
			
	        String html = "";
	        try {
	        	String str;
				while ((str = in.readLine()) !=null) {
				    html+=str;
				}
			} catch (IOException e) {
				status[0] = "Erreur de traitement du document HTML";
				e.printStackTrace();
				return status;
			}
	        
	        int startIndex=0;
	        String searcher = "img src=\"";
	        String startStr = "http://game-icons.net";
	        int searcherL = searcher.length();
	        for(int counter=0; counter<83; counter++) {
	        	int next = searcherL+html.indexOf(searcher, startIndex);
	        	int nextParen = html.indexOf(".svg", next);
	        	
	        	startIndex = nextParen+4;
	        	
	        	Path curPath = Paths.get("nki/tempImg/"+counter+".svg");
	        	URL curUrl;
				try {
					curUrl = new URL(startStr+ html.substring(next, nextParen)+".svg");
				} catch (MalformedURLException e1) {
					status[0] = "Erreur: url de svg est errone";
					e1.printStackTrace();
					return status;
				}
				try (InputStream dwnld = curUrl.openStream()) {
        		    Files.copy(dwnld, curPath, StandardCopyOption.REPLACE_EXISTING);
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
		
		for(int counter=0; counter<83; counter++) {
			final Canvas oldCanvas = new Canvas(512,512);
			GraphicsContext gc = oldCanvas.getGraphicsContext2D();
			
			String svg;
			try {
				svg = new String(Files.readAllBytes(Paths.get("nki/tempImg/"+counter+".svg")));
			} catch (IOException e1) {
				status[0] = "Erreur lors de la lecture des svg";
				e1.printStackTrace();
				return status;
			}
			int startIndex=0;
			String[] svgPaths = new String[2];
	        String searcher = "path d=\"";
	        int searcherL = searcher.length();
	        for(int i=0; i<2; i++) {
	        	int next = searcherL+svg.indexOf(searcher, startIndex);
	        	int nextParen = svg.indexOf("\"", next);
	        	startIndex = nextParen+4;
	        	
	        	svgPaths[i] = svg.substring(next, nextParen)+".svg";
	        }
	        
	        gc.setFill(Color.BLACK);
	        gc.setStroke(Color.BLACK);
	        gc.appendSVGPath(svgPaths[1]);
	        gc.fill();
	        gc.stroke();
	        
	        SnapshotParameters sp = new SnapshotParameters();
	        WritableImage wi = new WritableImage((int)oldCanvas.getWidth(),(int)oldCanvas.getHeight());
	        WritableImage snapShot = oldCanvas.snapshot(sp, wi);
	        File outputPath = new File("nki/tempImg/"+counter+".png");
	        
	        try {
				ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", outputPath);
			} catch (IOException e) {
				status[0] = "Erreur lors de la sauvegarde des nouveaux png";
				e.printStackTrace();
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
