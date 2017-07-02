package battle.ai;

import java.util.ArrayList;

import battle.CombatObject;
import battle.enemy.Enemy;
import battle.enemy.ShieldedEnemy;
import corecase.Cmd;
import corecase.MainZap;
import lib.SpeedVector;

public class FleeSingleProtocol extends AdvancedSingleProtocol {

	private float criticalPercentage;
	private boolean shield;

	public FleeSingleProtocol(float criticalPercentage, boolean shield) {
		super();
		this.criticalPercentage = criticalPercentage;
		this.shield = shield;
	}

	@Override
	public void updateMovement() {
		if (shield) {
			// Sicherheits-Abfrage
			if (!(getHost() instanceof ShieldedEnemy)) {
				Cmd.err("FleeSingleProtocol:28 _ Host not shielded.");
				return;
			}

			if (((ShieldedEnemy) getHost())
					.getShield() > (((ShieldedEnemy) getHost()).getMaxShield() * criticalPercentage)) {
				super.updateMovement(); // alles ok
				return;
			}
		} else if (getHost().getHealth() > (getHost().getMaxHealth() * criticalPercentage)) {
			super.updateMovement(); // alles ok
			return;
		}

		// Flüchten ---------------------
		ArrayList<CombatObject> enemys = new ArrayList<CombatObject>();

		if (!getHost().isFriend()) {
			if (getHost().distanceToPlayer() <= super.getLockOpticDetectionRange() && MainZap.getPlayer().isAlive()) {
				enemys.add(MainZap.getPlayer());
			}
		}

		// Umgebung holen
		ArrayList<Enemy> surrounding = MainZap.getGrid().getEnemySurrounding(getHost().getLocX(), getHost().getLocY(),
				super.getLockOpticDetectionRange());

		// Umgebung abgehen
		for (Enemy e : surrounding) {

			if (e.isFriend() == getHost().isFriend() || !e.isAlive())
				continue; // vor Keinem der eigenen Sippschaft oder Toten
							// flüchten

			// Da is was in Range
			if (e.isInRange(getHost(), super.getLockOpticDetectionRange()))
				enemys.add(e);
		}

		if (enemys.size() == 0)
			if (getLockOn() == null) {
				super.updateMovement();
				return;
			} else
				enemys.add(getLockOn());

		SpeedVector dTotal = new SpeedVector(0, 0); // Summe aller
													// Distanz-Vektoren
		for (CombatObject e : enemys) {
			dTotal.add(
					new SpeedVector(e.getPosX() - getHost().getPosX(), e.getPosY() - getHost().getPosY()).normalize());
		}

		dTotal.normalize();
		dTotal.multiply(getHost().getSpeed());
		dTotal.invert();
		getHost().setVelocity(dTotal);
		move();
	}

	public float getCriticalPercentage() {
		return criticalPercentage;
	}

	public void setCriticalPercentage(float criticalPercentage) {
		this.criticalPercentage = criticalPercentage;
	}

	public boolean isShield() {
		return shield;
	}

	public void setShield(boolean shield) {
		this.shield = shield;
	}
}
