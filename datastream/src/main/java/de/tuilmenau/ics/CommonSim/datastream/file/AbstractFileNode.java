package de.tuilmenau.ics.CommonSim.datastream.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.INode;
import de.tuilmenau.ics.CommonSim.datastream.StreamException;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;
import de.tuilmenau.ics.CommonSim.datastream.numeric.IDoubleWriter;
import de.tuilmenau.ics.CommonSim.datastream.object.IObjectWriter;

/**
 * Class implementing an output node that writes the values to a file.
 * Classes deriving from this class implement specific output formats.
 * Note: objects of this type will create writers under any desired name.
 * There are no predefined writer names. The writer names serve as an
 * additional meta information about the data currently written (e.g.
 * the name of the value currently written etc.).
 * 
 * @author Florian Liers
 * @author Markus Brueckner
 */
public abstract class AbstractFileNode implements INode<Object> // deriving from Object as an input type to allow any kind of input  
{
	// TODO need some generic way to select the output format based on some MIME-type/whatever -> derived nodes _MUST_ register here
	String mFilename;
	Writer mFile;
	Map<String, NamedInput> mInputs;

	/**
	 * Helper class providing a binding between a name and an input
	 */
	protected class NamedInput implements IDoubleWriter, IObjectWriter<Object>
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
			AbstractFileNode.this.doWrite(mName, value, time);
		}
		
		@Override
		public void write(Object value, TimeBase<?> time) 
		{
			AbstractFileNode.this.doWrite(mName, value, time);
		}
	}
	
	protected AbstractFileNode()
	{
		// just protected to prevent accidental instantiation
	}
	
	@Override
	public String[] getInputNames() 
	{
		return null;
	}

	/**
	 * Get the filename of the file underlying this node.
	 * 
	 * @return The name of the file under this object or null if no filename exists (e.g. when using writers to System.out)
	 */
	public String getFilename()
	{
		return mFilename;
	}
	
	/**
	 * Get the writer behind this node.
	 * 
	 * @return The writer object this node writes to
	 */
	protected Writer getWriter()
	{
		return mFile;
	}

	/**
	 * Set the filename behind this object. This call opens the file if
	 * possible.
	 * 
	 * @param fileName The file to write to
	 */
	public void setFilename(String filename) 
	{		
		try {
			setWriter(new BufferedWriter(new FileWriter(filename)));
		} catch (IOException e) {
			throw new StreamException("Could not open file "+filename+". Reason: "+e.getMessage());
		}
	}
 
	/**
	 * Set the underlying writer this object writes to.
	 * 
	 * @param writer The writer to write the output to.
	 */
	public void setWriter(Writer writer)
	{
		mFile = writer;
	}
	
	@Override
	public void reset() 
	{
		// close the file
		try {
			mFile.close();
		} catch (IOException e) {
			throw new StreamException("Could not close underlying file. Reason: "+e.getMessage());
		}
		mFile = null;
		mFilename = null;
	}
	
	/**
	 * Get a named input of the node. The call will return a helper object that
	 * represents the named input of the node. Writes to this object will appear
	 * inside the node as coming from that name.
	 * 
	 * @param name The name of the input to return. If there's no such name, the input will be
	 * 				created
	 * @return An input suitable for writing double values as well as objects.
	 */
	@Override
	public NamedInput getInput(String name)
	{
		if (mInputs == null) {
			mInputs = new HashMap<String, NamedInput>();
		}
		NamedInput wr = mInputs.get(name);
		if (wr == null) {
			// not defined until now -> create it
			wr = new NamedInput(name);
			mInputs.put(name, wr);
		}
		return wr;
	}
	
	/**
	 * Function implementing the actual writing process. Classes
	 * deriving from this class implement this method to do the actual
	 * writing of double values to a file. 
	 * 
	 * @param name  The name of the input that received the value.
	 * @param value The value that was given at this input.
	 * @param t     The time at which the value was written.
	 */
	protected abstract void doWrite(String name, double value, TimeBase<?> t);

	/**
	 * Function implementing the actual writing process. Classes
	 * deriving from this class implement this method to do the actual
	 * writing of object values to a file. 
	 * 
	 * @param name  The name of the input that received the value. 
	 * @param value The value that was received.
	 * @param t     The time at which the value was received.
	 */
	protected abstract void doWrite(String name, Object value, TimeBase<?> t);
	
	/**
	 * Helper function to get/create a node with the given name and class and return its named input.
	 * This method is mainly to be used by implementers of derived classes to save typing in the
	 * factory functions.
	 * 
	 * @param cls  The real class of the node in case it's not existing under that name. Must be default-constructible.
	 * @param name The name containing the named input. The format of the name is: "node-name/input-name". The node-name
	 * 				therefore <em>MUST NOT</em> contain a slash. It is recommended to do any hierarchical structuring of the
	 * 				node names using dots (e.g. "some.node/input")
	 * @param filename The underlying file name of the object. The file will be opened for writing.
	 * @return A dual-personality NamedInput writer suitable for writing to the named input of the node.
	 */
	protected static NamedInput openAsWriter(Class<? extends AbstractFileNode> cls, String name, String filename)
	{
		String[] parts = name.split("/");
		if (parts.length != 2) {
			throw new StreamException("Input name must be of the form 'node/input'. Wrong name format: '"+name+"'.");
		}
		AbstractFileNode node = DatastreamManager.open(cls, parts[0]);
		Writer nodeWriter = node.getWriter();
		if (nodeWriter != null && node.getFilename() != null && !node.getFilename().equals(filename)) {
			// do not reopen an id with a given writer
			throw new StreamException("The node saved under the given id has already opened a file. Most likely this is a bug. (id: "+name+")");
		}
		node.setFilename(filename);
		return node.getInput(parts[1]);
	}

	/**
	 * Helper function to get/create a node with the given name and class and return its named input.
	 * This method is mainly to be used by implementers of derived classes to save typing in the
	 * factory functions.
	 * 
	 * @param cls  The real class of the node in case it's not existing under that name. Must be default-constructible.
	 * @param name The name containing the named input. The format of the name is: "node-name/input-name". The node-name
	 * 				therefore <em>MUST NOT</em> contain a slash. It is recommended to do any hierarchical structuring of the
	 * 				node names using dots (e.g. "some.node/input")
	 * @param writer The writer this object should write to.
	 * @return A dual-personality NamedInput writer suitable for writing to the named input of the node.
	 */
	protected static NamedInput openAsWriter(Class<? extends AbstractFileNode> cls, String name, Writer writer)
	{
		String[] parts = name.split("/");
		if (parts.length != 2) {
			throw new StreamException("Input name must be of the form 'node/input'. Wrong name format: '"+name+"'.");
		}
		AbstractFileNode node = DatastreamManager.open(cls, parts[0]);
		Writer nodeWriter = node.getWriter();
		if (nodeWriter != null && nodeWriter != writer) {
			// do not reopen an id with a given writer
			throw new StreamException("The node saved under the given id has a different writer than desired. Most likely this is a bug. (id: "+name+")");
		}
		node.setWriter(writer);
		return node.getInput(parts[1]);
	}
	
	@Override
	public void connect(Object successor) 
	{
		// nothing to do here
	}
	
	@Override
	public void disconnect(Object successor) 
	{
		// nothing to do here
	}
}
