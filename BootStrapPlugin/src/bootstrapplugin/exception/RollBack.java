package bootstrapplugin.exception;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import bootstrapplugin.bootStrapPlugin;

public class RollBack {
public static void rollBack(String projectName){
	IWorkspaceRoot root=ResourcesPlugin.getWorkspace()
			.getRoot();
	try {
		root.getProject(projectName).delete(true, true, null);
	} catch (CoreException e) {
		e.printStackTrace();
	}
}
}
