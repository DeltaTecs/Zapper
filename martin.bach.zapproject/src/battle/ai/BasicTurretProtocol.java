package battle.ai;

import java.util.ArrayList;

import battle.CombatObject;
import battle.enemy.Enemy;
import corecase.MainZap;
import lib.ScheduledList;

public class BasicTurretProtocol extends AiProtocol {

	private static final float ROTATIONSPEED_DEFAULT = 0.001f;
	private static final int LOCK_OPTIC_DETECTION = 400;
	private static final int LOCK_FACE = 480;
	private static final int LOCK_PHYSICAL_DETECTION = 800;

	private boolean idleRotate = true;
	private float rotationSpeed = ROTATIONSPEED_DEFAULT;

	public BasicTurretProtocol() {
		super();
		setMoving(false);
		allowFacePlayer(true);
		setLockOpticDetectionRange(LOCK_OPTIC_DETECTION);
		setLockPhysicalDetectionRange(LOCK_PHYSICAL_DETECTION);
		setLockFaceDistance(LOCK_FACE);
		setSelfRotating(true);
	}

	@Override
	public void updateRotation() {

		if (idleRotate && getLockOn() == null)
			// Nix zu tun. Drehen...
			getHost().setRotation(getHost().getRotation() + ROTATIONSPEED_DEFAULT);
		else
			super.updateRotation(); // Aim vorhanden
	}

	@Override
	public void updateLockOn() {

		CombatObject lock = getLockOn();

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

		// Auch der Spieler ist Lockbar, wenn in Range
		if (!getHost().isFriend() && getHost().distanceToPlayer() <= getLockOpticDetectionRange() && MainZap.getPlayer().isAlive()) {
			if (isPrioritisePlayerAsLockOn()) // Spieler bevorzugen
				return MainZap.getPlayer();
			possibleLocks.add(MainZap.getPlayer());
		}

		// Umgebung holen
		ArrayList<Enemy> surrounding = MainZap.getGrid().getEnemySurrounding(getHost().getLocX(), getHost().getLocY(),
				getLockOpticDetectionRange());

		// Umgebung abgehen
		for (Enemy e : surrounding) {

			if (e.isFriend() == getHost().isFriend() || !e.isAlive())
				continue; // Keinen der eigenen Sippschaft anvisieren

			// Da is was in Range
			if (e.isInRange(getHost(), getLockOpticDetectionRange()))
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

	public boolean isIdleRotate() {
		return idleRotate;
	}

	public void setIdleRotate(boolean idleRotate) {
		this.idleRotate = idleRotate;
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

}
