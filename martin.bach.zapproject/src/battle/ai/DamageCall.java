package battle.ai;

import battle.projectile.Projectile;
import ingameobjects.InteractiveObject;

public interface DamageCall extends Call {

	public void damage(InteractiveObject source, Projectile proj, int dmg);

}
