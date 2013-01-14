package de.tuilmenau.ics.CommonSim.datastream.object;

import de.tuilmenau.ics.CommonSim.datastream.StreamException;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Object reader returning objects.
 * 
 * @param <ObjectType> The type of object that is read from this reader
 * 
 * @author Markus Brueckner
 */
public interface IObjectReader<ObjectType>
{
	/**
	 * Read a value from the object.
	 * 
	 * @return object value (if none available null is returned)
	 * @throws StreamException in case of an error
	 */
	ObjectType read();
	
	/**
	 * Read the time of the last write operation.
	 * 
	 * @return time of last write operation (null if not available)
	 */
	TimeBase<?> readTime();
}
