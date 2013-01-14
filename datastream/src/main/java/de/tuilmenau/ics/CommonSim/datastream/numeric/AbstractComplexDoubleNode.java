package de.tuilmenau.ics.CommonSim.datastream.numeric;

import java.util.HashMap;
import java.util.Map;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.StreamException;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Abstract base class for nodes having more than one input. This class provides all the infrastructure neccessary to
 * handle observers etc. It also handles the stuff necessary to cope with multiple named inputs. Implementers of
 * nodes with multiple inputs should derive from this class to save boiler-plate code.
 * 
 * @author Markus Brueckner
 */
public abstract class AbstractComplexDoubleNode extends AbstractDoubleNode
{
	Map<String, IDoubleWriter> mInputs;
	String[] mInputsCache;
	
	@Override
	public String[] getInputNames() 
	{
		// working with caching here to avoid excessive allocations
		if (mInputsCache == null) 
		{
			mInputsCache = mInputs.keySet().toArray(new String[mInputs.size()]);
		}
		return mInputsCache;
	}
	
	/**
	 * Helper class providing a binding between a name and an input
	 */
	protected class NamedInput implements IDoubleWriter
	{
		private String mName;
		
		/**
		 * create a new named input with the given name.
		 * 
		 * @param name The name of the input.
		 */
		public NamedInput(String name)
		{
			mName = name;
		}
		
		@Override
		public void tick(TimeBase<?> time) 
		{
			write(1.0, time);
		}
		
		@Override
		public void write(double value, TimeBase<?> time) 
		{
			AbstractComplexDoubleNode.this.doWrite(mName, value, time);
		}
	}
	
	/**
	 * Get a named input of the node. The call will return a helper object that
	 * represents the named input of the node. Writes to this object will appear
	 * inside the node as coming from that name.
	 * 
	 * @param name The name of the input to return. If there's no such name, a StreamException
	 * is thrown.
	 * @return A IDoubleWriter-compatible object representing that named input
	 */
	@Override
	public IDoubleWriter getInput(String name)
	{
		if (mInputs == null) {
			throw new StreamException("No inputs defined. Couldn't look up input '"+name+"'.");
		}
		IDoubleWriter wr = mInputs.get(name);
		if (wr == null) {
			throw new StreamException("Input '"+name+"' not defined for this node.");
		}
		return wr;
	}
	
	/**
	 * Add an input to the list of defined inputs. This method is called by implementing classes to define inputs for
	 * their implementation.
	 * 
	 * @param name The name of the input to add.
	 */
	protected void addInput(String name) 
	{
		if (mInputs == null) {
			mInputs = new HashMap<String, IDoubleWriter>(2); // small hash set as most nodes are expected to have a small number of inputs
		}
		if (!mInputs.containsKey(name)) {
			mInputs.put(name, new NamedInput(name));
		}
		else {
			// TODO log the fact that the input was to be added multiple times
		}
		// reset the cache which might have existed
		mInputsCache = null;
	}

	/**
	 * Method implemented by node implementers to receive a value from a named input.
	 * 
	 * @param name  The name of the input where the value came from.
	 * @param value The value itself.
	 * @param time  The time at which the value was received.
	 */
	protected abstract void doWrite(String name, double value, TimeBase<?> time);
	
	/**
	 * Helper function to get/create a node with the given name and class and return its named input.
	 * This method is mainly to be used by implementers of derived classes to save typing in the
	 * factory functions.
	 * 
	 * @param cls  The real class of the node in case it's not existing under that name. Must be default-constructible.
	 * @param name The name containing the named input. The format of the name is: "node-name/input-name". The node-name
	 * 				therefore <em>MUST NOT</em> contain a slash. It is recommended to do any hierarchical structuring of the
	 * 				node names using dots (e.g. "some.node/input")
	 * @return A IDoubleWrite suitable for writing to the name input of the node.
	 */
	public static IDoubleWriter openAsWriter(Class<? extends AbstractComplexDoubleNode> cls, String name)
	{
		String[] parts = name.split("/");
		if (parts.length != 2) {
			throw new StreamException("Input name must be of the form 'node/input'. Wrong name format: '"+name+"'.");
		}
		AbstractComplexDoubleNode node = DatastreamManager.open(cls, parts[0]);
		return node.getInput(parts[1]);
	}
}
