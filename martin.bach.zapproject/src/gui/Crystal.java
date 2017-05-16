package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import battle.stage.StageManager;
import corecase.MainZap;
import gui.effect.ExplosionEffect;
import io.TextureBuffer;
import lib.PaintingTask;
import lib.SpeedVector;
import lib.Updateable;

public class Crystal implements PaintingTask, Updateable {

	private static final float SPEED = 1000.0f;
	private static final int PLAYER_DISTANCE_TOLERANCE = 30;
	private static final int BREAKING_POINT_LARGENESS = 100;
	private static final int LARGE_CRYSTAL_VALUE = 50;
	public static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_CRYSTAL);
	private static final Color COLOR_MAIN = new Color(143, 39, 247, 184);
	private static final int HALFSIZE = 7;
	private static final int SIZE = 2 * HALFSIZE;
	private static final int HALFSIZE_LARGE = 21;
	private static final int SIZE_LARGE = 2 * HALFSIZE_LARGE;
	private static final int HALFSIZE_CORNERED = 12;
	private static final int SIZE_CORNERED = 2 * HALFSIZE_CORNERED;
	private static final int HALFSIZE_CORNERED_LARGE = 12;
	private static final int SIZE_CORNERED_LARGE = 2 * HALFSIZE_CORNERED_LARGE;

	private float posX;
	private float posY;
	private SpeedVector velocity = new SpeedVector(0, 0);
	private boolean large = false;

	public Crystal(float posX, float posY, boolean large) {
		super();
		this.posX = posX;
		this.posY = posY;
		this.large = large;
		MainZap.getMap().addPaintElement(this, true); // background
		MainZap.getMap().addUpdateElement(this);
		StageManager.getActiveStage().getPaintingTasks().add(this);
		StageManager.getActiveStage().getUpdateTasks().add(this);
	}

	@Override
	public void update() {

		if (MainZap.getPlayer().isInRange((int) posX, (int) posY, PLAYER_DISTANCE_TOLERANCE)) {
			if (large) {
				MainZap.setCrystals(MainZap.getCrystals() + LARGE_CRYSTAL_VALUE);
				Hud.pushCrystals(LARGE_CRYSTAL_VALUE);
			} else {
				MainZap.addCrystal();
				Hud.pushCrystals(1);
			}
			MainZap.getMap().removePaintElement(this, true); // background
			MainZap.getMap().removeUpdateElement(this);
			StageManager.getActiveStage().getPaintingTasks().remove(this);
			StageManager.getActiveStage().getUpdateTasks().remove(this);
			return; // Eingelocht
		}

		velocity.aimFor(posX, posY, SPEED / MainZap.getPlayer().distanceTo((int) posX, (int) posY),
				MainZap.getPlayer().getPosX(), MainZap.getPlayer().getPosY());
		posX += velocity.getX();
		posY += velocity.getY();

	}

	@Override
	public void paint(Graphics2D g) {

		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}

		int dx = (int) posX;
		int dy = (int) posY;

		g.translate(dx, dy);

		if (MainZap.fancyGraphics) {

			if (MainZap.generalAntialize) {
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
				if (large)
					g.drawImage(TEXTURE, -HALFSIZE_LARGE, -HALFSIZE_LARGE, SIZE_LARGE, SIZE_LARGE, null);
				else
					g.drawImage(TEXTURE, -HALFSIZE, -HALFSIZE, SIZE, SIZE, null);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			} else {
				g.drawImage(TEXTURE, -HALFSIZE, -HALFSIZE, SIZE, SIZE, null);
			}

		} else {
			g.setColor(COLOR_MAIN);
			if (large)
				g.fillRect(-HALFSIZE_CORNERED_LARGE, HALFSIZE_CORNERED_LARGE, SIZE_CORNERED_LARGE, SIZE_LARGE);
			else
				g.fillRect(-HALFSIZE_CORNERED, HALFSIZE_CORNERED, SIZE_CORNERED, SIZE);
		}

		g.translate(-dx, -dy);

		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}

	}

	public static void spawn(int x, int y, int amount, int range) {

		if (amount >= BREAKING_POINT_LARGENESS) {
			int amountLarge = amount / LARGE_CRYSTAL_VALUE;
			int amountSmall = amount % LARGE_CRYSTAL_VALUE;

			for (int i = 0; i != amountLarge; i++) {
				int[] coors = ExplosionEffect.getRandCircleCoordinates(range);
				new Crystal(coors[0] + x, coors[1] + y, true);
			}

			for (int i = 0; i != amountSmall; i++) {
				int[] coors = ExplosionEffect.getRandCircleCoordinates(range);
				new Crystal(coors[0] + x, coors[1] + y, false);
			}

		} else

			for (int i = 0; i != amount; i++) {
				int[] coors = ExplosionEffect.getRandCircleCoordinates(range);
				new Crystal(coors[0] + x, coors[1] + y, false);
			}

	}

}
