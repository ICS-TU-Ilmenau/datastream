package de.tuilmenau.ics.datastream.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPartSite;

import de.tuilmenau.ics.datastream.eclipse.Activator;


/**
 * Class for loading chart from (multiple) CVS files.
 */
public class LoadChartListener implements Listener
{
	public LoadChartListener(IWorkbenchPartSite workbenchPart)
	{
		this.shell = workbenchPart.getShell();
	}
	
	@Override
	public void handleEvent(Event event)
	{
		FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
		
		if(fileDialog.open() != null) {
			String[] filenames = fileDialog.getFileNames();
			for(String filename : filenames) {
				Activator.log(this, "Opening: " +filename);
			}
			
			// TODO implement stuff
		}
	}
	
	private Shell shell;
}
