/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.tools.genetic.view;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import kdo.domain.IIndividuumView;

public interface IGeneticInteractiveView extends IGeneticView {
	void setSelectionStrategyListener(ActionListener listener);

	void setRecombinationStrategyListener(ActionListener listener);

	void setMutationStrategyListener(ActionListener listener);

	void setGenerationsListener(ActionListener listener, FocusListener focusListener);

	void setMaxRuntimeListener(ActionListener listener, FocusListener focusListener);

	void setPopulationSizeListener(ActionListener listener, FocusListener focusListener);

	void setGenderSizeListener(ActionListener listener, FocusListener focusListener);

	void setThreadsListener(ActionListener listener, FocusListener focusListener);

	void setParentsListener(ActionListener listener, FocusListener focusListener);

	void setOldToNewListener(ActionListener listener, FocusListener focusListener);

	void setIndividuumMutationProbabilityListener(ActionListener listener, FocusListener focusListener);

	void setGeneMutationProbabilityListener(ActionListener listener, FocusListener focusListener);

	void setEliteSelectionRatioListener(ActionListener listener, FocusListener focusListener);

	void setProblemListener(ActionListener listener);

	void setRewindListener(ActionListener listener);

	void setStartListener(ActionListener listener);

	void setStopListener(ActionListener listener);

	void setPauseListener(ActionListener listener);

	void setStepListener(ActionListener listener);

	void setFirstIndividuumListener(ActionListener listener);

	void setPreviousIndividuumListener(ActionListener listener);

	void setNextIndividuumListener(ActionListener listener);

	void setLastIndividuumListener(ActionListener listener);

	String getSelectionStrategy();

	String getRecombinationStrategy();

	String getMutationStrategy();

	String getProblem();

	int getGenerations();

	int getMaxRuntime();

	int getPopulationSize();

	int getGenderSize();

	int getThreads();

	int getParents();

	float getOldToNew();

	float getIndividuumMutationProbability();

	float getGeneMutationProbability();

	float getEliteSelectionRatio();

	void setDisplay(IIndividuumView individuumView);
}