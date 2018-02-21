package program;

import java.awt.image.BufferedImage;

public class Item {
	BufferedImage icon;
	int allocationIndex = -1;//-1 = unallocated, -2 = guild, otherwise it's the index of a player
	public Item(BufferedImage icon){
		this.icon = icon;
		if(icon!=null)
			allocate();
	}
	public void allocate(){
		double tax = (double)GamePanel.tax/100.0;
		double roll = GamePanel.randomNumber(1, 1000000);
		double limit = 1000000.0 * (1.0 - tax);
		if(roll > limit){
			allocationIndex = -2;
		}
		else{
			if(GamePanel.players.size()>0){
				double players = GamePanel.players.size()+1;
				allocationIndex = (int)((double)(players * (roll/limit)));
			}
			else{
				allocationIndex = 0;
			}
		}
	}

}
