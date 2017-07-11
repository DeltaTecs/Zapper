package battle.enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import battle.CombatObject;
import battle.MultiCannonWeaponConfiguration;
import battle.WeaponConfiguration;
import battle.ai.AiProtocol;
import collision.CollisionInformation;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;

public class AttachedEnemy extends Enemy {

	private float dx, dy;
	private CombatObject host;
	private int[] latestPaintHostLoc;

	public AttachedEnemy(float speed, BufferedImage texture, float scale, CollisionInformation information,
			AiProtocol ai, WeaponConfiguration weaponconf, int health, ExplosionEffectPattern explPattern, int score,
			int projRange, int crystals, boolean friend, CombatObject host, float[] deltaLoc) {
		super(host.getPosX() + deltaLoc[0], host.getPosY() + deltaLoc[1], speed, texture, scale, information, ai,
				weaponconf, health, explPattern, score, projRange, crystals, friend);
		this.host = host;
		latestPaintHostLoc = host.getLocation(); // init
		this.dx = deltaLoc[0];
		this.dy = deltaLoc[1];
	}

	// --- VVV in fore-ground einordnen
	@Override
	public void register() {
		MainZap.getMap().addPaintElement(this, false);
		MainZap.getMap().addUpdateElement(this);
		MainZap.getGrid().add(this);
		getListedObjects().add(this);
	}

	@Override
	public void unRegister() {
		MainZap.getMap().removePaintElement(this, false);
		MainZap.getMap().removeUpdateElement(this);
		MainZap.getGrid().remove(this);
		getListedObjects().remove(this);
	}
	// ----

	// VVV Bei painting direkt auf host-pos beziehen
	@Override
	public void paint(Graphics2D g) {

		// Warp? -> benötigt Map-Kontext
		if (super.isWarping()) {
			// Der wird hier gemacht. Da der Textur-Transform beim Effekt
			// apspackt.. :(
			if (getWarpEffect() != null)
				getWarpEffect().paint(g);
		}

		// Von Kontext Karte zu Kontext EigenPos
		int dx = latestPaintHostLoc[0] + (int) this.dx;
		int dy = latestPaintHostLoc[1] + (int) this.dy;
		g.translate(dx, dy);

		// Schiff zeichnen
		AffineTransform buffer = new AffineTransform(getTextureTransform());
		buffer.rotate(getRotation(), getTexture().getWidth() / 2, getTexture().getHeight() / 2);
		g.drawImage(getTexture(), buffer, null);

		// HP-Leiste
		if (getDmgIndicatingTime() > 0)
			paintDamageIndicators(g);

		// Shock-Effetk
		if (isShocked())
			if (getShockEffect() != null)
				getShockEffect().paint(g);

		if (MainZap.debug) {

			g.setColor(Color.GREEN);

			g.fillRect(-2, -2, 4, 4);
			g.drawOval((int) -getInformation().getRadius(), (int) -getInformation().getRadius(),
					(int) (2 * getInformation().getRadius()), (int) (2 * getInformation().getRadius()));

			if (getWeaponConfiguration() instanceof MultiCannonWeaponConfiguration) {
				Point[] ps = ((MultiCannonWeaponConfiguration) getWeaponConfiguration()).getPositioning()
						.getRotated((float) getRotation());
				g.setColor(Color.CYAN);
				for (Point p : ps)
					g.fillRect(p.x - 3, p.y - 3, 6, 6);
			}

		}

		// Von EigenPos zu Karten Kontext
		g.translate(-dx, -dy);

	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public void setDeltaPosition(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public CombatObject getHost() {
		return host;
	}

	public void setHost(CombatObject host) {
		this.host = host;
	}

	public void setLatestHostPaintLoc(int x, int y) {
		latestPaintHostLoc = new int[] { x, y };
	}
}
