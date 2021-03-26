/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import kdo.ebnDevKit.ebnAccess.IEbnAccess;
import kdo.ebnDevKit.gui.DefaultEbnPanel;
import kdo.ebnDevKit.gui.ModifierActionListeners;
import kdo.ebnDevKit.gui.PopupMenuBuilder;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.GuiElements;

/**
 * ebn view component which displays the ebn graphically
 * @author Thomas Rinklin
 *
 */
public class EbnGraphPanel extends DefaultEbnPanel
{
	private static final long serialVersionUID = -6264330281010092566L;

	/** reference to the model */
	private final GuiElements guiElements;

	private final DrawingArea da;

	/**
	 * constructor
	 * @param parent parent JFrame (for displaying modal dialogs)
	 * @param ebnAccess reference to the model
	 * @param graphConf configuration
	 * @param enableModification enable modification features
	 */
	public EbnGraphPanel(JFrame parent, IEbnAccess ebnAccess, GraphConfiguration graphConf, boolean enableModification)
	{
		super(parent, ebnAccess);
		ModifierActionListeners modActions = new ModifierActionListeners(ebnAccess, parent);
		PopupMenuBuilder popupMenuBuilder = null;
		if (enableModification)
			popupMenuBuilder = new PopupMenuBuilder(modActions, ebnAccess);
		guiElements = new GuiElements(popupMenuBuilder);
		da = new DrawingArea(guiElements, graphConf, popupMenuBuilder);
		this.add(new JScrollPane(da));
	}

	@Override
	protected void ebnValuesChanged()
	{
		guiElements.refreshAllValues();
		repaint();
	}

	@Override
	protected void ebnStructureChanged()
	{
		guiElements.update(ebnAccess);
		guiElements.refreshAllValues();
		repaint();
	}

	@Override
	public void start()
	{
		super.start();
	}

	@Override
	public void stop()
	{
		super.stop();
	}
}
