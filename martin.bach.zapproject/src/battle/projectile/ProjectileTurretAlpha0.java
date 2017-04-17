package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileTurretAlpha0 extends Projectile {

	private static final float RADIUS = 8.0f;
	private static final int DAMAGE = 110;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final float SPEED = 10.5f;
	private static final Color COLOR = new Color(220, 10, 10);
	private static final boolean SQUARE = false;

	public ProjectileTurretAlpha0() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileTurretAlpha0();
	}

}
