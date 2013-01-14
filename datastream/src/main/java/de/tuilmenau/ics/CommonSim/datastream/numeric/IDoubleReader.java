package de.tuilmenau.ics.CommonSim.datastream.numeric;

import de.tuilmenau.ics.CommonSim.datastream.StreamException;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Interface implemented by nodes acting as a data source in the data stream.
 * Every node that enables other to read values from it implements this interface.
 * 
 * @author Florian Liers
 * @author Markus Brueckner
 */
public interface IDoubleReader 
{
	/**
	 * Read a value from the object.
	 * 
	 * @return numeric value (if none available NaN is returned)
	 * @throws StreamException in case of an error
	 */
	double read();
	
	/**
	 * Read the time of the last write operation.
	 * 
	 * @return time of last write operation (null if not available)
	 */
	TimeBase<?> readTime();
}
