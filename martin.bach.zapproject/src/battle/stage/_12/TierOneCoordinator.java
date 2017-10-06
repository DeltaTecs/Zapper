package battle.stage._12;

import battle.stage.StageManager;
import corecase.MainZap;
import lib.ScheduledList;

public class TierOneCoordinator extends DeltaCoordinator {

	private static final float ROTATION_SPEED = 0.0002f;
	private static final int TIME_TO_SPREAD_MIN = MainZap.inTicks(1000);
	private static final int TIME_TO_SPREAD_MAX = MainZap.inTicks(3000);

	private byte combatState = 0;
	private int tierTwoPartsDepart = 0;
	private int timeToSpread = TIME_TO_SPREAD_MAX;
	private ScheduledList<TierSixCoordinator> minionCoordinators = new ScheduledList<TierSixCoordinator>();

	public TierOneCoordinator(DeltaEnemy host) {
		super(host, null);
	}

	@Override
	public void update() {

		// Rotation-Update
		if (getHost().getPartsRemaining() == 4) {
			getHost().setRotation(getHost().getRotation() + ROTATION_SPEED);
			if (getHost().getRotation() >= Math.PI * 2)
				getHost().setRotation(getHost().getRotation() - Math.PI * 2);
		}

		// Listenupdate
		minionCoordinators.update();
		// Minions-leave-line
		if (timeToSpread == 0) {
			for (TierSixCoordinator m : minionCoordinators)
				m.leaveLine();
			timeToSpread = TIME_TO_SPREAD_MIN + MainZap.rand(TIME_TO_SPREAD_MAX - TIME_TO_SPREAD_MIN);
		} else
			timeToSpread--;

	}

	public void registerTierTwoPartInPlace() {
		if (combatState == 0) { // Erster angekommener Tier II - Part
			combatState = 1; // neue Phase einläuten: von Neutral zu Combat-I

			// Abgebrochenes Stück finden
			DeltaEnemy tier2 = null;
			for (DeltaEnemy d : getBreakResults())
				if (d != null) {
					tier2 = d;
					break;
				}

			// Ziel: ein tier 5er komplett in 4 tier 6er auflösen

			// was rauslösen
			tier2.breakAt((byte) 1);
			DeltaEnemy tier3 = tier2.getCoordinator().getBreakResults()[0];
			tier3.update();
			// was rauslösen
			tier3.breakAt((byte) 1);
			DeltaEnemy tier4 = tier3.getCoordinator().getBreakResults()[0];
			tier4.update();
			// was rauslösen
			tier4.breakAt((byte) 1);
			// Durch break eines tier 4er's entsteht ein tier 5er. Diese brechen immer
			// automatisch in 4 tier 6er...
		}
	}

	@Override
	public void die() {
		super.die();
		StageManager.getActiveStage().pass(); // Boss getötet
	}

	public void registerMinion(TierSixCoordinator c) {
		minionCoordinators.schedAdd(c);
	}

	public void unRegisterMinion(TierSixCoordinator c) {
		minionCoordinators.schedRemove(c);
	}

	@Override
	public void breakAt(byte posId, DeltaEnemy result) {
		super.breakAt(posId, result);
		tierTwoPartsDepart++;
	}

	public int getTierTwoPartsDepart() {
		return tierTwoPartsDepart;
	}

}
