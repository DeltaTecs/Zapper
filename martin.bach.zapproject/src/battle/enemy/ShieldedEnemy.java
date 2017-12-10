package battle.enemy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.AiProtocol;
import battle.collect.SimpleShieldAbsorbtionEffect;
import battle.projectile.Projectile;
import collision.CollisionInformation;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import gui.extention.Shielding;
import lib.ScheduledList;

public class ShieldedEnemy extends Enemy {

	private static final int DISTANCE_SHIELD_HP_BAR = 2;
	private static final Color COLOR_SHIELDBAR_FOREGROUND = new Color(43, 147, 198);
	private static final Color COLOR_SHIELDBAR_BACKGROUND = new Color(102, 177, 230, 128);
	private static final Color COLOR_SHIELD_BG = new Color(56, 164, 181, 35);
	private static final Color COLOR_SHIELD_FG = new Color(56, 164, 181, 198);
	private static final int SHIELD_DOWNTIME_AFTER_SHOCK = MainZap.inTicks(8000);
	public static final int SHIELD_REGEN_DEFAULT = 5;
	public static final int SHIELD_REGEN_LOW = 1;
	public static final int SHIELD_REGEN_HIGH = 20;
	public static final int SHIELD_NONRECOVERY_TIME_DEFAULT = MainZap.inTicks(1500);

	private int shield;
	private int maxShield;
	private int shieldRadian;
	private float shieldRegen = SHIELD_REGEN_HIGH;
	private int nonRecoveryTime = SHIELD_NONRECOVERY_TIME_DEFAULT;
	private int timeSinceLastDamage = SHIELD_NONRECOVERY_TIME_DEFAULT + 1;
	private int timeSincelastShock = SHIELD_DOWNTIME_AFTER_SHOCK + 1;
	private ScheduledList<SimpleShieldAbsorbtionEffect> hits = new ScheduledList<SimpleShieldAbsorbtionEffect>();

	public ShieldedEnemy(float posX, float posY, float speed, BufferedImage texture, float scale,
			CollisionInformation information, AiProtocol ai, WeaponConfiguration weaponconf, int health,
			ExplosionEffectPattern explPattern, int score, int projRange, int crystals, boolean friend, int shield, TailManager tail) {
		super(posX, posY, speed, texture, scale, information, ai, weaponconf, health, explPattern, score, projRange,
				crystals, friend, tail);
		this.shield = shield;
		maxShield = shield;
		shieldRadian = Shielding.calcShieldRange(texture, scale);
	}

	@Override
	public void paintDamageIndicators(Graphics2D g) {
		// HP-Leiste
		g.setColor(COLOR_HP_BACKGROUND);
		g.fillRect(-(getHpBarLength() / 2), getMaxMiddistance() + DISTANCE_HP_BAR_TO_MAX, getHpBarLength(),
				HEIGHT_HP_BAR);
		if (getHealth() > 0) {
			g.setColor(COLOR_HP_FOREGROUND);
			g.fillRect(-(getHpBarLength() / 2), getMaxMiddistance() + DISTANCE_HP_BAR_TO_MAX,
					(int) (getHpBarLength() * (getHealth() / (float) getMaxHealth())), HEIGHT_HP_BAR);
		}
		// schild
		g.setColor(COLOR_SHIELDBAR_BACKGROUND);
		g.fillRect(-(getHpBarLength() / 2),
				getMaxMiddistance() + DISTANCE_HP_BAR_TO_MAX + DISTANCE_SHIELD_HP_BAR + HEIGHT_HP_BAR, getHpBarLength(),
				HEIGHT_HP_BAR);
		if (getHealth() > 0) {
			g.setColor(COLOR_SHIELDBAR_FOREGROUND);
			g.fillRect(-(getHpBarLength() / 2),
					getMaxMiddistance() + DISTANCE_HP_BAR_TO_MAX + DISTANCE_SHIELD_HP_BAR + HEIGHT_HP_BAR,
					(int) (getHpBarLength() * (shield / (float) maxShield)), HEIGHT_HP_BAR);
		}
	}

