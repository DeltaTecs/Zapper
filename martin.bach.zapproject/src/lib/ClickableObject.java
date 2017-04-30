package lib;

import java.awt.Rectangle;
import java.util.ArrayList;

public class ClickableObject {

	private Rectangle bounds;
	private ArrayList<ClickableObject> containings = new ArrayList<ClickableObject>();
	private ArrayList<ClickListener> clickListeners = new ArrayList<ClickListener>();
	private ArrayList<MotionListener> motionListeners = new ArrayList<MotionListener>();
	private boolean visible;

	public ClickableObject(int x, int y, int width, int height) {
		bounds = new Rectangle(x, y, width, height);
	}

	public ClickableObject(Rectangle bounds) {
		this.bounds = new Rectangle(bounds);
	}

	public void callRelease(int x, int y) {

		if (!visible || !bounds.contains(x, y))
			return; // Nicht sichtbar oder click nicht drin

		int dx = x - bounds.x;
		int dy = y - bounds.y;

		for (ClickableObject co : containings) {
			co.callRelease(x, y);
		}

		for (ClickListener cl : clickListeners) {
			cl.release(dx, dy);
		}
	}

	public void callPress(int x, int y) {

		if (!visible || !bounds.contains(x, y))
			return; // Nicht sichtbar oder click nicht drin

		int dx = x - bounds.x;
		int dy = y - bounds.y;

		for (ClickableObject co : containings) {
			co.callPress(x, y);
		}

		for (ClickListener cl : clickListeners) {
			cl.press(dx, dy);
		}
	}

	public void callMove(int x, int y) {
		

		if (!visible || !bounds.contains(x, y))
			return; // Nicht sichtbar oder click nicht drin

		int dx = x - bounds.x;
		int dy = y - bounds.y;

		for (ClickableObject co : containings) {
			co.callMove(x, y);
		}

		for (MotionListener ml : motionListeners) {
			ml.move(dx, dy);
		}
	}

	public void callDrag(int x, int y) {

		if (!visible || !bounds.contains(x, y))
			return; // Nicht sichtbar oder click nicht drin

		int dx = x - bounds.x;
		int dy = y - bounds.y;

		for (ClickableObject co : containings) {
			co.callDrag(x, y);
		}

		for (MotionListener ml : motionListeners) {
			ml.drag(dx, dy);
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void addClickListener(ClickListener cl) {
		clickListeners.add(cl);
	}

	public void removeClickListener(ClickListener cl) {
		clickListeners.remove(cl);
	}

	public void addMotionListener(MotionListener cl) {
		motionListeners.add(cl);
	}

	public void removeMotionListener(MotionListener cl) {
		motionListeners.remove(cl);
	}

	public void add(ClickableObject o) {
		containings.add(o);
	}

	public void remove(ClickableObject o) {
		containings.remove(o);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Rectangle getBounds() {
		return bounds;
	}

}
