package gui.extention;

import java.awt.Graphics2D;
import java.awt.Point;

import battle.WeaponPositioning;
import battle.enemy.Enemy;
import battle.projectile.Projectile;
import collision.Collideable;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import ingameobjects.Player;

// Geht davon aus, dass der Spieler nicht mehr als 1 Kannone hat
public class MirrorImage extends Enemy {

	private static final boolean STAGE_BOUND = false;
	private static final float ALPHA_START = 0.9f;
	private static final float ALPHA_DELTA_DIEING = 0.02f;
	private static final int MAX_DURATION = MainZap.inTicks(15000); // 15 Sek.

	private WeaponPositioning weaponPositioning;
	private int duration = MAX_DURATION;
	private boolean dieing = false;
	private float alpha = 0.8f;
	private int hp;
	private int dx;
	private int dy;

	public MirrorImage() {
		super(0, 0, 0, MainZap.getPlayer().getTexture(), 0, new CollisionInformation(MainZap.getPlayer().getCollisionInfo().getRadius(),
				CollisionType.COLLIDE_AS_PLAYER, false), null, null, 0, null, 0, 0, 0, false);
		super.setFriend(true);
		super.setStageBound(STAGE_BOUND);
		weaponPositioning = MainZap.getPlayer().getActiveWeaponPositioning();
		register();
		hp = MainZap.getPlayer().getMaxHp() / 2;
	}

	public void fire() {

		Player p = MainZap.getPlayer();
		Point wp = weaponPositioning.getRotated((float) p.getRotation(), 0);
		int x = (int) (p.getPosX() + (float) wp.getX() + dx);
		int y = (int) (p.getPosY() + (float) wp.getY() + dy);
		int aimX = p.getMapAimX() + dx;
		int aimY = p.getMapAimY() + dy;

		Projectile proj = new Projectile(p.getBulletSpeed(), p.getProjDesign(), p.getBulletDamage());
		p.applyBoostsOnProjectile(proj);

		proj.launch(x, y, aimX, aimY, p.getVelocity(), p.getProjRange(), this);
		proj.register();
	}

	@Override
	public void paint(Graphics2D g) {
	}

	@Override
	public void collide(Collideable c) {
		if (!(c instanceof Projectile))
			return;

		Projectile proj = (Projectile) c;
		hp -= proj.getDamage();
		if (hp <= 0) // verpuffen
			dieing = true;
	}

	@Override
	public void update() {

		setPosition(MainZap.getPlayer().getPosX() + dx, MainZap.getPlayer().getPosY() + dy);

		duration--;
		if (duration <= 0) {
			duration = 0;
			unRegister();
		}

		// Alpha updaten
		if (dieing) {
			alpha -= ALPHA_DELTA_DIEING;
		} else {
			alpha = ALPHA_START * ((float) duration / MAX_DURATION);
		}

		if (alpha <= 0) { // Nicht mehr da
			alpha = 0;
			unRegister();
		}

	}

	public WeaponPositioning getWeaponPositioning() {
		return weaponPositioning;
	}

	public void setWeaponPositioning(WeaponPositioning weaponPositioning) {
		this.weaponPositioning = weaponPositioning;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public boolean isDieing() {
		return dieing;
	}

	public void setDieing(boolean dieing) {
		this.dieing = dieing;
	}

	public float getAlpha() {
		return alpha;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	@Override
	public boolean isAlive() {
		return (alpha > 0.001) && (duration > 0);
	}
	
}
