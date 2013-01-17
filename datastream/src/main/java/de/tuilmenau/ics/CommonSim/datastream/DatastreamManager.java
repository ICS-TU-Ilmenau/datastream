package de.tuilmenau.ics.CommonSim.datastream;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tuilmenau.ics.CommonSim.datastream.annotations.AutoWire;

/**
 * Management interface of the data stream manager. This interface
 * contains all information necessary to create, retrieve and delete
 * data stream nodes. 
 * 
 * @author Markus Brueckner
 */
public class DatastreamManager 
{
	private static DatastreamManager sManager;
	
	private Map<String, INode<?>> mNodes;
	private List<IStreamChangeListener> mListeners;
	
	/**
	 * Constructor
	 */
	private DatastreamManager()
	{
		mNodes = new HashMap<String, INode<?>>();
		mListeners = new ArrayList<IStreamChangeListener>();
	}
	
	/**
	 * Return the node that's available under the name given. Note that
	 * this function returns any kind of node, no matter what type the
	 * caller desires. If use the convenience methods provided by the different
	 * node types for easier access.
	 * 
	 * @param id The unique id of the node in the stream manager.
	 * @return A node that was saved under the given id. Returns null if there is
	 * 			no node of that name.
	 */
	public INode<?> get(String id)
	{
		return mNodes.get(id);
	}
	
	/**
	 * Register the given node at the stream manager under the given name.
	 * 
	 * @param id   The name to register the node under. If there is already a node under that
	 *              name a StreamException is thrown.
	 * @param node The node to register under the given name.
	 */
	public void register(String id, INode<?> node)
	{
		if (mNodes.containsKey(id)) {
			throw new StreamException("Node under that name already registered in the stream manager.");
		}
		mNodes.put(id, node);
		for (IStreamChangeListener l : mListeners) {
			l.registered(id, node);
		}
	}
	
	/**
	 * Unregister the node under a given id. If there's no node
	 * registered under the given id, the call is silently ignored.
	 * 
	 * @param id The id to unregister.
	 */
	public void unregister(String id)
	{
		INode<?> n = mNodes.remove(id);
		if (n!=null) {
			for (IStreamChangeListener l : mListeners) {
				l.unregistered(id, n);
			}
		}
	}

	/**
	 * Get all output nodes. Output nodes are considered nodes that
	 * have no successors.
	 * 
	 * @return A set of nodes having no successors
	 */
	public Set<INode<?>> getOutputs()
	{
		// TODO implement
		return null;
	}
	
	/**
	 * Get all registered names in the datastream manager. 
	 * 
	 * @return A set of all registered names. Nodes without a registered name are not accessible in that way.
	 */
	public Set<String> getRegisteredNames()
	{
		return mNodes.keySet();
	}
	
	/**
	 * Reset all nodes in the data stream system (e.g. in order to reset the simulator).
	 * This call will leave all the registrations and the node connections intact but
	 * will reset the internal state of all registered nodes.
	 */
	public void reset()
	{
		for (INode<?> n : mNodes.values()) {
			n.reset();
		}
	}
	
	/**
	 * Attach a listener to the service.
	 * 
	 * @param listener The listener that wants to be notified about registration
	 * 		and deletion of nodes.
	 */
	public void attachListener(IStreamChangeListener listener)
	{
		mListeners.add(listener);
	}
	
	/**
	 * Detach a listener from the stream manager.
	 * 
	 * @param listener The listener to detach.
	 */
	public void detachListener(IStreamChangeListener listener)
	{
		mListeners.remove(listener);
	}
	
	/**
	 * Open a node. This node is a generic version forsimplifying the implementation of static 
	 * getters in the node implementations.
	 * Note: This method is more targeted towards implementers of node types. They should provide
	 * 			static creator methods of the form <code>openAsReader(String id)</code> and 
	 * 			<code>openAsWriter(String id)</code> returning the fitting readers and writers for
	 * 			that name.
	 * 
	 * @param <NodeType> The actual type of the node to return (e.g. IDoubleWriter)
	 * @param cls The class object representing the class of the node to create in case the node does not exist.
	 * 				Note: the class object is used to create the node in case this is necessary. Therefore
	 * 						the corresponding type must be default constructible.
	 * @param id The unique id of the node to get. If there is no node under this id, it is created using the
	 * 				class object given in the <code>cls</code> parameter.
	 * @return The object existing under the given id.
	 * @throws ClassCastException in case there is a node under that id but NodeType is not assignable from that
	 * 								node's type.
	 * @throws RuntimeException in case a node could not be created under the name. 
	 */
	@SuppressWarnings("unchecked")
	public static <NodeType> NodeType open(Class<NodeType> cls, String id)
	{
		DatastreamManager m = getInstance();
		Object n = m.get(id);
		if (n != null) {
			if (!cls.isAssignableFrom(n.getClass())) {
				throw new ClassCastException("Node under id '"+id+"' of type '"+n.getClass().getCanonicalName()+"' is not compatible to the desired type '"+cls.getCanonicalName()+"'.");
			}
			return (NodeType)n;
		}
		// create a new node under the given name
		try {
			NodeType tmp = cls.newInstance();
			m.register(id, (INode<?>)tmp);
			return tmp;
		} 
		catch (InstantiationException e) {
			throw new RuntimeException("Could not create instance of type "+cls.getCanonicalName()+". Reason: '"+e.getMessage()+"'");
		} 
		catch (IllegalAccessException e) {
			throw new RuntimeException("Could not create instance of type "+cls.getCanonicalName()+". Reason: '"+e.getMessage()+"'");
		}
	}

