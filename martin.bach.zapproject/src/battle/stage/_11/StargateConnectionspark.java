package battle.stage._11;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import collision.Grid;
import corecase.MainZap;
import lib.PaintingTask;
import lib.SpeedVector;
import lib.Updateable;

public class StargateConnectionspark implements PaintingTask, Updateable {

	private static final float SPEED_MIN = 0.7f;
	private static final float SPEED_MAX = 15.0f;
	private static final int LEN_MIN = 5;
	private static final int LEN_MAX = 40;
	private static final int WIDTH_MIN = 1;
	private static final int WIDTH_MAX = 4;
	private static final int SPAWN_RADIUS = 5;

	private SpeedVector direction;
	private SpeedVector size;
	private int brightness;
	private float posX, posY;
	private int length;
	private Stroke stroke;
	private boolean finished = false;

	private StargateConnector end;

	public StargateConnectionspark(StargateConnector start, StargateConnector end) {
		super();
		this.end = end;
		float speed = SPEED_MIN + (float) ((SPEED_MAX - SPEED_MIN) * Math.random());
		length = LEN_MIN + MainZap.rand(LEN_MAX - LEN_MIN);
		size = new SpeedVector();
		size.aimFor(start.getPosX(), start.getPosY(), length, end.getPosX(), end.getPosY());
		int width = WIDTH_MIN + MainZap.rand(WIDTH_MAX - WIDTH_MIN);
		stroke = new BasicStroke(width);
		brightness = MainZap.rand(255);
		direction = new SpeedVector();
		direction.aimFor(start.getPosX(), start.getPosY(), speed, end.getPosX(), end.getPosY());
		posX = start.getPosX() + MainZap.rand(2 * SPAWN_RADIUS) - SPAWN_RADIUS;
		posY = start.getPosY() + MainZap.rand(2 * SPAWN_RADIUS) - SPAWN_RADIUS;
	}

	@Override
	public void update() { // bewegen
		if (finished)
			return;
		posX += direction.getX();
		posY += direction.getY();

		if (Grid.distance(new Point((int) posX, (int) posY),
				new Point((int) end.getPosX(), (int) end.getPosY())) <= length * 4)
			finished = true;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(new Color(255, 255, 255, brightness));
		g.setStroke(stroke);
		g.drawLine((int) posX, (int) posY, (int) (posX + size.getX()), (int) (posY + size.getY()));
	}

	public boolean isFinished() {
		return finished;
	}

}
