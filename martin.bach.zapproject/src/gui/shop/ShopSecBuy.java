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
import java.util.ArrayList;

import corecase.MainZap;
import error.InitializationTimingException;
import gui.Frame;
import gui.extention.Mirroring;
import gui.shop.meta.ShipStartConfig;
import io.TextureBuffer;

public abstract class ShopSecBuy {

	public static final int DESCRIPTION_WIDTH = 272;
	protected static final float SCROLL_SPEED = 3.0f;
	protected static final BufferedImage IMG_CRYSTAL = TextureBuffer.get(TextureBuffer.NAME_CRYSTAL);
	protected static final Color COLOR_HOVER_BACK = new Color(0, 0, 50, 30);
	protected static final Color COLOR_IMG_BG = new Color(230, 230, 255);
	protected static final Color COLOR_BG = new Color(240, 240, 255);
	protected static final Color COLOR_FG = new Color(50, 0, 0);
	protected static final Color COLOR_FRAME = new Color(180, 50, 50, 120);
	protected static final Color COLOR_FRAME_BG = new Color(255, 220, 220, 50);
	protected static final Color COLOR_SITE_BORDER = new Color(50, 50, 255, 80);
	protected static final Color COLOR_STATS_BG = new Color(200, 200, 255, 30);
	protected static final Color COLOR_BUY = new Color(255, 100, 100);
	protected static final Color COLOR_BUY_HOVER = new Color(255, 0, 0, 20);
	protected static final Color COLOR_SCROLL_BORDER = new Color(198, 24, 33, 100);
	protected static final Color COLOR_SCROLL_FG = new Color(104, 33, 38, 150);
	protected static final Color COLOR_SCROLL_HOVER = new Color(40, 0, 0, 40);
	protected static final Color COLOR_SCROLL_BG = new Color(255, 255, 255, 200);
	protected static final Font FONT_NAME = new Font("Arial", Font.BOLD, 20);
	protected static final Font FONT_PRICE = new Font("Arial", Font.BOLD, 19);
	protected static final Font FONT_BALANCE = new Font("Arial", Font.BOLD, 35);
	protected static final Font FONT_BUY = new Font("Arial", Font.BOLD, 44);
	protected static final Font FONT_STATS_PRICE = new Font("Arial", Font.BOLD, 24);
	protected static final Font FONT_STATS_TITEL = new Font("Arial", Font.BOLD, 24);
	protected static final Font FONT_STATS_DESC = new Font("Arial", 0, 15);
	protected static final BasicStroke STROKE_ITEM_FRAME = new BasicStroke(2.3f);
	protected static final BasicStroke STROKE_SITE_BORDER = new BasicStroke(4f);
	protected static final BasicStroke STROKE_BUY = new BasicStroke(4.5f);
	protected static final BasicStroke STROKE_SCROLL_BORDER = new BasicStroke(3.4f);
	protected static final BasicStroke STROKE_SHIPS_CUT = new BasicStroke(1);
	protected static final int SYM_BACK_X = 40;
	protected static final int SYM_BACK_Y = 70;
	protected static final Rectangle BOUNDS_BACK = new Rectangle(SYM_BACK_X - 5, SYM_BACK_Y - 5, 60, 60);
	protected static final Rectangle BOUNDS_SCROLL_UP = new Rectangle(230, 526, 50, 50);
	protected static final Rectangle BOUNDS_SCROLL_DOWN = new Rectangle(280, 526, 50, 50);
	protected static final Rectangle BOUNDS_BUY = new Rectangle(510, 152, 100, 50);
	protected static final Rectangle BOUNDS_DISPLAY_SHIPS = new Rectangle(39, 135, 293, 442);
	protected static final Polygon SYMBOL_BACK = new Polygon(new int[] { SYM_BACK_X, 50 + SYM_BACK_X, 50 + SYM_BACK_X },
			new int[] { 25 + SYM_BACK_Y, SYM_BACK_Y, 50 + SYM_BACK_Y }, 3);
	protected static final Polygon SYMBOL_UP = new Polygon(
			new int[] { BOUNDS_SCROLL_UP.x + 10, BOUNDS_SCROLL_UP.x + 40, BOUNDS_SCROLL_UP.x + 25 },
			new int[] { BOUNDS_SCROLL_UP.y + 40, BOUNDS_SCROLL_UP.y + 40, BOUNDS_SCROLL_UP.y + 10 }, 3);
	protected static final Polygon SYMBOL_DOWN = new Polygon(
			new int[] { BOUNDS_SCROLL_DOWN.x + 10, BOUNDS_SCROLL_DOWN.x + 40, BOUNDS_SCROLL_DOWN.x + 25 },
			new int[] { BOUNDS_SCROLL_DOWN.y + 10, BOUNDS_SCROLL_DOWN.y + 10, BOUNDS_SCROLL_DOWN.y + 40 }, 3);
	protected static final int ITEM_POS_X = 50;
	protected static final int ITEM_IMAGE_WIDTH = 75;
	protected static final int ITEM_IMAGE_HEIGHT = 50;
	protected static final int ITEM_HEIGHT = 80;
	protected static final int ITEM_WIDTH = 240;
	protected static final int[] POINTS_X_TITLE_BG_CORNER = new int[] { ITEM_WIDTH + ITEM_POS_X + 4,
			ITEM_WIDTH + ITEM_POS_X + 4 + 30, ITEM_WIDTH + ITEM_POS_X + 4 };
	protected static final int SELECT_IMAGE_WIDTH = 150;
	protected static final int SELECT_IMAGE_HEIGHT = 140;
	// -- Dailog ----------
	protected static final Color COLOR_DIA_BG = new Color(255, 255, 255, 240);
	protected static final Color COLOR_DIA_BORDER = new Color(0, 0, 0, 100);
	protected static final Color COLOR_DIA_TEXT = new Color(0, 0, 0, 180);
	protected static final Font FONT_DIALOG = new Font("Arial", Font.BOLD, 22);
	protected static final Font FONT_DIALOG_OPTION = new Font("Arial", Font.BOLD, 40);
	protected static final Rectangle BOUNDS_DIALOG = new Rectangle(140, 250, 370, 150);
	protected static final int BORDERWIDTH_DIALOG = 8;
	protected static final Stroke STROKE_BUTTON_BORDER = new BasicStroke(5);
	protected static final Rectangle BOUNDS_DIA_YES = new Rectangle(180, 330, 120, 50);
	protected static final Rectangle BOUNDS_DIA_NO = new Rectangle(350, 330, 120, 50);
	private static final String TEXT_DIA = "Exchange ship?";
	// ---

