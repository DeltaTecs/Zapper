package gui.screens.finish;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import corecase.MainZap;
import corecase.StringConverter;
import gui.Frame;
import gui.screens.end.EndScreen;
import gui.screens.end.ScoreEntry;
import gui.shop.Shop;
import io.ScoreReader;
import lib.ClickListener;
import lib.ClickableObject;
import lib.MotionListener;
import lib.PaintingTask;

public abstract class FinishScreen {

	private static final Color COLOR_BORDER = new Color(0, 0, 0, 50);
	private static final Color COLOR_BACKGROUND = new Color(255, 255, 255, 190);
	private static final Color COLOR_FOREGROUND = new Color(0, 0, 0, 200);
	private static final Color COLOR_HOVERHIGHLIGHT = new Color(255, 255, 255, 100);
	private static final Color COLOR_BUTTONBORDER = new Color(0, 0, 0, 180);
	private static final Stroke STROKE_BUTTONBORDER = new BasicStroke(5);
	private static final Stroke STROKE_SUBMITBORDER = new BasicStroke(3);
	private static final Font FONT_HEADLINE_0 = new Font("Arial", Font.BOLD, 50);
	private static final Font FONT_HEADLINE_1 = new Font("Arial", Font.BOLD, 25);
	private static final Font FONT_PLAYERNAME0 = new Font("Arial", Font.ITALIC, 40);
	private static final Font FONT_PLAYERNAME1 = new Font("Arial", 0, 40);
	private static final Font FONT_SUBMIT = new Font("Arial", Font.BOLD, 33);
	private static final Font FONT_MAINBUTTON_CONTINUE = new Font("Arial", Font.BOLD, 40);
	private static final Font FONT_MAINBUTTON = new Font("Arial", Font.BOLD, 50);
	private static final String TEXT_HEADLINE_0 = "Congratulations!";
	private static final String TEXT_HEADLINE_1_a = "You finished the Zapper Space Combat Simulator";
	private static final String TEXT_HEADLINE_1_b = " with a score of ";
	private static final String TEXT_SUBMIT = "SUBMIT";
	private static final String TEXT_MAINBUTTON_CONTINUE = "Continue with  1.000.000";
	private static final String TEXT_MAINBUTTON_RESTART = "Normal Restart";
	private static final String TEXT_MAINBUTTON_EXIT = "Exit";
	private static final Rectangle HITBOX_PLAYERNAME = new Rectangle(180, 250, 210, 45);
	private static final Rectangle HITBOX_SUBMIT = new Rectangle(410, 250, 210, 45);
	private static final Rectangle HITBOX_CONTINUE = new Rectangle(150, 350, 500, 80);
	private static final Rectangle HITBOX_RESTART = new Rectangle(150, 450, 500, 80);
	private static final Rectangle HITBOX_EXIT = new Rectangle(150, 550, 500, 80);

	private static final byte MAX_NAME_LENGTH = 41;
	private static final int REWARD = 1000000;

	private static ClickableObject clickObject;
	private static boolean[] hovered = { false, false, false, false }; // continue, restart, exit, submit
	private static boolean open = false;
	private static int finalScore = 0;
	private static boolean firstTime = true;
	private static String playername = "name";
	private static boolean submitted = false;
	private static boolean startedTyping = false;
	private static boolean typingUnlocked = false;

	public static void update() {

	}

