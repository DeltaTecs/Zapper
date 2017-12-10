package battle.stage._10;

import java.awt.image.BufferedImage;

import battle.MultiCannonWeaponConfiguration;
import battle.WeaponPositioning;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.FindLockAction;
import battle.enemy.Enemy;
import battle.projectile.ProjectileFriendGamma0;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import io.TextureBuffer;

public class FriendGamma0 extends Enemy {

	private static final float SPEED = 8.0f;
	private static final int MAX_HP = 4_000;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_FRIENDSHIP_GAMMA_0);
	private static final float SCALE = 0.85f;
	private static final float RADIUS = 40.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(50, 200);
	private static final WeaponPositioning WEAPON_POSITIONING = new WeaponPositioning((byte) 2, new int[] { -30, 30 },
			new int[] { -28, -28 });
	private static final int TAIL_SIZE = 20;
	private static final int TAIL_DISTANCE = 5;
	private static final float TAIL_SIZEREMOVAL = 0.4f;
	private static final int[] TAIL_POS_X = new int[] {0};
	private static final int[] TAIL_POS_Y = new int[] {20};
	private static final boolean TAIL_SQUARE = true;
	private static final float COOLDOWN = 5.0f;
	private static final int SCORE = 0;
	private static final int CRYSTALS = 0;
	private static final int PROJECTILE_RANGE = 450;
	private static final boolean FRIEND = true;

	public FriendGamma0() {
		super(0, 0, SPEED, TEXTURE, SCALE, COLINFO, new AdvancedSingleProtocol(),
				new MultiCannonWeaponConfiguration(COOLDOWN, PROJECTILE_RANGE, WEAPON_POSITIONING), MAX_HP,
				EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE, CRYSTALS, FRIEND, new TailManager(TAIL_SIZE, TAIL_DISTANCE, TAIL_SIZEREMOVAL, TAIL_POS_X, TAIL_POS_Y, TAIL_SQUARE));
		setNoWaitAfterWarp(true);
		setPreAiming(true);
		setMayShoot(true);
		setProjectilePattern(new ProjectileFriendGamma0());
		getAiProtocol().setDamageRecognizeable();
		getAiProtocol().setLockFaceDistance(300);
		getAiProtocol().setLockOutOfRangeRange(700);
		getAiProtocol().setLockOpticDetectionRange(300);
		getAiProtocol().setLockPhysicalDetectionRange(1000);
		getAiProtocol().setLockStopRange(50);
		((AdvancedSingleProtocol) getAiProtocol()).setLockCombatFreeMovementRange(200);
		((AdvancedSingleProtocol) getAiProtocol()).setCombatRangePerOutOfRangeRange(0.8f);
		((AdvancedSingleProtocol) getAiProtocol()).setLockAction(FindLockAction.LOCK_LINKED_ENEMYS);
	}

}
