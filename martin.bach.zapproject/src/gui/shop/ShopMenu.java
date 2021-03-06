package gui.shop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import gui.Frame;

public abstract class ShopMenu {

	public static final Color COLOR_SELECT_BORDER = new Color(0, 0, 0, 200);
	private static final Color COLOR_HOVER = new Color(0, 0, 0, 30);
	private static final Stroke STROKE_BORDER_SELECT = new BasicStroke(5.0f);
	private static final Rectangle BOUNDS_BUY = new Rectangle(Shop.X, Shop.Y, Shop.BOUNDS.width / 2,
			Shop.BOUNDS.height);
	private static final Rectangle BOUNDS_UPGRADE = new Rectangle(Shop.X + (Shop.BOUNDS.width / 2), Shop.Y,
			Shop.BOUNDS.width / 2, Shop.BOUNDS.height);

	private static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 63);
	private static final String TEXT_BUY = "Buy New";
	private static final String TEXT_UPGRADE = "Upgrade";

	// Maus �ber Buy, Upgrade?
	private static boolean[] hovering = new boolean[] { false, false };

	public static void callClick(int totX, int totY) {
		if (BOUNDS_BUY.contains(totX, totY)) {
			Shop.setDirectory(ShopDirectory.SHIPS);
			callMove(100000, 10000); // Hovers ausschalten
		} else if (BOUNDS_UPGRADE.contains(totX, totY)) {
			Shop.setDirectory(ShopDirectory.UPGRADES);
			callMove(100000, 10000); // Hovers ausschalten
		}
	}

	public static void callMove(int totX, int totY) {
		if (BOUNDS_BUY.contains(totX, totY)) {
			hovering[0] = true;
			hovering[1] = false;
			return;
		}
		if (BOUNDS_UPGRADE.contains(totX, totY)) {
			hovering[0] = false;
			hovering[1] = true;
			return;
		}
		hovering[0] = false;
		hovering[1] = false;
		return;
	}

	public static void paint(Graphics2D g) {

		g.setColor(COLOR_HOVER);
		if (hovering[0]) {
			g.fillRect(BOUNDS_BUY.x, BOUNDS_BUY.y, BOUNDS_BUY.width, BOUNDS_BUY.height);
		} else if (hovering[1]) {
			g.fillRect(BOUNDS_UPGRADE.x, BOUNDS_UPGRADE.y, BOUNDS_UPGRADE.width, BOUNDS_UPGRADE.height);
		}

		g.setColor(COLOR_SELECT_BORDER);
		g.setStroke(STROKE_BORDER_SELECT);
		g.drawLine(Frame.SIZE / 2, Shop.Y + 2, Frame.SIZE / 2, Shop.Y + Shop.BOUNDS.height - 3);

		g.setColor(COLOR_SELECT_BORDER);
		g.setFont(FONT_BUTTON);
		g.drawString(TEXT_BUY, 75, 355 + 75);
		g.drawString(TEXT_UPGRADE, 385 + 75, 355 + 75);
	}

	public static void update() {

	}

}
