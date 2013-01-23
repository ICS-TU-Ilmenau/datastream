package de.tuilmenau.ics.datastream.editor;


import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;

import de.tuilmenau.ics.CommonSim.datastream.DatastreamManager;


public class DatastreamSelectionPage extends FormPage
{
	public static final String PREFIX_IGNORE_STREAM = "__";
	
	private static final String BUTTON_TEXT_OPEN    = "Open stream(s)";
	private static final String BUTTON_TEXT_REFRESH = "Refresh list";
	private static final String BUTTON_TEXT_LOAD    = "Load CVS";
	
	
	public DatastreamSelectionPage(DatastreamEditor editor, String id, String title)
	{
		super(editor, id, title);
		
		this.editor = editor;
	}
	
	private class OpenListener implements Listener
	{
		@Override
		public void handleEvent(Event e)
		{
			int [] selection = list.getSelectionIndices ();
			LinkedList<String> streamNames = new LinkedList<String>();
			
			for(int i=0; i<selection.length; i++) {
				String streamName = list.getItem(selection[i]);
				streamNames.addLast(streamName);
			}
			
			if(streamNames.size() > 0) {
				try {
					editor.addChart(streamNames);
				}
				catch (PartInitException exc) {
					System.err.println("Can not create chart for " +streamNames +" due to " +exc);
				}
			}
		}
	}
	
	public static List createParts(Composite parent, Listener selectionListener, Listener refreshListener, Listener loadListener)
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);
		
		//
		// List
		//
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		
		List list = new List(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		list.setLayoutData(gridData);
		list.addListener(SWT.DefaultSelection, selectionListener);
		
		GridData gridDataButton = new GridData();
		gridDataButton.horizontalAlignment = GridData.FILL;
		gridDataButton.grabExcessHorizontalSpace = true;

		//
		// Buttons
		//
		Composite buttons = new Composite(parent, SWT.NONE);
		buttons.setLayout(new GridLayout(3, false));
		buttons.setLayoutData(gridDataButton);
		
		GridData gridDataButtonOk = new GridData();
		gridDataButtonOk.horizontalAlignment = GridData.FILL;
		gridDataButtonOk.grabExcessHorizontalSpace = true;

		Button buttonOk = new Button(buttons, SWT.PUSH);
		buttonOk.setText(BUTTON_TEXT_OPEN);
		buttonOk.addListener(SWT.Selection, selectionListener);
		buttonOk.setLayoutData(gridDataButtonOk);

		Button buttonRefresh = new Button(buttons, SWT.PUSH);
		buttonRefresh.setText(BUTTON_TEXT_REFRESH);
		buttonRefresh.addListener(SWT.Selection, refreshListener);

		Button buttonLoad = new Button(buttons, SWT.PUSH);
		buttonLoad.setText(BUTTON_TEXT_LOAD);
		buttonLoad.addListener(SWT.Selection, loadListener);

		return list;
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm)
	{
		Composite client = managedForm.getForm().getBody();

		list = createParts(client,
				new OpenListener(),
				new Listener() {
						@Override
						public void handleEvent(Event event) {
							fillList(list);
						}
					},
				new LoadChartListener(getSite()));

		fillList(list);
	}
	
	public static void fillList(List list)
	{
		list.removeAll();
		
		Set<String> streamNames = DatastreamManager.getInstance().getRegisteredNames();
		
		// sort the entries
		LinkedList<String> streamNamesList  = new LinkedList<String>(streamNames);
		Collections.sort(streamNamesList);

		// put entries in GUI list
		for(String name : streamNamesList) {
			if(!name.startsWith(PREFIX_IGNORE_STREAM)) {
				list.add(name);
			}
		}
	}
	
	private DatastreamEditor editor;
	private List list;
}
