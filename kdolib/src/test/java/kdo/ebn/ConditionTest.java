/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Klaus Dorer
 *
 */
public class ConditionTest extends EBNTestBase
{
	private Condition testee;

	@Override
	@BeforeEach
	public void setUp()
	{
		super.setUp();
		testee = new Condition(new Proposition(null, perceptionTrue1, false));
	}

	@Test
	public void testGetTruthValueSingleProposition()
	{
		assertEquals(1.0, testee.getTruthValue(), 0.0001);
	}

	@Test
	public void testGetTruthValueTwoPropositions()
	{
		testee.addProposition(new Proposition(null, perceptionFalse1, false));
		assertEquals(0.0, testee.getTruthValue(), 0.0001);
	}
}
