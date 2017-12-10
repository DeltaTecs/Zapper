package gui.effect;

import java.awt.Color;
import java.awt.Graphics2D;

import corecase.MainZap;
import lib.PaintingTask;
import lib.Updateable;

public class TailParticle implements PaintingTask, Updateable {

	private static final int START_ALPHA = 14;
	private static final float ALPHA_DELTA = 0.1f;

	private boolean square;
	private float size;
	private float sizeRemoval;
	private float alpha = START_ALPHA;
	private int x, y;

	public TailParticle(int x, int y, float size, float sizeRemoval, boolean square) {
		this.sizeRemoval = sizeRemoval;
		this.square = square;
		this.size = size;
		this.x = x;
		this.y = y;
	}

	@Override
	public void paint(Graphics2D g) {

		if (alpha < 0)
			return; // Fehler durch Path-Prediction.. ?

		g.setColor(new Color(0, 0, 0, (int) (alpha)));
		if (square)
			g.fillRect(x - (int) (size / 2.0f), y - (int) (size / 2.0f), (int) size, (int) size);
		else
			g.fillOval(x - (int) (size / 2.0f), y - (int) (size / 2.0f), (int) size, (int) size);

	}

	@Override
	public void update() {

		alpha -= ALPHA_DELTA;
		if (alpha < 0)
			alpha = 0;

		size -= sizeRemoval;
		if (size < 0)
			size = 0;

		if (faded())
			unRegister();

	}

	public void register() {
		MainZap.getMap().addUpdateElement(this);
		MainZap.getMap().addPaintElement(this, true);
	}

	public void unRegister() {
		MainZap.getMap().removeUpdateElement(this);
		MainZap.getMap().removePaintElement(this, true);
	}

	private boolean faded() {
		return alpha <= 5 || size == 0;
	}

}
