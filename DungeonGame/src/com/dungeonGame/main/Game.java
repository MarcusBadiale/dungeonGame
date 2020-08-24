package com.dungeonGame.main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.dungeonGame.entities.Enemy;
import com.dungeonGame.entities.Entity;
import com.dungeonGame.entities.Player;
import com.dungeonGame.entities.PotionType;
import com.dungeonGame.entities.Projectile;
import com.dungeonGame.graficos.Spritesheet;
import com.dungeonGame.graficos.UI;
import com.dungeonGame.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	private BufferedImage image;
	
	public static final int WIDTH = 200;
	public static final int HEIGHT = 120;
	private final int SCALE = 4; 
	
	public static List<Entity> entities;
	public static List<Projectile> projectiles;
	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	public static UI lifeUI;
	public static UI manaUI;
	
	public Game() {
		addKeyListener(this);
		addMouseListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setupFrame();
		initObjects();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	}
	
	public static void initObjects() {
		entities = new ArrayList<Entity>();
		projectiles = new ArrayList<Projectile>();
		spritesheet = new Spritesheet("/spritesheet.png");
		BufferedImage playerSprite = spritesheet.getSprite(0, 32, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
		player = new Player(0, 0, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT, playerSprite);
		world = new World("/mapa.png");
		lifeUI = new UI(PotionType.LIFE);
		manaUI = new UI(PotionType.MANA);
		entities.add(player);
	}

	private void setupFrame() {

		frame = new JFrame();
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		isRunning = true;
	}
	
	public synchronized void stop() {
		
		isRunning = false;
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		
		for(int i = 0; i < projectiles.size(); i++) {
			Entity e = projectiles.get(i);
			e.tick();
		}
	}
	
	public void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(49, 49, 49));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Renderização do jogo
		world.render(g);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < projectiles.size(); i++) {
			Entity e = projectiles.get(i);
			e.render(g);
		}
		
		lifeUI.render(g);
		manaUI.render(g);
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		bs.show();
	}
	
	@Override
	public void run() {
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		while (isRunning) {
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer = System.currentTimeMillis();
			}
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_D) {
			player.setRight(true);
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			player.setLeft(true);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_W) {
			player.setUp(true);
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			player.setDown(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_D) {
			player.setRight(false);
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			player.setLeft(false);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_W) {
			player.setUp(false);
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			player.setDown(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static List<Enemy> getEnemies() {
		
		List<Enemy> enemies = new ArrayList<>();
		
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			
			if (e instanceof Enemy) {
				enemies.add((Enemy) e);
			}
		}
		
		return enemies;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			player.setCastFire(true);
			player.mx = (e.getX()/3);
			player.my = (e.getY()/3);
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			player.setTeleport(true);
			player.mx = (e.getX()/3);
			player.my = (e.getY()/3);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}

