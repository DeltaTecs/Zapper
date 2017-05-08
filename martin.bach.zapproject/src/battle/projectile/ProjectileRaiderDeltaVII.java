package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileRaiderDeltaVII extends Projectile {

	private static final float RADIUS = 10.0f;
	private static final int DAMAGE = 105;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final float SPEED = 20.0f;
	private static final Color COLOR = new Color(120, 4, 4);
	private static final boolean SQUARE = false;

	public ProjectileRaiderDeltaVII() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileRaiderDeltaVII();
	}

}
