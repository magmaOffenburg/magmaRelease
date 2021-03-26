/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel;

import hso.autonomy.agent.model.worldmodel.localizer.IPointFeature;

/**
 * Base interface for all Landmarks on the filed (goal posts, flags)
 * @author Klaus Dorer, Stefan Glaser
 */
public interface ILandmark extends IVisibleObject, IPointFeature {
}