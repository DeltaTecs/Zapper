package battle.ai;

import corecase.MainZap;
import gui.Map;

public class BasicSingleProtocol extends AiProtocol {
	
	private static final int DISTANCE_FACE_PLAYER = 270;
	private static final int DISTANCE_FOLLOW_PLAYER = 250;
	private static final int DISTANCE_STOP_PLAYER = 100;
	private static final int DISTANCE_PHYSICAL_DETECTION = 800;
	private static final int MAX_STOP_TIME = MainZap.getMainLoop().inTicks(32000);
	private static final int MAX_MOVING_TIME = MainZap.getMainLoop().inTicks(4000);
	private static final int FORCED_MOVING_TIME = MainZap.getMainLoop().inTicks(3500);
	private static final int FORCED_WAITING_TIME = MainZap.getMainLoop().inTicks(2000);

	private int timeToNextMovementAction = 0;

	public BasicSingleProtocol() {
		super();
		allowFacePlayer(true);
		setLockFaceDistance(DISTANCE_FACE_PLAYER);
		setShootAimPlayerOnly(true);
		setLockOpticDetectionRange(DISTANCE_FOLLOW_PLAYER);
		setLockStopRange(DISTANCE_STOP_PLAYER);
		setLockPhysicalDetectionRange(DISTANCE_PHYSICAL_DETECTION);
		setDamageRecognizeable();
	}

	@Override
	public void updateIdle() {

		if (timeToNextMovementAction > 0) {
			timeToNextMovementAction--;
		} else {

			// Am Zug.

			if (isMoving()) { // Jetzt Stoppen
				stopMoving();
				timeToNextMovementAction = FORCED_WAITING_TIME + rand(MAX_STOP_TIME);

			} else { // Jetzt Bewegen
				moveTo(rand(Map.SIZE), rand(Map.SIZE));
				timeToNextMovementAction = FORCED_MOVING_TIME + rand(MAX_MOVING_TIME);
			}

		}

	}

	@Override
	public Object getClone() {
		return new BasicSingleProtocol();
	}

}
