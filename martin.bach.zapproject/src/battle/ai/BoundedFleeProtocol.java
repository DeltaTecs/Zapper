package battle.ai;

import java.awt.Rectangle;

import corecase.MainZap;
import lib.SpeedVector;

public class BoundedFleeProtocol extends FleeSingleProtocol {

	private Rectangle bounds;
	private boolean enabled = true;

	public BoundedFleeProtocol(float criticalPercentage, boolean shield, Rectangle bounds) {
		super(criticalPercentage, shield);
		this.bounds = bounds;
	}

	@Override
	public void updateMovement() {

		super.updateMovement();

		if (!enabled)
			return;

		// Begrenzung
		if (!bounds.contains(getHost().getLocX(), getHost().getLocY())) {
			// nicht drin

			if (bounds.contains((int) (getHost().getPosX() + getHost().getVelocity().getX()),
					(int) (getHost().getPosY() + getHost().getVelocity().getY())))
				return; // aber dann draußen

			if (bounds.contains((int) (getHost().getPosX() - getHost().getVelocity().getX()),
					(int) (getHost().getPosY() - getHost().getVelocity().getY()))) {
				// Zurücksetzen möglich...
				getHost().setPosition(getHost().getPosX() - getHost().getVelocity().getX(),
						getHost().getPosY() - getHost().getVelocity().getY());

			} else {
				// Nur Clippen möglich
				getHost().setPosition(MainZap.clip(getHost().getPosX()), MainZap.clip(getHost().getPosY()));

			}

			getHost().setVelocity(new SpeedVector(0, 0));

		}
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
