package battle.stage._12;

import corecase.MainZap;
import gui.Hud;
import lib.SpeedVector;

public class TierSixCoordinator extends DeltaCoordinator {

	private static final float EQUALIZING_FORCE = 0.0032f;
	private static final float LINE_SPREAD_FAC = 2.0f;

	public TierSixCoordinator(DeltaEnemy host, TierFiveCoordinator superior) {
		super(host, superior);
		host.setFaceMovementDirection(true);
		((TierOneCoordinator) superior.getSuperiorCoordinators()[0]).registerMinion(this);
	}

	@Override
	public void subinit() {
		super.subinit();

		// Bewegung initialisieren
		SpeedVector approxDirection = new SpeedVector( // Von TierOne Mitte zum jetzigen Host
				getHost().getPosX() - getSuperiorCoordinators()[4].getHost().getPosX() + 0.1f,
				getHost().getPosY() - getSuperiorCoordinators()[4].getHost().getPosY() + 0.1f); // +0.1f, um 0|0 zu
																								// verhindern
		approxDirection.scaleToLength(100); // Stutzen
		int randX = MainZap.rand(76) - 38; // Zufällige werte
		int randY = MainZap.rand(76) - 38;
		// Addition
		SpeedVector direction = new SpeedVector(randX + approxDirection.getX(), randY + approxDirection.getY());
		direction.scaleToLength(getHost().getSpeed()); // Stutzen
		getHost().setVelocity(direction); // Resultat setzten
	}

	@Override
	public void update() {
		updateMovement();
	}
	
	@Override
	public void die() {
		super.die();
		((TierOneCoordinator) getSuperiorCoordinators()[0]).unRegisterMinion(this);
		MainZap.addScore(2);
		Hud.pushScore();
	}

	private void updateMovement() {

		SpeedVector directWayToPlayer = new SpeedVector(MainZap.getPlayer().getPosX() - getHost().getPosX(),
				MainZap.getPlayer().getPosY() - getHost().getPosY());
		getHost().setVelocity(SpeedVector.equalize(directWayToPlayer, getHost().getVelocity(), EQUALIZING_FORCE,
				getHost().getSpeed()));
		getHost().move();
	}

	public void leaveLine() {
		SpeedVector newVelocity = new SpeedVector(LINE_SPREAD_FAC * ((float) (Math.random() - 0.5)),
				LINE_SPREAD_FAC * ((float) (Math.random() - 0.5)));
		newVelocity.add(getHost().getVelocity().normalize_fast());
		newVelocity.scaleToLength(getHost().getSpeed());
		getHost().setVelocity(newVelocity);
	}

}
