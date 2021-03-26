/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.scenegraph.impl;

import magma.util.scenegraph.IBaseNode;
import magma.util.scenegraph.ILightNode;
import magma.util.scenegraph.NodeType;

public class LightNode extends BaseNode implements ILightNode
{
	private float[] diffuse;

	private float[] ambient;

	private float[] specular;

	public LightNode()
	{
	}

	@Override
	public NodeType getNodeType()
	{
		return NodeType.LIGHT;
	}

	@Override
	public float[] getDiffuse()
	{
		return diffuse;
	}

	@Override
	public float[] getAmbient()
	{
		return ambient;
	}

	@Override
	public float[] getSpecular()
	{
		return specular;
	}

	public void setDiffuse(float[] diffuse)
	{
		this.diffuse = diffuse;
	}

	public void setAmbient(float[] ambient)
	{
		this.ambient = ambient;
	}

	public void setSpecular(float[] specular)
	{
		this.specular = specular;
	}

	@Override
	public void update(IBaseNode other)
	{
		if (other.getNodeType() == NodeType.LIGHT) {
			ILightNode newLight = (ILightNode) other;

			if (newLight.getDiffuse() != null) {
				diffuse = newLight.getDiffuse();
			}

			if (newLight.getAmbient() != null) {
				ambient = newLight.getAmbient();
			}

			if (newLight.getSpecular() != null) {
				specular = newLight.getSpecular();
			}
		}
	}

	@Override
	public <T extends IBaseNode> T getNode(Class<T> nodeType, String property, String value)
	{
		return super.getNode(nodeType, property, value);
	}

	@Override
	public String toString()
	{
		String ret = "(" + getNodeType();
		ret += " diffuse=" + diffuse;
		ret += " ambient=" + ambient;
		ret += " specular=" + specular;

		return ret + ")";
	}
}
