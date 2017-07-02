package gui.effect;

import java.awt.Color;
import java.awt.Graphics2D;

import corecase.MainZap;
import lib.PaintingTask;
import lib.Updateable;

public class ShockSparkle implements Updateable, PaintingTask {

	private static final Color BASECOLOR = new Color(115, 223, 244);
	private static final float FADE_PERCENTAGE = 0.2f;

	protected int x, y, width, height, maxDuration, duration;
	protected boolean fading = false;
	protected Color color;

	public ShockSparkle(int x, int y, int width, int height, int maxDuration) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.maxDuration = maxDuration;
		duration = maxDuration;
		color = new Color(BASECOLOR.getRed() + MainZap.rand(200 - BASECOLOR.getRed()),
				BASECOLOR.getGreen() + MainZap.rand(255 - BASECOLOR.getGreen()), BASECOLOR.getBlue());
	}

	@Override
	public void paint(Graphics2D g) {

		if (fading) {
			g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(),
					(int) (255 * (duration / (maxDuration * FADE_PERCENTAGE)))));
			g.fillRect(x, y, width, height);
		} else {
			g.setColor(color);
			g.fillRect(x, y, width, height);
		}

	}

	@Override
	public void update() {
		if (duration != 0)
			duration--;
		fading = duration < (maxDuration * FADE_PERCENTAGE);
	}

	public boolean faded() {
		return duration == 0;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getMaxDuration() {
		return maxDuration;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
