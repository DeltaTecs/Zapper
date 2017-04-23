package gui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Random;

import battle.WeaponPositioning;
import corecase.MainZap;

public abstract class Mirroring {

	private static final int PLAYER_DISTANCE = 70;
	private static final Random RAND = new Random(System.currentTimeMillis());
	private static boolean active = false;
	private static MirrorImage[] images = null;
	private static Point[] corePositions = new Point[] { new Point(0, PLAYER_DISTANCE),
			new Point(WeaponPositioning.rotate((float) (2 * Math.PI / 3.0), new Point(0, PLAYER_DISTANCE))),
			new Point(WeaponPositioning.rotate((float) (4 * Math.PI / 3.0), new Point(0, PLAYER_DISTANCE))) };
	private static int texHalfWidth;
	private static int texHalfHeight;

	public static void paint(Graphics2D g) {
		if (!active)
			return;
		// Benötigt Frame - 0/0 Kontext

		AffineTransform rotationScaleTransform = AffineTransform.getRotateInstance(MainZap.getPlayer().getRotation(),
				texHalfWidth, texHalfHeight);
		rotationScaleTransform.scale(MainZap.getPlayer().getTextureScale(), MainZap.getPlayer().getTextureScale());

		g.translate(Frame.HALF_SCREEN_SIZE - texHalfWidth, Frame.HALF_SCREEN_SIZE - texHalfHeight);

		for (MirrorImage m : images) {
			Composite storeComp = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, m.getAlpha()));
			int[] deltaPos = new int[] { m.getDx(), m.getDy() };
			g.translate(deltaPos[0], deltaPos[1]);
			g.drawImage(MainZap.getPlayer().getTexture(), rotationScaleTransform, null);
			g.translate(-deltaPos[0], -deltaPos[1]);
			g.setComposite(storeComp);
		}

		g.translate(-Frame.HALF_SCREEN_SIZE + texHalfWidth, -Frame.HALF_SCREEN_SIZE + texHalfHeight);

	}

	public static void update() {
		if (!active)
			return;

		boolean allFaded = true;
		for (MirrorImage m : images) {
			if (m.isAlive())
				allFaded = false;
		}

		if (allFaded) {
			active = false;
			return;
		}

	}

	// Cosmetic
	public static void checkMirrorStateChange() {
		if (!active)
			return;

		if (RAND.nextInt(260) == 0)
			changePositions();
	}

	// Sexy-Spiegel-Effekt. Die Spiegelbilder sollen nicht starr auf einem Fleck
	// bleiben
	private static void changePositions() {
		float nextRotation = (float) (Math.random() * 2 * Math.PI);
		for (int i = 0; i != 3; i++) {
			Point next = WeaponPositioning.rotate(nextRotation, corePositions[i]);
			images[i].setDx(next.x);
			images[i].setDy(next.y);
		}
	}

	public static void activate() {
		texHalfHeight = (int) ((MainZap.getPlayer().getTexture().getHeight() * MainZap.getPlayer().getTextureScale())
				/ 2.0f);
		texHalfWidth = (int) ((MainZap.getPlayer().getTexture().getWidth() * MainZap.getPlayer().getTextureScale())
				/ 2.0f);
		images = new MirrorImage[] { new MirrorImage(), new MirrorImage(), new MirrorImage() };
		changePositions();
		active = true;
	}

	public static void cancel() {
		if (!active)
			return;
		for (MirrorImage m : images) {
			m.unRegister();
		}
		active = false;
	}

	public static void fireImages() {
		if (!active)
			return;

		for (MirrorImage m : images) {
			if (!m.isAlive())
				continue;
			m.fire();
		}
	}

	public static boolean isActive() {
		return active;
	}

	public static MirrorImage[] getImages() {
		return images;
	}

}
