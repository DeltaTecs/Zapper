package gui.screens.death;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import corecase.MainZap;
import corecase.StringConverter;
import gui.Frame;
import gui.shop.Shop;
import io.ScoreReader;
import lib.ClickListener;
import lib.ClickableObject;
import lib.PaintingTask;

public abstract class DeathScreen {

	private static final Color COLOR_BORDER = new Color(0, 0, 0, 50);
	private static final Color COLOR_BACKGROUND = new Color(255, 255, 255, 190);
	private static final Color COLOR_FOREGROUND = new Color(0, 0, 0, 200);
	private static final Font FONT_RIP = new Font("Arial", Font.BOLD, 35);
	private static final Font FONT_SCORE = new Font("Arial", Font.BOLD, 25);
	private static final Font FONT_SUBMIT = new Font("Arial", Font.BOLD, 30);
	private static final Font FONT_NAME = new Font("Arial", Font.ITALIC, 28);
	private static final Font FONT_ENTRY = new Font("Arial", Font.BOLD, 18);
	private static final String TEXT_RIP = "Your ship has been destroyed";
	private static final String TEXT_YOUR_SCORE = "Your score:";
	private static final String TEXT_SUBMIT = "SUBMIT";
	private static final String TEXT_RETRY = "RETRY";
	private static final byte MAX_NAME_LENGTH = 40;
	private static final Rectangle HITBOX_SUBMIT = new Rectangle(70, 300, 140, 51);
	private static final Rectangle HITBOX_RETRY = new Rectangle(70, 360, 140, 51);
	private static final Rectangle HITBOX_SCROLL_UP = new Rectangle(300, 510, 50, 30);
	private static final Rectangle HITBOX_SCROLL_DOWN = new Rectangle(300, 540, 50, 30);

	private static boolean active = false;
	private static float currentScoreAlpha = 255;
	private static float currentScoreAlphaDelta = 10.0f;
	private static String nameEntered = "";
	private static ArrayList<ScoreEntry> entrys;
	private static int scorePanelPullOut = 0;
	private static int scorePanelPullOutDelta = 0;
	private static boolean scoreSubmitted = false;
	
	private static ClickableObject clickObject;

	public static void popUp() {

		// MainZap.getMainLoop().setPaused(true);

		entrys = ScoreReader.load();
		sortScoreEntrys();
		Shop.close();
		
		// Überdeckt ganzen Frame
		ClickableObject bg = new ClickableObject(0, 0, Frame.SIZE, Frame.SIZE);
		bg.setVisible(true);
		bg.addClickListener(LISTENER_CLICKS);
		MainZap.getFrame().addKeyListener(KEYLISTENER);
		MainZap.getFrame().addClickable(bg);
		clickObject = bg;
		
		active = true;
	}

	private static final PaintingTask PAINTINGTASK = new PaintingTask() {

		@Override
		public void paint(Graphics2D g) {

			if (!active)
				return; // Nix da zum zeigen

			Color scoreColor = new Color(1, 0, 4, (int) currentScoreAlpha);

			// Hintergrung
			g.setColor(COLOR_BORDER);
			g.fillRect(50, 50, Frame.SIZE - 100, Frame.SIZE - 100);
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(60, 60, Frame.SIZE - 120, Frame.SIZE - 120);

			// Überschrift
			g.setColor(COLOR_FOREGROUND);
			g.setFont(FONT_RIP);
			int ripPos = Frame.HALF_SCREEN_SIZE - g.getFontMetrics().stringWidth(TEXT_RIP) / 2;
			g.drawString(TEXT_RIP, ripPos, 110);

			// Score
			g.setFont(FONT_SCORE);
			g.drawString(TEXT_YOUR_SCORE, 80, 220);
			g.setColor(scoreColor);
			g.drawString("" + MainZap.getScore(), 80, 250);

			// Eingabe
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(80, 260, 250, 40);
			g.setColor(COLOR_FOREGROUND);
			g.setFont(FONT_NAME);
			g.clipRect(80, 260, 250, 40);
			g.drawString(nameEntered, 82, 290);
			g.setClip(null);

			// Einträge: Hintergrund
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(350, 140, 220, 440);

			g.clipRect(350, 140, 220, 440);
			g.setFont(FONT_ENTRY);
			g.setColor(COLOR_FOREGROUND);
			int y = 140 + 20 + scorePanelPullOut;
			for (ScoreEntry e : entrys) {
				g.drawString(e.getName(), 352, y);
				g.drawString(" > " + e.getScore(), 352, y + 20);
				y += 40;
			}

			g.setClip(null);

			if (entrys.size() * 40 > 440) {

				// Hoch/Runter - Pfeile
				g.fillPolygon(new int[] { 326, 306, 346 }, new int[] { 510, 535, 535 }, 3);
				g.fillPolygon(new int[] { 326, 306, 346 }, new int[] { 570, 545, 545 }, 3);
			}

			// Reset - Button
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(80, 370, 120, 31);
			g.setColor(COLOR_FOREGROUND);
			g.setFont(FONT_SUBMIT);
			g.drawString(TEXT_RETRY, 82, 397);
			
			if (scoreSubmitted)
				return;

			// Submit - Button
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(80, 310, 120, 31);
			g.setColor(COLOR_FOREGROUND);
			g.setFont(FONT_SUBMIT);
			g.drawString(TEXT_SUBMIT, 82, 337);
			
		}

	};

