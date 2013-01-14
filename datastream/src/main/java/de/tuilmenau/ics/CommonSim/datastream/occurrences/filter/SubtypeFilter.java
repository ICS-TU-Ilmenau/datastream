package de.tuilmenau.ics.CommonSim.datastream.occurrences.filter;

import de.tuilmenau.ics.CommonSim.datastream.TimeBase;
import de.tuilmenau.ics.CommonSim.datastream.occurrences.IOccurrence;
import de.tuilmenau.ics.CommonSim.datastream.occurrences.ISubtypeOccurrence;

/**
 * Filter node matching type and subtype of an occurrence. This filter first
 * matches on the class configured as the type of a node (thereby configurable 
 * whether it should match derived types or not) and second on a subtype given
 * as an object. The subtype matching is done using subtype.equals(...).
 * Potential uses could be matching of connection state changes, e.g. matching on
 * the occurrence type TCPConnectionStateChange and the subtype CONNECTION_ESTABLISHED
 * (hypothetical values. See the documentation of the exported occurrences of the
 * implementations you want to supervise for details)
 * Note: filters of this type work on {@link ISubtypeOccurrence}-implementing classes only.
 * Classes not implementing this interface will always be dropped by the filter.
 * 
 * @author Markus Brueckner
 */
public class SubtypeFilter extends AbstractFilterNode 
{
	private Object[] mSubtypes;
	private Class<? extends IOccurrence> mType;
	
	/**
	 * Create a new subtype matcher.
	 * 
	 * @param cls The type of occurrence to match.
	 * @param subtypes The subtypes to match. An occurrence matches if its subtype
	 * 					equals one of the subtypes given here.
	 */
	public SubtypeFilter(Class<? extends IOccurrence> cls, Object[] subtypes)
	{
		mType = cls;
		mSubtypes = subtypes;
	}

	@Override
	protected boolean pass(IOccurrence occurrence, TimeBase<?> time) 
	{
		if (!(occurrence instanceof ISubtypeOccurrence)) {
			// we're only able to match subtype-occurrences
			return false;
		}
		ISubtypeOccurrence soccurrence = (ISubtypeOccurrence)occurrence;
		if (mType.isAssignableFrom(occurrence.getClass())) {
			for (Object subtype : mSubtypes) {
				if (subtype.equals(soccurrence.getSubtype())) {
					return true;
				}
			}
		}
		return false;
	}

}
