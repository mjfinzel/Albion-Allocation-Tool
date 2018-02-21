package program;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


/*
 * @Author: Matthew Finzel
 * Ingame character name: PoorPeasant
 */
public class GamePanel extends JPanel{

	public static boolean paused = false;
	public static ArrayList<Long> recentRolls = new ArrayList<Long>();

	static Point mousePos = new Point(-1,-1);

	static Robot robot;

	static Item[][] items = new Item[4][5];
	public static ArrayList<Player> players = new ArrayList<Player>();

	static BufferedImage window;
	public static int tax = 0;
	public static boolean visible = false;
	static long lastTimeVisible = System.currentTimeMillis();
	static int updatesSinceVisible=0;

	BufferedImage guildCut = Images.load("/Images/Guild.png");
	static BufferedImage dice = Images.load("/Images/Dice.png");
	static BufferedImage exit = Images.load("/Images/Exit.png");
	static BufferedImage keep = Images.load("/Images/Keep.png");
	static BufferedImage[][] increment = Images.cut("/Images/Increment.png", 28, 28);

	static Button diceButton = new Button(0, 155, 9);
	static Button plusButton = new Button(1, 125, 17);
	static Button minusButton = new Button(2, 10, 17);
	static Button exitButton = new Button(3, 190, 0);
	boolean loopedOnce = false;
	public GamePanel(){
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static int randomNumber(int min, int max){
		return min + (int)(Math.random() * ((max - min) + 1));
	}

	public static void sleep(int min, int max){
		try {
			Thread.sleep(randomNumber(min, max));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Point p = MouseInfo.getPointerInfo().getLocation();

		Draw(g);
	}



	public static int getItemCount(){
		int total = 0;
		for(int j = 0; j<5; j++){
			for(int i = 0; i<4; i++){

				if(items[i][j]!=null&&items[i][j].icon!=null){
					total++;
				}
			}
		}
		return total;
	}
	
	public boolean pixelsAreSimilar(Color a, Color b){
		if(Math.abs(a.getRed()-b.getRed())<50){
			if(Math.abs(a.getGreen()-b.getGreen())<30){
				if(Math.abs(a.getBlue()-b.getBlue())<30){
					return true;
				}
			}
		}
		return false;
	}
	public boolean imagesAreSame(BufferedImage aa, BufferedImage bb){
		BufferedImage a = Images.resizeImage(aa, 10, 10);
		BufferedImage b = Images.resizeImage(bb, 10, 10);
		//same dimensions
		if(b.getWidth()==a.getWidth() && b.getHeight() == a.getHeight()){
			for(int x = 0; x<b.getWidth();x++){
				for(int y = 0; y<b.getHeight(); y++){
					Color ac = new Color(a.getRGB(x, y));
					Color bc = new Color(b.getRGB(x, y));
					if(!pixelsAreSimilar(ac, bc)){
						System.out.println("FALSE");
						return false;

					}
				}
			}

		}
		else{
			return false;
		}
		return true;
	}
	public static void update(){
		window = robot.createScreenCapture(new Rectangle(1552, 504, 315, 406));
		//if the inventory is open
		//System.out.println(window.getRGB(223,400));
		if(window.getRGB(223,400)==-6453638){
			
			AppletUI.appletUI.setVisible(true);
			visible = true;

		}
		else{
			AppletUI.appletUI.setVisible(false);
			visible = false;
			updatesSinceVisible=0;
			lastTimeVisible = System.currentTimeMillis();
		}
		//System.out.println("color1 = "+window.getRGB(0,405));
		//System.out.println("color2 = "+window.getRGB(223, 400));
	}
	public void Draw(Graphics g){
		BufferedImage party = robot.createScreenCapture(new Rectangle(18, 187, 160, 310));
		mousePos = MouseInfo.getPointerInfo().getLocation();
		
		if(paused==false && window!=null){
			int itemCount = getItemCount();
			g.setColor(new Color(160,117,89));
			g.fillRect(0, 0, 200, 50+10+(itemCount*37));
			if(updatesSinceVisible<5)
				updatesSinceVisible++;
			//items
			if(loopedOnce&&visible && System.currentTimeMillis() - lastTimeVisible > 100){
				if(mousePos.x<1452 || mousePos.y<400){
					for(int y = 0; y<5; y++){
						for(int x = 0; x<4; x++){
							BufferedImage icon = window.getSubimage(x*81, (y*81), 72, 72);

							Color color = new Color(icon.getRGB(4,68));
							//System.out.println("Color: "+color);

							//if the slot has an item
							if(color.getRed()<100||color.getGreen()<100||color.getBlue()==126){
								//System.out.println(x+","+y + "   " + color);
								if(items[x][y] == null ||items[x][y].icon==null){
									items[x][y] = new Item(icon);
								}
								else if(!imagesAreSame(icon, items[x][y].icon))
									items[x][y] = new Item(icon);
							}
							else{
								//System.out.println(x+","+y + "   " + color);
								items[x][y] = new Item(null);
							}

						}
					}
				}
				else{
					g.setColor(Color.black);
					g.fillRect(0, 0, 200, 50);
					Font font = new Font("Iwona Heavy",Font.BOLD,26);
					g.setFont(font);
					FontMetrics m = g.getFontMetrics();

					g.setColor(Color.white);
					g.drawString("Move Mouse", 14, 35);
				}
			}
			
			//resize window to match item count
			AppletUI.appletUI.setSize(200, 50+10+(itemCount*37));

			g.setColor(new Color(203,173,145));
			if(mousePos.x<1452 || mousePos.y<400){
				g.fillRect(3,3, 194, 50+4+(itemCount*37));
			}
			else{
				g.fillRect(3,53, 194, 50+4+(itemCount*37) - 50);
			}
			//draw items
			int itemsDrawn = 0;
			for(int j = 0; j<5; j++){
				for(int i = 0; i<4; i++){
					//System.out.println("GOT HERE"+players.size());
					if(items[i][j]!=null&&items[i][j].icon!=null&&players.size()>0){
						
						g.drawImage(items[i][j].icon, 5, 50+5+(itemsDrawn*37), 36, 36, null);
						//g.setColor(new Color(items[i][j].icon.getRGB(4,68)));

						if(items[i][j].allocationIndex==-2)
							g.drawImage(guildCut, 41, 50+5+(itemsDrawn*37), null);
						else{
							//System.out.println(items[i][j].allocationIndex);
							if(items[i][j].allocationIndex<players.size()){
								g.drawImage(players.get(items[i][j].allocationIndex).icon, 41, 50+5+(itemsDrawn*37), 154, 36, null);
							}
							else{
								g.drawImage(keep, 41, 50+5+(itemsDrawn*37), null);
							}
						}
						itemsDrawn++;
					}
				}
			}

			if(mousePos.x<1452 || mousePos.y<400){
				//draw tax
				Font font = new Font("Iwona Heavy",Font.BOLD,28);
				g.setFont(font);
				FontMetrics m = g.getFontMetrics();

				g.setColor(new Color(99,72,55));
				g.drawString(tax + "%", 80 - (m.stringWidth(tax + "%")/2), 40);

				diceButton.draw(g);
				plusButton.draw(g);
				minusButton.draw(g);
				exitButton.draw(g);
				//g.drawImage(party, 0, 0, null);
				//detect players
				players.clear();
				for(int i = 0; i<5; i++){
					BufferedImage icon = party.getSubimage(60, i*72, 80, 17);

					//if there is a player in the group at this location on screen
					if(icon.getRGB(2,8) == -1059401||icon.getRGB(2,8) == -1124937)
						players.add(new Player(icon));
					else{
						//System.out.println("icon color: "+icon.getRGB(2,8));
					}
				}
				if(players.size()==0){
					//check for raid group
					for(int i = 0; i<10; i++){
						double offset = .4;
						BufferedImage icon = party.getSubimage(10, 2+(int)(double)((double)offset*(double)i)+(i*22), 80, 22);
						//g.drawImage(icon, 0, i*26, null);
						
						Color color = new Color(icon.getRGB(0, 5));
						if(color.getRed()>200&&color.getRed()<220)
							if(color.getBlue()<40)
								if(color.getGreen()<70&&color.getGreen()>50)
									players.add(new Player(icon));
					}
					if(players.size()>=10){
						for(int i = 0; i<10; i++){
							double offset = .4;
							BufferedImage icon = party.getSubimage(105, 2+(int)(double)((double)offset*(double)i)+(i*22), 80, 22);
							//g.drawImage(icon, 0, i*26, null);
							
							Color color = new Color(icon.getRGB(0, 5));
							if(color.getRed()>200&&color.getRed()<220)
								if(color.getBlue()<40)
									if(color.getGreen()<70&&color.getGreen()>50)
										players.add(new Player(icon));
						}
					}
				}
				loopedOnce=true;
			}
		}
		else{
			Font font = new Font("Iwona Heavy",Font.BOLD,46);
			g.setFont(font);
			FontMetrics m = g.getFontMetrics();

			g.setColor(Color.red);
			g.drawString("PAUSED", 140, 200);
		}
	}

}
