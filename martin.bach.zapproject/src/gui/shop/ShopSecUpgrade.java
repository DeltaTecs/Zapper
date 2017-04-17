package gui.shop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import corecase.MainZap;
import io.TextureBuffer;

public abstract class ShopSecUpgrade {

	private static final BufferedImage IMG_CRYSTAL = TextureBuffer.get(TextureBuffer.NAME_CRYSTAL);
	private static final int STAT_POS_X = 40;
	private static final int SYM_BACK_X = 40;
	private static final int SYM_BACK_Y = 70;
	private static final int STAT_POS_Y = 140;
	private static final int STAT_SPACE_Y = 10;
	private static final Color COLOR_HOVER_BACK = new Color(0, 0, 50, 30);
	private static final Color COLOR_GREEN = new Color(30, 205, 30);
	private static final Font FONT_BALANCE = new Font("Arial", Font.BOLD, 35);
	private static final Font FONT_VALUE = new Font("Arial", 0, 20);
	private static final Font FONT_LVL = new Font("Arial", Font.BOLD, 25);
	private static final Font FONT_PERCENTAGE = new Font("Arial", Font.BOLD, 22);
	private static final Font FONT_PRICE = new Font("Arial", Font.BOLD, 26);
	private static final Font FONT_UPGRADE = new Font("Arial", Font.BOLD, 33);
	private static final Stroke STROKE_UPGRADE = new BasicStroke(3);
	private static final Rectangle BOUNDS_BACK = new Rectangle(SYM_BACK_X - 5, SYM_BACK_Y - 5, 60, 60);
	private static final Polygon SYMBOL_BACK = new Polygon(new int[] { SYM_BACK_X, 50 + SYM_BACK_X, 50 + SYM_BACK_X },
			new int[] { 25 + SYM_BACK_Y, SYM_BACK_Y, 50 + SYM_BACK_Y }, 3);
	private static final String TEXT_UPGRADE = "BUY";

	// -- Hovers -------
	private static boolean hoveringBack = false;
	// ---

	// -- Mechanics -----
	// Health, Damage, Ammo-Efficiency, Proj.Speed
	private static byte[] activeUpgrades = new byte[] { 0, 0, 0, 0 };
	private static int[] priceTable = new int[] { 100, 200, 400, 600, 1300};
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

		// Gesamt Geld
		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}
		g.drawImage(IMG_CRYSTAL, 150, 78, (int) (IMG_CRYSTAL.getWidth() * 3f), (int) (IMG_CRYSTAL.getHeight() * 3f),
				null);
		// Antialising reaktivieren
		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
		g.setColor(Color.BLACK);
		g.setFont(FONT_BALANCE);
		g.drawString(MainZap.getCrystals() + "", 180, 107);

		// Ganze Upgrade-Tabelle
		paintStatUpgradeSection(g);
	}

	private static void paintStatUpgradeSection(Graphics2D g) {

		int y = STAT_POS_Y;
		paintStatObject(g, STAT_POS_X, y, PaintableStats.IMG_HEALTH, PaintableStats.COLOR_HEALTH,
				"health: " + MainZap.getPlayer().getMaxHp() + " hp", activeUpgrades[0], false);
		y += 40 + STAT_SPACE_Y;
		paintStatObject(g, STAT_POS_X, y, PaintableStats.IMG_DAMAGE, PaintableStats.COLOR_DAMAGE,
				"damage: " + MainZap.getPlayer().getBulletDamage() + " hp", activeUpgrades[1], false);
		y += 40 + STAT_SPACE_Y;
		paintStatObject(g, STAT_POS_X, y, PaintableStats.IMG_EFFICIENCY, PaintableStats.COLOR_EFFICIENCY,
				"ammo usage: " + MainZap.getPlayer().getAmmoUsageFac() + "x", activeUpgrades[2], false);
		y += 40 + STAT_SPACE_Y;
		paintStatObject(g, STAT_POS_X, y, PaintableStats.IMG_BULLETSPEED, PaintableStats.COLOR_BULLETSPEED,
				"bullet speed: " + MainZap.getPlayer().getBulletSpeed() + " p/t", activeUpgrades[3], false);

	}

	private static void paintStatObject(Graphics2D g, int x, int y, BufferedImage img, Color c, String text, byte lvl,
			boolean hovered) {

		g.setStroke(new BasicStroke(1));
		g.setColor(Color.BLUE);
		// g.drawLine(x, y + 20, x + 600, y + 20);

		g.drawImage(img, x, y, 40, 40, null);
		g.setColor(c);
		g.setFont(FONT_VALUE);
		g.drawString(text, x + 50, y + FONT_VALUE.getSize() + 8);
		g.setColor(Color.BLACK);
		g.setFont(FONT_LVL);
		g.drawString(lvl + "/5", x + 250, y + FONT_LVL.getSize() + 5);

		if (lvl == 5)
			return; // Ausgemaxt. Man kann eh nix kaufen

		g.setFont(FONT_PERCENTAGE);
		g.setColor(COLOR_GREEN);
		if (img == PaintableStats.IMG_EFFICIENCY) {
			// Minus. Es geht um den Munitions-Verbrauch
			g.drawString("-10%", x + 310, y + FONT_PERCENTAGE.getSize() + 7);
		} else {
			g.drawString("+10%", x + 310, y + FONT_PERCENTAGE.getSize() + 7);
		}

		// Antialising deaktivieren
		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}
		g.drawImage(IMG_CRYSTAL, x + 389, y + 10, (int) (IMG_CRYSTAL.getWidth() * 2f),
				(int) (IMG_CRYSTAL.getHeight() * 2f), null);
		// Antialising reaktivieren
		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}

		g.setColor(Color.BLACK);
		g.setFont(FONT_PRICE);
		g.drawString(priceTable[lvl] + "", x + 411, y + FONT_PRICE.getSize() + 4);

		g.setColor(ShopSecBuy.COLOR_BUY);
		g.setStroke(STROKE_UPGRADE);
		g.drawRect(x + 490, y + 2, 76, 36);
		if (hovered) {
			g.setColor(ShopSecBuy.COLOR_BUY_HOVER);
			g.fillRect(x + 490, y + 2, 76, 36);
		}
		g.setColor(ShopSecBuy.COLOR_BUY);
		g.setFont(FONT_UPGRADE);
		g.drawString(TEXT_UPGRADE, x + 494, y + 33);

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
