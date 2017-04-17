package battle.collect;

public class CollectableSpawner {

	public static void spawn(PackType what, int x, int y, Runnable unregisterTask) {

		switch (what) {
		case AMMO_LARGE:
			AmmoPackLarge pack0 = new AmmoPackLarge();
			pack0.setPosition(x, y);
			pack0.setUnregisterTask(unregisterTask);
			pack0.register();
			break;
		case AMMO_SMALL:
			AmmoPackSmall pack1 = new AmmoPackSmall();
			pack1.setPosition(x, y);
			pack1.setUnregisterTask(unregisterTask);
			pack1.register();
			break;
		case BULLET_DMG:
			BulletDamageUp.spawnDamageUp(x, y, unregisterTask);
			break;
		case BULLET_RANGE:
			BulletRangeUp.spawnRangeUp(x, y, unregisterTask);
			break;
		case BULLET_SPEED:
			BulletSpeedUp.spawnBulletSpeedUp(x, y, unregisterTask);
			break;
		case HEALTH:
			HealthUp.spawnHpUp(x, y, unregisterTask);
			break;
		case RELOAD:
			ReloadUp.spawnReloadUp(x, y, unregisterTask);
			break;
		case SPEED:
			SpeedUp.spawnSpeedUp(x, y, unregisterTask);
			break;
		default:
			// Gibts nicht
			break;
		}

	}

	public static void spawnHpUp(int x, int y) {
		HealthUp.spawnHpUp(x, y);
	}

	public static void spawnAmmoPack(int x, int y, boolean large) {

		if (large) {
			AmmoPackLarge pack = new AmmoPackLarge();
			pack.setPosition(x, y);
			pack.register();
		} else {
			AmmoPackSmall pack = new AmmoPackSmall();
			pack.setPosition(x, y);
			pack.register();
		}

	}

	public static void spawnHpUp(int x, int y, Runnable unregisterTask) {
		HealthUp.spawnHpUp(x, y, unregisterTask);
	}

	public static void spawnAmmoPack(int x, int y, boolean large, Runnable unregisterTask) {

		if (large) {
			AmmoPackLarge pack = new AmmoPackLarge();
			pack.setPosition(x, y);
			pack.register();
			pack.setUnregisterTask(unregisterTask);
		} else {
			AmmoPackSmall pack = new AmmoPackSmall();
			pack.setPosition(x, y);
			pack.register();
			pack.setUnregisterTask(unregisterTask);
		}

	}

	public static void spawnSpeedUp(int x, int y) {
		SpeedUp.spawnSpeedUp(x, y);
	}

	public static void spawnSpeedUp(int x, int y, Runnable unregisterTask) {
		SpeedUp.spawnSpeedUp(x, y, unregisterTask);
	}

	public static void spawnBulletSpeedUp(int x, int y) {
		BulletSpeedUp.spawnBulletSpeedUp(x, y);
	}

	public static void spawnBulletSpeedUp(int x, int y, Runnable unregisterTask) {
		BulletSpeedUp.spawnBulletSpeedUp(x, y, unregisterTask);
	}

	public static void spawnBulletRangeUp(int x, int y) {
		BulletRangeUp.spawnRangeUp(x, y);
	}

	public static void spawnBulletRangeUp(int x, int y, Runnable unregisterTask) {
		BulletRangeUp.spawnRangeUp(x, y, unregisterTask);
	}

	public static void spawnBulletDamageUp(int x, int y) {
		BulletDamageUp.spawnDamageUp(x, y);
	}

	public static void spawnBulletDamageUp(int x, int y, Runnable unregisterTask) {
		BulletDamageUp.spawnDamageUp(x, y, unregisterTask);
	}

	public static void spawnReloadUp(int x, int y) {
		ReloadUp.spawnReloadUp(x, y);
	}

	public static void spawnReloadUp(int x, int y, Runnable unregisterTask) {
		ReloadUp.spawnReloadUp(x, y, unregisterTask);
	}

}
