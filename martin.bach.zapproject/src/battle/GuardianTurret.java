package battle;

import java.awt.image.BufferedImage;

import battle.ai.GuardianTurretProtocol;
import battle.enemy.Enemy;
import battle.projectile.GuardianProjectile;
import battle.projectile.Projectile;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class GuardianTurret extends Enemy {

	private static final float SPEED = 0;
	private static final int MAX_HP = Integer.MAX_VALUE;
	public static final int SHOOTING_RANGE = 600;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_FRIENDTURRET_SHOP0);
	private static final float SCALE = 1.2f;
	private static final float RADIUS = 50.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, false);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = null;
	private static final float COOLDOWN = 10;
	private static final int SCORE = 0;
	private static final int CRYSTALS = 0;
	private static final boolean FRIEND = true;
	private static final float ROTATION_SPEED = 0.002f;

	private int projectileSize;

	public GuardianTurret(float posX, float posY, int projectileSize) {
		super(posX, posY, SPEED, TEXTURE, SCALE, COLINFO, new GuardianTurretProtocol(),
				new WeaponConfiguration(COOLDOWN, 2 * SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE,
				2 * SHOOTING_RANGE, CRYSTALS, FRIEND);
		// 2 * SCHOOTING_RANGE, da mahcnmal trotz lock (quadratisch) nicht
		// schießt. (Außer reichweite) -> Überdimensionale Reichweite
		// 2 * SHOOTING_RANGE, damit Kugeln nicht einfach verpuffen
		setProjectilePattern(new GuardianProjectile(projectileSize));
		this.projectileSize = projectileSize;
		setMayShoot(true);
	}

	@Override
	public void damage(int damage, Projectile p) {
		// nix machen. unsterblich
	}

	@Override
	public void updateRotation() {
		if (getShootingAim() == null) {
			setRotation(getRotation() + ROTATION_SPEED);
			if (getRotation() >= Math.PI * 2)
				setRotation(getRotation() - Math.PI * 2);
			return;
		}

		super.updateRotation();
	}

	@Override
	public Object getClone() {
		return new GuardianTurret(getPosX(), getPosY(), projectileSize);
	}

	public int getProjectileSize() {
		return projectileSize;
	}

	public void setProjectileSize(int projectileSize) {
		this.projectileSize = projectileSize;
		setProjectilePattern(new GuardianProjectile(projectileSize));
	}

}
