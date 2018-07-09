package battle.collect;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import battle.Shockable;
import collision.Collideable;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import ingameobjects.InteractiveObject;
import ingameobjects.Player;

public class Collectable extends InteractiveObject implements Shockable {

	private static final int MAGNET_RANGE = 180;
	private static final float MAGNET_MAX_SPEED = 4.0f;
	private static final float STARTING_BLINK_FROM = 0.4f;
	private static final float ALPHA_DELTA_MAX = 30.0f;

	private int maxDuration;
	private int duration;
	private BufferedImage textureRound;
	private BufferedImage textureCornered;
	private AffineTransform textureTransform;
	private boolean magnetic = false;
	private boolean mayFlicker = true;
	private float currentAlpha = 255;
	private float currentAlphaDelta = 0;
	private float alphaDeltaDelta;
	private boolean collected = false;
	private Runnable unregisterTask = null;

	public Collectable(int size, int duration, BufferedImage textureRound, BufferedImage textureCornered,
			boolean flicker, boolean magnetic) {
		super(new CollisionInformation(size * 0.5f, CollisionType.COLLIDE_ONLY_WITH_PLAYER, true), true, true);
		// true -> immer an Stage gebunden; true -> immer im Hintergrund
		maxDuration = duration;
		this.duration = duration;
		this.textureRound = textureRound;
		this.textureCornered = textureCornered;
		this.mayFlicker = flicker;
		this.magnetic = magnetic;
		alphaDeltaDelta = ALPHA_DELTA_MAX / (duration * STARTING_BLINK_FROM);

		// Textur sollte quadratisch sein
		float scaling = (float) size / textureRound.getWidth();
		textureTransform = new AffineTransform();
		textureTransform.scale(scaling, scaling);
		textureTransform.translate(-textureRound.getWidth() / 2, -textureRound.getHeight() / 2);
	}

	@Override
	public void collide(Collideable c) {
		super.collide(c);
		if (!collected && c instanceof Player) {
			collect();
			collected = true;
		}
	}

	@Override
	public void paint(Graphics2D g) {

		Composite storeComp = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentAlpha / 255.0f));

		// Von Map zu Eigenpos-Kontext
		int dx = getLocX();
		int dy = getLocY();
		g.translate(dx, dy);

		if (MainZap.roundCorners) {
			g.drawImage(textureRound, textureTransform, null);
		} else {
			g.drawImage(textureCornered, textureTransform, null);
		}

		// Von Eigenpos-Kontext zu Map
		g.translate(-dx, -dy);

		g.setComposite(storeComp);
	}

	@Override
	public void update() {

		if (duration > 0) {
			duration--;
		} else if (duration == 0) { // duration = -1 -> unendlich
			unRegister();
		}

		// ---- Saug - Update
		if (magnetic && MainZap.getPlayer().isAlive()) {
			if (distanceToPlayer() <= MAGNET_RANGE) {
				// Ausrichten
				getVelocity().aimFor(getLocX(), getLocY(),
						((MAGNET_RANGE - distanceToPlayer()) / (float) MAGNET_RANGE) * MAGNET_MAX_SPEED,
						MainZap.getPlayer().getLocX(), MainZap.getPlayer().getLocY());
				// Bewegen
				move(getVelocity());
			}

		}

		// ---- Flacker - Update
		if (!mayFlicker)
			return; // Flackern nicht erwünscht

		if (duration <= (int) (maxDuration * STARTING_BLINK_FROM)) {
			// Flackern
			if (currentAlphaDelta >= 0) {
				currentAlphaDelta += alphaDeltaDelta; // Stärker flackern
			} else {
				currentAlphaDelta -= alphaDeltaDelta; // Stärker flackern
			}

			currentAlpha += currentAlphaDelta;
			if (currentAlpha > 255) {
				currentAlpha = 255;
				currentAlphaDelta *= -1;
				return;
			}
			if (currentAlpha < 50) {
				currentAlpha = 50;
				currentAlphaDelta *= -1;
				return;
			}
		}

	}

	@Override
	public void unRegister() {
		super.unRegister();
		if (unregisterTask != null)
			unregisterTask.run();
	}

	@Override
	public void shock() {
		duration -= (maxDuration * 0.9f);
	}

	public void collect() {
	}

	public BufferedImage getTextureRound() {
		return textureRound;
	}

	public float getCurrentAlpha() {
		return currentAlpha;
	}

	public int getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(int maxDuration) {
		this.maxDuration = maxDuration;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isMagnetic() {
		return magnetic;
	}

	public void setMagnetic(boolean magnetic) {
		this.magnetic = magnetic;
	}

	public boolean isMayFlicker() {
		return mayFlicker;
	}

	public void allowFlicker(boolean mayFlicker) {
		this.mayFlicker = mayFlicker;
	}

	public Runnable getUnregisterTask() {
		return unregisterTask;
	}

	public void setUnregisterTask(Runnable unregisterTask) {
		this.unregisterTask = unregisterTask;
	}

	public AffineTransform getTextureTransform() {
		return textureTransform;
	}
	
	

}
