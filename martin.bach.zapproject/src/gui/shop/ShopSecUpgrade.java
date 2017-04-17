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

import corecase.Cmd;
import corecase.MainZap;
import io.TextureBuffer;

public abstract class ShopSecUpgrade {

	private static final BufferedImage IMG_CRYSTAL = TextureBuffer.get(TextureBuffer.NAME_CRYSTAL);
	private static final int STAT_POS_X = 40;
	private static final int SYM_BACK_X = 40;
	private static final int SYM_BACK_Y = 70;
	private static final int STAT_POS_Y = 140;
	private static final int STAT_SPACE_Y = 15;
	private static final Color COLOR_HOVER_BACK = new Color(0, 0, 50, 30);
	private static final Color COLOR_HOVER_BUY = new Color(255, 0, 0, 60);
	private static final Color COLOR_GREEN = new Color(30, 205, 30);
	private static final Font FONT_BALANCE = new Font("Arial", Font.BOLD, 35);
	private static final Font FONT_VALUE = new Font("Arial", 0, 20);
	private static final Font FONT_LVL = new Font("Arial", Font.BOLD, 25);
	private static final Font FONT_PERCENTAGE = new Font("Arial", Font.BOLD, 22);
	private static final Font FONT_PRICE = new Font("Arial", Font.BOLD, 26);
	private static final Font FONT_UPGRADE = new Font("Arial", Font.BOLD, 33);
	private static final Stroke STROKE_UPGRADE = new BasicStroke(3);
	private static final Stroke STROK_TOPIC_FRAME = new BasicStroke(1.4f);
	private static final Rectangle BOUNDS_BACK = new Rectangle(SYM_BACK_X - 5, SYM_BACK_Y - 5, 60, 60);
	private static final Rectangle[] BOUNDS_BUY = new Rectangle[] {
			new Rectangle(STAT_POS_X + 490, STAT_POS_Y + (0 * 40) + (0 * STAT_SPACE_Y) + 2, 76, 36),
			new Rectangle(STAT_POS_X + 490, STAT_POS_Y + (1 * 40) + (1 * STAT_SPACE_Y) + 2, 76, 36),
			new Rectangle(STAT_POS_X + 490, STAT_POS_Y + (2 * 40) + (2 * STAT_SPACE_Y) + 2, 76, 36),
			new Rectangle(STAT_POS_X + 490, STAT_POS_Y + (3 * 40) + (3 * STAT_SPACE_Y) + 2, 76, 36) };
	private static final Polygon SYMBOL_BACK = new Polygon(new int[] { SYM_BACK_X, 50 + SYM_BACK_X, 50 + SYM_BACK_X },
			new int[] { 25 + SYM_BACK_Y, SYM_BACK_Y, 50 + SYM_BACK_Y }, 3);
	private static final String TEXT_UPGRADE = "BUY";
	// -- Hovers -------
	private static boolean hoveringBack = false;
	private static boolean[] hoveringBuys = new boolean[] { false, false, false, false };
	// ---

