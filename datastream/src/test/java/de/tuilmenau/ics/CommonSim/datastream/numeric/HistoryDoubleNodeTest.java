package de.tuilmenau.ics.CommonSim.datastream.numeric;

import java.util.Deque;

import junit.framework.Assert;

import org.junit.Test;

import de.tuilmenau.ics.CommonSim.datastream.StreamTime;
import de.tuilmenau.ics.CommonSim.datastream.numeric.HistoryDoubleNode.Tuple;

/**
 * Test-cases for the {@link HistoryDoubleNode} class
 *  
 * @author Markus Brueckner
 */
public class HistoryDoubleNodeTest 
{
	@Test
	public void testInserts()
	{
		HistoryDoubleNode n = new HistoryDoubleNode();
		n.write(1.25, new StreamTime(1.0));
		n.write(3.5, new StreamTime(1.5));
		Assert.assertEquals(2, n.readHistory().size());
		Deque<Tuple> h = n.readHistory();
		Assert.assertEquals(1.25, h.getFirst().value);
		Assert.assertEquals(1.0, h.getFirst().time.toSeconds());
		Assert.assertEquals(3.5, h.getLast().value);
		Assert.assertEquals(1.5, h.getLast().time.toSeconds());
	}

	/**
	 * Test whether the ring buffer system works well
	 */
	@Test
	public void testConstraint()
	{
		HistoryDoubleNode n = new HistoryDoubleNode(5);
		n.write(1.25, new StreamTime(1.0));
		n.write(3.5, new StreamTime(1.5));
		Assert.assertEquals(2, n.readHistory().size());
		n.write(3.25, new StreamTime(2.0));
		n.write(2.5, new StreamTime(3.5));
		n.write(2.25, new StreamTime(4.0));
		n.write(1.5, new StreamTime(5.5));
		Assert.assertEquals(5, n.readHistory().size());
		Deque<Tuple> h = n.readHistory();
		Assert.assertEquals(3.5, h.getFirst().value);
		Assert.assertEquals(1.5, h.getFirst().time.toSeconds());
		Assert.assertEquals(1.5, h.getLast().value);
		Assert.assertEquals(5.5, h.getLast().time.toSeconds());
	}
	
	/**
	 * Check whether the values are written from the history node to the next
	 */
	@Test
	public void testWriteThrough()
	{
		HistoryDoubleNode n  = new HistoryDoubleNode();
		HistoryDoubleNode n2 = new HistoryDoubleNode();
		n.connect(n2);
		n.write(1.25, new StreamTime(1.0));
		n.write(3.5, new StreamTime(1.5));
		Assert.assertEquals(2, n.readHistory().size());
		Deque<Tuple> h = n.readHistory();
		Assert.assertEquals(1.25, h.getFirst().value);
		Assert.assertEquals(1.0, h.getFirst().time.toSeconds());
		Assert.assertEquals(3.5, h.getLast().value);
		Assert.assertEquals(1.5, h.getLast().time.toSeconds());		
		Assert.assertEquals(2, n2.readHistory().size());
		Deque<Tuple> h2 = n2.readHistory();
		Assert.assertEquals(1.25, h2.getFirst().value);
		Assert.assertEquals(1.0, h2.getFirst().time.toSeconds());
		Assert.assertEquals(3.5, h2.getLast().value);
		Assert.assertEquals(1.5, h2.getLast().time.toSeconds());		
	}
}
