package com.dungeonGame.entities;
import java.awt.image.BufferedImage;

import com.dungeonGame.main.Game;

public class BigPotion extends Entity {

	private PotionType potionType;
	private double amount = 50;
	
	public BigPotion(int x, int y, int width, int height, BufferedImage sprite, PotionType potionType) {
		super(x, y, width, height, sprite);
		this.setMask(width/4, height/4, width/2, height/2);
		this.potionType = potionType;
	}
	
	public void drinkPotion() {
		if (getPotionType() == PotionType.LIFE) {
			Game.player.heal(this.getAmount());
		} else if (getPotionType() == PotionType.MANA) {
			Game.player.regenMana(this.getAmount());
		}
	}
	
	public PotionType getPotionType() {
		return potionType;
	}

	public void setPotionType(PotionType potionType) {
		this.potionType = potionType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
