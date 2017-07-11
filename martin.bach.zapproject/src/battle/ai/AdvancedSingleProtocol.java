package battle.ai;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import battle.CombatObject;
import battle.enemy.Enemy;
import battle.projectile.Projectile;
import collision.Collideable;
import collision.Grid;
import corecase.MainZap;
import err.MissingShipLinkageException;
import gui.Map;
import ingameobjects.InteractiveObject;
import ingameobjects.Player;

public class AdvancedSingleProtocol extends AiProtocol {

	private static final int MAX_STOP_TIME = MainZap.getMainLoop().inTicks(32000);
	private static final int MAX_MOVING_TIME_IDLE = MainZap.getMainLoop().inTicks(4000);
	private static final int MAX_MOVING_TIME_COMBAT = MainZap.getMainLoop().inTicks(2000);
	private static final int FORCED_MOVING_TIME_IDLE = MainZap.getMainLoop().inTicks(3500);
	private static final int FORCED_MOVING_TIME_COMBAT = MainZap.getMainLoop().inTicks(200);
	private static final int FORCED_WAITING_TIME = MainZap.getMainLoop().inTicks(2000);
	private static final int TIME_GOING_BACK_INTO_COMBAT = MainZap.inTicks(1800);
	private static final int COMBAT_FOLLOWUP_COOLDOWN = MainZap.inTicks(1000);

	private int ancX, ancY; // Lokalisation, von der nicht abgewichen werden
							// soll
	private int ancRange; // wie weit abgewichen werden darf
	private int ancRandReturn; // Wahrscheinlichkeit dann umzudrehen
	private int ancRadian; // In welchem Radius dieses Anker operiert
	private int ancAimX, ancAimY; // Temporäres Anker lock-on für Bewegung
	private boolean anchored = false; // Ob überhaupt verankert
	private boolean ancInAction = false; // Am zurück fliegen?
	private boolean ancCrowdEnabled = false; // Anker automatisch in der
												// armee-mitte setzen?

	// Was tun wenn ohne Lock?
	private FindLockAction lockAction = FindLockAction.LOCK_ENEMYS_INRANGE;

	private int timeToNextMovementAction = 2;
	private int combatFollowupCooldown = 0;
	private boolean wasInCombat = false;
	private Rectangle movementBounds = null; // Brgrenztes bewegungs-Gebiet
	private int lockCombatFreeMovementRange = 100;
	// Ab welcher relativ zur out-of-range-range gesehenen Range soll die
	// Distanz verringert werden?
	private float combatRangePerOutOfRangeRange = 0.75f; // -> 75%

	private ArrayList<CombatObject> linkedAllies;
	private ArrayList<CombatObject> linkedEnemys;

	public AdvancedSingleProtocol() {
	}

	@Override
	public void updateMovement() {

		if (updateAnchor())
			return; // Anker-Bewegung vorgenommen

		if (isIdleing()) { // reguläre Bewegung
			super.updateMovement();
			return;
		}

		// Bewegung in Kampfhandlung
		int maxCombatDistance = (int) (getLockOutOfRangeRange() * combatRangePerOutOfRangeRange);
		InteractiveObject lock = getLockOn();
		int distance = Grid.distance(new Point(lock.getLocX(), lock.getLocY()),
				new Point(getHost().getLocX(), getHost().getLocY()));

		if (combatFollowupCooldown > 0)
			combatFollowupCooldown--;

		if (distance >= maxCombatDistance) {

			if (combatFollowupCooldown == 0) {
				combatFollowupCooldown = COMBAT_FOLLOWUP_COOLDOWN;

				// Im Kampf, aus der Range gelangt
				// In Range bleiben!
				moveTo(rand(2 * lockCombatFreeMovementRange) - lockCombatFreeMovementRange + lock.getLocX(),
						rand(2 * lockCombatFreeMovementRange) - lockCombatFreeMovementRange + lock.getLocY());
				move();
				timeToNextMovementAction = TIME_GOING_BACK_INTO_COMBAT;
				return;

			}
		}

		if (timeToNextMovementAction > 0) { // warten
			timeToNextMovementAction--;
		} else { // handeln

			moveTo((int) (getHost().getPosX() + rand(2000) - 1000), (int) (getHost().getPosY() + rand(2000) - 1000));
			timeToNextMovementAction = FORCED_MOVING_TIME_COMBAT + rand(MAX_MOVING_TIME_COMBAT);
		}
		move();

	}

