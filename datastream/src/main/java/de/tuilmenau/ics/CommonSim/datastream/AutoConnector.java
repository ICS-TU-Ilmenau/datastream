package de.tuilmenau.ics.CommonSim.datastream;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tuilmenau.ics.CommonSim.datastream.numeric.IDoubleNode;
import de.tuilmenau.ics.CommonSim.datastream.numeric.IDoubleWriter;
import de.tuilmenau.ics.CommonSim.datastream.object.IObjectNode;
import de.tuilmenau.ics.CommonSim.datastream.object.IObjectWriter;

/**
 * Helper class to automatically connect nodes. Objects of this class
 * register themselves as listeners at the {@link DatastreamManager} and
 * evaluate each newly created node as to whether it should be connected
 * to a given successor node.
 * 
 * @author Markus Brueckner
 */
public class AutoConnector implements IStreamChangeListener 
{
	Pattern  mInPattern;
	Class<?> mTargetClass;
	private String mOutPattern;
	private boolean mIgnoreErrors;

	/**
	 * Create an AutoConnector that matches any incoming node whose
	 * value can be fed into a node of type outputClass. Equivalent to
	 * AutoConnector(".+", outputPattern, outputClass)
	 * Note: As this really matches ALL newly registered nodes be sure to
	 * 			use a class as a target class which can accept all kinds of
	 * 			inputs or set the ignore errors flag.
	 * 
	 * @param outputPattern  The pattern of the outputNode to feed the values into.
	 * @param outputClass The class of the outputNode. If no node under the given
	 * 						name exists, an instance of this class is created and
	 * 						registered.
	 */
	public AutoConnector(String outputPattern, Class<?> outputClass)
	{
		this(".+", outputPattern, outputClass);
	}
	
	/**
	 * Create a new AutoConnector matching all nodes with the given pattern.
	 * This AutoConnector connects a node to a successor if the node's name
	 * successfully matches the given pattern and the node's output can be
	 * fed into the target node class. If the matching node's output can't be
	 * fed into the target node, an exception is thrown when registering an 
	 * incompatible node. If you want to prevent this exception from being thrown
	 * (e.g. because you want to register double nodes only but your pattern
	 * might also match object nodes) look at {@link AutoConnector#setIgnoreErrors(boolean)}
	 * for further details.
	 * 
	 * @param inputPattern  The pattern to match against the node's name.
	 * @param outputPattern The pattern to create the output name from. Think
	 * 							of this as the second parameter to {@link String#replaceAll(String, String)}
	 * 							As a rule of thumb the name created by the output pattern should
	 * 							not match the inputPattern. While the connector will prevent nodes
	 * 							being connected to themselves, matching patterns might create unwanted
	 * 							results when using the {@link AutoConnector#connectExisting()} functionality
	 * @param outputClass   The class of the target node. If the outputPattern at
	 * 							runtime yields a name, that is not registered, a node
	 * 							of this class-type is created. 
	 */
	public AutoConnector(String inputPattern, String outputPattern, Class<?> outputClass)
	{
		mInPattern  = Pattern.compile(inputPattern);
		mOutPattern = outputPattern;
		mTargetClass = outputClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void registered(String name, INode<?> node) 
	{
		String outputName;
		Matcher m = mInPattern.matcher(name);
		if (m.matches()) {
			outputName = m.replaceAll(mOutPattern);
		}
		else {
			// if a pattern is set and the node doesn't match, bail out.
			return;
		}
		String[] nameParts = outputName.split("/");
		Object targetNode = DatastreamManager.open(mTargetClass, nameParts[0]); // open the node
		if (targetNode == node) 
		{
			// bail out if we're trying to create a loop
			return;
		}
		Object input = ((INode<?>)targetNode).getInput(nameParts.length > 1 ? nameParts[1] : null);
		if (node instanceof IDoubleNode) {
			// select the input. If there's only one name part available, we'll call getInput with null.
			if (input instanceof IDoubleWriter) {
				((IDoubleNode)node).connect((IDoubleWriter)input);
			}
			else {
				if (!mIgnoreErrors) {
					throw new StreamException("Incompatible node types in AutoConnector: "+node.getClass().getCanonicalName()+"->"+targetNode.getClass().getCanonicalName());
				}
			}
		}
		else if (node instanceof IObjectNode) {
			if (input instanceof IObjectWriter) {
				// reason for the warning suppression. This cast here is kind of fishy but we don't have a possibility to
				//  ensure at runtime the matching of successors according to their type (thanks, type erasure!)
				((IObjectNode<Object>)node).connect((IObjectWriter<Object>)input);
			}
			else {
				if (!mIgnoreErrors) {
					throw new StreamException("Incompatible node types in AutoConnector: "+node.getClass().getCanonicalName()+"->"+targetNode.getClass().getCanonicalName());
				}
			}
		}
		else {
			throw new StreamException("Unsupported node type in AutoConnector: "+node.getClass().getCanonicalName());
		}
	}

	@Override
	public void unregistered(String name, INode<?> node) 
	{
		// don't actually care as the connection is at the source node anyway, which is thrown away just now
	}
	
	/**
	 * Connect all existing nodes according to the rules of this AutoConnector. Normally
	 * the AutoConnector just connects nodes that are registered after its creation.
	 * Call this method to connect the already registered nodes according to the rules of
	 * this object.
	 */
	public void connectExisting()
	{
		// make a copy of the names map to prevent a concurrent modification error that might
		//  occur if a node must be created by the AutoConnector (i.e. the output node
		//  didn't exist until now)
		HashSet<String> names = new HashSet<String>(DatastreamManager.getInstance().getRegisteredNames());
		for (String name : names) {
			INode<?> n = DatastreamManager.getInstance().get(name);
			registered(name, n);
		}
	}
	
	/**
	 * Set the ignore errors flag of the AutoConnector. If this flag is set, the
	 * object will ignore errors from trying to connect incompatible nodes. The errors
	 * will be logged in DEBUG mode but will cause no exception.
	 * 
	 * @param flag The value of the flag to set.
	 */
	public void setIgnoreErrors(boolean flag)
	{
		mIgnoreErrors = flag;
	}
	
	/**
	 * Check whether the connector is set to ignore errors. 
	 * 
	 * @return true if this connector ignores connection errors, false otherwise.
	 */
	public boolean doesIgnoreErrors()
	{
		return mIgnoreErrors;
	}
}
