package battle.stage._11;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import battle.enemy.Enemy;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;
import lib.ScheduledList;

public class StargateConnector extends Enemy {

	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_STRUCTURE_STARGATE_CONNECTOR);
	private static final float SCALE = 9;
	private static final float RADIUS = 20;
	private static final int HEALTH = 3300;
	private static final ExplosionEffectPattern EXPL_PATTERN = new ExplosionEffectPattern(30, 50);
	private static final CollisionInformation COLL_INFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_FRIENDS, false);
	private static final int SCORE = 5;
	private static final int CRYSTALS = 5;
	private static final boolean FRIEND = false;

	private static final int AMOUNT_SPARKS = 160;

	private StargateConnector connection;
	private ScheduledList<StargateConnectionspark> sparks = new ScheduledList<StargateConnectionspark>();

	public StargateConnector(double rotation) {
		super(1, 1, 0, TEXTURE, SCALE, COLL_INFO, null, null, HEALTH, EXPL_PATTERN, SCORE, 0, CRYSTALS, FRIEND, null);
		setRotation(rotation);
	}

	@Override
	public void update() {
		super.update();
		if (this.isAlive() && connection.isAlive())
			updateAnimation();
	}

	private void updateAnimation() {

		if (!MainZap.fancyGraphics)
			return;

		synchronized (sparks) {
			for (StargateConnectionspark s : sparks)
				s.update();
		}

	}

	@Override
	public void paint(Graphics2D g) {

		if (isAlive() && connection.isAlive() && MainZap.fancyGraphics)
			synchronized (sparks) {
				for (StargateConnectionspark s : sparks)
					s.paint(g);
			}

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		super.paint(g);
		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

	}

	public StargateConnector getConnection() {
		return connection;
	}

	public void connect(StargateConnector connection) {
		this.connection = connection;
		// Sparks initialisieren
		synchronized (sparks) {
			sparks.clear();
			for (int i = 0; i != AMOUNT_SPARKS; i++) {
				if (MainZap.RANDOM.nextBoolean())
					sparks.add(new StargateConnectionspark(this, connection));
				else
					sparks.add(new StargateConnectionspark(connection, this));
			}
		}
	}

}
