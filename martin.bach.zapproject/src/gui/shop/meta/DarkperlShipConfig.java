package gui.shop.meta;

import java.awt.image.BufferedImage;

import battle.WeaponPositioning;
import battle.projectile.ProjectileDesign;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class DarkperlShipConfig extends ShipStartConfig {

	private static final String NAME = "Dark Perl";
	private static final String DESCRIPTION = "A unique sniper-like battle ship.";
	private static final int PRICE = 1100;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_DARKPERL);
	private static final float SCALE = 0.9f;
	private static final float AMMO_USAGE = 2.0f;
	private static final int DAMAGE = 400;
	private static final float SPEED = 3.0f;
	private static final float PROJ_SPEED = 26;
	private static final float RELOAD_WITH = 6.0f;
	private static final float RELOAD_WITHOUT = 9.5f;
	private static final int HP = 10000;
	private static final int HIT_RANGE = 38;
	private static final int PROJ_RANGE = 1800;
	private static final ProjectileDesign PROJ_DESIGN = new ProjDesignDarkPerl();
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(60, 600);
	private static final CollisionInformation COLL_INFO = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_AS_PLAYER, true);
	private static final WeaponPositioning WEAPON_POSITIONS_SINGLE = new WeaponPositioning((byte) 1, new int[] { 0 },
			new int[] { -38 });
	private static final WeaponPositioning WEAPON_POSITIONS_DOUBLE = new WeaponPositioning((byte) 2,
			new int[] { -35, 35 }, new int[] { -10, -10 });
	private static final WeaponPositioning WEAPON_POSITIONS_TRIPLE = new WeaponPositioning((byte) 3,
			new int[] { -35, 0, 35 }, new int[] { -10, -38, -10 });

	public DarkperlShipConfig() {
		super(TEXTURE, SCALE, DAMAGE, SPEED, PROJ_SPEED, RELOAD_WITH, RELOAD_WITHOUT, HP, COLL_INFO, PROJ_RANGE,
				PROJ_DESIGN, EXPL_PATTERN, AMMO_USAGE, NAME, DESCRIPTION, PRICE, WEAPON_POSITIONS_SINGLE,
				WEAPON_POSITIONS_DOUBLE, WEAPON_POSITIONS_TRIPLE);
	}

}
