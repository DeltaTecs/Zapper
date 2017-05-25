package gui.shop.meta;

import java.awt.image.BufferedImage;

import battle.WeaponPositioning;
import battle.projectile.ProjectileDesign;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class RainmakerShipConfig extends ShipStartConfig {

	private static final String NAME = "Rainmaker";
	private static final String DESCRIPTION = "This ship was the result of a weapon experiment crash. The Rainmaker can only be summoned from the void by using massive amounts of crystals.";
	private static final int PRICE = 800000;
	public static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_RAINMAKER);
	private static final float SCALE = 1.2f;
	private static final float AMMO_USAGE = 4.0f;
	private static final int DAMAGE = 250;
	private static final float SPEED = 8.2f;
	private static final float PROJ_SPEED = 18;
	private static final float RELOAD_WITH = 0.0f;
	private static final float RELOAD_WITHOUT = 1.0f;
	private static final int HP = 100000;
	private static final int HIT_RANGE = 80;
	private static final int PROJ_RANGE = 1000;
	private static final ProjectileDesign PROJ_DESIGN = new ProjDesignRainmaker();
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(600, 2000);
	private static final CollisionInformation COLL_INFO = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_AS_PLAYER, true);
	private static final WeaponPositioning WEAPON_POSITIONS_SINGLE = new WeaponPositioning((byte) 5,
			new int[] { -80, -50, 0, 50, 80 }, new int[] { -5, -50, -75, -50, -5 });
	private static final WeaponPositioning WEAPON_POSITIONS_DOUBLE = new WeaponPositioning((byte) 6,
			new int[] { -80, -50, -30, 30, 50, 80 }, new int[] { -5, -50, -44, -44, -50, -5 });
	private static final WeaponPositioning WEAPON_POSITIONS_TRIPLE = new WeaponPositioning((byte) 7,
			new int[] { -80, -50, -30, 0, 30, 50, 80 }, new int[] { -5, -50, -44, -75, -44, -50, -5 });;

	public RainmakerShipConfig() {
		super(TEXTURE, SCALE, DAMAGE, SPEED, PROJ_SPEED, RELOAD_WITH, RELOAD_WITHOUT, HP, COLL_INFO, PROJ_RANGE,
				PROJ_DESIGN, EXPL_PATTERN, AMMO_USAGE, NAME, DESCRIPTION, PRICE, WEAPON_POSITIONS_SINGLE,
				WEAPON_POSITIONS_DOUBLE, WEAPON_POSITIONS_TRIPLE);
	}

}
