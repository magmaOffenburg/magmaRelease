/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.strategies;

import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.impl.roles.Allrounder;
import magma.agent.model.thoughtmodel.strategy.impl.roles.FieldArea;
import magma.agent.model.thoughtmodel.strategy.impl.roles.ManToManMarker;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class AllrounderStrategy extends BaseStrategy
{
	public static final String NAME = "Allrounder";

	public AllrounderStrategy(IRoboCupThoughtModel thoughtModel)
	{
		super(NAME, thoughtModel);

		// defender
		Allrounder defenderLower = new Allrounder("defenderLower", thoughtModel,
				new Vector2D(-worldModel.fieldHalfLength() * 2 / 3, -3), 10, -worldModel.fieldHalfLength() + 2, 0);

		Allrounder defenderUpper = new Allrounder("defenderUpper", thoughtModel,
				new Vector2D(-worldModel.fieldHalfLength() * 2 / 3, 3), 9, -worldModel.fieldHalfLength() + 2, 0);

		availableRoles.add(new ManToManMarker(defenderUpper, FieldArea.UPPER));
		availableRoles.add(new ManToManMarker(defenderLower, FieldArea.LOWER));

		// midfield
		availableRoles.add(new Allrounder("midfieldLower", thoughtModel, new Vector2D(0, -3), 2,
				-worldModel.fieldHalfLength() / 2, worldModel.fieldHalfLength() / 2));
		availableRoles.add(new Allrounder("midfieldLowerBack", thoughtModel,
				new Vector2D(-worldModel.fieldHalfLength() * 1 / 5, -4), 5, -worldModel.fieldHalfLength() / 2 - 3,
				worldModel.fieldHalfLength() / 2 - 3));
		availableRoles.add(new Allrounder("midfieldUpperBack", thoughtModel,
				new Vector2D(-worldModel.fieldHalfLength() * 1 / 5, 4), 4, -worldModel.fieldHalfLength() / 2 - 3,
				worldModel.fieldHalfLength() / 2 - 3));
		availableRoles.add(new Allrounder("midfieldUpper", thoughtModel, new Vector2D(0, 3), 3,
				-worldModel.fieldHalfLength() / 2, worldModel.fieldHalfLength() / 2));
		availableRoles.add(new Allrounder("midfieldCenter", thoughtModel, new Vector2D(0, 0), 0,
				-worldModel.fieldHalfLength() / 2, worldModel.fieldHalfLength() / 2));

		// wing
		availableRoles.add(new Allrounder("wingLower", thoughtModel,
				new Vector2D(worldModel.fieldHalfLength() * 1 / 3, -3), 6, 0, worldModel.fieldHalfLength() - 5));
		availableRoles.add(new Allrounder("wingUpper", thoughtModel,
				new Vector2D(worldModel.fieldHalfLength() * 1 / 3, 3), 7, 0, worldModel.fieldHalfLength() - 5));

		// attacker
		availableRoles.add(new Allrounder("attacker", thoughtModel,
				new Vector2D(worldModel.fieldHalfLength() * 1 / 2, 0), 8, 0, worldModel.fieldHalfLength() - 2.8));
	}
}
