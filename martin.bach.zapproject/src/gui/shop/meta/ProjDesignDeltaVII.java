package gui.shop.meta;

import java.awt.Color;

import battle.projectile.ProjectileDesign;

public class ProjDesignDeltaVII extends ProjectileDesign {

	private static final int RADIUS = 10;
	private static final Color COLOR = new Color(120, 4, 4);
	private static final boolean SQUARE = false;

	public ProjDesignDeltaVII() {
		super(RADIUS, SQUARE, COLOR);
	}

}
