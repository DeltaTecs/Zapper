package battle.ai;

import java.util.ArrayList;

import battle.GuardianTurret;
import battle.enemy.Enemy;
import corecase.MainZap;
import ingameobjects.InteractiveObject;
import library.ScheduledList;

public class GuardianTurretProtocol extends AiProtocol {

	private static final int DISTANCE_FACE = 420;
	private static final int DISTANCE_SHOOT = GuardianTurret.SHOOTING_RANGE;
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

		InteractiveObject lock = getLockOn();

		if (lock instanceof Enemy) {
			if (!((Enemy) lock).isAlive()) {
				lock = null;
				setLockOn(null);
				getHost().setShootingAim(null);
			}
		}

		if (lock == null) {
			// Lock suchen
			lock = searchForLock();
			setLockOn(lock);
			getHost().setShootingAim(lock);
		}
	}

	public InteractiveObject searchForLock() {

		// Alle wirklich möglichen locks
		ArrayList<InteractiveObject> possibleLocks = new ArrayList<InteractiveObject>();

		// Umgebung holen
		ArrayList<Enemy> surrounding = MainZap.getGrid().getSurrounding(getHost().getLocX(), getHost().getLocY(),
				DISTANCE_SHOOT);

		// Umgebung abgehen
		for (Enemy e : surrounding) {

			if (e.isFriend())
				continue; // nur Feinde beschießen

			// Da is was in Range
			if (e.isInRange(getHost(), DISTANCE_SHOOT))
				possibleLocks.add(e);
		}

		if (possibleLocks.size() == 0)
			return null; // nix da

		// Zufälliges Lock wählen
		InteractiveObject choise = possibleLocks.get(rand(possibleLocks.size()));
		if (!MainZap.fittsMap(choise.getLocX(), choise.getLocY())) {
			// Nicht in der Grid
			for (InteractiveObject o : possibleLocks) {
				// Nimm einfach irgendeins
				if (MainZap.fittsMap(o.getLocX(), o.getLocY()))
					return o;
			}
			// Kein gefunden...
			return null;
		}
		return choise;

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
