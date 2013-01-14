package de.tuilmenau.ics.CommonSim.datastream.numeric;

import java.util.Deque;
import java.util.LinkedList;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.TimeBase;

/**
 * Node keeping track of the history of values written into it.
 * The size of the history can be configured. If the buffer space
 * is exhausted the buffer removes elements in an FIFO fashion.
 * As this class is mostly intended to hold series of statistical values
 * for visualization, it is thread-safe with respect to the history.
 * For thread-safe access to the saved values see the documentation
 * of {@link HistoryDoubleNode#readHistory()}
 * 
 * @author Markus Brueckner
 */
public class HistoryDoubleNode extends AbstractSimpleDoubleNode 
{
	int mHistorySize;
	/**
	 * 2-Tuple of time and value saved in the history buffer.
	 */
	public static class Tuple 
	{
		public final TimeBase<?> time;
		public final double      value;
		
		public Tuple(TimeBase<?> time, double value) {
			this.time  = time;
			this.value = value;
		}
	}

	Deque<Tuple> mHistory;
	
	/**
	 * Create an history node with an unbounded buffer size.
	 */
	public HistoryDoubleNode() 
	{
		this(0);
	}

	/**
	 * Create a history node with the given size.
	 * 
	 * @param historySize The number of values to keep in the internal buffer. If this
	 * 					if 0 then the buffer size is unbounded.
	 */
	public HistoryDoubleNode(int historySize)
	{
		mHistorySize = historySize;
		mHistory = new LinkedList<HistoryDoubleNode.Tuple>();
	}
	
	@Override
	public void write(double value, TimeBase<?> time) 
	{
		synchronized (mHistory) {
			if (mHistorySize != 0 && mHistory.size()==mHistorySize) {
				mHistory.remove();
			}
			mHistory.add(new Tuple(time, value));
			doWriteAll(value, time);
			doNotifyAll();
		}
	}

	@Override
	public void tick(TimeBase<?> time) 
	{
		synchronized (mHistory) {
			write(1.0, time);
		}
	}

	@Override
	public void reset() 
	{
		synchronized (mHistory) {
			mHistory.clear();
		}
	}

	/**
	 * Returns the element last written to the history
	 */
	@Override
	public double read() 
	{
		synchronized (mHistory) {
			return mHistory.getLast().value;
		}
	}
	
	/**
	 * Return the full history of this node. Note: the returned
	 * object is not synchronized anymore. Iterating over it 
	 * MUST be synchronized by the user!
	 * <code>
	 * Deque<Tuple> history = node.getHistory();
	 * 
	 * synchronized(history) {
	 *   for (Tuple t: history) {
	 *   	// do something
	 *   }
	 * }
	 * </code>
	 * The returned history is not to be written! If the user inserts data
	 * into the history, it might violate size constraints defined in the
	 * node. 
	 * 
	 * @return The history of this node.
	 */
	public Deque<Tuple> readHistory() 
	{
		synchronized (mHistory) {
			return mHistory;
		}
	}

	/**
	 * Returns the timestamp of the last write operation
	 */
	@Override
	public TimeBase<?> readTime() 
	{
		synchronized (mHistory) {
			return mHistory.getLast().time;
		}
	}

	/**
	 * Open (or create) a writer under the given name for that node type. 
	 * 
	 * @param id The id for the node. Either the node under that id is retrieved or a new
	 * 				node of type HistoryDoubleNode is created, registered under the given name and
	 * 				returned.
	 * @return A writer suitable for writing doubles to a HistoryDoubleNode.
	 */
	public synchronized static IDoubleWriter openAsWriter(String id)
	{
		return open(id).getInput(null);
	}

	/**
	 * Open (or create) a reader under the given name for that node type. 
	 * 
	 * @param id The id for the node. Either the node under that id is retrieved or a new
	 * 				node of type HistoryDoubleNode is created, registered under the given name and
	 * 				returned.
	 * @return A writer suitable for reading doubles from a HistoryDoubleNode.
	 */
	public synchronized static IDoubleReader openAsReader(String id)
	{
		return open(id);
	}

	/**
	 * Open/create the node under the given name.
	 * 
	 * @param name The name to attach to the node. 
	 * @return The node under that name.
	 */
	public synchronized static HistoryDoubleNode open(String name) 
	{
		return DatastreamManager.open(HistoryDoubleNode.class, name);
	}
}
