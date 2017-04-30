package gui.shop.meta;

import java.awt.Color;

import battle.projectile.ProjectileDesign;

public class ProjDesignFalconIII extends ProjectileDesign {

	private static final int RADIUS = 10;
	private static final Color COLOR = new Color(180, 10, 10);
	private static final boolean SQUARE = false;

	public ProjDesignFalconIII() {
		super(RADIUS, SQUARE, COLOR);
	}

}
