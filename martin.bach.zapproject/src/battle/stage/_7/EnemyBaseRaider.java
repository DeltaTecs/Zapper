package battle.stage._7;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import corecase.MainZap;
import io.TextureBuffer;
import lib.PaintingTask;

public class EnemyBaseRaider implements PaintingTask {

	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYBASE_RAIDER);
	private static final float SCALE = 6.0f;
	private static final int IMAGE_SIZE_X = (int) (TEXTURE.getWidth() * SCALE);
	private static final int IMAGE_SIZE_Y = (int) (TEXTURE.getHeight() * SCALE);

	
	private int posX;
	private int posY;
	private int movedX;
	private int movedY;

	@Override
	public void paint(Graphics2D g) {
		
		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		g.translate(movedX, movedY);
		g.drawImage(TEXTURE, 0, 0, IMAGE_SIZE_X, IMAGE_SIZE_Y, null);
		g.translate(-movedX, -movedY);

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

	}
	
	public void setPosition(int x, int y) {
		posX = x;
		posY = y;

		movedX = (int) (x - ((TEXTURE.getWidth() / 2) * SCALE));
		movedY = (int) (y - ((TEXTURE.getHeight() / 2) * SCALE));
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

}
