/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapUtil
{
	public enum Sorting { NONE, KEYS, VALUES }

	private final Sorting sorting;

	public MapUtil(Sorting sorting)
	{
		this.sorting = sorting;
	}

	private List<Map<String, Double>> toMapList(Map<String, Double> map1, Map<String, Double> map2)
	{
		List<Map<String, Double>> mapsToSum = new ArrayList<>();
		mapsToSum.add(map1);
		mapsToSum.add(map2);
		return mapsToSum;
	}

	public Map<String, Double> sum(Map<String, Double> map1, Map<String, Double> map2)
	{
		return sum(toMapList(map1, map2));
	}

	public Map<String, Double> sum(List<Map<String, Double>> maps)
	{
		Map<String, Double> result = new HashMap<>();
		for (Map<String, Double> map : maps) {
			for (String key : map.keySet()) {
				Double value = map.get(key);
				if (result.containsKey(key)) {
					result.put(key, result.get(key) + value);
				} else {
					result.put(key, value);
				}
			}
		}
		return sort(result);
	}

	Map<String, Double> divide(Map<String, Double> map, double divisor)
	{
		Map<String, Double> result = new HashMap<>();
		for (String key : map.keySet()) {
			result.put(key, map.get(key) / divisor);
		}
		return sort(result);
	}

	public Map<String, Double> average(Map<String, Double> map1, Map<String, Double> map2)
	{
		return average(toMapList(map1, map2));
	}

	public Map<String, Double> average(List<Map<String, Double>> maps)
	{
		return divide(sum(maps), maps.size());
	}

	public <T extends Comparable<T>> Map<String, T> sort(Map<String, T> map)
	{
		switch (sorting) {
		case KEYS:
			return sortByKeys(map);
		case VALUES:
			return sortByValues(map);
		default:
			return map;
		}
	}

	private static <T> Map<String, T> sortByKeys(Map<String, T> map)
	{
		List<String> sortedKeys = map.keySet().stream().sorted().collect(Collectors.toList());
		return createLinkedHashMap(map, sortedKeys);
	}

	private static <T extends Comparable<T>> Map<String, T> sortByValues(Map<String, T> map)
	{
		List<String> sortedKeys = map.keySet().stream().collect(Collectors.toList());
		sortedKeys.sort((o1, o2) -> map.get(o2).compareTo(map.get(o1)));
		return createLinkedHashMap(map, sortedKeys);
	}

	private static <T> Map<String, T> createLinkedHashMap(Map<String, T> map, List<String> sortedKeys)
	{
		Map<String, T> sortedMap = new LinkedHashMap<>();
		for (String key : sortedKeys) {
			sortedMap.put(key, map.get(key));
		}
		return sortedMap;
	}
}
