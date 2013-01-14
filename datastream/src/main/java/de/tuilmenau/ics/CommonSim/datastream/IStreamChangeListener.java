package de.tuilmenau.ics.CommonSim.datastream;

/**
 * Interface for listeners that want to be notified about
 * stream nodes being created or deleted. 
 * 
 * @author Markus Brueckner
 */
public interface IStreamChangeListener 
{
	/**
	 * Inform the listener about the registration of a node under a given name.
	 * 
	 * @param name The name the node was registered under.
	 * @param node The node object that was registered.
	 */
	void registered(String name, INode<?> node);
	
	/**
	 * Inform the listener about the de-registration of a node.
	 * 
	 * @param name The name under which the node was de-registered.
	 * @param node The node that is concerned.
	 */
	void unregistered(String name, INode<?> node);
}
