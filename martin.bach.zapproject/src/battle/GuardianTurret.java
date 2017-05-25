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
	public static final int SHOOTING_RANGE = 700;
	private static final BufferedImage[] TEXTURES = { TextureBuffer.get(TextureBuffer.NAME_FRIENDTURRET_SHOP0),
			TextureBuffer.get(TextureBuffer.NAME_FRIENDTURRET_SHOP1) };
	private static final float[] SCALES = { 1.2f, 1.2f };
	private static final float[] RADIANS = { 50.0f, 75.0f };
	private static final CollisionInformation[] COLINFOS = {
			new CollisionInformation(RADIANS[0], CollisionType.COLLIDE_WITH_ENEMYS, false),
			new CollisionInformation(RADIANS[1], CollisionType.COLLIDE_WITH_ENEMYS, false) };
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = null;
	private static final float COOLDOWN = 10;
	private static final WeaponPositioning[] WEAPON_POSITIONS = {
			new WeaponPositioning((byte) 1, new int[] { 0 }, new int[] { -55 }),
			new WeaponPositioning((byte) 2, new int[] { -22, 22 }, new int[] { -70, -70 }) };
	private static final MultiCannonWeaponConfiguration[] WEAPON_CONFIGS = {
			new MultiCannonWeaponConfiguration(COOLDOWN, SHOOTING_RANGE, WEAPON_POSITIONS[0]),
			new MultiCannonWeaponConfiguration(COOLDOWN, SHOOTING_RANGE, WEAPON_POSITIONS[1]) };
	private static final int SCORE = 0;
	private static final int CRYSTALS = 0;
	private static final boolean FRIEND = true;
	private static final float ROTATION_SPEED = 0.002f;

	private int projectileSize;
	private int type = 0;

	public GuardianTurret(float posX, float posY, int projectileSize, int img) {
		super(posX, posY, SPEED, TEXTURES[img], SCALES[img], COLINFOS[img], new GuardianTurretProtocol(),
				WEAPON_CONFIGS[img], MAX_HP, EXPL_EFFECT_PATTERN, SCORE, 2 * SHOOTING_RANGE, CRYSTALS, FRIEND);
		// 2 * SCHOOTING_RANGE, da mahcnmal trotz lock (quadratisch) nicht
		// schießt. (Außer reichweite) -> Überdimensionale Reichweite
		// 2 * SHOOTING_RANGE, damit Kugeln nicht einfach verpuffen
		setProjectilePattern(new GuardianProjectile(projectileSize));
		this.projectileSize = projectileSize;
		setMayShoot(true);
		type = img;
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
		return new GuardianTurret(getPosX(), getPosY(), projectileSize, type);
	}

	public int getProjectileSize() {
		return projectileSize;
	}

	public void setProjectileSize(int projectileSize) {
		this.projectileSize = projectileSize;
		setProjectilePattern(new GuardianProjectile(projectileSize));
	}

	public int getType() {
		return type;
	}

}
