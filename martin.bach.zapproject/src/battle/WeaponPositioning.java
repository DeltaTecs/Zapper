package battle;

import java.awt.Point;

public class WeaponPositioning {

	private byte weapons;
	private int[] corex;
	private int[] corey;

	/**
	 * Speichert die KernPositionen und berechnet bei Bedarf
	 * Rotations-Positionen. Werte in Abhänigkeit vom Schiffszentrum.
	 * 
	 * @param weapons
	 * @param corex
	 * @param corey
	 */
	public WeaponPositioning(byte weapons, int[] corex, int[] corey) {
		super();
		this.weapons = weapons;
		this.corex = corex;
		this.corey = corey;
	}

	public Point[] getRotated(float angle) {

		Point[] res = new Point[weapons];
		for (int i = 0; i != weapons; i++) {

			res[i] = new Point((int) ((corex[i] * Math.cos(angle)) - (corey[i] * Math.sin(angle))),
					(int) ((corex[i] * Math.sin(angle)) + (corey[i] * Math.cos(angle))));

		}

		return res;
	}

	public Point getRotated(float angle, int index) {

		return new Point((int) ((corex[index] * Math.cos(angle)) - (corey[index] * Math.sin(angle))),
				(int) ((corex[index] * Math.sin(angle)) + (corey[index] * Math.cos(angle))));
	}

	public static Point rotate(float angle, Point base) {
		return new Point((int) ((base.x * Math.cos(angle)) - (base.y * Math.sin(angle))),
				(int) ((base.x * Math.sin(angle)) + (base.y * Math.cos(angle))));
	}

	public byte getWeaponAmount() {
		return weapons;
	}

	public int[] getCorex() {
		return corex;
	}

	public int[] getCorey() {
		return corey;
	}

}
