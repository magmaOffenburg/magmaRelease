/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.thoughtmodel.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConsecutiveTruthValueTest
{
	private ConsecutiveTruthValue testee;

	@BeforeEach
	public void setUp() throws Exception
	{
		testee = new WrapperCTV(1, 3);
	}

	@Test
	public void testIsValid()
	{
		float time = 0.0f;
		assertFalse(testee.isValid());
		testee.setValidity(true, time++);
		assertTrue(testee.isValid(), "Did not believe first perception");

		testee.setValidity(false, time++);
		testee.setValidity(false, time++);
		assertTrue(testee.isValid());
		testee.setValidity(false, time++);
		assertFalse(testee.isValid());

		testee.setValidity(true, time++);
		assertTrue(testee.isValid());

		testee.setValidity(false, time++);
		testee.setValidity(true, time++);
		testee.setValidity(false, time++);
		testee.setValidity(false, time++);
		assertTrue(testee.isValid());
		testee.setValidity(false, time++);
		assertFalse(testee.isValid());
	}

	// just needed to create an instance
	class WrapperCTV extends ConsecutiveTruthValue
	{
		public WrapperCTV(int trueCount, int falseCount)
		{
			super(trueCount, falseCount);
		}

		@Override
		public void update(IThoughtModel thoughtModel)
		{
		}
	}
}
