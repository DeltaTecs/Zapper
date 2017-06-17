package battle.stage;

import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.stage._9.FriendBaseAlpha;
import battle.stage._9.FriendBasecoreAlpha;
import corecase.MainZap;

public class Stage9 extends Stage {

	private static final int LVL = 9;
	private static final String NAME = "Unknown Enemy";
	private static final String DESCRIPTION = "defend the base";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.MEDIUM;

	private static final int SPAWN_RATE_AMMO_SMALL = MainZap.getMainLoop().inTicks(4000);
	private static final int SPAWN_RATE_AMMO_LARGE = MainZap.getMainLoop().inTicks(9000);
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(8000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(9000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(6000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(10000);

	private SpawnScheduler[] spawner;
	private FriendBaseAlpha baseBG = new FriendBaseAlpha();
	private FriendBasecoreAlpha baseCore = new FriendBasecoreAlpha(this);

	public Stage9() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1500, 2400);
		spawner = new SpawnScheduler[] { new SpawnScheduler(50, SPAWN_RATE_AMMO_SMALL, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_AMMO_LARGE, PackType.AMMO_LARGE),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		
		// Base-Hintergrund init und registrierung
		baseBG.setPosition(1500, 2777);
		MainZap.getMap().addPaintElement(baseBG, true);
		getPaintingTasks().add(baseBG);
		
		// Base-Core init und registrierung
		baseCore.setPosition(1500, 2999);
		baseCore.register();
		
	}

	@Override
	public void update() {

		for (SpawnScheduler s : spawner) {
			s.update();
		}

	}

}
