package gui.extention;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import corecase.MainZap;
import gui.Frame;
import lib.ScheduledList;

public class Shielding {

	private static final int MAX_DURATION = MainZap.inTicks(15000); // 15 Sek
	private static final byte SHIPDISTANCE = 4;
	private static final float FLICKER_TIME_PERCENTAGE = 0.2f;
	private static final Color COLOR_SHIELD_BG = new Color(56, 164, 181, 35);
	protected static final Color COLOR_SHIELD_FG = new Color(56, 164, 181, 198);

	private static ScheduledList<ShieldAbsorptionEffect> absoptions = new ScheduledList<ShieldAbsorptionEffect>();
	private static int duration = MAX_DURATION;
	private static int radian;
	private static boolean active = false;
	private static boolean visible = true;

	public static void update() {

		if (!active)
			return;

		duration--;
		if (duration <= 0) {
			cancel();
			return;
		}

		// ConcurrentModExc. vermeiden, da Paintthread vomöglich gerade updaten
		// will
		synchronized (absoptions) {
			absoptions.update();
			for (ShieldAbsorptionEffect e : absoptions) {
				e.update();
				if (e.faded())
					absoptions.schedRemove(e);
			}
			absoptions.update();
		}

		if (duration <= (MAX_DURATION * FLICKER_TIME_PERCENTAGE))
			visible = !(MainZap.rand(4) == 0);

	}

	public static void paint(Graphics2D g) {

		if (!active || !visible)
			return;

		// Benötigt 0/0 Frame Kontext

		g.translate(Frame.HALF_SCREEN_SIZE, Frame.HALF_SCREEN_SIZE);

		if (MainZap.fancyGraphics) {
			g.setColor(COLOR_SHIELD_BG);
			g.fillOval(-radian, -radian, 2 * radian, 2 * radian);
			g.setColor(COLOR_SHIELD_FG);
			g.drawOval(-radian, -radian, 2 * radian, 2 * radian);
		} else {
			g.setColor(COLOR_SHIELD_BG);
			g.fillRect(-radian, -radian, 2 * radian, 2 * radian);
			g.setColor(COLOR_SHIELD_FG);
			g.drawRect(-radian, -radian, 2 * radian, 2 * radian);
		}

		// ConcurrentModExc. vermeiden, da CalcThread vomöglich gerade updaten
		// will
		synchronized (absoptions) {
			absoptions.update();

			for (ShieldAbsorptionEffect e : absoptions) { // Einschüsse zeichnen
				g.setColor(new Color(COLOR_SHIELD_FG.getRed(), COLOR_SHIELD_FG.getGreen(), COLOR_SHIELD_FG.getBlue(),
						e.getAlpha()));
				if (MainZap.fancyGraphics)
					g.fillOval(e.getDx() - e.getRadian(), e.getDy() - e.getRadian(), 2 * e.getRadian(),
							2 * e.getRadian());
				else
					g.fillRect(e.getDx() - e.getRadian(), e.getDy() - e.getRadian(), 2 * e.getRadian(),
							2 * e.getRadian());

			}

		}

		g.translate(-Frame.HALF_SCREEN_SIZE, -Frame.HALF_SCREEN_SIZE);

	}

	public static void activate() {

		radian = calcShieldRange(MainZap.getPlayer().getTexture(), MainZap.getPlayer().getTextureScale());

		duration = MAX_DURATION;
		active = true;
		visible = true;
		absoptions.update();
		absoptions.clear();
		MainZap.getPlayer().setShielded(true);
	}

	private static int calcShieldRange(BufferedImage img, float scale) {

		float hWidth = img.getWidth() * scale * 0.5f; // halbe Weite
		float hHeight = img.getHeight() * scale * 0.5f;

		return SHIPDISTANCE + (int) Math.sqrt((hWidth * hWidth) + (hHeight * hHeight));
	}

	public static void inboundProjectile(int tx, int ty) {
		absoptions.schedAdd(
				new ShieldAbsorptionEffect(tx - MainZap.getPlayer().getLocX(), ty - MainZap.getPlayer().getLocY()));
	}

	public static void cancel() {
		ExtentionManager.setBlocked(false);
		active = false;
		absoptions.schedClear();
		MainZap.getPlayer().setShielded(false);
	}

}
