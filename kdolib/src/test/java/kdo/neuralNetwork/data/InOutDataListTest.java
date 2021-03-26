/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InOutDataListTest
{
	private InOutDataList testee;

	@BeforeEach
	public void setup()
	{
		InOutPattern[] patterns = {//
				new InOutPattern(new float[] {0f}, new float[] {0.0f}),
				new InOutPattern(new float[] {1f}, new float[] {0.1f}),
				new InOutPattern(new float[] {2f}, new float[] {0.1f}),
				new InOutPattern(new float[] {3f}, new float[] {0.1f}),
				new InOutPattern(new float[] {4f}, new float[] {0.1f})};

		testee = new InOutDataList(patterns);
	}

	@Test
	public void testIteratorBatch1()
	{
		Iterator<List<InOutPattern>> iterator = testee.iterator(1);
		int i = 0;
		while (iterator.hasNext()) {
			List<InOutPattern> pattern = iterator.next();
			assertEquals(1, pattern.size());
			assertEquals(i, pattern.get(0).getInput()[0], 0.0001);
			i++;
		}
		assertEquals(5, i);
	}

	@Test
	public void testIteratorBatch2()
	{
		Iterator<List<InOutPattern>> iterator = testee.iterator(2);
		int i = 0;
		while (iterator.hasNext()) {
			List<InOutPattern> pattern = iterator.next();
			assertEquals((i < 2) ? 2 : 1, pattern.size());
			assertEquals(2 * i, pattern.get(0).getInput()[0], 0.0001);
			i++;
		}
	}
}
