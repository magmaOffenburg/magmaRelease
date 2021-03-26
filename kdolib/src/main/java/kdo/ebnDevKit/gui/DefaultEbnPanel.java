/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui;

import java.awt.BorderLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import kdo.ebn.observation.IEbnObserver;
import kdo.ebnDevKit.agent.IEbnAgent.AgentStatus;
import kdo.ebnDevKit.ebnAccess.IEbnAccess;

/**
 * abstract class for all ebn panel implementations. it handles all
 * specifications of the IEbnView interface. but the subclasses have to
 * implement two methods.
 * @author Thomas Rinklin
 *
 */
public abstract class DefaultEbnPanel extends JPanel
{
	private static final long serialVersionUID = 3279821649979701247L;

	/** reference to the ebn */
	protected final IEbnAccess ebnAccess;

	/** reference to the main panel */
	protected final JFrame parent;

	/** ebn observer object */
	private final ModelObserver ebnObserver;

	/**
	 * constructor, needs to be called from child class constructors
	 * @param g reference to the main panel
	 */
	public DefaultEbnPanel(JFrame parent, IEbnAccess ebnAccess)
	{
		this.parent = parent;
		this.ebnAccess = ebnAccess;
		this.setLayout(new BorderLayout());
		ebnObserver = new ModelObserver();
	}

	/**
	 * connects the panel to the model and forces to reload the panel
	 */
	public void connect()
	{
		this.ebnAccess.observe().attach(ebnObserver);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				ebnStructureChanged();
			}
		});
	}

	/**
	 * retruns if the ebn is running
	 * @return true if the ebn is running, false if not
	 */
	public boolean isRunning()
	{
		return (ebnAccess.getAgent().getStatus() == AgentStatus.Running);
	}

	/**
	 * saves the ebn
	 */
	public void save()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save Network");
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setSelectedFile(null);

		int dialogReturn = fileChooser.showSaveDialog(this);
		if (dialogReturn == JFileChooser.APPROVE_OPTION)
			ebnAccess.save((fileChooser.getSelectedFile()));
	}

	/**
	 * starts the ebn
	 */
	public void start()
	{
		if (ebnAccess.getAgent().getStatus() != AgentStatus.Running)
			ebnAccess.getAgent().start();
	}

	/**
	 * stops the ebn
	 */
	public void stop()
	{
		if (ebnAccess.getAgent().getStatus() != AgentStatus.Stopped)
			ebnAccess.getAgent().stop();
	}

	/**
	 * close the ebn view
	 */
	public void close()
	{
		ebnAccess.observe().detach(ebnObserver);
	}

	/**
	 * Returns if the ebn is startable
	 *
	 * @return True if startable, false if not
	 */
	public boolean isStartable()
	{
		return ebnAccess.getAgent().isStartable();
	}

	/**
	 * Method to get the agent of a ebnview
	 *
	 * @return The agent reference
	 */
	public IEbnAccess getEbnAccess()
	{
		return ebnAccess;
	}

	/**
	 * abstract method to implement behavior when the values have changed
	 */
	protected abstract void ebnValuesChanged();

	/**
	 * abstract method to implement behavior when the structure has changed
	 */
	protected abstract void ebnStructureChanged();

	@Override
	public String getName()
	{
		if (ebnAccess != null)
			return ebnAccess.getAgent().getName();
		return "";
	}

	/**
	 * checks if the tow panels contain the same agent
	 * @param panelOfAgent other panel
	 * @return true if tow passed panel contains the same agent as this panel,
	 *         false if not
	 */
	public boolean isSameAgent(DefaultEbnPanel panelOfAgent)
	{
		return ebnAccess.equals(panelOfAgent.ebnAccess);
	}

	/**
	 * model observer class
	 * @author Thomas Rinklin
	 *
	 */
	class ModelObserver implements IEbnObserver
	{
		@Override
		public void valuesChanged()
		{
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run()
				{
					ebnValuesChanged();
				}
			});
		}

		@Override
		public void structureChanged()
		{
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run()
				{
					ebnStructureChanged();
				}
			});
		}
	}
}
