/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit;

import javax.swing.JFrame;
import javax.swing.UIManager;
import kdo.ebnDevKit.agent.IEbnAgentCreator;
import kdo.ebnDevKit.ebnAccess.EbnAccessFactory;
import kdo.ebnDevKit.gui.EbnDevKit;

/**
 * Main class for the EbnDevKit application
 * @author Thomas Rinklin
 *
 */
public class EbnDevKitStarter
{
	/**
	 * starts the application
	 */
	public void start(IEbnAgentCreator agentCreator)
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		EbnAccessFactory agentFactory = new EbnAccessFactory();
		agentFactory.setAgentCreator(agentCreator);

		EbnDevKit app = new EbnDevKit(agentFactory);

		JFrame frame = new JFrame();
		app.setEnclosingFrame(frame);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(app);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
}
