package battle.projectile;

import java.awt.Color;
import java.awt.Graphics2D;

import battle.Shockable;
import collision.Collideable;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import ingameobjects.InteractiveObject;
import lib.SpeedVector;

public class Projectile extends InteractiveObject implements Shockable {

	private float speed;
	private int size;
	private boolean square;
	private Color color;
	private int range;
	private int damage;
	private float travelDistance;
	private InteractiveObject sender;
	private boolean collided = false;

	/**
	 * Initialisiert das Projektil
	 */
	public Projectile(CollisionInformation information, float speed, int size, boolean square, Color color,
			int damage) {
		super(information, true, true); // true -> immer an Stage gebunden; true
										// -> immer Hintergrund
		this.speed = speed;
		this.size = size;
		this.square = square;
		this.color = color;
		this.damage = damage;
	}

	/**
	 * Initialisiert das Projektil
	 */
	public Projectile(float speed, ProjectileDesign design, int damage) {
		super(new CollisionInformation(design.getSize(), CollisionType.COLLIDE_WITH_ENEMYS, false), true, true);
		// true -> immer an Stage gebunden; true -> immer Hintergrund
		this.speed = speed;
		this.size = design.getSize();
		this.square = design.isSquare();
		this.color = design.getColor();
		this.damage = damage;
	}

	/**
	 * Feuert das Projektil ab
	 */
	public void launch(int posX, int posY, int aimX, int aimY, int range, InteractiveObject sender) {
		travelDistance = 0;
		this.range = range;
		this.sender = sender;
		setPosition(posX, posY);
		getVelocity().aimFor(posX, posY, speed, aimX, aimY);
	}

	/**
	 * Feuert das Projektil ab
	 */
	public void launch(int posX, int posY, SpeedVector projVelo, int range, InteractiveObject sender) {
		travelDistance = 0;
		this.range = range;
		this.sender = sender;
		setPosition(posX, posY);
		setVelocity(projVelo);
	}

	/**
	 * Feuert das Projektil ab
	 */
	public void launch(int posX, int posY, int aimX, int aimY, SpeedVector base, int range, InteractiveObject sender) {
		travelDistance = 0;
		this.range = range;
		this.sender = sender;
		setPosition(posX, posY);
		getVelocity().aimFor(posX, posY, speed, aimX, aimY);
		getVelocity().setX(getVelocity().getX() + base.getX());
		getVelocity().setY(getVelocity().getY() + base.getY());
	}

	@Override
	public void update() {
		updatePosition();
		updateRange();
		super.update();
	}

	private void updateRange() {

		if (travelDistance > range) {
			unRegister();
			return;
		}

		travelDistance += speed;
	}

	private void updatePosition() {
		moveX(getVelocity().getX());
		moveY(getVelocity().getY());
	}

	@Override
	public void paint(Graphics2D g) {

		// Von Kontext Karte zu Kontext EigenPos
		int dx = getLocX();
		int dy = getLocY();
		g.translate(dx, dy);

		g.setColor(color);
		if (square) {
			g.fillRect(-size, -size, 2 * size, 2 * size);
		} else {
			g.fillOval(-size, -size, 2 * size, 2 * size);
		}

		// Zurück zu Karten Kontext
		g.translate(-dx, -dy);
	}

	@Override
	public void collide(Collideable c) {
		// Kollision
		if (c instanceof Projectile)
			return; // Keine Kollision mit andere Projektilen erwünscht

		unRegister();
	}

	@Override
	public void shock() {
		getVelocity().multiply(0.4f);
		if (MainZap.rand(2) == 0)
			unRegister();
	}

	@Override
	public Object getClone() {
		return new Projectile(getCollisionInfo(), speed, size, square, color, getDamage());
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getRadius() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isSquare() {
		return square;
	}

	public void setSquare(boolean square) {
		this.square = square;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public InteractiveObject getSender() {
		return sender;
	}

	public void setSender(InteractiveObject sender) {
		this.sender = sender;
	}

	public boolean collided() {
		return collided;
	}

	public boolean isCollided() {
		return collided;
	}

	public void setCollided(boolean collided) {
		this.collided = collided;
	}

}
