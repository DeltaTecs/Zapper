package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileBeta1 extends Projectile {

	private static final float RADIUS = 9.0f;
	private static final int DAMAGE = 850;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final float SPEED = 7.0f;
	private static final Color COLOR = new Color(200, 20, 20);
	private static final boolean SQUARE = true;

	public ProjectileBeta1() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileBeta1();
	}

}
