package corecase;

import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.Random;

import battle.stage.StageManager;
import collision.Grid;
import gui.AlphaStamp;
import gui.Frame;
import gui.Hud;
import gui.Map;
import gui.PaintingLayer;
import gui.extention.Extention;
import gui.extention.ExtentionManager;
import gui.screens.end.EndScreen;
import gui.screens.finish.FinishScreen;
import gui.screens.pause.PauseScreen;
import gui.shop.Shop;
import gui.shop.ShopSecUpgrade;
import gui.shop.meta.ShipStartConfig;
import ingameobjects.Player;
import io.CashReader;
import io.LootReader;
import io.SettingsInitReader;
import io.TextureBuffer;
import lib.Updateable;
import sched.DynamicUpdateLoop;

public abstract class MainZap {

	public static final String VERSION = "1.3.0";
	public static final String DIRECTORY = determineDirectory();

	public static final boolean FINAL_RUN = true || !inWorkspace();
	public static final boolean PAINT_CALC_THREAD_SPLIT = true;
	public static final Random RANDOM = new Random(System.currentTimeMillis());
	public static final float CRYSTAL_GETBACK = 0.20f;
	private static final int SLEEPTIME_AFTER_WINDOW_INIT = 20;
	private static final boolean SCHEDULE_INSET_RECHECK = false;
	public static boolean debug = false;
	public static boolean grid_debug = false;
	public static boolean speedMode = false;
	public static boolean generalAntialize = true;
	public static boolean antializeShips = true;
	public static boolean fancyGraphics = true;
	public static boolean roundCorners = false;
	public static boolean enableTails = true;
	public static boolean allowBiggerWindow = false;
	public static boolean firstRun = false;
	private static boolean playedThrough = false;
	private static int canvasDx;
	private static int canvasDy;
	private static float scale;
	private static AffineTransform scaleTransform;
	private static int fps = -1;
	private static FpsDiagnosis fpsDiagnosis = new FpsDiagnosis();

	private static Player player;
	private static Frame frame;
	private static PaintingLayer staticLayer;
	private static PaintingLayer dynamicLayer;
	private static Map map;
	private static Grid grid;
	private static DynamicUpdateLoop paintLoop = null;
	private static DynamicUpdateLoop mainLoop = new DynamicUpdateLoop("MainLoop");
	private static DynamicUpdateLoop collisionLoop = new DynamicUpdateLoop("CollisionLoop");

	private static int score = 0;
	private static int crystals = 10;
	private static int crystalsEverEarned = 10;

