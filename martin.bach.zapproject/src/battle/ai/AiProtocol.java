package battle.ai;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import battle.CombatObject;
import battle.enemy.Enemy;
import battle.projectile.Projectile;
import collision.Grid;
import corecase.MainZap;
import ingameobjects.CloneableObject;
import ingameobjects.InteractiveObject;
import lib.Updateable;

public class AiProtocol implements Updateable, CloneableObject {

	private static final int TOLERANCE_DESTINATION = 100;

	public static final String KEY_CALL_REACHED_DESTINATION = "call:destination-reached";
	public static final String KEY_CALL_GETTING_DAMAGED = "call:getting-damage";
	public static final String KEY_CALL_DIEING = "call:dieing";

	private Enemy host;
	private Random random = new Random();

	private boolean moving = false;
	private int destinationX;
	private int destinationY;
	private boolean areaBound = false;
	private Rectangle boundArea = null;
	private int waitingTime = 0;
	private int lockFaceDistance = 150;
	private int lockOpticDetectionRange = 150;
	private int lockOutOfRangeRange = 400;
	private int lockPhysicalDetectionRange = 300;
	private int lockStopRange = 100;
	private CombatObject lockOn;
	private boolean nonAutoLockon = false;
	private boolean prioritisePlayerAsLockOn = false;
	private boolean mayFacePlayer = false;
	private boolean mayFollowLock = true;
	private boolean shootAimPlayerOnly = false;
	private boolean parked = false;

	private HashMap<String, ArrayList<Call>> calls = new HashMap<String, ArrayList<Call>>();

	public AiProtocol() {
	}

	public void init(Enemy host) {
		this.host = host;
	}

	public Enemy getHost() {
		return host;
	}

	@Override
	public void update() {

		if (waitingTime > 0) {
			waitingTime--;
			return;
		}
		updateLockOn();
		if (isIdleing() && !parked)
			updateIdle(); // Nur für Nix-Zu-Tun
		updateRotation();
		updateMovement();
	}

	public void updateLockOn() {

		if (nonAutoLockon)
			return;

		int playerDistance = host.distanceToPlayer();

		if (lockOn == null) {
			// Kein Lock
			if (playerDistance <= lockOpticDetectionRange && MainZap.getPlayer().isAlive()) {
				// Spieler anvisieren da in Range und lebendig
				lockOn = MainZap.getPlayer();
				host.setShootingAim(MainZap.getPlayer());
			}
			if (shootAimPlayerOnly)
				return;
			// +++++ Weitere Lock-On Abragen............

		} else {
			// Lock bereits da

			if (lockOn != MainZap.getPlayer()) {
				// Lock ist kein Spieler
				// Spieler priorisieren?
				if (playerDistance <= lockOpticDetectionRange && prioritisePlayerAsLockOn
						&& MainZap.getPlayer().isAlive()) {
					// Spieler anvisieren da in Range und Priorität
					lockOn = MainZap.getPlayer();
				} else { // Lock
					// Kein Spieler in der Nähe / keine Prio
					// Lock noch in Range oder lebendig?
					if (!host.isInRange(lockOn, lockOutOfRangeRange) || !lockOn.isAlive()) {
						// Lock außer Sicht
						lockOn = null; // fallen lassen
						host.setShootingAim(null);
					}
				}
			} else {
				// Lock ist Spieler
				if (!host.isInRange(lockOn, lockOutOfRangeRange) || !MainZap.getPlayer().isAlive()) {
					// Lock außer Sicht oder spieler tot
					lockOn = null; // fallen lassen
					host.setShootingAim(null);
				}
			}

		}

	}

