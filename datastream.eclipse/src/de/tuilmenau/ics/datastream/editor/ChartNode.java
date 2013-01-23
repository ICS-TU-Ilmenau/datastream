package de.tuilmenau.ics.datastream.editor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.experimental.chart.swt.ChartComposite;

import de.tuilmenau.ics.CommonSim.datastream.TimeBase;
import de.tuilmenau.ics.CommonSim.datastream.numeric.AbstractSimpleDoubleNode;
import de.tuilmenau.ics.datastream.eclipse.Activator;


public class ChartNode extends AbstractSimpleDoubleNode implements Comparable<Object>
{
	private XYSeries dataset;
	private ChartComposite composite;
	private Queue<XYDataItem> queue = new ConcurrentLinkedQueue<XYDataItem>();
	
	// Helper for switching to SWT thread for updating the GUI
	private Runnable updateHelper = new Runnable() {		
		@Override
		public void run()
		{
			synchronized (dataset) {
				try {
					dataset.setNotify(true);
					dataset.fireSeriesChanged();
				}
				catch(SWTException exc) {
					Activator.err(this, "Exception during dataset update.", exc);
				}
			}
		}
	};
	
	private static final int DELAY_AFTER_TRIGGER_MSEC = 250;
	private static final int REFRESH_DELAY_WITHOUT_TRIGGER_MSEC = 1000*5;
	
	private Thread thread = new Thread() {
		@Override
		public void run()
		{
			while(composite != null) {
				try {
					Thread.sleep(DELAY_AFTER_TRIGGER_MSEC);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (dataset) {
					dataset.setNotify(false);
					while (!queue.isEmpty()) {
						dataset.add(queue.poll());
					}					
				}
				
				// thread transition from adapter thread to UI
				if(!composite.isDisposed()) {
					Display disp = composite.getDisplay();
					if(!disp.isDisposed()) {
						disp.syncExec(updateHelper);
					} else {
						composite = null;
					}
				} else {
					composite = null;
				}
				
				// Wait for next re-draw.
				// Do it after drawing, to avoid delay with initial
				// paint event after creating the window.
				try {
					synchronized (this) {
						wait(REFRESH_DELAY_WITHOUT_TRIGGER_MSEC);
					}
				} catch (InterruptedException exc) {
					// ignore it
				}
			}
			
			System.out.println("ChartNode thread ended.");
		}
	};
	
	public ChartNode()
	{
	}

	@Override
	public void write(double value, TimeBase<?> time)
	{
		final XYDataItem dataItem = new XYDataItem(time.toSeconds(), value);
		
		queue.add(dataItem);
		
		synchronized (thread) {
			thread.notify();
		}
	}

	@Override
	public void tick(TimeBase<?> time)
	{
	}

	@Override
	public void reset()
	{
	}

	@Override
	public double read()
	{
		return 0;
	}

	@Override
	public TimeBase<?> readTime()
	{
		return null;
	}

	@Override
	public int compareTo(Object o)
	{
		return -1;
	}

	public void setDataset(XYSeries dataset2)
	{
		this.dataset = dataset2;
	}

	public void setComposite(ChartComposite composite)
	{
		this.composite = composite;
		
		if(!thread.isAlive()) {
			thread.start();
		}
	}

}