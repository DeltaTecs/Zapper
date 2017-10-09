package battle.stage._12;

import java.awt.Graphics2D;

import corecase.MainZap;
import lib.PaintingTask;
import lib.ScheduledList;
import lib.SpeedVector;
import lib.Updateable;

public class FadeToPiecesEffect implements Updateable, PaintingTask {

	private static final int MIN_PIECES = 2;
	private static final int MAX_PIECES = 6;
	private static final int MIN_SIZE = 3;
	private static final int MAX_SIZE = 19;
	private static final float MAX_FAC_CHANGE_IN_VELOCITY = 0.25f;
	private static final float MIN_ALPHA = 0.15f;
	private static final float MAX_ALPHA = 0.9f;
	private static final float MIN_FADINGSPEED = 0.0003f;
	private static final float MAX_FADINGSPEED = 0.025f;

	private ScheduledList<FadingPiece> pieces = new ScheduledList<FadingPiece>();

	private FadeToPiecesEffect(DeltaEnemy e) {

		int amount = MIN_PIECES + MainZap.rand(MAX_PIECES - MIN_PIECES + 1);
		for (int i = 0; i != amount; i++) {

			int size = MIN_SIZE + MainZap.rand(MAX_SIZE - MIN_SIZE + 1);
			float alpha = MIN_ALPHA + (float) Math.random() * (MAX_ALPHA - MIN_ALPHA);
			float fadingspeed = MIN_FADINGSPEED + (float) Math.random() * (MAX_FADINGSPEED - MIN_FADINGSPEED);
			SpeedVector velocity = new SpeedVector(e.getVelocity().getX(), e.getVelocity().getY());
			SpeedVector dVelocity = new SpeedVector(
					(float) (2 * Math.random() - 1) * MAX_FAC_CHANGE_IN_VELOCITY * e.getVelocity().getX(),
					(float) (2 * Math.random() - 1) * MAX_FAC_CHANGE_IN_VELOCITY * e.getVelocity().getY());
			velocity.add(dVelocity);

			pieces.schedAdd(new FadingPiece(size, velocity, fadingspeed, alpha, e.getPosX() - (size / 2.0f),
					e.getPosY() - (size / 2.0f)));
		}
		pieces.update();
	}

	@Override
	public void update() {

		// Welche gefaded?
		for (FadingPiece p : pieces)
			if (p.isFaded())
				pieces.schedRemove(p);

		synchronized (pieces) {
			pieces.update();
		}

		// Beendigung prüfen (Noch Pieces da?)
		if (pieces.size() == 0)
			unRegister();

		// Positionen und Alpha updaten
		for (FadingPiece p : pieces)
			p.update();

	}

	@Override
	public void paint(Graphics2D g) {

		synchronized (pieces) {
			for (FadingPiece p : pieces)
				p.paint(g);
		}
	}

	private void register() {
		MainZap.getMap().addUpdateElement(this);
		MainZap.getMap().addPaintElement(this, true);
	}

	private void unRegister() {
		MainZap.getMap().removeUpdateElement(this);
		MainZap.getMap().removePaintElement(this, true);
	}

	public static void execute(DeltaEnemy e) {
		new FadeToPiecesEffect(e).register();
	}

}
