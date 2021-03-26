/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Klaus Dorer Tests for Goal class
 */
public class GoalTest extends EBNTestBase
{
	@Override
	@BeforeEach
	public void setUp()
	{
		super.setUp();
	}

	@Test
	public void testRelevance()
	{
		Condition condition = new Condition(new Proposition(goalTrue1, perceptionTrue1, false));
		goalTrue1.setRelevanceCondition(condition);
		assertEquals(1.0, goalTrue1.getRelevance(), 0.0001);
	}
}
