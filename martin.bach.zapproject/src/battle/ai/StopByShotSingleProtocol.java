package battle.ai;

import corecase.MainZap;

public class StopByShotSingleProtocol extends BasicSingleProtocol {

	private static final int SHOOT_STOPPING_TIME = MainZap.inTicks(8000);

	private float weaponCooldown;

	public StopByShotSingleProtocol(float wpCooldown) {
		super();
		weaponCooldown = wpCooldown;
		if (weaponCooldown < SHOOT_STOPPING_TIME) {
			// Es bleibt nix zum warten übrig
			new RuntimeException("less cooldown then waiting time").printStackTrace();
		}
	}


}
