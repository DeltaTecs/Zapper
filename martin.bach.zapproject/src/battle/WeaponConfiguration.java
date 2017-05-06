package battle;

import java.awt.Point;

import battle.enemy.Enemy;
import battle.projectile.Projectile;
import collision.Grid;
import ingameobjects.InteractiveObject;
import ingameobjects.Player;
import lib.SpeedVector;
import lib.Updateable;

public class WeaponConfiguration implements Updateable {

	private float maxCooldown;
	private float currentCooldown;
	private boolean ready;
	private int range;

	public WeaponConfiguration(float maxCooldown, int range) {
		this.maxCooldown = maxCooldown;
		this.range = range;
	}

	@Override
	public void update() {

		if (currentCooldown > 0) {
			currentCooldown--;
		} else if (!ready) {
			ready = true;
		}
	}

	public void fire(Projectile p, int aimx, int aimy, SpeedVector sourceVelo, InteractiveObject source) {

		if (sourceVelo == null) {
			p.launch(source.getLocX(), source.getLocY(), aimx, aimy, range, source);
		} else {
			p.launch(source.getLocX(), source.getLocY(), aimx, aimy, sourceVelo, range, source);
		}

		p.register();
		resetCooldown();

	}

	public void fire(Projectile p, Enemy e, boolean preAim) {

		if (preAim) {

			if (!e.getAiProtocol().isMoving()) {

				float ticksTilImpact = Grid.distance(
						new Point((int) e.getShootingAim().getPosX(), (int) e.getShootingAim().getPosY()),
						new Point((int) e.getPosX(), (int) e.getPosY())) / p.getSpeed();
				p.launch(e.getLocX(), e.getLocY(),
						e.getShootingAim().getLocX() + (int) (e.getShootingAim().getVelocity().getX() * ticksTilImpact),
						e.getShootingAim().getLocY() + (int) (e.getShootingAim().getVelocity().getY() * ticksTilImpact),
						e.getVelocity(), range, e);
			} else {

				SpeedVector relativEnemyVelocity = new SpeedVector(
						e.getShootingAim().getVelocity().getX() - e.getVelocity().getX(),
						e.getShootingAim().getVelocity().getY() - e.getVelocity().getY());
				float ticksTilImpact = Grid.distance(
						new Point((int) e.getShootingAim().getPosX(), (int) e.getShootingAim().getPosY()),
						new Point((int) e.getPosX(), (int) e.getPosY())) / p.getSpeed();

				p.launch(e.getLocX(), e.getLocY(),
						e.getShootingAim().getLocX() + (int) (relativEnemyVelocity.getX() * ticksTilImpact),
						e.getShootingAim().getLocY() + (int) (relativEnemyVelocity.getY() * ticksTilImpact),
						e.getVelocity(), range, e);
			}

		} else {
			if (!e.getAiProtocol().isMoving()) {
				p.launch(e.getLocX(), e.getLocY(), e.getShootingAim().getLocX(), e.getShootingAim().getLocY(), range,
						e);
			} else {
				p.launch(e.getLocX(), e.getLocY(), e.getShootingAim().getLocX(), e.getShootingAim().getLocY(),
						e.getVelocity(), range, e);
			}
		}

		p.register();
		resetCooldown();
	}

	public void fire(Projectile proj, Player p) {

		proj.launch(p.getLocX(), p.getLocY(), p.getMapAimX(), p.getMapAimY(), p.getVelocity(), range, p);
		proj.register();
		resetCooldown();
	}

	public void resetCooldown() {
		currentCooldown = maxCooldown;
		ready = false;
	}

	public float getCurrentCooldown() {
		return currentCooldown;
	}

	public void setCurrentCooldown(int currentCooldown) {
		this.currentCooldown = currentCooldown;
	}

	public float getMaxCooldown() {
		return maxCooldown;
	}

	public void setMaxCooldown(float maxCooldown) {
		this.maxCooldown = maxCooldown;
	}

	public boolean isReady() {
		return ready;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

}
