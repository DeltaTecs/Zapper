package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import corecase.MainZap;

public class BloodSplash {

	private static final int MIN_DURATION = 40;
	private static final int MAX_DURATION = 170;
	private static final float DURATION_REMOVAL = 0.9f;
	private static final int SIZE_MIN = 5;
	private static final int SIZE_MAX = 60;

	private int x, y, size;
	private float duration;

	public BloodSplash() {
		size = MainZap.rand(SIZE_MAX - SIZE_MIN) + SIZE_MIN + 1;
		x = MainZap.rand(Frame.SIZE - size);
		y = MainZap.rand(Frame.SIZE - size);
		duration = MainZap.rand(MAX_DURATION - MIN_DURATION) + MIN_DURATION;
	}

	public void paint(Graphics2D g) {
		if (duration < 0)
			return; // kann wegen multi-thread passieren
		g.setColor(new Color(214, 14, 0, (int) duration));
		g.fillRect(x, y, size, size);
	}

	public void update() {
		if (duration > 0) {
			duration -= DURATION_REMOVAL;
			if (duration < 0)
				duration = 0;
		}
	}

	public boolean faded() {
		return duration < 5;
	}

}
