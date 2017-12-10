package battle.stage._9;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import battle.MultiCannonWeaponConfiguration;
import battle.WeaponPositioning;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.BoundedFleeProtocol;
import battle.ai.FindLockAction;
import battle.enemy.ShieldedEnemy;
import battle.projectile.ProjectileGamma0;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import io.TextureBuffer;

public class EnemyGamma0 extends ShieldedEnemy {

	private static final float SPEED = 4.5f;
	private static final int MAX_HP = 500;
	private static final int SHIELD = 1_500;
	private static final int SHOOTING_RANGE = 700;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_GAMMA_0);
	private static final float SCALE = 0.7f;
	private static final float RADIUS = 38.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(26, 100);
	private static final WeaponPositioning WEAPON_POSITIONING = new WeaponPositioning((byte) 2, new int[] { -16, 16 },
			new int[] { -30, -30 });
	private static final int TAIL_SIZE = 20;
	private static final int TAIL_DISTANCE = 3;
	private static final float TAIL_SIZEREMOVAL = 0.55f;
	private static final int[] TAIL_POS_X = new int[] {18, -18};
	private static final int[] TAIL_POS_Y = new int[] {32, 32};
	private static final boolean TAIL_SQUARE = true;
	private static final float COOLDOWN = 40;
	private static final int SCORE = 40;
	private static final int CRYSTALS = 250;
	private static final int PROJECTILE_RANGE = 900;
	private static final boolean FRIEND = false;

	private static final int OPTIC_LOCK_RANGE = 1000;
	private static final int PHYSICAL_LOCK_RANGE = 1500;
	private static final int LOCK_LOOSE_RANGE = 1200;
	private static final int LOCK_FACE_RANGE = 1000;
	private static final float SHIELD_FLEE_PERCENTAGE = 0.5f;
	private static final int SHIELD_NON_RECOVERY_TIME = MainZap.inTicks(700);
	private static final float SHIELD_REGEN = 60;
	private static final boolean SHIELD_FLEE = true;

	private Rectangle movementBounds;

	public EnemyGamma0(float posX, float posY, Rectangle movementBounds) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO,
				new BoundedFleeProtocol(SHIELD_FLEE_PERCENTAGE, SHIELD_FLEE, movementBounds),
				new MultiCannonWeaponConfiguration(COOLDOWN, SHOOTING_RANGE, WEAPON_POSITIONING), MAX_HP,
				EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE, CRYSTALS, FRIEND, SHIELD, new TailManager(TAIL_SIZE, TAIL_DISTANCE, TAIL_SIZEREMOVAL, TAIL_POS_X, TAIL_POS_Y, TAIL_SQUARE));
		this.movementBounds = movementBounds;
		setNonRecoveryTime(SHIELD_NON_RECOVERY_TIME);
		setProjectilePattern(new ProjectileGamma0());
		setShieldRegen(SHIELD_REGEN);
		setMayShoot(true);
		getAiProtocol().setLockPhysicalDetectionRange(PHYSICAL_LOCK_RANGE);
		getAiProtocol().setLockOpticDetectionRange(OPTIC_LOCK_RANGE);
		getAiProtocol().setLockOutOfRangeRange(LOCK_LOOSE_RANGE);
		getAiProtocol().setLockFaceDistance(LOCK_FACE_RANGE);
		((AdvancedSingleProtocol) getAiProtocol()).setLockAction(FindLockAction.LOCK_LINKED_ENEMYS);
		((AdvancedSingleProtocol) getAiProtocol()).setCombatRangePerOutOfRangeRange(0.3f);
	}

	@Override
	public Object getClone() {
		return new EnemyGamma0(getPosX(), getPosY(), movementBounds);
	}

}
