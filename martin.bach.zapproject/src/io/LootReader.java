package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Formatter;

import corecase.MainZap;
import gui.shop.Shop;

public class LootReader {

	private static final File FILE = new File(MainZap.DIRECTORY + "\\assets\\save\\lt.sv");
	private static final String FLAG_SPLIT = "BOI";

	public static void load() {

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

			String[] split = content.split(FLAG_SPLIT);

			if (split.length != Shop.unlocked.length)
				throw new Exception();

			for (int i = 0; i != split.length; i++) {
				boolean b = Boolean.parseBoolean(split[i]);
				Shop.unlocked[i] = b;
			}

		} catch (Exception e) {
			System.err.println("[Err] Loot-Speicher Datei nicht gefunden oder anderer Fehler. Generiere neu");
			genFile();
			load();
		}

	}

	public static void save() {

		String out = "";
		for (int i = 0; i != Shop.unlocked.length; i++) {
			out += "" + Shop.unlocked[i];
			if (i != Shop.unlocked.length - 1)
				out += FLAG_SPLIT;
		}

		try {

			Formatter f = new Formatter(FILE);
			f.format(out);
			f.close();

		} catch (Exception e) {
			// juckt nicht
		}

	}

	private static void genFile() {
		try {

			Formatter f = new Formatter(FILE);
			String out = "";
			for (int i = 0; i != Shop.unlocked.length; i++) {
				out += "false";
				if (i != Shop.unlocked.length - 1)
					out += FLAG_SPLIT;
			}
			f.format(out);
			f.close();

		} catch (Exception e) {
			// juckt nicht
		}
	}
}