	public static void main(String[] args) throws InterruptedException {

		TextureBuffer.load();
		ShipStartConfig.loadAll();

		// Einstellungen laden
		generalAntialize = SettingsInitReader.loadSexyGraphics();
		antializeShips = SettingsInitReader.loadSexyShips();
		scale = SettingsInitReader.loadScale();
		fancyGraphics = SettingsInitReader.loadFancyGraphics();
		roundCorners = SettingsInitReader.loadRoundCorners();
		speedMode = SettingsInitReader.loadSpeedmode();
		enableTails = SettingsInitReader.loadEnableTrails();
		firstRun = SettingsInitReader.loadFirstrun();
		// Geld laden
		crystals = CashReader.load() + 9;
		crystalsEverEarned = crystals;
		// Freigeschaltetes laden
		LootReader.load();
		
		frame = new Frame();
		Thread.sleep(SLEEPTIME_AFTER_WINDOW_INIT);
		player = new Player();
		map = new Map();
		grid = new Grid(Map.SIZE, Map.SIZE);
		grid.add(player);

		// Kanten und Skalierung
		Insets s = frame.getInsets();
		canvasDx = s.left;
		canvasDy = s.top;
		scaleTransform = new AffineTransform();
		scaleTransform.scale(scale, scale);
		if (firstRun)
			frame.autoscale();
		else
			frame.rescale(scale);

		if (SCHEDULE_INSET_RECHECK)
			scheduleFrameInsetRecheck();

		// Startparameter abchecken
		for (String a : args) {
			switch (a) {
			case "joelsDopeSchiff":
				Cmd.print("Startparameter 'joelsDopeSchiff' registriert");
				Cmd.print(" -> ACHTERBAHN!!!");
				// OP a la carte
				// Aber nicht unsterblich!
				enableJoelsDopeSchiff();
				break;
			case "disablesexygraphics":
				generalAntialize = false;
				Cmd.print("Sexy Graphics deaktiviert");
				break;
			default:
				Cmd.err("Invalider Startparameter: " + s);
				break;
			}
		}

		// Painting - Stuff
		staticLayer = new PaintingLayer();
		staticLayer.addTask(player);
		staticLayer.addTask(Hud.getPaintingTask());
		staticLayer.addTask(EndScreen.getPaintingtask());
		staticLayer.addTask(FinishScreen.getPaintingTask());

		dynamicLayer = new PaintingLayer();
		dynamicLayer.addTask(map);
		dynamicLayer.addTask(grid.getDebugPaintingTask());

		frame.getContentPane().addMouseListener(player.getClickListener());
		frame.getContentPane().addMouseMotionListener(player.getMotionListener());
		frame.setFocusCycleRoot(true);
		frame.requestFocus();
		frame.addKeyListener(player.getKeyListener());
		frame.addKeyListener(Cmd.getOpenListener());

		if (FINAL_RUN) {
			// Wasser-Zeichen
			AlphaStamp stamp = new AlphaStamp();
			stamp.setBounds(0, 0, (int) (Frame.SIZE * scale), (int) (Frame.SIZE * scale));
			stamp.setLayout(null);
			stamp.setVisible(true);
			stamp.init();
			frame.getContentPane().add(stamp);
			stamp.execute();
			while (!stamp.isDone())
				Thread.sleep(1);
			frame.getContentPane().remove(stamp);
		}

		Freezer.init(); // Freezing-Option aktivieren

		if (FINAL_RUN) {
			StageManager.setUp(1);
			player.applyMeta(ShipStartConfig.get(ShipStartConfig.C_DEFAULT));
		} else {
			StageManager.setUp(0);
			player.applyMeta(ShipStartConfig.get(ShipStartConfig.C_RAINMAKER));
			crystals = 0;
			ShopSecUpgrade.purchaseUpgrade(0);
			ShopSecUpgrade.purchaseUpgrade(0);
			ShopSecUpgrade.purchaseUpgrade(1);
			ShopSecUpgrade.purchaseUpgrade(1);
			ShopSecUpgrade.purchaseUpgrade(3);
			ShopSecUpgrade.purchaseUpgrade(3);
			player.setHp(player.getMaxHp());
		}
		Hud.setUpClickListener();
		ExtentionManager.setUpClickListener();
		PauseScreen.setUp(false);
		Shop.setUp();

		if (FINAL_RUN && firstRun)
			Tutorial.show();

		// -- Loop ----------
		new Thread(mainLoop, "Zapper Update Thread").start();

		if (!PAINT_CALC_THREAD_SPLIT) {
			mainLoop.addTask(TASK_PAINT, false);
		} else {
			paintLoop = new DynamicUpdateLoop("PaintLoop");
			new Thread(paintLoop, "Zapper Paint Thread").start();
			paintLoop.addTask(TASK_PAINT, true);
		}
		mainLoop.addTask(TASK_UPDATE, true);
		if (speedMode) // SPEED IT UP!!!
			mainLoop.setBooster(mainLoop.getTimeBetweenFramesMS() / 2);
		new Thread(collisionLoop, "Zapper Collision Thread").start();
		collisionLoop.addTask(TASK_UPDATE_GRID, true);
		collisionLoop.setTimeBetweenFrames(25); // 40 fps
		if (speedMode) // SPEED IT UP!!!
			collisionLoop.setBooster(collisionLoop.getTimeBetweenFramesMS() / 2);
		// ----

		if (firstRun) {
			firstRun = false;
			SettingsInitReader.save();
		}
	}

	private static final Runnable TASK_UPDATE = new Runnable() {

		@Override
		public void run() {

			try {

				player.update();
				map.update();
				Hud.update();
				Shop.update();
				EndScreen.update();
				if (debug)
					fps = fpsDiagnosis.update();
				// Weitere updates....

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("VVVVV-------------------------------");
				Cmd.err("!! Exception caught in CALC loop. !!");
				if (e.getCause() != null) {
					Cmd.err(">>" + e.getClass().getName() + ": " + e.getCause().getMessage());
				} else {
					Cmd.err(">>" + e.getClass().getName());
				}
				for (StackTraceElement ste : e.getStackTrace()) {
					Cmd.err(": " + ste.toString());
				}
				System.err.println("------------------------------------");
			}
		}

	};

