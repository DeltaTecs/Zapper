package schedule;

import java.util.ArrayList;
import java.util.HashMap;

import lib.ScheduledList;

// Alles was seit Initialisierung vergangen ist, wird bei start aufgeholt
public class ConstantUpdateLoop implements Runnable {

	private ScheduledList<Runnable> updateList = new ScheduledList<Runnable>();
	private ScheduledList<Runnable> pauseableUpdateList = new ScheduledList<Runnable>();

	private HashMap<Runnable, Long> scheduleList = new HashMap<Runnable, Long>();
	private ArrayList<Runnable> removeScheduleList = new ArrayList<Runnable>();
	private HashMap<Runnable, Long> addScheduleList = new HashMap<Runnable, Long>();

	private HashMap<Runnable, Long> pauseableScheduleList = new HashMap<Runnable, Long>();
	private ArrayList<Runnable> removePauseableScheduleList = new ArrayList<Runnable>();
	private HashMap<Runnable, Long> addPauseableScheduleList = new HashMap<Runnable, Long>();

	private boolean done = false;
	private boolean paused = false;

	private int timeBetweenFrames = 20; // Momentan: 50 FPS
	private int timeSinceLastFrame = timeBetweenFrames;
	private long lastDate = System.currentTimeMillis();
	private long tickTime = 0;
	private long pauseableTickTime = 0;

	public ConstantUpdateLoop() {
	}

	@Override
	public void run() {
		System.out.println("[info] UpdateThread started. DeltaTime = " + timeBetweenFrames + "ms");
		while (true) {

			timeSinceLastFrame += System.currentTimeMillis() - lastDate;
			lastDate = System.currentTimeMillis();

			if (timeSinceLastFrame >= timeBetweenFrames) {
				done = false;
				timeSinceLastFrame -= timeBetweenFrames;

				if (!paused) {

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

				for (Runnable r : updateList) {
					r.run();
				}

				for (Runnable r : scheduleList.keySet()) {
					if (scheduleList.get(r) == tickTime) {
						r.run();
						removeScheduleList.add(r);
					}
				}

				// GraphicCore.refresh(); // neuzeichnen
				// GameLayer.ci().repaint(); // nur für Shop, etc

				tickTime++;

				done = true;
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.err.println("ConstantUpdateLoop-Thread interrupted!");
				e.printStackTrace();
			}
		}

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

	public void setTimeBetweenFrame(int time) {
		timeBetweenFrames = time;
	}

	public int getTimeBetweenFrame() {
		return timeBetweenFrames;
	}

	public boolean isPaused() {
		return paused;
	}

	public int getTimeSinceLastFrame() {
		return timeSinceLastFrame;
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

	public int inTicks(int ms) {
		return ms / timeBetweenFrames;
	}

	public boolean isDone() {
		return done;
	}

}
