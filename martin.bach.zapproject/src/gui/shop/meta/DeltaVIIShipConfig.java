package gui.shop.meta;

import java.awt.image.BufferedImage;

import battle.WeaponPositioning;
import battle.projectile.ProjectileDesign;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import io.TextureBuffer;

public class DeltaVIIShipConfig extends ShipStartConfig {

	private static final String NAME = "Delta VII";
	private static final String DESCRIPTION = "The seventh generation of a fast fighter with powerful weapons, but less amour to be lighter. In the right hands, this prototype becomes the most dangerous ship ever built.";
	private static final int PRICE = 2400;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_DELTAVII);
	private static final int TAIL_SIZE = 22;
	private static final int TAIL_DISTANCE = 0;
	private static final float TAIL_SIZEREMOVAL = 1.1f;
	private static final int[] TAIL_POS_X = new int[] { -16, 16 };
	private static final int[] TAIL_POS_Y = new int[] { 33, 33 };
	private static final boolean TAIL_SQUARE = true;
	private static final TailManager TAILMANAGER = new TailManager(TAIL_SIZE, TAIL_DISTANCE, TAIL_SIZEREMOVAL,
			TAIL_POS_X, TAIL_POS_Y, TAIL_SQUARE);
	private static final float SCALE = 1.0f;
	private static final float AMMO_USAGE = 3.5f;
	private static final int DAMAGE = 105;
	private static final float SPEED = 6.0f;
	private static final float PROJ_SPEED = 20;
	private static final float RELOAD_WITH = 0.9f;
	private static final float RELOAD_WITHOUT = 2.3f;
	private static final int HP = 8000;
	private static final int HIT_RANGE = 40;
	private static final int PROJ_RANGE = 350;
	private static final ProjectileDesign PROJ_DESIGN = new ProjDesignDeltaVII();
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(60, 600);
	private static final CollisionInformation COLL_INFO = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_AS_PLAYER, true);
	private static final WeaponPositioning WEAPON_POSITIONS_SINGLE = new WeaponPositioning((byte) 1, new int[] { 0 },
			new int[] { -30 });
	private static final WeaponPositioning WEAPON_POSITIONS_DOUBLE = new WeaponPositioning((byte) 2,
			new int[] { -39, 39 }, new int[] { -6, -6 });
	private static final WeaponPositioning WEAPON_POSITIONS_TRIPLE = new WeaponPositioning((byte) 3,
			new int[] { -39, 0, 39 }, new int[] { -6, -30, -6 });


	public DeltaVIIShipConfig() {
		super(TEXTURE, TAILMANAGER, SCALE, DAMAGE, SPEED, PROJ_SPEED, RELOAD_WITH, RELOAD_WITHOUT, HP, COLL_INFO, PROJ_RANGE,
				PROJ_DESIGN, EXPL_PATTERN, AMMO_USAGE, NAME, DESCRIPTION, PRICE, WEAPON_POSITIONS_SINGLE,
				WEAPON_POSITIONS_DOUBLE, WEAPON_POSITIONS_TRIPLE);
	}

}
