package gui.shop.meta;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import battle.WeaponPositioning;
import battle.projectile.ProjectileDesign;
import collision.CollisionInformation;
import corecase.Cmd;
import gui.effect.ExplosionEffectPattern;

public class ShipStartConfig {

	public static final int C_DEFAULT = 0;
	public static final int C_DELTA_VI = 1;
	public static final int C_ASHSLIDER = 2;
	public static final int C_DELTA_VII = 3;

	private static ArrayList<ShipStartConfig> configs = new ArrayList<ShipStartConfig>();

	private BufferedImage texture;
	private float scale;
	private int damage;
	private float speed;
	private float projSpeed;
	private float reloadWithout;
	private float reloadWith;
	private float efficiency;
	private int hp;
	private CollisionInformation collInfo;
	private int projRange;
	private ProjectileDesign projDesign;
	private ExplosionEffectPattern explPattern;
	private String name;
	private String description;
	private int price;
	private WeaponPositioning weaponPosSingle;
	private WeaponPositioning weaponPosDouble;
	private WeaponPositioning weaponPosTriple;

	public ShipStartConfig(BufferedImage texture, float scale, int damage, float speed, float projSpeed,
			float reloadTimeWithAmmo, float reloadTimeOutOfAmmo, int hp, CollisionInformation collInfo, int projRange,
			ProjectileDesign projDesign, ExplosionEffectPattern explPattern, float ammoUsageFac, String name,
			String description, int price, WeaponPositioning weaponPosSingle, WeaponPositioning weaponPosDouble,
			WeaponPositioning weaponPosTriple) {
		super();
		this.texture = texture;
		this.scale = scale;
		this.damage = damage;
		this.speed = speed;
		this.projSpeed = projSpeed;
		this.reloadWith = reloadTimeWithAmmo;
		this.reloadWithout = reloadTimeOutOfAmmo;
		this.efficiency = ammoUsageFac;
		this.hp = hp;
		this.collInfo = collInfo;
		this.projRange = projRange;
		this.projDesign = projDesign;
		this.explPattern = explPattern;
		this.name = name;
		this.description = description;
		this.price = price;
		this.weaponPosSingle = weaponPosSingle;
		this.weaponPosDouble = weaponPosDouble;
		this.weaponPosTriple = weaponPosTriple;

	}

	public static void loadAll() {
		// Greift auf Texturen zurück. -> Benötigt TextureBuffer init
		configs.add(new DefaultShipConfig());
		configs.add(new DeltaVIShipConfig());
		configs.add(new AshshliderShipConfig());
		configs.add(new DeltaVIIShipConfig());
		// ... weitere eintragen
	}

	public static ShipStartConfig get(int id) {
		if (id > configs.size() - 1) {
			Cmd.err("Ship Config not kown: " + id);
			System.exit(-1);
		}

		return configs.get(id);
	}

	public static ArrayList<ShipStartConfig> getConfigs() {
		return configs;
	}

	public String getDescription() {
		return description;
	}

	public int getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public float getScale() {
		return scale;
	}

	public int getDamage() {
		return damage;
	}

	public float getSpeed() {
		return speed;
	}

	public float getProjSpeed() {
		return projSpeed;
	}

	/**
	 * Ladezeit ohne Munition
	 * 
	 * @return
	 */
	public float getReloadWithout() {
		return reloadWithout;
	}

	/**
	 * Ladezeit mit Munition
	 * 
	 * @return
	 */
	public float getReloadWith() {
		return reloadWith;
	}

	public int getHp() {
		return hp;
	}

	public float getEfficiency() {
		return efficiency;
	}

	public CollisionInformation getCollInfo() {
		return collInfo;
	}

	public int getProjRange() {
		return projRange;
	}

	public ProjectileDesign getProjDesign() {
		return projDesign;
	}

	public ExplosionEffectPattern getExplPattern() {
		return explPattern;
	}

	public WeaponPositioning getWeaponPosSingle() {
		return weaponPosSingle;
	}

	public WeaponPositioning getWeaponPosDouble() {
		return weaponPosDouble;
	}

	public WeaponPositioning getWeaponPosTriple() {
		return weaponPosTriple;
	}

}
