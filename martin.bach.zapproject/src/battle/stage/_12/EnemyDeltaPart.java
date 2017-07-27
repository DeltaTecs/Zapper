package battle.stage._12;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.AiProtocol;
import battle.enemy.Enemy;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import lib.RotateablePolygon;

public class EnemyDeltaPart extends Enemy {

	private static final float BASE_SPEED = 3.0f; // eig. 300
	private static final int BASE_HP = 40;
	private static final int SHOOTING_RANGE = 300;
	private static final float BASE_SCALE = 0.144f;
	private static final float BASE_RADIUS = 0.4f;
	private static final float BASE_PREXEPLOSIONS = 0.5f;
	private static final float BASE_EXPLOSION_RANGE = 3.0f;
	private static final float COOLDOWN = 1;
	private static final int SCORE = 5;
	private static final int PROJECTILE_RANGE = 1;
	private static final boolean FRIEND = false;

	private static final boolean DISABLE_SHOCK = true;

	private int borderlen;
	private RotateablePolygon outline;
	private DeltaController controller;

	public EnemyDeltaPart(int borderlen) {
		super(0, 0, BASE_SPEED / borderlen,
				new BufferedImage((int) (borderlen * BASE_SCALE), (int) (borderlen * BASE_SCALE),
						BufferedImage.TYPE_INT_ARGB),
				BASE_SCALE * borderlen,
				new CollisionInformation(borderlen * BASE_RADIUS, CollisionType.COLLIDE_WITH_FRIENDS, false),
				new AiProtocol(), new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), (int) (BASE_HP * borderlen),
				new ExplosionEffectPattern((int) (BASE_PREXEPLOSIONS * borderlen),
						(int) (BASE_EXPLOSION_RANGE * borderlen)),
				SCORE, PROJECTILE_RANGE, borderlen / 2, FRIEND);
		this.borderlen = borderlen;
		int height = (int) ((borderlen / 2.0f) * Math.tan(Math.PI / 3.0));
		outline = new RotateablePolygon(new Point2D[] { new Point(0, -height / 2), new Point(borderlen / 2, height / 2),
				new Point(-borderlen / 2, height / 2) }, 0, 0);
		setProjectilePattern(null);
		setMayShoot(false);
		getAiProtocol().allowFacePlayer(false);
		getAiProtocol().setNonAutoLockon(true);

		
		
		
		
		
		
		
		
		// &&& Die neuen Shards haben ein shootingaim von 0/0, sie zeigen nach
		// oben, das und diee idle rotation (oder was auch immer) müsste man
		// abdrehen.

		
		
		
		
		
		
		
		
		
		
		
	}

	@Override
	public void paint(Graphics2D g) {

		// Von Kontext Karte zu Kontext EigenPos
		int dx = getLocX();
		int dy = getLocY();

		g.translate(dx, dy);

		// Schiff zeichnen
		g.setColor(Color.BLACK);
		g.fillPolygon(outline.getPolygon());

		// HP-Leiste
		if (getDmgIndicatingTime() > 0)
			paintDamageIndicators(g);

		// Shock-Effekt
		if (isShocked())
			if (getShockEffect() != null)
				getShockEffect().paint(g);

		if (MainZap.debug) {

			g.setColor(Color.GREEN);

			g.fillRect(-2, -2, 4, 4);
			g.drawOval((int) -getInformation().getRadius(), (int) -getInformation().getRadius(),
					(int) (2 * getInformation().getRadius()), (int) (2 * getInformation().getRadius()));

		}

		// Von EigenPos zu Karten Kontext
		g.translate(-dx, -dy);

	}

	@Override
	public Object getClone() {
		return new EnemyDeltaPart(borderlen);
	}

	@Override
	public void shock() {
		if (!DISABLE_SHOCK)
			super.shock();
	}

	@Override
	public void setRotation(double rotation) {
		super.setRotation(rotation);
		outline.rotateByRadians((float) getRotation());
	}

	public DeltaController getController() {
		return controller;
	}

	public void setController(DeltaController controler) {
		this.controller = controler;
	}

	public int getBorderlen() {
		return borderlen;
	}

}
