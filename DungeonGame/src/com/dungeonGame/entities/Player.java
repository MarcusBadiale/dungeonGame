package com.dungeonGame.entities;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dungeonGame.graficos.Spritesheet;
import com.dungeonGame.main.Constants;
import com.dungeonGame.main.Game;
import com.dungeonGame.world.Camera;
import com.dungeonGame.world.World;

public class Player extends Entity {
	
	private double life = 100;
	private double mana = 100;
	private boolean isDamaged = false;
	private boolean holdingStaff = false;
	private boolean castFire = false;
	private boolean teleport = false;
	//Positions of mouse
	public int mx, my;
	
	//Directions variables
	private boolean right, left, up, down, moving;
	private int right_dir = 0, left_dir = 1;
	private int dir = right_dir;
	
	//Speed Variavles
	public double speed = 0.5;
	public double desiredSpeed = 1.5;
	public double maxSpeed = 7;
	private double accelTime = 1000 / 100;
	
	//Sprites
	private Spritesheet playerSpriteSheet;
	private Spritesheet idlePlayerSpriteSheet;
	private Spritesheet damagePlayerSpriteSheet;
	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;
	private int idleframes = 0, idlemaxFrames = 30, idleindex = 0, idlemaxIndex = 1;
	private int manaFrames = 0, maxManaFrames = 30;
	private int damageFrames = 0;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage[] idleRightPlayer;
	private BufferedImage[] idleLeftPlayer;
	
