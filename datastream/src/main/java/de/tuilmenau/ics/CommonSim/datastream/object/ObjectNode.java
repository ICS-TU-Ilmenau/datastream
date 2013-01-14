package de.tuilmenau.ics.CommonSim.datastream.object;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Object node saving exactly one object of type Object.
 * 
 * @author Markus Brueckner
 */
public class ObjectNode extends AbstractSimpleObjectNode<Object> 
{
	Object      mValue;
	TimeBase<?> mTime;
	
	@Override
	public void write(Object value, TimeBase<?> time) 
	{
		mValue = value;
		mTime  = time;
		doWriteAll(value, time);
		doNotifyAll();
	}

	@Override
	public void reset() 
	{
		mValue = null;
		mTime  = null;
	}

	@Override
	public Object read() 
	{
		return mValue;
	}

	@Override
	public TimeBase<?> readTime() 
	{
		return mTime;
	}
	
	/**
	 * Open/create a writer for objects under the given name.
	 * 
	 * @param id The name to create the type under.
	 * @return A writer object suitable for writing to the node.
	 */
	public static IObjectWriter<Object> openAsWriter(String id)
	{
		return DatastreamManager.open(ObjectNode.class, id);
	}

	/**
	 * Open/create a reader for objects under the given name.
	 * 
	 * @param id The name to create the type under.
	 * @return A reader object suitable for reading objects from the node.
	 */
	public static IObjectReader<Object> openAsReader(String id)
	{
		return DatastreamManager.open(ObjectNode.class, id);
	}
}