	public void updateMovement() {

		if (lockOn != null) {
			if (host.isInRange(lockOn, lockStopRange)) {
				host.getVelocity().setX(0);
				host.getVelocity().setY(0);
				setMoving(false);
			} else {

				if (areaBound && !boundArea.contains(host.getPosX(), host.getPosY())) {
					// LockOn in Verbotener Zone verloren.
					// --> Verbotene Zone aufheben
					areaBound = false;
				}

				host.getVelocity().aimFor(host.getPosX(), host.getPosY(), host.getSpeed(), lockOn.getPosX(),
						lockOn.getPosY());
				setMoving(true);
			}
		}

		if (!moving)
			return; // Bewegung nicht erwünscht

		if (reachedDestination()) { // Angekommen
			moving = false;
			call(KEY_CALL_REACHED_DESTINATION, formDestinationReachesArgs(destinationX, destinationY));

			return;
		}

		if (areaBound && lockOn != null) {
			if (!boundArea.contains(host.getPosX() + host.getVelocity().getX(),
					host.getPosY() + host.getVelocity().getY())) {
				return; // Verbotene Zone
			}
		}

		// Beweg dich!
		// Das muss das Protocol machen.
		host.moveX(host.getVelocity().getX());
		host.moveY(host.getVelocity().getY());

	}

	public void updateIdle() {

	}

	public void updateRotation() {

		if (lockOn != null) {
			// Lock anvisieren
			host.setAimX((int) lockOn.getPosX());
			host.setAimY((int) lockOn.getPosY());
			return;
		}

		if (host.distanceToPlayer() <= lockFaceDistance && mayFacePlayer && MainZap.getPlayer().isAlive()) {
			// Anvisierung erwünscht und möglich
			host.setAimX((int) MainZap.getPlayer().getPosX());
			host.setAimY((int) MainZap.getPlayer().getPosY());
		} else {

			if (!(host.getVelocity().getX() == 0 && host.getVelocity().getY() == 0)) {
				// in Richtung der Bewegung zeigen, da Bewegung vorhanden
				host.setAimX(host.getLocX() + (int) (10000 * host.getVelocity().getX()));
				host.setAimY(host.getLocY() + (int) (10000 * host.getVelocity().getY()));
			} // Sonst: Aim beibehalten

		}

	}

	public void call(String key, Object[] args) {

		if (!calls.containsKey(key))
			return; // Nix da

		Call indicator = calls.get(key).get(0);

		if (indicator instanceof DamageCall) {

			// args sind: [0]: Inter.Obj. source; [1]: Proj. proj; [2]: int
			// damage
			CombatObject source = (CombatObject) args[0];
			Projectile proj = (Projectile) args[1];
			int dmg = (int) args[2];

			for (Call c : calls.get(key)) {
				((DamageCall) c).damage(source, proj, dmg);
			}

		} else if (indicator instanceof DieCall) {

			// Keine Args.
			for (Call c : calls.get(key)) {
				((DieCall) c).die();
			}

		} else if (indicator instanceof DestinationReachedCall) {

			// args sind: [0]: int desX; [1]: int desY
			int desX = (int) args[0];
			int desY = (int) args[1];

			for (Call c : calls.get(key)) {
				((DestinationReachedCall) c).desReached(desX, desY);
			}

		}

	}

	public void addCall(String key, Call c) {

		if (!calls.containsKey(key)) { // Noch nicht eingetragen
			calls.put(key, new ArrayList<Call>());
			calls.get(key).add(c);
		} else {
			calls.get(key).add(c);
		}

	}

	public void addCalls(String key, ArrayList<Call> c) {

		if (!calls.containsKey(key)) { // Noch nicht eingetragen
			calls.put(key, c);
		} else {
			calls.get(key).addAll(c);
		}

	}

	public boolean isAreaBound() {
		return areaBound;
	}

	public void setAreaBound(boolean areaBound) {
		this.areaBound = areaBound;
	}

	public Rectangle getBoundArea() {
		return boundArea;
	}

	public void setBoundArea(Rectangle boundArea) {
		this.boundArea = boundArea;
	}

	private boolean reachedDestination() {
		return Grid.inRange(new Point(host.getLocX(), host.getLocY()), new Point(destinationX, destinationY),
				TOLERANCE_DESTINATION);
	}

	public static Object[] formDamageCallArgs(InteractiveObject src, Projectile proj, int dmg) {
		return new Object[] { src, proj, dmg };
	}

	public static Object[] formDieCallArgs() {
		return new Object[] {};
	}

	public static Object[] formDestinationReachesArgs(int desX, int desY) {
		return new Object[] { desX, desY };
	}

