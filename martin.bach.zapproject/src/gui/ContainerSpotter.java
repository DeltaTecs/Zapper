package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import battle.looting.Container;
import battle.stage.StageManager;
import corecase.MainZap;
import lib.SpeedVector;

public abstract class ContainerSpotter {

	private static final Color COLOR = new Color(92, 172, 138);
	private static final int RADIUS_DOT = 4;
	private static final int RADIUS_SCREEN = (int) (Frame.SIZE / 2.4);

	public static void paint(Graphics2D g) {

		for (Container c : StageManager.getActiveStage().getContainers()) {

			if (!c.getStorage().isAlive()) // schon gelutscht
				continue;

			SpeedVector v = new SpeedVector(c.getX() - MainZap.getPlayer().getLocX(),
					c.getY() - MainZap.getPlayer().getLocY());
			v.scaleToLength(RADIUS_SCREEN);
			
			g.setColor(COLOR);
			g.fillOval((int)((Frame.SIZE / 2) + v.getX()) - RADIUS_DOT, (int)((Frame.SIZE / 2) + v.getY()) - RADIUS_DOT, RADIUS_DOT * 2, RADIUS_DOT * 2);
		}

	}

}
