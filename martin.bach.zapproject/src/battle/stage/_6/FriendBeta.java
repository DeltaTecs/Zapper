package battle.stage._6;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import battle.CombatObject;
import battle.WeaponConfiguration;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.FindLockAction;
import battle.enemy.Enemy;
import battle.stage._3.ProjectileFriendBeta;
import collision.CollisionInformation;
import collision.CollisionType;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;

public class FriendBeta extends Enemy {

	private static final float SPEED = 1.8f;
	private static final int MAX_HP = 8000;
	private static final int SHOOTING_RANGE = 300;
	private static final int LOCKRANGE = 500;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_FRIENDSHIP_BETA);
	private static final float SCALE = 0.64f;
	private static final float RADIUS = 30.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(15, 80);
	private static final float COOLDOWN = 20;
	private static final int SCORE = 0;
	private static final int CRYSTALS = 0;
	private static final int PROJECTILE_RANGE = 800;
	private static final boolean FRIEND = true;

	public FriendBeta() {
		super(0, 0, SPEED, TEXTURE, SCALE, COLINFO, new AdvancedSingleProtocol(),
				new WeaponConfiguration(COOLDOWN, PROJECTILE_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE, SHOOTING_RANGE,
				CRYSTALS, FRIEND);
		setMayShoot(true);
		setProjectilePattern(new ProjectileFriendBeta());
		getAiProtocol().setLockOpticDetectionRange(LOCKRANGE);
		((AdvancedSingleProtocol) getAiProtocol()).setLockAction(FindLockAction.LOCK_LOCK_OF_LINKED_FRIENDS);
	}

	public void link(ArrayList<CombatObject> allies) {
		((AdvancedSingleProtocol) getAiProtocol()).setLinkedAllies(allies);
	}

}
