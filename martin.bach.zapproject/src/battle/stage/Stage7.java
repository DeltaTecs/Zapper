package battle.stage;

import java.util.ArrayList;

import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.stage._7.EnemyBaseRaider;
import battle.stage._7.EnemyBasecoreRaider;
import battle.stage._7.EnemyTurretRaider;
import corecase.MainZap;

public class Stage7 extends Stage {

	private static final int LVL = 7;
	private static final String NAME = "The Raiders Base";
	private static final String DESCRIPTION = "Kill everything";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.HARD;

	private static final int MAX_AMOUNT_AMMO_PACK_LARGE = 4;
	private static final int MAX_AMOUNT_AMMO_PACK_SMALL = 14;
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(6000);

	private SpawnScheduler[] spawner;
	private EnemyBasecoreRaider core;
	private ArrayList<EnemyTurretRaider> turrets = new ArrayList<EnemyTurretRaider>();

	public Stage7() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1500, 1500);

		spawner = new SpawnScheduler[] { new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_LARGE, 0, PackType.AMMO_LARGE),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_SMALL, 0, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		EnemyBaseRaider baseImg = new EnemyBaseRaider();
		baseImg.setPosition(1500, 1500);
		MainZap.getMap().addPaintElement(baseImg, true);
		super.getPaintingTasks().add(baseImg); // Manuell als Stagebound
												// registrieren

		EnemyTurretRaider turret0 = new EnemyTurretRaider(1100, 2000);
		EnemyTurretRaider turret1 = new EnemyTurretRaider(1010, 1020);
		EnemyTurretRaider turret2 = new EnemyTurretRaider(1400, 1080);
		EnemyTurretRaider turret3 = new EnemyTurretRaider(1900, 1030);
		EnemyTurretRaider turret4 = new EnemyTurretRaider(1960, 1980);
		EnemyTurretRaider turret5 = new EnemyTurretRaider(1550, 1850);
		EnemyTurretRaider turret6 = new EnemyTurretRaider(1200, 1700);
		turrets.add(turret0);
		turrets.add(turret1);
		turrets.add(turret2);
		turrets.add(turret3);
		turrets.add(turret4);
		turrets.add(turret5);
		turrets.add(turret6);
		turret0.register();
		turret1.register();
		turret2.register();
		turret3.register();
		turret4.register();
		turret5.register();
		turret6.register();

		core = new EnemyBasecoreRaider(this);
		core.setPosition(1430, 1520);
		core.register();

	}

	@Override
	public void update() {

		for (SpawnScheduler s : spawner) {
			s.update();
		}

	}

	public ArrayList<EnemyTurretRaider> getTurrets() {
		return turrets;
	}

}
