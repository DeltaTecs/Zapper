package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileTurretRaider extends Projectile {

	private static final float RADIUS = 10.0f;
	private static final int DAMAGE = 500;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final float SPEED = 2.0f;
	private static final Color COLOR = new Color(120, 10, 10);
	private static final boolean SQUARE = true;

	public ProjectileTurretRaider() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileTurretRaider();
	}

}
