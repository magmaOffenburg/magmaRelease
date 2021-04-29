/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import kdo.domain.IIndividuum;
import kdo.domain.IOperator;
import kdo.domain.IProblemState;
import kdo.search.strategy.ILocalSearchStrategy;
import kdo.search.strategy.base.StrategyBase;

/**
 * Runs through the whole search space
 * @author dorer
 */
public class FullSearch extends StrategyBase implements ILocalSearchStrategy
{
	public FullSearch()
	{
		super("FullSearch");
	}

	@Override
	public IProblemState search(IProblemState initialState)
	{
		PrintStream[] out = null;
		int maxPropertiesWritten = 9;

		try {
			String headerLine = createHeaderLine(initialState);

			// now make the measurement
			IProblemState currentState = initialState;
			Iterator<IOperator> operatorIterator = currentState.fullSearchIterator();
			boolean firstTime = true;

			while (operatorIterator.hasNext()) {
				IOperator next = operatorIterator.next();
				IProblemState nextState = next.getSuccessorState(currentState);
				double utility = nextState.calculateUtility();
				double[] runtimeProperties = nextState.getRuntimeProperties();

				if (runtimeProperties == null) {
					runtimeProperties = new double[1];
					runtimeProperties[0] = utility;
				}

				float[] nextChromosom = ((IIndividuum) nextState).getChromosom();
				if (out == null) {
					maxPropertiesWritten = Math.min(maxPropertiesWritten, runtimeProperties.length);
					out = new PrintStream[maxPropertiesWritten];

					for (int i = 0; i < maxPropertiesWritten; i++) {
						out[i] = new PrintStream(new File("result" + i + ".csv"));
						out[i].println("#" + getName() + " Parameter: " + i);
						out[i].println(headerLine);
					}
				}

				float param0 = nextChromosom[0];
				if (param0 < ((IIndividuum) currentState).getChromosom()[0]) {
					for (int i = 0; i < maxPropertiesWritten; i++) {
						if (!firstTime) {
							out[i].print('\n');
						}
						if (nextChromosom.length > 1) {
							// print values of 2nd parameter if existing
							float param = nextChromosom[1];
							out[i].print(String.format("%.3f", param));
						}
					}
					firstTime = false;
				}

				for (int i = 0; i < maxPropertiesWritten; i++) {
					out[i].print(String.format(";%.3f", runtimeProperties[i]));
				}

				currentState = nextState;
			}

			for (int i = 0; i < maxPropertiesWritten; i++) {
				out[i].close();
			}

			return currentState;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return initialState;
		}
	}

	private String createHeaderLine(IProblemState initialState)
	{
		IProblemState currentState = initialState;
		boolean firstTime = true;
		// run once through the iterator without calculating utility to get params
		// row
		Iterator<IOperator> operatorIterator = currentState.fullSearchIterator();
		StringBuilder headerLine = new StringBuilder();
		headerLine.append("#y/x");
		while (operatorIterator.hasNext()) {
			IOperator next = operatorIterator.next();
			IProblemState nextState = next.getSuccessorState(currentState);
			float[] nextChromosom = ((IIndividuum) nextState).getChromosom();
			if (!firstTime && nextChromosom[0] < ((IIndividuum) currentState).getChromosom()[0]) {
				// finished one row
				break;
			}
			firstTime = false;
			currentState = nextState;
			// could not find out how to change the default separator of word()
			// function in gnuplot, so using spaces here
			headerLine.append(String.format(" %.3f", nextChromosom[0]));
		}
		return headerLine.toString();
	}
}
