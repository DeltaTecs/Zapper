package collision;

public class CollisionInformation {

	private float radius;
	private CollisionType type;
	private boolean interfer;

	public CollisionInformation(float radius, CollisionType type, boolean mayInterfer) {
		super();
		this.radius = radius;
		this.type = type;
		interfer = mayInterfer;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public CollisionType getType() {
		return type;
	}

	public void setType(CollisionType type) {
		this.type = type;
	}

	public boolean mayInterfer() {
		return interfer;
	}

}
