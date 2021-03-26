/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.symboltreeparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Simon Raffeiner
 */
public class SymbolTreeParserTest
{
	private SymbolTreeParser testee = null;

	@BeforeEach
	public void setUp()
	{
		testee = new SymbolTreeParser();
	}

	/**
	 * Test null input
	 */
	@Test
	public void testNullInput()
	{
		try {
			testee.parse(null);
			fail("Exception expected");
		} catch (IllegalSymbolInputException e) {
		}
	}

	/**
	 * Test empty input
	 */
	@Test
	public void testEmptyInput()
	{
		try {
			testee.parse("");
			fail("Exception expected");
		} catch (IllegalSymbolInputException e) {
		}
	}

	/**
	 * Test a basic on-level string
	 */
	@Test
	public void testSimpleCase()
	{
		SymbolNode list = testee.parse("(A A)");

		assertEquals(list.toString(), "(A A)");
	}

	/**
	 * Test a two-level string
	 */
	@Test
	public void testTwoLevelCase()
	{
		SymbolNode list = testee.parse("(A (B B))");

		assertEquals(list.toString(), "(A (B B))");

		list = testee.parse("(time (now 2.h18))");

		assertEquals(list.toString(), "(time (now 2.h18))");
	}

	/**
	 * Test a three-level string
	 */
	@Test
	public void testThreeLevelCase()
	{
		SymbolNode list = testee.parse("(A (B B (C C C)))");

		assertEquals(list.toString(), "(A (B B (C C C)))");
	}

	/**
	 * Test a string with mixed levels
	 */
	@Test
	public void testMixedLevelCases()
	{
		SymbolNode list = testee.parse("(A (B B (C (D) C) B) A A)");

		assertEquals("(A (B B (C (D) C) B) A A)", list.toString());
		list = testee.parse("(A (B B) (C C C))");

		assertEquals("(A (B B) (C C C))", list.toString());
	}

	/**
	 * Test corner cases (should throw exceptions)
	 */
	@Test
	public void testCornerCase1()
	{
		try {
			testee.parse("(A A");
			fail("Exception expected");
		} catch (IllegalSymbolInputException e) {
		}
	}

	/**
	 * Test corner cases (should throw exceptions)
	 */
	@Test
	public void testCornerCase2()
	{
		try {
			testee.parse("(A (B B)");
			fail("Exception expected");
		} catch (IllegalSymbolInputException e) {
		}
	}

	/**
	 * Test corner cases (should throw exceptions)
	 */
	@Test
	public void testCornerCase3()
	{
		try {
			testee.parse("(A (B B) (C C C)");
			fail("Exception expected");
		} catch (IllegalSymbolInputException e) {
		}
	}

	/**
	 * Test a complete SimSpark-like String
	 */
	@Test
	public void testSimSparkComplete()
	{
		SymbolNode list = testee.parse(
				"(time (now 51.82))(GS (t 0.00) (pm BeforeKickOff))(GYR (n torso) (rt 0.00 -0.00 0.00))(HJ (n hj1) (ax -0.00))(HJ (n hj2) (ax -0.00))(HJ (n raj1) (ax -0.00))(HJ (n raj2) (ax -0.00))(HJ (n raj3) (ax -0.00))(HJ (n raj4) (ax -0.00))(HJ (n laj1) (ax -0.00))(HJ (n laj2) (ax -0.00))(HJ (n laj3) (ax -0.00))(HJ (n laj4) (ax -0.00))(HJ (n rlj1) (ax -0.00))(HJ (n rlj2) (ax -0.00))(HJ (n rlj3) (ax -0.00))(HJ (n rlj4) (ax -0.00))(HJ (n rlj5) (ax -0.00))(HJ (n rlj6) (ax -0.00))(HJ (n llj1) (ax -0.00))(HJ (n llj2) (ax -0.00))(HJ (n llj3) (ax -0.00))(HJ (n llj4) (ax -0.00))(HJ (n llj5) (ax -0.00))(HJ (n llj6) (ax -0.00))");

		assertTrue(list.children.get(0) instanceof SymbolNode);

		SymbolNode timeNode = (SymbolNode) list.children.get(0);
		SymbolNode timeSubNode = (SymbolNode) timeNode.children.get(1);
		assertEquals(timeNode.children.get(0), "time");
		assertEquals(timeSubNode.children.get(0), "now");
		assertEquals(timeSubNode.children.get(1), "51.82");

		SymbolNode gameStateNode = (SymbolNode) list.children.get(1);
		SymbolNode gameStateSubNode = (SymbolNode) gameStateNode.children.get(1);
		assertEquals(gameStateNode.children.get(0), "GS");
		assertEquals(gameStateSubNode.children.get(0), "t");
		assertEquals(gameStateSubNode.children.get(1), "0.00");
	}
}