	private static boolean inDialog = false;
	// -- Hovers ----
	private static boolean hoveringBack = false;
	private static boolean hoveringBuy = false;
	private static boolean hoveringDiaYes = false;
	private static boolean hoveringDiaNo = false;
	// hoch, runter
	private static boolean[] hoveringScoll = new boolean[] { false, false };
	// ---
	// -- Scoll ----
	// Falls man mal scrollen muss
	private static float scroll = 0;
	private static float scrollDelta = 0;
	// ---

	private static ShipConfigGraphCalc selectedConfig;
	private static ShipConfigGraphCalc activeConfig;
	private static ArrayList<ShipConfigGraphCalc> availableConfigs = new ArrayList<ShipConfigGraphCalc>();
	private static PaintableStats shownStats;

	public static void open() {
		if (ShipConfigGraphCalc.LINEAR_MODE)
			ShipConfigGraphCalc.scanForStatMaximas();
		availableConfigs.clear();
		for (ShipStartConfig c : ShipStartConfig.getConfigs()) {
			availableConfigs.add(new ShipConfigGraphCalc(c));
		}
		activeConfig = new ShipConfigGraphCalc(MainZap.getPlayer().genConfig());
		selectedConfig = activeConfig;
		shownStats = new PaintableStats(selectedConfig, activeConfig);
	}

