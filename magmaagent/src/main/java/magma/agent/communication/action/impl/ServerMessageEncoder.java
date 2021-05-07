/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.agent.communication.action.IEffectorMap;
import hso.autonomy.agent.communication.action.IHingeEffector;
import hso.autonomy.agent.communication.action.IMessageEncoder;
import hso.autonomy.util.geometry.IPose2D;
import magma.agent.communication.action.IBeamEffector;
import magma.agent.communication.action.IPassEffector;
import magma.agent.communication.action.ISayEffector;

/**
 * Encapsulation of the protocol to simspark server
 * @author kdorer
 */
public class ServerMessageEncoder implements IMessageEncoder
{
	@Override
	public byte[] encodeMessage(IEffectorMap effectors)
	{
		StringBuilder builder = new StringBuilder(400);
		for (IEffector effector : effectors.values()) {
			if (effector instanceof IHingeEffector) {
				IHingeEffector hinge = (IHingeEffector) effector;
				builder.append("(").append(hinge.getName());
				builder.append(" ").append(truncate(hinge.getSpeed())).append(")");

			} else if (effector instanceof IBeamEffector) {
				IPose2D beam = ((IBeamEffector) effector).getBeam();
				builder.append("(beam ")
						.append(beam.getX())
						.append(" ")
						.append(beam.getY())
						.append(" ")
						.append(beam.getAngle().degrees())
						.append(")");

			} else if (effector instanceof ISayEffector) {
				String message = ((ISayEffector) effector).getMessage();
				if (message.length() > 0) {
					builder.append("(say ").append(message).append(")");
				}
			} else if (effector instanceof IPassEffector) {
				builder.append("(pass)");
			} else if (effector instanceof ProxyEffector) {
				encodeProxy(builder, (ProxyEffector) effector);
			}
		}

		builder.append("(syn)");

		return builder.toString().getBytes();
	}

	public void encodeProxy(StringBuilder builder, ProxyEffector proxyEffector)
	{
		builder.append("(proxy ").append(proxyEffector.getName());
		for (float value : proxyEffector.getValues()) {
			builder.append(" ").append(truncate(value));
		}
		builder.append(")");
	}

	/**
	 * Truncates to three digits
	 * @param value the value to truncate
	 * @return truncated float
	 */
	private float truncate(float value)
	{
		int i = (int) (value * 1000.0);
		return i / 1000.0f;
	}
}
