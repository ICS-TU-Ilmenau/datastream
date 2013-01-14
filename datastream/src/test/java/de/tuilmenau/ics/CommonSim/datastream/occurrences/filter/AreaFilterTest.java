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
import de.tuilmenau.ics.CommonSim.datastream.occurrences.IPositionedOccurrence;
import de.tuilmenau.ics.CommonSim.datastream.occurrences.types.Box;
import de.tuilmenau.ics.CommonSim.datastream.occurrences.types.Position;

/**
 * Test cases of the {@link AreaFilter} class.
 * 
 * @author Markus Brueckner
 */
@RunWith(JMock.class)
public class AreaFilterTest 
{	
	Mockery m = new JUnit4Mockery();
	
	/**
	 * Test of sending an occurrence in- and outside of the box.
	 */
	@Test
	public void filterTest()
	{
		@SuppressWarnings("unchecked")
		final IObjectWriter<IOccurrence> checker = m.mock(IObjectWriter.class);
		final IPositionedOccurrence inside = m.mock(IPositionedOccurrence.class, "inside");
		final IPositionedOccurrence outside = m.mock(IPositionedOccurrence.class, "outside");
		AreaFilter af = new AreaFilter(new Box(new Position(0,0,0), new Position(1,1,1)));
		af.connect(checker);
		m.checking(new Expectations() {{
			oneOf(inside).getPosition(); will(returnValue(new Position(0.5, 0.5, 0.5)));
			oneOf(checker).write(inside, StreamTime.ZERO);
			oneOf(outside).getPosition(); will(returnValue(new Position(2,2,2)));
		}});
		af.write(inside, StreamTime.ZERO);
		af.write(outside, StreamTime.ZERO);
	}
}
