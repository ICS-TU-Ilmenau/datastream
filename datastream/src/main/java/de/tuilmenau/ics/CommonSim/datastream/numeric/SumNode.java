package de.tuilmenau.ics.CommonSim.datastream.numeric;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Double node summing up all input values.
 * 
 * @author Markus Brueckner
 */
public class SumNode extends DoubleNode 
{
	@Override 
	public void write(double value, TimeBase<?> time)
	{
		super.write(super.read()+value, time);
	}
	
	/**
	 * Open (or create) a writer under the given name for that node type. 
	 * 
	 * @param id The id for the node. Either the node under that id is retrieved or a new
	 * 				node of type DoubleNode is created, registered under the given name and
	 * 				returned.
	 * @return A writer suitable for writing doubles to a DoubleSumNode.
	 */
	public static IDoubleWriter openAsWriter(String id)
	{
		return DatastreamManager.open(SumNode.class, id);
	}

	/**
	 * Open (or create) a writer under the given name for that node type. 
	 * 
	 * @param id The id for the node. Either the node under that id is retrieved or a new
	 * 				node of type DoubleNode is created, registered under the given name and
	 * 				returned.
	 * @return A writer suitable for writing doubles to a DoubleSumNode.
	 */
	public static IDoubleReader openAsReader(String id)
	{
		return DatastreamManager.open(SumNode.class, id);
	}
}
