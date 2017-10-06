package battle.stage._12;

import lib.Updateable;

public class DeltaCoordinator implements Updateable {

	private DeltaEnemy host;
	private DeltaCoordinator[] superiorCoordinators = null;
	private DeltaEnemy[] breakResults = new DeltaEnemy[4];
	private boolean subInitialised = false; // Debug

	public DeltaCoordinator(DeltaEnemy host, DeltaCoordinator superior) {
		super();
		this.host = host;

		if (superior != null) { // Übergeordnete Koordinatoren verzeichnen.
			superiorCoordinators = new DeltaCoordinator[superior.getSuperiorCoordinators().length + 1];
			for (int i = 0; i != superior.getSuperiorCoordinators().length; i++)
				superiorCoordinators[i] = superior.getSuperiorCoordinators()[i];
			superiorCoordinators[superiorCoordinators.length - 1] = superior;
		} else
			superiorCoordinators = new DeltaCoordinator[] {}; // Keine Superioren Koordinatoren

	}

	/**
	 * Overwritten method for further initialisation. Execution nessecary!
	 */
	public void subinit() {
		subInitialised = true;
	}

	public static DeltaCoordinator getCoordinator(DeltaEnemy host, DeltaCoordinator superiorCoordinator) {

		switch (host.getInstance()) {

		case 0:
			return new TierOneCoordinator(host);
		case 1:
			return new TierTwoCoordinator(host, (TierOneCoordinator) superiorCoordinator);
		case 2:
			return new TierThreeCoordinator(host, (TierTwoCoordinator) superiorCoordinator);
		case 3:
			return new TierFourCoordinator(host, (TierThreeCoordinator) superiorCoordinator);
		case 4:
			return new TierFiveCoordinator(host, (TierFourCoordinator) superiorCoordinator);
		case 5:
			return new TierSixCoordinator(host, (TierFiveCoordinator) superiorCoordinator);
		default:
			return new TierSixCoordinator(host, (TierFiveCoordinator) superiorCoordinator);
		}
	}

	@Override
	public void update() {

	}

	public void die() {
	}

	public boolean isSubInitialised() {
		return subInitialised;
	}

	public void setSubInitialised(boolean subInitialised) {
		this.subInitialised = subInitialised;
	}

	public void breakAt(byte posId, DeltaEnemy result) {
		breakResults[posId - 1] = result;
	}

	public DeltaEnemy getHost() {
		return host;
	}

	public DeltaCoordinator[] getSuperiorCoordinators() {
		return superiorCoordinators;
	}

	public DeltaEnemy[] getBreakResults() {
		return breakResults;
	}
	
	

}
