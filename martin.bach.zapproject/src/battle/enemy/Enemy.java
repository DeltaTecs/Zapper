package battle.enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import battle.CombatObject;
import battle.MultiCannonWeaponConfiguration;
import battle.Shockable;
import battle.WeaponConfiguration;
import battle.ai.AiProtocol;
import battle.projectile.Projectile;
import collision.Collideable;
import collision.CollisionInformation;
import corecase.MainZap;
import gui.Crystal;
import gui.effect.Effect;
import gui.effect.ExplosionEffect;
import gui.effect.ExplosionEffectPattern;
import gui.effect.WarpInEffect;
import gui.effect.WarpOutEffect;
import gui.extention.Shocking;
import ingameobjects.InteractiveObject;

public class Enemy extends CombatObject implements Shockable {

	private static final int MAX_NORMAL_SPEED_COOLDOWN = MainZap.inTicks(5000);
	private static final int DMG_INDICATING_MAX_TIME = MainZap.getMainLoop().inTicks(5000);
	private static final Color COLOR_HP_BACKGROUND = new Color(174, 1, 2, 113);
	private static final Color COLOR_HP_FOREGROUND = new Color(221, 22, 10);
	private static final int DISTANCE_HP_BAR_TO_MAX = 6;
	private static final int HEIGHT_HP_BAR = 6;

	private boolean warping = false;
	private boolean friend = false;
	private boolean exploded = false;
	private int aimX;
	private int aimY;
	private float speed;
	private BufferedImage texture;
	private float scale;
	private double rotation = 0;
	private int size;
	private int maxMiddistance;
	private int hpBarLength;
	private AffineTransform textureTransform;
	private Effect warpEffect = null;
	private AiProtocol aiProtocol;
	private int projRange;
	private boolean mayShoot;
	private boolean preAiming = false;
	private Projectile projectilePattern = null;
	private InteractiveObject shootingAim;
	private WeaponConfiguration weaponConfiguration;
	private ExplosionEffectPattern explosionEffectPattern;
	private int maxHealth;
	private int health;
	private int dmgIndicatingTime = 0;
	private int score;
	private int crystals;
	private boolean nodrops = false;
	private boolean noWaitAfterWarp = false;
	private int normalSpeedCooldown = -1;

	public Enemy(float posX, float posY, float speed, BufferedImage texture, float scale,
			CollisionInformation information, AiProtocol ai, WeaponConfiguration weaponconf, int health,
			ExplosionEffectPattern explPattern, int score, int projRange, int crystals, boolean friend) {
		super(information, true, false); // true -> Immer an Stage gebunden;
											// false -> im Vordergrund
		this.speed = speed;
		this.texture = texture;
		this.scale = scale;
		this.weaponConfiguration = weaponconf;
		this.maxHealth = health;
		this.health = maxHealth;
		this.explosionEffectPattern = explPattern;
		this.score = score;
		this.projRange = projRange;
		this.crystals = crystals;
		this.friend = friend;
		size = (int) (((texture.getHeight() * scale) + (texture.getWidth() * scale)) / 2);
		hpBarLength = (int) (size * 0.8f);
		maxMiddistance = (int) (texture.getHeight() * scale / 2);
		if ((int) (texture.getWidth() * scale / 2) > maxMiddistance) {
			maxMiddistance = (int) (texture.getWidth() * scale / 2);
		}
		setPosition(posX, posY);
		aiProtocol = ai;
		if (aiProtocol != null)
			aiProtocol.init(this);
		textureTransform = new AffineTransform();
		textureTransform.scale(scale, scale);
		textureTransform.translate(-texture.getWidth() / 2, -texture.getHeight() / 2);
	}

	@Override
	public void paint(Graphics2D g) {

		// Warp? -> benötigt Map-Kontext
		if (warping) {
			// Der wird hier gemacht. Da der Textur-Transform beim Effekt
			// apspackt.. :(
			if (warpEffect != null)
				warpEffect.paint(g);
		}

		// Von Kontext Karte zu Kontext EigenPos
		int dx = getLocX();
		int dy = getLocY();
		g.translate(dx, dy);

		// Schiff zeichnen
		AffineTransform buffer = new AffineTransform(textureTransform);
		buffer.rotate(rotation, texture.getWidth() / 2, texture.getHeight() / 2);
		g.drawImage(texture, buffer, null);

		// HP-Leiste
		if (dmgIndicatingTime > 0) {

			g.setColor(COLOR_HP_BACKGROUND);
			g.fillRect(-(hpBarLength / 2), maxMiddistance + DISTANCE_HP_BAR_TO_MAX, hpBarLength, HEIGHT_HP_BAR);
			if (health > 0) {
				g.setColor(COLOR_HP_FOREGROUND);
				g.fillRect(-(hpBarLength / 2), maxMiddistance + DISTANCE_HP_BAR_TO_MAX,
						(int) (hpBarLength * (health / (float) maxHealth)), HEIGHT_HP_BAR);
			}

		}

		if (MainZap.debug) {

			g.setColor(Color.GREEN);

			g.fillRect(-2, -2, 4, 4);
			g.drawOval((int) -getInformation().getRadius(), (int) -getInformation().getRadius(),
					(int) (2 * getInformation().getRadius()), (int) (2 * getInformation().getRadius()));

			if (weaponConfiguration instanceof MultiCannonWeaponConfiguration) {
				Point[] ps = ((MultiCannonWeaponConfiguration) weaponConfiguration).getPositioning()
						.getRotated((float) rotation);
				g.setColor(Color.CYAN);
				for (Point p : ps)
					g.fillRect(p.x - 3, p.y - 3, 6, 6);
			}

		}

		// Von EigenPos zu Karten Kontext
		g.translate(-dx, -dy);

	}

