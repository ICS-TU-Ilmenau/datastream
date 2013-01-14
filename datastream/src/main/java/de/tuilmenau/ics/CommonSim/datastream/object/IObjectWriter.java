package de.tuilmenau.ics.CommonSim.datastream.object;

import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Interface for writing objects to a data stream. 
 * 
 * @param <ObjectType> The type of object that can be written to this writer
 * 
 * @author Markus Brueckner
 */
public interface IObjectWriter<ObjectType>
{
	/**
	 * Write an object to the stream at a specific time. This method is used to write
	 * values at times other than the current simulator time.
	 * 
	 * @param value The object to write.
	 * @param time  The time at which the object was written. This time should not be in the future.
	 */
	void write(ObjectType value, TimeBase<?> time);
}
