package battle.stage;

import java.util.ArrayList;
import java.util.Random;

import corecase.MainZap;
import gui.Map;
import ingameobjects.InteractiveObject;
import library.PaintingTask;
import library.Updateable;

public class Stage implements Updateable {

	private static final Random RANDOM = new Random(System.currentTimeMillis());

	private int lvl;
	private String name;
	private String description;
	private StageDifficulty difficulty;
	private ArrayList<InteractiveObject> interactiveObjects = new ArrayList<InteractiveObject>();
	private ArrayList<PaintingTask> paintingTasks = new ArrayList<PaintingTask>();
	private ArrayList<Updateable> updateTasks = new ArrayList<Updateable>();
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
	}

	public void pass() {
		passed = true;
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

}
