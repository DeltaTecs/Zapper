package battle.stage._3;

import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.BasicTurretProtocol;
import battle.enemy.Enemy;
import battle.projectile.ProjectileTurretAlpha0;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class EnemyTurretAlpha0 extends Enemy {

	private static final float SPEED = 0;
	private static final int MAX_HP = 30000;
	private static final int SHOOTING_RANGE = 600;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYTURRET_ALPHA0);
	private static final float SCALE = 0.6f;
	private static final float RADIUS = 30.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(13, 70);
	private static final float COOLDOWN = 40;
	private static final int SCORE = 20;
	private static final int CRYSTALS = 14;
	private static final int PROJECTILE_RANGE = 800;
	private static final boolean FRIEND = false;

	public EnemyTurretAlpha0(float posX, float posY) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new BasicTurretProtocol(),
				new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE,
				CRYSTALS, FRIEND);
		setProjectilePattern(new ProjectileTurretAlpha0());
		setMayShoot(true);
	}

	@Override
	public Object getClone() {
		return new EnemyTurretAlpha0(getPosX(), getPosY());
	}

}
