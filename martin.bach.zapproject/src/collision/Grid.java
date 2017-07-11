package collision;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import battle.enemy.Enemy;
import corecase.MainZap;
import gui.Frame;
import gui.Map;
import ingameobjects.InteractiveObject;
import lib.PaintingTask;
import lib.ScheduledList;
import lib.SpeedVector;

public class Grid {

	private static final int DEFAULT_TILE_SIZE = 200;
	private static final float INTERFERING_RANGE_FACTOR = 1.5f;
	private static final float PUSHING_SPEED_DEFAULT = 0.3f;

	private ScheduledList<Collideable> totalObjects = new ScheduledList<Collideable>();
	private ArrayList<Collideable> outsideObjects = new ArrayList<Collideable>();

	private int tileSize = DEFAULT_TILE_SIZE;
	private Tile[][] tiles;
	private int rows;
	private int colums;
	private int tileSizeX;
	private int tileSizeY;

	public Grid(int width, int height) {
		int restX = width % tileSize;
		int restY = height % tileSize;
		colums = (width - restX) / tileSize;
		rows = (height - restY) / tileSize;
		int restXPerTile = (restX / colums) + 1;
		int restYPerTile = (restY / rows) + 1;
		tileSizeX = tileSize + restXPerTile;
		tileSizeY = tileSize + restYPerTile;

		tiles = new Tile[colums][rows];

		for (int x = 0; x != colums; x++) {
			for (int y = 0; y != rows; y++) {
				tiles[x][y] = new Tile(x * tileSizeX, y * tileSizeY, tileSizeX, tileSizeY);
			}
		}

	}

	public Grid(int width, int height, int tileSize) {
		this.tileSize = tileSize;
		int restX = width % tileSize;
		int restY = height % tileSize;
		colums = (width - restX) / tileSize;
		rows = (height - restY) / tileSize;
		int restXPerTile = (restX / colums) + 1;
		int restYPerTile = (restY / rows) + 1;
		tileSizeX = tileSize + restXPerTile;
		tileSizeY = tileSize + restYPerTile;

		tiles = new Tile[colums][rows];

		for (int x = 0; x != colums; x++) {
			for (int y = 0; y != rows; y++) {
				tiles[x][y] = new Tile(x * tileSizeX, y * tileSizeY, tileSizeX, tileSizeY);
			}
		}

	}

	public void update() {
		try {
			totalObjects.update();
			resortObjects();
			checkForCollision();
		} catch (Exception e) {
			// Dafür sorgen, dass das loop nicht abkackt
			e.printStackTrace();
		}
	}

	public void rebuild(int width, int height, int tileSize) {
		this.tileSize = tileSize;
		int restX = width % tileSize;
		int restY = height % tileSize;
		colums = (width - restX) / tileSize;
		rows = (height - restY) / tileSize;
		int restXPerTile = (restX / colums) + 1;
		int restYPerTile = (restY / rows) + 1;
		tileSizeX = tileSize + restXPerTile;
		tileSizeY = tileSize + restYPerTile;

		tiles = new Tile[colums][rows];

		for (int x = 0; x != colums; x++) {
			for (int y = 0; y != rows; y++) {
				tiles[x][y] = new Tile(x * tileSizeX, y * tileSizeY, tileSizeX, tileSizeY);
			}
		}
	}

	public void add(Collideable c) {
		totalObjects.schedAdd(c);
	}

	public void remove(Collideable c) {
		totalObjects.schedRemove(c);
	}

