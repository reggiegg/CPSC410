package net.sourceforge.svnmonitor.model;

import net.sourceforge.svnmonitor.model.impl.RepoModel;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class ModelActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "net.sourceforge.svnmonitor.model";

	// The shared instance
	private static ModelActivator plugin;

	private IRepoModel model;
	private ServiceRegistration modelReg;

	/**
	 * The constructor
	 */
	public ModelActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("ModelActivator.start()");
		model = new RepoModel();
		context.registerService(IRepoModel.class.getName(), model, null);
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		if (modelReg != null) {
			modelReg.unregister();
		}
		model = null;
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static ModelActivator getDefault() {
		return plugin;
	}

}
