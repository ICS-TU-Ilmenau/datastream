package de.tuilmenau.ics.CommonSim.datastream.occurrences;

/**
 * Occurrence that can have a subtype (i.e. a contained object that further describes
 * the specifics of the occurrence).
 * 
 * @author Markus Brueckner
 */
public interface ISubtypeOccurrence extends IOccurrence 
{
	/**
	 * Get the subtype of the occurrence.
	 * 
	 * @return An object that, when compared to the corresponding subtype object of another
	 * 			matching occurrence is equal.
	 */
	Object getSubtype();
}
