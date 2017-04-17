package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import corecase.MainZap;
import library.PaintingTask;
import library.ScheduledList;
import library.Updateable;

public class PlayerHpBar implements PaintingTask, Updateable {

	private static final float WARNING_BORDER = 0.3f; // Warnung ab 30% HP
	private static final int WARNING_BORDER_REACH = 6;
	private static final int ROUND_DEEPTH = 4;
	private static final int PULSE_LENGTH = 100;
	private static final int BAR_WIDTH = 570;
	private static final int BAR_POS = 70;
	private static final Color COLOR_BACKGROUND = new Color(174, 1, 2, 50);
	private static final Color COLOR_FOREGROUND = new Color(221, 22, 10, 100);

	public static boolean drawRoundedBars = true;

	private ScheduledList<int[]> pulses = new ScheduledList<int[]>();
	private int dmgAlpha = 0;
	private int maxHp;
	private int hp;

	// -- Hp-Warnung --------
	private float generalHpWarnAlphaFac = 1.0f;
	private float generalHpWarnAlphaFacDelta = 0.03f;
	// ---

	public PlayerHpBar(int max) {
		hp = max;
		maxHp = max;
	}

	public void remove(int d) {
		dmgAlpha = 60;
		hp -= d;
	}

	public void add(int h) {
		if ((long) h + (long) hp > maxHp) // Overdrive?
			h = maxHp - hp;

		pulses.schedAdd(new int[] { PULSE_LENGTH, h });
	}

	@Override
	public void paint(Graphics2D g) {

		if (hp <= (int) (maxHp * WARNING_BORDER))
			// Hp-Warnung aktiv
			paintHpWarning(g);

		// Reguläre Hp-Bar

		g.clipRect(BAR_POS, 6, BAR_WIDTH, 10);

		g.setColor(COLOR_BACKGROUND);

		if (MainZap.roundCorners) {

			g.fillRoundRect(BAR_POS, 6, BAR_WIDTH, 10, ROUND_DEEPTH, ROUND_DEEPTH);
			g.setColor(COLOR_FOREGROUND);
			g.fillRoundRect(BAR_POS, 6, (int) (BAR_WIDTH * (double) hp / maxHp), 10, ROUND_DEEPTH, ROUND_DEEPTH);

			if (dmgAlpha > 0) {
				g.setColor(new Color(COLOR_FOREGROUND.getRed(), COLOR_FOREGROUND.getGreen(), COLOR_FOREGROUND.getBlue(),
						dmgAlpha));
				g.fillRoundRect(BAR_POS, 6, BAR_WIDTH, 10, ROUND_DEEPTH, ROUND_DEEPTH);
			}

			g.setColor(COLOR_FOREGROUND);

			// *PingPT
			// Buffern für Multi-Thread, um ConcMod zu vermeiden
			ArrayList<int[]> pulses = new ArrayList<int[]>(this.pulses);

			for (int[] i : pulses) {

				int c = i[0] - PULSE_LENGTH;
				float alpha = 0;
				float alphaDelta = (float) 254 / PULSE_LENGTH;

				for (int j = c; j != i[0] + 1; j++) {

					if (alpha > 255)
						alpha = 255;

					g.setColor(new Color(COLOR_FOREGROUND.getRed(), COLOR_FOREGROUND.getGreen(),
							COLOR_FOREGROUND.getBlue(), (int) alpha));
					alpha += alphaDelta;
					g.fillRoundRect(j, 6, 1, 10, ROUND_DEEPTH, ROUND_DEEPTH);

				}

			}

			g.setClip(null);
			return;
		}

		g.fillRect(BAR_POS, 6, BAR_WIDTH, 10);
		g.setColor(COLOR_FOREGROUND);
		g.fillRect(BAR_POS, 6, (int) (BAR_WIDTH * (double) hp / maxHp), 10);

		if (dmgAlpha > 0) {
			g.setColor(new Color(COLOR_FOREGROUND.getRed(), COLOR_FOREGROUND.getGreen(), COLOR_FOREGROUND.getBlue(),
					dmgAlpha));
			g.fillRect(BAR_POS, 6, BAR_WIDTH, 10);
		}

		g.setColor(COLOR_FOREGROUND);
		for (int[] i : pulses) {

			int c = i[0] - PULSE_LENGTH;
			float alpha = 0;
			float alphaDelta = (float) 255 / PULSE_LENGTH;

			for (int j = c; j != i[0] + 1; j++) {

				if (alpha > 255)
					alpha = 255;

				g.setColor(new Color(COLOR_FOREGROUND.getRed(), COLOR_FOREGROUND.getGreen(), COLOR_FOREGROUND.getBlue(),
						(int) alpha));
				alpha += alphaDelta;
				g.fillRect(j, 6, 1, 10);

			}

		}

		g.setClip(null);

	}

	private void paintHpWarning(Graphics g) {

		g.setColor(new Color(COLOR_FOREGROUND.getRed(), COLOR_FOREGROUND.getGreen(), COLOR_FOREGROUND.getBlue(),
				(int) (100 * generalHpWarnAlphaFac)));

		if (MainZap.fancyGraphics) {
			// Fancy
			g.fillRect(BAR_POS, 6, BAR_WIDTH + 1, 11);

			float alpha = 100.0f * generalHpWarnAlphaFac;
			final float alphaDelta = alpha / WARNING_BORDER_REACH;

			for (int d = 1; d <= WARNING_BORDER_REACH; d++) {
				g.setColor(new Color(COLOR_FOREGROUND.getRed(), COLOR_FOREGROUND.getGreen(), COLOR_FOREGROUND.getBlue(),
						(int) (alpha)));
				g.drawRect(BAR_POS - d, 6 - d, BAR_WIDTH + 2 * d, 10 + 2 * d);
				alpha -= alphaDelta;
			}

		} else {
			// Basic
			g.fillRect(BAR_POS - 4, 2, BAR_WIDTH + 8, 18);
		}

	}

	@Override
	public void update() {

		if (hp <= (int) (maxHp * WARNING_BORDER)) {
			// Hp-Warnung aktiv
			generalHpWarnAlphaFac += generalHpWarnAlphaFacDelta;
			if (generalHpWarnAlphaFac <= 0.0f) {
				generalHpWarnAlphaFac = 0.0f;
				generalHpWarnAlphaFacDelta *= -1;
			} else if (generalHpWarnAlphaFac >= 1.0f) {
				generalHpWarnAlphaFac = 1.0f;
				generalHpWarnAlphaFacDelta *= -1;
			}
		}

		if (dmgAlpha > 0)
			dmgAlpha -= 3; // Dmg-Effekt-Verblassen

		for (int[] i : pulses) {
			if (i[0] >= (int) (520 * (double) (hp + i[1]) / maxHp) + PULSE_LENGTH) {
				pulses.schedRemove(i);
				hp += i[1];
				if (hp > maxHp)
					hp = maxHp;
				continue;
			}
			i[0] = i[0] + 6;
		}

		pulses.update();

	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

}
