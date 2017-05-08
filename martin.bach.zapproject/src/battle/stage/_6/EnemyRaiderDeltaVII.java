package battle.stage._6;

import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.FindLockAction;
import battle.enemy.Enemy;
import battle.projectile.ProjectileRaiderDeltaVII;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class EnemyRaiderDeltaVII extends Enemy {

	private static final float SPEED = 5.3f;
	private static final int MAX_HP = 8000;
	private static final int SHOOTING_RANGE = 680;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_RAIDER_DELTAVII);
	private static final float SCALE = 1.0f;
	private static final float RADIUS = 40.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(50, 500);
	private static final float COOLDOWN = 1.4f;
	private static final int SCORE = 50;
	private static final int CRYSTALS = 510;
	private static final int PROJECTILE_RANGE = 680;
	private static final boolean FRIEND = false;

	public EnemyRaiderDeltaVII(float posX, float posY) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new AdvancedSingleProtocol(),
				new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE,
				CRYSTALS, FRIEND);
		setHealth(1600);
		setProjectilePattern(new ProjectileRaiderDeltaVII());
		setMayShoot(true);
		setPreAiming(false);
		setNoWaitAfterWarp(true);
		getAiProtocol().setLockOpticDetectionRange(1000);
		((AdvancedSingleProtocol) getAiProtocol()).setLockAction(FindLockAction.LOCK_ENEMYS_INRANGE);
	}

	@Override
	public Object getClone() {
		return new EnemyRaiderDeltaVII(getPosX(), getPosY());
	}

}
