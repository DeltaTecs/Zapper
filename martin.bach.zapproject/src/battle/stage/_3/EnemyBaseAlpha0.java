package battle.stage._3;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import corecase.MainZap;
import io.TextureBuffer;
import lib.PaintingTask;

public class EnemyBaseAlpha0 implements PaintingTask {

	private static final float SCALE = 8.0f;
	private static final BufferedImage TEXTURE_ALIVE = TextureBuffer.get(TextureBuffer.NAME_ENEMYBASE_ALPHA0);
	private static final BufferedImage TEXTURE_DESTROYED = TextureBuffer.get(TextureBuffer.NAME_ENEMYBASE_ALPHA0_DES);
	private static final int IMAGE_SIZE_X = (int) (TEXTURE_ALIVE.getWidth() * SCALE);
	private static final int IMAGE_SIZE_Y = (int) (TEXTURE_ALIVE.getHeight() * SCALE);

	private int posX;
	private int posY;
	private int movedX;
	private int movedY;

	private EnemyBasecoreAlpha0 core;

	public EnemyBaseAlpha0(EnemyBasecoreAlpha0 core) {
		this.core = core;
	}

	@Override
	public void paint(Graphics2D g) {

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		g.translate(movedX, movedY);

		if (core.isAlive()) {
			g.drawImage(TEXTURE_ALIVE, 0, 0, IMAGE_SIZE_X, IMAGE_SIZE_Y, null);
		} else {
			g.drawImage(TEXTURE_DESTROYED, 0, 0, IMAGE_SIZE_X, IMAGE_SIZE_Y, null);
		}
		g.translate(-movedX, -movedY);

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

	}

	public void setPosition(int x, int y) {
		posX = x;
		posY = y;

		movedX = (int) (x - ((TEXTURE_ALIVE.getWidth() / 2) * SCALE));
		movedY = (int) (y - ((TEXTURE_ALIVE.getHeight() / 2) * SCALE));

	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

}
