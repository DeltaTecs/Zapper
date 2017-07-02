package battle.stage;

import java.awt.Rectangle;
import java.util.ArrayList;

import battle.CombatObject;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.AiProtocol;
import battle.ai.DieCall;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.enemy.Enemy;
import battle.stage._9.EnemyGamma0;
import battle.stage._9.EnemyGamma1;
import battle.stage._9.FriendBaseAlpha;
import battle.stage._9.FriendBasecoreAlpha;
import battle.stage._9.FriendTurretAlpha;
import corecase.MainZap;
import ingameobjects.Player;

public class Stage9 extends Stage {

	private static final int LVL = 9;
	private static final String NAME = "Unknown Enemy";
	private static final String DESCRIPTION = "defend the base";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.MEDIUM;

	private static final int SPAWN_RATE_AMMO_SMALL = MainZap.getMainLoop().inTicks(1500);
	private static final int SPAWN_RATE_AMMO_LARGE = MainZap.getMainLoop().inTicks(4000);
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(4500);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(4000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(4000);

	private static final int WAVE_0_AMOUNT_G_0 = 4;
	private static final int WAVE_0_AMOUNT_G_1 = 0;
	private static final int WAVE_1_AMOUNT_G_0 = 7;
	private static final int WAVE_1_AMOUNT_G_1 = 0;
	private static final int WAVE_2_AMOUNT_G_0 = 6;
	private static final int WAVE_2_AMOUNT_G_1 = 1;
	private static final int WAVE_3_AMOUNT_G_0 = 7;
	private static final int WAVE_3_AMOUNT_G_1 = 2;

	private static final Rectangle SPAWN_AREA_GAMMA_0 = new Rectangle(800, 1000, 1400, 1000);
	private static final Rectangle SPAWN_AREA_GAMMA_1 = new Rectangle(1100, 1500, 1800, 500);
	private static final Rectangle ENEMY_BOUNDARY = new Rectangle(50, 50, 2900, 2900);

	private int wave = 0;
	private int enemysRemaining = WAVE_0_AMOUNT_G_0 + WAVE_0_AMOUNT_G_1;
	private SpawnScheduler[] spawner;
	private FriendBaseAlpha baseBG = new FriendBaseAlpha();
	private FriendBasecoreAlpha baseCore = new FriendBasecoreAlpha(this);
	private FriendTurretAlpha[] turrets = new FriendTurretAlpha[6];
	private ArrayList<CombatObject> friendsAndPlayer = new ArrayList<CombatObject>();

	public Stage9() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 2050, 2900);
		spawner = new SpawnScheduler[] { new SpawnScheduler(50, SPAWN_RATE_AMMO_SMALL, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_AMMO_LARGE, PackType.AMMO_LARGE),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		// Base-Hintergrund init und registrierung
		baseBG.setPosition(1500, 2777);
		MainZap.getMap().addPaintElement(baseBG, true);
		getPaintingTasks().add(baseBG);

		// Base-Core init und registrierung
		baseCore.setPosition(1500, 2999);
		baseCore.register();

		friendsAndPlayer.add(MainZap.getPlayer());
		friendsAndPlayer.add(baseCore);
		applyRemoveTask(friendsAndPlayer, baseCore);
		applyRemoveTask(friendsAndPlayer, MainZap.getPlayer());

		// Turrets setzen
		double rot = Math.random() * Math.PI * 2;
		for (int i = 0; i != turrets.length; i++) {
			turrets[i] = new FriendTurretAlpha(0, 0); // init-koords
			turrets[i].setRotation(rot);
			turrets[i].register();
			friendsAndPlayer.add(turrets[i]);
			applyRemoveTask(friendsAndPlayer, turrets[i]);
		}
		turrets[0].setPosition(1642, 2308);
		turrets[1].setPosition(1500 - 142, 2308);
		turrets[2].setPosition(1786, 2544);
		turrets[3].setPosition(1500 - 286, 2544);
		turrets[4].setPosition(1830, 2852);
		turrets[5].setPosition(1500 - 330, 2852);

		// Gegener
		sendWave(WAVE_0_AMOUNT_G_0, WAVE_0_AMOUNT_G_1);

	}

