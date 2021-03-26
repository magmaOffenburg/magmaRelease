/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import kdo.ebnDevKit.gui.ebnGraphPanel.EbnGraphPanel;
import kdo.ebnDevKit.gui.ebnTreePanel.EbnTreePanel;

/**
 * The MenuBar for the EbnDevKit Application
 * @author Thomas Rinklin
 */
public class MenuBar extends JMenuBar
{
	private static final long serialVersionUID = 3128136828240357512L;

	/** file menu */
	private JMenu ebnMenu;

	/** agent menu */
	private JMenu agentMenu;

	/** view menu */
	private JMenu viewMenu;

	/** start menu entry */
	private JMenuItem buttonStart;

	/** stop menu entry */
	private JMenuItem buttonStop;

	/** save menu entry */
	private JMenuItem buttonSave;

	/** close menu entry */
	private JMenuItem buttonClose;

	/** button to create or remove a treeview */
	private JCheckBoxMenuItem buttonTreeView;

	/** button to create or remove a graphview */
	private JCheckBoxMenuItem buttonGraphView;

	private final ApplicationActionListeners actionListeners;

	private final EbnTabbedPane tabbedPane;

	public MenuBar(ApplicationActionListeners actionListeners, EbnTabbedPane tabbedPane)
	{
		this.actionListeners = actionListeners;
		this.tabbedPane = tabbedPane;
		createMenus();
		createMenuItems();
	}

	private void createMenus()
	{
		ebnMenu = new JMenu("EBN");
		add(ebnMenu);

		agentMenu = new JMenu("Agent");
		add(agentMenu);

		viewMenu = new JMenu("View");
		add(viewMenu);
	}

	/**
	 * create the menu items and add it to the menus
	 */
	private void createMenuItems()
	{
		JMenuItem buttonLoad = new JMenuItem("Load EBN");
		buttonLoad.addActionListener(actionListeners.new LoadAction());

		JMenuItem buttonNew = new JMenuItem("Create new EBN");
		buttonNew.addActionListener(actionListeners.new CreateNewAction());

		buttonStart = new JMenuItem("Start Agent");
		buttonStart.addActionListener(actionListeners.new StartAction());

		buttonStop = new JMenuItem("Stop Agent");
		buttonStop.addActionListener(actionListeners.new StopAction());

		buttonSave = new JMenuItem("Save as ...");
		buttonSave.addActionListener(actionListeners.new SaveAction());

		buttonClose = new JMenuItem("Close");
		buttonClose.addActionListener(actionListeners.new CloseAction());

		buttonTreeView = new JCheckBoxMenuItem("TreeView");
		buttonTreeView.addActionListener(actionListeners.new TreeViewAction());

		buttonGraphView = new JCheckBoxMenuItem("GraphView");
		buttonGraphView.addActionListener(actionListeners.new GraphViewAction());

		ebnMenu.add(buttonLoad);
		ebnMenu.add(buttonNew);
		ebnMenu.add(buttonClose);
		ebnMenu.add(buttonSave);
		agentMenu.add(buttonStart);
		agentMenu.add(buttonStop);
		viewMenu.add(buttonTreeView);
		viewMenu.add(buttonGraphView);
	}

	/**
	 * enables and disables buttons againts to current ebn view
	 */
	public void updateButtons()
	{
		DefaultEbnPanel selPanel = tabbedPane.getSelectedEbnPanel();

		boolean ebnOpend = (selPanel != null);
		boolean ebnStartable = ebnOpend && selPanel.isStartable();
		boolean ebnStarted = ebnOpend && selPanel.isRunning();

		enableButtons(ebnOpend, ebnStartable, ebnStarted);

		selectButtons(selPanel);
	}

	private void enableButtons(boolean ebnOpend, boolean ebnStartable, boolean ebnStarted)
	{
		buttonClose.setEnabled(ebnOpend);
		// buttonLoad.setEnabled(true);
		buttonSave.setEnabled(ebnOpend);
		buttonStart.setEnabled(ebnStartable && !ebnStarted);
		buttonStop.setEnabled(ebnStartable && ebnStarted);
		buttonTreeView.setEnabled(ebnOpend);
		buttonGraphView.setEnabled(ebnOpend);
	}

	private void selectButtons(DefaultEbnPanel selPanel)
	{
		if (selPanel != null) {
			boolean treeViewFound = tabbedPane.isViewOfSameAgentExisting(selPanel, EbnTreePanel.class);
			boolean graphViewFound = tabbedPane.isViewOfSameAgentExisting(selPanel, EbnGraphPanel.class);

			buttonTreeView.setSelected(treeViewFound);
			buttonGraphView.setSelected(graphViewFound);
		} else {
			buttonTreeView.setSelected(false);
			buttonGraphView.setSelected(false);
		}
	}

	public boolean isButtonTreeViewSelected()
	{
		return buttonTreeView.isSelected();
	}

	public boolean isButtonGraphViewSelected()
	{
		return buttonGraphView.isSelected();
	}
}
