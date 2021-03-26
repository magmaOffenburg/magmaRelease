/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import kdo.ebnDevKit.ebnAccess.EbnAccessFactory;
import kdo.ebnDevKit.ebnAccess.IEbnAccess;
import kdo.ebnDevKit.gui.ebnGraphPanel.EbnGraphPanel;
import kdo.ebnDevKit.gui.ebnGraphPanel.GraphConfiguration;
import kdo.ebnDevKit.gui.ebnTreePanel.EbnTreePanel;

/**
 * Provides ActionListeners for the EbnDevKit Application
 * @author Thomas Rinklin
 *
 */
public class ApplicationActionListeners
{
	private final EbnDevKit devKit;

	private final EbnAccessFactory ebnAccessFactory;

	private final GraphConfiguration graphConfiguration;

	private MenuBar menuBar;

	private EbnTabbedPane tabbedPane;

	public ApplicationActionListeners(
			EbnDevKit devKit, EbnAccessFactory ebnAccessFactory, GraphConfiguration graphConfiguration)
	{
		this.devKit = devKit;
		this.ebnAccessFactory = ebnAccessFactory;
		this.graphConfiguration = graphConfiguration;
	}

	public void setMenuBar(MenuBar menuBar)
	{
		this.menuBar = menuBar;
	}

	public void setTabbedPane(EbnTabbedPane tabbedPane)
	{
		this.tabbedPane = tabbedPane;
	}

	private void createPanel(IEbnAccess ebnAccess)
	{
		DefaultEbnPanel ebnPanel = new EbnGraphPanel(devKit.getFrame(), ebnAccess, graphConfiguration, true);

		ebnPanel.connect();
		tabbedPane.addToTabbedPane(ebnPanel);
	}

	private void closePanel(DefaultEbnPanel panel)
	{
		tabbedPane.removeFromTabbedPane(panel);

		if (!tabbedPane.isViewOfSameAgentExisting(panel, DefaultEbnPanel.class))
			panel.stop();

		panel.close();
	}

	/**
	 * action listener, that shows an open file dialog, and loads an ebn
	 * @author Thomas Rinklin
	 */
	class LoadAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fileChooser = new JFileChooser();

			fileChooser.setDialogTitle("Load Network");
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setSelectedFile(null);

			int dialogReturn = fileChooser.showOpenDialog(devKit);

			if (dialogReturn == JFileChooser.APPROVE_OPTION) {
				IEbnAccess ebnAccess = ebnAccessFactory.createEbnFromFile(fileChooser.getSelectedFile());
				createPanel(ebnAccess);
			}
		}
	}

	/**
	 * action listener, that creates a new ebn,opens a graph view and adds it to
	 * the tabbed pane
	 * @author Thomas Rinklin
	 */
	class CreateNewAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			IEbnAccess ebnAccess = ebnAccessFactory.creatEmptyEbnAccess();
			createPanel(ebnAccess);
		}
	}

	/**
	 * save action
	 * @author Thomas Rinklin
	 *
	 */
	class SaveAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			tabbedPane.getSelectedEbnPanel().save();
		}
	}

	/**
	 * close action
	 * @author Thomas Rinklin
	 *
	 */
	class CloseAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultEbnPanel panel = tabbedPane.getSelectedEbnPanel();
			closePanel(panel);
		}
	}

	/**
	 * start action
	 * @author Thomas Rinklin
	 */
	class StartAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			tabbedPane.getSelectedEbnPanel().start();
			menuBar.updateButtons();
		}
	}

	/**
	 * stop action
	 * @author Thomas Rinklin
	 */
	class StopAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			tabbedPane.getSelectedEbnPanel().stop();
			menuBar.updateButtons();
		}
	}

	/**
	 * action listener, that toggels the tree view of the agent of the current
	 * view
	 * @author Thomas Rinklin
	 *
	 */
	class TreeViewAction extends ToggleViewAction
	{
		@Override
		protected DefaultEbnPanel createObject(IEbnAccess ebnAccess)
		{
			return new EbnTreePanel(devKit.getFrame(), ebnAccess);
		}

		@Override
		protected Class<? extends DefaultEbnPanel> getViewClass()
		{
			return EbnTreePanel.class;
		}

		@Override
		protected boolean getSelected()
		{
			return menuBar.isButtonTreeViewSelected();
		}
	}

	/**
	 * action listener, that toggels the graph view of the agent of the current
	 * view
	 * @author Thomas Rinklin
	 */
	class GraphViewAction extends ToggleViewAction
	{
		@Override
		protected DefaultEbnPanel createObject(IEbnAccess ebnAccess)
		{
			return new EbnGraphPanel(devKit.getFrame(), ebnAccess, graphConfiguration, true);
		}

		@Override
		protected Class<? extends DefaultEbnPanel> getViewClass()
		{
			return EbnGraphPanel.class;
		}

		@Override
		protected boolean getSelected()
		{
			return menuBar.isButtonGraphViewSelected();
		}
	}

	/**
	 * action listener, that toggels the graph view of the agent of the current
	 * view
	 * @author Thomas Rinklin
	 */
	private abstract class ToggleViewAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultEbnPanel selectedPanel = tabbedPane.getSelectedEbnPanel();
			if (!getSelected()) {
				DefaultEbnPanel panel = tabbedPane.getViewOfSameAgent(selectedPanel, getViewClass());
				closePanel(panel);
			} else {
				IEbnAccess ebnAccess = selectedPanel.getEbnAccess();
				DefaultEbnPanel ebnPanel = createObject(ebnAccess);
				ebnPanel.connect();
				tabbedPane.addToTabbedPane(ebnPanel);
			}
		}

		abstract protected boolean getSelected();

		abstract protected DefaultEbnPanel createObject(IEbnAccess ebnAccess);

		abstract protected Class<? extends DefaultEbnPanel> getViewClass();
	}
}
