package gui.shop.meta;

import java.awt.image.BufferedImage;

import battle.WeaponPositioning;
import battle.projectile.ProjectileDesign;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class AshshliderShipConfig extends ShipStartConfig {

	private static final String NAME = "Ashslider";
	private static final String DESCRIPTION = "One of the most powerful one-man battle ships ever made. Heavy on damage, but using up alot of ammunition.";
	private static final int PRICE = 2500;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_ASHSLIDER);
	private static final float SCALE = 0.5f;
	private static final float AMMO_USAGE = 4.0f;
	private static final int DAMAGE = 100;
	private static final float SPEED = 3.6f;
	private static final float PROJ_SPEED = 14;
	private static final float RELOAD_WITH = 2.0f;
	private static final float RELOAD_WITHOUT = 4.5f;
	private static final int HP = 19000;
	private static final int HIT_RANGE = 52;
	private static final int PROJ_RANGE = 800;
	private static final ProjectileDesign PROJ_DESIGN = new ProjDesignAshslider();
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(60, 600);
	private static final CollisionInformation COLL_INFO = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_AS_PLAYER, true);
	private static final WeaponPositioning WEAPON_POSITIONS_SINGLE = new WeaponPositioning((byte) 1, new int[] { 0 },
			new int[] { -44 });
	private static final WeaponPositioning WEAPON_POSITIONS_DOUBLE = new WeaponPositioning((byte) 2,
			new int[] { -30, 30 }, new int[] { -42, -42 });
	private static final WeaponPositioning WEAPON_POSITIONS_TRIPLE = new WeaponPositioning((byte) 3,
			new int[] { -30, 0, 30 }, new int[] { -42, -44, -42 });

	public AshshliderShipConfig() {
		super(TEXTURE, SCALE, DAMAGE, SPEED, PROJ_SPEED, RELOAD_WITH, RELOAD_WITHOUT, HP, COLL_INFO, PROJ_RANGE,
				PROJ_DESIGN, EXPL_PATTERN, AMMO_USAGE, NAME, DESCRIPTION, PRICE, WEAPON_POSITIONS_SINGLE,
				WEAPON_POSITIONS_DOUBLE, WEAPON_POSITIONS_TRIPLE);
	}

}
