package battle.stage._12;

public class TierFiveCoordinator extends DeltaCoordinator {

	private int tierSixPartsAppart = 0;

	public TierFiveCoordinator(DeltaEnemy host, TierFourCoordinator superior) {
		super(host, superior);
	}

	@Override
	public void update() {

	}

	@Override
	public void breakAt(byte posId, DeltaEnemy result) {
		super.breakAt(posId, result);
		tierSixPartsAppart++;
	}

	public int getTierSixPartsAppart() {
		return tierSixPartsAppart;
	}

}
