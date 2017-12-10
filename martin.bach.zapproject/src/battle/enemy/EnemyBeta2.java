package battle.enemy;

import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.BasicSingleProtocol;
import battle.projectile.Projectile;
import battle.projectile.ProjectileBeta2;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import io.TextureBuffer;

public class EnemyBeta2 extends Enemy {

	private static final float SPEED = 4.3f;
	private static final int MAX_HP = 120;
	private static final int SHOOTING_RANGE = 900;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYSHIP_BETA2);
	private static final float SCALE = 0.7f;
	private static final float RADIUS = 27.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(19, 60);
	private static final int TAIL_SIZE = 20;
	private static final int TAIL_DISTANCE = 2;
	private static final float TAIL_SIZEREMOVAL = 0.4f;
	private static final int[] TAIL_POS_X = new int[] {0};
	private static final int[] TAIL_POS_Y = new int[] {18};
	private static final boolean TAIL_SQUARE = false;
	private static final float COOLDOWN = 2;
	private static final int SCORE = 7;
	private static final int CRYSTALS = 4;
	private static final int PROJECTILE_RANGE = 900;
	private static final boolean FRIEND = false;
	
	private static final int SHOOT_STOPPING_TIME = MainZap.inTicks(2000);

	
	private int shootingPreperationTime = 0;
	
	public EnemyBeta2(float posX, float posY) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new BasicSingleProtocol(),
				new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE,
				CRYSTALS, FRIEND, new TailManager(TAIL_SIZE, TAIL_DISTANCE, TAIL_SIZEREMOVAL, TAIL_POS_X, TAIL_POS_Y, TAIL_SQUARE));
		setProjectilePattern(new ProjectileBeta2());
		setMayShoot(true);
	}

	@Override
	public Object getClone() {
		return new EnemyBeta2(getPosX(), getPosY());
	}

	@Override
	public void updateShooting() {
		if (!mayShoot())
			return; // Schießen nicht erwünscht

		getWeaponConfiguration().update();

		// Technischer Teil ---------
		if (getShootingAim() == null)
			return; // Kein ziel
		if (!getShootingAim().isInRange(getLocX(), getLocY(), getWeaponConfiguration().getRange()))
			return; // Außer Reichweite
		if (getWeaponConfiguration().isReady()) {
			
			
			if (shootingPreperationTime > 0) {
				shootingPreperationTime--;
				return;
			}
			
			
			getAiProtocol().setMoving(false);
			Projectile p = new ProjectileBeta2();
			getWeaponConfiguration().fire(p, this, false);
			shootingPreperationTime = SHOOT_STOPPING_TIME / 2;
			getAiProtocol().waitTicks(shootingPreperationTime);
		}
	}

}
