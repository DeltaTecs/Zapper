package gui.shop.meta;

import java.awt.image.BufferedImage;

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
	private static final int DAMAGE = 1000;
	private static final float SPEED = 3.6f;
	private static final float PROJ_SPEED = 14;
	private static final float RELOAD_WITH = 2.0f;
	private static final float RELOAD_WITHOUT = 4.5f;
	private static final int HP = 190000;
	private static final int HIT_RANGE = 52;
	private static final int PROJ_RANGE = 800;
	private static final ProjectileDesign PROJ_DESIGN = new ProjDesignAshslider();
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(60, 600);
	private static final CollisionInformation COLL_INFO = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_AS_PLAYER, true);

	public AshshliderShipConfig() {
		super(TEXTURE, SCALE, DAMAGE, SPEED, PROJ_SPEED, RELOAD_WITH, RELOAD_WITHOUT, HP, COLL_INFO, PROJ_RANGE,
				PROJ_DESIGN, EXPL_PATTERN, AMMO_USAGE, NAME, DESCRIPTION, PRICE);
	}

}