	protected static void paint(Graphics2D g) {

		// Linke Seite

		// Antialising deaktivieren
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

		if (hoveringBack) {
			g.setColor(COLOR_HOVER_BACK);
			g.fillRect(BOUNDS_BACK.x, BOUNDS_BACK.y, BOUNDS_BACK.width, BOUNDS_BACK.height);
		}
		g.setColor(ShopMenu.COLOR_SELECT_BORDER);
		g.fillPolygon(SYMBOL_BACK);

		g.clipRect(BOUNDS_DISPLAY_SHIPS.x, BOUNDS_DISPLAY_SHIPS.y, BOUNDS_DISPLAY_SHIPS.width,
				BOUNDS_DISPLAY_SHIPS.height);
		int y = 150 + (int) scroll;
		for (ShipConfigGraphCalc c : availableConfigs) {
			paintItem(g, y, c);
			y += ITEM_HEIGHT;
		}
		g.setClip(null);
		g.setColor(Color.BLACK);
		g.setStroke(STROKE_SHIPS_CUT);
		g.drawLine(BOUNDS_DISPLAY_SHIPS.x, BOUNDS_DISPLAY_SHIPS.y,
				BOUNDS_DISPLAY_SHIPS.x + BOUNDS_DISPLAY_SHIPS.width - 1, BOUNDS_DISPLAY_SHIPS.y);
		g.drawLine(BOUNDS_DISPLAY_SHIPS.x, BOUNDS_DISPLAY_SHIPS.y + BOUNDS_DISPLAY_SHIPS.height, BOUNDS_SCROLL_UP.x - 2,
				BOUNDS_DISPLAY_SHIPS.y + BOUNDS_DISPLAY_SHIPS.height);

		paintScrollButtons(g);

		// -------------------------------------------------------------
		// Rechte Seite
		// Hintergrund und Trennung
		g.setColor(COLOR_STATS_BG);
		g.fillRect(Frame.SIZE / 2 + 15, Shop.Y, 279, Shop.BOUNDS.height);
		g.setColor(COLOR_SITE_BORDER);
		g.setStroke(STROKE_SITE_BORDER);
		g.drawLine(Frame.SIZE / 2 + 15, Shop.Y + 2, Frame.SIZE / 2 + 15, Shop.BOUNDS.height + Shop.Y - 2);

		// Schiffs-Textur
		Rectangle imgBounds = getItemImageBounds(selectedConfig.getConfig().getTexture(), SELECT_IMAGE_WIDTH,
				SELECT_IMAGE_HEIGHT);
		g.setColor(Color.WHITE);
		g.fillRect(350, 65, SELECT_IMAGE_WIDTH, SELECT_IMAGE_HEIGHT);
		g.drawImage(selectedConfig.getConfig().getTexture(), 350 + imgBounds.x, 65 + imgBounds.y, imgBounds.width,
				imgBounds.height, null);

		// - Preis
		// Antialising deaktivieren
		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}
		g.drawImage(IMG_CRYSTAL, 352 + SELECT_IMAGE_WIDTH, 105, IMG_CRYSTAL.getWidth() * 3, IMG_CRYSTAL.getHeight() * 3,
				null);
		// Antialising reaktivieren
		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
		g.setColor(Color.BLACK);
		g.setFont(FONT_STATS_PRICE);
		g.drawString(selectedConfig.getConfig().getPrice() + "", 350 + SELECT_IMAGE_WIDTH + 32, 131);

		// Buy-Button
		if (hoveringBuy) {
			g.setColor(COLOR_BUY_HOVER);
			g.fillRect(BOUNDS_BUY.x, BOUNDS_BUY.y, BOUNDS_BUY.width, BOUNDS_BUY.height);
		}
		g.setColor(COLOR_BUY);
		g.setStroke(STROKE_BUY);
		g.drawRect(BOUNDS_BUY.x, BOUNDS_BUY.y, BOUNDS_BUY.width, BOUNDS_BUY.height);
		g.setFont(FONT_BUY);
		g.drawString("BUY", BOUNDS_BUY.x + 3, BOUNDS_BUY.y + BOUNDS_BUY.height - 7);

		// Name
		g.setColor(Color.BLACK);
		g.setFont(FONT_STATS_TITEL);
		g.drawString(selectedConfig.getConfig().getName(), 350, 232);

		// Beschreibung
		g.setFont(FONT_STATS_DESC);
		selectedConfig.getDescription().paint(g, 352, 251);

		// Statistiken
		try {
			shownStats.paint(g);
		} catch (NullPointerException e) {
			throw new InitializationTimingException("Stats not ready yet!");
		}

