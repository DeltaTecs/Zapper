package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileTurretAlpha extends Projectile {

	private static final float RADIUS = 7.0f;
	private static final int DAMAGE = 3;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, false);
	private static final float SPEED = 27.0f;
	private static final Color COLOR = new Color(220, 10, 10);
	private static final boolean SQUARE = false;

	public ProjectileTurretAlpha() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileTurretAlpha();
	}

}
