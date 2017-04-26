package gui.shop;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import corecase.MainZap;
import gui.Frame;
import gui.Hud;
import gui.screens.pause.PauseScreen;
import library.ClickListener;
import library.ClickableObject;
import library.MotionListener;

public abstract class Shop {

	public static final int X = 30;
	public static final int Y = 60;
	public static final int HEIGHT_CENTER = (3 * Frame.SIZE / 7);
	public static final int HEIGHT_INFOBAR = (2 * Frame.SIZE / 7);
	private static final int BORDERWIDTH = 8;
	public static final Rectangle BOUNDS = new Rectangle(X, Y, Frame.SIZE - 2 * X, Frame.SIZE - 2 * Y);
	public static final Rectangle BOUNDS_CENTER_TOTAL = new Rectangle(X, HEIGHT_INFOBAR, BOUNDS.width, HEIGHT_CENTER);
	private static final Color COLOR_DARK_BACKGROUND = new Color(0, 0, 0, 70);
	private static final Color COLOR_BRIGHT_BACKGROUND = new Color(255, 255, 255, 240);

	private static boolean available = false;
	private static boolean open = false;
	private static ShopDirectory directory = ShopDirectory.MENU;

	public static void setUp() {

		ClickableObject clickLayer = new ClickableObject(new Rectangle(0, 0, Frame.SIZE, Frame.SIZE));
		clickLayer.setVisible(true);
		clickLayer.addClickListener(GENERELL_CLICKLISTENER);
		clickLayer.addMotionListener(GENERELL_MOTIONLISTENER);
		MainZap.getFrame().addClickable(clickLayer);
	}

	public static void update() {
		if (!open) // Geschlossen
			return;

		switch (directory) {
		case MENU:
			ShopMenu.update();
			break;
		case SHIPS:
			ShopSecBuy.update();
			break;
		case UPGRADES:
			ShopSecUpgrade.update();
			break;
		default: // Gibts nicht
			break;

		}

	}

	public static void paint(Graphics2D g) {
		if (!open) // Geschlossen
			return;

		// Hintergrund
		paintBackground(g);

		switch (directory) {
		case MENU:
			ShopMenu.paint(g);
			break;
		case SHIPS:
			ShopSecBuy.paint(g);
			break;
		case UPGRADES:
			ShopSecUpgrade.paint(g);
			break;
		default: // Gibts nicht
			break;

		}

	}

	private static final ClickListener GENERELL_CLICKLISTENER = new ClickListener() {

		@Override
		public void release(int dx, int dy) {

			if (Hud.clickInShopButton(dx, dy))
				return; // Nicht doppelt klicken

			switch (directory) {
			case MENU:
				ShopMenu.callClick(dx, dy);
				break;
			case SHIPS:
				ShopSecBuy.callClick(dx, dy);
				break;
			case UPGRADES:
				ShopSecUpgrade.callClick(dx, dy);
				break;
			default: // Gibts nicht
				break;
			}
		}

		@Override
		public void press(int dx, int dy) {
			if (directory == ShopDirectory.SHIPS) {
				ShopSecBuy.callPress(dx, dy);
			}
		}
	};

	private static final MotionListener GENERELL_MOTIONLISTENER = new MotionListener() {

		@Override
		public void move(int dx, int dy) {
			switch (directory) {
			case MENU:
				ShopMenu.callMove(dx, dy);
				break;
			case SHIPS:
				ShopSecBuy.callMove(dx, dy);
				break;
			case UPGRADES:
				ShopSecUpgrade.callMove(dx, dy);
				break;
			default: // Gibts nicht
				break;
			}
		}

		@Override
		public void drag(int dx, int dy) {
			switch (directory) {
			case MENU:
				ShopMenu.callMove(dx, dy);
				break;
			case SHIPS:
				ShopSecBuy.callMove(dx, dy);
				break;
			case UPGRADES:
				ShopSecUpgrade.callMove(dx, dy);
				break;
			default: // Gibts nicht
				break;
			}
		}
	};

	public static void open() {
		if (open || PauseScreen.isOpen() || !MainZap.getPlayer().isAlive())
			return; // Bereits offen oder Spieler tot
		open = true;
		directory = ShopDirectory.MENU;
		// &&& Öffnungsprozess
	}

	public static void close() {
		if (!open)
			return; // Bereits geschlossen
		open = false;
		directory = ShopDirectory.MENU;
	}

	public static void reset() {
		ShopSecUpgrade.reset();
	}

	private static void paintBackground(Graphics2D g) {

		int x = X - BORDERWIDTH;
		int y = Y - BORDERWIDTH;

		g.setColor(COLOR_DARK_BACKGROUND);
		g.fillRect(x, y, Frame.SIZE - 2 * x, Frame.SIZE - 2 * y);
		g.setColor(COLOR_BRIGHT_BACKGROUND);
		g.fillRect(X, Y, BOUNDS.width, BOUNDS.height);

	}

	public static void setDirectory(ShopDirectory d) {
		directory = d;
		if (d == ShopDirectory.SHIPS)
			ShopSecBuy.open();
	}

	public static boolean isOpen() {
		return open;
	}

	public static boolean isAvailable() {
		return available;
	}

	public static void setAvailable(boolean available) {
		Shop.available = available;
	}

}
