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
public class ResourceTest extends EBNTestBase
{
	@Override
	@BeforeEach
	public void setUp()
	{
		super.setUp();
	}

	/**
	 * Test method for
	 * {@link magma.agent.decision.ebn.Resource#reduceActivationLevel(double)}.
	 */
	@Test
	public void testReduceActivationLevel()
	{
		resource1.isActivityLowerThanThreshold(2.1);
		resource1.reduceActivationLevel(0.6);
		assertEquals(0.4, resource1.getActivation(), 0.001);
	}

	/**
	 * Test method for
	 * {@link magma.agent.decision.ebn.Resource#reduceActivationLevel(double)}.
	 */
	@Test
	public void testReduceActivationLevelBottom()
	{
		resource1.isActivityLowerThanThreshold(2.1);
		resource1.reduceActivationLevel(1.6);
		assertEquals(0.00001, resource1.getActivation(), 0.0000001);
	}
}
