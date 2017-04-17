package corecase;

import java.awt.Color;

import battle.projectile.ProjectileDesign;

public class DopeSchiffProjectileDesign extends ProjectileDesign {

	private static final int SIZE = 10;
	private static final boolean SQUARE = false;
	private static final Color COLOR = new Color(20, 20, 140);

	public DopeSchiffProjectileDesign() {
		super(SIZE, SQUARE, COLOR);
	}

}
