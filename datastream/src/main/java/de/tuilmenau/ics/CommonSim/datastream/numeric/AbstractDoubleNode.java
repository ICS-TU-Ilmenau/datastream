package de.tuilmenau.ics.CommonSim.datastream.numeric;

import java.util.HashSet;

import de.tuilmenau.ics.CommonSim.datastream.IObservable;
import de.tuilmenau.ics.CommonSim.datastream.IObserver;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

public abstract class AbstractDoubleNode implements IDoubleNode, IDoubleReader, IObservable
{
	protected HashSet<IObserver> mObservers;
	HashSet<IDoubleWriter> mSuccessors;

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
	 * Examples for this might be every write call (on a simple sum node) or
	 * every now and then (e.g. on a moving average node)
	 * 
	 * @param value the value to write through to all successors.
	 * @param time  the time which to give to all the successors.
	 */
	protected void doWriteAll(double value, TimeBase<?> time) 
	{
		if (mSuccessors != null) {
			for (IDoubleWriter wr : mSuccessors) {
				wr.write(value, time);
			}
		}
	}

	@Override
	public void connect(IDoubleWriter successor) 
	{
		if (mSuccessors == null) {
			mSuccessors = new HashSet<IDoubleWriter>();
		}
		if (!mSuccessors.add(successor)) {
			// TODO log warning message.
		}
	}

	@Override
	public void disconnect(IDoubleWriter successor) 
	{
		if (mSuccessors == null || !mSuccessors.remove(successor)) {
			// TODO log warning message
		}
	}
}
