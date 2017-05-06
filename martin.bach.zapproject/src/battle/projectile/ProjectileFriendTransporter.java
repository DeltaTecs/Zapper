package battle.projectile;

import java.awt.Color;

import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileFriendTransporter extends Projectile {

	private static final float RADIUS = 8.0f;
	private static final int DAMAGE = 15; // eig. 10
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, false);
	private static final float SPEED = 8.0f;
	private static final Color COLOR = Color.RED;
	private static final boolean SQUARE = false;

	public ProjectileFriendTransporter() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileFriendTransporter();
	}

}
