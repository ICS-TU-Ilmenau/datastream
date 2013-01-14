package de.tuilmenau.ics.CommonSim.datastream;

import junit.framework.Assert;

import org.junit.Test;

import de.tuilmenau.ics.CommonSim.datastream.annotations.AutoWire;
import de.tuilmenau.ics.CommonSim.datastream.numeric.DoubleNode;
import de.tuilmenau.ics.CommonSim.datastream.numeric.IDoubleWriter;
import de.tuilmenau.ics.CommonSim.datastream.numeric.SumNode;
import de.tuilmenau.ics.CommonSim.datastream.object.IObjectWriter;

/**
 * Test cases for the {@link DatastreamManager}
 * 
 * @author Markus Brueckner
 */
public class DatastreamManagerTest 
{
	@Test
	public void testIncompatibleType()
	{
		@SuppressWarnings({"unused"})
		class Test {
			@AutoWire(name="test",type=DoubleNode.class)
			public IObjectWriter<Object> mWriter;
		}
		
		Test t = new Test();
		try {
			DatastreamManager.autowire(t);
			Assert.fail("Expected ClassCastException because of wrong field type.");
		}
		catch (ClassCastException e) {
			// success
		}
	}
	
	/**
	 * Test checking whether reusing a name with another incompatible typed fails.
	 */
	@Test
	public void testIncompatibleSecondType()
	{
		@SuppressWarnings({"unused"})
		class Test {
			@AutoWire(name="test",type=DoubleNode.class)
			IDoubleWriter mWriter;
			@AutoWire(name="test",type=SumNode.class)
			IDoubleWriter mWriter2;
		}
		
		Test t = new Test();
		try {
			DatastreamManager.autowire(t);
			Assert.fail("Expected ClassCastException because of wrong field type.");
		}
		catch (ClassCastException e) {
			// success
		}		
	}
	
	@Test
	public void testCompatibleType()
	{
		class Test {
			@AutoWire(name="test",type=DoubleNode.class)
			public IDoubleWriter mWriter;
			@AutoWire(name="test",type=DoubleNode.class)
			public IDoubleWriter mWriter2;
		}
		
		Test t = new Test();
		DatastreamManager.autowire(t);
		Assert.assertNotNull(t.mWriter);
		Assert.assertEquals(t.mWriter, t.mWriter2);
	}
	
	@Test
	public void testUnique()
	{
		class Test
		{
			@AutoWire(name="test", unique=true)
			DoubleNode mWriter;
		}
		
		Test t1 = new Test();
		Test t2 = new Test();
		DatastreamManager.autowire(t1);
		DatastreamManager.autowire(t2);
		Assert.assertNotSame(t1.mWriter, t2.mWriter);
	}
	
	@Test
	public void testPrivate()
	{
		class Test
		{
			@AutoWire(name="test")
			private DoubleNode mWriter;
			
			public DoubleNode getWriter()
			{
				return mWriter;
			}
		}
		
		Test t1 = new Test();
		DatastreamManager.autowire(t1);
		Assert.assertNotNull(t1.getWriter());		
	}
	
	@Test
	public void testDerived()
	{
		class Base 
		{
			@AutoWire(name="derivedtest")
			public SumNode field1;
			
			public Base() {
				DatastreamManager.autowire(this);
			}
		}
		
		class Derived extends Base 
		{
			@AutoWire(name="derivedtest2")
			public SumNode field2;
			
			public Derived()
			{
				super();
				DatastreamManager.autowire(this);
			}
		}
		
		Derived d = new Derived();
		Assert.assertNotNull(d.field1);
		Assert.assertNotNull(d.field2);
	}
}
