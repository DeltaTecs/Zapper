package gui.effect;

public class ExplosionEffectPattern {

	private int preexplosions;
	private int ballradius;

	public ExplosionEffectPattern(int preexplosions, int ballradius) {
		super();
		this.preexplosions = preexplosions;
		this.ballradius = ballradius;
	}

	public int getPreexplosions() {
		return preexplosions;
	}

	public int getBallradius() {
		return ballradius;
	}

}
