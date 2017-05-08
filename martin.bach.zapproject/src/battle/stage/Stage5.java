package battle.stage;

import battle.CombatObject;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.AiProtocol;
import battle.ai.DestinationReachedCall;
import battle.ai.DieCall;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.enemy.Enemy;
import battle.stage._5.FriendTransporter;
import corecase.MainZap;
import gui.screens.end.EndScreen;
import ingameobjects.InteractiveObject;
import lib.ScheduledList;

public class Stage5 extends Stage {

	private static final int LVL = 5;
	private static final String NAME = "Expensive Goods";
	private static final String DESCRIPTION = "guard the transporter";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.MEDIUM;

	private static final int MAX_AMOUNT_AMMO_PACK_LARGE = 5;
	private static final int MAX_AMOUNT_AMMO_PACK_SMALL = 14;
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(2000);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(5000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(5000);

	private static final int TIMEING_WAVE_ONE = MainZap.inTicks(6000);
	private static final int TIMEING_WAVE_TWO = MainZap.inTicks(20000);
	private static final int TIMEING_WAVE_THREE = MainZap.inTicks(40000);

	private static final int AMOUNT_RAIDER_FIRSTWAVE = 4;
	private static final int AMOUNT_RAIDER_BASIC_SECONDWAVE = 5;
	private static final int AMOUNT_RAIDER_HEAVY_SECONDWAVE = 1;
	private static final int AMOUNT_RAIDER_BASIC_THIRDWAVE = 8;
	private static final int AMOUNT_RAIDER_HEAVY_THIRDWAVE = 3;

	private SpawnScheduler[] spawner;
	private ScheduledList<CombatObject> raiders = new ScheduledList<CombatObject>();
	private FriendTransporter transporter;
	private int time = 0;

	public Stage5() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 2850, 300);
		spawner = new SpawnScheduler[] { new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_LARGE, 0, PackType.AMMO_LARGE),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_SMALL, 0, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED) };

		transporter = new FriendTransporter(2800, 200, 200, 2800);
		transporter.register();
		transporter.getAiProtocol().addCall(AiProtocol.KEY_CALL_REACHED_DESTINATION, new DestinationReachedCall() {
			@Override
			public void desReached(int x, int y) {
				if (MainZap.getPlayer().isAlive())
					pass(); // Der Transporter hat das Ziel erreicht
				transporter.getAiProtocol().moveTo(-1000, 1000);
			}
		});
		transporter.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {
			@Override
			public void die() { // Verkackt
				if (!MainZap.getPlayer().isAlive())
					return;
				MainZap.getPlayer().setBlocked(true);
				EndScreen.popUp("The transporter exploded");
			}
		});

	}

	@Override
	public void update() {

		time++;

		if (time == TIMEING_WAVE_ONE) {
			startWaveOne();
		} else if (time == TIMEING_WAVE_TWO) {
			startWaveTwo();
		} else if (time == TIMEING_WAVE_THREE) {
			startWaveThree();
		}

		raiders.update();

		for (SpawnScheduler s : spawner) {
			s.update();
		}

	}

	private void startWaveOne() {

		for (int i = 0; i != AMOUNT_RAIDER_FIRSTWAVE; i++)
			raiders.add(new EnemyBasicRaider());

		int spawnRadius = 200;

		int warpAimX = transporter.getLocX() - 150 + rand(300);
		int warpAimY = transporter.getLocY() - 150 + rand(300);

		for (final CombatObject raider : raiders) {
			raider.setPosition(transporter.getLocX() - spawnRadius + rand(2 * spawnRadius),
					transporter.getLocY() - spawnRadius + rand(2 * spawnRadius));
			((AdvancedSingleProtocol) ((EnemyBasicRaider) raider).getAiProtocol()).setLinkedAllies(raiders);
			((EnemyBasicRaider) raider).getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {
				@Override
				public void die() { // Rausnehmen
					raiders.schedRemove(raider);
				}
			});
			((EnemyBasicRaider) raider).warpIn(warpAimX - raider.getLocX(), warpAimY - raider.getLocY());
			if (getRandom().nextBoolean())
				((EnemyBasicRaider) raider).getAiProtocol().setLockOn(transporter);
			else
				((EnemyBasicRaider) raider).getAiProtocol().setLockOn(MainZap.getPlayer());
		}

		for (InteractiveObject raider : raiders)
			raider.register();
	}

	private void startWaveTwo() {

		for (int i = 0; i != AMOUNT_RAIDER_BASIC_SECONDWAVE; i++)
			raiders.add(new EnemyBasicRaider());

		for (int i = 0; i != AMOUNT_RAIDER_HEAVY_SECONDWAVE; i++)
			raiders.add(new EnemyHeavyRaider());

		int spawnRadius = 300;
		
		int warpAimX = transporter.getLocX() - 150 + rand(300);
		int warpAimY = transporter.getLocY() - 150 + rand(300);

		for (final CombatObject raider : raiders) {
			raider.setPosition(transporter.getLocX() - spawnRadius + rand(2 * spawnRadius),
					transporter.getLocY() - spawnRadius + rand(2 * spawnRadius));
			((AdvancedSingleProtocol) ((Enemy) raider).getAiProtocol()).setLinkedAllies(raiders);
			((Enemy) raider).getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {
				@Override
				public void die() { // Rausnehmen
					raiders.schedRemove(raider);
				}
			});
			((Enemy) raider).warpIn(warpAimX - raider.getLocX(), warpAimY - raider.getLocY());
			if (getRandom().nextBoolean())
				((Enemy) raider).getAiProtocol().setLockOn(transporter);
			else
				((Enemy) raider).getAiProtocol().setLockOn(MainZap.getPlayer());
		}

		for (InteractiveObject raider : raiders)
			raider.register();
	}

	private void startWaveThree() {

		for (int i = 0; i != AMOUNT_RAIDER_BASIC_THIRDWAVE; i++)
			raiders.add(new EnemyBasicRaider());

		for (int i = 0; i != AMOUNT_RAIDER_HEAVY_THIRDWAVE; i++)
			raiders.add(new EnemyHeavyRaider());

		int spawnRadius = 300;
		
		int warpAimX = transporter.getLocX() - 150 + rand(300);
		int warpAimY = transporter.getLocY() - 150 + rand(300);

		for (final CombatObject raider : raiders) {
			raider.setPosition(transporter.getLocX() - spawnRadius + rand(2 * spawnRadius),
					transporter.getLocY() - spawnRadius + rand(2 * spawnRadius));
			((AdvancedSingleProtocol) ((Enemy) raider).getAiProtocol()).setLinkedAllies(raiders);
			((Enemy) raider).getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {
				@Override
				public void die() { // Rausnehmen
					raiders.schedRemove(raider);
				}
			});
			((Enemy) raider).warpIn(warpAimX - raider.getLocX(), warpAimY - raider.getLocY());
			if (getRandom().nextBoolean())
				((Enemy) raider).getAiProtocol().setLockOn(transporter);
			else
				((Enemy) raider).getAiProtocol().setLockOn(MainZap.getPlayer());
		}

		for (InteractiveObject raider : raiders)
			raider.register();
	}
}
