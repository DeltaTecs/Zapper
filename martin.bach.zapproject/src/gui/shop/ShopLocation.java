package gui.shop;

import java.awt.Point;

import battle.stage.StageManager;
import collision.Grid;
import corecase.MainZap;
import gui.Hud;
import lib.Updateable;

public class ShopLocation implements Updateable {

	int x;
	int y;
	int range;
	int lvl;

	public ShopLocation(int x, int y, int range, int lvl) {
		super();
		this.x = x;
		this.y = y;
		this.range = range;
		this.lvl = lvl;
	}

	@Override
	public void update() {

		if (StageManager.getActiveStage() != null && StageManager.getActiveStage().getLvl() != lvl) {
			Shop.setAvailable(false);
			Shop.close();
			Hud.setShopIconVisible(false);
			MainZap.getMap().removeUpdateElement(this);
			return;
		}

		Point a = new Point(MainZap.getPlayer().getLocX(), MainZap.getPlayer().getLocY());
		Point b = new Point(x, y);

		if (Grid.inRange(a, b, range)) {

			if (!Shop.isAvailable()) {
				Shop.setAvailable(true);
				Hud.setShopIconVisible(true);
			}
		} else if (Shop.isAvailable()) {
			Shop.setAvailable(false);
			Hud.setShopIconVisible(false);
		}

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

}
