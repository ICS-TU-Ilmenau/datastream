package de.tuilmenau.ics.CommonSim.datastream.occurrences;

/**
 * Basic interface supported by every occurrence type.
 * 
 * @author Markus Brueckner
 */
public interface IOccurrence
{
	/**
	 * Get the source of an occurrence. This method should return the object
	 * where an occurrence originated (e.g. the network node that detected a
	 * signal). 
	 * 
	 * @return The object from which the occurrence originated.
	 */ 
	Object getSource();
}
