package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileBeta2 extends Projectile {

	private static final float RADIUS = 14.0f;
	private static final int DAMAGE = 30000;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final float SPEED = 3.0f;
	private static final Color COLOR = new Color(130, 20, 20);
	private static final boolean SQUARE = false;

	public ProjectileBeta2() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileBeta2();
	}

}
