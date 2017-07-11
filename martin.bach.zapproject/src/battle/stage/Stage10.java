package battle.stage;

import java.awt.Rectangle;
import java.util.ArrayList;

import battle.CombatObject;
import battle.ai.AdvancedSingleProtocol;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.stage._10.EnemyGamma2;
import battle.stage._10.FriendGamma0;
import battle.stage._10.FriendGamma1;
import corecase.MainZap;

public class Stage10 extends Stage {

	private static final int LVL = 10;
	private static final String NAME = "Battle field";
	private static final String DESCRIPTION = "lead your armee to victory";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.MEDIUM;

	private static final int SPAWN_RATE_AMMO_SMALL = MainZap.getMainLoop().inTicks(1500);
	private static final int SPAWN_RATE_AMMO_LARGE = MainZap.getMainLoop().inTicks(4000);
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(4500);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(4000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(4000);

	private SpawnScheduler[] spawner;

	public Stage10() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1200, 1800);
		spawner = new SpawnScheduler[] { new SpawnScheduler(50, SPAWN_RATE_AMMO_SMALL, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_AMMO_LARGE, PackType.AMMO_LARGE),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		ArrayList<CombatObject> enemys = new ArrayList<CombatObject>();

		FriendGamma0 gamma0Test = new FriendGamma0();
		gamma0Test.setPosition(1600, 1600);
		((AdvancedSingleProtocol) gamma0Test.getAiProtocol()).setLinkedEnemys(enemys);
//		gamma0Test.register();

		FriendGamma1 gamma1Test = new FriendGamma1();
		gamma1Test.setPosition(1600, 1800);
		((AdvancedSingleProtocol) gamma1Test.getAiProtocol()).setLinkedEnemys(enemys);
		gamma1Test.register();

		EnemyGamma2 enemy0 = new EnemyGamma2(1400, 1600, new Rectangle(0, 0, 3000, 3000));
		enemy0.register();
		enemys.add(enemy0);
	}

	@Override
	public void update() {

		for (SpawnScheduler s : spawner)
			s.update();

	}

}
