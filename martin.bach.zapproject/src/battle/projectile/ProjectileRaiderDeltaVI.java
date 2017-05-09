package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileRaiderDeltaVI extends Projectile {

	private static final float RADIUS = 9.0f;
	private static final int DAMAGE = 65;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final float SPEED = 20.0f;
	private static final Color COLOR = new Color(180, 4, 4);
	private static final boolean SQUARE = false;

	public ProjectileRaiderDeltaVI() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileRaiderDeltaVI();
	}

}
