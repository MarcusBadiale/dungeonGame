package com.dungeonGame.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dungeonGame.main.Game;
import com.dungeonGame.world.Camera;

public class Projectile extends Entity{
	
	private double dx, dy;
	private double speed = 5;
	private int lifetime = 30, currentLife = 0;

	public Projectile(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		x+=dx * speed;
		y+=dy * speed;
		
		if (currentLife == lifetime) {
			Game.projectiles.remove(this);
			return;
		}
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 2, 2);
	}
}
