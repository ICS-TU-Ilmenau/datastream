package de.tuilmenau.ics.CommonSim.datastream.object;

import de.tuilmenau.ics.CommonSim.datastream.INode;

/**
 * Interface implemented by every object writer.
 * 
 * @author Markus Brueckner
 */
public interface IObjectNode<ObjectType> extends INode<IObjectWriter<ObjectType>> 
{
	// empty interface just for setting the input type
}
