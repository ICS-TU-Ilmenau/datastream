package de.tuilmenau.ics.CommonSim.datastream.numeric;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.INode;
import de.tuilmenau.ics.CommonSim.datastream.StreamTime;

/**
 * Unit test for {@link SumNode}
 * 
 * @author Markus Brueckner
 *
 */
public class DoubleSumNodeTest 
{
	IDoubleWriter mWriter;
	IDoubleReader mReader; 
	
	@Before
	public void setup()
	{
		DatastreamManager.clear();
		mWriter = SumNode.openAsWriter("test");
		((INode<?>)mWriter).reset();
		mReader = SumNode.openAsReader("test");
	}
	
	/**
	 * Test checking for the correct writing of one value after initialization
	 */
	@Test
	public void singleWrite()
	{
		mWriter.write(1.0, StreamTime.ZERO);
		Assert.assertEquals(1.0, mReader.read());
	}
	
	/**
	 * Test checking for the correct summation of multiple writes
	 */
	@Test
	public void multiWrite()
	{
		mWriter.write(1.0, StreamTime.ZERO);
		mWriter.write(1.5, StreamTime.ZERO);
		Assert.assertEquals(2.5, mReader.read());
	}
}
