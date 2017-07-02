package battle.stage._9;

import java.awt.image.BufferedImage;

import battle.MultiCannonWeaponConfiguration;
import battle.WeaponPositioning;
import battle.ai.BasicTurretProtocol;
import battle.enemy.Enemy;
import battle.projectile.ProjectileTurretAlpha;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class FriendTurretAlpha extends Enemy {

	private static final float SPEED = 0;
	private static final int MAX_HP = 100000;
//	private static final int SHOOTING_RANGE = 1000;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_FRIENDTURRET_ALPHA);
	private static final float SCALE = 0.9f;
	private static final float RADIUS = 55.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, false);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(13, 70);
	private static final WeaponPositioning WEAPON_POSITIONING = new WeaponPositioning((byte) 3,
			new int[] { -24, 0, 24 }, new int[] { -105, -115, -105 });
	private static final float COOLDOWN = 10f;
	private static final int SCORE = 0;
	private static final int CRYSTALS = 0;
	private static final int PROJECTILE_RANGE = 1200;
	private static final boolean FRIEND = true;
	
	private static final int SHOCK_TIMEOUT = MainZap.inTicks(4000);
	private static final int LOCK_OPTIC_DETECTION = 1000;
	private static final int LOCK_FACE = 1100;
	private static final int LOCK_PHYSICAL_DETECTION = 1500;
	
	private int timeSinceLastShock = SHOCK_TIMEOUT + 1;

	public FriendTurretAlpha(float posX, float posY) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new BasicTurretProtocol(),
				new MultiCannonWeaponConfiguration(COOLDOWN, PROJECTILE_RANGE, WEAPON_POSITIONING), MAX_HP,
				EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE, CRYSTALS, FRIEND);
		setProjectilePattern(new ProjectileTurretAlpha());
		setMayShoot(true);
		getAiProtocol().setSelfRotating(true);
		getAiProtocol().allowFacePlayer(false);
		getAiProtocol().setLockOpticDetectionRange(LOCK_OPTIC_DETECTION);
		getAiProtocol().setLockPhysicalDetectionRange(LOCK_PHYSICAL_DETECTION);
		getAiProtocol().setLockFaceDistance(LOCK_FACE);
		setMaxMiddistance(80);
	}
	
	@Override
	public void update() {
		
		if (timeSinceLastShock < SHOCK_TIMEOUT) {
			timeSinceLastShock++;
			setShocked(false);
			return;
		}
		
		super.update();
	}
	
	@Override
	public void shock() { // hat eigenen shock
		setShocked(true);
		timeSinceLastShock = 0;
	}

	@Override
	public Object getClone() {
		return new FriendTurretAlpha(getPosX(), getPosY());
	}

}
