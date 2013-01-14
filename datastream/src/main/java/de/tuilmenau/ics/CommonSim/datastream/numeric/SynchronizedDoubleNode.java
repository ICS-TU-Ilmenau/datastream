package de.tuilmenau.ics.CommonSim.datastream.numeric;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Thread-safe version of the {@link DoubleNode} class. The class serves as a
 * buffer node that is intended to be read and written from multiple threads.
 * Use that for example to create nodes that are directly to be read from a 
 * GUI thread without getting corrupted results.
 * 
 * @author Markus Brueckner
 */
public class SynchronizedDoubleNode extends DoubleNode 
{
	@Override
	public synchronized void connect(IDoubleWriter successor) 
	{
		super.connect(successor);
	}
	
	@Override
	public synchronized void disconnect(IDoubleWriter successor) 
	{
		super.disconnect(successor);
	}
	
	@Override
	public synchronized void tick(TimeBase<?> time) 
	{
		super.write(1.0, time);
	}

	@Override
	public synchronized void write(double value, TimeBase<?> time) 
	{
		super.write(value, time);
	}

	@Override
	public synchronized double read() 
	{
		return super.read();
	}

	@Override
	public synchronized TimeBase<?> readTime() 
	{
		return super.readTime();
	}
	
	@Override
	public synchronized void reset() 
	{
		super.reset();
	}
	
	/**
	 * Open (or create) a writer under the given name for that node type. 
	 * 
	 * @param id The id for the node. Either the node under that id is retrieved or a new
	 * 				node of type SynchronizedDoubleNode is created, registered under the given name and
	 * 				returned.
	 * @return A writer suitable for writing doubles to a SynchronizedDoubleNode.
	 */
	public synchronized static IDoubleWriter openAsWriter(String id)
	{
		return open(id).getInput(null);
	}

	/**
	 * Open (or create) a reader under the given name for that node type. 
	 * 
	 * @param id The id for the node. Either the node under that id is retrieved or a new
	 * 				node of type SynchronizedDoubleNode is created, registered under the given name and
	 * 				returned.
	 * @return A writer suitable for reading doubles from a SynchronizedDoubleNode.
	 */
	public synchronized static IDoubleReader openAsReader(String id)
	{
		return open(id);
	}

	/**
	 * Open/create the node under the given name.
	 * 
	 * @param name The name to attach to the node. 
	 * @return The node under that name.
	 */
	public synchronized static AbstractDoubleNode open(String name) 
	{
		return DatastreamManager.open(SynchronizedDoubleNode.class, name);
	}
}
