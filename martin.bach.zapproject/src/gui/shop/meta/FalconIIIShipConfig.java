package gui.shop.meta;

import java.awt.image.BufferedImage;

import battle.WeaponPositioning;
import battle.projectile.ProjectileDesign;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class FalconIIIShipConfig extends ShipStartConfig {

	private static final String NAME = "Falcon III";
	private static final String DESCRIPTION = "Known to be an established, basic fighter which is easy to control.";
	private static final int PRICE = 500;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_FALCONIII);
	private static final float SCALE = 0.85f;
	private static final float AMMO_USAGE = 1.8f;
	private static final int DAMAGE = 85;
	private static final float SPEED = 3.5f;
	private static final float PROJ_SPEED = 13;
	private static final float RELOAD_WITH = 2.5f;
	private static final float RELOAD_WITHOUT = 4.3f;
	private static final int HP = 12500;
	private static final int HIT_RANGE = 40;
	private static final int PROJ_RANGE = 840;
	private static final ProjectileDesign PROJ_DESIGN = new ProjDesignFalconIII();
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(60, 600);
	private static final CollisionInformation COLL_INFO = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_AS_PLAYER, true);
	private static final WeaponPositioning WEAPON_POSITIONS_SINGLE = new WeaponPositioning((byte) 1, new int[] { 0 },
			new int[] { -40 });
	private static final WeaponPositioning WEAPON_POSITIONS_DOUBLE = new WeaponPositioning((byte) 2,
			new int[] { -28, 28 }, new int[] { -17, -17 });
	private static final WeaponPositioning WEAPON_POSITIONS_TRIPLE = new WeaponPositioning((byte) 3,
			new int[] { -28, 0, 28 }, new int[] { -17, -40, -17 });
	
	public FalconIIIShipConfig() {
		super(TEXTURE, SCALE, DAMAGE, SPEED, PROJ_SPEED, RELOAD_WITH, RELOAD_WITHOUT, HP, COLL_INFO, PROJ_RANGE,
				PROJ_DESIGN, EXPL_PATTERN, AMMO_USAGE, NAME, DESCRIPTION, PRICE, WEAPON_POSITIONS_SINGLE,
				WEAPON_POSITIONS_DOUBLE, WEAPON_POSITIONS_TRIPLE);
	}

}
