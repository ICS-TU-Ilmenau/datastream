package de.tuilmenau.ics.CommonSim.datastream.object;

import java.util.HashSet;

import de.tuilmenau.ics.CommonSim.datastream.IObservable;
import de.tuilmenau.ics.CommonSim.datastream.IObserver;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

public abstract class AbstractObjectNode<ObjectType> implements IObjectNode<ObjectType>, IObjectReader<ObjectType>, IObservable
{
	protected HashSet<IObserver> mObservers;
	HashSet<IObjectWriter<ObjectType>> mSuccessors;

	@Override
	public void registerObserver(IObserver obs) 
	{
		mObservers.add(obs);
	}

	@Override
	public void unregisterObserver(IObserver obs) 
	{
		mObservers.remove(obs);
	}

	/**
	 * Notify all observers about the fact that a new value was written.
	 * This method is to be called by classes deriving from that class
	 * whenever an update of the internal state of the node took place.
	 */
	protected void doNotifyAll() 
	{
		for (IObserver obs : mObservers) {
			obs.notify(this);
		}
	}

	/**
	 * Write-through the new value to all successors.
	 * This method is to be called by classes deriving from that class
	 * whenever an update of the internal state of the node took place.
	 * Examples for this might be every write call or
	 * every now and then (e.g. on a batch processing node)
	 * 
	 * @param value the value to write through to all successors.
	 * @param time  the time which to give to all the successors.
	 */
	protected void doWriteAll(ObjectType value, TimeBase<?> time) 
	{
		if (mSuccessors != null) {
			for (IObjectWriter<ObjectType> wr : mSuccessors) {
				wr.write(value, time);
			}
		}
	}

	@Override
	public void connect(IObjectWriter<ObjectType> successor) 
	{
		if (mSuccessors == null) {
			mSuccessors = new HashSet<IObjectWriter<ObjectType>>();
		}
		if (!mSuccessors.add(successor)) {
			// TODO log warning message.
		}
	}

	@Override
	public void disconnect(IObjectWriter<ObjectType> successor) 
	{
		if (mSuccessors == null || !mSuccessors.remove(successor)) {
			// TODO log warning message
		}
	}
}
