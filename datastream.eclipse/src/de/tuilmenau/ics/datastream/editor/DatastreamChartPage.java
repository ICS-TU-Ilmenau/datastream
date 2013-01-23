package de.tuilmenau.ics.datastream.editor;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;
import de.tuilmenau.ics.CommonSim.datastream.numeric.AbstractDoubleNode;

/**
 * @author Peter Große
 * @author Florian Liers
 */
public class DatastreamChartPage extends FormPage
{
	private XYSeriesCollection dataSetCollection;	
	private ChartComposite composite;
	
	private LinkedList<?> streamNames;
	private LinkedList<String> chartNodeNames;
	
	private static final int MAX_NUMBER_ENTRIES_IN_CHART = 1000*20;
	
	private static final String X_AXIS_NAME = "time";
	private static final String Y_AXIS_NAME = "";
	

	public DatastreamChartPage(FormEditor editor, String id, String title, LinkedList<?> streamNames)
	{
		super(editor, id, title);
		
		this.streamNames = (LinkedList<?>) streamNames.clone();
		this.chartNodeNames = new LinkedList<String>();
	}
	
	private void connectToStream(String streamName)
	{
		String chartNodeName = DatastreamSelectionPage.PREFIX_IGNORE_STREAM +streamName +"." +this;
		
		//
		// Connect own chart node to stream
		//
		AbstractDoubleNode writer = DatastreamManager.open(AbstractDoubleNode.class, streamName);
		ChartNode updater = DatastreamManager.open(ChartNode.class, chartNodeName);
		// Transfer current value to chart node. It is important for cases, where no new values are emitted.
		if(writer.readTime() != null) {
			updater.write(writer.read(), writer.readTime());
		}
		// register as listener at output node
		writer.connect(updater);
		
		// add it after possible exceptions during the open process
		chartNodeNames.add(chartNodeName);

		//
		// Construct (x,y) series for drawing them
		//
		XYSeries dataset = new XYSeries(streamName); // string is label in chart legend
		dataset.setDescription(streamName);
		dataset.setNotify(false);
		dataset.setMaximumItemCount(MAX_NUMBER_ENTRIES_IN_CHART);
		
		//
		// Connect chart node with XYSeries
		//
		updater.setDataset(dataset);
		updater.setComposite(composite);
		
		dataSetCollection.addSeries(dataset);
	}
	

	@Override
	protected void createFormContent(IManagedForm managedForm)
	{
		Composite client = managedForm.getForm().getBody();
		client.setLayout(new FillLayout());
		
		dataSetCollection = new XYSeriesCollection();
		
		JFreeChart chart = createChart(dataSetCollection);
		composite = new ChartComposite(client, SWT.NONE, chart, true);
		
		for(Object obj : streamNames) {
			if(obj != null) {
				connectToStream(obj.toString());
			}
		}
		
		managedForm.refresh();
	}
	
	/**
	 * Creates the Chart based on a dataset
	 */
	private JFreeChart createChart(XYDataset dataset)
	{
		JFreeChart chart = ChartFactory.createXYLineChart(getTitle(),
				X_AXIS_NAME,
				Y_AXIS_NAME,
				dataset, // data
				PlotOrientation.VERTICAL,
				(streamNames.size() > 1), // include legend
				true, false);
		
		return chart;

	}
	
	/**
	 * Saves chart as PNG file.
	 * 
	 * @param filename Filename of PNG inclusive path and extension
	 * @throws IOException On error
	 */
	public void saveAsPNG(String filename) throws IOException
	{
		File file = new File(filename);
		ChartUtilities.saveChartAsPNG(file, composite.getChart(), composite.getBounds().width, composite.getBounds().height);	
	}
	
	/**
	 * Writes all data stored in the chart to several CVS files.
	 * 
	 * @param basefilename Base filename with path but without file extension (e.g. "c:/test")
	 * @param separator Separator between X and Y (null == default \t)
	 * @throws IOException On error
	 */
	public void saveAsCSV(String basefilename, String separator) throws IOException
	{
		List<XYSeries> dataSeries = dataSetCollection.getSeries();
		int counter = 0;
		boolean usePostfix = (dataSeries.size() > 1);
		
		if(separator == null) separator = "\t";
		
		for(XYSeries series : dataSeries) {
			counter++;
			
			String namePostfix = "";
			if(usePostfix) {
				namePostfix = "-" +Integer.toString(counter);
				
				if(series.getDescription() != null) {
					namePostfix += "-" +series.getDescription();
				}
			}
			
			FileWriter fstream = new FileWriter(basefilename +namePostfix +".csv");
			BufferedWriter out = new BufferedWriter(fstream);
			
			try {
				synchronized (series) {
					List<XYDataItem> itemlist = series.getItems();
					for(XYDataItem item : itemlist) {
						out.write(item.getX().toString());
						out.write(separator);
						out.write(item.getY().toString());
						out.write("\n"); // line break
					}
				}
			}
			finally {
				out.close();
			}
		}
	}
	
	@Override
	public void dispose()
	{	
		super.dispose();
		
		for(String chartNodeName : chartNodeNames) {
			DatastreamManager.getInstance().unregister(chartNodeName);
		}
		
		chartNodeNames.clear();
	}

}
