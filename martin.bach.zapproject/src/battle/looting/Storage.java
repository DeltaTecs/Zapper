package battle.looting;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import battle.enemy.Enemy;
import battle.projectile.Projectile;
import battle.stage.StageManager;
import collision.Collideable;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.Crystal;
import gui.effect.ExplosionEffect;
import gui.effect.ExplosionEffectPattern;
import gui.extention.Extention;
import gui.extention.ExtentionManager;
import gui.shop.Shop;
import io.LootReader;
import io.TextureBuffer;

public class Storage extends Enemy {

	private static final float SCALE = 2.0f;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_CONTAINER);
	private static final int HEALTH = 600;
	private static final int MIN_CRYSTALS_PER_STAGE = 10;
	private static final int MAX_CRYSTALS_PER_STAGE = 30;

	private static final int RAND_CONTAINER_SPOTTER = 7;
	private static final int RAND_DIRECT_EXTENTION = 16;
	private static final int RAND_LICENSE_SHIP = 7;
	private static final int RAND_LICENSE_EXTENTION = 8;
	private static final int RAND_LICENSE_UPGRADE = 3;

	public Storage(int posX, int posY) {
		super(posX, posY, 0, TEXTURE, SCALE, new CollisionInformation(30, CollisionType.COLLIDE_WITH_FRIENDS, false),
				null, null, HEALTH, new ExplosionEffectPattern(5, 50), 0, 0, 0, false, null);
	}

	@Override
	public void die() {
		super.die();

		int crystalAmount = MainZap.rand(MAX_CRYSTALS_PER_STAGE - MIN_CRYSTALS_PER_STAGE) + MIN_CRYSTALS_PER_STAGE;
		crystalAmount *= StageManager.getActiveStage().getLvl() == 0 ? 1 : StageManager.getActiveStage().getLvl();
		Crystal.spawn(this.getLocX(), this.getLocY(), crystalAmount, 100);

		// loot checken:

		// --- Direkte Extention
		if (MainZap.rand(RAND_DIRECT_EXTENTION) == 0 && ExtentionManager.getExtention() == null) {
			System.out.println("Dropping DIRECT Extention");

			int r = MainZap.rand(3);
			final Extention[] e = new Extention[1];
			String t;
			BufferedImage i;
			if (r == 0) {
				e[0] = Extention.MIRROR;
				t = "Mirror Extention";
				i = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_MIRROR);
			} else if (r == 1) {
				e[0] = Extention.SHIELD;
				t = "Shield Extention";
				i = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_SHIELD);
			} else {
				e[0] = Extention.SHOCK;
				t = "Shock Extention";
				i = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_SHOCK);
			}

			SpecialCollectable c = new SpecialCollectable(i, 1, t, 2, new Runnable() {
				@Override
				public void run() {
					ExtentionManager.setExtention(e[0]);
				}
			});
			int[] coors = ExplosionEffect.getRandCircleCoordinates(100);
			c.setPosition(coors[0] + this.getLocX(), coors[1] + this.getLocY());
			c.register();

			return;
		}

		// --- Container Detector
		if (MainZap.rand(RAND_CONTAINER_SPOTTER) == 0 && !Shop.unlocked[0]) {

			System.out.println("Dropping container detector");
			SpecialCollectable c = new SpecialCollectable(
					TextureBuffer.get(TextureBuffer.NAME_COLLECT_CONTAINER_SPOTTER), 3, "Container Detector", 0,
					new Runnable() {
						@Override
						public void run() {
							Shop.unlocked[0] = true;
							LootReader.save();
						}
					});
			int[] coors = ExplosionEffect.getRandCircleCoordinates(100);
			c.setPosition(coors[0] + this.getLocX(), coors[1] + this.getLocY());
			c.register();
			return;
		}

		// ---- Lizenz Schiff
		if (MainZap.rand(RAND_LICENSE_SHIP) == 0) {
			System.out.println("Dropping license Ship");
			final int[] r = new int[] { MainZap.rand(3) };
			String t = "err";
			BufferedImage i = null;
			float scale[] = new float[] { 1.0f };
			if (r[0] == 0 && !Shop.unlocked[8]) {
				t = "Delta VI Ship License";
				i = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_DELTAVI);
			} else if (r[0] == 1 && !Shop.unlocked[9]) {
				t = "Dark Perl Ship License";
				i = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_DARKPERL);
				scale[0] = 0.5f;
			} else if (r[0] == 2 && !Shop.unlocked[10]) {
				t = "Ashslider Ship License";
				i = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_ASHSLIDER);
				scale[0] = 0.25f;
			} else
				return;// schon alle Freigeschaltet

			SpecialCollectable c = new SpecialCollectable(i, scale[0], t, 0, new Runnable() {
				@Override
				public void run() {
					Shop.unlocked[r[0] + 8] = true;
					LootReader.save();
				}
			});
			int[] coors = ExplosionEffect.getRandCircleCoordinates(100);
			c.setPosition(coors[0] + this.getLocX(), coors[1] + this.getLocY());
			c.register();
			return;
		}

		// --- Lizens Erweiterung
		if (MainZap.rand(RAND_LICENSE_EXTENTION) == 0) {
			System.out.println("Dropping license Extention");

			final int[] r = new int[] { MainZap.rand(3) };
			final Extention[] e = new Extention[] { null };
			String t = "err";
			BufferedImage i = null;
			if (r[0] == 0 && !Shop.unlocked[1]) {
				e[0] = Extention.MIRROR;
				t = "Mirror License";
				i = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_MIRROR);
			} else if (r[0] == 1 && !Shop.unlocked[2]) {
				e[0] = Extention.SHIELD;
				t = "Shield License";
				i = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_SHIELD);
			} else if (r[0] == 2 && !Shop.unlocked[3]) {
				e[0] = Extention.SHOCK;
				t = "Shock License";
				i = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_EXT_SHOCK);
			}

			if (e[0] == null) // schon alle Freigeschaltet
				return;

			SpecialCollectable c = new SpecialCollectable(i, 1, t, 1, new Runnable() {
				@Override
				public void run() {
					Shop.unlocked[r[0] + 1] = true;
					LootReader.save();
				}
			});
			int[] coors = ExplosionEffect.getRandCircleCoordinates(100);
			c.setPosition(coors[0] + this.getLocX(), coors[1] + this.getLocY());
			c.register();
			return;
		}

		// --- Lizenz upgrade
		if (MainZap.rand(RAND_LICENSE_UPGRADE) == 0) {
			System.out.println("Dropping license Upgrade");
			final int[] r = new int[] { MainZap.rand(4) };
			String t = "err";
			BufferedImage i = null;
			if (r[0] == 0 && !Shop.unlocked[4]) {
				t = "Health Upgrade License";
				i = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_STAT_HEALTH);
			} else if (r[0] == 1 && !Shop.unlocked[5]) {
				t = "Damage Upgrade License";
				i = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_STAT_DAMAGE);
			} else if (r[0] == 2 && !Shop.unlocked[6]) {
				t = "Ammo Usage Upgrade License";
				i = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_STAT_EFFICIENCY);
			} else if (r[0] == 3 && !Shop.unlocked[7]) {
				t = "Bullet Speed Upgrade License";
				i = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_STAT_BULLETSPEED);
			} else
				return;// schon alle Freigeschaltet

			SpecialCollectable c = new SpecialCollectable(i, 1, t, 0, new Runnable() {
				@Override
				public void run() {
					Shop.unlocked[r[0] + 4] = true;
					LootReader.save();
				}
			});
			int[] coors = ExplosionEffect.getRandCircleCoordinates(100);
			c.setPosition(coors[0] + this.getLocX(), coors[1] + this.getLocY());
			c.register();
			return;
		}

	}

	@Override
	public void collide(Collideable c) {
		if (!(c instanceof Projectile))
			return;
		if (((Projectile) c).getSender() == MainZap.getPlayer())
			super.collide(c);
	}

	@Override
	public void paint(Graphics2D g) {
		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		super.paint(g);

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	}

}