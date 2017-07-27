package lib;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class RotateablePolygon {

	private Point2D[] base;
	private Point2D[] current;

	private Polygon polygon;

	private int rotateBaseX;
	private int rotateBaseY;

	private float posX = 0;
	private float posY = 0;

	public RotateablePolygon(Point2D[] base, int rotateBaseX, int rotateBaseY) {
		this.base = base;
		this.rotateBaseX = rotateBaseX;
		this.rotateBaseY = rotateBaseY;

		current = new Point2D[base.length];

		polygon = new Polygon();

		int c = 0;
		for (Point2D p : base) {
			polygon.addPoint((int) p.getX(), (int) p.getY());
			current[c] = new Point((int) p.getX(), (int) p.getY());
			c++;
		}

	}

	public RotateablePolygon(Rectangle rectBase, int rotateBaseX, int rotateBaseY) {
		this.base = recToPoint2D(rectBase);
		this.rotateBaseX = rotateBaseX;
		this.rotateBaseY = rotateBaseY;

		current = new Point2D[base.length];

		polygon = new Polygon();

		for (Point2D p : base) {
			polygon.addPoint((int) p.getX(), (int) p.getY());
		}

	}

	public void rotateByDegrees(float degrees) {
		AffineTransform.getRotateInstance(Math.toRadians(degrees), rotateBaseX, rotateBaseY).transform(base, 0, current,
				0, base.length);
		polygon = new Polygon();
		for (Point2D p : current) {
			polygon.addPoint((int) p.getX(), (int) p.getY());
		}
	}

	public void rotateByRadians(float radians) {
		AffineTransform.getRotateInstance(radians, rotateBaseX, rotateBaseY).transform(base, 0, current, 0,
				base.length);
		polygon = new Polygon();
		for (Point2D p : current) {
			polygon.addPoint((int) p.getX(), (int) p.getY());
		}
	}

	private Point2D[] recToPoint2D(Rectangle r) {
		return new Point2D[] { new Point(r.x, r.y), new Point(r.x + r.width, r.y),
				new Point(r.x + r.width, r.y + r.height), new Point(r.x, r.y + r.height) };
	}

	public Point2D[] getBase() {
		return base;
	}

	public void setBase(Point2D[] base) {
		this.base = base;
	}

	public Point2D[] getCurrent() {
		return current;
	}

	public void setCurrent(Point2D[] current) {
		this.current = current;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public int getRotateBaseX() {
		return rotateBaseX;
	}

	public void setRotateBaseX(int rotateBaseX) {
		this.rotateBaseX = rotateBaseX;
	}

	public int getRotateBaseY() {
		return rotateBaseY;
	}

	public void setRotateBaseY(int rotateBaseY) {
		this.rotateBaseY = rotateBaseY;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public void setPosition(float x, float y) {
		posX = x;
		posY = y;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

}
