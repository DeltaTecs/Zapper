package battle.stage._11;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import battle.stage.Stage11;
import battle.stage.StageManager;
import corecase.MainZap;
import gui.Hud;
import gui.HudLightningEffect;
import io.TextureBuffer;
import lib.PaintingTask;
import lib.ScheduledList;
import lib.Updateable;

public class Stargate implements PaintingTask, Updateable {

	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_STRUCTURE_STARGATE_FRAME);
	private static final float PULSSPEED_DEFAULT = 2.0f;
	private static final int PULS_DELAY_DEFAULT = MainZap.inTicks(1200);
	private static final float COLLAPS_UPSPEED = 0.015f;
	private static final float COLLAPS_DELAY_REDUCE = 0.14f;
	private static final int PORTALSPARKS_PER_TICK_DEFAULT = 20;
	private static final int RAND_ADD_PORTALSPARK_COLLAPSE = 25;
	private static final int RAND_RESET_TP_BLENDING = MainZap.inTicks(800);
	private static final float SCALE = 5.0f;
	private static final int IMAGE_SIZE_X = (int) (TEXTURE.getWidth() * SCALE);
	private static final int IMAGE_SIZE_Y = (int) (TEXTURE.getHeight() * SCALE);
	private static final Color COLOR_CONNECTION_DARK = new Color(255, 255, 255, 150);
	private static final Color COLOR_CONNECTION_BRIGHT = new Color(255, 255, 255, 200);
	private static final Stroke STROKE_CONNECTION = new BasicStroke(9, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL);

	private int posX;
	private int posY;
	private int movedX;
	private int movedY;
	private int portalSparksPerTick = PORTALSPARKS_PER_TICK_DEFAULT;
	private float pulsSpeed = PULSSPEED_DEFAULT;
	private float pulsDelay = PULS_DELAY_DEFAULT;
	private int timeToNextPuls = MainZap.rand((int) pulsDelay);
	private boolean collapsing = false;
	private boolean collapsed = false;

	private StargateConnector[] connectors = new StargateConnector[8];
	private ScheduledList<StargatePortalspark> portalLightnings = new ScheduledList<StargatePortalspark>();
	private ScheduledList<StargatePuls> portalPulses = new ScheduledList<StargatePuls>();
	private Stage11 stage;

	public Stargate(int locX, int locY, Stage11 stage) {

		this.stage = stage;
		posX = locX;
		posY = locY;

		movedX = (int) (locX - ((TEXTURE.getWidth() / 2) * SCALE));
		movedY = (int) (locY - ((TEXTURE.getHeight() / 2) * SCALE));

		connectors[0] = new StargateConnector(0);
		connectors[1] = new StargateConnector(0);
		connectors[2] = new StargateConnector(0);
		connectors[3] = new StargateConnector(0);
		connectors[4] = new StargateConnector(0);
		connectors[5] = new StargateConnector(0);
		connectors[6] = new StargateConnector(0);
		connectors[7] = new StargateConnector(0);

		positionConnectors(); // muss vor sparks-init kommen

		connectors[0].connect(connectors[1]);
		connectors[1].connect(connectors[2]);
		connectors[2].connect(connectors[3]);
		connectors[3].connect(connectors[4]);
		connectors[4].connect(connectors[5]);
		connectors[5].connect(connectors[6]);
		connectors[6].connect(connectors[7]);
		connectors[7].connect(connectors[0]);

		for (StargateConnector c : connectors)
			if (c != null)
				c.register();

	}

	@Override
	public void paint(Graphics2D g) {

		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}

		synchronized (portalPulses) {
			for (StargatePuls p : portalPulses)
				p.paint(g);
		}

		if (MainZap.fancyGraphics && !collapsed)
			synchronized (portalLightnings) {
				for (StargatePortalspark s : portalLightnings)
					s.paint(g);
			}

		g.translate(movedX, movedY);
		g.drawImage(TEXTURE, 0, 0, IMAGE_SIZE_X, IMAGE_SIZE_Y, null);
		g.translate(-movedX, -movedY);

		if (MainZap.fancyGraphics)
			g.setColor(COLOR_CONNECTION_DARK);
		else
			g.setColor(COLOR_CONNECTION_BRIGHT);
		g.setStroke(STROKE_CONNECTION);
		// Verbindungen
		for (StargateConnector c : connectors) {
			if (c.isAlive() && c.getConnection().isAlive()) {
				g.drawLine(c.getLocX(), c.getLocY(), c.getConnection().getLocX(), c.getConnection().getLocY());
			}
		}

		if (MainZap.generalAntialize) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
	}

	@Override
	public void update() {

		// Prüfen ob alle Conectoren nochda
		if (!collapsing && !collapsed) {
			boolean oneALive = false;
			for (StargateConnector c : connectors) {
				if (c.isAlive())
					oneALive = true;
			}

			if (!oneALive)
				collapse();

		}
		// Kollaps-update
		if (collapsing) {

			pulsDelay -= COLLAPS_DELAY_REDUCE;
			pulsSpeed += COLLAPS_UPSPEED;
			if (MainZap.rand(RAND_ADD_PORTALSPARK_COLLAPSE) == 0)
				portalSparksPerTick++;

			if (pulsDelay < 45 && Hud.getLightningEffect() == null && MainZap.fancyGraphics)
				Hud.setLightningEffect(new HudLightningEffect());

			if (pulsDelay < 50 && !stage.isEnemysDespawned())
				stage.despawnNpcs();

			if (pulsDelay < 0) { // Teleport
				portalPulses.schedClear();
				collapsed = true;
				collapsing = false;
				Hud.setLightningEffect(null);
				if (MainZap.getPlayer().isAlive())
					StageManager.setUp(12);
			}
		}

		// Blitze
		if (MainZap.fancyGraphics)
			synchronized (portalLightnings) {
				// update und remove
				for (StargatePortalspark s : portalLightnings) {
					s.update();
					if (s.faded())
						portalLightnings.schedRemove(s);
				}

				// add
				for (int i = 0; i != portalSparksPerTick; i++)
					portalLightnings.schedAdd(new StargatePortalspark(posX, posY));

				// flush
				portalLightnings.update();
			}

		// Impulse
		if (!collapsed)
			synchronized (portalPulses) {
				// update unt remove
				for (StargatePuls p : portalPulses) {
					p.update();
					if (p.isDone())
						portalPulses.schedRemove(p);
				}

				// add
				if (timeToNextPuls == 0) {
					timeToNextPuls = (int) pulsDelay;
					portalPulses.add(new StargatePuls(pulsSpeed, posX, posY));
				} else
					timeToNextPuls--;

				portalPulses.update();
			}
		else
			synchronized (portalPulses) {
				portalPulses.update();
			}

		// Hud-Effekte: Spieler blenden
		if (collapsing)
			if (MainZap.rand(RAND_RESET_TP_BLENDING) == 0)
				Hud.resetPortalBlending();

	}

	public void setPosition(int x, int y) {
		posX = x;
		posY = y;

		movedX = (int) (x - ((TEXTURE.getWidth() / 2) * SCALE));
		movedY = (int) (y - ((TEXTURE.getHeight() / 2) * SCALE));

		positionConnectors();
	}

	private void positionConnectors() {

		connectors[0].setPosition(posX + 95, posY - 284);
		connectors[1].setPosition(posX + 267, posY - 151);
		connectors[2].setPosition(posX + 267, posY + 151);
		connectors[3].setPosition(posX + 95, posY + 284);
		connectors[4].setPosition(posX - 95, posY + 284);
		connectors[5].setPosition(posX - 267, posY + 151);
		connectors[6].setPosition(posX - 267, posY - 151);
		connectors[7].setPosition(posX - 95, posY - 284);

	}

	public boolean isCollapsing() {
		return collapsing;
	}

	public void collapse() {
		collapsing = true;
		Hud.resetPortalBlending();
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

}
