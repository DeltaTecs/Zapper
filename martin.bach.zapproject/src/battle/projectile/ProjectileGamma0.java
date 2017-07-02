package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileGamma0 extends Projectile {

	private static final float RADIUS = 8.0f;
	private static final int DAMAGE = 100;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final float SPEED = 6.0f;
	private static final Color COLOR = new Color(100, 20, 20);
	private static final boolean SQUARE = true;

	public ProjectileGamma0() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileGamma0();
	}

}
