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

import battle.projectile.Projectile;
import battle.projectile.ProjectileDesign;
import battle.stage.StageManager;
import battle.stage._12.DeltaDummy;
import corecase.MainZap;
import gui.extention.ExtentionManager;
import gui.shop.Shop;
import io.TextureBuffer;
import lib.ClickListener;
import lib.ClickableObject;
import lib.PaintingTask;
import lib.SpeedVector;

public abstract class Hud {

	public static final int[] COLOR_SCORE = new int[] { 15, 17, 25, 130 };
	public static final Font FONT_BIG = new Font("Arial", Font.BOLD, 19);
	private static final BufferedImage LVL_UP_TEXTURE_R = TextureBuffer.get(TextureBuffer.NAME_BUTTON_LVL_UP_ROUND);
	private static final BufferedImage LVL_UP_TEXTURE_C = TextureBuffer.get(TextureBuffer.NAME_BUTTON_LVL_UP_CORNER);
	private static final float LVL_UP_HIDDEN_ALPHA = 0.2f;
	private static final float TP_BLEND_ALPHA_REMOVE = 2.0f;
	private static final Color COLOR_SHOP = new Color(0, 0, 93, 180);
	private static final Color COLOR_STAGE_PASS_EFFECT = new Color(76, 132, 0, 120);
	private static final Stroke STROKE_SHOP = new BasicStroke(5);
	private static final Font FONT_SHOP = new Font("Arial", Font.BOLD, 36);
	private static final int SPACE_Y_SHOP = 30;
	private static final int SPACE_X_SHOP = 5;
	private static final int WIDTH_SHOP = 150;
	private static final int BLACK_BLEND_DURATION = MainZap.inTicks(150);
	private static final int BLACK_BLEND_MAX_ALPHA = 220;
	private static final float STAGE_PASS_EFFECT_DX = 15;
	private static final float BLACK_BLEND_SPEED = 12.0f;
	private static final Polygon OUTLINE_SHOP = new Polygon(new int[] { 0, WIDTH_SHOP, WIDTH_SHOP, 40 },
			new int[] { 0, 0, 40, 40 }, 4);
	private static final float SHOP_BLEND_SPEED = 4;
	private static final Rectangle CLICK_WARP_AREA = new Rectangle(Frame.SIZE - 248, Frame.SIZE - 70, 245, 69);
	private static final Rectangle CLICK_OPENSHOP_AREA = new Rectangle(Frame.SIZE - 160, 25, 160, 50);

	private static float alphaScore = COLOR_SCORE[3];
	private static float alphaCrystals = alphaScore;
	private static float alphaBlackBlend = 0;
	private static int durationBlackBlendRemaining = 0;
	private static int lvlUpSignVisibleTime = MainZap.inTicks(800);
	private static int lvlUpSignStatusTime = 0;
	private static boolean lvlUpSignVisible = false;
	private static ClickableObject clickObj;
	private static int levelSwitchBlendAlpha = 0;
	private static float tpBlendAlpha = 0;
	private static HudLightningEffect lightningEffect = null;
	private static boolean shopIconVisible = false;
	private static float shopIconX = 0;
	private static float shopBackgroundLength = WIDTH_SHOP;
	private static float posStagePassEffect = -1;
	private static boolean stagePassEffectFinished = true;

	private static boolean switchBooleanForShieldBlackBlend = false;

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
					if (!StageManager.getActiveStage().isPassed() || !MainZap.getPlayer().isAlive())
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

			// Warp-Effect
			if (MainZap.getPlayer().isWarping()) {
				if (levelSwitchBlendAlpha != 0) {
					if (levelSwitchBlendAlpha > 255)
						levelSwitchBlendAlpha = 255;
					g.setColor(new Color(255, 255, 255, levelSwitchBlendAlpha));
					g.fillRect(0, 0, Frame.SIZE, Frame.SIZE);
				}
				return;
			}

			// Stage-Pass-Effect
			if (!stagePassEffectFinished && StageManager.getActiveStage().getLvl() != 12) {
				g.setColor(COLOR_STAGE_PASS_EFFECT);
				g.fillRect((int) (posStagePassEffect), (int) (posStagePassEffect),
						Frame.SIZE - (int) (posStagePassEffect), Frame.SIZE - (int) (posStagePassEffect));
			}

			// -- Schwarze Blende, letzter Boss
			if (alphaBlackBlend > 0) {
				// halb so durchsichtig, wenn shielded
				g.setColor(new Color(0, 0, 0,
						(MainZap.getPlayer().isShielded()) ? (int) (alphaBlackBlend / 2.0f) : (int) (alphaBlackBlend)));
				g.fillRect(0, 0, Frame.SIZE + 5, Frame.SIZE + 5);
			}
			// ---

			if (StageManager.getActiveStage() == null || !MainZap.getPlayer().isAlive())
				return; // noch nicht initialisiert, oder
						// tot, oder im warp

			// ----- Hit-Indicator
			if (MainZap.fancyGraphics)
				PlayerDamageIndicator.paint(g);

			// ---- Bloody-Screen
			BloodyScreen.paint(g);

			// ----- Spieler - Score
			g.setColor(new Color(COLOR_SCORE[0], COLOR_SCORE[1], COLOR_SCORE[2], (int) alphaScore));
			g.setFont(FONT_BIG);
			g.drawString(MainZap.getScore() + " x" + StageManager.getActiveStage().getLvl() + "", 6, Frame.SIZE - 7);

