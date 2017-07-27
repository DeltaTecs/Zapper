package battle.stage._12;

import java.awt.Point;

import battle.WeaponPositioning;
import lib.ScheduledList;
import lib.Updateable;

public class DeltaController implements Updateable {

	private boolean attacking = false;
	private float rotationSpeed;
	private int rotationAncX, rotationAncY;
	private ScheduledList<EnemyDeltaPart> slaves = new ScheduledList<EnemyDeltaPart>();

	public DeltaController(float rotationSpeed, int rotationAncX, int rotationAncY) {
		super();
		this.rotationSpeed = rotationSpeed;
		this.rotationAncX = rotationAncX;
		this.rotationAncY = rotationAncY;
	}

	@Override
	public void update() {

		synchronized (slaves) {
			slaves.update();
		}

	}

	public void split(int instances) {

		synchronized (slaves) {

			for (int i = 0; i != instances; i++) {

				for (EnemyDeltaPart p : slaves) {
					// p entfernen
					p.unRegister();
					slaves.schedRemove(p);
					int newlen = p.getBorderlen() / 2;

					// Neue anlegen
					EnemyDeltaPart s0 = new EnemyDeltaPart(newlen);
					EnemyDeltaPart s1 = new EnemyDeltaPart(newlen);
					EnemyDeltaPart s2 = new EnemyDeltaPart(newlen);
					EnemyDeltaPart s3 = new EnemyDeltaPart(newlen);
					// Positionen setzen
					float[] deltaPosSet = getSplitPositionSet(p.getBorderlen(), p.getRotation());
					s0.setPosition(p.getPosX() + deltaPosSet[0], p.getPosY() + deltaPosSet[1]);
					s1.setPosition(p.getPosX() + deltaPosSet[2], p.getPosY() + deltaPosSet[3]);
					s2.setPosition(p.getPosX() + deltaPosSet[4], p.getPosY() + deltaPosSet[5]);
					s3.setPosition(p.getPosX() + deltaPosSet[6], p.getPosY() + deltaPosSet[7]);
					// Rotation setzen
					s0.setRotation(p.getRotation());
					s1.setRotation(p.getRotation() + Math.PI);
					s2.setRotation(p.getRotation());
					s3.setRotation(p.getRotation());
					// Zugehörigkeit updaten
					s0.setController(this);
					s1.setController(this);
					s2.setController(this);
					s3.setController(this);
					slaves.schedAdd(s0);
					slaves.schedAdd(s1);
					slaves.schedAdd(s2);
					slaves.schedAdd(s3);
					// adden
					s0.register();
					s1.register();
					s2.register();
					s3.register();

				}

			}
			slaves.update(); // flush

		}

	}

	public void link(EnemyDeltaPart slave) {
		slaves.schedAdd(slave);
	}

	/**
	 * Weißt die von den Controllern gelinkten Shards neu zu, so dass sie nur
	 * einem Controller unterstehen
	 * 
	 * @param controller
	 */
	public static void merge(DeltaController[] controller) {

		if (controller.length == 1) // nix zu mergen
			throw new DeltaControllerException("Controller merge unnessecary: only one controller given");

		// Der erste Controller ist jetzt neu zuständig
		DeltaController newBoss = controller[0];

		// neu zuweseisen
		for (int i = 1; i != controller.length; i++) {

			synchronized (controller[i].getSlaves()) { // listen flushen
				controller[i].getSlaves().update();
			}

			newBoss.getSlaves().schedAddAll(controller[i].getSlaves());

			for (EnemyDeltaPart s : controller[i].getSlaves())
				s.setController(newBoss); // Sklave updaten
		}
	}

	/**
	 * Gibt Rotierte Punkte für die Positionen von Split-Resultaten im Format
	 * (x0, y0, x1, y1, ...), es sind insgesamt 4 Punkte
	 * 
	 * @param oldBorderLen
	 * @param roration
	 * @return
	 */
	private static float[] getSplitPositionSet(int oldBorderLen, double rotation) {

		double[] pos0 = WeaponPositioning.rotate(rotation, new Point(0, -oldBorderLen / 4));
		double[] pos1 = WeaponPositioning.rotate(rotation, new Point(-oldBorderLen / 4, oldBorderLen / 4));
		double[] pos2 = WeaponPositioning.rotate(rotation, new Point(0, oldBorderLen / 4));
		double[] pos3 = WeaponPositioning.rotate(rotation, new Point(oldBorderLen / 4, oldBorderLen / 4));

		return new float[] { (float) pos0[0], (float) pos0[1], (float) pos1[0], (float) pos1[1], (float) pos2[0],
				(float) pos2[1], (float) pos3[0], (float) pos3[1] };
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public int getRotationAncX() {
		return rotationAncX;
	}

	public int getRotationAncY() {
		return rotationAncY;
	}

	public ScheduledList<EnemyDeltaPart> getSlaves() {
		return slaves;
	}

}