	private static final Runnable TASK_UPDATE_GRID = new Runnable() {

		@Override
		public void run() {

			try {

				grid.update();
				// Weitere updates....

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("VVVVV-------------------------------");
				Cmd.err("!! Exception caught in COLLISION loop. !!");
				if (e.getCause() != null) {
					Cmd.err(">>" + e.getClass().getName() + ": " + e.getCause().getMessage());
				} else {
					Cmd.err(">>" + e.getClass().getName());
				}
				for (StackTraceElement ste : e.getStackTrace()) {
					Cmd.err(": " + ste.toString());
				}
				System.err.println("------------------------------------");
			}
		}

	};

	private static final Runnable TASK_PAINT = new Runnable() {

		@Override
		public void run() {

			try {

				Graphics2D g2d = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();

				// Balken �berbr�cken
				g2d.translate(canvasDx - 1, canvasDy);

				// Vergr��ern?
				g2d.transform(scaleTransform);
				g2d.clearRect(0, 0, Map.SIZE, Map.SIZE);
				g2d.clipRect(0, 0, Map.SIZE, Map.SIZE);

				if (generalAntialize) {
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				}

				dynamicLayer.drawLayer(g2d);
				staticLayer.drawLayer(g2d);
				Shop.paint(g2d);
				PauseScreen.paint(g2d);

				// Balken �berbr�cken
				g2d.translate(canvasDx, canvasDy);

				g2d.dispose();
				frame.getBufferStrategy().show();

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("VVVVV-------------------------------");
				Cmd.err("!! Exception caught in PAINT loop. !!");
				if (e.getCause() != null) {
					Cmd.err(">>" + e.getClass().getName() + ": " + e.getCause().getMessage());
				} else {
					Cmd.err(">>" + e.getClass().getName());
				}
				for (StackTraceElement ste : e.getStackTrace()) {
					Cmd.err(": " + ste.toString());
				}
				System.err.println("------------------------------------");
			}

		}

	};

	public static void restartGame() {

		Cmd.print("Clearing up loops...");
		mainLoop.setRunningOnFreeWheel(true);
		collisionLoop.setRunningOnFreeWheel(true);
		if (PAINT_CALC_THREAD_SPLIT)
			paintLoop.setRunningOnFreeWheel(true);
		mainLoop.clear();
		collisionLoop.clear();
		if (!PAINT_CALC_THREAD_SPLIT) {
			Cmd.print("Option: Split paint and calc. thread - was enabled.");
		} else {
			Cmd.print("Option: Split paint and calc. thread - was disabled.");
			paintLoop.clear();
		}

		Cmd.print("Reassembling loops...");
		if (PAINT_CALC_THREAD_SPLIT)
			paintLoop.addTask(TASK_PAINT, true);
		mainLoop.addTask(TASK_UPDATE, true);
		collisionLoop.addTask(TASK_UPDATE_GRID, true);
		collisionLoop.setTimeBetweenFrames(25); // 40 fps
		if (speedMode) { // SPEED IT UP!!!
			Cmd.print("Option: Speedmode - was enabled");
			mainLoop.setBooster(mainLoop.getTimeBetweenFramesMS() / 2);
			collisionLoop.setBooster(collisionLoop.getTimeBetweenFramesMS() / 2);
		} else {
			Cmd.print("Option: Speedmode - was disabled");
		}
		// ----

		mainLoop.setRunningOnFreeWheel(false);
		collisionLoop.setRunningOnFreeWheel(false);
		if (PAINT_CALC_THREAD_SPLIT)
			paintLoop.setRunningOnFreeWheel(false);
		Cmd.print("Reseting Player...");
		player.totalReset();
		crystals = (int) (crystalsEverEarned * CRYSTAL_GETBACK) + 10;
		crystalsEverEarned = crystals;
		score = 0;
		grid.add(player);
		Cmd.print("Reseting Screens...");
		Shop.reset();
		// Hud.setUpClickListener(); <--- added nur listener. -> schon da
		PauseScreen.setUp(true);
		// Shop.setUp(); <--- added nur listener. -> schon da
		Cmd.print("Reseting Stage...");
		StageManager.setUp(1);
		Cmd.print("Calling GarbageCollector...");
		System.gc();

	}

	public static int clipRGB(int v) {
		if (v > 255)
			return 255;
		if (v < 0)
			return 0;
		return v;
	}

	public static boolean fittsMap(int x, int y) {
		return (x >= 0 && x <= Map.SIZE && y >= 0 && y <= Map.SIZE);
	}

	public static float clip(float pos) {

		if (pos < 0)
			return 0;

		if (pos > Map.SIZE)
			return Map.SIZE;

		return pos;
	}

