package lib;

public class SpeedVector {

	private float x = 0;
	private float y = 0;

	/**
	 * Eine schnelle Version der Vectors. Arbeitet mit Float statt Double
	 * 
	 * @param x
	 * @param y
	 */
	public SpeedVector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public SpeedVector() {
	}

	public void multiply(float m) {
		x = x * m;
		y = y * m;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	// Auf eine Lokalisation zeigen
	public void aimFor(float currentLocX, float currentLocY, float speed, float aimingLocX, float aimingLocY) {

		x = aimingLocX - currentLocX;
		y = aimingLocY - currentLocY;

		normalize();

		x *= speed;
		y *= speed;

	}

	public void aimFor(float currentLocX, float currentLocY, float aimingLocX, float aimingLocY) {

		float sum = Math.abs(x) + Math.abs(y);
		x = aimingLocX - currentLocX;
		y = aimingLocY - currentLocY;

		normalize();

		x *= sum;
		y *= sum;

	}

	public void normalize() {
		float sum = Math.abs(x) + Math.abs(y);
		x = x /= sum;
		y = y /= sum;
	}

	public double angle() {
		return (Math.PI * 0.5f) - Math.atan(y / x);
	}

	/**
	 * Lässt einen aktuellen Vektor sich dem anderen annähern.
	 * 
	 * @param aim
	 *            Perfekter, angestrebter Vektor
	 * @param current
	 *            Jetziger Vektor
	 * @param force
	 *            Stärke der Annäherung, pro Aufruf (0 < force < 1)
	 * @param speed
	 *            Geschwindigkeit des End-Vektors
	 * @return Den angenäherten Vektor
	 */
	public static SpeedVector equalize(SpeedVector aim, SpeedVector current, float force, float speed) {

		float addX = current.getX() + aim.getX() * force;
		float addY = current.getY() + aim.getY() * force;

		current.aimFor(0, 0, speed, addX, addY);

		return current;
	}

	public void turnAround() {
		x = x * -1;
		y = y * -1;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Simple addition x = x1 + x2; y = y1 + y2
	 * 
	 * @param v
	 */
	public void add(SpeedVector v) {
		x += v.getX();
		y += v.getY();
	}

	/**
	 * Returns the sum of x and y
	 */
	public float getSpeed() {
		return x + y;
	}

	public void invertX() {
		x = x * -1;
	}

	public void invertY() {
		y = y * -1;
	}

}
