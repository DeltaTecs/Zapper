package corecase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

import battle.enemy.Enemy;
import battle.projectile.Projectile;
import battle.stage.StageManager;
import gui.Frame;
import gui.Mirroring;
import ingameobjects.Collectable;
import ingameobjects.InteractiveObject;
import ingameobjects.Player;

public abstract class Cmd {

	protected static final int WITDH = 400;
	protected static final int HEIGHT = 400;
	protected static final Color COLOR_BG = Color.BLACK;
	protected static final Color COLOR_FG_SDT = new Color(0, 255, 0);
	protected static final Color COLOR_FG_ERR = new Color(255, 178, 0);
	protected static final int FONT_SIZE = 15;
	protected static final Font FONT = new Font("Sans Serif", 0, FONT_SIZE);
	protected static final String FLAG_ERROR = "%err%";
	protected static final int MAX_ENTRYS = 40;
	protected static final int BORDERSPACE = 2;

	protected static final String[] COMMANDLIST = new String[] { "passlvl", "exelvl <int>", "heal", "setspeed <float>",
			"wipeout", "debug <bool>", "griddebug <bool>", "restart", "exit", "enablesexygraphics", "enabledopeschiff",
			"throwerr", "rescale <float>", "addcash <int>", "activatemirror" };

	protected static int canvasDx;
	protected static int canvasDy;
	protected static ArrayList<String> entrys = new ArrayList<String>();
	protected static JFrame currentWindow = null;
	protected static String input = "";
	protected static boolean waiting = false;
	protected static String requestedInput = "";
	protected static boolean modeCoEditor = false;
	protected static boolean active = false;

	private static boolean[] keys_OC_pressed = new boolean[] { false, false };
	
