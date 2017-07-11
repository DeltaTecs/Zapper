package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileFriendGamma1Turret extends Projectile {

	private static final float RADIUS = 5.0f;
	private static final int DAMAGE = 80;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, false);
	private static final float SPEED = 15.0f;
	private static final Color COLOR = new Color(140, 20, 20);
	private static final boolean SQUARE = false;

	public ProjectileFriendGamma1Turret() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileFriendGamma1Turret();
	}

}
