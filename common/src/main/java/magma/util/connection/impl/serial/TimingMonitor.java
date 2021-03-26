/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.connection.impl.serial;

public class TimingMonitor
{
	private long numberOfCalls;

	private long sumOfCallTimes;

	private long minimumTime;

	private long maximumTime;

	private long maximumCallPeriod;

	private long lastCallTime;

	private long sumOfCallPeriods;

	private long lastOutTime;

	private float averageValue;

	private long startTime;

	private boolean printStats;

	public TimingMonitor(boolean printStats)
	{
		this.printStats = printStats;
		numberOfCalls = 0;
		sumOfCallTimes = 0;
		minimumTime = Long.MAX_VALUE;
		maximumTime = 0;
		maximumCallPeriod = 0;
		lastCallTime = 0;
		sumOfCallPeriods = 0;
		lastOutTime = 0;
		averageValue = 0;
		startTime = 0;
	}

	public void doBeforeHandling(float eventValue)
	{
		// Timing
		if (numberOfCalls > 0) {
			lastOutTime = System.nanoTime() - lastCallTime;

			sumOfCallPeriods += lastOutTime;

			if (lastOutTime > maximumCallPeriod) {
				maximumCallPeriod = lastOutTime;
			}
		}

		averageValue = (averageValue * numberOfCalls + eventValue) / (numberOfCalls + 1);

		numberOfCalls++;

		startTime = System.nanoTime();
	}

	public void doAfterHandling()
	{
		// Timing
		long callDuration = System.nanoTime() - startTime;
		sumOfCallTimes += callDuration;

		if (callDuration > maximumTime) {
			maximumTime = callDuration;
		}

		if (callDuration < minimumTime) {
			minimumTime = callDuration;
		}

		if (printStats && (numberOfCalls % 100) == 0) {
			System.out.printf("Past time for 100 calls is %4.3f: ", getAverageInTime());

			System.out.printf(" Maximum duration is: %f", getMaximumTime());

			System.out.printf(" Minimum duration is: %f", getMinimumTime());

			System.out.printf(" Average call periods is: %4.3f", getAverageOutTimes());

			System.out.printf(" Maximum Call period is: %4.3f", getMaximumOutTime());

			System.out.printf(" Last Call period is: %4.3f", getLastOutTime());

			System.out.printf(" Average event value is: %f4.3", averageValue);

			System.out.println("");
		}

		lastCallTime = System.nanoTime();
	}

	public float getMaximumOutTime()
	{
		return maximumCallPeriod / 1000000f;
	}

	public float getAverageOutTimes()
	{
		return (sumOfCallPeriods / (float) numberOfCalls) / 1000000;
	}

	public float getMinimumTime()
	{
		return minimumTime / 1000000f;
	}

	public float getMaximumTime()
	{
		return maximumTime / (float) 1000000;
	}

	public float getAverageInTime()
	{
		return (sumOfCallTimes / (float) numberOfCalls) / 1000000;
	}

	public float getLastOutTime()
	{
		return lastOutTime / 1000000;
	}
}
