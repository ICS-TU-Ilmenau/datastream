package de.tuilmenau.ics.CommonSim.datastream.numeric;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.StreamTime;

/**
 * Unit test for the DoubleNode  
 * 
 * @author Markus Brueckner
 */
public class SynchronizedDoubleNodeTest 
{
	IDoubleWriter mWriter;
	IDoubleReader mReader; 
	
	@Before
	public void setup()
	{
		DatastreamManager.clear();
		mWriter = SynchronizedDoubleNode.openAsWriter("test");
		mReader = SynchronizedDoubleNode.openAsReader("test");
	}
	
	/**
	 * Test whether a write and read works correctly
	 */
	@Test
	public void testWriteRead()
	{
		mWriter.write(1.25, StreamTime.ZERO);
		Assert.assertEquals(1.25, mReader.read(), 0.0);
	}
	
	/**
	 * Test whether the write time is saved correctly.
	 */
	@Test
	public void testTime()
	{
		mWriter.write(1.25, new StreamTime(1.0));
		Assert.assertEquals(1.25, mReader.read(),0.0);
		Assert.assertEquals(1.0, mReader.readTime().toSeconds(), 0.0);
	}
	
	/**
	 * Test whether a write is correctly written through to a successor
	 */
	@Test
	public void connectionTest()
	{
		IDoubleWriter succ = SynchronizedDoubleNode.openAsWriter("test2");
		SynchronizedDoubleNode.open("test").connect(succ);
		mWriter.write(1.5, StreamTime.ZERO);
		Assert.assertEquals(1.5, SynchronizedDoubleNode.openAsReader("test2").read(), 0.0);
	}
}
