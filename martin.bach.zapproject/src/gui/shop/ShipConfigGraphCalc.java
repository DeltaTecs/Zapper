package gui.shop;

import gui.shop.meta.ShipStartConfig;

public class ShipConfigGraphCalc {

	public static final boolean LINEAR_MODE = true;

	// Nur für linearen Modus:
	private static int maxHealth = 500000;
	private static int maxDamage = 4000;
	private static float maxSpeed = 16.0f;
	private static float maxEfficiency = 10.0f; // umdrehen
	private static float maxProjSpeed = 50.0f;
	private static int maxProjRange = 2000;
	private static float minReloadWith = 10000.0f;
	private static float minReloadWithout = 10000.0f;

	private ShipStartConfig config;

	private float health;
	private float damage;
	private float speed;
	private float reloadWith;
	private float reloadWithout;
	private float efficiency;
	private float projSpeed;
	private float projRange;

	private PaintableDescription description;

	public ShipConfigGraphCalc(ShipStartConfig config) {
		super();
		this.config = config;
		if (LINEAR_MODE) {
			health = config.getHp() / (float) maxHealth;
			damage = config.getDamage() / (float) maxDamage;
			speed = config.getSpeed() / (float) maxSpeed;
			reloadWith = minReloadWith / config.getReloadWith();
			reloadWithout = minReloadWithout / config.getReloadWithout();
			efficiency = config.getEfficiency() / maxEfficiency;
			projSpeed = config.getProjSpeed() / maxProjSpeed;
			projRange = config.getProjRange() / (float) maxProjRange;
		} else {
			health = 1.0f - (1.0f / (float) Math.pow(1.5, config.getHp() / 30000.0f));
			damage = 1.0f - (1.0f / (float) Math.pow(1.5, config.getDamage() / 300.0f));
			speed = 1.0f - (1.0f / (float) Math.pow(1.5, config.getSpeed() / 2.9f));
			reloadWith = 1.0f / (float) Math.pow(1.5, config.getReloadWith() / 2);
			reloadWithout = 1.0f / (float) Math.pow(1.5, config.getReloadWithout() / 2);
			efficiency = 1.0f - (1.0f / (float) Math.pow(1.5, config.getEfficiency() / 2.0f));
			projSpeed = 1.0f - (1.0f / (float) Math.pow(1.5, config.getProjSpeed() / 10.9f));
			projRange = 1.0f - (1.0f / (float) Math.pow(1.5, config.getProjRange() / 800.0f));
		}

		description = new PaintableDescription(config.getDescription());
	}

	// Läuft alle verfügbaren Statistiken ab und trägen die höchsten Werte ein
	public static void scanForStatMaximas() {
		maxHealth = 0;
		maxSpeed = 0;
		maxDamage = 0;
		maxEfficiency = 0.0f;
		maxProjSpeed = 0;
		maxProjRange = 0;
		minReloadWith = 1000000.0f;
		for (ShipStartConfig c : ShipStartConfig.getConfigs()) {

			if (c.getHp() > maxHealth)
				maxHealth = c.getHp();

			if (c.getSpeed() > maxSpeed)
				maxSpeed = c.getSpeed();

			if (c.getDamage() > maxDamage)
				maxDamage = c.getDamage();

			if (c.getEfficiency() > maxEfficiency)
				maxEfficiency = c.getEfficiency();

			if (c.getProjSpeed() > maxProjSpeed)
				maxProjSpeed = c.getProjSpeed();

			if (c.getProjRange() > maxProjRange)
				maxProjRange = c.getProjRange();

			if (c.getReloadWith() < minReloadWith)
				minReloadWith = c.getReloadWith();

			if (c.getReloadWithout() < minReloadWithout)
				minReloadWithout = c.getReloadWithout();
		}

	}

	public float getHealth() {
		return health;
	}

	public float getDamage() {
		return damage;
	}

	public float getReloadWith() {
		return reloadWith;
	}

	public float getReloadWithout() {
		return reloadWithout;
	}

	public float getSpeed() {
		return speed;
	}

	public float getProjSpeed() {
		return projSpeed;
	}

	public float getEfficiency() {
		return efficiency;
	}

	public float getProjRange() {
		return projRange;
	}

	public ShipStartConfig getConfig() {
		return config;
	}

	public PaintableDescription getDescription() {
		return description;
	}

}
