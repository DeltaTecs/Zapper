package battle.stage;

import java.awt.Rectangle;
import java.util.ArrayList;

import battle.CombatObject;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.FindLockAction;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.enemy.Enemy;
import battle.stage._10.EnemyGamma2;
import battle.stage._10.FriendGamma0;
import battle.stage._11.Stargate;
import battle.stage._9.EnemyGamma0;
import battle.stage._9.EnemyGamma1;
import corecase.MainZap;
import gui.Map;

public class Stage11 extends Stage {

	private static final int LVL = 11;
	private static final String NAME = "The Source";
	private static final String DESCRIPTION = "destroy the Gate";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.MEDIUM;

	private static final int SPAWN_RATE_AMMO_SMALL = MainZap.getMainLoop().inTicks(1500);
	private static final int SPAWN_RATE_AMMO_LARGE = MainZap.getMainLoop().inTicks(4000);
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(4500);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(4000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(4000);

	private static final Rectangle FLEE_BOUNDS = new Rectangle(50, 50, 2900, 2900);
	private static final Rectangle NO_UPGRADES_AREA = new Rectangle(1000, 1000, 1000, 1000);
	private static final int ENEMY_SPAWN_X = 1500;
	private static final int ENEMY_SPAWN_Y = 2000;
	private static final int RAND_SPAWN_ENEMY_0 = MainZap.inTicks(6000);
	private static final int RAND_SPAWN_ENEMY_1 = MainZap.inTicks(14000);
	private static final int RAND_SPAWN_ENEMY_2 = MainZap.inTicks(14000);
	private static final float RAND_SPPAWN_DECREASE_FAC = 0.9997f;
	private static final int GAMMA_0_ASSISTANCE = 12;

	private SpawnScheduler[] spawner;
	private Stargate gate;
	private ArrayList<CombatObject> enemys = new ArrayList<CombatObject>();
	private ArrayList<CombatObject> friendsAndPlayer = new ArrayList<CombatObject>();
	private float randSpawnEnemy0 = RAND_SPAWN_ENEMY_0;
	private float randSpawnEnemy1 = RAND_SPAWN_ENEMY_1;
	private float randSpawnEnemy2 = RAND_SPAWN_ENEMY_2;
	private boolean enemysDespawned = false;

	public Stage11() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1500, 2000);
		spawner = new SpawnScheduler[] { new SpawnScheduler(50, SPAWN_RATE_AMMO_SMALL, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_AMMO_LARGE, PackType.AMMO_LARGE),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		for (SpawnScheduler s : spawner)
			s.setProhibitedArea(NO_UPGRADES_AREA);

		friendsAndPlayer.add(MainZap.getPlayer());
		applyRemoveTask(friendsAndPlayer, MainZap.getPlayer());

		// Assistance
		for (int i = 0; i != GAMMA_0_ASSISTANCE; i++) {
			FriendGamma0 assist = new FriendGamma0();
			assist.setPosition(1200 + rand(600), 2400);
			((AdvancedSingleProtocol) assist.getAiProtocol()).setLockAction(FindLockAction.LOCK_LINKED_ENEMYS);
			((AdvancedSingleProtocol) assist.getAiProtocol()).setLinkedEnemys(enemys);
			((AdvancedSingleProtocol) assist.getAiProtocol()).setNoAlternativeLock(true);
			friendsAndPlayer.add(assist);
			applyRemoveTask(friendsAndPlayer, assist);
			assist.register();
		}

		// Stargate spawnen
		gate = new Stargate(1500, 1500, this);
		MainZap.getMap().addPaintElement(gate, true);
		getPaintingTasks().add(gate); // manuelles registrieren

	}

	@Override
	public void update() {

		for (SpawnScheduler s : spawner)
			s.update();

		gate.update();

		if (!MainZap.getPlayer().isAlive() || gate.isCollapsing())
			return;

		randSpawnEnemy0 *= RAND_SPPAWN_DECREASE_FAC;
		randSpawnEnemy1 *= RAND_SPPAWN_DECREASE_FAC;
		randSpawnEnemy2 *= RAND_SPPAWN_DECREASE_FAC;

		if (rand((int) randSpawnEnemy0) == 0)
			spawnEnemy(0);
		if (rand((int) randSpawnEnemy1) == 0)
			spawnEnemy(1);
		if (rand((int) randSpawnEnemy2) == 0)
			spawnEnemy(2);
	}

	public void despawnEnemys() { // panische Flucht
		enemysDespawned = true;
		for (CombatObject e : enemys)
			((Enemy) e).warpOut(rand(Map.SIZE), rand(Map.SIZE), MainZap.inTicks(500));
	}

	private void spawnEnemy(int type) {

		Enemy e = null;
		int dx = rand(300) - 150;
		int dy = rand(300) - 150;

		switch (type) {

		case 0:
			e = new EnemyGamma0(ENEMY_SPAWN_X + dx, ENEMY_SPAWN_Y + dy, FLEE_BOUNDS);
			break;

		case 1:
			e = new EnemyGamma1(ENEMY_SPAWN_X + dx, ENEMY_SPAWN_Y + dy, FLEE_BOUNDS);
			break;

		case 2:
			e = new EnemyGamma2(ENEMY_SPAWN_X + dx, ENEMY_SPAWN_Y + dy, FLEE_BOUNDS);
			break;

		default: // gibts nich
			throw new RuntimeException("ein EnemyGamma" + type + " existiert nicht");
		}

		enemys.add(e);
		applyRemoveTask(enemys, e);
		((AdvancedSingleProtocol) e.getAiProtocol()).setLinkedEnemys(friendsAndPlayer);
		e.warpIn(ENEMY_SPAWN_X + dx, ENEMY_SPAWN_Y + 1024);
		// e.setNoWaitAfterWarp(true);
		e.register();
	}

	public boolean isEnemysDespawned() {
		return enemysDespawned;
	}

}
