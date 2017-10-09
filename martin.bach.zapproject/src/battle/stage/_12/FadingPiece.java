package battle.stage._12;

import java.awt.Color;
import java.awt.Graphics2D;

import lib.SpeedVector;

public class FadingPiece {

	private float posX, posY;
	private int size;
	private SpeedVector velocity;
	private float fadingSpeed;
	private float alpha;

	public FadingPiece(int size, SpeedVector velocity, float fadingSpeed, float alpha, float posX, float posY) {
		super();
		this.size = size;
		this.velocity = velocity;
		this.fadingSpeed = fadingSpeed;
		this.alpha = alpha;
		this.posX = posX;
		this.posY = posY;
	}

	public void update() {

		// Alpha-Update
		if (alpha > 0)
			alpha -= fadingSpeed;
		
		if (alpha < 0)
			alpha = 0;

		// Positions-Update
		posX += velocity.getX();
		posY += velocity.getY();
	}

	public void paint(Graphics2D g) {
		// braucht 0/0 Kontext

		g.setColor(new Color(0, 0, 0, alpha));
		g.fillRect((int) posX, (int) posY, size, size);
	}

	public boolean isFaded() {
		return alpha <= 0.002f;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public SpeedVector getVelocity() {
		return velocity;
	}

	public void setVelocity(SpeedVector velocity) {
		this.velocity = velocity;
	}

	public float getFadingSpeed() {
		return fadingSpeed;
	}

	public void setFadingSpeed(float fadingSpeed) {
		this.fadingSpeed = fadingSpeed;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

}
