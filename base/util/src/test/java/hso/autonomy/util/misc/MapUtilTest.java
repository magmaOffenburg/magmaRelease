/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hso.autonomy.util.misc.MapUtil.Sorting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class MapUtilTest
{
	private List<Map<String, Double>> createMaps()
	{
		Map<String, Double> map1 = new HashMap<>();
		map1.put("a", 15.0);
		map1.put("b", 5.0);

		Map<String, Double> map2 = new HashMap<>();
		map2.put("a", 3.0);

		List<Map<String, Double>> maps = new ArrayList<>();
		maps.add(map1);
		maps.add(map2);
		return maps;
	}

	@Test
	public void testSumMaps()
	{
		Map<String, Double> result = new MapUtil(Sorting.NONE).sum(createMaps());

		assertEquals(result.get("a"), 18.0, 0.0001);
		assertEquals(result.get("b"), 5.0, 0.0001);
	}

	@Test
	public void testDivideMap()
	{
		Map<String, Double> map = new HashMap<>();
		map.put("a", 3.0);
		map.put("b", 5.0);

		int divisor = 3;
		Map<String, Double> result = new MapUtil(Sorting.NONE).divide(map, divisor);

		assertEquals(result.get("a"), 3.0 / divisor, 0.0001);
		assertEquals(result.get("b"), 5.0 / divisor, 0.0001);
	}

	@Test
	public void testAverageMap()
	{
		Map<String, Double> result = new MapUtil(Sorting.NONE).average(createMaps());

		assertEquals(result.get("a"), 9, 0.0001);
		assertEquals(result.get("b"), 2.5, 0.0001);
	}
}
