package gui;

import java.awt.Graphics2D;

import corecase.MainZap;
import lib.PaintingTask;
import lib.ScheduledList;
import lib.Updateable;

public class HudLightningEffect implements PaintingTask, Updateable {

	private static final int RAND_ADD_LIGHTNING = 1;

	private ScheduledList<HudLightning> lightnings = new ScheduledList<HudLightning>();

	@Override
	public void update() {

		// remove und update
		synchronized (lightnings) {
			for (HudLightning l : lightnings) {
				l.update();
				if (l.faded())
					lightnings.schedRemove(l);
			}
		}
		
		// add
		if (MainZap.rand(RAND_ADD_LIGHTNING) == 0) {
			synchronized (lightnings) {
				lightnings.add(new HudLightning());
			}
		}
		
		// flush
		synchronized (lightnings) {
			lightnings.update();
		}

	}

	@Override
	public void paint(Graphics2D g) {

		synchronized (lightnings) {
			for (HudLightning l : lightnings)
				l.paint(g);
			
		}

	}

}
