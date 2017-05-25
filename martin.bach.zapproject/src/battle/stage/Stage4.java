package battle.stage;

import java.awt.Rectangle;

import battle.CombatObject;
import battle.GuardianTurret;
import battle.ai.AiProtocol;
import battle.ai.ArmyCombatProtocol;
import battle.ai.DamageCall;
import battle.collect.PackType;
import battle.collect.SpawnScheduler;
import battle.enemy.Enemy;
import battle.enemy.EnemyBeta0;
import battle.enemy.EnemyBeta1;
import battle.enemy.EnemyBeta2;
import battle.projectile.Projectile;
import battle.stage._3.FriendAlpha1;
import battle.stage._4.Rock0;
import battle.stage._4.Shop0;
import corecase.MainZap;
import gui.Map;
import gui.shop.ShopLocation;

public class Stage4 extends Stage {

	private static final int LVL = 4;
	private static final String NAME = "Lobby - Stage";
	private static final String DESCRIPTION = "chillin' n' shoppin'";
	private static final StageDifficulty DIFFICULTY = StageDifficulty.EASY;

	private static final int MAX_AMOUNT_BETA0 = 3;
	private static final int MAX_AMOUNT_BETA2 = 1;
	private static final int MAX_AMOUNT_BETA1 = 2;
	private static final int MAX_AMOUNT_AMMO_PACK_LARGE = 2;
	private static final int MAX_AMOUNT_AMMO_PACK_SMALL = 7;
	private static final int SPAWN_RATE_BETA0 = MainZap.getMainLoop().inTicks(50);
	private static final int SPAWN_RATE_BETA2 = MainZap.getMainLoop().inTicks(50);
	private static final int SPAWN_RATE_HP_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final int SPAWN_RATE_SPEED_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final int SPAWN_RATE_DMG_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final int SPAWN_RATE_BL_SPEED_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final int SPAWN_RATE_RANGE_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final int SPAWN_RATE_RELOAD_PACK = MainZap.getMainLoop().inTicks(10000);
	private static final Rectangle BOUNDS_SHOPAREA = new Rectangle(1000, 1000, 1000, 1000);

	private SpawnScheduler[] spawner;

	public Stage4() {
		super(LVL, NAME, DIFFICULTY, DESCRIPTION, 1200, 2000);
		spawner = new SpawnScheduler[] { new SpawnScheduler(MAX_AMOUNT_BETA0, SPAWN_RATE_BETA0, new EnemyBeta0(0, 0)),
				new SpawnScheduler(MAX_AMOUNT_BETA2, SPAWN_RATE_BETA2, new EnemyBeta2(0, 0)),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_LARGE, 0, PackType.AMMO_LARGE),
				new SpawnScheduler(MAX_AMOUNT_AMMO_PACK_SMALL, 0, PackType.AMMO_SMALL),
				new SpawnScheduler(50, SPAWN_RATE_HP_PACK, PackType.HEALTH),
				new SpawnScheduler(50, SPAWN_RATE_BL_SPEED_PACK, PackType.BULLET_SPEED),
				new SpawnScheduler(50, SPAWN_RATE_DMG_PACK, PackType.BULLET_DMG),
				new SpawnScheduler(50, SPAWN_RATE_RANGE_PACK, PackType.BULLET_RANGE),
				new SpawnScheduler(50, SPAWN_RATE_RELOAD_PACK, PackType.RELOAD),
				new SpawnScheduler(50, SPAWN_RATE_SPEED_PACK, PackType.SPEED),
				new SpawnScheduler(MAX_AMOUNT_BETA1, 0, new EnemyBeta1(0, 0)) };

		pass();

		Shop0 shop = new Shop0();
		super.getPaintingTasks().add(shop);
		MainZap.getMap().addPaintElement(shop, true);
		shop.setPosition(1500, 1500);

		Rock0 rock = new Rock0();
		super.getPaintingTasks().add(rock);
		MainZap.getMap().addPaintElement(rock, true);
		rock.setPosition(1780, 1300);

		GuardianTurret turret = new GuardianTurret(1780, 1300, 20, 0);
		super.getInteractiveObjects().add(turret);
		turret.register();

