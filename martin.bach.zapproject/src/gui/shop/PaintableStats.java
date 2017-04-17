package gui.shop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import io.TextureBuffer;

public class PaintableStats {

	protected static final BufferedImage IMG_HEALTH = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_STAT_HEALTH);
	protected static final BufferedImage IMG_SPEED = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_STAT_SPEED);
	protected static final BufferedImage IMG_EFFICIENCY = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_STAT_EFFICIENCY);
	protected static final BufferedImage IMG_RELOAD_WITH = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_STAT_RELOAD_WITH);
	protected static final BufferedImage IMG_RELOAD_WITHOUT = TextureBuffer
			.get(TextureBuffer.NAME_SYMBOL_STAT_RELOAD_WITHOUT);
	protected static final BufferedImage IMG_DAMAGE = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_STAT_DAMAGE);
	protected static final BufferedImage IMG_BULLETSPEED = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_STAT_BULLETSPEED);
	protected static final BufferedImage IMG_RANGE = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_STAT_RANGE);

	protected static final Color COLOR_HEALTH = new Color(247, 0, 0);
	protected static final Color COLOR_SPEED = new Color(100, 190, 52);
	protected static final Color COLOR_EFFICIENCY = new Color(226, 104, 135);
	protected static final Color COLOR_RELOAD = new Color(213, 40, 0);
	protected static final Color COLOR_DAMAGE = new Color(130, 23, 0);
	protected static final Color COLOR_BULLETSPEED = new Color(244, 195, 0);
	protected static final Color COLOR_RANGE = new Color(104, 175, 226);
	protected static final Color COLOR_DELTA = new Color(223, 186, 130);
	private static final Color COLOR_DETAIL_BG = ShopSecBuy.COLOR_BG;
	private static final Stroke STOKE_CLASSIC = new BasicStroke(1);
	private static final Font FONT_DETAIL = new Font("Arial", Font.BOLD, 15);

	private static final int POS_Y = 330;
	private static final int POS_X = 350;
	private static final int POS_BAR_X = POS_X + 20 + 5;
	private static final int SPACE_Y = 15;
	private static final int HEIGHT = 16;
	private static final int WIDTH = 230;
	private static final int DELTA_MARKWIDTH = 3;

	private ShipConfigGraphCalc thisConfig;
	private ShipConfigGraphCalc baseConfig;

	private StatHoverOption hoveringOver = StatHoverOption.NONE;

	public PaintableStats(ShipConfigGraphCalc selected, ShipConfigGraphCalc base) {
		thisConfig = selected;
		baseConfig = base;
	}

	public void paint(Graphics2D g) {

		int y = POS_Y;
		g.drawImage(IMG_HEALTH, POS_X, y, 20, 20, null);
		g.setColor(COLOR_HEALTH);
		g.fillRect(POS_BAR_X, y + 2, (int) (thisConfig.getHealth() * WIDTH), HEIGHT);
		y += HEIGHT + SPACE_Y;
		g.drawImage(IMG_SPEED, POS_X, y, 20, 20, null);
		g.setColor(COLOR_SPEED);
		g.fillRect(POS_BAR_X, y + 2, (int) (thisConfig.getSpeed() * WIDTH), HEIGHT);
		y += HEIGHT + SPACE_Y;
		g.drawImage(IMG_EFFICIENCY, POS_X, y, 20, 20, null);
		g.setColor(COLOR_EFFICIENCY);
		g.fillRect(POS_BAR_X, y + 2, (int) (thisConfig.getEfficiency() * WIDTH), HEIGHT);
		y += HEIGHT + SPACE_Y;
		g.drawImage(IMG_RELOAD_WITH, POS_X, y, 20, 20, null);
		g.setColor(COLOR_RELOAD);
		g.fillRect(POS_BAR_X, y + 2, (int) (thisConfig.getReloadWith() * WIDTH), HEIGHT);
		y += HEIGHT + SPACE_Y;
		g.drawImage(IMG_RELOAD_WITHOUT, POS_X, y, 20, 20, null);
		g.fillRect(POS_BAR_X, y + 2, (int) (thisConfig.getReloadWithout() * WIDTH), HEIGHT);
		y += HEIGHT + SPACE_Y;
		g.drawImage(IMG_DAMAGE, POS_X, y, 20, 20, null);
		g.setColor(COLOR_DAMAGE);
		g.fillRect(POS_BAR_X, y + 2, (int) (thisConfig.getDamage() * WIDTH), HEIGHT);
		y += HEIGHT + SPACE_Y;
		g.drawImage(IMG_BULLETSPEED, POS_X, y, 20, 20, null);
		g.setColor(COLOR_BULLETSPEED);
		g.fillRect(POS_BAR_X, y + 2, (int) (thisConfig.getProjSpeed() * WIDTH), HEIGHT);
		y += HEIGHT + SPACE_Y;
		g.drawImage(IMG_RANGE, POS_X, y, 20, 20, null);
		g.setColor(COLOR_RANGE);
		g.fillRect(POS_BAR_X, y + 2, (int) (thisConfig.getProjRange() * WIDTH), HEIGHT);

		if (thisConfig.getConfig().getName() != baseConfig.getConfig().getName()) {
			// Nur Unterschied einzeichnen, wenn es einen gibt

			y = POS_Y;
			g.setColor(COLOR_DELTA);
			int posX = (int) (baseConfig.getHealth() * WIDTH) + POS_BAR_X;
			g.fillRect(posX - 2, y, 3, HEIGHT + 4);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y, 1 + DELTA_MARKWIDTH * 2, 3);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y + HEIGHT + 1, 1 + DELTA_MARKWIDTH * 2, 3);
			y += SPACE_Y + HEIGHT;
			posX = (int) (baseConfig.getSpeed() * WIDTH) + POS_BAR_X;
			g.fillRect(posX - 2, y, 3, HEIGHT + 4);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y, 1 + DELTA_MARKWIDTH * 2, 3);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y + HEIGHT + 1, 1 + DELTA_MARKWIDTH * 2, 3);
			y += SPACE_Y + HEIGHT;
			posX = (int) (baseConfig.getEfficiency() * WIDTH) + POS_BAR_X;
			g.fillRect(posX - 2, y, 3, HEIGHT + 4);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y, 1 + DELTA_MARKWIDTH * 2, 3);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y + HEIGHT + 1, 1 + DELTA_MARKWIDTH * 2, 3);
			y += SPACE_Y + HEIGHT;
			posX = (int) (baseConfig.getReloadWith() * WIDTH) + POS_BAR_X;
			g.fillRect(posX - 2, y, 3, HEIGHT + 4);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y, 1 + DELTA_MARKWIDTH * 2, 3);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y + HEIGHT + 1, 1 + DELTA_MARKWIDTH * 2, 3);
			y += SPACE_Y + HEIGHT;
			posX = (int) (baseConfig.getReloadWithout() * WIDTH) + POS_BAR_X;
			g.fillRect(posX - 2, y, 3, HEIGHT + 4);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y, 1 + DELTA_MARKWIDTH * 2, 3);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y + HEIGHT + 1, 1 + DELTA_MARKWIDTH * 2, 3);
			y += SPACE_Y + HEIGHT;
			posX = (int) (baseConfig.getDamage() * WIDTH) + POS_BAR_X;
			g.fillRect(posX - 2, y, 3, HEIGHT + 4);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y, 1 + DELTA_MARKWIDTH * 2, 3);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y + HEIGHT + 1, 1 + DELTA_MARKWIDTH * 2, 3);
			y += SPACE_Y + HEIGHT;
			posX = (int) (baseConfig.getProjSpeed() * WIDTH) + POS_BAR_X;
			g.fillRect(posX - 2, y, 3, HEIGHT + 4);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y, 1 + DELTA_MARKWIDTH * 2, 3);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y + HEIGHT + 1, 1 + DELTA_MARKWIDTH * 2, 3);
			y += SPACE_Y + HEIGHT;
			posX = (int) (baseConfig.getProjRange() * WIDTH) + POS_BAR_X;
			g.fillRect(posX - 2, y, 3, HEIGHT + 4);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y, 1 + DELTA_MARKWIDTH * 2, 3);
			g.fillRect(posX - DELTA_MARKWIDTH - 1, y + HEIGHT + 1, 1 + DELTA_MARKWIDTH * 2, 3);
		}
		paintDetails(g, hoveringOver);
	}

	private void paintDetails(Graphics2D g, StatHoverOption option) {
		if (option == StatHoverOption.NONE)
			return; // Nix ausgewählt

		int y = 0;
		String text = "";
		Color cBorder = Color.BLACK;
		int trueSpace = SPACE_Y - 4;

		switch (option) {
		case HEALTH:
			y = POS_Y + (20 * 1) + (trueSpace * 0);
			text = "Amour: " + thisConfig.getConfig().getHp() + " hp";
			cBorder = COLOR_HEALTH;
			break;
		case SPEED:
			y = POS_Y + (20 * 2) + (trueSpace * 1);
			text = "Speed: " + thisConfig.getConfig().getSpeed() + " p/t";
			cBorder = COLOR_SPEED;
			break;
		case EFFICIENCY:
			y = POS_Y + (20 * 3) + (trueSpace * 2);
			text = "Ammunition usage: " + thisConfig.getConfig().getEfficiency() + "x";
			cBorder = COLOR_EFFICIENCY;
			break;
		case RELOAD_WITH:
			y = POS_Y + (20 * 4) + (trueSpace * 3);
			text = "Cooldown, ammo available: " + thisConfig.getConfig().getReloadWith() + " t";
			cBorder = COLOR_RELOAD;
			break;
		case RELOAD_WITHOUT:
			y = POS_Y + (20 * 5) + (trueSpace * 4);
			text = "Cooldown, no ammo: " + thisConfig.getConfig().getReloadWithout() + " t";
			cBorder = COLOR_RELOAD;
			break;
		case DAMAGE:
			y = POS_Y + (20 * 6) + (trueSpace * 5);
			text = "Damage per bullet: " + thisConfig.getConfig().getDamage() + " hp";
			cBorder = COLOR_DAMAGE;
			break;
		case BULLETSPEED:
			y = POS_Y + (20 * 7) + (trueSpace * 6);
			text = "Bullet speed: " + thisConfig.getConfig().getProjSpeed() + " p/t";
			cBorder = COLOR_BULLETSPEED;
			break;
		case RANGE:
			y = POS_Y + (20 * 8) + (trueSpace * 7);
			text = "Weapon range: " + thisConfig.getConfig().getProjRange() + " p";
			cBorder = COLOR_RANGE;
			break;
		default: // Wird nicht gebraucht! Eclipse zwingt mich !! :(
			break;
		}

		g.setColor(COLOR_DETAIL_BG);
		g.fillRect(POS_BAR_X - 3, y - 1, WIDTH + 8, 20);
		g.setColor(cBorder);
		g.setStroke(STOKE_CLASSIC);
		g.drawRect(POS_BAR_X - 3, y - 1, WIDTH + 8, 20);
		g.setColor(COLOR_DETAIL_BG);
		g.drawLine(POS_BAR_X - 3, y - 1, POS_BAR_X - 3 + WIDTH + 8, y - 1);
		g.setColor(Color.BLACK);
		g.setFont(FONT_DETAIL);
		g.drawString(text, POS_BAR_X, y + FONT_DETAIL.getSize());

	}

	public void callMove(int tx, int ty) {

		if (tx < POS_X || tx > POS_BAR_X + WIDTH || ty < POS_Y) {
			// Falsche Seite oder drüber hinaus oder zu weit oben
			hoveringOver = StatHoverOption.NONE;
			return;
		}

		int y = POS_Y;
		if (ty >= y - 2 && ty <= y + 20 + 2) {
			hoveringOver = StatHoverOption.HEALTH;
			return;
		}
		y += HEIGHT + SPACE_Y;
		if (ty >= y - 2 && ty <= y + 20 + 2) {
			hoveringOver = StatHoverOption.SPEED;
			return;
		}
		y += HEIGHT + SPACE_Y;
		if (ty >= y - 2 && ty <= y + 20 + 2) {
			hoveringOver = StatHoverOption.EFFICIENCY;
			return;
		}
		y += HEIGHT + SPACE_Y;
		if (ty >= y - 2 && ty <= y + 20 + 2) {
			hoveringOver = StatHoverOption.RELOAD_WITH;
			return;
		}
		y += HEIGHT + SPACE_Y;
		if (ty >= y - 2 && ty <= y + 20 + 2) {
			hoveringOver = StatHoverOption.RELOAD_WITHOUT;
			return;
		}
		y += HEIGHT + SPACE_Y;
		if (ty >= y - 2 && ty <= y + 20 + 2) {
			hoveringOver = StatHoverOption.DAMAGE;
			return;
		}
		y += HEIGHT + SPACE_Y;
		if (ty >= y - 2 && ty <= y + 20 + 2) {
			hoveringOver = StatHoverOption.BULLETSPEED;
			return;
		}
		y += HEIGHT + SPACE_Y;
		if (ty >= y - 2 && ty <= y + 20 + 2) {
			hoveringOver = StatHoverOption.RANGE;
			return;
		}
	}

	public void setConfigs(ShipConfigGraphCalc selected, ShipConfigGraphCalc base) {
		thisConfig = selected;
		baseConfig = base;
	}
}
