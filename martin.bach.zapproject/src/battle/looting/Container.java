package battle.looting;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import battle.enemy.Enemy;
import battle.projectile.Projectile;
import battle.stage.Stage;
import battle.stage.StageManager;
import collision.Collideable;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.Crystal;
import gui.Map;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;
import lib.PaintingTask;

public class Container {

	private Rock rock;
	private Storage storage;

	public Container(int x, int y) {
		rock = new Rock();
		storage = new Storage(x, y);
		rock.setPosition(x, y);
	}

	public void register(Stage s) {
		s.getPaintingTasks().add(rock);
		MainZap.getMap().addPaintElement(rock, true);
		storage.register();
	}

	public static void spawn(Stage stage) {

		final Rectangle forbiddenZone = new Rectangle(Map.SIZE / 3, Map.SIZE / 3, Map.SIZE / 3, 2 * Map.SIZE / 3);

		int x = MainZap.rand(Map.SIZE - 200) + 100;
		int y = MainZap.rand(Map.SIZE - 200) + 100;
		while (forbiddenZone.contains(x, y)) {
			x = MainZap.rand(Map.SIZE - 200) + 100;
			y = MainZap.rand(Map.SIZE - 200) + 100;
		}

		Container c = new Container(x, y);
		c.register(stage);

		System.out.println("container at " + x + " : " + y);
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

class Storage extends Enemy {

	private static final float SCALE = 2.0f;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_CONTAINER);
	private static final int HEALTH = 600;
	private static final int MIN_CRYSTALS_PER_STAGE = 30;
	private static final int MAX_CRYSTALS_PER_STAGE = 80;
	

	public Storage(int posX, int posY) {
		super(posX, posY, 0, TEXTURE, SCALE, new CollisionInformation(30, CollisionType.COLLIDE_WITH_FRIENDS, false),
				null, null, HEALTH, new ExplosionEffectPattern(5, 50), 0, 0, 0, false, null);
	}
	
	@Override
	public void die() {
		super.die();
		
		int crystalAmount = MainZap.rand(MAX_CRYSTALS_PER_STAGE - MIN_CRYSTALS_PER_STAGE) + MIN_CRYSTALS_PER_STAGE;
		crystalAmount *= StageManager.getActiveStage().getLvl() == 0 ? 1 : StageManager.getActiveStage().getLvl();
		Crystal.spawn(this.getLocX(), this.getLocY(), crystalAmount, 100);
	}

	@Override
	public void collide(Collideable c) {
		if (!(c instanceof Projectile))
			return;
		if (((Projectile) c).getSender() == MainZap.getPlayer())
			super.collide(c);
	}

	@Override
	public void paint(Graphics2D g) {
		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		super.paint(g);

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	}

}