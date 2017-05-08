package battle.stage;

import battle.CombatObject;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.AiProtocol;
import battle.ai.DieCall;
import battle.ai.FindLockAction;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.stage._6.EnemyRaiderDeltaVII;
import battle.stage._6.FriendBeta;
import corecase.MainZap;
import lib.ScheduledList;

public class Stage6 extends Stage {

	private static final int LVL = 6;
	private static final String NAME = "Raiders Loot";
	private static final String DESCRIPTION = "eliminate the stolen DeltaVII (survive)";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.MEDIUM;

	private static final int SPAWN_RATE_AMMO_PACK_SMALL = MainZap.getMainLoop().inTicks(3000);
	private static final int SPAWN_RATE_AMMO_PACK_LARGE = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(600);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(2000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(2000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(2000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(2000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(2000);

	private static final int AMOUNT_FRIENDS = 8;
	private static final int AMOUNT_RAIDERS = 2;
	private static final int RAND_SPAWN_RAIDER_BASIC = 1200;
	private static final int RAND_SPAWN_RAIDER_HEAVY = 5000;
	private static final int RAND_SPAWN_FRIEND = 20;
	private static final int RAIDER_SPAWN_RANGE = 200;
	private static final int FRIEND_SPAWN_RANGE = 100;

	private SpawnScheduler[] spawner;
	private ScheduledList<CombatObject> raiders = new ScheduledList<CombatObject>();
	private ScheduledList<CombatObject> friends = new ScheduledList<CombatObject>();
	private EnemyRaiderDeltaVII deltaVII;

	public Stage6() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1500, 1800);
		spawner = new SpawnScheduler[] { new SpawnScheduler(50, SPAWN_RATE_AMMO_PACK_SMALL, PackType.AMMO_LARGE),
				new SpawnScheduler(50, SPAWN_RATE_AMMO_PACK_LARGE, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		deltaVII = new EnemyRaiderDeltaVII(1500, 1500);
		deltaVII.register();
		raiders.add(deltaVII);

		for (int i = 0; i != AMOUNT_FRIENDS; i++)
			friends.add(new FriendBeta());

		for (int i = 0; i != AMOUNT_RAIDERS; i++)
			raiders.add(new EnemyBasicRaider());

		for (CombatObject c : friends) {

			final FriendBeta friend = (FriendBeta) c;
			friend.link(friends);
			friend.register();
			friend.setPosition(deltaVII.getLocX() - FRIEND_SPAWN_RANGE + rand(2 * FRIEND_SPAWN_RANGE),
					deltaVII.getLocY() - FRIEND_SPAWN_RANGE + rand(2 * FRIEND_SPAWN_RANGE));
			friend.getAiProtocol().setLockOn(deltaVII);
			friend.setNoWaitAfterWarp(true);
			friend.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {
				@Override
				public void die() {
					friends.schedRemove(friend);
				}
			});
			friend.register();
		}

		for (CombatObject c : raiders) {

			if (c == deltaVII)
				continue;

			final EnemyBasicRaider raider = (EnemyBasicRaider) c;
			((AdvancedSingleProtocol) raider.getAiProtocol()).setLinkedAllies(raiders);
			((AdvancedSingleProtocol) raider.getAiProtocol()).setLockAction(FindLockAction.LOCK_LOCK_OF_LINKED_FRIENDS);
			raider.register();
			raider.setPosition(deltaVII.getLocX() - RAIDER_SPAWN_RANGE + rand(2 * RAIDER_SPAWN_RANGE),
					deltaVII.getLocY() - RAIDER_SPAWN_RANGE + rand(2 * RAIDER_SPAWN_RANGE));
			if ((rand(2) == 0 || rand(2) == 0) && friends.size() > 0)
				raider.getAiProtocol().setLockOn(friends.get(rand(friends.size())));
			else
				raider.getAiProtocol().setLockOn(MainZap.getPlayer());
			raider.setNoWaitAfterWarp(true);
			raider.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {
				@Override
				public void die() {
					raiders.schedRemove(raider);
				}
			});
			raider.register();
		}

		deltaVII.getAiProtocol().setLockOn(friends.get(rand(friends.size())));

	}

	@Override
	public void update() {

		if (!deltaVII.isAlive())
			pass();

		if (rand(RAND_SPAWN_FRIEND) == 0 && deltaVII.isAlive()) {
			final FriendBeta friend = new FriendBeta();
			friend.link(friends);
			friend.warpIn();
			friend.register();
			friend.setPosition(deltaVII.getLocX() - FRIEND_SPAWN_RANGE + rand(2 * FRIEND_SPAWN_RANGE),
					deltaVII.getLocY() - FRIEND_SPAWN_RANGE + rand(2 * FRIEND_SPAWN_RANGE));
			friend.getAiProtocol().setLockOn(raiders.get(rand(raiders.size())));

			friend.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {
				@Override
				public void die() {
					friends.schedRemove(friend);
				}
			});
			friend.register();
			friends.schedAdd(friend);
		}

		if (rand(RAND_SPAWN_RAIDER_BASIC) == 0 && deltaVII.isAlive()) {
			final EnemyBasicRaider raider = new EnemyBasicRaider();
			((AdvancedSingleProtocol) raider.getAiProtocol()).setLinkedAllies(raiders);
			raider.warpIn();
			raider.register();
			raider.setPosition(deltaVII.getLocX() - RAIDER_SPAWN_RANGE + rand(2 * RAIDER_SPAWN_RANGE),
					deltaVII.getLocY() - RAIDER_SPAWN_RANGE + rand(2 * RAIDER_SPAWN_RANGE));

			if ((rand(2) == 0 || rand(2) == 0) && friends.size() > 0)
				raider.getAiProtocol().setLockOn(friends.get(rand(friends.size())));
			else
				raider.getAiProtocol().setLockOn(MainZap.getPlayer());

			raider.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {
				@Override
				public void die() {
					raiders.schedRemove(raider);
				}
			});
			raider.register();
			raiders.schedAdd(raider);
		}

		if (rand(RAND_SPAWN_RAIDER_HEAVY) == 0 && deltaVII.isAlive()) {
			final EnemyHeavyRaider raider = new EnemyHeavyRaider();
			((AdvancedSingleProtocol) raider.getAiProtocol()).setLinkedAllies(raiders);
			raider.warpIn();
			raider.register();
			raider.setPosition(deltaVII.getLocX() - RAIDER_SPAWN_RANGE + rand(2 * RAIDER_SPAWN_RANGE),
					deltaVII.getLocY() - RAIDER_SPAWN_RANGE + rand(2 * RAIDER_SPAWN_RANGE));

			if ((rand(2) == 0 || rand(2) == 0) && friends.size() > 0)
				raider.getAiProtocol().setLockOn(friends.get(rand(friends.size())));
			else
				raider.getAiProtocol().setLockOn(MainZap.getPlayer());

			raider.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {
				@Override
				public void die() {
					raiders.schedRemove(raider);
				}
			});
			raider.register();
			raiders.schedAdd(raider);
		}

		raiders.update();
		friends.update();

		for (SpawnScheduler s : spawner) {
			s.update();
		}

	}

}
