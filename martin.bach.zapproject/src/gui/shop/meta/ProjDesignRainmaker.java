package gui.shop.meta;

import java.awt.Color;

import battle.projectile.ProjectileDesign;

public class ProjDesignRainmaker extends ProjectileDesign {

	private static final int RADIUS = 13;
	private static final Color COLOR = new Color(247, 0, 247);
	private static final boolean SQUARE = false;

	public ProjDesignRainmaker() {
		super(RADIUS, SQUARE, COLOR);
	}

}
