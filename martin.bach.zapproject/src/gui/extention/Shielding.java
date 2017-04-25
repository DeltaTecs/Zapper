package gui.extention;

import java.awt.Color;
import java.awt.Graphics2D;

import corecase.MainZap;
import gui.Frame;
import library.ScheduledList;

public class Shielding {

	private static final int MAX_DURATION = MainZap.inTicks(15000); // 15 Sek
	private static final byte SHIPDISTANCE = 27;
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

		// ConcurrentModExc. vermeiden, da Paintthread vom�glich gerade updaten
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

		// Ben�tigt 0/0 Frame Kontext

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

		// ConcurrentModExc. vermeiden, da CalcThread vom�glich gerade updaten
		// will
		synchronized (absoptions) {
			absoptions.update();

			for (ShieldAbsorptionEffect e : absoptions) { // Einsch�sse zeichnen
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

		int textLength;
		if (MainZap.getPlayer().getTexture().getWidth() > MainZap.getPlayer().getTexture().getHeight())
			textLength = MainZap.getPlayer().getTexture().getWidth();
		else
			textLength = MainZap.getPlayer().getTexture().getHeight();

		radian = (int) (textLength * MainZap.getPlayer().getTextureScale() * 0.5f) + SHIPDISTANCE;

		duration = MAX_DURATION;
		active = true;
		visible = true;
		absoptions.update();
		absoptions.clear();
		MainZap.getPlayer().setShielded(true);
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
