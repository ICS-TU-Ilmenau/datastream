package de.tuilmenau.ics.CommonSim.datastream.numeric;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Node implementing the division of two values. The node provides two
 * inputs: "dividend" and "divisor". The node's output is the quotient
 * of the values last written at the inputs (so it changes if one of the 
 * inputs is written. Write time is the time of the last input change.
 * If the divisor is 0.0 then the read value returns positive infinity, negative
 * infinity or NaN depending on the value of the dividend.
 * 
 * @author Markus Brueckner
 */
public class DivNode extends AbstractComplexDoubleNode 
{
	private double      mDividend;
	private double      mDivisor;
	private TimeBase<?> mTime;
	
	@Override
	protected void doWrite(String name, double value, TimeBase<?> time) 
	{
		if (name.equals("dividend")) {
			mDividend = value;
		}
		else {
			mDivisor = value;
		}
		mTime = time;
		doWriteAll(getValue(), mTime);
		doNotifyAll();
	}

	@Override
	public void reset() 
	{
		mDividend = 0.0;
		mDivisor  = 0.0;
		mTime     = null;
	}

	@Override
	public double read() 
	{
		return getValue();
	}

	@Override
	public TimeBase<?> readTime() 
	{
		return mTime;
	}

	/**
	 * Little helper to avoid code duplication when calculating the return value of the node.
	 * 
	 * @return The calculated value of the node.
	 */
	private double getValue()
	{
		if (mDivisor == 0.0)
		{
			// return positive or negative infinity depending on the number
			if (mDividend > 0) {
				return Double.POSITIVE_INFINITY;
			}
			if (mDividend < 0) {
				return Double.NEGATIVE_INFINITY;
			}
			return Double.NaN;
		}
		return mDividend/mDivisor;
	}
	
	/**
	 * Get a writer for an input of the node with the given id.
	 * 
	 * @param name A name of the form "node/input". 
	 * @return An IDoubleWriter suitable for writing to that input.
	 */
	public static IDoubleWriter openAsWriter(String name)
	{
		return AbstractComplexDoubleNode.openAsWriter(DivNode.class, name);
	}
	
	/**
	 * Get a reader for reading values from this node.
	 * 
	 * @param name The name of the node.
	 * @return An IDoubleReader suitable for reading from this node.
	 */
	public static IDoubleReader openAsReader(String name)
	{
		return DatastreamManager.open(DivNode.class, name);
	}
}
