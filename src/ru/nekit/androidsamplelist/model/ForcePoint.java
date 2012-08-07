package ru.nekit.androidsamplelist.model;

import org.puremvc.java.interfaces.IProxy;
import org.puremvc.java.patterns.proxy.Proxy;

public class ForcePoint extends Proxy implements IProxy {

	public static final String NAME = "forcePoint";

	public static float threshold_max;

	public float x;
	public float y;
	public float z;
	public double force;

	public ForcePoint() {
		super(NAME);

	}

	public void round(int i) {
		int roundFactor = (int)Math.pow(10, i);
		x = (float)Math.round(x * roundFactor) / roundFactor;
		y = (float)Math.round(y * roundFactor) / roundFactor;
		z = (float)Math.round(z * roundFactor) / roundFactor;
		force = (double)Math.round(force * roundFactor) / roundFactor;

	}
}
