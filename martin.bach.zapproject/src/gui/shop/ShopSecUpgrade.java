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
import corecase.StringConverter;
import gui.extention.Extention;
import gui.extention.ExtentionManager;
import gui.extention.Mirroring;
import gui.extention.Shielding;
import gui.shop.meta.RainmakerShipConfig;
import io.TextureBuffer;

public abstract class ShopSecUpgrade {

	private static final int PRICE_MIRROR = 1400;
	private static final int PRICE_SHIELD = 700;
	private static final int PRICE_SHOCK = 1500;
	private static final int[] PRICE_CANNON = new int[] { 2000, 3000, -1 };
	private static final BufferedImage IMG_CRYSTAL = TextureBuffer.get(TextureBuffer.NAME_CRYSTAL);
	private static final BufferedImage IMG_EXT_MIRROR = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_MIRROR);
	private static final BufferedImage IMG_EXT_SHIELD = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_SHIELD);
	private static final BufferedImage IMG_EXT_SHOCK = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_SHOCK);
	private static final BufferedImage IMG_EXT_ADDCANNON = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_ADDCANNON);
	private static final byte DIGITS_FLOATSTATS = 2;
	private static final int STAT_POS_X = 40 + 75;
	private static final int EXT_D_POS_X = 75;
	private static final int EXT_D_POS_Y = 75;
	private static final int SYM_BACK_X = 40;
	private static final int SYM_BACK_Y = 70;
	private static final int STAT_POS_Y = 180;
	private static final int STAT_SPACE_Y = 15;
	private static final int HEADLINE_LINE_BASIC_POS_Y = STAT_POS_Y - 140 + 123;
	private static final int HEADLINE_LINE_EXTENTION_POS_Y = 370;
	private static final int EXT_DESCRIPTIONWIDTH = 190;
	private static final Color COLOR_HOVER_BACK = new Color(0, 0, 50, 30);
	private static final Color COLOR_HOVER_BUY = new Color(255, 0, 0, 60);
	private static final Color COLOR_GREEN = new Color(30, 205, 30);
	private static final Color COLOR_MAX = new Color(210, 30, 30);
	private static final Color COLOR_EXT_ACTIVE_BASE = new Color(255, 127, 89);
	private static final Color COLOR_CANNON_ACTIVE_BG = new Color(70, 85, 255, 30);
	private static final Color COLOR_CANNON_ACTIVE_FG = new Color(70, 85, 255, 170);
	private static final Color COLOR_LOCKED_BG = new Color(0, 0, 0, 190);
	private static final Color COLOR_LOCKED_FG = new Color(240, 240, 240, 190);
	private static final Font FONT_BALANCE = new Font("Arial", Font.BOLD, 35);
	private static final Font FONT_VALUE = new Font("Arial", 0, 20);
	private static final Font FONT_LVL = new Font("Arial", Font.BOLD, 25);
	private static final Font FONT_PERCENTAGE = new Font("Arial", Font.BOLD, 22);
	private static final Font FONT_PRICE = new Font("Arial", Font.BOLD, 26);
	private static final Font FONT_UPGRADE = new Font("Arial", Font.BOLD, 33);
	private static final Font FONT_MAX = new Font("Arial", Font.BOLD + Font.ITALIC, 38);
	private static final Font FONT_HEADLINE = new Font("Arial", Font.ITALIC, 20);
	private static final Font FONT_EXT_NAME = new Font("Arial", 0, 18);
	private static final Font FONT_EXT_PRICE = new Font("Arial", Font.BOLD, 16);
	private static final Font FONT_EXT_DESC_NAME = new Font("Arial", Font.BOLD, 18);
	private static final Font FONT_EXT_DESC_DESC = new Font("Arial", 0, 15);
	private static final Font FONT_EXT_DESC_PRICE = new Font("Arial", Font.BOLD, 20);
	private static final Font FONT_CANNNONS_ACTIVE = new Font("Arial", Font.BOLD, 18);
	private static final Font FONT_LOCKED_0 = new Font("Arial", Font.BOLD, 18);
	private static final Font FONT_LOCKED_1 = new Font("Arial", Font.BOLD, 16);
	private static final Stroke STROKE_UPGRADE = new BasicStroke(3);
	private static final Stroke STROKE_HEADLINE_LINE = new BasicStroke(3);
	private static final Stroke STROK_TOPIC_FRAME = new BasicStroke(1.4f);
	private static final Stroke STROKE_EXT_ACTIVE = new BasicStroke(5f);
	private static final Rectangle BOUNDS_BACK = new Rectangle(SYM_BACK_X - 5, SYM_BACK_Y - 5, 60, 60);
	private static final Rectangle BOUNDS_EXT_BUY = new Rectangle(605, 617, 76, 36);
	private static final Rectangle[] BOUNDS_BUY = new Rectangle[] {
			new Rectangle(STAT_POS_X + 490, STAT_POS_Y + (0 * 40) + (0 * STAT_SPACE_Y) + 2, 76, 36),
			new Rectangle(STAT_POS_X + 490, STAT_POS_Y + (1 * 40) + (1 * STAT_SPACE_Y) + 2, 76, 36),
			new Rectangle(STAT_POS_X + 490, STAT_POS_Y + (2 * 40) + (2 * STAT_SPACE_Y) + 2, 76, 36),
			new Rectangle(STAT_POS_X + 490, STAT_POS_Y + (3 * 40) + (3 * STAT_SPACE_Y) + 2, 76, 36) };
	private static final Rectangle[] BOUNDS_EXT = new Rectangle[] { new Rectangle(120, 465, 84, 84),
			new Rectangle(120, 567, 84, 84), new Rectangle(310, 567, 84, 84), new Rectangle(310, 465, 84, 84) };
	private static final Polygon SYMBOL_BACK = new Polygon(new int[] { SYM_BACK_X, 50 + SYM_BACK_X, 50 + SYM_BACK_X },
			new int[] { 25 + SYM_BACK_Y, SYM_BACK_Y, 50 + SYM_BACK_Y }, 3);
	private static final String TEXT_UPGRADE = "BUY";
	private static final String TEXT_HEADLINE_BASIC = "Basic Upgrades";
	private static final String TEXT_HEADLINE_EXTENTIONS = "Ship Extentions";
	private static final String TEXT_EXT_DESC_ACTIVE = "ACTIVE";
	private static final String TEXT_TITEL_MIRROR = "Mirror";
	private static final String TEXT_TITEL_SHIELD = "Shield";
	private static final String TEXT_TITEL_SHOCK = "Shock";
	private static final String TEXT_TITEL_ADDCANNON = "+Cannon";
	private static final String TEXT_CANNONS_ACTIVE = "installed:";
	private static final String TEXT_DES_ONLY_ONE_EXTENTION = "You can only select one extention at a time!";
	private static final String TEXT_DES_MIRROR = "No more alone! Duplicates your ship to an armee of four. Mirror images have the same meta like you, but can not be healed. Cooldown: 20 sec. ";
	private static final String TEXT_DES_SHIELD = "Temporary sentry mode. You are slowed down for 15 seconds, but your weapons are buffed and damage is heavily absorbed. ";
	private static final String TEXT_DES_SHOCK = "Shock wave. Damages surrounding enemys and slows down their systems as well as breaking shields.";
	private static final String TEXT_DES_ADDCANNON = "Adds a cannon, but does not affect ammo usage. You can only have 3 cannons at total. ";

	// -- Dailog ----------
	private static final Color COLOR_DIA_BG = new Color(255, 255, 255, 240);
	private static final Color COLOR_DIA_BORDER = new Color(0, 0, 0, 100);
	private static final Color COLOR_DIA_TEXT = new Color(0, 0, 0, 180);
	private static final Font FONT_DIALOG = new Font("Arial", Font.BOLD, 22);
	private static final Font FONT_DIALOG_OPTION = new Font("Arial", Font.BOLD, 40);
	private static final Rectangle BOUNDS_DIALOG = new Rectangle(215, 325, 370, 150);
	private static final int BORDERWIDTH_DIALOG = 8;
	private static final Stroke STROKE_BUTTON_BORDER = new BasicStroke(5);
	private static final Rectangle BOUNDS_DIA_YES = new Rectangle(255, 405, 120, 50);
	private static final Rectangle BOUNDS_DIA_NO = new Rectangle(425, 405, 120, 50);
	// ---

	private static boolean inDialog = false;
	private static String textDia = "";
	private static int priceDia = 0;
	// -- Hovers -------
	private static boolean hoveringBack = false;
	private static boolean[] hoveringBuys = new boolean[] { false, false, false, false };
	private static boolean hoveringExtBuy = false;
	private static boolean hoveringDiaYes = false;
	private static boolean hoveringDiaNo = false;
	// ---

	// -- Mechanics -----
	// Health, Damage, Ammo-Efficiency, Proj.Speed
	private static byte[] activeUpgrades = new byte[] { 0, 0, 0, 0 };
	private static int[] priceTable = new int[] { 60, 120, 350, 550, 1100 };
	// Mirror, Shield, Shock, ExtraCannons
	private static boolean[] extentionStates = new boolean[] { false, false, false, false };
	private static byte addedCannons = 0;
	private static byte selectedExtention = 0; // Mirror, Shield, Shock, Cannon
	private static PaintableDescription activeDescription = new PaintableDescription(TEXT_DES_MIRROR,
			EXT_DESCRIPTIONWIDTH);
	// ---

	// -- Animationen ----
	private static Color colorExtActive = COLOR_EXT_ACTIVE_BASE;
	private static boolean gettingWhite = true;
	private static final int C_DELTA_GREEN = 255 - COLOR_EXT_ACTIVE_BASE.getGreen();
	private static final int C_DELTA_BLUE = 255 - COLOR_EXT_ACTIVE_BASE.getBlue();
	private static final float CHANGE_SPEED = 0.02f;
	private static final float D_G = C_DELTA_GREEN * CHANGE_SPEED;
	private static final float D_B = (C_DELTA_BLUE / (float) C_DELTA_GREEN) * CHANGE_SPEED;
	private static float r = 255;
	private static float g = colorExtActive.getGreen();
	private static float b = colorExtActive.getBlue();
	// ---

	protected static void update() {

		if (g >= 180) {
			gettingWhite = false;
		} else if (g <= 90) {
			gettingWhite = true;
		}

		if (gettingWhite) {
			g += D_G;
			b += D_B;
		} else {
			g -= D_G;
			b -= D_B;
		}

		// clippen
		if (g < 0)
			g = 0;
		if (g > 255)
			g = 255;
		if (b < 0)
			b = 0;
		if (b > 255)
			b = 255;
		colorExtActive = new Color((int) r, (int) g, (int) b);
	}

	protected static void paint(Graphics2D g) {

		// -- Zur�ck-Symbol -------
		g.setColor(Color.BLACK);
		if (hoveringBack) {
			g.setColor(COLOR_HOVER_BACK);
			g.fillRect(BOUNDS_BACK.x, BOUNDS_BACK.y, BOUNDS_BACK.width, BOUNDS_BACK.height);
		}
		g.setColor(ShopMenu.COLOR_SELECT_BORDER);
		g.fillPolygon(SYMBOL_BACK);
		// ----

		// -- Gesamt Geld -----
		Shop.drawCrystal(g, 150, 78, 3);
		g.setColor(Color.BLACK);
		g.setFont(FONT_BALANCE);
		g.drawString(MainZap.getCrystals() + "", 180, 107);
		// -----

		// -- �berschrift: Basic Upgrades ----
		g.setFont(FONT_HEADLINE);
		g.drawString(TEXT_HEADLINE_BASIC, 420 + 75, 130 + STAT_POS_Y - 140);
		g.setStroke(STROKE_HEADLINE_LINE);
		g.drawLine(400 + 75, HEADLINE_LINE_BASIC_POS_Y, 413 + 75, HEADLINE_LINE_BASIC_POS_Y);
		g.drawLine(567 + 75, HEADLINE_LINE_BASIC_POS_Y, 610 + 75, HEADLINE_LINE_BASIC_POS_Y);
		// ---

		// Ganze Upgrade-Tabelle
		paintStatUpgradeSection(g);

		// -- �berschrift: Extention Upgrades ----
		g.setColor(Color.BLACK);
		g.setFont(FONT_HEADLINE);
		g.drawString(TEXT_HEADLINE_EXTENTIONS, 420 + EXT_D_POS_X, HEADLINE_LINE_EXTENTION_POS_Y + 7 + EXT_D_POS_Y);
		g.setStroke(STROKE_HEADLINE_LINE);
		g.drawLine(400 + EXT_D_POS_X, HEADLINE_LINE_EXTENTION_POS_Y + EXT_D_POS_Y, 413 + EXT_D_POS_X,
				HEADLINE_LINE_EXTENTION_POS_Y + EXT_D_POS_Y);
		g.drawLine(567 + EXT_D_POS_X, HEADLINE_LINE_EXTENTION_POS_Y + EXT_D_POS_Y, 610 + EXT_D_POS_X,
				HEADLINE_LINE_EXTENTION_POS_Y + EXT_D_POS_Y);
		// ---

		paintExtentionUpgradeSection(g);

		// --------------------------------------------
		// Dialog
		if (inDialog) {
			paintDialog(g);
		}
	}

	private static void paintExtentionUpgradeSection(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setStroke(STROK_TOPIC_FRAME);
		g.drawRect(37 + EXT_D_POS_X, 382 + EXT_D_POS_Y, 576, 201);
		g.drawRect(420 + EXT_D_POS_X, 382 + EXT_D_POS_Y, 193, 201);
		paintExtentionUpgradeButton(g, IMG_EXT_MIRROR, 45 + EXT_D_POS_X, 390 + EXT_D_POS_Y, TEXT_TITEL_MIRROR,
				extentionStates[0], PRICE_MIRROR, Shop.unlocked[1]);
		paintExtentionUpgradeButton(g, IMG_EXT_SHIELD, 45 + EXT_D_POS_X, 492 + EXT_D_POS_Y, TEXT_TITEL_SHIELD,
				extentionStates[1], PRICE_SHIELD, Shop.unlocked[2]);
		paintExtentionUpgradeButton(g, IMG_EXT_SHOCK, 235 + EXT_D_POS_X, 492 + EXT_D_POS_Y, TEXT_TITEL_SHOCK,
				extentionStates[2], PRICE_SHOCK, Shop.unlocked[3]);
		paintExtentionUpgradeButton(g, IMG_EXT_ADDCANNON, 235 + EXT_D_POS_X, 390 + EXT_D_POS_Y, TEXT_TITEL_ADDCANNON,
				extentionStates[3], PRICE_CANNON[addedCannons], true);
		paintExtentionDesciption(g, activeDescription, selectedExtention, extentionStates[selectedExtention],
				hoveringExtBuy);
	}

	private static void paintExtentionDesciption(Graphics2D g, PaintableDescription description, int index,
			boolean active, boolean buyHovered) {

		String titel = "";
		int price = 0;
		switch (index) {
		case 0:
			titel = TEXT_TITEL_MIRROR;
			price = PRICE_MIRROR;
			break;
		case 1:
			titel = TEXT_TITEL_SHIELD;
			price = PRICE_SHIELD;
			break;
		case 2:
			titel = TEXT_TITEL_SHOCK;
			price = PRICE_SHOCK;
			break;
		case 3:
			titel = "Add Cannon";
			price = PRICE_CANNON[addedCannons];
			break;
		}

		g.setFont(FONT_EXT_DESC_NAME);
		g.setColor(Color.BLACK);
		g.drawString(titel, 425 + EXT_D_POS_X, 400 + EXT_D_POS_Y);
		g.setFont(FONT_EXT_DESC_DESC);
		description.paint(g, 425 + EXT_D_POS_X, 415 + EXT_D_POS_Y);

		if (active && index != 3) {
			g.setColor(COLOR_MAX);
			g.setFont(FONT_MAX);
			g.drawString(TEXT_EXT_DESC_ACTIVE, 446 + EXT_D_POS_X, 570 + EXT_D_POS_Y);
			return;
		}

		if (index == 3) {
			// Anzahl installierter Kannonen zeichnen
			g.setColor(ShopSecBuy.COLOR_FG);
			g.setFont(FONT_CANNNONS_ACTIVE);
			g.drawString(TEXT_CANNONS_ACTIVE, 444 + EXT_D_POS_X, 522 + EXT_D_POS_Y);
			g.setColor(COLOR_CANNON_ACTIVE_BG);
			g.fillRect(529 + EXT_D_POS_X, 503 + EXT_D_POS_Y, 20, 25);
			g.fillRect(551 + EXT_D_POS_X, 503 + EXT_D_POS_Y, 20, 25);
			g.fillRect(573 + EXT_D_POS_X, 503 + EXT_D_POS_Y, 20, 25);
			g.setColor(COLOR_CANNON_ACTIVE_FG);
			g.fillRect(529 + EXT_D_POS_X, 503 + EXT_D_POS_Y, 20, 25);
			if (addedCannons > 0) {
				g.fillRect(551 + EXT_D_POS_X, 503 + EXT_D_POS_Y, 20, 25);
				if (addedCannons > 1) {
					g.fillRect(573 + EXT_D_POS_X, 503 + EXT_D_POS_Y, 20, 25);
					return; // Mann kan eh nix mehr kaufen
				}
			}
		}

		if (index != 3 && !Shop.unlocked[index + 1])
			return;

		Shop.drawCrystal(g, 418 + EXT_D_POS_X, 550 + EXT_D_POS_Y, 2);
		g.setColor(ShopSecBuy.COLOR_FG);
		g.setFont(FONT_EXT_DESC_PRICE);
		g.drawString(price + "", 439 + EXT_D_POS_X, 569 + EXT_D_POS_Y);

		g.setColor(ShopSecBuy.COLOR_BUY);
		g.setStroke(STROKE_UPGRADE);
		g.drawRect(530 + EXT_D_POS_X, 542 + EXT_D_POS_Y, 76, 36);
		if (buyHovered) {
			g.setColor(COLOR_HOVER_BUY);
			g.fillRect(530 + EXT_D_POS_X, 542 + EXT_D_POS_Y, 76, 36);
		}
		g.setColor(ShopSecBuy.COLOR_BUY);
		g.setFont(FONT_UPGRADE);
		g.drawString(TEXT_UPGRADE, 534 + EXT_D_POS_X, 572 + EXT_D_POS_Y);

	}

	private static void paintExtentionUpgradeButton(Graphics2D g, BufferedImage img, int x, int y, String name,
			boolean active, int price, boolean unlocked) {

		if (active) {
			g.setColor(colorExtActive);
			g.setStroke(STROKE_EXT_ACTIVE);
			g.drawRect(x - 2, y - 2, 84 + 4, 84 + 4);
		}

		// Nametag-Pricetag-BG
		g.setColor(ShopSecBuy.COLOR_BG);
		g.fillPolygon(new int[] { x + 90, x + 182, x + 164, x + 90 }, new int[] { y + 15, y + 15, y + 35, y + 35 }, 4);
		if (!(img == IMG_EXT_ADDCANNON && addedCannons == 2))
			g.fillRect(x + 105, y + 45, 52, 22);

		// Nametag-Pricetag-FG
		g.setColor(ShopSecBuy.COLOR_FG);
		g.setFont(FONT_EXT_NAME);
		g.drawString(name, x + 92, y + 32);
		if (!(img == IMG_EXT_ADDCANNON && addedCannons == 2)) {
			g.setFont(FONT_EXT_PRICE);
			g.drawString(price + "", x + 106, y + 62);
		}

		// Antialising deaktivieren
		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		g.drawImage(img, x, y, 84, 84, null);
		if (!(img == IMG_EXT_ADDCANNON && addedCannons == 2))
			g.drawImage(IMG_CRYSTAL, x + 86, y + 45, (int) (IMG_CRYSTAL.getWidth() * 2f),
					(int) (IMG_CRYSTAL.getHeight() * 2f), null);

		// Antialising reaktivieren
		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		if (!unlocked) {
			g.setColor(COLOR_LOCKED_BG);
			g.fillRect(x - 2, y - 2, 175 + 4, 84 + 4);
			g.setColor(COLOR_LOCKED_FG);
			g.setFont(FONT_LOCKED_0);
			g.drawString("[LOCKED]", x + 42, y + 42 - 7);
			g.setFont(FONT_LOCKED_1);
			g.drawString("No License", x + 42 + 1, y + 42 - 9 + 24);
		}

	}

	private static void paintStatUpgradeSection(Graphics2D g) {

		int y = STAT_POS_Y;
		paintStatObject(g, STAT_POS_X, y, PaintableStats.IMG_HEALTH, PaintableStats.COLOR_HEALTH,
				"health: " + MainZap.getPlayer().getMaxHp() + " hp", activeUpgrades[0], hoveringBuys[0],
				Shop.unlocked[4]);
		y += 40 + STAT_SPACE_Y;
		paintStatObject(g, STAT_POS_X, y, PaintableStats.IMG_DAMAGE, PaintableStats.COLOR_DAMAGE,
				"damage: " + MainZap.getPlayer().getBulletDamage() + " hp", activeUpgrades[1], hoveringBuys[1],
				Shop.unlocked[5]);
		y += 40 + STAT_SPACE_Y;
		paintStatObject(g, STAT_POS_X, y, PaintableStats.IMG_EFFICIENCY, PaintableStats.COLOR_EFFICIENCY,
				"ammo usage: " + cut(MainZap.getPlayer().getAmmoUsageFac(), DIGITS_FLOATSTATS) + "x", activeUpgrades[2],
				hoveringBuys[2], Shop.unlocked[6]);
		y += 40 + STAT_SPACE_Y;
		paintStatObject(g, STAT_POS_X, y, PaintableStats.IMG_BULLETSPEED, PaintableStats.COLOR_BULLETSPEED,
				"bullet speed: " + cut(MainZap.getPlayer().getBulletSpeed(), DIGITS_FLOATSTATS) + " p/t",
				activeUpgrades[3], hoveringBuys[3], Shop.unlocked[7]);

	}

	private static void paintStatObject(Graphics2D g, int x, int y, BufferedImage img, Color c, String text, byte lvl,
			boolean hovered, boolean unlocked) {

		g.setColor(Color.BLACK);
		g.setStroke(STROK_TOPIC_FRAME);
		g.drawRect(x - 3, y - 3, 575, 46);

		// Antialising deaktivieren
		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		g.drawImage(img, x, y, 40, 40, null);

		// Antialising reaktivieren
		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		g.setColor(c);
		g.setFont(FONT_VALUE);
		g.drawString(text, x + 50, y + FONT_VALUE.getSize() + 8);

		if (lvl == 5) {
			g.setColor(COLOR_MAX);
			g.setFont(FONT_MAX);
			g.drawString("MAX", x + 389, y + FONT_MAX.getSize() - 3);
			return; // Ausgemaxt. Man kann eh nix kaufen
		}

		g.setColor(Color.BLACK);
		g.setFont(FONT_LVL);
		g.drawString(lvl + "/5", x + 254, y + FONT_LVL.getSize() + 5);

		g.setFont(FONT_PERCENTAGE);
		g.setColor(COLOR_GREEN);
		if (img == PaintableStats.IMG_EFFICIENCY) {
			// Minus. Es geht um den Munitions-Verbrauch
			g.drawString("-15%", x + 314, y + FONT_PERCENTAGE.getSize() + 7);
		} else {
			g.drawString("+15%", x + 310, y + FONT_PERCENTAGE.getSize() + 7);
		}

		Shop.drawCrystal(g, x + 389, y + 10, 2);

		g.setColor(Color.BLACK);
		g.setStroke(STROK_TOPIC_FRAME);
		g.drawRect(x + 380, y - 3, 192, 46);

		g.setColor(Color.BLACK);
		g.setFont(FONT_PRICE);
		g.drawString(priceTable[lvl] + "", x + 411, y + FONT_PRICE.getSize() + 4);

		if (unlocked) {
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
		} else {
			g.setColor(COLOR_LOCKED_BG);
			g.fillRect(x - 3, y - 3, 575, 46);
			g.setColor(COLOR_LOCKED_FG);
			g.setFont(FONT_LOCKED_0);
			g.drawString("[LOCKED]", x + 20 + 30, y + 23 + 2);
			g.setFont(FONT_LOCKED_1);
			g.drawString("License required", x + 120 + 30, y + 23 + 2);
		}

	}

	private static float cut(float f, byte digits) {
		return (((int) (f * 10 * digits)) / (10.0f * digits));
	}

	protected static void callClick(int tx, int ty) {

		if (inDialog) {
			if (BOUNDS_DIA_YES.contains(tx, ty)) {
				inDialog = false;
				MainZap.removeCrystals(priceDia);
				extentionStates = new boolean[] { false, false, false, false };
				byte cannonsBeforeReset = (byte) (addedCannons + 1);
				resetExtentions();
				extentionStates[selectedExtention] = true;
				if (selectedExtention == 3) {
					// Cannons
					addedCannons = cannonsBeforeReset;
					MainZap.getPlayer().setWeaponAmount((byte) (1 + cannonsBeforeReset));
					ExtentionManager.setExtention(null);
				} else if (selectedExtention == 0) {
					// Mirror
					ExtentionManager.setExtention(Extention.MIRROR);
				} else if (selectedExtention == 1) {
					// Shield
					ExtentionManager.setExtention(Extention.SHIELD);
				} else if (selectedExtention == 2) {
					// Shock
					ExtentionManager.setExtention(Extention.SHOCK);
				}

			} else if (BOUNDS_DIA_NO.contains(tx, ty)) {
				inDialog = false;
			}
			return;
		}

		if (BOUNDS_BACK.contains(tx, ty)) {
			Shop.setDirectory(ShopDirectory.MENU);
			callMove(100000, 10000); // Hovers ausschalten
			return;
		}

		if (BOUNDS_EXT_BUY.contains(tx, ty)) {
			purchaseExtention();
		}

		// Irgend ein "Buy" geklickt?
		byte i = 0;
		for (Rectangle r : BOUNDS_BUY) {
			if (r.contains(tx, ty)) {
				purchaseUpgrade(i); // Versuch zu kaufen
				return;
			}
			i++;
		}

		// Irgend ne Extention geklickt?
		i = 0;
		for (Rectangle r : BOUNDS_EXT) {
			if (r.contains(tx, ty)) {
				clickExtention(i);
				return;
			}
			i++;
		}

	}

	protected static void callMove(int tx, int ty) {

		if (inDialog) {
			if (BOUNDS_DIA_YES.contains(tx, ty)) {
				hoveringDiaYes = true;
				hoveringDiaNo = false;
				return;
			} else if (BOUNDS_DIA_NO.contains(tx, ty)) {
				hoveringDiaYes = false;
				hoveringDiaNo = true;
				return;
			}
			hoveringBuys[0] = false;
			hoveringBuys[1] = false;
			hoveringBuys[2] = false;
			hoveringBuys[3] = false;
			hoveringDiaYes = false;
			hoveringDiaNo = false;
			hoveringBack = false;
			hoveringExtBuy = false;
			return;
		}

		if (BOUNDS_EXT_BUY.contains(tx, ty)) {
			hoveringBack = false;
			hoveringBuys[0] = false;
			hoveringBuys[1] = false;
			hoveringBuys[2] = false;
			hoveringBuys[3] = false;
			hoveringDiaYes = false;
			hoveringDiaNo = false;
			hoveringExtBuy = true;
			return;
		}

		if (BOUNDS_BACK.contains(tx, ty)) {
			hoveringBack = true;
			hoveringBuys[0] = false;
			hoveringBuys[1] = false;
			hoveringBuys[2] = false;
			hoveringBuys[3] = false;
			hoveringExtBuy = false;
			hoveringDiaYes = false;
			hoveringDiaNo = false;
			return;
		}

		// Maus �ber "Buy" bei Stats?
		int i = 0;
		hoveringBuys[0] = false;
		hoveringBuys[1] = false;
		hoveringBuys[2] = false;
		hoveringBuys[3] = false;
		hoveringDiaYes = false;
		hoveringDiaNo = false;
		for (Rectangle r : BOUNDS_BUY) {
			if (r.contains(tx, ty)) {
				hoveringBuys[i] = true;
				return;
			}
			i++;
		}

		hoveringDiaYes = false;
		hoveringDiaNo = false;
		hoveringBuys[0] = false;
		hoveringBuys[1] = false;
		hoveringBuys[2] = false;
		hoveringBuys[3] = false;
		hoveringBack = false;
		hoveringExtBuy = false;

	}

	private static void clickExtention(byte i) {
		selectedExtention = i;
		switch (i) {
		case 0:
			activeDescription = new PaintableDescription(TEXT_DES_MIRROR + TEXT_DES_ONLY_ONE_EXTENTION,
					EXT_DESCRIPTIONWIDTH);
			break;
		case 1:
			activeDescription = new PaintableDescription(TEXT_DES_SHIELD + TEXT_DES_ONLY_ONE_EXTENTION,
					EXT_DESCRIPTIONWIDTH);
			break;
		case 2:
			activeDescription = new PaintableDescription(TEXT_DES_SHOCK + TEXT_DES_ONLY_ONE_EXTENTION,
					EXT_DESCRIPTIONWIDTH);
			break;
		case 3:
			activeDescription = new PaintableDescription(TEXT_DES_ADDCANNON + TEXT_DES_ONLY_ONE_EXTENTION,
					EXT_DESCRIPTIONWIDTH);
			break;
		default:
			Cmd.err("ShopSecUpgrade:397 impossible");
			break;
		}
	}

	private static void purchaseExtention() {

		// �berhaupt g�ltig?
		if ((extentionStates[selectedExtention] && selectedExtention != 3)
				|| (selectedExtention == 3 && addedCannons == 2))
			return; // Haste schon. Oder Kannonen ausgemaxt
		// Freigeschaltet?
		if (!Shop.unlocked[selectedExtention + 1])
			return;
		// -Preis:
		if (selectedExtention == 0) {
			priceDia = PRICE_MIRROR;
		} else if (selectedExtention == 1) {
			priceDia = PRICE_SHIELD;
		} else if (selectedExtention == 2) {
			priceDia = PRICE_SHOCK;
		} else if (selectedExtention == 3) {
			priceDia = PRICE_CANNON[addedCannons];
		}
		if (MainZap.getCrystals() < priceDia)
			return;

		String sel = ""; // Ausgew�hlt
		String res = ""; // Endergebniss
		String act = ""; // Aktiv
		boolean cannon = false; // Kannonen-�nderung

		// Ausgew�hltes Bestimmen
		if (selectedExtention == 0) {
			sel = TEXT_TITEL_MIRROR;
		} else if (selectedExtention == 1) {
			sel = TEXT_TITEL_SHIELD;
		} else if (selectedExtention == 2) {
			sel = TEXT_TITEL_SHOCK;
		} else if (selectedExtention == 3) {
			sel = TEXT_TITEL_ADDCANNON;
			cannon = true;
		}

		// Aktives Bestimmen, falls da
		boolean active = false;
		int i = 0;
		for (boolean b : extentionStates) {
			if (b == true) {
				active = true;
				if (i == 0) {
					act = TEXT_TITEL_MIRROR;
				} else if (i == 1) {
					act = TEXT_TITEL_SHIELD;
				} else if (i == 2) {
					act = TEXT_TITEL_SHOCK;
				} else if (i == 3) {
					act = TEXT_TITEL_ADDCANNON;
				}
				break;
			}
			i++;
		}

		// Aktion bestimmen
		if (active) {
			if (!cannon) {
				res = "Exchange " + act + " by " + sel + "?";
			} else {
				if (MainZap.getPlayer().getTexture() == RainmakerShipConfig.TEXTURE)
					res = "Add cannon " + StringConverter.inRoman(addedCannons + 6) + " ?";
				else
					res = "Add cannon " + StringConverter.inRoman(addedCannons + 2) + " ?";

			}
		} else {
			if (!cannon) {
				res = "Activate " + sel + "?";
			} else {
				if (MainZap.getPlayer().getTexture() == RainmakerShipConfig.TEXTURE)
					res = "Add cannon " + StringConverter.inRoman(addedCannons + 6) + " ?";
				else
					res = "Add cannon " + StringConverter.inRoman(addedCannons + 2) + " ?";
			}
		}

		textDia = res;
		inDialog = true;
		return;
	}

	public static void purchaseUpgrade(int index) {
		if (priceTable[activeUpgrades[index]] > MainZap.getCrystals() || activeUpgrades[index] == 5
				|| !Shop.unlocked[index + 4])
			return; // Nicht genug Knete oder alle schon gekauft oder nicht freigschaltet

		MainZap.removeCrystals(priceTable[activeUpgrades[index]]);
		activeUpgrades[index]++;
		MainZap.getPlayer().setUpgraded(true);

		switch (index) {
		case 0:
			MainZap.getPlayer()
					.setMaxHp(MainZap.getPlayer().getMaxHp() + (int) (MainZap.getPlayer().getMaxHp() * 0.15f));
			break;
		case 1:
			MainZap.getPlayer().setBulletDamage(
					MainZap.getPlayer().getBulletDamage() + (int) (MainZap.getPlayer().getBulletDamage() * 0.15f));
			break;
		case 2:
			MainZap.getPlayer().setAmmoUsageFac(
					MainZap.getPlayer().getAmmoUsageFac() - (MainZap.getPlayer().getAmmoUsageFac() * 0.15f));
			break;
		case 3:
			MainZap.getPlayer().setBulletSpeed(
					MainZap.getPlayer().getBulletSpeed() + (int) (MainZap.getPlayer().getBulletSpeed() * 0.15f));
			break;
		default:
			Cmd.err("ShopSecUpgrade:231 Impossible!");
			break;
		}
	}

	protected static void reset() {
		activeUpgrades = new byte[] { 0, 0, 0, 0 };
		extentionStates = new boolean[] { false, false, false, false };
		MainZap.getPlayer().setUpgraded(false);
		resetExtentions();
	}

	protected static void resetExtentions() {
		Shielding.cancel();
		Mirroring.cancel();
		ExtentionManager.setExtention(null);
		MainZap.getPlayer().setWeaponAmount((byte) 1);
		addedCannons = 0;
	}

	private static void paintDialog(Graphics2D g) {
		g.setColor(COLOR_DIA_BORDER);
		g.fillRect(BOUNDS_DIALOG.x - BORDERWIDTH_DIALOG, BOUNDS_DIALOG.y - BORDERWIDTH_DIALOG,
				BOUNDS_DIALOG.width + 2 * BORDERWIDTH_DIALOG, BOUNDS_DIALOG.height + 2 * BORDERWIDTH_DIALOG);
		g.setColor(COLOR_DIA_BG);
		g.fillRect(BOUNDS_DIALOG.x, BOUNDS_DIALOG.y, BOUNDS_DIALOG.width, BOUNDS_DIALOG.height);

		g.setColor(new Color(0, 0, 0, 80));
		if (hoveringDiaYes) {
			// Maus �ber YES
			g.fillRect(BOUNDS_DIA_YES.x, BOUNDS_DIA_YES.y, BOUNDS_DIA_YES.width, BOUNDS_DIA_YES.height);
		} else if (hoveringDiaNo) {
			// Maus �ber NO
			g.fillRect(BOUNDS_DIA_NO.x, BOUNDS_DIA_NO.y, BOUNDS_DIA_NO.width, BOUNDS_DIA_NO.height);
		}

		g.setFont(FONT_DIALOG);
		g.setColor(COLOR_DIA_TEXT);
		g.drawString(textDia, BOUNDS_DIALOG.x + 9, BOUNDS_DIALOG.y + 50);

		g.setStroke(STROKE_BUTTON_BORDER);
		g.drawRect(BOUNDS_DIA_YES.x, BOUNDS_DIA_YES.y, BOUNDS_DIA_YES.width, BOUNDS_DIA_YES.height);
		g.drawRect(BOUNDS_DIA_NO.x, BOUNDS_DIA_NO.y, BOUNDS_DIA_NO.width, BOUNDS_DIA_NO.height);

		g.setFont(FONT_DIALOG_OPTION);
		g.drawString("Yes", BOUNDS_DIA_YES.x + 22, BOUNDS_DIA_YES.y + FONT_DIALOG_OPTION.getSize());
		g.drawString("No", BOUNDS_DIA_NO.x + 34, BOUNDS_DIA_NO.y + FONT_DIALOG_OPTION.getSize());

	}

}
