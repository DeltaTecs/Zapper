package battle.stage;

import corecase.MainZap;
import gui.Hud;
import ingameobjects.InteractiveObject;

public abstract class StageManager {

	private static final int WARP_DURATION = MainZap.inTicks(3000);

	private static Stage activeStage;
	private static int currentLvl = 0;
	private static int amountActiveIntros = 0;
	private static boolean nextStageReachable = true;

	public static void setUp(int lvl) {

		switch (lvl) {
		case 0:
			setUpLvlZero();
			break;
		case 1:
			setUpLvlOne();
			break;
		case 2:
			setUpLvlTwo();
			break;
		case 3:
			setUpLvlThree();
			break;
		case 4:
			setUpLvlFour();
			break;
		case 5:
			setUpLvlFive();
			break;
		case 6:
			setUpLvlSix();
			break;
		case 7:
			setUpLvlSeven();
			break;
		case 8:
			setUpLvlEight();
			break;
		case 9:
			setUpLvlNine();
			break;
		case 10:
			setUpLvlTen();
			break;
		default:
			System.err.println("[Err] Level not existing: lvl " + lvl);
			setUpLvlZero();
			break;

		}

	}

	public static void jumpToNextStage() {
		if (!nextStageReachable)
			return; // Spam-Schutz
		nextStageReachable = false;
		MainZap.getPlayer().enterWarp();

		MainZap.getMainLoop().scheduleTask(new Runnable() {

			@Override
			public void run() {

				MainZap.getPlayer().exitWarp();
				setUp(currentLvl + 1);
				Hud.setBlending(0);
				nextStageReachable = true;
			}

		}, WARP_DURATION, false);

	}

	private static void setUpLvlZero() {
		clearStage();
		activeStage = new Stage0();
		registerStage();
	}

	private static void setUpLvlOne() {
		clearStage();
		activeStage = new Stage1();
		registerStage();
	}

	private static void setUpLvlTwo() {
		clearStage();
		activeStage = new Stage2();
		registerStage();
	}

	private static void setUpLvlThree() {
		clearStage();
		activeStage = new Stage3();
		registerStage();
	}

	private static void setUpLvlFour() {
		clearStage();
		activeStage = new Stage4();
		registerStage();
	}

	private static void setUpLvlFive() {
		clearStage();
		activeStage = new Stage5();
		registerStage();
	}

	private static void setUpLvlSix() {
		clearStage();
		activeStage = new Stage6();
		registerStage();
	}

	private static void setUpLvlSeven() {
		clearStage();
		activeStage = new Stage7();
		registerStage();
	}

	private static void setUpLvlEight() {
		clearStage();
		activeStage = new Stage8();
		registerStage();
	}

	private static void setUpLvlNine() {
		clearStage();
		activeStage = new Stage9();
		registerStage();
	}

	private static void setUpLvlTen() {
		clearStage();
		activeStage = new Stage10();
		registerStage();
	}

	private static void registerStage() {
		if (activeStage != null) {
			if (MainZap.FINAL_RUN)
				new StageIntro(activeStage).register();
			MainZap.getMap().addUpdateElement(activeStage);
			currentLvl = activeStage.getLvl();
		}
	}

	private static void clearStage() {
		InteractiveObject.removeAllStageBound(); // Alle Objekte rausnehmen
		if (activeStage != null) {
			// Stage-Update entfernen
			MainZap.getMap().removeUpdateElement(activeStage);
			// Stage-Bound-Objekte entfernen
			MainZap.getMap().getUpdateElements().schedRemoveAll(activeStage.getUpdateTasks());
			MainZap.getMap().getForegroundPaintElements().schedRemoveAll(activeStage.getPaintingTasks());
			MainZap.getMap().getBackgroundPaintElements().schedRemoveAll(activeStage.getPaintingTasks());

			// Stage null setzten
			activeStage = null;
		}
		System.gc(); // Garbage-Collector, weil groﬂe Datenmenge auf einmal
						// nicht mehr gebraucht
		MainZap.getMap().rebuildBgParticles();
	}

	public static Stage getActiveStage() {
		return activeStage;
	}

	public static int getAmountActiveIntros() {
		return amountActiveIntros;
	}

	public static void setAmountActiveIntros(int amountActiveIntros) {
		StageManager.amountActiveIntros = amountActiveIntros;
	}

}
