package de.tuilmenau.ics.CommonSim.datastream.file;

import java.io.StringWriter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.StreamTime;
import de.tuilmenau.ics.CommonSim.datastream.numeric.IDoubleWriter;
import de.tuilmenau.ics.CommonSim.datastream.object.IObjectWriter;

/**
 * Test cases for the CSV file node class.
 * 
 * @author Markus Brueckner
 */
public class CSVFileNodeTest 
{	
	@Before
	public void setup()
	{
		DatastreamManager.clear();
	}
	
	@Test
	public void testDouble()
	{
		StringWriter sw = new StringWriter();
		IDoubleWriter wr = CSVFileNode.openAsDoubleWriter("test.test/test", sw);
		wr.write(1.0, StreamTime.ZERO);
		Assert.assertEquals("0.0;\"test\";1.0\n", sw.toString());
	}
	
	@Test
	public void testObject()
	{
		StringWriter sw = new StringWriter();
		IObjectWriter<Object> wr = CSVFileNode.openAsObjectWriter("test.test/test2", sw);
		wr.write("Just a test", StreamTime.ZERO);
		Assert.assertEquals("0.0;\"test2\";\"Just a test\"\n", sw.toString());
	}
}
