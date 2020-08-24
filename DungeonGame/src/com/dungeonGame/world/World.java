package com.dungeonGame.world;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.dungeonGame.entities.BigPotion;
import com.dungeonGame.entities.Enemy;
import com.dungeonGame.entities.Entity;
import com.dungeonGame.entities.PotionType;
import com.dungeonGame.entities.SmallPotion;
import com.dungeonGame.entities.Staff;
import com.dungeonGame.main.Constants;
import com.dungeonGame.main.Game;

public class World {

	private static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	
	public World(String path) {
		
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			
			int[] pixels = new int[WIDTH * HEIGHT];
			map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
			tiles = new Tile[WIDTH * HEIGHT];

			for (int xx = 0; xx < WIDTH; xx++) {
				for (int yy = 0; yy < HEIGHT; yy++ ) {
					
					int pixelatual = pixels[xx + (yy * WIDTH)];
					
					if (pixelatual == 0xFF000000) {
						//FLOOR
						int randomFloor = new Random().nextInt(13);
						BufferedImage sprite = Tile.TILE_FLOOR_1;
						
						if (randomFloor <= 6) {
							sprite = Tile.TILE_FLOOR_1;
						} else if (randomFloor <=11) {
							sprite = Tile.TILE_FLOOR_2;
						} else if (randomFloor <= 12) {
							sprite = Tile.TILE_FLOOR_3;
						} else {
							sprite = Tile.TILE_FLOOR_1;
						}

						tiles[xx + (yy * WIDTH)] = new FloorTile(sprite, xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE);

					} else if (pixelatual == 0xFF00FF00) {
						
						//FLOOR WITH GREEN WATER
						tiles[xx + (yy * WIDTH)] = new FloorTile(Tile.TILE_FLOOR_GREEN, xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE);
						
					} else if (pixelatual == 0xFFFFFFFF) {
						
						//Wall
						int randomInt = new Random().nextInt(3);
						BufferedImage sprite = Tile.TILE_WALL_1;
						
						switch (randomInt) {
						case 0:
							sprite = Tile.TILE_WALL_1;
							break;
						case 1:
							sprite = Tile.TILE_WALL_2;
							break;
						case 2:
							sprite = Tile.TILE_WALL_3;
							break;
						default:
							sprite = Tile.TILE_WALL_1;
						}
						
						tiles[xx + (yy * WIDTH)] = new WallTile(sprite, xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE);
//						FFAA91
					} else if (pixelatual == 0xFF808080) {
						
						//Wall with green water
						tiles[xx + (yy * WIDTH)] = new WallTile(Tile.TILE_WALL_GREEN, xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE);
						
					} else if (pixelatual == 0xFFFFAA91) {
						
						//Weapon
						Staff e = new Staff((xx*Constants.TILE_SIZE) + (Constants.STAFF_WIDTH/2), yy*Constants.TILE_SIZE, Constants.STAFF_WIDTH, Constants.STAFF_HEIGHT, Entity.STAFF);
						Game.entities.add(e);
						tiles[xx + (yy * WIDTH)] = new FloorTile(Tile.TILE_FLOOR_1, xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE);
						
					} else if (pixelatual == 0xFFFF00DC) {
				
						//Enemy
						tiles[xx + (yy * WIDTH)] = new FloorTile(Tile.TILE_FLOOR_1, xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE);
						Enemy e = new Enemy(xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE, Entity.ENEMY);
						Game.entities.add(e);
						
					} else if (pixelatual == 0xFFFFD800) {

						//Player
						tiles[xx + (yy * WIDTH)] = new FloorTile(Tile.TILE_FLOOR_1, xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE);
						Game.player.setX(xx * Constants.TILE_SIZE);
						Game.player.setY(yy * Constants.TILE_SIZE);
						
					} else if (pixelatual == 0xFFFF0000) {

						//Life Potion
						
						int randomInt = new Random().nextInt(2);
						BufferedImage sprite = Tile.TILE_WALL_1;
						PotionType potionType = PotionType.LIFE;
						Entity e = new Entity();
						
						switch (randomInt) {
						case 0:
							sprite = Entity.LIFE_POTION_BIG;
							e = new BigPotion(xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE, Constants.TILE_SIZE, 
									Constants.TILE_SIZE, sprite, potionType);
							break;
						case 1:
							sprite = Entity.LIFE_POTION_SMALL;
							e = new SmallPotion(xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE, Constants.TILE_SIZE, 
									Constants.TILE_SIZE, sprite, potionType);
							break;
						default:
							sprite = Entity.LIFE_POTION_SMALL;
							e = new BigPotion(xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE, Constants.TILE_SIZE, 
									Constants.TILE_SIZE, sprite, potionType);
							break;
						}
						
						tiles[xx + (yy * WIDTH)] = new FloorTile(Tile.TILE_FLOOR_1, xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE);
						Game.entities.add(e);
						
					} else if (pixelatual == 0xFF0094FF) {

						//Mana Potion
						int randomInt = new Random().nextInt(2);
						BufferedImage sprite = Tile.TILE_WALL_1;
						PotionType potionType = PotionType.MANA;
						Entity e = new Entity();
						
						switch (randomInt) {
						case 0:
							sprite = Entity.MANA_POTION_BIG;
							e = new BigPotion(xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE, Constants.TILE_SIZE, 
									Constants.TILE_SIZE, sprite, potionType);
							break;
						case 1:
							sprite = Entity.MANA_POTION_SMALL;
							e = new SmallPotion(xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE, Constants.TILE_SIZE, 
									Constants.TILE_SIZE, sprite, potionType);
							break;
						default:
							sprite = Entity.LIFE_POTION_SMALL;
							break;
						}
						
						tiles[xx + (yy * WIDTH)] = new FloorTile(Tile.TILE_FLOOR_1, xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE);
						Game.entities.add(e);
						
					}

				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / Constants.TILE_SIZE;
		int y1 = ynext / Constants.TILE_SIZE;
		
		int x2 = (xnext+Constants.TILE_SIZE-1) / Constants.TILE_SIZE;
		int y2 = (ynext / Constants.TILE_SIZE);
		
		int x3 = xnext / Constants.TILE_SIZE;
		int y3 = (ynext+Constants.TILE_SIZE-1) / Constants.TILE_SIZE;
		
		int x4 = (xnext+Constants.TILE_SIZE-1) / Constants.TILE_SIZE;
		int y4 = (ynext+Constants.TILE_SIZE-1) / Constants.TILE_SIZE;
		
		return !(tiles[x1 + (y1*World.WIDTH)] instanceof WallTile ||
				tiles[x2 + (y2*World.WIDTH)] instanceof WallTile ||
				tiles[x3 + (y3*World.WIDTH)] instanceof WallTile ||
				tiles[x4 + (y4*World.WIDTH)] instanceof WallTile);
	}
	
	public void render(Graphics g) {
		
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4) + 2;
		int yfinal = ystart + (Game.HEIGHT >> 4) + 2;
		
		for(int xx = xstart; xx < xfinal; xx++) {
			for(int yy = ystart; yy < yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}















