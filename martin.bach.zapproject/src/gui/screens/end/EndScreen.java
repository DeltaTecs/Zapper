package gui.screens.end;

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
import io.CashReader;
import io.ScoreReader;
import lib.ClickListener;
import lib.ClickableObject;
import lib.PaintingTask;

public abstract class EndScreen {

	private static final Color COLOR_BORDER = new Color(0, 0, 0, 50);
	private static final Color COLOR_BACKGROUND = new Color(255, 255, 255, 190);
	private static final Color COLOR_FOREGROUND = new Color(0, 0, 0, 200);
	private static final Color COLOR_FILL_FOCUS = new Color(0, 0, 0, 250);
	private static final Font FONT_RIP = new Font("Arial", Font.BOLD, 40);
	private static final Font FONT_SCORE = new Font("Arial", Font.BOLD, 25);
	private static final Font FONT_SUBMIT = new Font("Arial", Font.BOLD, 30);
	private static final Font FONT_RETRY = new Font("Arial", Font.BOLD, 65);
	private static final Font FONT_NAME = new Font("Arial", Font.ITALIC, 28);
	private static final Font FONT_ENTRY = new Font("Arial", Font.BOLD, 18);
	public static final String TEXT_RIP = "Your ship has been destroyed";
	private static final String TEXT_YOUR_SCORE = "Your score:";
	private static final String TEXT_SUBMIT = "SUBMIT";
	private static final String TEXT_RETRY = "RETRY";
	private static final byte MAX_NAME_LENGTH = 40;
	private static final Rectangle HITBOX_SUBMIT = new Rectangle(70, 320, 140, 51);
	private static final Rectangle HITBOX_RETRY = new Rectangle(70, 558, 270, 98);
	private static final Rectangle HITBOX_SCROLL_UP = new Rectangle(365, 510 + 73, 50, 30);
	private static final Rectangle HITBOX_SCROLL_DOWN = new Rectangle(365, 540 + 73, 50, 30);
	private static final Rectangle HITBOX_NAMEFIELD = new Rectangle(80, 280, 250, 40);

	private static boolean active = false;
	private static boolean startedTyping = false;
	private static float currentScoreAlpha = 255;
	private static float currentScoreAlphaDelta = 10.0f;
	private static String nameEntered = "Name";
	private static ArrayList<ScoreEntry> entrys;
	private static int scorePanelPullOut = 0;
	private static int scorePanelPullOutDelta = 0;
	private static boolean scoreSubmitted = false;
	private static boolean[] scrollButtonsPressed = new boolean[] { false, false };

	private static ClickableObject clickObject;
	private static String message;

