/*
 *  Copyright 2011 RoboViz
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kdo.tools.genetic.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import kdo.domain.IIndividuum;
import kdo.tools.genetic.model.GeneticOptimization;
import kdo.util.observer.IObserver;

/**
 * Dialog containing the media player controls for log mode
 *
 * @author dorer
 */
class PlayerControlls extends JPanel implements IObserver<Boolean>
{
	private static final long serialVersionUID = -3858876806399693025L;

	/** reference to the model class (TODO: should be read only) */
	private GeneticOptimization model;

	private JComboBox<String> problemSelected;

	private JButton rewindButton;

	private JButton stopButton;

	private JButton playButton;

	private JButton stepForwardButton;

	private JButton pauseButton;

	private JLabel generationCount;

	private JLabel individuumCount;

	private JButton firstIndividuumButton;

	private JButton previousIndividuumButton;

	private JButton nextIndividuumButton;

	private JButton lastIndividuumButton;

	private JLabel individuumFitness;

	public PlayerControlls(GeneticOptimization model, String[] problemNames)
	{
		this.model = model;
		createControlls(problemNames);
		this.model.attach(this);
	}

	public void setModel(GeneticOptimization model)
	{
		this.model.detach(this);
		this.model = model;
		this.model.attach(this);
	}

	/**
	 * Create the buttons and other GUI controls
	 */
	private void createControlls(String[] problemNames)
	{
		String path = "resources/icons/";
		File file = new File(path);
		if (!file.exists()) {
			path = "icons/";
		}

		setSize(400, 50);
		setLayout(new FlowLayout());
		setVisible(true);

		add(new JLabel("Problem: "));
		problemSelected = new JComboBox<String>(problemNames);
		problemSelected.setToolTipText("Choose Problem to optimize");
		add(problemSelected);

		add(new JLabel("   "));
		add(new JLabel("Generations: "));
		generationCount = new JLabel("0");
		generationCount.setToolTipText("Generations");
		add(generationCount);

		ImageIcon theIcon = new ImageIcon(ClassLoader.getSystemResource(path + "rewind.png"));
		rewindButton = new RoundButton(theIcon);
		rewindButton.setToolTipText("Stop (Restart from beginning)");
		add(rewindButton);

		theIcon = new ImageIcon(ClassLoader.getSystemResource(path + "stop.png"));
		stopButton = new RoundButton(theIcon);
		stopButton.setToolTipText("Stop (Stop and Restart)");
		add(stopButton);

		theIcon = new ImageIcon(ClassLoader.getSystemResource(path + "pause.png"));
		pauseButton = new RoundButton(theIcon);
		pauseButton.setToolTipText("Pause");
		add(pauseButton);

		theIcon = new ImageIcon(ClassLoader.getSystemResource(path + "play.png"));
		playButton = new RoundButton(theIcon);
		playButton.setToolTipText("Play");
		add(playButton);

		theIcon = new ImageIcon(ClassLoader.getSystemResource(path + "next_frame.png"));
		stepForwardButton = new RoundButton(theIcon);
		stepForwardButton.setToolTipText("Step Forward");
		add(stepForwardButton);

		add(new JLabel("   "));
		add(new JLabel("Individuum: "));
		individuumCount = new JLabel("" + (model.getCurrentIndividuumIndex() + 1));
		individuumCount.setToolTipText("Current Individuum");
		add(individuumCount);

		theIcon = new ImageIcon(ClassLoader.getSystemResource(path + "fast_backward.png"));
		firstIndividuumButton = new RoundButton(theIcon);
		firstIndividuumButton.setToolTipText("First Individuum");
		add(firstIndividuumButton);

		theIcon = new ImageIcon(ClassLoader.getSystemResource(path + "previous_frame.png"));
		previousIndividuumButton = new RoundButton(theIcon);
		previousIndividuumButton.setToolTipText("Previous Individuum");
		add(previousIndividuumButton);

		theIcon = new ImageIcon(ClassLoader.getSystemResource(path + "next_frame.png"));
		nextIndividuumButton = new RoundButton(theIcon);
		nextIndividuumButton.setToolTipText("Next Individuum");
		add(nextIndividuumButton);

		theIcon = new ImageIcon(ClassLoader.getSystemResource(path + "fast_forward.png"));
		lastIndividuumButton = new RoundButton(theIcon);
		lastIndividuumButton.setToolTipText("Last Individuum");
		add(lastIndividuumButton);

		individuumFitness = new JLabel("");
		individuumFitness.setToolTipText("Individuum Fitness");
		add(individuumFitness);
	}

