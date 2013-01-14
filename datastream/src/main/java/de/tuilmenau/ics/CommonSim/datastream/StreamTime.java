package de.tuilmenau.ics.CommonSim.datastream;

/**
 * Helper class used to express time in the datastream framework. While throughout the
 * whole framework only references of {@link TimeBase} are handled, this class is provided
 * to ease stand-alone uses. In order to accommodate simpler extension of the time concept
 * in derived classes, {@link TimeBase} is an abstract and generic class. While stand-alone
 * users might opt to implement their own notion of time based on this class, they may also
 * directly use StreamTime and safe the effort (depending on their needs). StreamTime is 
 * nothing more than an unchanged implementation of {@link TimeBase} without any additional
 * functionality.
 * 
 * @author Markus Brueckner
 *
 */
public class StreamTime extends TimeBase<StreamTime> 
{
	/**
	 * Timespan of exactly nothing (useful for decoupling in some cases)
	 */
	public static final TimeBase<StreamTime> ZERO = new StreamTime(0,0,0,0,0,0);	
	
	/**
	 * Create a new timespan with the given characteristics.
	 * 
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @param millis
	 * @param micros
	 * @param nanos
	 */
	public StreamTime(long hours, long minutes, long seconds, long millis,
			long micros, long nanos) {
		super(hours, minutes, seconds, millis, micros, nanos);
	}
	
	/**
	 * Create a new timespan spanning the given amount of seconds.
	 * 
	 * @param seconds
	 */
	public StreamTime(double seconds) 
	{
		super(seconds);
	}

	@Override
	public StreamTime newInstance(long hours, long minutes, long seconds,
		long millis, long micros, long nanos) {
		return new StreamTime(hours, minutes, seconds, millis, micros, nanos);
	}
}
