package battle.enemy;

import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.BasicSingleProtocol;
import battle.projectile.ProjectileBeta1;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import io.TextureBuffer;

public class EnemyBeta1 extends Enemy {

	private static final float SPEED = 1.4f;
	private static final int MAX_HP = 400;
	private static final int SHOOTING_RANGE = 500;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_BETA1);
	private static final float SCALE = 0.7f;
	private static final float RADIUS = 40.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(27, 100);
	private static final int TAIL_SIZE = 15;
	private static final int TAIL_DISTANCE = 4;
	private static final float TAIL_SIZEREMOVAL = 0.35f;
	private static final int[] TAIL_POS_X = new int[] {20, -20};
	private static final int[] TAIL_POS_Y = new int[] {40, 40};
	private static final boolean TAIL_SQUARE = false;
	private static final float COOLDOWN = 70;
	private static final int SCORE = 8;
	private static final int CRYSTALS = 9;
	private static final int PROJECTILE_RANGE = 900;
	private static final boolean FRIEND = false;

	public EnemyBeta1(float posX, float posY) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new BasicSingleProtocol(),
				new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE,
				CRYSTALS, FRIEND, new TailManager(TAIL_SIZE, TAIL_DISTANCE, TAIL_SIZEREMOVAL, TAIL_POS_X, TAIL_POS_Y, TAIL_SQUARE));
		setProjectilePattern(new ProjectileBeta1());
		setMayShoot(true);
	}

	@Override
	public Object getClone() {
		return new EnemyBeta1(getPosX(), getPosY());
	}

}
