package battle.collect;

import java.awt.image.BufferedImage;

import corecase.MainZap;
import ingameobjects.Collectable;
import io.TextureBuffer;

public class BulletDamageUp extends Collectable {

	private static final int SIZE = 26;
	private static final int DURATION = MainZap.inTicks(25000); // 25 Sek
	private static final BufferedImage TEXTURE_R = TextureBuffer.get(TextureBuffer.NAME_COLLECT_BULLET_DMG_UP_ROUND);
	private static final BufferedImage TEXTURE_C = TextureBuffer.get(TextureBuffer.NAME_COLLECT_BULLET_DMG_UP_CORNER);
	private static final boolean MAGNETIC = true;
	private static final boolean FLICKERING = true;
	public static final int BOOST_TIME = MainZap.inTicks(12000); // 12 Sek

	public BulletDamageUp() {
		super(SIZE, DURATION, TEXTURE_R, TEXTURE_C, FLICKERING, MAGNETIC);
	}

	@Override
	public void collect() {
		super.collect();
		MainZap.getPlayer().bulletDamageBoost(BOOST_TIME);
		unRegister();

	}

	public static void spawnDamageUp(int x, int y) {

		BulletDamageUp hp = new BulletDamageUp();
		hp.setPosition(x, y);
		hp.register();

	}

	public static void spawnDamageUp(int x, int y, Runnable finishTask) {

		BulletDamageUp hp = new BulletDamageUp();
		hp.setPosition(x, y);
		hp.register();
		hp.setUnregisterTask(finishTask);
	}

}
