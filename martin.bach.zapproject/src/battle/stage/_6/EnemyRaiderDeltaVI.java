package battle.stage._6;

import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.FindLockAction;
import battle.enemy.Enemy;
import battle.projectile.ProjectileRaiderDeltaVI;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import io.TextureBuffer;

public class EnemyRaiderDeltaVI extends Enemy {

	private static final float SPEED = 4.2f;
	private static final int MAX_HP = 3000;
	private static final int SHOOTING_RANGE = 680;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_RAIDER_DELTAVI);
	private static final float SCALE = 1.0f;
	private static final float RADIUS = 40.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(50, 500);
	private static final int TAIL_SIZE = 20;
	private static final int TAIL_DISTANCE = 3;
	private static final float TAIL_SIZEREMOVAL = 0.4f;
	private static final int[] TAIL_POS_X = new int[] {21, -21};
	private static final int[] TAIL_POS_Y = new int[] {30, 30};
	private static final boolean TAIL_SQUARE = false;
	private static final float COOLDOWN = 1.4f;
	private static final int SCORE = 50;
	private static final int CRYSTALS = 300;
	private static final int PROJECTILE_RANGE = 600;
	private static final boolean FRIEND = false;

	public EnemyRaiderDeltaVI(float posX, float posY) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new AdvancedSingleProtocol(),
				new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE,
				CRYSTALS, FRIEND, new TailManager(TAIL_SIZE, TAIL_DISTANCE, TAIL_SIZEREMOVAL, TAIL_POS_X, TAIL_POS_Y, TAIL_SQUARE));
		setHealth(2600);
		setProjectilePattern(new ProjectileRaiderDeltaVI());
		setMayShoot(true);
		setPreAiming(false);
		setNoWaitAfterWarp(true);
		getAiProtocol().setLockOpticDetectionRange(1000);
		((AdvancedSingleProtocol) getAiProtocol()).setLockAction(FindLockAction.LOCK_ENEMYS_INRANGE);
	}

	@Override
	public Object getClone() {
		return new EnemyRaiderDeltaVI(getPosX(), getPosY());
	}

}
