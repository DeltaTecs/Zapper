package gui;

import java.awt.Graphics2D;

import corecase.MainZap;
import lib.ScheduledList;

public abstract class BloodyScreen {

	private static final int RAND_ADD_SPLASH = 15;
	public static final float HP_TRIGGER_BORDER = 0.2f;

	private static ScheduledList<BloodSplash> splashes = new ScheduledList<BloodSplash>();

	public static void paint(Graphics2D g) {

		if (triggered())
			synchronized (splashes) {
				splashes.update();
				for (BloodSplash b : splashes)
					b.paint(g);
			}

	}

	public static void update() {
		for (BloodSplash b : splashes) {
			b.update();
			if (b.faded())
				splashes.schedRemove(b);
		}

		if (triggered())
			if (MainZap.rand(RAND_ADD_SPLASH) == 0)
				splashes.schedAdd(new BloodSplash());
	}

	private static boolean triggered() {
		return MainZap.getPlayer().getHp() <= MainZap.getPlayer().getMaxHp() * HP_TRIGGER_BORDER;
	}

}
