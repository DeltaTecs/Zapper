package battle.stage._12;

import corecase.MainZap;

public class TierTwoCoordinator extends DeltaCoordinator {

	private static final int TIME_OF_DRIFT = MainZap.inTicks(12000);
	private static final float SPEED_DRIFT = 0.15f;

	private int timeTillStop = TIME_OF_DRIFT;
	private boolean partInPlace = false;

	public TierTwoCoordinator(DeltaEnemy host, TierOneCoordinator superiorCoordinator) {
		super(host, superiorCoordinator);

	}

	@Override
	public void subinit() {
		super.subinit();
		// Drift initialisieren
		getHost().getVelocity().aimFor(getHost().getPosX(), getHost().getPosY(), SPEED_DRIFT,
				getSuperiorCoordinators()[0].getHost().getPosX(), getSuperiorCoordinators()[0].getHost().getPosY());
		getHost().getVelocity().invert();
	}

	@Override
	public void update() {

		// Drift
		if (getHost().getPosIdLastInstance() != 3 && timeTillStop > 0) {
			getHost().move();
			timeTillStop--;

			partInPlace = timeTillStop == 0;
			if (partInPlace)
				((TierOneCoordinator) getSuperiorCoordinators()[0]).registerTierTwoPartInPlace();
		}

	}

	@Override
	public void die() {
		super.die();

		if (!partInPlace) // Noch nicht angekommen, aber schon zerholzt?
			((TierOneCoordinator) getSuperiorCoordinators()[0]).registerTierTwoPartInPlace();
	}

	@Override
	public void breakAt(byte posId, DeltaEnemy result) {
		super.breakAt(posId, result);
	}

	public boolean isPartInPlace() {
		return partInPlace;
	}

}
