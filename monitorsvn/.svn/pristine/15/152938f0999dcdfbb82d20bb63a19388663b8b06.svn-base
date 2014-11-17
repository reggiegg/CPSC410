package net.sourceforge.svnmonitor.repoview.views;

import net.sourceforge.svnmonitor.model.IRepoModel;
import net.sourceforge.svnmonitor.model.IRepositoryItem;
import net.sourceforge.svnmonitor.model.RepoModelTrackerWrapper;
import net.sourceforge.svnmonitor.repoview.RepoViewActivator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class RepoViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	private final RepoModelTrackerWrapper tracker = new RepoModelTrackerWrapper(RepoViewActivator.getDefault().getBundle().getBundleContext());

	public RepoViewContentProvider() {
		tracker.publishRegisteredServices();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		IRepoModel model = tracker.getModel();
		if (model != null) {
			return model.getItems().toArray();
		} else {
			return new Object[0];
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IRepositoryItem) {
			IRepositoryItem item = (IRepositoryItem) parentElement;
			return item.getChildren().toArray();
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IRepositoryItem) {
			IRepositoryItem item = (IRepositoryItem) element;
			return item.getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IRepositoryItem) {
			IRepositoryItem item = (IRepositoryItem) element;
			return item.getChildren().size() > 0;
		}
		return false;
	}

}
