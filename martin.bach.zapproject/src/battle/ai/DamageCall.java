package battle.ai;

import battle.CombatObject;
import battle.projectile.Projectile;

public interface DamageCall extends Call {

	public void damage(CombatObject source, Projectile proj, int dmg);

}
