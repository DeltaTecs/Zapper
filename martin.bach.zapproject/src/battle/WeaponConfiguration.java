package battle;

import library.Updateable;

public class WeaponConfiguration implements Updateable {

	private float maxCooldown;
	private float currentCooldown;
	private boolean ready;
	private int range;

	public WeaponConfiguration(float maxCooldown, int range) {
		this.maxCooldown = maxCooldown;
		this.range = range;
	}

	@Override
	public void update() {

		if (currentCooldown > 0) {
			currentCooldown--;
		} else if (!ready) {
			ready = true;
		}
	}

	public void resetCooldown() {
		currentCooldown = maxCooldown;
		ready = false;
	}

	public float getCurrentCooldown() {
		return currentCooldown;
	}

	public void setCurrentCooldown(int currentCooldown) {
		this.currentCooldown = currentCooldown;
	}

	public float getMaxCooldown() {
		return maxCooldown;
	}

	public boolean isReady() {
		return ready;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

}
