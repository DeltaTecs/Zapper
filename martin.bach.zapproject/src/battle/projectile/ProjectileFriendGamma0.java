package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileFriendGamma0 extends Projectile {

	private static final float RADIUS = 8.0f;
	private static final int DAMAGE = 0_300;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, false);
	private static final float SPEED = 9.0f;
	private static final Color COLOR = new Color(120, 20, 20);
	private static final boolean SQUARE = false;

	public ProjectileFriendGamma0() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileFriendGamma0();
	}

}
