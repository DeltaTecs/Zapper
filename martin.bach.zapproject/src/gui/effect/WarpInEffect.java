package gui.effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import battle.enemy.Enemy;
import corecase.MainZap;
import library.SpeedVector;

public class WarpInEffect extends Effect {

	private static final int DURATION = MainZap.getMainLoop().inTicks(200);
	private static final int LIGHTNING_PART_UNTIL = (int) (DURATION * 0.2f);
	private static final int MAX_LIGHTNING_SIZE = 200;
	private static final float WARP_SPEED = 40.0f;

	private float aimX;
	private float aimY;
	private float startX;
	private float startY;
	private SpeedVector velocity;
	private Enemy host;

	/**
	 * Warp-Effekt auf eine Position zu. In zuf‰lliger Richtung
	 * 
	 * @param x
	 * @param y
	 */
	public WarpInEffect(Enemy host) {
		super(DURATION);
		Random r = new Random();
		aimX = host.getPosX();
		aimY = host.getPosY();
		velocity = new SpeedVector(0, 0);
		velocity.aimFor(aimX + (r.nextInt(200) - 100), aimY + (r.nextInt(200) - 100), WARP_SPEED, aimX, aimY);
		host.setPosition(aimX - (DURATION * velocity.getX()), aimY - (DURATION * velocity.getY()));
		host.getVelocity().aimFor(aimX + (r.nextInt(200) - 100), aimY + (r.nextInt(200) - 100), host.getSpeed(), aimX,
				aimY);
		startX = host.getPosX();
		startY = host.getPosY();
		this.host = host;
		host.setRotation(Math.PI - Math.atan2(aimX - host.getPosX(), aimY - host.getPosY()));
	}

	/**
	 * Warp-Effekt auf eine Position zu. In gew‰hlte Richtung
	 * 
	 * @param x
	 * @param y
	 */
	public WarpInEffect(int aimX, int aimY, Enemy host, float scale) {
		super(DURATION);
		this.aimX = host.getPosX();
		this.aimY = host.getPosY();
		velocity = new SpeedVector(0, 0);
		velocity.aimFor(this.aimX, this.aimY, WARP_SPEED, aimX, aimX);
		host.setPosition(this.aimX - (DURATION * velocity.getX()), this.aimY - (DURATION * velocity.getY()));
		host.getVelocity().aimFor(this.aimX, this.aimY, host.getSpeed(), aimX, aimX);
		startX = host.getPosX();
		startY = host.getPosY();
		this.host = host;
		host.setRotation(Math.PI - Math.atan2(aimX - host.getPosX(), aimY - host.getPosY()));
	}

	@Override
	public void paint(Graphics2D g) {

		// Benˆtigt Map-Kontext

		if (getDuration() >= LIGHTNING_PART_UNTIL)
			paintLightning(g);

	}

	@Override
	public void update() {
		host.moveX(velocity.getX());
		host.moveY(velocity.getY());
		super.update();
	}

	private void paintLightning(Graphics2D g) {
		int deltaDur = getDuration() - LIGHTNING_PART_UNTIL;
		int maxDelta = DURATION - LIGHTNING_PART_UNTIL;

		// Weiﬂ. Lineare Alpha abnahme
		g.setColor(new Color(255, 255, 255, (int) ((254 * (float) deltaDur) / maxDelta)));

		int size = (int) (MAX_LIGHTNING_SIZE * (0.5f + (0.5f * Math.random())));

		g.fillOval((int) (startX + 3 * Math.random()) - size, (int) (startY + 3 * Math.random()) - size, 2 * size,
				2 * size);

	}

}