			// ----- Spieler - Knette
			if (MainZap.generalAntialize) {
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			}
			g.drawImage(Crystal.TEXTURE, 100, Frame.SIZE - 25, 22, 22, null);
			if (MainZap.generalAntialize) {
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			}
			g.setColor(new Color(COLOR_SCORE[0], COLOR_SCORE[1], COLOR_SCORE[2], (int) alphaCrystals));
			g.drawString(MainZap.getCrystals() + "", 120, Frame.SIZE - 7);

			// ----- NextStage - Button
			if (StageManager.getActiveStage().getLvl() != 12) {
				if (StageManager.getActiveStage().isPassed()) {

					if (lvlUpSignVisible) {
						if (MainZap.roundCorners) {
							g.drawImage(LVL_UP_TEXTURE_R, Frame.SIZE - 248, Frame.SIZE - 72, 245, 69, null);
						} else {
							g.drawImage(LVL_UP_TEXTURE_C, Frame.SIZE - 248, Frame.SIZE - 72, 245, 69, null);
						}
					}

				} else {

					Composite storeComp = g.getComposite();
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, LVL_UP_HIDDEN_ALPHA));
					if (MainZap.roundCorners) {
						g.drawImage(LVL_UP_TEXTURE_R, Frame.SIZE - 248, Frame.SIZE - 70, 245, 69, null);
					} else {
						g.drawImage(LVL_UP_TEXTURE_C, Frame.SIZE - 248, Frame.SIZE - 70, 245, 69, null);
					}
					g.setComposite(storeComp);

				}
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

			// ---------- Mögliche Blende durch Portal Effekt (lvl 11)
			if (tpBlendAlpha > 3.0f) {
				g.setColor(new Color(255, 255, 255, (int) tpBlendAlpha));
				g.fillRect(0, 0, Frame.SIZE, Frame.SIZE);
			}

			// ----------- Blitze von Portal Effekt (lvl 11)
			if (lightningEffect != null && MainZap.fancyGraphics)
				lightningEffect.paint(g);

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

	public static void update() {

		if (StageManager.getActiveStage() == null)
			return; // Noch nicht initialisiert

		// ----- Stage-Pass-Effect
		if (!stagePassEffectFinished) {
			if (posStagePassEffect < Frame.SIZE - 80)
				posStagePassEffect += STAGE_PASS_EFFECT_DX;
			else
				stagePassEffectFinished = true;
		}

		// ---

		// ----- Hit-Indicator
		if (MainZap.fancyGraphics)
			PlayerDamageIndicator.update();

		// ----- Bloody-Screen
		BloodyScreen.update();

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

		// --- Portal-Effekt-Blende -----
		if (tpBlendAlpha > 0) {
			tpBlendAlpha -= TP_BLEND_ALPHA_REMOVE;
			if (tpBlendAlpha < 0)
				tpBlendAlpha = 0;
		}

		// --- Blitze von Portal Effekt (lvl 11)
		if (lightningEffect != null && MainZap.fancyGraphics)
			lightningEffect.update();

		// --- Black Blende Letzter Boss --
		if (durationBlackBlendRemaining == 0) {
			if (alphaBlackBlend > 0) {
				alphaBlackBlend -= BLACK_BLEND_SPEED;
			}
			if (alphaBlackBlend < 0)
				alphaBlackBlend = 0;
		} else {
			// Projectil-einschlag
			if (!MainZap.getPlayer().isShielded()) {
				Projectile pseudoProj = new Projectile(1000, new ProjectileDesign(2, false, Color.BLACK),
						DeltaDummy.DUMMY_DAMAGE * 10);
				pseudoProj.setVelocity(
						new SpeedVector((int) (1000 * Math.random()) - 500, (int) (1000 * Math.random()) - 500));
				PlayerDamageIndicator.register(pseudoProj);
			} else {
				if (switchBooleanForShieldBlackBlend) { // Für halb so viele Einschläge
					switchBooleanForShieldBlackBlend = false;
					Projectile pseudoProj = new Projectile(1000, new ProjectileDesign(2, false, Color.BLACK),
							DeltaDummy.DUMMY_DAMAGE_SHIELDED * 10);
					pseudoProj.setVelocity(
							new SpeedVector((int) (1000 * Math.random()) - 500, (int) (1000 * Math.random()) - 500));
					PlayerDamageIndicator.register(pseudoProj);
				} else
					switchBooleanForShieldBlackBlend = true;
			}
			durationBlackBlendRemaining--;
		}

	}

	public static void resetStagePassEffect() {
		posStagePassEffect = 100;
		stagePassEffectFinished = false;
	}

	public static void pushBlackBlending() {
		alphaBlackBlend = BLACK_BLEND_MAX_ALPHA;
		durationBlackBlendRemaining = BLACK_BLEND_DURATION;
	}

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
		levelSwitchBlendAlpha += 5;
	}

	public static void resetPortalBlending() {
		tpBlendAlpha = 250;
	}

	public static void setBlending(int a) {
		levelSwitchBlendAlpha = a;
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

	public static HudLightningEffect getLightningEffect() {
		return lightningEffect;
	}

	public static void setLightningEffect(HudLightningEffect lightningEffect) {
		Hud.lightningEffect = lightningEffect;
	}

}
