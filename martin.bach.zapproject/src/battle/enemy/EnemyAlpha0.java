package battle.enemy;

import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.ArmyCombatProtocol;
import battle.projectile.ProjectileAlpha0;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;
import library.ScheduledList;

public class EnemyAlpha0 extends Enemy {

	private static final float SPEED = 2.3f;
	private static final int MAX_HP = 2400;
	private static final int SHOOTING_RANGE = 300;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_ALPHA0);
	private static final float SCALE = 0.5f;
	private static final float RADIUS = 20.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(12, 50);
	private static final float COOLDOWN = 50;
	private static final int SCORE = 0;
	private static final int CRYSTALS = 1;
	private static final int PROJECTILE_RANGE = 800;
	private static final boolean FRIEND = false;

	private ScheduledList<Enemy> surrounding;

	public EnemyAlpha0(float posX, float posY, ScheduledList<Enemy> surrounding) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new ArmyCombatProtocol(surrounding, SHOOTING_RANGE, true),
				new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE,
				CRYSTALS, FRIEND);
		setProjectilePattern(new ProjectileAlpha0());
		setMayShoot(true);
		getAiProtocol().setPrioritisePlayerAsLockOn(false);
		this.surrounding = surrounding;
	}

	@Override
	public Object getClone() {
		return new EnemyAlpha0(getPosX(), getPosY(), surrounding);
	}

}
