package net.sourceforge.svnmonitor.repoview.views;

import net.sourceforge.svnmonitor.model.IRepository;
import net.sourceforge.svnmonitor.model.IRepositoryItem;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class RepoViewLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if (element instanceof IRepositoryItem) {
			IRepositoryItem item = (IRepositoryItem) element;
			return item.getName();
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IRepository) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		} else {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		}
	}
}