	private static final PaintingTask PAINTING_TASK = new PaintingTask() {

		@Override
		public void paint(Graphics2D g) {

			if (!open)
				return;

			// Hintergrung
			g.setColor(COLOR_BORDER);
			g.fillRect(50, 50, Frame.SIZE - 100, Frame.SIZE - 100);
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(60, 60, Frame.SIZE - 120, Frame.SIZE - 120);

			// Headlines
			g.setColor(COLOR_FOREGROUND);
			g.setFont(FONT_HEADLINE_0);
			g.drawString(TEXT_HEADLINE_0, Frame.HALF_SCREEN_SIZE - g.getFontMetrics().stringWidth(TEXT_HEADLINE_0) / 2,
					120);
			g.setFont(FONT_HEADLINE_1);
			g.drawString(TEXT_HEADLINE_1_a,
					Frame.HALF_SCREEN_SIZE - g.getFontMetrics().stringWidth(TEXT_HEADLINE_1_a) / 2, 170);
			g.drawString(TEXT_HEADLINE_1_b + finalScore + ".",
					Frame.HALF_SCREEN_SIZE - g.getFontMetrics().stringWidth(TEXT_HEADLINE_1_b + finalScore + ".") / 2,
					200);

			// Playername - Submit
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(HITBOX_PLAYERNAME.x, HITBOX_PLAYERNAME.y, HITBOX_PLAYERNAME.width, HITBOX_PLAYERNAME.height);
			g.setColor(COLOR_FOREGROUND);
			if (submitted)
				g.setFont(FONT_PLAYERNAME1);
			else
				g.setFont(FONT_PLAYERNAME0);
			g.clipRect(HITBOX_PLAYERNAME.x, HITBOX_PLAYERNAME.y, HITBOX_PLAYERNAME.width, HITBOX_PLAYERNAME.height);
			g.drawString(playername, HITBOX_PLAYERNAME.x + 6, HITBOX_PLAYERNAME.y + HITBOX_PLAYERNAME.height - 8);
			g.setClip(null);

			// Submit-Button
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(HITBOX_SUBMIT.x, HITBOX_SUBMIT.y, HITBOX_SUBMIT.width, HITBOX_SUBMIT.height);
			if (!submitted) {
				g.setColor(COLOR_BUTTONBORDER);
				g.setStroke(STROKE_SUBMITBORDER);
				g.drawRect(HITBOX_SUBMIT.x + 1, HITBOX_SUBMIT.y + 1, HITBOX_SUBMIT.width - 2, HITBOX_SUBMIT.height - 2);
				g.setFont(FONT_SUBMIT);
				g.setColor(COLOR_FOREGROUND);
				g.drawString(TEXT_SUBMIT, HITBOX_SUBMIT.x + 38, HITBOX_SUBMIT.y + HITBOX_SUBMIT.height - 9);
				if (hovered[3]) {
					g.setColor(COLOR_HOVERHIGHLIGHT);
					g.fillRect(HITBOX_SUBMIT.x + 1, HITBOX_SUBMIT.y + 1, HITBOX_SUBMIT.width - 2,
							HITBOX_SUBMIT.height - 2);
				}
			} else {
				g.setFont(FONT_PLAYERNAME1);
				g.setColor(COLOR_FOREGROUND);
				g.drawString(finalScore + "!", HITBOX_SUBMIT.x + 6, HITBOX_SUBMIT.y + HITBOX_SUBMIT.height - 8);
			}

			// Main-Buttons:
			// -- Continue ---------
			g.setColor(COLOR_BUTTONBORDER);
			g.setStroke(STROKE_BUTTONBORDER);
			g.drawRect(HITBOX_CONTINUE.x, HITBOX_CONTINUE.y, HITBOX_CONTINUE.width, HITBOX_CONTINUE.height);
			g.setFont(FONT_MAINBUTTON_CONTINUE);
			g.setColor(COLOR_FOREGROUND);
			g.drawString(TEXT_MAINBUTTON_CONTINUE, HITBOX_CONTINUE.x + 7,
					HITBOX_CONTINUE.y + HITBOX_CONTINUE.height - 26);
			Shop.drawCrystal(g, HITBOX_CONTINUE.x + HITBOX_CONTINUE.width - 48, HITBOX_CONTINUE.y + 12, 5);
			// ----

			// -- Restart ---------------
			g.setColor(COLOR_BUTTONBORDER);
			g.setStroke(STROKE_BUTTONBORDER);
			g.drawRect(HITBOX_RESTART.x, HITBOX_RESTART.y, HITBOX_RESTART.width, HITBOX_RESTART.height);
			g.setFont(FONT_MAINBUTTON);
			g.setColor(COLOR_FOREGROUND);
			g.drawString(TEXT_MAINBUTTON_RESTART,
					Frame.HALF_SCREEN_SIZE - g.getFontMetrics().stringWidth(TEXT_MAINBUTTON_RESTART) / 2,
					HITBOX_RESTART.y + HITBOX_RESTART.height - 22);
			// ----

			// -- Restart ---------------
			g.setColor(COLOR_BUTTONBORDER);
			g.setStroke(STROKE_BUTTONBORDER);
			g.drawRect(HITBOX_EXIT.x, HITBOX_EXIT.y, HITBOX_EXIT.width, HITBOX_EXIT.height);
			g.setFont(FONT_MAINBUTTON);
			g.setColor(COLOR_FOREGROUND);
			g.drawString(TEXT_MAINBUTTON_EXIT,
					Frame.HALF_SCREEN_SIZE - g.getFontMetrics().stringWidth(TEXT_MAINBUTTON_EXIT) / 2,
					HITBOX_EXIT.y + HITBOX_EXIT.height - 22);
			// ----

			g.setColor(COLOR_HOVERHIGHLIGHT);
			if (hovered[0])
				g.fillRect(HITBOX_CONTINUE.x, HITBOX_CONTINUE.y, HITBOX_CONTINUE.width, HITBOX_CONTINUE.height);
			else if (hovered[1])
				g.fillRect(HITBOX_RESTART.x, HITBOX_RESTART.y, HITBOX_RESTART.width, HITBOX_RESTART.height);
			else if (hovered[2])
				g.fillRect(HITBOX_EXIT.x, HITBOX_EXIT.y, HITBOX_EXIT.width, HITBOX_EXIT.height);

		}
	};

