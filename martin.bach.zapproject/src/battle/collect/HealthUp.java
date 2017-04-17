package battle.collect;

import java.awt.image.BufferedImage;

import corecase.MainZap;
import ingameobjects.Collectable;
import io.TextureBuffer;

public class HealthUp extends Collectable {

	private static final int SIZE = 23;
	private static final int DURATION = MainZap.inTicks(25000); // 25 Sek
	private static final BufferedImage TEXTURE_R = TextureBuffer.get(TextureBuffer.NAME_COLLECT_HEALTHUP_ROUND);
	private static final BufferedImage TEXTURE_C = TextureBuffer.get(TextureBuffer.NAME_COLLECT_HEALTHUP_CORNER);
	private static final boolean MAGNETIC = true;
	private static final boolean FLICKERING = true;

	public HealthUp() {
		super(SIZE, DURATION, TEXTURE_R, TEXTURE_C, FLICKERING, MAGNETIC);
	}

	@Override
	public void collect() {
		super.collect();

		MainZap.getPlayer().heal((int) (MainZap.getPlayer().getMaxHp() * 0.6f));
		unRegister();

	}

	public static void spawnHpUp(int x, int y) {

		HealthUp hp = new HealthUp();
		hp.setPosition(x, y);
		hp.register();

	}

	public static void spawnHpUp(int x, int y, Runnable finishTask) {

		HealthUp hp = new HealthUp();
		hp.setPosition(x, y);
		hp.register();
		hp.setUnregisterTask(finishTask);
	}

}
