package battle.stage._12;

import java.awt.Color;

import battle.projectile.Projectile;
import battle.projectile.ProjectileDesign;
import collision.Collideable;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.Hud;
import gui.PlayerDamageIndicator;
import ingameobjects.Player;
import lib.SpeedVector;

/**
 * Ein Dreieck-Platzhalter, von dem es in jedem DeltaEnemy 4 geben sollte
 * (Randbereiche und Mittelstück). Diese stellen gemeinsam die Hitbox des
 * Gegener und insizieren einen Wegbruch durch eigegen Tot.
 * 
 * @author Martin
 *
 */
public class DeltaDummy implements Collideable {

	private static final int DUMMY_DAMAGE = 5;

	private float dx, dy, posX, posY, radius;
	private CollisionInformation collInfo;
	private byte id;
	private int hp;
	private int startHp;
	private DeltaEnemy host;
	private boolean removed = false;

	public DeltaDummy(float dx, float dy, float radius, byte id, int hp, DeltaEnemy host) {
		super();
		this.dx = dx;
		this.dy = dy;
		this.radius = radius;
		this.id = id;
		this.hp = hp;
		this.startHp = hp;
		this.host = host;
		collInfo = new CollisionInformation(radius, CollisionType.COLLIDE_WITH_FRIENDS, false);
	}

	@Override
	public void collide(Collideable c) {

		if (removed)
			return;

		if (c instanceof Player) {
			MainZap.getPlayer().trueDamage(DUMMY_DAMAGE);
			Projectile pseudoProj = new Projectile(1000, new ProjectileDesign(2, false, Color.BLACK),
					DUMMY_DAMAGE * 10);
			pseudoProj.setVelocity(
					new SpeedVector((int) (1000 * Math.random()) - 500, (int) (1000 * Math.random()) - 500));
			PlayerDamageIndicator.register(pseudoProj);
			Hud.pushBlackBlending();
		} else if (c instanceof Projectile) {

			if (hp > 0) { // Beschädigen
				hp -= ((Projectile) c).getDamage();

				if (hp <= 0)
					if (!host.breakAt(id)) // Wegbruch registrieren und nach Reset fragen
						hp = startHp; // Reset gefordert

			}
		}
	}

	/**
	 * Ersetzt Dummy durch neues DeltaEnemy
	 */
	public void replace() {
		DeltaEnemy child = new DeltaEnemy(getHost().getBorderlen() / 2, getHost().getInstance() + 1);
		child.setPosition(posX, posY);
		child.setRotation((id != 3) ? (getHost().getRotation()) : (getHost().getRotation() + Math.PI));
		child.register();
		unRegister();
	}

	public void register() {
		MainZap.getGrid().add(this); // Dummy register
	}

	public void unRegister() {
		removed = true;
		MainZap.getGrid().remove(this); // Dummy unregister
	}

	@Override
	public int[] getLocation() {
		return new int[] { (int) posX, (int) posY };
	}

	@Override
	public CollisionInformation getInformation() {
		return collInfo;
	}

	@Override
	public void push(Collideable from, float speed) {
		return; // Interaktion untersagt
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public DeltaEnemy getHost() {
		return host;
	}

	public float getRadius() {
		return radius;
	}

	public byte getId() {
		return id;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void setPosition(float x, float y) {
		posX = x;
		posY = y;
	}

}
