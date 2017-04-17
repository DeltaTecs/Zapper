package gui.shop.meta;

import java.awt.Color;

import battle.projectile.ProjectileDesign;

public class ProjDesignAshslider extends ProjectileDesign {

	private static final int RADIUS = 10;
	private static final Color COLOR = new Color(140, 10, 10);
	private static final boolean SQUARE = false;

	public ProjDesignAshslider() {
		super(RADIUS, SQUARE, COLOR);
	}

}
