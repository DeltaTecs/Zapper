package corecase;

public abstract class StringConverter {

	private static final String[] ROMAN_NUMBERS = new String[] { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX",
			"X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI", "XXII", "XXIII", "XXIV",
			"XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX", "XXXI", "XXXII", "XXXXIII", "XXXIV", "XXXV", "XXXVI",
			"XXXVII", "XXXVIII", "XXXIX", "XL", "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII", "IL",
			"L" };

	public static String inRoman(int i) {

		if (i > 50) {
			return "Fuck of";
		}

		if (i == 0) {
			return "0";
		}

		if (i < 0) {
			return "-x";
		}

		return ROMAN_NUMBERS[i - 1];
	}

	public static boolean isClassic(char c) {
		return c == 'a' || c == 'A' || c == 'b' || c == 'B' || c == 'c' || c == 'C' || c == 'd' || c == 'D' || c == 'e'
				|| c == 'E' || c == 'f' || c == 'F' || c == 'g' || c == 'G' || c == 'h' || c == 'H' || c == 'i'
				|| c == 'I' || c == 'j' || c == 'J' || c == 'k' || c == 'K' || c == 'l' || c == 'L' || c == 'm'
				|| c == 'M' || c == 'n' || c == 'N' || c == 'o' || c == 'O' || c == 'p' || c == 'P' || c == 'q'
				|| c == 'Q' || c == 'r' || c == 'R' || c == 's' || c == 'S' || c == 't' || c == 'T' || c == 'u'
				|| c == 'U' || c == 'v' || c == 'V' || c == 'w' || c == 'W' || c == 'x' || c == 'X' || c == 'y'
				|| c == 'Y' || c == 'z' || c == 'Z' || c == '0' || c == '1' || c == '2' || c == '3' || c == '4'
				|| c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '!' || c == '?' || c == '('
				|| c == ')' || c == '"' || c == '-' || c == '.' || c == ',' || c == ';' || c == '_' || c == 'ä'
				|| c == 'ö' || c == 'ü' || c == ' ';
	}

}
