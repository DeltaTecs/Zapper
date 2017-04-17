package battle.projectile;

import java.awt.Color;

public class ProjectileDesign {

	private int size;
	private boolean square;
	private Color color;

	public ProjectileDesign(int size, boolean square, Color color) {
		super();
		this.size = size;
		this.square = square;
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public boolean isSquare() {
		return square;
	}

	public Color getColor() {
		return color;
	}

}
