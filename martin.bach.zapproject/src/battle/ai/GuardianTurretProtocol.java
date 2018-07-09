package battle.ai;

import java.util.ArrayList;

import battle.CombatObject;
import battle.GuardianTurret;
import battle.enemy.Enemy;
import battle.looting.Storage;
import corecase.MainZap;
import lib.ScheduledList;

public class GuardianTurretProtocol extends AiProtocol {

	private static final int DISTANCE_FACE = GuardianTurret.SHOOTING_RANGE - 100;
	private static final int DISTANCE_SHOOT = GuardianTurret.SHOOTING_RANGE - 200;
	private static final int DISTANCE_PHYSICAL_DETECTION = 800;

	public GuardianTurretProtocol() {
		super();
		setMoving(false);
		setLockFaceDistance(DISTANCE_FACE);
		setLockPhysicalDetectionRange(DISTANCE_PHYSICAL_DETECTION);
		setLockOpticDetectionRange(DISTANCE_SHOOT);
	}

	@Override
	public void updateLockOn() {

		CombatObject lock = getLockOn();

		if (lock != null) {
			if (lock.distanceTo(getHost()) > DISTANCE_SHOOT)
				lock = null; // Fallen lassen, da out of range
		}

		if (lock == null) {
			// Lock suchen
			lock = searchForLock();
			setLockOn(lock);
			getHost().setShootingAim(lock);
			return;
		}

		if (!lock.isAlive()) {
			lock = null;
			setLockOn(null);
			getHost().setShootingAim(null);
		}

	}

	public CombatObject searchForLock() {

		// Alle wirklich möglichen locks
		ArrayList<CombatObject> possibleLocks = new ArrayList<CombatObject>();

		// Umgebung holen
		ArrayList<Enemy> surrounding = MainZap.getGrid().getEnemySurrounding(getHost().getLocX(), getHost().getLocY(),
				DISTANCE_SHOOT);

		// Umgebung abgehen
		for (Enemy e : surrounding) {

			if (e.isFriend() || !e.isAlive() || !MainZap.fittsMap(e.getLocX(), e.getLocY()) || e instanceof Storage)
				continue; // nur Feinde in der Grid beschießen

			// Da is was in Range
			if (e.isInRange(getHost(), DISTANCE_SHOOT))
				possibleLocks.add(e);
		}

		if (possibleLocks.size() == 0)
			return null; // nix da

		// Das Nächst-Gelegene auswählen
		int lowestDistance = 3000;
		CombatObject nearest = possibleLocks.get(0);
		for (CombatObject c : possibleLocks) {
			if (getHost().distanceTo(c) < lowestDistance) {
				nearest = c;
				lowestDistance = getHost().distanceTo(c);
			}
		}
		return nearest;
	}

	public void registerForAutoUnregisterFrom(final ScheduledList<Enemy> list) {
		addCall(KEY_CALL_DIEING, new DieCall() {

			@Override
			public void die() {
				list.schedAdd(getHost());
			}
		});
	}

	@Override
	public Object getClone() {
		return new GuardianTurretProtocol();
	}

}
