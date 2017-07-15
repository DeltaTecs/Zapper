package battle.stage;

import java.util.ArrayList;

import battle.CombatObject;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import corecase.MainZap;

public class Stage11 extends Stage {

	private static final int LVL = 10;
	private static final String NAME = "The Source";
	private static final String DESCRIPTION = "destroy the star gate";
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
	private ArrayList<CombatObject> enemys = new ArrayList<CombatObject>();
	private ArrayList<CombatObject> friendsAndPlayer = new ArrayList<CombatObject>();

	public Stage11() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1500, 2400);
		spawner = new SpawnScheduler[] { new SpawnScheduler(50, SPAWN_RATE_AMMO_SMALL, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_AMMO_LARGE, PackType.AMMO_LARGE),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		friendsAndPlayer.add(MainZap.getPlayer());
		applyRemoveTask(friendsAndPlayer, MainZap.getPlayer());

	}

	@Override
	public void update() {

		for (SpawnScheduler s : spawner)
			s.update();

	}

}
