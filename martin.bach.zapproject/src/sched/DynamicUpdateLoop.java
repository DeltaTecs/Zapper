package sched;

import java.util.ArrayList;
import java.util.HashMap;

import corecase.Cmd;
import lib.ScheduledList;

public class DynamicUpdateLoop implements Runnable {

	private static final int NANO_MULTIPIER = 1000000;
	private static final int MAX_COMPENSABLE_RUNTIME = 400 * NANO_MULTIPIER;
	private static final int BOOST_CONSTANT = 400000;

	private ScheduledList<Runnable> updateList = new ScheduledList<Runnable>();
	private ScheduledList<Runnable> pauseableUpdateList = new ScheduledList<Runnable>();

	private HashMap<Runnable, Long> scheduleList = new HashMap<Runnable, Long>();
	private ArrayList<Runnable> removeScheduleList = new ArrayList<Runnable>();
	private HashMap<Runnable, Long> addScheduleList = new HashMap<Runnable, Long>();

	private HashMap<Runnable, Long> pauseableScheduleList = new HashMap<Runnable, Long>();
	private ArrayList<Runnable> removePauseableScheduleList = new ArrayList<Runnable>();
	private HashMap<Runnable, Long> addPauseableScheduleList = new HashMap<Runnable, Long>();

	private boolean paused = false;
	private boolean runningOnFreeWheel = false;
	private long tickTime = 0;
	private long pauseableTickTime = 0;
	private long timeBetweenFrames = 14 * NANO_MULTIPIER; // Momentan: 71 FPS
	private long booster = 0;

	private String name;

	public DynamicUpdateLoop(String name) {
		super();
		this.name = name;
	}

	@Override
	public void run() {
		Cmd.print(name + " started. DeltaTime = " + (timeBetweenFrames / NANO_MULTIPIER) + "ms" + " -> "
				+ (1000 / (timeBetweenFrames / NANO_MULTIPIER)) + " FPS");

		long uncompensedRuntime = 0; // Nanos!!

		while (true) {

			long startTime = System.nanoTime(); // Nanos!!

			if (!runningOnFreeWheel)
				updateProgress();

			uncompensedRuntime += (System.nanoTime() - startTime) + BOOST_CONSTANT;

			if (uncompensedRuntime > MAX_COMPENSABLE_RUNTIME) {
				Cmd.print("[Warn] Overload or System-Time-Change in " + name + ". Skipping "
						+ (uncompensedRuntime / (timeBetweenFrames - booster)) + " ticks");
				uncompensedRuntime = 0;
			} else if (uncompensedRuntime >= timeBetweenFrames - booster) {
				uncompensedRuntime -= timeBetweenFrames - booster;
				continue;
			}

			try {

				double totalMs = (double) (timeBetweenFrames - uncompensedRuntime - booster) / NANO_MULTIPIER;
				int msOnly = (int) (totalMs);
				int nanoOnly = (int) ((totalMs - msOnly) * NANO_MULTIPIER);

				Thread.sleep(msOnly, nanoOnly);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			uncompensedRuntime = 0;

		}

	}

	private void updateProgress() {
		if (!paused) {

			// Sync, da multithread clear -> ConcurrentMod
			// -> warten bis an der Reihe
			synchronized (pauseableUpdateList) {

				updateLists();

				// berechnen
				for (Runnable r : pauseableUpdateList) {
					r.run();
				}
				for (Runnable r : pauseableScheduleList.keySet()) {
					if (pauseableScheduleList.get(r) == pauseableTickTime) {
						r.run();
						removePauseableScheduleList.add(r);
					}
				}

				pauseableTickTime++;
			}

		}

		for (Runnable r : updateList) {
			r.run();
		}

		for (Runnable r : scheduleList.keySet()) {
			if (scheduleList.get(r) <= tickTime) {
				r.run();
				removeScheduleList.add(r);
			}
		}
		tickTime++;
	}

	private void updateLists() {

		updateList.update();
		pauseableUpdateList.update();

		for (Runnable r : removeScheduleList) {
			scheduleList.remove(r);
		}
		for (Runnable r : addScheduleList.keySet()) {
			scheduleList.put(r, addScheduleList.get(r));
		}
		removeScheduleList.clear();
		addScheduleList.clear();

		for (Runnable r : removePauseableScheduleList) {
			pauseableScheduleList.remove(r);
		}
		for (Runnable r : addPauseableScheduleList.keySet()) {
			pauseableScheduleList.put(r, addPauseableScheduleList.get(r));
		}
		removePauseableScheduleList.clear();
		addPauseableScheduleList.clear();
	}

	public void addTask(Runnable run, boolean pauseable) {
		if (pauseable) {
			pauseableUpdateList.schedAdd(run);
		} else {
			updateList.schedAdd(run);
		}
	}

	public void removeTask(Runnable run, boolean pauseable) {
		if (pauseable) {
			pauseableUpdateList.schedRemove(run);
		} else {
			updateList.schedRemove(run);
		}
	}

	public void clear() {
		synchronized (pauseableUpdateList) {
			scheduleList.clear();
			pauseableScheduleList.clear();
			updateList.clear();
			pauseableUpdateList.clear();
			removeScheduleList.clear();
			addScheduleList.clear();
			removePauseableScheduleList.clear();
			addPauseableScheduleList.clear();
		}
	}

	public long getTimeBetweenFrames() {
		return timeBetweenFrames;
	}

	public int getTimeBetweenFramesMS() {
		return (int) (timeBetweenFrames / NANO_MULTIPIER);
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void scheduleTask(Runnable task, long ticks, boolean unpauseable) {
		if (unpauseable) {
			addScheduleList.put(task, tickTime + ticks);
		} else {
			addPauseableScheduleList.put(task, pauseableTickTime + ticks);
		}
	}

	public int inTicks(long ms) {
		return (int) ((ms * NANO_MULTIPIER) / timeBetweenFrames);
	}

	public long getBooster() {
		return booster;
	}

	public void setBooster(int boosterMS) {
		long boosterNano = boosterMS * NANO_MULTIPIER ;
		if (boosterNano > timeBetweenFrames) {
			System.err.println("[Err] Loop-Booster stärker als Zeit-Delta");
			return;
		}
		this.booster = boosterNano;
	}

	public void pushUpdate() {
		updateProgress();
	}

	public void setTimeBetweenFrames(int timeBetweenFramesMS) {
		this.timeBetweenFrames = timeBetweenFramesMS * NANO_MULTIPIER;
	}

	public boolean isRunningOnFreeWheel() {
		return runningOnFreeWheel;
	}

	public void setRunningOnFreeWheel(boolean runningOnFreeWheel) {
		this.runningOnFreeWheel = runningOnFreeWheel;
	}

}
