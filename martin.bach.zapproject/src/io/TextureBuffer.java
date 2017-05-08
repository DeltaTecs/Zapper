package io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import corecase.MainZap;

public abstract class TextureBuffer {

	private static final String PATH = MainZap.DIRECTORY + "\\assets\\textures\\";

	public static final String NAME_HEADIMAGE = "headImage.png";
	public static final String NAME_PLAYERSHIP_DEFAULT = "playerShipDefault.png";
	public static final String NAME_PLAYERSHIP_DELTAVI = "playerShipDeltaVI.png";
	public static final String NAME_PLAYERSHIP_DELTAVII = "playerShipDeltaVII.png";
	public static final String NAME_PLAYERSHIP_ASHSLIDER = "playerShipAshslider.png";
	public static final String NAME_PLAYERSHIP_FALCONIII = "playerShipFalconIII.png";
	public static final String NAME_PLAYERSHIP_DARKPERL = "playerShipDarkPerl.png";
	public static final String NAME_PLAYERSHIP_RAINMAKER = "playerShipRainmaker.png";
	public static final String NAME_ENEMYSHIP_ALPHA0 = "enemyShipAlpha0.png";
	public static final String NAME_FRIENDSHIP_ALPHA1 = "friendShipAlpha1.png";
	public static final String NAME_FRIENDSHIP_BETA = "friendShipBeta.png";
	public static final String NAME_FRIENDSHIP_TRANSPORTER = "friendShipTransporter.png";
	public static final String NAME_FRIENDTURRET_SHOP0 = "friendTurretShop0.png";
	public static final String NAME_ENEMYSHIP_ALPHA2 = "enemyShipAlpha2.png";
	public static final String NAME_ENEMYSHIP_BETA0 = "enemyShipBeta0.png";
	public static final String NAME_ENEMYSHIP_BETA1 = "enemyShipBeta1.png";
	public static final String NAME_ENEMYSHIP_BETA2 = "enemyShipBeta2.png";
	public static final String NAME_ENEMYSHIP_RAIDER_BASIC_0 = "enemyShipRaider0.png";
	public static final String NAME_ENEMYSHIP_RAIDER_BASIC_1 = "enemyShipRaider1.png";
	public static final String NAME_ENEMYSHIP_RAIDER_BASIC_2 = "enemyShipRaider2.png";
	public static final String NAME_ENEMYSHIP_RAIDER_BASIC_3 = "enemyShipRaider3.png";
	public static final String NAME_ENEMYSHIP_RAIDER_HEAVY_0 = "enemyShipRaider4.png";
	public static final String NAME_ENEMYSHIP_RAIDER_HEAVY_1 = "enemyShipRaider5.png";
	public static final String NAME_ENEMYSHIP_RAIDER_DELTAVII = "enemyShipRaiderDeltaVII.png";
	public static final String NAME_ENEMYTURRET_ALPHA0 = "enemyTurretAlpha0.png";
	public static final String NAME_ENEMYBASE_ALPHA0 = "enemyBaseAlpha0.png";
	public static final String NAME_ENEMYBASE_ALPHA0_DES = "enemyBaseAlpha0Des.png";
	public static final String NAME_ENEMYBASECORE_ALPHA0 = "enemyBasecoreAlpha0.png";
	public static final String NAME_STRUCTURE_SHOP0 = "strShop0.png";
	public static final String NAME_STRUCTURE_ROCK0 = "strRock0.png";
	public static final String NAME_COLLECT_HEALTHUP_ROUND = "collectHealthupRound.png";
	public static final String NAME_COLLECT_AMMO_SMALL_ROUND = "collectAmmopackSmallRound.png";
	public static final String NAME_COLLECT_AMMO_BIG_ROUND = "collectAmmopackBigRound.png";
	public static final String NAME_COLLECT_SPEEDUP_ROUND = "collectSpeedUpRound.png";
	public static final String NAME_COLLECT_BULLET_SPEED_UP_ROUND = "collectBulletSpeedUpRound.png";
	public static final String NAME_COLLECT_BULLET_DMG_UP_ROUND = "collectBulletDamageUpRound.png";
	public static final String NAME_COLLECT_BULLET_RANGE_UP_ROUND = "collectBulletRangeUpRound.png";
	public static final String NAME_COLLECT_RELOAD_UP_ROUND = "collectReloadSpeedUpRound.png";
	public static final String NAME_COLLECT_HEALTHUP_CORNER = "collectHealthup.png";
	public static final String NAME_COLLECT_AMMO_SMALL_CORNER = "collectAmmopackSmall.png";
	public static final String NAME_COLLECT_AMMO_BIG_CORNER = "collectAmmopackBig.png";
	public static final String NAME_COLLECT_SPEEDUP_CORNER = "collectSpeedUp.png";
	public static final String NAME_COLLECT_BULLET_SPEED_UP_CORNER = "collectBulletSpeedUp.png";
	public static final String NAME_COLLECT_BULLET_DMG_UP_CORNER = "collectBulletDamageUp.png";
	public static final String NAME_COLLECT_BULLET_RANGE_UP_CORNER = "collectBulletRangeUp.png";
	public static final String NAME_COLLECT_RELOAD_UP_CORNER = "collectReloadSpeedUp.png";
	public static final String NAME_CRYSTAL = "crystal.png";
	public static final String NAME_BUTTON_LVL_UP_ROUND = "nextLvlRound.png";
	public static final String NAME_BUTTON_LVL_UP_CORNER = "nextLvl.png";
	public static final String NAME_TUTORIAL_SCREEN = "tutorial.png";
	public static final String NAME_SYMBOL_RESTART = "symbolRestart.png";
	public static final String NAME_SYMBOL_SETTINGS = "symbolSettings.png";
	public static final String NAME_SYMBOL_STAT_SPEED = "symbolStatSpeed.png";
	public static final String NAME_SYMBOL_STAT_DAMAGE = "symbolStatDamage.png";
	public static final String NAME_SYMBOL_STAT_RELOAD_WITH = "symbolStatReloadWith.png";
	public static final String NAME_SYMBOL_STAT_RELOAD_WITHOUT = "symbolStatReloadWithout.png";
	public static final String NAME_SYMBOL_STAT_BULLETSPEED = "symbolStatBulletspeed.png";
	public static final String NAME_SYMBOL_STAT_EFFICIENCY = "symbolStatEfficiency.png";
	public static final String NAME_SYMBOL_STAT_HEALTH = "symbolStatHealth.png";
	public static final String NAME_SYMBOL_STAT_RANGE = "symbolStatRange.png";
	public static final String NAME_SYMBOL_EXT_MIRROR = "symbolExtMirror.png";
	public static final String NAME_SYMBOL_EXT_SHIELD = "symbolExtDefensive.png";
	public static final String NAME_SYMBOL_EXT_SHOCK = "symbolExtShock.png";
	public static final String NAME_SYMBOL_EXT_ADDCANNON = "symbolExtAddCannon.png";
	public static final String NAME_GRAPHIC_BUY_NEW_SHIP = "graphicBuyNewShip.png";
	public static final String NAME_GRAPHIC_UPGRADE_SHIP = "graphicUpgradeShip.png";
	private static HashMap<String, BufferedImage> textures = new HashMap<String, BufferedImage>();
	private static ArrayList<String> names = new ArrayList<String>();

