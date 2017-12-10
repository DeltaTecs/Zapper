package battle.stage._7;

import java.awt.image.BufferedImage;

import battle.MultiCannonWeaponConfiguration;
import battle.WeaponPositioning;
import battle.ai.BasicTurretProtocol;
import battle.enemy.Enemy;
import battle.projectile.ProjectileTurretRaider;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class EnemyTurretRaider extends Enemy {

	private static final float SPEED = 0;
	private static final int MAX_HP = 4000;
	private static final int SHOOTING_RANGE = 1500;
	private static final BufferedImage[] TEXTURES = new BufferedImage[] {
			TextureBuffer.get(TextureBuffer.NAME_ENEMYTURRET_RAIDER0),
			TextureBuffer.get(TextureBuffer.NAME_ENEMYTURRET_RAIDER1),
			TextureBuffer.get(TextureBuffer.NAME_ENEMYTURRET_RAIDER2),
			TextureBuffer.get(TextureBuffer.NAME_ENEMYTURRET_RAIDER3),
			TextureBuffer.get(TextureBuffer.NAME_ENEMYTURRET_RAIDER4) };
	private static final float SCALE = 0.8f;
	private static final float RADIUS = 40.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(13, 70);
	private static final WeaponPositioning WEAPON_POSITIONING = new WeaponPositioning((byte) 2, new int[] { -15, 15 },
			new int[] { -40, -40 });
	private static final float COOLDOWN = 80;
	private static final int SCORE = 40;
	private static final int CRYSTALS = 250;
	private static final int PROJECTILE_RANGE = 2000;
	private static final boolean FRIEND = false;

	public EnemyTurretRaider(float posX, float posY) {
		super(posX, posY, SPEED, TEXTURES[MainZap.rand(TEXTURES.length)], SCALE, COLINFO, new BasicTurretProtocol(),
				new MultiCannonWeaponConfiguration(COOLDOWN, SHOOTING_RANGE, WEAPON_POSITIONING), MAX_HP,
				EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE, CRYSTALS, FRIEND, null);
		setProjectilePattern(new ProjectileTurretRaider());
		setMayShoot(true);
		getAiProtocol().setLockOn(MainZap.getPlayer());
	}

	@Override
	public Object getClone() {
		return new EnemyTurretRaider(getPosX(), getPosY());
	}

}
