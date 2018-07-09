package battle.looting;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import battle.stage.Stage;
import corecase.MainZap;
import gui.Map;
import io.TextureBuffer;
import lib.PaintingTask;

public class Container {

	private Rock rock;
	private Storage storage;
	private int x, y;

	public Container(int x, int y) {
		rock = new Rock();
		storage = new Storage(x, y);
		rock.setPosition(x, y);
		this.x = x;
		this.y = y;
	}

	public void register(Stage s) {
		s.getPaintingTasks().add(rock);
		MainZap.getMap().addPaintElement(rock, true);
		storage.register();
	}

	public static Container spawn(Stage stage) {

		final Rectangle forbiddenZone = new Rectangle(Map.SIZE / 3, Map.SIZE / 3, Map.SIZE / 3, 2 * Map.SIZE / 3);

		int x = MainZap.rand(Map.SIZE - 200) + 100;
		int y = MainZap.rand(Map.SIZE - 200) + 100;
		while (forbiddenZone.contains(x, y)) {
			x = MainZap.rand(Map.SIZE - 200) + 100;
			y = MainZap.rand(Map.SIZE - 200) + 100;
		}

		Container c = new Container(x, y);
		c.register(stage);

		return c;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Storage getStorage() {
		return storage;
	}

}

class Rock implements PaintingTask {

	private static final float SCALE = 2.0f;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_STRUCTURE_ROCK1);
	private static final int IMAGE_SIZE_X = (int) (TEXTURE.getWidth() * SCALE);
	private static final int IMAGE_SIZE_Y = (int) (TEXTURE.getHeight() * SCALE);

	private int posX;
	private int posY;
	private int movedX;
	private int movedY;

	public Rock() {
	}

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
