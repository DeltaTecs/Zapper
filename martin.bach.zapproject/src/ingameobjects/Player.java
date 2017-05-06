package ingameobjects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

import battle.CombatObject;
import battle.WeaponPositioning;
import battle.collect.BulletDamageUp;
import battle.collect.BulletRangeUp;
import battle.collect.BulletSpeedUp;
import battle.collect.ReloadUp;
import battle.collect.SpeedUp;
import battle.projectile.Projectile;
import battle.projectile.ProjectileDesign;
import collision.Collideable;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.Cmd;
import corecase.MainZap;
import gui.Frame;
import gui.Hud;
import gui.Map;
import gui.PlayerAmmoIndicator;
import gui.PlayerHpBar;
import gui.effect.ExplosionEffect;
import gui.effect.ExplosionEffectPattern;
import gui.extention.Mirroring;
import gui.extention.Shielding;
import gui.screens.end.EndScreen;
import gui.shop.Shop;
import gui.shop.meta.DefaultShipConfig;
import gui.shop.meta.ProjDesignDefault;
import gui.shop.meta.ShipStartConfig;
import io.TextureBuffer;
import lib.SpeedVector;

public class Player extends CombatObject {

	// -- Texturen für Boost-Indicator -------
	private static final BufferedImage IMG_SPEED_BOOST_R = TextureBuffer.get(TextureBuffer.NAME_COLLECT_SPEEDUP_ROUND);
	private static final BufferedImage IMG_SPEED_BOOST_C = TextureBuffer.get(TextureBuffer.NAME_COLLECT_SPEEDUP_CORNER);
	private static final BufferedImage IMG_BULLETSPEED_BOOST_R = TextureBuffer
			.get(TextureBuffer.NAME_COLLECT_BULLET_SPEED_UP_ROUND);
	private static final BufferedImage IMG_BULLETSPEED_BOOST_C = TextureBuffer
			.get(TextureBuffer.NAME_COLLECT_BULLET_SPEED_UP_CORNER);
	private static final BufferedImage IMG_RANGE_BOOST_R = TextureBuffer
			.get(TextureBuffer.NAME_COLLECT_BULLET_RANGE_UP_ROUND);
	private static final BufferedImage IMG_RANGE_BOOST_C = TextureBuffer
			.get(TextureBuffer.NAME_COLLECT_BULLET_RANGE_UP_CORNER);
	private static final BufferedImage IMG_DMG_BOOST_R = TextureBuffer
			.get(TextureBuffer.NAME_COLLECT_BULLET_DMG_UP_ROUND);
	private static final BufferedImage IMG_DMG_BOOST_C = TextureBuffer
			.get(TextureBuffer.NAME_COLLECT_BULLET_DMG_UP_CORNER);
	private static final BufferedImage IMG_RELOAD_BOOST_R = TextureBuffer
			.get(TextureBuffer.NAME_COLLECT_RELOAD_UP_ROUND);
	private static final BufferedImage IMG_RELOAD_BOOST_C = TextureBuffer
			.get(TextureBuffer.NAME_COLLECT_RELOAD_UP_CORNER);
	// ---

	private static final float SHIELD_FAC_SPEED = 0.2f;
	private static final float SHIELD_FAC_RANGE = 2.5f;
	private static final float SHIELD_FAC_DAMAGE = 3.0f;
	private static final float SHIELD_FAC_INBOUND_DAMAGE = 0.1f;

	private static final int POS_BOOST_INDICATOR = 170;
	private static final int SIZE_BOOST_INDICATOR = 53;
	private static final int SPACE_BOOST_INDICATOR = 6;
	private static final float ALPHA_BOOST_IND_FG = 0.4f;
	private static final float ALPHA_BOOST_IND_BG = 0.3f;
	private static final float ALPHA_BOOST_IND_ADD_BOUND = 0.1f;
	private static final float ALPHA_BOOST_IND_ADD_DELTA = 0.008f;

	private static final int PROJ_RANGE_DEFAULT = 800;
	private static final int HP_MAX_DEFAULT = 80000;
	private static final boolean REALISTIC_PROJ_VELO = true;
	private static final float BOOST_FAC_SPEED = 1.5f;
	private static final float BOOST_FAC_BULLET_SPEED = 1.5f;
	private static final float BOOST_FAC_BULLET_RANGE = 3f;
	private static final float BOOST_FAC_BULLET_DMG = 1.5f;
	private static final float BOOST_FAC_RELOAD = 0.4f;
	private static final float RES_RELOAD_BOOST_AMMO_WASTE_FAC = 2f;
	private static final float WARPSPEED_MULTIPLIER = 1.04f;
	private static final float WARP_BLENDING_SPEEDBORDER = -35f;

