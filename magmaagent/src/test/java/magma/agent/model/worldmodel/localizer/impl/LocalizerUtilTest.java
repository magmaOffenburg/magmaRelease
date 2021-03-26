/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.localizer.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hso.autonomy.agent.model.agentmodel.IFieldOfView;
import hso.autonomy.agent.model.agentmodel.impl.FieldOfView;
import hso.autonomy.agent.model.worldmodel.ILandmark;
import hso.autonomy.agent.model.worldmodel.impl.Landmark;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeature;
import hso.autonomy.agent.model.worldmodel.localizer.impl.LocalizerUtil;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magma.agent.IHumanoidConstants;
import magma.agent.model.worldmeta.IPointFeatureConfiguration;
import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import magma.agent.model.worldmeta.RCFieldConfigurationHelper;
import magma.agent.model.worldmodel.impl.RCSoccerField;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LocalizerUtilTest
{
	private IRoboCupWorldMetaModel metaModel;
	private RCSoccerField map;
	private IFieldOfView fov;
	@BeforeEach
	public void setUp()
	{
		metaModel = RCFieldConfigurationHelper.getRCFieldMetalModel(IHumanoidConstants.DEFAULT_FIELD_VERSION);
		// map
		Map<String, ILandmark> landmarks = new HashMap<>();
		if (metaModel.getLandmarks() != null) {
			for (IPointFeatureConfiguration pfc : metaModel.getLandmarks().values()) {
				landmarks.put(pfc.getName(), new Landmark(pfc.getName(), pfc.getType(), pfc.getKnownPosition()));
			}
		}
		map = new RCSoccerField(new HashMap<String, IPointFeature>(landmarks), null,
				(float) metaModel.getFieldDimensions().getX() / 2, (float) metaModel.getFieldDimensions().getY() / 2,
				(float) metaModel.getGoalDimensions().getX(), (float) metaModel.getGoalDimensions().getY() / 2,
				(float) metaModel.getGoalDimensions().getZ(), (float) metaModel.getPenaltyAreaDimensions().getX(),
				(float) metaModel.getPenaltyAreaDimensions().getY() / 2, metaModel.getMiddleCircleRadius());
		fov = new FieldOfView(Angle.deg(60), Angle.deg(60));
	}

	@Test
	public void testExtractPlausibleFeatures_AngleZero()
	{
		String[] plausibleLandmarks = new String[] {"Golg", "Tola", "Lolf", "Lola"};
		IPose3D robotPose = new Pose3D(new Vector3D(5, 2.5, 1.6), Angle.ZERO);
		List<IPointFeature> result =
				LocalizerUtil.extractPlausiblePointFeatures(map.getPointFeatures().values(), robotPose, fov, 5);

		assertEquals(plausibleLandmarks.length, result.size());
		for (int i = 0; i < plausibleLandmarks.length; i++) {
			String plausibleLandmark = plausibleLandmarks[i];
			assertEquals(plausibleLandmark, result.get(i).getName());
		}
	}

	@Test
	public void testExtractPlausibleFeatures_Angle180()
	{
		String[] plausibleLandmarks = new String[] {"Gsrg", "Tsra", "Lsra", "Lsrf"};
		IPose3D robotPose = new Pose3D(new Vector3D(-5, -2.5, 1.6), Angle.ANGLE_180);
		List<IPointFeature> result =
				LocalizerUtil.extractPlausiblePointFeatures(map.getPointFeatures().values(), robotPose, fov, 5);

		assertEquals(plausibleLandmarks.length, result.size());
		for (int i = 0; i < plausibleLandmarks.length; i++) {
			String plausibleLandmark = plausibleLandmarks[i];
			assertEquals(plausibleLandmark, result.get(i).getName());
		}
	}

	@Test
	public void testExtractPlausibleFeatures_Angle90()
	{
		String[] plausibleLandmarks = new String[] {"Xclc", "Tclf"};
		IPose3D robotPose = new Pose3D(new Vector3D(0, 0, 1.6), Angle.ANGLE_90);
		List<IPointFeature> result =
				LocalizerUtil.extractPlausiblePointFeatures(map.getPointFeatures().values(), robotPose, fov, 5);

		assertEquals(plausibleLandmarks.length, result.size());
		for (int i = 0; i < plausibleLandmarks.length; i++) {
			String plausibleLandmark = plausibleLandmarks[i];
			assertEquals(plausibleLandmark, result.get(i).getName());
		}
	}
}