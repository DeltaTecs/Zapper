package battle.stage._7;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import battle.enemy.Enemy;
import battle.stage.Stage;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;
import lib.PaintingTask;

public class EnemyBasecoreRaider extends Enemy {

	private static final double ROTATION_SPEED = -0.00035;
	private static final int HIT_RANGE = 140;
	private static final float SCALE = 2.0f;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYBASECORE_RAIDER);
	private static final BufferedImage TEXTURE_DES = TextureBuffer.get(TextureBuffer.NAME_ENEMYBASECORE_RAIDER_DES);
	private static final int MAX_HP = 40000;
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(120, 1500);
	private static final int SCORE = 200;
	private static final int CRYSTALS = 2200;
	private static final CollisionInformation COLLISION_INFORMATION = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final boolean FRIEND = false;

	private double rotation = 0;
	private boolean alive = true;
	private Stage stage;

	public EnemyBasecoreRaider(Stage stage) {
		super(0, 0, 0, TEXTURE, SCALE, COLLISION_INFORMATION, null, null, MAX_HP, EXPL_PATTERN, SCORE, 0, CRYSTALS,
				FRIEND);
		this.stage = stage;
	}

	@Override
	public void paint(Graphics2D g) {

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		super.paint(g);

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	}

	private final PaintingTask PAINTING_TASK_DESTROYED = new PaintingTask() {
		@Override
		public void paint(Graphics2D g) {
			if (MainZap.generalAntialize)
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

			g.drawImage(TEXTURE_DES, (int) (getPosX() - ((TEXTURE_DES.getWidth() * SCALE) * 0.5f)),
					(int) (getPosY() - ((TEXTURE_DES.getHeight() * SCALE) * 0.5f)),
					(int) (TEXTURE_DES.getWidth() * SCALE), (int) (TEXTURE_DES.getHeight() * SCALE), null);

			if (MainZap.generalAntialize)
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
	};

	@Override
	public void die() {
		super.die();
		alive = false;
		stage.getPaintingTasks().add(PAINTING_TASK_DESTROYED);
		stage.pass();
		MainZap.getMap().addPaintElement(PAINTING_TASK_DESTROYED, false);
	}

	@Override
	public void update() {

		if (!alive)
			return;

		super.update();

		// Rorations-Update
		rotation += ROTATION_SPEED;
		if (rotation >= Math.PI * 2)
			rotation -= Math.PI * 2;

		super.setRotation(rotation);

	}

	public boolean isAlive() {
		return alive;
	}

}