	// Opens the frame
	private static void open() {
		JFrame frame = getCmdFrame();
		Canvas canvas = new Canvas();

		frame.getContentPane().add(canvas);
		frame.pack();
		frame.repaint();

		currentWindow = frame;

		if (!MainZap.FINAL_RUN) { // Nur verifizieren, wenn finale edition
			active = true;
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				verify();
				active = true;
			}
		}).start();

	}

	// Gibt den Rahmen
	private static JFrame getCmdFrame() {

		JFrame f = new JFrame("zapper debug cmd");
		f.getContentPane().setPreferredSize(
				new Dimension((int) (WITDH * MainZap.getScale()), (int) (HEIGHT * MainZap.getScale())));
		f.setLayout(null);
		f.pack();
		f.setVisible(true);
		f.setResizable(false);
		canvasDx = f.getInsets().left;
		canvasDy = f.getInsets().top;
		Point p = MainZap.getFrame().getLocation();
		f.setLocation((int) (p.getX() + Frame.SIZE * MainZap.getScale()), (int) (p.getY()));
		f.addKeyListener(INPUT_LISTENER);

		return f;
	}

	protected static void verify() {

		print("Who are you?");
		String in = nextInput();

		if (in.equalsIgnoreCase("joel")) {
			print("What is your profession?");
			String in1 = nextInput();
			if (in1.equalsIgnoreCase("bitcoinminer")) {
				print("Successfully verified as co-editor.");
				modeCoEditor = true;
				return;
			} else {
				print("Sorry my dude. Wrong buisness.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				currentWindow.setVisible(false);
				entrys.clear();
				return;
			}

		}

		if (in.equalsIgnoreCase("player") || in.equalsIgnoreCase("implayer")) {
			print("This is not your space.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentWindow.setVisible(false);
			entrys.clear();
			return;
		}

		if (!in.equals("imdev")) {
			print("Wrong answer.");
			System.exit(0);
		}
		print("Who is commander?");
		in = nextInput();
		if (!in.equals("harris")) {
			print("Wrong answer.");
			System.exit(0);
		}
		print("Successfully verified.");

	}

	public static void print(String s, boolean err) {

		if (err) {
			System.err.println(s);
			s = FLAG_ERROR + s;
		} else {
			System.out.println(s);
		}

		entrys.add(s);

		if (entrys.size() > MAX_ENTRYS)
			entrys.remove(0);

		if (currentWindow != null)
			currentWindow.repaint();
	}

	public static void print(String s) {

		System.out.println(s);

		entrys.add(s);

		if (entrys.size() > MAX_ENTRYS)
			entrys.remove(0);

		if (currentWindow != null)
			currentWindow.repaint();

	}

	public static void err(String s) {

		System.err.println(s);
		s = FLAG_ERROR + s;

		entrys.add(s);

		if (entrys.size() > MAX_ENTRYS)
			entrys.remove(0);

		if (currentWindow != null)
			currentWindow.repaint();

	}

	public static void rescale() {
		currentWindow.dispose();

		currentWindow = getCmdFrame();
		Canvas canvas = new Canvas();
		currentWindow.getContentPane().add(canvas);
		currentWindow.pack();
		currentWindow.repaint();
	}

	private static final KeyListener OPEN_LISTENER = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {

			if (keys_OC_pressed[0] && keys_OC_pressed[1])
				// alle gedrückt
				open();

			// Einen losgelassen?
			if (e.getKeyCode() == KeyEvent.VK_O)
				keys_OC_pressed[0] = false;
			if (e.getKeyCode() == KeyEvent.VK_C)
				keys_OC_pressed[1] = false;

		}

		@Override
		public void keyPressed(KeyEvent e) {

			// Einen gedrückt?
			if (e.getKeyCode() == KeyEvent.VK_O)
				keys_OC_pressed[0] = true;
			if (e.getKeyCode() == KeyEvent.VK_C)
				keys_OC_pressed[1] = true;

		}
	};

	private static final KeyListener INPUT_LISTENER = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyPressed(final KeyEvent e) {

			new Thread(new Runnable() {

				@Override
				public void run() {

					if (e.getKeyCode() == KeyEvent.VK_ENTER) {

						if (waiting) {
							requestedInput = input;
							waiting = false;
							input = "";
						} else {
							String in = input + "";
							input = "";
							CommandLibrary.execute(in);
						}

					}

					if (StringConverter.isClassic(e.getKeyChar())) {
						input += e.getKeyChar();
					} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && input.length() > 0) {
						input = input.substring(0, input.length() - 1);
					}

					currentWindow.repaint();
				}
			}).start();

		}
	};

	public static void flush() {
		currentWindow.repaint();
	}

	private static String nextInput() {

		waiting = true;

		while (waiting) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return requestedInput;

	}

	public static KeyListener getOpenListener() {
		return OPEN_LISTENER;
	}

	public static boolean isActive() {
		return active;
	}

}

class CommandLibrary {

