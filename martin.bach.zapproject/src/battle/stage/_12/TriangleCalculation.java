package battle.stage._12;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import battle.WeaponPositioning;

public abstract class TriangleCalculation {

	public static float getTotalHeight(int borderlen) {
		return (float) ((borderlen / 2.0f) * Math.tan(Math.PI / 3.0)); // tan im Zähler
	}

	/**
	 * Gibt die Umrandung eines Dreiecks. 0|0 ist Zentrum
	 * 
	 * @param borderlen
	 * @return
	 */
	public static Point2D[] getTriangleOutline(int borderlen) {

		float totalHeight = (float) ((borderlen / 2.0f) * Math.tan(Math.PI / 3.0)); // tan im Zähler
		float heightToMid = (float) (borderlen / (2.0f * Math.tan(Math.PI / 3.0))); // tan im Nenner
		float heightFromMidToTop = totalHeight - heightToMid;
		return new Point2D[] { new Point(0, -(int) heightFromMidToTop),
				new Point((int) (borderlen / 2), (int) heightToMid),
				new Point(-(int) (borderlen / 2.0f), (int) heightToMid) };
	}



	/**
	 * Gibt Rotierte Punkte für die Positionen von Split-Resultaten im Format (x0,
	 * y0, x1, y1, ...), es sind insgesamt 4 Punkte
	 * 
	 * @param oldBorderLen
	 * @param roration
	 * @return
	 */
	public static float[] getSplitPositionSet(int baseBorderlen, float baseHeightToMid, double rotation) {

		float heightToMid = (float) (baseBorderlen / (4.0f * Math.tan(Math.PI / 3.0))); // tan im Nenner

		double[] pos0 = WeaponPositioning.rotate(rotation, new Point(0, (int) -(2.0f * heightToMid)));
		double[] pos1 = WeaponPositioning.rotate(rotation,
				new Point(-(int) (baseBorderlen / 4), (int) (baseHeightToMid - heightToMid)));
		double[] pos2 = WeaponPositioning.rotate(rotation, new Point(0, 0));
		double[] pos3 = WeaponPositioning.rotate(rotation,
				new Point((int) (baseBorderlen / 4), (int) (baseHeightToMid - heightToMid)));

		return new float[] { (float) pos0[0], (float) pos0[1], (float) pos1[0], (float) pos1[1], (float) pos2[0],
				(float) pos2[1], (float) pos3[0], (float) pos3[1] };
	}

	/**
	 * Gibt Punkte für die Positionen von Split-Resultaten im Format (x0, y0, x1,
	 * y1, ...), es sind insgesamt 4 Punkte (von Spitze zu unten links zu mitte zu
	 * unten rechts)
	 * 
	 * @param oldBorderLen
	 * @return (x0, y0, x1, y1, ...)
	 */
	public static float[] getSplitPositionSet(int baseBorderlen) {

		float heightToMid = (float) (baseBorderlen / (4.0f * Math.tan(Math.PI / 3.0))); // tan im Nenner
		return new float[] { 0, -(2.0f * heightToMid), -(baseBorderlen / 4), heightToMid, 0, 0,
				(baseBorderlen / 4),  heightToMid };
	}

	/**
	 * Gibt die Umrandung eines Dreiecks. 0|0 ist Zentrum Die existenz des
	 * Mittelstücks juckt nicht.
	 * 
	 * @param borderlen
	 * @return
	 */
	public static Point2D[] getTriangleOutline(int borderlen, boolean[] availableParts) {

		float totalHeight = (float) ((borderlen / 2.0f) * Math.tan(Math.PI / 3.0)); // tan im Zähler
		float heightToMid = (float) (borderlen / (2.0f * Math.tan(Math.PI / 3.0))); // tan im Nenner
		float heightFromMidToTop = totalHeight - heightToMid;

		ArrayList<Point> points = new ArrayList<Point>();

		if (availableParts[0] && false)
			points.add(new Point(0, -(int) heightFromMidToTop));
		points.add(new Point((int) (totalHeight / 4), 0));
		if (availableParts[3] && false)
			points.add(new Point((int) (borderlen / 2), (int) heightToMid));
		points.add(new Point(0, (int) heightToMid));
		if (availableParts[1] && false)
			points.add(new Point((int) -(borderlen / 2), (int) heightToMid));
		points.add(new Point((int) -(totalHeight / 4), 0));

		Point2D[] res = new Point2D[points.size()];
		for (int i = 0; i != points.size(); i++)
			res[i] = points.get(i);

		return res;
	}

}
