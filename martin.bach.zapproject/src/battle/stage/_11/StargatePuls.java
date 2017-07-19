package battle.stage._11;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import lib.PaintingTask;
import lib.Updateable;

public class StargatePuls implements PaintingTask, Updateable {

	private static final int START_RADIUS = 300;
	private static final Color COLOR_BG = new Color(244, 244, 244);
	private static final Color COLOR_FG = new Color(117, 148, 160);
	private static final Stroke STROKE_BG = new BasicStroke(40);
	private static final Stroke STROKE_FG_0 = new BasicStroke(5);
	private static final Stroke STROKE_FG_1 = new BasicStroke(4);
	private static final Stroke STROKE_FG_2 = new BasicStroke(2);

	private float speed;
	private float distance = START_RADIUS;
	private boolean done = false;

	private int midX;
	private int midY;

	public StargatePuls(float speed, int midX, int midY) {
		this.speed = speed;
		this.midX = midX;
		this.midY = midY;
	}

	@Override
	public void update() {

		if (done)
			return;

		distance -= speed;
		if (distance == 0)
			done = true;

	}

	@Override
	public void paint(Graphics2D g) {

		if (done)
			return;

		g.setColor(COLOR_BG);
		g.setStroke(STROKE_BG);
		g.drawOval(midX - (int) distance, midY - (int) distance, 2 * (int) distance, 2 * (int) distance);

		g.setColor(COLOR_FG);
		g.setStroke(STROKE_FG_0);
		g.drawOval(midX - (int) distance - 14, midY - (int) distance - 14, 2 * (int) distance + 28,
				2 * (int) distance + 28);
		g.setStroke(STROKE_FG_1);
		g.drawOval(midX - (int) distance - 1, midY - (int) distance - 1, 2 * (int) distance + 2,
				2 * (int) distance + 2);
		g.setStroke(STROKE_FG_2);
		g.drawOval(midX - (int) distance + 8, midY - (int) distance + 8, 2 * (int) distance - 16,
				2 * (int) distance - 16);
	}

	public boolean isDone() {
		return done;
	}

}
