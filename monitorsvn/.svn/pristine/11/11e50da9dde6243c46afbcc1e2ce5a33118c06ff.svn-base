package net.sourceforge.svnmonitor.base.osgi;

import org.eclipse.core.runtime.Assert;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * find osgi services using whiteboard pattern
 */
public abstract class BaseServiceTrackerWrapper {

	private final BundleContext bundleContext;
	private ServiceTracker tracker;
	private final Class<?> serviceReference;
	private final ServiceTrackerCustomizer customizer;

	private ServiceListener serviceListener;

	/**
	 * @param bundleContext
	 * @param serviceReference
	 */
	public BaseServiceTrackerWrapper(BundleContext bundleContext, Class<?> serviceReference) {
		this(bundleContext, serviceReference, null);
	}

	/**
	 * @param bundleContext
	 * @param serviceReference
	 * @param customizer
	 */
	public BaseServiceTrackerWrapper(BundleContext bundleContext, Class<?> serviceReference, ServiceTrackerCustomizer customizer) {
		super();
		Assert.isNotNull(bundleContext);
		Assert.isNotNull(serviceReference);
		this.bundleContext = bundleContext;
		this.serviceReference = serviceReference;
		this.customizer = customizer;
		init();
	}

	private void init() {
		tracker = new ServiceTracker(bundleContext, serviceReference.getName(), customizer);
		tracker.open();

		serviceListener = new ServiceListener() {

			public void serviceChanged(ServiceEvent ev) {
				ServiceReference sr = ev.getServiceReference();
				switch (ev.getType()) {
				case ServiceEvent.REGISTERED:
					serviceRegistered(sr);
					break;
				case ServiceEvent.UNREGISTERING:
					serviceUnregistered(sr);
					break;
				}
			}
		};

		String filter = "(objectclass=" + serviceReference.getName() + ")";
		try {
			bundleContext.addServiceListener(serviceListener, filter);

		}
		catch (InvalidSyntaxException e) {
			throw new RuntimeException("init failed", e);
		}
	}

	/**
	 * send REGISTERED events for all already registered (before listener registration) services to the wrapper service
	 */
	public void publishRegisteredServices() {
		String filter = "(objectclass=" + serviceReference.getName() + ")";
		try {
			ServiceReference[] srl = bundleContext.getServiceReferences(null, filter);
			for (int i = 0; srl != null && i < srl.length; i++) {
				serviceListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, srl[i]));
			}

		}
		catch (InvalidSyntaxException e) {
			throw new RuntimeException("sendServiceEvents failed", e);
		}
	}

	/**
	 * stop the wrapper
	 */
	public void close() {
		bundleContext.removeServiceListener(serviceListener);
		tracker.close();
		tracker = null;
		serviceListener = null;
	}

	/**
	 * @return the bundleContext
	 */
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * @return the tracker
	 */
	public ServiceTracker getTracker() {
		return tracker;
	}

	/**
	 * @return the serviceReference
	 */
	public Class<?> getServiceReference() {
		return serviceReference;
	}

	/**
	 * @return the customizer
	 */
	public ServiceTrackerCustomizer getCustomizer() {
		return customizer;
	}

	/**
	 * @param sr
	 */
	public abstract void serviceUnregistered(ServiceReference sr);

	/**
	 * @param sr
	 */
	public abstract void serviceRegistered(ServiceReference sr);

}