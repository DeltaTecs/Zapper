package battle.ai;

import java.util.ArrayList;

import battle.enemy.Enemy;
import corecase.MainZap;
import ingameobjects.InteractiveObject;
import library.ScheduledList;

public class BasicTurretProtocol extends AiProtocol {

	private static final int DISTANCE_FACE = 420;
	private static final int DISTANCE_SHOOT = 400;
	private static final int DISTANCE_PHYSICAL_DETECTION = 800;

	public BasicTurretProtocol() {
		super();
		setMoving(false);
		allowFacePlayer(true);
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
		} else if (!MainZap.getPlayer().isAlive()) {
			lock = null;
			setLockOn(null);
			getHost().setShootingAim(null);
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

		// Auch der Spieler ist Lockbar, wenn in Range
		if (getHost().distanceToPlayer() <= DISTANCE_SHOOT && MainZap.getPlayer().isAlive()) {
			if (isPrioritisePlayerAsLockOn()) // Spieler bevorzugen
				return MainZap.getPlayer();
			possibleLocks.add(MainZap.getPlayer());
		}

		// Umgebung holen
		ArrayList<Enemy> surrounding = MainZap.getGrid().getSurrounding(getHost().getLocX(), getHost().getLocY(),
				DISTANCE_SHOOT);

		// Umgebung abgehen
		for (Enemy e : surrounding) {

			if (e.isFriend() == getHost().isFriend())
				continue; // Keinen der eigenen Sippschaft anvisieren

			// Da is was in Range
			if (e.isInRange(getHost(), DISTANCE_SHOOT))
				possibleLocks.add(e);
		}

		if (possibleLocks.size() == 0)
			return null; // nix da

		// Zufälliges Lock wählen
		return possibleLocks.get(rand(possibleLocks.size()));

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
		return new BasicTurretProtocol();
	}

}