	public static void execute(String userInput) {

		Cmd.print("| " + userInput);

		String[] in = userInput.split(" ");
		int parts = in.length;

		if (parts == 0) {
			p(" ");
			return;
		}

		String cmd = in[0];

		switch (cmd) {
		case "verify":
			Cmd.verify();
			break;
		case "list":
			printCommandList();
			break;
		case "passlvl":
			StageManager.getActiveStage().pass();
			p("Stage passed.");
			break;
		case "exelvl":
			if (Cmd.modeCoEditor) { // Gesperrter Befehl
				Cmd.err("Access denied.");
				break;
			}
			if (parts != 2) {
				printInvalidArgs("exelvl <int>");
				return;
			}
			if (!isNumber(in[1])) {
				printInvalidArgs("exelvl <int>");
				return;
			}
			StageManager.setUp(Integer.parseInt(in[1]));
			p("Level executed.");
			break;

		case "heal":
			MainZap.getPlayer().heal(MainZap.getPlayer().getMaxHp());
			p("Health restored.");
			break;
		case "setspeed":
			if (Cmd.modeCoEditor) { // Gesperrter Befehl
				Cmd.err("Access denied.");
				break;
			}
			if (parts != 2) {
				printInvalidArgs("setspeed <float>");
				return;
			}
			if (!isFloat(in[1])) {
				printInvalidArgs("setspeed <float>");
				return;
			}
			MainZap.getPlayer().setSpeed(Float.parseFloat(in[1]));
			p("Player Speed was set to " + in[1] + " px/t.");
			break;
		case "wipeout":
			if (Cmd.modeCoEditor) { // Gesperrter Befehl
				Cmd.err("Access denied.");
				break;
			}
			int k = 0;
			ArrayList<InteractiveObject> buffer = new ArrayList<InteractiveObject>(
					InteractiveObject.getListedObjects());
			for (InteractiveObject o : buffer) {

				if (o instanceof Player)
					continue;

				if (o instanceof Projectile || o instanceof Collectable)
					o.unRegister();

				if (o instanceof Enemy) {
					((Enemy) o).explode();
					p("killed " + o.getClass().getSimpleName());
				}

				k++;
			}
			p("-- Wipeout finished. " + k + " objects deleted.");
			break;

		case "debug":
			if (Cmd.modeCoEditor) { // Gesperrter Befehl
				Cmd.err("Access denied.");
				break;
			}
			if (parts != 2) {
				printInvalidArgs("debug <bool>");
				return;
			}
			if (!isBoolean(in[1])) {
				printInvalidArgs("debug <bool>");
				return;
			}

			boolean b0 = Boolean.parseBoolean(in[1]);
			MainZap.debug = b0;
			p("Debug-Mode was set to " + b0 + ".");
			break;

		case "griddebug":
			if (Cmd.modeCoEditor) { // Gesperrter Befehl
				Cmd.err("Access denied.");
				break;
			}
			if (parts != 2) {
				printInvalidArgs("griddebug <bool>");
				return;
			}
			if (!isBoolean(in[1])) {
				printInvalidArgs("griddebug <bool>");
				return;
			}

			boolean b1 = Boolean.parseBoolean(in[1]);
			MainZap.grid_debug = b1;
			p("Grid-Debug-Mode was set to " + b1 + ".");
			break;

		case "restart":
			Cmd.print("Resering game. Re-initialising...");
			MainZap.restartGame();
			Cmd.print("Reset finished.");
			break;
		case "exit":
			Cmd.print("Quiting...");
			System.exit(1);
			break;
		case "enablesexygraphics":
			if (parts != 2) {
				printInvalidArgs("enablesexygraphics <bool>");
				return;
			}
			if (!isBoolean(in[1])) {
				printInvalidArgs("enablesexygraphics <bool>");
				return;
			}
			MainZap.generalAntialize = Boolean.parseBoolean(in[1]);
			Cmd.print("Sexy Graphics are turned to " + in[1] + ".");
			break;
		case "enabledopeschiff":
			MainZap.enableJoelsDopeSchiff();
			Cmd.print("DopeSchiff enabled. This feature might be out of date.");
			break;
		case "throwerr":
			if (Cmd.modeCoEditor) { // Gesperrter Befehl
				Cmd.err("Access denied. Y u wana crash da game?");
				break;
			}
			Cmd.print("Inserting NullPointerException...");
			MainZap.getMap().addPaintElement(null, false); // Fehler
			break;
		case "rescale":
			if (parts != 2) {
				printInvalidArgs("rescale <float>");
				return;
			}
			if (!isFloat(in[1])) {
				printInvalidArgs("rescale <float>");
				return;
			}
			float scale = Float.parseFloat(in[1]);
			MainZap.setScale(scale);
			Cmd.rescale();
			Cmd.print("GUI was rescaled to " + scale + "x");
			break;
		case "addcash":
			if (parts != 2) {
				printInvalidArgs("addcash <int>");
				return;
			}
			if (!isNumber(in[1])) {
				printInvalidArgs("addcash <int>");
				return;
			}
			MainZap.setCrystals(MainZap.getCrystals() + Integer.parseInt(in[1]));
			Cmd.print(in[1] + " crystals added.");
			break;
		case "activatemirror":
			if (parts != 1) {
				printInvalidArgs("No args requiered");
				return;
			}
			if (Mirroring.isActive()) {
				Mirroring.cancel();
				Cmd.print("Mirroring was already active. Cancled.");
			}
			Mirroring.activate();
			Cmd.print("Effect Mirror executed.");
			break;
		default:
			printInvalidCommand();
			break;
		}

	}

