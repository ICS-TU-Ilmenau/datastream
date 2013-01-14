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
import de.tuilmenau.ics.CommonSim.datastream.occurrences.ISubtypeOccurrence;
import de.tuilmenau.ics.CommonSim.datastream.occurrences.filter.SubtypeFilterTest.SOccurrence.Subtypes;

/**
 * Test cases for the {@link SubtypeFilter} class.
 * 
 * @author Markus Brueckner
 */
@RunWith(JMock.class)
public class SubtypeFilterTest 
{
	Mockery m = new JUnit4Mockery();

	static class SOccurrence implements ISubtypeOccurrence
	{
		public enum Subtypes
		{
			ONE,
			TWO
		}
		
		Subtypes mSub;
		
		public SOccurrence(Subtypes sub) 
		{
			mSub = sub;
		}
		
		@Override
		public Object getSource() 
		{
			return null;
		}
		
		@Override
		public Object getSubtype() 
		{
			return mSub;
		}
	}
	
	static class DerivedSOccurrence extends SOccurrence
	{
		public DerivedSOccurrence(Subtypes sub) 
		{
			super(sub);
		}
	}
	
	static class TotallyDifferentSOccurrence implements ISubtypeOccurrence 
	{
		@Override
		public Object getSource() 
		{
			return null;
		}
		
		@Override
		public Object getSubtype() 
		{
			return null;
		}
	}
	
	@Test
	public void subtypeTest()
	{
		final ISubtypeOccurrence o1 = new SOccurrence(Subtypes.ONE);
		final ISubtypeOccurrence o2 = new DerivedSOccurrence(Subtypes.TWO);
		final ISubtypeOccurrence o3 = new DerivedSOccurrence(Subtypes.ONE);
		final ISubtypeOccurrence o4 = new TotallyDifferentSOccurrence();
		@SuppressWarnings("unchecked")
		final IObjectWriter<IOccurrence> checker = m.mock(IObjectWriter.class);		
		SubtypeFilter sf = new SubtypeFilter(SOccurrence.class, new Object[]{SOccurrence.Subtypes.ONE});
		sf.connect(checker);
		m.checking(new Expectations() {{
			oneOf(checker).write(o1, StreamTime.ZERO);
			oneOf(checker).write(o3, StreamTime.ZERO);
		}});
		sf.write(o1, StreamTime.ZERO);
		sf.write(o2, StreamTime.ZERO);
		sf.write(o3, StreamTime.ZERO);
		sf.write(o4, StreamTime.ZERO);
	}
}
