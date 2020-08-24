package com.dungeonGame.world;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dungeonGame.main.Constants;
import com.dungeonGame.main.Game;

public class Tile {

//	public static BufferedImage TILE_FLOOR_1 = Game.spritesheet.getSprite(0, Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage TILE_FLOOR_1 = Game.spritesheet.getSprite(0, Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage TILE_FLOOR_2 = Game.spritesheet.getSprite(Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage TILE_FLOOR_3 = Game.spritesheet.getSprite(Constants.TILE_SIZE * 2, Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage TILE_FLOOR_GREEN = Game.spritesheet.getSprite(Constants.TILE_SIZE * 3, Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
	
	public static BufferedImage TILE_WALL_1 = Game.spritesheet.getSprite(0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage TILE_WALL_2 = Game.spritesheet.getSprite(Constants.TILE_SIZE, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage TILE_WALL_3 = Game.spritesheet.getSprite(Constants.TILE_SIZE * 2, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
	public static BufferedImage TILE_WALL_GREEN = Game.spritesheet.getSprite(Constants.TILE_SIZE * 3, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);

	private BufferedImage sprite;
	private int x, y;
	
	public Tile(BufferedImage sprite, int x, int y) {
		super();
		this.sprite = sprite;
		this.x = x;
		this.y = y;
	}

	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}


}
