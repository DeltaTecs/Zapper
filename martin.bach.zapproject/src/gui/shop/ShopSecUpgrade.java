package gui.shop;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;

public abstract class ShopSecUpgrade {

	private static final int SYM_BACK_X = 40;
	private static final int SYM_BACK_Y = 70;
	private static final Color COLOR_HOVER_BACK = new Color(0, 0, 50, 30);
	private static final Rectangle BOUNDS_BACK = new Rectangle(SYM_BACK_X - 5, SYM_BACK_Y - 5, 60, 60);
	private static final Polygon SYMBOL_BACK = new Polygon(new int[] { SYM_BACK_X, 50 + SYM_BACK_X, 50 + SYM_BACK_X },
			new int[] { 25 + SYM_BACK_Y, SYM_BACK_Y, 50 + SYM_BACK_Y }, 3);

	// -- Hovers -------
	private static boolean hoveringBack = false;
	// ---

	protected static void update() {

	}

	protected static void paint(Graphics2D g) {

		// Zurück-Symbol
		g.setColor(Color.BLACK);
		if (hoveringBack) {
			g.setColor(COLOR_HOVER_BACK);
			g.fillRect(BOUNDS_BACK.x, BOUNDS_BACK.y, BOUNDS_BACK.width, BOUNDS_BACK.height);
		}
		g.setColor(ShopMenu.COLOR_SELECT_BORDER);
		g.fillPolygon(SYMBOL_BACK);

	}

	protected static void callClick(int tx, int ty) {

		if (BOUNDS_BACK.contains(tx, ty)) {
			Shop.setDirectory(ShopDirectory.MENU);
			callMove(100000, 10000); // Hovers ausschalten
			return;
		}

	}

	protected static void callMove(int tx, int ty) {

		if (BOUNDS_BACK.contains(tx, ty)) {
			hoveringBack = true;
			return;
		}

		hoveringBack = false;

	}

	protected static void reset() {

	}

}
