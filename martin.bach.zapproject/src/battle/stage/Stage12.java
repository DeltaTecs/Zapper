package battle.stage;

import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.stage._12.DeltaEnemy;
import corecase.MainZap;

public class Stage12 extends Stage {

	private static final int LVL = 12;
	private static final String NAME = "Far, Far Away";
	private static final String DESCRIPTION = "dominate";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.PRO;

	private static final int SPAWN_RATE_AMMO_SMALL = MainZap.getMainLoop().inTicks(1500);
	private static final int SPAWN_RATE_AMMO_LARGE = MainZap.getMainLoop().inTicks(4000);
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(4500);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(4000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(4000);

	private SpawnScheduler[] spawner;
	private int timeTillSplit = MainZap.inTicks(1000);
	DeltaEnemy deltatest;

	public Stage12() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1500, 1500);
		spawner = new SpawnScheduler[] { new SpawnScheduler(50, SPAWN_RATE_AMMO_SMALL, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_AMMO_LARGE, PackType.AMMO_LARGE),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		deltatest = new DeltaEnemy(880, true);
		deltatest.register();
		deltatest.setPosition(1500, 1500);

	}

	@Override
	public void update() {

		if (timeTillSplit == 0) {
			// deltatest.breakAt((byte) 2);
			timeTillSplit = -1;
		} else if (timeTillSplit > 0)
			timeTillSplit--;

		for (SpawnScheduler s : spawner)
			s.update();

	}

}
