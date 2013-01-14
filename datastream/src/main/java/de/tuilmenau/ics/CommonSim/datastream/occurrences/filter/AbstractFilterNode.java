package de.tuilmenau.ics.CommonSim.datastream.occurrences.filter;

import de.tuilmenau.ics.CommonSim.datastream.TimeBase;
import de.tuilmenau.ics.CommonSim.datastream.object.AbstractSimpleObjectNode;
import de.tuilmenau.ics.CommonSim.datastream.occurrences.IOccurrence;

/**
 * Abstract base class for occurrence filters that releases the filter developer
 * of most of the recurring work when writing a filter. 
 * 
 * @author Markus Brueckner
 */
public abstract class AbstractFilterNode extends AbstractSimpleObjectNode<IOccurrence> 
{
	private TimeBase<?> mTime;
	private IOccurrence mOccurrence;
	
	@Override
	public void write(IOccurrence occurrence, TimeBase<?> time) 
	{
		if (pass(occurrence, time)) {
			mTime = time;
			mOccurrence = occurrence;
			doWriteAll(occurrence, time);
			doNotifyAll();
		}
	}
	
	@Override
	public TimeBase<?> readTime() 
	{
		return mTime;
	}
	
	@Override
	public IOccurrence read() 
	{
		return mOccurrence;
	}
	
	@Override
	public void reset() 
	{
		mTime = null;
		mOccurrence = null;
	}
	
	/**
	 * Filter function implemented by deriving filters. The implemented filters get passed
	 * the occurrence and time under examination and decide whether this occurrence is to be
	 * filtered. If this method returns true, the occurrence is saved in the node and written through
	 * to all successors, as well as all observers are informed. If this method returns false, the
	 * occurrence is quietly dropped.
	 * 
	 * @param occurrence The occurrence under examination
	 * @param time The time at which this occurrence took place
	 * @return true if the occurrence is to pass the filter, false otherwise
	 */
	protected abstract boolean pass(IOccurrence occurrence, TimeBase<?> time);
}
