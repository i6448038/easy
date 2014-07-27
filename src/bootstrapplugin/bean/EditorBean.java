package bootstrapplugin.bean;

import org.eclipse.core.resources.IFile;



public class EditorBean {
	
	/**
	 *the IPath of the file which needs to be editored.
	 */
   public static IFile htmlFile;

public IFile getHtmlFile() {
	return htmlFile;
}

public void setHtmlFile(IFile htmlFile) {
	EditorBean.htmlFile= htmlFile;
}
}
