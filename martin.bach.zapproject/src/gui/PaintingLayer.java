package gui;

import java.awt.Graphics2D;

import lib.PaintingTask;
import lib.ScheduledList;

public class PaintingLayer {

	private ScheduledList<PaintingTask> tasks = new ScheduledList<PaintingTask>();

	public PaintingLayer() {
	}

	public void drawLayer(Graphics2D g) {

		// Vorab alles Updaten
		tasks.update();

		// Zeichnen
		for (PaintingTask task : tasks) {
			task.paint(g);
		}

	}

	public void addTask(PaintingTask task) {
		tasks.schedAdd(task);
	}

	public void removeTask(PaintingTask task) {
		tasks.schedRemove(task);
	}

}
