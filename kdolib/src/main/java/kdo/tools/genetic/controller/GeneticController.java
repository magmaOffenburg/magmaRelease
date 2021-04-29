/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.tools.genetic.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kdo.domain.IIndividuum;
import kdo.domain.IIndividuumView;
import kdo.domain.IProblem;
import kdo.domain.IProblemFactory;
import kdo.search.strategy.local.genetic.impl.GeneticSearchParameter;
import kdo.tools.genetic.model.GeneticOptimization;
import kdo.tools.genetic.view.GeneticConsoleView;
import kdo.tools.genetic.view.GeneticView;
import kdo.tools.genetic.view.IGeneticInteractiveView;
import kdo.tools.genetic.view.IGeneticView;
import kdo.util.IRandomSource;
import kdo.util.RandomSource;

/**
 * Controller and main entry point of genetic visualization
 * @author dorer
 */
public class GeneticController
{
	/** link to the model object */
	private GeneticOptimization model;

	/** link to the view object */
	private IGeneticView view;

	/** factories for the different problems */
	private Map<String, IProblemFactory> factories;

	private String[] problemNames;

	/** the parametrization to use for gentic optimization */
	private GeneticSearchParameter params;

	private IProblemFactory iProblemFactory;

	/**
	 * @param args command line arguments
	 */
	public static void main(String[] args)
	{
		new GeneticController(null, true, null);
	}

	/**
	 * Constructor that allows to specify the factories
	 * @param classes the fully qualified names of factory classes that are
	 *        available for optimization, null if they should be read from
	 *        configuration file
	 */
	public GeneticController(List<String> classes, boolean gui, GeneticSearchParameter params)
	{
		if (classes == null) {
			classes = readClasses();
		}
		if (params == null) {
			// use default parameters
			IRandomSource randomSource = new RandomSource(System.currentTimeMillis());
			params = new GeneticSearchParameter(randomSource);
			params.setPopulationSize(50);
			params.setGenerations(50);
			params.setMaxRuntime(60000L);
			params.setGenders(2);
			params.setParentsPerIndividuum(2);
			params.setIndividuumMutationProbability(0.01f);
			params.setGeneMutationProbability(0.05f);
			params.setFitnessCalculationThreadpoolSize(1);
		}
		this.params = params;

		getProblems(classes);
		createModel(problemNames[0]);
		createView(gui);
	}

	/**
	 * Starts the optimization if not already running
	 */
	public void start()
	{
		if (!model.isRunning()) {
			Thread runThread;
			runThread = new Thread("GeneticRun") {
				@Override
				public void run()
				{
					model.start();
				}
			};
			runThread.start();
		}
	}