	@Override
	public void updateIdle() {

		if (timeToNextMovementAction > 0) {
			timeToNextMovementAction--;
		} else {

			// Am Zug.

			if (isMoving() && !wasInCombat) { // Jetzt Stoppen
				stopMoving();
				timeToNextMovementAction = FORCED_WAITING_TIME + rand(MAX_STOP_TIME);

			} else { // Jetzt Bewegen
				if (wasInCombat)
					wasInCombat = false;
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

	// --- Lock-On ----------------------

	@Override
	public void updateLockOn() {

		if (getLockOn() == null) { // Kein Lock

			if (!tryLockOn()) // Nochmal suchen
				setLockOn(searchForLock());

		} else { // Hat Lock

			if (!wasInCombat)
				wasInCombat = true;

			if (getLockOn() instanceof Enemy) {
				Enemy lock = (Enemy) getLockOn();
				if ((!lock.isAlive() || !lock.isInRange(getHost(), getLockOutOfRangeRange())
						&& lockAction != FindLockAction.LOCK_LINKED_ENEMYS
						&& lockAction != FindLockAction.LOCK_LOCK_OF_LINKED_FRIENDS
						&& lockAction != FindLockAction.LOCK_LOCK_OF_LINKED_FRIENDS_INRANGE))
					setLockOn(null); // Fallen lassen
			} else if (getLockOn() instanceof Player) {
				if ((!MainZap.getPlayer().isAlive() || getLockOn().distanceToPlayer() > getLockOutOfRangeRange())
						&& lockAction != FindLockAction.LOCK_LINKED_ENEMYS
						&& lockAction != FindLockAction.LOCK_LOCK_OF_LINKED_FRIENDS
						&& lockAction != FindLockAction.LOCK_LOCK_OF_LINKED_FRIENDS_INRANGE)
					setLockOn(null); // Fallen lassen
			}

		}

	}

	// Versucht einen Lock-On im Rahmen der Lock-Action
	// Gibt TRUE zurück, wenn einer getätigt wurde
	private boolean tryLockOn() {

		switch (lockAction) {
		case LOCK_ENEMYS_INRANGE:
			// Was aus der Umgebung holen
			setLockOn(searchForLock());
			return getLockOn() != null;
		case LOCK_LINKED_ENEMYS:
			// Was verknüpfter holen
			if (linkedEnemys == null) // NullPointer
				throw new MissingShipLinkageException("linked enemys is null");
			if (linkedEnemys.size() == 0)
				return false; // nix da zum locken

			ArrayList<CombatObject> aliveLocks0 = new ArrayList<CombatObject>();
			for (CombatObject e : linkedEnemys)
				if (e.isAlive())
					aliveLocks0.add(e);

			if (aliveLocks0.size() == 0)
				return false; // nix lebendiges da

			setLockOn(aliveLocks0.get(rand(aliveLocks0.size())));
			return true;
		case LOCK_LINKED_ENEMYS_INRANGE:
			// Was Verknüpftes in Radius holen
			if (linkedEnemys == null) // NullPointer
				throw new MissingShipLinkageException("linked enemys is null");
			if (linkedEnemys.size() == 0)
				return false; // nix da zum locken

			ArrayList<CombatObject> inRange = new ArrayList<CombatObject>();
			for (CombatObject e : linkedEnemys)
				if (e.isInRange(getHost(), getLockOpticDetectionRange()) && e.isAlive())
					inRange.add(e); // In range

			if (inRange.size() == 0)
				return false;

			setLockOn(inRange.get(rand(inRange.size())));
			return true;
		case LOCK_LOCK_OF_FRIENDS_INRANGE:
			// Was locken, was andere im Radius locken

			ArrayList<Collideable> surrounding = MainZap.getGrid().getTotalSurrounding(getHost().getLocX(),
					getHost().getLocY(), getLockOpticDetectionRange());

			ArrayList<CombatObject> possibleLocks = new ArrayList<CombatObject>();
			for (Collideable c : surrounding) {
				if (c instanceof Enemy) { // Entity
					Enemy e = (Enemy) c;
					if (e.isFriend() == getHost().isFriend()) // Gleichgesittteter
						if (e.isInRange(getHost(), getLockOpticDetectionRange()))
							if (e.getAiProtocol().getLockOn() != null && e.getAiProtocol().getLockOn().isAlive()) // lockt
								possibleLocks.add(e.getAiProtocol().getLockOn());
				}
			}

			if (possibleLocks.size() == 0)
				return false;

			setLockOn(possibleLocks.get(rand(possibleLocks.size())));
			return true;
		case LOCK_LOCK_OF_LINKED_FRIENDS:
			// Was von was verknüpftem holen

			if (linkedAllies == null) // NullPointer
				throw new MissingShipLinkageException("linked allies is null");
			if (linkedAllies.size() == 0)
				return false; // nix da zum locken

			ArrayList<CombatObject> possibleLocks1 = new ArrayList<CombatObject>();
			for (CombatObject e : linkedAllies)
				if (e instanceof Enemy)
					if (((Enemy) e).getAiProtocol().getLockOn() != null && e.isAlive()
							&& ((Enemy) e).getAiProtocol().getLockOn().isAlive())
						possibleLocks1.add(((Enemy) e).getAiProtocol().getLockOn());

			if (possibleLocks1.size() == 0)
				return false;

			setLockOn(possibleLocks1.get(rand(possibleLocks1.size())));
			return true;
		case LOCK_LOCK_OF_LINKED_FRIENDS_INRANGE:
			// Was von was verknüpftem holen was in range ist

			if (linkedAllies == null) // NullPointer
				throw new MissingShipLinkageException("linked allies is null");
			if (linkedAllies.size() == 0)
				return false; // nix da zum locken

			ArrayList<CombatObject> possibleLocks2 = new ArrayList<CombatObject>();
			for (CombatObject o : linkedAllies)
				if (o instanceof Enemy) {
					Enemy e = (Enemy) o;
					if (e.isInRange(getHost(), getLockOpticDetectionRange()) && e.getAiProtocol().getLockOn() != null
							&& e.getAiProtocol().getLockOn().isAlive())
						possibleLocks2.add(e.getAiProtocol().getLockOn());
				}

			if (possibleLocks2.size() == 0)
				return false;

			setLockOn(possibleLocks2.get(rand(possibleLocks2.size())));
			return true;
		case NO_AUTO_LOCK:
			return false;
		default: // Gibts nicht
			return false;
		}

	}

	public void preLock(ArrayList<CombatObject> enemys) {

		ArrayList<CombatObject> possibleLocks = new ArrayList<CombatObject>();

		for (CombatObject e : enemys) {

			// Muss Enemy sein. Sonst invalid
			if (e.isFriend() != getHost().isFriend())
				possibleLocks.add(e);
		}

		if (!getHost().isFriend()) // Falls Gengner, Spieler auch lockbar
			possibleLocks.add(MainZap.getPlayer());

		if (possibleLocks.size() == 0)
			return; // nix da zum locken

		CombatObject lock = possibleLocks.get(rand(possibleLocks.size()));
		setLockOn(lock);
		getHost().setShootingAim(lock);
	}

	// Lock-On aus der Umgebung fischen
	public CombatObject searchForLock() {

		ArrayList<CombatObject> possibleLocks = new ArrayList<CombatObject>();

		if (!getHost().isFriend()) {
			if (getHost().distanceToPlayer() <= super.getLockOpticDetectionRange() && MainZap.getPlayer().isAlive()) {
				if (isPrioritisePlayerAsLockOn())
					return MainZap.getPlayer();
				possibleLocks.add(MainZap.getPlayer());
			}
		}

		// Umgebung holen
		ArrayList<Enemy> surrounding = MainZap.getGrid().getEnemySurrounding(getHost().getLocX(), getHost().getLocY(),
				super.getLockOpticDetectionRange());

		// Umgebung abgehen
		for (Enemy e : surrounding) {

			if (e.isFriend() == getHost().isFriend() || !e.isAlive())
				continue; // Keinen der eigenen Sippschaft oder Tote anvisieren

			// Da is was in Range
			if (e.isInRange(getHost(), super.getLockOpticDetectionRange()))
				possibleLocks.add(e);
		}

		if (possibleLocks.size() == 0)
			return null; // nix da zum locken

		return possibleLocks.get(rand(possibleLocks.size()));
	}

	// ------------

	// ----- Anchor ------------------------------

	// Updated den Anker und gibt TRUE zurück, wenn bewegende Aktionen
	// vorgenommen wurden
	private boolean updateAnchor() {

		if (!anchored)
			return false;

		if (ancCrowdEnabled) { // Ankerpunkt in das Armee-Zentrum verschieben
			if (linkedAllies == null)
				throw new MissingShipLinkageException("linked allies null");

			int totX = 0;
			int totY = 0;
			for (InteractiveObject e : linkedAllies) {
				totX += (int) e.getPosX();
				totY += (int) e.getPosY();
			}
			ancX = totX / linkedAllies.size();
			ancY = totY / linkedAllies.size();
		}

		if (ancInAction) {
			// Anker wird schon beansprucht.

			if (anchorReached()) {
				ancInAction = false;
				return false; // Angekommen. Aufheben.
			} else {
				move();
				return true;
			}

		} else { // Noch kein Anker in Aktion

			if (anchorCallNeeded()) { // Anker in Aktion benötigt
				ancInAction = true;
				ancAimX = ancX + rand(ancRadian * 2) - ancRadian;
				ancAimY = ancY + rand(ancRadian * 2) - ancRadian;
				getHost().getVelocity().aimFor(getHost().getLocX(), getHost().getLocY(), getHost().getSpeed(), ancAimX,
						ancAimY);
				move();
				return true;
			} else // Nix benötigt
				return false;

		}
	}

	@Override
	public void setDamageRecognizeable() {
		DamageCall call = new DamageCall() {

			@Override
			public void damage(CombatObject source, Projectile proj, int dmg) {

				// Zur Quelle gehen, falls lockon nicht schon vorhanden
				if (getLockOn() != null)
					return;

				if (anchored) // Im Anker-Prozess nicht umdrehen
					return;

				// Distanz prüfen
				if (!source.isInRange(getHost(), getLockPhysicalDetectionRange()))
					return;

				if (source == MainZap.getPlayer() && getHost().isFriend())
					return; // Friendly-Fire

				// Alle Bedingungen erfüllt
				if (isParked()) // Park-Bremse aufheben
					setParked(false);
				setLockOn(source);
				getHost().setShootingAim(source);
			}

		};

		addCall(KEY_CALL_GETTING_DAMAGED, call);
	}

	private boolean anchorCallNeeded() {

		if (Grid.distance(new Point(getHost().getLocX(), getHost().getLocY()), new Point(ancX, ancY)) < ancRange)
			return false; // Noch in Range

		if (rand(ancRandReturn) == 0)
			return true; // Trotz Out-Of-Range noch warten

		// Out of range und keine Ausnahme.
		return false;
	}

	private boolean anchorReached() {
		if (Grid.distance(new Point(getHost().getLocX(), getHost().getLocY()), new Point(ancX, ancY)) < ancRadian)
			return true; // Im Radius
		return false; // Nicht im Radius...
	}

	// Anker aufheben
	public void removeAnchor() {
		anchored = false;
		ancInAction = false;
	}

	// Anker setzen
	public void anchor(int x, int y, int ancRadian, int maxDistance, int returnRand) {
		ancX = x;
		ancY = y;
		ancRange = maxDistance;
		this.ancRadian = ancRadian;
		ancRandReturn = returnRand;
		anchored = true;
		ancInAction = false;
	}

	public void setEnableCrowdAnchor(boolean enable) {
		ancCrowdEnabled = enable;
	}

	// -------------

	protected void move() {
		getHost().moveX(getHost().getVelocity().getX());
		getHost().moveY(getHost().getVelocity().getY());
	}

	public ArrayList<CombatObject> getLinkedAllies() {
		return linkedAllies;
	}

	public void setLinkedAllies(ArrayList<CombatObject> linkedAllies) {
		this.linkedAllies = linkedAllies;
	}

	public ArrayList<CombatObject> getLinkedEnemys() {
		return linkedEnemys;
	}

	public void setLinkedEnemys(ArrayList<CombatObject> linkedEnemys) {
		this.linkedEnemys = linkedEnemys;
	}

	public int getAncX() {
		return ancX;
	}

	public void setAncX(int ancX) {
		this.ancX = ancX;
	}

	public int getAncY() {
		return ancY;
	}

	public void setAncY(int ancY) {
		this.ancY = ancY;
	}

	public int getAncRange() {
		return ancRange;
	}

	public void setAncRange(int ancRange) {
		this.ancRange = ancRange;
	}

	public int getAncRandReturn() {
		return ancRandReturn;
	}

	public void setAncRandReturn(int ancRandReturn) {
		this.ancRandReturn = ancRandReturn;
	}

	public int getAncRadian() {
		return ancRadian;
	}

	public void setAncRadian(int ancRadian) {
		this.ancRadian = ancRadian;
	}

	public int getAncAimX() {
		return ancAimX;
	}

	public void setAncAimX(int ancAimX) {
		this.ancAimX = ancAimX;
	}

	public int getAncAimY() {
		return ancAimY;
	}

	public void setAncAimY(int ancAimY) {
		this.ancAimY = ancAimY;
	}

	public FindLockAction getLockAction() {
		return lockAction;
	}

	public void setLockAction(FindLockAction lockAction) {
		this.lockAction = lockAction;
	}

	public Rectangle getMovementBounds() {
		return movementBounds;
	}

	public void setMovementBounds(Rectangle movementBounds) {
		this.movementBounds = movementBounds;
	}

	public int getLockCombatFreeMovementRange() {
		return lockCombatFreeMovementRange;
	}

	public void setLockCombatFreeMovementRange(int lockCombatFreeMovementRange) {
		this.lockCombatFreeMovementRange = lockCombatFreeMovementRange;
	}

	public float getCombatRangePerOutOfRangeRange() {
		return combatRangePerOutOfRangeRange;
	}

	public void setCombatRangePerOutOfRangeRange(float combatRangePerOutOfRangeRange) {
		this.combatRangePerOutOfRangeRange = combatRangePerOutOfRangeRange;
	}

	public boolean isAnchored() {
		return anchored;
	}

	public boolean isAncInAction() {
		return ancInAction;
	}

	public boolean isAncCrowdEnabled() {
		return ancCrowdEnabled;
	}

	@Override
	public Object getClone() {
		return new AdvancedSingleProtocol();
	}

}