	private void checkForCollision() {

		int c = 0;
		int r = 0;

		for (Tile[] colum : tiles) {
			r = 0;

			// Von oben nach unten. Von links nach rechts

			if (c != colums - 1) { // Nicht ganz rechts

				for (Tile row : colum) {

					for (Collideable c0 : row.getObjects()) {
						for (Collideable c1 : row.getObjects()) {
							// Dieses Feld abgehen
							if (intersects(c0, c1) && mayCollide(c0, c1)) {
								c0.collide(c1);
								c1.collide(c0);
							}
							if (interfers(c0, c1)) {
								c0.push(c1, PUSHING_SPEED_DEFAULT);
								c1.push(c0, PUSHING_SPEED_DEFAULT);
							}
						}
						for (Collideable c1 : tiles[c + 1][r].getObjects()) {
							// Rechts abgehen
							if (intersects(c0, c1) && mayCollide(c0, c1)) {
								c0.collide(c1);
								c1.collide(c0);
							}
							if (interfers(c0, c1)) {
								c0.push(c1, PUSHING_SPEED_DEFAULT);
								c1.push(c0, PUSHING_SPEED_DEFAULT);
							}
						}

						if (r == rows - 1) { // darunter nicht prüfbar
							continue;
						}
						for (Collideable c1 : tiles[c][r + 1].getObjects()) {
							// Unten abgehen
							if (intersects(c0, c1) && mayCollide(c0, c1)) {
								c0.collide(c1);
								c1.collide(c0);
							}
							if (interfers(c0, c1)) {
								c0.push(c1, PUSHING_SPEED_DEFAULT);
								c1.push(c0, PUSHING_SPEED_DEFAULT);
							}
						}

						for (Collideable c1 : tiles[c + 1][r + 1].getObjects()) {
							// Unten Rechts abgehen
							if (intersects(c0, c1) && mayCollide(c0, c1)) {
								c0.collide(c1);
								c1.collide(c0);
							}
							if (interfers(c0, c1)) {
								c0.push(c1, PUSHING_SPEED_DEFAULT);
								c1.push(c0, PUSHING_SPEED_DEFAULT);
							}
						}
					}

					r++;
				}
			} else { // ganz rechts
				for (Tile row : colum) {
					if (r == rows - 1) { // schnon vorher gecheckt
						continue;
					}

					for (Collideable c0 : row.getObjects()) {
						for (Collideable c1 : row.getObjects()) {
							// Dieses Feld abgehen
							if (intersects(c0, c1) && mayCollide(c0, c1)) {
								c0.collide(c1);
								c1.collide(c0);
							}
							if (interfers(c0, c1)) {
								c0.push(c1, PUSHING_SPEED_DEFAULT);
								c1.push(c0, PUSHING_SPEED_DEFAULT);
							}
						}

						for (Collideable c1 : tiles[c][r + 1].getObjects()) {
							// Unten abgehen
							if (intersects(c0, c1) && mayCollide(c0, c1)) {
								c0.collide(c1);
								c1.collide(c0);
							}
							if (interfers(c0, c1)) {
								c0.push(c1, PUSHING_SPEED_DEFAULT);
								c1.push(c0, PUSHING_SPEED_DEFAULT);
							}
						}

					}

				}

				r++;
			}
			c++;
		}

		// Objekte außerhalb der Grid auf Kollision prüfen
		// Das hier geht besser. Man könnte unendlich große Außenkacheln
		// einbauen und auch Kacheln am Rand über Kollision abprüfen, aber
		// billig gehts auch so:
		for (Collideable c0 : outsideObjects) {
			for (Collideable c1 : outsideObjects) {
				if (intersects(c0, c1) && mayCollide(c0, c1)) {
					c0.collide(c1);
					c1.collide(c0);
				}
				if (interfers(c0, c1)) {
					c0.push(c1, PUSHING_SPEED_DEFAULT);
					c1.push(c0, PUSHING_SPEED_DEFAULT);
				}
			}
		}

	}

