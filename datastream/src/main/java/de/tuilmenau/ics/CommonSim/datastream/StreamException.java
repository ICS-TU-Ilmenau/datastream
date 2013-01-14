package de.tuilmenau.ics.CommonSim.datastream;

/**
 * Exception thrown in case of an error in the data stream
 * manager.
 * 
 * @author Markus Brueckner
 */
public class StreamException extends RuntimeException 
{
	private static final long serialVersionUID = -1530629307136096180L;

	/**
	 * Create a new stream exception
	 * 
	 * @param message A human-readable message describing the error
	 * 					that led to the exception.
	 */
	public StreamException(String message) 
	{
		super(message);
	}
}
