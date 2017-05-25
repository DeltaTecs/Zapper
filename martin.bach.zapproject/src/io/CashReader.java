package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;

import corecase.Cmd;
import corecase.MainZap;

public class CashReader {

	private static final File FILE = new File(MainZap.DIRECTORY + "\\assets\\save\\lst.sv");
	private static final long UPER_LIMIT = 263856394 * 9635 * 99247;
	private static final int CASH_FAC = 23;

	public static int load() {

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
				return (int) ((UPER_LIMIT - Long.parseLong(content)) / CASH_FAC);
			} catch (NumberFormatException e) {
				genFile();
				Cmd.err("WHO IS CHEATING OVER HERE!??");
			}

			return 0;

		} catch (IOException e) {
			System.err.println("[Err] Crystal-Speicher Datei nicht gefunden oder anderer Fehler. Generiere neu");
			genFile();
			return load();
		}

	}

	public static void save(int amount) {

		String out = "" + (UPER_LIMIT - (amount * CASH_FAC));

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
			f.format("" + UPER_LIMIT);
			f.close();

		} catch (Exception e) {
			// juckt nicht
		}
	}
}
