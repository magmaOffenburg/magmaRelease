/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class GsonUtil
{
	private static Gson gson =
			new GsonBuilder()
					.registerTypeAdapter(Double.class,
							(JsonSerializer<Double>) (src, typeOfSrc, context) -> {
								if (src.isNaN() || src.isInfinite()) {
									return new JsonPrimitive(src.toString());
								}
								return new JsonPrimitive(
										new BigDecimal(src).setScale(3, RoundingMode.HALF_UP).doubleValue());
							})
					.registerTypeAdapter(Float.class,
							(JsonSerializer<Float>) (src, typeOfSrc, context) -> {
								if (src.isNaN() || src.isInfinite()) {
									return new JsonPrimitive(src.toString());
								}
								return new JsonPrimitive(
										new BigDecimal(src).setScale(3, RoundingMode.HALF_UP).floatValue());
							})
					.registerTypeAdapter(Duration.class,
							(JsonSerializer<Duration>) (src, typeOfSrc,
									context) -> new JsonPrimitive(DurationUtil.format(src)))
					.registerTypeAdapter(Duration.class,
							(JsonDeserializer<Duration>) (src, typeOfSrc,
									context) -> DurationUtil.parse(src.getAsString()))
					.registerTypeAdapter(IPose2D.class,
							(JsonDeserializer<IPose2D>) (jsonElement, type, jsonDeserializationContext) -> {
								JsonObject pose = jsonElement.getAsJsonObject();
								JsonObject angle = pose.get("angle").getAsJsonObject();
								return new Pose2D(pose.get("x").getAsDouble(), pose.get("y").getAsDouble(),
										Angle.rad(angle.get("angle").getAsDouble()));
							})
					.registerTypeAdapter(Rotation.class,
							(JsonDeserializer<Rotation>) (src, typeOfSrc, context) -> {
								Vector3D axis = context.deserialize(src.getAsJsonObject().get("axis"), Vector3D.class);
								if (axis != null) {
									double angle = src.getAsJsonObject().getAsJsonPrimitive("angle").getAsDouble();
									return new Rotation(
											axis, Math.toRadians(angle), RotationConvention.VECTOR_OPERATOR);
								}
								JsonObject gyro = src.getAsJsonObject();
								return new Rotation(gyro.get("q0").getAsDouble(), gyro.get("q1").getAsDouble(),
										gyro.get("q2").getAsDouble(), gyro.get("q3").getAsDouble(), false);
							})
					.registerTypeAdapter(IPose3D.class,
							(JsonDeserializer<IPose3D>) (src, typeOfSrc, context) -> {
								Vector3D position =
										context.deserialize(src.getAsJsonObject().get("position"), Vector3D.class);
								Rotation rotation =
										context.deserialize(src.getAsJsonObject().get("orientation"), Rotation.class);
								return new Pose3D(position, rotation);
							})
					.setPrettyPrinting()
					.serializeSpecialFloatingPointValues()
					.create();

	/**
	 * JSON-Serialization with pretty-printing, float / double rounding to 3
	 * decimals and support for special floating point values (NaN, infinity...).
	 */
	public static String toJson(Object src)
	{
		return gson.toJson(src);
	}

	public static <T> T fromJson(String src, Type typeOfT)
	{
		return gson.fromJson(src, typeOfT);
	}
}
