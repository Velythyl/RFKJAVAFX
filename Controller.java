import java.awt.AlphaComposite;
import java.awt.Color;
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

public class Controller {
	private RobotFindsKitten rfk;
	private boolean firstTurnDone;
	private boolean isGameDone;
	private boolean sound;
	private boolean dlc;
	
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
		this.dlc = false;
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
	
	public void toggleDLC() {
		this.dlc = !dlc;
	}
	
	public boolean hasDLC() {
		return this.dlc;
	}
	
	/**
	 * Les methodes ...DLC() telechargent de nouvelles images pour l'affichage.
	 * 
	 * En regle generale, l'utilisateur ne voit pas le message de status de ces methodes.
	 * Si quelque chose plante, on le voit pour savoir quoi debugger. Normalement, l'utilisateur
	 * voit quelque chose comme "succes! vous pouvez lancer le jeu"
	 * 
	 * getDLC prend le document html des icons recents de game-icons.net dans un string
	 * On parse le string et ou trouve les sources des images (icones). Ces liens sont vers des svg
	 * On remplace svg par png et on download ces images dans nki/tempImg/counter.png
	 * 
	 * Si ces methodes n'ont pas eu d'erreur, le deuxieme champ de status est "false"
	 * 
	 * @return le message de status
	 */
	public String[] getDLC() {
		String[] status = {"", "true"};
		
		/*
		 * Ping du site pour verifier la connection internet
		 */
		boolean reachable = false;
		try{
            InetAddress address = InetAddress.getByName("game-icons.net");
            reachable = address.isReachable(10000);
        } catch (Exception e){
            e.printStackTrace();
            status[0] = "Ping vers game-icons.net a echoue: mauvaise connexion";
            return status;
        }
		
		if(reachable) {
        	/*
        	 * Si on a internet, on continue
        	 */
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
	        //html a maintenant un string qui est tous le document html de game-icons.net/recent.html
	        
	        try {
				in.close();
			} catch (IOException e2) {
				status[0] = "Erreur de fermeture du reader";
				e2.printStackTrace();
				return status;
			}
	        
	        /*
	         * Puis, on cherche ce string html pour img src="
	         * 
	         * Lorsqu'on l'a, on prend le string qui part de ca a .svg
	         * 
	         * On remplace les svg dans ce string par des png
	         * 
	         * On sauve le png vers lequel ce string en url pointe
	         */
	        int startIndex=0;
	        String searcher = "img src=\"";
	        String startStr = "http://game-icons.net";
	        int searcherL = searcher.length();
	        for(int counter=0; counter<83; counter++) {
	        	int next = searcherL+html.indexOf(searcher, startIndex);
	        	int nextParen = html.indexOf(".svg", next);
	        	
	        	startIndex = nextParen+4;
	        	
	        	Path curPath = Paths.get("nki/tempImg/"+counter+".png");
	        	
	        	String strUrl = (startStr+ html.substring(next, nextParen)+".png").replaceAll("svg", "png");
	        	URL curUrl;
				try {
					curUrl = new URL(strUrl);
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
        	status[0] = "Ping vers game-icons.net a echoue: mauvaise connexion";
        	return status;
        }
	}
	
	/**
	 * On scale et on recolorie les images. 
	 * 
	 * On perds de la resolution, mais c'est le mieux qu'on peut faire sans utiliser des librairies
	 * externes. Je m'etais mise au defit de tout faire en jdk9 pur.
	 * 
	 * On recolorie les images en les traversant et en creant une nouvelle image newImg.
	 * On regarde la couleur du pixel x,y de oldImg: si noir, newImg x,y sera transparent, et blanc sinon
	 * 
	 * @return le status de l'operation
	 */
	public String[] resizeAndColorDLC() {
		String[] status = {"","false"};
		
		/*
		 * On charge toutes les images dlc en Image 50,50
		 * 
		 * Oncree un buffered reader vide
		 * 
		 * On parcours les pixels de la oldImg et on change la couleur de ceux de newImg
		 * de facon appropriee: noir=transparent, autre=blanc
		 */
		for(int counter=0; counter<83; counter++) {
			Image tempOldImg = new Image("File:nki/tempImg/"+counter+".png", 50, 50, false, false);
			BufferedImage oldImg = SwingFXUtils.fromFXImage(tempOldImg, null);
			
			BufferedImage newImg = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		 
			Color black = new Color(0, 0, 0, 255);
			Color white = new Color(255, 255, 255, 255);
			Color transp = new Color(0, 0, 0, 0);
			for (int x = 0; x <50; x++) {
		        for (int y = 0; y <50; y++) {
		        	int currentColor = oldImg.getRGB(x, y);
		        	
		        	if(currentColor == black.getRGB()) {
		        		newImg.setRGB(x, y, transp.getRGB());
		        	} else newImg.setRGB(x, y, white.getRGB());
		        }
		    }
			
			/*
			 * On sauve cette nouvelle image par-dessus l'ancienne
			 */
			File outputfile = new File("nki/tempImg/"+counter+".png");
			try {
				ImageIO.write(newImg, "png", outputfile);
			} catch (IOException e) {
				status[0] = "Erreur d'ecriture des nouveaux png";
				e.printStackTrace();
				return status;
			}
		}

		status[0] = "Image rapetisee et recoloriee avec succes";
		return status;
		
	}

	/**
	 * On merge les images et le back utilise dans notre affichage
	 * 
	 * On utilise un alphaComposite... voir le lien
	 * http://www.java2s.com/Tutorials/Java/Graphics_How_to/Image/Merge_an_image_over_another_in_the_specified_position_and_save_it_as_a_new_image.htm
	 * 
	 * Puis, on sauve la nouvelle image dans nki/dlc et on delete l'ancienne image dans nki/tempImg
	 * 
	 * @return le status de l'operation. Si tout vas bien, l'utilisateur recoit un message de succes a cette etape
	 */
	public String[] parseDLC() {
		String[] status = {"","false"};
		
		for(int counter=0; counter<83; counter++) {
			//http://www.java2s.com/Tutorials/Java/Graphics_How_to/Image/Merge_an_image_over_another_in_the_specified_position_and_save_it_as_a_new_image.htm
			Image back = new Image("File:nki/back.png");
			BufferedImage bBack = SwingFXUtils.fromFXImage(back, null);
			
			Image dlcImg = new Image("File:nki/tempImg/"+counter+".png");
			BufferedImage bDlcImg = SwingFXUtils.fromFXImage(dlcImg, null);
			
			Graphics2D g = bBack.createGraphics();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g.drawImage(bDlcImg, (bDlcImg.getWidth() - bDlcImg.getWidth()) / 2, (bDlcImg.getHeight() - bDlcImg.getHeight()) / 2, null);
			g.dispose();
			
			/*
			 * Lorsqu'on appelle les dlc de controller, on fait en sorte que les images seront tirees du
			 * fichier nki/dlc. Comme ca on garde toujours l'affichage du jeu dans nki
			 */
			File outputPath = new File("nki/dlc/"+counter+".png");
			try {
				ImageIO.write(bBack, "png", outputPath);
			} catch (IOException e) {
				e.printStackTrace();
				status[0] = "Une erreur d'ecriture de l'image mergee est survenue";
				status[1] = "true";
				return status;
			}
			
			/*
			 * Pour prendre moins de place, on delete les anciennes images
			 */
			File file = new File("nki/tempImg/"+counter+".png");
			file.delete();
		}
		
		
		status[0] = "Succes! Vous pouvez lancer le jeu et les DLC seront utilises";
		return status;
	}
}
