package de.tuilmenau.ics.datastream.editor;

import java.io.IOException;
import java.util.LinkedList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.SharedHeaderFormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import de.tuilmenau.ics.datastream.eclipse.Activator;
import de.tuilmenau.ics.fog.eclipse.ui.editors.EditorInput;

/**
 * @author Peter Große
 * @author Florian Liers
 * 
 */
public class DatastreamEditor extends SharedHeaderFormEditor
{
	public static String ID = "de.tuilmenau.ics.datastream.editor.DatastreamEditor";

	private final static String SELECTION_TITLE = "Selection";
	private final static String FORM_TITLE = "FoG Charts";
	private final static String PAGE_TITLE_FOR_MULTIPLE_STREAMS = "FoG Output";
	
	private int chartCounter = 0;
	private LinkedList<Object> initialStreamNames = null;


	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException
	{	
		if(input instanceof EditorInput) {
			Object obj = ((EditorInput) input).getObj();
			
			if(obj != null) {
				if(obj instanceof LinkedList<?>) {
					initialStreamNames = (LinkedList<Object>) obj;
				} else {
					initialStreamNames = new LinkedList<Object>();
					initialStreamNames.add(obj);
				}
			}
		}
		
		super.init(site, input);
		
		getEditorSite().getActionBars().getStatusLineManager().setMessage("editor created");
	}
	
	public void addChart(LinkedList<?> streamNames) throws PartInitException
	{
		String title = PAGE_TITLE_FOR_MULTIPLE_STREAMS;
		
		if(streamNames.size() > 0) {
			if(streamNames.size() == 1) {
				title = streamNames.getFirst().toString();
			}
			
			int no = addPage(new DatastreamChartPage(this, "Chart." +chartCounter, title, streamNames));
			setActivePage(no);
			chartCounter++;
		}
	}

	@Override
	protected void addPages()
	{
		try {
			addPage(new DatastreamSelectionPage(this, SELECTION_TITLE, SELECTION_TITLE));
			
			if(initialStreamNames != null) {
				addChart(initialStreamNames);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void createHeaderContents(IManagedForm headerForm)
	{
		ScrolledForm form = headerForm.getForm();
		
		form.setText(FORM_TITLE);

		headerForm.getToolkit().decorateFormHeading(form.getForm());
		contributeToToolbar(form.getToolBarManager());
	}

	@Override
	public void doSave(IProgressMonitor monitor)
	{
	}

	@Override
	public void doSaveAs()
	{
	}

	@Override
	public boolean isSaveAsAllowed()
	{
		return false;
	}

	public void contributeToToolbar(IToolBarManager manager)
	{
		manager.add(new SaveCSVAction());
		manager.add(new SavePNGAction());
		manager.add(new CloseAction());

		manager.update(true);
	}

	private class CloseAction extends Action
	{
		public CloseAction()
		{
			setText("Close");
			
			setImageDescriptor(Activator.getImageDescriptor("icons/terminate_co.gif"));
			setDisabledImageDescriptor(Activator.getImageDescriptor("icons/resume_co_disabled.gif"));
		}

		@Override
		public void run()
		{
			int activePage = getActivePage();
			
			// -1 : no page at all
			//  0 : selection page
			// >0 : chart pages
			if(activePage > 0) {
				removePage(activePage);
			}
		}

	}
	
	private class SaveCSVAction extends Action
	{
		public SaveCSVAction()
		{
			setText("Save as CSV");
//			setImageDescriptor(Activator.getImageDescriptor("icons/save_co.gif"));
		}

		@Override
		public void run()
		{
			int activePage = getActivePage();
			
			// -1 : no page at all
			//  0 : selection page
			// >0 : chart pages
			if(activePage > 0) {
				Object page = pages.get(activePage);
				if(page instanceof DatastreamChartPage) {
					FileDialog fileDialog = new FileDialog(getSite().getShell(), SWT.SAVE);
					String filename = fileDialog.open();
					
					if(filename != null) {
						// TODO remove extension from filename or invent a new naming scheme
						//      in order to detect overwriting of files
						
						try {
							((DatastreamChartPage) page).saveAsCSV(filename, null);
						}
						catch (IOException exc) {
							Activator.err(this, "Can not save chart to file " +filename, exc);
						}
					}
					// else: user canceled operation
				}
			}
		}

	}
	
	private class SavePNGAction extends Action
	{
		public SavePNGAction()
		{
			setText("Save as PNG");
//			setImageDescriptor(Activator.getImageDescriptor("icons/save_co.gif"));
		}

		@Override
		public void run()
		{
			int activePage = getActivePage();
			
			// -1 : no page at all
			//  0 : selection page
			// >0 : chart pages
			if(activePage > 0) {
				Object page = pages.get(activePage);
				if(page instanceof DatastreamChartPage) {
					FileDialog fileDialog = new FileDialog(getSite().getShell(), SWT.SAVE);
					fileDialog.setOverwrite(true);
					String filename = fileDialog.open();
					
					if(filename != null) {
						try {
							((DatastreamChartPage) page).saveAsPNG(filename);
						}
						catch (IOException exc) {
							Activator.err(this, "Can not save chart to PNG file " +filename, exc);
						}
					}
					// else: user canceled operation
				}
			}
		}

	}
	

}