	private void resortObjects() {

		outsideObjects.clear();
		for (Tile[] colum : tiles) {
			for (Tile row : colum) {
				row.getObjects().clear();
			}
		}

		for (Collideable c : totalObjects) {
			if (c == null) // Glitch
				continue;
			int x = c.getLocation()[0];
			int y = c.getLocation()[1];
			if (x < 0 || x >= Map.SIZE || y < 0 || y >= Map.SIZE) {
				// Außerhalb der Map
				outsideObjects.add(c);
				continue;
			}
			tiles[(int) (x / tileSizeX)][(int) (y / tileSizeY)].addObject(c);
		}

	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public static int distance(Point2D a, Point2D b) {
		return (int) Math
				.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY()));
	}

	public static boolean inRange(Point2D a, Point2D b, int d) {
		if ((int) Math.sqrt(
				(a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY())) <= d) {
			return true;
		}
		return false;
	}

	public static boolean intersects(Collideable a, Collideable b) {
		return inRange(new Point(a.getLocation()[0], a.getLocation()[1]),
				new Point(b.getLocation()[0], b.getLocation()[1]), getSpeedDistance(a, b));
	}

	public static boolean mayCollide(Collideable a, Collideable b) {

		if (a == b) { // Kollision mit sich selbst
			return false;
		}

		CollisionType ta = a.getInformation().getType();
		CollisionType tb = b.getInformation().getType();

		if ((ta == CollisionType.COLLIDE_WITH_ALL && tb != CollisionType.DO_NOT_COLLIDE)
				|| (tb == CollisionType.COLLIDE_WITH_ALL && ta != CollisionType.DO_NOT_COLLIDE)) {
			// Kollision mit allem
			return true;
		}

		switch (a.getInformation().getType()) {
		case COLLIDE_WITH_FRIENDS:
			return tb == CollisionType.COLLIDE_WITH_ENEMYS
					|| tb == CollisionType.COLLIDE_AS_PLAYER && ta != CollisionType.DO_NOT_COLLIDE;
		case COLLIDE_AS_PLAYER:
			return tb == CollisionType.COLLIDE_ONLY_WITH_PLAYER
					|| tb == CollisionType.COLLIDE_WITH_FRIENDS && ta != CollisionType.DO_NOT_COLLIDE;
		case COLLIDE_ONLY_WITH_PLAYER:
			return tb == CollisionType.COLLIDE_AS_PLAYER && ta != CollisionType.DO_NOT_COLLIDE;
		case COLLIDE_WITH_ENEMYS:
			return tb == CollisionType.COLLIDE_WITH_FRIENDS && ta != CollisionType.DO_NOT_COLLIDE;
		case DO_NOT_COLLIDE:
			return false;
		default:
			return false;
		}

	}

	private static boolean interfers(Collideable a, Collideable b) {

		if (a == b)
			return false; // Identisch

		if (!a.getInformation().mayInterfer() || !b.getInformation().mayInterfer()) {
			return false;
		}

		if (inRange(new Point(a.getLocation()[0], a.getLocation()[1]),
				new Point(b.getLocation()[0], b.getLocation()[1]),
				(int) ((a.getInformation().getRadius() * INTERFERING_RANGE_FACTOR)
						+ (b.getInformation().getRadius() * INTERFERING_RANGE_FACTOR)))) {
			return true;
		}
		return false;

	}

	private static int getSpeedDistance(Collideable a, Collideable b) {

		if (!(a instanceof InteractiveObject) || !(b instanceof InteractiveObject)) {
			return (int) (a.getInformation().getRadius() + b.getInformation().getRadius());
		} else {

			SpeedVector va = ((InteractiveObject) a).getVelocity();
			SpeedVector vb = ((InteractiveObject) b).getVelocity();

			return (int) (a.getInformation().getRadius() + b.getInformation().getRadius() + Math.abs(va.getX())
					+ Math.abs(va.getY()) + Math.abs(vb.getX()) + Math.abs(vb.getY()));
		}

	}

	public ArrayList<Enemy> getEnemySurrounding(int cx, int cy, int range) {

		int ix = cx / tileSizeX; // index-x
		if (ix > colums - 1)
			ix = colums - 1;
		if (ix < 0)
			ix = 0;

		int iy = cy / tileSizeY; // index-y
		if (iy > rows - 1)
			iy = rows - 1;
		if (iy < 0)
			iy = 0;

		int ir; // index-range
		if (range % tileSizeX == 0) {
			ir = range / tileSizeX;
		} else {
			ir = (range / tileSizeX) + 1;
		}

		ArrayList<Enemy> collect = new ArrayList<Enemy>();

		for (int dix = -ir; dix <= ir; dix++) {
			// Delta-Index-X (von -range bis +range)

			for (int diy = -ir; diy <= ir; diy++) {
				// Delta-Index-Y (von -range bis +range)

				int x = ix + dix; // finaler Kachel-Index
				int y = iy + diy;

				// Gibts es diese Kachel überhaupt?
				if (x < 0 || x > colums - 1 || y < 0 || y > rows - 1)
					continue; // nö. Außer Grid-Range

				// Liste puffern (ConcurrentMod vermeiden)
				ArrayList<Collideable> elementBuffer = new ArrayList<Collideable>(tiles[x][y].getObjects());
				for (Collideable c : elementBuffer) {

					// Auf Typ prüfen und sammeln
					if (c instanceof Enemy)
						collect.add((Enemy) c);

				}
			}
		}
		return collect;
	}

	public ArrayList<Collideable> getTotalSurrounding(int cx, int cy, int range) {

		int ix = cx / tileSizeX; // index-x
		if (ix > colums - 1)
			ix = colums - 1;
		if (ix < 0)
			ix = 0;

		int iy = cy / tileSizeY; // index-y
		if (iy > rows - 1)
			iy = rows - 1;
		if (iy < 0)
			iy = 0;

		int ir; // index-range
		if (range % tileSizeX == 0) {
			ir = range / tileSizeX;
		} else {
			ir = (range / tileSizeX) + 1;
		}

		ArrayList<Collideable> collect = new ArrayList<Collideable>();

		for (int dix = -ir; dix <= ir; dix++) {
			// Delta-Index-X (von -range bis +range)

			for (int diy = -ir; diy <= ir; diy++) {
				// Delta-Index-Y (von -range bis +range)

				int x = ix + dix; // finaler Kachel-Index
				int y = iy + diy;

				// Gibts es diese Kachel überhaupt?
				if (x < 0 || x > colums - 1 || y < 0 || y > rows - 1)
					continue; // nö. Außer Grid-Range

				collect.addAll(tiles[x][y].getObjects());
			}
		}
		return collect;
	}

	private PaintingTask debugPaintingTask = new PaintingTask() {

		@Override
		public void paint(Graphics2D g) {

			if (!MainZap.debug || !MainZap.grid_debug) {
				return;
			}

			// Von 0/0 Kontext zu Karte Kontext
			int dx = (int) MainZap.getPlayer().getPosX() - Frame.HALF_SCREEN_SIZE;
			int dy = (int) MainZap.getPlayer().getPosY() - Frame.HALF_SCREEN_SIZE;
			g.translate(-dx, -dy);

			for (Tile[] collum : tiles)
				for (Tile row : collum) {

					g.setColor(Color.BLACK);
					g.drawString(row.getX() + " / " + row.getY(), row.getX() + 10, row.getY() + 10);
					g.setColor(Color.RED);
					g.drawRect(row.getX(), row.getY(), row.getWidth(), row.getHeight());
					g.setColor(new Color(255, 0, 0, 60));
					for (int i = 0; i != row.getObjects().size(); i++) {
						g.fillRect(row.getX(), row.getY(), row.getWidth(), row.getHeight());
					}
				}

			// Zurück zu 0/0 Kontext
			g.translate(dx, dy);
		}

	};

	/**
	 * Knapp effizienter als die Java-Implementation (Math.sqr) Aber dafür extrem
	 * ungenau
	 * 
	 * @param a
	 * @return
	 */
	public static float fastSqr(float a) {
		// Heron-Verfahren

		float lastValue = (a + 1) / 2.0f; // idealer Startwert

		lastValue = (lastValue + (a / lastValue)) / 2.0f;
		lastValue = (lastValue + (a / lastValue)) / 2.0f;

		return lastValue;
	}

	public ArrayList<Enemy> debugGetSurrounding(int cx, int cy, int range) {

		int ix = cx / tileSizeX; // index-x
		if (ix > colums - 1)
			ix = colums - 1;
		if (ix < 0)
			ix = 0;

		int iy = cy / tileSizeY; // index-y
		if (iy > rows - 1)
			iy = rows - 1;
		if (iy < 0)
			iy = 0;

		int ir; // index-range
		if (range % tileSizeX == 0) {
			ir = range / tileSizeX;
		} else {
			ir = (range / tileSizeX) + 1;
		}

		System.out.println("recieved r: " + range);
		System.out.println("recieved x: " + cx + "/" + cy);
		System.out.println("tilesize: " + tileSizeX);
		System.out.println("indexrange: " + ir);

		ArrayList<Enemy> collect = new ArrayList<Enemy>();

		for (int dix = -ir; dix <= ir; dix++) {
			// Delta-Index-X (von -range bis +range)

			for (int diy = -ir; diy <= ir; diy++) {
				// Delta-Index-Y (von -range bis +range)

				int x = ix + dix; // finaler Kachel-Index
				int y = iy + diy;

				System.out.println("checking tile x: " + tiles[x][y].getX() + " - "
						+ (tiles[x][y].getX() + tiles[x][y].getWidth()) + " ; y: " + tiles[x][y].getY() + " - "
						+ (tiles[x][y].getY() + tiles[x][y].getHeight()));

				// Gibts es diese Kachel überhaupt?
				if (x < 0 || x > colums - 1 || y < 0 || y > rows - 1)
					continue; // nö. Außer Grid-Range

				// Liste puffern (ConcurrentMod vermeiden)
				ArrayList<Collideable> elementBuffer = new ArrayList<Collideable>(tiles[x][y].getObjects());
				for (Collideable c : elementBuffer) {

					System.out.println("  adding from: " + c.getLocation()[0] + " | " + c.getLocation()[1]);

					// Auf Typ prüfen und sammeln
					if (c instanceof Enemy)
						collect.add((Enemy) c);

				}
			}
		}
		return collect;
	}

	public PaintingTask getDebugPaintingTask() {
		return debugPaintingTask;
	}

	public int getTileSize() {
		return tileSize;
	}

}
