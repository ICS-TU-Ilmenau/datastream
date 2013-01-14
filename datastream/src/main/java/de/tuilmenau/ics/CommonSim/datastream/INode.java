package de.tuilmenau.ics.CommonSim.datastream;

/**
 * Basic interface implemented by every node in the
 * data stream graph.
 * 
 * @author Markus Brueckner
 */
public interface INode<InputType> 
{
	/**
	 * Get all the defined input names of the node. The array of strings will contain
	 * all names for inputs defined on this object. <em>Note on the concept of inputs</em>:
	 * an input is like an "inbox with a meaning". The node interprets values arriving at a
	 * specific input in a special way. Implementers have to document what input names are
	 * available and what they mean. An input is <em>NOT</em> a 1:1 connection to a predecessor.
	 * There is no fixed connection, only values written to that input.
	 * 
	 * @return An array of successors of this node. If a node only defines one input this array
	 * 			may be empty. If a node does not have any input (e.g. {@link ConfigNode}
	 * 			this returns null; 
	 */
	String[] getInputNames();
	
	/**
	 * Reset the internal state of this node. A call to this function <em>MUST NOT</em>
	 * trigger a write through to other nodes.
	 */
	void reset();
	
	/**
	 * Get a named input of a node. Nodes having more than one input distinguish them by using
	 * different names. Nodes implementing one input only must return that input under the names
	 * "" and null (that is: the empty string or null). Any other name must cause an exception.
	 * 
	 * @param name The name of the input.
	 * @return The writer object that serves as an input to the node under the given name
	 */
	InputType getInput(String id);
	
	/**
	 * Connect the given writer as the successor of this node. Objects implementing
	 * the writer interface are required to call InputType#write(...) with
	 * the correct values every time they create a new value (e.g. if somebody calls <code>write</code> on 
	 * them). A writer <em>MUST</em> support multiple successors.
	 * 
	 * @param successor The writer to connect as a successor. If this writer is already in the list
	 * 					of successors, the call is ignored. Implementers may choose to issue a warning
	 * 					message in the log.
	 */
	void connect(InputType successor);
	
	/**
	 * Disconnect the successor from this object. 
	 * 
	 * @param successor The successor to disconnect. If this successor is not connected, the call is
	 * 					ignored. Implementers may choose to issue a warning message in the log.
	 */
	void disconnect(InputType successor);

}
