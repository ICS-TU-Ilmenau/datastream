package de.tuilmenau.ics.CommonSim.datastream;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test cases for the {@link StreamTime} class
 * 
 * @author Markus Brueckner
 */
public class StreamTimeTest 
{
	@Test
	public void testSort()
	{
		StreamTime t1 = new StreamTime(0);
		StreamTime t2 = new StreamTime(1);
		Assert.assertTrue(t1.compareTo(t2) < 0);
		Assert.assertTrue(t2.compareTo(t1) > 0);
	}
	
	@Test
	public void testEquality()
	{
		StreamTime t1 = new StreamTime(1);
		StreamTime t2 = new StreamTime(1);
		Assert.assertEquals(t1, t2);
	}
	
	@Test
	public void testCalculations()
	{
		StreamTime t1 = new StreamTime(1.5);
		StreamTime t2 = new StreamTime(2.25);
		Assert.assertEquals(new StreamTime(3.75), t1.add(t2));
		Assert.assertEquals(new StreamTime(0.75), t2.subtract(t1));
	}
}