	private static CollisionInformation collisionInfo = new CollisionInformation(30, CollisionType.COLLIDE_AS_PLAYER,
			true);
	private float speed = 3.5f; // default
	private BufferedImage texture;
	private float textureScale = 1.0f;
	private SpeedVector velocity = new SpeedVector(0, 0);
	private int screenAimX = Frame.HALF_SCREEN_SIZE;
	private int screenAimY = 0;
	private int midSizeX;
	private int midSizeY;
	private double rotation = 0;
	private AffineTransform textureTransform;
	private PlayerHpBar hpBar = new PlayerHpBar(HP_MAX_DEFAULT);
	private PlayerAmmoIndicator ammoIndicator = new PlayerAmmoIndicator();
	private int projRange = PROJ_RANGE_DEFAULT;
	private boolean warping = false;
	private boolean shooting = false;
	private boolean outOfAmmo = false;
	// ---VVV Waffen-konfig --- !!! Wenn geändert: Auch in totalReset()
	// ändern!!!
	private WeaponPositioning singleWeaponPositioning = new WeaponPositioning((byte) 1, new int[] { 0 },
			new int[] { -20 });
	private WeaponPositioning doubleWeaponPositioning = new WeaponPositioning((byte) 2, new int[] { -10, 10 },
			new int[] { -15, -15 });
	private WeaponPositioning tripleWeaponPositioning = new WeaponPositioning((byte) 3, new int[] { -15, 0, 15 },
			new int[] { -15, -20, -15 });
	private WeaponPositioning activeWeaponPositioning = singleWeaponPositioning;
	private int nextWeapon = 0;
	private boolean upgraded = false;
	private float ammoUsageFac = 1.8f;
	private float maxWeaponCooldownWithout = 4.5f;
	private float maxWeaponCooldownWith = 3.0f;
	private float maxWeaponCooldown = maxWeaponCooldownWith;
	private float[] weaponCooldown = new float[] { maxWeaponCooldown };
	// !!! Waffen-Cooldown wird in 0.1 - Schritten ab gezogen.
	// Alles andere in 1er!
	private int bulletDamage = 300; // eig. 300
	private float bulletSpeed = 12;
	private ProjectileDesign projDesign = new ProjDesignDefault();
	// --- ende: Waffen-konfig

	// W, A, S, D
	private boolean[] arrowKeysPressed = new boolean[] { false, false, false, false };
	private int maxHp = HP_MAX_DEFAULT;
	private int hp = HP_MAX_DEFAULT;
	private ExplosionEffectPattern explPattern = new ExplosionEffectPattern(60, 600);
	private ExplosionEffect explEffect;
	private boolean visibile = true;
	private boolean blocked = false; // nicht mehr spielbar
	// speed-boost?, bullet-speed-boost?, bullet-range-boost?, bullet-dmg-boost?
	// reload-boost?
	private boolean[] boostsActive = new boolean[] { false, false, false, false, false };
	private int[] boostDurations = new int[] { 0, 0, 0, 0, 0 };
	private float boostAlphaAdd = 0.0f;
	private float boostAlphaDelta = ALPHA_BOOST_IND_ADD_DELTA;
	private ShipStartConfig lastApplyedConfig;
	private boolean shielded = false; // Für Schild-Effekt

