package battle.enemy;

import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.FindLockAction;
import battle.projectile.ProjectileRaiderBasic;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import io.TextureBuffer;

public class EnemyBasicRaider extends Enemy {

	private static final float SPEED = 3.5f;
	private static final int MAX_HP = 400;
	private static final int SHOOTING_RANGE = 900;
	private static final BufferedImage[] TEXTURES = new BufferedImage[] {
			TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_RAIDER_BASIC_0),
			TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_RAIDER_BASIC_1),
			TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_RAIDER_BASIC_2),
			TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_RAIDER_BASIC_3) };
	private static final float SCALE = 0.5f;
	private static final float RADIUS = 24.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(30, 60);
	private static final int TAIL_SIZE = 16;
	private static final int TAIL_DISTANCE = 2;
	private static final float TAIL_SIZEREMOVAL = 0.55f;
	private static final int[] TAIL_POS_X = new int[] {0};
	private static final int[] TAIL_POS_Y = new int[] {20};
	private static final boolean TAIL_SQUARE = false;
	private static final float COOLDOWN = 30.0f;
	private static final int SCORE = 20;
	private static final int CRYSTALS = 13;
	private static final int PROJECTILE_RANGE = 900;
	private static final boolean FRIEND = false;

	public EnemyBasicRaider() {
		super(0, 0, SPEED, TEXTURES[MainZap.rand(TEXTURES.length)], SCALE, COLINFO, new AdvancedSingleProtocol(),
				new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE,
				CRYSTALS, FRIEND, new TailManager(TAIL_SIZE, TAIL_DISTANCE, TAIL_SIZEREMOVAL, TAIL_POS_X, TAIL_POS_Y, TAIL_SQUARE));
		setNoWaitAfterWarp(true);
		setPreAiming(true);
		setMayShoot(true);
		setProjectilePattern(new ProjectileRaiderBasic());
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
