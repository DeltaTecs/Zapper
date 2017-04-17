package battle.stage;

import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.enemy.EnemyBeta0;
import battle.enemy.EnemyBeta2;
import battle.stage._3.EnemyTurretAlpha0;
import corecase.MainZap;
import gui.Map;

public class Stage0 extends Stage {

	private static final int LVL = 0;
	private static final String NAME = "Debug - Stage";
	private static final String DESCRIPTION = "Its only a Test. Y u have to be mad?";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.BEGINNER;

	private static final int MAX_AMOUNT_BETA0 = 30;
	private static final int MAX_AMOUNT_BETA2 = 30;
	private static final int MAX_AMOUNT_AMMO_PACK_LARGE = 5;
	private static final int MAX_AMOUNT_AMMO_PACK_SMALL = 14;
	private static final int SPAWN_RATE_BETA0 = MainZap.getMainLoop().inTicks(50);
	private static final int SPAWN_RATE_BETA2 = MainZap.getMainLoop().inTicks(50);
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(600);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(600);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(600);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(600);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(600);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(600);

	private SpawnScheduler[] spawner;

	public Stage0() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION);
		spawner = new SpawnScheduler[] { new SpawnScheduler(MAX_AMOUNT_BETA0, SPAWN_RATE_BETA0, new EnemyBeta0(0, 0)),
				new SpawnScheduler(MAX_AMOUNT_BETA2, SPAWN_RATE_BETA2, new EnemyBeta2(0, 0)),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_LARGE, 0, PackType.AMMO_LARGE),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_SMALL, 0, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		for (int i = 0; i != 26; i++) {
			EnemyTurretAlpha0 turret = new EnemyTurretAlpha0(rand(Map.SIZE), rand(Map.SIZE));
			turret.register();
		}

		pass();
	}

	@Override
	public void update() {

		for (SpawnScheduler s : spawner) {
			s.update();
		}

	}

}
