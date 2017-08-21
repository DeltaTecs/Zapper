package battle.stage._12;

import battle.projectile.Projectile;
import collision.Collideable;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;

/**
 * Ein Dreieck-Platzhalter, von dem es in jedem DeltaEnemy 4 geben sollte
 * (Randbereiche und Mittelstück). Diese stellen gemeinsam die Hitbox des
 * Gegener und insizieren einen Wegbruch durch eigegen Tot.
 * 
 * @author Martin
 *
 */
public class DeltaDummy implements Collideable {

	private float dx, dy, posX, posY, radius;
	private CollisionInformation collInfo;
	private byte id;
	private int hp;
	private DeltaEnemy host;

	public DeltaDummy(float dx, float dy, float radius, byte id, int hp, DeltaEnemy host) {
		super();
		this.dx = dx;
		this.dy = dy;
		this.radius = radius;
		this.id = id;
		this.hp = hp;
		this.host = host;
		collInfo = new CollisionInformation(radius, CollisionType.COLLIDE_WITH_FRIENDS, false);
	}

	@Override
	public void collide(Collideable c) {
		if (c instanceof Projectile) {

			if (hp > 0) { // Beschädigen
				hp -= ((Projectile) c).getDamage();

				if (hp <= 0) {
					host.breakAt(id); // Wegbruch registrieren
					unRegister(); // Dummy unregister
				}

			}
		}
	}

	public void register() {
		MainZap.getGrid().add(this); // Dummy register
	}

	public void unRegister() {
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
