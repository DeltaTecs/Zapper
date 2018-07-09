package battle.looting;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import battle.collect.Collectable;
import corecase.MainZap;

public class SpecialCollectable extends Collectable {

	private static final Color[] COLORS = new Color[] { new Color(20, 20, 20), new Color(0, 127, 14),
			new Color(234, 170, 0), new Color(178, 0, 255) };
	private static final Font FONT = new Font("Arial", Font.BOLD, 16);

	private Runnable collectAction;
	private String title;
	private Color color;
	private int size;

	public SpecialCollectable(BufferedImage texture, float scale, String title, int color, Runnable collectAction) {
		super((int) (texture.getHeight() * scale), -1, texture, texture, false, true);
		this.collectAction = collectAction;
		this.title = title;
		this.color = COLORS[color];
		this.size = (int) (texture.getHeight() * scale);
	}

	@Override
	public void paint(Graphics2D g) {
		// Von Map zu Eigenpos-Kontext
		int dx = getLocX();
		int dy = getLocY();
		g.translate(dx, dy);

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		g.drawImage(getTextureRound(), getTextureTransform(), null);

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		
		g.setColor(color);
		g.setFont(FONT);
		g.drawString(title, (this.size / 2) + 6, 6);

		// Von Eigenpos-Kontext zu Map
		g.translate(-dx, -dy);

	}

	@Override
	public void collect() {
		collectAction.run();
		this.unRegister();
	}
}
