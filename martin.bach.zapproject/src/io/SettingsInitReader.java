package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;

import corecase.Cmd;
import corecase.MainZap;

public abstract class SettingsInitReader {

	private static final File FILE = new File(MainZap.DIRECTORY + "\\assets\\save\\set.sv");
	private static final String SPLIT_FLAG = ";";

	public static float loadScale() {

		float result = 1.0f;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE));
			String content = "";
			while (reader.ready()) {

				String s = reader.readLine();
				if (s == null)
					break;
				content += s;
			}
			reader.close();

			try {
				result = Float.parseFloat(content.split(SPLIT_FLAG)[0]);
			} catch (Exception e) {
				Cmd.err("Who modified the data store? Regenerating...");
				genFile();
				return loadScale();
			}

			return result;

		} catch (IOException e) {
			Cmd.err("Data store not available. Regenerating...");
			genFile();
			return loadScale();
		}

	}

	public static boolean loadSexyGraphics() {

		boolean result = true;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE));
			String content = "";
			while (reader.ready()) {

				String s = reader.readLine();
				if (s == null)
					break;
				content += s;
			}
			reader.close();

			try {
				result = Boolean.parseBoolean(content.split(SPLIT_FLAG)[1]);
			} catch (Exception e) {
				Cmd.err("Who modified the data store? Regenerating...");
				genFile();
				return loadSexyGraphics();
			}

			return result;

		} catch (IOException e) {
			Cmd.err("Data store not available. Regenerating...");
			genFile();
			return loadSexyGraphics();
		}
	}

	public static boolean loadSexyShips() {

		boolean result = true;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE));
			String content = "";
			while (reader.ready()) {

				String s = reader.readLine();
				if (s == null)
					break;
				content += s;
			}
			reader.close();

			try {
				result = Boolean.parseBoolean(content.split(SPLIT_FLAG)[2]);
			} catch (Exception e) {
				Cmd.err("Who modified the data store? Regenerating...");
				genFile();
				return loadSexyShips();
			}

			return result;

		} catch (IOException e) {
			Cmd.err("Data store not available. Regenerating...");
			genFile();
			return loadSexyShips();
		}

	}

	public static boolean loadSpeedmode() {

		boolean result = true;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE));
			String content = "";
			while (reader.ready()) {

				String s = reader.readLine();
				if (s == null)
					break;
				content += s;
			}
			reader.close();

			try {
				result = Boolean.parseBoolean(content.split(SPLIT_FLAG)[3]);
			} catch (Exception e) {
				Cmd.err("Who modified the data store? Regenerating...");
				genFile();
				return loadSpeedmode();
			}

			return result;

		} catch (IOException e) {
			Cmd.err("Data store not available. Regenerating...");
			genFile();
			return loadSpeedmode();
		}
	}

	public static boolean loadFancyGraphics() {

		boolean result = true;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE));
			String content = "";
			while (reader.ready()) {

				String s = reader.readLine();
				if (s == null)
					break;
				content += s;
			}
			reader.close();

			try {
				result = Boolean.parseBoolean(content.split(SPLIT_FLAG)[4]);
			} catch (Exception e) {
				Cmd.err("Who modified the data store? Regenerating...");
				genFile();
				return loadFancyGraphics();
			}

			return result;

		} catch (IOException e) {
			Cmd.err("Data store not available. Regenerating...");
			genFile();
			return loadFancyGraphics();
		}
	}
	
	public static boolean loadRoundCorners() {

		boolean result = true;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE));
			String content = "";
			while (reader.ready()) {

				String s = reader.readLine();
				if (s == null)
					break;
				content += s;
			}
			reader.close();

			try {
				result = Boolean.parseBoolean(content.split(SPLIT_FLAG)[5]);
			} catch (Exception e) {
				Cmd.err("Who modified the data store? Regenerating...");
				genFile();
				return loadRoundCorners();
			}

			return result;

		} catch (IOException e) {
			Cmd.err("Data store not available. Regenerating...");
			genFile();
			return loadRoundCorners();
		}
	}
	
	public static boolean loadFirstrun() {

		boolean result = true;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE));
			String content = "";
			while (reader.ready()) {

				String s = reader.readLine();
				if (s == null)
					break;
				content += s;
			}
			reader.close();

			try {
				result = Boolean.parseBoolean(content.split(SPLIT_FLAG)[6]);
			} catch (Exception e) {
				Cmd.err("Who modified the data store? Regenerating...");
				genFile();
				return loadSpeedmode();
			}

			return result;

		} catch (IOException e) {
			Cmd.err("Data store not available. Regenerating...");
			genFile();
			return loadSpeedmode();
		}
	}

	public static void save(float scale, boolean genAntialize, boolean shipAntialize, boolean speedMode, boolean fancyGraph,
			boolean roundCorners, boolean firstrun) {

		try {

			Formatter f = new Formatter(FILE);
			f.format(scale + SPLIT_FLAG + genAntialize + SPLIT_FLAG + shipAntialize + SPLIT_FLAG + speedMode + SPLIT_FLAG
					+ fancyGraph + SPLIT_FLAG + roundCorners + SPLIT_FLAG + firstrun);
			f.close();

		} catch (Exception e) {
			// juckt nicht
		}

	}

	public static void save() {
		try {

			Formatter f = new Formatter(FILE);
			f.format(MainZap.getScale() + SPLIT_FLAG + MainZap.generalAntialize + SPLIT_FLAG + MainZap.antializeShips
					+ SPLIT_FLAG + MainZap.speedMode + SPLIT_FLAG + MainZap.fancyGraphics + SPLIT_FLAG
					+ MainZap.roundCorners + SPLIT_FLAG + MainZap.firstRun);
			f.close();

		} catch (Exception e) {
			// juckt nicht
		}

	}

	private static void genFile() {
		try {

			Formatter f = new Formatter(FILE);
			// scale, generalAA, shipAA, speedMode, fancyEffekts, roundCorners
			f.format("1.0f" + SPLIT_FLAG + "true" + SPLIT_FLAG + "true" + SPLIT_FLAG + "false" + SPLIT_FLAG + "true"
					+ SPLIT_FLAG + "true" + SPLIT_FLAG + "true");
			f.close();

		} catch (Exception e) {
			// juckt nicht
		}
	}

}