	public void setDamageRecognizeable() {

		DamageCall call = new DamageCall() {

			@Override
			public void damage(CombatObject source, Projectile proj, int dmg) {

				// Zur Quelle gehen, falls lockon nicht schon vorhanden
				if (lockOn != null)
					return;

				// Distanz prüfen
				if (!source.isInRange(host, lockPhysicalDetectionRange))
					return;

				if (source == MainZap.getPlayer() && getHost().isFriend())
					return; // Friendly-Fire

				// Alle Bedingungen erfüllt
				if (parked) // Park-Bremse aufheben
					parked = false;
				lockOn = source;
				host.setShootingAim(source);
			}

		};

		addCall(KEY_CALL_GETTING_DAMAGED, call);

	}

	public boolean prop(int denominator) {
		return random.nextInt(denominator - 1) == 0;
	}

	public int rand(int bound) {
		return random.nextInt(bound);
	}

	public void waitTicks(int ticks) {
		waitingTime = ticks;
	}

	public void stopMoving() {
		moving = false;
	}

	public void moveTo(int x, int y) {

		// Ausrichten
		host.getVelocity().aimFor(host.getPosX(), host.getPosY(), host.getSpeed(), x, y);
		destinationX = x;
		destinationY = y;
		moving = true;
	}

	public boolean isIdleing() {
		return lockOn == null;
	}

	public boolean isWaiting() {
		return waitingTime > 0;
	}

	public boolean isMoving() {
		return moving;
	}

	public int getDestinationX() {
		return destinationX;
	}

	public int getDestinationY() {
		return destinationY;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public Random getRandom() {
		return random;
	}

	public boolean mayFacePlayer() {
		return mayFacePlayer;
	}

	public void allowFacePlayer(boolean mayFacePlayer) {
		this.mayFacePlayer = mayFacePlayer;
	}

	public int getLockFaceDistance() {
		return lockFaceDistance;
	}

	public void setLockFaceDistance(int playerFaceDistance) {
		this.lockFaceDistance = playerFaceDistance;
	}

	public boolean isShootAimPlayerOnly() {
		return shootAimPlayerOnly;
	}

	public void setShootAimPlayerOnly(boolean shootAimPlayerOnly) {
		this.shootAimPlayerOnly = shootAimPlayerOnly;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public boolean isMayFollowLock() {
		return mayFollowLock;
	}

	public void allowFollowPlayer(boolean mayFollowPlayer) {
		this.mayFollowLock = mayFollowPlayer;
	}

	public int getLockOpticDetectionRange() {
		return lockOpticDetectionRange;
	}

	public void setLockOpticDetectionRange(int playerOpticDetectionRange) {
		this.lockOpticDetectionRange = playerOpticDetectionRange;
	}

	public int getLockOutOfRangeRange() {
		return lockOutOfRangeRange;
	}

	public void setLockOutOfRangeRange(int playerOutOfRangeRange) {
		this.lockOutOfRangeRange = playerOutOfRangeRange;
	}

	public int getLockPhysicalDetectionRange() {
		return lockPhysicalDetectionRange;
	}

	public void setLockPhysicalDetectionRange(int playerPhysicalDetectionRange) {
		this.lockPhysicalDetectionRange = playerPhysicalDetectionRange;
	}

	public int getLockStopRange() {
		return lockStopRange;
	}

	public void setLockStopRange(int playerStopRange) {
		this.lockStopRange = playerStopRange;
	}

	public boolean isFollowingLock() {
		return lockOn != null;
	}

	public CombatObject getLockOn() {
		return lockOn;
	}

	public void setLockOn(CombatObject lockOn) {
		this.lockOn = lockOn;
		getHost().setShootingAim(lockOn);
	}

	public boolean isPrioritisePlayerAsLockOn() {
		return prioritisePlayerAsLockOn;
	}

	public void setPrioritisePlayerAsLockOn(boolean prioritisePlayerAsLockOn) {
		this.prioritisePlayerAsLockOn = prioritisePlayerAsLockOn;
	}

	public HashMap<String, ArrayList<Call>> getCalls() {
		return calls;
	}

	public boolean isParked() {
		return parked;
	}

	public void setParked(boolean parked) {
		this.parked = parked;
	}

	public boolean isNonAutoLockon() {
		return nonAutoLockon;
	}

	public void setNonAutoLockon(boolean nonAutoLockon) {
		this.nonAutoLockon = nonAutoLockon;
	}

	@Override
	public Object getClone() {
		return new AiProtocol();
	}
}
