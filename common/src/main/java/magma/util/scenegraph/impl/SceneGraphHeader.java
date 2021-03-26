/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.scenegraph.impl;

import magma.util.scenegraph.ISceneGraphHeader;

public class SceneGraphHeader implements ISceneGraphHeader
{
	private String type;

	private int majorVersion;

	private int minorVersion;

	public SceneGraphHeader()
	{
	}

	public SceneGraphHeader(ISceneGraphHeader other)
	{
		this.type = other.getType();
		this.majorVersion = other.getMajorVersion();
		this.minorVersion = other.getMinorVersion();
	}

	public SceneGraphHeader(String type, int majorVersion, int minorVersion)
	{
		this.type = type;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Override
	public int getMajorVersion()
	{
		return majorVersion;
	}

	@Override
	public int getMinorVersion()
	{
		return minorVersion;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setMajorVersion(int majorVersion)
	{
		this.majorVersion = majorVersion;
	}

	public void setMinorVersion(int minorVersion)
	{
		this.minorVersion = minorVersion;
	}

	@Override
	public void update(ISceneGraphHeader other)
	{
		setType(other.getType());
		setMajorVersion(other.getMajorVersion());
		setMinorVersion(other.getMinorVersion());
	}
}
