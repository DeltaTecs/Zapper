package battle.stage._3;

import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.ArmyCombatProtocol;
import battle.enemy.Enemy;
import battle.projectile.ProjectileFriendAlpha0;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import io.TextureBuffer;
import lib.ScheduledList;

public class FriendAlpha1 extends Enemy {

	private static final float SPEED = 2.3f;
	private static final int MAX_HP = 420;
	private static final int SHOOTING_RANGE = 300;
	private static final int LOCKRANGE = 500;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_FRIENDSHIP_ALPHA1);
	private static final float SCALE = 0.5f;
	private static final float RADIUS = 20.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(12, 50);
	private static final int TAIL_SIZE = 12;
	private static final int TAIL_DISTANCE = 8;
	private static final float TAIL_SIZEREMOVAL = 0.4f;
	private static final int[] TAIL_POS_X = new int[] {0};
	private static final int[] TAIL_POS_Y = new int[] {27};
	private static final boolean TAIL_SQUARE = true;
	private static final float COOLDOWN = 50;
	private static final int SCORE = 0;
	private static final int CRYSTALS = 0;
	private static final int PROJECTILE_RANGE = 800;
	private static final boolean FRIEND = true;
	
	private ScheduledList<Enemy> surrounding;

	public FriendAlpha1(float posX, float posY, ScheduledList<Enemy> surrounding) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new ArmyCombatProtocol(surrounding, LOCKRANGE, true),
				new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE,
				CRYSTALS, FRIEND, new TailManager(TAIL_SIZE, TAIL_DISTANCE, TAIL_SIZEREMOVAL, TAIL_POS_X, TAIL_POS_Y, TAIL_SQUARE));
		this.surrounding = surrounding;
		setProjectilePattern(new ProjectileFriendAlpha0());
		setMayShoot(true);
	}

	@Override
	public Object getClone() {
		return new FriendAlpha1(getPosX(), getPosY(), surrounding);
	}

}
