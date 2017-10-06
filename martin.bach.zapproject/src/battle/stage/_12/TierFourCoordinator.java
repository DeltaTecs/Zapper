package battle.stage._12;

public class TierFourCoordinator extends DeltaCoordinator {

	private int tierFivePartsAppart = 0;

	public TierFourCoordinator(DeltaEnemy host, TierThreeCoordinator superior) {
		super(host, superior);
	}

	@Override
	public void update() {

	}

	@Override
	public void breakAt(byte posId, DeltaEnemy result) {
		super.breakAt(posId, result);
		tierFivePartsAppart++;
	}

	public int getTierFivePartsAppart() {
		return tierFivePartsAppart;
	}

}