	public static void popUp(String message) {

		EndScreen.message = message;
		entrys = ScoreReader.load();
		entrys = getSortedScoreEntrys(entrys);
		Shop.close();

		// Überdeckt ganzen Frame
		ClickableObject bg = new ClickableObject(0, 0, Frame.SIZE, Frame.SIZE);
		bg.setVisible(true);
		bg.addClickListener(LISTENER_CLICKS);
		MainZap.getFrame().addKeyListener(KEYLISTENER);
		MainZap.getFrame().addClickable(bg);
		clickObject = bg;
		CashReader.save((int) (MainZap.getCrystalsEverEarned() * MainZap.CRYSTAL_GETBACK));
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
			int ripPos = Frame.HALF_SCREEN_SIZE - g.getFontMetrics().stringWidth(message) / 2;
			g.drawString(message, ripPos, 145);

			// Score
			g.setFont(FONT_SCORE);
			g.drawString(TEXT_YOUR_SCORE, 80, 220);
			g.setColor(scoreColor);
			g.drawString("" + MainZap.getScore(), 80, 250);

			// Eingabe
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(HITBOX_NAMEFIELD.x, HITBOX_NAMEFIELD.y, HITBOX_NAMEFIELD.width, HITBOX_NAMEFIELD.height);
			g.setColor(COLOR_FOREGROUND);
			g.setFont(FONT_NAME);
			g.clipRect(HITBOX_NAMEFIELD.x, HITBOX_NAMEFIELD.y, HITBOX_NAMEFIELD.width, HITBOX_NAMEFIELD.height);
			g.drawString(nameEntered, HITBOX_NAMEFIELD.x + 4, HITBOX_NAMEFIELD.y + 30);
			g.setClip(null);

			// Einträge: Hintergrund
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(430, 195, 285, 450);

			g.clipRect(430, 195, 285, 450);
			g.setFont(FONT_ENTRY);
			g.setColor(COLOR_FOREGROUND);
			int y = 195 + 20 + scorePanelPullOut;
			int place = 1;
			Color lineColor = new Color(0, 0, 0, 50);
			for (ScoreEntry e : entrys) {
				g.setColor(COLOR_FOREGROUND);
				g.drawString(place + ": " + e.getName(), 432, y);
				g.drawString("   > " + e.getScore(), 432, y + 20);
				g.setColor(lineColor);
				g.drawLine(430, y + 25, 430 + 285, y + 25);
				y += 45;
				place++;
			}
			g.setColor(COLOR_FOREGROUND);

			g.setClip(null);

			if (entrys.size() * 40 > 440) {

				// Hoch/Runter - Pfeile
				int ancX = 365;
				int ancY = 573;

				g.setColor(scrollButtonsPressed[0] ? COLOR_FILL_FOCUS : COLOR_FOREGROUND);
				g.fillPolygon(new int[] { ancX + 26, ancX + 6, ancX + 46 },
						new int[] { ancY + 10, ancY + 35, ancY + 35 }, 3);
				g.setColor(scrollButtonsPressed[1] ? COLOR_FILL_FOCUS : COLOR_FOREGROUND);
				g.fillPolygon(new int[] { ancX + 26, ancX + 6, ancX + 46 },
						new int[] { ancY + 70, ancY + 45, ancY + 45 }, 3);
			}

			// Reset - Button
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(HITBOX_RETRY.x + 10, HITBOX_RETRY.y + 10, HITBOX_RETRY.width - 20, HITBOX_RETRY.height - 20);
			g.setColor(COLOR_FOREGROUND);
			g.setFont(FONT_RETRY);
			g.drawString(TEXT_RETRY, HITBOX_RETRY.x + 13 + 10, HITBOX_RETRY.y + HITBOX_RETRY.height - 14 - 10);

			if (scoreSubmitted)
				return;

			// Submit - Button
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(HITBOX_SUBMIT.x + 10, HITBOX_SUBMIT.y + 10, HITBOX_SUBMIT.width - 20, HITBOX_SUBMIT.height - 20);
			g.setColor(COLOR_FOREGROUND);
			g.setFont(FONT_SUBMIT);
			g.drawString(TEXT_SUBMIT, HITBOX_SUBMIT.x + 2 + 10, HITBOX_SUBMIT.y + 27 + 10);

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

			if (scoreSubmitted || !active)
				return; // schon erledigt oder inaktiv

			if ((e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
					&& nameEntered.length() > 0) {

				if (nameEntered.length() == 0)
					return;

				nameEntered = nameEntered.substring(0, nameEntered.length() - 1);
				return;

			} else if (e.getKeyCode() == KeyEvent.VK_ENTER && startedTyping) { // Schneller Abschuss
				entrys.add(new ScoreEntry(nameEntered, MainZap.getScore()));
				scoreSubmitted = true;
				entrys = getSortedScoreEntrys(entrys);
				ScoreReader.save(entrys);
				return;
			}

			char c = e.getKeyChar();
			if (!StringConverter.isClassic(c) || nameEntered.length() > MAX_NAME_LENGTH)
				return; // Nich zulässig oder zu lang

			if (!startedTyping) {
				startedTyping = true;
				nameEntered = "";
			}

			nameEntered += c;
		}
	};

	public static void update() {

		if (!active)
			return; // Ist doch garnich an

		updateScoreAlpha();
		if (((entrys.size() * 40) + scorePanelPullOut > 440 && scorePanelPullOutDelta < 0)
				|| (scorePanelPullOut <= 0 && scorePanelPullOutDelta > 0)) {
			scorePanelPullOut += scorePanelPullOutDelta;
		}

	}

	private static final ClickListener LISTENER_CLICKS = new ClickListener() {

		@Override
		public void release(int dx, int dy) {
			// d = 1. Da ganzer Bildschirm überdeckt

			if (HITBOX_SUBMIT.contains(dx, dy) && nameEntered.length() > 0 && !scoreSubmitted && startedTyping) {
				entrys.add(new ScoreEntry(nameEntered, MainZap.getScore()));
				scoreSubmitted = true;
				entrys = getSortedScoreEntrys(entrys);
				ScoreReader.save(entrys);
			} else if (HITBOX_RETRY.contains(dx, dy)) {
				MainZap.getFrame().removeClickable(clickObject);
				MainZap.restartGame();
				active = false;
				return;
			} else if (HITBOX_NAMEFIELD.contains(dx, dy) && !startedTyping) {
				nameEntered = "";
				startedTyping = true;
			}

			scorePanelPullOutDelta = 0;
			scrollButtonsPressed = new boolean[] { false, false };
		}

		@Override
		public void press(int dx, int dy) {

			if (HITBOX_SCROLL_DOWN.contains(dx, dy)) {
				scrollButtonsPressed[0] = false;
				scrollButtonsPressed[1] = true;
				if ((entrys.size() * 40) + scorePanelPullOut > 440)
					scorePanelPullOutDelta = -4;
				return;
			}
			if (HITBOX_SCROLL_UP.contains(dx, dy)) {
				scrollButtonsPressed[0] = true;
				scrollButtonsPressed[1] = false;
				if (scorePanelPullOut < 0)
					scorePanelPullOutDelta = 4;
				return;
			}

			scrollButtonsPressed[0] = false;
			scrollButtonsPressed[1] = false;

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

	public static ArrayList<ScoreEntry> getSortedScoreEntrys(ArrayList<ScoreEntry> entrys) {

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

		return tops;
	}

	public static PaintingTask getPaintingtask() {
		return PAINTINGTASK;
	}

	public static void setActive(boolean active) {
		EndScreen.active = active;
	}

}
