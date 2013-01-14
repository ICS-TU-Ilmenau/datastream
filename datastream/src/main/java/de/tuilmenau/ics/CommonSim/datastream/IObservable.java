package de.tuilmenau.ics.CommonSim.datastream;

/**
 * Observable interface. Data stream object intending to
 * notify observers about changes implement this interface
 * to provide observer management interfaces
 * 
 * @author Markus Brueckner
 */
public interface IObservable 
{
	/**
	 * Register an observer to be informed about changes.
	 * 
	 * @param obs The observer to be notified about changes. If
	 * 				the observer was already registered once, the
	 * 				call is ignored. Implementers may choose to log
	 * 				doubled registration attempts.
	 */
	public void registerObserver(IObserver obs);
	
	/**
	 * Delete an observer from the list of observers.
	 * 
	 * @param obs The observer to delete. If the observer is not
	 * 				registered, the call is ignored. Implementers
	 * 				may choose to log attempts to delete unknown
	 * 				observers.
	 */
	public void unregisterObserver(IObserver obs);
}
