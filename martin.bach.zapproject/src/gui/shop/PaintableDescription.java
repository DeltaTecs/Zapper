package gui.shop;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

// Die Vor-Kalkulation für färbung der Schiffsbeschreibung (Weil Zeilenumbrüche)
// Benötigt Initialisierung bei erstem Paint-Vorgang
public class PaintableDescription {

	public static final int LINE_SPACE = 1; // Zeilenabstand in px.

	private String[] lines = null;
	private boolean initilized = false;
	private String content;
	private int maxLineLength;

	/*
	 * Keine Mega-Langen Wörter erlaubt. Es wird nach Leerzeichen umgebrochen
	 * Soll ein Zeilenumbruch vermieden werden aber trozdem ein Leerzeichen
	 * gesetzt werden, dann statt" "einfach"~"verwenden.
	 */
	public PaintableDescription(String desc, int maxLineLength) {
		content = desc;
		this.maxLineLength = maxLineLength;
	}

	public void init(FontMetrics fm) {

		String[] words = content.split(" ");
		// "~" nachträglich durch Leerzeichen ersetzen
		for (int i = 0; i != words.length; i++) {
			words[i] = words[i].replaceAll("~", " ");
		}

		// Sonderfälle
		if (words.length == 0) {
			lines = new String[] { "" };
			return;
		} else if (words.length == 1) {
			lines = new String[] { words[0] };
			return;
		}

		ArrayList<String> storeLines = new ArrayList<String>();
		String currentLine = "";
		int currentLineLength = 0;

		for (String word : words) {

			word += " "; // Leerzeichen wurden vorher weggeschnitten
			int wordLen = fm.stringWidth(word);

			if (currentLineLength + wordLen > maxLineLength) {
				// Maximale Länge übertreten, neue Zeile anfangen
				storeLines.add(currentLine);
				currentLine = word;
				currentLineLength = wordLen;
			} else {
				// Noch Platz
				currentLineLength += wordLen;
				currentLine += word;
			}
		} // Ende: FOR

		// Letzte Zeile hinzufügen
		storeLines.add(currentLine);

		// Vorgang beendet. Liste erstellen
		lines = storeLines.toArray(new String[] {});
		initilized = true;
	}

	// Färbt im Graphischen Kontext
	public void paint(Graphics2D g, int x, int y) {

		if (!initilized) // Schon initialisiert?
			init(g.getFontMetrics());

		// Kontext durch Koordinaten gegeben

		final int fontHeight = g.getFont().getSize();

		for (String line : lines) {
			g.drawString(line, x, y);
			y += fontHeight + LINE_SPACE;
		}
	}

}
