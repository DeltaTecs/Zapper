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
	private static final int LEN_MAX = 39;
	private static final int WIDTH_MIN = 1;
	private static final int WIDTH_MAX = 3;
	private static final int SPAWN_RADIUS = 5;

	private SpeedVector direction;
	private SpeedVector size;
	private int brightness;
	private float posX, posY;
	private int length;
	private float speed;
	private Stroke stroke;

	private StargateConnector end;
	private StargateConnector start;

	public StargateConnectionspark(StargateConnector start, StargateConnector end) {
		super();
		this.end = end;
		this.start = start;
		speed = SPEED_MIN + (float) ((SPEED_MAX - SPEED_MIN + 1) * Math.random());
		length = LEN_MIN + MainZap.rand(LEN_MAX - LEN_MIN + 1);
		size = new SpeedVector();
		size.aimFor(start.getPosX(), start.getPosY(), length, end.getPosX(), end.getPosY());
		int width = WIDTH_MIN + MainZap.rand(WIDTH_MAX - WIDTH_MIN + 1);
		stroke = new BasicStroke(width);
		brightness = MainZap.rand(255);
		direction = new SpeedVector();
		direction.aimFor(start.getPosX(), start.getPosY(), speed, end.getPosX(), end.getPosY());
		// An zufäliger Stelle positionieren
		SpeedVector distance = new SpeedVector(end.getPosX() - start.getPosX(), end.getPosY() - start.getPosY());
		float rand = (float) Math.random();
		posX = start.getPosX() + (rand * distance.getX()) + MainZap.rand(2 * SPAWN_RADIUS) - SPAWN_RADIUS;
		posY = start.getPosY() + (rand * distance.getY()) + MainZap.rand(2 * SPAWN_RADIUS) - SPAWN_RADIUS;
	}

	@Override
	public void update() { // bewegen

		if (Grid.distance(new Point((int) posX, (int) posY),
				new Point((int) end.getPosX(), (int) end.getPosY())) <= length * 2.5) {
			posX = start.getPosX() + MainZap.rand(2 * SPAWN_RADIUS) - SPAWN_RADIUS;
			posY = start.getPosY() + MainZap.rand(2 * SPAWN_RADIUS) - SPAWN_RADIUS;
			return;
		}

		posX += direction.getX();
		posY += direction.getY();

	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(new Color(255, 255, 255, brightness));
		g.setStroke(stroke);
		g.drawLine((int) posX, (int) posY, (int) (posX + size.getX()), (int) (posY + size.getY()));
	}

}
