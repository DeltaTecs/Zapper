package battle.stage._3;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import battle.enemy.Enemy;
import battle.stage.Stage3;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;
import lib.ScheduledList;

public class EnemyBasecoreAlpha0 extends Enemy {

	private static final int HIT_RANGE = 60;
	private static final float ROTATION_SPEED = 0.01f;
	private static final float SCALE = 5;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_ENEMYBASECORE_ALPHA0);
	private static final int MAX_HP = 20000;
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(80, 800);
	private static final int SCORE = 100;
	private static final int CRYSTALS = 120;
	private static final CollisionInformation COLLISION_INFORMATION = new CollisionInformation(HIT_RANGE,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final boolean FRIEND = false;

	private double rotation = 0;
	private boolean alive = true;
	private Stage3 stage;
	private ScheduledList<Enemy> listedEnemys;

	public EnemyBasecoreAlpha0(Stage3 stage, ScheduledList<Enemy> listedEnemys) {
		super(0, 0, 0, TEXTURE, SCALE, COLLISION_INFORMATION, null, null, MAX_HP, EXPL_PATTERN, SCORE, 0, CRYSTALS,
				FRIEND);
		this.stage = stage;
		this.listedEnemys = listedEnemys;
	}

	@Override
	public void paint(Graphics2D g) {

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		super.paint(g);

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	}

	@Override
	public void die() {
		super.die();
		alive = false;
		stage.destroyTurrets();
		stage.pass();
		listedEnemys.schedRemove(this);
	}

	@Override
	public void update() {
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
