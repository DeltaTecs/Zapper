package gui.shop.meta;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import battle.WeaponPositioning;
import battle.projectile.ProjectileDesign;
import collision.CollisionInformation;
import corecase.Cmd;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;

public class ShipStartConfig {

	public static final int C_DEFAULT = 0;
	public static final int C_DELTA_VI = 1;
	public static final int C_FALCON_III = 2;
	public static final int C_DARKPERL = 3;
	public static final int C_ASHSLIDER = 4;
	public static final int C_DELTA_VII = 5;
	public static final int C_RAINMAKER = 6;

	private static ArrayList<ShipStartConfig> configs = new ArrayList<ShipStartConfig>();

	private BufferedImage texture;
	private TailManager tailManager;
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
	private boolean lockable = false;


	public ShipStartConfig(BufferedImage texture, TailManager tailmanager, float scale, int damage, float speed,
			float projSpeed, float reloadWith, float reloadWithout, int hp, CollisionInformation collInfo,
			int projRange, ProjectileDesign projDesign, ExplosionEffectPattern explPattern, float ammoUsage,
			String name, String description, int price, WeaponPositioning weaponPositionsSingle,
			WeaponPositioning weaponPositionsDouble, WeaponPositioning weaponPositionsTriple, boolean lockable) {
		this.texture = texture;
		this.tailManager = tailmanager;
		this.scale = scale;
		this.damage = damage;
		this.speed = speed;
		this.projSpeed = projSpeed;
		this.reloadWithout = reloadWithout;
		this.reloadWith = reloadWith;
		this.efficiency = ammoUsage;
		this.hp = hp;
		this.collInfo = collInfo;
		this.projRange = projRange;
		this.projDesign = projDesign;
		this.explPattern = explPattern;
		this.name = name;
		this.description = description;
		this.price = price;
		this.weaponPosSingle = weaponPositionsSingle;
		this.weaponPosDouble = weaponPositionsDouble;
		this.weaponPosTriple = weaponPositionsTriple;
		this.lockable = lockable;
	}

	public static void loadAll() {
		// Greift auf Texturen zur�ck. -> Ben�tigt TextureBuffer init
		configs.add(new DefaultShipConfig());
		configs.add(new DeltaVIShipConfig());
		configs.add(new FalconIIIShipConfig());
		configs.add(new DarkperlShipConfig());
		configs.add(new AshshliderShipConfig());
		configs.add(new DeltaVIIShipConfig());
		configs.add(new RainmakerShipConfig());
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
	

	public TailManager getTailManager() {
		return tailManager;
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

	public boolean isLockable() {
		return lockable;
	}
}
