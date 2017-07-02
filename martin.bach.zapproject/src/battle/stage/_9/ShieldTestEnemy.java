package battle.stage._9;

import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.BasicSingleProtocol;
import battle.enemy.ShieldedEnemy;
import battle.projectile.ProjectileBeta1;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class ShieldTestEnemy extends ShieldedEnemy {

	private static final float SPEED = 1.4f;
	private static final int MAX_HP = 5_000;
	private static final int SHIELD = 20_000;
	private static final int SHOOTING_RANGE = 500;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_BETA1);
	private static final float SCALE = 0.7f;
	private static final float RADIUS = 40.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(27, 100);
	private static final float COOLDOWN = 70;
	private static final int SCORE = 8;
	private static final int CRYSTALS = 9;
	private static final int PROJECTILE_RANGE = 900;
	private static final boolean FRIEND = false;

	public ShieldTestEnemy(float posX, float posY) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new BasicSingleProtocol(),
				new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE,
				CRYSTALS, FRIEND, SHIELD);
		setProjectilePattern(new ProjectileBeta1());
		setMayShoot(true);
	}

	@Override
	public Object getClone() {
		return new ShieldTestEnemy(getPosX(), getPosY());
	}

}
