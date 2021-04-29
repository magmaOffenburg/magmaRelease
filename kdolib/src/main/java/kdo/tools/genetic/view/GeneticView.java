/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.tools.genetic.view;

/*
 * Copyright (c) 2007-2009 Klaus Dorer
 * Copyright (c) 2009 Simon Raffeiner
 *
 * Hochschule Offenburg
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import kdo.domain.IIndividuumView;
import kdo.search.strategy.local.genetic.impl.GeneticSearchParameter;
import kdo.tools.genetic.model.GeneticOptimization;
import kdo.util.observer.IObserver;

/**
 * The frame containing all the elements to do in the exercise
 *
 * @author klaus
 */
public class GeneticView extends JFrame implements IObserver<Boolean>, IGeneticInteractiveView
{
	/** for serialization */
	private static final long serialVersionUID = 1L;

	/** reference to the model class (TODO: should be read only) */
	private GeneticOptimization model;

	/** reference to the panel containing the play buttons */
	private PlayerControlls playerControlls;

	private IIndividuumView graphics;

	// Strategy settings
	private JComboBox<String> selectionStrategy;

	private JComboBox<String> recombinationStrategy;

	private JComboBox<String> mutationStrategy;

	// Value parameters
	/** the text field for the number of individuums */
	private JTextField populationSizeText;

	/** the text field for the number of generations to breed */
	private JTextField generationsCountText;

	/** the text field for the number of threads */
	private JTextField threadsText;

	/** the text field for the maximal runtime in ms */
	private JTextField maxRuntimeText;

	/** the text field for the number of genders */
	private JTextField genderSizeText;

	/** the text field for the number of parents per individuum */
	private JTextField parentsPerIndividuumText;

	/** the text field for the relative amount of individuums that */
	private JTextField oldToNewGenerationText;

	/** the text field for the mutation probability */
	private JTextField mutationProbabilityText;

	/** the text field for the single gene mutation probability */
	private JTextField geneMutationProbabilityText;

	/** the text field for the relative amount of individuums to select */
	private JTextField eliteSelectionRatio;

	/** the label for the current fitness of the best individuum */
	private JTextField bestFitness;

	/** the label for the average diversity */
	private JTextField averageDiversity;

	/** the label for the time breeding is/was running */
	private JTextField runtime;

