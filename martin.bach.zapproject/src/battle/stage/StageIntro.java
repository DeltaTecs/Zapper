package battle.stage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;

import corecase.MainZap;
import corecase.StringConverter;
import gui.Frame;
import gui.screens.pause.PauseScreen;
import lib.PaintingTask;
import lib.Updateable;

public class StageIntro implements PaintingTask, Updateable {

	public static final int DURATION = MainZap.inTicks(6000);
	private static final int FADING_START = MainZap.inTicks(600);
	private static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 50);
	private static final Font FONT_LVL = new Font("Arial", Font.BOLD, 30);
	private static final Font FONT_DESCRIPTION = new Font("Arial", Font.BOLD, 20);
	private static final Color COLOR_STAR = new Color(147, 51, 0);

	private Stage stage;
	private int duration = DURATION;
	private String romanLvl;

	public StageIntro(Stage stage) {
		this.stage = stage;
		romanLvl = StringConverter.inRoman(stage.getLvl());
	}

	public void register() {
		StageManager.setAmountActiveIntros(StageManager.getAmountActiveIntros() + 1);
		MainZap.getMap().addUpdateElement(this);
		MainZap.getStaticLayer().addTask(this);
	}

	public void unRegister() {
		StageManager.setAmountActiveIntros(StageManager.getAmountActiveIntros() - 1);
		MainZap.getMap().removeUpdateElement(this);
		MainZap.getStaticLayer().removeTask(this);
	}

	@Override
	public void update() {

		duration--;
		if (duration == 0) {
			unRegister();
		}

	}

	@Override
	public void paint(Graphics2D g) {
		
		if (PauseScreen.isOpen())
			return; // Nicht mit Pausier-Bildschirm überlagern.

		int alpha = 255; // Sollange nicht faden. nicht faden.

		if (duration <= FADING_START) {
			alpha = (int) ((255 * duration) / (float) FADING_START);
		}

		g.setColor(new Color(20, 20, 20, alpha));

		// lvl
		g.setFont(FONT_LVL);
		int halfsize = g.getFontMetrics().stringWidth(romanLvl) / 2;
		g.drawString(romanLvl, Frame.HALF_SCREEN_SIZE - halfsize + 6, 206);

		// Name
		g.setFont(FONT_TITLE);
		halfsize = g.getFontMetrics().stringWidth(stage.getName()) / 2;
		g.drawString(stage.getName(), Frame.HALF_SCREEN_SIZE - halfsize + 6, 256);

		// Beschreibung
		g.setFont(FONT_DESCRIPTION);
		halfsize = g.getFontMetrics().stringWidth(stage.getDescription()) / 2;
		g.drawString(stage.getDescription(), Frame.HALF_SCREEN_SIZE - halfsize + 6, 286);

		g.setColor(new Color(COLOR_STAR.getRed(), COLOR_STAR.getGreen(), COLOR_STAR.getBlue(), alpha));
		drawStars(g, stage.getDifficulty());

	}

	private static void drawStars(Graphics2D g, StageDifficulty difficulty) {

		switch (difficulty) {
		case BEGINNER:
			drawStar(g, Frame.HALF_SCREEN_SIZE - 8, 316);
			break;
		case EASY:
			drawStar(g, Frame.HALF_SCREEN_SIZE - 30 - 8, 316);
			drawStar(g, Frame.HALF_SCREEN_SIZE + 30 - 8, 316);
			break;
		case MEDIUM:
			drawStar(g, Frame.HALF_SCREEN_SIZE + 60 - 8, 316);
			drawStar(g, Frame.HALF_SCREEN_SIZE - 8, 316);
			drawStar(g, Frame.HALF_SCREEN_SIZE - 60 - 8, 316);
			break;
		case HARD:
			drawStar(g, Frame.HALF_SCREEN_SIZE - 85 - 8, 316);
			drawStar(g, Frame.HALF_SCREEN_SIZE - 30 - 8, 316);
			drawStar(g, Frame.HALF_SCREEN_SIZE + 30 - 8, 316);
			drawStar(g, Frame.HALF_SCREEN_SIZE + 85 - 8, 316);
			break;
		case PRO:
			drawStar(g, Frame.HALF_SCREEN_SIZE - 120 - 8, 316);
			drawStar(g, Frame.HALF_SCREEN_SIZE - 60 - 8, 316);
			drawStar(g, Frame.HALF_SCREEN_SIZE - 8, 316);
			drawStar(g, Frame.HALF_SCREEN_SIZE + 60 - 8, 316);
			drawStar(g, Frame.HALF_SCREEN_SIZE + 120 - 8, 316);
			break;
		default:
			break;

		}

	}

	private static void drawStar(Graphics2D g, int x, int y) {

		g.translate(x, y);

		g.fillPolygon(new Polygon(new int[] { -14, 20, 20 }, new int[] { 0, 20, -20 }, 3));
		g.fillPolygon(new Polygon(new int[] { 34, 0, 0 }, new int[] { 0, 20, -20 }, 3));

		g.translate(-x, -y);
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