	private static final ClickListener LISTENER_CLICKS = new ClickListener() {

		@Override
		public void release(int dx, int dy) {
			if (HITBOX_SUBMIT.contains(dx, dy)) {
				if (!submitted && startedTyping)
					submit();
			} else if (HITBOX_CONTINUE.contains(dx, dy)) {
				close();
				MainZap.setCrystals(REWARD);
			}
		}

		@Override
		public void press(int dx, int dy) {

		}
	};

	private static final MotionListener LISTENER_HOVER = new MotionListener() {

		@Override
		public void move(int dx, int dy) {

			if (!open)
				return;

			if (HITBOX_CONTINUE.contains(dx, dy)) {
				hovered = new boolean[] { true, false, false, false };
				return;
			} else if (HITBOX_SUBMIT.contains(dx, dy)) {
				hovered = new boolean[] { false, false, false, true };
				return;
			} else if (HITBOX_RESTART.contains(dx, dy)) {
				hovered = new boolean[] { false, true, false, false };
				return;
			} else if (HITBOX_EXIT.contains(dx, dy)) {
				hovered = new boolean[] { false, false, true, false };
				return;
			}

			hovered = new boolean[] { false, false, false, false };

		}

		@Override
		public void drag(int dx, int dy) {

		}
	};

	private static final KeyListener LISTENER_KEY = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {

			if (submitted || !open)
				return; // schon erledigt oder geschlossen

			if ((e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
					&& playername.length() > 0) {

				if (playername.length() == 0)
					return; // kein weiteres Entfernen mehr möglich

				playername = playername.substring(0, playername.length() - 1);
				return;

			} else if (e.getKeyCode() == KeyEvent.VK_ENTER && startedTyping) {
				submit();
				return;
			}

			char c = e.getKeyChar();
			if (!StringConverter.isClassic(c) || playername.length() > MAX_NAME_LENGTH)
				return; // Nich zulässig oder zu lang

			if (!startedTyping) {
				startedTyping = true;
				playername = "";
			}

			playername += c;
		}
	};

	public static boolean isOpen() {
		return open;
	}

	public static void open() {

		if (firstTime) {
			clickObject = new ClickableObject(0, 0, Frame.SIZE, Frame.SIZE);
			clickObject.setVisible(true);
			clickObject.addClickListener(LISTENER_CLICKS);
			clickObject.addMotionListener(LISTENER_HOVER);
			MainZap.getFrame().addKeyListener(LISTENER_KEY);
			MainZap.getFrame().addClickable(clickObject);
		}
		firstTime = false;

		open = true;
		EndScreen.setActive(false);
		finalScore = MainZap.getScore();
	}

	public static void close() {
		open = false;
		MainZap.getPlayer().setAlive(true);
	}

	private static void submit() {
		ArrayList<ScoreEntry> entrys = ScoreReader.load();
		entrys.add(new ScoreEntry(playername, finalScore));
		submitted = true;
		entrys = EndScreen.getSortedScoreEntrys(entrys);
		ScoreReader.save(entrys);
	}

	public static PaintingTask getPaintingTask() {
		return PAINTING_TASK;
	}

}
