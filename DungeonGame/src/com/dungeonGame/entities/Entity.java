package com.dungeonGame.entities;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.dungeonGame.graficos.Spritesheet;
import com.dungeonGame.main.Constants;
import com.dungeonGame.main.Game;
import com.dungeonGame.world.Camera;

public class Entity {
	
	//Sprites
	public static BufferedImage LIFE_POTION_SMALL = Game.spritesheet.getSprite(Constants.TILE_SIZE * 5, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage LIFE_POTION_BIG = Game.spritesheet.getSprite(Constants.TILE_SIZE * 4, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage MANA_POTION_SMALL = Game.spritesheet.getSprite(Constants.TILE_SIZE * 5, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage MANA_POTION_BIG = Game.spritesheet.getSprite(Constants.TILE_SIZE * 4, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage ENEMY = Game.spritesheet.getSprite(Constants.TILE_SIZE * 6, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage STAFF = new Spritesheet("/weapon_red_magic_staff.png").getSprite(0, 0, Constants.STAFF_WIDTH, Constants.STAFF_HEIGHT);
	
	protected double x, y;
	protected int width, height;
	
	protected BufferedImage sprite;
	
	protected int maskx, masky, mwidth, mheight;

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public Entity() {
		
	}
	
	public void tick() {
		
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
	
	// Getters and Setters
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	
	public int getX() {
		return (int)x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int)y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	

}
