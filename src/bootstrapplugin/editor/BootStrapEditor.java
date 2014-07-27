package bootstrapplugin.editor;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.browser.BrowserViewer;
import org.eclipse.ui.internal.browser.WebBrowserEditor;
import org.eclipse.ui.internal.browser.WebBrowserEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.openon.ExternalFileEditorInput;






import bootstrapplugin.bootStrapPlugin;
import bootstrapplugin.bean.EditorBean;
import bootstrapplugin.exception.BootStrapException;

public class BootStrapEditor extends MultiPageEditorPart {
	private StructuredTextEditor editor=new StructuredTextEditor();	
	private WebBrowserEditor    webEditor=new WebBrowserEditor();
	private SourceViewWebBrowserEditor   sourceViewer=new SourceViewWebBrowserEditor();
	private int layOutPage,sourceViwerPage,sourcePage;
	private static final String LAYOUT_PAGE="layOut";
	private static final String SHOW_FOR_SOURCE="SourceViewer";
	private static final String SOURCE_PAGE="Source";
	@Override
	protected void createPages() {
		try {		
		layOutPage=addPage(webEditor,getEditorInput());
		sourceViwerPage=addPage(sourceViewer,this.getSourceInput());
		sourcePage=addPage(editor,this.getSourceInput());
			setPageText(layOutPage,LAYOUT_PAGE);
			setPageText(sourceViwerPage,SHOW_FOR_SOURCE);
			setPageText(sourcePage,SOURCE_PAGE);
		} catch (PartInitException e) {
			BootStrapException exception=new BootStrapException(e);
			exception.popErrorDailog(exception);
		}
		
	}
	/**
	 * get the inputstream of source page
	 */
	private IEditorInput getSourceInput(){
	   return	new FileEditorInput(EditorBean.htmlFile);
	}
	
	@Override
	public void doSave(IProgressMonitor arg) {
		editor.doSave(arg);
		sourceViewer.refresh();
	}
	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		editor.doSaveAs();
		
	}
	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
	class SourceViewWebBrowserEditor extends WebBrowserEditor{
		public void refresh(){
			this.webBrowser.refresh();
		}
		
	}
}