package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import corecase.MainZap;
import lib.PaintingTask;
import lib.SpeedVector;
import lib.Updateable;

public class HudLightning implements PaintingTask, Updateable {

	private static final int CORNERS_MIN = 2;
	private static final int CORNERS_MAX = 4;
	private static final int SIZE_MIN = 1;
	private static final int SIZE_MAX = 4;
	private static final int ALPHA_MIN = 100;
	private static final int ALPHA_MAX = 240;
	private static final int RED_MIN = 200;
	private static final int RED_MAX = 240;
	private static final int GREEN_MIN = 240;
	private static final int GREEN_MAX = 250;
	private static final float ALPHA_DECAY_BASIS = 1.05f;

	private int[] xs;
	private int[] ys;
	private Stroke stroke;
	private int alpha;
	private int startAlpha;
	private int time = 0;
	private int red;
	private int green;

	public HudLightning() {

		int startX = MainZap.rand(Frame.SIZE);
		int startY = MainZap.rand(Frame.SIZE);
		int endX = MainZap.rand(Frame.SIZE);
		int endY = MainZap.rand(Frame.SIZE);

		red = RED_MIN + MainZap.rand(RED_MAX - RED_MIN + 1);
		green = GREEN_MIN + MainZap.rand(GREEN_MAX - GREEN_MIN + 1);
		stroke = new BasicStroke(SIZE_MIN + MainZap.rand(SIZE_MAX - SIZE_MIN + 1));
		startAlpha = ALPHA_MIN + MainZap.rand(ALPHA_MAX - ALPHA_MIN + 1);
		alpha = startAlpha;

		int corners = CORNERS_MIN + MainZap.rand(CORNERS_MAX - CORNERS_MIN + 1);

		SpeedVector directConnection = new SpeedVector(endX - startX, endY - startY);
		xs = new int[corners];
		ys = new int[corners];
		xs[0] = startX;
		ys[0] = startY;
		xs[corners - 1] = endX;
		ys[corners - 1] = endY;

		for (int i = 1; i != corners - 1; i++) {
			xs[i] = (int) (directConnection.getX() * Math.random() * i / (float) (corners - 1)) + startX;
			ys[i] = (int) (directConnection.getY() * Math.random() * i / (float) (corners - 1)) + startY;
		}
	}

	@Override
	public void update() {
		time++;
		alpha = (int) ((float) startAlpha * (2 - Math.pow(ALPHA_DECAY_BASIS, time)));
	}

	@Override
	public void paint(Graphics2D g) {

		if (alpha <= 0) // schon verpufft
			return;

		g.setColor(new Color(red, green, 255, alpha));
		g.setStroke(stroke);
		drawLine(g, xs, ys);
	}

	private void drawLine(Graphics2D g, int[] xs, int[] ys) {

		int lastX = xs[0];
		int lastY = ys[0];

		for (int i = 1; i != xs.length; i++) {
			g.drawLine(lastX, lastY, xs[i], ys[i]);
			lastX = xs[i];
			lastY = ys[i];
		}

	}

	public boolean faded() {
		return alpha < 20;
	}

}
