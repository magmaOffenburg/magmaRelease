/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.benchmark;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sgrossma
 *
 */
public class PathParameterWalkBenchmark
{
	/** filter circles more stable than this value */
	private static double riskFactor = 0.95;

	/** filter circles greater radius than this value */
	private static double ignoreSmallCircles = 0.15;

	private static double maxSpeedAllowed = 1.1;

	private static double maxSpeed = 0.841;

	private List<PathParameterWalkBenchmarkItem> benchmarkItems;

	public PathParameterWalkBenchmark()
	{
		benchmarkItems = new ArrayList<>();

		try {
			this.loadFromXML();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * loads XML benchmark file with given filename
	 * @param filename of the xml-file
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void loadFromXML(String filename) throws IOException
	{
		FileReader fileReader = null;
		try {
			// load our xml file
			fileReader = new FileReader(filename + ".xml");
			// init XStream
			XStream xstream = new XStream(new DomDriver());
			xstream.alias("benchmark", PathParameterWalkBenchmarkItem.class);
			benchmarkItems = (List<PathParameterWalkBenchmarkItem>) xstream.fromXML(fileReader);
		} finally {
			if (fileReader != null) {
				fileReader.close();
			}
		}
	}

	/**
	 * loads standard xml-file "benchmark"
	 * @throws IOException
	 */
	public void loadFromXML() throws IOException
	{
		this.loadFromXML("benchmark");
	}

	/**
	 * saves current benchmarkItems to xml-file
	 * @param filename in which items has to be saved
	 * @throws IOException
	 */
	public void saveToXML(String filename) throws IOException
	{
		FileWriter writer = null;
		try {
			writer = new FileWriter(filename + ".xml");
			XStream xstream = new XStream(new DomDriver());
			xstream.toXML(this.benchmarkItems, writer);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * saves to standard file "benchmark"
	 * @throws IOException
	 */
	public void saveToXML() throws IOException
	{
		saveToXML("benchmark");
	}

	/**
	 * add a given item to the benchmark item list
	 * @param item: that has to be added
	 */
	public void addBenchmarkItem(PathParameterWalkBenchmarkItem item)
	{
		boolean exists = false;
		for (PathParameterWalkBenchmarkItem items : benchmarkItems) {
			if (item.equals(items)) {
				exists = true;
				items.update(item);
			}
		}

		if (!exists)
			benchmarkItems.add(item);
	}

	public List<PathParameterWalkBenchmarkItem> getBenchmarkItems()
	{
		return benchmarkItems;
	}

	/**
	 * get speed about a defined range (between toleranceStart and toleranceEnd)
	 * the range is set over and under value example: for value 5.0, start 0.2,
	 * end 0.3 range1 = 5.2-5.3 range2 = 4.7-4.8
	 *
	 * @param toleranceStart: lower range value
	 * @param toleranceEnd: higher range value
	 * @return all items that matches to the speed ranges
	 */
	public List<PathParameterWalkBenchmarkItem> getSpeedAbout(double value, double toleranceStart, double toleranceEnd)
	{
		List<PathParameterWalkBenchmarkItem> filteredItems = new ArrayList<>();
		for (PathParameterWalkBenchmarkItem item : benchmarkItems) {
			if (item.getRadius() != 0 && item.getAngle().degrees() != 0 && item.getUtility() >= riskFactor &&
					item.getRadius() > ignoreSmallCircles) {
				if ((item.getAvgSpeed() < (value - toleranceStart)					// )
							&& (item.getAvgSpeed() > (value - toleranceEnd)))		// )
						|| ((item.getAvgSpeed() > value + toleranceStart)			// )
								   && (item.getAvgSpeed() < value + toleranceEnd))) // )
				{
					filteredItems.add(item);
				}
			}
		}
		return filteredItems;
	}

	/**
	 * gets all circles after base filtering
	 * @return filtered benchmark items
	 */
	public List<PathParameterWalkBenchmarkItem> getCirclesAll()
	{
		List<PathParameterWalkBenchmarkItem> filteredItems = new ArrayList<>();
		for (PathParameterWalkBenchmarkItem item : benchmarkItems) {
			if (baseFilter(item)) {
				filteredItems.add(item);
			}
		}
		return filteredItems;
	}

	/**
	 * gets all circles after base filtering and faster than parameter
	 * @param startSpeed: all circles faster than start speed
	 * @return filtered benchmark items
	 */
	public List<PathParameterWalkBenchmarkItem> getCirclesFaster(double startSpeed)
	{
		if (startSpeed > maxSpeed)
			startSpeed = maxSpeed;

		List<PathParameterWalkBenchmarkItem> filteredItems = new ArrayList<>();
		for (PathParameterWalkBenchmarkItem item : benchmarkItems) {
			if (baseFilter(item)) {
				if (item.getAvgSpeed() > (startSpeed - 0.1)) {
					filteredItems.add(item);
				}
			}
		}
		return filteredItems;
	}

	/**
	 * gets all circles after base filtering and slower than parameter
	 * @param startSpeed: all circles slower than start speed
	 * @return filtered benchmark items
	 */
	public List<PathParameterWalkBenchmarkItem> getCirclesSlower(double startSpeed)
	{
		if (startSpeed > maxSpeed)
			startSpeed = maxSpeed;

		List<PathParameterWalkBenchmarkItem> filteredItems = new ArrayList<>();
		for (PathParameterWalkBenchmarkItem item : benchmarkItems) {
			if (baseFilter(item)) {
				if (item.getAvgSpeed() < (startSpeed)) {
					filteredItems.add(item);
				}
			}
		}
		return filteredItems;
	}

	/**
	 * checks if given item is filtered by base filter definition
	 * @param item: item for check
	 * @return true if item passes base filter, else false
	 */
	private boolean baseFilter(PathParameterWalkBenchmarkItem item)
	{
		if (item != null) {
			return item.getRadius() != 0 && item.getAngle().degrees() != 0 && item.getUtility() >= riskFactor &&
					item.getRadius() > ignoreSmallCircles && item.getAvgSpeed() < maxSpeedAllowed;
		}
		return false;
	}
}