	private BufferedImage[] damagePlayer;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		setupSprites();
	}
	
	public void tick() {
		
		setMoving(false);
		move();
		updateFrames();
		updateCamera();
		checkCollisionWithPotion();
		checkCollisionWithStaff();
		passiveRegenMana();
		attak();
//		teleport();
	}
	
	public void render(Graphics g) {
		
		if (!isDamaged) {
			if (dir == right_dir) {
				if (isMoving()) {
					g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				} else {
					g.drawImage(idleRightPlayer[idleindex], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}
				if(holdingStaff) {
					g.drawImage(Entity.STAFF, (this.getX() - Camera.x) - 2, (this.getY() - Camera.y) - 8, null);
				}
			} else if (dir == left_dir) {
				if (isMoving()) {
					g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				} else {
					g.drawImage(idleLeftPlayer[idleindex], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}
				if(holdingStaff) {
					g.drawImage(Entity.STAFF, (this.getX() - Camera.x) + 10, (this.getY() - Camera.y) - 8, null);
				}
			}
		} else {
			g.drawImage(getDamageSprite(), this.getX() - Camera.x, this.getY() - Camera.y, null);
			if(holdingStaff) {
				if (dir == right_dir) {
					g.drawImage(Entity.STAFF, (this.getX() - Camera.x) - 2, (this.getY() - Camera.y) - 8, null);
				} else if (dir == left_dir) {
					g.drawImage(Entity.STAFF, (this.getX() - Camera.x) + 10, (this.getY() - Camera.y) - 8, null);
				} 
			}
		}
	}
	
	public void move() {
		//This if is for not moving faster in diagonals 
		if (this.isRight() && this.isUp() && World.isFree((int)(x + speed), (int)(y - speed))) {
			
			double halfSpeed = this.getSpeed() / 1.5;			
			x += halfSpeed;
			y -= halfSpeed;
			dir = right_dir;
			accelerate();
			setMoving(true);
		} else if (this.isRight() && this.isDown() && World.isFree((int)(x + speed), (int)(y + speed))) {
			double halfSpeed = this.getSpeed() / 1.5;			
			x += halfSpeed;
			y += halfSpeed;
			dir = right_dir;
			accelerate();
			setMoving(true);
		} else if (this.isLeft() && this.isUp() && World.isFree((int)(x - speed), (int)(y - speed))) {
			double halfSpeed = this.getSpeed() / 1.5;			
			x -= halfSpeed;
			y -= halfSpeed;
			dir = left_dir;
			accelerate();
			setMoving(true);
		} else if (this.isLeft() && this.isDown() && World.isFree((int)(x - speed), (int)(y + speed))) {
			double halfSpeed = this.getSpeed() / 1.5;			
			x -= halfSpeed;
			y += halfSpeed;
			dir = left_dir;
			accelerate();
			setMoving(true);
		} else {
			if (this.isRight() && World.isFree((int)(x + speed), (int)y)) {
				
				x += this.getSpeed();
				dir = right_dir;
				accelerate();
				setMoving(true);
				
			} else if (this.isLeft() && World.isFree((int)(x - speed), (int)y)) {
				
				x -= this.getSpeed();
				dir = left_dir;
				accelerate();
				setMoving(true);
			}
			
			if (this.isUp() && World.isFree((int)x, (int)(y - speed))) {
				y -= this.getSpeed();
				accelerate();
				setMoving(true);
			} else if (this.isDown() && World.isFree((int)x, (int)(y + speed))) {
				y += this.getSpeed();
				accelerate();
				setMoving(true);
			}
		}
	}
	
	public void updateFrames() {
		if (isMoving()) {
			frames ++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		} else {
			speed = 0.1;
			idleframes ++;
			if (idleframes == idlemaxFrames) {
				idleframes = 0;
				idleindex++;
				if (idleindex > idlemaxIndex) {
					idleindex = 0;
				}
			}
		}
		
		if (isDamaged) {
			this.damageFrames++;
			if (this.damageFrames == 5) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
	}
	
	public void updateCamera() {
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void accelerate() {
		if (speed < desiredSpeed || speed > desiredSpeed) {
			speed += (desiredSpeed - speed) / accelTime;
		}
	}
	
	public void attak() {
		if (isCastFire()) {
			System.out.println("Apertou cm o mouse");
			setCastFire(false);
			
			double angle = Math.atan2(my - (this.getY()+8 - Camera.y), mx - (this.getX()+8 - Camera.x));

			System.out.println(angle);
			
			if (isHoldingStaff() && mana > 0) {
				mana -= 5;
				
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				int px = 0;
				int py = -5;
				if (dir == right_dir) {
					px = +2;
				} else {
					px = Constants.PLAYER_WIDTH - 2;
				}
				Projectile fire = new Projectile(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.projectiles.add(fire);
			}
		}
	}
	
	public void teleport() {
		
		if (isTeleport()) {
			teleport = false;
			this.setX(mx);
			this.setY(my);
		}
	}
	
	public void checkCollisionWithPotion() {
		
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			
			if (atual instanceof SmallPotion) {
				
				SmallPotion potion = (SmallPotion) atual;
				
				if (Entity.isColliding(this, potion)) {
					potion.drinkPotion();
					Game.entities.remove(atual);
				}
				
			} else if (atual instanceof BigPotion) {
				
				BigPotion potion = (BigPotion) atual;
				
				if (Entity.isColliding(this, potion)) {
					potion.drinkPotion();
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionWithStaff() {
		
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			
			if (atual instanceof Staff) {
				
				if (Entity.isColliding(this, atual)) {
					Game.entities.remove(atual);
					this.holdingStaff = true;
				}
				
			}
		}
	}
	public void setupSprites() {
		
		playerSpriteSheet = new Spritesheet("/movingPlayer.png");
		idlePlayerSpriteSheet = new Spritesheet("/idlePlayer.png");
		damagePlayerSpriteSheet = new Spritesheet("/damagePlayer.png");
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		idleRightPlayer = new BufferedImage[4];
		idleLeftPlayer = new BufferedImage[4];
		damagePlayer = new BufferedImage[2];
		
		//loop for save sprites
		int xPos_left = 64;
		for (int i = 0; i < 4; i++) {
			int xPos = 16 * i;
			xPos_left -= 16;
			rightPlayer[i] = playerSpriteSheet.getSprite(xPos, 0, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
			leftPlayer[i] = playerSpriteSheet.getSprite(xPos_left, 32, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
			
			idleRightPlayer[i] = idlePlayerSpriteSheet.getSprite(xPos, 0, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
			idleLeftPlayer[i] = idlePlayerSpriteSheet.getSprite(xPos_left, 32, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
		}
		
		//Damage in the right dir
		damagePlayer[0] = damagePlayerSpriteSheet.getSprite(0, 0, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
		//Damage in the left dir
		damagePlayer[1] = damagePlayerSpriteSheet.getSprite(16, 0, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
	}
	
	public void takeDamage(double damage) {
		
		this.setDamaged(true);
		this.life -= damage;
		if (this.life <= 0) {
			this.die();
		}
	}
	
	public void heal(double heal) {
		
		System.out.println(heal);
		System.out.println(life);
		
		if (this.life + heal > Constants.PLAYER_MAX_LIFE) {
			this.life = 100;
		} else {
			this.life += heal;
		}
	}
	
	public void spendMana(double mana) {
		this.mana -= mana;
	}
	
	public void passiveRegenMana() {
		
		if (mana < Constants.PLAYER_MAX_MANA) {
			manaFrames++;
			if (manaFrames > maxManaFrames) {
				manaFrames = 0;
				mana += 2;
			}
		}
	}
	
	public void regenMana(double mana) {

		if (this.mana + mana > Constants.PLAYER_MAX_MANA) {
			this.mana = Constants.PLAYER_MAX_MANA;
		} else {
			this.mana += mana;
		}
	}
	
	public BufferedImage getDamageSprite() {
		if (dir == right_dir) {
			return damagePlayer[0];
		} else if (dir == left_dir) {
			return damagePlayer[1];
		} else {
			return damagePlayer[0];
		}
	}

	public void die() {
		Game.initObjects();
	}
	
	
	//Getters and Setters

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


	public double getSpeed() {
		return speed;
	}


	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public boolean isDown() {
		return down;
	}


	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public double getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public double getMana() {
		return mana;
	}

	public void setMana(double mana) {
		this.mana = mana;
	}

	public boolean isDamaged() {
		return isDamaged;
	}

	public void setDamaged(boolean isDamage) {
		this.isDamaged = isDamage;
	}

	public boolean isHoldingStaff() {
		return holdingStaff;
	}

	public void setHoldingStaff(boolean holdingStaff) {
		this.holdingStaff = holdingStaff;
	}

	public boolean isCastFire() {
		return castFire;
	}

	public void setCastFire(boolean castFire) {
		this.castFire = castFire;
	}

	public boolean isTeleport() {
		return teleport;
	}

	public void setTeleport(boolean teleport) {
		this.teleport = teleport;
	}

}