	public void updateShooting() {
		if (!mayShoot)
			return; // Schießen nicht erwünscht
		if (projectilePattern == null) {
			System.err.println("[Err] Enemy hat kein ProjectilePattern will aber schießen. " + this.toString()
					+ ". Überspringe Schuss-Zyklus...");
			return;
		}
		if (weaponConfiguration == null) {
			System.err.println("[Err] Enemy hat keine Weaponconfiguration will aber schießen. " + this.toString()
					+ ". Überspringe Schuss-Zyklus...");
			return;
		}
		weaponConfiguration.update();

		// Technischer Teil ---------
		if (shootingAim == null)
			return; // Kein ziel
		if (!weaponConfiguration.isReady())
			return;
		if (!shootingAim.isInRange(getLocX(), getLocY(), weaponConfiguration.getRange()))
			return; // Außer Reichweite
		weaponConfiguration.fire((Projectile) projectilePattern.getClone(), this, preAiming);
	}

	public void updateRotation() {
		if (!getAiProtocol().isParked())
			rotation = Math.PI - Math.atan2(aimX - getLocX(), aimY - getLocY());
	}

	@Override
	public void update() {

		if (aiProtocol == null) {
			updateUI();
			return;
		}

		if (warping) {
			updateWarp();
			return;
		}

		// Geshockt?
		if (normalSpeedCooldown > -1)
			if (normalSpeedCooldown <= 0) {
				normalSpeedCooldown = -1;
				speed *= 5;
			} else
				normalSpeedCooldown--;

		updateAi();
		if (aiProtocol.isWaiting()) {
			updateUI();
			super.update();
			return;
		}
		// ... Positionsupdate übernimmt AiProtocol
		updateRotation();
		updateShooting();
		updateUI();
		super.update();
	}

	private void updateUI() {
		if (dmgIndicatingTime > 0) {
			dmgIndicatingTime--;
		}
	}

	protected void updateAi() {
		aiProtocol.update();
	}

	@Override
	public void collide(Collideable c) {

		if (c instanceof Projectile) {
			if (((Projectile) c).collided())
				return;
			damage(((Projectile) c).getDamage(), ((Projectile) c));
			((Projectile) c).setCollided(true);
		}
	}

	public void damage(int damage, Projectile p) {

		if (damage < 0) { // Guardian Projektil
			health -= maxHealth + damage;
			nodrops = true; // Keine XP / Score / Crystals für Spieler
		} else {
			health -= damage;
		}
		if (aiProtocol != null)
			aiProtocol.call(AiProtocol.KEY_CALL_GETTING_DAMAGED,
					AiProtocol.formDamageCallArgs(p.getSender(), p, damage));

		if (health <= 0 && isAlive()) {
			// Kaputt gehen
			explode();
		}

		dmgIndicatingTime = DMG_INDICATING_MAX_TIME;
	}

	public void updateWarp() {
		if (warpEffect == null) {
			warping = false;
			return;
		}
		if (warpEffect.isFinished()) {
			warping = false;
			return;
		}
		warpEffect.update();
	}

	public void warpIn() {
		warpEffect = new WarpInEffect(this);
		warping = true;
		if (!noWaitAfterWarp)
			getAiProtocol().waitTicks(MainZap.getMainLoop().inTicks(1000));
	}

	public void warpIn(int warpAimX, int warpAimY) {
		warpEffect = new WarpInEffect(warpAimX, warpAimY, this);
		warping = true;
		if (!noWaitAfterWarp)
			getAiProtocol().waitTicks(MainZap.getMainLoop().inTicks(1000));
	}

	public void warpOut() {
		warpEffect = new WarpOutEffect(this, 1);
		warping = true;
	}

	public void warpOut(int warpAimX, int warpAimY) {
		warpEffect = new WarpOutEffect(warpAimX, warpAimY, this, 1);
		warping = true;
	}

	public void warpOut(int moveRand) {
		warpEffect = new WarpOutEffect(this, moveRand);
		warping = true;
	}

	public void warpOut(int warpAimX, int warpAimY, int moveRand) {
		warpEffect = new WarpOutEffect(warpAimX, warpAimY, this, moveRand);
		warping = true;
	}

