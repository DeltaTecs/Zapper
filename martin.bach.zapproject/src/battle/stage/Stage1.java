package battle.stage;

import java.awt.Rectangle;

import battle.collect.PackType;
import battle.collect.SpawnEvent;
import battle.collect.SpawnScheduler;
import battle.enemy.Enemy;
import battle.enemy.EnemyBeta0;
import battle.enemy.EnemyBeta1;
import corecase.MainZap;
import gui.Map;
import ingameobjects.InteractiveObject;
import io.SettingsInitReader;

public class Stage1 extends Stage {

	private static final int PASSSCORE = 20;
	private static final int LVL = 1;
	private static final String NAME = "Welcome - Stage";
	private static final String DESCRIPTION = "Obj: Reach " + PASSSCORE + " Score";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.BEGINNER;

	private static final Rectangle BOUND_AREA_BETA1 = new Rectangle(0, 0, Map.SIZE, 2000);
	private static final Rectangle SPAWN_AREA_BETA1 = BOUND_AREA_BETA1;

	private static final int MAX_AMOUNT_BETA0 = 20;
	private static final int MAX_AMOUNT_BETA1 = 4;
	private static final int MAX_AMOUNT_AMMO_PACK_LARGE = 2;
	private static final int MAX_AMOUNT_AMMO_PACK_SMALL = 7;
	private static final int SPAWN_RATE_BETA0 = MainZap.getMainLoop().inTicks(200);
	private static final int SPAWN_RATE_BETA1 = MainZap.getMainLoop().inTicks(1000);
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(8000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(9000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(10000);

	private SpawnScheduler[] spawner;

	public Stage1() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 2400, 2400);
		spawner = new SpawnScheduler[] { new SpawnScheduler(MAX_AMOUNT_BETA0, SPAWN_RATE_BETA0, new EnemyBeta0(0, 0)),
				new SpawnScheduler(MAX_AMOUNT_BETA1, SPAWN_RATE_BETA1, SPAWN_AREA_BETA1, new EnemyBeta1(0, 0)),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_LARGE, 0, PackType.AMMO_LARGE),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_SMALL, 0, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		// VVV Beta1 Spawner
		// --> Beta1s sollen sich nicht in den unteren Bereich gehen
		spawner[1].addSpawnEvent(new SpawnEvent() {

			@Override
			public void spawn(int x, int y, Enemy e) {
				e.getAiProtocol().setAreaBound(true);
				e.getAiProtocol().setBoundArea(BOUND_AREA_BETA1);
			}

			@Override
			public void spawn(int x, int y, InteractiveObject o) {
			}

		});
	}

	@Override
	public void pass() {
		super.pass();
		if (MainZap.firstRun) {
			MainZap.firstRun = false;
			SettingsInitReader.save();
		}
	}

	@Override
	public void update() {

		if (!isPassed() && MainZap.getScore() >= PASSSCORE)
			pass();

		for (SpawnScheduler s : spawner) {
			s.update();
		}

	}

}
