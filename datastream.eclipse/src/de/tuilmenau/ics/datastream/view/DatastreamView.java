package de.tuilmenau.ics.datastream.view;

import java.util.LinkedList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import de.tuilmenau.ics.datastream.editor.DatastreamEditor;
import de.tuilmenau.ics.datastream.editor.DatastreamSelectionPage;
import de.tuilmenau.ics.datastream.editor.LoadChartListener;
import de.tuilmenau.ics.fog.eclipse.ui.editors.EditorInput;


/**
 * View showing an overview of stream names provided by the Datastream
 * framework. Double click will open an editor showing the stream
 * data.
 */
public class DatastreamView extends ViewPart
{

	public DatastreamView()
	{
	}

	@Override
	public void createPartControl(Composite parent)
	{
		list = DatastreamSelectionPage.createParts(parent,
				new OpenListener(),
				new Listener() {
					@Override
					public void handleEvent(Event event) {
						DatastreamSelectionPage.fillList(list);
					}
				},
				new LoadChartListener(getSite()));
		
		DatastreamSelectionPage.fillList(list);
	}
	
	private class OpenListener implements Listener
	{
		public void handleEvent(Event e)
		{
			int [] selection = list.getSelectionIndices ();
			LinkedList<String> streamNames = new LinkedList<String>();
			
			for(int i=0; i<selection.length; i++) {
				String streamName = list.getItem(selection[i]);
				streamNames.add(streamName);
			}
			
			if(streamNames.size() > 0) {
				try {
					IWorkbenchPage page = getSite().getPage();
					
					EditorInput input = new EditorInput(streamNames, false);
					page.openEditor(input, DatastreamEditor.ID);
				}
				catch (PartInitException exc) {
					System.err.println("Can not open chart editor for " +streamNames +" due to " +exc);
				}
			}
		}
	}

	@Override
	public void setFocus()
	{
	}

	private List list;
}