	/**
	 * Default constructor to create the frame
	 */
	public GeneticView(GeneticOptimization model, String[] problemNames, IIndividuumView display)
	{
		super("Optimization");
		this.model = model;
		this.setSize(900, 750);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container myContainer = this.getContentPane();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 3));
		addComponents(panel);
		myContainer.add(BorderLayout.SOUTH, panel);

		setGraphics(display);

		playerControlls = new PlayerControlls(model, problemNames);
		myContainer.add(BorderLayout.CENTER, playerControlls);

		updateView();
		// pack();
		model.attach(this);
		setVisible(true);
	}

	/**
	 * @param myContainer
	 */
	private void addComponents(Container myContainer)
	{
		JPanel generalPanel = new JPanel();
		generalPanel.setLayout(new GridLayout(3, 2));
		generalPanel.setBorder(new TitledBorder("General"));

		generalPanel.add(new JLabel("Population size"));
		generalPanel.add(populationSizeText = new JTextField(8));

		generalPanel.add(new JLabel("Genders"));
		generalPanel.add(genderSizeText = new JTextField(8));

		generalPanel.add(new JLabel("Threads"));
		generalPanel.add(threadsText = new JTextField(8));

		myContainer.add(generalPanel);

		JPanel runtimePanel = new JPanel();
		runtimePanel.setLayout(new GridLayout(2, 2));
		runtimePanel.setBorder(new TitledBorder("Runtime"));

		runtimePanel.add(new JLabel("Generations"));
		runtimePanel.add(generationsCountText = new JTextField(8));

		runtimePanel.add(new JLabel("Max runtime (ms)"));
		runtimePanel.add(maxRuntimeText = new JTextField(8));

		myContainer.add(runtimePanel);

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(3, 2));
		infoPanel.setBorder(new TitledBorder("Info"));

		// Result elements
		infoPanel.add(new JLabel("Fitness of best"));
		infoPanel.add(bestFitness = new JTextField(""));
		bestFitness.setEditable(false);

		infoPanel.add(new JLabel("Average diversity"));
		infoPanel.add(averageDiversity = new JTextField(""));
		averageDiversity.setEditable(false);

		infoPanel.add(new JLabel("Runtime (ms)"));
		infoPanel.add(runtime = new JTextField(""));
		runtime.setEditable(false);

		myContainer.add(infoPanel);

		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new GridLayout(3, 2));
		selectionPanel.setBorder(new TitledBorder("Selection"));

		selectionPanel.add(new JLabel("Old to new"));
		selectionPanel.add(oldToNewGenerationText = new JTextField(8));

		selectionPanel.add(new JLabel("Strategy"));
		String[] items1 = model.getSelectionStrategyNames();
		selectionPanel.add(selectionStrategy = new JComboBox<>(items1));

		selectionPanel.add(new JLabel("Elite ratio"));
		selectionPanel.add(eliteSelectionRatio = new JTextField(8));

		myContainer.add(selectionPanel);

		JPanel recombinationPanel = new JPanel();
		recombinationPanel.setLayout(new GridLayout(2, 2));
		recombinationPanel.setBorder(new TitledBorder("Recombination"));

		recombinationPanel.add(new JLabel("Strategy"));
		String[] items2 = model.getRecombinationStrategyNames();
		recombinationPanel.add(recombinationStrategy = new JComboBox<>(items2));

		recombinationPanel.add(new JLabel("Parents"));
		recombinationPanel.add(parentsPerIndividuumText = new JTextField(8));

		myContainer.add(recombinationPanel);

		JPanel mutationPanel = new JPanel();
		mutationPanel.setLayout(new GridLayout(3, 2));
		mutationPanel.setBorder(new TitledBorder("Mutation"));

		mutationPanel.add(new JLabel("Strategy"));
		String[] items3 = model.getMutationStrategyNames();
		mutationPanel.add(mutationStrategy = new JComboBox<>(items3));

		mutationPanel.add(new JLabel("Probability (individuum)"));
		mutationPanel.add(mutationProbabilityText = new JTextField(8));

		mutationPanel.add(new JLabel("Probability (gene)"));
		mutationPanel.add(geneMutationProbabilityText = new JTextField(8));

		myContainer.add(mutationPanel);
	}

	@Override
	public void update(Boolean running)
	{
		bestFitness.setText("" + model.getBestFitness());
		averageDiversity.setText("" + model.getAverageDiversity());
		runtime.setText("" + model.getRuntime());
		graphics.setPopulation(model.getPopulation());
		graphics.setCurrentIndividuum(model.getCurrentIndividuum());
		eliteSelectionRatio.setEnabled(model.getEliteSlectionName().equals(getSelectionStrategy()));
		parentsPerIndividuumText.setEnabled(!model.getSingleCrossoverName().equals(getRecombinationStrategy()));
		mutationProbabilityText.setEnabled(!model.getNoMutationName().equals(getMutationStrategy()));
		geneMutationProbabilityText.setEnabled(!model.getNoMutationName().equals(getMutationStrategy()));
		populationSizeText.setText("" + model.getParams().getPopulationSize());
		threadsText.setText("" + model.getParams().getFitnessCalculationThreadpoolSize());
	}

	/**
	 * Updates all frame elements that depend on model changes
	 */
	@Override
	public void updateView()
	{
		// TODO: remove dependency from GeneticSearchParameter
		GeneticSearchParameter params = model.getParams();
		generationsCountText.setText("" + params.getGenerations());
		maxRuntimeText.setText("" + params.getMaxRuntime());
		populationSizeText.setText("" + params.getPopulationSize());
		genderSizeText.setText("" + params.getGenders());
		threadsText.setText("" + params.getFitnessCalculationThreadpoolSize());
		parentsPerIndividuumText.setText("" + params.getParentsPerIndividuum());
		oldToNewGenerationText.setText("" + params.getOldToNew());
		mutationProbabilityText.setText("" + params.getIndividuumMutationProbability());
		geneMutationProbabilityText.setText("" + params.getGeneMutationProbability());
		eliteSelectionRatio.setText("" + params.getEliteSelectionRatio());
		selectionStrategy.setSelectedItem(model.getSelectionStrategyName());
		recombinationStrategy.setSelectedItem(model.getRecombinationStrategyName());
		mutationStrategy.setSelectedItem(model.getMutationStrategyName());
	}

	@Override
	public void setSelectionStrategyListener(ActionListener listener)
	{
		selectionStrategy.addActionListener(listener);
	}

	@Override
	public void setRecombinationStrategyListener(ActionListener listener)
	{
		recombinationStrategy.addActionListener(listener);
	}

	@Override
	public void setMutationStrategyListener(ActionListener listener)
	{
		mutationStrategy.addActionListener(listener);
	}

	@Override
	public void setGenerationsListener(ActionListener listener, FocusListener focusListener)
	{
		generationsCountText.addActionListener(listener);
		generationsCountText.addFocusListener(focusListener);
	}

	@Override
	public void setMaxRuntimeListener(ActionListener listener, FocusListener focusListener)
	{
		maxRuntimeText.addActionListener(listener);
		populationSizeText.addFocusListener(focusListener);
	}

	@Override
	public void setPopulationSizeListener(ActionListener listener, FocusListener focusListener)
	{
		populationSizeText.addActionListener(listener);
		populationSizeText.addFocusListener(focusListener);
	}

	@Override
	public void setGenderSizeListener(ActionListener listener, FocusListener focusListener)
	{
		genderSizeText.addActionListener(listener);
		genderSizeText.addFocusListener(focusListener);
	}

	@Override
	public void setThreadsListener(ActionListener listener, FocusListener focusListener)
	{
		threadsText.addActionListener(listener);
		threadsText.addFocusListener(focusListener);
	}

	@Override
	public void setParentsListener(ActionListener listener, FocusListener focusListener)
	{
		parentsPerIndividuumText.addActionListener(listener);
		parentsPerIndividuumText.addFocusListener(focusListener);
	}

	@Override
	public void setOldToNewListener(ActionListener listener, FocusListener focusListener)
	{
		oldToNewGenerationText.addActionListener(listener);
		oldToNewGenerationText.addFocusListener(focusListener);
	}

	@Override
	public void setIndividuumMutationProbabilityListener(ActionListener listener, FocusListener focusListener)
	{
		mutationProbabilityText.addActionListener(listener);
		mutationProbabilityText.addFocusListener(focusListener);
	}

	@Override
	public void setGeneMutationProbabilityListener(ActionListener listener, FocusListener focusListener)
	{
		geneMutationProbabilityText.addActionListener(listener);
		geneMutationProbabilityText.addFocusListener(focusListener);
	}

	@Override
	public void setEliteSelectionRatioListener(ActionListener listener, FocusListener focusListener)
	{
		eliteSelectionRatio.addActionListener(listener);
		eliteSelectionRatio.addFocusListener(focusListener);
	}

	@Override
	public void setProblemListener(ActionListener listener)
	{
		playerControlls.addProblemListener(listener);
	}

	@Override
	public void setRewindListener(ActionListener listener)
	{
		playerControlls.addRewindListener(listener);
	}

	@Override
	public void setStartListener(ActionListener listener)
	{
		playerControlls.addStartListener(listener);
	}

	@Override
	public void setStopListener(ActionListener listener)
	{
		playerControlls.addStopListener(listener);
	}

	@Override
	public void setPauseListener(ActionListener listener)
	{
		playerControlls.addPauseListener(listener);
	}

	@Override
	public void setStepListener(ActionListener listener)
	{
		playerControlls.addStepListener(listener);
	}

	@Override
	public void setFirstIndividuumListener(ActionListener listener)
	{
		playerControlls.addFirstIndividuumListener(listener);
	}

	@Override
	public void setPreviousIndividuumListener(ActionListener listener)
	{
		playerControlls.addPreviousIndividuumListener(listener);
	}

	@Override
	public void setNextIndividuumListener(ActionListener listener)
	{
		playerControlls.addNextIndividuumListener(listener);
	}

	@Override
	public void setLastIndividuumListener(ActionListener listener)
	{
		playerControlls.addLastIndividuumListener(listener);
	}

	@Override
	public String getSelectionStrategy()
	{
		return (String) selectionStrategy.getSelectedItem();
	}

	@Override
	public String getRecombinationStrategy()
	{
		return (String) recombinationStrategy.getSelectedItem();
	}

	@Override
	public String getMutationStrategy()
	{
		return (String) mutationStrategy.getSelectedItem();
	}

	@Override
	public String getProblem()
	{
		return playerControlls.getProblem();
	}

	@Override
	public int getGenerations()
	{
		return Integer.valueOf(generationsCountText.getText()).intValue();
	}

	@Override
	public int getMaxRuntime()
	{
		return Integer.valueOf(maxRuntimeText.getText()).intValue();
	}

	@Override
	public int getPopulationSize()
	{
		return Integer.valueOf(populationSizeText.getText()).intValue();
	}

	@Override
	public int getGenderSize()
	{
		return Integer.valueOf(genderSizeText.getText()).intValue();
	}

	@Override
	public int getThreads()
	{
		return Integer.valueOf(threadsText.getText()).intValue();
	}

	@Override
	public int getParents()
	{
		return Integer.valueOf(parentsPerIndividuumText.getText()).intValue();
	}

	@Override
	public float getOldToNew()
	{
		return Float.valueOf(oldToNewGenerationText.getText()).floatValue();
	}

	@Override
	public float getIndividuumMutationProbability()
	{
		return Float.valueOf(mutationProbabilityText.getText()).floatValue();
	}

	@Override
	public float getGeneMutationProbability()
	{
		return Float.valueOf(geneMutationProbabilityText.getText()).floatValue();
	}

	@Override
	public float getEliteSelectionRatio()
	{
		return Float.valueOf(eliteSelectionRatio.getText()).floatValue();
	}

	@Override
	public void setModel(GeneticOptimization model)
	{
		this.model.detach(this);
		this.model = model;
		model.attach(this);
		playerControlls.setModel(model);
		update(false);
		updateView();
	}

	@Override
	public void setDisplay(IIndividuumView individuumView)
	{
		setGraphics(individuumView);
	}

	private void setGraphics(IIndividuumView individuumView)
	{
		Container myContainer = this.getContentPane();
		if (graphics != null) {
			myContainer.remove(graphics.getDisplayPanel());
		}
		this.graphics = individuumView;
		myContainer.add(BorderLayout.NORTH, graphics.getDisplayPanel());
	}
}
