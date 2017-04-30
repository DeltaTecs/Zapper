package gui.shop.meta;

import java.awt.Color;

import battle.projectile.ProjectileDesign;

public class ProjDesignDarkPerl extends ProjectileDesign {

	private static final int RADIUS = 11;
	private static final Color COLOR = new Color(120, 10, 10);
	private static final boolean SQUARE = false;

	public ProjDesignDarkPerl() {
		super(RADIUS, SQUARE, COLOR);
	}

}
