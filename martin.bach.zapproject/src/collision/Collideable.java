package collision;

public interface Collideable {

	public CollisionInformation getInformation();

	public void collide(Collideable c);

	public int[] getLocation();

	public void push(Collideable from, float speed);

}
