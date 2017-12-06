package gui.shop.meta;

import java.awt.image.BufferedImage;

import battle.WeaponPositioning;
import battle.projectile.ProjectileDesign;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class DeltaVIShipConfig extends ShipStartConfig {

	private static final String NAME = "Delta VI";
	private static final String DESCRIPTION = "The sixth generation of a fast fighter with powerful weapons, but less amour to be lighter. The Delta VI is known to be a rather skill-based fighter.";
	private static final int PRICE = 450;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_DELTAVI);
	private static final float SCALE = 1.0f;
	private static final float AMMO_USAGE = 3.0f;
	private static final int DAMAGE = 70;
	private static final float SPEED = 4.8f;
	private static final float PROJ_SPEED = 16;
	private static final float RELOAD_WITH = 1.2f;
	private static final float RELOAD_WITHOUT = 2.5f;
	private static final int HP = 4500;
	private static final int HIT_RANGE = 35;
	private static final int PROJ_RANGE = 300;
	private static final ProjectileDesign PROJ_DESIGN = new ProjDesignDeltaVI();
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(60, 600);
	private static final CollisionInformation COLL_INFO = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_AS_PLAYER, true);
	private static final WeaponPositioning WEAPON_POSITIONS_SINGLE = new WeaponPositioning((byte) 1, new int[] { 0 },
			new int[] { -30 });
	private static final WeaponPositioning WEAPON_POSITIONS_DOUBLE = new WeaponPositioning((byte) 2,
			new int[] { -43, 43 }, new int[] { -6, -6 });
	private static final WeaponPositioning WEAPON_POSITIONS_TRIPLE = new WeaponPositioning((byte) 3,
			new int[] { -43, 0, 43 }, new int[] { -6, -30, -6 });

	public DeltaVIShipConfig() {
		super(TEXTURE, SCALE, DAMAGE, SPEED, PROJ_SPEED, RELOAD_WITH, RELOAD_WITHOUT, HP, COLL_INFO, PROJ_RANGE,
				PROJ_DESIGN, EXPL_PATTERN, AMMO_USAGE, NAME, DESCRIPTION, PRICE, WEAPON_POSITIONS_SINGLE,
				WEAPON_POSITIONS_DOUBLE, WEAPON_POSITIONS_TRIPLE);
	}

}
