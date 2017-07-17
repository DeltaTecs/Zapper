package battle.stage._11;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import corecase.MainZap;
import io.TextureBuffer;
import lib.PaintingTask;

public class Stargate implements PaintingTask {

	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_STRUCTURE_STARGATE_FRAME);
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

	private StargateConnector[] connectors = new StargateConnector[8];

	public Stargate() {

		connectors[0] = new StargateConnector(0);
		connectors[1] = new StargateConnector(0);
		connectors[2] = new StargateConnector(0);
		connectors[3] = new StargateConnector(0);
		connectors[4] = new StargateConnector(0);
		connectors[5] = new StargateConnector(0);
		connectors[6] = new StargateConnector(0);
		connectors[7] = new StargateConnector(0);

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

		positionConnectors();
	}

	@Override
	public void paint(Graphics2D g) {

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		g.translate(movedX, movedY);
		g.drawImage(TEXTURE, 0, 0, IMAGE_SIZE_X, IMAGE_SIZE_Y, null);
		g.translate(-movedX, -movedY);

		if (MainZap.generalAntialize)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

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

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}
}
