package de.tuilmenau.ics.CommonSim.datastream;

/**
 * Basic notion of time as handled in the data stream framework. This class just provides
 * the functionality necessary to implement one's concept of time in deriving classes
 * while still retaining all the necessary information for handling said time in the
 * data streams. Implementers basing on the framework must derive from this class in order
 * to create objects holding their concept of time and making that available to the stream
 * framework. Users just looking for a ready-made implementation of this class should have
 * a look at {@link StreamTime}
 * 
 * @author Markus Brueckner
 */
public abstract class TimeBase<T extends TimeBase<T>> implements Comparable<TimeBase<T>> {
		
	protected final long mTimeStamp;
	
	/**
	 * Create a new timestamp object
	 *  
	 * @param hours The number of hours in the timestamp
	 * @param minutes The number of minutes.
	 * @param seconds The number of seconds.
	 * @param millis The number of milliseconds.
	 * @param micros The number of microseconds.
	 * @param nanos The number of nanoseconds.
	 */
	public TimeBase(long hours, long minutes, long seconds, long millis, long micros, long nanos) {
		this.mTimeStamp = calculate(hours, minutes, seconds, millis, micros, nanos);
	}
	
	/**
	 * Generate a time object from the given seconds.
	 * 
	 * @param seconds The seconds represented by this time object
	 */
	public TimeBase(double seconds) 
	{
		mTimeStamp = (long)(seconds*1000000000L);
	}

	/**
	 * add a time object to another (and return a new object). This method adds the time span
	 * given as parameter o to the time span of this object and returns a new object representing
	 * the sum.
	 * 
	 * @param o The timespan to add.
	 * @return A new Time object representing the sum of both timespans.
	 */
	public T add(T o) {
		return newInstance(0,0,0,0,0, this.mTimeStamp+((TimeBase<T>)o).mTimeStamp);
	}
	
	/**
	 * Subtract a time from this time object. The result will be the time difference between the two points in time.
	 *  
	 * @param o the other time object to subtract from this one.
	 * @return A time object encoding the difference in time to this one.
	 */
	public T subtract(T o) {
		return newInstance(0,0,0,0,0, this.mTimeStamp-o.mTimeStamp);
	}

	/**
	 * convert the current time to seconds (as double)
	 * 
	 * @return The current time in seconds.
	 */
	public double toSeconds() {
		return ((double) mTimeStamp) / 1000000000L;
	}

	/**
	 * Create a new instance in a derived class. This method is to be implemented by classes
	 * deriving from this base in order to implement actual time objects. The method is used
	 * internally by the class to get the actual objects of deriving classes.
	 * 
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @param millis
	 * @param micros
	 * @param nanos
	 * @return A new instance with the given values set
	 */
	protected abstract T newInstance(long hours, long minutes, long seconds, long millis, long micros, long nanos);
	
	/**
	 * calculate a time value from the given input.
	 * 
	 * @param hours    The hours of the time value
	 * @param minutes  The minutes field.
	 * @param seconds  The seconds field
	 * @param millis   The milliseconds field.
	 * @param micros   The microseconds field.
	 * @param nanos    The nanoseconds field.
	 * @return         An opaque time value representing the time difference as given in
	 * 				   the parameters.
	 */
	public static long calculate(long hours, long minutes, long seconds, long millis, long micros, long nanos) {
		return hours*3600L*1000000000L+minutes*60*1000000000L+seconds*1000000000L+
			   millis*1000000L+micros*1000L+nanos;
	}
	
	@Override
	public int compareTo(TimeBase<T> o) {
		if (this.mTimeStamp < o.mTimeStamp) {
			return -1;
		}
		else if (this.mTimeStamp > o.mTimeStamp) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		double seconds = this.toSeconds();
		return String.format("%d:%02d:%06.12f", (int)(seconds/3600), (int)(seconds/60), seconds%60);
	}
	
	@Override
	public int hashCode() {
		return ((Object)this).hashCode();
	};
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null) {
			return false;
		} else if (o instanceof TimeBase) {
			return mTimeStamp == ((TimeBase)o).mTimeStamp;
		} else {
			return false;
		}
	}
	
	protected long getTimeStamp()
	{
		return mTimeStamp;
	}
}
