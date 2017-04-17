package battle.collect;

import battle.enemy.Enemy;
import ingameobjects.InteractiveObject;

public interface SpawnEvent {

	public void spawn(int x, int y, Enemy e);

	public void spawn(int x, int y, InteractiveObject o);

}
