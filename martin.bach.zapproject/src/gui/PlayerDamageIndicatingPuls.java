package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import battle.projectile.Projectile;
import corecase.MainZap;
import lib.PaintingTask;
import lib.SpeedVector;
import lib.Updateable;

public class PlayerDamageIndicatingPuls implements PaintingTask, Updateable {

	private static final int MAX_DURATION = MainZap.inTicks(3000);
	private static final int MIDDISTANCE = 280;
	private static final float DURATION_FAC = 2.0f;
	private static final int MAX_ALPHA = 100;
	private static final int BACKPOS_DELTA_FAC = 11;

	private int duration;
	private int[] locsX = new int[4];
	private int[] locsY = new int[4];

	public PlayerDamageIndicatingPuls(Projectile p) {
		// Stärke ist relativ zur Auswirkung des Schadens auf die Spieler-HP
		duration = (int) (MAX_DURATION * DURATION_FAC
				* (0.1f + ((float) p.getDamage() / MainZap.getPlayer().getMaxHp())));
		if (duration > MAX_DURATION) // clip
			duration = MAX_DURATION;
		SpeedVector midToRing = new SpeedVector(p.getVelocity().getX(), p.getVelocity().getY());
		midToRing.invert();
		midToRing.scaleToLength(MIDDISTANCE);

		int midX = (int) (325 + midToRing.getX()); // punkt auf dem kreis (middistance)
		int midY = (int) (325 + midToRing.getY());
		int size = p.getRadius() * 2;

		// VVV Rechtwinklige Striche
		// alpha = arcTan(s / 2l)
		// dx = sin(alpha) * (s / 2)
		// dy = cos(alpha) * (s / 2)
		double alpha = Math.atan(-midToRing.getY() / midToRing.getX());
		int dx = (int) (size * Math.sin(alpha) / 2.0f);
		int dy = (int) (size * Math.cos(alpha) / 2.0f);

		
		// Positionen gegen den Uhrzeigersinn
		locsX[0] = midX - dx;
		locsY[0] = midY - dy;
		locsX[1] = midX + dx;
		locsY[1] = midY + dy;
		locsX[2] = midX + (int) midToRing.getX() + dx * BACKPOS_DELTA_FAC;
		locsY[2] = midY + (int) midToRing.getY() + dy * BACKPOS_DELTA_FAC;
		locsX[3] = midX + (int) midToRing.getX() - dx * BACKPOS_DELTA_FAC;
		locsY[3] = midY + (int) midToRing.getY() - dy * BACKPOS_DELTA_FAC;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(new Color(220, 0, 0, (int) (MAX_ALPHA * (float) duration / MAX_DURATION)));
		g.fillPolygon(locsX, locsY, 4);
	}

	@Override
	public void update() {
		if (duration != 0)
			duration--;
	}

	public boolean isFaded() {
		return duration == 0;
	}

}