	private static void printCommandList() {

		p("*--- cmd list ---- *");

		for (String s : Cmd.COMMANDLIST) {
			p(s);
		}

		p("--------");
	}

	// Print
	private static void p(String s) {
		Cmd.print(s);
	}

	private static void printInvalidCommand() {
		Cmd.print("Unknown command.", true);
	}

	private static void printInvalidArgs(String info) {
		Cmd.print("Invalid arguments: " + info, true);
	}

	private static boolean isNumber(String s) {
		for (char c : s.toCharArray()) {
			if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7'
					|| c == '8' || c == '9'))
				return false;
		}
		return true;
	}

	private static boolean isFloat(String s) {
		for (char c : s.toCharArray()) {
			if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7'
					|| c == '8' || c == '9' || c == '.'))
				return false;
		}

		return true;
	}

	private static boolean isBoolean(String s) {
		return s.equals("true") || s.equals("false");
	}

}

class Canvas extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Canvas() {
		setBounds(0, 0, 10000, 10000);
		setLayout(null);
		setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g0) {
		super.paintComponent(g0);

		Graphics2D g = (Graphics2D) g0;
		g.transform(MainZap.getScaleTransform());

		ArrayList<String> entrys = new ArrayList<String>(Cmd.entrys);

		g.setColor(Cmd.COLOR_BG);
		g.fillRect(0, 0, Cmd.WITDH, Cmd.HEIGHT);

		g.setColor(Cmd.COLOR_FG_SDT);
		g.fillRect(0, Cmd.HEIGHT - Cmd.FONT_SIZE - 4, Cmd.WITDH, 2);

		g.setFont(Cmd.FONT);

		int totalHeight = 0;
		// Totale Höhe berechnen
		for (String s : entrys) {

			int totalLen = g.getFontMetrics().stringWidth(s);
			for (int i = totalLen; i >= 0; i -= Cmd.WITDH - 2 * Cmd.BORDERSPACE) {
				totalHeight += Cmd.FONT_SIZE + Cmd.BORDERSPACE / 2;
			}
			totalHeight += Cmd.BORDERSPACE / 2;
		}

		// Startpunkt festlegen:
		int availableHeight = Cmd.HEIGHT - Cmd.FONT_SIZE - 4 - Cmd.BORDERSPACE;
		int y = 0;
		if (totalHeight > availableHeight)
			y = availableHeight - totalHeight;

		int maxLen = Cmd.WITDH - 2 * Cmd.BORDERSPACE;

		// Zeichen
		for (String s : entrys) {

			ArrayList<String> lines = new ArrayList<String>();

			String currentLine = "> ";
			if (s.startsWith("| "))
				currentLine = "";

			g.setColor(Cmd.COLOR_FG_SDT);

			if (s.startsWith(Cmd.FLAG_ERROR)) { // Fehlermeldung
				g.setColor(Cmd.COLOR_FG_ERR);
				s = s.replaceAll(Cmd.FLAG_ERROR, "");
			}

			for (char c : s.toCharArray()) {

				if (g.getFontMetrics().stringWidth(currentLine + c) > maxLen) {
					lines.add(currentLine);
					currentLine = "  ";
				}

				currentLine += c;
			}

			if (!currentLine.equals("  ")) // Zeile nicht am Anschlag
				lines.add(currentLine);

			for (String line : lines) {
				g.drawString(line, Cmd.BORDERSPACE, y + Cmd.FONT_SIZE);
				y += Cmd.FONT_SIZE + Cmd.BORDERSPACE / 2;
			}

			y += Cmd.BORDERSPACE / 2;

		}

		g.setColor(Cmd.COLOR_FG_SDT);
		g.drawString(Cmd.input, Cmd.BORDERSPACE, Cmd.HEIGHT - Cmd.BORDERSPACE - 2);
	}

}
