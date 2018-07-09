package battle.stage;

import java.util.ArrayList;
import java.util.Random;

import battle.CombatObject;
import battle.ai.AiProtocol;
import battle.ai.DieCall;
import battle.enemy.Enemy;
import battle.looting.Container;
import collision.Collideable;
import corecase.MainZap;
import gui.Hud;
import gui.Map;
import ingameobjects.InteractiveObject;
import ingameobjects.Player;
import lib.PaintingTask;
import lib.Updateable;

public class Stage implements Updateable {

	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private static final int MAX_AMOUNT_CONTAINER = 2;

	private int lvl;
	private String name;
	private String description;
	private StageDifficulty difficulty;
	private ArrayList<InteractiveObject> interactiveObjects = new ArrayList<InteractiveObject>();
	private ArrayList<PaintingTask> paintingTasks = new ArrayList<PaintingTask>();
	private ArrayList<Updateable> updateTasks = new ArrayList<Updateable>();
	private ArrayList<Collideable> collisionTasks = new ArrayList<Collideable>();
	private boolean passed = false;

	public Stage(int lvl, String name, StageDifficulty difficulty, String description) {
		super();
		this.lvl = lvl;
		this.name = name;
		this.difficulty = difficulty;
		this.description = description;
		MainZap.getPlayer().setPosition(Map.SIZE / 2, Map.SIZE / 2);

		passed = MainZap.debug; // in Debug alle Skippen können

	}

	public Stage(int lvl, String name, StageDifficulty difficulty, String description, int x, int y) {
		super();
		this.lvl = lvl;
		this.name = name;
		this.difficulty = difficulty;
		this.description = description;
		MainZap.getPlayer().setPosition(x, y);

		passed = MainZap.debug; // in Debug alle Skippen können
		
		// Container spawnen
		int containers = MainZap.rand(MAX_AMOUNT_CONTAINER + 1);
		for (int i = 0; i != containers; i++)
			Container.spawn(this);
	}

	public void applyRemoveTask(final ArrayList<CombatObject> list, final CombatObject subject) {

		final Stage stage = this;

		if (subject instanceof Player) {

			MainZap.getMainLoop().addTask(new Runnable() {
				@Override
				public void run() {
					if (!MainZap.getPlayer().isAlive() || StageManager.getActiveStage() != stage) {
						list.remove(subject);
						MainZap.getMainLoop().removeTask(this, true);
					}

				}
			}, true);

		} else if (subject instanceof Enemy) {

			Enemy e = (Enemy) subject;

			if (e.getAiProtocol() != null) { // Subjekt verfügt über AI

				e.getAiProtocol().addCall(AiProtocol.KEY_CALL_DIEING, new DieCall() {

					@Override
					public void die() {
						list.remove(subject);
					}
				});

			} else {
				// keine AI-Vorhanden. Manueller Die-Call

				MainZap.getMainLoop().addTask(new Runnable() {
					@Override
					public void run() {
						if (!subject.isAlive() || StageManager.getActiveStage() != stage) {
							list.remove(subject);
							MainZap.getMainLoop().removeTask(this, true);
						}

					}
				}, true);

			}

		} else // WTF ist das subject!!???
			throw new RuntimeException("remove-tasks can only be applyed on a player or an enemy");

	}


	public void pass() {
		passed = true;
		Hud.resetStagePassEffect();
	}

	public boolean isPassed() {
		return passed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLvl() {
		return lvl;
	}

	public String getName() {
		return name;
	}

	public StageDifficulty getDifficulty() {
		return difficulty;
	}

	public int rand(int bound) {
		return RANDOM.nextInt(bound);
	}

	@Override
	public void update() {
	}

	public ArrayList<InteractiveObject> getInteractiveObjects() {
		return interactiveObjects;
	}

	public ArrayList<PaintingTask> getPaintingTasks() {
		return paintingTasks;
	}

	public ArrayList<Updateable> getUpdateTasks() {
		return updateTasks;
	}

	public static Random getRandom() {
		return RANDOM;
	}

	public ArrayList<Collideable> getCollisionTasks() {
		return collisionTasks;
	}

}
