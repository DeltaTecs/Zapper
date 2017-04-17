package gui.effect;

import java.awt.Graphics2D;

import corecase.MainZap;
import library.PaintingTask;
import library.Updateable;

public class Effect implements PaintingTask, Updateable {

	private int totalDuration;
	private int duration;

	public Effect(int duration) {
		this.duration = duration;
		totalDuration = duration;
	}

	public void register() {
		MainZap.getMap().addPaintElement(this, false); // foreground
		MainZap.getMap().addUpdateElement(this);
	}

	public void unRegister() {
		MainZap.getMap().removePaintElement(this, false); // foreground
		MainZap.getMap().removeUpdateElement(this);
	}

	@Override
	public void update() {

		if (duration == 0) {
			unRegister();
		} else {
			duration--;
		}
	}

	@Override
	public void paint(Graphics2D g) {

	}

	public boolean isFinished() {
		return duration == 0;
	}

	public int getTotalDuration() {
		return totalDuration;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
