package gui.extention;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import battle.Shockable;
import battle.projectile.Projectile;
import battle.projectile.ProjectileDesign;
import collision.Collideable;
import corecase.MainZap;

public class Shocking {

	private static final int MAX_DURATION = MainZap.inTicks(400); // 0,4 Sek
	private static final int RANGE = 300;
	private static final float START_RADIAN = 20.0f;
	private static final float MAX_RADIAN = RANGE;
	private static final float DURATION_START_FADE_PERCENTAGE = 0.2f;
	private static final Color COLOR_INCIRCLE = new Color(222, 252, 244, 217);
	private static final Color COLOR_OUTCIRCLE = new Color(88, 239, 199);
	private static final Stroke STROKE_INCIRCLE = new BasicStroke(101);
	private static final Stroke STROKE_OUTCIRCLE = new BasicStroke(5);
	private static Projectile TAG_PROJECTILE = new Projectile(0, new ProjectileDesign(0, false, Color.BLACK), 0);

	private static int duration = MAX_DURATION;
	private static boolean active = false;
	private static int centerX;
	private static int centerY;
	private static float radian = START_RADIAN;
	private static float alphaFac = 1.0f;
	private static ArrayList<Shockable> shocked = new ArrayList<Shockable>();

	public static void update() {

		if (!active)
			return;

		duration--;
		if (duration <= 0) {
			duration = 0;
			active = false;
			return;
		}

		if (duration <= (MAX_DURATION * DURATION_START_FADE_PERCENTAGE))
			alphaFac = (float) duration / (MAX_DURATION * DURATION_START_FADE_PERCENTAGE);

		radian = ((MAX_RADIAN - START_RADIAN) * (float) (MAX_DURATION - duration) / MAX_DURATION) + START_RADIAN;

		// Shocken
		ArrayList<Collideable> sourround = MainZap.getGrid().getTotalSurrounding(centerX, centerY, (int) radian);
		for (Collideable c : sourround) {

			if (!(c instanceof Shockable))
				continue; // Nicht shockbares

			if (shocked.contains(c)) // shoen geshockt?
				continue;

			((Shockable) c).shock(); // Shocken
			shocked.add((Shockable) c); // Makieren
		}

	}

	// Im idealfall von Map gezeichnet, da 0/0 Kontext benötigt
	public static void paint(Graphics2D g) {

		if (!active)
			return;

		Composite storeComp = g.getComposite();
		if (MainZap.fancyGraphics)
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFac));
		g.setColor(COLOR_INCIRCLE);
		g.setStroke(STROKE_INCIRCLE);
		g.drawOval(centerX - (int) radian, centerY - (int) radian, (int) (2 * radian), (int) (2 * radian));
		g.setColor(COLOR_OUTCIRCLE);
		g.setStroke(STROKE_OUTCIRCLE);
		g.drawOval(centerX - (int) radian - 45, centerY - (int) radian - 45, (int) (2 * radian) + 90,
				(int) (2 * radian) + 90);

		g.setComposite(storeComp);
	}

	public static void activate() {

		if (active)
			return;

		TAG_PROJECTILE.setSender(MainZap.getPlayer());
		centerX = MainZap.getPlayer().getLocX();
		centerY = MainZap.getPlayer().getLocY();
		duration = MAX_DURATION;
		alphaFac = 1.0f;
		shocked.clear();
		active = true;
	}

	public static Projectile getTagProjectile() {
		return TAG_PROJECTILE;
	}

}
