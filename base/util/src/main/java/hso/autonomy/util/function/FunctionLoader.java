/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Factory for creating motor file functions. <br>
 * <br>
 * A function can be stored as a line in a file, in the following format:<br>
 * <code>&lt;joint-name&gt;,&lt;function-type&gt;,&lt;support-point-count&gt;,
 * &lt;phase&gt;,[&lt;support- point&gt;]{ 2...* }</code><br>
 * where:<br>
 * <code>&lt;joint-name&gt;</code>: String: Name of the joint<br>
 * <code>&lt;function-type&gt;</code>: String: Name of the function (sine,
 * linear, bezier, spline)<br>
 * <code>&lt;function-stiffness&gt;</code>: Float from 0 to 1: Stiffness of the
 * function<br>
 * <code>&lt;support-point&gt;</code>: 2 or 6 in brackets encapsulated, space
 * separated float values<br>
 *
 * @author Stefan Glaser
 */
public class FunctionLoader
{
	public static final FunctionLoader INSTANCE = new FunctionLoader();

	private static final String[] KNOWN_FUNCTIONS = new String[] {SinFunction.NAME, PiecewiseLinearFunction.NAME,
			PiecewiseBezierFunction.NAME, SplineFunction.NAME, PiecewiseSineSquare.NAME};

	public String[] getKnownFunctionTypes()
	{
		return KNOWN_FUNCTIONS;
	}

	public String toCSVString(IFunction function)
	{
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("##0.0#", symbols);
		StringBuilder result = new StringBuilder();

		// Append Name
		result.append(function.getName());

		// Append Stiffness (not editable from the Function Editor yet)
		result.append(",").append(function.getStiffness());

		// Append SupportPoints
		for (int i = 0; i < function.getSupportPointSize(); i++) {
			ISupportPoint point = function.getSupportPoint(i);

			boolean addTangentPoints = point.getTangentPointAfter() != null && point.getTangentPointBefore() != null;

			result.append(",(");
			if (addTangentPoints) {
				result.append(df.format(point.getTangentPointBefore().getX()));
				result.append(" ");
				result.append(df.format(point.getTangentPointBefore().getY()));
				result.append(" ");
			}

			result.append(df.format(point.getX()));
			result.append(" ").append(df.format(point.getY()));

			if (addTangentPoints) {
				result.append(" ");
				result.append(df.format(point.getTangentPointAfter().getX()));
				result.append(" ");
				result.append(df.format(point.getTangentPointAfter().getY()));
			}
			result.append(")");
		}

		return result.toString();
	}

	public IFunction fromCSVString(String csvLine)
	{
		String[] csvContents = csvLine.split(",");

		String functionName = csvContents[0];
		float stiffness = Float.parseFloat(csvContents[1]);

		ArrayList<SupportPoint> supportPoints = parseSupportPoints(csvContents);

		return createFunction(functionName, supportPoints, stiffness);
	}

	protected ArrayList<SupportPoint> parseSupportPoints(String[] csvLine)
	{
		ArrayList<SupportPoint> supportPoints = new ArrayList<>();
		int startIndex = 2;
		int minSize = 2;

		if (csvLine.length < startIndex + minSize) {
			throw new IllegalArgumentException(
					"Expected at least " + (startIndex + minSize) + " values in " + Arrays.toString(csvLine));
		}

		for (int i = startIndex; i < csvLine.length; i++) {
			if (!csvLine[i].startsWith("(") || !csvLine[i].endsWith(")")) {
				throw new MalformatedSupportPointException("Expected brackets! value: " + csvLine[i]);
			}

			String[] supportPointValues = csvLine[i].substring(1, csvLine[i].length() - 1).split(" ");

			if (supportPointValues.length == 2) {
				float x = Float.parseFloat(supportPointValues[0]);
				float y = Float.parseFloat(supportPointValues[1]);
				supportPoints.add(new SupportPoint(x, y));
			} else if (supportPointValues.length == 6) {
				float xBefore = Float.parseFloat(supportPointValues[0]);
				float yBefore = Float.parseFloat(supportPointValues[1]);
				float x = Float.parseFloat(supportPointValues[2]);
				float y = Float.parseFloat(supportPointValues[3]);
				float xAfter = Float.parseFloat(supportPointValues[4]);
				float yAfter = Float.parseFloat(supportPointValues[5]);
				supportPoints.add(new SupportPoint(xBefore, yBefore, x, y, xAfter, yAfter));
			} else {
				throw new MalformatedSupportPointException(
						"Expected 2 or 6 values but got: " + supportPointValues.length + " value: " + csvLine[i]);
			}
		}

		return supportPoints;
	}

	/**
	 * @return the function defined by the csv-line
	 */
	public IFunction createFunction(String name, ArrayList<SupportPoint> supportPoints, float stiffness)
	{
		switch (name) {
		case SinFunction.NAME:
			return new SinFunction(supportPoints, stiffness);
		case PiecewiseSineSquare.NAME:
			return new PiecewiseSineSquare(supportPoints, stiffness);
		case SplineFunction.NAME:
			return new SplineFunction(supportPoints, stiffness);
		case PiecewiseBezierFunction.NAME:
			return new PiecewiseBezierFunction(supportPoints, stiffness);
		case PiecewiseLinearFunction.NAME:
			return new PiecewiseLinearFunction(supportPoints, stiffness);
		default:
			throw new IllegalArgumentException("Unknown function " + name + "!");
		}
	}
}
