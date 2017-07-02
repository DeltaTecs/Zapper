package battle;

import collision.CollisionInformation;
import ingameobjects.InteractiveObject;

public class CombatObject extends InteractiveObject {

	private boolean alive = true;
	private boolean friend = false;

	public CombatObject(CollisionInformation collisionInfo, boolean stageBound, boolean background, boolean friend) {
		super(collisionInfo, stageBound, background);
		this.friend = friend;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isFriend() {
		return friend;
	}

	public void setFriend(boolean friend) {
		this.friend = friend;
	}

	
}
