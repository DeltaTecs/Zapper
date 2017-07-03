package battle.stage;

import java.awt.Rectangle;

import battle.ai.AdvancedSingleProtocol;
import battle.ai.FindLockAction;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.stage._10.EnemyGamma2;
import battle.stage._9.EnemyGamma0;
import battle.stage._9.EnemyGamma1;
import corecase.MainZap;
import gui.shop.ShopLocation;

public class Stage0 extends Stage {

	private static final int LVL = 0;
	private static final String NAME = "Debug - Stage";
	private static final String DESCRIPTION = "Its only a Test. Y u have to be mad?";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.BEGINNER;

	private static final int MAX_AMOUNT_AMMO_PACK_LARGE = 5;
	private static final int MAX_AMOUNT_AMMO_PACK_SMALL = 14;
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(600);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(600);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(600);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(600);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(600);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(600);

	private SpawnScheduler[] spawner;

	public Stage0() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION);
		spawner = new SpawnScheduler[] { new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_LARGE, 0, PackType.AMMO_LARGE),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_SMALL, 0, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		ShopLocation shop = new ShopLocation(1500, 1500, 3000, 0);
		MainZap.getMap().addUpdateElement(shop);

		EnemyGamma0 testEnemy0 = new EnemyGamma0(1600, 1500, new Rectangle(50, 50, 2900, 2900));
		((AdvancedSingleProtocol) testEnemy0.getAiProtocol()).setLockAction(FindLockAction.NO_AUTO_LOCK);
		// testEnemy0.register();
		EnemyGamma1 testEnemy1 = new EnemyGamma1(1600, 1400, new Rectangle(50, 50, 2900, 2900));
		((AdvancedSingleProtocol) testEnemy1.getAiProtocol()).setLockAction(FindLockAction.NO_AUTO_LOCK);
		// testEnemy1.register();
		EnemyGamma2 testEnemy2 = new EnemyGamma2(1600, 1600, new Rectangle(1000, 1000, 1000, 1000));
		testEnemy2.register();

		pass();
	}

	@Override
	public void update() {

		for (SpawnScheduler s : spawner) {
			s.update();
		}

	}

}
