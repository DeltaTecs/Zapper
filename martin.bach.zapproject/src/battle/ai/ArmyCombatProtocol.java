package battle.ai;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import battle.enemy.Enemy;
import battle.projectile.Projectile;
import collision.Grid;
import corecase.MainZap;
import gui.Map;
import ingameobjects.InteractiveObject;
import lib.ScheduledList;

public class ArmyCombatProtocol extends AiProtocol {

	private static final int DISTANCE_FACE = 300;
	private static final int DISTANCE_FOLLOW = 300;
	private static final int DISTANCE_STOP = 100;
	private static final int DISTANCE_PHYSICAL_DETECTION = 800;

	private static final int MAX_STOP_TIME = MainZap.getMainLoop().inTicks(32000);
	private static final int MAX_MOVING_TIME_IDLE = MainZap.getMainLoop().inTicks(4000);
	private static final int MAX_MOVING_TIME_COMBAT = MainZap.getMainLoop().inTicks(2000);
	private static final int FORCED_MOVING_TIME_IDLE = MainZap.getMainLoop().inTicks(3500);
	private static final int FORCED_MOVING_TIME_COMBAT = MainZap.getMainLoop().inTicks(200);
	private static final int FORCED_WAITING_TIME = MainZap.getMainLoop().inTicks(2000);
	private static final int TIME_GOING_BACK_INTO_COMBAT = MainZap.inTicks(1800);

	private static final int MAX_AIM_POS_D_ENEMY_LOC = 50;

	private ScheduledList<Enemy> enemysAround;
	private int lockRange;
	private boolean preLocked;
	private Rectangle movementBounds = null;
	private boolean hadNoContactYet = true;
	private boolean preLockedYet = false;

	private int timeToNextMovementAction = 0;

	public ArmyCombatProtocol(ScheduledList<Enemy> sourrounding, int lockRange, boolean preLocked) {
		super();
		setLockFaceDistance(DISTANCE_FACE);
		setLockOpticDetectionRange(DISTANCE_FOLLOW);
		setLockStopRange(DISTANCE_STOP);
		setLockPhysicalDetectionRange(DISTANCE_PHYSICAL_DETECTION);
		setDamageRecognizeable();

		this.preLocked = preLocked;
		this.enemysAround = sourrounding;
		this.lockRange = lockRange;

		addCall(KEY_CALL_DIEING, new DieCall() {
			@Override
			public void die() {
				if (enemysAround != null)
					getEnemysAround().schedRemove(getHost());
			}
		});

		addCall(KEY_CALL_GETTING_DAMAGED, new DamageCall() {

			@Override
			public void damage(InteractiveObject source, Projectile proj, int dmg) {
				if (!hadNoContactYet)
					return; // preLock schon verpufft
				if (source == MainZap.getPlayer() && getHost().isFriend())
					return; // Friendly-Fire
				
				setLockOn(source);
				getHost().setShootingAim(source);
			}
		});

	}

	@Override
	public void updateIdle() {

		if (timeToNextMovementAction > 0) {
			timeToNextMovementAction--;
		} else {

			// Am Zug.

			if (isMoving()) { // Jetzt Stoppen
				stopMoving();
				timeToNextMovementAction = FORCED_WAITING_TIME + rand(MAX_STOP_TIME);

			} else { // Jetzt Bewegen

				if (movementBounds == null) { // Auf ganzer Map bewegen
					moveTo(rand(Map.SIZE), rand(Map.SIZE));
				} else { // Die Bounds anaimen
					moveTo(rand(movementBounds.width) + movementBounds.x,
							rand(movementBounds.height) + movementBounds.y);
				}

				timeToNextMovementAction = FORCED_MOVING_TIME_IDLE + rand(MAX_MOVING_TIME_IDLE);
			}

		}

	}