	/**
	 * Get the problem factories we can optimize
	 */
	private void getProblems(List<String> classes)
	{
		factories = new LinkedHashMap<>();

		IProblemFactory factory;
		for (String classname : classes) {
			try {
				factory = (IProblemFactory) Class.forName(classname).newInstance();
				factories.put(factory.getProblemName(), factory);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		problemNames = new String[factories.size()];
		int i = 0;
		for (String factoryName : factories.keySet()) {
			problemNames[i] = factoryName;
			i++;
		}
	}

	private List<String> readClasses()
	{
		ClassLoader cl = this.getClass().getClassLoader();
		String filename = "configs/problems.txt";
		InputStream is = cl.getResourceAsStream(filename);

		if (is == null) {
			System.out.println("File can not be found: " + filename);
			return null;
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;

		List<String> result = new ArrayList<>();
		try {
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			return result;

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	/**
	 *
	 */
	private void createModel()
	{
		String problemName = ((IGeneticInteractiveView) view).getProblem();
		createModel(problemName);
	}

	/**
	 *
	 */
	private void createModel(String problemName)
	{
		iProblemFactory = factories.get(problemName);
		IProblem problem = iProblemFactory.getProblem(params.getRandomSource());
		params.setDomain(problem);

		model = new GeneticOptimization(params);
		if (view != null) {
			view.setModel(model);
			if (view instanceof IGeneticInteractiveView) {
				((IGeneticInteractiveView) view).setDisplay(iProblemFactory.getIndividuumView());
			}
		}
	}

	/**
	 *
	 */
	private void createView(boolean guiView)
	{
		IIndividuumView display = iProblemFactory.getIndividuumView();
		if (guiView) {
			view = new GeneticView(model, problemNames, display);
			createListeners();
		} else {
			view = new GeneticConsoleView(model);
			start();
		}
	}

	public IIndividuum getBestIndividuum()
	{
		return model.getBestIndividuum();
	}

	public boolean isRunning()
	{
		return model != null && model.isRunning();
	}

	private void createListeners()
	{
		IGeneticInteractiveView view = (IGeneticInteractiveView) this.view;
		view.setSelectionStrategyListener(new SelectionStrategyListener());
		view.setRecombinationStrategyListener(new RecombinationStrategyListener());
		view.setMutationStrategyListener(new MutationStrategyListener());
		view.setProblemListener(new ProblemListener());
		GenerationsListener generationsListener = new GenerationsListener();
		view.setGenerationsListener(generationsListener, generationsListener);
		MaxRuntimeListener maxRuntimeListener = new MaxRuntimeListener();
		view.setMaxRuntimeListener(maxRuntimeListener, maxRuntimeListener);
		PopulationSizeListener sizeListener = new PopulationSizeListener();
		view.setPopulationSizeListener(sizeListener, sizeListener);
		GenderSizeListener genderSizeListener = new GenderSizeListener();
		view.setGenderSizeListener(genderSizeListener, genderSizeListener);
		ThreadsListener threadsListener = new ThreadsListener();
		view.setThreadsListener(threadsListener, threadsListener);
		ParentsListener parentsListener = new ParentsListener();
		view.setParentsListener(parentsListener, parentsListener);
		OldToNewListener oldToNewListener = new OldToNewListener();
		view.setOldToNewListener(oldToNewListener, oldToNewListener);
		IndividuumMutationProbabilityListener probabilityListener = new IndividuumMutationProbabilityListener();
		view.setIndividuumMutationProbabilityListener(probabilityListener, probabilityListener);
		GeneMutationProbabilityListener listener = new GeneMutationProbabilityListener();
		view.setGeneMutationProbabilityListener(listener, listener);
		EliteSelectionRatioListener ratioListener = new EliteSelectionRatioListener();
		view.setEliteSelectionRatioListener(ratioListener, ratioListener);
		view.setRewindListener(new RewindListener());
		view.setStartListener(new StartListener());
		view.setStopListener(new StopListener());
		view.setPauseListener(new PauseListener());
		view.setStepListener(new StepListener());
		view.setFirstIndividuumListener(new FirstIndividuumListener());
		view.setPreviousIndividuumListener(new PreviousIndividuumListener());
		view.setNextIndividuumListener(new NextIndividuumListener());
		view.setLastIndividuumListener(new LastIndividuumListener());
	}

	class SelectionStrategyListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			model.setSelectionStrategy(((IGeneticInteractiveView) view).getSelectionStrategy());
			view.update(false);
		}
	}

	class RecombinationStrategyListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			model.setRecombinationStrategy(((IGeneticInteractiveView) view).getRecombinationStrategy());
			view.update(false);
		}
	}

	class MutationStrategyListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			model.setMutationStrategy(((IGeneticInteractiveView) view).getMutationStrategy());
			view.update(false);
		}
	}

	class ProblemListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			createModel();
		}
	}

	abstract class BaseListener extends FocusAdapter implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			action();
		}

		@Override
		public void focusLost(FocusEvent e)
		{
			action();
		}

		protected abstract void action();
	}

	class GenerationsListener extends BaseListener implements ActionListener
	{
		@Override
		protected void action()
		{
			model.setGenerations(((IGeneticInteractiveView) view).getGenerations());
		}
	}

	class MaxRuntimeListener extends BaseListener implements ActionListener
	{
		@Override
		protected void action()
		{
			model.setMaxRuntime(((IGeneticInteractiveView) view).getMaxRuntime());
		}
	}

	class PopulationSizeListener extends BaseListener implements ActionListener
	{
		@Override
		protected void action()
		{
			try {
				int populationSize = ((IGeneticInteractiveView) view).getPopulationSize();
				if (model.getNumberOfIndividuums() != populationSize) {
					params.setPopulationSize(populationSize);
					createModel();
					((IGeneticInteractiveView) view).update(false);
				}
			} catch (Exception e) {
				((IGeneticInteractiveView) view).update(false);
			}
		}
	}

	class GenderSizeListener extends BaseListener implements ActionListener
	{
		@Override
		protected void action()
		{
			try {
				int oldValue = ((IGeneticInteractiveView) view).getGenderSize();
				if (model.getGenders() != oldValue) {
					params.setGenders(oldValue);
					createModel();
					((IGeneticInteractiveView) view).update(false);
				}
			} catch (Exception e) {
				((IGeneticInteractiveView) view).updateView();
			}
		}
	}

	class ThreadsListener extends BaseListener implements ActionListener
	{
		@Override
		protected void action()
		{
			try {
				int populationSize = ((IGeneticInteractiveView) view).getPopulationSize();
				int threads = ((IGeneticInteractiveView) view).getThreads();
				if (threads < 1) {
					threads = 1;
				} else if (threads > populationSize) {
					threads = populationSize;
				}
				params.setFitnessCalculationThreadpoolSize(threads);
				createModel();
				((IGeneticInteractiveView) view).update(false);

			} catch (Exception e) {
				((IGeneticInteractiveView) view).update(false);
			}
		}
	}

	class ParentsListener extends BaseListener implements ActionListener
	{
		@Override
		protected void action()
		{
			try {
				int oldValue = ((IGeneticInteractiveView) view).getParents();
				if (model.getParents() != oldValue) {
					params.setParentsPerIndividuum(oldValue);
					createModel();
					((IGeneticInteractiveView) view).update(false);
				}
			} catch (Exception e) {
				((IGeneticInteractiveView) view).updateView();
			}
		}
	}

	class OldToNewListener extends BaseListener implements ActionListener
	{
		@Override
		protected void action()
		{
			try {
				model.setOldToNew(((IGeneticInteractiveView) view).getOldToNew());
			} catch (Exception e) {
				((IGeneticInteractiveView) view).updateView();
			}
		}
	}

	class IndividuumMutationProbabilityListener extends BaseListener implements ActionListener
	{
		@Override
		protected void action()
		{
			try {
				model.setIndividuumMutationProbability(
						((IGeneticInteractiveView) view).getIndividuumMutationProbability());
			} catch (Exception e) {
				((IGeneticInteractiveView) view).updateView();
			}
		}
	}

	class GeneMutationProbabilityListener extends BaseListener implements ActionListener
	{
		@Override
		protected void action()
		{
			try {
				model.setGeneMutationProbability(((IGeneticInteractiveView) view).getGeneMutationProbability());
			} catch (Exception e) {
				((IGeneticInteractiveView) view).updateView();
			}
		}
	}

	class EliteSelectionRatioListener extends BaseListener implements ActionListener
	{
		@Override
		protected void action()
		{
			model.setEliteSelectionRatio(((IGeneticInteractiveView) view).getEliteSelectionRatio());
		}
	}

	class RewindListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (!model.isRunning()) {
				model.rewind();
			}
		}
	}

	class StartListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			start();
		}
	}

	class StopListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (model.isRunning()) {
				model.stop();
			}
		}
	}

	class PauseListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (model.isRunning()) {
				model.pause();
			}
		}
	}

	class StepListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (!model.isRunning()) {
				model.step();
			}
		}
	}

	class FirstIndividuumListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (!model.isRunning() && model.hasStarted()) {
				model.setCurrentIndividuumIndex(0);
			}
		}
	}

	class PreviousIndividuumListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int currentIndividuumIndex = model.getCurrentIndividuumIndex();
			if (!model.isRunning() && model.hasStarted() && currentIndividuumIndex > 0) {
				model.setCurrentIndividuumIndex(currentIndividuumIndex - 1);
			}
		}
	}

	class NextIndividuumListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int currentIndividuumIndex = model.getCurrentIndividuumIndex();
			if (!model.isRunning() && model.hasStarted() &&
					currentIndividuumIndex < model.getNumberOfIndividuums() - 1) {
				model.setCurrentIndividuumIndex(currentIndividuumIndex + 1);
			}
		}
	}

	class LastIndividuumListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (!model.isRunning() && model.hasStarted()) {
				model.setCurrentIndividuumIndex(model.getNumberOfIndividuums() - 1);
			}
		}
	}
}
