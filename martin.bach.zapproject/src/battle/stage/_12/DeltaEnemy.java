package battle.stage._12;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import battle.WeaponPositioning;
import corecase.MainZap;
import lib.PaintingTask;
import lib.RotateablePolygon;
import lib.Updateable;

public class DeltaEnemy implements PaintingTask, Updateable {

	private static final float BASE_RADIUS = 0.35f;
	private static final float BASE_HP = 0.1f;

	private int volume, borderlen;
	private float posX, posY;
	private RotateablePolygon outline;
	private boolean splitable;
	private DeltaDummy[] dummys;
	private double rotation;
	private boolean[] partsAvailable;

	public DeltaEnemy(int borderlen, boolean splitable) {
		volume = borderlen * borderlen / 2;
		this.borderlen = borderlen;
		this.splitable = splitable;
		outline = new RotateablePolygon(TriangleCalculation.getTriangleOutline(borderlen), 0, 0);

		// Dummys einrichten
		if (splitable) { // Mehrere

			partsAvailable = new boolean[] { true, true, true, true };

			float[] splitpos = TriangleCalculation.getSplitPositionSet(borderlen);
			dummys = new DeltaDummy[4];
			dummys[0] = new DeltaDummy(splitpos[0], splitpos[1], BASE_RADIUS * borderlen / 2, (byte) 1,
					(int) (volume * BASE_HP * 0.25f), this);
			dummys[1] = new DeltaDummy(splitpos[2], splitpos[3], BASE_RADIUS * borderlen / 2, (byte) 2,
					(int) (volume * BASE_HP * 0.25f), this);
			dummys[2] = new DeltaDummy(splitpos[4], splitpos[5], BASE_RADIUS * borderlen / 2, (byte) 3,
					(int) (volume * BASE_HP * 0.25f), this);
			dummys[3] = new DeltaDummy(splitpos[6], splitpos[7], BASE_RADIUS * borderlen / 2, (byte) 4,
					(int) (volume * BASE_HP * 0.25f), this);

		} else { // nur einer

			dummys = new DeltaDummy[1];
			dummys[0] = new DeltaDummy(0, 0, BASE_RADIUS * borderlen, (byte) 3, (int) (volume * BASE_HP * 0.25f), this);
		}

		for (DeltaDummy d : dummys)
			d.register();
	}

	@Override
	public void update() {

		// ### DEBUG
//		rotation += 0.001;
		outline.rotateByRadians((float) rotation);
		updateDummyLocations();
	}

	private void updateDummyLocations() {

		for (DeltaDummy d : dummys) {
			double[] rotPos = WeaponPositioning.rotate(rotation, new Point((int) d.getDx(), (int) d.getDy()));
			d.setPosition((float) rotPos[0] + posX, (float) rotPos[1] + posY);
		}

	}

	@Override
	public void paint(Graphics2D g) {

		g.setColor(Color.BLACK);
		float dx = posX;
		float dy = posY;
		g.translate(dx, dy);
		g.fillPolygon(outline.getPolygon());
		g.translate(-dx, -dy);

		g.setColor(Color.GREEN);
		for (DeltaDummy d : dummys) {
			g.drawOval((int) (d.getPosX() - d.getRadius()), (int) (d.getPosY() - d.getRadius()),
					(int) (2 * d.getRadius()), (int) (2 * d.getRadius()));
		}

	}

	public void breakAt(byte posId) {

		if (splitable) {
			partsAvailable[posId - 1] = false;
			outline = new RotateablePolygon(TriangleCalculation.getTriangleOutline(borderlen, partsAvailable), 0, 0);
			outline.rotateByRadians((float) rotation);
		} else
			die();

	}

	private void die() {

	}

	public void register() {
		MainZap.getMap().addUpdateElement(this);
		MainZap.getMap().addPaintElement(this, true);
	}

	public void unRegister() {
		MainZap.getMap().removeUpdateElement(this);
		MainZap.getMap().removePaintElement(this, true);
	}

	public int getVolume() {
		return volume;
	}

	public void setPosition(float x, float y) {
		posX = x;
		posY = y;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

}
