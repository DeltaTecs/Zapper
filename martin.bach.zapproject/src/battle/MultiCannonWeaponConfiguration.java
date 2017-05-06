package battle;

import java.awt.Point;

import battle.enemy.Enemy;
import battle.projectile.Projectile;
import collision.Grid;
import ingameobjects.Player;
import lib.SpeedVector;

public class MultiCannonWeaponConfiguration extends WeaponConfiguration {

	private WeaponPositioning positioning;

	private byte weapons;
	private byte nextWeapon;

	public MultiCannonWeaponConfiguration(float maxCooldown, int range, WeaponPositioning positioning) {
		super(maxCooldown, range);
		this.positioning = positioning;
		nextWeapon = 0;
		weapons = positioning.getWeaponAmount();
	}

	@Override
	public void fire(Projectile p, Enemy e, boolean preAim) {

		Point wp = positioning.getRotated((float) e.getRotation(), nextWeapon);

		if (preAim) {

			if (!e.getAiProtocol().isMoving()) {
				float ticksTilImpact = Grid.distance(
						new Point((int) e.getShootingAim().getPosX(), (int) e.getShootingAim().getPosY()),
						new Point((int) e.getPosX(), (int) e.getPosY())) / p.getSpeed();
				p.launch(e.getLocX() + (int) wp.getX(), e.getLocY() + (int) wp.getY(),
						e.getShootingAim().getLocX() + (int) (e.getShootingAim().getVelocity().getX() * ticksTilImpact),
						e.getShootingAim().getLocY() + (int) (e.getShootingAim().getVelocity().getY() * ticksTilImpact),
						getRange(), e);
			} else {
				
				SpeedVector relativEnemyVelocity = new SpeedVector(
						e.getShootingAim().getVelocity().getX() - e.getVelocity().getX(),
						e.getShootingAim().getVelocity().getY() - e.getVelocity().getY());
				float ticksTilImpact = Grid.distance(
						new Point((int) e.getShootingAim().getPosX(), (int) e.getShootingAim().getPosY()),
						new Point((int) e.getPosX(), (int) e.getPosY())) / p.getSpeed();

				p.launch(e.getLocX() + (int) wp.getX(), e.getLocY() + (int) wp.getY(),
						e.getShootingAim().getLocX() + (int) (relativEnemyVelocity.getX() * ticksTilImpact),
						e.getShootingAim().getLocY() + (int) (relativEnemyVelocity.getY() * ticksTilImpact),
						e.getVelocity(), getRange(), e);
			}

		} else {
			if (!e.getAiProtocol().isMoving()) {
				p.launch(e.getLocX() + (int) wp.getX(), e.getLocY() + (int) wp.getY(), e.getShootingAim().getLocX(),
						e.getShootingAim().getLocY(), getRange(), e);
			} else {
				p.launch(e.getLocX() + (int) wp.getX(), e.getLocY() + (int) wp.getY(), e.getShootingAim().getLocX(),
						e.getShootingAim().getLocY(), e.getVelocity(), getRange(), e);
			}
		}

		p.register();
		resetCooldown();
	}

	public void fire(Projectile proj, Player p) {

		Point wp = positioning.getRotated((float) p.getRotation(), nextWeapon);

		proj.launch(p.getLocX() + (int) wp.getX(), p.getLocY() + (int) wp.getY(), p.getMapAimX(), p.getMapAimY(),
				p.getVelocity(), getRange(), p);
		proj.register();
		resetCooldown();
	}

	@Override
	public void resetCooldown() {
		super.resetCooldown();
		nextWeapon++;
		if (nextWeapon > weapons - 1)
			nextWeapon = 0;
	}

}
