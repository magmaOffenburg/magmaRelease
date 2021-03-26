/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.worldmodel.localizer.IFeatureMap;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeature;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeature;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implements a simple map of geometric features.
 *
 * @author Stefan Glaser
 *
 */
public class FeatureMap implements IFeatureMap
{
	/** The map of point features in the environment. */
	protected Map<String, IPointFeature> pointFeatures;

	/** The map of line features in the environment. */
	protected Map<String, ILineFeature> lineFeatures;

	/**
	 * Construct a new feature map with no initial features.
	 */
	public FeatureMap()
	{
		this(null, null);
	}

	/**
	 * Construct a new feature map from the given point and line features.
	 *
	 * @param pointFeatures the initially known point features
	 * @param lineFeatures the initially known line features
	 */
	public FeatureMap(Map<String, IPointFeature> pointFeatures, Map<String, ILineFeature> lineFeatures)
	{
		this.pointFeatures = pointFeatures != null ? pointFeatures : new HashMap<String, IPointFeature>();
		this.lineFeatures = lineFeatures != null ? lineFeatures : new HashMap<String, ILineFeature>();
	}

	@Override
	public Map<String, IPointFeature> getPointFeatures()
	{
		return pointFeatures;
	}

	@Override
	public Collection<IPointFeature> getPointFeatures(String type)
	{
		return pointFeatures.values()
				.stream()
				.filter(f -> type == f.getType() || type.equals(f.getType()) || f.getType().contains(type))
				.collect(Collectors.toList());
	}

	@Override
	public IPointFeature getPointFeature(String name)
	{
		return pointFeatures.get(name);
	}

	@Override
	public void setPointFeatures(Map<String, IPointFeature> pointFeatures)
	{
		if (pointFeatures != null) {
			this.pointFeatures = pointFeatures;
		} else {
			this.pointFeatures.clear();
		}
	}

	@Override
	public void addPointFeature(IPointFeature pointFeature)
	{
		if (pointFeature != null) {
			pointFeatures.put(pointFeature.getName(), pointFeature);
		}
	}

	@Override
	public void clearPointFeatures()
	{
		pointFeatures.clear();
	}

	@Override
	public Map<String, ILineFeature> getLineFeatures()
	{
		return lineFeatures;
	}

	@Override
	public Collection<ILineFeature> getLineFeatures(String type)
	{
		return lineFeatures.values()
				.stream()
				.filter(f -> type == f.getType() || type.equals(f.getType()))
				.collect(Collectors.toList());
	}

	@Override
	public ILineFeature getLineFeature(String name)
	{
		return lineFeatures.get(name);
	}

	@Override
	public void setLineFeatures(Map<String, ILineFeature> lineFeatures)
	{
		if (lineFeatures != null) {
			this.lineFeatures = lineFeatures;
		} else {
			this.lineFeatures.clear();
		}
	}

	@Override
	public void addLineFeature(ILineFeature lineFeature)
	{
		if (lineFeature != null) {
			lineFeatures.put(lineFeature.getName(), lineFeature);
		}
	}

	@Override
	public void clearLineFeatures()
	{
		lineFeatures.clear();
	}

	@Override
	public void clear()
	{
		clearPointFeatures();
		clearLineFeatures();
	}
}
