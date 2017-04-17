package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileBasicPlayer extends Projectile {

	private static final float RADIUS = 8.0f;
	private static final int DAMAGE = 100; // eig. 100
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, false);
	private static final float SPEED = 12.0f;
	private static final Color COLOR = Color.RED;
	private static final boolean SQUARE = false;

	public ProjectileBasicPlayer() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileBasicPlayer();
	}

}
