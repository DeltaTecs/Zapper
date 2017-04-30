package battle.stage;

import java.awt.Rectangle;

import battle.ai.AiProtocol;
import battle.ai.DieCall;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.enemy.EnemyBeta0;
import battle.enemy.EnemyBeta1;
import battle.enemy.EnemyBeta2;
import corecase.MainZap;

public class Stage2 extends Stage {

	private static final int LVL = 2;
	private static final String NAME = "District 4";
	private static final String DESCRIPTION = "Obj: kill 75 % of all enemys";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.HARD;

	private static final int AMOUNT_BETA0 = 4;
	private static final int AMOUNT_BETA1 = 10;
	private static final int AMOUNT_BETA2 = 17;
	private static final int TOTAL_AMOUNT = AMOUNT_BETA0 + AMOUNT_BETA1 + AMOUNT_BETA2;
//	private static final Rectangle SPAWN_AREA = new Rectangle(900, 900, 1200, 1200);
	private static final Rectangle SPAWN_AREA = new Rectangle(1000, 1000, 1000, 1000);

	private int enemysRemaining = TOTAL_AMOUNT;
	
	private static final int MAX_AMOUNT_AMMO_PACK_LARGE = 1;
	private static final int MAX_AMOUNT_AMMO_PACK_SMALL = 7;
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(12000);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(13000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(17000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(11000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(11000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(19000);

	private SpawnScheduler[] spawner;
	public Stage2() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 2500, 2500);
		spawner = new SpawnScheduler[] {
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_LARGE, 0, SPAWN_AREA,  PackType.AMMO_LARGE),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_SMALL, 0, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, SPAWN_AREA, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, SPAWN_AREA, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, SPAWN_AREA, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, SPAWN_AREA, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, SPAWN_AREA, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, SPAWN_AREA, PackType.SPEED) };

		// Beta0s
		for (int i = 0; i != AMOUNT_BETA0; i++) {

			int x = rand(SPAWN_AREA.width) + SPAWN_AREA.x;
			int y = rand(SPAWN_AREA.height) + SPAWN_AREA.y;
			

			EnemyBeta0 e = new EnemyBeta0(x, y);
			e.setPosition(x, y);
			e.register();
			e.getAiProtocol().setAreaBound(true);
			e.getAiProtocol().setBoundArea(SPAWN_AREA);
			e.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, lowerCounter);
		}

		// Beta1s
		for (int i = 0; i != AMOUNT_BETA1; i++) {

			int x = rand(SPAWN_AREA.width) + SPAWN_AREA.x;
			int y = rand(SPAWN_AREA.height) + SPAWN_AREA.y;

			EnemyBeta1 e = new EnemyBeta1(x, y);
			e.setPosition(x, y);
			e.register();
			e.getAiProtocol().setAreaBound(true);
			e.getAiProtocol().setBoundArea(SPAWN_AREA);
			e.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, lowerCounter);
		}

		// Beta2s
		for (int i = 0; i != AMOUNT_BETA2; i++) {

			int x = rand(SPAWN_AREA.width) + SPAWN_AREA.x;
			int y = rand(SPAWN_AREA.height) + SPAWN_AREA.y;

			EnemyBeta2 e = new EnemyBeta2(x, y);
			e.setPosition(x, y);
			e.register();
			e.getAiProtocol().setAreaBound(true);
			e.getAiProtocol().setBoundArea(SPAWN_AREA);
			e.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, lowerCounter);
		}

	}

	@Override
	public void update() {

		if (!isPassed() && enemysRemaining <= TOTAL_AMOUNT * 0.25f)
			pass();
		
		for (SpawnScheduler s : spawner) {
			s.update();
		}

	}

	private DieCall lowerCounter = new DieCall() {

		@Override
		public void die() {
			enemysRemaining--;
		}
	};

}
