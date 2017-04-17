package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileFriendAlpha0 extends Projectile {

	private static final float RADIUS = 7.0f;
	private static final int DAMAGE = 400;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, false);
	private static final float SPEED = 4.0f;
	private static final Color COLOR = Color.RED;
	private static final boolean SQUARE = false;

	public ProjectileFriendAlpha0() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileFriendAlpha0();
	}

}
