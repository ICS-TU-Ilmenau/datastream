package de.tuilmenau.ics.CommonSim.datastream.occurrences;

import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Main entry point for the Occurrences framework. Occurrences are high-level events
 * in the simulation (as opposed to low-level timed events handled by the simulator
 * kernel) that are to be exported to anybody who's interested. Depending on your system
 * design occurrences and events might often be the same, but the might differ (e.g. having
 * TCP SYN, SYN-ACK and ACK as packet level events with underlying events on the wire in the
 * kernel and having a high-level "TCP connection established"-occurrence exported).
 * Occurrences are transported using the Datastream framework. They can be filtered using specialized
 * datastream nodes. 
 * 
 * @author Markus Brueckner
 */
public class Occurrences 
{
	/**
	 * The id of the datastream node handling the publication of occurrences in
	 * the datastream framework. Sending to this node can be conveniently done
	 * using the {@link Occurrences#publish(IOccurrence)} method
	 */
	public static final String PUBLISHER_NODE = "occurences";
	
	private static OccurrenceNode sPublisherNode;
	
	/**
	 * Publish an occurrence to the global occurrence node.
	 * 
	 * @param occurence The occurrence to publish.
	 * @param time      The time at which the occurrence was published
	 */
	public static void publish(IOccurrence occurence, TimeBase<?> time) 
	{
		if (sPublisherNode == null) {
			synchronized (sPublisherNode) {
				if (sPublisherNode == null) {
					sPublisherNode = OccurrenceNode.open(PUBLISHER_NODE);
				}
			}
		}
		sPublisherNode.write(occurence, time);
	}
}
