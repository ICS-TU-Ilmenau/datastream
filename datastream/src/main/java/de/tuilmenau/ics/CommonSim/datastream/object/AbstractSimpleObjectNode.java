package de.tuilmenau.ics.CommonSim.datastream.object;

import java.util.HashSet;

import de.tuilmenau.ics.CommonSim.datastream.IObserver;
import de.tuilmenau.ics.CommonSim.datastream.StreamException;


/**
 * Base class for simple nodes with one input only handling double values.
 * This class provides the infrastructure for notifying observers, writing
 * values to successors etc. Nodes implementing one input only should derive from 
 * this class.
 * 
 * @author Markus Brueckner.
 */
public abstract class AbstractSimpleObjectNode<ObjectType> extends AbstractObjectNode<ObjectType> implements IObjectWriter<ObjectType>
{
	static final String[]  sInputNames = {};
	
	public AbstractSimpleObjectNode()
	{
		mObservers = new HashSet<IObserver>();
	}
	
	@Override
	public IObjectWriter<ObjectType> getInput(String name)
	{
		if (name != null && !name.equals("")) {
			throw new StreamException("No named inputs here. Error while trying to get input '"+name+"'.");
		}
		return this;
	}
	
	@Override
	public String[] getInputNames()
	{
		return sInputNames;
	}
}
