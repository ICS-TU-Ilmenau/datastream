package de.tuilmenau.ics.CommonSim.datastream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.tuilmenau.ics.CommonSim.datastream.file.CSVFileNode;
import de.tuilmenau.ics.CommonSim.datastream.numeric.DoubleNode;
import de.tuilmenau.ics.CommonSim.datastream.numeric.IDoubleReader;
import de.tuilmenau.ics.CommonSim.datastream.numeric.IDoubleWriter;
import de.tuilmenau.ics.CommonSim.datastream.numeric.SumNode;
import de.tuilmenau.ics.CommonSim.datastream.object.IObjectReader;
import de.tuilmenau.ics.CommonSim.datastream.object.IObjectWriter;
import de.tuilmenau.ics.CommonSim.datastream.object.ObjectNode;

/**
 * Test cases for the AutoConnector class
 * 
 * @author Markus Brueckner
 */
public class AutoConnectorTest 
{
	@Before
	public void setup()
	{
		DatastreamManager.clear();
	}
	
	/**
	 * Test whether the auto-connection of compatible types works.
	 */
	@Test
	public void compatibleTest()
	{
		AutoConnector dc = new AutoConnector("double\\..*", "doubleOut", DoubleNode.class);
		AutoConnector oc = new AutoConnector("object\\..*", "objectOut", ObjectNode.class);
		DatastreamManager.getInstance().attachListener(dc);
		DatastreamManager.getInstance().attachListener(oc);
		IDoubleWriter dw = DoubleNode.openAsWriter("double.something");
		IObjectWriter<Object> ow = ObjectNode.openAsWriter("object.something");
		Object test = new Object();
		dw.write(123.25, StreamTime.ZERO);
		ow.write(test, StreamTime.ZERO);
		IDoubleReader dr = DoubleNode.openAsReader("doubleOut");		
		IObjectReader<Object> or = ObjectNode.openAsReader("objectOut");
		Assert.assertEquals(123.25, dr.read());
		Assert.assertEquals(test, or.read());
	}
	
	/**
	 * Test whether the connector connects multiple objects
	 */
	@Test
	public void multipleTest()
	{
		AutoConnector dc = new AutoConnector("double\\..*", "doubleOut", SumNode.class);
		DatastreamManager.getInstance().attachListener(dc);
		IDoubleWriter dw1 = DoubleNode.openAsWriter("double.something1");
		IDoubleWriter dw2 = DoubleNode.openAsWriter("double.something2");
		dw1.write(1.0, StreamTime.ZERO);
		dw2.write(0.5, StreamTime.ZERO);
		IDoubleReader dr = DoubleNode.openAsReader("doubleOut");		
		Assert.assertEquals(1.5, dr.read());
	}
	
	/**
	 * Test whether two incompatible nodes are correctly flagged as an error.
	 * The error actually should be thrown when the second node is created
	 */
	@Test
	public void incompatibleTest()
	{
		AutoConnector dc = new AutoConnector("double\\..*", "doubleOut", DoubleNode.class);
		DatastreamManager.getInstance().attachListener(dc);
		try {
			ObjectNode.openAsWriter("double.error");
			Assert.fail("Connecting an object node to a double node should flag an error.");
		}
		catch (StreamException e) {
			// success case
		}
	}
	
	/**
	 * Test whether two incompatible nodes are correctly flagged as an error.
	 * The error actually should be thrown when the second node is created
	 */
	@Test
	public void incompatibleIgnoreTest()
	{
		AutoConnector dc = new AutoConnector("double\\..*", "doubleOut", DoubleNode.class);
		dc.setIgnoreErrors(true);
		DatastreamManager.getInstance().attachListener(dc);
		try {
			IObjectWriter<Object> ow = ObjectNode.openAsWriter("double.error");
			ow.write(new Object(), new StreamTime(1.0));
			IDoubleReader dr = DoubleNode.openAsReader("doubleOut");
			// be sure nobody touched the output node
			Assert.assertEquals(0.0, dr.read(), 0.0);
			Assert.assertEquals(null, dr.readTime());
		}
		catch (StreamException e) {
			Assert.fail("Connecting an object node to a double node should be ignored.");
		}
	}
	
	/**
	 * Test the functionality to connect existing nodes
	 */
	@Test
	public void connectExistingTest()
	{
		IDoubleWriter dw1 = DoubleNode.openAsWriter("double.something1");
		IDoubleWriter dw2 = DoubleNode.openAsWriter("double.something2");
		AutoConnector dc = new AutoConnector("double\\..*", "doubleOut", SumNode.class);
		DatastreamManager.getInstance().attachListener(dc);
		dc.connectExisting();
		dw1.write(1.0, StreamTime.ZERO);
		dw2.write(0.5, StreamTime.ZERO);
		IDoubleReader dr = DoubleNode.openAsReader("doubleOut");		
		Assert.assertEquals(1.5, dr.read());
	}

	/**
	 * Test the functionality to connect existing nodes
	 */
	@Test
	public void connectAnyTest()
	{
		AutoConnector dc = new AutoConnector("doubleOut", SumNode.class);
		DatastreamManager.getInstance().attachListener(dc);
		IDoubleWriter dw1 = DoubleNode.openAsWriter("double.something1");
		IDoubleWriter dw2 = DoubleNode.openAsWriter("double.something2");
		IDoubleWriter dw3 = DoubleNode.openAsWriter("something.else");
		dw1.write(1.0, StreamTime.ZERO);
		dw2.write(0.5, StreamTime.ZERO);
		dw3.write(0.25, StreamTime.ZERO);
		IDoubleReader dr = DoubleNode.openAsReader("doubleOut");		
		Assert.assertEquals(1.75, dr.read());
	}
	
	/**
	 * Check whether nodes that accept multiple inputs may be connected correctly. 
	 */
	@Test
	public void connectMultitypeTest()
	{
		AutoConnector dc = new AutoConnector("doubleOut", CSVFileNode.class);
		DatastreamManager.getInstance().attachListener(dc);
		DoubleNode.openAsWriter("double.something1");
		ObjectNode.openAsWriter("object.something2");
	}
}
