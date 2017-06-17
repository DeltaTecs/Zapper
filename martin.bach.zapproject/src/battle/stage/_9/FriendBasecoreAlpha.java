package battle.stage._9;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import battle.enemy.Enemy;
import battle.stage.Stage;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import gui.screens.end.EndScreen;
import io.TextureBuffer;
import lib.PaintingTask;

public class FriendBasecoreAlpha extends Enemy {

	private static final double ROTATION_SPEED = 0.04;
	private static final double ROTATION_SPEED_REMOVAL = 1.004;
	private static final int HIT_RANGE = 70;
	private static final float SCALE = 3.5f;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_FRIENDBASECORE_ALPHA);
	private static final BufferedImage TEXTURE_DES = TextureBuffer.get(TextureBuffer.NAME_FRIENDBASECORE_ALPHA_DES);
	private static final int MAX_HP = 20000;
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(40, 200);
	private static final int SCORE = 0;
	private static final int CRYSTALS = 0;
	private static final CollisionInformation COLLISION_INFORMATION = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_WITH_ENEMYS, false);
	private static final boolean FRIEND = true;

	private double rotationSpeedDead = ROTATION_SPEED;
	private double rotation = 0;
	private boolean alive = true;
	private Stage stage;

	public FriendBasecoreAlpha(Stage stage) {
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

			double rot = rotation;
			g.transform(AffineTransform.getRotateInstance(rot, getPosX(), getPosY()));

			g.drawImage(TEXTURE_DES, (int) (getPosX() - ((TEXTURE_DES.getWidth() * SCALE) * 0.5f)),
					(int) (getPosY() - ((TEXTURE_DES.getHeight() * SCALE) * 0.5f)),
					(int) (TEXTURE_DES.getWidth() * SCALE), (int) (TEXTURE_DES.getHeight() * SCALE), null);
			g.transform(AffineTransform.getRotateInstance(-rot, getPosX(), getPosY()));
			if (MainZap.generalAntialize)
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
	};

	@Override
	public void die() {
		super.die();
		alive = false;
		stage.getPaintingTasks().add(PAINTING_TASK_DESTROYED);
		EndScreen.popUp("You failed.");
		MainZap.getMap().addPaintElement(PAINTING_TASK_DESTROYED, false);
		MainZap.getMap().addUpdateElement(this); // Ausdrehen
	}

	@Override
	public void update() {

		if (!alive) {
			// Nur Ausschwung-rotation

			if (rotationSpeedDead <= 0.001) {
				MainZap.getMap().removeUpdateElement(this);
				return;
			}

			rotationSpeedDead /= ROTATION_SPEED_REMOVAL;
			rotation += rotationSpeedDead;
			if (rotation >= Math.PI * 2)
				rotation -= Math.PI * 2;
			super.setRotation(rotation);
			return;
		}

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
