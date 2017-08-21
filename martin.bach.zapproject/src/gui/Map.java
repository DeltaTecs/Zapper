package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;

import battle.enemy.Enemy;
import battle.stage.StageManager;
import corecase.MainZap;
import gui.extention.Shocking;
import lib.PaintingTask;
import lib.ScheduledList;
import lib.Updateable;

public class Map implements PaintingTask, Updateable {

	private static final int BG_CLASSIC_PARTICLE_AMOUNT = 200;
	private static final int BG_GROUPED_CENTER_AMOUNT = 78;
	public static final int SIZE = 3000;

	private ArrayList<Rectangle> backgroundParticles = new ArrayList<Rectangle>();
	private ScheduledList<Updateable> updateElements = new ScheduledList<Updateable>();
	private ScheduledList<PaintingTask> backgroundPaintElements = new ScheduledList<PaintingTask>();
	private ScheduledList<PaintingTask> foregroundPaintElements = new ScheduledList<PaintingTask>();
	private ScheduledList<PaintingTask> shipElements = new ScheduledList<PaintingTask>();

	public Map() {
		rebuildBgParticles();
	}

	@Override
	public void paint(Graphics2D g) {

		// Von 0/0 Kontext zu Karte Kontext
		float dx = MainZap.getPlayer().getPosX() - Frame.HALF_SCREEN_SIZE;
		float dy = MainZap.getPlayer().getPosY() - Frame.HALF_SCREEN_SIZE;
		g.translate(-dx, -dy);

		g.setColor(Color.WHITE);
		if (MainZap.debug)
			g.setColor(Color.GRAY);
		g.fillRect(-2 * SIZE, -2 * SIZE, 8 * SIZE, 8 * SIZE);

		if (MainZap.debug) {
			g.setColor(new Color(255, 0, 255)); // LILA
			g.fillRect(-96, 196, 8, 8);
			g.setColor(Color.BLACK);
			g.drawString("-100 | 200", -80, 194);
		}

		// ----- Zeichen-Reihenfolge -------
		// 1. Background-Objects (Strukturen, darüber alle Projektile)
		// 2. Ship-Tasks (Schiffe)
		// 3. Foreground-Objects (sonderfall. eig. nur Turrets auf FriendGamma1)
		// ------

		// *PingPT
		// Listen Buffern, falls Paint/calc getrennt
		if (MainZap.PAINT_CALC_THREAD_SPLIT) {

			if (StageManager.getActiveStage() != null && StageManager.getActiveStage().getLvl() != 12) {
				ArrayList<Rectangle> backgroundParticles = new ArrayList<Rectangle>(this.backgroundParticles);
				g.setColor(new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 30));
				for (Rectangle r : backgroundParticles)
					g.fillRect(r.x, r.y, r.width, r.height);

			}

			ArrayList<PaintingTask> backgroundPaintElements = new ArrayList<PaintingTask>(this.backgroundPaintElements);
			// Hintergrund-Objekte zeichnen
			for (PaintingTask pt : backgroundPaintElements) {
				pt.paint(g);
			}

			ArrayList<PaintingTask> shipElements = new ArrayList<PaintingTask>(this.shipElements);
			// Schiffe zeichnen
			if (MainZap.antializeShips) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			}
			for (PaintingTask pt : shipElements) {
				pt.paint(g);
			}
			if (!MainZap.generalAntialize) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			}

			ArrayList<PaintingTask> foregroundPaintElements = new ArrayList<PaintingTask>(this.foregroundPaintElements);
			// Fordergrund-Objekte zeichnen
			for (PaintingTask pt : foregroundPaintElements) {
				pt.paint(g);
			}

		} else {

			if (StageManager.getActiveStage().getLvl() != 12) {
				g.setColor(new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 30));
				for (Rectangle r : backgroundParticles)
					g.fillRect(r.x, r.y, r.width, r.height);
			}

			// Hintergrund-Objekte zeichnen
			for (PaintingTask pt : backgroundPaintElements) {
				pt.paint(g);
			}

			// Schiffe zeichnen
			if (MainZap.antializeShips) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			}
			for (PaintingTask pt : shipElements) {
				pt.paint(g);
			}
			if (!MainZap.generalAntialize) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			}

			// Fordergrund-Objekte zeichnen
			for (PaintingTask pt : foregroundPaintElements) {
				pt.paint(g);
			}

		}

		// Shock-Effekt
		Shocking.paint(g);

		// Zurück zu 0/0 Kontext
		g.translate(dx, dy);

	}

	@Override
	public void update() {

		foregroundPaintElements.update();
		backgroundPaintElements.update();
		shipElements.update();
		updateElements.update();
		for (Updateable u : updateElements) {
			u.update();
		}
		// ShockEffekt
		Shocking.update();

	}

	public void rebuildBgParticles() {
		backgroundParticles.clear();

		if (!MainZap.fancyGraphics)
			buildClassicBgParticles();
		else
			buildGroupedBgParticles();

	}

	private void buildClassicBgParticles() {
		Random r = new Random();
		for (int i = 0; i != BG_CLASSIC_PARTICLE_AMOUNT; i++) {

			int x = r.nextInt(SIZE + 600) - 300 + 1;
			int y = r.nextInt(SIZE + 600) - 300 + 1;
			int width = r.nextInt(250) + 51;
			int height = r.nextInt(250) + 51;

			if (r.nextBoolean()) {
				height = width;
			}

			backgroundParticles.add(new Rectangle(x, y, width, height));
		}
	}

	private void buildGroupedBgParticles() {
		Random r = new Random();
		for (int i = 0; i != BG_GROUPED_CENTER_AMOUNT; i++) {

			int x0 = r.nextInt(SIZE + 600) - 300 + 1;
			int y0 = r.nextInt(SIZE + 600) - 300 + 1;
			int width0 = r.nextInt(100) + 21;
			int height0 = r.nextInt(100) + 21;

			if (r.nextBoolean()) {
				height0 = width0;
			}

			backgroundParticles.add(new Rectangle(x0, y0, width0, height0));
			if (!(r.nextBoolean() && r.nextBoolean()))
				backgroundParticles.add(new Rectangle(x0, y0, width0, height0));
			if (!r.nextBoolean())
				backgroundParticles.add(new Rectangle(x0, y0, width0, height0));

			int secondObjects = r.nextInt(16) + 4;
			for (int j = 0; j != secondObjects; j++) {
				int x1 = x0 + r.nextInt(200) - 100;
				int y1 = y0 + r.nextInt(200) - 100;
				int width1 = r.nextInt(70) + 26;
				int height1 = r.nextInt(70) + 26;
				backgroundParticles.add(new Rectangle(x1, y1, width1, height1));

				int thirdObjects = r.nextInt(4);
				for (int k = 0; k != thirdObjects; k++) {
					int x2 = x1 + r.nextInt(460) - 230;
					int y2 = y1 + r.nextInt(460) - 230;
					int width2 = r.nextInt(24) + 4;
					int height2 = r.nextInt(24) + 4;
					if (r.nextBoolean() || r.nextBoolean())
						width2 = height2;
					backgroundParticles.add(new Rectangle(x2, y2, width2, height2));
				}
			}

		}
	}

	public void addUpdateElement(Updateable u) {
		updateElements.schedAdd(u);
	}

	public void removeUpdateElement(Updateable u) {
		updateElements.schedRemove(u);
	}

	public void addPaintElement(PaintingTask t, boolean bg) {
		if (bg)
			backgroundPaintElements.schedAdd(t);
		else
			foregroundPaintElements.schedAdd(t);

	}

	public void removePaintElement(PaintingTask t, boolean bg) {
		if (bg)
			backgroundPaintElements.schedRemove(t);
		else
			foregroundPaintElements.schedRemove(t);

	}

	public void addPaintShip(Enemy e) {
		shipElements.schedAdd(e);
	}

	public void removePaintShip(Enemy e) {
		shipElements.schedRemove(e);
	}

	public ScheduledList<Updateable> getUpdateElements() {
		return updateElements;
	}

	public ScheduledList<PaintingTask> getBackgroundPaintElements() {
		return backgroundPaintElements;
	}

	public ScheduledList<PaintingTask> getForegroundPaintElements() {
		return foregroundPaintElements;
	}

}
