package battle.stage._9;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import battle.MultiCannonWeaponConfiguration;
import battle.WeaponPositioning;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.FindLockAction;
import battle.enemy.ShieldedEnemy;
import battle.projectile.ProjectileGamma1;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import io.TextureBuffer;

public class EnemyGamma1 extends ShieldedEnemy {

	private static final float SPEED = 1.0f;
	private static final int MAX_HP = 1_500;
	private static final int SHIELD = 7_000;
	private static final int SHOOTING_RANGE = 1000;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_GAMMA_1);
	private static final float SCALE = 1.2f;
	private static final float RADIUS = 50.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(85, 320);
	private static final WeaponPositioning WEAPON_POSITIONING = new WeaponPositioning((byte) 1, new int[] { 0 },
			new int[] { -40 });
	private static final int TAIL_SIZE = 25;
	private static final int TAIL_DISTANCE = 8;
	private static final float TAIL_SIZEREMOVAL = 0.2f;
	private static final int[] TAIL_POS_X = new int[] {-43, 0, 43};
	private static final int[] TAIL_POS_Y = new int[] {47, 45, 47};
	private static final boolean TAIL_SQUARE = true;
	private static final float COOLDOWN = 50;
	private static final int SCORE = 70;
	private static final int CRYSTALS = 600;
	private static final int PROJECTILE_RANGE = 1500;
	private static final boolean FRIEND = false;

	private static final int OPTIC_LOCK_RANGE = 1000;
	private static final int PHYSICAL_LOCK_RANGE = 1500;
	private static final int LOCK_LOOSE_RANGE = 1200;
	private static final int LOCK_FACE_RANGE = 1000;

	private Rectangle movementBounds;

	public EnemyGamma1(float posX, float posY, Rectangle movementBounds) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new AdvancedSingleProtocol(),
				new MultiCannonWeaponConfiguration(COOLDOWN, SHOOTING_RANGE, WEAPON_POSITIONING), MAX_HP,
				EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE, CRYSTALS, FRIEND, SHIELD, new TailManager(TAIL_SIZE, TAIL_DISTANCE, TAIL_SIZEREMOVAL, TAIL_POS_X, TAIL_POS_Y, TAIL_SQUARE));
		this.movementBounds = movementBounds;
		setProjectilePattern(new ProjectileGamma1());
		setMayShoot(true);
		getAiProtocol().setLockPhysicalDetectionRange(PHYSICAL_LOCK_RANGE);
		getAiProtocol().setLockOpticDetectionRange(OPTIC_LOCK_RANGE);
		getAiProtocol().setLockOutOfRangeRange(LOCK_LOOSE_RANGE);
		getAiProtocol().setLockFaceDistance(LOCK_FACE_RANGE);
		((AdvancedSingleProtocol) getAiProtocol()).setLockAction(FindLockAction.LOCK_LINKED_ENEMYS);
		((AdvancedSingleProtocol) getAiProtocol()).setCombatRangePerOutOfRangeRange(0.8f);
		setNonRecoveryTime(MainZap.inTicks(200));
	}

	@Override
	public Object getClone() {
		return new EnemyGamma1(getPosX(), getPosY(), movementBounds);
	}

}
