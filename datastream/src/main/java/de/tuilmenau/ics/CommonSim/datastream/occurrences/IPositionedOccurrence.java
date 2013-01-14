package de.tuilmenau.ics.CommonSim.datastream.occurrences;

import de.tuilmenau.ics.CommonSim.datastream.occurrences.types.Position;

/**
 * Occurrence that has a position. Occurrences that might be position based (e.g.
 * "node detected") have to implement this interface in order to be handled position-wise.
 * 
 * @author Markus Brueckner
 */
public interface IPositionedOccurrence extends IOccurrence 
{
	/**
	 * Return the position at which this occurrence took place.
	 * 
	 * @return The position of this occurrence.
	 */
	Position getPosition();
}