	@Override
	public void update() {

		for (SpawnScheduler s : spawner)
			s.update();

		if (isPassed())
			return;

		if (enemysRemaining == 0) {
			switch (wave) {
			case 0:
				sendWave(WAVE_1_AMOUNT_G_0, WAVE_1_AMOUNT_G_1);
				wave = 1;
				break;
			case 1:
				sendWave(WAVE_2_AMOUNT_G_0, WAVE_2_AMOUNT_G_1);
				wave = 2;
				break;
			case 2:
				sendWave(WAVE_3_AMOUNT_G_0, WAVE_3_AMOUNT_G_1);
				wave = 3;
				break;
			case 3:
				pass();
				break;
			}
		}

	}

	private void sendWave(int amountG_0, int amountG_1) {

		for (int i = 0; i != amountG_0; i++) {
			EnemyGamma0 enemy = new EnemyGamma0(SPAWN_AREA_GAMMA_0.x + rand(SPAWN_AREA_GAMMA_0.width),
					SPAWN_AREA_GAMMA_0.y + rand(SPAWN_AREA_GAMMA_0.height), ENEMY_BOUNDARY);
			enemy.register();
			enemy.warpIn(baseCore.getLocX(), baseCore.getLocY());
			((AdvancedSingleProtocol) enemy.getAiProtocol()).preLock(friendsAndPlayer);
			((AdvancedSingleProtocol) enemy.getAiProtocol()).setLinkedEnemys(friendsAndPlayer);
			enemy.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {
				@Override
				public void die() {
					enemysRemaining--;
				}
			});
		}

		for (int i = 0; i != amountG_1; i++) {
			EnemyGamma1 enemy = new EnemyGamma1(SPAWN_AREA_GAMMA_1.x + rand(SPAWN_AREA_GAMMA_1.width),
					SPAWN_AREA_GAMMA_1.y + rand(SPAWN_AREA_GAMMA_1.height), ENEMY_BOUNDARY);
			enemy.register();
			enemy.warpIn(baseCore.getLocX(), baseCore.getLocY());
			enemy.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {
				@Override
				public void die() {
					enemysRemaining--;
				}
			});
			((AdvancedSingleProtocol) enemy.getAiProtocol()).preLock(friendsAndPlayer);
			((AdvancedSingleProtocol) enemy.getAiProtocol()).setLinkedEnemys(friendsAndPlayer);
		}

		enemysRemaining = amountG_0 + amountG_1;
	}

	private void applyRemoveTask(final ArrayList<CombatObject> list, final CombatObject subject) {

		final Stage stage = this;

		if (subject instanceof Player) {

			MainZap.getMainLoop().addTask(new Runnable() {
				@Override
				public void run() {
					if (!MainZap.getPlayer().isAlive() || StageManager.getActiveStage() != stage) {
						list.remove(subject);
						MainZap.getMainLoop().removeTask(this, true);
					}

				}
			}, true);

		} else if (subject instanceof Enemy) {

			Enemy e = (Enemy) subject;

			if (e.getAiProtocol() != null) { // Subjekt verfügt über AI

				e.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {

					@Override
					public void die() {
						list.remove(subject);
					}
				});

			} else {
				// keine AI-Vorhanden. Manueller Die-Call

				MainZap.getMainLoop().addTask(new Runnable() {
					@Override
					public void run() {
						if (!subject.isAlive() || StageManager.getActiveStage() != stage) {
							list.remove(subject);
							MainZap.getMainLoop().removeTask(this, true);
						}

					}
				}, true);

			}

		} else // WTF ist das subject!!???
			throw new RuntimeException("remove-tasks can only be applyed on a player or an enemy");

	}

}
