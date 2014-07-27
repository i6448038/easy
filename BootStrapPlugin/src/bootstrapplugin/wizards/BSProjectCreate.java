package bootstrapplugin.wizards;


import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.core.internal.registry.ConfigurationElementHandle;
import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import org.eclipse.jface.resource.ImageDescriptor;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;

import java.io.*;

import org.eclipse.ui.*;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IIdentifier;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.osgi.framework.Bundle;

import bootstrapplugin.bootStrapPlugin;
import bootstrapplugin.bean.EditorBean;
import bootstrapplugin.bean.ProjectBean;
import bootstrapplugin.exception.BootStrapException;
import bootstrapplugin.exception.RollBack;
import bootstrapplugin.perspectives.BootStrapPerspective;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "mpe". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class BSProjectCreate extends Wizard implements INewWizard {
	private BootStrapWizardPage page;
	private ISelection selection;
	private ProjectBean projectBean;
	private EditorBean editorBean=new EditorBean();
	private final static String BOOTSTRAP_PERSPECTIVE_ID="bootstrapplugin.perspectives.BootStrapPerspective";
	private final static String PAGE_NAME="BootStrap";
	private static final String WINDOW_TITLE="BOOTSTRAP";
	private static final String TEMPLET_NAME="WebLayout";
	private static final String FILE_TEMPLET_NAME="templet";
	/**
	 * Constructor for SampleNewWizard.
	 */
	public BSProjectCreate() {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(WINDOW_TITLE);
		setDefaultPageImageDescriptor(ImageDescriptor.createFromURL(bootStrapPlugin.
				getDefault().getBundle().getEntry("icons\\descriptionTitle.jpg")));
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		this.projectBean=new ProjectBean();
		page = new BootStrapWizardPage(PAGE_NAME,this.projectBean);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish()  {		
	try{	
		
		this.createProjectTemplet(projectBean);
		this.accessBootStrapPerpective();
		return true;
	}catch(Exception e){
		BootStrapException bootstrap=new BootStrapException(e);
		bootstrap.popErrorDailog(bootstrap);
		RollBack.rollBack(projectBean.getProjectName());
		return true;
	}
	  
	}
	/**
	 * create a project templet.
	 * @throws Exception 
	 */
	private void createProjectTemplet(ProjectBean projectBean) throws Exception{
		IWorkspaceRoot root=ResourcesPlugin.getWorkspace().getRoot();
		 IProject project=root.getProject(projectBean.getProjectName());
			project.create(null);
			project.open(null);
			String templetPath=bootStrapPlugin.getTempletPath(TEMPLET_NAME);
			File templetBootStrap=new File(templetPath);
		 IFolder bootstrapFolder=project.getFolder("bootstrapWebLayout");
		 bootstrapFolder.create(true, true, null);
		 bootstrapFolder.setHidden(true);
		 this.createBasicalFiles(templetBootStrap, bootstrapFolder);
		 
		 
		 IFile tFile=project.getFile(projectBean.getWebFileName()+".html");
		 editorBean.setHtmlFile(tFile);
		 InputStream inputstream=new FileInputStream(new File
				 (bootStrapPlugin.getTempletPath("templet.html")));
		 tFile.create(inputstream, true, null);
	}
	/**
	 * create the files from the WebLayout folder.
	 */
	private void createBasicalFiles(File templetFile ,IFolder createdFolder) 
			throws FileNotFoundException, CoreException{
		File[] templetFileList=templetFile.listFiles();
		for(File f:templetFileList){
			if(f.isDirectory()){
			IFolder subFolder=createdFolder.getFolder(f.getName());
			subFolder.create(true, true, null);
			createBasicalFiles(f,subFolder);
			}
			else{
				if(this.isLastOne(f.getName())){
					IFile file=createdFolder.getFile("index.html");
					InputStream source=new FileInputStream(f);
					file.create(source, true, null);
					this.openResource(file);
				}else{
					IFile file=createdFolder.getFile(f.getName());
		         InputStream source=new FileInputStream(f);
					file.create(source, true, null);
					file.setHidden(true);
				}
			}
		}
	
	}
	/**
	 * judge whether the file is the last one or not.
	 * @return true  the file is the last one.
	 *         false the file is not the last one.
	 */
	private boolean isLastOne(String fileName){
		if(fileName.equals("index.html")){
			return true;
		}
		else{
			return false;
		}
	}
	//open the index.html
	protected void openResource(final IFile resource) {
		final IWorkbenchPage activePage= bootStrapPlugin.getActivePage();
		if (activePage != null) {
			final Display display= getShell().getDisplay();
			if (display != null) {
				display.asyncExec(new Runnable() {
					public void run() {
						try {
							//IDE.openEditor(activePage, resource, true);
							IDE.openEditor(activePage, resource, "BootStrapPlugin.htmlEditor");
						} catch (PartInitException e) {
							//bootStrapPlugin.log(e);
							System.out.println("+++++");
						}
					}
				});
			}
		}
	}
	
	
	private void accessBootStrapPerpective(){
		IPerspectiveRegistry reg = PlatformUI.getWorkbench()
				.getPerspectiveRegistry();
		IPerspectiveDescriptor finalPersp = reg
				.findPerspectiveWithId(BOOTSTRAP_PERSPECTIVE_ID);
			int workbenchPerspectiveSetting = WorkbenchPlugin.getDefault()
					.getPreferenceStore().getInt(
							IPreferenceConstants.OPEN_PERSP_MODE);
			if (workbenchPerspectiveSetting == IPreferenceConstants.OPM_NEW_WINDOW) {
				openInNewWindow(finalPersp);
				return;
			}
			replaceCurrentPerspective(finalPersp);
	}
	
	private void openInNewWindow(IPerspectiveDescriptor desc){
		try {
			PlatformUI.getWorkbench().openWorkbenchWindow(desc.getId(),
					ResourcesPlugin.getWorkspace().getRoot());
		} catch (WorkbenchException e) {
			IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			if (window != null) {
				ErrorDialog.openError(window.getShell(), "WINDOW_PROBLEMS_TITLE",
						e.getMessage(), e.getStatus());
			}
		}
	}
	
	private void replaceCurrentPerspective(IPerspectiveDescriptor persp){
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window == null) {
			return;
		}
		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return;
		}
		page.setPerspective(persp);
	}
	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}