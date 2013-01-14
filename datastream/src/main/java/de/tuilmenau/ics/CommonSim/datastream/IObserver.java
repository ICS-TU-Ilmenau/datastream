package de.tuilmenau.ics.CommonSim.datastream;

public interface IObserver 
{
	/**
	 * Notify an observer about changes.
	 * 
	 * @param obs The observable that triggered the change. In most cases this
	 * 				function needs to cast the observable to an appropriate reader
	 * 				interface and get the new value from it.
	 */
	public void notify(IObservable obs);
}
