package bootstrapplugin.exception;



import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import bootstrapplugin.bootStrapPlugin;

public class BootStrapException extends Exception {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERROR_DIALOG_TITLE="an error happens!";
	private static final String  ERROR_MESSAGE="bootStrap error";
	
	public BootStrapException(String message){
		super(message);
	}
	public BootStrapException(Throwable cause){
		super(cause);
	}
	
	
	public static void popErrorDailog(Exception e){
	ErrorDialog errorDialog=new ErrorDialog(PlatformUI.getWorkbench().getDisplay()
			.getActiveShell(),ERROR_DIALOG_TITLE,ERROR_MESSAGE,new Status(IStatus.ERROR,
					bootStrapPlugin.PLUGIN_ID,e.getMessage()),IStatus.ERROR);
	      errorDialog.open();
	}
	
}


	


