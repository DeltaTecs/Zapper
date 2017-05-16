package battle.enemy;

import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.BasicSingleProtocol;
import battle.projectile.ProjectileAlpha0;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class EnemyBeta0 extends Enemy {

	private static final float SPEED = 1.2f;
	private static final int MAX_HP = 300;
	private static final int SHOOTING_RANGE = 300;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_BETA0);
	private static final float SCALE = 0.6f;
	private static final float RADIUS = 35.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(16, 90);
	private static final float COOLDOWN = 40;
	private static final int SCORE = 2;
	private static final int CRYSTALS = 2;
	private static final int PROJECTILE_RANGE = 600;
	private static final boolean FRIEND = false;

	public EnemyBeta0(float posX, float posY) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new BasicSingleProtocol(),
				new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE,
				CRYSTALS, FRIEND);
		setProjectilePattern(new ProjectileAlpha0());
		setMayShoot(true);
	}

	@Override
	public Object getClone() {
		return new EnemyBeta0(getPosX(), getPosY());
	}

}
