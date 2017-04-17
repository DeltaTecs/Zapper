package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileAlpha0 extends Projectile {

	private static final float RADIUS = 7.0f;
	private static final int DAMAGE = 40;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final float SPEED = 6.0f;
	private static final Color COLOR = new Color(245, 7, 7);
	private static final boolean SQUARE = false;

	public ProjectileAlpha0() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileAlpha0();
	}

}
