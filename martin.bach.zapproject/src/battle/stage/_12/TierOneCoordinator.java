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

	// Combat-State-Gedöns (regelt wieviele kleine 3-ecke gerade auf den Spieler
	// zufliegen sollen)
	private DeltaEnemy toSplitForState2 = null;
	private DeltaEnemy toSplitForState3 = null;
	private DeltaEnemy toSplitForState4 = null;
	private ScheduledList<DeltaEnemy> partsFromCurrentPhase = new ScheduledList<DeltaEnemy>();
	private byte posIdOfFirstTierII = 0;

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
		partsFromCurrentPhase.update();
		// Minions-leave-line
		if (timeToSpread == 0) {
			for (TierSixCoordinator m : minionCoordinators)
				m.leaveLine();
			timeToSpread = TIME_TO_SPREAD_MIN + MainZap.rand(TIME_TO_SPREAD_MAX - TIME_TO_SPREAD_MIN);
		} else
			timeToSpread--;

		// parts-from-current-combat-phase-Update:
		// Alle Toten aus der Liste entnehmen:
		for (DeltaEnemy e : partsFromCurrentPhase) {
			if (!e.isAlive())
				partsFromCurrentPhase.schedRemove(e);
		}
		partsFromCurrentPhase.update();

		// Checken ob die Nächste Combat-Phase eingeleutet werden muss
		if (combatState != 7 && combatState != 0 && partsFromCurrentPhase.size() == 0) {
			combatState++;
			if (combatState != 7)
				executeCombatPhase(combatState);
			else
				StageManager.getActiveStage().pass(); // Alle Phasen beendet
		}

	}

	public void registerTierTwoPartInPlace() {
		if (combatState == 0) { // Erster angekommener Tier II - Part
			combatState = 1; // neue Phase einläuten: von Neutral zu Combat-I
			executeCombatPhase(combatState);
		}
	}

	private void executeCombatPhase(byte phase) {
		
		System.out.println("[Debug] DeltaEnemy: executing combat phase " + phase);

		switch (phase) {
		case 1:

			// Abgebrochenes Stück finden
			DeltaEnemy tier2 = null;
			for (DeltaEnemy d : getBreakResults())
				if (d != null) {
					tier2 = d;
					break;
				}
			toSplitForState4 = tier2; // Vermerken für nächste Kombat-Zustände

			// Ziel: ein tier 5er komplett in 4 tier 6er auflösen

			// was rauslösen
			tier2.breakAt((byte) 1);
			DeltaEnemy tier3 = tier2.getCoordinator().getBreakResults()[0];
			toSplitForState3 = tier3; // Vermerken für nächste Kombat-Zustände
			tier3.update();
			// was rauslösen
			tier3.breakAt((byte) 1);
			DeltaEnemy tier4 = tier3.getCoordinator().getBreakResults()[0];
			toSplitForState2 = tier4; // Vermerken für nächsten Kombat-Zustand
			tier4.update();
			// was rauslösen
			tier4.breakAt((byte) 1);
			for (int i = 0; i != 4; i++)
				partsFromCurrentPhase
						.schedAdd(tier4.getCoordinator().getBreakResults()[0].getCoordinator().getBreakResults()[i]);
			// Durch break eines tier 4er's entsteht ein tier 5er. Diese brechen immer
			// automatisch in 4 tier 6er...

			break;
		case 2:

			toSplitForState2.breakAt((byte) 2);
			toSplitForState2.breakAt((byte) 4);
			// Bei 1 schon gebrochen, bei 3 automatisch
			// Minions registrieren
			for (int i = 0; i != 4; i++)
				partsFromCurrentPhase.schedAdd(
						toSplitForState2.getCoordinator().getBreakResults()[1].getCoordinator().getBreakResults()[i]);
			for (int i = 0; i != 4; i++)
				partsFromCurrentPhase.schedAdd(
						toSplitForState2.getCoordinator().getBreakResults()[2].getCoordinator().getBreakResults()[i]);
			for (int i = 0; i != 4; i++)
				partsFromCurrentPhase.schedAdd(
						toSplitForState2.getCoordinator().getBreakResults()[3].getCoordinator().getBreakResults()[i]);

			break;
		case 3:

			toSplitForState3.breakAt((byte) 2);
			toSplitForState3.breakAt((byte) 4);
			// Bei 1 schon gebrochen, bei 3 automatisch
			// Resultate brechen
			for (int i = 1; i != 4; i++)
				toSplitForState3.getCoordinator().getBreakResults()[i].update();
			toSplitForState3.getCoordinator().getBreakResults()[1].breakAll();
			toSplitForState3.getCoordinator().getBreakResults()[2].breakAll();
			toSplitForState3.getCoordinator().getBreakResults()[3].breakAll();
			// Minions registrieren
			for (int i = 1; i != 4; i++)
				for (int j = 0; j != 4; j++)
					for (int k = 0; k != 4; k++)
						partsFromCurrentPhase.schedAdd(toSplitForState3.getCoordinator().getBreakResults()[i]
								.getCoordinator().getBreakResults()[j].getCoordinator().getBreakResults()[k]);

			break;
		case 4:

			toSplitForState4.breakAt((byte) 2);
			toSplitForState4.breakAt((byte) 4);
			// Bei 1 schon gebrochen, bei 3 automatisch
			// Resultate brechen
			for (int i = 1; i != 4; i++) // Positions update zwischen register und bruch
				toSplitForState4.getCoordinator().getBreakResults()[i].update();
			toSplitForState4.getCoordinator().getBreakResults()[1].breakAll();
			toSplitForState4.getCoordinator().getBreakResults()[2].breakAll();
			toSplitForState4.getCoordinator().getBreakResults()[3].breakAll();
			for (int i = 1; i != 4; i++) // Positions update zwischen register und bruch
				for (int j = 0; j != 4; j++)
					toSplitForState4.getCoordinator().getBreakResults()[i].getCoordinator().getBreakResults()[j]
							.update();
			// Weiter brechen. Jetzt entstehen Tier Ver (->> Tier VIer)
			for (int i = 1; i != 4; i++) // Positions update zwischen register und bruch
				for (int j = 0; j != 4; j++)
					toSplitForState4.getCoordinator().getBreakResults()[i].getCoordinator().getBreakResults()[j]
							.breakAll();

			// Minions registrieren
			for (int i = 1; i != 4; i++)
				for (int j = 0; j != 4; j++)
					for (int k = 0; k != 4; k++)
						for (int l = 0; l != 4; l++)
							partsFromCurrentPhase.schedAdd(toSplitForState4.getCoordinator().getBreakResults()[i]
									.getCoordinator().getBreakResults()[j].getCoordinator().getBreakResults()[k]
											.getCoordinator().getBreakResults()[l]);

			break;
		case 5:

			// Part rausfischen (darf nicht der schon weggeholzte Part sein)
			DeltaEnemy tierIIres = null;
			if (posIdOfFirstTierII == 1) {
				getHost().breakAt((byte) 2);
				tierIIres = getBreakResults()[1];
			} else {
				getHost().breakAt((byte) 1);
				tierIIres = getBreakResults()[0];
			}

			// Updaten und Breaken
			tierIIres.update();
			tierIIres.breakAll();
			for (int i = 0; i != 4; i++) // Results-updaten (Tier IIIer)
				tierIIres.getCoordinator().getBreakResults()[i].update();

			for (int i = 0; i != 4; i++) // Tier IIIer breaken
				tierIIres.getCoordinator().getBreakResults()[i].breakAll();

			for (int i = 0; i != 4; i++) // Tier IVer updaten
				for (int j = 0; j != 4; j++)
					tierIIres.getCoordinator().getBreakResults()[i].getCoordinator().getBreakResults()[j].update();

			for (int i = 0; i != 4; i++) // Tier IVer breaken (->> Tier Ver)
				for (int j = 0; j != 4; j++)
					tierIIres.getCoordinator().getBreakResults()[i].getCoordinator().getBreakResults()[j].breakAll();

			// Minions registrieren
			for (int i = 1; i != 4; i++)
				for (int j = 0; j != 4; j++)
					for (int k = 0; k != 4; k++)
						for (int l = 0; l != 4; l++)
							partsFromCurrentPhase.schedAdd(tierIIres.getCoordinator().getBreakResults()[i]
									.getCoordinator().getBreakResults()[j].getCoordinator().getBreakResults()[k]
											.getCoordinator().getBreakResults()[l]);

			break;
		case 6:

			// Totale Eskalation: Alles breaken was über ist

			// Zu Tier IIern
			getHost().breakAll();
			for (int i = 0; i != 4; i++)
				getBreakResults()[i].update();

			// Zu Tier IIIern
			for (int i = 1; i != 4; i++)
				getBreakResults()[i].breakAll();
			for (int i = 1; i != 4; i++)
				for (int j = 0; j != 4; j++)
					getBreakResults()[i].getCoordinator().getBreakResults()[j].update();

			// Zu Tier IVern
			for (int i = 1; i != 4; i++)
				for (int j = 0; j != 4; j++)
					getBreakResults()[i].getCoordinator().getBreakResults()[j].breakAll();
			for (int i = 1; i != 4; i++)
				for (int j = 0; j != 4; j++)
					for (int k = 0; k != 4; k++)
						getBreakResults()[i].getCoordinator().getBreakResults()[j].getCoordinator().getBreakResults()[k]
								.update();
			
			// Zu Tier Vern
			for (int i = 1; i != 4; i++)
				for (int j = 0; j != 4; j++)
					for (int k = 0; k != 4; k++)
						getBreakResults()[i].getCoordinator().getBreakResults()[j].getCoordinator().getBreakResults()[k]
								.breakAll();
			

			// Minions registrieren
			for (int i = 1; i != 4; i++)
				for (int j = 0; j != 4; j++)
					for (int k = 0; k != 4; k++)
						for (int l = 0; l != 4; l++)
							for (int m = 0; m != 4; m++)
								partsFromCurrentPhase.schedAdd(
										getBreakResults()[i].getCoordinator().getBreakResults()[j].getCoordinator()
												.getBreakResults()[k].getCoordinator().getBreakResults()[l]
														.getCoordinator().getBreakResults()[m]);

			break;
		default:
			throw new RuntimeException("Unknown phase: " + phase);
		}
	}

	@Override
	public void die() {
		super.die();
		MainZap.getMainLoop().addTask(new Runnable() {

			@Override
			public void run() {

				update();

				if (combatState == 7)
					MainZap.getMainLoop().removeTask(this, true);

			}
		}, true);
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
		if (tierTwoPartsDepart == 1)
			posIdOfFirstTierII = posId;
	}

	public int getTierTwoPartsDepart() {
		return tierTwoPartsDepart;
	}

}