		// --------------------------------------------
		// Dialog
		if (inDialog) {
			paintDialog(g);
		}
	}

	private static void paintItem(Graphics2D g, int y, ShipConfigGraphCalc meta) {

		// Rahmen
		g.setColor(COLOR_FRAME_BG);
		g.fillRect(ITEM_POS_X - 10, y - 13, ITEM_WIDTH + 50, ITEM_HEIGHT - 5);
		g.setColor(COLOR_FRAME);
		g.setStroke(STROKE_ITEM_FRAME);
		g.drawRect(ITEM_POS_X - 10, y - 13, ITEM_WIDTH + 50, ITEM_HEIGHT - 5);

		// Bild
		g.setColor(COLOR_IMG_BG);
		g.fillRect(ITEM_POS_X - 2, y - 2, ITEM_IMAGE_WIDTH + 4, ITEM_IMAGE_HEIGHT + 4);
		Rectangle imageBounds = getItemImageBounds(meta.getConfig().getTexture());
		g.drawImage(meta.getConfig().getTexture(), imageBounds.x + ITEM_POS_X, imageBounds.y + y, imageBounds.width,
				imageBounds.height, null);

		// Titel
		g.setColor(COLOR_BG);
		g.fillRect(ITEM_POS_X + ITEM_IMAGE_WIDTH + 4, y - 2, ITEM_WIDTH - ITEM_IMAGE_WIDTH, 30);
		g.fillPolygon(POINTS_X_TITLE_BG_CORNER, new int[] { y - 2, y - 2, y - 2 + 30 }, 3);
		g.setColor(COLOR_FG);
		g.setFont(FONT_NAME);
		g.drawString(meta.getConfig().getName(), ITEM_POS_X + ITEM_IMAGE_WIDTH + 6, y - 2 + 22);

		// Preis
		g.setColor(COLOR_BG);
		g.fillRect(ITEM_POS_X + ITEM_IMAGE_WIDTH + 4, y - 2 + 28 + 4, (int) ((ITEM_WIDTH - ITEM_IMAGE_WIDTH) * 0.8f),
				22);
		// Antialising deaktivieren
		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}
		g.drawImage(IMG_CRYSTAL, ITEM_POS_X + ITEM_IMAGE_WIDTH + 3, y - 2 + 28 + 6,
				(int) (IMG_CRYSTAL.getWidth() * 1.6f), (int) (IMG_CRYSTAL.getHeight() * 1.6f), null);
		// Antialising reaktivieren
		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
		g.setFont(FONT_PRICE);
		g.setColor(Color.BLACK);
		g.drawString(meta.getConfig().getPrice() + "", ITEM_POS_X + ITEM_IMAGE_WIDTH + 21, y - 2 + 50);
	}

	private static void paintDialog(Graphics2D g) {
		g.setColor(COLOR_DIA_BORDER);
		g.fillRect(BOUNDS_DIALOG.x - BORDERWIDTH_DIALOG, BOUNDS_DIALOG.y - BORDERWIDTH_DIALOG,
				BOUNDS_DIALOG.width + 2 * BORDERWIDTH_DIALOG, BOUNDS_DIALOG.height + 2 * BORDERWIDTH_DIALOG);
		g.setColor(COLOR_DIA_BG);
		g.fillRect(BOUNDS_DIALOG.x, BOUNDS_DIALOG.y, BOUNDS_DIALOG.width, BOUNDS_DIALOG.height);

		g.setColor(new Color(0, 0, 0, 80));
		if (hoveringDiaYes) {
			// Maus über YES
			g.fillRect(BOUNDS_DIA_YES.x, BOUNDS_DIA_YES.y, BOUNDS_DIA_YES.width, BOUNDS_DIA_YES.height);
		} else if (hoveringDiaNo) {
			// Maus über NO
			g.fillRect(BOUNDS_DIA_NO.x, BOUNDS_DIA_NO.y, BOUNDS_DIA_NO.width, BOUNDS_DIA_NO.height);
		}

		g.setFont(FONT_DIALOG);
		g.setColor(COLOR_DIA_TEXT);
		g.drawString(TEXT_DIA, BOUNDS_DIALOG.x + 9, BOUNDS_DIALOG.y + 50);

		g.setStroke(STROKE_BUTTON_BORDER);
		g.drawRect(BOUNDS_DIA_YES.x, BOUNDS_DIA_YES.y, BOUNDS_DIA_YES.width, BOUNDS_DIA_YES.height);
		g.drawRect(BOUNDS_DIA_NO.x, BOUNDS_DIA_NO.y, BOUNDS_DIA_NO.width, BOUNDS_DIA_NO.height);

		g.setFont(FONT_DIALOG_OPTION);
		g.drawString("Yes", BOUNDS_DIA_YES.x + 22, BOUNDS_DIA_YES.y + FONT_DIALOG_OPTION.getSize());
		g.drawString("No", BOUNDS_DIA_NO.x + 34, BOUNDS_DIA_NO.y + FONT_DIALOG_OPTION.getSize());

	}

	private static void paintScrollButtons(Graphics2D g) {
		g.setColor(COLOR_SCROLL_BG);
		g.fillRect(BOUNDS_SCROLL_DOWN.x, BOUNDS_SCROLL_DOWN.y, BOUNDS_SCROLL_DOWN.width, BOUNDS_SCROLL_DOWN.height);
		g.fillRect(BOUNDS_SCROLL_UP.x, BOUNDS_SCROLL_UP.y, BOUNDS_SCROLL_UP.width, BOUNDS_SCROLL_UP.height);
		g.setColor(COLOR_SCROLL_BORDER);
		g.setStroke(STROKE_SCROLL_BORDER);
		g.drawRect(BOUNDS_SCROLL_UP.x, BOUNDS_SCROLL_UP.y, BOUNDS_SCROLL_UP.width * 2, BOUNDS_SCROLL_UP.height);
		g.drawLine(BOUNDS_SCROLL_UP.x + BOUNDS_SCROLL_UP.width, BOUNDS_SCROLL_UP.y + 3,
				BOUNDS_SCROLL_UP.x + BOUNDS_SCROLL_UP.width, BOUNDS_SCROLL_UP.y + BOUNDS_SCROLL_UP.height - 3);

		g.setColor(COLOR_SCROLL_HOVER);
		if (hoveringScoll[0]) {
			g.fillRect(BOUNDS_SCROLL_DOWN.x, BOUNDS_SCROLL_DOWN.y, BOUNDS_SCROLL_DOWN.width, BOUNDS_SCROLL_DOWN.height);
		} else if (hoveringScoll[1]) {
			g.fillRect(BOUNDS_SCROLL_UP.x, BOUNDS_SCROLL_UP.y, BOUNDS_SCROLL_UP.width, BOUNDS_SCROLL_UP.height);
		}

		g.setColor(COLOR_SCROLL_FG);
		g.fillPolygon(SYMBOL_UP);
		g.fillPolygon(SYMBOL_DOWN);
	}

	protected static void callClick(int tx, int ty) {

		scrollDelta = 0;

		if (inDialog) {
			if (BOUNDS_DIA_YES.contains(tx, ty)) {
				Mirroring.cancel();
				inDialog = false;
				activeConfig = selectedConfig;
				shownStats.setConfigs(selectedConfig, activeConfig);
				MainZap.getPlayer().applyMeta(selectedConfig.getConfig());
				MainZap.removeCrystals(selectedConfig.getConfig().getPrice());
				ShopSecUpgrade.reset();
				Shop.close();
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
		if (BOUNDS_BUY.contains(tx, ty)) {
			if (selectedConfig.getConfig().getPrice() <= MainZap.getCrystals())
				inDialog = true;
			return;
		}

		if (BOUNDS_DISPLAY_SHIPS.contains(tx, ty)) {
			int y = 150 + (int) scroll;
			for (ShipConfigGraphCalc c : availableConfigs) {
				if (new Rectangle(ITEM_POS_X - 10, y - 13, ITEM_WIDTH + 50, ITEM_HEIGHT - 5).contains(tx, ty)) {
					selectedConfig = c;
					shownStats.setConfigs(selectedConfig, activeConfig);
					return;
				}
				y += ITEM_HEIGHT;
			}
		}
	}

	protected static void callMove(int tx, int ty) {

		if (inDialog) {
			if (BOUNDS_DIA_YES.contains(tx, ty)) {
				hoveringDiaYes = true;
				hoveringDiaNo = false;
			} else if (BOUNDS_DIA_NO.contains(tx, ty)) {
				hoveringDiaYes = false;
				hoveringDiaNo = true;
			}
			hoveringBuy = false;
			hoveringBack = false;
			hoveringScoll[0] = false;
			hoveringScoll[1] = false;
			return;
		}

		if (BOUNDS_BACK.contains(tx, ty)) {
			hoveringBack = true;
			hoveringBuy = false;
			hoveringScoll[0] = false;
			hoveringScoll[1] = false;
			return;
		}

		if (BOUNDS_BUY.contains(tx, ty)) {
			hoveringBuy = true;
			hoveringBack = false;
			hoveringScoll[0] = false;
			hoveringScoll[1] = false;
			return;
		}

		if (BOUNDS_SCROLL_DOWN.contains(tx, ty)) {
			hoveringBuy = false;
			hoveringBack = false;
			hoveringScoll[0] = true;
			hoveringScoll[1] = false;
			return;
		}

		if (BOUNDS_SCROLL_UP.contains(tx, ty)) {
			hoveringBuy = false;
			hoveringBack = false;
			hoveringScoll[0] = false;
			hoveringScoll[1] = true;
			return;
		}

		// Maus über Statistik?
		shownStats.callMove(tx, ty);

		hoveringBuy = false;
		hoveringBack = false;
		hoveringScoll[0] = false;
		hoveringScoll[1] = false;

	}

	protected static void callPress(int tx, int ty) {
		if (BOUNDS_SCROLL_DOWN.contains(tx, ty)) {
			scrollDelta = -SCROLL_SPEED;
			return;
		}

		if (BOUNDS_SCROLL_UP.contains(tx, ty)) {
			scrollDelta = SCROLL_SPEED;
			return;
		}
	}

	private static Rectangle getItemImageBounds(BufferedImage img) {

		if (((float) img.getWidth() / (float) img.getHeight()) >= ((float) ITEM_IMAGE_WIDTH
				/ (float) ITEM_IMAGE_HEIGHT)) {
			// zu breit

			int x = 0;
			int width = ITEM_IMAGE_WIDTH;
			float height = ((float) img.getHeight() / (float) img.getWidth()) * width;
			int y = (int) ((ITEM_IMAGE_HEIGHT - height) / 2.0f);
			return new Rectangle(x, y, width, (int) height);
		} else if (((float) img.getHeight() / (float) img.getWidth()) >= ((float) ITEM_IMAGE_HEIGHT
				/ (float) ITEM_IMAGE_WIDTH)) {
			// zu hoch

			int y = 0;
			int height = ITEM_IMAGE_HEIGHT;
			float width = ((float) img.getWidth() / (float) img.getHeight()) * height;
			int x = (int) ((ITEM_IMAGE_WIDTH - width) / 2.0f);
			return new Rectangle(x, y, (int) width, height);
		}

		// Sollte nicht passieren. Sonst is was falsch
		System.err.println("Returning null. This should not happen.");
		System.err.println(
				img.getWidth() + " : " + img.getHeight() + " - " + ITEM_IMAGE_WIDTH + " : " + ITEM_IMAGE_HEIGHT);
		return null;
	}

	public static Rectangle getItemImageBounds(BufferedImage img, float width, float height) {

		if (((float) img.getWidth() / (float) img.getHeight()) >= (width / height)) {
			// zu breit

			int x = 0;
			int w = (int) width;
			float h = ((float) img.getHeight() / (float) img.getWidth()) * w;
			int y = (int) ((height - h) / 2.0f);
			return new Rectangle(x, y, w, (int) h);
		} else if (((float) img.getHeight() / (float) img.getWidth()) >= ((float) height / width)) {
			// zu hoch

			int y = 0;
			int h = (int) height;
			float w = ((float) img.getWidth() / (float) img.getHeight()) * h;
			int x = (int) ((width - w) / 2.0f);
			return new Rectangle(x, y, (int) w, h);
		}

		// Sollte nicht passieren. Sonst is was falsch
		new RuntimeException("Returning null. This should not happen.").printStackTrace();
		System.err.println(img.getWidth() + " : " + img.getHeight() + " - " + width + " : " + height);
		return null;
	}

	protected static void update() {

		if ((ShipStartConfig.getConfigs().size() * ITEM_HEIGHT) < BOUNDS_DISPLAY_SHIPS.height)
			return; // Nix zu scrollen

		scroll += scrollDelta;

		if (scroll > 0) {
			scroll = 0;
		} else if (scroll < -((ShipStartConfig.getConfigs().size() * ITEM_HEIGHT) - BOUNDS_DISPLAY_SHIPS.height - 1)) {
			scroll = -((ShipStartConfig.getConfigs().size() * ITEM_HEIGHT) - BOUNDS_DISPLAY_SHIPS.height - 1);
		}
	}

}
