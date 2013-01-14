package de.tuilmenau.ics.CommonSim.datastream.occurrences;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;
import de.tuilmenau.ics.CommonSim.datastream.object.AbstractSimpleObjectNode;

/**
 * Simple occurrence node saving exactly one occurrence. This node does not
 * do any filtering but merely saves every occurrence published to it.
 * 
 * @author Markus Brueckner
 */
public class OccurrenceNode extends AbstractSimpleObjectNode<IOccurrence> 
{
	IOccurrence mOccurrence;
	TimeBase<?> mTime;
	
	@Override
	public void write(IOccurrence occurrence, TimeBase<?> time) 
	{
		mOccurrence = occurrence;
		mTime = time;
		doWriteAll(occurrence, time);
		doNotifyAll();
	}

	@Override
	public void reset() 
	{
		mOccurrence = null;
		mTime = null;
	}

	@Override
	public IOccurrence read() 
	{
		return mOccurrence;
	}

	@Override
	public TimeBase<?> readTime() 
	{
		return mTime;
	}

	/**
	 * Open the OccurrenceNode under the given name.
	 * 
	 * @param name The name of the OccurrenceNode. If no such name is registered, a new node is created.
	 * @return The occurrence node.
	 */
	public static OccurrenceNode open(String name)
	{
		return DatastreamManager.open(OccurrenceNode.class, name);
	}
}
