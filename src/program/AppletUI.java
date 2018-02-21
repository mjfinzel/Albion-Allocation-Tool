package program;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class AppletUI extends JFrame{
	public BufferStrategy myStrategy;
	private static final long serialVersionUID = -6215774992938009947L;
	public static int windowWidth=200;
	public static int windowHeight=100;
	public static Point location = new Point(1306,150);
	public static int GAME_FPS = 3;
	public static final long milisecInNanosec = 1000000L;
	public static final long secInNanosec = 1000000000L;
	private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
	static AppletUI appletUI;
	Controller ctrl;
	GamePanel drawPanel;
	public static void main(String[] args) throws IOException{

		appletUI = new AppletUI ();
		appletUI.setSize(windowWidth,windowHeight);
		appletUI.setVisible(true);
		
	}
	public AppletUI() {
		setUndecorated(true);  
		setIgnoreRepaint(true);
		setTitle("AAT (Albion Allocation Tool)");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(windowWidth,windowHeight);
		
		setVisible(true);
		setFocusable(true);
		
		this.createBufferStrategy(2);
		myStrategy = getBufferStrategy();
		
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		
		drawPanel = new GamePanel();
		drawPanel.setIgnoreRepaint(true);
		
		this.setAlwaysOnTop(true);
		drawPanel.setBackground(new Color(160,117,89));
		pane.add(drawPanel);
		
		ctrl = new Controller();
		this.addKeyListener(ctrl);
		ctrl.setGamePanel(drawPanel);
		//this.setFocusable(true);
		
		setLocation(location.x,location.y);
		//this.setExtendedState(Frame.MAXIMIZED_BOTH);  
		setFocusableWindowState(false);
		setFocusable(false);
		//setVisible(false);
		
		//We start game in new thread.
		Thread gameThread = new Thread() {			
			public void run(){
				gameLoop();
			}
		};
		//setVisible(true);
		gameThread.start();
	}
	public void update(){
		Graphics2D g = (Graphics2D) myStrategy.getDrawGraphics();
		drawPanel.Draw(g);
		myStrategy.show();
		GamePanel.update();
	}
	public void gameLoop(){
		long beginTime, timeTaken, timeLeft;
		while(true){
			windowWidth = this.getWidth();
			windowHeight = this.getHeight();
			
			
			beginTime = System.nanoTime();
			update();
			//repaint();
			
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
			// If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
			if (timeLeft < 10){ 
				timeLeft = 10; //set a minimum
			}
			try {
				//Provides the necessary delay and also yields control so that other thread can do work.
				Thread.sleep(timeLeft);
			} catch (InterruptedException ex) { }
		}
	}
}
