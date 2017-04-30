package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import corecase.MainZap;
import lib.PaintingTask;

public class PlayerAmmoIndicator implements PaintingTask {

	private static final int ROUND_DEEPTH = 7;
	private static final float VALUE_DELTA = 0.00071f;
	private static final int BAR_HEIGHT = 150;
	private static final int BAR_WIDTH = 15;
	private static final int BAR_DISTANCE = 4;
	private static final int BAR_POS = 6;
	private static final Color COLOR_BARS = new Color(229, 71, 110, 200);


	private byte usingBar = 3; // max
	private float barValue = 1.0f; // max

	@Override
	public void paint(Graphics2D g) {

		g.setColor(COLOR_BARS);

		if (MainZap.roundCorners) {

			switch (usingBar) {
			case 3:
				g.fillRoundRect(BAR_POS, BAR_POS, BAR_WIDTH, BAR_HEIGHT, ROUND_DEEPTH, ROUND_DEEPTH);
				g.fillRoundRect(BAR_POS + BAR_WIDTH + BAR_DISTANCE, BAR_POS, BAR_WIDTH, BAR_HEIGHT, ROUND_DEEPTH,
						ROUND_DEEPTH);
				g.fillRoundRect(BAR_POS + BAR_WIDTH + BAR_DISTANCE + BAR_WIDTH + BAR_DISTANCE, BAR_POS, BAR_WIDTH,
						(int) (BAR_HEIGHT * barValue), ROUND_DEEPTH, ROUND_DEEPTH);
				break;
			case 2:
				g.fillRoundRect(BAR_POS, BAR_POS, BAR_WIDTH, BAR_HEIGHT, ROUND_DEEPTH, ROUND_DEEPTH);
				g.fillRoundRect(BAR_POS + BAR_WIDTH + BAR_DISTANCE, BAR_POS, BAR_WIDTH, (int) (BAR_HEIGHT * barValue),
						ROUND_DEEPTH, ROUND_DEEPTH);
				break;
			case 1:
				g.fillRoundRect(BAR_POS, BAR_POS, BAR_WIDTH, (int) (BAR_HEIGHT * barValue), ROUND_DEEPTH, ROUND_DEEPTH);
				break;
			}

			return;
		}

		switch (usingBar) {
		case 3:
			g.fillRect(BAR_POS, BAR_POS, BAR_WIDTH, BAR_HEIGHT);
			g.fillRect(BAR_POS + BAR_WIDTH + BAR_DISTANCE, BAR_POS, BAR_WIDTH, BAR_HEIGHT);
			g.fillRect(BAR_POS + BAR_WIDTH + BAR_DISTANCE + BAR_WIDTH + BAR_DISTANCE, BAR_POS, BAR_WIDTH,
					(int) (BAR_HEIGHT * barValue));
			break;
		case 2:
			g.fillRect(BAR_POS, BAR_POS, BAR_WIDTH, BAR_HEIGHT);
			g.fillRect(BAR_POS + BAR_WIDTH + BAR_DISTANCE, BAR_POS, BAR_WIDTH, (int) (BAR_HEIGHT * barValue));
			break;
		case 1:
			g.fillRect(BAR_POS, BAR_POS, BAR_WIDTH, (int) (BAR_HEIGHT * barValue));
			break;
		}

	}

	public void remove(float wastMulti) {
		
		// Leer genoggert
		if (usingBar == 0)
			return; // Nix da. Bereits akzeptiert

		// Was da zum wegnehmen
		barValue -= VALUE_DELTA * wastMulti;

		if (barValue > 0) 
			return; // noch was da

		// Bar aufgebraucht
		usingBar--;
		barValue = 1.0f;
	}

	public void add(boolean large) {

		if (large) { // Komplett füllen
			usingBar = 3;
			barValue = 1.0f;
			return;
		}

		usingBar++;
		if (usingBar > 3) {
			usingBar = 3;
			barValue = 1.0f; // maximal möglich
		}
	}

	public boolean ammoRemaining() {
		return usingBar != 0; // Sollange nich auf 0 noggern: ja
	}

}
