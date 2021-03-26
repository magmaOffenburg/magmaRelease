/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui;

import java.awt.BorderLayout;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import kdo.ebnDevKit.ebnAccess.EbnAccessFactory;
import kdo.ebnDevKit.gui.ebnGraphPanel.GraphConfiguration;

/**
 * EbnDevKit main panel
 * @author Thomas Rinklin
 */
public class EbnDevKit extends JPanel
{
	private static final long serialVersionUID = -6756767454758078432L;

	/** menu bar */
	private final MenuBar menuBar;

	/** tabbed pane for the ebn views */
	private final EbnTabbedPane tabbedPane;

	/** enclosing frame */
	private JFrame frame;

	public EbnDevKit(EbnAccessFactory ebnAccessFactory)
	{
		GraphConfiguration graphConfiguration = new GraphConfiguration();
		ApplicationActionListeners actionListeners =
				new ApplicationActionListeners(this, ebnAccessFactory, graphConfiguration);
		tabbedPane = new EbnTabbedPane();
		menuBar = new MenuBar(actionListeners, tabbedPane);
		actionListeners.setMenuBar(menuBar);
		actionListeners.setTabbedPane(tabbedPane);

		setLayout(new BorderLayout());
		add(menuBar, BorderLayout.NORTH);
		add(tabbedPane, BorderLayout.CENTER);

		addTabbedPaneChangeListener();

		menuBar.updateButtons();
	}

	private void addTabbedPaneChangeListener()
	{
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e)
			{
				menuBar.updateButtons();
			}
		});

		tabbedPane.addContainerListener(new ContainerListener() {
			@Override
			public void componentRemoved(ContainerEvent arg0)
			{
				menuBar.updateButtons();
			}

			@Override
			public void componentAdded(ContainerEvent arg0)
			{
				menuBar.updateButtons();
			}
		});
	}

	/**
	 * sets the enclosing frame
	 * @param frame enclosing frame
	 */
	public void setEnclosingFrame(JFrame frame)
	{
		this.frame = frame;
	}

	/**
	 * returns the enclosing frame
	 * @return enclosing frame
	 */
	public JFrame getFrame()
	{
		return this.frame;
	}
}