		ShopLocation shoploc = new ShopLocation(1500, 1500, 500, LVL);
		MainZap.getMap().addUpdateElement(shoploc);

		double rot0 = Math.PI / 4.0;
		double rot1 = Math.PI + Math.PI / 4.0;
		int distance = 45;
		int x = 1410;
		int y = 1650;

		final FriendAlpha1 friend0 = new FriendAlpha1(x, y, null);
		friend0.getAiProtocol().setMoving(false);
		friend0.getAiProtocol().setParked(true);
		((ArmyCombatProtocol) friend0.getAiProtocol()).setPreLocked(false);
		friend0.register();
		friend0.setRotation(rot0);
		x += distance;
		y += distance;
		final FriendAlpha1 friend1 = new FriendAlpha1(x, y, null);
		friend1.getAiProtocol().setMoving(false);
		friend1.getAiProtocol().setParked(true);
		((ArmyCombatProtocol) friend1.getAiProtocol()).setPreLocked(false);
		friend1.register();
		friend1.setRotation(rot0);
		x += distance;
		y += distance;
		final FriendAlpha1 friend2 = new FriendAlpha1(x, y, null);
		friend2.getAiProtocol().setMoving(false);
		friend2.getAiProtocol().setParked(true);
		((ArmyCombatProtocol) friend2.getAiProtocol()).setPreLocked(false);
		friend2.register();
		friend2.setRotation(rot0);
		x += distance;
		y += distance;
		final FriendAlpha1 friend3 = new FriendAlpha1(x, y, null);
		friend3.getAiProtocol().setMoving(false);
		friend3.getAiProtocol().setParked(true);
		((ArmyCombatProtocol) friend3.getAiProtocol()).setPreLocked(false);
		friend3.register();
		friend3.setRotation(rot0);
		final FriendAlpha1 friend4 = new FriendAlpha1(1560, 1660, null);
		friend4.getAiProtocol().setMoving(false);
		friend4.getAiProtocol().setParked(true);
		((ArmyCombatProtocol) friend4.getAiProtocol()).setPreLocked(false);
		friend4.register();
		friend4.setRotation(rot1);
		final FriendAlpha1 friend5 = new FriendAlpha1(1510, 1610, null);
		friend5.getAiProtocol().setMoving(false);
		friend5.getAiProtocol().setParked(true);
		((ArmyCombatProtocol) friend5.getAiProtocol()).setPreLocked(false);
		friend5.register();
		friend5.setRotation(rot1);

		DamageCall awakeFriends = new DamageCall() {

			@Override
			public void damage(CombatObject source, Projectile proj, int dmg) {
				friend0.getAiProtocol().setParked(false);
				friend1.getAiProtocol().setParked(false);
				friend2.getAiProtocol().setParked(false);
				friend3.getAiProtocol().setParked(false);
				friend4.getAiProtocol().setParked(false);
				friend5.getAiProtocol().setParked(false);
			}
		};

		friend0.getAiProtocol().addCall(AiProtocol.KEY_CALL_GETTING_DAMAGED, awakeFriends);
		friend1.getAiProtocol().addCall(AiProtocol.KEY_CALL_GETTING_DAMAGED, awakeFriends);
		friend2.getAiProtocol().addCall(AiProtocol.KEY_CALL_GETTING_DAMAGED, awakeFriends);
		friend3.getAiProtocol().addCall(AiProtocol.KEY_CALL_GETTING_DAMAGED, awakeFriends);
		friend4.getAiProtocol().addCall(AiProtocol.KEY_CALL_GETTING_DAMAGED, awakeFriends);
		friend5.getAiProtocol().addCall(AiProtocol.KEY_CALL_GETTING_DAMAGED, awakeFriends);
		super.getInteractiveObjects().add(friend0);
		super.getInteractiveObjects().add(friend1);
		super.getInteractiveObjects().add(friend2);
		super.getInteractiveObjects().add(friend3);
		super.getInteractiveObjects().add(friend4);
		super.getInteractiveObjects().add(friend5);

		// Hie und da n paar Gegener...
		for (int i = 0; i != 10; i++) {

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

	}

}
