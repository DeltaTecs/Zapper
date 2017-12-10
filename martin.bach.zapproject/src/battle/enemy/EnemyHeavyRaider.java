package battle.enemy;

import java.awt.image.BufferedImage;

import battle.MultiCannonWeaponConfiguration;
import battle.WeaponPositioning;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.FindLockAction;
import battle.projectile.ProjectileRaiderHeavy;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import io.TextureBuffer;

public class EnemyHeavyRaider extends Enemy {

	private static final float SPEED = 1.6f;
	private static final int MAX_HP = 1200;
	private static final int SHOOTING_RANGE = 1000;
	private static final BufferedImage[] TEXTURES = new BufferedImage[] {
			TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_RAIDER_HEAVY_0),
			TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_RAIDER_HEAVY_1), };
	private static final float SCALE = 0.8f;
	private static final float RADIUS = 40.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(30, 150);
	private static final int TAIL_SIZE = 20;
	private static final int TAIL_DISTANCE = 8;
	private static final float TAIL_SIZEREMOVAL = 0.25f;
	private static final int[] TAIL_POS_X = new int[] {-20, 20};
	private static final int[] TAIL_POS_Y = new int[] {30, 30};
	private static final boolean TAIL_SQUARE = true;
	private static final float COOLDOWN = 20.0f;
	private static final WeaponPositioning WEAPON_POSITIONING = new WeaponPositioning((byte) 2, new int[] { -10, 10 },
			new int[] { -20, -20 });
	private static final int SCORE = 30;
	private static final int CRYSTALS = 28;
	private static final int PROJECTILE_RANGE = 900;
	private static final boolean FRIEND = false;

	public EnemyHeavyRaider() {
		super(0, 0, SPEED, TEXTURES[MainZap.rand(TEXTURES.length)], SCALE, COLINFO, new AdvancedSingleProtocol(),
				new MultiCannonWeaponConfiguration(COOLDOWN, SHOOTING_RANGE, WEAPON_POSITIONING), MAX_HP,
				EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE, CRYSTALS, FRIEND, new TailManager(TAIL_SIZE, TAIL_DISTANCE, TAIL_SIZEREMOVAL, TAIL_POS_X, TAIL_POS_Y, TAIL_SQUARE));
		setNoWaitAfterWarp(true);
		setPreAiming(true);
		setMayShoot(true);
		setProjectilePattern(new ProjectileRaiderHeavy());
		getAiProtocol().setDamageRecognizeable();
		getAiProtocol().setLockFaceDistance(300);
		getAiProtocol().setLockOutOfRangeRange(400);
		getAiProtocol().setLockOpticDetectionRange(300);
		getAiProtocol().setLockPhysicalDetectionRange(1000);
		getAiProtocol().setLockStopRange(50);
		((AdvancedSingleProtocol) getAiProtocol()).setLockCombatFreeMovementRange(200);
		((AdvancedSingleProtocol) getAiProtocol()).setCombatRangePerOutOfRangeRange(0.8f);
		((AdvancedSingleProtocol) getAiProtocol()).setLockAction(FindLockAction.LOCK_LOCK_OF_LINKED_FRIENDS);
	}

}