	private static void initLoadList() {

		names.add(NAME_HEADIMAGE);
		names.add(NAME_PLAYERSHIP_DELTAVI);
		names.add(NAME_PLAYERSHIP_DELTAVII);
		names.add(NAME_PLAYERSHIP_DEFAULT);
		names.add(NAME_PLAYERSHIP_ASHSLIDER);
		names.add(NAME_PLAYERSHIP_FALCONIII);
		names.add(NAME_PLAYERSHIP_DARKPERL);
		names.add(NAME_PLAYERSHIP_RAINMAKER);
		names.add(NAME_ENEMYSHIP_ALPHA0);
		names.add(NAME_FRIENDSHIP_ALPHA1);
		names.add(NAME_FRIENDSHIP_BETA);
		names.add(NAME_FRIENDSHIP_TRANSPORTER);
		names.add(NAME_FRIENDTURRET_SHOP0);
		names.add(NAME_ENEMYSHIP_BETA0);
		names.add(NAME_ENEMYSHIP_BETA1);
		names.add(NAME_ENEMYSHIP_BETA2);
		names.add(NAME_ENEMYSHIP_RAIDER_BASIC_0);
		names.add(NAME_ENEMYSHIP_RAIDER_BASIC_1);
		names.add(NAME_ENEMYSHIP_RAIDER_BASIC_2);
		names.add(NAME_ENEMYSHIP_RAIDER_BASIC_3);
		names.add(NAME_ENEMYSHIP_RAIDER_HEAVY_0);
		names.add(NAME_ENEMYSHIP_RAIDER_HEAVY_1);
		names.add(NAME_ENEMYSHIP_RAIDER_DELTAVII);
		names.add(NAME_ENEMYTURRET_ALPHA0);
		names.add(NAME_ENEMYBASE_ALPHA0);
		names.add(NAME_ENEMYBASE_ALPHA0_DES);
		names.add(NAME_ENEMYBASECORE_ALPHA0);
		names.add(NAME_STRUCTURE_SHOP0);
		names.add(NAME_STRUCTURE_ROCK0);
		names.add(NAME_COLLECT_HEALTHUP_ROUND);
		names.add(NAME_COLLECT_AMMO_SMALL_ROUND);
		names.add(NAME_COLLECT_AMMO_BIG_ROUND);
		names.add(NAME_COLLECT_SPEEDUP_ROUND);
		names.add(NAME_COLLECT_BULLET_DMG_UP_ROUND);
		names.add(NAME_COLLECT_BULLET_RANGE_UP_ROUND);
		names.add(NAME_COLLECT_BULLET_SPEED_UP_ROUND);
		names.add(NAME_COLLECT_RELOAD_UP_ROUND);
		names.add(NAME_COLLECT_HEALTHUP_CORNER);
		names.add(NAME_COLLECT_AMMO_SMALL_CORNER);
		names.add(NAME_COLLECT_AMMO_BIG_CORNER);
		names.add(NAME_COLLECT_SPEEDUP_CORNER);
		names.add(NAME_COLLECT_BULLET_DMG_UP_CORNER);
		names.add(NAME_COLLECT_BULLET_RANGE_UP_CORNER);
		names.add(NAME_COLLECT_BULLET_SPEED_UP_CORNER);
		names.add(NAME_COLLECT_RELOAD_UP_CORNER);
		names.add(NAME_BUTTON_LVL_UP_ROUND);
		names.add(NAME_BUTTON_LVL_UP_CORNER);
		names.add(NAME_CRYSTAL);
		names.add(NAME_TUTORIAL_SCREEN);
		names.add(NAME_SYMBOL_RESTART);
		names.add(NAME_SYMBOL_SETTINGS);
		names.add(NAME_SYMBOL_STAT_SPEED);
		names.add(NAME_SYMBOL_STAT_DAMAGE);
		names.add(NAME_SYMBOL_STAT_RELOAD_WITH);
		names.add(NAME_SYMBOL_STAT_RELOAD_WITHOUT);
		names.add(NAME_SYMBOL_STAT_BULLETSPEED);
		names.add(NAME_SYMBOL_STAT_EFFICIENCY);
		names.add(NAME_SYMBOL_STAT_HEALTH);
		names.add(NAME_SYMBOL_STAT_RANGE);
		names.add(NAME_SYMBOL_EXT_ADDCANNON);
		names.add(NAME_SYMBOL_EXT_SHIELD);
		names.add(NAME_SYMBOL_EXT_MIRROR);
		names.add(NAME_SYMBOL_EXT_SHOCK);
		names.add(NAME_GRAPHIC_BUY_NEW_SHIP);
		names.add(NAME_GRAPHIC_UPGRADE_SHIP);
		// Bilder eintragen...

	}

	public static void load() {

		initLoadList(); // Eintragen

		for (String name : names) { // Auswerten

			try {
				BufferedImage img = ImageIO.read(new File(PATH + name));

				textures.put(name, img);

			} catch (IOException e) { // Nicht gefunden
				System.err.println(
						"[ERR] Datei nicht gefunden: " + name + "; Name vomöglich fehlerhaft eingetragen! Schließe.");
				System.exit(-1);
			}

		}

	}

	public static BufferedImage get(String name) {

		if (!textures.containsKey(name)) {
			System.err.println("[ERR] Bild nicht eingetragen: " + name + " ! Schließe.");
			System.exit(-1);
		}

		return textures.get(name);

	}

}
