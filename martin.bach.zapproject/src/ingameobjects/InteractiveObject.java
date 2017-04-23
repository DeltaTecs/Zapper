package ingameobjects;

import java.awt.Graphics2D;
import java.util.ArrayList;

import collision.Collideable;
import collision.CollisionInformation;
import corecase.MainZap;
import gui.Frame;
import gui.Map;
import library.PaintingTask;
import library.SpeedVector;
import library.Updateable;

public class InteractiveObject implements Collideable, PaintingTask, Updateable, CloneableObject {

	private static ArrayList<InteractiveObject> listedObjects = new ArrayList<InteractiveObject>();

	private CollisionInformation collisionInfo;
	private float posX;
	private float posY;
	private SpeedVector velocity = new SpeedVector(0, 0);
	private boolean stageBound;
	private boolean background;

	public InteractiveObject(CollisionInformation collisionInfo, boolean stageBound, boolean background) {
		this.collisionInfo = collisionInfo;
		this.stageBound = stageBound;
		this.background = background;
	}

	public void register() {
		MainZap.getMap().addPaintElement(this, background);
		MainZap.getMap().addUpdateElement(this);
		MainZap.getGrid().add(this);
		listedObjects.add(this);
	}

	public void unRegister() {
		MainZap.getMap().removePaintElement(this, background);
		MainZap.getMap().removeUpdateElement(this);
		MainZap.getGrid().remove(this);
		listedObjects.remove(this);
	}

	@Override
	public void update() {
		if (isFarAway()) { // Zu weit weg. Entfernen. Sonst lag
			unRegister();
		}

	}

	@Override
	public void paint(Graphics2D g) {

	}

	@Override
	public CollisionInformation getInformation() {
		return collisionInfo;
	}

	@Override
	public void collide(Collideable c) {

	}

	// Bitte in unterklassen überschreiben!!!
	@Override
	public Object getClone() {
		return new InteractiveObject(collisionInfo, stageBound, background);
	}

	@Override
	public int[] getLocation() {
		return new int[] { (int) posX, (int) posY };
	}

	public static void removeAllStageBound() {

		ArrayList<InteractiveObject> bufferList = new ArrayList<InteractiveObject>(listedObjects);
		for (InteractiveObject o : bufferList) {
			if (!o.stageBound)
				continue;
			o.unRegister();

		}

	}

	public boolean isBackground() {
		return background;
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

	public CollisionInformation getCollisionInfo() {
		return collisionInfo;
	}

	public int getLocX() {
		return (int) posX;
	}

	public int getLocY() {
		return (int) posY;
	}

	public void moveX(float dx) {
		posX += dx;
	}

	public void moveY(float dy) {
		posY += dy;
	}

	public void move(SpeedVector v) {
		posX += v.getX();
		posY += v.getY();
	}

	public void setPosition(float x, float y) {
		posX = x;
		posY = y;
	}

	public int distanceToPlayer() {
		return (int) Math
				.sqrt(((getPosX() - MainZap.getPlayer().getPosX()) * (getPosX() - MainZap.getPlayer().getPosX()))
						+ ((getPosY() - MainZap.getPlayer().getPosY()) * (getPosY() - MainZap.getPlayer().getPosY())));
	}

	public boolean isFarAway() {
		return (posX < -Frame.SIZE || posX > Frame.SIZE + Map.SIZE || posY < -Frame.SIZE
				|| posY > Frame.SIZE + Map.SIZE);
	}

	public SpeedVector getVelocity() {
		return velocity;
	}

	public boolean isInRange(int x, int y, int d) {
		return Math.sqrt((x - getPosX()) * (x - getPosX()) + (y - getPosY()) * (y - getPosY())) <= d;
	}

	public boolean isInRange(InteractiveObject o, int d) {
		return Math.sqrt((o.getPosX() - getPosX()) * (o.getPosX() - getPosX())
				+ (o.getPosY() - getPosY()) * (o.getPosY() - getPosY())) <= d;
	}

	@Override
	public void push(Collideable from, float speed) {
		SpeedVector vec = new SpeedVector(0, 0);
		vec.aimFor(from.getLocation()[0], from.getLocation()[1], speed, posX, posY);
		posX += vec.getX();
		posY += vec.getY();
	}

	public boolean isStageBound() {
		return stageBound;
	}

	public void setCollisionInfo(CollisionInformation collisionInfo) {
		this.collisionInfo = collisionInfo;
	}

	public static ArrayList<InteractiveObject> getListedObjects() {
		return listedObjects;
	}

	public void setStageBound(boolean stageBound) {
		this.stageBound = stageBound;
	}

}
