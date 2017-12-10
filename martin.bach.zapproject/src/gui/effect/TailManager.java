package gui.effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import battle.enemy.Enemy;
import corecase.MainZap;

public class TailManager {

	private float size;
	private float sizeRemoval;
	private boolean square;
	private int distance;
	private int distanceCounter = 0;
	private int[] posX, posY;

	public TailManager(float size, int distance, float sizeRemoval, int[] posX, int[] posY, boolean square) {
		super();
		this.size = size;
		this.distance = distance;
		this.posX = posX;
		this.posY = posY;
		this.sizeRemoval = sizeRemoval;
		this.square = square;
	}

	public void paintDebug(Graphics2D g, Enemy e) {

		g.setColor(Color.PINK);

		for (int i = 0; i != posX.length; i++) {

			Point2D res = new Point();
			AffineTransform.getRotateInstance(e.getRotation(), 0, 0).transform(new Point(posX[i], posY[i]), res);

			g.fillRect((int) (res.getX() + e.getPosX()) - 2, (int) (res.getY() + e.getPosY()) - 2, 4, 4);

		}
	}

	public void paintDebug(Graphics2D g) {

		g.setColor(Color.PINK);

		for (int i = 0; i != posX.length; i++) {

			Point2D res = new Point();
			AffineTransform.getRotateInstance(MainZap.getPlayer().getRotation(), 0, 0)
					.transform(new Point(posX[i], posY[i]), res);

			g.fillRect((int) (res.getX()) - 2, (int) (res.getY()) - 2, 4, 4);

		}
	}

	public void update(Enemy e) {

		if (e.getVelocity().getSpeed() < 0.1f || !e.getAiProtocol().isMoving()) // zu Lahm
			return;

		if (distanceCounter != 0) // Nur alle 'distance' einen Ablassen
			distanceCounter--;
		else {
			distanceCounter = distance;
			for (int i = 0; i != posX.length; i++) {

				Point2D res = new Point();
				AffineTransform.getRotateInstance(e.getRotation(), 0, 0).transform(new Point(posX[i], posY[i]), res);

				new TailParticle((int) (e.getPosX() + res.getX()), (int) (e.getPosY() + res.getY()), size, sizeRemoval,
						square).register();

			}
		}
	}

	public void update() {

		if (MainZap.getPlayer().getVelocity().getSpeed() < 0.08f) // zu Lahm
			return;

		if (distanceCounter != 0) // Nur alle 'distance' einen Ablassen
			distanceCounter--;
		else {
			distanceCounter = distance;
			for (int i = 0; i != posX.length; i++) {

				Point2D res = new Point();
				AffineTransform.getRotateInstance(MainZap.getPlayer().getRotation(), 0, 0)
						.transform(new Point(posX[i], posY[i]), res);

				new TailParticle((int) (MainZap.getPlayer().getPosX() + res.getX()),
						(int) (MainZap.getPlayer().getPosY() + res.getY()), size, sizeRemoval, square).register();

			}
		}
	}

	public float getSize() {
		return size;
	}

	public int getDistance() {
		return distance;
	}

	public int[] getPosX() {
		return posX;
	}

	public int[] getPosY() {
		return posY;
	}

	public float getSizeRemoval() {
		return sizeRemoval;
	}

	public boolean isSquare() {
		return square;
	}

	public int getDistanceCounter() {
		return distanceCounter;
	}
	
	

}