	public void explode() {
		if (exploded)
			return;
		exploded = true;
		ExplosionEffect explEffect = new ExplosionEffect(explosionEffectPattern, this);
		explEffect.setFinishTask(new Runnable() {
			@Override
			public void run() {
				die();
			}
		});
		// aiProtocol.waitTicks(ExplosionEffect.DURATION +
		// explEffect.preexplodeTime); <-- Enemy steht während Explosion still.
		// Nicht sexy.
		explEffect.register();
	}

	public void die() {
		setAlive(false);
		if (aiProtocol != null)
			aiProtocol.call(AiProtocol.KEY_CALL_DIEING, null);
		unRegister();
		if (!MainZap.getPlayer().isAlive() || nodrops)
			return;
		Crystal.spawn(getLocX(), getLocY(), crystals, (int) (getCollisionInfo().getRadius() * 1.8f));
		MainZap.addScore(score);
	}

	@Override
	public Object getClone() {
		if (weaponConfiguration != null) {
			return new Enemy(getPosX(), getPosY(), speed, texture, scale, getCollisionInfo(),
					(AiProtocol) aiProtocol.getClone(),
					new WeaponConfiguration(weaponConfiguration.getMaxCooldown(), weaponConfiguration.getRange()),
					getMaxHealth(), getExplosionEffectPattern(), score, projRange, crystals, friend);
		} else {
			return new Enemy(getPosX(), getPosY(), speed, texture, scale, getCollisionInfo(),
					(AiProtocol) aiProtocol.getClone(), null, getMaxHealth(), getExplosionEffectPattern(), score,
					projRange, crystals, friend);
		}
	}

	public void switchProtocol(AiProtocol protocol) {

		for (String s : aiProtocol.getCalls().keySet()) {
			protocol.addCalls(s, aiProtocol.getCalls().get(s));
		}

		aiProtocol = protocol;
		protocol.init(this);
	}

	@Override
	public void register() {
		MainZap.getMap().addPaintShip(this);
		MainZap.getMap().addUpdateElement(this);
		MainZap.getGrid().add(this);
		super.getListedObjects().add(this);
	}

	@Override
	public void unRegister() {
		MainZap.getMap().removePaintShip(this);
		MainZap.getMap().removeUpdateElement(this);
		MainZap.getGrid().remove(this);
		super.getListedObjects().remove(this);
	}

	@Override
	public void shock() {
		damage((int) (maxHealth * 0.4f), Shocking.getTagProjectile());
		if (weaponConfiguration != null)
			weaponConfiguration.setMaxCooldown(weaponConfiguration.getMaxCooldown() * 2);
		speed *= 0.2f;
		normalSpeedCooldown = MAX_NORMAL_SPEED_COOLDOWN;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getAimX() {
		return aimX;
	}

	public void setAimX(int aimX) {
		this.aimX = aimX;
	}

	public int getAimY() {
		return aimY;
	}

	public void setAimY(int aimY) {
		this.aimY = aimY;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public float getScale() {
		return scale;
	}

	public boolean mayShoot() {
		return mayShoot;
	}

	public void setMayShoot(boolean mayShoot) {
		this.mayShoot = mayShoot;
	}

	public double getRotation() {
		return rotation;
	}

	public AiProtocol getAiProtocol() {
		return aiProtocol;
	}

	public void setProjectilePattern(Projectile projectilePattern) {
		this.projectilePattern = projectilePattern;
	}

	public Projectile getProjectilePattern() {
		return projectilePattern;
	}

	public WeaponConfiguration getWeaponConfiguration() {
		return weaponConfiguration;
	}

	public InteractiveObject getShootingAim() {
		return shootingAim;
	}

	public void setShootingAim(InteractiveObject shootingAim) {
		this.shootingAim = shootingAim;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public AffineTransform getTextureTransform() {
		return textureTransform;
	}

	public ExplosionEffectPattern getExplosionEffectPattern() {
		return explosionEffectPattern;
	}

	public void setExplosionEffectPattern(ExplosionEffectPattern explosionEffectPattern) {
		this.explosionEffectPattern = explosionEffectPattern;
	}

	public int getProjRange() {
		return projRange;
	}

	public void setProjRange(int projRange) {
		this.projRange = projRange;
	}

	public boolean isFriend() {
		return friend;
	}

	public void setFriend(boolean friend) {
		this.friend = friend;
	}

	public boolean isPreAiming() {
		return preAiming;
	}

	public void setPreAiming(boolean preAiming) {
		this.preAiming = preAiming;
	}

	public boolean isNoWaitAfterWarp() {
		return noWaitAfterWarp;
	}

	public void setNoWaitAfterWarp(boolean noWaitAfterWarp) {
		this.noWaitAfterWarp = noWaitAfterWarp;
	}

	public Effect getWarpEffect() {
		return warpEffect;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}

}