	@Override
	public void damage(int damage, Projectile p) {
		if (damage < 0) { // Guardian Projektil
			setHealth(getHealth() - (getMaxHealth() + damage)); // true-dmg
			setNodrops(true); // Keine XP / Score / Crystals für Spieler
		} else {
			if (shield > damage) // Schild absorbiert
				shield -= damage;
			else if (shield > 0) { // Schild wird abgenutzt
				setHealth(getHealth() - damage + (int) shield);
				shield = 0;
			} else // Schild down
				setHealth(getHealth() - damage);
		}
		if (getAiProtocol() != null) // Listener rufen
			getAiProtocol().call(AiProtocol.KEY_CALL_GETTING_DAMAGED,
					AiProtocol.formDamageCallArgs(p.getSender(), p, damage));

		if (getHealth() <= 0 && isAlive()) // RIP
			explode();

		setDmgIndicatingTime(DMG_INDICATING_MAX_TIME); // Indikator zeigen
		timeSinceLastDamage = 0; // Regen-Zeit zurücksetzen

		// Einschlag eintragen
		if (MainZap.fancyGraphics)
			hits.schedAdd(new SimpleShieldAbsorbtionEffect(p, this, shieldRadian));
	}

	@Override
	public void update() {
		super.update();
		updateShield();
		// Effekte aktualisieren
		if (MainZap.fancyGraphics) {
			synchronized (hits) { // Zugriff vor Paint-Loop shotgunnen.
				for (SimpleShieldAbsorbtionEffect e : hits) {
					if (e == null) // Seltener Fehler
						continue;
					e.update();
					if (e.isFinished())
						hits.schedRemove(e);
				}
				hits.update();
			}
		}
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		g.setStroke(new BasicStroke(1));

		if (shield <= 0)
			return; // Schild down

		int dx = getLocX();
		int dy = getLocY();
		g.translate(dx, dy);

		// Hülle und Rand
		if (MainZap.fancyGraphics) {
			g.setColor(COLOR_SHIELD_BG);
			g.fillOval(-shieldRadian, -shieldRadian, 2 * shieldRadian, 2 * shieldRadian);
			g.setColor(COLOR_SHIELD_FG);
			g.drawOval(-shieldRadian, -shieldRadian, 2 * shieldRadian, 2 * shieldRadian);
		} else {
			g.setColor(COLOR_SHIELD_BG);
			g.fillRect(-shieldRadian, -shieldRadian, 2 * shieldRadian, 2 * shieldRadian);
			g.setColor(COLOR_SHIELD_FG);
			g.drawRect(-shieldRadian, -shieldRadian, 2 * shieldRadian, 2 * shieldRadian);
		}

		// Einschläge
		if (MainZap.fancyGraphics) {
			synchronized (hits) { // Zugriff vor Calc-Loop shotgunnen.
				for (SimpleShieldAbsorbtionEffect e : hits) {
					if (e == null) // Seltener Fehler
						continue;
					e.paint(g);
				}
			}
		}

		g.translate(-dx, -dy);
	}

	public void updateShield() {

		if (timeSincelastShock < SHIELD_DOWNTIME_AFTER_SHOCK) {
			timeSincelastShock++;
			return;
		}

		if (timeSinceLastDamage > nonRecoveryTime) {
			// regenerieren
			shield += shieldRegen;
			if (shield > maxShield)
				shield = maxShield;
			setDmgIndicatingTime(DMG_INDICATING_MAX_TIME); // ### DEBUG
		} else {
			// warten
			timeSinceLastDamage++;
		}

	}

	@Override
	public void shock() { // Schock deaktiviert Schilde
		super.shock();
		shield = 0;
		timeSincelastShock = 0;
	}

	public int getShield() {
		return shield;
	}

	public void setShield(int shield) {
		this.shield = shield;
	}

	public int getMaxShield() {
		return maxShield;
	}

	public void setMaxShield(int maxShield) {
		this.maxShield = maxShield;
	}

	public float getShieldRegen() {
		return shieldRegen;
	}

	public void setShieldRegen(float shieldRegen) {
		this.shieldRegen = shieldRegen;
	}

	public int getNonRecoveryTime() {
		return nonRecoveryTime;
	}

	public void setNonRecoveryTime(int nonRecoveryTime) {
		this.nonRecoveryTime = nonRecoveryTime;
	}

}
