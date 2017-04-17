package gui.shop.meta;

import java.awt.image.BufferedImage;

import battle.projectile.ProjectileDesign;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class DeltaVIShipConfig extends ShipStartConfig {

	private static final String NAME = "Delta VI";
	private static final String DESCRIPTION = "The sixth generation of a fast fighter with powerful weapons, but less amour to be lighter.";
	private static final int PRICE = 450;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_DELTAVI);
	private static final float SCALE = 1.0f;
	private static final float AMMO_USAGE = 3.0f;
	private static final int DAMAGE = 400;
	private static final float SPEED = 4.2f;
	private static final float PROJ_SPEED = 16;
	private static final float RELOAD_WITH = 1.4f;
	private static final float RELOAD_WITHOUT = 2.5f;
	private static final int HP = 30000;
	private static final int HIT_RANGE = 35;
	private static final int PROJ_RANGE = 600;
	private static final ProjectileDesign PROJ_DESIGN = new ProjDesignDeltaVI();
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(60, 600);
	private static final CollisionInformation COLL_INFO = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_AS_PLAYER, true);

	public DeltaVIShipConfig() {
		super(TEXTURE, SCALE, DAMAGE, SPEED, PROJ_SPEED, RELOAD_WITH, RELOAD_WITHOUT, HP, COLL_INFO, PROJ_RANGE,
				PROJ_DESIGN, EXPL_PATTERN, AMMO_USAGE, NAME, DESCRIPTION, PRICE);
	}

}
