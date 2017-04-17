package battle.projectile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import collision.CollisionInformation;
import collision.CollisionType;

public class GuardianProjectile extends Projectile {

	private static final float SPEED = 33.0f;
	private static final Color COLOR = new Color(70, 3, 93);

	private static final float[] SEXTUPLE_X = new float[] { -2.6f, 2.6f, 5, 2.6f, -2.6f, -5 };
	private static final float[] SEXTUPLE_Y = new float[] { -5, -5, 0, 5, 5, 0 };

	private Polygon outline;
	private int size;

	public GuardianProjectile(int size) {
		super(new CollisionInformation(size * 5, CollisionType.COLLIDE_WITH_ENEMYS, false), SPEED, size, false, null, -100);
		this.size = size;
		int[] xs = new int[6];
		int[] ys = new int[6];

		float fac = size / 10.0f;

		for (int i = 0; i != 6; i++) {
			xs[i] = (int) (SEXTUPLE_X[i] * fac);
			ys[i] = (int) (SEXTUPLE_Y[i] * fac);
		}

		outline = new Polygon(xs, ys, 6);
	}

	public GuardianProjectile(Polygon outline, int size) {
		super(new CollisionInformation(size, CollisionType.COLLIDE_WITH_ENEMYS, false), SPEED, size, false, null, -100);
		this.outline = outline;
		this.size = size;
	}

	@Override
	public Object getClone() {
		return new GuardianProjectile(outline, size);
	}

	@Override
	public void paint(Graphics2D g) {

		// Von Kontext Karte zu Kontext EigenPos
		int dx = getLocX();
		int dy = getLocY();
		g.translate(dx, dy);

		g.setColor(COLOR);
		g.fillPolygon(outline);

		// Zurück zu Karten Kontext
		g.translate(-dx, -dy);
	}

}
