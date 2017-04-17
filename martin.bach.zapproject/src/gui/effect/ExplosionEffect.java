package gui.effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import corecase.MainZap;
import ingameobjects.InteractiveObject;

public class ExplosionEffect extends Effect {

	public static final int DURATION = MainZap.inTicks(300);
	private static final byte TIME_PER_PRE_EXPL = (byte) MainZap.inTicks(50);
	private static final float PRE_EXPL_INDEX_DELTA = 1.0f / TIME_PER_PRE_EXPL;
	private static final int FADING_START_FROM = (int) (DURATION * 0.5f);
	private static final int ALPHA_START = 180;
	private static final float ALPHA_DELTA = (float) (ALPHA_START) / (float) FADING_START_FROM;
	private static final byte MAX_PRE_EXPL_AT_ONCE = 5;
	private static final float SIZE_INNER_PER_OUDER = 0.6f;
	private static final float SIZE_BALL_REMOVAL = 0.5f;
	private static final int SIZE_PRE_EXPL = 10;
	private static final int[] RGB_OUDER = new int[] { 224, 162, 69 };
	// private static final int[] RGB_INNER = new int[] { 249, 222, 159 };
	private static final int[] RGB_INNER = new int[] { 247, 242, 232 };

	private int preexplosions;
	public int preexplodeTime;
	private int ballradius;
	private int ballradiusDelta;
	private int hostradius;
	private InteractiveObject host;
	private Runnable finishTask;
	private ArrayList<Point2D[]> preExpls = new ArrayList<Point2D[]>();
	private float preExplIndex = 0;
	private int currentBallRadius;
	private float currentBallAlpha = ALPHA_START;

	public ExplosionEffect(int preexplosions, int ballradius, InteractiveObject host) {
		super(DURATION + (2 * preexplosions / MAX_PRE_EXPL_AT_ONCE));
		this.preexplosions = preexplosions;
		this.ballradius = ballradius;
		currentBallRadius = ballradius;
		this.hostradius = (int) host.getCollisionInfo().getRadius();
		this.host = host;
		ballradiusDelta = (int) ((SIZE_BALL_REMOVAL * ballradius) / DURATION);

		// Pre-Explosions initialisieren
		while (preexplosions > 0) {

			int nextAmount = MainZap.rand(MAX_PRE_EXPL_AT_ONCE);

			preexplosions -= nextAmount;
			if (preexplosions < 0) {
				nextAmount += preexplosions; // Überschuss zurück kippen
			}

			Point2D[] nextSet = new Point2D[nextAmount];
			for (int i = 0; i != nextAmount; i++) {
				int[] randCors = getRandCircleCoordinates(hostradius);
				nextSet[i] = new Point(randCors[0], randCors[1]);
			}
			preExpls.add(nextSet);
		}
		preexplodeTime = TIME_PER_PRE_EXPL * preExpls.size();
		super.setDuration(DURATION + preexplodeTime);
	}

	public ExplosionEffect(ExplosionEffectPattern pattern, InteractiveObject host) {
		super(DURATION);
		this.preexplosions = pattern.getPreexplosions();
		this.ballradius = pattern.getBallradius();
		this.hostradius = (int) host.getCollisionInfo().getRadius();
		currentBallRadius = ballradius;
		this.host = host;
		ballradiusDelta = (int) ((SIZE_BALL_REMOVAL * ballradius) / (float) DURATION);

		// Pre-Explosions initialisieren
		while (preexplosions > 0) {

			int nextAmount = MainZap.rand(MAX_PRE_EXPL_AT_ONCE);

			preexplosions -= nextAmount;
			if (preexplosions < 0) {
				nextAmount += preexplosions; // Überschuss zurück kippen
			}

			Point2D[] nextSet = new Point2D[nextAmount];
			for (int i = 0; i != nextAmount; i++) {
				int[] randCors = getRandCircleCoordinates(hostradius);
				nextSet[i] = new Point(randCors[0], randCors[1]);
			}
			preExpls.add(nextSet);
		}
		preexplodeTime = TIME_PER_PRE_EXPL * preExpls.size();
		super.setDuration(DURATION + preexplodeTime);
	}

	@Override
	public void update() {
		super.update();

		if (getDuration() == DURATION && finishTask != null)
			finishTask.run(); // Z.B. Textur wegnehmen

		if (getDuration() >= DURATION && preExplIndex < preExpls.size() - 1) {
			// Pre-Explosion-Zeit

			preExplIndex += PRE_EXPL_INDEX_DELTA;

		} else {
			// Haupt-Explosion

			if (getDuration() <= FADING_START_FROM)
				currentBallAlpha -= ALPHA_DELTA;
			currentBallRadius -= ballradiusDelta;

		}

	}

	@Override
	public void paint(Graphics2D g) {

		// Zu Eigen-Pos-Kontext
		int dx = host.getLocX();
		int dy = host.getLocY();
		g.translate(dx, dy);

		if (getDuration() >= DURATION) {
			// Vor-Explosionen zeichnen

			for (Point2D p : preExpls.get((int) preExplIndex)) {

				drawExplosion(g, (int) p.getX(), (int) p.getY(), SIZE_PRE_EXPL, MainZap.rand(100) + 155);
			}

			g.translate(-dx, -dy);
			return;
		}

		// Echte Explosion zeichnen
		drawExplosion(g, 0, 0, currentBallRadius, (int) currentBallAlpha);

		g.translate(-dx, -dy);

	}

	private static void drawExplosion(Graphics2D g, int x, int y, int radius, int alpha) {

		if (alpha <= 0)
			return; // Man sieht eh nix

		g.translate(x, y);
		g.setColor(new Color(RGB_OUDER[0], RGB_OUDER[1], RGB_OUDER[2], alpha));
		int innerSize = (int) (radius * SIZE_INNER_PER_OUDER);
		g.fillOval(-innerSize, -innerSize, 2 * innerSize, 2 * innerSize);
		g.setColor(new Color(RGB_INNER[0], RGB_INNER[1], RGB_INNER[2], alpha));
		g.fillOval(-radius, -radius, 2 * radius, 2 * radius);
		g.translate(-x, -y);

	}

	public static int[] getRandCircleCoordinates(int radius) {

		int x = MainZap.rand(2 * radius) - radius; // -rad bis +rad
		int yBound = (int) (radius * Math.sin(Math.acos(x / (float) radius)));
		yBound = Math.abs(yBound);
		if (yBound == 0)
			yBound++;
		int y = MainZap.rand(2 * yBound) - yBound; // Unterkante bis Oberkante
		return new int[] { x, y };

	}

	public int getPreexplosions() {
		return preexplosions;
	}

	public void setPreexplosions(int preexplosions) {
		this.preexplosions = preexplosions;
	}

	public void setFinishTask(Runnable finishTask) {
		this.finishTask = finishTask;
	}

}
