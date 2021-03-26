/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui;

import java.awt.Component;
import javax.swing.JTabbedPane;

/**
 * TabbedPane for the EbnDevKit Application
 * @author Thomas Rinklin
 */
public class EbnTabbedPane extends JTabbedPane
{
	private static final long serialVersionUID = -8956990044310904469L;

	/**
	 * returns a view the same agent as the passed panel and with a class that is
	 * assignable to the passed class-object
	 * @param panelOfAgent panel of the agent against the others will be checked
	 * @param clazz clazz type of view
	 * @return view with the same agent as the passed view and a type which is
	 *         assignable to the passed class. if no matching view exists, null
	 *         will be returned
	 */
	public DefaultEbnPanel getViewOfSameAgent(DefaultEbnPanel panelOfAgent, Class<? extends DefaultEbnPanel> clazz)
	{
		Component[] components = getComponents();
		for (Component component : components) {
			if (component instanceof DefaultEbnPanel) {
				DefaultEbnPanel panel = (DefaultEbnPanel) component;

				if (panel.isSameAgent(panelOfAgent) && (clazz.isAssignableFrom(panel.getClass()))) {
					return panel;
				}
			}
		}
		return null;
	}

	/**
	 * checks if a specific view of the same agent is existing
	 * @param panelOfAgent panel of the agent against the others will be checked
	 * @param clazz type of view
	 * @return true if a view with the same agent exists and the type is
	 *         assignable to the passed class
	 */
	public boolean isViewOfSameAgentExisting(DefaultEbnPanel panelOfAgent, Class<? extends DefaultEbnPanel> clazz)
	{
		return getViewOfSameAgent(panelOfAgent, clazz) != null;
	}

	/**
	 * returns the selected view panel
	 * @return the selected view panel
	 */
	public DefaultEbnPanel getSelectedEbnPanel()
	{
		Component comp = getSelectedComponent();
		if (comp instanceof DefaultEbnPanel) {
			return ((DefaultEbnPanel) comp);
		}
		return null;
	}

	/**
	 * adds a ebn panel to the tabbed pane
	 */
	public void addToTabbedPane(DefaultEbnPanel panel)
	{
		add(panel);
	}

	/**
	 * removes a ebn panel from the tabbed pane
	 */
	public void removeFromTabbedPane(DefaultEbnPanel panel)
	{
		remove(panel);
	}
}
