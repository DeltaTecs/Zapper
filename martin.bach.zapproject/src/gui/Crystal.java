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
	public static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_CRYSTAL);
	private static final Color COLOR_MAIN = new Color(143, 39, 247, 184);
	private static final int HALFSIZE = 7;
	private static final int SIZE = 2 * HALFSIZE;
	private static final int HALFSIZE_CORNERED = 4;
	private static final int SIZE_CORNERED = 2 * HALFSIZE_CORNERED;

	private float posX;
	private float posY;
	private SpeedVector velocity = new SpeedVector(0, 0);

	public Crystal(float posX, float posY) {
		super();
		this.posX = posX;
		this.posY = posY;
		MainZap.getMap().addPaintElement(this, true); // background
		MainZap.getMap().addUpdateElement(this);
		StageManager.getActiveStage().getPaintingTasks().add(this);
		StageManager.getActiveStage().getUpdateTasks().add(this);
	}

	@Override
	public void update() {

		if (MainZap.getPlayer().isInRange((int) posX, (int) posY, PLAYER_DISTANCE_TOLERANCE)) {
			MainZap.addCrystal();
			Hud.pushCrystals();
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
				g.drawImage(TEXTURE, -HALFSIZE, -HALFSIZE, SIZE, SIZE, null);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			} else {
				g.drawImage(TEXTURE, -HALFSIZE, -HALFSIZE, SIZE, SIZE, null);
			}

		} else {
			g.setColor(COLOR_MAIN);
			g.fillRect(-HALFSIZE_CORNERED, HALFSIZE_CORNERED, SIZE_CORNERED, SIZE);
		}

		g.translate(-dx, -dy);

		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}

	}

	public static void spawn(int x, int y, int amount, int range) {

		for (int i = 0; i != amount; i++) {
			int[] coors = ExplosionEffect.getRandCircleCoordinates(range);
			new Crystal(coors[0] + x, coors[1] + y);
		}

	}

}
