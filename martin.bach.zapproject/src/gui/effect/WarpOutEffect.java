package gui.effect;

import java.awt.Graphics2D;
import java.util.Random;

import battle.enemy.Enemy;
import corecase.MainZap;

public class WarpOutEffect extends Effect {

	private static final int MAX_DURATION = MainZap.getMainLoop().inTicks(1000);
	private static final float WARP_SPEED_FAC = 1.04f;

	private float aimX;
	private float aimY;
	private Enemy host;
	private boolean moving = false;
	private int moveRand = 1;
	private int waitingTime = 0;

	/**
	 * Warp-Effekt auf eine Position zu. In zufälliger Richtung
	 * 
	 * @param x
	 * @param y
	 */
	public WarpOutEffect(Enemy host, int moveRand) {
		super(MAX_DURATION);
		this.moveRand = moveRand;
		Random r = new Random();
		aimX = host.getPosX() + r.nextInt(200) - 100;
		aimY = host.getPosY() + r.nextInt(200) - 100;
		host.getVelocity().aimFor(host.getPosY(), host.getPosY(), host.getSpeed(), aimX, aimY);
		this.host = host;
		host.setRotation(Math.PI - Math.atan2(aimX - host.getPosX(), aimY - host.getPosY()));
	}

	/**
	 * Warp-Effekt auf eine Position zu. In gewählte Richtung
	 * 
	 * @param x
	 * @param y
	 */
	public WarpOutEffect(int aimX, int aimY, Enemy host, int moveRand) {
		super(MAX_DURATION);
		this.moveRand = moveRand;
		this.aimX = aimX;
		this.aimY = aimY;
		host.getVelocity().aimFor(host.getPosX(), host.getPosY(), host.getSpeed(), aimX, aimY);
		this.host = host;
		host.setRotation(Math.PI - Math.atan2(aimX - host.getPosX(), aimY - host.getPosY()));
	}

	@Override
	public void paint(Graphics2D g) {

	}

	@Override
	public void update() {

		if (waitingTime > 0) {
			waitingTime--;
			return;
		}

		if (!moving) {
			if (MainZap.rand(moveRand) == 0)
				moving = true;
			else
				return;
		}

		host.getVelocity().multiply(WARP_SPEED_FAC);
		host.moveX(host.getVelocity().getX());
		host.moveY(host.getVelocity().getY());
		super.update();
		if (getDuration() == 0)
			host.unRegister(); // Raus aus der Sicht
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
		setDuration(getDuration() + waitingTime); 
	}

}
