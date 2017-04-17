package collision;

import java.util.ArrayList;

public class Tile {

	// Weite: 250.
	private int x;
	private int y;
	private int width;
	private int height;

	private ArrayList<Collideable> objects = new ArrayList<Collideable>();

	public Tile(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void addObject(Collideable c) {
		objects.add(c);
	}

	public void removeObject(Collideable c) {
		objects.remove(c);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean contains(int x1, int y1) {
		if ((x <= x1) && ((x + width) > x1) && (y <= y1) && ((y + height) > y1)) {
			return true;
		}
		return false;
	}

	public ArrayList<Collideable> getObjects() {
		return objects;
	}

}
