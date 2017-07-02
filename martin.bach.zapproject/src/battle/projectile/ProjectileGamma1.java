package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileGamma1 extends Projectile {

	private static final float RADIUS = 12.0f;
	private static final int DAMAGE = 2_000;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final float SPEED = 3.5f;
	private static final Color COLOR = new Color(60, 20, 20);
	private static final boolean SQUARE = true;

	public ProjectileGamma1() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileGamma1();
	}

}
