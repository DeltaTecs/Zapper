package battle.stage._3;

import java.awt.Color;

import battle.projectile.Projectile;
import collision.CollisionInformation;
import collision.CollisionType;

public class ProjectileFriendBeta extends Projectile {

	private static final float RADIUS = 9.0f;
	private static final int DAMAGE = 400;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final float SPEED = 8.0f;
	private static final Color COLOR = new Color(180, 20, 20);
	private static final boolean SQUARE = false;

	public ProjectileFriendBeta() {
		super(COLINFO, SPEED, (int) RADIUS, SQUARE, COLOR, DAMAGE);
	}

	@Override
	public Object getClone() {
		return new ProjectileFriendBeta();
	}

}
