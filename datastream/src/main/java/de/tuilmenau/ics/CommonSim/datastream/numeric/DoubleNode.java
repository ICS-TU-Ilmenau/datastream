package de.tuilmenau.ics.CommonSim.datastream.numeric;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Node saving a double value. This is the simplest double node just saving one
 * double value along with the write time. 
 * 
 * @author Markus Brueckner
 */
public class DoubleNode extends AbstractSimpleDoubleNode 
{
	private double      mValue;
	private TimeBase<?> mTime;

	@Override
	public void tick(TimeBase<?> time) 
	{
		write(1.0, time);
	}

	@Override
	public void write(double value, TimeBase<?> time) 
	{
		mValue = value;
		mTime  = time;
		doWriteAll(mValue, mTime);
		doNotifyAll();
	}

	@Override
	public double read() 
	{
		return mValue;
	}

	@Override
	public TimeBase<?> readTime() 
	{
		return mTime;
	}
	
	@Override
	public void reset() 
	{
		mValue = 0;
		mTime  = null;
	}
	
	/**
	 * Open (or create) a writer under the given name for that node type. 
	 * 
	 * @param id The id for the node. Either the node under that id is retrieved or a new
	 * 				node of type DoubleNode is created, registered under the given name and
	 * 				returned.
	 * @return A writer suitable for writing doubles to a DoubleNode.
	 */
	public static IDoubleWriter openAsWriter(String id)
	{
		return open(id).getInput(null);
	}

	/**
	 * Open (or create) a reader under the given name for that node type. 
	 * 
	 * @param id The id for the node. Either the node under that id is retrieved or a new
	 * 				node of type DoubleNode is created, registered under the given name and
	 * 				returned.
	 * @return A writer suitable for reading doubles from a DoubleNode.
	 */
	public static IDoubleReader openAsReader(String id)
	{
		return open(id);
	}

	/**
	 * Open/create the node under the given name.
	 * 
	 * @param name The name to attach to the node. 
	 * @return The node under that name.
	 */
	public static AbstractDoubleNode open(String name) 
	{
		return DatastreamManager.open(DoubleNode.class, name);
	}
}
