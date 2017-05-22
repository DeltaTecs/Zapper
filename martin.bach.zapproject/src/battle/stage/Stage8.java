package battle.stage;

import java.awt.Rectangle;

import battle.GuardianTurret;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.AiProtocol;
import battle.ai.DieCall;
import battle.ai.FindLockAction;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.enemy.Enemy;
import battle.enemy.EnemyBasicRaider;
import battle.enemy.EnemyBeta0;
import battle.enemy.EnemyBeta1;
import battle.enemy.EnemyBeta2;
import battle.enemy.EnemyHeavyRaider;
import battle.stage._8.Shop1;
import corecase.MainZap;
import gui.Map;
import gui.shop.ShopLocation;

public class Stage8 extends Stage {

	private static final int LVL = 8;
	private static final String NAME = "Lobby - Stage";
	private static final String DESCRIPTION = "chillin' n' shoppin'";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.EASY;

	private static final int MAX_AMOUNT_AMMO_PACK_LARGE = 2;
	private static final int MAX_AMOUNT_AMMO_PACK_SMALL = 7;
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final Rectangle BOUNDS_SHOPAREA = new Rectangle(1000, 1000, 1000, 1000);

	private static final int RAND_SPAWN_RAIDER_BASIC = MainZap.inTicks(8000);
	private static final int RAND_SPAWN_RAIDER_HEAVY = MainZap.inTicks(20000);
	private static final int MAX_RAIDER_AMOUNT = 4;

	private SpawnScheduler[] spawner;
	private GuardianTurret guardianTurret;
	private int raiders = 0;

	public Stage8() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1200, 2000);
		spawner = new SpawnScheduler[] { new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_LARGE, 0, PackType.AMMO_LARGE),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_SMALL, 0, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		pass();

		Shop1 shop = new Shop1();
		super.getPaintingTasks().add(shop);
		MainZap.getMap().addPaintElement(shop, true); // Manuell registieren
		shop.setPosition(1500, 1500);

		guardianTurret = new GuardianTurret(1720, 1240, 20);
		super.getInteractiveObjects().add(guardianTurret);
		guardianTurret.register();

		ShopLocation shoploc = new ShopLocation(1400, 1600, 600, LVL);
		MainZap.getMap().addUpdateElement(shoploc);

		// Hie und da n paar Gegener...
		for (int i = 0; i != 7; i++) {

			int lx = rand(Map.SIZE);
			int ly = rand(Map.SIZE);

			if (BOUNDS_SHOPAREA.contains(lx, ly)) {
				i--;
				continue;
			}

			int r = rand(3);
			switch (r) {
			case 0:
				Enemy e0 = new EnemyBeta0(lx, ly);
				e0.register();
				break;
			case 1:
				Enemy e1 = new EnemyBeta1(lx, ly);
				e1.register();
				break;
			case 2:
				Enemy e2 = new EnemyBeta2(lx, ly);
				e2.register();
				break;
			}

		}

	}

	@Override
	public void update() {

		for (SpawnScheduler s : spawner) {
			s.update();
		}

		if (raiders >= MAX_RAIDER_AMOUNT)
			return; // Voll

		if (rand(RAND_SPAWN_RAIDER_BASIC) == 0) {

			EnemyBasicRaider raider = new EnemyBasicRaider();
			((AdvancedSingleProtocol) raider.getAiProtocol()).setLockAction(FindLockAction.NO_AUTO_LOCK);
			raider.setPosition(rand(Map.SIZE), rand(Map.SIZE));
			raider.warpIn();
			if (getRandom().nextBoolean())
				raider.getAiProtocol().setLockOn(MainZap.getPlayer());
			else
				raider.getAiProtocol().setLockOn(guardianTurret);
			raider.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {

				@Override
				public void die() {
					raiders--;
				}
			});
			raider.register();
		}

		if (rand(RAND_SPAWN_RAIDER_HEAVY) == 0) {

			EnemyHeavyRaider raider = new EnemyHeavyRaider();
			((AdvancedSingleProtocol) raider.getAiProtocol()).setLockAction(FindLockAction.NO_AUTO_LOCK);
			raider.setPosition(rand(Map.SIZE), rand(Map.SIZE));
			raider.warpIn();
			if (getRandom().nextBoolean())
				raider.getAiProtocol().setLockOn(MainZap.getPlayer());
			else
				raider.getAiProtocol().setLockOn(guardianTurret);
			raider.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {

				@Override
				public void die() {
					raiders--;
				}
			});
			raider.register();
		}

	}

}