	// -- Mechanics -----
	// Health, Damage, Ammo-Efficiency, Proj.Speed
	private static byte[] activeUpgrades = new byte[] { 0, 0, 0, 0 };
	private static int[] priceTable = new int[] { 100, 200, 400, 600, 1300 };
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
				"health: " + MainZap.getPlayer().getMaxHp() + " hp", activeUpgrades[0], hoveringBuys[0]);
		y += 40 + STAT_SPACE_Y;
		paintStatObject(g, STAT_POS_X, y, PaintableStats.IMG_DAMAGE, PaintableStats.COLOR_DAMAGE,
				"damage: " + MainZap.getPlayer().getBulletDamage() + " hp", activeUpgrades[1], hoveringBuys[1]);
		y += 40 + STAT_SPACE_Y;
		paintStatObject(g, STAT_POS_X, y, PaintableStats.IMG_EFFICIENCY, PaintableStats.COLOR_EFFICIENCY,
				"ammo usage: " + MainZap.getPlayer().getAmmoUsageFac() + "x", activeUpgrades[2], hoveringBuys[2]);
		y += 40 + STAT_SPACE_Y;
		paintStatObject(g, STAT_POS_X, y, PaintableStats.IMG_BULLETSPEED, PaintableStats.COLOR_BULLETSPEED,
				"bullet speed: " + MainZap.getPlayer().getBulletSpeed() + " p/t", activeUpgrades[3], hoveringBuys[3]);

	}

	private static void paintStatObject(Graphics2D g, int x, int y, BufferedImage img, Color c, String text, byte lvl,
			boolean hovered) {

		g.setColor(Color.BLACK);
		g.setStroke(STROK_TOPIC_FRAME);
		g.drawRect(x - 3, y - 3, 575, 46);

		g.drawImage(img, x, y, 40, 40, null);
		g.setColor(c);
		g.setFont(FONT_VALUE);
		g.drawString(text, x + 50, y + FONT_VALUE.getSize() + 8);
		g.setColor(Color.BLACK);
		g.setFont(FONT_LVL);
		g.drawString(lvl + "/5", x + 254, y + FONT_LVL.getSize() + 5);

		if (lvl == 5)
			return; // Ausgemaxt. Man kann eh nix kaufen

		g.setFont(FONT_PERCENTAGE);
		g.setColor(COLOR_GREEN);
		if (img == PaintableStats.IMG_EFFICIENCY) {
			// Minus. Es geht um den Munitions-Verbrauch
			g.drawString("-10%", x + 314, y + FONT_PERCENTAGE.getSize() + 7);
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
		g.setStroke(STROK_TOPIC_FRAME);
		g.drawRect(x + 380, y - 3, 192, 46);

		g.setColor(Color.BLACK);
		g.setFont(FONT_PRICE);
		g.drawString(priceTable[lvl] + "", x + 411, y + FONT_PRICE.getSize() + 4);

		g.setColor(ShopSecBuy.COLOR_BUY);
		g.setStroke(STROKE_UPGRADE);
		g.drawRect(x + 490, y + 2, 76, 36);
		if (hovered) {
			g.setColor(COLOR_HOVER_BUY);
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

		// Irgend ein "Buy" geklickt?
		int i = 0;
		for (Rectangle r : BOUNDS_BUY) {
			if (r.contains(tx, ty)) {
				purchase(i); // Versuch zu kaufen
				return;
			}
			i++;
		}

	}

	protected static void callMove(int tx, int ty) {

		if (BOUNDS_BACK.contains(tx, ty)) {
			hoveringBack = true;
			return;
		}

		// Maus über "Buy" bei Stats?
		int i = 0;
		hoveringBuys[0] = false;
		hoveringBuys[1] = false;
		hoveringBuys[2] = false;
		hoveringBuys[3] = false;
		for (Rectangle r : BOUNDS_BUY) {
			if (r.contains(tx, ty)) {
				hoveringBuys[i] = true;
				return;
			}
			i++;
		}

		hoveringBuys[0] = false;
		hoveringBuys[1] = false;
		hoveringBuys[2] = false;
		hoveringBuys[3] = false;
		hoveringBack = false;

	}

	private static void purchase(int index) {
		if (priceTable[index] > MainZap.getCrystals() || activeUpgrades[index] == 5)
			return; // Nicht genug Knete oder alle schon gekauft

		MainZap.setCrystals(MainZap.getCrystals() - priceTable[index]);
		activeUpgrades[index]++;

		switch (index) {
		case 0:
			MainZap.getPlayer()
					.setMaxHp(MainZap.getPlayer().getMaxHp() + (int) (MainZap.getPlayer().getMaxHp() * 0.1f));
			break;
		case 1:
			MainZap.getPlayer().setBulletDamage(
					MainZap.getPlayer().getBulletDamage() + (int) (MainZap.getPlayer().getBulletDamage() * 0.1f));
			break;
		case 2:
			MainZap.getPlayer().setAmmoUsageFac(
					MainZap.getPlayer().getAmmoUsageFac() - (MainZap.getPlayer().getAmmoUsageFac() * 0.1f));
			break;
		case 3:
			MainZap.getPlayer().setBulletSpeed(
					MainZap.getPlayer().getBulletSpeed() + (int) (MainZap.getPlayer().getBulletSpeed() * 0.1f));
			break;
		default:
			Cmd.err("ShopSecUpgrade:231 Impossible!");
			break;
		}
	}

	protected static void reset() {

	}

}
