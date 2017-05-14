package battle.stage;

import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.stage._7.EnemyBaseRaider;
import battle.stage._7.EnemyBasecoreRaider;
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

	public Stage7() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1500, 2400);

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

}
