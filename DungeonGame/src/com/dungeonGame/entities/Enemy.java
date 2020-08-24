package com.dungeonGame.entities;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import com.dungeonGame.graficos.Spritesheet;
import com.dungeonGame.main.Constants;
import com.dungeonGame.main.Game;
import com.dungeonGame.world.Camera;
import com.dungeonGame.world.World;

public class Enemy extends Entity{
	
	private int damage = 5;
	private int attakSpeed = 60;
	private double speed = 1.0;
	
	//Directions variables
	private boolean right, left, up, down, moving;
	private int right_dir = 0, left_dir = 1;
	private int dir = right_dir;
	
	//Sprites
	private Spritesheet enemySpriteSheet;
	private Spritesheet idleenemySpriteSheet;
	
	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3, attakFrames = 90;
	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;
	private BufferedImage[] idlerightEnemy;
	private BufferedImage[] idleleftEnemy;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		setupSprites();
	}
	
	public void tick() {
		
		setMoving(false);
		move();
		updateFrames();
	}
	
	
	public void render(Graphics g) {
		
		if (isMoving()) {
			if (dir == right_dir) {
				g.drawImage(rightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == left_dir) {
				g.drawImage(leftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		} else {
			if (dir == right_dir) {
				g.drawImage(idlerightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == left_dir) {
				g.drawImage(idleleftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
	}
	
	public void move() {
		
		double range = 100;
		
		if (Game.player.getX() < this.getX() + range  && Game.player.getX() > this.getX() - range
				&& Game.player.getY() > this.getY() - range && Game.player.getY() < this.getY() + range) {
			
			if (!isCollidingWithPlayer()) {
				if (this.getX() < Game.player.getX() && World.isFree((int)(x + speed), (int)y)
						&& !isColliding((int)(x + speed), (int)y)) {
					
					x += speed;
					dir = right_dir;
					setMoving(true);
					
				} else if (this.getX() > Game.player.getX() && World.isFree((int)(x - speed), (int)y)
						&& !isColliding((int)(x - speed), (int)y)) {
					
					x -= speed;
					dir = left_dir;
					setMoving(true);
					
				} else if (this.getY() < Game.player.getY() && World.isFree((int)x, (int)(y + speed))
						&& !isColliding((int)x, (int)(y + speed))) {
					
					y += speed;
					setMoving(true);
					
				} else if (this.getY() > Game.player.getY() && World.isFree((int)x, (int)(y - speed))
						&& !isColliding((int)x, (int)(y - speed))) {
					
					y -= speed;
					setMoving(true);
				}
			} else {
				attakFrames++;
				if (attakFrames > attakSpeed) {
					attakFrames = 0;
					Game.player.takeDamage(this.getDamage());
					System.out.println("Vida:" + Game.player.getLife());
				}
				//estou perto do jogador
			}
		}
	}
	
	public boolean isCollidingWithPlayer() {
		
		Rectangle enemyCurrent = new Rectangle(this.getX(), this.getY(), Constants.TILE_SIZE/2, Constants.TILE_SIZE/2);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), Constants.PLAYER_WIDTH/2, Constants.PLAYER_HEIGHT/2);
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColliding(int xNext, int yNext) {
		
		Rectangle enemyCurrent = new Rectangle(xNext, yNext, Constants.TILE_SIZE/2, Constants.TILE_SIZE/2);
		List<Enemy> enemies = Game.getEnemies();
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			
			if (e == this) {
				continue;
			}
			
			Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), Constants.TILE_SIZE/2, Constants.TILE_SIZE/2);
			
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}
	
	public void setupSprites() {
		
		enemySpriteSheet = new Spritesheet("/movingEnemy.png");
		idleenemySpriteSheet = new Spritesheet("/idleEnemy.png");
		
		rightEnemy = new BufferedImage[4];
		leftEnemy = new BufferedImage[4];
		idlerightEnemy = new BufferedImage[4];
		idleleftEnemy = new BufferedImage[4];
		
		//loop for save sprites
		int xPos_left = 64;
		for (int i = 0; i < 4; i++) {
			int xPos = 16 * i;
			xPos_left -= 16;
			rightEnemy[i] = enemySpriteSheet.getSprite(xPos, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
			leftEnemy[i] = enemySpriteSheet.getSprite(xPos_left, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
			
			idlerightEnemy[i] = idleenemySpriteSheet.getSprite(xPos, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
			idleleftEnemy[i] = idleenemySpriteSheet.getSprite(xPos_left, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
		}
	}
	
	public void updateFrames() {
		frames ++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}
	}

	//Getters and Setters
	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getAttakSpeed() {
		return attakSpeed;
	}

	public void setAttakSpeed(int attakSpeed) {
		this.attakSpeed = attakSpeed;
	}
}















