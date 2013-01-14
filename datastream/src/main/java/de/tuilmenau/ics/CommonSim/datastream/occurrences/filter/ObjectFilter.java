package de.tuilmenau.ics.CommonSim.datastream.occurrences.filter;

import de.tuilmenau.ics.CommonSim.datastream.TimeBase;
import de.tuilmenau.ics.CommonSim.datastream.occurrences.IOccurrence;

/**
 * Generic object filter matching on the object that caused a specific occurrence.
 * This filter can be used e.g. to filter occurrences from a specific source like
 * a network node. 
 * 
 * @author Markus Brueckner
 */
public class ObjectFilter extends AbstractFilterNode 
{
	private Object[] mObjects;
	
	/**
	 * Create a new object filter matching on the objects given here. The filter matches
	 * if and only if the object that is the source of an occurrence is exactly the same
	 * as given in the object list. 
	 * 
	 * @param objects The objects this filter should trigger on.
	 */
	public ObjectFilter(Object[] objects) 
	{
		mObjects = objects;
	}
	
	@Override
	protected boolean pass(IOccurrence occurrence, TimeBase<?> time) 
	{
		Object source = occurrence.getSource(); 
		for (Object o : mObjects) {
			if (o == source)
			{
				return true;
			}
		}
		return false;
	}

}