	public static void setSpeedMode(boolean s) {
		speedMode = s;
		if (s) {
			mainLoop.setBooster(mainLoop.getTimeBetweenFramesMS() / 2);
			collisionLoop.setBooster(collisionLoop.getTimeBetweenFramesMS() / 2);
		} else {
			mainLoop.setBooster(0);
			collisionLoop.setBooster(0);
		}
	}

	public static int inTicks(long ms) {
		return mainLoop.inTicks(ms);
	}

	public static void addScore(int s) {
		if (!playedThrough)
			score += s * StageManager.getActiveStage().getLvl();
		Hud.pushScore();
	}

	public static void addCrystal() {
		crystals++;
		if (!playedThrough)
			crystalsEverEarned++;
	}

	public static void addCrystals(int amount) {
		crystals += amount;
		if (!playedThrough)
			crystalsEverEarned += amount;
	}

	public static void setCrystals(int crystals) {
		MainZap.crystals = crystals;
	}

	public static void removeCrystals(int amount) {
		crystals -= amount;
	}

	public static int getCrystals() {
		return crystals;
	}

	public static int getScore() {
		return score;
	}

	public static void setScore(int score) {
		MainZap.score = score;
	}

	public static Player getPlayer() {
		return player;
	}

	public static Frame getFrame() {
		return frame;
	}

	public static PaintingLayer getStaticLayer() {
		return staticLayer;
	}

	public static PaintingLayer getDynamicLayer() {
		return dynamicLayer;
	}

	public static Map getMap() {
		return map;
	}

	public static DynamicUpdateLoop getMainLoop() {
		return mainLoop;
	}

	public static DynamicUpdateLoop getCollisionLoop() {
		return collisionLoop;
	}

	public static Grid getGrid() {
		return grid;
	}

	public static int rand(int bound) {
		return RANDOM.nextInt(bound);
	}

	public static int getFps() {
		return fps;
	}

	public static float getScale() {
		return scale;
	}

	public static void setScale(float s) {
		scale = s;
		frame.rescale(s);
		scaleTransform = new AffineTransform();
		scaleTransform.scale(s, s);
	}

	public static void enableJoelsDopeSchiff() {
		player.setTexture(TextureBuffer.get(TextureBuffer.NAME_PLAYERSHIP_ASHSLIDER));
		player.setTextureScale(0.5f);
		player.setProjDesign(new DopeSchiffProjectileDesign());
		player.setAmmoUsageFac(0.8f);
		player.setBulletSpeed(3.0f);
		player.setMaxWeaponCooldownWith(0.1f);
		player.setMaxWeaponCooldown(0.1f);
		player.setMaxHp(Integer.MAX_VALUE / 2);
		player.setHp(Integer.MAX_VALUE / 2);
		player.setSpeed(10.0f);
		player.setBulletSpeed(17.0f);
		player.setBulletDamage(700);
	}

	public static String determineDirectory() {
		String s = new File("").getAbsolutePath();
		System.out.println("[Debug] Path is: " + s);
		return s;
	}

	private static boolean inWorkspace() {
		String path = MainZap.class.getResource("").toString();
		return !path.contains("jar");
	}

	public static AffineTransform getScaleTransform() {
		return scaleTransform;
	}

	public static void pushLoops() {
		collisionLoop.pushUpdate();
		mainLoop.pushUpdate();
		paintLoop.pushUpdate();
	}

	public static int getCrystalsEverEarned() {
		return crystalsEverEarned;
	}

	public static double getRotation(double aim_dx, double aim_dy) {
		return Math.PI - Math.atan2(aim_dx, aim_dy);
	}

	private static void scheduleFrameInsetRecheck() {
		final long now = System.currentTimeMillis();
		map.addUpdateElement(new Updateable() { // Sp�ter erneut abfragen
			@Override
			public void update() {
				if (System.currentTimeMillis() - now > 2000) { // nach 2
																// Sekunden
					Insets s = frame.getInsets();
					canvasDx = s.left;
					canvasDy = s.top;
					map.removeUpdateElement(this);
					map.addUpdateElement(new Updateable() { // Sp�ter erneut
															// abfragen
						@Override
						public void update() {
							if (System.currentTimeMillis() - now > 5000) { // nach
																			// 7
																			// Sekunden
								Insets s = frame.getInsets();
								canvasDx = s.left;
								canvasDy = s.top;
								map.removeUpdateElement(this);
							}
						}
					});
				}
			}
		});
	}

	public static boolean isPlayedThrough() {
		return playedThrough;
	}

	public static void setPlayedThrough(boolean playedThrough) {
		MainZap.playedThrough = playedThrough;
	}

}
