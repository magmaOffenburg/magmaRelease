/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.localizer.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hso.autonomy.agent.model.worldmodel.impl.Landmark;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureLocalizer;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureMap;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeature;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeature;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeatureObservation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magma.agent.model.worldmeta.IPointFeatureConfiguration;
import magma.agent.model.worldmeta.impl.RCServerMetaModelV62;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link hso.autonomy.agent.model.worldmodel.localizer.IPointFeature}
 *
 * @author Klaus Dorer, Stefan Glaser
 */
public class LocalizerBaseTest
{
	IFeatureLocalizer testee;

	IFeatureMap map;

	IPointFeatureObservation G1LObs;

	IPointFeatureObservation G2LObs;

	IPointFeatureObservation G1RObs;

	IPointFeatureObservation G2RObs;

	IPointFeatureObservation F1LObs;

	IPointFeatureObservation F2LObs;

	IPointFeatureObservation F1RObs;

	IPointFeatureObservation F2RObs;

	float height = 0;

	Rotation orientationEstimation = null;

	Map<String, IPointFeature> flags = null;

	Map<String, ILineFeature> fieldLines = null;

	List<IPointFeatureObservation> pointObservations = null;

	List<ILineFeatureObservation> lineObservations = null;

	@BeforeEach
	public void setUp()
	{
		Map<String, IPointFeatureConfiguration> landmarks = RCServerMetaModelV62.INSTANCE.getLandmarks();
		flags = new HashMap<>();
		fieldLines = new HashMap<>();

		for (IPointFeatureConfiguration pfc : landmarks.values()) {
			flags.put(pfc.getName(), new Landmark(pfc.getName(), pfc.getType(), pfc.getKnownPosition()));
		}

		G1LObs = mock(IPointFeatureObservation.class);
		when(G1LObs.getName()).thenReturn("G1L");
		when(G1LObs.hasDepth()).thenReturn(true);

		G2LObs = mock(IPointFeatureObservation.class);
		when(G2LObs.getName()).thenReturn("G2L");
		when(G2LObs.hasDepth()).thenReturn(true);

		G1RObs = mock(IPointFeatureObservation.class);
		when(G1RObs.getName()).thenReturn("G1R");
		when(G1RObs.hasDepth()).thenReturn(true);

		G2RObs = mock(IPointFeatureObservation.class);
		when(G2RObs.getName()).thenReturn("G2R");
		when(G2RObs.hasDepth()).thenReturn(true);

		F1LObs = mock(IPointFeatureObservation.class);
		when(F1LObs.getName()).thenReturn("F1L");
		when(F1LObs.hasDepth()).thenReturn(true);

		F2LObs = mock(IPointFeatureObservation.class);
		when(F2LObs.getName()).thenReturn("F2L");
		when(F2LObs.hasDepth()).thenReturn(true);

		F1RObs = mock(IPointFeatureObservation.class);
		when(F1RObs.getName()).thenReturn("F1R");
		when(F1RObs.hasDepth()).thenReturn(true);

		F2RObs = mock(IPointFeatureObservation.class);
		when(F2RObs.getName()).thenReturn("F2R");
		when(F2RObs.hasDepth()).thenReturn(true);

		map = mock(IFeatureMap.class);

		pointObservations = new ArrayList<>();
		lineObservations = new ArrayList<>();

		when(map.getPointFeatures()).thenReturn(flags);
		when(map.getLineFeatures()).thenReturn(fieldLines);
	}

	@Test
	public void emptyDummyTestToKeepJenkinsHappy() throws Exception
	{
	}
}
