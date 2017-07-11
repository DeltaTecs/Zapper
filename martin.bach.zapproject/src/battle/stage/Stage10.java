package battle.stage;

import java.awt.Rectangle;
import java.util.ArrayList;

import battle.CombatObject;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.BoundedFleeProtocol;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.enemy.Enemy;
import battle.stage._10.EnemyGamma2;
import battle.stage._10.FriendGamma0;
import battle.stage._10.FriendGamma1;
import battle.stage._9.EnemyGamma0;
import battle.stage._9.EnemyGamma1;
import corecase.MainZap;
import gui.effect.WarpOutEffect;
import ingameobjects.Player;

public class Stage10 extends Stage {

	private static final int LVL = 10;
	private static final String NAME = "The Engage";
	private static final String DESCRIPTION = "fight";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.MEDIUM;

	private static final int SPAWN_RATE_AMMO_SMALL = MainZap.getMainLoop().inTicks(1500);
	private static final int SPAWN_RATE_AMMO_LARGE = MainZap.getMainLoop().inTicks(4000);
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(3000);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(4000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(4000);

	private static final Rectangle SPAWN_AREA_ENEMYS = new Rectangle(0, 0, 3000, 1500);
	private static final Rectangle SPAWN_AREA_FRIENDS = new Rectangle(0, 1500, 3000, 1500);
	private static final Rectangle FLEE_BOUNDS = new Rectangle(50, 50, 2900, 2900);

	private static final int AMOUNT_ENEMY_GAMMA_0 = 22;
	private static final int AMOUNT_ENEMY_GAMMA_1 = 7;
	private static final int AMOUNT_ENEMY_GAMMA_2 = 5;
	private static final int AMOUNT_FRIEND_GAMMA_0 = 14;
	private static final int AMOUNT_FRIEND_GAMMA_1 = 2;
	private static final int RAND_SPAWN_ENEMY_GAMMA_0 = 130;
	private static final int RAND_SPAWN_ENEMY_GAMMA_1 = 420;
	private static final int RAND_SPAWN_ENEMY_GAMMA_2 = 500;
	private static final int RAND_SPAWN_FRIEND_GAMMA_0 = 280;
	private static final int RAND_SPAWN_FRIEND_GAMMA_1 = 530;

	private SpawnScheduler[] spawner;
	private ArrayList<CombatObject> enemys = new ArrayList<CombatObject>();
	private ArrayList<CombatObject> friendsAndPlayer = new ArrayList<CombatObject>();
	private boolean armeeWarpingOut = false;

	public Stage10() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1500, 2400);
		spawner = new SpawnScheduler[] { new SpawnScheduler(50, SPAWN_RATE_AMMO_SMALL, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_AMMO_LARGE, PackType.AMMO_LARGE),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		friendsAndPlayer.add(MainZap.getPlayer());
		applyRemoveTask(friendsAndPlayer, MainZap.getPlayer());

		for (int i = 0; i != AMOUNT_ENEMY_GAMMA_0; i++) {
			EnemyGamma0 e = new EnemyGamma0(randEnemyLocX(), randEnemyLocY(), FLEE_BOUNDS);
			applyRemoveTask(enemys, e);
			enemys.add(e);
		}

		for (int i = 0; i != AMOUNT_ENEMY_GAMMA_1; i++) {
			EnemyGamma1 e = new EnemyGamma1(randEnemyLocX(), randEnemyLocY(), FLEE_BOUNDS);
			applyRemoveTask(enemys, e);
			enemys.add(e);
		}

		for (int i = 0; i != AMOUNT_ENEMY_GAMMA_2; i++) {
			EnemyGamma2 e = new EnemyGamma2(randEnemyLocX(), randEnemyLocY(), FLEE_BOUNDS);
			applyRemoveTask(enemys, e);
			enemys.add(e);
		}

		for (int i = 0; i != AMOUNT_FRIEND_GAMMA_0; i++) {
			FriendGamma0 f = new FriendGamma0();
			f.setPosition(randFriendLocX(), randFriendLocY());
			applyRemoveTask(friendsAndPlayer, f);
			friendsAndPlayer.add(f);
			((AdvancedSingleProtocol) f.getAiProtocol()).setLinkedEnemys(enemys);
			((AdvancedSingleProtocol) f.getAiProtocol()).preLock(enemys);
			f.register();
		}

		for (int i = 0; i != AMOUNT_FRIEND_GAMMA_1; i++) {
			FriendGamma1 f = new FriendGamma1();
			f.setPosition(randFriendLocX(), randFriendLocY());
			applyRemoveTask(friendsAndPlayer, f);
			friendsAndPlayer.add(f);
			((AdvancedSingleProtocol) f.getAiProtocol()).setLinkedEnemys(enemys);
			((AdvancedSingleProtocol) f.getAiProtocol()).preLock(enemys);
			f.register();
		}

		for (CombatObject e : enemys) {
			((AdvancedSingleProtocol) ((Enemy) e).getAiProtocol()).setLinkedEnemys(friendsAndPlayer);
			((AdvancedSingleProtocol) ((Enemy) e).getAiProtocol()).preLock(friendsAndPlayer);
			e.register();
		}

	}

