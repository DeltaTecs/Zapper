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

	private static final int MAX_INSTANCES = 5;
	private static final float BASE_RADIUS = 0.35f;
	private static final float BASE_HP = 0.1f;

	private int volume, borderlen;
	private float posX, posY;
	private RotateablePolygon outline;
	private boolean splitable;
	private DeltaDummy[] dummys;
	private double rotation;
	private boolean[] partsAvailable = new boolean[] { false };
	private int partsRemaining;
	private int instance;

	public DeltaEnemy(int borderlen, int instance) {
		volume = borderlen * borderlen / 2;
		this.borderlen = borderlen;
		splitable = instance < MAX_INSTANCES;
		this.instance = instance;
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
			partsRemaining = 4;

		} else { // nur einer
			partsRemaining = 1;
			partsAvailable = new boolean[] { true };
			dummys = new DeltaDummy[1];
			dummys[0] = new DeltaDummy(0, 0, BASE_RADIUS * borderlen, (byte) 3, (int) (volume * BASE_HP), this);
		}

		// Dummys registrieren
		for (DeltaDummy d : dummys)
			d.register();
	}

	@Override
	public void update() {

		// Rotations-Update
		if (instance == 0 && partsRemaining == 4)
			rotation += 0.0004;
		if (splitable && partsRemaining == 1)
			outline.rotateByRadians((float) (rotation + Math.PI));
		else
			outline.rotateByRadians((float) rotation);
		// DummyUpdate (Hitbox, etc)
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
		if (!splitable && MainZap.debug) // ### DEBUG
			g.setColor(Color.BLUE);
		float dx = posX;
		float dy = posY;
		g.translate(dx, dy);
		g.fillPolygon(outline.getPolygon());
		g.translate(-dx, -dy);

		if (MainZap.debug) {
			g.setColor(Color.GREEN);
			int i = 0;
			for (DeltaDummy d : dummys) {
				if (partsAvailable[i])
					g.drawOval((int) (d.getPosX() - d.getRadius()), (int) (d.getPosY() - d.getRadius()),
							(int) (2 * d.getRadius()), (int) (2 * d.getRadius()));
				i++;
			}
		}

	}

	public boolean breakAt(byte posId) {

		if (splitable) {

			if (partsRemaining == 2) {
				// Noch Mitte und ein anderes �ber.
				// -> beide breaken diese Instanz l�schen

				for (int i = 0; i != partsAvailable.length; i++) {
					if (partsAvailable[i]) // beide Teile rausfischen
						replacePartAt((byte) (i + 1));
				}

				// Instanz beenden
				unRegister();

				return true; // egal. wird eh alles replaced.

			} // else:

			if (posId == 3) {
				// Mitte weggeholzt, und noch min 2 andere Teile vorhanden
				// -> ein random, nicht-Mittel-St�ck breaken

				// Alle breakable Parts finden
				byte[] nonMiddleParts = new byte[] { 0, 0, 0 };
				int partIndex = 0;
				for (int i = 0; i != partsAvailable.length; i++)
					if (partsAvailable[i] && dummys[i].getId() != 3) {
						nonMiddleParts[partIndex] = dummys[i].getId();
						partIndex++;
					}

				// Ein Part ausw�hlen
				byte selectedId = 0;
				if (partIndex == 2) // zwei Teile gefunden
					selectedId = (byte) (MainZap.rand(3) + 1);
				else // drei Teile gefunden
					selectedId = (byte) (MainZap.rand(3) + 1);

				// Part breaken
				replacePartAt(selectedId);
				return false; // Es wurde eine alternativer Part replaced.

			} // else:

			// Au�enst�ck weggeholzt
			replacePartAt(posId);
			return true; // Alles tuti

		} else
			die();
		return true; // Reset verweigert (alles tuti)

	}

	private void replacePartAt(byte id) {
		dummys[id - 1].replace();
		partsAvailable[id - 1] = false;
		partsRemaining--;
		outline = new RotateablePolygon(TriangleCalculation.getTriangleOutline(borderlen, partsAvailable), 0, 0);
		outline.rotateByRadians((float) rotation);
	}

	private void die() {

		for (int i = 0; i != dummys.length; i++)
			if (partsAvailable[i]) {
				partsAvailable[i] = false;
				dummys[i].unRegister();
			}
		partsRemaining = 0;
		unRegister();

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

	public int getInstance() {
		return instance;
	}

	public void setInstance(int instance) {
		this.instance = instance;
	}

	public int getBorderlen() {
		return borderlen;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

}
