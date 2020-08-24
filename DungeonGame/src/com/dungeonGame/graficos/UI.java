package com.dungeonGame.graficos;
import java.awt.Color;
import java.awt.Graphics;

import com.dungeonGame.entities.PotionType;
import com.dungeonGame.main.Constants;
import com.dungeonGame.main.Game;

public class UI {
	
	private PotionType potionType;
	
	public UI(PotionType potionType) {
		this.potionType = potionType;
	}
	
	public void render(Graphics g) {
		
		if (potionType == PotionType.LIFE) {
			g.setColor(Color.red);
			g.fillRect(Constants.LIFE_BAR_XPOS, Constants.LIFE_BAR_YPOS, Constants.BAR_WIDTH, Constants.BAR_HEIGHT);
			g.setColor(Color.green);
			g.fillRect(Constants.LIFE_BAR_XPOS, Constants.LIFE_BAR_YPOS, (int)((Game.player.getLife()/Constants.PLAYER_MAX_LIFE) * Constants.BAR_WIDTH), Constants.BAR_HEIGHT);
		} else if (potionType == PotionType.MANA) {
			g.setColor(Color.blue);
			g.fillRect(Constants.MANA_BAR_XPOS, Constants.MANA_BAR_YPOS, Constants.BAR_WIDTH, Constants.BAR_HEIGHT);
			g.setColor(Color.cyan);
			g.fillRect(Constants.MANA_BAR_XPOS, Constants.MANA_BAR_YPOS, (int)((Game.player.getMana()/Constants.PLAYER_MAX_MANA) * Constants.BAR_WIDTH), Constants.BAR_HEIGHT);
		}
	}
}
