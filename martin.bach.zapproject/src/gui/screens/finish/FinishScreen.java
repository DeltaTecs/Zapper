package gui.screens.finish;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import corecase.MainZap;
import gui.Frame;
import gui.screens.end.EndScreen;
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
	private static final Font FONT_HEADLINE_0 = new Font("Arial", Font.BOLD, 50);
	private static final Font FONT_HEADLINE_1 = new Font("Arial", Font.BOLD, 25);
	private static final String TEXT_HEADLINE_0 = "Congratulations!";
	private static final String TEXT_HEADLINE_1_a = "You finished the Zapper Space Combat Simulator";
	private static final String TEXT_HEADLINE_1_b = " with a score of ";
	private static final Rectangle HITBOX_CONTINUE = new Rectangle(150, 350, 500, 80);

	private static ClickableObject clickObject;
	private static boolean[] hovered = { false, false, false };
	private static boolean open = false;
	private static int finalScore = 0;
	private static boolean firstTime = true;

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

			// Buttons:
			g.setColor(COLOR_BUTTONBORDER);
			g.setStroke(STROKE_BUTTONBORDER);
			g.drawRect(HITBOX_CONTINUE.x, HITBOX_CONTINUE.y, HITBOX_CONTINUE.width, HITBOX_CONTINUE.height);

			
			
			
			
			
			
			
			
			
			
			// &&& Button fertig designen...
			// &&& Continue, Restart, Exit....
			
			
			
			
			
			
			
			g.setColor(COLOR_HOVERHIGHLIGHT);
			if (hovered[0])
				g.fillRect(HITBOX_CONTINUE.x, HITBOX_CONTINUE.y, HITBOX_CONTINUE.width, HITBOX_CONTINUE.height);

		}
	};

	private static final ClickListener LISTENER_CLICKS = new ClickListener() {

		@Override
		public void release(int dx, int dy) {

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
				hovered = new boolean[] { true, false, false };
				return;
			}

			hovered = new boolean[] { false, false, false };

		}

		@Override
		public void drag(int dx, int dy) {

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
			MainZap.getFrame().addClickable(clickObject);
		}
		firstTime = false;

		open = true;
		EndScreen.setActive(false);
		finalScore = MainZap.getScore();
	}

	public static PaintingTask getPaintingTask() {
		return PAINTING_TASK;
	}

}
