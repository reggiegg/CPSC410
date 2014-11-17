package net.sourceforge.svnmonitor.model;

import net.sourceforge.svnmonitor.base.osgi.BaseServiceTrackerWrapper;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class RepoModelTrackerWrapper extends BaseServiceTrackerWrapper {
	private IRepoModel model;

	public RepoModelTrackerWrapper(BundleContext bundleContext) {
		super(bundleContext, IRepoModel.class);
	}

	public synchronized IRepoModel getModel() {
		return model;
	}

	@Override
	public synchronized void serviceRegistered(ServiceReference sr) {
		IRepoModel newModel = (IRepoModel) getBundleContext().getService(sr);
		this.model = newModel;
	}

	@Override
	public synchronized void serviceUnregistered(ServiceReference sr) {
		IRepoModel newModel = (IRepoModel) getBundleContext().getService(sr);
		if (this.model == newModel) {
			this.model = null;
		}
	}
}
