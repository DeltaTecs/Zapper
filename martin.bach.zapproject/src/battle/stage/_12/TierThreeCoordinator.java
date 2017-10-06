package battle.stage._12;

public class TierThreeCoordinator extends DeltaCoordinator {

	private int tierFourPartsAppart = 0;

	public TierThreeCoordinator(DeltaEnemy host, TierTwoCoordinator superior) {
		super(host, superior);
	}

	@Override
	public void update() {

	}

	@Override
	public void breakAt(byte posId, DeltaEnemy result) {
		super.breakAt(posId, result);
		tierFourPartsAppart++;
	}

	public int getTierFourPartsAppart() {
		return tierFourPartsAppart;
	}

}
