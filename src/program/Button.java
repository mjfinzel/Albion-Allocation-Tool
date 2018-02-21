package program;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Button {
	BufferedImage icon;
	int xpos;
	int ypos;
	int id;
	public Button(int id, int x, int y){
		xpos = x;
		ypos = y;
		this.id = id;
		if(id==0){
			icon = GamePanel.dice;
		}
		else if(id==1){
			icon = GamePanel.increment[0][0];
		}
		else if(id==2){
			icon = GamePanel.increment[1][0];
		}
		else if(id==3){
			icon = GamePanel.exit;
		}
	}
	public void pressed(){
		if(id==0){
			GamePanel.items = new Item[4][5];
			GamePanel.recentRolls.add(System.currentTimeMillis());
		}
		else if(id==1){
			if(GamePanel.tax<100){
				GamePanel.tax++;
			}
		}
		else if(id==2){
			if(GamePanel.tax>0){
				GamePanel.tax--;
			}
		}
		else if(id==3){
			AppletUI.appletUI.dispatchEvent(new WindowEvent(AppletUI.appletUI, WindowEvent.WINDOW_CLOSING));
		}
	}
	public boolean mouseIsOver(){
		int x = GamePanel.mousePos.x;
		int y = GamePanel.mousePos.y;

		if(x>=1306+xpos&&x<=1306+xpos + icon.getWidth()){
			if(y>=150+ypos&&y<=150+ypos+icon.getHeight()){
				return true;
			}
		}
		return false;
	}
	public void draw(Graphics g){
		g.drawImage(icon, xpos, ypos, null);
		if(id==0){//dice
			while(GamePanel.recentRolls.size()>0&&System.currentTimeMillis() - GamePanel.recentRolls.get(0)>60000){
				GamePanel.recentRolls.remove(0);
			}
			if(GamePanel.recentRolls.size()>0){
				g.setColor(Color.red);

				Font font = new Font("Iwona Heavy",Font.BOLD,28);
				g.setFont(font);
				FontMetrics m = g.getFontMetrics();
				g.drawString(GamePanel.recentRolls.size()+"", xpos + (icon.getWidth()/2) - (m.stringWidth(GamePanel.recentRolls.size()+"")/2), ypos+30);
			}
		}
		if(mouseIsOver()){
			g.setColor(new Color(255,255,255, 40));
			g.fillRect(xpos, ypos, icon.getWidth(), icon.getHeight());
		}
	}
}
