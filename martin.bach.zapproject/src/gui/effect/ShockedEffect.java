package gui.effect;

import java.awt.Graphics2D;
import java.util.Random;

import corecase.MainZap;
import lib.ScheduledList;

// kleine Blitze über Gegener. Indiziert einen Shock. Benötigt Enemy-Mid-Kontext.
public class ShockedEffect extends Effect {

	private static final int MIN_SPARKLE_DURATION = MainZap.inTicks(200);
	private static final int MAX_SPARKLE_DURATION = MainZap.inTicks(1000);
	private static final int MIN_SPARKLE_WIDTH = 1;
	private static final int MAX_SPARKLE_WIDTH = 2;
	private static final int MIN_SPAKLE_LEN = 3;
	private static final int MAX_SPAKLE_LEN = 12;
	private static final float AMOUNT_START_SPARKLES_PER_AREA = 0.007f;
	private static final float SPARKLES_PER_TICK_PER_AREA = 0.0001f;
	private static final Random RANDOM = new Random(System.currentTimeMillis());

	private ScheduledList<ShockSparkle> sparkles = new ScheduledList<ShockSparkle>();
	private int range;
	private int area;
	private float sparklesPerTick;

	public ShockedEffect(int duration, int range) {
		super(duration);
		range += 10; // N bisschen darf rausragen
		this.range = range;
		area = (int) (Math.PI * range * range);
		int startSparkles = (int) (AMOUNT_START_SPARKLES_PER_AREA * area);
		sparklesPerTick = SPARKLES_PER_TICK_PER_AREA * area;
		for (int i = 0; i != startSparkles; i++)
			addSparkle();
	}

	@Override
	public void update() {

		synchronized (sparkles) {
			for (ShockSparkle s : sparkles) {
				s.update();
				if (s.faded())
					sparkles.schedRemove(s);
			}

			if (sparklesPerTick < 1.0f) {
				if (RANDOM.nextInt((int) (1.0f / sparklesPerTick)) == 0)
					addSparkle();
			} else
				for (int i = 0; i < sparklesPerTick; i++)
					addSparkle();

			sparkles.update();
		}
	}

	@Override
	public void paint(Graphics2D g) {
		synchronized (sparkles) {
			for (ShockSparkle s : sparkles)
				s.paint(g);
		}
	}

	private void addSparkle() {
		boolean horizontal = RANDOM.nextBoolean();
		int duration = MIN_SPARKLE_DURATION + RANDOM.nextInt(MAX_SPARKLE_DURATION - MIN_SPARKLE_DURATION);
		int width = MIN_SPARKLE_WIDTH + RANDOM.nextInt(MAX_SPARKLE_WIDTH - MIN_SPARKLE_WIDTH);
		int len = MIN_SPAKLE_LEN + RANDOM.nextInt(MAX_SPAKLE_LEN - MIN_SPAKLE_LEN);
		int[] coords = ExplosionEffect.getRandCircleCoordinates(range);
		ShockSparkle sparkle;
		if (horizontal)
			sparkle = new ShockSparkle(coords[0], coords[1], width, len, duration);
		else
			sparkle = new ShockSparkle(coords[0], coords[1], len, width, duration);
		sparkles.schedAdd(sparkle);
	}

}
