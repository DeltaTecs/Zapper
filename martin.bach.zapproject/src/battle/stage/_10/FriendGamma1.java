package battle.stage._10;

import java.awt.Point;
import java.awt.image.BufferedImage;

import battle.WeaponConfiguration;
import battle.WeaponPositioning;
import battle.ai.AdvancedSingleProtocol;
import battle.ai.FindLockAction;
import battle.enemy.Enemy;
import collision.CollisionInformation;
import collision.CollisionType;
import corecase.MainZap;
import gui.effect.ExplosionEffectPattern;
import io.TextureBuffer;
import lib.SpeedVector;

public class FriendGamma1 extends Enemy {

	private static final float SPEED = 1.5f;
	private static final int MAX_HP = 10_000;
	private static final BufferedImage TEXTURE = TextureBuffer.get(TextureBuffer.NAME_FRIENDSHIP_GAMMA_1);
	private static final float SCALE = 0.85f;
	private static final float RADIUS = 90.0f;
	private static final CollisionInformation COLINFO = new CollisionInformation(RADIUS,
			CollisionType.COLLIDE_WITH_ENEMYS, true);
	private static final ExplosionEffectPattern EXPL_EFFECT_PATTERN = new ExplosionEffectPattern(100, 320);
	private static final float COOLDOWN = 0;
	private static final int SCORE = 0;
	private static final int CRYSTALS = 0;
	private static final int PROJECTILE_RANGE = 0;
	private static final boolean FRIEND = true;

	private static final Point[] TURRET_BASE_LOCS = new Point[] { new Point(57, -72), new Point(-57, -72),
			new Point(85, 75), new Point(-85, 75) };

	private FriendGamma1Turret[] turrets = new FriendGamma1Turret[4];

	public FriendGamma1() {
		super(0, 0, SPEED, TEXTURE, SCALE, COLINFO, new AdvancedSingleProtocol(),
				new WeaponConfiguration(COOLDOWN, PROJECTILE_RANGE), MAX_HP, EXPL_EFFECT_PATTERN, SCORE,
				PROJECTILE_RANGE, CRYSTALS, FRIEND);
		setBackground(true);
		setNoWaitAfterWarp(true);
		setPreAiming(true);
		setMayShoot(false);
		getAiProtocol().setDamageRecognizeable();
		getAiProtocol().setLockFaceDistance(600);
		getAiProtocol().setLockOutOfRangeRange(1500);
		getAiProtocol().setLockOpticDetectionRange(600);
		getAiProtocol().setLockPhysicalDetectionRange(1500);
		getAiProtocol().setLockStopRange(200);
		((AdvancedSingleProtocol) getAiProtocol()).setLockCombatFreeMovementRange(400);
		((AdvancedSingleProtocol) getAiProtocol()).setCombatRangePerOutOfRangeRange(0.8f);
		((AdvancedSingleProtocol) getAiProtocol()).setLockAction(FindLockAction.LOCK_LINKED_ENEMYS);

		// Turrets
		for (int i = 0; i != turrets.length; i++)
			turrets[i] = new FriendGamma1Turret(TURRET_BASE_LOCS[i].x, TURRET_BASE_LOCS[i].y, this);

		for (FriendGamma1Turret t : turrets) {
			if (t == null)
				continue; // unfinished
			this.attach(t);
			t.register();
		}

	}

	private void totalTurretPosUpdate(double deltarot) {

		if (turrets == null)
			return; // preinit

		for (int i = 0; i != turrets.length; i++) {
			double[] newRotatedDeltaLoc = WeaponPositioning.rotate(getRotation(), TURRET_BASE_LOCS[i]);
			turrets[i].setPosition(getPosX() + (float) newRotatedDeltaLoc[0],
					getPosY() + (float) newRotatedDeltaLoc[1]);
			turrets[i].setDeltaPosition((float) newRotatedDeltaLoc[0], (float) newRotatedDeltaLoc[1]);
			turrets[i].setRotation(turrets[i].getRotation() + deltarot);
		}

	}

	private void deltaPosTurretUpdate() {
		if (turrets == null)
			return; // preinit
		for (FriendGamma1Turret t : turrets)
			t.setPosition(getPosX() + t.getDx(), getPosY() + t.getDy());
	}

	@Override
	public void explode() {
		super.explode();
		for (FriendGamma1Turret t : turrets)
			t.explode();
	}

	// --- Localitasions-updates überschreiben VVV
	@Override
	public void setRotation(double rotation) {
		double dr = super.getRotation() - rotation;
		super.setRotation(rotation);
		totalTurretPosUpdate(dr);
	}

	@Override
	public void move(SpeedVector v) {
		super.move(v);
		deltaPosTurretUpdate();
	}

	@Override
	public void moveX(float dx) {
		super.moveX(dx);
		deltaPosTurretUpdate();
	}

	@Override
	public void moveY(float dy) {
		super.moveY(dy);
		deltaPosTurretUpdate();
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		deltaPosTurretUpdate();
	}

	@Override
	public void setPosX(float posX) {
		super.setPosX(posX);
		deltaPosTurretUpdate();
	}

	@Override
	public void setPosY(float posY) {
		super.setPosY(posY);
		deltaPosTurretUpdate();
	}

	// VVV --- als Background registieren
	@Override
	public void register() {
		MainZap.getMap().addPaintElement(this, true);
		MainZap.getMap().addUpdateElement(this);
		MainZap.getGrid().add(this);
		getListedObjects().add(this);
	}

	@Override
	public void unRegister() {
		MainZap.getMap().removePaintElement(this, true);
		MainZap.getMap().removeUpdateElement(this);
		MainZap.getGrid().remove(this);
		getListedObjects().remove(this);
	}
	// -----

}
