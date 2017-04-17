package gui.shop.meta;

import java.awt.Color;

import battle.projectile.ProjectileDesign;

public class ProjDesignDeltaVI extends ProjectileDesign {

	private static final int RADIUS = 9;
	private static final Color COLOR = new Color(180, 4, 4);
	private static final boolean SQUARE = false;

	public ProjDesignDeltaVI() {
		super(RADIUS, SQUARE, COLOR);
	}

}
