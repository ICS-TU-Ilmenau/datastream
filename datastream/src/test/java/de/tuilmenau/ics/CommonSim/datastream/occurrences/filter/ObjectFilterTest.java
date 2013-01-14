package de.tuilmenau.ics.CommonSim.datastream.occurrences.filter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.tuilmenau.ics.CommonSim.datastream.StreamTime;
import de.tuilmenau.ics.CommonSim.datastream.object.IObjectWriter;
import de.tuilmenau.ics.CommonSim.datastream.occurrences.IOccurrence;

/**
 * Test cases for the {@link ObjectFilter} class.
 * 
 * @author Markus Brueckner
 */
@RunWith(JMock.class)
public class ObjectFilterTest 
{
	Mockery m = new JUnit4Mockery();
	
	@Test
	public void testFilter()
	{
		@SuppressWarnings("unchecked")
		final IObjectWriter<IOccurrence> checker = m.mock(IObjectWriter.class);
		final IOccurrence match1= m.mock(IOccurrence.class, "match1");
		final IOccurrence match2 = m.mock(IOccurrence.class, "match2");
		final IOccurrence nonmatch = m.mock(IOccurrence.class, "nonmatch");
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		ObjectFilter of = new ObjectFilter(new Object[]{o1, o2});
		m.checking(new Expectations() {{
			oneOf(match1).getSource(); will(returnValue(o1));
			oneOf(checker).write(match1, StreamTime.ZERO);
			oneOf(match2).getSource(); will(returnValue(o2));
			oneOf(checker).write(match2, StreamTime.ZERO);
			oneOf(nonmatch).getSource(); will(returnValue(o3));			
		}});
		of.connect(checker);
		of.write(match1, StreamTime.ZERO);
		of.write(match2, StreamTime.ZERO);
		of.write(nonmatch, StreamTime.ZERO);
	}
}
