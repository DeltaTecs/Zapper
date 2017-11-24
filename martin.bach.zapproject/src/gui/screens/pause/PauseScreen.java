package gui.screens.pause;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import corecase.Cmd;
import corecase.MainZap;
import gui.Frame;
import gui.screens.finish.FinishScreen;
import gui.shop.Shop;
import io.CashReader;
import io.SettingsInitReader;
import io.TextureBuffer;
import lib.ClickListener;
import lib.ClickableObject;
import lib.MotionListener;

public abstract class PauseScreen {

	// -- Main ------------
	private static final Color COLOR_BG = new Color(255, 255, 255, 140);
	private static final Color COLOR_BORDER = new Color(0, 10, 2, 200);
	private static final Color COLOR_CONTINUE = new Color(22, 203, 226);
	private static final Color COLOR_RESTART = new Color(230, 84, 155);
	private static final Color COLOR_SETTINGS = new Color(73, 68, 59);
	private static final Color COLOR_EXIT = new Color(96, 50, 50);
	private static final int FONTSIZE = 70;
	private static final Font FONT = new Font("Arial", Font.BOLD + Font.ITALIC, FONTSIZE);
	private static final BasicStroke STROKE_BORDER = new BasicStroke(4);
	private static final int BORDER_ROUND_DEEPTH = 6;
	private static final int[] KONTEXT_TRANSLATION = new int[] { 75, 75 };
	private static final Rectangle BOUNDS_CONTINUE = new Rectangle(75 + KONTEXT_TRANSLATION[0],
			120 + KONTEXT_TRANSLATION[1], 500, 80);
	private static final Rectangle BOUNDS_RESTART = new Rectangle(75 + KONTEXT_TRANSLATION[0],
			230 + KONTEXT_TRANSLATION[1], 500, 80);
	private static final Rectangle BOUNDS_SETTINGS = new Rectangle(75 + KONTEXT_TRANSLATION[0],
			340 + KONTEXT_TRANSLATION[1], 500, 80);
	private static final Rectangle BOUNDS_EXIT = new Rectangle(75 + KONTEXT_TRANSLATION[0],
			450 + KONTEXT_TRANSLATION[1], 500, 80);
	private static final String TEXT_CONTINUE = "CONTINUE";
	private static final String TEXT_RESTART = "RESTART";
	private static final String TEXT_SETTINGS = "SETTINGS";
	private static final String TEXT_EXIT = "EXIT";
	private static final Polygon SYMBOL_CONTINUE = new Polygon(
			new int[] { BOUNDS_CONTINUE.x + 24, BOUNDS_CONTINUE.x + 84, BOUNDS_CONTINUE.x + 24 },
			new int[] { BOUNDS_CONTINUE.y + 6, BOUNDS_CONTINUE.y + 40, BOUNDS_CONTINUE.y + 74 }, 3);
	private static final BufferedImage SYMBOL_RESTART = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_RESTART);
	private static final BufferedImage SYMBOL_SETTINGS = TextureBuffer.get(TextureBuffer.NAME_SYMBOL_SETTINGS);
	private static final Stroke SYMBOL_EXIT_STROKE = new BasicStroke(10);
	private static final int[] SYMBOL_EXIT_LOCS = new int[] { BOUNDS_EXIT.x + 20, BOUNDS_EXIT.y + 15,
			BOUNDS_EXIT.x + 70, BOUNDS_EXIT.y + 65 }; // x1,y1,x2,y2
	// ---
	// -- Dailog ----------
	private static final Color COLOR_DIA_BG = new Color(255, 255, 255, 240);
	private static final Color COLOR_DIA_BORDER = new Color(0, 0, 0, 100);
	private static final Color COLOR_DIA_TEXT = new Color(0, 0, 0, 180);
	private static final Font FONT_DIALOG = new Font("Arial", Font.BOLD, 22);
	private static final Font FONT_DIALOG_OPTION = new Font("Arial", Font.BOLD, 40);
	private static final Rectangle BOUNDS_DIALOG = new Rectangle(140 + KONTEXT_TRANSLATION[0],
			250 + KONTEXT_TRANSLATION[1], 370, 150);
	private static final int BORDERWIDTH_DIALOG = 8;
	private static final Stroke STROKE_BUTTON_BORDER = new BasicStroke(5);
	private static final Rectangle BOUNDS_DIA_YES = new Rectangle(180 + KONTEXT_TRANSLATION[0],
			330 + KONTEXT_TRANSLATION[1], 120, 50);
	private static final Rectangle BOUNDS_DIA_NO = new Rectangle(350 + KONTEXT_TRANSLATION[0],
			330 + KONTEXT_TRANSLATION[1], 120, 50);
	private static final String TEXT_DIA_RESTART = "Restart? (Progress will be deleted)";
	private static final String TEXT_DIA_EXIT = "Exit? Progress will be deleted.";
	// ---
	// -- Settings ---------
	private static final Rectangle BOUNDS_SET_BACK = new Rectangle(20 + KONTEXT_TRANSLATION[0],
			20 + KONTEXT_TRANSLATION[1], 80, 80);
	private static final Rectangle BOUNDS_SET_GRAPH_SCALE = new Rectangle(380 + KONTEXT_TRANSLATION[0],
			140 + KONTEXT_TRANSLATION[1], 80, 40);
	private static final Rectangle BOUNDS_SET_GRAPH_FANCY = new Rectangle(380 + KONTEXT_TRANSLATION[0],
			192 + KONTEXT_TRANSLATION[1], 60, 40);
	private static final Rectangle BOUNDS_SET_GRAPH_ROUND = new Rectangle(380 + KONTEXT_TRANSLATION[0],
			242 + KONTEXT_TRANSLATION[1], 60, 40);
	private static final Rectangle BOUNDS_SET_AA_GENERAL = new Rectangle(380 + KONTEXT_TRANSLATION[0],
			292 + KONTEXT_TRANSLATION[1], 60, 40);
	private static final Rectangle BOUNDS_SET_AA_SHIPS = new Rectangle(380 + KONTEXT_TRANSLATION[0],
			342 + KONTEXT_TRANSLATION[1], 60, 40);
	private static final Rectangle BOUNDS_SET_SPEEDBOOST = new Rectangle(380 + KONTEXT_TRANSLATION[0],
			410 + KONTEXT_TRANSLATION[1], 60, 40);
	private static final Color COLOR_SET_HOVER = new Color(45, 35, 11, 35);
	private static final Color COLOR_SET_AA_FG = new Color(0, 0, 0, 220);
	private static final Stroke STROKE_SET_BACK = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private static final Stroke STROKE_SET_SCALE = new BasicStroke(2.5f);
	private static final Stroke STROKE_SET_AA = new BasicStroke(1.6f);
	private static final Font FONT_SET_SCL_INPUT = new Font("Arial", Font.BOLD, 35);
	private static final Font FONT_SET_AA_BUTTON = new Font("Arial", Font.BOLD, 29);
	private static final Font FONT_SET = new Font("Arial", Font.BOLD, 30);
	private static final Font FONT_SET_SUB = new Font("Arial", Font.BOLD, 22);
	// x1/x3, x2, y1, y2, y3
	private static final int[] SYMBOL_SET_BACK_LOCS = new int[] { BOUNDS_SET_BACK.x + 60, BOUNDS_SET_BACK.x + 15,
			BOUNDS_SET_BACK.y + 5, BOUNDS_SET_BACK.y + 37, BOUNDS_SET_BACK.y + 72 };
	private static final String TEXT_SET_GRAPH = "Graphics";
	private static final String TEXT_SET_GRAPH_SCALE = "Window size";
	private static final String TEXT_SET_GRAPH_ROUNDCORNERS = "Round corners";
	private static final String TEXT_SET_GRAPH_FANCY = "Fancyness";
	private static final String TEXT_SET_AA_GENERAL = "General smoothness";
	private static final String TEXT_SET_AA_SHIPS = "Smooth ships";
	private static final String TEXT_ON = "ON";
	private static final String TEXT_OFF = "OFF";
	private static final String TEXT_SPEEDMODE = "Hyper-Mode";
	private static final float SWITCHING_SPEED = 32.0f;
	// ---

	// Maus über: Continue, Restart, Setting, Exit?
	private static boolean[] hoveringMain = new boolean[] { false, false, false, false };
	// Maus über: Yes, No?
	private static boolean[] hoveringDialog = new boolean[] { false, false };
	// Maus über: AA-General, AA-Ships?
	private static boolean[] hoveringAA = new boolean[] { false, false };
	private static boolean hoveringSpeedMode = false; // Maus über HyperMode?
	private static boolean hoveringRoundcorners = false;
	private static boolean hoveringFancygraphics = false;
	private static boolean open = false;
	private static ClickableObject listeningComponent;
	private static PauseScreenDirectory dir = PauseScreenDirectory.MAIN;
	private static PauseScreenDialogoption dialog = PauseScreenDialogoption.RESTART;
	private static String strScale = "100";
	private static float locSettings = Frame.SIZE;

	public static void setUp(boolean loopOnly) {

		MainZap.getMainLoop().addTask(new Runnable() {
			@Override
			public void run() {
				update(); // Update auch im paused modus
			}
		}, false);

		if (loopOnly)
			return;

		MainZap.getFrame().addKeyListener(KEY_LISTENER);

		listeningComponent = new ClickableObject(0, 0, Frame.SIZE, Frame.SIZE);
		listeningComponent.addMotionListener(MOTION_LISTENER);
		listeningComponent.addClickListener(CLICK_LISTENER);
		MainZap.getFrame().addClickable(listeningComponent);
	}

	private static void update() {

		if (!open) // nicht sichtbar
			return;

		if (dir == PauseScreenDirectory.MAIN) {
			if (locSettings < Frame.SIZE) {
				locSettings += SWITCHING_SPEED;
			} else {
				locSettings = Frame.SIZE;
			}
		} else if (dir == PauseScreenDirectory.SETTINGS) {
			if (locSettings > 0) {
				locSettings -= SWITCHING_SPEED;
			} else {
				locSettings = 0;
			}
		}

	}

	public static void paint(Graphics2D g) {

		if (!open) // nicht sichtbar
			return;

		g.setColor(COLOR_BG);
		g.fillRect(0, 0, Frame.SIZE, Frame.SIZE);

		g.setStroke(STROKE_BORDER);
		g.setFont(FONT);

		if (dir == PauseScreenDirectory.MAIN || dir == PauseScreenDirectory.MAIN_DIALOG) {

			if (locSettings != Frame.SIZE) {
				g.translate(-(Frame.SIZE - locSettings), 0);
				drawDirMain(g);
				g.translate(Frame.SIZE, 0);
				drawDirSettings(g);
				g.translate(-locSettings, 0);
			} else {
				drawDirMain(g);
			}

			if (dir == PauseScreenDirectory.MAIN_DIALOG) {
				// Mit Dialog überzeichnen

				if (dialog == PauseScreenDialogoption.RESTART) {
					drawDialogRestart(g);
				} else if (dialog == PauseScreenDialogoption.EXIT) {
					drawDialogExit(g);
				}
			}

			return;
		}

		if (dir == PauseScreenDirectory.SETTINGS) {
			if (locSettings != 0) {
				g.translate(-(Frame.SIZE - locSettings), 0);
				drawDirMain(g);
				g.translate(Frame.SIZE, 0);
				drawDirSettings(g);
				g.translate(-locSettings, 0);
			} else {
				drawDirSettings(g);
			}
		}

	}

	private static final ClickListener CLICK_LISTENER = new ClickListener() {

		@Override
		public void release(int dx, int dy) {

			if (dir == PauseScreenDirectory.MAIN) {

				if (BOUNDS_CONTINUE.contains(dx, dy)) {
					setOpen(false);
					return;
				}
				if (BOUNDS_RESTART.contains(dx, dy)) {
					dialog = PauseScreenDialogoption.RESTART;
					dir = PauseScreenDirectory.MAIN_DIALOG;
					return;
				}
				if (BOUNDS_SETTINGS.contains(dx, dy)) {
					dir = PauseScreenDirectory.SETTINGS;
					strScale = "" + (int) (MainZap.getScale() * 100);
					return;
				}
				if (BOUNDS_EXIT.contains(dx, dy)) {
					dialog = PauseScreenDialogoption.EXIT;
					dir = PauseScreenDirectory.MAIN_DIALOG;
				}
				return;
			}

			if (dir == PauseScreenDirectory.MAIN_DIALOG) {
				if (BOUNDS_DIA_YES.contains(dx, dy)) {
					if (dialog == PauseScreenDialogoption.RESTART) {
						MainZap.restartGame();
						setOpen(false);
					} else if (dialog == PauseScreenDialogoption.EXIT) {
						// Geld speichern
						CashReader.save((int) (MainZap.getCrystalsEverEarned() * MainZap.CRYSTAL_GETBACK));
						Cmd.print("User quits...");
						System.exit(1);
					}
					return;
				}
				if (BOUNDS_DIA_NO.contains(dx, dy)) {
					dir = PauseScreenDirectory.MAIN;
					return;
				}
				return;
			}

			if (dir == PauseScreenDirectory.SETTINGS) {
				if (BOUNDS_SET_BACK.contains(dx, dy)) {
					dir = PauseScreenDirectory.MAIN;
					SettingsInitReader.save();
					return;
				}
				if (BOUNDS_SET_AA_GENERAL.contains(dx, dy)) {
					MainZap.generalAntialize = !MainZap.generalAntialize;
					SettingsInitReader.save();
					return;
				}
				if (BOUNDS_SET_AA_SHIPS.contains(dx, dy)) {
					MainZap.antializeShips = !MainZap.antializeShips;
					SettingsInitReader.save();
					return;
				}
				if (BOUNDS_SET_SPEEDBOOST.contains(dx, dy)) {
					MainZap.setSpeedMode(!MainZap.speedMode);
					SettingsInitReader.save();
					return;
				}
				if (BOUNDS_SET_GRAPH_FANCY.contains(dx, dy)) {
					MainZap.fancyGraphics = !MainZap.fancyGraphics;
					MainZap.getMap().rebuildBgParticles();
					SettingsInitReader.save();
					return;
				}
				if (BOUNDS_SET_GRAPH_ROUND.contains(dx, dy)) {
					MainZap.roundCorners = !MainZap.roundCorners;
					SettingsInitReader.save();
					return;
				}
				if (BOUNDS_SET_GRAPH_SCALE.contains(dx, dy)) {
					strScale = "";
					return;
				}

				return;
			}

		}

		@Override
		public void press(int dx, int dy) {
		}
	};

	private static final KeyListener KEY_LISTENER = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// --- Open-Listener -------
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

				if (Shop.isOpen()) {
					Shop.close();
				} else {

					if (open) {
						setOpen(false);
					} else {
						setOpen(true);
					}
				}
			}
			// ---

			// --- Scaling-Field: Input-Listening--
			if (open && dir == PauseScreenDirectory.SETTINGS) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (strScale.length() == 0) // nix drin
						return;

					try {
						float s = Integer.parseInt(strScale) / 100.0f;
						if (s < 0.1) // DAFUQ. das is zu klein!
							return;
						MainZap.setScale(s);
						SettingsInitReader.save();
					} catch (NumberFormatException exc) {
						Cmd.err("InputError in PauseScreen: Settings. This should not happen.");
					}
					return;
				}

				if ((e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
						&& strScale.length() > 0) {
					if (strScale.length() == 0)
						return;

					strScale = strScale.substring(0, strScale.length() - 1);
					return;
				}

				if (strScale.length() == 3) // Nicht zu große Zahlen
					return;

				if (!isNumber(e.getKeyChar())) // Nur positive Zahlen
					return;

				// Alles ok.
				strScale += e.getKeyChar();
			}

		}

		@Override
		public void keyPressed(KeyEvent e) {
		}
	};

	private static final MotionListener MOTION_LISTENER = new MotionListener() {

		@Override
		public void move(int dx, int dy) {
			checkMotion(dx, dy);
		}

		@Override
		public void drag(int dx, int dy) {
			checkMotion(dx, dy);
		}
	};

	private static void checkMotion(int x, int y) {
		if (dir == PauseScreenDirectory.MAIN) {
			checkDirMainHover(x, y);
			return;
		}
		if (dir == PauseScreenDirectory.MAIN_DIALOG) {
			checkDirDialogHover(x, y);
			return;
		}
		if (dir == PauseScreenDirectory.SETTINGS) {
			checkDirSettingsHover(x, y);
			return;
		}
	}

	private static void checkDirMainHover(int x, int y) {
		if (BOUNDS_CONTINUE.contains(x, y)) {
			hoveringMain[0] = true;
			hoveringMain[1] = false;
			hoveringMain[2] = false;
			hoveringMain[3] = false;
			return;
		}

		if (BOUNDS_RESTART.contains(x, y)) {
			hoveringMain[0] = false;
			hoveringMain[1] = true;
			hoveringMain[2] = false;
			hoveringMain[3] = false;
			return;
		}

		if (BOUNDS_SETTINGS.contains(x, y)) {
			hoveringMain[0] = false;
			hoveringMain[1] = false;
			hoveringMain[2] = true;
			hoveringMain[3] = false;
			return;
		}

		if (BOUNDS_EXIT.contains(x, y)) {
			hoveringMain[0] = false;
			hoveringMain[1] = false;
			hoveringMain[2] = false;
			hoveringMain[3] = true;
			return;
		}

		hoveringMain[0] = false;
		hoveringMain[1] = false;
		hoveringMain[2] = false;
		hoveringMain[3] = false;
	}

	private static void checkDirDialogHover(int x, int y) {
		if (BOUNDS_DIA_YES.contains(x, y)) {
			hoveringDialog[0] = true;
			hoveringDialog[1] = false;
			return;
		}
		if (BOUNDS_DIA_NO.contains(x, y)) {
			hoveringDialog[0] = false;
			hoveringDialog[1] = true;
			return;
		}
		hoveringDialog[0] = false;
		hoveringDialog[1] = false;
		return;
	}

	private static void checkDirSettingsHover(int x, int y) {

		if (BOUNDS_SET_AA_GENERAL.contains(x, y)) {
			hoveringAA[0] = true;
			hoveringAA[1] = false;
			hoveringSpeedMode = false;
			hoveringFancygraphics = false;
			hoveringRoundcorners = false;
			return;
		}

		if (BOUNDS_SET_AA_SHIPS.contains(x, y)) {
			hoveringAA[0] = false;
			hoveringAA[1] = true;
			hoveringSpeedMode = false;
			hoveringFancygraphics = false;
			hoveringRoundcorners = false;
			return;
		}
		if (BOUNDS_SET_SPEEDBOOST.contains(x, y)) {
			hoveringAA[0] = false;
			hoveringAA[1] = false;
			hoveringSpeedMode = true;
			hoveringFancygraphics = false;
			hoveringRoundcorners = false;
			return;
		}
		if (BOUNDS_SET_GRAPH_FANCY.contains(x, y)) {
			hoveringAA[0] = false;
			hoveringAA[1] = false;
			hoveringSpeedMode = false;
			hoveringFancygraphics = true;
			hoveringRoundcorners = false;
			return;
		}
		if (BOUNDS_SET_GRAPH_ROUND.contains(x, y)) {
			hoveringAA[0] = false;
			hoveringAA[1] = false;
			hoveringSpeedMode = false;
			hoveringFancygraphics = false;
			hoveringRoundcorners = true;
			return;
		}

		hoveringAA[0] = false;
		hoveringAA[1] = false;
		hoveringSpeedMode = false;
		hoveringFancygraphics = false;
		hoveringRoundcorners = false;
		return;

	}

	private static void drawDialogRestart(Graphics2D g) {

		g.setColor(COLOR_DIA_BORDER);
		g.fillRect(BOUNDS_DIALOG.x - BORDERWIDTH_DIALOG, BOUNDS_DIALOG.y - BORDERWIDTH_DIALOG,
				BOUNDS_DIALOG.width + 2 * BORDERWIDTH_DIALOG, BOUNDS_DIALOG.height + 2 * BORDERWIDTH_DIALOG);
		g.setColor(COLOR_DIA_BG);
		g.fillRect(BOUNDS_DIALOG.x, BOUNDS_DIALOG.y, BOUNDS_DIALOG.width, BOUNDS_DIALOG.height);

		g.setColor(new Color(0, 0, 0, 80));
		if (hoveringDialog[0]) {
			// Maus über YES
			g.fillRect(BOUNDS_DIA_YES.x, BOUNDS_DIA_YES.y, BOUNDS_DIA_YES.width, BOUNDS_DIA_YES.height);
		} else if (hoveringDialog[1]) {
			// Maus über NO
			g.fillRect(BOUNDS_DIA_NO.x, BOUNDS_DIA_NO.y, BOUNDS_DIA_NO.width, BOUNDS_DIA_NO.height);
		}

		g.setFont(FONT_DIALOG);
		g.setColor(COLOR_DIA_TEXT);
		g.drawString(TEXT_DIA_RESTART, BOUNDS_DIALOG.x + 9, BOUNDS_DIALOG.y + 50);

		g.setStroke(STROKE_BUTTON_BORDER);
		g.drawRect(BOUNDS_DIA_YES.x, BOUNDS_DIA_YES.y, BOUNDS_DIA_YES.width, BOUNDS_DIA_YES.height);
		g.drawRect(BOUNDS_DIA_NO.x, BOUNDS_DIA_NO.y, BOUNDS_DIA_NO.width, BOUNDS_DIA_NO.height);

		g.setFont(FONT_DIALOG_OPTION);
		g.drawString("Yes", BOUNDS_DIA_YES.x + 22, BOUNDS_DIA_YES.y + FONT_DIALOG_OPTION.getSize());
		g.drawString("No", BOUNDS_DIA_NO.x + 34, BOUNDS_DIA_NO.y + FONT_DIALOG_OPTION.getSize());

	}

	private static void drawDialogExit(Graphics2D g) {

		g.setColor(COLOR_DIA_BORDER);
		g.fillRect(BOUNDS_DIALOG.x - BORDERWIDTH_DIALOG, BOUNDS_DIALOG.y - BORDERWIDTH_DIALOG,
				BOUNDS_DIALOG.width + 2 * BORDERWIDTH_DIALOG, BOUNDS_DIALOG.height + 2 * BORDERWIDTH_DIALOG);
		g.setColor(COLOR_DIA_BG);
		g.fillRect(BOUNDS_DIALOG.x, BOUNDS_DIALOG.y, BOUNDS_DIALOG.width, BOUNDS_DIALOG.height);

		g.setColor(new Color(0, 0, 0, 80));
		if (hoveringDialog[0]) {
			// Maus über YES
			g.fillRect(BOUNDS_DIA_YES.x, BOUNDS_DIA_YES.y, BOUNDS_DIA_YES.width, BOUNDS_DIA_YES.height);
		} else if (hoveringDialog[1]) {
			// Maus über NO
			g.fillRect(BOUNDS_DIA_NO.x, BOUNDS_DIA_NO.y, BOUNDS_DIA_NO.width, BOUNDS_DIA_NO.height);
		}

		g.setFont(FONT_DIALOG);
		g.setColor(COLOR_DIA_TEXT);
		g.drawString(TEXT_DIA_EXIT, BOUNDS_DIALOG.x + 33, BOUNDS_DIALOG.y + 50);

		g.setStroke(STROKE_BUTTON_BORDER);
		g.drawRect(BOUNDS_DIA_YES.x, BOUNDS_DIA_YES.y, BOUNDS_DIA_YES.width, BOUNDS_DIA_YES.height);
		g.drawRect(BOUNDS_DIA_NO.x, BOUNDS_DIA_NO.y, BOUNDS_DIA_NO.width, BOUNDS_DIA_NO.height);

		g.setFont(FONT_DIALOG_OPTION);
		g.drawString("Yes", BOUNDS_DIA_YES.x + 22, BOUNDS_DIA_YES.y + FONT_DIALOG_OPTION.getSize());
		g.drawString("No", BOUNDS_DIA_NO.x + 34, BOUNDS_DIA_NO.y + FONT_DIALOG_OPTION.getSize());

	}

	private static void drawDirMain(Graphics2D g) {
		if (hoveringMain[0]) { // Maus über "CONTINUE"
			g.setColor(Color.WHITE);
			if (MainZap.roundCorners)
				g.fillRoundRect(BOUNDS_CONTINUE.x, BOUNDS_CONTINUE.y, BOUNDS_CONTINUE.width, BOUNDS_CONTINUE.height,
						BORDER_ROUND_DEEPTH, BORDER_ROUND_DEEPTH);
			else
				g.fillRect(BOUNDS_CONTINUE.x, BOUNDS_CONTINUE.y, BOUNDS_CONTINUE.width, BOUNDS_CONTINUE.height);
		}
		g.setColor(COLOR_BORDER);
		if (MainZap.roundCorners)
			g.drawRoundRect(BOUNDS_CONTINUE.x, BOUNDS_CONTINUE.y, BOUNDS_CONTINUE.width, BOUNDS_CONTINUE.height,
					BORDER_ROUND_DEEPTH, BORDER_ROUND_DEEPTH);
		else
			g.drawRect(BOUNDS_CONTINUE.x, BOUNDS_CONTINUE.y, BOUNDS_CONTINUE.width, BOUNDS_CONTINUE.height);
		g.setColor(COLOR_CONTINUE);
		g.drawString(TEXT_CONTINUE, BOUNDS_CONTINUE.x + 100, BOUNDS_CONTINUE.y + BOUNDS_CONTINUE.height - 12);
		// ----- Continue - Symbol --
		g.fillPolygon(SYMBOL_CONTINUE);
		// --

		if (hoveringMain[1]) { // Maus über "RESTART"
			g.setColor(Color.WHITE);
			if (MainZap.roundCorners)
				g.fillRoundRect(BOUNDS_RESTART.x, BOUNDS_RESTART.y, BOUNDS_RESTART.width, BOUNDS_RESTART.height,
						BORDER_ROUND_DEEPTH, BORDER_ROUND_DEEPTH);
			else
				g.fillRect(BOUNDS_RESTART.x, BOUNDS_RESTART.y, BOUNDS_RESTART.width, BOUNDS_RESTART.height);
		}
		g.setColor(COLOR_BORDER);
		if (MainZap.roundCorners)
			g.drawRoundRect(BOUNDS_RESTART.x, BOUNDS_RESTART.y, BOUNDS_RESTART.width, BOUNDS_RESTART.height,
					BORDER_ROUND_DEEPTH, BORDER_ROUND_DEEPTH);
		else
			g.drawRect(BOUNDS_RESTART.x, BOUNDS_RESTART.y, BOUNDS_RESTART.width, BOUNDS_RESTART.height);
		g.setColor(COLOR_RESTART);
		g.drawString(TEXT_RESTART, BOUNDS_RESTART.x + 100, BOUNDS_RESTART.y + BOUNDS_RESTART.height - 12);
		// ------ Restart - Symbol --
		g.drawImage(SYMBOL_RESTART, BOUNDS_RESTART.x + 15, BOUNDS_RESTART.y + 10, 64, 64, null);
		// --

		if (hoveringMain[2]) { // Maus über "SETTINGS"
			g.setColor(Color.WHITE);
			if (MainZap.roundCorners)
				g.fillRoundRect(BOUNDS_SETTINGS.x, BOUNDS_SETTINGS.y, BOUNDS_SETTINGS.width, BOUNDS_SETTINGS.height,
						BORDER_ROUND_DEEPTH, BORDER_ROUND_DEEPTH);
			else
				g.fillRect(BOUNDS_SETTINGS.x, BOUNDS_SETTINGS.y, BOUNDS_SETTINGS.width, BOUNDS_SETTINGS.height);
		}
		g.setColor(COLOR_BORDER);
		if (MainZap.roundCorners)
			g.drawRoundRect(BOUNDS_SETTINGS.x, BOUNDS_SETTINGS.y, BOUNDS_SETTINGS.width, BOUNDS_SETTINGS.height,
					BORDER_ROUND_DEEPTH, BORDER_ROUND_DEEPTH);
		else
			g.drawRect(BOUNDS_SETTINGS.x, BOUNDS_SETTINGS.y, BOUNDS_SETTINGS.width, BOUNDS_SETTINGS.height);
		g.setColor(COLOR_SETTINGS);
		g.drawString(TEXT_SETTINGS, BOUNDS_SETTINGS.x + 100, BOUNDS_SETTINGS.y + BOUNDS_SETTINGS.height - 12);
		// ------ Settings - Symbol --
		g.drawImage(SYMBOL_SETTINGS, BOUNDS_SETTINGS.x + 15, BOUNDS_SETTINGS.y + 10, 64, 64, null);
		// --

		if (hoveringMain[3]) { // Maus über "EXIT"
			g.setColor(Color.WHITE);
			if (MainZap.roundCorners)
				g.fillRoundRect(BOUNDS_EXIT.x, BOUNDS_EXIT.y, BOUNDS_EXIT.width, BOUNDS_EXIT.height,
						BORDER_ROUND_DEEPTH, BORDER_ROUND_DEEPTH);
			else
				g.fillRect(BOUNDS_EXIT.x, BOUNDS_EXIT.y, BOUNDS_EXIT.width, BOUNDS_EXIT.height);
		}
		g.setColor(COLOR_BORDER);
		if (MainZap.roundCorners)
			g.drawRoundRect(BOUNDS_EXIT.x, BOUNDS_EXIT.y, BOUNDS_EXIT.width, BOUNDS_EXIT.height, BORDER_ROUND_DEEPTH,
					BORDER_ROUND_DEEPTH);
		else
			g.drawRect(BOUNDS_EXIT.x, BOUNDS_EXIT.y, BOUNDS_EXIT.width, BOUNDS_EXIT.height);
		g.setColor(COLOR_EXIT);
		g.drawString(TEXT_EXIT, BOUNDS_EXIT.x + 100, BOUNDS_EXIT.y + BOUNDS_EXIT.height - 12);
		// ------ Exit - Symbol --
		g.setStroke(SYMBOL_EXIT_STROKE);
		g.drawLine(SYMBOL_EXIT_LOCS[0], SYMBOL_EXIT_LOCS[1], SYMBOL_EXIT_LOCS[2], SYMBOL_EXIT_LOCS[3]);
		g.drawLine(SYMBOL_EXIT_LOCS[2], SYMBOL_EXIT_LOCS[1], SYMBOL_EXIT_LOCS[0], SYMBOL_EXIT_LOCS[3]);
		// --

	}

	private static void drawDirSettings(Graphics2D g) {

		g.setColor(COLOR_SETTINGS);
		g.setStroke(STROKE_SET_BACK);
		g.drawLine(SYMBOL_SET_BACK_LOCS[0], SYMBOL_SET_BACK_LOCS[2], SYMBOL_SET_BACK_LOCS[1], SYMBOL_SET_BACK_LOCS[3]);
		g.drawLine(SYMBOL_SET_BACK_LOCS[1], SYMBOL_SET_BACK_LOCS[3], SYMBOL_SET_BACK_LOCS[0], SYMBOL_SET_BACK_LOCS[4]);

		g.setColor(Color.WHITE);
		g.fillRect(BOUNDS_SET_GRAPH_SCALE.x, BOUNDS_SET_GRAPH_SCALE.y, BOUNDS_SET_GRAPH_SCALE.width,
				BOUNDS_SET_GRAPH_SCALE.height);
		g.setColor(COLOR_SETTINGS);
		g.setStroke(STROKE_SET_SCALE);
		g.drawRect(BOUNDS_SET_GRAPH_SCALE.x, BOUNDS_SET_GRAPH_SCALE.y, BOUNDS_SET_GRAPH_SCALE.width,
				BOUNDS_SET_GRAPH_SCALE.height);
		g.setColor(COLOR_BORDER);
		g.setFont(FONT_SET_SCL_INPUT);
		g.drawString(strScale, BOUNDS_SET_GRAPH_SCALE.x + 5,
				BOUNDS_SET_GRAPH_SCALE.y + FONT_SET_SCL_INPUT.getSize() - 1);
		g.setFont(FONT_SET);
		g.drawString("%", BOUNDS_SET_GRAPH_SCALE.x + BOUNDS_SET_GRAPH_SCALE.width + 6,
				BOUNDS_SET_GRAPH_SCALE.y + FONT_SET.getSize() + 4);
		g.setColor(COLOR_SETTINGS);
		g.drawString(TEXT_SET_GRAPH, BOUNDS_SET_GRAPH_SCALE.x - 205,
				BOUNDS_SET_GRAPH_SCALE.y - FONT_SET.getSize() + 16);
		g.setFont(FONT_SET_SUB);
		g.drawString(TEXT_SET_GRAPH_SCALE, BOUNDS_SET_GRAPH_SCALE.x - 142,
				BOUNDS_SET_GRAPH_SCALE.y + FONT_SET.getSize() - 4);

		// Fancy-Effekte:
		g.setFont(FONT_SET_SUB);
		g.setColor(COLOR_SETTINGS);
		g.drawString(TEXT_SET_GRAPH_FANCY, BOUNDS_SET_GRAPH_FANCY.x - 119,
				BOUNDS_SET_GRAPH_FANCY.y + FONT_SET_SUB.getSize() + 4);
		// Fancy-Effekte-Button:
		drawButton(g, BOUNDS_SET_GRAPH_FANCY, MainZap.fancyGraphics, hoveringFancygraphics);

		// Runde-Ecken:
		g.setColor(COLOR_SETTINGS);
		g.setFont(FONT_SET_SUB);
		g.drawString(TEXT_SET_GRAPH_ROUNDCORNERS, BOUNDS_SET_GRAPH_ROUND.x - 159,
				BOUNDS_SET_GRAPH_ROUND.y + FONT_SET_SUB.getSize() + 4);
		// Runde-Ecken-Button:
		drawButton(g, BOUNDS_SET_GRAPH_ROUND, MainZap.roundCorners, hoveringRoundcorners);

		// Antialisizing ---------
		g.setColor(COLOR_SETTINGS);
		g.setFont(FONT_SET_SUB);
		g.drawString(TEXT_SET_AA_GENERAL, BOUNDS_SET_AA_GENERAL.x - 220,
				BOUNDS_SET_AA_GENERAL.y + FONT_SET_SUB.getSize() + 4);
		g.drawString(TEXT_SET_AA_SHIPS, BOUNDS_SET_AA_SHIPS.x - 151,
				BOUNDS_SET_AA_SHIPS.y + FONT_SET_SUB.getSize() + 4);

		// Generelles Antializing Button:
		drawButton(g, BOUNDS_SET_AA_GENERAL, MainZap.generalAntialize, hoveringAA[0]);

		// Schiffe glätten:
		drawButton(g, BOUNDS_SET_AA_SHIPS, MainZap.antializeShips, hoveringAA[1]);

		g.setFont(FONT_SET);
		g.setColor(COLOR_SETTINGS);
		g.drawString(TEXT_SPEEDMODE, BOUNDS_SET_SPEEDBOOST.x - 207, BOUNDS_SET_SPEEDBOOST.y + FONT_SET.getSize());

		// Speedmode-Button:
		drawButton(g, BOUNDS_SET_SPEEDBOOST, MainZap.speedMode, hoveringSpeedMode);
	}

	private static void drawButton(Graphics2D g, Rectangle bounds, boolean on, boolean hovered) {
		g.setStroke(STROKE_SET_AA);
		g.setColor(COLOR_DIA_BG);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		g.setColor(COLOR_SETTINGS);
		g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
		if (hovered) {
			g.setColor(COLOR_SET_HOVER);
			g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		}
		g.setColor(COLOR_SET_AA_FG);
		g.setFont(FONT_SET_AA_BUTTON);
		if (on) {
			g.drawString(TEXT_ON, bounds.x + 7, bounds.y + FONT_SET_AA_BUTTON.getSize() + 2);
		} else {
			g.drawString(TEXT_OFF, bounds.x + 1, bounds.y + FONT_SET_AA_BUTTON.getSize() + 2);
		}
	}

	private static void setOpen(boolean b) {

		if (b && FinishScreen.isOpen())
			return;

		open = b;
		listeningComponent.setVisible(b);
		MainZap.getMainLoop().setPaused(b);
		MainZap.getCollisionLoop().setPaused(b);
		dir = PauseScreenDirectory.MAIN;
		locSettings = Frame.SIZE;
	}

	public static boolean isOpen() {
		return open;
	}

	private static boolean isNumber(char c) {
		return c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9'
				|| c == '0';
	}

}
