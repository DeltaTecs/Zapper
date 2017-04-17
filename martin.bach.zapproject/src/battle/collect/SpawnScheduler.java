package battle.collect;

import java.awt.Rectangle;
import java.util.ArrayList;

import battle.ai.AiProtocol;
import battle.ai.DieCall;
import battle.enemy.Enemy;
import corecase.MainZap;
import gui.Map;
import ingameobjects.InteractiveObject;
import library.Updateable;

public class SpawnScheduler implements Updateable {

	private static final boolean MAKE_ENEMYS_ALWAYS_WARP_IN = true;

	private int maxAmount;
	private int amount;
	private int maxDelay;
	private int delay;
	private boolean spawningPacks;

	private Rectangle range;
	private PackType packType;
	private InteractiveObject objectPattern;
	private ArrayList<SpawnEvent> spawnEvents = new ArrayList<SpawnEvent>();

	public SpawnScheduler(int maxAmount, int delay, PackType type) {
		super();
		spawningPacks = true;
		this.maxAmount = maxAmount;
		amount = 0;
		this.maxDelay = delay;
		packType = type;
	}

	public SpawnScheduler(int maxAmount, int delay, Rectangle range, PackType type) {
		super();
		spawningPacks = true;
		this.maxAmount = maxAmount;
		this.maxDelay = delay;
		this.range = range;
		packType = type;
	}

	public SpawnScheduler(int maxAmount, int delay, InteractiveObject pattern) {
		super();
		spawningPacks = false;
		this.maxAmount = maxAmount;
		amount = 0;
		this.maxDelay = delay;
		objectPattern = pattern;
	}

	public SpawnScheduler(int maxAmount, int delay, Rectangle range, InteractiveObject pattern) {
		super();
		spawningPacks = false;
		this.maxAmount = maxAmount;
		this.maxDelay = delay;
		this.range = range;
		objectPattern = pattern;
	}

	@Override
	public void update() {

		if (amount >= maxAmount)
			return; // Maximum erreicht

		if (maxDelay == 0) { // instant-Spawner
			// Mehrere Spawnen
			pushSpawns();
		}

		if (delay > 0) {
			delay--; // Noch nicht
		} else {
			// Der Zeitpunkt ist gekommen
			delay = maxDelay;
			spawn();
		}

	}

	private void pushSpawns() {
		while (amount < maxAmount) {
			spawn();
		}
	}

	private void spawn() {
		int x, y;

		if (range != null) { // Begrenztes Spawn-Feld
			x = MainZap.rand(range.width) + range.x;
			y = MainZap.rand(range.height) + range.y;
		} else {
			x = MainZap.rand(Map.SIZE);
			y = MainZap.rand(Map.SIZE);
		}

		if (spawningPacks) { // nur Packs spawnen -> Typ != null
			CollectableSpawner.spawn(packType, x, y, amountUpdateRunnable);
		} else { // nur Objekte spawnen -> pattern != null
			InteractiveObject obj = (InteractiveObject) objectPattern.getClone();
			obj.setPosition(x, y);
			obj.register();
			if (obj instanceof Enemy) {
				if (MAKE_ENEMYS_ALWAYS_WARP_IN)
					((Enemy) obj).warpIn(); // Warp erwünscht und möglich
				((Enemy) obj).getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, amountUpdateCall);
				callAllSpawnEvents(x, y, (Enemy) obj);
			} else {
				callAllSpawnEvents(x, y, obj);
			}

		}

		amount++;
	}

	private final Runnable amountUpdateRunnable = new Runnable() {

		@Override
		public void run() {
			amount--;
		}

	};

	private final DieCall amountUpdateCall = new DieCall() {

		@Override
		public void die() {
			amount--;
		}

	};

	private void callAllSpawnEvents(int x, int y, Enemy e) {
		for (SpawnEvent ev : spawnEvents) {
			ev.spawn(x, y, e);
		}
	}

	private void callAllSpawnEvents(int x, int y, InteractiveObject o) {
		for (SpawnEvent ev : spawnEvents) {
			ev.spawn(x, y, o);
		}
	}

	public void lowerAmountCounter() {
		amount--;
	}

	public void addSpawnEvent(SpawnEvent ev) {
		spawnEvents.add(ev);
	}

	public ArrayList<SpawnEvent> getSpawnEvents() {
		return spawnEvents;
	}

}
