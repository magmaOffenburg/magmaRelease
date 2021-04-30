package magma.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.impl.Effector;

public class ProxyEffector extends Effector
{
	private float[] values;

	public ProxyEffector(String name, float[] values)
	{
		super(name);
		this.values = values;
	}

	public float[] getValues()
	{
		return this.values;
	}
}