	/**
	 * Get a unique name for a data stream node. The name will be of the form "prefix.id.suffix". 
	 * 
	 * @param toStringPrefix Indicates if the prefix 
	 * @param prefix The prefix of the name
	 * @param obj    The object for which to get the name.
	 * @param suffix The suffix of the name.
	 * @return A string suitable as a unique name for this prefix, suffix and object.
	 */
	public static String getUniqueName(boolean toStringPrefix, String prefix, Object obj, String suffix)
	{
		String name = prefix;

		if(toStringPrefix) {
			name = obj.toString() +"." +prefix;
		} else {
			name = prefix;
		}

		if (name != null) {
			name += "."+Integer.toHexString(System.identityHashCode(obj));
		}
		else {
			name = Integer.toHexString(System.identityHashCode(obj));
		}
		if ((suffix != null) && !"".equals(suffix)) {
			name += "."+suffix;
		}
		return name;
	}
	
	/**
	 * Auto-wire the datastreams inside the given object. This method will read all the
	 * annotations defined in the annotations sub-package and auto-wire them according to
	 * their definition.
	 * 
	 * @param obj The object for which to auto-wire all the necessary fields
	 * @param reconfigure Reconfigure the object. If this parameter is true, the object will
	 * 			be reconfigured even if it was already configured previously (i.e. if the configured
	 * 			fields are non-null)
	 */
	public static void autowire(Object obj, boolean reconfigure)
	{
		// check for all fields inside the object that are annotated
		Class<?> checkedClass = obj.getClass();
		while (checkedClass != null) {
			Field[] fields = checkedClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(AutoWire.class)) {
					// stop if the object was already configured
					try {
						field.setAccessible(true);
						if (field.get(obj) != null && !reconfigure) {
							return;
						}
					} catch (IllegalArgumentException e1) {
						throw new StreamException("Could not check whether the object was already configured. (class: "+ obj.getClass().getCanonicalName()+")");
					} catch (IllegalAccessException e1) {
						throw new StreamException("Could not check whether the object was already configured. (class: "+ obj.getClass().getCanonicalName()+")");
					} finally {
						field.setAccessible(false);
					}

					AutoWire annot = field.getAnnotation(AutoWire.class);
					Class<?> cls = annot.type();
					if (cls.equals(Object.class)) {
						// the field was not set
						cls = field.getType();
					}
					if (!field.getType().isAssignableFrom(cls)) {
						throw new ClassCastException(
								"Cannot auto-wire field of type "
										+ field.getType().getCanonicalName()
										+ " with node of type "
										+ annot.type().getCanonicalName()
										+ " (field " + field.getName()
										+ " in class "
										+ checkedClass.getCanonicalName() + ")");
					}
					// TODO implement input name handling as in
					// AbstractComplexDoubleNode
					// implement unique name handling
					String name = (annot.unique() ? getUniqueName(annot.prefix(), annot.name(),
							obj, annot.suffix()) : annot.name());
					Object node = open(cls, name);
					// we'll only reach this if the type is compatible anyway
					try {
						field.setAccessible(true);
						field.set(obj, node);
						field.setAccessible(false);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(
								"Could not autowire field " + field.getName()
										+ " in class "
										+ checkedClass.getCanonicalName(), e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(
								"Could not autowire field " + field.getName()
										+ " in class "
										+ checkedClass.getCanonicalName(), e);
					}
				}
			}
			checkedClass = checkedClass.getSuperclass();
		}
	}
	
	/**
	 * Corresponds to {@link DatastreamManager#autowire(Object, false)}
	 */
	public static void autowire(Object obj)
	{
		autowire(obj, false);
	}
	
	/**
	 * get the Singleton-instance of the datastream manager
	 */
	public static DatastreamManager getInstance()
	{
		if (sManager == null) {
			synchronized(DatastreamManager.class) {
				if (sManager == null) {
					sManager = new DatastreamManager();
				}
			}
		}
		return sManager;
	}
	
	public static void clear()
	{
		synchronized (DatastreamManager.class) {
			sManager = null;
		}
	}
}