	public Player() {
		super(collisionInfo, false, false); // false -> nicht an Stage gebunden;
											// false -> egal ob BG oder FG
		texture = TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_DEFAULT);
		midSizeX = (int) ((texture.getWidth() * textureScale) / 2);
		midSizeY = (int) ((texture.getHeight() * textureScale) / 2);
		textureTransform = new AffineTransform();
		textureTransform.translate(Frame.HALF_SCREEN_SIZE - midSizeX, Frame.HALF_SCREEN_SIZE - midSizeY);
		textureTransform.scale(textureScale, textureScale);
		setPosition(Map.SIZE / 2, Map.SIZE / 2);
		lastApplyedConfig = ShipStartConfig.get(ShipStartConfig.C_DEFAULT);
		applyMeta(lastApplyedConfig);
	}

	@Override
	public void paint(Graphics2D g) {

		// Graphics sind auf 0/0 Kontext

		if (visibile) {

			// Schiff zeichnen
			AffineTransform buffer = new AffineTransform(textureTransform);
			buffer.rotate(rotation, texture.getWidth() / 2, texture.getHeight() / 2);
			g.drawImage(texture, buffer, null);

		}

		// Explosion (falls tot)
		if (explEffect != null) {
			int x = getLocX();
			int y = getLocY();
			g.translate(-x + Frame.HALF_SCREEN_SIZE, -y + Frame.HALF_SCREEN_SIZE);
			explEffect.paint(g);
			g.translate(x - Frame.HALF_SCREEN_SIZE, y - Frame.HALF_SCREEN_SIZE);
		}

		// Spiegel-Effekt
		Mirroring.paint(g);

		if (!isAlive() || warping)
			return; // TOT oder im Warp

		// HP-Leiste
		hpBar.paint(g);

		// Ammo-Leisten
		ammoIndicator.paint(g);

		// Boost-Indicator
		paintBoostIndicator(g);

		// Mögliches Schild
		Shielding.paint(g);

		if (MainZap.debug) {

			// Pos
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 14));
			g.drawString("P-Pos " + getPosX() + " | " + getPosY(), 10, 20);
			g.drawString("P-Aim v:" + getMapAimX() + " | " + getMapAimY(), 10, 36);

			g.setColor(Color.RED);
			g.fillRect(getMapAimX() - 2 - (int) getPosX() + Frame.HALF_SCREEN_SIZE,
					getMapAimY() - 2 - (int) getPosY() + Frame.HALF_SCREEN_SIZE, 4, 4);
			g.setColor(Color.GREEN);
			g.translate(Frame.SIZE / 2, Frame.SIZE / 2);
			g.drawOval((int) (-collisionInfo.getRadius()), (int) (-collisionInfo.getRadius()),
					(int) (2 * collisionInfo.getRadius()), (int) (2 * collisionInfo.getRadius()));
			g.translate(-Frame.SIZE / 2, -Frame.SIZE / 2);
			g.setColor(Color.CYAN);
			for (Point p : activeWeaponPositioning.getRotated((float) rotation)) {
				g.fillRect(Frame.HALF_SCREEN_SIZE - 2 + (int) p.getX(), Frame.HALF_SCREEN_SIZE - 2 + (int) p.getY(), 4,
						4);
			}
			if (new Random().nextInt(20) == 0) {
				switch (new Random().nextInt(3)) {
				case 0:
					activeWeaponPositioning = singleWeaponPositioning;
					break;
				case 1:
					activeWeaponPositioning = doubleWeaponPositioning;
					break;
				case 2:
					activeWeaponPositioning = tripleWeaponPositioning;
					break;
				}
				nextWeapon = 0;
			}

		}

	}

	@Override
	public void collide(Collideable c) {

		if (warping) // im Warp nicht erlaubt
			return;

		if (c instanceof Projectile) {

			Projectile p = (Projectile) c;
			if (p.collided())
				return;
			p.setCollided(true);
			Shielding.inboundProjectile(p.getLocX(), p.getLocY());
			if (shielded) {
				hp -= (int) (p.getDamage() * SHIELD_FAC_INBOUND_DAMAGE);
				hpBar.remove((int) (p.getDamage() * SHIELD_FAC_INBOUND_DAMAGE));
			} else {
				hp -= p.getDamage();
				hpBar.remove(p.getDamage());
			}

			if (hp <= 0 && isAlive())
				die(); // rip

		}

	}

	private void die() {

		setAlive(false);
		MainZap.getGrid().remove(this);

		explEffect = new ExplosionEffect(explPattern, this);
		explEffect.setFinishTask(new Runnable() {

			@Override
			public void run() {

				visibile = false;
				MainZap.getMainLoop().scheduleTask(new Runnable() {

					@Override
					public void run() {

						EndScreen.popUp(EndScreen.TEXT_RIP);

					}

				}, ExplosionEffect.DURATION, false);

			}

		});

	}

	private void updateRotation() {
		if (warping) { // Im Warp nicht erlaubt
			return;
		}
		rotation = Math.PI - Math.atan2(screenAimX - Frame.HALF_SCREEN_SIZE, screenAimY - Frame.HALF_SCREEN_SIZE);
	}

	@Override
	public void update() {
		Mirroring.update();
		Shielding.update();
		if (isAlive() && !warping) {
			if (!blocked) {
				updatePosition();
				updateRotation();
				updateShooting();
				updateAmmo();
			}
			updateBoosts();
			hpBar.update();
			updateBoostIndicatorAlpha();
		}
		if (warping)
			updateWarp();
		if (explEffect != null)
			explEffect.update();
	}

	private void updateAmmo() {

		if (shooting) {

			if (boostsActive[4]) { // Boost verschwendet muni
				ammoIndicator.remove(ammoUsageFac * RES_RELOAD_BOOST_AMMO_WASTE_FAC);
			} else {
				ammoIndicator.remove(ammoUsageFac);
			}

		}

		if (outOfAmmo) {

			if (ammoIndicator.ammoRemaining() == true)
				setRunningOnLowAmmo(false);

		} else {

			if (ammoIndicator.ammoRemaining() == false)
				setRunningOnLowAmmo(true);

		}

	}

	private void updateBoostIndicatorAlpha() {

		if (boostAlphaAdd > ALPHA_BOOST_IND_ADD_BOUND) {
			boostAlphaAdd = ALPHA_BOOST_IND_ADD_BOUND;
			boostAlphaDelta *= -1;
			return;
		}

		if (boostAlphaAdd < -ALPHA_BOOST_IND_ADD_BOUND) {
			boostAlphaAdd = -ALPHA_BOOST_IND_ADD_BOUND;
			boostAlphaDelta *= -1;
			return;
		}

		boostAlphaAdd += boostAlphaDelta;
	}

	private void updatePosition() {

		if (Shop.isOpen())
			return; // Im Schop keine Bewegung

		float speed = this.speed;
		if (boostsActive[0]) // speedboost
			speed = this.speed * BOOST_FAC_SPEED;

		if (shielded)
			speed *= SHIELD_FAC_SPEED;

		getVelocity().setX(0);
		getVelocity().setY(0);

		boolean[] keys = arrowKeysPressed;

		if (keys[0]) { // W
			velocity.setY(-speed);
		}
		if (keys[1]) { // A
			velocity.setX(-speed);
		}
		if (keys[2]) { // S
			velocity.setY(velocity.getY() + speed);
		}
		if (keys[3]) { // D
			velocity.setX(velocity.getX() + speed);
		}

		if (Math.abs(velocity.getX()) + Math.abs(velocity.getY()) > speed) {
			velocity.setX(velocity.getX() * 0.7f);
			velocity.setY(velocity.getY() * 0.7f);
		}

		moveX(velocity.getX());
		moveY(velocity.getY());

		// Position mit der Map-Größe Clippen:
		setPosition(MainZap.clip(getPosX()), MainZap.clip(getPosY()));
	}

	private void updateWarp() {
		velocity.setY(velocity.getY() * WARPSPEED_MULTIPLIER);
		if (velocity.getY() < WARP_BLENDING_SPEEDBORDER)
			Hud.pushBlending();
		moveX(velocity.getX());
		moveY(velocity.getY());
	}

	private void updateBoosts() {

		for (int i = 0; i != boostsActive.length; i++) {

			if (boostsActive[i]) {

				if (boostDurations[i] == 0) {
					boostsActive[i] = false;
					continue;
				}
				boostDurations[i]--;

			}
		}
	}

	public void updateShooting() {

		if (warping) // im Warp
			return;

		int i = 0;
		for (float cooldown : weaponCooldown) {

			if (cooldown > 0) {
				weaponCooldown[i] -= 0.1f;
			} else if (shooting) {
				if (!boostsActive[4]) {
					weaponCooldown[i] = maxWeaponCooldown;
				} else {
					weaponCooldown[i] = maxWeaponCooldown * BOOST_FAC_RELOAD;
				}
				shoot();
			} else {
				// Schießt nicht. Cooldown aber 0
				break;
			}
			i++;
		}

	}

	private void shoot() {

		Mirroring.fireImages();

		Projectile proj = new Projectile(bulletSpeed, projDesign, bulletDamage);
		if (REALISTIC_PROJ_VELO) {
			if (activeWeaponPositioning == null) {
				proj.launch((int) getPosX(), (int) getPosY(), getMapAimX(), getMapAimY(), getVelocity(), projRange,
						this);
			} else {
				Point wp = activeWeaponPositioning.getRotated((float) rotation, nextWeapon);
				nextWeapon++;
				if (nextWeapon > activeWeaponPositioning.getWeaponAmount() - 1)
					nextWeapon = 0;
				proj.launch((int) (getPosX() + wp.getX()), (int) (getPosY() + wp.getY()),
						getMapAimX() + (int) (wp.getX()), getMapAimY() + (int) (wp.getY()), getVelocity(), projRange,
						this);
			}
		} else {
			if (activeWeaponPositioning == null) {
				proj.launch((int) getPosX(), (int) getPosY(), getMapAimX(), getMapAimY(), projRange, this);
			} else {
				Point wp = activeWeaponPositioning.getRotated((float) rotation, nextWeapon);
				nextWeapon++;
				if (nextWeapon > activeWeaponPositioning.getWeaponAmount() - 1)
					nextWeapon = 0;
				proj.launch((int) (getPosX() + wp.getX()), (int) (getPosY() + wp.getY()),
						getMapAimX() + (int) (wp.getX()), getMapAimY() + (int) (wp.getY()), projRange, this);
			}
		}
		applyBoostsOnProjectile(proj);
		proj.register();
	}

	public void applyBoostsOnProjectile(Projectile proj) {
		if (boostsActive[1]) { // Bullet-speed-boost
			proj.setSpeed(proj.getSpeed() * BOOST_FAC_BULLET_SPEED);
			int[] rgba = new int[] { proj.getColor().getRed(), proj.getColor().getGreen(), proj.getColor().getBlue(),
					proj.getColor().getAlpha() };
			proj.setColor(new Color(rgba[0], MainZap.clipRGB(rgba[1] + 80), rgba[2], rgba[3]));
		}

		if (boostsActive[2]) { // Bullet-range-boost
			proj.setRange((int) (proj.getRange() * BOOST_FAC_BULLET_RANGE));
			int[] rgba = new int[] { proj.getColor().getRed(), proj.getColor().getGreen(), proj.getColor().getBlue(),
					proj.getColor().getAlpha() };
			proj.setColor(new Color(rgba[0], rgba[1], MainZap.clipRGB(rgba[2] + 80), rgba[3]));
		}

		if (boostsActive[3]) { // Bullet-dmg-boost
			proj.setDamage((int) (proj.getDamage() * BOOST_FAC_BULLET_DMG));
			proj.setSquare(true); // sexy? dann quadrat?
		}

		if (shielded) {
			proj.setDamage((int) (proj.getDamage() * SHIELD_FAC_DAMAGE));
			proj.setRange((int) (proj.getRange() * SHIELD_FAC_RANGE));
		}
	}

	public void heal(int h) {

		if ((long) h + (long) hp > Integer.MAX_VALUE) {
			hp = Integer.MAX_VALUE;
			hpBar.add(h);
			return;
		}

		hp += h;
		hpBar.add(h);
		if (hp > maxHp)
			hp = maxHp;

	}

	public void setRunningOnLowAmmo(boolean b) {

		if (b)
			maxWeaponCooldown = maxWeaponCooldownWithout;
		else
			maxWeaponCooldown = maxWeaponCooldownWith;

		outOfAmmo = b;
	}

	private void paintBoostIndicator(Graphics2D g) {

		int y = POS_BOOST_INDICATOR;
		Composite storeComp = g.getComposite();

		if (boostsActive[0]) { // Speed-Boost

			// Background
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_BOOST_IND_BG + boostAlphaAdd));
			if (MainZap.roundCorners) {
				g.drawImage(IMG_SPEED_BOOST_R, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			} else {
				g.drawImage(IMG_SPEED_BOOST_C, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			}
			// Foreground
			g.clipRect(6, 0, (int) ((float) SIZE_BOOST_INDICATOR * (boostDurations[0] / (float) SpeedUp.BOOST_TIME)),
					1000);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_BOOST_IND_FG + boostAlphaAdd));
			if (MainZap.roundCorners) {
				g.drawImage(IMG_SPEED_BOOST_R, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			} else {
				g.drawImage(IMG_SPEED_BOOST_C, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			}
			g.setClip(null);

			y += SIZE_BOOST_INDICATOR + SPACE_BOOST_INDICATOR;
		}

		if (boostsActive[3]) { // Damage-Boost

			// Background
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_BOOST_IND_BG + boostAlphaAdd));
			if (MainZap.roundCorners) {
				g.drawImage(IMG_DMG_BOOST_R, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			} else {
				g.drawImage(IMG_DMG_BOOST_C, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			}
			// Foreground
			g.clipRect(6, 0, (int) (SIZE_BOOST_INDICATOR * (boostDurations[3] / (float) BulletDamageUp.BOOST_TIME)),
					1000);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_BOOST_IND_FG + boostAlphaAdd));
			if (MainZap.roundCorners) {
				g.drawImage(IMG_DMG_BOOST_R, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			} else {
				g.drawImage(IMG_DMG_BOOST_C, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			}
			g.setClip(null);

			y += SIZE_BOOST_INDICATOR + SPACE_BOOST_INDICATOR;
		}

		if (boostsActive[4]) { // Reload-Boost

			// Background
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_BOOST_IND_BG + boostAlphaAdd));
			if (MainZap.roundCorners) {
				g.drawImage(IMG_RELOAD_BOOST_R, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			} else {
				g.drawImage(IMG_RELOAD_BOOST_C, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			}
			// Foreground
			g.clipRect(6, 0, (int) (SIZE_BOOST_INDICATOR * (boostDurations[4] / (float) ReloadUp.BOOST_TIME)), 1000);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_BOOST_IND_FG + boostAlphaAdd));
			if (MainZap.roundCorners) {
				g.drawImage(IMG_RELOAD_BOOST_R, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			} else {
				g.drawImage(IMG_RELOAD_BOOST_C, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			}
			g.setClip(null);

			y += SIZE_BOOST_INDICATOR + SPACE_BOOST_INDICATOR;
		}

		if (boostsActive[1]) { // Bullet-Speed-Boost

			// Background
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_BOOST_IND_BG + boostAlphaAdd));
			if (MainZap.roundCorners) {
				g.drawImage(IMG_BULLETSPEED_BOOST_R, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			} else {
				g.drawImage(IMG_BULLETSPEED_BOOST_C, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			}
			// Foreground
			g.clipRect(6, 0, (int) (SIZE_BOOST_INDICATOR * (boostDurations[1] / (float) BulletSpeedUp.BOOST_TIME)),
					1000);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_BOOST_IND_FG + boostAlphaAdd));
			if (MainZap.roundCorners) {
				g.drawImage(IMG_BULLETSPEED_BOOST_R, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			} else {
				g.drawImage(IMG_BULLETSPEED_BOOST_C, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			}
			g.setClip(null);

			y += SIZE_BOOST_INDICATOR + SPACE_BOOST_INDICATOR;
		}

		if (boostsActive[2]) { // Range-Boost

			// Background
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_BOOST_IND_BG + boostAlphaAdd));
			if (MainZap.roundCorners) {
				g.drawImage(IMG_RANGE_BOOST_R, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			} else {
				g.drawImage(IMG_RANGE_BOOST_C, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			}
			// Foreground
			g.clipRect(6, 0, (int) (SIZE_BOOST_INDICATOR * (boostDurations[2] / (float) BulletRangeUp.BOOST_TIME)),
					1000);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_BOOST_IND_FG + boostAlphaAdd));
			if (MainZap.roundCorners) {
				g.drawImage(IMG_RANGE_BOOST_R, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			} else {
				g.drawImage(IMG_RANGE_BOOST_C, 6, y, SIZE_BOOST_INDICATOR, SIZE_BOOST_INDICATOR, null);
			}
			g.setClip(null);

			y += SIZE_BOOST_INDICATOR + SPACE_BOOST_INDICATOR;
		}

		g.setComposite(storeComp);
	}

	public void applyMeta(ShipStartConfig c) {
		speed = c.getSpeed();
		textureScale = c.getScale();
		ammoUsageFac = c.getEfficiency();
		maxWeaponCooldownWithout = c.getReloadWithout();
		maxWeaponCooldownWith = c.getReloadWith();
		bulletDamage = c.getDamage();
		projDesign = c.getProjDesign();
		bulletSpeed = c.getProjSpeed();
		maxHp = c.getHp();
		hp = c.getHp();
		collisionInfo = c.getCollInfo();
		singleWeaponPositioning = c.getWeaponPosSingle();
		doubleWeaponPositioning = c.getWeaponPosDouble();
		tripleWeaponPositioning = c.getWeaponPosTriple();
		activeWeaponPositioning = singleWeaponPositioning;
		nextWeapon = 0;

		texture = c.getTexture();
		midSizeX = (int) ((texture.getWidth() * textureScale) / 2);
		midSizeY = (int) ((texture.getHeight() * textureScale) / 2);
		textureTransform = new AffineTransform();
		textureTransform.translate(Frame.HALF_SCREEN_SIZE - midSizeX, Frame.HALF_SCREEN_SIZE - midSizeY);
		textureTransform.scale(textureScale, textureScale);

		reset();

		lastApplyedConfig = c;
	}

	public void reset() {

		ammoIndicator.add(true);
		hp = maxHp;
		hpBar.setHp(maxHp);
		hpBar.setMaxHp(maxHp);
		for (int i = 0; i != boostsActive.length; i++) {
			boostsActive[i] = false;
			boostDurations[i] = 0;
		}
		velocity.setX(0);
		velocity.setY(0);

		maxWeaponCooldown = maxWeaponCooldownWith;
		weaponCooldown = new float[activeWeaponPositioning.getWeaponAmount()];
		for (int i = 0; i != activeWeaponPositioning.getWeaponAmount(); i++) {
			weaponCooldown[i] = i * (maxWeaponCooldown / activeWeaponPositioning.getWeaponAmount());
		}

	}

	public void totalReset() {

		applyMeta(new DefaultShipConfig());

		screenAimX = Frame.HALF_SCREEN_SIZE;
		screenAimY = 0;
		rotation = 0;
		warping = false;
		shooting = false;
		outOfAmmo = false;
		maxWeaponCooldown = maxWeaponCooldownWith;
		weaponCooldown = new float[] { maxWeaponCooldown };
		activeWeaponPositioning = singleWeaponPositioning;
		setAlive(true);
		visibile = true;
		boostsActive = new boolean[] { false, false, false, false, false };
		boostDurations = new int[] { 0, 0, 0, 0, 0 };
		boostAlphaDelta = ALPHA_BOOST_IND_ADD_DELTA;

		reset();
	}

	public ShipStartConfig genConfig() {
		return new ShipStartConfig(texture, textureScale, bulletDamage, speed, bulletSpeed, maxWeaponCooldownWith,
				maxWeaponCooldownWithout, maxHp, collisionInfo, projRange, projDesign, explPattern, ammoUsageFac,
				lastApplyedConfig.getName(), lastApplyedConfig.getDescription(), lastApplyedConfig.getPrice(),
				singleWeaponPositioning, doubleWeaponPositioning, tripleWeaponPositioning);
	}

	private MouseMotionListener motionListener = new MouseMotionListener() {

		@Override
		public void mouseDragged(MouseEvent arg0) {
			if (Shop.isOpen())
				return; // Im
						// Schop
						// keine
						// Bewegung
			screenAimX = (int) (arg0.getX() / MainZap.getScale());
			screenAimY = (int) (arg0.getY() / MainZap.getScale());

			Mirroring.checkMirrorStateChange();
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			if (Shop.isOpen())
				return; // Im
						// Schop
						// keine
						// Bewegung
			screenAimX = (int) (arg0.getX() / MainZap.getScale());
			screenAimY = (int) (arg0.getY() / MainZap.getScale());

			Mirroring.checkMirrorStateChange();
		}

	};

	private MouseListener clickListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent arg0) {

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			if (!Shop.isOpen())
				shooting = true;
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			shooting = false;
			weaponCooldown = new float[activeWeaponPositioning.getWeaponAmount()];
			for (int i = 0; i != activeWeaponPositioning.getWeaponAmount(); i++) {
				weaponCooldown[i] = (i + 1) * (maxWeaponCooldown / activeWeaponPositioning.getWeaponAmount());
			}
		}

	};

	private KeyListener keyListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent arg0) {
			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_W:
				arrowKeysPressed[0] = true;
				break;
			case KeyEvent.VK_A:
				arrowKeysPressed[1] = true;
				break;
			case KeyEvent.VK_S:
				arrowKeysPressed[2] = true;
				break;
			case KeyEvent.VK_D:
				arrowKeysPressed[3] = true;
				break;
			default:
				// unbelegt
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_W:
				arrowKeysPressed[0] = false;
				break;
			case KeyEvent.VK_A:
				arrowKeysPressed[1] = false;
				break;
			case KeyEvent.VK_S:
				arrowKeysPressed[2] = false;
				break;
			case KeyEvent.VK_D:
				arrowKeysPressed[3] = false;
				break;
			default:
				// unbelegt
				break;
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}

	};

	public void setWeaponAmount(byte i) {
		if (i < 1 || i > 3) {
			Cmd.err("Player:828 impossible. Only up to 3 Weapons allowed");
			return;
		}

		if (i == 1) {
			activeWeaponPositioning = singleWeaponPositioning;
		} else if (i == 2) {
			activeWeaponPositioning = doubleWeaponPositioning;
		} else {
			activeWeaponPositioning = tripleWeaponPositioning;
		}
		weaponCooldown = new float[activeWeaponPositioning.getWeaponAmount()];
		for (int j = 0; j != activeWeaponPositioning.getWeaponAmount(); j++) {
			weaponCooldown[j] = j * (maxWeaponCooldown / activeWeaponPositioning.getWeaponAmount());
		}

	}

	public BufferedImage getTexture() {
		return texture;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}

	public SpeedVector getVelocity() {
		return velocity;
	}

	public void setVelocity(SpeedVector velocity) {
		this.velocity = velocity;
	}

	public int getMapAimX() {
		return (int) getPosX() + screenAimX - Frame.HALF_SCREEN_SIZE;
	}

	public int getMapAimY() {
		return (int) getPosY() + screenAimY - Frame.HALF_SCREEN_SIZE;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public AffineTransform getTextureTransform() {
		return textureTransform;
	}

	public MouseMotionListener getMotionListener() {
		return motionListener;
	}

	public MouseListener getClickListener() {
		return clickListener;
	}

	public KeyListener getKeyListener() {
		return keyListener;
	}

	@Override
	public CollisionInformation getInformation() {
		return collisionInfo;
	}

	@Override
	public void register() {
		System.err.println("[Warn] Spieler nicht als Interaktive Komponente registirier bar! ingameobjects.Player");
	}

	@Override
	public void unRegister() {
		System.err.println("[Warn] Spieler nicht als Interaktive Komponente unRegistrier bar! ingameobjects.Player");
	}

	public int distanceTo(int x, int y) {
		return (int) Math.sqrt((x - getPosX()) * (x - getPosX()) + (y - getPosY()) * (y - getPosY()));
	}

	public void speedBoost(int duration) {
		if (warping) // im Warp nicht erlaubt
			return;
		boostsActive[0] = true;
		boostDurations[0] += duration;
	}

	public void bulletSpeedBoost(int duration) {
		if (warping) // im Warp nicht erlaubt
			return;
		boostsActive[1] = true;
		boostDurations[1] += duration;
	}

	public void bulletRangeBoost(int duration) {
		if (warping) // im Warp nicht erlaubt
			return;
		boostsActive[2] = true;
		boostDurations[2] += duration;
	}

	public void bulletDamageBoost(int duration) {
		if (warping) // im Warp nicht erlaubt
			return;
		boostsActive[3] = true;
		boostDurations[3] += duration;
	}

	public void reloadBoost(int duration) {
		if (warping) // im Warp nicht erlaubt
			return;
		boostsActive[4] = true;
		boostDurations[4] += duration;
	}

	public void enterWarp() {
		reset();
		warping = true;
		rotation = 0;
		velocity.setX(0);
		velocity.setY(-0.2f);
	}

	public void exitWarp() {
		warping = false;
		rotation = 0;
		velocity.setX(0);
		velocity.setY(0);
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		hpBar.setHp(hp);
		this.hp = hp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
		hpBar.setMaxHp(maxHp);
	}

	public float getAmmoUsageFac() {
		return ammoUsageFac;
	}

	public void setAmmoUsageFac(float ammoUsage) {
		this.ammoUsageFac = ammoUsage;
	}

	public PlayerAmmoIndicator getAmmoIndicator() {
		return ammoIndicator;
	}

	public boolean isWarping() {
		return warping;
	}

	public void setWarping(boolean warping) {
		this.warping = warping;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getProjRange() {
		return projRange;
	}

	public void setProjRange(int projRange) {
		this.projRange = projRange;
	}

	public float getMaxWeaponCooldownWithout() {
		return maxWeaponCooldownWithout;
	}

	public void setMaxWeaponCooldownWithout(float maxWeaponCooldownWhenOutOfAmmo) {
		this.maxWeaponCooldownWithout = maxWeaponCooldownWhenOutOfAmmo;
	}

	public float getMaxWeaponCooldownWith() {
		return maxWeaponCooldownWith;
	}

	public void setMaxWeaponCooldownWith(float maxWeaponCooldownWhenWithAmmo) {
		this.maxWeaponCooldownWith = maxWeaponCooldownWhenWithAmmo;
	}

	public float getMaxWeaponCooldown() {
		return maxWeaponCooldown;
	}

	public void setMaxWeaponCooldown(float maxWeaponCooldown) {
		this.maxWeaponCooldown = maxWeaponCooldown;
	}

	public int getBulletDamage() {
		return bulletDamage;
	}

	public void setBulletDamage(int bulletDamage) {
		this.bulletDamage = bulletDamage;
	}

	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(float bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	public ProjectileDesign getProjDesign() {
		return projDesign;
	}

	public void setProjDesign(ProjectileDesign projDesign) {
		this.projDesign = projDesign;
	}

	public float getTextureScale() {
		return textureScale;
	}

	public PlayerHpBar getHpBar() {
		return hpBar;
	}

	public void setTextureScale(float textureScale) {
		midSizeX = (int) ((texture.getWidth() * textureScale) / 2);
		midSizeY = (int) ((texture.getHeight() * textureScale) / 2);
		this.textureScale = textureScale;
		textureTransform = new AffineTransform();
		textureTransform.translate(Frame.HALF_SCREEN_SIZE - midSizeX, Frame.HALF_SCREEN_SIZE - midSizeY);
		textureTransform.scale(textureScale, textureScale);
	}

	public boolean isUpgraded() {
		return upgraded;
	}

	public void setUpgraded(boolean upgraded) {
		this.upgraded = upgraded;
	}

	public boolean isShooting() {
		return shooting;
	}

	public WeaponPositioning getActiveWeaponPositioning() {
		return activeWeaponPositioning;
	}

	public boolean isShielded() {
		return shielded;
	}

	public void setShielded(boolean shielded) {
		this.shielded = shielded;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

}
