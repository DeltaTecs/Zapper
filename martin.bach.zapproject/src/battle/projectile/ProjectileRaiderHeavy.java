package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileRaiderHeavy extends Projectile {

	private static final float RADIUS = 8.0f;
	private static final int DAMAGE = 250;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final float SPEED = 5.0f;
	private static final Color COLOR = new Color(120, 7, 7);
	private static final boolean SQUARE = true;

	public ProjectileRaiderHeavy() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileRaiderHeavy();
	}

}
