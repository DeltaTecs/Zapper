package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;

import battle.enemy.Enemy;
import corecase.MainZap;
import library.PaintingTask;
import library.ScheduledList;
import library.Updateable;

public class Map implements PaintingTask, Updateable {

	private static final int BACKGROUND_PARTICLE_AMOUNT = 200;
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
		int dx = (int) MainZap.getPlayer().getPosX() - Frame.HALF_SCREEN_SIZE;
		int dy = (int) MainZap.getPlayer().getPosY() - Frame.HALF_SCREEN_SIZE;
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

		// *PingPT
		// Listen Buffern, falls Paint/calc getrennt
		if (MainZap.PAINT_CALC_THREAD_SPLIT) {

			ArrayList<Rectangle> backgroundParticles = new ArrayList<Rectangle>(this.backgroundParticles);
			g.setColor(new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 30));
			for (Rectangle r : backgroundParticles) {
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

			g.setColor(new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 30));
			for (Rectangle r : backgroundParticles) {
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

		// Zur�ck zu 0/0 Kontext
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

	}

	public void rebuildBgParticles() {
		backgroundParticles.clear();
		Random r = new Random();
		for (int i = 0; i != BACKGROUND_PARTICLE_AMOUNT; i++) {

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

	public void addUpdateElement(Updateable u) {
		updateElements.schedAdd(u);
	}

	public void removeUpdateElement(Updateable u) {
		updateElements.schedRemove(u);
	}

	public void addPaintElement(PaintingTask t, boolean bg) {
		if (bg) {
			backgroundPaintElements.schedAdd(t);
		} else {
			foregroundPaintElements.schedAdd(t);
		}
	}

	public void removePaintElement(PaintingTask t, boolean bg) {
		if (bg) {
			backgroundPaintElements.schedRemove(t);
		} else {
			foregroundPaintElements.schedRemove(t);
		}
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