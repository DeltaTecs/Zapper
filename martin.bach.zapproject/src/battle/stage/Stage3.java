package battle.stage;

import java.awt.Rectangle;

import battle.ai.ArmyCombatProtocol;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.enemy.Enemy;
import battle.enemy.EnemyAlpha0;
import battle.stage._3.EnemyBaseAlpha0;
import battle.stage._3.EnemyBasecoreAlpha0;
import battle.stage._3.EnemyTurretAlpha0;
import battle.stage._3.FriendAlpha1;
import corecase.MainZap;
import lib.ScheduledList;

public class Stage3 extends Stage {

	private static final int LVL = 3;
	private static final String NAME = "Subject Solaris 9";
	private static final String DESCRIPTION = "Obj. Destroy the enemy base";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.MEDIUM;

	private static final int PROP_ENEMYSPAWN = 45;
	private static final int PROP_FRIENDSPAWN = 47;

	private static final Rectangle BATTLE_AREA_ENEMY = new Rectangle(1000, 1800, 1000, 1200);
	private static final Rectangle BATTLE_AREA_FRIEND = new Rectangle(1000, 0, 1000, 1200);

	private static final int MAX_AMOUNT_AMMO_PACK_LARGE = 4;
	private static final int MAX_AMOUNT_AMMO_PACK_SMALL = 14;
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(6000);

	private ScheduledList<Enemy> all = new ScheduledList<Enemy>();
	private EnemyTurretAlpha0[] turrets = new EnemyTurretAlpha0[8];
	private EnemyBasecoreAlpha0 core;

	private SpawnScheduler[] spawner;

	public Stage3() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1500, 2400);

		spawner = new SpawnScheduler[] { new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_LARGE, 0, PackType.AMMO_LARGE),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_SMALL, 0, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		// - Base Frame+Core ------------
		core = new EnemyBasecoreAlpha0(this, all);
		EnemyBaseAlpha0 base = new EnemyBaseAlpha0(core);

		super.getPaintingTasks().add(base); // Manuell als Stagebound
											// registrieren
		MainZap.getMap().addPaintElement(base, true);
		base.setPosition(1500, 1500);

		core.setPosition(1500, 1500);
		core.register();
		all.add(core);
		// ----

		// - Turrets ------------------
		turrets[0] = new EnemyTurretAlpha0(1610, 1630);
		turrets[1] = new EnemyTurretAlpha0(1390, 1630);
		turrets[2] = new EnemyTurretAlpha0(1390, 1370);
		turrets[3] = new EnemyTurretAlpha0(1610, 1370);
		turrets[4] = new EnemyTurretAlpha0(1690, 1700);
		turrets[5] = new EnemyTurretAlpha0(1310, 1700);
		turrets[6] = new EnemyTurretAlpha0(1310, 1300);
		turrets[7] = new EnemyTurretAlpha0(1690, 1300);

		for (EnemyTurretAlpha0 turret : turrets) {
			turret.register();
			all.add(turret);
		}
		// ----

		// - Schiffe, die schon vorher da waren -------
		for (int i = 0; i != 40; i++) {
			FriendAlpha1 friend = new FriendAlpha1(rand(1000) + 1000, rand(1600) + 1800, all);
			all.schedAdd(friend);
			friend.register();
			((ArmyCombatProtocol) friend.getAiProtocol()).setMovementBounds(BATTLE_AREA_FRIEND);
		}

		for (int i = 0; i != 30; i++) {
			EnemyAlpha0 enemy = new EnemyAlpha0(rand(1000) + 1000, rand(2000) - 600, all);
			all.schedAdd(enemy);
			enemy.register();
			((ArmyCombatProtocol) enemy.getAiProtocol()).setMovementBounds(BATTLE_AREA_ENEMY);
		}
		// ----

	}

	@Override
	public void update() {

		for (SpawnScheduler s : spawner) {
			s.update();
		}

		if (!core.isAlive())
			return;

		if (rand(PROP_ENEMYSPAWN) == 0) {
			EnemyAlpha0 enemy = new EnemyAlpha0(rand(1000) + 1000, -600, all);
			all.schedAdd(enemy);
			enemy.register();
			((ArmyCombatProtocol) enemy.getAiProtocol()).setMovementBounds(BATTLE_AREA_ENEMY);
		}

		if (rand(PROP_FRIENDSPAWN) == 0) {
			FriendAlpha1 friend = new FriendAlpha1(rand(1000) + 1000, 3600, all);
			all.schedAdd(friend);
			friend.register();
			((ArmyCombatProtocol) friend.getAiProtocol()).setMovementBounds(BATTLE_AREA_FRIEND);
		}

		all.update();

	}

	public void destroyTurrets() {

		for (EnemyTurretAlpha0 t : turrets) {

			if (t.getHealth() <= 0)
				continue; // schon tot

			t.explode();
		}

	}

}
