package gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import battle.stage.StageManager;
import corecase.MainZap;
import gui.extention.ExtentionManager;
import gui.shop.Shop;
import io.TextureBuffer;
import lib.ClickListener;
import lib.ClickableObject;
import lib.PaintingTask;

public abstract class Hud {

	public static final int[] COLOR_SCORE = new int[] { 15, 17, 25, 130 };
	public static final Font FONT_BIG = new Font("Arial", Font.BOLD, 19);
	private static final BufferedImage LVL_UP_TEXTURE_R = TextureBuffer.get(TextureBuffer.NAME_BUTTON_LVL_UP_ROUND);
	private static final BufferedImage LVL_UP_TEXTURE_C = TextureBuffer.get(TextureBuffer.NAME_BUTTON_LVL_UP_CORNER);
	private static final float LVL_UP_HIDDEN_ALPHA = 0.2f;
	private static final Color COLOR_SHOP = new Color(0, 0, 93, 180);
	private static final Stroke STROKE_SHOP = new BasicStroke(5);
	private static final Font FONT_SHOP = new Font("Arial", Font.BOLD, 36);
	private static final int SPACE_Y_SHOP = 30;
	private static final int SPACE_X_SHOP = 5;
	private static final int WIDTH_SHOP = 150;
	private static final Polygon OUTLINE_SHOP = new Polygon(new int[] { 0, WIDTH_SHOP, WIDTH_SHOP, 40 },
			new int[] { 0, 0, 40, 40 }, 4);
	private static final float SHOP_BLEND_SPEED = 4;
	private static final Rectangle CLICK_WARP_AREA = new Rectangle(547, 580, 100, 69);
	private static final Rectangle CLICK_OPENSHOP_AREA = new Rectangle(Frame.SIZE - 160, 25, 160, 50);

	private static float alphaScore = COLOR_SCORE[3];
	private static float alphaCrystals = alphaScore;
	private static int lvlUpSignVisibleTime = MainZap.inTicks(800);
	private static int lvlUpSignStatusTime = 0;
	private static boolean lvlUpSignVisible = false;
	private static ClickableObject clickObj;
	private static int blendAlpha = 0;
	private static boolean shopIconVisible = false;
	private static float shopIconX = 0;
	private static float shopBackgroundLength = WIDTH_SHOP;

	public static void setUpClickListener() {

		clickObj = new ClickableObject(0, 0, Frame.SIZE, Frame.SIZE);
		clickObj.setVisible(true);
		clickObj.addClickListener(new ClickListener() {

			@Override
			public void release(int dx, int dy) {

				if (Shop.isOpen())
					return;

				if (CLICK_WARP_AREA.contains(dx, dy)) {
					// next lvl
					if (!StageManager.getActiveStage().isPassed())
						return; // Noch nicht durch!

					StageManager.jumpToNextStage();
					return;
				}

				if (CLICK_OPENSHOP_AREA.contains(dx, dy) && Shop.isAvailable()) {
					Shop.open();
				}
			}

			@Override
			public void press(int dx, int dy) {
			}

		});

		MainZap.getFrame().addClickable(clickObj);
	}

	private static final PaintingTask PAINTING_TASK = new PaintingTask() {
		@Override
		public void paint(Graphics2D g) {

			if (MainZap.getPlayer().isWarping()) {
				if (blendAlpha != 0) {
					if (blendAlpha > 255)
						blendAlpha = 255;
					g.setColor(new Color(255, 255, 255, blendAlpha));
					g.fillRect(0, 0, Frame.SIZE, Frame.SIZE);
				}
				return;
			}

			if (StageManager.getActiveStage() == null || !MainZap.getPlayer().isAlive())
				return; // noch nicht initialisiert, oder tot, oder im warp

			// ----- Spieler - Score
			g.setColor(new Color(COLOR_SCORE[0], COLOR_SCORE[1], COLOR_SCORE[2], (int) alphaScore));
			g.setFont(FONT_BIG);
			g.drawString("s: " + MainZap.getScore() + " (x" + StageManager.getActiveStage().getLvl() + ")", 6,
					Frame.SIZE - 6);

			// ----- Spieler - Knette
			if (MainZap.generalAntialize) {
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			}
			g.drawImage(Crystal.TEXTURE, 230, 625, 22, 22, null);
			if (MainZap.generalAntialize) {
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			}
			g.setColor(new Color(COLOR_SCORE[0], COLOR_SCORE[1], COLOR_SCORE[2], (int) alphaCrystals));
			g.drawString(": " + MainZap.getCrystals(), 250, 643);

			// ----- NextStage - Button
			if (StageManager.getActiveStage().isPassed()) {

				if (lvlUpSignVisible) {
					if (MainZap.roundCorners) {
						g.drawImage(LVL_UP_TEXTURE_R, 547, 578, 100, 69, null);
					} else {
						g.drawImage(LVL_UP_TEXTURE_C, 547, 578, 100, 69, null);
					}
				}

			} else {

				Composite storeComp = g.getComposite();
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, LVL_UP_HIDDEN_ALPHA));
				if (MainZap.roundCorners) {
					g.drawImage(LVL_UP_TEXTURE_R, 547, 580, 100, 69, null);
				} else {
					g.drawImage(LVL_UP_TEXTURE_C, 547, 580, 100, 69, null);
				}
				g.setComposite(storeComp);

			}

