package bootstrapplugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.http.jetty.JettyConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.internal.serviceregistry.ServiceRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;

import servlets.AbstractServer;

/**
 * The activator class controls the plug-in life cycle
 */
public class bootStrapPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "BootStrapPlugin"; //$NON-NLS-1$

	// The shared instance
	private static bootStrapPlugin plugin;

	private HttpService service;

	public static String getTempletPath(String name) throws Exception {
		return FileLocator
				.toFileURL(getDefault().getBundle().getResource(name))
				.getPath();
	}

	public static IWorkspaceRoot getRoot() {
		return new Workspace().getRoot();
	}

	public static Image getICONS(Composite container) throws IOException {
		InputStream image = getDefault().getBundle()
				.getEntry("icons//description.jpg").openStream();
		return new Image(container.getDisplay(), image);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		this.startServer(80, "127.0.0.1", "bootStrap", PLUGIN_ID);
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static bootStrapPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static IWorkbenchPage getActivePage() {
		return getDefault().internalGetActivePage();
	}

	private IWorkbenchPage internalGetActivePage() {
		IWorkbenchWindow window = getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return window.getActivePage();
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	/*
	 * 
	 * start the bootStrapServer
	 */
	private void startServer(int port, String host, String webappName,
			String pluginID) throws Exception {
		Dictionary dict = new Hashtable();

		// configure the port
		dict.put("http.port", port); //$NON-NLS-1$

		// configure the host
		dict.put("http.host", host); //$NON-NLS-1$

		// set the base URL
		dict.put("context.path", "/" + webappName); //$NON-NLS-1$ //$NON-NLS-2$

		dict.put("other.info", pluginID); //$NON-NLS-1$

		// Startup Jetty web server
		JettyConfigurator.startServer(webappName, dict);
		Bundle bundle = Platform
				.getBundle("org.eclipse.equinox.http.registry");
		if (bundle != null) {
			if (bundle.getState() == Bundle.RESOLVED) {
				bundle.start(Bundle.START_TRANSIENT);
			}
		}
	}
}
