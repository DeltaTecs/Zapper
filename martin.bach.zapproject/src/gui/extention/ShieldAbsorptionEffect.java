package gui.extention;

import corecase.MainZap;

public class ShieldAbsorptionEffect {

	private static final int MAX_DURATION = MainZap.inTicks(800);
	private static final int MAX_RADIAN = 17;
	private static final int MIN_DURATION = MainZap.inTicks(200);
	private static final int MIN_RADIAN = 10;
	private int startDuration;
	private int duration;
	private int dx;
	private int dy;
	private int alpha = Shielding.COLOR_SHIELD_FG.getAlpha();
	private int radian;

	public ShieldAbsorptionEffect(int dx, int dy) {
		super();
		this.dx = dx;
		this.dy = dy;
		startDuration = MainZap.rand(MAX_DURATION - MIN_DURATION) + MIN_DURATION;
		radian = MainZap.rand(MAX_RADIAN - MIN_RADIAN) + MIN_RADIAN;
		duration = startDuration;
	}

	public void update() {
		duration--;
		alpha = (int) (((float) duration * Shielding.COLOR_SHIELD_FG.getAlpha()) / startDuration);
	}

	public boolean faded() {
		return duration <= 0;
	}

	public int getAlpha() {
		return alpha;
	}

	public int getRadian() {
		return radian;
	}

	public int getDuration() {
		return duration;
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

}
