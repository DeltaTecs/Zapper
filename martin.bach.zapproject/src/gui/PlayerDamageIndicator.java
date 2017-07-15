package gui;

import java.awt.Graphics2D;

import battle.projectile.Projectile;
import lib.ScheduledList;

public abstract class PlayerDamageIndicator {

	private static ScheduledList<PlayerDamageIndicatingPuls> pulses = new ScheduledList<PlayerDamageIndicatingPuls>();

	public static void update() {
		synchronized (pulses) {
			for (PlayerDamageIndicatingPuls p : pulses) {
				p.update();
				if (p.isFaded())
					pulses.schedRemove(p);
			}
			pulses.update();
		}
	}

	public static void paint(Graphics2D g) {

		synchronized (pulses) {
			for (PlayerDamageIndicatingPuls p : pulses)
				p.paint(g);
		}

	}

	public static void register(Projectile p) {
		pulses.schedAdd(new PlayerDamageIndicatingPuls(p));
	}

}