	private static final KeyListener KEYLISTENER = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {

			if (scoreSubmitted)
				return; // schon erledigt

			if ((e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
					&& nameEntered.length() > 0) {
				if (nameEntered.length() == 0)
					return;

				nameEntered = nameEntered.substring(0, nameEntered.length() - 1);
				return;
			}

			char c = e.getKeyChar();
			if (!StringConverter.isClassic(c) || nameEntered.length() > MAX_NAME_LENGTH)
				return; // Nich zulässig oder zu lang

			nameEntered += c;
		}
	};

	public static void update() {

		if (!active)
			return; // Ist doch garnich an

		updateScoreAlpha();
		if (((entrys.size() * 40) + scorePanelPullOut > 440 && scorePanelPullOutDelta < 0) || (scorePanelPullOut <= 0 && scorePanelPullOutDelta > 0)) {
			scorePanelPullOut += scorePanelPullOutDelta;
		}

	}

	private static final ClickListener LISTENER_CLICKS = new ClickListener() {

		@Override
		public void release(int dx, int dy) {
			// d = 1. Da ganzer Bildschirm überdeckt

			if (HITBOX_SUBMIT.contains(dx, dy) && nameEntered.length() > 0 && !scoreSubmitted) {
				entrys.add(new ScoreEntry(nameEntered, MainZap.getScore()));
				scoreSubmitted = true;
				sortScoreEntrys();
				ScoreReader.save(entrys);
			} else if (HITBOX_RETRY.contains(dx, dy)) {
				MainZap.getFrame().removeClickable(clickObject);
				MainZap.restartGame();
				active = false;
				return;
			}
			
			scorePanelPullOutDelta = 0;
		}

		@Override
		public void press(int dx, int dy) {

			if (HITBOX_SCROLL_DOWN.contains(dx, dy) && (entrys.size() * 40) + scorePanelPullOut > 440) {
				scorePanelPullOutDelta = -4;
				return;
			}
			if (HITBOX_SCROLL_UP.contains(dx, dy) && scorePanelPullOut < 0) {
				scorePanelPullOutDelta = 4;
			}

		}
	};

	private static void updateScoreAlpha() {

		currentScoreAlpha += currentScoreAlphaDelta;

		if (currentScoreAlpha > 255) {
			currentScoreAlpha = 255;
			currentScoreAlphaDelta *= -1;
			return;
		}
		if (currentScoreAlpha < 60) {
			currentScoreAlpha = 60;
			currentScoreAlphaDelta *= -1;
			return;
		}

	}

	private static void sortScoreEntrys() {

		ArrayList<ScoreEntry> tops = new ArrayList<ScoreEntry>();
		int size = entrys.size();
		for (int i = 0; i != size; i++) {

			ScoreEntry currentTopEntry = entrys.get(0);
			int currentTopScore = currentTopEntry.getScore();

			for (ScoreEntry e : entrys) {
				if (e.getScore() > currentTopScore) {
					currentTopEntry = e;
					currentTopScore = e.getScore();
				}
			}

			entrys.remove(currentTopEntry);
			tops.add(currentTopEntry);
		}

		entrys.clear();
		entrys = tops;
	}

	public static PaintingTask getPaintingtask() {
		return PAINTINGTASK;
	}

}
