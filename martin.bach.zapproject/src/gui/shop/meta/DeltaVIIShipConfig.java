package gui.shop.meta;

import java.awt.image.BufferedImage;

import battle.projectile.ProjectileDesign;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class DeltaVIIShipConfig extends ShipStartConfig {

	private static final String NAME = "Delta VII";
	private static final String DESCRIPTION = "The seventh generation of a fast fighter with powerful weapons, but less amour to be lighter. In the right hands, this prototype becomes the most dangerous ship ever built.";
	private static final int PRICE = 2400;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_DELTAVII);
	private static final float SCALE = 1.0f;
	private static final float AMMO_USAGE = 3.5f;
	private static final int DAMAGE = 600;
	private static final float SPEED = 5.3f;
	private static final float PROJ_SPEED = 20;
	private static final float RELOAD_WITH = 0.9f;
	private static final float RELOAD_WITHOUT = 2.3f;
	private static final int HP = 80000;
	private static final int HIT_RANGE = 40;
	private static final int PROJ_RANGE = 680;
	private static final ProjectileDesign PROJ_DESIGN = new ProjDesignDeltaVII();
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(60, 600);
	private static final CollisionInformation COLL_INFO = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_AS_PLAYER, true);

	public DeltaVIIShipConfig() {
		super(TEXTURE, SCALE, DAMAGE, SPEED, PROJ_SPEED, RELOAD_WITH, RELOAD_WITHOUT, HP, COLL_INFO, PROJ_RANGE,
				PROJ_DESIGN, EXPL_PATTERN, AMMO_USAGE, NAME, DESCRIPTION, PRICE);
	}

}
