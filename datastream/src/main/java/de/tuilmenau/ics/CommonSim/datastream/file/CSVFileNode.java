package de.tuilmenau.ics.CommonSim.datastream.file;

import java.io.PrintWriter;
import java.io.Writer;

import de.tuilmenau.ics.CommonSim.datastream.TimeBase;
import de.tuilmenau.ics.CommonSim.datastream.numeric.IDoubleWriter;
import de.tuilmenau.ics.CommonSim.datastream.object.IObjectWriter;

/**
 * Writer node writing the data as [time];[writer name];[value] per line
 * where <value> is the string representation of the written object/double value.
 * <time> is the writing time in simulation seconds.
 * 
 * @author Markus Brueckner
 */
public class CSVFileNode extends AbstractFileNode 
{
	@Override
	protected void doWrite(String name, double value, TimeBase<?> t) 
	{
		((PrintWriter)getWriter()).format("%s;\"%s\";%s\n", Double.toString(t.toSeconds()), name, Double.toString(value));
	}

	@Override
	protected void doWrite(String name, Object value, TimeBase<?> t) 
	{
		((PrintWriter)getWriter()).format("%s;\"%s\";\"%s\"\n", Double.toString(t.toSeconds()), name, value.toString());
	}
	
	@Override
	public void setWriter(Writer writer) 
	{
		// overriding the method to wrap the writer in a print writer 
		super.setWriter(new PrintWriter(writer));
	}

	/**
	 * Open a double writer that writes values to the given file name.  
	 * 
	 * @param id The id to register that writer under. The id has the form "nodename/inputname".
	 * 				If a node with the given nodename already exists in the system and writes to the file with the
	 * 				given name, it is reused. If it writes to a different name, a StreamException is thrown. If the
	 * 				node does not exist yet under the given name, it is created. 
	 * @param filename The name of the file to write the values to.
	 * @return An IDoubleWriter object writing to the CSV file under the given inputname from the id.
	 */
	public static IDoubleWriter openAsDoubleWriter(String id, String filename)
	{
		return AbstractFileNode.openAsWriter(CSVFileNode.class, id, filename);
	}

	/**
	 * Open an object writer that writes values to the given file name.  
	 * 
	 * @param id The id to register that writer under. The id has the form "nodename/inputname".
	 * 				If a node with the given nodename already exists in the system and writes to the file with the
	 * 				given name, it is reused. If it writes to a different name, a StreamException is thrown. If the
	 * 				node does not exist yet under the given name, it is created. 
	 * @param filename The name of the file to write the values to.
	 * @return An IObjectWriter object writing to the CSV file under the given inputname from the id.
	 */
	public static IObjectWriter<Object> openAsObjectWriter(String id, String filename)
	{
		return AbstractFileNode.openAsWriter(CSVFileNode.class, id, filename);
	}

	/**
	 * Open a double writer that writes values to the given writer.  
	 * 
	 * @param id The id to register that writer under. The id has the form "nodename/inputname".
	 * 				If a node with the given nodename already exists in the system and writes to the 
	 * 				given writer, it is reused. If it writes to a different writer, a StreamException is thrown. If the
	 * 				node does not exist yet under the given name, it is created. 
	 * @param writer The writer to write the values to.
	 * @return An IDoubleWriter object writing to the CSV file under the given inputname from the id.
	 */
	public static IDoubleWriter openAsDoubleWriter(String id, Writer writer)
	{
		return AbstractFileNode.openAsWriter(CSVFileNode.class, id, writer);
	}

	/**
	 * Open an object writer that writes values to the given writer.  
	 * 
	 * @param id The id to register that writer under. The id has the form "nodename/inputname".
	 * 				If a node with the given nodename already exists in the system and writes to the given writer
	 * 				it is reused. If it writes to a different writer, a StreamException is thrown. If the
	 * 				node does not exist yet under the given name, it is created. 
	 * @param wroter The writer to write the values to.
	 * @return An IObjectWriter object writing to the CSV file under the given inputname from the id.
	 */
	public static IObjectWriter<Object> openAsObjectWriter(String id, Writer writer)
	{
		return AbstractFileNode.openAsWriter(CSVFileNode.class, id, writer);
	}
}
