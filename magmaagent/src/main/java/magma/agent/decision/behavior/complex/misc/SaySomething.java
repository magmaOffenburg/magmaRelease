/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.misc;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.behavior.complex.SingleComplexBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import magma.agent.decision.behavior.IBehaviorConstants;

/**
 * Behavior for speech output.
 *
 * @author Stephan Kammerer
 */
public class SaySomething extends SingleComplexBehavior
{
	public SaySomething(IThoughtModel thoughtModel, BehaviorMap behaviors, String filename)
	{
		super(IBehaviorConstants.SAY_SOMETHING, thoughtModel, behaviors);
		new MP3Player(filename).start();
	}

	@Override
	public IBehavior decideNextBasicBehavior()
	{
		// TODO: Call next behavior
		return behaviors.get(IBehavior.NONE);
	}

	/**
	 * MP3 Player for sound output (JLayer framework)
	 */
	public class MP3Player extends Thread
	{
		private Player p;

		/**
		 * @param filename Filename of a MP3 input-file
		 */
		public MP3Player(String filename)
		{
			try {
				FileInputStream in = new FileInputStream(filename);

				// Player-instance
				p = new Player(in);

			} catch (JavaLayerException | FileNotFoundException jle) {
				System.err.println("Error: " + jle);
			}
		}

		@Override
		public void run()
		{
			try {
				p.play();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
		}
	}
}
