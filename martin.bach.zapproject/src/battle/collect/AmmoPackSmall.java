package battle.collect;

import java.awt.image.BufferedImage;

import corecase.MainZap;
import io.TextureBuffer;

public class AmmoPackSmall extends Collectable {

	private static final BufferedImage TEXTURE_R = TextureBuffer.get(TextureBuffer.NAME_COLLECT_AMMO_SMALL_ROUND);
	private static final BufferedImage TEXTURE_C = TextureBuffer.get(TextureBuffer.NAME_COLLECT_AMMO_SMALL_CORNER);

	private static final int SIZE = 26;

	public AmmoPackSmall() {
		super(SIZE, -1, TEXTURE_R, TEXTURE_C, false, true);
	}

	@Override
	public void collect() {
		MainZap.getPlayer().getAmmoIndicator().add(false);
		unRegister();
	}

}
