package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;

import corecase.MainZap;
import gui.screens.end.ScoreEntry;

public class ScoreReader {

	private static final File FILE = new File(MainZap.DIRECTORY + "\\assets\\save\\scr.sv");
	private static final String SPLIT_POINTER_ENTRY = ">";
	private static final String SPLIT_POINTER_VALUE = "<";

	public static ArrayList<ScoreEntry> load() {

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

			String[] entrys = content.split(SPLIT_POINTER_ENTRY);
			String[][] values = new String[entrys.length][2];
			for (int i = 0; i != entrys.length; i++) {
				if (values[i].length == 0)
					continue; // Leerer Wert
				values[i] = entrys[i].split(SPLIT_POINTER_VALUE);
			}

			// Finalisieren
			ArrayList<ScoreEntry> result = new ArrayList<ScoreEntry>();

			for (String[] v : values) {
				if (v.length == 0)
					continue; // Leerer Wert
				result.add(new ScoreEntry(v[0], Integer.parseInt(v[1])));
			}

			return result;

		} catch (IOException e) {
			System.err.println("[Err] Score-Speicher Datei nicht gefunden oder anderer Fehler. Generiere neu");
			genFile();
			return load();
		}

	}

	public static void save(ArrayList<ScoreEntry> entrys) {

		String out = "";
		for (ScoreEntry e : entrys) {
			out += e.getName() + SPLIT_POINTER_VALUE + e.getScore() + SPLIT_POINTER_ENTRY;
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
			f.format("Arthur Dent<42");
			f.close();

		} catch (Exception e) {
			// juckt nicht
		}
	}

}