			// --------- Shop-Schild
			if (shopIconX != 0) {

				int dx = (int) shopIconX + Frame.SIZE;
				g.translate(dx, SPACE_Y_SHOP);

				g.clipRect(WIDTH_SHOP - (int) shopBackgroundLength, 0, (int) shopBackgroundLength, 1000);

				g.setColor(new Color(COLOR_SHOP.getRed(), COLOR_SHOP.getGreen(), COLOR_SHOP.getBlue(), 120));
				g.fillPolygon(OUTLINE_SHOP);
				g.setClip(null);

				g.setStroke(STROKE_SHOP);
				g.setColor(COLOR_SHOP);
				g.setFont(FONT_SHOP);

				g.drawPolygon(OUTLINE_SHOP);
				g.drawString("SHOP", 40, 34);

				g.translate(-dx, -SPACE_Y_SHOP);
			}
			// ----

			// --------- Fähigkeits-Aktivierungs-Knopf
			ExtentionManager.paint(g);

			// --------- DEBUG Stuff
			if (MainZap.debug) {
				// FPS-Anzeige
				g.setColor(Color.BLACK);
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("FPS: " + MainZap.getFps(), 150, 50);
				String s = " E. reg.: " + (MainZap.getMap().getForegroundPaintElements().size()
						+ MainZap.getMap().getBackgroundPaintElements().size());
				g.drawString(s, 150, 70);

			}

		}
	};

	public static void pushScore() {
		alphaScore += 50;
		if (alphaScore > 255) {
			alphaScore = 255;
		}
	}

	public static void pushCrystals(int amount) {
		alphaCrystals += 10 * amount;
		if (alphaCrystals > 255) {
			alphaCrystals = 255;
		}
	}

	public static void pushBlending() {
		blendAlpha += 5;
	}

	public static void setBlending(int a) {
		blendAlpha = a;
	}

	public static void update() {

		if (StageManager.getActiveStage() == null)
			return; // Noch nicht initialisiert

		// -- Score ---------
		if (alphaScore > COLOR_SCORE[3]) {
			alphaScore -= (alphaScore - COLOR_SCORE[3]) / 20.0f;
		} else if (alphaScore < COLOR_SCORE[3]) {
			alphaScore = COLOR_SCORE[3];
		}
		// ---

		// -- Crystals -------
		if (alphaCrystals > COLOR_SCORE[3]) {
			alphaCrystals -= (alphaCrystals - COLOR_SCORE[3]) / 20.0f;
		} else if (alphaCrystals < COLOR_SCORE[3]) {
			alphaCrystals = COLOR_SCORE[3];
		}
		// ---

		// -- Warp-Button -----
		if (StageManager.getActiveStage().isPassed()) {
			if (lvlUpSignStatusTime >= lvlUpSignVisibleTime) {
				lvlUpSignVisible = !lvlUpSignVisible;
				lvlUpSignStatusTime = 0;
			} else {
				lvlUpSignStatusTime++;
			}
		}
		// ---

		// -- Shop -----------
		if (shopBackgroundLength >= WIDTH_SHOP + 5000) {
			shopBackgroundLength = 1;
		} else {
			shopBackgroundLength *= 1.05f;
		}
		// Einblendung
		if (shopIconVisible) {
			if (shopIconX <= -WIDTH_SHOP - SPACE_X_SHOP) {
				shopIconX = -WIDTH_SHOP - SPACE_X_SHOP;
			} else {
				shopIconX -= SHOP_BLEND_SPEED;
			}
		} else {
			if (shopIconX >= 0) {
				shopIconX = 0;
			} else {
				shopIconX += SHOP_BLEND_SPEED;
			}
		}

		// -- Fähigkeit-UI -------------
		ExtentionManager.update();
		// ---

	}

	public static PaintingTask getPaintingTask() {
		return PAINTING_TASK;
	}

	public static boolean isShopIconVisible() {
		return shopIconVisible;
	}

	public static void setShopIconVisible(boolean shopIconVisible) {
		Hud.shopIconVisible = shopIconVisible;
	}

	public static boolean clickInShopButton(int tx, int ty) {
		return CLICK_OPENSHOP_AREA.contains(tx, ty);
	}

}
