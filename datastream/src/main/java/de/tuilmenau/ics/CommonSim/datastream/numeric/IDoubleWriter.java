package de.tuilmenau.ics.CommonSim.datastream.numeric;

import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Writer to a stream for double values.
 * 
 * @author Florian Liers
 * @author Markus Brueckner
 */
public interface IDoubleWriter
{	
	/**
	 * Write a value to the object at a specific time. This method is used to write
	 * values at times other than the current simulator time.
	 * 
	 * @param value The value to write.
	 * @param time  The time at which the value was written. This time should not be in the future.
	 */
	void write(double value, TimeBase<?> time);
	
	/**
	 * Convenience function implemented as write(1.0). Can be used for
	 * counting objects etc.
	 */
	void tick(TimeBase<?> time);
}
