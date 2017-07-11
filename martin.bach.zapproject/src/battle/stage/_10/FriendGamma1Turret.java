package battle.stage._10;

import java.awt.image.BufferedImage;

import battle.CombatObject;
import battle.MultiCannonWeaponConfiguration;
import battle.WeaponPositioning;
import battle.ai.BasicTurretProtocol;
import battle.enemy.AttachedEnemy;
import battle.projectile.ProjectileFriendGamma1Turret;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class FriendGamma1Turret extends AttachedEnemy {

	private static final float SPEED = 0f;
	private static final int MAX_HP = 1000;
	private static final int SHOOTING_RANGE = 1500;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_FRIENDSHIP_GAMMA_1_TURRET);
	private static final float SCALE = 0.85f;
	private static final float RADIUS = 30.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS, CollisionType.DO_NOT_COLLIDE,
			false);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(90, 180);
	private static final WeaponPositioning WEAPON_POSITIONING = new WeaponPositioning((byte) 2, new int[] { -6, 6 },
			new int[] { -50, -50 });
	private static final float COOLDOWN = 1.0f;
	private static final int SCORE = 0;
	private static final int CRYSTALS = 0;
	private static final int PROJECTILE_RANGE = 1500;
	private static final boolean FRIEND = true;

	private static final int OPTIC_LOCK_RANGE = 1500;
	private static final int PHYSICAL_LOCK_RANGE = 2000;
	private static final int LOCK_LOOSE_RANGE = 2500;
	private static final int LOCK_FACE_RANGE = 1500;

	public FriendGamma1Turret(float dx, float dy, CombatObject host) {
		super(SPEED, TEXTURE, SCALE, COLINFO, new BasicTurretProtocol(),
				new MultiCannonWeaponConfiguration(COOLDOWN, SHOOTING_RANGE, WEAPON_POSITIONING), MAX_HP,
				EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE, CRYSTALS, FRIEND, host, new float[] { dx, dy });
		setProjectilePattern(new ProjectileFriendGamma1Turret());
		setMayShoot(true);
		setPreAiming(false); // Hardcore: pre-aim anschalten. -> Lieber aus lassen
		getAiProtocol().setLockPhysicalDetectionRange(PHYSICAL_LOCK_RANGE);
		getAiProtocol().setLockOpticDetectionRange(OPTIC_LOCK_RANGE);
		getAiProtocol().setLockOutOfRangeRange(LOCK_LOOSE_RANGE);
		getAiProtocol().setLockFaceDistance(LOCK_FACE_RANGE);
	}

	@Override
	public Object getClone() {
		return new FriendGamma1Turret(getDx(), getDy(), getHost());
	}
	

}
