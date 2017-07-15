package battle.collect;

import java.awt.Color;
import java.awt.Graphics2D;

import battle.CombatObject;
import battle.projectile.Projectile;
import corecase.MainZap;
import lib.PaintingTask;
import lib.Updateable;

public class SimpleShieldAbsorbtionEffect implements PaintingTask, Updateable {

	private static final float VISIBLE_TIME_PER_DMG = 1f;
	private static final int[] RGB = { 56, 164, 181 };

	int x; // pos (relativ)
	int y; // pos (relativ)
	int r; // radius
	int duration;
	int maxDuration;

	public SimpleShieldAbsorbtionEffect(Projectile p, CombatObject c, int maxRange) {
		this.x = p.getLocX() - c.getLocX();
		this.y = p.getLocY() - c.getLocY();
		r = p.getRadius();
		duration = (int) (p.getDamage() * VISIBLE_TIME_PER_DMG);
		maxDuration = duration;
		// Prüfen ob Position außerhalb des Radius
		float overleap = (float) Math.sqrt((x * x) + (y * y)) + r - maxRange;

		if (overleap > 0)
			r -= overleap;

		// VVV Auskommentiert, da nicht sinnvoll, wenn das Projektil nicht
		// schnurrgerade auf das Zentrum zufliegt. VVV Benötigt das overleap.
		// if (overleap > 0) {
		// // Bewegungs-Vektor des Projektils normalisieren und mit
		// // Overleap-Verschiebung multiplizieren
		// // vx = (coreVX / speed) * overleap
		// float vx = p.getVelocity().getX() * overleap
		// / (float) Math.sqrt((p.getVelocity().getX() * p.getVelocity().getX())
		// + (p.getVelocity().getY() * p.getVelocity().getY()));
		// float vy = p.getVelocity().getY() * overleap
		// / (float) Math.sqrt((p.getVelocity().getX() * p.getVelocity().getX())
		// + (p.getVelocity().getY() * p.getVelocity().getY()));
		// // /\ wird der Compiler schon optimieren
		// x += (int) vx;
		// y += (int) vy;
		// }
	}

	@Override
	public void update() {
		if (duration > 0)
			duration--;
	}

	@Override
	public void paint(Graphics2D g) {

		if (MainZap.fancyGraphics) {
			g.setColor(new Color(RGB[0], RGB[1], RGB[2], (int) (255.0f * ((float) duration / (float) maxDuration))));
			g.fillOval(x - r, y - r, 2 * r, 2 * r);
		} else {
			g.setColor(new Color(RGB[0], RGB[1], RGB[2]));
			g.fillRect(x - r, y - r, 2 * r, 2 * r);
		}
	}

	public boolean isFinished() {
		return duration <= 0;
	}

}
