package de.tuilmenau.ics.CommonSim.datastream.numeric;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Node with a counter, which can be incremented or decremented
 * in steps of one. 
 * 
 * @author Florian Liers
 */
public class CounterNode extends DoubleNode 
{
	@Override
	public void tick(TimeBase<?> time) 
	{
		write(+1.0, time);
	}

	@Override
	public synchronized void write(double value, TimeBase<?> time) 
	{
		if(value < 0) {
			super.write(read() -1.0, time);
		}
		else if(value > 0) {
			super.write(read() +1.0, time);
		}
		// else value == 0: do nothing
	}

	@Override
	public synchronized void reset() 
	{
		super.reset();
	}
	
	public static IDoubleWriter openAsWriter(String id)
	{
		return DatastreamManager.open(CounterNode.class, id);
	}

	public static IDoubleReader openAsReader(String id)
	{
		return DatastreamManager.open(CounterNode.class, id);
	}

}
