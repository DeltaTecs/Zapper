package gui.shop;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

// Die Vor-Kalkulation f�r f�rbung der Schiffsbeschreibung (Weil Zeilenumbr�che)
// Ben�tigt Initialisierung bei erstem Paint-Vorgang
public class PaintableDescription {

	public static final int LINE_SPACE = 1; // Zeilenabstand in px.

	private String[] lines = null;
	private boolean initilized = false;
	private String content;
	private int maxLineLength;

	/*
	 * Keine Mega-Langen W�rter erlaubt. Es wird nach Leerzeichen umgebrochen
	 * Soll ein Zeilenumbruch vermieden werden aber trozdem ein Leerzeichen
	 * gesetzt werden, dann statt" "einfach"~"verwenden.
	 */
	public PaintableDescription(String desc, int maxLineLength) {
		content = desc;
		this.maxLineLength = maxLineLength;
	}

	public void init(FontMetrics fm) {

		String[] words = content.split(" ");
		// "~" nachtr�glich durch Leerzeichen ersetzen
		for (int i = 0; i != words.length; i++) {
			words[i] = words[i].replaceAll("~", " ");
		}

		// Sonderf�lle
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
				// Maximale L�nge �bertreten, neue Zeile anfangen
				storeLines.add(currentLine);
				currentLine = word;
				currentLineLength = wordLen;
			} else {
				// Noch Platz
				currentLineLength += wordLen;
				currentLine += word;
			}
		} // Ende: FOR

		// Letzte Zeile hinzuf�gen
		storeLines.add(currentLine);

		// Vorgang beendet. Liste erstellen
		lines = storeLines.toArray(new String[] {});
		initilized = true;
	}

	// F�rbt im Graphischen Kontext
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