	@Override
	public void updateMovement() {

		if (isIdleing()) { // reguläre Bewegung
			super.updateMovement();
			return;
		}

		// Bewegung in Kampfhandlung
		int maxCombatDistance = (int) (getLockOutOfRangeRange() * 0.75f);
		InteractiveObject lock = getLockOn();
		int distance = Grid.distance(new Point(lock.getLocX(), lock.getLocY()),
				new Point(getHost().getLocX(), getHost().getLocY()));

		if (distance >= maxCombatDistance) {
			if (hadNoContactYet) { // Noch am hinfliegen
				getHost().getVelocity().aimFor(getHost().getPosX(), getHost().getPosY(), getHost().getSpeed(),
						lock.getPosX(), lock.getPosY());
				move();
				return;
			}
			// Im Kampf, aus der Range gelangt
			// In Range bleiben!
			moveTo(rand(2 * MAX_AIM_POS_D_ENEMY_LOC) - MAX_AIM_POS_D_ENEMY_LOC,
					rand(2 * MAX_AIM_POS_D_ENEMY_LOC) - MAX_AIM_POS_D_ENEMY_LOC);
			move();
			timeToNextMovementAction = TIME_GOING_BACK_INTO_COMBAT;
			return;
		} else if (hadNoContactYet)
			hadNoContactYet = true;

		if (timeToNextMovementAction > 0) { // warten
			timeToNextMovementAction--;
		} else { // handeln

			moveTo((int) (getHost().getPosX() + rand(2000) - 1000), (int) (getHost().getPosY() + rand(2000) - 1000));
			timeToNextMovementAction = FORCED_MOVING_TIME_COMBAT + rand(MAX_MOVING_TIME_COMBAT);
		}
		move();

	}

	@Override
	public void updateLockOn() {

		if (getLockOn() != null) {
			// Lock vorhanden

			if (getLockOn() instanceof Enemy) {
				Enemy lock = (Enemy) getLockOn();

				if (!lock.isAlive()) { // Lock tot
					setLockOn(searchForLock());
					getHost().setShootingAim(getLockOn());
					return;
				}

			} else if (!MainZap.getPlayer().isAlive()) {
				setLockOn(searchForLock());
				getHost().setShootingAim(getLockOn());
			}

		} else if (!isParked()){ // Wenn nicht geparkt...

			if (preLocked && !preLockedYet) { // erster Durchlauf
				preLock();
				preLockedYet = true;
				return;
			}

			// Nach Lock suchen
			setLockOn(searchForLock());
			getHost().setShootingAim(getLockOn());

		}
	}

	private void move() {
		getHost().moveX(getHost().getVelocity().getX());
		getHost().moveY(getHost().getVelocity().getY());
	}

	public int getLockRange() {
		return lockRange;
	}

	public void setLockRange(int lockRange) {
		this.lockRange = lockRange;
	}

	public boolean isPreLocked() {
		return preLocked;
	}

	public void preLock() {

		ArrayList<InteractiveObject> possibleLocks = new ArrayList<InteractiveObject>();

		for (Enemy e : enemysAround) {

			if (e.isFriend() != getHost().isFriend())
				possibleLocks.add(e);
		}

		if (!getHost().isFriend()) // Falls Gengner, Spieler auch lockbar
			possibleLocks.add(MainZap.getPlayer());

		if (possibleLocks.size() == 0)
			return; // nix da zum locken

		InteractiveObject lock = possibleLocks.get(rand(possibleLocks.size()));
		setLockOn(lock);
		getHost().setShootingAim(lock);
	}

	public InteractiveObject searchForLock() {

		ArrayList<InteractiveObject> possibleLocks = new ArrayList<InteractiveObject>();

		if (!getHost().isFriend()) {
			if (getHost().distanceToPlayer() <= lockRange && MainZap.getPlayer().isAlive()) {
				if (isPrioritisePlayerAsLockOn())
					return MainZap.getPlayer();
				possibleLocks.add(MainZap.getPlayer());
			}
		}

		// Umgebung holen
		ArrayList<Enemy> surrounding = MainZap.getGrid().getEnemySurrounding(getHost().getLocX(), getHost().getLocY(),
				getLockRange());

		// Umgebung abgehen
		for (Enemy e : surrounding) {

			if (e.isFriend() == getHost().isFriend())
				continue; // Keinen der eigenen Sippschaft anvisieren

			// Da is was in Range
			if (e.isInRange(getHost(), getLockRange()))
				possibleLocks.add(e);
		}

		if (possibleLocks.size() == 0)
			return null; // nix da zum locken

		return possibleLocks.get(rand(possibleLocks.size()));
	}

	public Rectangle getMovementBounds() {
		return movementBounds;
	}

	public void setMovementBounds(Rectangle movementBounds) {
		this.movementBounds = movementBounds;
	}

	public ScheduledList<Enemy> getEnemysAround() {
		return enemysAround;
	}

	public void setPreLocked(boolean preLocked) {
		this.preLocked = preLocked;
	}

}
