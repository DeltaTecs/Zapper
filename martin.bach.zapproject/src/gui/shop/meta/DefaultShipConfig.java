package gui.shop.meta;

import java.awt.image.BufferedImage;

import battle.projectile.ProjectileDesign;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class DefaultShipConfig extends ShipStartConfig {

	private static final String NAME = "Explorer V";
	private static final String DESCRIPTION = "A cheap and universal ship.";
	private static final int PRICE = 0;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_DEFAULT);
	private static final float SCALE = 1.0f;
	private static final float AMMO_USAGE = 1.8f;
	private static final int DAMAGE = 30;
	private static final float SPEED = 3.5f;
	private static final float PROJ_SPEED = 12;
	private static final float RELOAD_WITH = 3.0f;
	private static final float RELOAD_WITHOUT = 4.5f;
	private static final int HP = 8000;
	private static final int HIT_RANGE = 30;
	private static final int PROJ_RANGE = 800;
	private static final ProjectileDesign PROJ_DESIGN = new ProjDesignDefault();
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(60, 600);
	private static final CollisionInformation COLL_INFO = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_AS_PLAYER, true);

	public DefaultShipConfig() {
		super(TEXTURE, SCALE, DAMAGE, SPEED, PROJ_SPEED, RELOAD_WITH, RELOAD_WITHOUT, HP, COLL_INFO, PROJ_RANGE,
				PROJ_DESIGN, EXPL_PATTERN, AMMO_USAGE, NAME, DESCRIPTION, PRICE);
	}

}
