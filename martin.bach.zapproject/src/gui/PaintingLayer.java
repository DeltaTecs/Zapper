package gui;

import java.awt.Graphics2D;
import java.util.ArrayList;

import lib.ListContaining;
import lib.PaintingTask;
import lib.ScheduledList;

public class PaintingLayer implements ListContaining {

	private ScheduledList<PaintingTask> tasks = new ScheduledList<PaintingTask>();
	private ArrayList<ListContaining> updateable = new ArrayList<ListContaining>();

	public PaintingLayer() {
	}

	public void drawLayer(Graphics2D g) {

		// Vorab alles Updaten
		for (ListContaining o : updateable) {
			o.update();
		}
		tasks.update();

		// Zeichnen
		for (PaintingTask task : tasks) {
			task.paint(g);
		}

	}

	@Override
	public void update() {
		tasks.update();
	}

	public void addTask(PaintingTask task) {
		tasks.schedAdd(task);
		if (task instanceof ListContaining) {
			updateable.add((ListContaining) task);
		}
	}

	public void removeTask(PaintingTask task) {
		tasks.schedRemove(task);
		if (task instanceof ListContaining) {
			updateable.remove((ListContaining) task);
		}
	}

	public void addUpdateComponent(ListContaining o) {
		updateable.add(o);
	}

	public void removeUpdateComponent(ListContaining o) {
		updateable.remove(o);
	}

}
