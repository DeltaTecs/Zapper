package battle.stage._10;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import battle.MultiCannonWeaponConfiguration;
import battle.WeaponPositioning;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.BoundedFleeProtocol;
import battle.ai.FindLockAction;
import battle.enemy.ShieldedEnemy;
import battle.projectile.ProjectileGamma2;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class EnemyGamma2 extends ShieldedEnemy {

	private static final float SPEED = 6.8f;
	private static final int MAX_HP = 1000;
	private static final int SHIELD = 5_000;
	private static final int SHOOTING_RANGE = 700;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_GAMMA_2);
	private static final float SCALE = 0.65f;
	private static final float RADIUS = 47.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(26, 280);
	private static final WeaponPositioning WEAPON_POSITIONING = new WeaponPositioning((byte) 2, new int[] { -12, 12 },
			new int[] { -42, -42 });
	private static final float COOLDOWN = 0.5f;
	private static final int SCORE = 40;
	private static final int CRYSTALS = 250;
	private static final int PROJECTILE_RANGE = 900;
	private static final boolean FRIEND = false;

	private static final int OPTIC_LOCK_RANGE = 1000;
	private static final int PHYSICAL_LOCK_RANGE = 1500;
	private static final int LOCK_LOOSE_RANGE = 1200;
	private static final int LOCK_FACE_RANGE = 1000;
	private static final float SHIELD_FLEE_PERCENTAGE = 0.3f;
	private static final int SHIELD_NON_RECOVERY_TIME = MainZap.inTicks(700);
	private static final float SHIELD_REGEN = 60;
	private static final boolean SHIELD_FLEE = true;

	private Rectangle movementBounds;

	public EnemyGamma2(float posX, float posY, Rectangle movementBounds) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO,
				new BoundedFleeProtocol(SHIELD_FLEE_PERCENTAGE, SHIELD_FLEE, movementBounds),
				new MultiCannonWeaponConfiguration(COOLDOWN, SHOOTING_RANGE, WEAPON_POSITIONING), MAX_HP,
				EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE, CRYSTALS, FRIEND, SHIELD);
		this.movementBounds = movementBounds;
		setNonRecoveryTime(SHIELD_NON_RECOVERY_TIME);
		setProjectilePattern(new ProjectileGamma2());
		setShieldRegen(SHIELD_REGEN);
		setMayShoot(true);
		setPreAiming(false); // Hardcore: pre-aim anschalten. -> Lieber aus lassen
		getAiProtocol().setLockPhysicalDetectionRange(PHYSICAL_LOCK_RANGE);
		getAiProtocol().setLockOpticDetectionRange(OPTIC_LOCK_RANGE);
		getAiProtocol().setLockOutOfRangeRange(LOCK_LOOSE_RANGE);
		getAiProtocol().setLockFaceDistance(LOCK_FACE_RANGE);
		((AdvancedSingleProtocol) getAiProtocol()).setLockAction(FindLockAction.LOCK_ENEMYS_INRANGE);
		((AdvancedSingleProtocol) getAiProtocol()).setCombatRangePerOutOfRangeRange(0.3f);
	}

	@Override
	public Object getClone() {
		return new EnemyGamma2(getPosX(), getPosY(), movementBounds);
	}

}
