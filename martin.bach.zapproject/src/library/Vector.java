package library;

public class Vector {

	private double x = 0;
	private double y = 0;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void multiply(double m) {
		x = x * m;
		y = y * m;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	// Auf eine Lokalisation zeigen
	public void aimFor(double currentLocX, double currentLocY, double speed, double aimingLocX, double aimingLocY) {

		x = aimingLocX - currentLocX;
		y = aimingLocY - currentLocY;

		normalize();

		x *= speed;
		y *= speed;

	}

	public void aimFor(double currentLocX, double currentLocY, double aimingLocX, double aimingLocY) {

		double sum = Math.abs(x) + Math.abs(y);
		x = aimingLocX - currentLocX;
		y = aimingLocY - currentLocY;

		normalize();

		x *= sum;
		y *= sum;

	}

	public void normalize() {
		double sum = Math.abs(x) + Math.abs(y);
		x = x /= sum;
		y = y /= sum;
	}

	public void turnAround() {
		x = x * -1;
		y = y * -1;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Returns the sum of x and y
	 */
	public double getSpeed() {
		return x + y;
	}

	public void invertX() {
		x = x * -1;
	}

	public void invertY() {
		y = y * -1;
	}
}