	/**
	 * Allows to have round swing buttons
	 */
	class RoundButton extends JButton
	{
		private static final long serialVersionUID = 1L;

		protected Shape shape;

		protected Shape base;

		public RoundButton(Icon icon)
		{
			setModel(new DefaultButtonModel());
			init(null, icon);
			setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			setBackground(Color.BLACK);
			setContentAreaFilled(false);
			setFocusPainted(false);
			setAlignmentY(Component.TOP_ALIGNMENT);
			initShape();
		}

		protected void initShape()
		{
			if (!getBounds().equals(base)) {
				Dimension s = getPreferredSize();
				base = getBounds();
				shape = new Ellipse2D.Float(0, 0, s.width - 1, s.height - 1);
			}
		}

		@Override
		public Dimension getPreferredSize()
		{
			Icon icon = getIcon();
			Insets i = getInsets();
			int iw = Math.max(icon.getIconWidth(), icon.getIconHeight());
			return new Dimension(iw + i.right + i.left, iw + i.top + i.bottom);
		}

		@Override
		public boolean contains(int x, int y)
		{
			initShape();
			return shape.contains(x, y);
		}
	}

	public void addProblemListener(ActionListener listener)
	{
		problemSelected.addActionListener(listener);
	}

	public void addRewindListener(ActionListener listener)
	{
		rewindButton.addActionListener(listener);
	}

	public void addStartListener(ActionListener listener)
	{
		playButton.addActionListener(listener);
	}

	public void addStopListener(ActionListener listener)
	{
		stopButton.addActionListener(listener);
	}

	public void addPauseListener(ActionListener listener)
	{
		pauseButton.addActionListener(listener);
	}

	public void addStepListener(ActionListener listener)
	{
		stepForwardButton.addActionListener(listener);
	}

	public void addFirstIndividuumListener(ActionListener listener)
	{
		firstIndividuumButton.addActionListener(listener);
	}

	public void addPreviousIndividuumListener(ActionListener listener)
	{
		previousIndividuumButton.addActionListener(listener);
	}

	public void addNextIndividuumListener(ActionListener listener)
	{
		nextIndividuumButton.addActionListener(listener);
	}

	public void addLastIndividuumListener(ActionListener listener)
	{
		lastIndividuumButton.addActionListener(listener);
	}

	public void update(Boolean running)
	{
		boolean playing = running.booleanValue();
		boolean atEnd = model.isAtEnd();
		problemSelected.setEnabled(!playing);
		rewindButton.setEnabled(!playing);
		stopButton.setEnabled(playing);
		pauseButton.setEnabled(playing);
		playButton.setEnabled(!playing && !atEnd);
		stepForwardButton.setEnabled(!playing && !atEnd);
		generationCount.setText("" + model.getCurrentGeneration());

		IIndividuum currentIndividuum = model.getCurrentIndividuum();
		if (currentIndividuum != null) {
			boolean hasStarted = model.hasStarted();
			int numberOfIndividuums = model.getNumberOfIndividuums();
			int currentIndividuumIndex = model.getCurrentIndividuumIndex();
			firstIndividuumButton.setEnabled(!playing && hasStarted && currentIndividuumIndex > 0);
			previousIndividuumButton.setEnabled(!playing && hasStarted && currentIndividuumIndex > 0);
			nextIndividuumButton.setEnabled(!playing && hasStarted && currentIndividuumIndex < numberOfIndividuums - 1);
			lastIndividuumButton.setEnabled(!playing && hasStarted && currentIndividuumIndex < numberOfIndividuums - 1);

			individuumCount.setText("" + (currentIndividuumIndex + 1));
			DecimalFormat formatter = new DecimalFormat("#.##");
			individuumFitness.setText("" + formatter.format(currentIndividuum.getFitness()));

		} else {
			firstIndividuumButton.setEnabled(false);
			previousIndividuumButton.setEnabled(false);
			nextIndividuumButton.setEnabled(false);
			lastIndividuumButton.setEnabled(false);
		}
	}

	public String getProblem()
	{
		return (String) problemSelected.getSelectedItem();
	}
}
