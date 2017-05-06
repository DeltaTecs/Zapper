package battle;

import collision.CollisionInformation;
import ingameobjects.InteractiveObject;

public class CombatObject extends InteractiveObject {

	private boolean alive = true;

	public CombatObject(CollisionInformation collisionInfo, boolean stageBound, boolean background) {
		super(collisionInfo, stageBound, background);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}
