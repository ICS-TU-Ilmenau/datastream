package de.tuilmenau.ics.CommonSim.datastream.occurrences.filter;

import de.tuilmenau.ics.CommonSim.datastream.TimeBase;
import de.tuilmenau.ics.CommonSim.datastream.occurrences.IOccurrence;
import de.tuilmenau.ics.CommonSim.datastream.occurrences.IPositionedOccurrence;
import de.tuilmenau.ics.CommonSim.datastream.occurrences.types.Box;

/**
 * Filter passing on only occurrences that are in a specific area.
 * 
 * @author Markus Brueckner
 */
public class AreaFilter extends AbstractFilterNode 
{
	Box mFilterBox;
	
	/**
	 * Create a new filter node that only lets occurrences in a specific box pass. This
	 * filter only works on instances of {@link IPositionedOccurrence}. Other types of
	 * occurrences will be dropped.
	 * 
	 * @param filterBox The box in which the occurrences have to be located to be passed on.
	 */
	public AreaFilter(Box filterBox)
	{
		mFilterBox = filterBox;
	}
	
	@Override
	protected boolean pass(IOccurrence occurrence, TimeBase<?> time) 
	{
		if (!(occurrence instanceof IPositionedOccurrence)) {
			return false;
		}
		IPositionedOccurrence po = (IPositionedOccurrence)occurrence;
		if (mFilterBox.contains(po.getPosition())) {
			return true;
		}
		return false;
	}

}
