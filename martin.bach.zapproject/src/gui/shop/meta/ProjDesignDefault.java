package gui.shop.meta;

import java.awt.Color;

import battle.projectile.ProjectileDesign;

public class ProjDesignDefault extends ProjectileDesign {

	private static final int RADIUS = 9;
	private static final Color COLOR = new Color(200, 10, 10);
	private static final boolean SQUARE = false;

	public ProjDesignDefault() {
		super(RADIUS, SQUARE, COLOR);
	}

}
