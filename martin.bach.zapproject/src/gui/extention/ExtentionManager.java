package gui.extention;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import corecase.MainZap;
import gui.Frame;
import gui.screens.pause.PauseScreen;
import gui.shop.Shop;
import io.TextureBuffer;
import lib.ClickListener;
import lib.ClickableObject;

// Schnittstelle fürs HUD. Erweiterungs-Button (Fähigkeit) und so
public abstract class ExtentionManager {

	// Mirror, Shield, Shock
	private static final int[] COOLDOWNS = new int[] { MainZap.inTicks(40000), MainZap.inTicks(30000),
			MainZap.inTicks(45000) };
	private static final BufferedImage[] TEXTURES = new BufferedImage[] {
			TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_MIRROR),
			TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_SHIELD),
			TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_SHOCK) };
	private static final int ROUND_DEEPTH = 8;
	private static final int POS_BG_X = Frame.SIZE - 103;
	private static final int POS_BG_Y = Frame.SIZE - 178;
	private static final int POS_FG_D = 8;
	private static final int BG_SIZE = 100;
	private static final int FG_SIZE = 84;
	private static final Color COLOR_BG = new Color(0, 0, 0, 30);
	private static final Color COLOR_FG_SHADOW = new Color(0, 0, 0, 100);
	private static final float ALPHA_TOTAL_SHADOW = 0.8f;
	private static final float ALPHA_TOTAL_BLOCKED = 0.3f;

	private static Extention extention = null;
	private static int currentCooldown;
	private static int currentMaxCooldown;
	private static float readyAlpha = 1;
	private static float readyAlphaDelta = 0.02f;
	private static BufferedImage image;
	private static boolean ready = false;
	private static boolean blocked = true;

	public static void setUpClickListener() {

		ClickableObject clickable = new ClickableObject(POS_BG_X, POS_BG_Y, BG_SIZE, BG_SIZE);
		clickable.setVisible(true);

		clickable.addClickListener(new ClickListener() {

			@Override
			public void release(int dx, int dy) {
				if (!PauseScreen.isOpen() && !Shop.isOpen())
					requestActivation();
			}

			@Override
			public void press(int dx, int dy) {
			}
		});

		MainZap.getFrame().addClickable(clickable);

	}

	public static void paint(Graphics2D g) {

		if (extention == null)
			return; // Nix ausgewählt

		if (ready) {

			Composite storeComp = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, readyAlpha));

			g.setColor(COLOR_BG);
			if (MainZap.roundCorners) {
				g.fillRoundRect(POS_BG_X, POS_BG_Y, BG_SIZE, BG_SIZE, ROUND_DEEPTH, ROUND_DEEPTH);
			} else {
				g.fillRect(POS_BG_X, POS_BG_Y, BG_SIZE, BG_SIZE);
			}
			drawImage(g);

			g.setComposite(storeComp);

		} else {

			Composite storeComp = g.getComposite();

			if (blocked)
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_TOTAL_BLOCKED));
			else
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_TOTAL_SHADOW));

			g.setColor(COLOR_BG);
			if (MainZap.roundCorners) {
				g.fillRoundRect(POS_BG_X, POS_BG_Y, BG_SIZE, BG_SIZE, ROUND_DEEPTH, ROUND_DEEPTH);
			} else {
				g.fillRect(POS_BG_X, POS_BG_Y, BG_SIZE, BG_SIZE);
			}
			drawImage(g);

			g.setComposite(storeComp);

			if (blocked)
				return;

			g.clipRect(POS_BG_X,
					POS_BG_Y + (BG_SIZE - (int) (BG_SIZE * ((float) currentCooldown / currentMaxCooldown))), BG_SIZE,
					BG_SIZE);
			g.setColor(COLOR_FG_SHADOW);
			if (MainZap.roundCorners) {
				g.fillRoundRect(POS_BG_X, POS_BG_Y, BG_SIZE, BG_SIZE, ROUND_DEEPTH, ROUND_DEEPTH);
			} else {
				g.fillRect(POS_BG_X, POS_BG_Y, BG_SIZE, BG_SIZE);
			}
			g.setClip(null);
		}

	}

	private static void drawImage(Graphics2D g) {
		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g.drawImage(image, POS_BG_X + POS_FG_D, POS_BG_Y + POS_FG_D, FG_SIZE, FG_SIZE, null);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		} else {
			g.drawImage(image, POS_BG_X + POS_FG_D, POS_BG_Y + POS_FG_D, FG_SIZE, FG_SIZE, null);
		}
	}

	public static void update() {

		if (extention == null)
			return; // Nix ausgewählt

		// Alpha
		readyAlpha += readyAlphaDelta;
		if (readyAlpha >= 1) {
			readyAlpha = 1;
			readyAlphaDelta *= -1;
		} else if (readyAlpha <= 0.4f) {
			readyAlpha = 0.4f;
			readyAlphaDelta *= -1;
		}

		// Cooldown
		if (ready || blocked) // Schon auf 0 oder noch am worken
			return;
		currentCooldown--;
		if (currentCooldown <= 0) {
			ready = true;
			readyAlpha = 1;
			readyAlphaDelta = 0.02f;
		}
	}

	private static void requestActivation() {

		if (blocked || !ready || extention == null)
			return;

		// Bereit und nicht geblockt und eine Fähigkeit ausgewählt
		switch (extention) {
		case MIRROR:
			blocked = true;
			Mirroring.activate();
			break;
		case SHIELD:
			blocked = true;
			Shielding.activate();
			break;
		case SHOCK:
			Shocking.activate();
			break;
		default: // Braucht man nicht !
			break;
		}

		ready = false;
		currentCooldown = currentMaxCooldown;
	}

	public static boolean isBlocked() {
		return blocked;
	}

	public static void setBlocked(boolean blocked) {
		ExtentionManager.blocked = blocked;
	}

	public static Extention getExtention() {
		return extention;
	}

	public static void setExtention(Extention extention) {
		ExtentionManager.extention = extention;
		ready = false;
		blocked = false;

		if (extention == null)
			return;

		ready = true;
		currentMaxCooldown = COOLDOWNS[extention.ordinal()];
		currentCooldown = 0;
		image = TEXTURES[extention.ordinal()];
	}

}