	@Override
	public void update() {

		for (SpawnScheduler s : spawner)
			s.update();

		if (enemys.size() < 4)
			pass();

		if (!MainZap.getPlayer().isAlive() && friendsAndPlayer.size() == 0) {
			// Abzug der Gegener
			if (armeeWarpingOut)
				return;
			armeeWarpingOut = true;
			int warpX = rand(500) - 250;
			int warpY = rand(500) - 250;

			for (CombatObject e : enemys) {
				if (((Enemy) e).getAiProtocol() instanceof BoundedFleeProtocol)
					((BoundedFleeProtocol) ((Enemy) e).getAiProtocol()).setEnabled(false);
				((Enemy) e).warpOut(warpX + e.getLocX(), warpY + e.getLocY(), 50);
				((WarpOutEffect) ((Enemy) e).getWarpEffect()).setWaitingTime(MainZap.inTicks(900));
			}
		} else if (enemys.size() == 0) {
			// Abzug der Alliierten

			if (armeeWarpingOut)
				return;
			armeeWarpingOut = true;
			int warpX = rand(500) - 250;
			int warpY = rand(500) - 250;

			for (CombatObject e : friendsAndPlayer) {
				if (e instanceof Player)
					continue; // Splieler aussortieren
				((Enemy) e).warpOut(warpX + e.getLocX(), warpY + e.getLocY(), 50);
				((WarpOutEffect) ((Enemy) e).getWarpEffect()).setWaitingTime(MainZap.inTicks(900));
			}

		}

		if (isPassed() || !MainZap.getPlayer().isAlive())
			return;

		if (rand(RAND_SPAWN_ENEMY_GAMMA_0) == 0)
			spawnEnemyGamma0();
		if (rand(RAND_SPAWN_ENEMY_GAMMA_1) == 0)
			spawnEnemyGamma1();
		if (rand(RAND_SPAWN_ENEMY_GAMMA_2) == 0)
			spawnEnemyGamma2();
		if (rand(RAND_SPAWN_FRIEND_GAMMA_0) == 0)
			spawnFriendGamma0();
		if (rand(RAND_SPAWN_FRIEND_GAMMA_1) == 0)
			spawnFriendGamma1();

	}

	private void spawnEnemyGamma0() {
		EnemyGamma0 e = new EnemyGamma0(randEnemyLocX(), randEnemyLocY(), FLEE_BOUNDS);
		applyRemoveTask(enemys, e);
		enemys.add(e);
		((AdvancedSingleProtocol) ((Enemy) e).getAiProtocol()).setLinkedEnemys(friendsAndPlayer);
		((AdvancedSingleProtocol) ((Enemy) e).getAiProtocol()).preLock(friendsAndPlayer);
		e.register();
		e.warpIn(1500, 10000000);
	}

	private void spawnEnemyGamma1() {
		EnemyGamma1 e = new EnemyGamma1(randEnemyLocX(), randEnemyLocY(), FLEE_BOUNDS);
		applyRemoveTask(enemys, e);
		enemys.add(e);
		((AdvancedSingleProtocol) ((Enemy) e).getAiProtocol()).setLinkedEnemys(friendsAndPlayer);
		((AdvancedSingleProtocol) ((Enemy) e).getAiProtocol()).preLock(friendsAndPlayer);
		e.register();
		e.warpIn(1500, 10000000);
	}

	private void spawnEnemyGamma2() {
		EnemyGamma2 e = new EnemyGamma2(randEnemyLocX(), randEnemyLocY(), FLEE_BOUNDS);
		applyRemoveTask(enemys, e);
		enemys.add(e);
		((AdvancedSingleProtocol) ((Enemy) e).getAiProtocol()).setLinkedEnemys(friendsAndPlayer);
		((AdvancedSingleProtocol) ((Enemy) e).getAiProtocol()).preLock(friendsAndPlayer);
		e.register();
		e.warpIn(1500, 10000000);
	}

	private void spawnFriendGamma0() {
		FriendGamma0 f = new FriendGamma0();
		f.setPosition(randFriendLocX(), randFriendLocY());
		applyRemoveTask(friendsAndPlayer, f);
		friendsAndPlayer.add(f);
		((AdvancedSingleProtocol) f.getAiProtocol()).setLinkedEnemys(enemys);
		((AdvancedSingleProtocol) f.getAiProtocol()).preLock(enemys);
		f.register();
		f.warpIn(1500, -100000);
	}

	private void spawnFriendGamma1() {
		FriendGamma1 f = new FriendGamma1();
		f.setPosition(randFriendLocX(), randFriendLocY());
		applyRemoveTask(friendsAndPlayer, f);
		friendsAndPlayer.add(f);
		((AdvancedSingleProtocol) f.getAiProtocol()).setLinkedEnemys(enemys);
		((AdvancedSingleProtocol) f.getAiProtocol()).preLock(enemys);
		f.register();
		f.warpIn(1500, -100000);
	}

	private int randEnemyLocX() {
		return SPAWN_AREA_ENEMYS.x + rand(SPAWN_AREA_ENEMYS.width);
	}

	private int randEnemyLocY() {
		return SPAWN_AREA_ENEMYS.y + rand(SPAWN_AREA_ENEMYS.height);
	}

	private int randFriendLocX() {
		return SPAWN_AREA_FRIENDS.x + rand(SPAWN_AREA_FRIENDS.width);
	}

	private int randFriendLocY() {
		return SPAWN_AREA_FRIENDS.y + rand(SPAWN_AREA_FRIENDS.height);
	}

}
