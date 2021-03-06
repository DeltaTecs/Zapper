package battle.stage._5;

import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.ai.AiProtocol;
import battle.enemy.Enemy;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import gui.effect.TailManager;
import io.TextureBuffer;

public class FriendTransporter extends Enemy {

	private static final float SPEED = 1f;
	private static final int MAX_HP = 55000;
	private static final int SHOOTING_RANGE = 900;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_FRIENDSHIP_TRANSPORTER);
	private static final float SCALE = 0.9f;
	private static final float RADIUS = 35.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(40, 300);
	private static final int TAIL_SIZE = 20;
	private static final int TAIL_DISTANCE = 4;
	private static final float TAIL_SIZEREMOVAL = 0.3f;
	private static final int[] TAIL_POS_X = new int[] {0};
	private static final int[] TAIL_POS_Y = new int[] {35};
	private static final boolean TAIL_SQUARE = false;
	private static final float COOLDOWN = 27.0f;
	private static final int SCORE = 0;
	private static final int CRYSTALS = 0;
	private static final int PROJECTILE_RANGE = 900;
	private static final boolean FRIEND = true;

	public FriendTransporter(int x, int y, int desX, int desY) {
		super(x, y, SPEED, TEXTURE, SCALE, COLINFO, new AiProtocol(),
				new WeaponConfiguration(COOLDOWN, SHOOTING_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, PROJECTILE_RANGE,
				CRYSTALS, FRIEND, new TailManager(TAIL_SIZE, TAIL_DISTANCE, TAIL_SIZEREMOVAL, TAIL_POS_X, TAIL_POS_Y, TAIL_SQUARE));
		setMayShoot(false);
		getAiProtocol().setNonAutoLockon(true);
		getAiProtocol().moveTo(desX, desY);
	}

}
