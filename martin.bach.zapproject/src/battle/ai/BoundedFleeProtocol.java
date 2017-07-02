package battle.ai;

import java.awt.Rectangle;

public class BoundedFleeProtocol extends FleeSingleProtocol {

	private Rectangle bounds;

	public BoundedFleeProtocol(float criticalPercentage, boolean shield, Rectangle bounds) {
		super(criticalPercentage, shield);
		this.bounds = bounds;
	}

	@Override
	public void updateMovement() {

		// begrenzung
		if (bounds.contains(getHost().getLocX(), getHost().getLocY()))
			super.updateMovement();
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

}
